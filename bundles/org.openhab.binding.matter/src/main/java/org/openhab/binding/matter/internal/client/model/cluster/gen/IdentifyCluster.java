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

import java.util.List;
import java.util.Map;

import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.IdentifyClusterTypes.*;

/**
 * Identify
 *
 * @author Dan Cunningham - Initial contribution
 */
public class IdentifyCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "Identify";
    public static final int CLUSTER_ID = 0x0003;

    public Integer identifyTime; // 0 int16u reportable writable
    public IdentifyTypeEnum identifyType; // 1 IdentifyTypeEnum reportable
    public List<Integer> generatedCommandList; // 65528 command_id reportable
    public List<Integer> acceptedCommandList; // 65529 command_id reportable
    public List<Integer> eventList; // 65530 event_id reportable
    public List<Integer> attributeList; // 65531 attrib_id reportable
    public Map<String, Boolean> featureMap; // 65532 bitmap32 reportable
    public Integer clusterRevision; // 65533 int16u reportable

    public IdentifyCluster(String nodeId, int endpointId) {
        super(nodeId, endpointId, 33, "Identify");
    }

    public String toString() {
        String str = "";
        str += "identifyTime : " + identifyTime + "\n";
        str += "identifyType : " + identifyType + "\n";
        str += "generatedCommandList : " + generatedCommandList + "\n";
        str += "acceptedCommandList : " + acceptedCommandList + "\n";
        str += "eventList : " + eventList + "\n";
        str += "attributeList : " + attributeList + "\n";
        str += "featureMap : " + featureMap + "\n";
        str += "clusterRevision : " + clusterRevision + "\n";
        return str;
    }
}
