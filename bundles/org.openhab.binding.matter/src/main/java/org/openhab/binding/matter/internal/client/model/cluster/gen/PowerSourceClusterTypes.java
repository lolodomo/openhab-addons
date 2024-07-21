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
 * PowerSource
 *
 * @author Dan Cunningham - Initial contribution
 */
public class PowerSourceClusterTypes {

    public static final String CLUSTER_NAME = "POWER_SOURCE_CLUSTER";
    public static final int CLUSTER_ID = 0x002F;

     public class BatChargeFaultChangeType {
        public BatChargeFaultEnum current; // BatChargeFaultEnum
        public BatChargeFaultEnum previous; // BatChargeFaultEnum
        public BatChargeFaultChangeType(BatChargeFaultEnum current, BatChargeFaultEnum previous) {
            this.current = current;
            this.previous = previous;
        }
     }
     public class BatFaultChangeType {
        public BatFaultEnum current; // BatFaultEnum
        public BatFaultEnum previous; // BatFaultEnum
        public BatFaultChangeType(BatFaultEnum current, BatFaultEnum previous) {
            this.current = current;
            this.previous = previous;
        }
     }
     public class WiredFaultChangeType {
        public WiredFaultEnum current; // WiredFaultEnum
        public WiredFaultEnum previous; // WiredFaultEnum
        public WiredFaultChangeType(WiredFaultEnum current, WiredFaultEnum previous) {
            this.current = current;
            this.previous = previous;
        }
     }
    //ZCL Enums
    public enum BatApprovedChemistryEnum {
        UNSPECIFIED(0, "Unspecified"),
        ALKALINE(1, "Alkaline"),
        LITHIUMCARBONFLUORIDE(2, "LithiumCarbonFluoride"),
        LITHIUMCHROMIUMOXIDE(3, "LithiumChromiumOxide"),
        LITHIUMCOPPEROXIDE(4, "LithiumCopperOxide"),
        LITHIUMIRONDISULFIDE(5, "LithiumIronDisulfide"),
        LITHIUMMANGANESEDIOXIDE(6, "LithiumManganeseDioxide"),
        LITHIUMTHIONYLCHLORIDE(7, "LithiumThionylChloride"),
        MAGNESIUM(8, "Magnesium"),
        MERCURYOXIDE(9, "MercuryOxide"),
        NICKELOXYHYDRIDE(10, "NickelOxyhydride"),
        SILVEROXIDE(11, "SilverOxide"),
        ZINCAIR(12, "ZincAir"),
        ZINCCARBON(13, "ZincCarbon"),
        ZINCCHLORIDE(14, "ZincChloride"),
        ZINCMANGANESEDIOXIDE(15, "ZincManganeseDioxide"),
        LEADACID(16, "LeadAcid"),
        LITHIUMCOBALTOXIDE(17, "LithiumCobaltOxide"),
        LITHIUMION(18, "LithiumIon"),
        LITHIUMIONPOLYMER(19, "LithiumIonPolymer"),
        LITHIUMIRONPHOSPHATE(20, "LithiumIronPhosphate"),
        LITHIUMSULFUR(21, "LithiumSulfur"),
        LITHIUMTITANATE(22, "LithiumTitanate"),
        NICKELCADMIUM(23, "NickelCadmium"),
        NICKELHYDROGEN(24, "NickelHydrogen"),
        NICKELIRON(25, "NickelIron"),
        NICKELMETALHYDRIDE(26, "NickelMetalHydride"),
        NICKELZINC(27, "NickelZinc"),
        SILVERZINC(28, "SilverZinc"),
        SODIUMION(29, "SodiumIon"),
        SODIUMSULFUR(30, "SodiumSulfur"),
        ZINCBROMIDE(31, "ZincBromide"),
        ZINCCERIUM(32, "ZincCerium"),
        UNKNOWN_VALUE(33,"UnknownValue");

