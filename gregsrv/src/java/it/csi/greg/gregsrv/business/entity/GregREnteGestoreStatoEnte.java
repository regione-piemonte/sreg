/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_ente_gestore_stato_ente database table.
 * 
 */
@Entity
@Table(name="greg_r_ente_gestore_stato_ente")
@NamedQueries({
	@NamedQuery(name="GregREnteGestoreStatoEnte.findAll", query="SELECT g FROM GregREnteGestoreStatoEnte g"),
	@NamedQuery(name="GregREnteGestoreStatoEnte.findAllStatiById", query="SELECT g FROM GregREnteGestoreStatoEnte g WHERE g.gregTSchedeEntiGestori.idSchedaEnteGestore = :idScheda and g.dataCancellazione is null order by g.dataCreazione"),
	@NamedQuery(name="GregREnteGestoreStatoEnte.findLastStato", query="SELECT g FROM GregREnteGestoreStatoEnte g where g.gregTSchedeEntiGestori.idSchedaEnteGestore = :idScheda and g.dataCancellazione is null and g.dataFineValidita is null"),
	@NamedQuery(name="GregREnteGestoreStatoEnte.findLastStatoAperto", query="SELECT g FROM GregREnteGestoreStatoEnte g where g.gregTSchedeEntiGestori.idSchedaEnteGestore = :idScheda and g.dataCancellazione is null and "
			+ "g.idEnteGestoreStatoEnte = (Select MAX(g.idEnteGestoreStatoEnte) from GregREnteGestoreStatoEnte g "
			+ "where g.gregTSchedeEntiGestori.idSchedaEnteGestore = :idScheda "
			+ "and g.dataCancellazione is null and g.gregDStatoEnte.codStatoEnte = 'APE')"),
})

public class GregREnteGestoreStatoEnte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_ENTE_GESTORE_STATO_ENTE_IDENTEGESTORESTATOENTE_GENERATOR", sequenceName="GREG_R_ENTE_GESTORE_STATO_ENTE_ID_ENTE_GESTORE_STATO_ENTE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_ENTE_GESTORE_STATO_ENTE_IDENTEGESTORESTATOENTE_GENERATOR")
	@Column(name="id_ente_gestore_stato_ente")
	private Integer idEnteGestoreStatoEnte;

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
	
	@Column(name="nota_per_ente")
	private String notaPerEnte;
	
	@Column(name="nota_interna")
	private String notaInterna;

	//bi-directional many-to-one association to GregDMotivazione
	@ManyToOne
	@JoinColumn(name="id_motivazione")
	private GregDMotivazione gregDMotivazione;

	//bi-directional many-to-one association to GregDStatoEnte
	@ManyToOne
	@JoinColumn(name="id_stato_ente")
	private GregDStatoEnte gregDStatoEnte;

	//bi-directional many-to-one association to GregTSchedeEntiGestori
	@ManyToOne
	@JoinColumn(name="id_scheda_ente_gestore")
	private GregTSchedeEntiGestori gregTSchedeEntiGestori;

	public GregREnteGestoreStatoEnte() {
	}

	public Integer getIdEnteGestoreStatoEnte() {
		return this.idEnteGestoreStatoEnte;
	}

	public void setIdEnteGestoreStatoEnte(Integer idEnteGestoreStatoEnte) {
		this.idEnteGestoreStatoEnte = idEnteGestoreStatoEnte;
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

	public GregDMotivazione getGregDMotivazione() {
		return this.gregDMotivazione;
	}

	public void setGregDMotivazione(GregDMotivazione gregDMotivazione) {
		this.gregDMotivazione = gregDMotivazione;
	}

	public GregDStatoEnte getGregDStatoEnte() {
		return this.gregDStatoEnte;
	}

	public void setGregDStatoEnte(GregDStatoEnte gregDStatoEnte) {
		this.gregDStatoEnte = gregDStatoEnte;
	}

	public GregTSchedeEntiGestori getGregTSchedeEntiGestori() {
		return this.gregTSchedeEntiGestori;
	}

	public void setGregTSchedeEntiGestori(GregTSchedeEntiGestori gregTSchedeEntiGestori) {
		this.gregTSchedeEntiGestori = gregTSchedeEntiGestori;
	}

	public String getNotaPerEnte() {
		return notaPerEnte;
	}

	public void setNotaPerEnte(String notaPerEnte) {
		this.notaPerEnte = notaPerEnte;
	}

	public String getNotaInterna() {
		return notaInterna;
	}

	public void setNotaInterna(String notaInterna) {
		this.notaInterna = notaInterna;
	}

	
}