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

import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_COMMAND;
import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_PAIR_CODE;
import static org.openhab.binding.matter.internal.MatterBindingConstants.THING_TYPE_NODE;

import java.io.File;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.AttributeListener;
import org.openhab.binding.matter.internal.client.ControllerStateListener;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.NodeStateListener;
import org.openhab.binding.matter.internal.client.model.Node;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.client.model.ws.NodeStateMessage;
import org.openhab.binding.matter.internal.config.ControllerConfiguration;
import org.openhab.binding.matter.internal.discovery.NodeDiscoveryService;
import org.openhab.core.OpenHAB;
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
public class ControllerHandler extends AbstractMatterBridgeHandler
        implements AttributeListener, NodeStateListener, ControllerStateListener {

    private final Logger logger = LoggerFactory.getLogger(ControllerHandler.class);
    // private int nodeId; // the controller nodeId?
    // private List<Node> nodes = Collections.synchronizedList(new LinkedList<Node>());
    private MatterWebsocketClient client;
    private @Nullable ScheduledFuture<?> reconnectFuture;
    private boolean running = true;
    private boolean ready = false;

    public ControllerHandler(Bridge bridge) {
        super(bridge);
        client = new MatterWebsocketClient();
        client.addAttributeListener(this);
        client.addNodeStateListener(this);
        client.addControllerStateListener(this);
    }

    @Override
    public void onEvent(NodeStateMessage message) {
        switch (message.state) {
            case CONNECTED:
                if (ready) {
                    scheduler.execute(() -> {
                        updateNode(message.nodeId);
                    });
                }
                break;
            case DISCONNECTED:
            case DECOMMISSIONED:
            case RECONNECTING:
                NodeHandler handler = nodeHandler(message.nodeId);
                if (handler != null) {
                    handler.setNodeStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
                }
                break;
            case WAITINGFORDEVICEDISCOVERY:
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
    public void onConnect() {
    }

    @Override
    public void onDisconnect(String reason) {
        if (!running) {
            return;
        }
        client.disconnect();
        setOffline(reason);
    }

    @Override
    public void onReady() {
        updateStatus(ThingStatus.ONLINE);
        ready = true;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (!client.isConnected()) {
            logger.debug("not connected");
            return;
        }

        if (command instanceof RefreshType) {
            if (ready) {
                refresh();
                return;
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

    // @Override
    // public void handleConfigurationUpdate(Map<String, Object> configurationParameters)
    // throws ConfigValidationException {
    // logger.debug("handleConfigurationUpdate");
    // validateConfigurationParameters(configurationParameters);
    // Configuration configuration = editConfiguration();
    // boolean reinitialize = false;
    // String pairCode = null;
    // for (Entry<String, Object> configurationParameter : configurationParameters.entrySet()) {
    // Object value = configurationParameter.getValue();

    // logger.debug("{}: old: {} new: {}", configurationParameter.getKey(),
    // configuration.get(configurationParameter.getKey()), configurationParameter.getValue());
    // // Ignore any configuration parameters that have not changed
    // if (Objects.equals(configurationParameter.getValue(), configuration.get(configurationParameter.getKey()))) {
    // logger.debug("Controller Configuration update ignored {} to {} ({})", configurationParameter.getKey(),
    // value, value == null ? "null" : value.getClass().getSimpleName());
    // continue;
    // }
    // logger.debug("Controller Configuration update {} to {}", configurationParameter.getKey(), value);
    // switch (configurationParameter.getKey()) {
    // case "host":
    // case "port":
    // case "nodeId":
    // reinitialize = true;
    // break;
    // case "pairCode":
    // if (!value.toString().isBlank()) {
    // pairCode = value.toString();
    // value = "";
    // }
    // }
    // configuration.put(configurationParameter.getKey(), value);
    // }
    // updateConfiguration(configuration);
    // if (reinitialize) {
    // dispose();
    // updateStatus(ThingStatus.INITIALIZING);
    // initialize();
    // } else if (pairCode != null && ready) {
    // try {
    // client.pairNode(pairCode);
    // } catch (Exception e) {
    // logger.debug("Could not pair", e);
    // }
    // }
    // }

    @Override
    public void initialize() {
        logger.debug("initialize");
        scheduler.execute(() -> {
            try {
                String folderName = OpenHAB.getUserDataFolder() + File.separator + "matter";
                File folder = new File(folderName);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                String storagePath = folder.getAbsolutePath() + File.separator + "controller-"
                        + getThing().getUID().getId() + ".json";
                logger.debug("matter config: {}", storagePath);
                ControllerConfiguration config = getConfigAs(ControllerConfiguration.class);
                if (!config.host.isBlank() && config.port > 0) {
                    logger.debug("Connecting to custom host {} and port {}", config.host, config.port);
                    client.connect(config.host, config.port, storagePath);
                } else {
                    logger.debug("Connecting to embedded service");
                    client.connect(storagePath);
                }
            } catch (Exception e) {
                logger.debug("Could not init", e);
                setOffline(e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void dispose() {
        logger.debug("dispose");
        running = false;
        if (reconnectFuture != null) {
            reconnectFuture.cancel(true);
        }
        client.disconnect();
    }

    @Override
    public void childHandlerInitialized(ThingHandler childHandler, Thing childThing) {
        super.childHandlerInitialized(childHandler, childThing);
        logger.debug("childHandlerInitialized {}", childHandler);
        if (childHandler instanceof NodeHandler) {
            String id = ((NodeHandler) childHandler).getNodeId();
            updateNode(id);
        }
    }

    @Override
    public void childHandlerDisposed(ThingHandler childHandler, Thing childThing) {
        // do nothing by default, can be overridden by subclasses
    }

    public MatterWebsocketClient getClient() {
        return client;
    }

    public void refresh() {
        logger.debug("refresh");
        try {
            List<String> nodeIds = client.getCommissionedNodeIds();
            for (String id : nodeIds) {
                updateNode(id);
            }
            // wait until we have at least one update before becoming ready
            ready = true;
        } catch (Exception e) {
            logger.debug("Error communicating with controller", e);
            setOffline(e.getLocalizedMessage());
        }
    }

    private synchronized void reconnect() {
        if (reconnectFuture != null) {
            reconnectFuture.cancel(true);
        }
        reconnectFuture = scheduler.schedule(this::initialize, 30, TimeUnit.SECONDS);
    }

    void updateNode(Node node) {
        discoverChildNode(node);
        NodeHandler handler = nodeHandler(node.id);
        if (handler != null) {
            handler.updateNode(node);
        }
    }

    void updateNode(String id) {
        logger.debug("updateNode {}", id);
        try {
            Node n = client.getNode(id);
            updateNode(n);
        } catch (Exception e) {
            logger.debug("Could not update node {}", id, e);
            NodeHandler handler = nodeHandler(id);
            if (handler != null) {
                handler.setNodeStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
            }
        }
    }

    private void setOffline(@Nullable String message) {
        client.disconnect();
        ready = false;
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, message);
        reconnect();
    }

    private void discoverChildNode(Node node) {
        NodeDiscoveryService discoveryService = this.discoveryService;
        if (discoveryService != null) {
            ThingUID bridgeUID = getThing().getUID();
            ThingUID thingUID = new ThingUID(THING_TYPE_NODE, bridgeUID, node.id);
            discoveryService.discoverChildNodeThing(thingUID, bridgeUID, node.id);

        }
    }

    private @Nullable NodeHandler nodeHandler(String nodeId) {
        for (Thing thing : getThing().getThings()) {

            ThingHandler handler = thing.getHandler();
            logger.debug("Checking Thing {} {}", thing.getLabel(), handler);
            if (handler instanceof NodeHandler nodeHandler) {
                logger.debug("Checking {} == {} ", nodeHandler.getNodeId(), nodeId);
                if (nodeHandler.getNodeId().equals(nodeId)) {
                    return nodeHandler;
                }
            }
        }
        return null;
    }
}
