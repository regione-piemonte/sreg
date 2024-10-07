/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class GenericResponseWarnCheckErrGreg {
    constructor(
        public id : string,
        public descrizione: string,
        public warnings: string[],
        public errors: string[],
        public check: string[],
        public valorizzato: boolean
    ) {}
}