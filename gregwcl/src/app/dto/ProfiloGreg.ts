import { AzioneGreg } from "./AzioneGreg";
import { ListaGreg } from "./ListaGreg";

export class ProfiloGreg {
    constructor(
        public codProfilo: string,
        public descProfilo: string,
		public infoProfilo: string,
        public tipoProfilo: string,
		public descTipoProfilo: string,
		public listaenti: ListaGreg[],
		public listaazioni: Map<String, boolean[]>
    ) {}
}
