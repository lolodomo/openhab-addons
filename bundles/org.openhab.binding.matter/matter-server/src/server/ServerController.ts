import { Controller } from "../Controller";
import { Request } from "../MessageTypes";
import { WebSocketSession } from "../app";

export class ServerController extends Controller {

    constructor(ws: WebSocketSession) {
        super(ws);
    }

    handleRequest(request: Request): Promise<void> {
        throw new Error("Method not implemented.");
    }

    close(): void {
    }
}