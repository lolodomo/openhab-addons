/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
import org.openhab.binding.matter.internal.client.model.Endpoint;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.LevelControlCluster;
import org.openhab.binding.matter.internal.config.EndpointConfiguration;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BridgeHandler;
import org.openhab.core.thing.binding.builder.BridgeBuilder;
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
public class EndpointHandler extends AbstractMatterBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(EndpointHandler.class);
    protected long nodeId;
    protected int endpointId;
    private List<BaseCluster> clusters = Collections.synchronizedList(new LinkedList<BaseCluster>());
    private Map<Integer, Class<? extends ClusterHandler>> handlersMapping = new HashMap();
    private Map<String, ClusterHandler> channelIdMap = new HashMap<String, ClusterHandler>();
    private Map<Integer, ClusterHandler> clusterIdMap = new HashMap<Integer, ClusterHandler>();

    public EndpointHandler(Bridge bridge) {
        super(bridge);
        handlersMapping.put(LevelControlCluster.CLUSTER_ID, LevelControlHandler.class);

    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // if (CHANNEL_1.equals(channelUID.getId())) {
        // if (command instanceof RefreshType) {
        // NodeHandler handler = nodeHandler();
        // if (handler != null) {
        // handler.refresh();
        // }
        // }
        //
        // }
        ClusterHandler c = channelIdMap.get(channelUID.getId());
        if (c != null) {
            c.handleCommand(channelUID, command);
        }
    }

    @Override
    public void initialize() {
        EndpointConfiguration config = getConfigAs(EndpointConfiguration.class);
        endpointId = config.id;
        logger.debug("initialize endpoint {}", endpointId);
        NodeHandler handler = nodeHandler();
        if (handler == null) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_UNINITIALIZED);
        } else if (handler.getThing().getStatus() != ThingStatus.ONLINE) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE);
        } else {
            updateStatus(ThingStatus.ONLINE);
            scheduler.execute(() -> {
                handler.refresh();
            });
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public BridgeBuilder editThing() {
        return super.editThing();
    }

    @Override
    public void updateThing(Thing thing) {
        super.updateThing(thing);
    }

    @Override
    public void updateState(String channelID, State state) {
        super.updateState(channelID, state);
    }

    public int getEndpointId() {
        return endpointId;
    }

    public void updateEndpoint(Endpoint endpoint) {
        if (endpoint.id != endpointId) {
            return;
        }
        endpoint.clusters.forEach((k, v) -> {
            Integer id = v.id;
            ClusterHandler c = clusterIdMap.get(id);
            if (c == null) {
                // lookup handler
                // c = new handler
                Class<? extends ClusterHandler> clazz = handlersMapping.get(id);
                if (clazz != null) {
                    try {
                        Class<?>[] constructorParameterTypes = new Class<?>[] { EndpointHandler.class, Long.class,
                                int.class };
                        Constructor<? extends ClusterHandler> constructor = clazz
                                .getConstructor(constructorParameterTypes);
                        final ClusterHandler handler = constructor.newInstance(this, nodeId, endpointId);
                        clusterIdMap.put(id, handler);
                        // now we need to create channels and add those to the channel map
                        handler.createChannels(v).forEach(channel -> {
                            channelIdMap.put(channel.getId(), handler);
                        });
                        c = handler;
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        logger.debug("Could not create cluster handler", e);
                    }
                }
            }
            if (c == null) {
                // no handler found
                return;
            }
            c.updateCluster(v);
        });
    }

    public void refresh() {

        /// this is old
        // step 1 iterate over all clusters in the endpoint
        // step 2 look up a ClusterHandler by the cluster ID
        // step 4 create a new instance of the handler
        // step 5 call createChannels on this
        // step 6 link channles to the handler in a Map
        // step 7 make sure to clean up on dispose

        // when a command comes in, we need to lookup the channel to a handler

        // synchronized (clusters) {
        // for (BaseCluster c : clusters) {
        // ClusterHandler handler = clusterHandler(c.id);
        // if (handler != null) {
        // handler.updateCluster(c);
        // }
        // }
        // }
    }

    private @Nullable NodeHandler nodeHandler() {
        Bridge bridge = getBridge();
        if (bridge != null) {
            BridgeHandler handler = bridge.getHandler();
            if (handler instanceof NodeHandler nodeHandler) {
                return nodeHandler;
            }
        }
        return null;
    }

    // private @Nullable ClusterHandler clusterHandler(int clusterId) {
    // for (Thing thing : getThing().getThings()) {
    // ThingHandler handler = thing.getHandler();
    // if (handler instanceof ClusterHandler clusterHandler) {
    // if (clusterHandler.getClusterId() == clusterId) {
    // return clusterHandler;
    // }
    // }
    // }
    // return null;
    // }

    // private void discoverChildCluster(BaseCluster cluster) {
    // // so we need a map of THING_TYPE_CLUSTER to clusters, so LevelControl, or OnOff
    // NodeDiscoveryService discoveryService = this.discoveryService;
    // if (discoveryService != null) {
    // logger.debug("discoverChildCluster {}", cluster.name);
    // ThingTypeUID clusterThing = ClusterThingTypes.CLUSTER_NAME_TO_THING_TYPE_MAPPING.get(cluster.name);
    // if (clusterThing != null) {
    // ThingUID bridgeUID = getThing().getUID();
    // ThingUID thingUID = new ThingUID(clusterThing, bridgeUID, clusterThing.getId());
    // discoveryService.discoverhildThing(thingUID, bridgeUID, (long) cluster.id,
    // "Matter Cluster " + cluster.name);
    // }
    //
    // }
    // }
}
