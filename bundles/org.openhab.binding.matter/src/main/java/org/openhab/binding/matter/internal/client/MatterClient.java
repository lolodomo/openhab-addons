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

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.openhab.binding.matter.internal.client.model.Endpoint;
import org.openhab.binding.matter.internal.client.model.Node;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

/**
 * @author Dan Cunningham
 *
 */
public class MatterClient extends MatterWebsocketClient {
    private final Logger logger = LoggerFactory.getLogger(MatterClient.class);

    Gson gson = new GsonBuilder().registerTypeAdapter(Node.class, new NodeDeserializer()).create();

    public Map<String, Node> getNodes() throws Exception {
        CompletableFuture<JsonElement> future = sendMessage("nodes", "getNodes", null);
        JsonElement obj = future.get();
        Map<String, Node> nodes = gson.fromJson(obj, new TypeToken<Map<String, Node>>() {
        }.getType());

        return nodes;
    }

    public void pairNode(String code) throws Exception {
        CompletableFuture<JsonElement> future = sendMessage("nodes", "pair", new Object[] { code });
        future.get();
    }

    public void clusterCommand(String clusterName, String functionName, Object arg) throws Exception {
        CompletableFuture<JsonElement> future = sendMessage("clusters", "command", new Object[] { arg });
        future.get();
    }

    public class NodeDeserializer implements JsonDeserializer<Node> {
        @Override
        public Node deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObjectNode = json.getAsJsonObject();
            Node node = new Node();
            node.id = jsonObjectNode.get("id").getAsLong();
            node.endpoints = new HashMap<>();
            JsonObject endpointsJson = jsonObjectNode.get("endpoints").getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> endpointEntries = endpointsJson.entrySet();
            for (Map.Entry<String, JsonElement> endpointEntry : endpointEntries) {
                JsonObject jsonObjectElement = endpointEntry.getValue().getAsJsonObject();
                Endpoint endpoint = new Endpoint();
                endpoint.id = jsonObjectElement.get("id").getAsInt();
                endpoint.clusters = new HashMap<>();
                JsonObject clustersJson = jsonObjectElement.get("clusters").getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> clusterEntries = clustersJson.entrySet();
                for (Map.Entry<String, JsonElement> clusterEntry : clusterEntries) {
                    String clusterName = clusterEntry.getKey();
                    JsonElement clusterElement = clusterEntry.getValue();

                    try {
                        Class<?> clazz = Class
                                .forName(BaseCluster.class.getPackageName() + "." + clusterName + "Cluster");
                        if (BaseCluster.class.isAssignableFrom(clazz)) {
                            // Constructor<?> ctor = clazz.getConstructor(long.class, int.class);
                            // BaseCluster cluster = (BaseCluster) ctor.newInstance(node.id, endpoint.id);
                            BaseCluster cluster = context.deserialize(clusterElement, clazz);
                            for (Map.Entry<String, JsonElement> entry : clusterElement.getAsJsonObject().entrySet()) {
                                Field field;
                                try {
                                    field = getField(clazz, entry.getKey());
                                } catch (NoSuchFieldException e) {
                                    logger.debug("Skipping field {}", entry.getKey());
                                    continue; // Skip if the field is not found
                                }
                                field.setAccessible(true);
                                field.set(cluster, gson.fromJson(entry.getValue(), field.getType()));
                            }
                            endpoint.clusters.put(clusterName, cluster);
                        }
                    } catch (ClassNotFoundException e) {
                        logger.debug("Cluster not found: {} ", clusterName);
                    } catch (IllegalArgumentException | SecurityException | IllegalAccessException e) {
                        logger.debug("Exception for cluster " + clusterName, e);
                    }
                }
                node.endpoints.put(endpoint.id, endpoint);
            }

            return node;
        }

        private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                Class<?> superClass = clazz.getSuperclass();
                if (superClass == null) {
                    throw e;
                } else {
                    return getField(superClass, fieldName);
                }
            }
        }
    }
}
