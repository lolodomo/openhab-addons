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

import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.LevelControlClusterTypes.*;
/**
 * LevelControl
 *
 * @author Dan Cunningham - Initial contribution
 */
public class LevelControlClusterCommands {

    public static ClusterCommand moveToLevel(Integer level, Integer transitionTime, OptionsBitmap optionsMask, OptionsBitmap optionsOverride) {
        return new ClusterCommand("moveToLevel", new MoveToLevelCommandOptions(level, transitionTime, optionsMask, optionsOverride));
    }

    public static ClusterCommand move(MoveModeEnum moveMode, Integer rate, OptionsBitmap optionsMask, OptionsBitmap optionsOverride) {
        return new ClusterCommand("move", new MoveCommandOptions(moveMode, rate, optionsMask, optionsOverride));
    }

    public static ClusterCommand step(StepModeEnum stepMode, Integer stepSize, Integer transitionTime, OptionsBitmap optionsMask, OptionsBitmap optionsOverride) {
        return new ClusterCommand("step", new StepCommandOptions(stepMode, stepSize, transitionTime, optionsMask, optionsOverride));
    }

    public static ClusterCommand stop(OptionsBitmap optionsMask, OptionsBitmap optionsOverride) {
        return new ClusterCommand("stop", new StopCommandOptions(optionsMask, optionsOverride));
    }

    public static ClusterCommand moveToLevelWithOnOff(Integer level, Integer transitionTime, OptionsBitmap optionsMask, OptionsBitmap optionsOverride) {
        return new ClusterCommand("moveToLevelWithOnOff", new MoveToLevelWithOnOffCommandOptions(level, transitionTime, optionsMask, optionsOverride));
    }

    public static ClusterCommand moveWithOnOff(MoveModeEnum moveMode, Integer rate, OptionsBitmap optionsMask, OptionsBitmap optionsOverride) {
        return new ClusterCommand("moveWithOnOff", new MoveWithOnOffCommandOptions(moveMode, rate, optionsMask, optionsOverride));
    }

    public static ClusterCommand stepWithOnOff(StepModeEnum stepMode, Integer stepSize, Integer transitionTime, OptionsBitmap optionsMask, OptionsBitmap optionsOverride) {
        return new ClusterCommand("stepWithOnOff", new StepWithOnOffCommandOptions(stepMode, stepSize, transitionTime, optionsMask, optionsOverride));
    }

    public static ClusterCommand stopWithOnOff(OptionsBitmap optionsMask, OptionsBitmap optionsOverride) {
        return new ClusterCommand("stopWithOnOff", new StopWithOnOffCommandOptions(optionsMask, optionsOverride));
    }

    public static ClusterCommand moveToClosestFrequency(Integer frequency) {
        return new ClusterCommand("moveToClosestFrequency", new MoveToClosestFrequencyCommandOptions(frequency));
    }
}
