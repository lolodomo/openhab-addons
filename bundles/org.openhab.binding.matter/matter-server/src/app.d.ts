/**
 * @license
 * Copyright 2022-2023 Project CHIP Authors
 * SPDX-License-Identifier: Apache-2.0
 */
import "../time/register";
import "../crypto/register";
import "../net/register";
import { Controller } from "./Controller";
/**
 * @file Top level application for Matter Node.
 */
export declare function createController(stoagePath: string, nodeNum: number, factoryReset?: boolean): Promise<Controller>;
export declare function getController(nodeNum: number): Controller;
export declare function close(nodeNum: number): Promise<void>;
export declare function closeAll(): Promise<void>;
