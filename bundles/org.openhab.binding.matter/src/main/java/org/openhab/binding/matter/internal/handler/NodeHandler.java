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

import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_1;
import static org.openhab.binding.matter.internal.MatterBindingConstants.THING_TYPE_ENDPOINT;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.AttributeListener;
import org.openhab.binding.matter.internal.client.model.Endpoint;
import org.openhab.binding.matter.internal.client.model.Node;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.config.NodeConfiguration;
import org.openhab.binding.matter.internal.discovery.NodeDiscoveryService;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingStatusInfo;
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
public class NodeHandler extends AbstractMatterBridgeHandler implements AttributeListener {

    private final Logger logger = LoggerFactory.getLogger(NodeHandler.class);
    // private List<Endpoint> endpoints2 = Collections.synchronizedList(new LinkedList<Endpoint>());

    private Map<Integer, Endpoint> endpoints = Collections.synchronizedMap(new HashMap<>());

    private String nodeId = "";

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
        logger.debug("Disposing Handler");
    }

    @Override
    public void childHandlerInitialized(ThingHandler childHandler, Thing childThing) {
        super.childHandlerInitialized(childHandler, childThing);
        logger.debug("childHandlerInitialized {}", childHandler);
        if (childHandler instanceof EndpointHandler) {
            EndpointHandler handler = (EndpointHandler) childHandler;
            int id = handler.getEndpointId();
            Endpoint e = endpoints.get(id);
            if (e != null) {
                handler.updateEndpoint(e);
            }
        }
    }

    @Override
    public void childHandlerDisposed(ThingHandler childHandler, Thing childThing) {
        // do nothing by default, can be overridden by subclasses
    }

    public String getNodeId() {
        return nodeId;
    }

    public void updateNode(Node node) {
        logger.debug("updateNode {}", nodeId);

        synchronized (endpoints) {
            endpoints.clear();
            for (Endpoint e : node.endpoints.values()) {
                endpoints.put(e.number, e);
                discoverChildEndpoint(e);
            }
        }
        if (getThing().getStatus() != ThingStatus.ONLINE) {
            updateStatus(ThingStatus.ONLINE);
        }
        refresh();
    }

    @Override
    public void onEvent(AttributeChangedMessage message) {
        EndpointHandler handler = endpointHandler(message.path.endpointId);
        if (handler != null) {
            handler.onEvent(message);
        }
    }

    public void refresh() {
        synchronized (endpoints) {
            for (Endpoint e : endpoints.values()) {
                EndpointHandler handler = endpointHandler(e.number);
                if (handler != null) {
                    handler.updateEndpoint(e);
                }
            }
        }
    }

    public void updateEndpoint(int endpointNum) {
        synchronized (endpoints) {
            for (Endpoint e : endpoints.values()) {
                EndpointHandler handler = endpointHandler(e.number);
                if (handler != null) {
                    handler.updateEndpoint(e);
                }
            }
        }
    }

    public void setNodeStatus(ThingStatus status, ThingStatusDetail detail) {
        updateStatus(status, detail);
    }

    private @Nullable EndpointHandler endpointHandler(int endpointId) {
        for (Thing thing : getThing().getThings()) {
            ThingHandler handler = thing.getHandler();
            if (handler instanceof EndpointHandler endpointHandler) {
                logger.debug("endpointHandler checking {} == {}", endpointHandler.getEndpointId(), endpointId);
                if (endpointHandler.getEndpointId() == endpointId) {
                    return endpointHandler;
                }
            }
        }
        return null;
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

    private void discoverChildEndpoint(Endpoint endpoint) {
        logger.debug("discoverChildEndpoint {}", endpoint.number);
        NodeDiscoveryService discoveryService = this.discoveryService;
        if (discoveryService != null) {
            ThingUID bridgeUID = getThing().getUID();
            ThingUID thingUID = new ThingUID(THING_TYPE_ENDPOINT, bridgeUID, String.valueOf(endpoint.number));
            discoveryService.discoverChildEndpointThing(thingUID, bridgeUID, nodeId, endpoint.number);

        }
    }
}
