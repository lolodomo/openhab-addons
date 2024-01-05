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
import org.openhab.binding.matter.internal.client.model.Node;
import org.openhab.binding.matter.internal.config.NodeConfiguration;
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
 * The {@link NodeHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
public class NodeHandler extends AbstractMatterBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(NodeHandler.class);
    private List<Endpoint> endpoints = Collections.synchronizedList(new LinkedList<Endpoint>());

    private long nodeId;

    public NodeHandler(Bridge bridge) {
        super(bridge);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (CHANNEL_1.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                ControllerHandler handler = controllerHandler();
                if (handler != null) {
                    handler.refresh();
                }
            }
        }
    }

    @Override
    public void initialize() {
        NodeConfiguration config = getConfigAs(NodeConfiguration.class);
        nodeId = config.id;
        logger.debug("initialize node {}", nodeId);
        initializeNode();
    }

    @Override
    public void dispose() {
    }

    public long getNodeId() {
        return nodeId;
    }

    public void updateNode(Node node) {
        logger.debug("updateNode {}", nodeId);

        synchronized (endpoints) {
            endpoints.clear();
            for (Endpoint e : node.endpoints.values()) {
                endpoints.add(e);
                discoverChildEndpoint(e);
            }
        }
        refresh();
    }

    public void refresh() {
        synchronized (endpoints) {
            for (Endpoint e : endpoints) {
                EndpointHandler handler = endpointHandler(e.id);
                if (handler != null) {
                    handler.updateEndpoint(e);
                }
            }
        }
    }

    // public void endpointsUpdate(Map<String, Endpoint> endpoints) {
    // }

    private void initializeNode() {
        ControllerHandler handler = controllerHandler();
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

    private @Nullable EndpointHandler endpointHandler(int endpointId) {
        for (Thing thing : getThing().getThings()) {
            ThingHandler handler = thing.getHandler();
            if (handler instanceof EndpointHandler endpointHandler) {
                if (endpointHandler.getEndpointId() == endpointId) {
                    return endpointHandler;
                }
            }
        }
        return null;
    }

    private @Nullable ControllerHandler controllerHandler() {
        Bridge bridge = getBridge();
        if (bridge != null) {
            BridgeHandler handler = bridge.getHandler();
            if (handler instanceof ControllerHandler controllerHandler) {
                return controllerHandler;
            }
        }
        return null;
    }

    private void discoverChildEndpoint(Endpoint endpoint) {
        logger.debug("discoverChildEndpoint {}", endpoint.id);
        NodeDiscoveryService discoveryService = this.discoveryService;
        if (discoveryService != null) {
            ThingUID bridgeUID = getThing().getUID();
            ThingUID thingUID = new ThingUID(THING_TYPE_ENDPOINT, bridgeUID, String.valueOf(endpoint.id));
            discoveryService.discoverhildThing(thingUID, bridgeUID, (long) endpoint.id,
                    "Matter Endpoint " + endpoint.id);

        }
    }
}
