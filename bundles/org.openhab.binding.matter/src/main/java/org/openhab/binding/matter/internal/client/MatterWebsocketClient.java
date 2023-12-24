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
package org.openhab.binding.matter.internal.client;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author Dan Cunningham
 *
 */
public abstract class MatterWebsocketClient implements WebSocketListener {

    private final Logger logger = LoggerFactory.getLogger(MatterWebsocketClient.class);

    protected final Gson gson = new Gson();
    private Session session;
    private final ConcurrentHashMap<String, CompletableFuture<JsonElement>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<AttributeListener, String> attributeListeners = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<NodeStateListener, Long> nodeStateListeners = new ConcurrentHashMap<>();

    static class Request {
        public Request(String requestId, String namespace, String function, Object args[]) {
            this.id = requestId;
            this.namespace = namespace;
            this.function = function;
            this.args = args;
        }

        String id;
        String namespace;
        String function;
        Object args[];
        // Constructor, getters and setters
    }

    static class Response {
        String type;
        String id;
        JsonElement result;
        String error;
    }

    static class Event {
        String type;
        JsonElement data;
    }

    static class Message {
        String type;
        JsonObject message;
    }

    public static class AttributeChangedMessage {
        public Path path;
        public Long version;
        public String value;
    }

    public static class NodeStateMessage {
        public Long nodeId;
        public NodeState nodeState;
    }

    public enum NodeState {
        /** Node is connected and all data is up-to-date. */
        CONNECTED("Connected"),

        /**
         * Node is disconnected. Data are stale and interactions will most likely return an error. If controller
         * instance
         * is still active then the device will be reconnected once it is available again.
         */
        DISCONNECTED("Disconnected"),

        /** Node is reconnecting. Data are stale. It is yet unknown if the reconnection is successful. */
        RECONNECTING("Reconnecting"),

        /**
         * The node could not be connected and the controller is now waiting for a MDNS announcement and tries every 10
         * minutes to reconnect.
         */
        WAITINGFORDEVICEDISCOVERY("WaitingForDeviceDiscovery"),

        /**
         * Node structure has changed (Endpoints got added or also removed). Data are up-to-date.
         * This State information will only be fired when the subscribeAllAttributesAndEvents option is set to true.
         */
        STRUCTURECHANGED("StructureChanged"),

        /**
         * The node was just Decommissioned.
         */
        DECOMMISSIONED("Decommissioned");

        private String state;

        NodeState(String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return state;
        }
    }

    public static class Path {
        public Long nodeId;
        public Integer endpointId;
        public Integer clusterId;
        public Integer attributeId;
        public String attributeName;
    }

    public void connect(String host, int port) throws Exception {
        logger.debug("Connecting {}:{} ", host, port);
        String dest = "ws://" + host + ":" + port + "?nodeId=0&storagePath=/Users/daniel/tmp/matter-server";
        WebSocketClient client = new WebSocketClient();
        client.setMaxIdleTimeout(Long.MAX_VALUE);
        client.start();
        URI uri = new URI(dest);
        client.connect(this, uri, new ClientUpgradeRequest()).get();
    }

    public void disconnect() {
        if (session != null) {
            session.close();
        }
    }

    public void addAttributeListener(AttributeListener listener, Long nodeId, int endpointId, int clusterId) {
        attributeListeners.put(listener, attributeListenerId(nodeId, endpointId, clusterId));
    }

    public void addAttributeListener(AttributeListener listener) {
        attributeListeners.put(listener, "");
    }

    public void removeAttributeListener(AttributeListener listener) {
        attributeListeners.remove(listener);
    }

    public void addNodeStateListener(NodeStateListener listener, Long nodeId) {
        nodeStateListeners.put(listener, nodeId);
    }

    public void removeNodeStateListener(NodeStateListener listener) {
        nodeStateListeners.remove(listener);
    }

    protected CompletableFuture<JsonElement> sendMessage(String namespace, String functionName, Object args[]) {
        String requestId = UUID.randomUUID().toString();
        CompletableFuture<JsonElement> responseFuture = new CompletableFuture<>();
        pendingRequests.put(requestId, responseFuture);
        Request message = new Request(requestId, namespace, functionName, args);
        String jsonMessage = gson.toJson(message);
        logger.debug("sendMessage: {}", jsonMessage);
        session.getRemote().sendStringByFuture(jsonMessage);

        return responseFuture;
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
    }

    @Override
    public void onWebSocketText(String msg) {
        logger.debug("onWebSocketText {}", msg);
        Message message = gson.fromJson(msg, Message.class);
        if (message.type.equals("response")) {
            Response response = gson.fromJson(message.message, Response.class);
            CompletableFuture<JsonElement> future = pendingRequests.remove(response.id);
            if (future != null) {
                logger.debug("result type: {} ", response.type);
                if (!"resultSuccess".equals(response.type)) {
                    future.completeExceptionally(new Exception(response.error));
                } else {
                    future.complete(response.result);
                }
            }
        } else if (message.type.equals("event")) {
            Event event = gson.fromJson(message.message, Event.class);
            switch (event.type) {
                case "attributeChanged":
                    AttributeChangedMessage changedMessage = gson.fromJson(event.data, AttributeChangedMessage.class);
                    String id = attributeListenerId(changedMessage.path.nodeId, changedMessage.path.endpointId,
                            changedMessage.path.clusterId);
                    for (Map.Entry<AttributeListener, String> entry : attributeListeners.entrySet()) {
                        String value = entry.getValue();
                        if (value.length() == 0 || value.equals(id)) {
                            entry.getKey().onEvent(changedMessage);
                        }
                    }
                    break;
                case "nodeStateInformation":
                    NodeStateMessage nodeStateMessage = gson.fromJson(event.data, NodeStateMessage.class);
                    for (Map.Entry<NodeStateListener, Long> entry : nodeStateListeners.entrySet()) {
                        Long value = entry.getValue();
                        if (value.equals(nodeStateMessage.nodeId)) {
                            entry.getKey().onEvent(nodeStateMessage);
                        }
                    }
            }
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        logger.debug("onWebSocketClose {} {}", statusCode, reason);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        logger.debug("onWebSocketError {}", cause);
    }

    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }

    private String attributeListenerId(Long nodeId, int endpointId, int clusterId) {
        return nodeId + ":" + endpointId + ":" + clusterId;
    }
}
