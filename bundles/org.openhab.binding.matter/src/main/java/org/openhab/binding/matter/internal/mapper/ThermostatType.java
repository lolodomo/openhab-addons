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
package org.openhab.binding.matter.internal.mapper;

import static org.openhab.binding.matter.internal.MatterBindingConstants.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.*;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.handler.EndpointHandler;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.StateDescription;
import org.openhab.core.types.StateDescriptionFragmentBuilder;
import org.openhab.core.types.StateOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dan Cunningham
 *
 */
@NonNullByDefault
public class ThermostatType extends DeviceType {
    private final Logger logger = LoggerFactory.getLogger(ThermostatType.class);

    public ThermostatType(EndpointHandler handler, Integer deviceType) {
        super(handler, deviceType);
    }

    public static List<Integer> supportedTypes() {
        return List.of(DeviceTypes.Thermostat);
    }

    @Override
    public List<Integer> supportedClusters() {
        return List.of(ThermostatCluster.CLUSTER_ID, OnOffCluster.CLUSTER_ID, FanControlCluster.CLUSTER_ID,
                OccupancySensingCluster.CLUSTER_ID);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        MatterWebsocketClient client = handler.getClient();
        if (client == null) {
            return;
        }
        String id = channelUID.getId();
        if (id.equals(CHANNEL_SWITCH_ONOFF.getId()) && command instanceof OnOffType onOffType) {
            sendOnOff(client, onOffType == OnOffType.ON);
        }
        if (id.equals(CHANNEL_THERMOSTAT_SYSTEMMODE.getId())) {
            writeAttribute(ThermostatCluster.CLUSTER_NAME, "systemMode", command.toString());
            return;
        }
        if (id.equals(CHANNEL_THERMOSTAT_OCCUPIEDHEATING.getId())) {
            writeAttribute(ThermostatCluster.CLUSTER_NAME, "occupiedHeatingSetpoint",
                    String.valueOf(temperatureToValue(command)));
            return;
        }
        if (id.equals(CHANNEL_THERMOSTAT_OCCUPIEDCOOLING.getId())) {
            writeAttribute(ThermostatCluster.CLUSTER_NAME, "occupiedCoolingSetpoint",
                    String.valueOf(temperatureToValue(command)));
            return;
        }
    }

    @Override
    public void updateCluster(BaseCluster cluster) {
        if (cluster instanceof OnOffCluster onOffCluster) {
            OnOffType onOff = OnOffType.from(Boolean.valueOf(onOffCluster.onOff));
            logger.debug("OnOff {}", onOff);
            updateState(CHANNEL_SWITCH_ONOFF, onOff);
        }
        if (cluster instanceof ThermostatCluster thermostatCluster) {
            if (thermostatCluster.localTemperature != null) {
                updateState(CHANNEL_THERMOSTAT_LOCALTEMPERATURE,
                        valueToTemperature(thermostatCluster.localTemperature));
            }
            if (thermostatCluster.outdoorTemperature != null) {
                updateState(CHANNEL_THERMOSTAT_OUTDOORTEMPERATURE,
                        valueToTemperature(thermostatCluster.outdoorTemperature));
            }
            if (thermostatCluster.systemMode != null) {
                updateState(CHANNEL_THERMOSTAT_SYSTEMMODE, new DecimalType(thermostatCluster.systemMode.value));
            }
            if (thermostatCluster.occupiedHeatingSetpoint != null) {
                updateState(CHANNEL_THERMOSTAT_OCCUPIEDHEATING,
                        valueToTemperature(thermostatCluster.occupiedHeatingSetpoint));
            }
            if (thermostatCluster.occupiedCoolingSetpoint != null) {
                updateState(CHANNEL_THERMOSTAT_OCCUPIEDCOOLING,
                        valueToTemperature(thermostatCluster.occupiedCoolingSetpoint));
            }
            if (thermostatCluster.unoccupiedHeatingSetpoint != null) {
                updateState(CHANNEL_THERMOSTAT_UNOCCUPIEDHEATING,
                        valueToTemperature(thermostatCluster.unoccupiedHeatingSetpoint));
            }
            if (thermostatCluster.unoccupiedCoolingSetpoint != null) {
                updateState(CHANNEL_THERMOSTAT_UNOCCUPIEDCOOLING,
                        valueToTemperature(thermostatCluster.unoccupiedCoolingSetpoint));
            }
        }
    }

