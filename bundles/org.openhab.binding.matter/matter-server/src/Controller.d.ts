import { MatterNode } from "./MatterNode";
import { Commission } from "./commands/commission";
import { Nodes } from "./commands/nodes";
export declare class Controller {
    private theNode;
    commission: Commission;
    nodes: Nodes;
    constructor(theNode: MatterNode);
    close(): Promise<void>;
}
