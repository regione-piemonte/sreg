/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { CausaleGreg } from "./CausaleGreg";
import { ComuneAssociatoGreg } from "./ComuneAssociatoGreg";
import { ComuneGreg } from "./ComuneGreg";

export class TrasferimentoGreg {
    public id: number;
    public comune: ComuneAssociatoGreg;
    public causale: CausaleGreg;
    public importo: number;
    
    constructor(){}
}