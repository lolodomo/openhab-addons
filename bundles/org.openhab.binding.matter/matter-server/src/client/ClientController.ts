import { NodeStateInformation } from "@project-chip/matter.js/device";
import { Logger } from"@project-chip/matter.js/log";
import { MatterNode } from "./MatterNode";
import { Nodes } from "./namespaces/Nodes";
import { Clusters } from "./namespaces/Clusters";
import { WebSocketSession } from "../app";
import { Request, MessageType } from '../MessageTypes';
import { Controller } from "../Controller";

const logger = Logger.get("Controller");

/**
 * This class exists to expose the "nodes" and "clusters" namespaces to websocket clients
 */
export class ClientController extends Controller {
    
    nodes: Nodes;
    clusters: Clusters;
    
    constructor(override ws: WebSocketSession, private theNode: MatterNode) {
        super(ws);
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

    async close() {
        await this.theNode.close();
    }

    async handleRequest(request: Request): Promise<void> {
        const { id, namespace, function: functionName, args } = request;
        logger.debug(`Received request: ${Logger.toJSON(request)}`);
        try {
            const result = this.executeCommand(namespace, functionName, args || []);
            if (result instanceof Promise) {
                result.then((asyncResult) => {
                    this.ws.sendResponse(MessageType.ResultSuccess, id, asyncResult);
                }).catch((error) => {
                    this.printError(error, functionName);
                    this.ws.sendResponse(MessageType.ResultError, id, undefined, error.message);
                });
            } else {
                this.ws.sendResponse(MessageType.ResultSuccess, id, result);
            }
        } catch (error) {
            if (error instanceof Error) {
                this.printError(error, functionName);
                this.ws.sendResponse(MessageType.ResultError, id, undefined, error.message);
            } else {
                logger.error(`Unexpected error executing function ${functionName}: ${error}`);
                this.ws.sendResponse(MessageType.ResultError, id, undefined, String(error));
            }
        }
    }
    
     executeCommand(namespace: string, functionName: string, args: any[]): any | Promise<any> {
        const controllerAny: any = this;
        let baseObject: any;
    
        logger.debug(`Executing function ${namespace}.${functionName}(${Logger.toJSON(args)})`);
       
        if (typeof controllerAny[namespace] !== 'object') {
            throw new Error(`Namespace ${namespace} not found`);
        }
         
        baseObject = controllerAny[namespace];
        if (typeof baseObject[functionName] !== 'function') {
            throw new Error(`Function ${functionName} not found`);
        }
         
        return baseObject[functionName](...args);
     }
    
    printError(error: Error, functionName: String) {

        logger.error(`Error executing function ${functionName}: ${error.message}`);
        logger.error(`Stack trace: ${error.stack}`);

        // Log additional error properties if available
        if ('code' in error) {
            logger.error(`Error code: ${(error as any).code}`);
        }
        if ('name' in error) {
            logger.error(`Error name: ${(error as any).name}`);
        }

        // Fallback: log the entire error object in case there are other useful details
        logger.error(`Full error object: ${JSON.stringify(error, Object.getOwnPropertyNames(error))}`)
    }
}