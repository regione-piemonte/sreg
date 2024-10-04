/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_t_cronologia database table.
 * 
 */
@Entity
@Table(name="greg_t_cronologia")
@NamedQueries({
@NamedQuery(name="GregTCronologia.findAll", query="SELECT g FROM GregTCronologia g WHERE g.dataCancellazione IS NULL"),
@NamedQuery(name="GregTCronologia.findByIdNotDeleted", query="SELECT g FROM GregTCronologia g WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione AND g.dataCancellazione IS NULL ORDER BY g.dataOra ASC"),
@NamedQuery(name="GregTCronologia.findByIdNotDeletedDesc", query="SELECT g FROM GregTCronologia g WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione AND g.dataCancellazione IS NULL ORDER BY g.dataOra DESC")
})
public class GregTCronologia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_CRONOLOGIA_IDCRONOLOGIA_GENERATOR", sequenceName="GREG_T_CRONOLOGIA_ID_CRONOLOGIA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_CRONOLOGIA_IDCRONOLOGIA_GENERATOR")
	@Column(name="id_cronologia")
	private Integer idCronologia;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="data_ora")
	private Timestamp dataOra;

	@Column(name="modello")
	private String modello;
	
	@Column(name="nota_interna")
	private String notaInterna;

	@Column(name="nota_per_ente")
	private String notaPerEnte;

	private String utente;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDStatoRendicontazione
	@ManyToOne
	@JoinColumn(name="id_stato_rendicontazione")
	private GregDStatoRendicontazione gregDStatoRendicontazione;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;
	
	@OneToOne(mappedBy="gregTCronologia")
	private GregRCheck gregRCheck;

	public GregTCronologia() {
	}

	public Integer getIdCronologia() {
		return this.idCronologia;
	}

	public void setIdCronologia(Integer idCronologia) {
		this.idCronologia = idCronologia;
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

	public Timestamp getDataOra() {
		return this.dataOra;
	}

	public void setDataOra(Timestamp dataOra) {
		this.dataOra = dataOra;
	}

	public String getNotaInterna() {
		return this.notaInterna;
	}

	public void setNotaInterna(String notaInterna) {
		this.notaInterna = notaInterna;
	}

	public String getNotaPerEnte() {
		return this.notaPerEnte;
	}

	public void setNotaPerEnte(String notaPerEnte) {
		this.notaPerEnte = notaPerEnte;
	}

	public String getUtente() {
		return this.utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDStatoRendicontazione getGregDStatoRendicontazione() {
		return this.gregDStatoRendicontazione;
	}

	public void setGregDStatoRendicontazione(GregDStatoRendicontazione gregDStatoRendicontazione) {
		this.gregDStatoRendicontazione = gregDStatoRendicontazione;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

}