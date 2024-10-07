/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

export class ResponseSalvaModelloGreg {
    public id : string;
    public esito: string;
	public messaggio:string;
    public descrizione: string;
    public warnings: string[];
    public errors: string[];
    public idEnte : number;
    public idPrestazione : number;

    public constructor() {}
}