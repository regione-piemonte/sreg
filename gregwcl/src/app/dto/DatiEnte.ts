/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { AllegatoGreg } from "./AllegatoGreg";
import { CronologiaGreg } from "./CronologiaGreg";
import { FileAllegatoToUploadGreg } from "./FileAllegatoToUploadGreg";
import { Fondi } from "./Fondi";
import { ModelTabTranche } from "./ModelTabTranche";
import { PrestazioneAssociataGreg } from "./PrestazioneAssociataGreg";
import { ProfiloGreg } from "./ProfiloGreg";
import { RendicontazioneEnteGreg } from "./RendicontazioneEnteGreg";

export class DatiEnteToSave {
    modelli: ModelTabTranche[];
    profilo: ProfiloGreg;
	rendicontazioneEnte: RendicontazioneEnteGreg;
    prestazioniAssociate: PrestazioneAssociataGreg[];
    cronologia: CronologiaGreg;
    allegatiAssociati: AllegatoGreg[];
    fileIniziale: FileAllegatoToUploadGreg;
    fileFinale: FileAllegatoToUploadGreg;
    fondi: Fondi[];
}
