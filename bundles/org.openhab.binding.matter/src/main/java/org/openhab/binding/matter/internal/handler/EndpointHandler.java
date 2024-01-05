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

import static org.openhab.binding.matter.internal.MatterBindingConstants.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.model.Endpoint;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.config.EndpointConfiguration;
import org.openhab.binding.matter.internal.discovery.NodeDiscoveryService;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.BridgeHandler;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
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
    private int endpointId;
    private List<BaseCluster> clusters = Collections.synchronizedList(new LinkedList<BaseCluster>());

    public EndpointHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (CHANNEL_1.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                NodeHandler handler = nodeHandler();
                if (handler != null) {
                    handler.refresh();
                }
            }

        }
    }

    @Override
    public void initialize() {
        EndpointConfiguration config = getConfigAs(EndpointConfiguration.class);
        endpointId = config.id;
        logger.debug("initialize endpoint {}", endpointId);
        initializeEndpoint();
    }

    private void initializeEndpoint() {
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

    public int getEndpointId() {
        return endpointId;
    }

    public void updateEndpoint(Endpoint endpoint) {
        logger.debug("updateEndpoint {}", endpoint.id);
        synchronized (clusters) {
            clusters.clear();
            for (BaseCluster c : endpoint.clusters.values()) {
                clusters.add(c);
                discoverChildCluster(c);
            }
        }
        refresh();
    }

    public void refresh() {
        synchronized (clusters) {
            for (BaseCluster c : clusters) {
                ClusterHandler handler = clusterHandler(c.id);
                if (handler != null) {
                    handler.updateCluster(c);
                }
            }
        }
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

    private @Nullable ClusterHandler clusterHandler(int clusterId) {
        for (Thing thing : getThing().getThings()) {
            ThingHandler handler = thing.getHandler();
            if (handler instanceof ClusterHandler clusterHandler) {
                if (clusterHandler.getClusterId() == clusterId) {
                    return clusterHandler;
                }
            }
        }
        return null;
    }

    private void discoverChildCluster(BaseCluster cluster) {
        NodeDiscoveryService discoveryService = this.discoveryService;
        if (discoveryService != null) {
            ThingUID bridgeUID = getThing().getUID();
            ThingUID thingUID = new ThingUID(THING_TYPE_CLUSTER, bridgeUID, String.valueOf(cluster.id));
            discoveryService.discoverhildThing(thingUID, bridgeUID, (long) cluster.id,
                    "Matter Cluster " + cluster.name);

        }
    }
}
