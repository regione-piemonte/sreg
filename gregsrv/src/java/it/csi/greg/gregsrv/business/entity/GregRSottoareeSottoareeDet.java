/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_sottoaree_sottoaree_det database table.
 * 
 */
@Entity
@Table(name="greg_r_sottoaree_sottoaree_det")
@NamedQuery(name="GregRSottoareeSottoareeDet.findAll", query="SELECT g FROM GregRSottoareeSottoareeDet g where g.dataCancellazione IS NULL")
public class GregRSottoareeSottoareeDet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_SOTTOAREE_SOTTOAREE_DET_IDRSOTTOAREESOTTOAREEDET_GENERATOR", sequenceName="GREG_R_SOTTOAREE_SOTTOAREE_DET_ID_R_SOTTOAREE_SOTTOAREE_DET_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_SOTTOAREE_SOTTOAREE_DET_IDRSOTTOAREESOTTOAREEDET_GENERATOR")
	@Column(name="id_r_sottoaree_sottoaree_det")
	private Integer idRSottoareeSottoareeDet;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDSottoaree
	@ManyToOne
	@JoinColumn(name="id_sottoarea")
	private GregDSottoaree gregDSottoaree;

	//bi-directional many-to-one association to GregDSottoareeDet
	@ManyToOne
	@JoinColumn(name="id_sottoarea_det")
	private GregDSottoareeDet gregDSottoareeDet;

	public GregRSottoareeSottoareeDet() {
	}

	public Integer getIdRSottoareeSottoareeDet() {
		return this.idRSottoareeSottoareeDet;
	}

	public void setIdRSottoareeSottoareeDet(Integer idRSottoareeSottoareeDet) {
		this.idRSottoareeSottoareeDet = idRSottoareeSottoareeDet;
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

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDSottoaree getGregDSottoaree() {
		return this.gregDSottoaree;
	}

	public void setGregDSottoaree(GregDSottoaree gregDSottoaree) {
		this.gregDSottoaree = gregDSottoaree;
	}

	public GregDSottoareeDet getGregDSottoareeDet() {
		return this.gregDSottoareeDet;
	}

	public void setGregDSottoareeDet(GregDSottoareeDet gregDSottoareeDet) {
		this.gregDSottoareeDet = gregDSottoareeDet;
	}

}