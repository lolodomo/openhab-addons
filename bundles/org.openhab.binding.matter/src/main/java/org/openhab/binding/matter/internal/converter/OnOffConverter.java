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
import org.openhab.binding.matter.internal.client.model.cluster.gen.OnOffCluster;
import org.openhab.binding.matter.internal.client.model.cluster.gen.OnOffClusterCommands;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.handler.EndpointHandler;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dan Cunningham
 *
 */
@NonNullByDefault
public class OnOffConverter extends ClusterConverter {
    private final Logger logger = LoggerFactory.getLogger(OnOffConverter.class);

    public OnOffConverter(EndpointHandler handler) {
        super(handler);
    }

    @Override
    public List<Integer> supportedClusters() {
        return List.of(OnOffCluster.CLUSTER_ID);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        MatterWebsocketClient client = handler.getClient();
        if (client == null) {
            return;
        }

        if (command instanceof OnOffType) {
            ClusterCommand onOffCommand = command == OnOffType.ON ? OnOffClusterCommands.on()
                    : OnOffClusterCommands.off();
            client.clusterCommand(handler.getNodeId(), handler.getEndpointId(), OnOffCluster.CLUSTER_NAME,
                    onOffCommand);
        }
    }

    @Override
    public void updateCluster(BaseCluster cluster) {
        if (cluster instanceof OnOffCluster) {
            handler.updateState("OnOff_" + CHANNEL_NAME_SWITCH_ONOFF,
                    OnOffType.from(Boolean.valueOf(((OnOffCluster) cluster).onOff)));
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
                handler.updateState("OnOff_" + CHANNEL_NAME_SWITCH_ONOFF,
                        OnOffType.from(Boolean.valueOf(message.value)));
                break;
            default:
                logger.debug("Unknow attribute {}", message.path.attributeName);
        }
    }
}
