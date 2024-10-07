/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class GenericResponseWarnErrGreg {
    constructor(
        public id : string,
        public descrizione: string,
        public ok: string[],
        public warnings: string[],
        public errors: string[],
        public obblMotivazione: boolean,
        public idEnte: number,
        public idPrestazione: number,
        public warningCheck: string,
        public dataCreazione: Date
    ) {}
}