import {RendicontazioneModAPart3} from '@greg-app/app/dto/RendicontazioneModAPart3';
import {VociModelloA} from '@greg-app/app/dto/VociModeloA';
import {VociRendModAPart3} from '@greg-app/app/dto/VociRendModAPart3';


export class EsportaModelloA {
    public idEnte: number;
    public rendicontazioneModAPart3: VociRendModAPart3[];
    public rendicontazioneModAPart1: VociModelloA;
	public denominazioneEnte : string;
    public constructor() {}
}
