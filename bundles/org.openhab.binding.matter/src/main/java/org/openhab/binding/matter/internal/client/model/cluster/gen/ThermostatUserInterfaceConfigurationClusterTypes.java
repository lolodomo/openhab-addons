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

import java.util.Map;
import java.util.List;

import org.openhab.binding.matter.internal.client.model.cluster.types.*;
import static java.util.Map.entry;  

/**
 * AUTO-GENERATED by zap. DO NOT EDIT!
 *
 * ThermostatUserInterfaceConfiguration
 *
 * @author Dan Cunningham - Initial contribution
 */
public class ThermostatUserInterfaceConfigurationClusterTypes {

    public static final String CLUSTER_NAME = "THERMOSTAT_USER_INTERFACE_CONFIGURATION_CLUSTER";
    public static final int CLUSTER_ID = 0x0204;

    //ZCL Enums
    public enum KeypadLockoutEnum {
        NOLOCKOUT(0, "NoLockout"),
        LOCKOUT1(1, "Lockout1"),
        LOCKOUT2(2, "Lockout2"),
        LOCKOUT3(3, "Lockout3"),
        LOCKOUT4(4, "Lockout4"),
        LOCKOUT5(5, "Lockout5"),
        UNKNOWN_VALUE(6,"UnknownValue");

        public final int value;
        public final String label;
        private KeypadLockoutEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum ScheduleProgrammingVisibilityEnum {
        SCHEDULEPROGRAMMINGPERMITTED(0, "ScheduleProgrammingPermitted"),
        SCHEDULEPROGRAMMINGDENIED(1, "ScheduleProgrammingDenied"),
        UNKNOWN_VALUE(2,"UnknownValue");

        public final int value;
        public final String label;
        private ScheduleProgrammingVisibilityEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum TemperatureDisplayModeEnum {
        CELSIUS(0, "Celsius"),
        FAHRENHEIT(1, "Fahrenheit"),
        UNKNOWN_VALUE(2,"UnknownValue");

        public final int value;
        public final String label;
        private TemperatureDisplayModeEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };


}
