/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { AnagraficaEnteGreg } from "./AnagraficaEnteGreg";
import { ComuneAssociatoGreg } from "./ComuneAssociatoGreg";
import { ListaGreg } from "./ListaGreg";
import { ProfiloGreg } from "./ProfiloGreg";

export class DatiAnagraficiToSave {
    datiEnte: AnagraficaEnteGreg;
    comuniAssociati: ComuneAssociatoGreg[];
    modificaEnte: boolean;
    modificaResp: boolean;
    modificaComune: boolean;
    profilo: ProfiloGreg;
    dataModifica: Date;
    dataApertura: Date;
    listaSelezionata: ListaGreg;
    sameData: boolean;
}
