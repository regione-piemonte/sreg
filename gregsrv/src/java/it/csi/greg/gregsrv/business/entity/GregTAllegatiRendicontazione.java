/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_t_allegati_rendicontazione database table.
 * 
 */
@Entity
@Table(name="greg_t_allegati_rendicontazione")
@NamedQueries({
	@NamedQuery(name="GregTAllegatiRendicontazione.findAll", query="SELECT g FROM GregTAllegatiRendicontazione g"),
	@NamedQuery(name="GregTAllegatiRendicontazione.findByIdNotDeleted", query="SELECT g FROM GregTAllegatiRendicontazione g WHERE g.idAllegatiRendicontazione = :idAllegato AND g.dataCancellazione IS NULL ")
})
public class GregTAllegatiRendicontazione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_ALLEGATI_RENDICONTAZIONE_IDALLEGATIRENDICONTAZIONE_GENERATOR", sequenceName="GREG_T_ALLEGATI_RENDICONTAZIONE_ID_ALLEGATI_RENDICONTAZIONE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_ALLEGATI_RENDICONTAZIONE_IDALLEGATIRENDICONTAZIONE_GENERATOR")
	@Column(name="id_allegati_rendicontazione")
	private Integer idAllegatiRendicontazione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="file_allegato")
	private byte[] fileAllegato;

	@Column(name="file_size")
	private BigDecimal fileSize;

	@Column(name="nome_file")
	private String nomeFile;

	@Column(name="note_file")
	private String noteFile;

	@Column(name="tipo_documentazione")
	private String tipoDocumentazione;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregTAllegatiRendicontazione() {
	}

	public Integer getIdAllegatiRendicontazione() {
		return this.idAllegatiRendicontazione;
	}

	public void setIdAllegatiRendicontazione(Integer idAllegatiRendicontazione) {
		this.idAllegatiRendicontazione = idAllegatiRendicontazione;
	}

	public Timestamp getDataCancellazione() {
		return this.dataCancellazione;
	}

	public void setDataCancellazione(Timestamp dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}

	public Timestamp getDataCreazione() {
		return this.dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataModifica() {
		return this.dataModifica;
	}

	public void setDataModifica(Timestamp dataModifica) {
		this.dataModifica = dataModifica;
	}

	public byte[] getFileAllegato() {
		return this.fileAllegato;
	}

	public void setFileAllegato(byte[] fileAllegato) {
		this.fileAllegato = fileAllegato;
	}

	public BigDecimal getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(BigDecimal fileSize) {
		this.fileSize = fileSize;
	}

	public String getNomeFile() {
		return this.nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getNoteFile() {
		return this.noteFile;
	}

	public void setNoteFile(String noteFile) {
		this.noteFile = noteFile;
	}

	public String getTipoDocumentazione() {
		return this.tipoDocumentazione;
	}

	public void setTipoDocumentazione(String tipoDocumentazione) {
		this.tipoDocumentazione = tipoDocumentazione;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}
	public GregTAllegatiRendicontazione(Integer idAllegatiRendicontazione, Timestamp dataCancellazione,
			Timestamp dataCreazione, Timestamp dataModifica, byte[] fileAllegato, BigDecimal fileSize, String nomeFile,
			String noteFile, String tipoDocumentazione, String utenteOperazione,
			GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.idAllegatiRendicontazione = idAllegatiRendicontazione;
		this.dataCancellazione = dataCancellazione;
		this.dataCreazione = dataCreazione;
		this.dataModifica = dataModifica;
		this.fileAllegato = fileAllegato;
		this.fileSize = fileSize;
		this.nomeFile = nomeFile;
		this.noteFile = noteFile;
		this.tipoDocumentazione = tipoDocumentazione;
		this.utenteOperazione = utenteOperazione;
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}
	
	public GregTAllegatiRendicontazione(GregTAllegatiRendicontazione allegato) {
		this.idAllegatiRendicontazione = allegato.getIdAllegatiRendicontazione();
		this.dataCancellazione = allegato.getDataCancellazione();
		this.dataCreazione = allegato.getDataCreazione();
		this.dataModifica = allegato.getDataModifica();
		this.fileAllegato = allegato.getFileAllegato();
		this.fileSize = allegato.getFileSize();
		this.nomeFile = allegato.getNomeFile();
		this.noteFile = allegato.getNoteFile();
		this.tipoDocumentazione = allegato.getTipoDocumentazione();
		this.utenteOperazione = allegato.getUtenteOperazione();
		this.gregTRendicontazioneEnte = allegato.getGregTRendicontazioneEnte();
	}

}