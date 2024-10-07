/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { TotaleMacroaggregati } from "./TotaleMacroaggregati";

export class RendicontazioneTotaliMacroaggregati {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public denominazioneEnte: string;
    public annoGestione: string;
    public valoriMacroaggregati: TotaleMacroaggregati[];

    public constructor() {}
}