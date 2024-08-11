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

// AUTO-GENERATED, DO NOT EDIT!

package org.openhab.binding.matter.internal.client.model.cluster.gen;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DataTypes.*;

/**
 * UnitLocalization
 *
 * @author Dan Cunningham - Initial contribution
 */
public class UnitLocalizationCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "UnitLocalization";
    public static final int CLUSTER_ID = 0x002D;

    public Integer clusterRevision; // 65533 ClusterRevision 
    public FeatureMap featureMap; // 65532 FeatureMap 
    /**
    * The TemperatureUnit attribute shall indicate the unit for the Node to use only when conveying temperature in communication to the user. If provided, this value shall take priority over any unit implied through the ActiveLocale Attribute.
    */
    public TempUnitEnum temperatureUnit; // 0 TempUnitEnum RW VM


    //Enums
    public enum TempUnitEnum {
        FAHRENHEIT(0, "Fahrenheit"),
        CELSIUS(1, "Celsius"),
        KELVIN(2, "Kelvin");
        public final Integer value;
        public final String label;
        private TempUnitEnum(Integer value, String label){
            this.value = value;
            this.label = label;
        }
    }

    // Bitmaps
    public static class FeatureMap {
        /**
        * The Node can be configured to use different units of temperature when conveying values to a user.
        */
        public boolean tEMP;
        public FeatureMap(boolean tEMP){
            this.tEMP = tEMP;
        }
    }

    public UnitLocalizationCluster(String nodeId, int endpointId) {
        super(nodeId, endpointId, 45, "UnitLocalization");
    }

    

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        str += "featureMap : " + featureMap + "\n";
        str += "temperatureUnit : " + temperatureUnit + "\n";
        return str;
    }
}
