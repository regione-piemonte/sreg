import { TotaleMacroaggregati } from "./TotaleMacroaggregati";

export class RendicontazioneTotaliMacroaggregati {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public denominazioneEnte: string;
    public annoGestione: string;
    public valoriMacroaggregati: TotaleMacroaggregati[];

    public constructor() {}
}