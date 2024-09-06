export interface Request {
    id: string;
    namespace: string;
    function: string;
    args?: any[];
}

export interface Response {
    type: string;
    id: string;
    result?: any;
    error?: string;
}

export interface Event {
    type: string;
    data?: any;
}

export interface Message {
    type: string;
    message: any;
}

export enum MessageType {
    Result = "result",
    ResultError = "resultError",
    ResultSuccess = "resultSuccess",
}