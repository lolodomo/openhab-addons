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

import static org.openhab.core.thing.DefaultSystemChannelTypeProvider.SYSTEM_COLOR_TEMPERATURE;

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
    // public static final ThingTypeUID THING_TYPE_NODE = new ThingTypeUID(BINDING_ID, "node");
    public static final ThingTypeUID THING_TYPE_ENDPOINT = new ThingTypeUID(BINDING_ID, "endpoint");

    // List of all Channel ids
    public static final String CHANNEL_PAIR_CODE = "pair_code";
    public static final String CHANNEL_COMMAND = "command";

    // This was borrowed from the zigbee binding as Matter uses the same cluster API model
    // List of Channel UIDs
    public static final String CHANNEL_NAME_ONOFF_ONOFF = "onoff";
    public static final String CHANNEL_LABEL_ONOFF_ONOFF = "On Off";
    public static final ChannelTypeUID CHANNEL_ONOFF_ONOFF = new ChannelTypeUID("matter:onoffcontrol_onoff");

    public static final String CHANNEL_NAME_LEVEL_LEVEL = "level";
    public static final String CHANNEL_LABEL_LEVEL_LEVEL = "Level Control";
    public static final ChannelTypeUID CHANNEL_LEVEL_LEVEL = new ChannelTypeUID("matter:levelcontrol_level");

    public static final String CHANNEL_NAME_WARNING_DEVICE = "warning_device";
    public static final String CHANNEL_LABEL_WARNING_DEVICE = "Warning Device";
    public static final ChannelTypeUID CHANNEL_WARNING_DEVICE = new ChannelTypeUID("matter:warning_device");

    public static final String CHANNEL_NAME_COLOR_COLOR = "color";
    public static final String CHANNEL_LABEL_COLOR_COLOR = "Color Control";
    public static final ChannelTypeUID CHANNEL_COLOR_COLOR = new ChannelTypeUID("matter:colorcontrol_color");

    public static final String CHANNEL_NAME_COLOR_TEMPERATURE = "colortemperature";
    public static final String CHANNEL_LABEL_COLOR_TEMPERATURE = "Color Temperature";
    public static final ChannelTypeUID CHANNEL_COLOR_TEMPERATURE = SYSTEM_COLOR_TEMPERATURE.getUID();

    public static final String CHANNEL_NAME_FANCONTROL = "fancontrol";
    public static final String CHANNEL_LABEL_FANCONTROL = "Fan Control";
    public static final ChannelTypeUID CHANNEL_FANCONTROL = new ChannelTypeUID("matter:fancontrol");

    public static final String CHANNEL_NAME_POWER_BATTERYPERCENT = "batterylevel";
    public static final String CHANNEL_LABEL_POWER_BATTERYPERCENT = "Battery Level";
    public static final ChannelTypeUID CHANNEL_POWER_BATTERYPERCENT = new ChannelTypeUID("system:battery-level");

    public static final String CHANNEL_NAME_POWER_BATTERYVOLTAGE = "batteryvoltage";
    public static final String CHANNEL_LABEL_POWER_BATTERYVOLTAGE = "Battery Voltage";
    public static final ChannelTypeUID CHANNEL_POWER_BATTERYVOLTAGE = new ChannelTypeUID("matter:battery_voltage");

    public static final String CHANNEL_NAME_POWER_BATTERYALARM = "batteryalarm";
    public static final String CHANNEL_LABEL_POWER_BATTERYALARM = "Battery Alarm";
    public static final ChannelTypeUID CHANNEL_POWER_BATTERYALARM = new ChannelTypeUID("matter:battery_alarm");

    public static final String CHANNEL_NAME_THERMOSTAT_LOCALTEMPERATURE = "thermostatlocaltemp";
    public static final String CHANNEL_LABEL_THERMOSTAT_LOCALTEMPERATURE = "Local Temperature";
    public static final String CHANNEL_ID_THERMOSTAT_LOCALTEMPERATURE = "thermostat_localtemp";
    public static final ChannelTypeUID CHANNEL_THERMOSTAT_LOCALTEMPERATURE = new ChannelTypeUID(
            "matter:" + CHANNEL_ID_THERMOSTAT_LOCALTEMPERATURE);

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

    public static final String ITEM_TYPE_COLOR = "Color";
    public static final String ITEM_TYPE_CONTACT = "Contact";
    public static final String ITEM_TYPE_DIMMER = "Dimmer";
    public static final String ITEM_TYPE_NUMBER = "Number";
    public static final String ITEM_TYPE_NUMBER_PRESSURE = "Number:Pressure";
    public static final String ITEM_TYPE_NUMBER_TEMPERATURE = "Number:Temperature";
    public static final String ITEM_TYPE_ROLLERSHUTTER = "Rollershutter";
    public static final String ITEM_TYPE_SWITCH = "Switch";
    public static final String ITEM_TYPE_STRING = "String";
}
