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

// 

package org.openhab.binding.matter.internal.client.model.cluster.gen;

import org.openhab.binding.matter.internal.client.model.cluster.types.*;

/**
 * AUTO-GENERATED by zap. DO NOT EDIT!
 *
 * AirQuality
 *
 * @author Dan Cunningham - Initial contribution
 */
public class AirQualityClusterTypes {

    public static final String CLUSTER_NAME = "AIR_QUALITY_CLUSTER";
    public static final int CLUSTER_ID = 0x005B;

    // ZCL Enums
    public enum AirQualityEnum {
        UNKNOWN(0, "Unknown"),
        GOOD(1, "Good"),
        FAIR(2, "Fair"),
        MODERATE(3, "Moderate"),
        POOR(4, "Poor"),
        VERY_POOR(5, "Very poor"),
        EXTREMELY_POOR(6, "Extremely poor"),
        UNKNOWN_VALUE(7, "UnknownValue");

        public final int value;
        public final String label;

        private AirQualityEnum(int value, String label) {
            this.value = value;
            this.label = label;
        }
    };

    // ZCL Bitmaps
    public static class Feature {
        public boolean fair;
        public boolean moderate;
        public boolean veryPoor;
        public boolean extremelyPoor;

        public Feature(boolean fair, boolean moderate, boolean veryPoor, boolean extremelyPoor) {
            this.fair = fair;
            this.moderate = moderate;
            this.veryPoor = veryPoor;
            this.extremelyPoor = extremelyPoor;
        }
    }
}
