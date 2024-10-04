/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;


public class ModelFileAllegatoToUpload {
	
	private String fileBytes;
	private BigDecimal dimensione;
	private String nomeFile;
	private String formatoFile;
	private String noteFile;
	private String tipoDocumento;	
	
	public ModelFileAllegatoToUpload() { }

	public String getFileBytes() {
		return fileBytes;
	}

	public void setFileBytes(String fileBytes) {
		this.fileBytes = fileBytes;
	}

	public BigDecimal getDimensione() {
		return dimensione;
	}

	public void setDimensione(BigDecimal dimensione) {
		this.dimensione = dimensione;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getFormatoFile() {
		return formatoFile;
	}

	public void setFormatoFile(String formatoFile) {
		this.formatoFile = formatoFile;
	}

	public String getNoteFile() {
		return noteFile;
	}

	public void setNoteFile(String noteFile) {
		this.noteFile = noteFile;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
}