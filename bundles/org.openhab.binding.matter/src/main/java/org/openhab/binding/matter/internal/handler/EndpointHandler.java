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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.AttributeListener;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.model.Endpoint;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.BasicInformationCluster;
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
    private String nodeId = "";
    protected int endpointId;
    private List<BaseCluster> clusters = Collections.synchronizedList(new LinkedList<BaseCluster>());
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
        String[] parts = config.id.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Endpoints should have an ID in the format 'nodeID:endpointNum'");
        }
        nodeId = parts[0];
        endpointId = Integer.parseInt(parts[1]);
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

    @Override
    public void dispose() {
    }

    @Override
    public void updateThing(Thing thing) {
        super.updateThing(thing);
    }

    @Override
    public void updateState(String channelID, State state) {
        super.updateState(channelID, state);
    }

    @Override
    public ThingBuilder editThing() {
        return super.editThing();
    }

    public int getEndpointId() {
        return endpointId;
    }

    @SuppressWarnings({ "null", "unused" })
    public void updateEndpoint(Endpoint endpoint) {
        logger.debug("updateEndpoint {} {}", endpoint.number, nodeId);
        if (getThing().getStatus() != ThingStatus.ONLINE) {
            updateStatus(ThingStatus.ONLINE);
        }
        Map<String, BaseCluster> clusters = endpoint.clusters;
        boolean hasLevelControl = clusters.containsKey(LevelControlCluster.CLUSTER_NAME);

        Object basicInfoObject = clusters.get(BasicInformationCluster.CLUSTER_NAME);
        if (basicInfoObject == null) {
            basicInfoObject = clusters.get("BridgedDeviceBasicInformation");
        }
        if (basicInfoObject != null) {
            BasicInformationCluster basicInfo = (BasicInformationCluster) basicInfoObject;
            String label = basicInfo.nodeLabel != null ? basicInfo.nodeLabel : basicInfo.productLabel;
            updateProperty("label", label);
        }
        endpoint.clusters.forEach((clusterName, cluster) -> {
            logger.debug("checking cluster {} for handler", clusterName);
            Integer id = cluster.id;
            // hack to support a single handler when Level and OnOff
            // if (hasLevelControl && OnOffCluster.CLUSTER_NAME.equals(clusterName)) {
            // id = LevelControlCluster.CLUSTER_ID;
            // }
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
    public void onEvent(AttributeChangedMessage message) {
        ClusterConverter c = clusterIdMap.get(message.path.clusterId);
        if (c == null) {
            logger.debug("No cluster found for id {}", message.path.clusterId);
            return;
        }
        c.onEvent(message);
    }

    public void refresh() {
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setEndpointStatus(ThingStatus status, ThingStatusDetail detail) {
        updateStatus(status, detail);
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
