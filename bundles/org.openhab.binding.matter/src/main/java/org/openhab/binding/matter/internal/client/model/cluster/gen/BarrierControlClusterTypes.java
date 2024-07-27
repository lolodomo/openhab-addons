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

import org.openhab.binding.matter.internal.client.model.cluster.types.*;

/**
 * AUTO-GENERATED by zap. DO NOT EDIT!
 *
 * BarrierControl
 *
 * @author Dan Cunningham - Initial contribution
 */
public class BarrierControlClusterTypes {

    public static final String CLUSTER_NAME = "BARRIER_CONTROL_CLUSTER";
    public static final int CLUSTER_ID = 0x0103;

    // ZCL Bitmaps
    public static class BarrierControlCapabilities {
        public boolean partialBarrier;

        public BarrierControlCapabilities(boolean partialBarrier) {
            this.partialBarrier = partialBarrier;
        }
    }

    public static class BarrierControlSafetyStatus {
        public boolean remoteLockout;
        public boolean temperDetected;
        public boolean failedCommunication;
        public boolean positionFailure;

        public BarrierControlSafetyStatus(boolean remoteLockout, boolean temperDetected, boolean failedCommunication,
                boolean positionFailure) {
            this.remoteLockout = remoteLockout;
            this.temperDetected = temperDetected;
            this.failedCommunication = failedCommunication;
            this.positionFailure = positionFailure;
        }
    }

    public static class BarrierControlGoToPercentCommandOptions {
        public Integer percentOpen;

        public BarrierControlGoToPercentCommandOptions(Integer percentOpen) {
            this.percentOpen = percentOpen;
        }
    }

    public static class BarrierControlStopCommandOptions {
        public BarrierControlStopCommandOptions() {
        }
    }
}
