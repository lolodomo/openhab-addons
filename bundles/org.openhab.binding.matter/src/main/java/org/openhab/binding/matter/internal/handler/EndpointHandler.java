/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.matter.internal.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.AttributeListener;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.model.Endpoint;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.BasicInformationCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.BridgedDeviceBasicInformationCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DescriptorCluster;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.config.EndpointConfiguration;
import org.openhab.binding.matter.internal.mapper.DeviceType;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.config.core.validation.ConfigValidationException;
import org.openhab.core.thing.*;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.BridgeHandler;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.DynamicStateDescriptionProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.State;
import org.openhab.core.types.StateDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EndpointHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
public class EndpointHandler extends BaseThingHandler implements AttributeListener, DynamicStateDescriptionProvider {

    private final Logger logger = LoggerFactory.getLogger(EndpointHandler.class);
    private BigInteger nodeId = BigInteger.valueOf(0);
    protected int endpointId;
    private List<Integer> deviceTypes = Collections.emptyList();
    private Map<String, DeviceType> channelIdMap = new HashMap<String, DeviceType>();
    // private Map<Integer, DeviceTypeMapper> clusterIdMap = new HashMap<Integer, DeviceTypeMapper>();
    private Map<Integer, DeviceType> deviceTypeMap = new HashMap<Integer, DeviceType>();

    private @Nullable MatterWebsocketClient cachedClient;

    public EndpointHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        MatterWebsocketClient client = getClient();
        if (client == null) {
            logger.debug("Matter client not present, ignoring command");
            return;
        }

        if (command instanceof RefreshType) {
            ControllerHandler clusterHandler = controllerHandler();
            if (clusterHandler != null) {
                clusterHandler.updateNode(nodeId);
            }
            return;
        }

