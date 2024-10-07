/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { SottotitoliB } from "./SottotitoliB";

export class TitoliB {
    public idTitolo: number;
    public codTitolo: string;
    public descTitolo: string;
    public altraDescTitolo: string;
    public msgInformativo: string;
    public valore: string;
    public listaSottotitolo: Array<SottotitoliB>;

    public constructor() {}
}