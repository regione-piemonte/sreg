import { ProfiloGreg } from "./ProfiloGreg";
import { VociRendModD } from "./VociRendModD";

export class RendicontazioneModD {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public denominazioneEnte: string;
    public annoGestione: number;
    public vociModello: Array<VociRendModD>;
    public profilo: ProfiloGreg;
    public constructor() {}
}