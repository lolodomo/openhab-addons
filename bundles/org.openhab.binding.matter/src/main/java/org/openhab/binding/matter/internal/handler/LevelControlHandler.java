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
package org.openhab.binding.matter.internal.handler;

import static org.openhab.binding.matter.internal.MatterBindingConstants.*;

import java.util.List;

import org.openhab.binding.matter.internal.client.MatterWebsocketClient.AttributeChangedMessage;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.LevelControlCluster;
import org.openhab.binding.matter.internal.client.model.cluster.OnOffCluster;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dan Cunningham
 *
 */
public class LevelControlHandler extends ClusterHandler {
    private final Logger logger = LoggerFactory.getLogger(ClusterHandler.class);
    private LevelControlCluster levelControlCluster;
    private PercentType lastLevel = new PercentType(0);
    private OnOffType lastOnOff = OnOffType.OFF;

    public LevelControlHandler(EndpointHandler handler, long nodeId, int endpointId) {
        super(handler, LevelControlCluster.CLUSTER_ID);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    @Override
    public void updateCluster(BaseCluster cluster) {
        if (cluster instanceof LevelControlCluster) {
            // update channels with new state, how do we handle onOff?
            // maybe this handler needs to advertise what other base clusters it needs? Map.of LevelControlCluster
            // OnOffControlCluster
            lastLevel = levelToPercent(((LevelControlCluster) cluster).currentLevel);
            handler.updateState("LevelControl_" + CHANNEL_NAME_SWITCH_LEVEL, lastLevel);
            handler.updateState("OnOff_" + CHANNEL_NAME_SWITCH_ONOFF, OnOffType.from(lastLevel.intValue() > 0));
        } else if (cluster instanceof OnOffCluster) {
            lastOnOff = OnOffType.from(Boolean.valueOf(((OnOffCluster) cluster).onOff));
            handler.updateState("OnOff_" + CHANNEL_NAME_SWITCH_ONOFF, lastOnOff);
            handler.updateState("LevelControl_" + CHANNEL_NAME_SWITCH_LEVEL,
                    lastOnOff == OnOffType.ON ? lastLevel : new PercentType(0));
        }
    }

    @Override
    public List<ChannelUID> createChannels(BaseCluster cluster) {
        return List.of(createChannel(cluster, CHANNEL_NAME_SWITCH_LEVEL, CHANNEL_SWITCH_LEVEL,
                CHANNEL_LABEL_SWITCH_LEVEL, ITEM_TYPE_DIMMER));
    }

    @Override
    public void onEvent(AttributeChangedMessage message) {
        logger.debug("OnEvent: {}", message.path.attributeName);
        switch (message.path.attributeName) {
            case "onOff":
                lastOnOff = OnOffType.from(Boolean.valueOf(message.value));
                handler.updateState("OnOff_" + CHANNEL_NAME_SWITCH_ONOFF, lastOnOff);
                handler.updateState("LevelControl_" + CHANNEL_NAME_SWITCH_LEVEL,
                        lastOnOff == OnOffType.ON ? lastLevel : new PercentType(0));
                break;
            case "currentLevel":
                logger.debug("currentLevel {}", message.value);
                lastLevel = levelToPercent(Integer.parseInt(message.value));
                handler.updateState("LevelControl_" + CHANNEL_NAME_SWITCH_LEVEL, lastLevel);
                handler.updateState("OnOff_" + CHANNEL_NAME_SWITCH_ONOFF, OnOffType.from(lastLevel.intValue() > 0));
                break;
            default:
                logger.debug("Unknow attribute {}", message.path.attributeName);
        }
    }
}
