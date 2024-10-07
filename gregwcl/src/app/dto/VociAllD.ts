/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */


import { Aree } from "./Aree";
import { ModelPrestazioneAllD } from "./ModelPrestazioneAllD";
import { ModelUtenzaAllD } from "./ModelUtenzaAllD";
import { TargetUtenzeC } from "./TargetUtenzeC";

export class VociAllD {
    public aree: Array<Aree>;
    public listaTargetUtenze: Array<ModelUtenzaAllD>;
    public listaPrestazione: Array<ModelPrestazioneAllD>;
    

    public constructor() {}
}