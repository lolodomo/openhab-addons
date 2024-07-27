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
import org.openhab.binding.matter.internal.client.model.cluster.gen.FanControlClusterTypes.*;

/**
 * FanControl
 *
 * @author Dan Cunningham - Initial contribution
 */
public class FanControlCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "FanControl";
    public static final int CLUSTER_ID = 0x0202;

    public FanModeEnum fanMode; // 0 FanModeEnum reportable writable
    public FanModeSequenceEnum fanModeSequence; // 1 FanModeSequenceEnum reportable
    public Integer percentSetting; // 2 percent reportable writable
    public Integer percentCurrent; // 3 percent reportable
    public Integer speedMax; // 4 int8u reportable
    public Integer speedSetting; // 5 int8u reportable writable
    public Integer speedCurrent; // 6 int8u reportable
    public RockBitmap rockSupport; // 7 RockBitmap reportable
    public RockBitmap rockSetting; // 8 RockBitmap reportable writable
    public WindBitmap windSupport; // 9 WindBitmap reportable
    public WindBitmap windSetting; // 10 WindBitmap reportable writable
    public AirflowDirectionEnum airflowDirection; // 11 AirflowDirectionEnum reportable writable
    public List<Integer> generatedCommandList; // 65528 command_id reportable
    public List<Integer> acceptedCommandList; // 65529 command_id reportable
    public List<Integer> eventList; // 65530 event_id reportable
    public List<Integer> attributeList; // 65531 attrib_id reportable
    public Map<String, Boolean> featureMap; // 65532 bitmap32 reportable
    public Integer clusterRevision; // 65533 int16u reportable

    public FanControlCluster(String nodeId, int endpointId) {
        super(nodeId, endpointId, 30, "FanControl");
    }

    public String toString() {
        String str = "";
        str += "fanMode : " + fanMode + "\n";
        str += "fanModeSequence : " + fanModeSequence + "\n";
        str += "percentSetting : " + percentSetting + "\n";
        str += "percentCurrent : " + percentCurrent + "\n";
        str += "speedMax : " + speedMax + "\n";
        str += "speedSetting : " + speedSetting + "\n";
        str += "speedCurrent : " + speedCurrent + "\n";
        str += "rockSupport : " + rockSupport + "\n";
        str += "rockSetting : " + rockSetting + "\n";
        str += "windSupport : " + windSupport + "\n";
        str += "windSetting : " + windSetting + "\n";
        str += "airflowDirection : " + airflowDirection + "\n";
        str += "generatedCommandList : " + generatedCommandList + "\n";
        str += "acceptedCommandList : " + acceptedCommandList + "\n";
        str += "eventList : " + eventList + "\n";
        str += "attributeList : " + attributeList + "\n";
        str += "featureMap : " + featureMap + "\n";
        str += "clusterRevision : " + clusterRevision + "\n";
        return str;
    }
}
