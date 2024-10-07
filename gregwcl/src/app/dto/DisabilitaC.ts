/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { DettagliDisabilitaC } from "./DettagliDisabilitaC";

export class DisabilitaC {
    public idDisabilita: number;
    public codDisabilita: string;
    public descDisabilita: string;
    public listaDettagliDisabilita: Array<DettagliDisabilitaC>;
    public valore: string;

    public constructor() {}
}