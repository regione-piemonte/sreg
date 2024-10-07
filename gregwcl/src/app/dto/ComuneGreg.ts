/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ProvinciaGreg } from "./ProvinciaGreg";

export class ComuneGreg {
    constructor(
        public idComune: number,
        public codIstatComune: string,
        public desComune: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
        public provincia: ProvinciaGreg
    ){}
}