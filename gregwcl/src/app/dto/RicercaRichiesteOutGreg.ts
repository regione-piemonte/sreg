/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class RicercaRichiesteOutGreg {
    constructor(
        public codiceRegionale : string,
        public statoRendicontazione: string,
        public denominazioneEnte: string,
        public comuneSedeLegale: string,
        public tipoEnte: string  
    ) {}
}
