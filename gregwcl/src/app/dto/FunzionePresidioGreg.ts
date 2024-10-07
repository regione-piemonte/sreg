/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ComuniGreg {
    constructor(
        public idFunzionePresidio: number,
        public codFunzionePresidio: number,
        public descFunzionePresidio: string,
        public utenteOperazione: string,
        public dataCreazione: Date,
        public dataModifica: Date,
        public dataCancellazione: Date,
    ){}
}