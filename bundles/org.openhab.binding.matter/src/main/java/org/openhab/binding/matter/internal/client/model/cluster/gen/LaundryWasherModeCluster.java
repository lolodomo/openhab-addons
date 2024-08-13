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
 * LaundryWasherMode
 *
 * @author Dan Cunningham - Initial contribution
 */
public class LaundryWasherModeCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "LaundryWasherMode";
    public static final int CLUSTER_ID = 0x0051;

    public Integer clusterRevision; // 65533 ClusterRevision 
    public String supportedModes; // 0  
    public String currentMode; // 1  
    /**
    * If this attribute is supported, the device SHOULD initially set this to one of the supported modes that has the Normal tag associated with it. See the Mode Base cluster specification for full details about the StartUpMode attribute.
    */
    public String startUpMode; // 2  
    public String onMode; // 3  


    //Enums
    public enum ModeTag {
        NORMAL(16384, "Normal"),
        DELICATE(16385, "Delicate"),
        HEAVY(16386, "Heavy"),
        WHITES(16387, "Whites");
        public final Integer value;
        public final String label;
        private ModeTag(Integer value, String label){
            this.value = value;
            this.label = label;
        }
    }


    public LaundryWasherModeCluster(BigInteger nodeId, int endpointId) {
        super(nodeId, endpointId, 81, "LaundryWasherMode");
    }

    

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        str += "supportedModes : " + supportedModes + "\n";
        str += "currentMode : " + currentMode + "\n";
        str += "startUpMode : " + startUpMode + "\n";
        str += "onMode : " + onMode + "\n";
        return str;
    }
}
