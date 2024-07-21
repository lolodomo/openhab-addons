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

import static java.util.Map.entry;

import java.util.Map;
import java.util.Set;

import org.openhab.binding.matter.internal.MatterBindingConstants;
import org.openhab.core.thing.ThingTypeUID;

/**
 * AUTO-GENERATED by zap. DO NOT EDIT!
 *
 * ClusterThingTypes
 * 
 * @author Dan Cunningham - Initial contribution
 */
public class ClusterThingTypes {
    public static final ThingTypeUID THING_TYPE_IDENTIFY = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "identify");
    public static final ThingTypeUID THING_TYPE_GROUPS = new ThingTypeUID(MatterBindingConstants.BINDING_ID, "groups");
    public static final ThingTypeUID THING_TYPE_SCENES = new ThingTypeUID(MatterBindingConstants.BINDING_ID, "scenes");
    public static final ThingTypeUID THING_TYPE_ON_OFF = new ThingTypeUID(MatterBindingConstants.BINDING_ID, "onOff");
    public static final ThingTypeUID THING_TYPE_ON_OFF_SWITCH_CONFIGURATION = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "onOffSwitchConfiguration");
    public static final ThingTypeUID THING_TYPE_LEVEL_CONTROL = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "levelControl");
    public static final ThingTypeUID THING_TYPE_BINARY_INPUT__BASIC_ = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "binaryInputBasic");
    public static final ThingTypeUID THING_TYPE_DESCRIPTOR = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "descriptor");
    public static final ThingTypeUID THING_TYPE_BINDING = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "binding");
    public static final ThingTypeUID THING_TYPE_ACCESS_CONTROL = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "accessControl");
    public static final ThingTypeUID THING_TYPE_ACTIONS = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "actions");
    public static final ThingTypeUID THING_TYPE_BASIC_INFORMATION = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "basicInformation");
    public static final ThingTypeUID THING_TYPE_OTA_SOFTWARE_UPDATE_PROVIDER = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "otaSoftwareUpdateProvider");
    public static final ThingTypeUID THING_TYPE_OTA_SOFTWARE_UPDATE_REQUESTOR = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "otaSoftwareUpdateRequestor");
    public static final ThingTypeUID THING_TYPE_LOCALIZATION_CONFIGURATION = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "localizationConfiguration");
    public static final ThingTypeUID THING_TYPE_TIME_FORMAT_LOCALIZATION = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "timeFormatLocalization");
    public static final ThingTypeUID THING_TYPE_UNIT_LOCALIZATION = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "unitLocalization");
    public static final ThingTypeUID THING_TYPE_POWER_SOURCE_CONFIGURATION = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "powerSourceConfiguration");
    public static final ThingTypeUID THING_TYPE_POWER_SOURCE = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "powerSource");
    public static final ThingTypeUID THING_TYPE_GENERAL_COMMISSIONING = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "generalCommissioning");
    public static final ThingTypeUID THING_TYPE_NETWORK_COMMISSIONING = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "networkCommissioning");
    public static final ThingTypeUID THING_TYPE_DIAGNOSTIC_LOGS = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "diagnosticLogs");
    public static final ThingTypeUID THING_TYPE_GENERAL_DIAGNOSTICS = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "generalDiagnostics");
    public static final ThingTypeUID THING_TYPE_SOFTWARE_DIAGNOSTICS = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "softwareDiagnostics");
    public static final ThingTypeUID THING_TYPE_THREAD_NETWORK_DIAGNOSTICS = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "threadNetworkDiagnostics");
    public static final ThingTypeUID THING_TYPE_WIFI_NETWORK_DIAGNOSTICS = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "wiFiNetworkDiagnostics");
    public static final ThingTypeUID THING_TYPE_ETHERNET_NETWORK_DIAGNOSTICS = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "ethernetNetworkDiagnostics");
    public static final ThingTypeUID THING_TYPE_TIME_SYNCHRONIZATION = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "timeSynchronization");
    public static final ThingTypeUID THING_TYPE_SWITCH = new ThingTypeUID(MatterBindingConstants.BINDING_ID, "switch");
    public static final ThingTypeUID THING_TYPE_ADMINISTRATOR_COMMISSIONING = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "administratorCommissioning");
    public static final ThingTypeUID THING_TYPE_OPERATIONAL_CREDENTIALS = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "operationalCredentials");
    public static final ThingTypeUID THING_TYPE_GROUP_KEY_MANAGEMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "groupKeyManagement");
    public static final ThingTypeUID THING_TYPE_FIXED_LABEL = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "fixedLabel");
    public static final ThingTypeUID THING_TYPE_USER_LABEL = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "userLabel");
    public static final ThingTypeUID THING_TYPE_BOOLEAN_STATE = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "booleanState");
    public static final ThingTypeUID THING_TYPE_MODE_SELECT = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "modeSelect");
    public static final ThingTypeUID THING_TYPE_LAUNDRY_WASHER_MODE = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "laundryWasherMode");
    public static final ThingTypeUID THING_TYPE_REFRIGERATOR_AND_TEMPERATURE_CONTROLLED_CABINET_MODE = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "refrigeratorAndTemperatureControlledCabinetMode");
    public static final ThingTypeUID THING_TYPE_LAUNDRY_WASHER_CONTROLS = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "laundryWasherControls");
    public static final ThingTypeUID THING_TYPE_RVC_RUN_MODE = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "rvcRunMode");
    public static final ThingTypeUID THING_TYPE_RVC_CLEAN_MODE = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "rvcCleanMode");
    public static final ThingTypeUID THING_TYPE_TEMPERATURE_CONTROL = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "temperatureControl");
    public static final ThingTypeUID THING_TYPE_REFRIGERATOR_ALARM = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "refrigeratorAlarm");
    public static final ThingTypeUID THING_TYPE_DISHWASHER_MODE = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "dishwasherMode");
    public static final ThingTypeUID THING_TYPE_AIR_QUALITY = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "airQuality");
    public static final ThingTypeUID THING_TYPE_SMOKE_CO_ALARM = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "smokeCoAlarm");
    public static final ThingTypeUID THING_TYPE_DISHWASHER_ALARM = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "dishwasherAlarm");
    public static final ThingTypeUID THING_TYPE_MICROWAVE_OVEN_MODE = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "microwaveOvenMode");
    public static final ThingTypeUID THING_TYPE_OPERATIONAL_STATE = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "operationalState");
    public static final ThingTypeUID THING_TYPE_RVC_OPERATIONAL_STATE = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "rvcOperationalState");
    public static final ThingTypeUID THING_TYPE_HEPA_FILTER_MONITORING = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "hepaFilterMonitoring");
    public static final ThingTypeUID THING_TYPE_ACTIVATED_CARBON_FILTER_MONITORING = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "activatedCarbonFilterMonitoring");
    public static final ThingTypeUID THING_TYPE_WINDOW_COVERING = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "windowCovering");
    public static final ThingTypeUID THING_TYPE_BARRIER_CONTROL = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "barrierControl");
    public static final ThingTypeUID THING_TYPE_PUMP_CONFIGURATION_AND_CONTROL = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "pumpConfigurationAndControl");
    public static final ThingTypeUID THING_TYPE_THERMOSTAT = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "thermostat");
    public static final ThingTypeUID THING_TYPE_FAN_CONTROL = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "fanControl");
    public static final ThingTypeUID THING_TYPE_THERMOSTAT_USER_INTERFACE_CONFIGURATION = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "thermostatUserInterfaceConfiguration");
    public static final ThingTypeUID THING_TYPE_COLOR_CONTROL = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "colorControl");
    public static final ThingTypeUID THING_TYPE_BALLAST_CONFIGURATION = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "ballastConfiguration");
    public static final ThingTypeUID THING_TYPE_ILLUMINANCE_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "illuminanceMeasurement");
    public static final ThingTypeUID THING_TYPE_TEMPERATURE_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "temperatureMeasurement");
    public static final ThingTypeUID THING_TYPE_PRESSURE_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "pressureMeasurement");
    public static final ThingTypeUID THING_TYPE_FLOW_MEASUREMENT = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "flowMeasurement");
    public static final ThingTypeUID THING_TYPE_RELATIVE_HUMIDITY_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "relativeHumidityMeasurement");
    public static final ThingTypeUID THING_TYPE_OCCUPANCY_SENSING = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "occupancySensing");
    public static final ThingTypeUID THING_TYPE_CARBON_MONOXIDE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "carbonMonoxideConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_CARBON_DIOXIDE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "carbonDioxideConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_NITROGEN_DIOXIDE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "nitrogenDioxideConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_OZONE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "ozoneConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_PM2_5_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "pm25ConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_FORMALDEHYDE_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "formaldehydeConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_PM1_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "pm1ConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_PM10_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "pm10ConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_TOTAL_VOLATILE_ORGANIC_COMPOUNDS_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "totalVolatileOrganicCompoundsConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_RADON_CONCENTRATION_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "radonConcentrationMeasurement");
    public static final ThingTypeUID THING_TYPE_WAKE_ON_LAN = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "wakeOnLan");
    public static final ThingTypeUID THING_TYPE_LOW_POWER = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "lowPower");
    public static final ThingTypeUID THING_TYPE_ELECTRICAL_MEASUREMENT = new ThingTypeUID(
            MatterBindingConstants.BINDING_ID, "electricalMeasurement");
    public static final ThingTypeUID THING_TYPE_UNIT_TESTING = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "unitTesting");
    public static final ThingTypeUID THING_TYPE_FAULT_INJECTION = new ThingTypeUID(MatterBindingConstants.BINDING_ID,
            "faultInjection");

    public static final Map<ThingTypeUID, String> THING_TYPE_TO_HANDLER_MAPPING = Map.ofEntries(
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

    public static final Map<String, ThingTypeUID> CLUSTER_NAME_TO_THING_TYPE_MAPPING = Map.ofEntries(
            entry("Identify", THING_TYPE_IDENTIFY), entry("Groups", THING_TYPE_GROUPS),
            entry("Scenes", THING_TYPE_SCENES), entry("OnOff", THING_TYPE_ON_OFF),
            entry("OnOffSwitchConfiguration", THING_TYPE_ON_OFF_SWITCH_CONFIGURATION),
            entry("LevelControl", THING_TYPE_LEVEL_CONTROL), entry("BinaryInputBasic", THING_TYPE_BINARY_INPUT__BASIC_),
            entry("Descriptor", THING_TYPE_DESCRIPTOR), entry("Binding", THING_TYPE_BINDING),
            entry("AccessControl", THING_TYPE_ACCESS_CONTROL), entry("Actions", THING_TYPE_ACTIONS),
            entry("BasicInformation", THING_TYPE_BASIC_INFORMATION),
            entry("OtaSoftwareUpdateProvider", THING_TYPE_OTA_SOFTWARE_UPDATE_PROVIDER),
            entry("OtaSoftwareUpdateRequestor", THING_TYPE_OTA_SOFTWARE_UPDATE_REQUESTOR),
            entry("LocalizationConfiguration", THING_TYPE_LOCALIZATION_CONFIGURATION),
            entry("TimeFormatLocalization", THING_TYPE_TIME_FORMAT_LOCALIZATION),
            entry("UnitLocalization", THING_TYPE_UNIT_LOCALIZATION),
            entry("PowerSourceConfiguration", THING_TYPE_POWER_SOURCE_CONFIGURATION),
            entry("PowerSource", THING_TYPE_POWER_SOURCE),
            entry("GeneralCommissioning", THING_TYPE_GENERAL_COMMISSIONING),
            entry("NetworkCommissioning", THING_TYPE_NETWORK_COMMISSIONING),
            entry("DiagnosticLogs", THING_TYPE_DIAGNOSTIC_LOGS),
            entry("GeneralDiagnostics", THING_TYPE_GENERAL_DIAGNOSTICS),
            entry("SoftwareDiagnostics", THING_TYPE_SOFTWARE_DIAGNOSTICS),
            entry("ThreadNetworkDiagnostics", THING_TYPE_THREAD_NETWORK_DIAGNOSTICS),
            entry("WiFiNetworkDiagnostics", THING_TYPE_WIFI_NETWORK_DIAGNOSTICS),
            entry("EthernetNetworkDiagnostics", THING_TYPE_ETHERNET_NETWORK_DIAGNOSTICS),
            entry("TimeSynchronization", THING_TYPE_TIME_SYNCHRONIZATION), entry("Switch", THING_TYPE_SWITCH),
            entry("AdministratorCommissioning", THING_TYPE_ADMINISTRATOR_COMMISSIONING),
            entry("OperationalCredentials", THING_TYPE_OPERATIONAL_CREDENTIALS),
            entry("GroupKeyManagement", THING_TYPE_GROUP_KEY_MANAGEMENT), entry("FixedLabel", THING_TYPE_FIXED_LABEL),
            entry("UserLabel", THING_TYPE_USER_LABEL), entry("BooleanState", THING_TYPE_BOOLEAN_STATE),
            entry("ModeSelect", THING_TYPE_MODE_SELECT), entry("LaundryWasherMode", THING_TYPE_LAUNDRY_WASHER_MODE),
            entry("RefrigeratorAndTemperatureControlledCabinetMode",
                    THING_TYPE_REFRIGERATOR_AND_TEMPERATURE_CONTROLLED_CABINET_MODE),
            entry("LaundryWasherControls", THING_TYPE_LAUNDRY_WASHER_CONTROLS),
            entry("RvcRunMode", THING_TYPE_RVC_RUN_MODE), entry("RvcCleanMode", THING_TYPE_RVC_CLEAN_MODE),
            entry("TemperatureControl", THING_TYPE_TEMPERATURE_CONTROL),
            entry("RefrigeratorAlarm", THING_TYPE_REFRIGERATOR_ALARM),
            entry("DishwasherMode", THING_TYPE_DISHWASHER_MODE), entry("AirQuality", THING_TYPE_AIR_QUALITY),
            entry("SmokeCoAlarm", THING_TYPE_SMOKE_CO_ALARM), entry("DishwasherAlarm", THING_TYPE_DISHWASHER_ALARM),
            entry("MicrowaveOvenMode", THING_TYPE_MICROWAVE_OVEN_MODE),
            entry("OperationalState", THING_TYPE_OPERATIONAL_STATE),
            entry("RvcOperationalState", THING_TYPE_RVC_OPERATIONAL_STATE),
            entry("HepaFilterMonitoring", THING_TYPE_HEPA_FILTER_MONITORING),
            entry("ActivatedCarbonFilterMonitoring", THING_TYPE_ACTIVATED_CARBON_FILTER_MONITORING),
            entry("WindowCovering", THING_TYPE_WINDOW_COVERING), entry("BarrierControl", THING_TYPE_BARRIER_CONTROL),
            entry("PumpConfigurationAndControl", THING_TYPE_PUMP_CONFIGURATION_AND_CONTROL),
            entry("Thermostat", THING_TYPE_THERMOSTAT), entry("FanControl", THING_TYPE_FAN_CONTROL),
            entry("ThermostatUserInterfaceConfiguration", THING_TYPE_THERMOSTAT_USER_INTERFACE_CONFIGURATION),
            entry("ColorControl", THING_TYPE_COLOR_CONTROL),
            entry("BallastConfiguration", THING_TYPE_BALLAST_CONFIGURATION),
            entry("IlluminanceMeasurement", THING_TYPE_ILLUMINANCE_MEASUREMENT),
            entry("TemperatureMeasurement", THING_TYPE_TEMPERATURE_MEASUREMENT),
            entry("PressureMeasurement", THING_TYPE_PRESSURE_MEASUREMENT),
            entry("FlowMeasurement", THING_TYPE_FLOW_MEASUREMENT),
            entry("RelativeHumidityMeasurement", THING_TYPE_RELATIVE_HUMIDITY_MEASUREMENT),
            entry("OccupancySensing", THING_TYPE_OCCUPANCY_SENSING),
            entry("CarbonMonoxideConcentrationMeasurement", THING_TYPE_CARBON_MONOXIDE_CONCENTRATION_MEASUREMENT),
            entry("CarbonDioxideConcentrationMeasurement", THING_TYPE_CARBON_DIOXIDE_CONCENTRATION_MEASUREMENT),
            entry("NitrogenDioxideConcentrationMeasurement", THING_TYPE_NITROGEN_DIOXIDE_CONCENTRATION_MEASUREMENT),
            entry("OzoneConcentrationMeasurement", THING_TYPE_OZONE_CONCENTRATION_MEASUREMENT),
            entry("Pm25ConcentrationMeasurement", THING_TYPE_PM2_5_CONCENTRATION_MEASUREMENT),
            entry("FormaldehydeConcentrationMeasurement", THING_TYPE_FORMALDEHYDE_CONCENTRATION_MEASUREMENT),
            entry("Pm1ConcentrationMeasurement", THING_TYPE_PM1_CONCENTRATION_MEASUREMENT),
            entry("Pm10ConcentrationMeasurement", THING_TYPE_PM10_CONCENTRATION_MEASUREMENT),
            entry("TotalVolatileOrganicCompoundsConcentrationMeasurement",
                    THING_TYPE_TOTAL_VOLATILE_ORGANIC_COMPOUNDS_CONCENTRATION_MEASUREMENT),
            entry("RadonConcentrationMeasurement", THING_TYPE_RADON_CONCENTRATION_MEASUREMENT),
            entry("WakeOnLan", THING_TYPE_WAKE_ON_LAN), entry("LowPower", THING_TYPE_LOW_POWER),
            entry("ElectricalMeasurement", THING_TYPE_ELECTRICAL_MEASUREMENT),
            entry("UnitTesting", THING_TYPE_UNIT_TESTING), entry("FaultInjection", THING_TYPE_FAULT_INJECTION));

    public static final Set<ThingTypeUID> SUPPORTED_DISCOVERY_THING_TYPES_UIDS = Set.of(THING_TYPE_IDENTIFY,
            THING_TYPE_GROUPS, THING_TYPE_SCENES, THING_TYPE_ON_OFF, THING_TYPE_ON_OFF_SWITCH_CONFIGURATION,
            THING_TYPE_LEVEL_CONTROL, THING_TYPE_BINARY_INPUT__BASIC_, THING_TYPE_DESCRIPTOR, THING_TYPE_BINDING,
            THING_TYPE_ACCESS_CONTROL, THING_TYPE_ACTIONS, THING_TYPE_BASIC_INFORMATION,
            THING_TYPE_OTA_SOFTWARE_UPDATE_PROVIDER, THING_TYPE_OTA_SOFTWARE_UPDATE_REQUESTOR,
            THING_TYPE_LOCALIZATION_CONFIGURATION, THING_TYPE_TIME_FORMAT_LOCALIZATION, THING_TYPE_UNIT_LOCALIZATION,
            THING_TYPE_POWER_SOURCE_CONFIGURATION, THING_TYPE_POWER_SOURCE, THING_TYPE_GENERAL_COMMISSIONING,
            THING_TYPE_NETWORK_COMMISSIONING, THING_TYPE_DIAGNOSTIC_LOGS, THING_TYPE_GENERAL_DIAGNOSTICS,
            THING_TYPE_SOFTWARE_DIAGNOSTICS, THING_TYPE_THREAD_NETWORK_DIAGNOSTICS, THING_TYPE_WIFI_NETWORK_DIAGNOSTICS,
            THING_TYPE_ETHERNET_NETWORK_DIAGNOSTICS, THING_TYPE_TIME_SYNCHRONIZATION, THING_TYPE_SWITCH,
            THING_TYPE_ADMINISTRATOR_COMMISSIONING, THING_TYPE_OPERATIONAL_CREDENTIALS, THING_TYPE_GROUP_KEY_MANAGEMENT,
            THING_TYPE_FIXED_LABEL, THING_TYPE_USER_LABEL, THING_TYPE_BOOLEAN_STATE, THING_TYPE_MODE_SELECT,
            THING_TYPE_LAUNDRY_WASHER_MODE, THING_TYPE_REFRIGERATOR_AND_TEMPERATURE_CONTROLLED_CABINET_MODE,
            THING_TYPE_LAUNDRY_WASHER_CONTROLS, THING_TYPE_RVC_RUN_MODE, THING_TYPE_RVC_CLEAN_MODE,
            THING_TYPE_TEMPERATURE_CONTROL, THING_TYPE_REFRIGERATOR_ALARM, THING_TYPE_DISHWASHER_MODE,
            THING_TYPE_AIR_QUALITY, THING_TYPE_SMOKE_CO_ALARM, THING_TYPE_DISHWASHER_ALARM,
            THING_TYPE_MICROWAVE_OVEN_MODE, THING_TYPE_OPERATIONAL_STATE, THING_TYPE_RVC_OPERATIONAL_STATE,
            THING_TYPE_HEPA_FILTER_MONITORING, THING_TYPE_ACTIVATED_CARBON_FILTER_MONITORING,
            THING_TYPE_WINDOW_COVERING, THING_TYPE_BARRIER_CONTROL, THING_TYPE_PUMP_CONFIGURATION_AND_CONTROL,
            THING_TYPE_THERMOSTAT, THING_TYPE_FAN_CONTROL, THING_TYPE_THERMOSTAT_USER_INTERFACE_CONFIGURATION,
            THING_TYPE_COLOR_CONTROL, THING_TYPE_BALLAST_CONFIGURATION, THING_TYPE_ILLUMINANCE_MEASUREMENT,
            THING_TYPE_TEMPERATURE_MEASUREMENT, THING_TYPE_PRESSURE_MEASUREMENT, THING_TYPE_FLOW_MEASUREMENT,
            THING_TYPE_RELATIVE_HUMIDITY_MEASUREMENT, THING_TYPE_OCCUPANCY_SENSING,
            THING_TYPE_CARBON_MONOXIDE_CONCENTRATION_MEASUREMENT, THING_TYPE_CARBON_DIOXIDE_CONCENTRATION_MEASUREMENT,
            THING_TYPE_NITROGEN_DIOXIDE_CONCENTRATION_MEASUREMENT, THING_TYPE_OZONE_CONCENTRATION_MEASUREMENT,
            THING_TYPE_PM2_5_CONCENTRATION_MEASUREMENT, THING_TYPE_FORMALDEHYDE_CONCENTRATION_MEASUREMENT,
            THING_TYPE_PM1_CONCENTRATION_MEASUREMENT, THING_TYPE_PM10_CONCENTRATION_MEASUREMENT,
            THING_TYPE_TOTAL_VOLATILE_ORGANIC_COMPOUNDS_CONCENTRATION_MEASUREMENT,
            THING_TYPE_RADON_CONCENTRATION_MEASUREMENT, THING_TYPE_WAKE_ON_LAN, THING_TYPE_LOW_POWER,
            THING_TYPE_ELECTRICAL_MEASUREMENT, THING_TYPE_UNIT_TESTING, THING_TYPE_FAULT_INJECTION);
}
