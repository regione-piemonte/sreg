import {VoceModA} from '@greg-app/app/dto/VoceModA';

export class TipologiaModA {
    public idTipologia: number;
    public codTipologia: string;
    public descCodTipologia: string;
    public descTipologia: string;
    public ordinamento: number;
    public msgInformativo: string;
    public valore: string;
    public listaVoci: Array<VoceModA>;

    public constructor() {}
}
