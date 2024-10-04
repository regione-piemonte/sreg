import { Aree } from "./Aree";
import { ModelPrestazioneAllD } from "./ModelPrestazioneAllD";
import { ModelUtenzaAllD } from "./ModelUtenzaAllD";
import { PrestazioniC } from "./PrestazioniC";
import { RendicontazioneModAllD } from "./RendicontazioneModAllD";
import { RendicontazioneModC } from "./RendicontazioneModC";


export class EsportaModuloFnps {
    public idEnte: number;
    public annoGestione: string;
    public totaleUtenze: Array<string>;
    public datiFnps: RendicontazioneModAllD;
    public prestazioniFnps: ModelPrestazioneAllD[];
    public utenzeFnps: ModelUtenzaAllD[];
    public areeUtenze:  Array<Aree>;
    public fnps: string;
	public denominazioneEnte : string;
	public totaloneUtenze : string;
	public residuo : string;
	public giustificazione : string;
    public residuoAzioni : string;
    public constructor() {}
}