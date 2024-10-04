import { CausaleGreg } from "./CausaleGreg";
import { CronologiaGreg } from "./CronologiaGreg";
import { ProfiloGreg } from "./ProfiloGreg";
import { TrasferimentoGreg } from "./TrasferimentoGreg";

export class SalvaModelloA2Greg {
    public idEnte: number;
    public idRendicontazioneEnte: number;
    public causali: CausaleGreg[];
    public trasferimentiEnteComune: TrasferimentoGreg[];
    public trasferimentiComuneEnte: TrasferimentoGreg[];
    public cronologia : CronologiaGreg;
    public profilo : ProfiloGreg;
    public constructor() {}
}