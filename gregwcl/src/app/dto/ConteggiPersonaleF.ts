/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { PersonaleEnteF } from "./PersonaleEnteF";
import { ProfiloProfessionaleF } from "./ProfiloProfessionaleF";

export class ConteggiPersonaleF {
    public listaProfiloProfessionale: Array<ProfiloProfessionaleF>;
    public listaPersonaleEnte: Array<PersonaleEnteF>;

    public constructor() {}
}