        logger.debug("Looking up converter for {}", channelUID);
        DeviceType converter = channelIdMap.get(channelUID.getId());
        if (converter != null) {
            logger.debug("Found converter for {} : {} ", channelUID, converter);
            converter.handleCommand(channelUID, command);
        } else {
            logger.debug("No converter found for {}", channelUID);
        }
    }

    @Override
    public void initialize() {
        EndpointConfiguration config = getConfigAs(EndpointConfiguration.class);
        nodeId = new BigInteger(config.nodeId);
        endpointId = config.endpointId;
        String deviceTypesProp = getThing().getProperties().get("deviceTypes");
        if (deviceTypesProp != null) {
            deviceTypes = Arrays.stream(deviceTypesProp.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        }
        logger.debug("initialize endpoint {}", endpointId);
        ControllerHandler handler = controllerHandler();
        if (handler == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_UNINITIALIZED);
        } else if (handler.getThing().getStatus() != ThingStatus.ONLINE) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        } else {
            updateStatus(ThingStatus.UNKNOWN);
        }
    }

    @Override
    public void handleConfigurationUpdate(Map<String, Object> configurationParameters)
            throws ConfigValidationException {
        logger.debug("handleConfigurationUpdate");
        validateConfigurationParameters(configurationParameters);
        Configuration configuration = editConfiguration();
        boolean reinitialize = false;
        boolean commission = false;
        for (Entry<String, Object> configurationParameter : configurationParameters.entrySet()) {
            Object value = configurationParameter.getValue();
            logger.debug("{}: old: {} {} new: {} {}", configurationParameter.getKey(),
                    configuration.get(configurationParameter.getKey()),
                    configuration.get(configurationParameter.getKey()).getClass().getName(),
                    configurationParameter.getValue(), configurationParameter.getValue().getClass().getName());
            // Ignore any configuration parameters that have not changed
            if (areEqual(configurationParameter.getValue(), configuration.get(configurationParameter.getKey()))) {
                logger.debug("Endpoint Configuration update ignored {} to {} ({})", configurationParameter.getKey(),
                        value, value == null ? "null" : value.getClass().getSimpleName());
                continue;
            }
            logger.debug("Endpoint Configuration update {} to {}", configurationParameter.getKey(), value);
            switch (configurationParameter.getKey()) {
                case "nodeId":
                case "endpointId":
                    reinitialize = true;
                    break;
                case "commissionMode":
                    if (value instanceof Boolean mode) {
                        commission = mode;
                        value = false;
                    }
            }
            configuration.put(configurationParameter.getKey(), value);
        }
        updateConfiguration(configuration);
        if (reinitialize) {
            dispose();
            initialize();
        } else if (commission) {
            MatterWebsocketClient client = getClient();
            if (client != null) {
                client.enhancedCommissioningWindow(nodeId).thenAccept(pairingCodes -> {
                    getThing().setProperty("externalPairCode", pairingCodes.manualPairingCode);
                }).exceptionally(e -> {
                    logger.debug("Error communicating with controller", e);
                    return null;
                });
            }
        }
    }

    @Override
    public void bridgeStatusChanged(ThingStatusInfo bridgeStatusInfo) {
        if (bridgeStatusInfo.getStatus() != ThingStatus.ONLINE) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        }
    }

    // making this public
    @Override
    public void updateThing(Thing thing) {
        super.updateThing(thing);
    }

    // making this public
    @Override
    public void updateState(String channelID, State state) {
        super.updateState(channelID, state);
    }

    // making this public
    @Override
    public ThingBuilder editThing() {
        return super.editThing();
    }

    @Override
    public @Nullable StateDescription getStateDescription(Channel channel,
            @Nullable StateDescription originalStateDescription, @Nullable Locale locale) {
        for (DeviceType dt : deviceTypeMap.values()) {
            StateDescription sd = dt.getStateDescription(channel.getUID());
            if (sd != null) {
                return sd;
            }
        }
        return null;
    }

    public int getEndpointId() {
        return endpointId;
    }

    public List<Integer> getDeviceTypes() {
        return deviceTypes;
    }

    @SuppressWarnings({ "null", "unused" })
    public void updateEndpoint(Endpoint endpoint) {
        logger.debug("updateEndpoint {} {}", endpoint.number, nodeId);
        if (getThing().getStatus() != ThingStatus.ONLINE) {
            logger.debug("Setting Online {} {}", endpoint.number, nodeId);
            updateStatus(ThingStatus.ONLINE);
        }
        Map<String, BaseCluster> clusters = endpoint.clusters;

        Object basicInfoObject = clusters.get(BasicInformationCluster.CLUSTER_NAME);
        if (basicInfoObject != null) {
            BasicInformationCluster basicInfo = (BasicInformationCluster) basicInfoObject;
            String label = basicInfo.nodeLabel != null ? basicInfo.nodeLabel : basicInfo.productLabel;
            updateProperty("label", label);

        } else {
            basicInfoObject = clusters.get(BridgedDeviceBasicInformationCluster.CLUSTER_NAME);
            if (basicInfoObject != null) {
                BridgedDeviceBasicInformationCluster basicInfo = (BridgedDeviceBasicInformationCluster) basicInfoObject;
                String label = basicInfo.nodeLabel != null ? basicInfo.nodeLabel : basicInfo.productLabel;
                updateProperty("label", label);

            }
        }

        DescriptorCluster descriptorCluster = (DescriptorCluster) clusters.get(DescriptorCluster.CLUSTER_NAME);

        descriptorCluster.deviceTypeList.forEach(dt -> {
            DeviceType mapper = deviceTypeMap.get(dt.deviceType);
            if (mapper == null) {
                Class<? extends DeviceType> clazz = DeviceType.getDeviceMapper(dt.deviceType);
                if (clazz != null) {
                    try {
                        Class<?>[] constructorParameterTypes = new Class<?>[] { EndpointHandler.class, Integer.class };
                        Constructor<? extends DeviceType> constructor = clazz.getConstructor(constructorParameterTypes);
                        final DeviceType mapperInternal = constructor.newInstance(this, dt.deviceType);
                        deviceTypeMap.put(dt.deviceType, mapperInternal);

                        mapperInternal.createChannels(clusters).forEach(channel -> {
                            logger.debug("Added channel {}", channel.getId());
                            channelIdMap.put(channel.getId(), mapperInternal);
                        });
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        logger.debug("Could not create device type mapper", e);
                    }
                } else {
                    logger.debug("No mapper found for device type {}", dt.deviceType);
                }
            }
        });

        clusters.forEach((clusterName, cluster) -> {
            deviceTypeMap.forEach((id, dm) -> dm.updateCluster(cluster));
        });
    }
    // clusters.forEach((clusterName, cluster) -> {
    // logger.trace("checking cluster {} for handler", clusterName);
    // // TODO this is a hack to ignore the dimmer cluster that switches advertise as a convienence, need to
    // // revisit this.
    // if (cluster.id == LevelControlCluster.CLUSTER_ID && isSwitchType) {
    // return;
    // }
    // Integer id = cluster.id;
    // DeviceTypeMapper clusterConverter = clusterIdMap.get(id);
    // if (clusterConverter == null) {
    // Class<? extends DeviceTypeMapper> clazz = DeviceTypeMapper.getConverterClass(id);
    // logger.trace("Creating handler {}", clazz);
    // if (clazz != null) {
    // try {
    // Class<?>[] constructorParameterTypes = new Class<?>[] { EndpointHandler.class };
    // Constructor<? extends DeviceTypeMapper> constructor = clazz
    // .getConstructor(constructorParameterTypes);
    // final DeviceTypeMapper converter = constructor.newInstance(this);
    // for (Integer i : converter.supportedClusters()) {
    // clusterIdMap.put(i, converter);
    // }
    // // now we need to create channels and add those to the channel map
    // converter.createChannels(cluster).forEach(channel -> {
    // logger.trace("Added channel {}", channel.getId());
    // channelIdMap.put(channel.getId(), converter);
    // });
    // clusterConverter = converter;
    // } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
    // | InvocationTargetException | NoSuchMethodException | SecurityException e) {
    // logger.debug("Could not create cluster handler", e);
    // }
    // }
    // }
    // if (clusterConverter == null) {
    // logger.trace("No handler found for cluster {}", clusterName);
    // return;
    // }
    // clusterConverter.updateCluster(cluster);
    // });
    // }

    @Override
    public void handleRemoval() {
        ControllerHandler bridge = controllerHandler();
        if (bridge != null) {
            bridge.endpointRemoved(nodeId, endpointId, true);
        }
        updateStatus(ThingStatus.REMOVED);
    }

    @Override
    public void onEvent(AttributeChangedMessage message) {
        deviceTypeMap.forEach((id, dm) -> {
            if (dm.supportedClusters().contains(message.path.clusterId)) {
                dm.onEvent(message);
            }
        });
    }

    public BigInteger getNodeId() {
        return nodeId;
    }

    public void setEndpointStatus(ThingStatus status, ThingStatusDetail detail, String description) {
        logger.debug("setEndpointStatus {} {} {} {} {}", status, detail, description, endpointId, nodeId);
        updateStatus(status, detail, description);
    }

    protected @Nullable ControllerHandler controllerHandler() {
        Bridge bridge = getBridge();
        if (bridge != null) {
            BridgeHandler handler = bridge.getHandler();
            if (handler instanceof ControllerHandler controllerHandler) {
                return controllerHandler;
            }
        }
        return null;
    }

    public @Nullable MatterWebsocketClient getClient() {
        if (cachedClient == null) {
            ControllerHandler c = controllerHandler();
            if (c != null) {
                cachedClient = c.getClient();
            }
        }
        return cachedClient;
    }

    public static boolean areEqual(Object obj1, Object obj2) {
        if (obj1 == null || obj2 == null) {
            return Objects.equals(obj1, obj2); // Return true if both are null, false if one is null
        }

        if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
            return ((BigDecimal) obj1).compareTo((BigDecimal) obj2) == 0;
        }

        if (obj1 instanceof Number && obj2 instanceof Number) {
            return BigDecimal.valueOf(((Number) obj1).doubleValue())
                    .compareTo(BigDecimal.valueOf(((Number) obj2).doubleValue())) == 0;
        }

        return obj1.equals(obj2); // Fallback to the default equals method
    }
}
