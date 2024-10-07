/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */


import { Abilitazioni } from "./Abilitazioni";


export class Utenti {
    public idUtente:number;
    public nome: string;
    public cognome: string;
    public codiceFiscale: string;
    public email: string;
    public abilitazioni: Abilitazioni[];
    public utente: boolean;
    public maxData: Date;
    public dataFineValidita: Date;
    
    public constructor() {}
}