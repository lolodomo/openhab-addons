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
import java.util.Map;

import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DataTypes.*;

/**
 * BooleanStateConfiguration
 *
 * @author Dan Cunningham - Initial contribution
 */
public class BooleanStateConfigurationCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "BooleanStateConfiguration";
    public static final int CLUSTER_ID = 0x0080;

    public Integer clusterRevision; // 65533 ClusterRevision
    public FeatureMap featureMap; // 65532 FeatureMap
    /**
     * Indicates the currently selected sensitivity level.
     * If a write interaction to this attribute contains an unsupported sensitivity value, a CONSTRAINT_ERROR status
     * shall be returned.
     */
    public Integer currentSensitivityLevel; // 0 uint8 RW VO
    /**
     * Indicates the number of supported sensitivity levels by the device.
     * These supported sensitivity levels shall be ordered by sensitivity, where a value of 0 shall be considered the
     * lowest sensitivity level (least sensitive) and the highest supported value shall be considered the highest
     * sensitivity level.
     * The number of supported sensitivity levels SHOULD represent unique sensitivity levels supported by the device.
     */
    public Integer supportedSensitivityLevels; // 1 uint8 R V
    /**
     * Indicates the default sensitivity level selected by the manufacturer.
     */
    public Integer defaultSensitivityLevel; // 2 uint8 R V
    /**
     * Indicates which specific alarm modes on the server are currently active. When the sensor is no longer triggered,
     * this attribute shall be set to the inactive state, by setting the bit to 0, for all supported alarm modes.
     * If an alarm mode is not supported, the bit indicating this alarm mode shall always be 0. A bit shall indicate
     * whether the alarm mode inactive or not:
     * • 0 &#x3D; Inactive
     * • 1 &#x3D; Active
     */
    public AlarmModeBitmap alarmsActive; // 3 AlarmModeBitmap R V
    /**
     * Indicates which specific alarm modes on the server are currently suppressed. When the sensor is no longer
     * triggered, this attribute shall be set to the unsuppressed state, by setting the bit to 0, for all supported
     * alarm modes.
     * If an alarm mode is not supported, the bit indicating this alarm mode shall always be 0. A bit shall indicate
     * whether the alarm mode is suppressed or not:
     * • 0 &#x3D; Not suppressed
     * • 1 &#x3D; Suppressed
     */
    public AlarmModeBitmap alarmsSuppressed; // 4 AlarmModeBitmap R V
    /**
     * Indicates the alarm modes that will be emitted if the sensor is triggered. If an alarm mode is not supported, the
     * bit indicating this alarm mode shall always be 0.
     * A bit shall indicate whether the alarm mode is enabled or disabled:
     * • 0 &#x3D; Disabled
     * • 1 &#x3D; Enabled
     */
    public AlarmModeBitmap alarmsEnabled; // 5 AlarmModeBitmap R V
    /**
     * Indicates the alarms supported by the sensor. A bit shall indicate whether the alarm mode is supported:
     * • 0 &#x3D; Not supported
     * • 1 &#x3D; Supported
     */
    public AlarmModeBitmap alarmsSupported; // 6 AlarmModeBitmap R V
    /**
     * Indicates any faults registered by the device.
     */
    public SensorFaultBitmap sensorFault; // 7 SensorFaultBitmap R V

    // Bitmaps
    public static class AlarmModeBitmap {
        public boolean visual;
        public boolean audible;

        public AlarmModeBitmap(boolean visual, boolean audible) {
            this.visual = visual;
            this.audible = audible;
        }
    }

    public static class SensorFaultBitmap {
        public boolean generalFault;

        public SensorFaultBitmap(boolean generalFault) {
            this.generalFault = generalFault;
        }
    }

    public static class FeatureMap {
        /**
         * Visual
         * Supports visual alarms
         */
        public boolean vIS;
        /**
         * Audible
         * Supports audible alarms
         */
        public boolean aUD;
        /**
         * AlarmSuppress
         * This feature shall indicate that the device is able to suppress the supported alarm modes, when the user
         * acknowledges the alarm. This is intended to stop visual and/or audible alarms, when the user has become aware
         * that the sensor is triggered, but it is no longer desired to have the alarm modes active on the device, e.g.:
         * • The triggering cause have been resolved by the user, but the sensor has not yet stopped detecting the
         * triggering cause.
         * • The user is not able to address the triggering cause, but is aware of the alarm and suppress/acknowledge it
         * be addressed at a later point.
         * Acknowledge of alarms will for the remainder of this cluster be referred to as suppress.
         * A suppressed alarm is still considered active and will remain so unless it is actively disabled or the
         * triggering condition is not longer present. The action of suppressing an alarm mode is only applicable to and
         * is intended to stop the physical alarming, e.g. emitting a sound or blinking a light; it does not impact
         * alarm reporting in AlarmsActive.
         */
        public boolean sPRS;
        /**
         * SensitivityLevel
         * Supports ability to set sensor sensitivity
         */
        public boolean sENSLVL;

        public FeatureMap(boolean vIS, boolean aUD, boolean sPRS, boolean sENSLVL) {
            this.vIS = vIS;
            this.aUD = aUD;
            this.sPRS = sPRS;
            this.sENSLVL = sENSLVL;
        }
    }

    public BooleanStateConfigurationCluster(BigInteger nodeId, int endpointId) {
        super(nodeId, endpointId, 128, "BooleanStateConfiguration");
    }

    // commands
    public static ClusterCommand suppressAlarm(AlarmModeBitmap alarmsToSuppress) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("alarmsToSuppress", alarmsToSuppress);

        return new ClusterCommand("suppressAlarm", map);
    }

    public static ClusterCommand enableDisableAlarm(AlarmModeBitmap alarmsToEnableDisable) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("alarmsToEnableDisable", alarmsToEnableDisable);

        return new ClusterCommand("enableDisableAlarm", map);
    }

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        str += "featureMap : " + featureMap + "\n";
        str += "currentSensitivityLevel : " + currentSensitivityLevel + "\n";
        str += "supportedSensitivityLevels : " + supportedSensitivityLevels + "\n";
        str += "defaultSensitivityLevel : " + defaultSensitivityLevel + "\n";
        str += "alarmsActive : " + alarmsActive + "\n";
        str += "alarmsSuppressed : " + alarmsSuppressed + "\n";
        str += "alarmsEnabled : " + alarmsEnabled + "\n";
        str += "alarmsSupported : " + alarmsSupported + "\n";
        str += "sensorFault : " + sensorFault + "\n";
        return str;
    }
}
