import { MatterNode } from "../MatterNode";

class Server {
    constructor(private theNode: MatterNode) {
    }

    async status() {
        return {
            node: await this.theNode.commissioningController?.getPort()
            // network: await this.theNode.getNetworkId(),
            // fabric: await this.theNode.getFabricId(),
            // operational: await this.theNode.isOperational(),
            // pairing: await this.theNode.isPairing(),
        };
    }
}