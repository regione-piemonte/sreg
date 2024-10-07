/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ComuniAttivitaValoriModE } from "./ComuniAttivitaValoriModE";

export class RendicontazioneModE {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public denominazioneEnte: string;
    public annoGestione: number;
    public comuniAttivitaValori: ComuniAttivitaValoriModE[];
    public constructor() {}
}