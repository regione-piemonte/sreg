import { TotaleSpese } from "./TotaleSpese";

export class RendicontazioneTotaliSpeseMissioni {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public denominazioneEnte: string;
    public annoGestione: string;
    public valoriSpese: TotaleSpese[];

    public constructor() {}
}