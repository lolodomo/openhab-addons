import { Logger } from "@project-chip/matter.js/log";
import { MatterNode } from "../MatterNode";
import * as MatterClusters from "@project-chip/matter-node.js/cluster";
import { ClusterModel, CommandModel, MatterModel } from "@project-chip/matter.js/model";

const logger = Logger.get("Clusters");

export class Clusters {
    constructor(private theNode: MatterNode) {
    }

    /**
     * Dynamically executes a command on a specified cluster within a device node.
     * This method retrieves the cluster client for the device at the given node and endpoint, checks the existence
     * of the command on the cluster, and calls it with any provided arguments.
     *
     * @param nodeId Identifier for the node containing the target device.
     * @param endpointId Endpoint on the node where the command is directed.
     * @param clusterName Name of the cluster targeted by the command.
     * @param commandName Specific command to be executed on the cluster.
     * @param args Optional arguments for executing the command.
     * @throws Error if the cluster or command is not found on the device.
     */
    async command(nodeId: number, endpointId: number, clusterName: string, commandName: string, args: any[]) {
        logger.debug(`command ${nodeId} ${endpointId} ${clusterName} ${commandName} ${Logger.toJSON(args)}`);
        const device = this.theNode.getEndpoint(await this.theNode.getNode(nodeId), endpointId);
        if (device == undefined) {
            throw new Error(`Endpoint ${endpointId} not found`);
        }
        const cluster = (MatterClusters as any)[`${clusterName}Cluster`];
        const clusterClient: any = device.getClusterClient(cluster);
        if (clusterClient === undefined) {
            throw new Error(`Cluster ${clusterName} not found`);
        }
        if (typeof clusterClient[commandName] !== 'function') {
            throw new Error(`Cluster Function ${commandName} not found`);
        }
        if (args == null || Object.keys(args).length === 0) {
            return clusterClient[commandName]();
        } else {
            return clusterClient[commandName](args);
        }
    }
}