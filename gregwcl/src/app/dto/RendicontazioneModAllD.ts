import { Fondi } from "./Fondi";
import { MissioniB } from "./MissioniB";
import { ProfiloGreg } from "./ProfiloGreg";
import { VociAllD } from "./VociAllD";

export class RendicontazioneModAllD {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public vociAllD: VociAllD;
    public giustificazione: string;
    public giustificazionePerInf: boolean; 
    public profilo: ProfiloGreg;
    public azioniSistema: Fondi[];
    public quote: Fondi[];
    public azzeramentoGiustificativo: boolean;

    public constructor() {}
}