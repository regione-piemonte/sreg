/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

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