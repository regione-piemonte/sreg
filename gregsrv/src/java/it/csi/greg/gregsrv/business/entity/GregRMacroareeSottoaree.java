/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_macroaree_sottoaree database table.
 * 
 */
@Entity
@Table(name="greg_r_macroaree_sottoaree")
@NamedQuery(name="GregRMacroareeSottoaree.findAll", query="SELECT g FROM GregRMacroareeSottoaree g where g.dataCancellazione IS NULL")
public class GregRMacroareeSottoaree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_MACROAREE_SOTTOAREE_IDRMACROAREESOTTOAREE_GENERATOR", sequenceName="GREG_R_MACROAREE_SOTTOAREE_ID_R_MACROAREE_SOTTOAREE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_MACROAREE_SOTTOAREE_IDRMACROAREESOTTOAREE_GENERATOR")
	@Column(name="id_r_macroaree_sottoaree")
	private Integer idRMacroareeSottoaree;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDMacroaree
	@ManyToOne
	@JoinColumn(name="id_macroarea")
	private GregDMacroaree gregDMacroaree;

	//bi-directional many-to-one association to GregDSottoaree
	@ManyToOne
	@JoinColumn(name="id_sottoarea")
	private GregDSottoaree gregDSottoaree;

	public GregRMacroareeSottoaree() {
	}

	public Integer getIdRMacroareeSottoaree() {
		return this.idRMacroareeSottoaree;
	}

	public void setIdRMacroareeSottoaree(Integer idRMacroareeSottoaree) {
		this.idRMacroareeSottoaree = idRMacroareeSottoaree;
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

	public GregDMacroaree getGregDMacroaree() {
		return this.gregDMacroaree;
	}

	public void setGregDMacroaree(GregDMacroaree gregDMacroaree) {
		this.gregDMacroaree = gregDMacroaree;
	}

	public GregDSottoaree getGregDSottoaree() {
		return this.gregDSottoaree;
	}

	public void setGregDSottoaree(GregDSottoaree gregDSottoaree) {
		this.gregDSottoaree = gregDSottoaree;
	}

}