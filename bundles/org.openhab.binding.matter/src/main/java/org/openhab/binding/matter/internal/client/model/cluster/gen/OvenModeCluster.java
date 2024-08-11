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

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DataTypes.*;

/**
 * OvenMode
 *
 * @author Dan Cunningham - Initial contribution
 */
public class OvenModeCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "OvenMode";
    public static final int CLUSTER_ID = 0x0049;

    public Integer clusterRevision; // 65533 ClusterRevision 


    //Enums
    public enum ModeTag {
        BAKE(16384, "Bake"),
        CONVECTION(16385, "Convection"),
        GRILL(16386, "Grill"),
        ROAST(16387, "Roast"),
        CLEAN(16388, "Clean"),
        CONVECTION_BAKE(16389, "ConvectionBake"),
        CONVECTION_ROAST(16390, "ConvectionRoast"),
        WARMING(16391, "Warming"),
        PROOFING(16392, "Proofing"),
        STEAM(16393, "Steam");
        public final Integer value;
        public final String label;
        private ModeTag(Integer value, String label){
            this.value = value;
            this.label = label;
        }
    }


    public OvenModeCluster(String nodeId, int endpointId) {
        super(nodeId, endpointId, 73, "OvenMode");
    }

    

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        return str;
    }
}
