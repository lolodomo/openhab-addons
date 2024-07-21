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
 * OnOff
 *
 * @author Dan Cunningham - Initial contribution
 */
public class OnOffClusterTypes {

    public static final String CLUSTER_NAME = "ON_OFF_CLUSTER";
    public static final int CLUSTER_ID = 0x0006;

    //ZCL Enums
    public enum DelayedAllOffEffectVariantEnum {
        DELAYEDOFFFASTFADE(0, "DelayedOffFastFade"),
        NOFADE(1, "NoFade"),
        DELAYEDOFFSLOWFADE(2, "DelayedOffSlowFade"),
        UNKNOWN_VALUE(3,"UnknownValue");

        public final int value;
        public final String label;
        private DelayedAllOffEffectVariantEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum DyingLightEffectVariantEnum {
        DYINGLIGHTFADEOFF(0, "DyingLightFadeOff"),
        UNKNOWN_VALUE(1,"UnknownValue");

        public final int value;
        public final String label;
        private DyingLightEffectVariantEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum EffectIdentifierEnum {
        DELAYEDALLOFF(0, "DelayedAllOff"),
        DYINGLIGHT(1, "DyingLight"),
        UNKNOWN_VALUE(2,"UnknownValue");

        public final int value;
        public final String label;
        private EffectIdentifierEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum StartUpOnOffEnum {
        OFF(0, "Off"),
        ON(1, "On"),
        TOGGLE(2, "Toggle"),
        UNKNOWN_VALUE(3,"UnknownValue");

        public final int value;
        public final String label;
        private StartUpOnOffEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    //ZCL Bitmaps
    public static class Feature {
        public boolean lighting;
        public boolean deadFrontBehavior;
        public boolean offOnly;
        public Feature(boolean lighting, boolean deadFrontBehavior, boolean offOnly){
            this.lighting = lighting;
            this.deadFrontBehavior = deadFrontBehavior;
            this.offOnly = offOnly;
        }
    }
    public static class OnOffControlBitmap {
        public boolean acceptOnlyWhenOn;
        public OnOffControlBitmap(boolean acceptOnlyWhenOn){
            this.acceptOnlyWhenOn = acceptOnlyWhenOn;
        }
    }

    public static class OffCommandOptions {
        public  OffCommandOptions(){
        }
    }
    public static class OnCommandOptions {
        public  OnCommandOptions(){
        }
    }
    public static class ToggleCommandOptions {
        public  ToggleCommandOptions(){
        }
    }
    public static class OffWithEffectCommandOptions {
        public EffectIdentifierEnum effectIdentifier;
        public Integer effectVariant;
        public  OffWithEffectCommandOptions(EffectIdentifierEnum effectIdentifier, Integer effectVariant){
            this.effectIdentifier = effectIdentifier;
            this.effectVariant = effectVariant;
        }
    }
    public static class OnWithRecallGlobalSceneCommandOptions {
        public  OnWithRecallGlobalSceneCommandOptions(){
        }
    }
    public static class OnWithTimedOffCommandOptions {
        public OnOffControlBitmap onOffControl;
        public Integer onTime;
        public Integer offWaitTime;
        public  OnWithTimedOffCommandOptions(OnOffControlBitmap onOffControl, Integer onTime, Integer offWaitTime){
            this.onOffControl = onOffControl;
            this.onTime = onTime;
            this.offWaitTime = offWaitTime;
        }
    }
}
