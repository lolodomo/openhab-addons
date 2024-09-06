// Include this first to auto-register Crypto, Network and Time Node.js implementations
import "@project-chip/matter-node.js";

import { BridgedDeviceBasicInformationServer } from "@project-chip/matter.js/behavior/definitions/bridged-device-basic-information";
import { VendorId } from "@project-chip/matter.js/datatype";
import { logEndpoint } from "@project-chip/matter.js/device";
import { OnOffLightDevice } from "@project-chip/matter.js/devices/OnOffLightDevice";
import { ColorDimmerSwitchDevice } from "@project-chip/matter.js/devices/ColorDimmerSwitchDevice";
import { ThermostatDevice } from "@project-chip/matter.js/devices/ThermostatDevice"
import { Endpoint, EndpointServer } from "@project-chip/matter.js/endpoint";
import { MutableEndpoint } from "@project-chip/matter.js/endpoint/type";
import { AggregatorEndpoint } from "@project-chip/matter.js/endpoints/AggregatorEndpoint";
import { ServerNode } from "@project-chip/matter.js/node";
import { EndpointType } from "@project-chip/matter.js/endpoint/type";

class DeviceNode {
    private server!: ServerNode;
    private aggregator!: Endpoint;
    private deviceTypes: { [key: string]: MutableEndpoint } = {
        "OnOffLightDevice": OnOffLightDevice,
        "ColorDimmerSwitchDevice": ColorDimmerSwitchDevice,
        "ThermostatDevice": ThermostatDevice,
    }

    constructor(private deviceName: string, private vendorName: string, private passcode: number, private discriminator: number, private vendorId: number, private productName: string, private productId: number, private port: number, private uniqueId: string) {
    }
    private async init() {
        /**
         * Create a Matter ServerNode, which contains the Root Endpoint and all relevant data and configuration
         */
        this.server = await ServerNode.create({
            // Required: Give the Node a unique ID which is used to store the state of this node
            id: this.uniqueId,

            // Provide Network relevant configuration like the port
            // Optional when operating only one device on a host, Default port is 5540
            network: {
                port: this.port,
            },

            // Provide Commissioning relevant settings
            // Optional for development/testing purposes
            commissioning: {
                passcode: this.passcode,
                discriminator: this.discriminator,
            },

            // Provide Node announcement settings
            // Optional: If Ommitted some development defaults are used
            productDescription: {
                name: this.deviceName,
                deviceType: AggregatorEndpoint.deviceType,
            },

            // Provide defaults for the BasicInformation cluster on the Root endpoint
            // Optional: If Omitted some development defaults are used
            basicInformation: {
                vendorName: this.vendorName,
                vendorId: VendorId(this.vendorId),
                nodeLabel: this.productName,
                productName: this.productName,
                productLabel: this.productName,
                productId: this.productId,
                serialNumber: `matterjs-${this.uniqueId}`,
                uniqueId: this.uniqueId,
            },
        });

        this.aggregator = new Endpoint(AggregatorEndpoint, { id: "aggregator" });
        await this.server.add(this.aggregator);
        await this.server.start();
    }


    async addEndpoint(endpointType: string, nodeId: string, nodeLabel: string, productName: string, productLabel: string, serialNumber: string) {
        const deviceType = this.deviceTypes[endpointType];
        const endpoint = new Endpoint(deviceType.with(BridgedDeviceBasicInformationServer), {
            id: nodeId,
            bridgedDeviceBasicInformation: {
                nodeLabel: nodeLabel,
                productName: productName,
                productLabel: productLabel,
                serialNumber: serialNumber,
                reachable: true,
            },
        });
        await this.aggregator.add(endpoint);

        /**
         * Log the endpoint structure for debugging reasons and to allow to verify anything is correct
         */
        logEndpoint(EndpointServer.forEndpoint(this.server));
    }
}
