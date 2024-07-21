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

import java.util.Map;
import java.util.List;

import org.openhab.binding.matter.internal.client.model.cluster.types.*;
import static java.util.Map.entry;  

/**
 * AUTO-GENERATED by zap. DO NOT EDIT!
 *
 * BallastConfiguration
 *
 * @author Dan Cunningham - Initial contribution
 */
public class BallastConfigurationClusterTypes {

    public static final String CLUSTER_NAME = "BALLAST_CONFIGURATION_CLUSTER";
    public static final int CLUSTER_ID = 0x0301;

    //ZCL Bitmaps
    public static class BallastStatusBitmap {
        public boolean ballastNonOperational;
        public boolean lampFailure;
        public BallastStatusBitmap(boolean ballastNonOperational, boolean lampFailure){
            this.ballastNonOperational = ballastNonOperational;
            this.lampFailure = lampFailure;
        }
    }
    public static class LampAlarmModeBitmap {
        public boolean lampBurnHours;
        public LampAlarmModeBitmap(boolean lampBurnHours){
            this.lampBurnHours = lampBurnHours;
        }
    }

}