    @Override
    public List<ChannelUID> createChannels(Map<String, BaseCluster> clusters) {
        List<ChannelUID> channels = new ArrayList<>();
        clusters.forEach((name, cluster) -> {
            logger.debug("createChannels cluster {}", name);
            switch (name) {
                case OnOffCluster.CLUSTER_NAME:
                    channels.add(createChannel(cluster, CHANNEL_NAME_SWITCH_ONOFF, CHANNEL_SWITCH_ONOFF, "Mode Switch",
                            ITEM_TYPE_SWITCH));
                    break;
                case ThermostatCluster.CLUSTER_NAME:
                    if (cluster instanceof ThermostatCluster thermostatCluster) {
                        logger.debug("createChannels adding thermo channel");

                        ChannelUID channelMode = createChannel(cluster, CHANNEL_NAME_THERMOSTAT_SYSTEMMODE,
                                CHANNEL_THERMOSTAT_SYSTEMMODE, CHANNEL_LABEL_THERMOSTAT_SYSTEMMODE, ITEM_TYPE_NUMBER);
                        channels.add(channelMode);

                        List<StateOption> options = new ArrayList<>();
                        options.add(new StateOption("0", "Off"));
                        if (thermostatCluster.featureMap.autoMode) {
                            options.add(new StateOption("1", "Auto"));
                        }
                        if (thermostatCluster.featureMap.heating) {
                            options.add(new StateOption("4", "Heat"));
                            options.add(new StateOption("5", "Emergency Heating"));
                        }
                        if (thermostatCluster.featureMap.cooling) {
                            options.add(new StateOption("3", "Cool"));
                            options.add(new StateOption("6", "Precooling"));
                        }
                        options.add(new StateOption("7", "Fan Only"));
                        options.add(new StateOption("8", "Dry"));
                        options.add(new StateOption("9", "Sleep"));
                        StateDescription stateDescriptionMode = StateDescriptionFragmentBuilder.create()
                                .withOptions(options).build().toStateDescription();
                        if (stateDescriptionMode != null) {
                            stateDescriptions.put(channelMode, stateDescriptionMode);
                        }

                        if (!thermostatCluster.featureMap.localTemperatureNotExposed) {
                            channels.add(createChannel(cluster, CHANNEL_NAME_THERMOSTAT_LOCALTEMPERATURE,
                                    CHANNEL_THERMOSTAT_LOCALTEMPERATURE, CHANNEL_LABEL_THERMOSTAT_LOCALTEMPERATURE,
                                    ITEM_TYPE_NUMBER_TEMPERATURE));
                        }
                        if (thermostatCluster.outdoorTemperature != null) {
                            channels.add(createChannel(cluster, CHANNEL_NAME_THERMOSTAT_OUTDOORTEMPERATURE,
                                    CHANNEL_THERMOSTAT_OUTDOORTEMPERATURE, CHANNEL_LABEL_THERMOSTAT_OUTDOORTEMPERATURE,
                                    ITEM_TYPE_NUMBER_TEMPERATURE));
                        }
                        if (thermostatCluster.featureMap.heating) {
                            ChannelUID channel = createChannel(cluster, CHANNEL_NAME_THERMOSTAT_OCCUPIEDHEATING,
                                    CHANNEL_THERMOSTAT_OCCUPIEDHEATING, CHANNEL_LABEL_THERMOSTAT_OCCUPIEDHEATING,
                                    ITEM_TYPE_NUMBER_TEMPERATURE);
                            channels.add(channel);
                            StateDescription stateDescription = StateDescriptionFragmentBuilder.create()
                                    .withMinimum(valueToTemperature(thermostatCluster.absMinHeatSetpointLimit)
                                            .toBigDecimal())
                                    .withMaximum(valueToTemperature(thermostatCluster.absMaxHeatSetpointLimit)
                                            .toBigDecimal())
                                    .withStep(BigDecimal.valueOf(1)).withPattern("").withReadOnly(false).build()
                                    .toStateDescription();
                            if (stateDescription != null) {
                                stateDescriptions.put(channel, stateDescription);
                            }
                        }
                        if (thermostatCluster.featureMap.cooling) {
                            ChannelUID channel = createChannel(cluster, CHANNEL_NAME_THERMOSTAT_OCCUPIEDCOOLING,
                                    CHANNEL_THERMOSTAT_OCCUPIEDCOOLING, CHANNEL_LABEL_THERMOSTAT_OCCUPIEDCOOLING,
                                    ITEM_TYPE_NUMBER_TEMPERATURE);
                            channels.add(channel);
                            StateDescription stateDescription = StateDescriptionFragmentBuilder.create()
                                    .withMinimum(valueToTemperature(thermostatCluster.absMinCoolSetpointLimit)
                                            .toBigDecimal())
                                    .withMaximum(valueToTemperature(thermostatCluster.absMaxCoolSetpointLimit)
                                            .toBigDecimal())
                                    .withStep(BigDecimal.valueOf(1)).withPattern("").withReadOnly(false).build()
                                    .toStateDescription();
                            if (stateDescription != null) {
                                stateDescriptions.put(channel, stateDescription);
                            }
                        }
                        if (thermostatCluster.featureMap.occupancy) {
                            if (thermostatCluster.featureMap.heating) {
                                ChannelUID channel = createChannel(cluster, CHANNEL_NAME_THERMOSTAT_UNOCCUPIEDHEATING,
                                        CHANNEL_THERMOSTAT_UNOCCUPIEDHEATING,
                                        CHANNEL_LABEL_THERMOSTAT_UNOCCUPIEDHEATING, ITEM_TYPE_NUMBER_TEMPERATURE);
                                channels.add(channel);
                                StateDescription stateDescription = StateDescriptionFragmentBuilder.create()
                                        .withMinimum(valueToTemperature(thermostatCluster.absMinHeatSetpointLimit)
                                                .toBigDecimal())
                                        .withMaximum(valueToTemperature(thermostatCluster.absMaxHeatSetpointLimit)
                                                .toBigDecimal())
                                        .withStep(BigDecimal.valueOf(1)).withPattern("").withReadOnly(false).build()
                                        .toStateDescription();
                                if (stateDescription != null) {
                                    stateDescriptions.put(channel, stateDescription);
                                }
                            }
                            if (thermostatCluster.featureMap.cooling) {
                                ChannelUID channel = createChannel(cluster, CHANNEL_NAME_THERMOSTAT_UNOCCUPIEDCOOLING,
                                        CHANNEL_THERMOSTAT_UNOCCUPIEDCOOLING,
                                        CHANNEL_LABEL_THERMOSTAT_UNOCCUPIEDCOOLING, ITEM_TYPE_NUMBER_TEMPERATURE);
                                channels.add(channel);
                                StateDescription stateDescription = StateDescriptionFragmentBuilder.create()
                                        .withMinimum(valueToTemperature(thermostatCluster.absMinCoolSetpointLimit)
                                                .toBigDecimal())
                                        .withMaximum(valueToTemperature(thermostatCluster.absMaxCoolSetpointLimit)
                                                .toBigDecimal())
                                        .withStep(BigDecimal.valueOf(1)).withPattern("").withReadOnly(false).build()
                                        .toStateDescription();
                                if (stateDescription != null) {
                                    stateDescriptions.put(channel, stateDescription);
                                }
                            }
                        }
                    }
            }
        });
        return channels;
    }

