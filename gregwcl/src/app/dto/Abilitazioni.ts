/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ListeConfiguratore } from "./ListeConfiguratore";


export class Abilitazioni {
    public idUserProfilo: number;
    public profilo: ListeConfiguratore;
    public lista: ListeConfiguratore;
    public dataInizioValidita: Date;
    public dataFineValidita: Date;
    public stato: string;

    public constructor() { }
}