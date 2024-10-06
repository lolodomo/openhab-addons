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
package org.openhab.binding.matter.internal.mapper;

import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_LABEL_WINDOWCOVERING_LIFT;
import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_NAME_WINDOWCOVERING_LIFT;
import static org.openhab.binding.matter.internal.MatterBindingConstants.CHANNEL_WINDOWCOVERING_LIFT;
import static org.openhab.binding.matter.internal.MatterBindingConstants.ITEM_TYPE_ROLLERSHUTTER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.matter.internal.client.MatterWebsocketClient;
import org.openhab.binding.matter.internal.client.model.cluster.BaseCluster;
import org.openhab.binding.matter.internal.client.model.cluster.ClusterCommand;
import org.openhab.binding.matter.internal.client.model.cluster.gen.DeviceTypes;
import org.openhab.binding.matter.internal.client.model.cluster.gen.WindowCoveringCluster;
import org.openhab.binding.matter.internal.client.model.ws.AttributeChangedMessage;
import org.openhab.binding.matter.internal.handler.EndpointHandler;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dan Cunningham
 *
 */
@NonNullByDefault
public class WindowCoveringType extends DeviceType {
    private final Logger logger = LoggerFactory.getLogger(WindowCoveringType.class);

    public WindowCoveringType(EndpointHandler handler, Integer deviceType) {
        super(handler, deviceType);
    }

    public static List<Integer> supportedTypes() {
        return List.of(DeviceTypes.WindowCovering);
    }

    @Override
    public List<Integer> supportedClusters() {
        return List.of(WindowCoveringCluster.CLUSTER_ID);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        MatterWebsocketClient client = handler.getClient();
        if (client == null) {
            return;
        }
        if (command instanceof UpDownType upDownType) {
            switch (upDownType) {
                case UP:
                    moveCommand(client, WindowCoveringCluster.upOrOpen());
                    break;
                case DOWN:
                    moveCommand(client, WindowCoveringCluster.downOrClose());
                    break;
                default:
                    break;
            }
        } else if (command instanceof StopMoveType stopMoveType) {
            switch (stopMoveType) {
                case STOP:
                    moveCommand(client, WindowCoveringCluster.stopMotion());
                    break;
                default:
                    break;
            }
        } else if (command instanceof PercentType percentType) {
            moveCommand(client, WindowCoveringCluster.goToLiftPercentage(percentType.intValue()));
        }
    }

    @Override
    public void updateCluster(BaseCluster cluster) {
        if (cluster instanceof WindowCoveringCluster windowCoveringCluster) {
            Integer pos = 0;
            if (windowCoveringCluster.currentPositionLift != null) {
                pos = windowCoveringCluster.currentPositionLiftPercentage;
            } else if (windowCoveringCluster.currentPositionLiftPercent100ths != null) {
                pos = windowCoveringCluster.currentPositionLiftPercent100ths / 100;
            }
            updateState(CHANNEL_WINDOWCOVERING_LIFT, new PercentType(pos));
        }
    }

    @Override
    public List<ChannelUID> createChannels(Map<String, BaseCluster> clusters) {
        List<ChannelUID> channels = new ArrayList<>();
        clusters.forEach((name, cluster) -> {
            logger.debug("createChannels cluster {}", name);
            switch (name) {
                case WindowCoveringCluster.CLUSTER_NAME:
                    channels.add(createChannel(cluster, CHANNEL_NAME_WINDOWCOVERING_LIFT, CHANNEL_WINDOWCOVERING_LIFT,
                            CHANNEL_LABEL_WINDOWCOVERING_LIFT, ITEM_TYPE_ROLLERSHUTTER));
                    break;
            }
        });
        return channels;
    }

    @Override
    public void onEvent(AttributeChangedMessage message) {
        logger.debug("OnEvent: {}", message.path.attributeName);
        Integer numberValue = message.value instanceof Number ? ((Number) message.value).intValue() : 0;
        switch (message.path.attributeName) {
            case "currentPositionLiftPercentage":
                updateState(CHANNEL_WINDOWCOVERING_LIFT, new PercentType(numberValue));
                break;
            case "currentPositionLiftPercent100ths":
                updateState(CHANNEL_WINDOWCOVERING_LIFT, new PercentType(numberValue / 100));
                break;
            default:
                logger.debug("Unknown attribute {}", message.path.attributeName);
        }
    }

    private void moveCommand(MatterWebsocketClient client, ClusterCommand command) {
        client.clusterCommand(handler.getNodeId(), handler.getEndpointId(), WindowCoveringCluster.CLUSTER_NAME,
                command);
    }
}
