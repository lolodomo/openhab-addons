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

import static org.openhab.binding.matter.internal.MatterBindingConstants.*;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.AttributeListener;
import org.openhab.binding.matter.internal.client.MatterClient;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient.AttributeChangedMessage;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient.NodeStateMessage;
import org.openhab.binding.matter.internal.client.NodeStateListener;
import org.openhab.binding.matter.internal.client.model.Node;
import org.openhab.binding.matter.internal.config.ControllerConfiguration;
import org.openhab.binding.matter.internal.discovery.NodeDiscoveryService;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ControllerHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
public class ControllerHandler extends AbstractMatterBridgeHandler implements AttributeListener, NodeStateListener {

    private final Logger logger = LoggerFactory.getLogger(ControllerHandler.class);
    // private int nodeId; // the controller nodeId?
    // private List<Node> nodes = Collections.synchronizedList(new LinkedList<Node>());
    private MatterClient client;

    public ControllerHandler(Bridge bridge) {
        super(bridge);
        client = new MatterClient();
        client.addAttributeListener(this);
        client.addNodeStateListener(this);
    }

    @Override
    public void onEvent(NodeStateMessage message) {
        switch (message.state) {
            case CONNECTED:
                // we need to set the child node online here, it should be offline until we get this message
                // we have a concurency issue, proabbly some synchroniced method in the client that is still in recieve
                // when we try and send
                scheduler.execute(() -> {
                    updateNode(message.nodeId);
                });
                break;
            case DISCONNECTED:
                break;
            case WAITINGFORDEVICEDISCOVERY:
                break;
            case RECONNECTING:
                break;
            case DECOMMISSIONED:
                break;
            case STRUCTURECHANGED:
                break;
            default:
        }
    }

    @Override
    public void onEvent(AttributeChangedMessage message) {
        NodeHandler handler = nodeHandler(message.path.nodeId);
        if (handler == null) {
            logger.debug("No handler found for node {}", message.path.nodeId);
            return;
        }
        handler.onEvent(message);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (!client.isConnected()) {
            logger.debug("not connected");
            return;
        }
        if (CHANNEL_1.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
            }
        }
        if (CHANNEL_PAIR_CODE.equals(channelUID.getId())) {
            try {
                client.pairNode(command.toString());
            } catch (Exception e) {
                logger.debug("Could not pair", e);
            }
        }
        if (CHANNEL_COMMAND.equals(channelUID.getId())) {
            try {
                refresh();
            } catch (Exception e) {
                logger.debug("Could not send command", e);
            }
        }
    }

    @Override
    public void initialize() {
        logger.debug("initialize");
        updateStatus(ThingStatus.UNKNOWN);
        ControllerConfiguration c = getConfigAs(ControllerConfiguration.class);
        // nodeId = c.nodeId;
        scheduler.execute(() -> {
            try {
                client.connect("localhost", 8888);
                updateStatus(ThingStatus.ONLINE);
            } catch (Exception e) {
                logger.debug("Could init", e);
            }
        });
    }

    @Override
    public void dispose() {
        client.disconnect();
    }

    public MatterClient getClient() {
        return client;
    }

    public void refresh() {
        logger.debug("refresh");
        try {
            Map<String, Node> nodesMap = client.getNodes();
            for (Node n : nodesMap.values()) {
                discoverChildNode(n);
                NodeHandler handler = nodeHandler(n.id);
                if (handler != null) {
                    handler.updateNode(n);
                }
            }
        } catch (Exception e) {
            logger.debug("Error communicating with controller", e);
            setOffline(e.getLocalizedMessage());
        }
    }

    private void updateNode(long id) {
        try {
            logger.debug("updateNode {}", id);
            // TODO handle when the id is not found as well
            Node node = client.getNode(id);
            discoverChildNode(node);
            NodeHandler handler = nodeHandler(node.id);
            if (handler != null) {
                handler.updateNode(node);
            }
        } catch (Exception e) {
            logger.debug("Error communicating with controller", e);
            setOffline(e.getLocalizedMessage());
        }
    }

    private void setOffline(@Nullable String message) {
        client.disconnect();
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE, message);
    }

    private void discoverChildNode(Node node) {
        NodeDiscoveryService discoveryService = this.discoveryService;
        if (discoveryService != null) {
            ThingUID bridgeUID = getThing().getUID();
            ThingUID thingUID = new ThingUID(THING_TYPE_NODE, bridgeUID, String.valueOf(node.id));
            discoveryService.discoverhildThing(thingUID, bridgeUID, node.id, "Matter Node " + node.id);

        }
    }

    private @Nullable NodeHandler nodeHandler(long nodeId) {
        for (Thing thing : getThing().getThings()) {
            ThingHandler handler = thing.getHandler();
            if (handler instanceof NodeHandler nodeHandler) {
                if (nodeHandler.getNodeId() == nodeId) {
                    return nodeHandler;
                }
            }
        }
        return null;
    }
}
