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
 * RvcRunMode
 *
 * @author Dan Cunningham - Initial contribution
 */
public class RvcRunModeCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "RvcRunMode";
    public static final int CLUSTER_ID = 0x0054;

    public Integer clusterRevision; // 65533 ClusterRevision 
    public String supportedModes; // 0  
    public String currentMode; // 1  


    //Enums
    public enum ModeChangeStatus {
        STUCK(65, "Stuck"),
        DUST_BIN_MISSING(66, "DustBinMissing"),
        DUST_BIN_FULL(67, "DustBinFull"),
        WATER_TANK_EMPTY(68, "WaterTankEmpty"),
        WATER_TANK_MISSING(69, "WaterTankMissing"),
        WATER_TANK_LID_OPEN(70, "WaterTankLidOpen"),
        MOP_CLEANING_PAD_MISSING(71, "MopCleaningPadMissing"),
        BATTERY_LOW(72, "BatteryLow");
        public final Integer value;
        public final String label;
        private ModeChangeStatus(Integer value, String label){
            this.value = value;
            this.label = label;
        }
    }
    public enum ModeTag {
        IDLE(16384, "Idle"),
        CLEANING(16385, "Cleaning"),
        MAPPING(16386, "Mapping");
        public final Integer value;
        public final String label;
        private ModeTag(Integer value, String label){
            this.value = value;
            this.label = label;
        }
    }


    public RvcRunModeCluster(String nodeId, int endpointId) {
        super(nodeId, endpointId, 84, "RvcRunMode");
    }

    

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        str += "supportedModes : " + supportedModes + "\n";
        str += "currentMode : " + currentMode + "\n";
        return str;
    }
}
