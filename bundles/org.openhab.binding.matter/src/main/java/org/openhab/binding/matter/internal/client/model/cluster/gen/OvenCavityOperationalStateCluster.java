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

// AUTO-GENERATED, DO NOT EDIT!

package org.openhab.binding.matter.internal.client.model.cluster.gen;

import java.math.BigInteger;

import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DataTypes.*;

/**
 * OvenCavityOperationalState
 *
 * @author Dan Cunningham - Initial contribution
 */
public class OvenCavityOperationalStateCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "OvenCavityOperationalState";
    public static final int CLUSTER_ID = 0x0048;

    public Integer clusterRevision; // 65533 ClusterRevision

    public OvenCavityOperationalStateCluster(BigInteger nodeId, int endpointId) {
        super(nodeId, endpointId, 72, "OvenCavityOperationalState");
    }

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        return str;
    }
}
