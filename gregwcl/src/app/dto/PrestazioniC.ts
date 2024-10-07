/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { TargetUtenzeC } from "./TargetUtenzeC";

export class PrestazioniC {
    public idPrestazione: number;
    public codPrestazione: string;
    public descPrestazione: string;
    public informativa: string;
    public flag: boolean;
    public listaTargetUtenze: Array<TargetUtenzeC>;

    public constructor() {}
}