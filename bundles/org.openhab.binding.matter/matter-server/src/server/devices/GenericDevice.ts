import { Endpoint, EndpointServer } from "@project-chip/matter.js/endpoint";

export abstract class GenericDevice {
    constructor() {
    }

    abstract updateValue(key: string, value: any): Promise<void>;
    
    abstract getEndpoint(): Endpoint;

    sendEvent(event: string, data: any) {
        console.log(`Sending event: ${event} with data: ${data}`);
    }
}