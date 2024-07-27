/**
 * @license
 * Copyright 2022-2023 Project CHIP Authors
 * SPDX-License-Identifier: Apache-2.0
 */
import { CommissioningController } from "@project-chip/matter.js";
import { Endpoint, PairedNode } from "@project-chip/matter.js/device";
import { StorageContext } from "@project-chip/matter.js/storage";
export declare class MatterNode {
    private storageLocation;
    private readonly nodeNum;
    private readonly netInterface?;
    private storage?;
    private storageManager?;
    private storageContext?;
    commissioningController?: CommissioningController;
    private matterController?;
    constructor(storageLocation: string, nodeNum: number, netInterface?: string | undefined);
    initialize(resetStorage: boolean): Promise<void>;
    get Store(): StorageContext;
    close(): Promise<void>;
    closeStorage(): void;
    start(): Promise<void>;
    connectAndGetNode(nodeId: number): Promise<PairedNode>;
    connectAndGetNodes(): Promise<PairedNode[]>;
    iterateNodeDevices(nodes: PairedNode[], callback: (device: Endpoint, node: PairedNode) => Promise<void>, endpointId?: number): Promise<void>;
    /**
     *
     * @param root Endpoints can have child endpoints. This function recursively searches for the endpoint with the given id.
     * @param endpointId
     * @returns
     */
    private findEndpoint;
}
