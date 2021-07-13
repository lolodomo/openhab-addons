/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.somfytahoma.internal.handler;

import static org.openhab.binding.somfytahoma.internal.SomfyTahomaBindingConstants.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.somfytahoma.internal.model.SomfyTahomaState;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link CozytouchElectricHeaterHandler} is responsible for handling commands,
 * which are sent to one of the channels of the Cozytouch electric heater thing.
 *
 * @author Laurent Garnier - Initial contribution
 */
@NonNullByDefault
public class CozytouchElectricHeaterHandler extends SomfyTahomaBaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(CozytouchElectricHeaterHandler.class);

    private @Nullable String operatiungMode;
    private @Nullable String targetHeatingLevel;
    private double targetTemperature;
    private double confortTemperature;
    private double loweringTemperature;

    public CozytouchElectricHeaterHandler(Thing thing) {
        super(thing);
        stateNames.put(OPERATING_MODE, "core:OperatingModeState");
        stateNames.put(HEATING_MODE, "io:TargetHeatingLevelState");
        stateNames.put(HEATING, "core:OnOffState");
        stateNames.put(OPEN_WINDOW_DETECTION, "core:OpenWindowDetectionActivationState");
    }

    private boolean shouldApplyCmdsToPlace() {
        return getThing().getConfiguration().get("applyCmdsToPlace") != null
                ? getThing().getConfiguration().get("applyCmdsToPlace").toString().equals("true")
                : false;
    }

    @Override
    public void updateThingChannels(List<SomfyTahomaState> states) {
        super.updateThingChannels(states);

        // io:InternalExternalSchedulingTypeState external internal
        SomfyTahomaState operatingModeState = getState(states, "core:OperatingModeState", TYPE_STRING);
        SomfyTahomaState targetHeatingLevelState = getState(states, "io:TargetHeatingLevelState", null);
        SomfyTahomaState targetTemperatureState = getState(states, "core:TargetTemperatureState", null);
        SomfyTahomaState confortRoomTemperatureState = getState(states, "core:ComfortRoomTemperatureState", null);
        SomfyTahomaState ecoRoomTemperatureState = getState(states, "core:EcoRoomTemperatureState", null);
        SomfyTahomaState loweringTemperatureState = getState(states, "io:SetpointLoweringTemperatureInProgModeState",
                null);

        boolean operatingModeUpdated = false;
        if (operatingModeState != null) {
            operatiungMode = operatingModeState.getValue().toString();
            logger.debug("operatiungMode = {}", operatiungMode);
            operatingModeUpdated = true;
        }

        boolean targetHeatingLevelUpdated = false;
        if (targetHeatingLevelState != null) {
            targetHeatingLevel = targetHeatingLevelState.getValue().toString();
            logger.debug("targetHeatingLevel = {}", targetHeatingLevel);
            targetHeatingLevelUpdated = true;
        }

        boolean targetTemperatureUpdated = false;
        if (operatingModeUpdated || targetHeatingLevelUpdated || targetTemperatureState != null) {
            if (targetTemperatureState != null) {
                targetTemperature = Double.parseDouble(targetTemperatureState.getValue().toString());
                logger.debug("targetTemperature = {}", targetTemperature);
                targetTemperatureUpdated = true;
            }
            if (isLinked(TARGET_TEMPERATURE)) {
                updateState(TARGET_TEMPERATURE, isInStandbyMode() || isInOffMode() ? UnDefType.UNDEF
                        : new QuantityType<>(targetTemperature, getTemperatureUnit()));
            }
        }

        boolean confortTemperatureUpdated = false;
        if (operatingModeUpdated || targetHeatingLevelUpdated || targetTemperatureUpdated
                || confortRoomTemperatureState != null) {
            if (confortRoomTemperatureState != null) {
                confortTemperature = Double.parseDouble(confortRoomTemperatureState.getValue().toString());
                logger.debug("confortTemperature = {}", confortTemperature);
                confortTemperatureUpdated = true;
            }
            if (isLinked(CONFORT_TEMPERATURE_SETPOINT)) {
                updateState(CONFORT_TEMPERATURE_SETPOINT,
                        isInStandbyMode() || isInOffMode() || isInFrostProtectionMode() ? UnDefType.UNDEF
                                : new QuantityType<>(confortTemperature, getTemperatureUnit()));
            }
        }

        if (operatingModeUpdated || targetHeatingLevelUpdated || ecoRoomTemperatureState != null
                || loweringTemperatureState != null) {
            if (ecoRoomTemperatureState != null) {
                loweringTemperature = Double.parseDouble(ecoRoomTemperatureState.getValue().toString());
                logger.debug("loweringTemperature = {}", loweringTemperature);
            } else if (loweringTemperatureState != null) {
                loweringTemperature = Double.parseDouble(loweringTemperatureState.getValue().toString());
                logger.debug("loweringTemperature = {}", loweringTemperature);
            }
            if (isLinked(LOWERING_TEMPERATURE)) {
                updateState(LOWERING_TEMPERATURE,
                        isInStandbyMode() || isInBasicMode() || isInOffMode() || isInFrostProtectionMode()
                                ? UnDefType.UNDEF
                                : new QuantityType<>(loweringTemperature, getTemperatureUnit()));
            }
        }

        boolean allUpdated = operatingModeUpdated && targetHeatingLevelUpdated && targetTemperatureUpdated
                && confortTemperatureUpdated;
        boolean atLeastOneChange = operatingModeUpdated || targetHeatingLevelUpdated || targetTemperatureUpdated
                || confortTemperatureUpdated;
        logger.debug("allUpdated {} atLeastOneChange {}", allUpdated, atLeastOneChange);
        if (atLeastOneChange && !allUpdated) {
            SomfyTahomaBridgeHandler handler = getBridgeHandler();
            if (handler != null) {
                logger.debug("refresh {}", url);
                handler.refresh(url);
            }
        }
    }

    @Override
    protected void refresh(String channel) {
        SomfyTahomaBridgeHandler handler = getBridgeHandler();
        if (handler != null) {
            String stateName = stateNames.get(channel);
            if (stateName != null) {
                handler.refresh(url, stateName);
            } else {
                handler.refresh(url);
            }
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        super.handleCommand(channelUID, command);
        if (command instanceof RefreshType) {
            return;
        }
        BigDecimal temperature;
        String param;
        switch (channelUID.getId()) {
            case OPERATING_MODE:
                if (command instanceof StringType) {
                    param = "[\"" + command.toString() + "\"]";
                    sendCommand("setOperatingMode", param);
                }
                break;
            case TARGET_TEMPERATURE:
                temperature = toTemperature(command);
                if (temperature != null && !isInStandbyMode() && !isInOffMode()) {
                    param = "[" + roundTemperature(temperature).toPlainString() + "]";
                    sendCommand("setTargetTemperature", param);
                }
                break;
            case CONFORT_TEMPERATURE_SETPOINT:
                temperature = toTemperature(command);
                if (temperature != null && !isInStandbyMode() && !isInOffMode() && !isInBasicMode()) {
                    param = "[" + roundTemperature(temperature).toPlainString() + "]";
                    sendCommand("setComfortTemperature", param);
                }
                break;
            case LOWERING_TEMPERATURE:
                temperature = toTemperature(command);
                if (temperature != null && !isInStandbyMode() && !isInOffMode() && !isInBasicMode()
                        && !isInFrostProtectionMode()) {
                    param = "[" + roundTemperature(temperature).toPlainString() + "]";
                    sendCommand("setEcoTemperature", param);
                }
                break;
            case OPEN_WINDOW_DETECTION:
                if (command instanceof OnOffType) {
                    param = "[" + (command == OnOffType.ON ? "active" : "inactive") + "]";
                    sendCommand("setOpenWindowDetectionActivation", param);
                }
                break;
            default:
                // setDerogatedTargetTemperature
                // setOccupancyActivation
                break;
        }
    }

    @Override
    protected void sendCommand(String cmd, String param) {
        if (shouldApplyCmdsToPlace()) {
            sendCommandToSameDevicesInPlace(cmd, param);
        } else {
            super.sendCommand(cmd, param);
        }
    }

    private BigDecimal roundTemperature(BigDecimal temperature) {
        // Round the value to a multiple of 0.5
        return temperature.multiply(BigDecimal.valueOf(2.0)).setScale(0, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(2.0));
    }

    private boolean isInStandbyMode() {
        return "standby".equalsIgnoreCase(operatiungMode);
    }

    private boolean isInBasicMode() {
        return "basic".equalsIgnoreCase(operatiungMode);
    }

    // private boolean isInConfortMode() {
    // return "comfort".equalsIgnoreCase(targetHeatingLevel);
    // }

    // private boolean isInEcoMode() {
    // return "eco".equalsIgnoreCase(targetHeatingLevel);
    // }

    private boolean isInFrostProtectionMode() {
        return "frostprotection".equalsIgnoreCase(targetHeatingLevel);
    }

    private boolean isInOffMode() {
        return "off".equalsIgnoreCase(targetHeatingLevel);
    }
}
