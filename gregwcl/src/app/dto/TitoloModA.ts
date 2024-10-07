/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import {TipologiaModA} from '@greg-app/app/dto/TipologiaModA';

export class TitoloModA {
    public idTitolo: number;
    public codTitolo: string;
    public descTitolo: string;
    public ordinamento: number;
    public msgInformativo: string;

    public listaTipologie: Array<TipologiaModA>;

    public totale?: string;

    public constructor() {}
}
