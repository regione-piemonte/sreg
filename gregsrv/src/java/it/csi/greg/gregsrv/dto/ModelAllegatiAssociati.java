/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.math.BigDecimal;
import java.util.Base64;
import it.csi.greg.gregsrv.business.entity.GregTAllegatiRendicontazione;
import it.csi.greg.gregsrv.util.SharedConstants;


public class ModelAllegatiAssociati {
	
	private Integer pkAllegatoAssociato;
	private String file;
	private BigDecimal dimensione;
	private String nomeFile;
	private String noteFile;
	private String tipoDocumentazione;
	private String utenteOperazione;
	private Boolean nuovo;
	
	
	public ModelAllegatiAssociati() { }
	
	public ModelAllegatiAssociati(Object[] obj) { 
		this.pkAllegatoAssociato = (Integer) obj[0];
		this.dimensione = (BigDecimal) obj[1];
		this.nomeFile = String.valueOf(obj[2]);
		this.noteFile = String.valueOf(obj[3]);
		this.tipoDocumentazione = String.valueOf(obj[4]).equals(SharedConstants.TRANCHEI)? SharedConstants.DOC_INIZIALE : SharedConstants.DOC_FINALE;
		this.utenteOperazione = String.valueOf(obj[5]);
	}
	
	public ModelAllegatiAssociati(GregTAllegatiRendicontazione allegato) {
		this.pkAllegatoAssociato = allegato.getIdAllegatiRendicontazione();
//		this.file = Base64.getEncoder().encodeToString(allegato.getFileAllegato());
		this.dimensione = allegato.getFileSize();
		this.nomeFile = allegato.getNomeFile();
		this.noteFile = allegato.getNoteFile();
		this.tipoDocumentazione = allegato.getTipoDocumentazione().equals(SharedConstants.TRANCHEI)? SharedConstants.DOC_INIZIALE : SharedConstants.DOC_FINALE;
		this.utenteOperazione = allegato.getUtenteOperazione();
	}
	
	public ModelAllegatiAssociati(String nomeFile, byte[] fileToDownload) {
		this.nomeFile = nomeFile;
		this.file = Base64.getEncoder().encodeToString(fileToDownload);
	}

	public Integer getPkAllegatoAssociato() {
		return pkAllegatoAssociato;
	}

	public void setPkAllegatoAssociato(Integer pkAllegatoAssociato) {
		this.pkAllegatoAssociato = pkAllegatoAssociato;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
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

	public String getNoteFile() {
		return noteFile;
	}

	public void setNoteFile(String noteFile) {
		this.noteFile = noteFile;
	}

	public String getTipoDocumentazione() {
		return tipoDocumentazione;
	}

	public void setTipoDocumentazione(String tipoDocumentazione) {
		this.tipoDocumentazione = tipoDocumentazione;
	}

	public String getUtenteOperazione() {
		return utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Boolean getNuovo() {
		return nuovo;
	}

	public void setNuovo(Boolean nuovo) {
		this.nuovo = nuovo;
	}
	
}