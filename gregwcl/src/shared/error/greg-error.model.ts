export class GregError {
    constructor(
        public errorDesc: string,
        public message: string,
        public name: string,
        public status: string,
        public statusText: string,
        public url: string,
        public date: Date,
    ) { }

    static toGregError(json: any, errorDesc = "Si e' verificato un errore nell'applicativo.") {
        return new GregError(
            json.error.descrizione || errorDesc,
            json.message || '',
            json.name || '',
            json.status || '',
            json.statusText,
            json.url,
            new Date()
        );
    }
}
