import { NumberSymbol } from "@angular/common";

export class MacroaggregatiConf {
    public idMacroaggregati: number;
    public codMacroaggregati: string;
    public descMacroaggregati: string;
    public dal: Date;
    public al: Date;
    public dataMin: Date;
    public modificabile: boolean;
    public dataCancellazione: Date;
    dataCreazione: Date;

    public constructor() {}
}