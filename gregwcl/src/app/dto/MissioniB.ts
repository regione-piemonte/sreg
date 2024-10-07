/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { ProgrammiB } from "./ProgrammiB";

export class MissioniB {
    public idMissione: number;
    public codMissione: string;
    public descMissione: string;
    public altraDescMissione: string;
    public msgInformativo: string;
    public listaProgramma: Array<ProgrammiB>;

    public constructor() {}
}