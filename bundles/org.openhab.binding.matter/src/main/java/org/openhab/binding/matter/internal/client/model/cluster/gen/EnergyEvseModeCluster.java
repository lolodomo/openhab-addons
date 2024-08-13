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
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DataTypes.*;

/**
 * EnergyEvseMode
 *
 * @author Dan Cunningham - Initial contribution
 */
public class EnergyEvseModeCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "EnergyEvseMode";
    public static final int CLUSTER_ID = 0x009D;

    public Integer clusterRevision; // 65533 ClusterRevision 


    //Enums
    public enum ModeTag {
        MANUAL(16384, "Manual"),
        TIME_OF_USE(16385, "TimeOfUse"),
        SOLAR_CHARGING(16386, "SolarCharging");
        public final Integer value;
        public final String label;
        private ModeTag(Integer value, String label){
            this.value = value;
            this.label = label;
        }
    }


    public EnergyEvseModeCluster(BigInteger nodeId, int endpointId) {
        super(nodeId, endpointId, 157, "EnergyEvseMode");
    }

    

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        return str;
    }
}
