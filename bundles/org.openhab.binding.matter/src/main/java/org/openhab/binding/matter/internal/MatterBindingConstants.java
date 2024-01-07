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
package org.openhab.binding.matter.internal;

import static java.util.Map.entry;
import static org.openhab.core.thing.DefaultSystemChannelTypeProvider.*;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.type.ChannelTypeUID;

/**
 * The {@link MatterBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
public class MatterBindingConstants {

    public static final String BINDING_ID = "matter";

    // List of all Thing Type UIDs
    // public static final ThingTypeUID THING_TYPE_SAMPLE = new ThingTypeUID(BINDING_ID, "sample");
    public static final ThingTypeUID THING_TYPE_CONTROLLER = new ThingTypeUID(BINDING_ID, "controller");
    public static final ThingTypeUID THING_TYPE_NODE = new ThingTypeUID(BINDING_ID, "node");
    public static final ThingTypeUID THING_TYPE_ENDPOINT = new ThingTypeUID(BINDING_ID, "endpoint");
    public static final ThingTypeUID THING_TYPE_CLUSTER = new ThingTypeUID(BINDING_ID, "cluster");

    // List of all Channel ids
    public static final String CHANNEL_1 = "channel1";
    public static final String CHANNEL_PAIR_CODE = "pair_code";
    public static final String CHANNEL_COMMAND = "command";

    // This was borrowed from the zigbee binding as Matter uses the same cluster API model
    // List of Channel UIDs
    public static final String CHANNEL_NAME_SWITCH_ONOFF = "switch";
    public static final String CHANNEL_LABEL_SWITCH_ONOFF = "Switch";
    public static final ChannelTypeUID CHANNEL_SWITCH_ONOFF = new ChannelTypeUID("matter:switch_onoff");

    public static final String CHANNEL_NAME_SWITCH_LEVEL = "dimmer";
    public static final String CHANNEL_LABEL_SWITCH_LEVEL = "Level Control";
    public static final ChannelTypeUID CHANNEL_SWITCH_LEVEL = new ChannelTypeUID("matter:switch_level");

    public static final String CHANNEL_NAME_WARNING_DEVICE = "warning_device";
    public static final String CHANNEL_LABEL_WARNING_DEVICE = "Warning Device";
    public static final ChannelTypeUID CHANNEL_WARNING_DEVICE = new ChannelTypeUID("matter:warning_device");

    public static final String CHANNEL_NAME_COLOR_COLOR = "color";
    public static final String CHANNEL_LABEL_COLOR_COLOR = "Color";
    public static final ChannelTypeUID CHANNEL_COLOR_COLOR = new ChannelTypeUID("matter:color_color");

    public static final String CHANNEL_NAME_COLOR_TEMPERATURE = "colortemperature";
    public static final String CHANNEL_LABEL_COLOR_TEMPERATURE = "Color Temperature";
    public static final ChannelTypeUID CHANNEL_COLOR_TEMPERATURE = SYSTEM_COLOR_TEMPERATURE.getUID();

    public static final String CHANNEL_NAME_ILLUMINANCE_VALUE = "illuminance";
    public static final String CHANNEL_LABEL_ILLUMINANCE_VALUE = "Illuminance";
    public static final ChannelTypeUID CHANNEL_ILLUMINANCE_VALUE = new ChannelTypeUID("matter:measurement_illuminance");

    public static final String CHANNEL_NAME_TEMPERATURE_VALUE = "temperature";
    public static final String CHANNEL_LABEL_TEMPERATURE_VALUE = "Temperature";
    public static final ChannelTypeUID CHANNEL_TEMPERATURE_VALUE = new ChannelTypeUID("matter:measurement_temperature");

    public static final String CHANNEL_NAME_HUMIDITY_VALUE = "humidity";
    public static final String CHANNEL_LABEL_HUMIDITY_VALUE = "Humidity";
    public static final ChannelTypeUID CHANNEL_HUMIDITY_VALUE = new ChannelTypeUID(
            "matter:measurement_relativehumidity");

    public static final String CHANNEL_NAME_PRESSURE_VALUE = "pressure";
    public static final String CHANNEL_LABEL_PRESSURE_VALUE = "Atmospheric Pressure";
    public static final ChannelTypeUID CHANNEL_PRESSURE_VALUE = new ChannelTypeUID("matter:measurement_pressure");

    public static final String CHANNEL_NAME_OCCUPANCY_SENSOR = "occupancy";
    public static final String CHANNEL_LABEL_OCCUPANCY_SENSOR = "Occupancy";
    public static final ChannelTypeUID CHANNEL_OCCUPANCY_SENSOR = new ChannelTypeUID("matter:sensor_occupancy");

    public static final String CHANNEL_NAME_FANCONTROL = "fancontrol";
    public static final String CHANNEL_LABEL_FANCONTROL = "Fan Control";
    public static final ChannelTypeUID CHANNEL_FANCONTROL = new ChannelTypeUID("matter:fancontrol");

    public static final String CHANNEL_NAME_BINARYINPUT = "binaryinput";
    public static final String CHANNEL_LABEL_BINARYINPUT = "Binary Input";
    public static final ChannelTypeUID CHANNEL_BINARYINPUT = new ChannelTypeUID("matter:binaryinput");

    public static final String CHANNEL_NAME_IAS_CODETECTOR = "cosensor";
    public static final String CHANNEL_LABEL_IAS_CODETECTOR = "Carbon Monoxide Detector";
    public static final ChannelTypeUID CHANNEL_IAS_CODETECTOR = new ChannelTypeUID("matter:ias_cosensor");

    public static final String CHANNEL_NAME_IAS_CONTACTPORTAL1 = "contact1";
    public static final String CHANNEL_LABEL_IAS_CONTACTPORTAL1 = "Contact Portal 1";
    public static final ChannelTypeUID CHANNEL_IAS_CONTACTPORTAL1 = new ChannelTypeUID("matter:ias_contactportal1");

    public static final String CHANNEL_NAME_IAS_CONTACTPORTAL2 = "contact2";
    public static final String CHANNEL_LABEL_IAS_CONTACTPORTAL2 = "Contact Portal 2";
    public static final ChannelTypeUID CHANNEL_IAS_CONTACTPORTAL2 = new ChannelTypeUID("matter:ias_contactportal2");

    public static final String CHANNEL_NAME_IAS_MOTIONINTRUSION = "intrusion";
    public static final String CHANNEL_LABEL_IAS_MOTIONINTRUSION = "Motion Intrusion";
    public static final ChannelTypeUID CHANNEL_IAS_MOTIONINTRUSION = new ChannelTypeUID("matter:ias_motionintrusion");

    public static final String CHANNEL_NAME_IAS_MOTIONPRESENCE = "motion";
    public static final String CHANNEL_LABEL_IAS_MOTIONPRESENCE = "Motion Presence";
    public static final ChannelTypeUID CHANNEL_IAS_MOTIONPRESENCE = new ChannelTypeUID("matter:ias_motionpresence");

    public static final String CHANNEL_NAME_IAS_STANDARDCIESYSTEM = "system";
    public static final String CHANNEL_LABEL_IAS_STANDARDCIESYSTEM = "CIE System Alarm";
    public static final ChannelTypeUID CHANNEL_IAS_STANDARDCIESYSTEM = new ChannelTypeUID("matter:ias_standard_system");

    public static final String CHANNEL_NAME_IAS_FIREINDICATION = "fire";
    public static final String CHANNEL_LABEL_IAS_FIREINDICATION = "Fire Alarm";
    public static final ChannelTypeUID CHANNEL_IAS_FIREINDICATION = new ChannelTypeUID("matter:ias_fire");

    public static final String CHANNEL_NAME_IAS_WATERSENSOR = "water";
    public static final String CHANNEL_LABEL_IAS_WATERSENSOR = "Water Alarm";
    public static final ChannelTypeUID CHANNEL_IAS_WATERSENSOR = new ChannelTypeUID("matter:ias_water");

    public static final String CHANNEL_NAME_IAS_MOVEMENTSENSOR = "movement";
    public static final String CHANNEL_LABEL_IAS_MOVEMENTSENSOR = "Movement Alarm";
    public static final ChannelTypeUID CHANNEL_IAS_MOVEMENTSENSOR = new ChannelTypeUID("matter:ias_movement");

    public static final String CHANNEL_NAME_IAS_VIBRATIONSENSOR = "vibration";
    public static final String CHANNEL_LABEL_IAS_VIBRATIONSENSOR = "Vibration Alarm";
    public static final ChannelTypeUID CHANNEL_IAS_VIBRATIONSENSOR = new ChannelTypeUID("matter:ias_vibration");

    public static final String CHANNEL_NAME_IAS_LOWBATTERY = "iaslowbattery";
    public static final String CHANNEL_LABEL_IAS_LOWBATTERY = "Low Battery";
    public static final ChannelTypeUID CHANNEL_IAS_LOWBATTERY = SYSTEM_CHANNEL_LOW_BATTERY.getUID();

    public static final String CHANNEL_NAME_IAS_TAMPER = "tamper";
    public static final String CHANNEL_LABEL_IAS_TAMPER = "Tamper";
    public static final ChannelTypeUID CHANNEL_IAS_TAMPER = new ChannelTypeUID("matter:ias_tamper");

    public static final String CHANNEL_NAME_ELECTRICAL_ACTIVEPOWER = "activepower";
    public static final String CHANNEL_LABEL_ELECTRICAL_ACTIVEPOWER = "Total Active Power";
    public static final ChannelTypeUID CHANNEL_ELECTRICAL_ACTIVEPOWER = new ChannelTypeUID(
            "matter:electrical_activepower");

    public static final String CHANNEL_NAME_ELECTRICAL_RMSVOLTAGE = "voltage";
    public static final String CHANNEL_LABEL_ELECTRICAL_RMSVOLTAGE = "Voltage";
    public static final ChannelTypeUID CHANNEL_ELECTRICAL_RMSVOLTAGE = new ChannelTypeUID(
            "matter:electrical_rmsvoltage");

    public static final String CHANNEL_NAME_POWER_BATTERYPERCENT = "batterylevel";
    public static final String CHANNEL_LABEL_POWER_BATTERYPERCENT = "Battery Level";
    public static final ChannelTypeUID CHANNEL_POWER_BATTERYPERCENT = new ChannelTypeUID("system:battery-level");

    public static final String CHANNEL_NAME_POWER_BATTERYVOLTAGE = "batteryvoltage";
    public static final String CHANNEL_LABEL_POWER_BATTERYVOLTAGE = "Battery Voltage";
    public static final ChannelTypeUID CHANNEL_POWER_BATTERYVOLTAGE = new ChannelTypeUID("matter:battery_voltage");

    public static final String CHANNEL_NAME_POWER_BATTERYALARM = "batteryalarm";
    public static final String CHANNEL_LABEL_POWER_BATTERYALARM = "Battery Alarm";
    public static final ChannelTypeUID CHANNEL_POWER_BATTERYALARM = new ChannelTypeUID("matter:battery_alarm");

    public static final String CHANNEL_NAME_ELECTRICAL_RMSCURRENT = "current";
    public static final String CHANNEL_LABEL_ELECTRICAL_RMSCURRENT = "Current";
    public static final ChannelTypeUID CHANNEL_ELECTRICAL_RMSCURRENT = new ChannelTypeUID(
            "matter:electrical_rmscurrent");

    public static final String CHANNEL_NAME_THERMOSTAT_LOCALTEMPERATURE = "thermostatlocaltemp";
    public static final String CHANNEL_LABEL_THERMOSTAT_LOCALTEMPERATURE = "Local Temperature";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_LOCALTEMPERATURE = new ChannelTypeUID(
            "matter:thermostat_localtemp");

    public static final String CHANNEL_NAME_THERMOSTAT_OUTDOORTEMPERATURE = "thermostatoutdoortemp";
    public static final String CHANNEL_LABEL_THERMOSTAT_OUTDOORTEMPERATURE = "Outdoor Temperature";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_OUTDOORTEMPERATURE = new ChannelTypeUID(
            "matter:thermostat_outdoortemp");

    public static final String CHANNEL_NAME_THERMOSTAT_OCCUPIEDCOOLING = "thermostatoccupiedcooling";
    public static final String CHANNEL_LABEL_THERMOSTAT_OCCUPIEDCOOLING = "Occupied Cooling Setpoint";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_OCCUPIEDCOOLING = new ChannelTypeUID(
            "matter:thermostat_occupiedcooling");

    public static final String CHANNEL_NAME_THERMOSTAT_OCCUPIEDHEATING = "thermostatoccupiedheating";
    public static final String CHANNEL_LABEL_THERMOSTAT_OCCUPIEDHEATING = "Occupied Heating Setpoint";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_OCCUPIEDHEATING = new ChannelTypeUID(
            "matter:thermostat_occupiedheating");

    public static final String CHANNEL_NAME_THERMOSTAT_UNOCCUPIEDCOOLING = "thermostatunoccupiedcooling";
    public static final String CHANNEL_LABEL_THERMOSTAT_UNOCCUPIEDCOOLING = "Unoccupied Cooling Setpoint";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_UNOCCUPIEDCOOLING = new ChannelTypeUID(
            "matter:thermostat_unoccupiedcooling");

    public static final String CHANNEL_NAME_THERMOSTAT_UNOCCUPIEDHEATING = "thermostatunoccupiedheating";
    public static final String CHANNEL_LABEL_THERMOSTAT_UNOCCUPIEDHEATING = "Unoccupied Heating Setpoint";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_UNOCCUPIEDHEATING = new ChannelTypeUID(
            "matter:thermostat_unoccupiedheating");

    public static final String CHANNEL_NAME_THERMOSTAT_SYSTEMMODE = "thermostatsystemmode";
    public static final String CHANNEL_LABEL_THERMOSTAT_SYSTEMMODE = "System Mode";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_SYSTEMMODE = new ChannelTypeUID(
            "matter:thermostat_systemmode");

    public static final String CHANNEL_NAME_THERMOSTAT_RUNNINGMODE = "thermostatrunningmode";
    public static final String CHANNEL_LABEL_THERMOSTAT_RUNNINGMODE = "Running Mode";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_RUNNINGMODE = new ChannelTypeUID(
            "matter:thermostat_runningmode");

    public static final String CHANNEL_NAME_THERMOSTAT_HEATING_DEMAND = "thermostatheatingdemand";
    public static final String CHANNEL_LABEL_THERMOSTAT_HEATING_DEMAND = "Heating Demand";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_HEATING_DEMAND = new ChannelTypeUID(
            "matter:thermostat_heatingdemand");

    public static final String CHANNEL_NAME_THERMOSTAT_COOLING_DEMAND = "thermostatcoolingdemand";
    public static final String CHANNEL_LABEL_THERMOSTAT_COOLING_DEMAND = "Cooling Demand";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_COOLING_DEMAND = new ChannelTypeUID(
            "matter:thermostat_coolingdemand");

    public static final String CHANNEL_NAME_DOORLOCK_STATE = "doorlockstate";
    public static final String CHANNEL_LABEL_DOORLOCK_STATE = "Door Lock State";
    public static final ChannelTypeUID CHANNEL_DOORLOCK_STATE = new ChannelTypeUID("matter:door_state");

    public static final String CHANNEL_NAME_WINDOWCOVERING_LIFT = "windowcoveringlift";
    public static final String CHANNEL_LABEL_WINDOWCOVERING_LIFT = "Window Covering Lift";
    public static final ChannelTypeUID CHANNEL_WINDOWCOVERING_LIFT = new ChannelTypeUID("matter:windowcovering_lift");

    public static final String CHANNEL_NAME_INSTANTANEOUS_DEMAND = "meteringinstantdemand";
    public static final String CHANNEL_LABEL_INSTANTANEOUS_DEMAND = "Metering Instantaneous Demand";
    public static final ChannelTypeUID CHANNEL_INSTANTANEOUS_DEMAND = new ChannelTypeUID(
            "matter:metering_instantdemand");

    public static final String CHANNEL_NAME_SUMMATION_DELIVERED = "meteringsumdelivered";
    public static final String CHANNEL_LABEL_SUMMATION_DELIVERED = "Metering Summation Delivered";
    public static final ChannelTypeUID CHANNEL_SUMMATION_DELIVERED = new ChannelTypeUID("matter:metering_sumdelivered");

    public static final String CHANNEL_NAME_SUMMATION_RECEIVED = "meteringsumreceived";
    public static final String CHANNEL_LABEL_SUMMATION_RECEIVED = "Metering Summation Received";
    public static final ChannelTypeUID CHANNEL_SUMMATION_RECEIVED = new ChannelTypeUID("matter:metering_sumreceived");

    public static final String ITEM_TYPE_COLOR = "Color";
    public static final String ITEM_TYPE_CONTACT = "Contact";
    public static final String ITEM_TYPE_DIMMER = "Dimmer";
    public static final String ITEM_TYPE_NUMBER = "Number";
    public static final String ITEM_TYPE_NUMBER_PRESSURE = "Number:Pressure";
    public static final String ITEM_TYPE_NUMBER_TEMPERATURE = "Number:Temperature";
    public static final String ITEM_TYPE_ROLLERSHUTTER = "Rollershutter";
    public static final String ITEM_TYPE_SWITCH = "Switch";
    public static final String ITEM_TYPE_STRING = "String";

    public static final ThingTypeUID THING_TYPE_IDENTIFY = new ThingTypeUID(BINDING_ID, "identify");
    public static final ThingTypeUID THING_TYPE_GROUPS = new ThingTypeUID(BINDING_ID, "groups");
    public static final ThingTypeUID THING_TYPE_SCENES = new ThingTypeUID(BINDING_ID, "scenes");
    public static final ThingTypeUID THING_TYPE_ON_OFF = new ThingTypeUID(BINDING_ID, "onOff");
    public static final ThingTypeUID THING_TYPE_ON_OFF_SWITCH_CONFIGURATION = new ThingTypeUID(BINDING_ID,
            "onOffSwitchConfiguration");
    public static final ThingTypeUID THING_TYPE_LEVEL_CONTROL = new ThingTypeUID(BINDING_ID, "levelControl");
    public static final ThingTypeUID THING_TYPE_BINARY_INPUT__BASIC_ = new ThingTypeUID(BINDING_ID, "binaryInputBasic");
    public static final ThingTypeUID THING_TYPE_DESCRIPTOR = new ThingTypeUID(BINDING_ID, "descriptor");
    public static final ThingTypeUID THING_TYPE_BINDING = new ThingTypeUID(BINDING_ID, "binding");
    public static final ThingTypeUID THING_TYPE_ACCESS_CONTROL = new ThingTypeUID(BINDING_ID, "accessControl");
    public static final ThingTypeUID THING_TYPE_ACTIONS = new ThingTypeUID(BINDING_ID, "actions");
    public static final ThingTypeUID THING_TYPE_BASIC_INFORMATION = new ThingTypeUID(BINDING_ID, "basicInformation");
    public static final ThingTypeUID THING_TYPE_OTA_SOFTWARE_UPDATE_PROVIDER = new ThingTypeUID(BINDING_ID,
            "otaSoftwareUpdateProvider");
    public static final ThingTypeUID THING_TYPE_OTA_SOFTWARE_UPDATE_REQUESTOR = new ThingTypeUID(BINDING_ID,
            "otaSoftwareUpdateRequestor");
    public static final ThingTypeUID THING_TYPE_LOCALIZATION_CONFIGURATION = new ThingTypeUID(BINDING_ID,
            "localizationConfiguration");
    public static final ThingTypeUID THING_TYPE_TIME_FORMAT_LOCALIZATION = new ThingTypeUID(BINDING_ID,
            "timeFormatLocalization");
    public static final ThingTypeUID THING_TYPE_UNIT_LOCALIZATION = new ThingTypeUID(BINDING_ID, "unitLocalization");
    public static final ThingTypeUID THING_TYPE_POWER_SOURCE_CONFIGURATION = new ThingTypeUID(BINDING_ID,
            "powerSourceConfiguration");
    public static final ThingTypeUID THING_TYPE_POWER_SOURCE = new ThingTypeUID(BINDING_ID, "powerSource");
    public static final ThingTypeUID THING_TYPE_GENERAL_COMMISSIONING = new ThingTypeUID(BINDING_ID,
            "generalCommissioning");
    public static final ThingTypeUID THING_TYPE_NETWORK_COMMISSIONING = new ThingTypeUID(BINDING_ID,
            "networkCommissioning");
    public static final ThingTypeUID THING_TYPE_DIAGNOSTIC_LOGS = new ThingTypeUID(BINDING_ID, "diagnosticLogs");
    public static final ThingTypeUID THING_TYPE_GENERAL_DIAGNOSTICS = new ThingTypeUID(BINDING_ID,
            "generalDiagnostics");
    public static final ThingTypeUID THING_TYPE_SOFTWARE_DIAGNOSTICS = new ThingTypeUID(BINDING_ID,
            "softwareDiagnostics");
    public static final ThingTypeUID THING_TYPE_THREAD_NETWORK_DIAGNOSTICS = new ThingTypeUID(BINDING_ID,
            "threadNetworkDiagnostics");
    public static final ThingTypeUID THING_TYPE_WIFI_NETWORK_DIAGNOSTICS = new ThingTypeUID(BINDING_ID,
            "wiFiNetworkDiagnostics");
    public static final ThingTypeUID THING_TYPE_ETHERNET_NETWORK_DIAGNOSTICS = new ThingTypeUID(BINDING_ID,
            "ethernetNetworkDiagnostics");
    public static final ThingTypeUID THING_TYPE_TIME_SYNCHRONIZATION = new ThingTypeUID(BINDING_ID,
            "timeSynchronization");
    public static final ThingTypeUID THING_TYPE_SWITCH = new ThingTypeUID(BINDING_ID, "switch");
    public static final ThingTypeUID THING_TYPE_ADMINISTRATOR_COMMISSIONING = new ThingTypeUID(BINDING_ID,
            "administratorCommissioning");
    public static final ThingTypeUID THING_TYPE_OPERATIONAL_CREDENTIALS = new ThingTypeUID(BINDING_ID,
            "operationalCredentials");
    public static final ThingTypeUID THING_TYPE_GROUP_KEY_MANAGEMENT = new ThingTypeUID(BINDING_ID,
            "groupKeyManagement");
    public static final ThingTypeUID THING_TYPE_FIXED_LABEL = new ThingTypeUID(BINDING_ID, "fixedLabel");
    public static final ThingTypeUID THING_TYPE_USER_LABEL = new ThingTypeUID(BINDING_ID, "userLabel");
    public static final ThingTypeUID THING_TYPE_BOOLEAN_STATE = new ThingTypeUID(BINDING_ID, "booleanState");
    public static final ThingTypeUID THING_TYPE_MODE_SELECT = new ThingTypeUID(BINDING_ID, "modeSelect");
    public static final ThingTypeUID THING_TYPE_LAUNDRY_WASHER_MODE = new ThingTypeUID(BINDING_ID, "laundryWasherMode");
    public static final ThingTypeUID THING_TYPE_REFRIGERATOR_AND_TEMPERATURE_CONTROLLED_CABINET_MODE = new ThingTypeUID(
            BINDING_ID, "refrigeratorAndTemperatureControlledCabinetMode");
    public static final ThingTypeUID THING_TYPE_LAUNDRY_WASHER_CONTROLS = new ThingTypeUID(BINDING_ID,
            "laundryWasherControls");
    public static final ThingTypeUID THING_TYPE_RVC_RUN_MODE = new ThingTypeUID(BINDING_ID, "rvcRunMode");
    public static final ThingTypeUID THING_TYPE_RVC_CLEAN_MODE = new ThingTypeUID(BINDING_ID, "rvcCleanMode");
    public static final ThingTypeUID THING_TYPE_TEMPERATURE_CONTROL = new ThingTypeUID(BINDING_ID,
            "temperatureControl");
    public static final ThingTypeUID THING_TYPE_REFRIGERATOR_ALARM = new ThingTypeUID(BINDING_ID, "refrigeratorAlarm");
    public static final ThingTypeUID THING_TYPE_DISHWASHER_MODE = new ThingTypeUID(BINDING_ID, "dishwasherMode");
    public static final ThingTypeUID THING_TYPE_AIR_QUALITY = new ThingTypeUID(BINDING_ID, "airQuality");
    public static final ThingTypeUID THING_TYPE_SMOKE_CO_ALARM = new ThingTypeUID(BINDING_ID, "smokeCoAlarm");
    public static final ThingTypeUID THING_TYPE_DISHWASHER_ALARM = new ThingTypeUID(BINDING_ID, "dishwasherAlarm");
    public static final ThingTypeUID THING_TYPE_MICROWAVE_OVEN_MODE = new ThingTypeUID(BINDING_ID, "microwaveOvenMode");
    public static final ThingTypeUID THING_TYPE_OPERATIONAL_STATE = new ThingTypeUID(BINDING_ID, "operationalState");
    public static final ThingTypeUID THING_TYPE_RVC_OPERATIONAL_STATE = new ThingTypeUID(BINDING_ID,
            "rvcOperationalState");
    public static final ThingTypeUID THING_TYPE_HEPA_FILTER_MONITORING = new ThingTypeUID(BINDING_ID,
            "hepaFilterMonitoring");
    public static final ThingTypeUID THING_TYPE_ACTIVATED_CARBON_FILTER_MONITORING = new ThingTypeUID(BINDING_ID,
            "activatedCarbonFilterMonitoring");
    public static final ThingTypeUID THING_TYPE_WINDOW_COVERING = new ThingTypeUID(BINDING_ID, "windowCovering");
    public static final ThingTypeUID THING_TYPE_BARRIER_CONTROL = new ThingTypeUID(BINDING_ID, "barrierControl");
    public static final ThingTypeUID THING_TYPE_PUMP_CONFIGURATION_AND_CONTROL = new ThingTypeUID(BINDING_ID,
            "pumpConfigurationAndControl");
    public static final ThingTypeUID THING_TYPE_THERMOSTAT = new ThingTypeUID(BINDING_ID, "thermostat");
    public static final ThingTypeUID THING_TYPE_FAN_CONTROL = new ThingTypeUID(BINDING_ID, "fanControl");
    public static final ThingTypeUID THING_TYPE_THERMOSTAT_USER_INTERFACE_CONFIGURATION = new ThingTypeUID(BINDING_ID,
            "thermostatUserInterfaceConfiguration");
    public static final ThingTypeUID THING_TYPE_COLOR_CONTROL = new ThingTypeUID(BINDING_ID, "colorControl");
    public static final ThingTypeUID THING_TYPE_BALLAST_CONFIGURATION = new ThingTypeUID(BINDING_ID,
            "ballastConfiguration");
    public static final ThingTypeUID THING_TYPE_ILLUMINANCE_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "illuminanceMeasurement");
    public static final ThingTypeUID THING_TYPE_TEMPERATURE_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "temperatureMeasurement");
    public static final ThingTypeUID THING_TYPE_PRESSURE_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "pressureMeasurement");
    public static final ThingTypeUID THING_TYPE_FLOW_MEASUREMENT = new ThingTypeUID(BINDING_ID, "flowMeasurement");
    public static final ThingTypeUID THING_TYPE_RELATIVE_HUMIDITY_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "relativeHumidityMeasurement");
    public static final ThingTypeUID THING_TYPE_OCCUPANCY_SENSING = new ThingTypeUID(BINDING_ID, "occupancySensing");
    public static final ThingTypeUID THING_TYPE_CARBON_MONOXIDE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "carbonMonoxideConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_CARBON_DIOXIDE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "carbonDioxideConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_NITROGEN_DIOXIDE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            BINDING_ID, "nitrogenDioxideConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_OZONE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "ozoneConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_PM2_5_CONCENTRATION_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "pm25ConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_FORMALDEHYDE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "formaldehydeConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_PM1_CONCENTRATION_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "pm1ConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_PM10_CONCENTRATION_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "pm10ConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_TOTAL_VOLATILE_ORGANIC_COMPOUNDS_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            BINDING_ID, "totalVolatileOrganicCompoundsConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_RADON_CONCENTRATION_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "radonConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_WAKE_ON_LAN = new ThingTypeUID(BINDING_ID, "wakeOnLan");
    public static final ThingTypeUID THING_TYPE_LOW_POWER = new ThingTypeUID(BINDING_ID, "lowPower");
    public static final ThingTypeUID THING_TYPE_ELECTRICAL_MEASUREMENT = new ThingTypeUID(BINDING_ID,
            "electricalMeasurement");
    public static final ThingTypeUID THING_TYPE_UNIT_TESTING = new ThingTypeUID(BINDING_ID, "unitTesting");
    public static final ThingTypeUID THING_TYPE_FAULT_INJECTION = new ThingTypeUID(BINDING_ID, "faultInjection");

    public static final Map<ThingTypeUID, String> THING_TYPE_HANDLER_MAPPING = Map.ofEntries(
            entry(THING_TYPE_IDENTIFY, "IdentifyHandler"), entry(THING_TYPE_GROUPS, "GroupsHandler"),
            entry(THING_TYPE_SCENES, "ScenesHandler"), entry(THING_TYPE_ON_OFF, "OnOffHandler"),
            entry(THING_TYPE_ON_OFF_SWITCH_CONFIGURATION, "OnOffSwitchConfigurationHandler"),
            entry(THING_TYPE_LEVEL_CONTROL, "LevelControlHandler"),
            entry(THING_TYPE_BINARY_INPUT__BASIC_, "BinaryInputBasicHandler"),
            entry(THING_TYPE_DESCRIPTOR, "DescriptorHandler"), entry(THING_TYPE_BINDING, "BindingHandler"),
            entry(THING_TYPE_ACCESS_CONTROL, "AccessControlHandler"), entry(THING_TYPE_ACTIONS, "ActionsHandler"),
            entry(THING_TYPE_BASIC_INFORMATION, "BasicInformationHandler"),
            entry(THING_TYPE_OTA_SOFTWARE_UPDATE_PROVIDER, "OtaSoftwareUpdateProviderHandler"),
            entry(THING_TYPE_OTA_SOFTWARE_UPDATE_REQUESTOR, "OtaSoftwareUpdateRequestorHandler"),
            entry(THING_TYPE_LOCALIZATION_CONFIGURATION, "LocalizationConfigurationHandler"),
            entry(THING_TYPE_TIME_FORMAT_LOCALIZATION, "TimeFormatLocalizationHandler"),
            entry(THING_TYPE_UNIT_LOCALIZATION, "UnitLocalizationHandler"),
            entry(THING_TYPE_POWER_SOURCE_CONFIGURATION, "PowerSourceConfigurationHandler"),
            entry(THING_TYPE_POWER_SOURCE, "PowerSourceHandler"),
            entry(THING_TYPE_GENERAL_COMMISSIONING, "GeneralCommissioningHandler"),
            entry(THING_TYPE_NETWORK_COMMISSIONING, "NetworkCommissioningHandler"),
            entry(THING_TYPE_DIAGNOSTIC_LOGS, "DiagnosticLogsHandler"),
            entry(THING_TYPE_GENERAL_DIAGNOSTICS, "GeneralDiagnosticsHandler"),
            entry(THING_TYPE_SOFTWARE_DIAGNOSTICS, "SoftwareDiagnosticsHandler"),
            entry(THING_TYPE_THREAD_NETWORK_DIAGNOSTICS, "ThreadNetworkDiagnosticsHandler"),
            entry(THING_TYPE_WIFI_NETWORK_DIAGNOSTICS, "WiFiNetworkDiagnosticsHandler"),
            entry(THING_TYPE_ETHERNET_NETWORK_DIAGNOSTICS, "EthernetNetworkDiagnosticsHandler"),
            entry(THING_TYPE_TIME_SYNCHRONIZATION, "TimeSynchronizationHandler"),
            entry(THING_TYPE_SWITCH, "SwitchHandler"),
            entry(THING_TYPE_ADMINISTRATOR_COMMISSIONING, "AdministratorCommissioningHandler"),
            entry(THING_TYPE_OPERATIONAL_CREDENTIALS, "OperationalCredentialsHandler"),
            entry(THING_TYPE_GROUP_KEY_MANAGEMENT, "GroupKeyManagementHandler"),
            entry(THING_TYPE_FIXED_LABEL, "FixedLabelHandler"), entry(THING_TYPE_USER_LABEL, "UserLabelHandler"),
            entry(THING_TYPE_BOOLEAN_STATE, "BooleanStateHandler"), entry(THING_TYPE_MODE_SELECT, "ModeSelectHandler"),
            entry(THING_TYPE_LAUNDRY_WASHER_MODE, "LaundryWasherModeHandler"),
            entry(THING_TYPE_REFRIGERATOR_AND_TEMPERATURE_CONTROLLED_CABINET_MODE,
                    "RefrigeratorAndTemperatureControlledCabinetModeHandler"),
            entry(THING_TYPE_LAUNDRY_WASHER_CONTROLS, "LaundryWasherControlsHandler"),
            entry(THING_TYPE_RVC_RUN_MODE, "RvcRunModeHandler"),
            entry(THING_TYPE_RVC_CLEAN_MODE, "RvcCleanModeHandler"),
            entry(THING_TYPE_TEMPERATURE_CONTROL, "TemperatureControlHandler"),
            entry(THING_TYPE_REFRIGERATOR_ALARM, "RefrigeratorAlarmHandler"),
            entry(THING_TYPE_DISHWASHER_MODE, "DishwasherModeHandler"),
            entry(THING_TYPE_AIR_QUALITY, "AirQualityHandler"), entry(THING_TYPE_SMOKE_CO_ALARM, "SmokeCoAlarmHandler"),
            entry(THING_TYPE_DISHWASHER_ALARM, "DishwasherAlarmHandler"),
            entry(THING_TYPE_MICROWAVE_OVEN_MODE, "MicrowaveOvenModeHandler"),
            entry(THING_TYPE_OPERATIONAL_STATE, "OperationalStateHandler"),
            entry(THING_TYPE_RVC_OPERATIONAL_STATE, "RvcOperationalStateHandler"),
            entry(THING_TYPE_HEPA_FILTER_MONITORING, "HepaFilterMonitoringHandler"),
            entry(THING_TYPE_ACTIVATED_CARBON_FILTER_MONITORING, "ActivatedCarbonFilterMonitoringHandler"),
            entry(THING_TYPE_WINDOW_COVERING, "WindowCoveringHandler"),
            entry(THING_TYPE_BARRIER_CONTROL, "BarrierControlHandler"),
            entry(THING_TYPE_PUMP_CONFIGURATION_AND_CONTROL, "PumpConfigurationAndControlHandler"),
            entry(THING_TYPE_THERMOSTAT, "ThermostatHandler"), entry(THING_TYPE_FAN_CONTROL, "FanControlHandler"),
            entry(THING_TYPE_THERMOSTAT_USER_INTERFACE_CONFIGURATION, "ThermostatUserInterfaceConfigurationHandler"),
            entry(THING_TYPE_COLOR_CONTROL, "ColorControlHandler"),
            entry(THING_TYPE_BALLAST_CONFIGURATION, "BallastConfigurationHandler"),
            entry(THING_TYPE_ILLUMINANCE_MEASUREMENT, "IlluminanceMeasurementHandler"),
            entry(THING_TYPE_TEMPERATURE_MEASUREMENT, "TemperatureMeasurementHandler"),
            entry(THING_TYPE_PRESSURE_MEASUREMENT, "PressureMeasurementHandler"),
            entry(THING_TYPE_FLOW_MEASUREMENT, "FlowMeasurementHandler"),
            entry(THING_TYPE_RELATIVE_HUMIDITY_MEASUREMENT, "RelativeHumidityMeasurementHandler"),
            entry(THING_TYPE_OCCUPANCY_SENSING, "OccupancySensingHandler"),
            entry(THING_TYPE_CARBON_MONOXIDE_CONCENTRATION_MEASUREMENT,
                    "CarbonMonoxideConcentrationMeasurementHandler"),
            entry(THING_TYPE_CARBON_DIOXIDE_CONCENTRATION_MEASUREMENT, "CarbonDioxideConcentrationMeasurementHandler"),
            entry(THING_TYPE_NITROGEN_DIOXIDE_CONCENTRATION_MEASUREMENT,
                    "NitrogenDioxideConcentrationMeasurementHandler"),
            entry(THING_TYPE_OZONE_CONCENTRATION_MEASUREMENT, "OzoneConcentrationMeasurementHandler"),
            entry(THING_TYPE_PM2_5_CONCENTRATION_MEASUREMENT, "Pm25ConcentrationMeasurementHandler"),
            entry(THING_TYPE_FORMALDEHYDE_CONCENTRATION_MEASUREMENT, "FormaldehydeConcentrationMeasurementHandler"),
            entry(THING_TYPE_PM1_CONCENTRATION_MEASUREMENT, "Pm1ConcentrationMeasurementHandler"),
            entry(THING_TYPE_PM10_CONCENTRATION_MEASUREMENT, "Pm10ConcentrationMeasurementHandler"),
            entry(THING_TYPE_TOTAL_VOLATILE_ORGANIC_COMPOUNDS_CONCENTRATION_MEASUREMENT,
                    "TotalVolatileOrganicCompoundsConcentrationMeasurementHandler"),
            entry(THING_TYPE_RADON_CONCENTRATION_MEASUREMENT, "RadonConcentrationMeasurementHandler"),
            entry(THING_TYPE_WAKE_ON_LAN, "WakeOnLanHandler"), entry(THING_TYPE_LOW_POWER, "LowPowerHandler"),
            entry(THING_TYPE_ELECTRICAL_MEASUREMENT, "ElectricalMeasurementHandler"),
            entry(THING_TYPE_UNIT_TESTING, "UnitTestingHandler"),
            entry(THING_TYPE_FAULT_INJECTION, "FaultInjectionHandler"));
}
