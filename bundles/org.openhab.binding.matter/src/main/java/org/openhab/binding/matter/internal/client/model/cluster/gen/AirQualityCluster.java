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
 * AirQuality
 *
 * @author Dan Cunningham - Initial contribution
 */
public class AirQualityCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "AirQuality";
    public static final int CLUSTER_ID = 0x005B;

    public Integer clusterRevision; // 65533 ClusterRevision 
    public FeatureMap featureMap; // 65532 FeatureMap 
    /**
    * Indicates a value from AirQualityEnum that is indicative of the currently measured air quality.
    */
    public AirQualityEnum airQuality; // 0 AirQualityEnum R V


    //Enums
    /**
    * The AirQualityEnum provides a representation of the quality of the analyzed air. It is up to the device manufacturer to determine the mapping between the measured values and their corresponding enumeration values.
    */
    public enum AirQualityEnum {
        UNKNOWN(0, "Unknown"),
        GOOD(1, "Good"),
        FAIR(2, "Fair"),
        MODERATE(3, "Moderate"),
        POOR(4, "Poor"),
        VERY_POOR(5, "VeryPoor"),
        EXTREMELY_POOR(6, "ExtremelyPoor");
        public final Integer value;
        public final String label;
        private AirQualityEnum(Integer value, String label){
            this.value = value;
            this.label = label;
        }
    }

    // Bitmaps
    public static class FeatureMap {
        /**
        * Cluster supports the Fair air quality level
        */
        public boolean fAIR;
        /**
        * Cluster supports the Moderate air quality level
        */
        public boolean mOD;
        /**
        * Cluster supports the Very poor air quality level
        */
        public boolean vPOOR;
        /**
        * Cluster supports the Extremely poor air quality level
        */
        public boolean xPOOR;
        public FeatureMap(boolean fAIR, boolean mOD, boolean vPOOR, boolean xPOOR){
            this.fAIR = fAIR;
            this.mOD = mOD;
            this.vPOOR = vPOOR;
            this.xPOOR = xPOOR;
        }
    }

    public AirQualityCluster(String nodeId, int endpointId) {
        super(nodeId, endpointId, 91, "AirQuality");
    }

    

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        str += "featureMap : " + featureMap + "\n";
        str += "airQuality : " + airQuality + "\n";
        return str;
    }
}
