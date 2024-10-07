/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { MacroaggregatiRend } from "./MacroaggregatiRend";
import { ProfiloGreg } from "./ProfiloGreg";

export class RendicontazioneMacroaggregati {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public denominazioneEnte: string;
    public annoGestione: number;
    public valoriMacroaggregati: Array<MacroaggregatiRend>;
    public profilo: ProfiloGreg;

    public constructor() {}
}