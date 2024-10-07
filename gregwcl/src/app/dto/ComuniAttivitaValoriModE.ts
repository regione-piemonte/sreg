/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { RendicontazioneAttivitaModE } from "./RendicontazioneAttivitaModE";

export class ComuniAttivitaValoriModE {
    public idComune: number;
    public codIstatComune: string;
    public descComune: string;
    public attivita: RendicontazioneAttivitaModE[];
    public constructor() {}
}