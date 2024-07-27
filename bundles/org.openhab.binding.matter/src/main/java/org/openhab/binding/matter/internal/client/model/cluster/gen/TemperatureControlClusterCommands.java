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

import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.TemperatureControlClusterTypes.*;

/**
 * TemperatureControl
 *
 * @author Dan Cunningham - Initial contribution
 */
public class TemperatureControlClusterCommands {

    public static ClusterCommand setTemperature(Integer targetTemperature, Integer targetTemperatureLevel) {
        return new ClusterCommand("setTemperature",
                new SetTemperatureCommandOptions(targetTemperature, targetTemperatureLevel));
    }
}
