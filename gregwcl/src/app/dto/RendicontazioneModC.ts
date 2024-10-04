import { PrestazioniC } from "./PrestazioniC";
import { ProfiloGreg } from "./ProfiloGreg";

export class RendicontazioneModC {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public listaPrestazioni: Array<PrestazioniC>;
    public profilo: ProfiloGreg;

    public constructor() {}
}