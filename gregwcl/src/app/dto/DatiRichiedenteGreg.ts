export class DatiRichiedenteGreg {
        constructor (
                public id: number,
                public score: number,
                public nome: string,
                public cognome: string,
                public sesso: string,
                public data_nascita: string,
                public codice_fiscale: string,
                public eta: number,
                public luogo_nascita: string,
                public residenza_dichiarata: string,
                public domicilio_dichiarato: string,
                public asl_residenza_dichiarata: string,
                public asl_domicilio_dichiarato: string,
                public cittadinanza:string,
                public distretto_domicilio_dichiarato: string,
                public ambito_domicilio_dichiarato: string,
                public telefono: string,
        ) {}
        
}