        public final int value;
        public final String label;
        private BatApprovedChemistryEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum BatChargeFaultEnum {
        UNSPECIFIED(0, "Unspecified"),
        AMBIENTTOOHOT(1, "AmbientTooHot"),
        AMBIENTTOOCOLD(2, "AmbientTooCold"),
        BATTERYTOOHOT(3, "BatteryTooHot"),
        BATTERYTOOCOLD(4, "BatteryTooCold"),
        BATTERYABSENT(5, "BatteryAbsent"),
        BATTERYOVERVOLTAGE(6, "BatteryOverVoltage"),
        BATTERYUNDERVOLTAGE(7, "BatteryUnderVoltage"),
        CHARGEROVERVOLTAGE(8, "ChargerOverVoltage"),
        CHARGERUNDERVOLTAGE(9, "ChargerUnderVoltage"),
        SAFETYTIMEOUT(10, "SafetyTimeout"),
        UNKNOWN_VALUE(11,"UnknownValue");

        public final int value;
        public final String label;
        private BatChargeFaultEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum BatChargeLevelEnum {
        OK(0, "OK"),
        WARNING(1, "Warning"),
        CRITICAL(2, "Critical"),
        UNKNOWN_VALUE(3,"UnknownValue");

        public final int value;
        public final String label;
        private BatChargeLevelEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum BatChargeStateEnum {
        UNKNOWN(0, "Unknown"),
        ISCHARGING(1, "IsCharging"),
        ISATFULLCHARGE(2, "IsAtFullCharge"),
        ISNOTCHARGING(3, "IsNotCharging"),
        UNKNOWN_VALUE(4,"UnknownValue");

        public final int value;
        public final String label;
        private BatChargeStateEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum BatCommonDesignationEnum {
        UNSPECIFIED(0, "Unspecified"),
        AAA(1, "AAA"),
        AA(2, "AA"),
        C(3, "C"),
        D(4, "D"),
        T4V5(5, "4v5"),
        T6V0(6, "6v0"),
        T9V0(7, "9v0"),
        T1_2AA(8, "1_2AA"),
        AAAA(9, "AAAA"),
        A(10, "A"),
        B(11, "B"),
        F(12, "F"),
        N(13, "N"),
        NO6(14, "No6"),
        SUBC(15, "SubC"),
        A23(16, "A23"),
        A27(17, "A27"),
        BA5800(18, "BA5800"),
        DUPLEX(19, "Duplex"),
        T4SR44(20, "4SR44"),
        T523(21, "523"),
        T531(22, "531"),
        T15V0(23, "15v0"),
        T22V5(24, "22v5"),
        T30V0(25, "30v0"),
        T45V0(26, "45v0"),
        T67V5(27, "67v5"),
        J(28, "J"),
        CR123A(29, "CR123A"),
        CR2(30, "CR2"),
        T2CR5(31, "2CR5"),
        CR_P2(32, "CR_P2"),
        CR_V3(33, "CR_V3"),
        SR41(34, "SR41"),
        SR43(35, "SR43"),
        SR44(36, "SR44"),
        SR45(37, "SR45"),
        SR48(38, "SR48"),
        SR54(39, "SR54"),
        SR55(40, "SR55"),
        SR57(41, "SR57"),
        SR58(42, "SR58"),
        SR59(43, "SR59"),
        SR60(44, "SR60"),
        SR63(45, "SR63"),
        SR64(46, "SR64"),
        SR65(47, "SR65"),
        SR66(48, "SR66"),
        SR67(49, "SR67"),
        SR68(50, "SR68"),
        SR69(51, "SR69"),
        SR516(52, "SR516"),
        SR731(53, "SR731"),
        SR712(54, "SR712"),
        LR932(55, "LR932"),
        A5(56, "A5"),
        A10(57, "A10"),
        A13(58, "A13"),
        A312(59, "A312"),
        A675(60, "A675"),
        AC41E(61, "AC41E"),
        T10180(62, "10180"),
        T10280(63, "10280"),
        T10440(64, "10440"),
        T14250(65, "14250"),
        T14430(66, "14430"),
        T14500(67, "14500"),
        T14650(68, "14650"),
        T15270(69, "15270"),
        T16340(70, "16340"),
        RCR123A(71, "RCR123A"),
        T17500(72, "17500"),
        T17670(73, "17670"),
        T18350(74, "18350"),
        T18500(75, "18500"),
        T18650(76, "18650"),
        T19670(77, "19670"),
        T25500(78, "25500"),
        T26650(79, "26650"),
        T32600(80, "32600"),
        UNKNOWN_VALUE(81,"UnknownValue");

        public final int value;
        public final String label;
        private BatCommonDesignationEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum BatFaultEnum {
        UNSPECIFIED(0, "Unspecified"),
        OVERTEMP(1, "OverTemp"),
        UNDERTEMP(2, "UnderTemp"),
        UNKNOWN_VALUE(3,"UnknownValue");

        public final int value;
        public final String label;
        private BatFaultEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum BatReplaceabilityEnum {
        UNSPECIFIED(0, "Unspecified"),
        NOTREPLACEABLE(1, "NotReplaceable"),
        USERREPLACEABLE(2, "UserReplaceable"),
        FACTORYREPLACEABLE(3, "FactoryReplaceable"),
        UNKNOWN_VALUE(4,"UnknownValue");

        public final int value;
        public final String label;
        private BatReplaceabilityEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum PowerSourceStatusEnum {
        UNSPECIFIED(0, "Unspecified"),
        ACTIVE(1, "Active"),
        STANDBY(2, "Standby"),
        UNAVAILABLE(3, "Unavailable"),
        UNKNOWN_VALUE(4,"UnknownValue");

        public final int value;
        public final String label;
        private PowerSourceStatusEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum WiredCurrentTypeEnum {
        AC(0, "AC"),
        DC(1, "DC"),
        UNKNOWN_VALUE(2,"UnknownValue");

        public final int value;
        public final String label;
        private WiredCurrentTypeEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    public enum WiredFaultEnum {
        UNSPECIFIED(0, "Unspecified"),
        OVERVOLTAGE(1, "OverVoltage"),
        UNDERVOLTAGE(2, "UnderVoltage"),
        UNKNOWN_VALUE(3,"UnknownValue");

        public final int value;
        public final String label;
        private WiredFaultEnum(int value, String label){
            this.value = value;
            this.label = label;
        }
    };

    //ZCL Bitmaps
    public static class Feature {
        public boolean wired;
        public boolean battery;
        public boolean rechargeable;
        public boolean replaceable;
        public Feature(boolean wired, boolean battery, boolean rechargeable, boolean replaceable){
            this.wired = wired;
            this.battery = battery;
            this.rechargeable = rechargeable;
            this.replaceable = replaceable;
        }
    }

}
