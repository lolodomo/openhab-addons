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
package org.openhab.binding.matter.internal.converter;

import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_LABEL_SWITCH_LEVEL;
import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_NAME_SWITCH_LEVEL;
import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_NAME_SWITCH_ONOFF;
import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_SWITCH_LEVEL;
import static org.openhab.binding.matter.internal.MatterBindingConstants.ITEM_TYPE_DIMMER;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.LevelControlCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.LevelControlCluster.OptionsBitmap;
import org.openhab.binding.matter.internal.client.model.cluster.gen.OnOffCluster;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.handler.EndpointHandler;
import org.openhab.core.library.types.DecimalType;
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
@NonNullByDefault
public class LevelControlConverter extends ClusterConverter {
    private final Logger logger = LoggerFactory.getLogger(LevelControlConverter.class);
    // private LevelControlCluster levelControlCluster;
    private PercentType lastLevel = new PercentType(0);
    private OnOffType lastOnOff = OnOffType.OFF;

    public LevelControlConverter(EndpointHandler handler) {
        super(handler);
    }

    @Override
    public List<Integer> supportedClusters() {
        return List.of(LevelControlCluster.CLUSTER_ID, OnOffCluster.CLUSTER_ID);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        MatterWebsocketClient client = handler.getClient();
        if (client == null) {
            return;
        }
        if (command instanceof PercentType) {
            float level = ((DecimalType) command).floatValue();
            sendOnOff(client, level > 0);
            if (level > 0) {
                sendLevel(client, (PercentType) command);
            }
        }

        if (command instanceof OnOffType) {
            sendOnOff(client, command == OnOffType.ON);
        }
    }

    private void sendOnOff(MatterWebsocketClient client, boolean on) {
        ClusterCommand onOffCommand = on ? OnOffCluster.on() : OnOffCluster.off();
        client.clusterCommand(handler.getNodeId(), handler.getEndpointId(), OnOffCluster.CLUSTER_NAME, onOffCommand);
    }

    private void sendLevel(MatterWebsocketClient client, PercentType level) {
        ClusterCommand levelCommand = LevelControlCluster.moveToLevel(percentToLevel(level), 0,
                new OptionsBitmap(false, true), new OptionsBitmap(false, true));
        client.clusterCommand(handler.getNodeId(), handler.getEndpointId(), LevelControlCluster.CLUSTER_NAME,
                levelCommand);
    }

    @Override
    public void updateCluster(BaseCluster cluster) {
        if (cluster instanceof LevelControlCluster) {
            lastLevel = levelToPercent(((LevelControlCluster) cluster).currentLevel);
            logger.debug("LevelControl {}", lastLevel);
            handler.updateState("LevelControl_" + CHANNEL_NAME_SWITCH_LEVEL, lastLevel);
            handler.updateState("OnOff_" + CHANNEL_NAME_SWITCH_ONOFF, OnOffType.from(lastLevel.intValue() > 0));
        } else if (cluster instanceof OnOffCluster) {
            lastOnOff = OnOffType.from(Boolean.valueOf(((OnOffCluster) cluster).onOff));
            logger.debug("OnOff {}", lastOnOff);
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
                lastOnOff = OnOffType.from((Boolean) message.value);
                handler.updateState("OnOff_" + CHANNEL_NAME_SWITCH_ONOFF, lastOnOff);
                handler.updateState("LevelControl_" + CHANNEL_NAME_SWITCH_LEVEL,
                        lastOnOff == OnOffType.ON ? lastLevel : new PercentType(0));
                break;
            case "currentLevel":
                logger.debug("currentLevel {}", message.value);
                lastLevel = levelToPercent(((Number) message.value).intValue());
                handler.updateState("LevelControl_" + CHANNEL_NAME_SWITCH_LEVEL, lastLevel);
                handler.updateState("OnOff_" + CHANNEL_NAME_SWITCH_ONOFF, OnOffType.from(lastLevel.intValue() > 0));
                break;
            default:
                logger.debug("Unknow attribute {}", message.path.attributeName);
        }
    }
}
