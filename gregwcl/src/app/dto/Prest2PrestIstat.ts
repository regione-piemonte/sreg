import { NumberSymbol } from "@angular/common";
import { PrestUtenza } from "./PrestUtenza";

export class Prest2PrestIstat {
	idPrestIstat: number;
	codPrestIstat: string;
	descPrestIstat: string;
	utenzeMin: string[];
	utenzeMinConf: PrestUtenza[];
	dataCancellazione: Date;
	dataCreazione: Date;

    public constructor() {}
}