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
 * MicrowaveOvenMode
 *
 * @author Dan Cunningham - Initial contribution
 */
public class MicrowaveOvenModeCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "MicrowaveOvenMode";
    public static final int CLUSTER_ID = 0x005E;

    public Integer clusterRevision; // 65533 ClusterRevision
    public String supportedModes; // 0
    public String currentMode; // 1

    // Enums
    public enum ModeTag {
        NORMAL(16384, "Normal"),
        DEFROST(16385, "Defrost");

        public final Integer value;
        public final String label;

        private ModeTag(Integer value, String label) {
            this.value = value;
            this.label = label;
        }
    }

    public MicrowaveOvenModeCluster(BigInteger nodeId, int endpointId) {
        super(nodeId, endpointId, 94, "MicrowaveOvenMode");
    }

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        str += "supportedModes : " + supportedModes + "\n";
        str += "currentMode : " + currentMode + "\n";
        return str;
    }
}
