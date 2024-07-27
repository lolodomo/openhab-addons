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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
    private Map<String, Map<Integer, Endpoint>> nodeEndpoints = Collections.synchronizedMap(new HashMap<>());
    private @Nullable MatterDiscoveryService discoveryService;
    private MatterWebsocketClient client;
    private @Nullable ScheduledFuture<?> reconnectFuture;
    private boolean running = true;
    private final ReentrantLock refreshLock = new ReentrantLock();

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
    public void onEvent(NodeStateMessage message) {
        switch (message.state) {
            case CONNECTED:
                if (!refreshLock.isLocked()) {
                    updateNode(message.nodeId);
                }
                break;
            case DISCONNECTED:
            case DECOMMISSIONED:
            case RECONNECTING:
                updateEndpointStatuses(message.nodeId, ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
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
        try {
            client.connectAllNodes();
            updateStatus(ThingStatus.ONLINE);
        } catch (Exception e) {
            logger.debug("Could not connect to nodes", e);
            setOffline(e.getMessage());
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (!client.isConnected()) {
            logger.debug("not connected");
            return;
        }

        if (command instanceof RefreshType) {
            refresh();
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
            if ("REFRESH".equals(command.toString())) {
                refresh();
                return;
            }

            try {
                String[] args = command.toString().split(" ");
                if (args.length < 3) {
                    logger.debug("Commands require at least 3 segments");
                    return;
                }

                String result = client.genericCommand(args[0], args[1],
                        (Object) Arrays.copyOfRange(args, 2, args.length));
                logger.debug("Command {} ", command);
                logger.debug("Result: {}", result);
            } catch (Exception e) {
                logger.debug("Could not send command", e);
            }
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
        if (childHandler instanceof EndpointHandler handler) {
            String nodeId = handler.getNodeId();
            int endpointId = handler.getEndpointId();
            synchronized (nodeEndpoints) {
                var endpoints = nodeEndpoints.get(nodeId);
                logger.debug("childHandlerInitialized endpoints {}", endpoints);
                Optional.ofNullable(endpoints).map(ep -> ep.get(endpointId)).ifPresent(handler::updateEndpoint);
            }
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

    protected MatterWebsocketClient getClient() {
        return client;
    }

    protected void refresh() {
        logger.debug("refresh");
        if (refreshLock.isLocked()) {
            return;
        }
        refreshLock.lock();
        try {
            try {
                var nodeIds = client.getConnectedNodeIds();
                for (String id : nodeIds) {
                    updateNode(id);
                }
            } catch (Exception e) {
                logger.debug("Error communicating with controller", e);
                setOffline(e.getLocalizedMessage());
            }
        } finally {
            refreshLock.unlock();
        }
    }

    protected void endpointRemoved(String nodeId, int endpointId) {
        logger.debug("endpointRemoved endpoint {}:{}", nodeId, endpointId);

        if (!getConfigAs(ControllerConfiguration.class).decommissionNodesOnDelete) {
            return;
        }
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
        if (reconnectFuture != null) {
            reconnectFuture.cancel(true);
        }
        reconnectFuture = scheduler.schedule(this::initialize, 30, TimeUnit.SECONDS);
    }

    private void updateNode(String id) {
        try {
            Node node = client.getNode(id);
            if (node == null) {
                return;
            }
            updateNode(node);
        } catch (Exception e) {
            logger.debug("Could not update node {}", id, e);
            updateEndpointStatuses(id, ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR);
        }
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

    private void updateEndpointStatuses(String nodeId, ThingStatus status, ThingStatusDetail detail) {
        for (Thing thing : getThing().getThings()) {
            ThingHandler handler = thing.getHandler();
            if (handler instanceof EndpointHandler endpointHandler) {
                if (nodeId.equals(endpointHandler.getNodeId())) {
                    endpointHandler.setEndpointStatus(status, detail);
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
                if (nodeId.equals(nodeId) && endpointHandler.getEndpointId() == endpointId) {
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
}
