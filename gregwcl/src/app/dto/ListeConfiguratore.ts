/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */


import { ModelPrestazioneAllD } from "./ModelPrestazioneAllD";
import { ModelUtenzaAllD } from "./ModelUtenzaAllD";
import { TargetUtenzeC } from "./TargetUtenzeC";

export class ListeConfiguratore {
    public id:number;
    public codice:string;
    public descrizione: string;
    public colore: string;
    
    public constructor() {}
}