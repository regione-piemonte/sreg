/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */


export class UnisciEnte{

    codiceRegionaleEnteDest : string;
    codiceRegionaleEnteDaUnire:string;
/*
    unioneDenominazione : boolean
    unionePartitaIva : boolean;
    unioneComune : boolean;
    unioneCodIstat : boolean;
    unioneProvincia : boolean;
    unioneAsl : boolean;
    unioneEMail : boolean;
    unioneTelefono : boolean;
    unionePec : boolean;
    unioneComuni : boolean;*/
    lista: number[];
    dataMerge: Date;
    denominazioneEnteDest: string;
    denominazioneEnteDaUnire : string;
	listacheckeds: Map<String, boolean>[];

    constructor(){}
}