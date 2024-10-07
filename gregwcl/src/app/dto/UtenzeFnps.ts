/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { NumberSymbol } from "@angular/common";

export class UtenzeFnps {
    idUtenza: number;
    codUtenza: string;
	descUtenza: string;
    idUtenzaFnps: number;
    valorePercentuale: string;
    utilizzatoPerCalcolo: boolean;
    annoInizioValidita: string;
    annoFineValidita: string;
    modificabile: boolean;
    
    public constructor() {}
}