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

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DataTypes.*;

/**
 * BridgedDeviceBasicInformation
 *
 * @author Dan Cunningham - Initial contribution
 */
public class BridgedDeviceBasicInformationCluster extends BaseCluster {

    public static final String CLUSTER_NAME = "BridgedDeviceBasicInformation";
    public static final int CLUSTER_ID = 0x0039;

    public Integer clusterRevision; // 65533 ClusterRevision 
    public String vendorName; // 1  
    public String vendorId; // 2  
    public String productName; // 3  
    public String nodeLabel; // 5  
    public String hardwareVersion; // 7  
    public String hardwareVersionString; // 8  
    public String softwareVersion; // 9  
    public String softwareVersionString; // 10  
    public String manufacturingDate; // 11  
    public String partNumber; // 12  
    public String productUrl; // 13  
    public String productLabel; // 14  
    public String serialNumber; // 15  
    public String reachable; // 17  
    public String uniqueId; // 18  
    public String productAppearance; // 20  




    public BridgedDeviceBasicInformationCluster(BigInteger nodeId, int endpointId) {
        super(nodeId, endpointId, 57, "BridgedDeviceBasicInformation");
    }

    

    public String toString() {
        String str = "";
        str += "clusterRevision : " + clusterRevision + "\n";
        str += "vendorName : " + vendorName + "\n";
        str += "vendorId : " + vendorId + "\n";
        str += "productName : " + productName + "\n";
        str += "nodeLabel : " + nodeLabel + "\n";
        str += "hardwareVersion : " + hardwareVersion + "\n";
        str += "hardwareVersionString : " + hardwareVersionString + "\n";
        str += "softwareVersion : " + softwareVersion + "\n";
        str += "softwareVersionString : " + softwareVersionString + "\n";
        str += "manufacturingDate : " + manufacturingDate + "\n";
        str += "partNumber : " + partNumber + "\n";
        str += "productUrl : " + productUrl + "\n";
        str += "productLabel : " + productLabel + "\n";
        str += "serialNumber : " + serialNumber + "\n";
        str += "reachable : " + reachable + "\n";
        str += "uniqueId : " + uniqueId + "\n";
        str += "productAppearance : " + productAppearance + "\n";
        return str;
    }
}
