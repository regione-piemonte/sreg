/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { NumberSymbol } from "@angular/common";

export class Prest1PrestMin {
    idPrestMin: number;
    codMacro: string;
	descMacro: string;
    codPrestMin: string;
	descPrestMin: string;
    utenzeMin: string[];
    dal: Date;
	al: Date;
     dataMin: Date;
	modificabile: boolean;
    dataCancellazione: Date;
    dataCreazione: Date;

    public constructor() {}
}