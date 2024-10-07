/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { CronologiaGreg } from './CronologiaGreg';
import {RendicontazioneModAPart3} from '@greg-app/app/dto/RendicontazioneModAPart3';
import {VociModelloA} from '@greg-app/app/dto/VociModeloA';
import {VociRendModAPart3} from '@greg-app/app/dto/VociRendModAPart3';
import { ProfiloGreg } from './ProfiloGreg';


export class SalvaModelloA {
    public idEnte: number;
    public idRendicontazioneEnte: number;
    public rendicontazioneModAPart3: VociRendModAPart3[];
    public rendicontazioneModAPart1: VociModelloA;
    public cronologia: CronologiaGreg;
    public profilo: ProfiloGreg;
    public constructor() {}
}
