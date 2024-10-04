import { ProfiloGreg } from "./ProfiloGreg";


export class UserInfoGreg {
    constructor(
        public codFisc: string,
        public cognome: string,
        public nome: string,
        public idAura: string,
		public listaprofili: ProfiloGreg[]
    ){}    
}