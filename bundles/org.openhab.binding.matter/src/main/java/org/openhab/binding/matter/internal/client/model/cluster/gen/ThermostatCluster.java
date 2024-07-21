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
import org.openhab.binding.matter.internal.client.model.cluster.gen.ThermostatClusterTypes.*;

/**
 * Thermostat
 *
 * @author Dan Cunningham - Initial contribution
 */
public class ThermostatCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "Thermostat";
    public static final int CLUSTER_ID = 0x0201;

    public Integer localTemperature; // 0 int16s reportable 
    public Integer outdoorTemperature; // 1 int16s reportable 
    public Map<String, Boolean> occupancy; // 2 bitmap8 reportable 
    public Integer absMinHeatSetpointLimit; // 3 int16s reportable 
    public Integer absMaxHeatSetpointLimit; // 4 int16s reportable 
    public Integer absMinCoolSetpointLimit; // 5 int16s reportable 
    public Integer absMaxCoolSetpointLimit; // 6 int16s reportable 
    public Integer PICoolingDemand; // 7 int8u reportable 
    public Integer PIHeatingDemand; // 8 int8u reportable 
    public Map<String, Boolean> HVACSystemTypeConfiguration; // 9 bitmap8 reportable writable
    public Integer localTemperatureCalibration; // 16 int8s reportable writable
    public Integer occupiedCoolingSetpoint; // 17 int16s reportable writable
    public Integer occupiedHeatingSetpoint; // 18 int16s reportable writable
    public Integer unoccupiedCoolingSetpoint; // 19 int16s reportable writable
    public Integer unoccupiedHeatingSetpoint; // 20 int16s reportable writable
    public Integer minHeatSetpointLimit; // 21 int16s reportable writable
    public Integer maxHeatSetpointLimit; // 22 int16s reportable writable
    public Integer minCoolSetpointLimit; // 23 int16s reportable writable
    public Integer maxCoolSetpointLimit; // 24 int16s reportable writable
    public Integer minSetpointDeadBand; // 25 int8s reportable writable
    public Map<String, Boolean> remoteSensing; // 26 bitmap8 reportable writable
    public ThermostatControlSequence controlSequenceOfOperation; // 27 ThermostatControlSequence reportable writable
    public Integer systemMode; // 28 enum8 reportable writable
    public Integer thermostatRunningMode; // 30 enum8 reportable 
    public Integer startOfWeek; // 32 enum8 reportable 
    public Integer numberOfWeeklyTransitions; // 33 int8u reportable 
    public Integer numberOfDailyTransitions; // 34 int8u reportable 
    public Integer temperatureSetpointHold; // 35 enum8 reportable writable
    public Integer temperatureSetpointHoldDuration; // 36 int16u reportable writable
    public Map<String, Boolean> thermostatProgrammingOperationMode; // 37 bitmap8 reportable writable
    public Map<String, Boolean> thermostatRunningState; // 41 bitmap16 reportable 
    public Integer setpointChangeSource; // 48 enum8 reportable 
    public Integer setpointChangeAmount; // 49 int16s reportable 
    public Integer setpointChangeSourceTimestamp; // 50 epoch_s reportable 
    public Integer occupiedSetback; // 52 int8u reportable writable
    public Integer occupiedSetbackMin; // 53 int8u reportable 
    public Integer occupiedSetbackMax; // 54 int8u reportable 
    public Integer unoccupiedSetback; // 55 int8u reportable writable
    public Integer unoccupiedSetbackMin; // 56 int8u reportable 
    public Integer unoccupiedSetbackMax; // 57 int8u reportable 
    public Integer emergencyHeatDelta; // 58 int8u reportable writable
    public Integer ACType; // 64 enum8 reportable writable
    public Integer ACCapacity; // 65 int16u reportable writable
    public Integer ACRefrigerantType; // 66 enum8 reportable writable
    public Integer ACCompressorType; // 67 enum8 reportable writable
    public Map<String, Boolean> ACErrorCode; // 68 bitmap32 reportable writable
    public Integer ACLouverPosition; // 69 enum8 reportable writable
    public Integer ACCoilTemperature; // 70 int16s reportable 
    public Integer ACCapacityformat; // 71 enum8 reportable writable
    public List<Integer> generatedCommandList; // 65528 command_id reportable 
    public List<Integer> acceptedCommandList; // 65529 command_id reportable 
    public List<Integer> eventList; // 65530 event_id reportable 
    public List<Integer> attributeList; // 65531 attrib_id reportable 
    public Map<String, Boolean> featureMap; // 65532 bitmap32 reportable 
    public Integer clusterRevision; // 65533 int16u reportable 

    public ThermostatCluster(String nodeId, int endpointId) {
        super(nodeId, endpointId, 75, "Thermostat");
    }

    public String toString() {
        String str = "";
        str += "localTemperature : " + localTemperature + "\n";
        str += "outdoorTemperature : " + outdoorTemperature + "\n";
        str += "occupancy : " + occupancy + "\n";
        str += "absMinHeatSetpointLimit : " + absMinHeatSetpointLimit + "\n";
        str += "absMaxHeatSetpointLimit : " + absMaxHeatSetpointLimit + "\n";
        str += "absMinCoolSetpointLimit : " + absMinCoolSetpointLimit + "\n";
        str += "absMaxCoolSetpointLimit : " + absMaxCoolSetpointLimit + "\n";
        str += "PICoolingDemand : " + PICoolingDemand + "\n";
        str += "PIHeatingDemand : " + PIHeatingDemand + "\n";
        str += "HVACSystemTypeConfiguration : " + HVACSystemTypeConfiguration + "\n";
        str += "localTemperatureCalibration : " + localTemperatureCalibration + "\n";
        str += "occupiedCoolingSetpoint : " + occupiedCoolingSetpoint + "\n";
        str += "occupiedHeatingSetpoint : " + occupiedHeatingSetpoint + "\n";
        str += "unoccupiedCoolingSetpoint : " + unoccupiedCoolingSetpoint + "\n";
        str += "unoccupiedHeatingSetpoint : " + unoccupiedHeatingSetpoint + "\n";
        str += "minHeatSetpointLimit : " + minHeatSetpointLimit + "\n";
        str += "maxHeatSetpointLimit : " + maxHeatSetpointLimit + "\n";
        str += "minCoolSetpointLimit : " + minCoolSetpointLimit + "\n";
        str += "maxCoolSetpointLimit : " + maxCoolSetpointLimit + "\n";
        str += "minSetpointDeadBand : " + minSetpointDeadBand + "\n";
        str += "remoteSensing : " + remoteSensing + "\n";
        str += "controlSequenceOfOperation : " + controlSequenceOfOperation + "\n";
        str += "systemMode : " + systemMode + "\n";
        str += "thermostatRunningMode : " + thermostatRunningMode + "\n";
        str += "startOfWeek : " + startOfWeek + "\n";
        str += "numberOfWeeklyTransitions : " + numberOfWeeklyTransitions + "\n";
        str += "numberOfDailyTransitions : " + numberOfDailyTransitions + "\n";
        str += "temperatureSetpointHold : " + temperatureSetpointHold + "\n";
        str += "temperatureSetpointHoldDuration : " + temperatureSetpointHoldDuration + "\n";
        str += "thermostatProgrammingOperationMode : " + thermostatProgrammingOperationMode + "\n";
        str += "thermostatRunningState : " + thermostatRunningState + "\n";
        str += "setpointChangeSource : " + setpointChangeSource + "\n";
        str += "setpointChangeAmount : " + setpointChangeAmount + "\n";
        str += "setpointChangeSourceTimestamp : " + setpointChangeSourceTimestamp + "\n";
        str += "occupiedSetback : " + occupiedSetback + "\n";
        str += "occupiedSetbackMin : " + occupiedSetbackMin + "\n";
        str += "occupiedSetbackMax : " + occupiedSetbackMax + "\n";
        str += "unoccupiedSetback : " + unoccupiedSetback + "\n";
        str += "unoccupiedSetbackMin : " + unoccupiedSetbackMin + "\n";
        str += "unoccupiedSetbackMax : " + unoccupiedSetbackMax + "\n";
        str += "emergencyHeatDelta : " + emergencyHeatDelta + "\n";
        str += "ACType : " + ACType + "\n";
        str += "ACCapacity : " + ACCapacity + "\n";
        str += "ACRefrigerantType : " + ACRefrigerantType + "\n";
        str += "ACCompressorType : " + ACCompressorType + "\n";
        str += "ACErrorCode : " + ACErrorCode + "\n";
        str += "ACLouverPosition : " + ACLouverPosition + "\n";
        str += "ACCoilTemperature : " + ACCoilTemperature + "\n";
        str += "ACCapacityformat : " + ACCapacityformat + "\n";
        str += "generatedCommandList : " + generatedCommandList + "\n";
        str += "acceptedCommandList : " + acceptedCommandList + "\n";
        str += "eventList : " + eventList + "\n";
        str += "attributeList : " + attributeList + "\n";
        str += "featureMap : " + featureMap + "\n";
        str += "clusterRevision : " + clusterRevision + "\n";
        return str;
    }
}
