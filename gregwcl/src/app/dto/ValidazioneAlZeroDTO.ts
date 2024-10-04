import { CronologiaGreg } from "./CronologiaGreg";
import { ProfiloGreg } from "./ProfiloGreg";

export class ValidazioneAlZero {
    public idRendicontazioneEnte: number;
	public toggle: boolean;
	public cronologia: CronologiaGreg;
	public profilo: ProfiloGreg;
	public notaEnte: string;
	public notaInterna: string;
	public modello: string;
    public constructor() {}
}