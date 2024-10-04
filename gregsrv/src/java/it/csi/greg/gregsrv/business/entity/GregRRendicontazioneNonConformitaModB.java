/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_rendicontazione_non_conformita_mod_b database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_non_conformita_mod_b")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneNonConformitaModB.findAll", query="SELECT g FROM GregRRendicontazioneNonConformitaModB g"),
	@NamedQuery(name="GregRRendicontazioneNonConformitaModB.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneNonConformitaModB g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.motivazione is not null")
})
public class GregRRendicontazioneNonConformitaModB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_NON_CONFORMITA_MOD_B_IDRENDICONTAZIONENONCONFORMITAMODB_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_NON_CO_ID_RENDICONTAZIONE_NON_CONFOR_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_NON_CONFORMITA_MOD_B_IDRENDICONTAZIONENONCONFORMITAMODB_GENERATOR")
	@Column(name="id_rendicontazione_non_conformita_mod_b")
	private Integer idRendicontazioneNonConformitaModB;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	private String motivazione;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDProgrammaMissione
	@ManyToOne
	@JoinColumn(name="id_programma_missione")
	private GregDProgrammaMissione gregDProgrammaMissione;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneNonConformitaModB() {
	}

	public Integer getIdRendicontazioneNonConformitaModB() {
		return this.idRendicontazioneNonConformitaModB;
	}

	public void setIdRendicontazioneNonConformitaModB(Integer idRendicontazioneNonConformitaModB) {
		this.idRendicontazioneNonConformitaModB = idRendicontazioneNonConformitaModB;
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

	public Timestamp getDataFineValidita() {
		return this.dataFineValidita;
	}

	public void setDataFineValidita(Timestamp dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public Timestamp getDataInizioValidita() {
		return this.dataInizioValidita;
	}

	public void setDataInizioValidita(Timestamp dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public Timestamp getDataModifica() {
		return this.dataModifica;
	}

	public void setDataModifica(Timestamp dataModifica) {
		this.dataModifica = dataModifica;
	}

	public String getMotivazione() {
		return this.motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDProgrammaMissione getGregDProgrammaMissione() {
		return this.gregDProgrammaMissione;
	}

	public void setGregDProgrammaMissione(GregDProgrammaMissione gregDProgrammaMissione) {
		this.gregDProgrammaMissione = gregDProgrammaMissione;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}