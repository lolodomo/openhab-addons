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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.AttributeListener;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.model.Endpoint;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.BasicInformationCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.BridgedDeviceBasicInformationCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.LevelControlCluster;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.config.EndpointConfiguration;
import org.openhab.binding.matter.internal.converter.ClusterConverter;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingStatusInfo;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.BridgeHandler;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link EndpointHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
public class EndpointHandler extends BaseThingHandler implements AttributeListener {

    private final Logger logger = LoggerFactory.getLogger(EndpointHandler.class);
    private BigInteger nodeId = BigInteger.valueOf(0);
    protected int endpointId;
    private List<Integer> deviceTypes = Collections.emptyList();
    private Map<String, ClusterConverter> channelIdMap = new HashMap<String, ClusterConverter>();
    private Map<Integer, ClusterConverter> clusterIdMap = new HashMap<Integer, ClusterConverter>();
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
        logger.debug("Looking up converter for {}", channelUID);
        ClusterConverter converter = channelIdMap.get(channelUID.getId());
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
        boolean hasLevelControl = clusters.containsKey(LevelControlCluster.CLUSTER_NAME);

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
        clusters.forEach((clusterName, cluster) -> {
            logger.debug("checking cluster {} for handler", clusterName);
            Integer id = cluster.id;
            ClusterConverter clusterConverter = clusterIdMap.get(id);
            if (clusterConverter == null) {
                // lookup handler
                // c = new handler
                Class<? extends ClusterConverter> clazz = ClusterConverter.getConverterClass(id);
                logger.debug("Creating handler {}", clazz);
                if (clazz != null) {
                    try {
                        Class<?>[] constructorParameterTypes = new Class<?>[] { EndpointHandler.class };
                        Constructor<? extends ClusterConverter> constructor = clazz
                                .getConstructor(constructorParameterTypes);
                        final ClusterConverter converter = constructor.newInstance(this);
                        for (Integer i : converter.supportedClusters()) {
                            clusterIdMap.put(i, converter);
                        }
                        // now we need to create channels and add those to the channel map
                        converter.createChannels(cluster).forEach(channel -> {
                            logger.debug("Added channel {}", channel.getId());
                            channelIdMap.put(channel.getId(), converter);
                        });
                        clusterConverter = converter;
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        logger.debug("Could not create cluster handler", e);
                    }
                }
            }
            if (clusterConverter == null) {
                logger.debug("No handler found for cluster {}", clusterName);
                return;
            }
            clusterConverter.updateCluster(cluster);
        });
    }

    @Override
    public void handleRemoval() {
        super.handleRemoval();
        ControllerHandler bridge = controllerHandler();
        if (bridge != null) {
            bridge.endpointRemoved(nodeId, endpointId);
        }
    }

    @Override
    public void onEvent(AttributeChangedMessage message) {
        ClusterConverter c = clusterIdMap.get(message.path.clusterId);
        if (c == null) {
            logger.debug("No cluster found for id {}", message.path.clusterId);
            return;
        }
        c.onEvent(message);
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
}
