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

// AUTO-GENERATED by zap. DO NOT EDIT!

package org.openhab.binding.matter.internal.client.model.cluster.gen;

import java.util.Map;
import java.util.List;

import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.PressureMeasurementClusterTypes.*;

/**
 * PressureMeasurement
 *
 * @author Dan Cunningham - Initial contribution
 */
public class PressureMeasurementCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "PressureMeasurement";
    public static final int CLUSTER_ID = 0x0403;

    public Integer measuredValue; // 0 int16s reportable 
    public Integer minMeasuredValue; // 1 int16s reportable 
    public Integer maxMeasuredValue; // 2 int16s reportable 
    public Integer tolerance; // 3 int16u reportable 
    public Integer scaledValue; // 16 int16s reportable 
    public Integer minScaledValue; // 17 int16s reportable 
    public Integer maxScaledValue; // 18 int16s reportable 
    public Integer scaledTolerance; // 19 int16u reportable 
    public Integer scale; // 20 int8s reportable 
    public List<Integer> generatedCommandList; // 65528 command_id reportable 
    public List<Integer> acceptedCommandList; // 65529 command_id reportable 
    public List<Integer> eventList; // 65530 event_id reportable 
    public List<Integer> attributeList; // 65531 attrib_id reportable 
    public Map<String, Boolean> featureMap; // 65532 bitmap32 reportable 
    public Integer clusterRevision; // 65533 int16u reportable 

    public PressureMeasurementCluster(String nodeId, int endpointId) {
        super(nodeId, endpointId, 56, "PressureMeasurement");
    }

    public String toString() {
        String str = "";
        str += "measuredValue : " + measuredValue + "\n";
        str += "minMeasuredValue : " + minMeasuredValue + "\n";
        str += "maxMeasuredValue : " + maxMeasuredValue + "\n";
        str += "tolerance : " + tolerance + "\n";
        str += "scaledValue : " + scaledValue + "\n";
        str += "minScaledValue : " + minScaledValue + "\n";
        str += "maxScaledValue : " + maxScaledValue + "\n";
        str += "scaledTolerance : " + scaledTolerance + "\n";
        str += "scale : " + scale + "\n";
        str += "generatedCommandList : " + generatedCommandList + "\n";
        str += "acceptedCommandList : " + acceptedCommandList + "\n";
        str += "eventList : " + eventList + "\n";
        str += "attributeList : " + attributeList + "\n";
        str += "featureMap : " + featureMap + "\n";
        str += "clusterRevision : " + clusterRevision + "\n";
        return str;
    }
}
