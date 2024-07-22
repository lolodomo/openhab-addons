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
package org.openhab.binding.matter.internal.client.model.cluster;

import java.util.Map;

import com.google.gson.Gson;

/**
 * @author Dan Cunningham
 *
 */
public abstract class BaseCluster {
    protected static final Gson GSON = new Gson();
    public String nodeId;
    public int endpointId;
    public int id;
    public String name;
    public static Map<Integer, String> ATTRIBUTE_MAPPING;
    public static Map<Integer, String> COMMAND_MAPPING;

    public BaseCluster(String nodeId, int endpointId, int clusterId, String clusterName) {
        this.nodeId = nodeId;
        this.endpointId = endpointId;
        this.id = clusterId;
        this.name = clusterName;
    }
}
