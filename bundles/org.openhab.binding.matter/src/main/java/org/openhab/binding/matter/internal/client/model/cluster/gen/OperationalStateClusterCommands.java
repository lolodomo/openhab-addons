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
import org.openhab.binding.matter.internal.client.model.cluster.gen.OperationalStateClusterTypes.*;
/**
 * OperationalState
 *
 * @author Dan Cunningham - Initial contribution
 */
public class OperationalStateClusterCommands {

    public static ClusterCommand pause() {
        return new ClusterCommand("pause", new PauseCommandOptions());
    }

    public static ClusterCommand stop() {
        return new ClusterCommand("stop", new StopCommandOptions());
    }

    public static ClusterCommand start() {
        return new ClusterCommand("start", new StartCommandOptions());
    }

    public static ClusterCommand resume() {
        return new ClusterCommand("resume", new ResumeCommandOptions());
    }

    public static ClusterCommand operationalCommandResponse(ErrorStateStruct[] commandResponseState) {
        return new ClusterCommand("operationalCommandResponse", new OperationalCommandResponseCommandOptions(commandResponseState));
    }
}
