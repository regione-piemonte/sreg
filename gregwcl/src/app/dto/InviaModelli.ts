/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ProfiloGreg } from "./ProfiloGreg";

export class InviaModelli {
        public idEnte : number;
        public tranche: string;
        public operazione: string;
        public nota: string;
        public esito: string;
	    public tranchestesa: string;
	    public profilo: ProfiloGreg;
      public modello: string;
  public constructor() {}
}
