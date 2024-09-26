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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DataTypes.*;

/**
 * SoftwareDiagnostics
 *
 * @author Dan Cunningham - Initial contribution
 */
public class SoftwareDiagnosticsCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "SoftwareDiagnostics";
    public static final int CLUSTER_ID = 0x0034;

    public Integer clusterRevision; // 65533 ClusterRevision
    public FeatureMap featureMap; // 65532 FeatureMap
    /**
     * The ThreadMetrics attribute shall be a list of ThreadMetricsStruct structs. Each active thread on the Node shall
     * be represented by a single entry within the ThreadMetrics attribute.
     */
    public List<ThreadMetricsStruct> threadMetrics; // 0 list R V
    /**
     * The CurrentHeapFree attribute shall indicate the current amount of heap memory, in bytes, that are free for
     * allocation. The effective amount may be smaller due to heap fragmentation or other reasons.
     */
    public BigInteger currentHeapFree; // 1 uint64 R V
    /**
     * The CurrentHeapUsed attribute shall indicate the current amount of heap memory, in bytes, that is being used.
     */
    public BigInteger currentHeapUsed; // 2 uint64 R V
    /**
     * The CurrentHeapHighWatermark attribute shall indicate the maximum amount of heap memory, in bytes, that has been
     * used by the Node. This value shall only be reset upon a Node reboot or upon receiving of the ResetWatermarks
     * command.
     */
    public BigInteger currentHeapHighWatermark; // 3 uint64 R V
    // Structs

    public class ThreadMetricsStruct {
        /**
         * The Id field shall be a server-assigned per-thread unique ID that is constant for the duration of the thread.
         * Efforts SHOULD be made to avoid reusing ID values when possible.
         */
        public BigInteger id; // uint64
        /**
         * The Name field shall be set to a vendor defined name or prefix of the software thread that is static for the
         * duration of the thread.
         */
        public String name; // string
        /**
         * The StackFreeCurrent field shall indicate the current amount of stack memory, in bytes, that are not being
         * utilized on the respective thread.
         */
        public Integer stackFreeCurrent; // uint32
        /**
         * The StackFreeMinimum field shall indicate the minimum amount of stack memory, in bytes, that has been
         * available at any point between the current time and this attribute being reset or initialized on the
         * respective thread. This value shall only be reset upon a Node reboot or upon receiving of the ResetWatermarks
         * command.
         */
        public Integer stackFreeMinimum; // uint32
        /**
         * The StackSize field shall indicate the amount of stack memory, in bytes, that has been allocated for use by
         * the respective thread.
         */
        public Integer stackSize; // uint32

        public ThreadMetricsStruct(BigInteger id, String name, Integer stackFreeCurrent, Integer stackFreeMinimum,
                Integer stackSize) {
            this.id = id;
            this.name = name;
            this.stackFreeCurrent = stackFreeCurrent;
            this.stackFreeMinimum = stackFreeMinimum;
            this.stackSize = stackSize;
        }
    }

    // Bitmaps
    public static class FeatureMap {
        /**
         * Watermarks
         * Node makes available the metrics for high watermark related to memory consumption.
         */
        public boolean wTRMRK;

        public FeatureMap(boolean wTRMRK) {
            this.wTRMRK = wTRMRK;
        }
    }

    public SoftwareDiagnosticsCluster(BigInteger nodeId, int endpointId) {
        super(nodeId, endpointId, 52, "SoftwareDiagnostics");
    }

    // commands
    /**
     * Receipt of this command shall reset the following values which track high and lower watermarks:
     * • The StackFreeMinimum field of the ThreadMetrics attribute
     * • The CurrentHeapHighWatermark attribute This command has no payload.
     * Effect on Receipt
     * On receipt of this command, the Node shall make the following modifications to attributes it supports:
     * If implemented, the server shall set the value of the CurrentHeapHighWatermark attribute to the value of the
     * CurrentHeapUsed attribute.
     * If implemented, the server shall set the value of the StackFreeMinimum field for every thread to the value of the
     * corresponding thread’s StackFreeCurrent field.
     */
    public static ClusterCommand resetWatermarks() {
        Map<String, Object> map = new LinkedHashMap<>();

        return new ClusterCommand("resetWatermarks");
    }

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        str += "featureMap : " + featureMap + "\n";
        str += "threadMetrics : " + threadMetrics + "\n";
        str += "currentHeapFree : " + currentHeapFree + "\n";
        str += "currentHeapUsed : " + currentHeapUsed + "\n";
        str += "currentHeapHighWatermark : " + currentHeapHighWatermark + "\n";
        return str;
    }
}
