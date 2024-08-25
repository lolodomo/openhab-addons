import { CommissioningControllerNodeOptions, NodeStateInformation } from "@project-chip/matter.js/device";
import { Logger } from "@project-chip/matter-node.js/log";
import { MatterNode } from "./MatterNode";
import { Nodes } from "./namespaces/Nodes";
import { Clusters } from "./namespaces/Clusters";
import { WebSocketSession } from "./app";
const logger = Logger.get("Controller");

/**
 * This class exists to expose the "nodes" and "clusters" namespaces to websocket clients
 */
export class Controller {
    public nodes: Nodes;
    public clusters: Clusters;

    constructor(private ws: WebSocketSession, private theNode: MatterNode) {
        //set up listeners to send events back to the client
        this.nodes = new Nodes(theNode, {
            autoSubscribe: true,
            attributeChangedCallback: (peerNodeId, data) => {
                logger.debug(`attributeChangedCallback ${peerNodeId} ${Logger.toJSON(data)}`);
                data.path.nodeId = peerNodeId;
                ws.sendEvent("attributeChanged", data)
            },
            eventTriggeredCallback: (peerNodeId, data) => {
                logger.debug(`eventTriggeredCallback ${peerNodeId} ${Logger.toJSON(data)}`);
                data.path.nodeId = peerNodeId;
                ws.sendEvent("eventTriggered", data)
            },
            stateInformationCallback: (peerNodeId, info) => {
                logger.debug(`stateInformationCallback ${peerNodeId} ${Logger.toJSON(info)}`);
                const data: any = {
                    nodeId: peerNodeId,
                    state: NodeStateInformation[info]
                };
                ws.sendEvent("nodeStateInformation", data)
            }
        });
        this.clusters = new Clusters(theNode);
    }

    sessionInformation() {
        return this.theNode.commissioningController?.getActiveSessionInformation() || {}
    }

    async close() {
        await this.theNode.close();
    }

}