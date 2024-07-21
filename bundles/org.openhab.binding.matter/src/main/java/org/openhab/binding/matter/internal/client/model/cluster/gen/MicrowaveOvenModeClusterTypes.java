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
 * MicrowaveOvenMode
 *
 * @author Dan Cunningham - Initial contribution
 */
public class MicrowaveOvenModeClusterTypes {

    public static final String CLUSTER_NAME = "MICROWAVE_OVEN_MODE_CLUSTER";
    public static final int CLUSTER_ID = 0x005E;

     public class ModeTagStruct {
        public Integer mfgCode; // vendor_id
        public Integer value; // enum16
        public ModeTagStruct(Integer mfgCode, Integer value) {
            this.mfgCode = mfgCode;
            this.value = value;
        }
     }
     public class ModeOptionStruct {
        public String label; // char_string
        public Integer mode; // int8u
        public ModeTagStruct[] modeTags; // ModeTagStruct
        public ModeOptionStruct(String label, Integer mode, ModeTagStruct[] modeTags) {
            this.label = label;
            this.mode = mode;
            this.modeTags = modeTags;
        }
     }
    //ZCL Enums
    public enum ModeTag {
        NORMAL(16384, "Normal"),
        DEFROST(16385, "Defrost"),
        UNKNOWN_VALUE(0,"UnknownValue");

        public final int value;
        public final String label;
        private ModeTag(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    //ZCL Bitmaps
    public static class Feature {
        public boolean onOff;
        public Feature(boolean onOff){
            this.onOff = onOff;
        }
    }

}
