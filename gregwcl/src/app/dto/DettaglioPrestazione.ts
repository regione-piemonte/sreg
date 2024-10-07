/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { Macroaggregati } from "./Macroaggregati";
import { Prest1Prest2 } from "./Prest1Prest2";
import { Prest1PrestCollegate } from "./Prest1PrestCollegate";
import { Prest1PrestMin } from "./Prest1PrestMin";
import { PrestUtenza } from "./PrestUtenza";

export class DettaglioPrestazione {

    desTipologia: string;
	tipo: string;
	ordinamento: number;
	codPrestRegionale: string;
	desPrestRegionale: string;
	macroaggregati: Macroaggregati[];
	targetUtenzaPrestReg1: PrestUtenza[];
	notaPrestazione: string;
	prest1Prest2: Prest1Prest2[];
	prest1PrestMin: Prest1PrestMin;
	prestazioniCollegate: Prest1PrestCollegate[];

    constructor() {}
}