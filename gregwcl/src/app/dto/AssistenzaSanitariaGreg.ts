/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class AssistenzaSanitariaGreg {
    constructor(
        public idAssistenzaSanitaria : number,
        public codAssistenzaSanitaria: number,
        public descAssistenzaSanitaria: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ) {}
}