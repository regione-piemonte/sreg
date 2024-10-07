/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ProfiloGreg } from "./ProfiloGreg";

export class ChiusuraEnte {
    public idScheda: number;
    public dataChiusura: Date;
    public motivazione: string;
    public notaEnte: string;
    public notaInterna: string;
    public profilo: ProfiloGreg;
    public denominazione: string;
    public email: string;
    public nome: string;
    public cognome: string;
    public emailResp: string;

    public constructor() {}
}