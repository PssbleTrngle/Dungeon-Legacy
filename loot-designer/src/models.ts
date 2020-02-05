export interface IData {
    groups: {
        [key: string]: IGroup;
    };
}

export type IRange = {
    min: number;
    max: number;
} | number;

export const enum EntryType {
    STACK, GROUP, REFERENCE
}

interface IBaseEntry {
    functions?: IFunction[];
}

export type IEntry = IGroup | IReference | IStack;

export interface IReference extends IBaseEntry {
    type: EntryType.REFERENCE;
    to: string;
}

export interface IGroup extends IBaseEntry {
    type: EntryType.GROUP;
    entries: IEntry[];
}

export interface IStack extends IBaseEntry {
    id: string;
    type: EntryType.STACK;
    amount: IRange;
}

export interface IFunction {
    id: string;
}