    @Override
    public void onEvent(AttributeChangedMessage message) {
        logger.debug("OnEvent: {}", message.path.attributeName);
        Integer numberValue = message.value instanceof Number number ? number.intValue() : 0;
        switch (message.path.attributeName) {
            case "onOff":
                OnOffType onOff = OnOffType.from((Boolean) message.value);
                updateState(CHANNEL_SWITCH_ONOFF, onOff);
                break;
            case "systemMode":
                updateState(CHANNEL_THERMOSTAT_SYSTEMMODE, new DecimalType(numberValue));
                break;
            case "occupiedHeatingSetpoint":
                updateState(CHANNEL_THERMOSTAT_OCCUPIEDHEATING, valueToTemperature(numberValue));
                break;
            case "occupiedCoolingSetpoint":
                updateState(CHANNEL_THERMOSTAT_OCCUPIEDCOOLING, valueToTemperature(numberValue));
                break;
            case "unoccupiedHeatingSetpoint":
                updateState(CHANNEL_THERMOSTAT_UNOCCUPIEDHEATING, valueToTemperature(numberValue));
                break;
            case "unoccupiedCoolingSetpoint":
                updateState(CHANNEL_THERMOSTAT_UNOCCUPIEDCOOLING, valueToTemperature(numberValue));
                break;
            case "localTemperature":
                updateState(CHANNEL_THERMOSTAT_LOCALTEMPERATURE, valueToTemperature(numberValue));
                break;
            case "outdoorTemperature":
                updateState(CHANNEL_THERMOSTAT_OUTDOORTEMPERATURE, valueToTemperature(numberValue));
                break;
            default:
                logger.debug("Unknown attribute {}", message.path.attributeName);
        }
    }

    private void sendOnOff(MatterWebsocketClient client, boolean on) {
        ClusterCommand onOffCommand = on ? OnOffCluster.on() : OnOffCluster.off();
        client.clusterCommand(handler.getNodeId(), handler.getEndpointId(), OnOffCluster.CLUSTER_NAME, onOffCommand);
    }
}
