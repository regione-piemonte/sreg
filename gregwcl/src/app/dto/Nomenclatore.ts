import { NumberSymbol } from "@angular/common";
import { PrestUtenza } from "./PrestUtenza";

export class Nomenclatore {
   
	idNomenclatore: number;
	codNomenclatore: string;
    descMacroArea: string;
	descSottoArea: string;
	descSottoAreaDet: string;
	descPresidio: string;
	descClassificazionePresidio: string;
	descFunzionePresidio: string;
	descVoce: string;
	descSottoVoce: string;
	descTipoResidenza: string;
	codMacroArea: string;
	codSottoArea: string;
	codSottoAreaDet: string;
	codPresidio: string;
	codClassificazionePresidio: string;
	codFunzionePresidio: string;
	codVoce: string;
	codSottoVoce: string;
	codTipoResidenza: string;
	dal: Date;
	al: Date;
	modificabile: boolean;
	cancellabile: boolean;
	dataCancellazione: Date;
	
    public constructor() {}
}