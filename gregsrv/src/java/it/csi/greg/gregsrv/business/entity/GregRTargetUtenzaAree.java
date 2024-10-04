/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_target_utenza_aree database table.
 * 
 */
@Entity
@Table(name="greg_r_target_utenza_aree")
@NamedQuery(name="GregRTargetUtenzaAree.findAll", query="SELECT g FROM GregRTargetUtenzaAree g where g.dataCancellazione IS NULL")
public class GregRTargetUtenzaAree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_TARGET_UTENZA_AREE_IDTARGETUTENZAAREA_GENERATOR", sequenceName="GREG_R_TARGET_UTENZA_AREE_ID_TARGET_UTENZA_AREA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_TARGET_UTENZA_AREE_IDTARGETUTENZAAREA_GENERATOR")
	@Column(name="id_target_utenza_area")
	private Integer idTargetUtenzaArea;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDAree
	@ManyToOne
	@JoinColumn(name="id_area")
	private GregDAree gregDAree;

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza")
	private GregDTargetUtenza gregDTargetUtenza;

	public GregRTargetUtenzaAree() {
	}

	public Integer getIdTargetUtenzaArea() {
		return this.idTargetUtenzaArea;
	}

	public void setIdTargetUtenzaArea(Integer idTargetUtenzaArea) {
		this.idTargetUtenzaArea = idTargetUtenzaArea;
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

	public GregDAree getGregDAree() {
		return this.gregDAree;
	}

	public void setGregDAree(GregDAree gregDAree) {
		this.gregDAree = gregDAree;
	}

	public GregDTargetUtenza getGregDTargetUtenza() {
		return this.gregDTargetUtenza;
	}

	public void setGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		this.gregDTargetUtenza = gregDTargetUtenza;
	}

}