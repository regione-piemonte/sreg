import { CronologiaGreg } from "./CronologiaGreg";
import { ProfiloGreg } from "./ProfiloGreg";
import { ModelPrestazioniB1 } from "./VociModelloB1";

export class SalvaModelloB1 {
    public idEnte: number;
    public idRendicontazioneEnte: number;
    public dati: ModelPrestazioniB1[];
    public cronologia : CronologiaGreg;
	public profilo: ProfiloGreg;
    public constructor() {}
}
