import { CronologiaGreg } from "./CronologiaGreg";
import { ComuniAttivitaValoriModE } from "./ComuniAttivitaValoriModE";
import { ProfiloGreg } from "./ProfiloGreg";

export class SalvaModelloEGreg {
    public idEnte: number;
    public idRendicontazioneEnte: number;
    public listaRendicontazione: ComuniAttivitaValoriModE[];
    public cronologia : CronologiaGreg;
    public profilo: ProfiloGreg;
    public constructor() {}
}