/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { NumberSymbol } from "@angular/common";

export class PrestUtenza {
    idUtenza: number;
    codUtenza: string;
	descUtenza: string;
    codMissioneProgramma: string;
	descMissioneProgramma: string;
    coloreMissioneProgramma: string;
    codTipologiaSpesa: string;
	descTipologiaSpesa: string;
	dal: Date;
	al: Date;
    dataMin: Date;
    modificabile: boolean;
    cancellabile: boolean;
    dataCancellazione: Date;
    dataCreazione: Date;

    public constructor() {}
}