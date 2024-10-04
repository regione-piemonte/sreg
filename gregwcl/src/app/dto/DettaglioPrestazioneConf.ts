import { MacroaggregatiConf } from "./MacroaggregatiConf";
import { Prest1Prest2 } from "./Prest1Prest2";
import { Prest1PrestCollegate } from "./Prest1PrestCollegate";
import { Prest1PrestMin } from "./Prest1PrestMin";
import { PrestUtenza } from "./PrestUtenza";

export class DettaglioPrestazioneConf {

    codTipologia: string;
	tipoStruttura: string;
	tipoQuota: string;
	idTipologia: number;
	idTipoStruttura: number;
	idTipoQuota: number;
	ordinamento: number;
	idPrestazione: number;
	codPrestRegionale: string;
	desPrestRegionale: string;
	dal: Date;
	al: Date;
	modificabile: boolean;
	macroaggregati: MacroaggregatiConf[];
	targetUtenzaPrestReg1: PrestUtenza[];
	notaPrestazione: string;
	prest1Prest2: Prest1Prest2[];
	prest1PrestMin: Prest1PrestMin[];
	prestazioniCollegate: Prest1PrestCollegate[];
	dataMin: Date;
	dataCreazione: Date;

    constructor() {}
}