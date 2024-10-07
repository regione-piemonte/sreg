/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

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