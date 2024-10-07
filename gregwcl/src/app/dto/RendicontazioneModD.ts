/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ProfiloGreg } from "./ProfiloGreg";
import { VociRendModD } from "./VociRendModD";

export class RendicontazioneModD {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public denominazioneEnte: string;
    public annoGestione: number;
    public vociModello: Array<VociRendModD>;
    public profilo: ProfiloGreg;
    public constructor() {}
}