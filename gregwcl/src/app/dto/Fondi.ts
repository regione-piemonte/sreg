/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { NumberSymbol } from "@angular/common";

export class Fondi {
    idFondo: number;
    codFondo: string;
	descFondo: string;
    idRegola: number;
    codRegola: string;
	descRegola: string;
    valore: string;
    codTipologiaFondo: string;
    funzioneRegola: string;
    notModificabile: boolean;
    esistente: boolean;
    idEnte: number;
    idFondoRendicontazione: number;
    idSpesaFnps: number;
    valoreSpesaFnps: string;
    leps: boolean;
    msgInformativo: string;
    public constructor() {}
}