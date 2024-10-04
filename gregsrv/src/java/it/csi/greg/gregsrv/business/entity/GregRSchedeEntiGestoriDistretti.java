/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_schede_enti_gestori_distretti database table.
 * 
 */
@Entity
@Table(name="greg_r_schede_enti_gestori_distretti")
@NamedQuery(name="GregRSchedeEntiGestoriDistretti.findAll", query="SELECT g FROM GregRSchedeEntiGestoriDistretti g where g.dataCancellazione IS NULL")
public class GregRSchedeEntiGestoriDistretti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_SCHEDE_ENTI_GESTORI_DISTRETTI_IDSCHEDAENTEDISTRETTO_GENERATOR", sequenceName="GREG_R_SCHEDE_ENTI_GESTORI_DISTRET_ID_SCHEDA_ENTE_DISTRETTO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_SCHEDE_ENTI_GESTORI_DISTRETTI_IDSCHEDAENTEDISTRETTO_GENERATOR")
	@Column(name="id_scheda_ente_distretto")
	private Integer idSchedaEnteDistretto;

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

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDDistretti
	@ManyToOne
	@JoinColumn(name="id_distretto")
	private GregDDistretti gregDDistretti;

	//bi-directional many-to-one association to GregTSchedeEntiGestori
	@ManyToOne
	@JoinColumn(name="id_scheda_ente_gestore")
	private GregTSchedeEntiGestori gregTSchedeEntiGestori;

	public GregRSchedeEntiGestoriDistretti() {
	}

	public Integer getIdSchedaEnteDistretto() {
		return this.idSchedaEnteDistretto;
	}

	public void setIdSchedaEnteDistretto(Integer idSchedaEnteDistretto) {
		this.idSchedaEnteDistretto = idSchedaEnteDistretto;
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

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDDistretti getGregDDistretti() {
		return this.gregDDistretti;
	}

	public void setGregDDistretti(GregDDistretti gregDDistretti) {
		this.gregDDistretti = gregDDistretti;
	}

	public GregTSchedeEntiGestori getGregTSchedeEntiGestori() {
		return this.gregTSchedeEntiGestori;
	}

	public void setGregTSchedeEntiGestori(GregTSchedeEntiGestori gregTSchedeEntiGestori) {
		this.gregTSchedeEntiGestori = gregTSchedeEntiGestori;
	}

}