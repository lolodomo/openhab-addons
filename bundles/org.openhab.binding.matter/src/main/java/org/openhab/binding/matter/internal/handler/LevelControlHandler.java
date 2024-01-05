/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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

import org.openhab.binding.matter.internal.client.AttributeListener;
import org.openhab.binding.matter.internal.client.MatterClient;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.LevelControlCluster;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
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

    public LevelControlHandler(EndpointHandler handler, long nodeId, int endpointId) {
        super(handler, nodeId, endpointId, LevelControlCluster.CLUSTER_ID);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

    }

    @Override
    protected void registerListeners() {
        // we should pass in the matter client, we should register to listen for cluster events for this
        // node/endpoint/cluster and use generics <T> for the specific cluster
        MatterClient client = this.client;
        if (client == null) {
            logger.debug("Can not register for listeners, client not set");
            return;
        }
        // maybe we should just listen to all events, so we can respond to multiple clusterss
        client.addAttributeListener(new AttributeListener() {
            @Override
            public void onEvent(MatterWebsocketClient.AttributeChangedMessage message) {
                switch (message.path.attributeName) {
                    case "onOff":
                        handler.updateState(CHANNEL_NAME_SWITCH_LEVEL, OnOffType.from(Boolean.valueOf(message.value)));
                        break;
                    case "currentLevel":
                        handler.updateState(CHANNEL_NAME_SWITCH_LEVEL, new DecimalType(message.value));
                }
            }
        }, nodeId, endpointId);
    }

    @Override
    public void updateCluster(BaseCluster cluster) {
        if (cluster instanceof LevelControlCluster) {
            // update channels with new state, how do we handle onOff?
            // maybe this handler needs to advertise what other base clusters it needs? Map.of LevelControlCluster
            // OnOffControlCluster
            handler.updateState(CHANNEL_NAME_SWITCH_LEVEL,
                    new DecimalType(((LevelControlCluster) cluster).currentLevel));
        }
    }

    @Override
    public List<ChannelUID> createChannels(BaseCluster cluster) {
        return List.of(createChannel(cluster, CHANNEL_NAME_SWITCH_LEVEL, CHANNEL_SWITCH_LEVEL,
                CHANNEL_LABEL_SWITCH_LEVEL, ITEM_TYPE_DIMMER));
    }
}
