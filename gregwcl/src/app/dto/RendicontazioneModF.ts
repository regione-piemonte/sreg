/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ConteggiF } from "./ConteggiF";
import { ProfiloGreg } from "./ProfiloGreg";

export class RendicontazioneModF {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public conteggi: ConteggiF;
    public profilo: ProfiloGreg;

    public constructor() {}
}