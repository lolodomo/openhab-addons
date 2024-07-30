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
import static org.openhab.binding.matter.internal.MatterBindingConstants.THING_TYPE_ENDPOINT;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.MatterClientListener;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.model.Endpoint;
import org.openhab.binding.matter.internal.client.model.Node;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.BasicInformationCluster;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.client.model.ws.NodeStateMessage;
import org.openhab.binding.matter.internal.config.ControllerConfiguration;
import org.openhab.binding.matter.internal.discovery.MatterDiscoveryHandler;
import org.openhab.binding.matter.internal.discovery.MatterDiscoveryService;
import org.openhab.core.OpenHAB;
import org.openhab.core.config.core.Configuration;
import org.openhab.core.config.core.validation.ConfigValidationException;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerService;
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
public class ControllerHandler extends BaseBridgeHandler implements MatterClientListener, MatterDiscoveryHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerHandler.class);
    // The endpoints / devices associated with a node. Typically a node has 1 device endpoint, but may have more (Hue
    // bridge, complicated devices, etc..)
    private Map<String, Map<Integer, Endpoint>> nodeEndpoints = Collections.synchronizedMap(new HashMap<>());
    // Last time the node sent an update to us
    private Map<String, LocalDateTime> nodesLastUpdate = new ConcurrentHashMap<>();
    // Set of nodes we are waiting to connect to
    private Set<String> outstandingNodeRequests = Collections.synchronizedSet(new HashSet<>());
    // Set of nodes we need to try reconnecting to
    private Set<String> disconnectedNodes = Collections.synchronizedSet(new HashSet<>());

    private @Nullable MatterDiscoveryService discoveryService;
    private MatterWebsocketClient client;
    private @Nullable ScheduledFuture<?> reconnectFuture;
    private boolean running = true;
    private boolean ready = false;
    private final ReentrantLock refreshLock = new ReentrantLock();
    private @Nullable ScheduledFuture<?> checkFuture;

    public ControllerHandler(Bridge bridge) {
        super(bridge);
        client = new MatterWebsocketClient();
        client.addListener(this);
    }

    @Override
    public Collection<Class<? extends ThingHandlerService>> getServices() {
        return Set.of(MatterDiscoveryService.class);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("handleCommand {} {}", channelUID, command);
        if (!client.isConnected()) {
            logger.debug("not connected");
            return;
        }
        if (CHANNEL_PAIR_CODE.equals(channelUID.getId())) {
            try {
                client.pairNode(command.toString());
            } catch (Exception e) {
                logger.debug("Could not pair", e);
            }
        }
        if (CHANNEL_COMMAND.equals(channelUID.getId())) {
            if (command instanceof RefreshType || "REFRESH".equals(command.toString())) {
                refresh();
                return;
            }

            String[] args = command.toString().split(" ");
            if (args.length < 2) {
                logger.debug("Commands require at least 2 segments");
                return;
            }
            Object[] params = args.length > 2 ? (Object[]) Arrays.copyOfRange(args, 2, args.length) : new String[0];
            client.genericCommand(args[0], args[1], params).thenAccept(result -> {
                logger.debug("Command {} ", command);
                logger.debug("Result: {}", result);
            }).exceptionally(e -> {
                logger.debug("Could not send command", e);
                return null;
            });
        }
    }

    public void handleConfigurationUpdate2(Map<String, Object> configurationParameters)
            throws ConfigValidationException {
        logger.debug("handleConfigurationUpdate");
        validateConfigurationParameters(configurationParameters);
        Configuration configuration = editConfiguration();
        boolean reinitialize = false;
        String pairCode = null;
        for (Entry<String, Object> configurationParameter : configurationParameters.entrySet()) {
            Object value = configurationParameter.getValue();

            logger.debug("{}: old: {} new: {}", configurationParameter.getKey(),
                    configuration.get(configurationParameter.getKey()), configurationParameter.getValue());
            // Ignore any configuration parameters that have not changed
            if (Objects.equals(configurationParameter.getValue(), configuration.get(configurationParameter.getKey()))) {
                logger.debug("Controller Configuration update ignored {} to {} ({})", configurationParameter.getKey(),
                        value, value == null ? "null" : value.getClass().getSimpleName());
                continue;
            }
            logger.debug("Controller Configuration update {} to {}", configurationParameter.getKey(), value);
            switch (configurationParameter.getKey()) {
                case "host":
                case "port":
                case "nodeId":
                    reinitialize = true;
                    break;
                case "pairCode":
                    if (!value.toString().isBlank()) {
                        pairCode = value.toString();
                        value = "";
                    }
            }
            configuration.put(configurationParameter.getKey(), value);
        }
        updateConfiguration(configuration);
        if (reinitialize) {
            dispose();
            updateStatus(ThingStatus.INITIALIZING);
            initialize();
        } else if (pairCode != null) {
            try {
                client.pairNode(pairCode);
            } catch (Exception e) {
                logger.debug("Could not pair", e);
            }
        }
    }

    @Override
    public void initialize() {
        logger.debug("initialize");
        String folderName = OpenHAB.getUserDataFolder() + File.separator + "matter";
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String storagePath = folder.getAbsolutePath() + File.separator + "controller-" + getThing().getUID().getId()
                + ".json";
        logger.debug("matter config: {}", storagePath);
        final ControllerConfiguration config = getConfigAs(ControllerConfiguration.class);
        checkFuture = scheduler.scheduleAtFixedRate(this::checkNodes, 0, 5, TimeUnit.MINUTES);
        scheduler.execute(() -> {
            try {
                if (!config.host.isBlank() && config.port > 0) {
                    logger.debug("Connecting to custom host {} and port {}", config.host, config.port);
                    client.connect(config.host, config.port, storagePath);
                } else {
                    logger.debug("Connecting to embedded service");
                    client.connect(storagePath);
                }
                running = true;
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
        ScheduledFuture<?> reconnectFuture = this.reconnectFuture;
        if (reconnectFuture != null) {
            reconnectFuture.cancel(true);
        }
        ScheduledFuture<?> checkFuture = this.checkFuture;
        if (checkFuture != null) {
            checkFuture.cancel(true);
        }
        outstandingNodeRequests.clear();
        disconnectedNodes.clear();
        client.disconnect();
    }

    @Override
    public void childHandlerInitialized(ThingHandler childHandler, Thing childThing) {
        super.childHandlerInitialized(childHandler, childThing);
        logger.debug("childHandlerInitialized {}", childHandler);
        if (!ready || refreshLock.isLocked()) {
            return;
        }
        if (childHandler instanceof EndpointHandler handler) {
            String nodeId = handler.getNodeId();
            updateNode(nodeId);
            // // TODO we need to check with the matter network if this node is connected or not. If it is, then update
            // the
            // // endpoint, if not we need to connect to it.
            // synchronized (nodeEndpoints) {
            // var endpoints = nodeEndpoints.get(nodeId);
            // logger.debug("childHandlerInitialized endpoints {}", endpoints);
            // Optional.ofNullable(endpoints).map(ep -> ep.get(endpointId)).ifPresent(handler::updateEndpoint);
            // }
        }
    }

    @Override
    public void setDiscoveryService(@Nullable MatterDiscoveryService service) {
        logger.debug("setDiscoveryService");
        this.discoveryService = service;
    }

    @Override
    public void startScan() {
        refresh();
    }

    @Override
    public void onEvent(NodeStateMessage message) {
        logger.debug("Node onEvent: node {} is {}", message.nodeId, message.state);
        switch (message.state) {
            case CONNECTED:
                if (!refreshLock.isLocked()) {
                    updateNode(message.nodeId);
                }
                break;
            case DISCONNECTED:
            case DECOMMISSIONED:
            case RECONNECTING:
                updateEndpointStatuses(message.nodeId, ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                        "Node " + message.state.name());
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
        EndpointHandler handler = endpointHandler(message.path.nodeId, message.path.endpointId);
        modifyNodesLastUpdate(message.path.nodeId);
        if (handler == null) {
            logger.debug("No handler found for node {}", message.path.nodeId);
            return;
        }
        handler.onEvent(message);
    }

    @Override
    public void onConnect() {
        logger.debug("Websocket connected");
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
        ready = true;
        refreshLock.lock();
        updateStatus(ThingStatus.ONLINE);
        client.getCommissionedNodeIds(false).thenAccept(nodeIds -> {
            for (String id : nodeIds) {
                updateNode(id);
            }
        }).exceptionally(e -> {
            logger.debug("Error communicating with controller", e);
            setOffline(e.getLocalizedMessage());
            return null;
        }).whenComplete((nodeIds, e) -> refreshLock.unlock());
    }

    protected MatterWebsocketClient getClient() {
        return client;
    }

    private void refresh() {
        logger.debug("refresh");
        if (!ready || refreshLock.isLocked()) {
            return;
        }
        refreshLock.lock();
        client.getConnectedNodeIds().thenAccept(nodeIds -> {
            for (String id : nodeIds) {
                updateNode(id);
            }
        }).exceptionally(e -> {
            logger.debug("Error communicating with controller", e);
            setOffline(e.getLocalizedMessage());
            return null;
        }).whenComplete((nodeIds, e) -> refreshLock.unlock());
    }

    protected void endpointRemoved(String nodeId, int endpointId) {
        logger.debug("endpointRemoved endpoint {}:{}", nodeId, endpointId);

        // check if we remove deleted endpoint things from the actual matter network
        if (!getConfigAs(ControllerConfiguration.class).decommissionNodesOnDelete) {
            return;
        }
        // only remove the node from the network if all endpoints things on the node are deleted
        synchronized (nodeEndpoints) {
            boolean lastEndpoint = true;
            for (Thing thing : getThing().getThings()) {
                ThingHandler handler = thing.getHandler();
                if (handler instanceof EndpointHandler endpointHandler) {
                    if (endpointHandler.endpointId == endpointId) {
                        continue;
                    }
                    lastEndpoint = false;
                    break;
                }
            }
            if (lastEndpoint) {
                try {
                    logger.debug("Decommissioning node {}", nodeId);
                    client.decommissionNode(nodeId);
                    nodeEndpoints.remove(nodeId);
                } catch (Exception e) {
                    logger.debug("Could not decommission node {}", nodeId, e);
                }
            }
        }
    }

    private synchronized void reconnect() {
        ScheduledFuture<?> reconnectFuture = this.reconnectFuture;
        if (reconnectFuture != null) {
            reconnectFuture.cancel(true);
        }
        this.reconnectFuture = scheduler.schedule(this::initialize, 30, TimeUnit.SECONDS);
    }

    private synchronized void updateNode(String id) {
        logger.debug("updateNode BEGIN {}", id);
        // If we are already waiting to get this node, return;
        if (outstandingNodeRequests.contains(id)) {
            return;
        }
        outstandingNodeRequests.add(id);
        client.getNode(id).thenAccept(node -> {
            updateNode(node);
            modifyNodesLastUpdate(node.id);
            disconnectedNodes.remove(id);
            logger.debug("updateNode END {}", id);
        }).exceptionally(e -> {
            logger.debug("Could not update node {}", id, e);
            disconnectedNodes.add(id);
            String message = e.getMessage();
            updateEndpointStatuses(id, ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
                    message != null ? message : "");
            return null;
        }).whenComplete((node, e) -> outstandingNodeRequests.remove(id));
    }

    private void updateNode(Node node) {
        synchronized (nodeEndpoints) {
            Map<Integer, Endpoint> endpoints = new HashMap<>();
            for (Endpoint e : node.endpoints.values()) {
                endpoints.put(e.number, e);
                discoverChildEndpoint(node, e);
                EndpointHandler handler = endpointHandler(node.id, e.number);
                if (handler != null) {
                    Thing thing = handler.getThing();
                    updateEndpointThingProperties(node, thing, e.number);
                    handler.updateEndpoint(e);
                }
            }
            nodeEndpoints.put(node.id, endpoints);
        }
    }

    private void updateEndpointStatuses(String nodeId, ThingStatus status, ThingStatusDetail detail, String details) {
        for (Thing thing : getThing().getThings()) {
            ThingHandler handler = thing.getHandler();
            if (handler instanceof EndpointHandler endpointHandler) {
                if (nodeId.equals(endpointHandler.getNodeId())) {
                    endpointHandler.setEndpointStatus(status, detail, details);
                }
            }
        }
    }

    private void setOffline(@Nullable String message) {
        client.disconnect();
        updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR, message);
        reconnect();
    }

    private void discoverChildEndpoint(Node node, Endpoint endpoint) {
        logger.debug("discoverChildEndpoint {}", node.id);
        // endpoint 0 is the root info cluster, not an actual device
        if (endpoint.number == 0) {
            return;
        }

        MatterDiscoveryService discoveryService = this.discoveryService;
        if (discoveryService != null) {
            ThingUID bridgeUID = getThing().getUID();
            ThingUID thingUID = new ThingUID(THING_TYPE_ENDPOINT, bridgeUID, node.id + "_" + endpoint.number);
            discoveryService.discoverChildEndpointThing(thingUID, bridgeUID, node, endpoint.number);
        }
    }

    private @Nullable EndpointHandler endpointHandler(String nodeId, int endpointId) {
        for (Thing thing : getThing().getThings()) {
            ThingHandler handler = thing.getHandler();
            if (handler instanceof EndpointHandler endpointHandler) {
                if (nodeId.equals(endpointHandler.getNodeId()) && endpointHandler.getEndpointId() == endpointId) {
                    return endpointHandler;
                }
            }
        }
        return null;
    }

    private void updateEndpointThingProperties(Node node, Thing thing, int endpointNum) {
        Endpoint root = node.endpoints.get(Integer.valueOf(0));
        if (root != null) {
            BaseCluster cluster = root.clusters.get(BasicInformationCluster.CLUSTER_NAME);
            if (cluster != null && cluster instanceof BasicInformationCluster basicCluster) {
                thing.setProperty(Thing.PROPERTY_SERIAL_NUMBER, basicCluster.serialNumber);
                thing.setProperty(Thing.PROPERTY_FIRMWARE_VERSION, basicCluster.softwareVersionString);
                thing.setProperty(Thing.PROPERTY_VENDOR, basicCluster.vendorName);
                thing.setProperty(Thing.PROPERTY_MODEL_ID, basicCluster.productName);
                thing.setProperty(Thing.PROPERTY_HARDWARE_VERSION, basicCluster.hardwareVersionString);
                thing.setProperty("path", node.id + ":" + endpointNum);
            }
        }
    }

    private void modifyNodesLastUpdate(String nodeId) {
        nodesLastUpdate.put(nodeId, LocalDateTime.now());
    }

    private void checkNodes() {
        LocalDateTime checkTime = LocalDateTime.now().minusMinutes(60);
        nodesLastUpdate.forEach((nodeId, lastUpdate) -> {
            if (lastUpdate.isBefore(checkTime)) {
                updateNode(nodeId);
            }
        });
        disconnectedNodes.forEach(nodeId -> updateNode(nodeId));
    }
}
