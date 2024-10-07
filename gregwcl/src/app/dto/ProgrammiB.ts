/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { TitoliB } from "./TitoliB";

export class ProgrammiB {
    public idProgramma: number;
    public codProgramma: string;
    public descProgramma: string;
    public informativa: string;
    public colore: string;
    public msgInformativo: string;
    public motivazione: string;
    public flagConf: boolean;
    public listaTitolo: Array<TitoliB>;

    public constructor() {}
}