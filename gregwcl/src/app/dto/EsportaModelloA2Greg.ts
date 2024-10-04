import { CausaleGreg } from "./CausaleGreg";
import { CronologiaGreg } from "./CronologiaGreg";
import { TrasferimentoGreg } from "./TrasferimentoGreg";

export class EsportaModelloA2Greg {
    public idEnte: number;
    public causali: CausaleGreg[];
    public trasferimentiEnteComune: TrasferimentoGreg[];
    public trasferimentiComuneEnte: TrasferimentoGreg[];
    public trasferimentiEnteComuneFiltered: TrasferimentoGreg[];
    public trasferimentiComuneEnteFiltered: TrasferimentoGreg[];
	public denominazioneEnte : string;
    public constructor() {}
}