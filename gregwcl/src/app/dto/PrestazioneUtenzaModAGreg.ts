import { TargetUtenzaGreg } from "./TargetUtenzaGreg";

export class PrestazioneUtenzaModAGreg {
    idPrestazioneUtenza: number;
	idPrestazione: number;
	codPrestazione: string;
	descPrestazione: string;
	listaTargetUtenza: TargetUtenzaGreg[];
}