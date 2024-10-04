import { NumberSymbol } from "@angular/common";
import { Nomenclatore } from "./Nomenclatore";
import { Prest2PrestIstat } from "./Prest2PrestIstat";
import { PrestUtenza } from "./PrestUtenza";

export class Prest1Prest2 {
	idPrest2: number;
    codPrest2: string;
	descPrest2: string;
    codPrestIstat: string;
	descPrestIstat: string;
	idTipologia: number;
    codTipologia: string;
    prest2: boolean;
    utenze: string[];
    utenzeMin: string[];
	utenzeConf: PrestUtenza[];
    utenzeMinConf: PrestUtenza[];
	prestIstat: Prest2PrestIstat[];
	dataCreazione: Date;

    macroArea: string;
	sottoArea: string;
	sottoAreaDet: string;
	presidio: string;
	classificazionePresidio: string;
	funzionePresidio: string;
	voce: string;
	sottoVoce: string;
	nota: string;
	tipoResidenza: string;
	nomenclatore: Nomenclatore[];
	ordinamento: number;
    x:number;
	dal: Date;
	al: Date;
	dalRelazione: Date;
	alRelazione: Date;
	dataMin: Date;
	dataLastRelazione: Date;
	modificabile: boolean;
	utilizzato: boolean;
	dataCancellazione: Date;
	idPrest1: number;
	
    public constructor() {}
}