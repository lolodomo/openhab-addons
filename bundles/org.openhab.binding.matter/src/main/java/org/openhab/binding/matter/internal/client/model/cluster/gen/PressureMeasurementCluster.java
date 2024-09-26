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
 * PressureMeasurement
 *
 * @author Dan Cunningham - Initial contribution
 */
public class PressureMeasurementCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "PressureMeasurement";
    public static final int CLUSTER_ID = 0x0403;

    public Integer clusterRevision; // 65533 ClusterRevision
    public FeatureMap featureMap; // 65532 FeatureMap
    /**
     * Indicates the pressure in kPa as follows:
     * MeasuredValue &#x3D; 10 x Pressure [kPa]
     * The null value indicates that the value is not available.
     */
    public Integer measuredValue; // 0 int16 R V
    /**
     * Indicates the minimum value of MeasuredValue that can be measured. See Measured Value for more details.
     * The null value indicates that the value is not available.
     */
    public Integer minMeasuredValue; // 1 int16 R V
    /**
     * Indicates the maximum value of MeasuredValue that can be measured. See Measured Value for more details.
     * The null value indicates that the value is not available.
     */
    public Integer maxMeasuredValue; // 2 int16 R V
    /**
     * See Measured Value.
     */
    public Integer tolerance; // 3 uint16 R V
    /**
     * Indicates the pressure in Pascals as follows:
     * ScaledValue &#x3D; 10Scale x Pressure [Pa]
     * The null value indicates that the value is not available.
     */
    public Integer scaledValue; // 16 int16 R V
    /**
     * Indicates the minimum value of ScaledValue that can be measured. The null value indicates that the value is not
     * available.
     */
    public Integer minScaledValue; // 17 int16 R V
    /**
     * Indicates the maximum value of ScaledValue that can be measured. The null value indicates that the value is not
     * available.
     */
    public Integer maxScaledValue; // 18 int16 R V
    /**
     * Indicates the magnitude of the possible error that is associated with Scaled
     * Value. The true value is located in the range
     * (ScaledValue – ScaledTolerance) to (ScaledValue + ScaledTolerance).
     */
    public Integer scaledTolerance; // 19 uint16 R V
    /**
     * Indicates the base 10 exponent used to obtain ScaledValue (see ScaledValue).
     */
    public Integer scale; // 20 int8 R V

    // Bitmaps
    public static class FeatureMap {
        /**
         * Extended
         * Extended range and resolution
         */
        public boolean eXT;

        public FeatureMap(boolean eXT) {
            this.eXT = eXT;
        }
    }

    public PressureMeasurementCluster(BigInteger nodeId, int endpointId) {
        super(nodeId, endpointId, 1027, "PressureMeasurement");
    }

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        str += "featureMap : " + featureMap + "\n";
        str += "measuredValue : " + measuredValue + "\n";
        str += "minMeasuredValue : " + minMeasuredValue + "\n";
        str += "maxMeasuredValue : " + maxMeasuredValue + "\n";
        str += "tolerance : " + tolerance + "\n";
        str += "scaledValue : " + scaledValue + "\n";
        str += "minScaledValue : " + minScaledValue + "\n";
        str += "maxScaledValue : " + maxScaledValue + "\n";
        str += "scaledTolerance : " + scaledTolerance + "\n";
        str += "scale : " + scale + "\n";
        return str;
    }
}
