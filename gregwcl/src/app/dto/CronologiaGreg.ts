/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { StatoRendicontazioneGreg } from "./StatoRendicontazioneGreg";

export class CronologiaGreg {
    public idCronologia: number;
    public notaEnte: string;
    public notaInterna: string;
    public utente: string;
    public modello: string;
    public dataOra: Date;
    public utenteOperazione: string;
    public dataCreazione: Date;
    public dataModifica: Date;
    public dataCancellazione: Date;
    public statoRendicontazione: StatoRendicontazioneGreg
    constructor(){}
    
}