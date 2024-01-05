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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.matter.internal.client.MatterClient;
import org.openhab.binding.matter.internal.client.model.Node;
import org.openhab.binding.matter.internal.config.ControllerConfiguration;
import org.openhab.binding.matter.internal.discovery.NodeDiscoveryService;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ControllerHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Dan Cunningham - Initial contribution
 */
@NonNullByDefault
public class ControllerHandler extends AbstractMatterBridgeHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerHandler.class);
    private final MatterClient matterClient;
    private int nodeId;
    private List<Node> nodes = Collections.synchronizedList(new LinkedList<Node>());

    public ControllerHandler(Bridge bridge) {
        super(bridge);
        this.matterClient = new MatterClient();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (!matterClient.isConnected()) {
            logger.debug("not connected");
            return;
        }
        if (CHANNEL_1.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
            }
        }
        if (CHANNEL_PAIR_CODE.equals(channelUID.getId())) {
            try {
                matterClient.pairNode(command.toString());
            } catch (Exception e) {
                logger.debug("Could not pair", e);
            }
        }
        if (CHANNEL_COMMAND.equals(channelUID.getId())) {
            try {
                updateNodes();
            } catch (Exception e) {
                logger.debug("Could not send command", e);
            }
        }
    }

    @Override
    public void initialize() {
        logger.debug("initialize");
        updateStatus(ThingStatus.UNKNOWN);
        ControllerConfiguration c = getConfigAs(ControllerConfiguration.class);
        nodeId = c.nodeId;
        scheduler.execute(() -> {
            try {
                matterClient.connect("localhost", 8888);
                updateStatus(ThingStatus.ONLINE);
            } catch (Exception e) {
                logger.debug("Could init", e);
            }
        });
    }

    @Override
    public void dispose() {
        matterClient.disconnect();
    }

    public void refresh() {
        synchronized (nodes) {
            for (Node n : nodes) {
                logger.debug("finding handler for {} ", n.id);
                NodeHandler handler = nodeHandler(n.id);
                if (handler != null) {
                    handler.updateNode(n);
                }
            }
        }
    }

    private void updateNodes() throws Exception {
        Map<String, Node> nodesMap = matterClient.getNodes();
        synchronized (nodes) {
            nodes.clear();
            for (Node n : nodesMap.values()) {
                discoverChildNode(n);
                nodes.add(n);
            }
        }
        refresh();
    }

    private void discoverChildNode(Node node) {
        NodeDiscoveryService discoveryService = this.discoveryService;
        if (discoveryService != null) {
            ThingUID bridgeUID = getThing().getUID();
            ThingUID thingUID = new ThingUID(THING_TYPE_NODE, bridgeUID, String.valueOf(node.id));
            discoveryService.discoverhildThing(thingUID, bridgeUID, node.id, "Matter Node " + node.id);

        }
    }

    private @Nullable NodeHandler nodeHandler(long nodeId) {
        for (Thing thing : getThing().getThings()) {
            ThingHandler handler = thing.getHandler();
            if (handler instanceof NodeHandler nodeHandler) {
                if (nodeHandler.getNodeId() == nodeId) {
                    return nodeHandler;
                }
            }
        }
        return null;
    }
}
