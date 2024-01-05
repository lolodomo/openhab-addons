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
}
