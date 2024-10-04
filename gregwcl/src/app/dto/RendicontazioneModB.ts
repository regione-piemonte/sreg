import { MissioniB } from "./MissioniB";
import { ProfiloGreg } from "./ProfiloGreg";

export class RendicontazioneModB {
    public idRendicontazioneEnte: number;
    public idSchedaEnteGestore: number;
    public listaMissioni: Array<MissioniB>;
    public profilo: ProfiloGreg;

    public constructor() {}
}