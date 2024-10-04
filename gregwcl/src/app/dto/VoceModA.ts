import { DettaglioVoceModA } from './DettaglioVoceModA';
import { PrestazioniModAGreg } from './PrestazioniModAGreg';

export class VoceModA {
    public idVoce: number;
    public codVoce: string;
    public descVoce: string;
    public ordinamento: number;
    public msgInformativo: string;
    public valoreText: string;
    public valoreNumb: string;
    public prestazioni: PrestazioniModAGreg;
    public rotate?: string = 'default';
    public showPrestazioni?: boolean = false;

    public constructor() {}
}
