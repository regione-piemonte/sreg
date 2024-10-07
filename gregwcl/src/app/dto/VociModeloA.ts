/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { DettaglioVoceModA } from './DettaglioVoceModA';
import {TitoloModA} from '@greg-app/app/dto/TitoloModA';
import {VociRendModAPart3} from '@greg-app/app/dto/VociRendModAPart3';

export class VociModelloA {
    public listaTitoli: Array<TitoloModA>;
    public listaVociModAPart3: Array<VociRendModAPart3>;
    public constructor() {}
}
