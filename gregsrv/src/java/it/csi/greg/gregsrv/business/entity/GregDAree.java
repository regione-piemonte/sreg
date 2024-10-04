/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_d_aree database table.
 * 
 */
@Entity
@Table(name="greg_d_aree")
@NamedQuery(name="GregDAree.findAll", query="SELECT g FROM GregDAree g where g.dataCancellazione IS NULL")
public class GregDAree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_AREE_IDAREA_GENERATOR", sequenceName="GREG_D_AREE_ID_AREA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_AREE_IDAREA_GENERATOR")
	@Column(name="id_area")
	private Integer idArea;

	@Column(name="cod_area")
	private String codArea;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_area")
	private String descArea;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDTargetUtenza
	@JsonIgnore
	@OneToMany(mappedBy="gregDAree")
	private Set<GregDTargetUtenza> gregDTargetUtenzas;

	//bi-directional many-to-one association to GregRTargetUtenzaAree
	@JsonIgnore
	@OneToMany(mappedBy="gregDAree")
	private Set<GregRTargetUtenzaAree> gregRTargetUtenzaArees;

	public GregDAree() {
	}

	public Integer getIdArea() {
		return this.idArea;
	}

	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}

	public String getCodArea() {
		return this.codArea;
	}

	public void setCodArea(String codArea) {
		this.codArea = codArea;
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

	public String getDescArea() {
		return this.descArea;
	}

	public void setDescArea(String descArea) {
		this.descArea = descArea;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDTargetUtenza> getGregDTargetUtenzas() {
		return this.gregDTargetUtenzas;
	}

	public void setGregDTargetUtenzas(Set<GregDTargetUtenza> gregDTargetUtenzas) {
		this.gregDTargetUtenzas = gregDTargetUtenzas;
	}

	public GregDTargetUtenza addGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		getGregDTargetUtenzas().add(gregDTargetUtenza);
		gregDTargetUtenza.setGregDAree(this);

		return gregDTargetUtenza;
	}

	public GregDTargetUtenza removeGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		getGregDTargetUtenzas().remove(gregDTargetUtenza);
		gregDTargetUtenza.setGregDAree(null);

		return gregDTargetUtenza;
	}

	public Set<GregRTargetUtenzaAree> getGregRTargetUtenzaArees() {
		return this.gregRTargetUtenzaArees;
	}

	public void setGregRTargetUtenzaArees(Set<GregRTargetUtenzaAree> gregRTargetUtenzaArees) {
		this.gregRTargetUtenzaArees = gregRTargetUtenzaArees;
	}

	public GregRTargetUtenzaAree addGregRTargetUtenzaAree(GregRTargetUtenzaAree gregRTargetUtenzaAree) {
		getGregRTargetUtenzaArees().add(gregRTargetUtenzaAree);
		gregRTargetUtenzaAree.setGregDAree(this);

		return gregRTargetUtenzaAree;
	}

	public GregRTargetUtenzaAree removeGregRTargetUtenzaAree(GregRTargetUtenzaAree gregRTargetUtenzaAree) {
		getGregRTargetUtenzaArees().remove(gregRTargetUtenzaAree);
		gregRTargetUtenzaAree.setGregDAree(null);

		return gregRTargetUtenzaAree;
	}

}