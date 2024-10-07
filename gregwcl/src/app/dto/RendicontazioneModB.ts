/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { MissioniB } from "./MissioniB";
import { ProfiloGreg } from "./ProfiloGreg";

export class RendicontazioneModB {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public listaMissioni: Array<MissioniB>;
    public profilo: ProfiloGreg;

    public constructor() {}
}