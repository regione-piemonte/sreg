/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_merge_enti database table.
 * 
 */
@Entity
@Table(name="greg_r_merge_enti")
@NamedQuery(name="GregRMergeEnti.findAll", query="SELECT g FROM GregRMergeEnti g where g.dataCancellazione IS NULL")
public class GregRMergeEnti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_MERGE_ENTI_IDMERGEENTI_GENERATOR", sequenceName="GREG_R_MERGE_ENTI_ID_MERGE_ENTI_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_MERGE_ENTI_IDMERGEENTI_GENERATOR")
	@Column(name="id_merge_enti")
	private Integer idMergeEnti;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;
	
	@Column(name="data_merge")
	private Timestamp dataMerge;

	//bi-directional many-to-one association to GregTSchedeEntiGestori
	@ManyToOne
	@JoinColumn(name="id_ente_destinazione")
	private GregTSchedeEntiGestori gregTSchedeEntiGestori1;

	//bi-directional many-to-one association to GregTSchedeEntiGestori
	@ManyToOne
	@JoinColumn(name="id_ente_da_unire")
	private GregTSchedeEntiGestori gregTSchedeEntiGestori2;

	public GregRMergeEnti() {
	}

	public Integer getIdMergeEnti() {
		return this.idMergeEnti;
	}

	public void setIdMergeEnti(Integer idMergeEnti) {
		this.idMergeEnti = idMergeEnti;
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

	public Timestamp getDataMerge() {
		return this.dataMerge;
	}

	public void setDataMerge(Timestamp dataMerge) {
		this.dataMerge = dataMerge;
	}
	
	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregTSchedeEntiGestori getGregTSchedeEntiGestori1() {
		return this.gregTSchedeEntiGestori1;
	}

	public void setGregTSchedeEntiGestori1(GregTSchedeEntiGestori gregTSchedeEntiGestori1) {
		this.gregTSchedeEntiGestori1 = gregTSchedeEntiGestori1;
	}

	public GregTSchedeEntiGestori getGregTSchedeEntiGestori2() {
		return this.gregTSchedeEntiGestori2;
	}

	public void setGregTSchedeEntiGestori2(GregTSchedeEntiGestori gregTSchedeEntiGestori2) {
		this.gregTSchedeEntiGestori2 = gregTSchedeEntiGestori2;
	}

}