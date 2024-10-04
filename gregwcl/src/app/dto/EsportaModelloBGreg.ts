import { MissioniB } from "./MissioniB";
import { RendicontazioneModB } from "./RendicontazioneModB";


export class EsportaModelloBGreg {
    public idEnte: number;
    public totaleSpesaTutteMissioni: number;
    public totaleSpesaCorrenteMis1204: number;
    public totaleSpesaCorrenteMis01041215: number;
    public totaleSpesaCorrenteTutteMissioni: number;
    public datiB: RendicontazioneModB;
    public missioniB: MissioniB[];
    public rowTotaleSpeseContoCapitale : string[];
    public rowTotaleSpeseIncrementoAttFinanz : string[];
    public rowTotaleMissione : string[];
    public rowTotaleSpeseCorrenti : string[];
	public denominazioneEnte : string;
    public constructor() {}
}