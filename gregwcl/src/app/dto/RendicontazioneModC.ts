/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { PrestazioniC } from "./PrestazioniC";
import { ProfiloGreg } from "./ProfiloGreg";

export class RendicontazioneModC {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public listaPrestazioni: Array<PrestazioniC>;
    public profilo: ProfiloGreg;

    public constructor() {}
}