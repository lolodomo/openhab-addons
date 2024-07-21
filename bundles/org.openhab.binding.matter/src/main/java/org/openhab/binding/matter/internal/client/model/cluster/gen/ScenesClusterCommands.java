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
import org.openhab.binding.matter.internal.client.model.cluster.gen.ScenesClusterTypes.*;
/**
 * Scenes
 *
 * @author Dan Cunningham - Initial contribution
 */
public class ScenesClusterCommands {

    public static ClusterCommand addScene(List<Integer> groupID, Integer sceneID, Integer transitionTime, String sceneName, ExtensionFieldSet extensionFieldSets) {
        return new ClusterCommand("addScene", new AddSceneCommandOptions(groupID, sceneID, transitionTime, sceneName, extensionFieldSets));
    }

    public static ClusterCommand addSceneResponse(Integer status, List<Integer> groupID, Integer sceneID) {
        return new ClusterCommand("addSceneResponse", new AddSceneResponseCommandOptions(status, groupID, sceneID));
    }

    public static ClusterCommand viewScene(List<Integer> groupID, Integer sceneID) {
        return new ClusterCommand("viewScene", new ViewSceneCommandOptions(groupID, sceneID));
    }

    public static ClusterCommand viewSceneResponse(Integer status, List<Integer> groupID, Integer sceneID, Integer transitionTime, String sceneName, ExtensionFieldSet extensionFieldSets) {
        return new ClusterCommand("viewSceneResponse", new ViewSceneResponseCommandOptions(status, groupID, sceneID, transitionTime, sceneName, extensionFieldSets));
    }

    public static ClusterCommand removeScene(List<Integer> groupID, Integer sceneID) {
        return new ClusterCommand("removeScene", new RemoveSceneCommandOptions(groupID, sceneID));
    }

    public static ClusterCommand removeSceneResponse(Integer status, List<Integer> groupID, Integer sceneID) {
        return new ClusterCommand("removeSceneResponse", new RemoveSceneResponseCommandOptions(status, groupID, sceneID));
    }

    public static ClusterCommand removeAllScenes(List<Integer> groupID) {
        return new ClusterCommand("removeAllScenes", new RemoveAllScenesCommandOptions(groupID));
    }

    public static ClusterCommand removeAllScenesResponse(Integer status, List<Integer> groupID) {
        return new ClusterCommand("removeAllScenesResponse", new RemoveAllScenesResponseCommandOptions(status, groupID));
    }

    public static ClusterCommand storeScene(List<Integer> groupID, Integer sceneID) {
        return new ClusterCommand("storeScene", new StoreSceneCommandOptions(groupID, sceneID));
    }

    public static ClusterCommand storeSceneResponse(Integer status, List<Integer> groupID, Integer sceneID) {
        return new ClusterCommand("storeSceneResponse", new StoreSceneResponseCommandOptions(status, groupID, sceneID));
    }

    public static ClusterCommand recallScene(List<Integer> groupID, Integer sceneID, Integer transitionTime) {
        return new ClusterCommand("recallScene", new RecallSceneCommandOptions(groupID, sceneID, transitionTime));
    }

    public static ClusterCommand getSceneMembership(List<Integer> groupID) {
        return new ClusterCommand("getSceneMembership", new GetSceneMembershipCommandOptions(groupID));
    }

    public static ClusterCommand getSceneMembershipResponse(Integer status, Integer capacity, List<Integer> groupID, Integer sceneList) {
        return new ClusterCommand("getSceneMembershipResponse", new GetSceneMembershipResponseCommandOptions(status, capacity, groupID, sceneList));
    }

    public static ClusterCommand enhancedAddScene(List<Integer> groupID, Integer sceneID, Integer transitionTime, String sceneName, ExtensionFieldSet extensionFieldSets) {
        return new ClusterCommand("enhancedAddScene", new EnhancedAddSceneCommandOptions(groupID, sceneID, transitionTime, sceneName, extensionFieldSets));
    }

    public static ClusterCommand enhancedAddSceneResponse(Integer status, List<Integer> groupID, Integer sceneID) {
        return new ClusterCommand("enhancedAddSceneResponse", new EnhancedAddSceneResponseCommandOptions(status, groupID, sceneID));
    }

    public static ClusterCommand enhancedViewScene(List<Integer> groupID, Integer sceneID) {
        return new ClusterCommand("enhancedViewScene", new EnhancedViewSceneCommandOptions(groupID, sceneID));
    }

    public static ClusterCommand enhancedViewSceneResponse(Integer status, List<Integer> groupID, Integer sceneID, Integer transitionTime, String sceneName, ExtensionFieldSet extensionFieldSets) {
        return new ClusterCommand("enhancedViewSceneResponse", new EnhancedViewSceneResponseCommandOptions(status, groupID, sceneID, transitionTime, sceneName, extensionFieldSets));
    }

    public static ClusterCommand copyScene(CopyModeBitmap mode, List<Integer> groupIdentifierFrom, Integer sceneIdentifierFrom, List<Integer> groupIdentifierTo, Integer sceneIdentifierTo) {
        return new ClusterCommand("copyScene", new CopySceneCommandOptions(mode, groupIdentifierFrom, sceneIdentifierFrom, groupIdentifierTo, sceneIdentifierTo));
    }

    public static ClusterCommand copySceneResponse(Integer status, List<Integer> groupIdentifierFrom, Integer sceneIdentifierFrom) {
        return new ClusterCommand("copySceneResponse", new CopySceneResponseCommandOptions(status, groupIdentifierFrom, sceneIdentifierFrom));
    }
}
