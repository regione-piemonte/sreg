/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class AllegatoGreg {
    constructor(
        public pkAllegatoAssociato : number,
        public file: string,
        public dimensione: number,
        public nomeFile: string,
        public noteFile: string,
        public tipoDocumentazione: string,
        public utenteOperazione: string,
        public nuovo?: boolean,
    ) {}
}