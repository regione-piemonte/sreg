export class FileAllegatoToUploadGreg {
    constructor(
        public fileBytes: string,
        public dimensione: number,
        public nomeFile: string,
        public formatoFile: string, 
        public noteFile: string,        
        public tipoDocumento: string
    ) {}
}