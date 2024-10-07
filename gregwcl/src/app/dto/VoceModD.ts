/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { DettaglioVoceModD } from "./DettaglioVoceModD";

export class VoceModD {
    public sezioneModello: string;
    public listaVoci: Array<DettaglioVoceModD>;

    public constructor() {}
}