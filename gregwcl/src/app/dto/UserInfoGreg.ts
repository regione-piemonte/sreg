/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ProfiloGreg } from "./ProfiloGreg";


export class UserInfoGreg {
    constructor(
        public codFisc: string,
        public cognome: string,
        public nome: string,
        public idAura: string,
		public listaprofili: ProfiloGreg[]
    ){}    
}