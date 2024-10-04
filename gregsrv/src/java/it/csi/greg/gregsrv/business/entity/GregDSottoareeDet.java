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
 * The persistent class for the greg_d_sottoaree_det database table.
 * 
 */
@Entity
@Table(name="greg_d_sottoaree_det")
@NamedQuery(name="GregDSottoareeDet.findAll", query="SELECT g FROM GregDSottoareeDet g where g.dataCancellazione IS NULL")
public class GregDSottoareeDet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_SOTTOAREE_DET_IDSOTTOAREADET_GENERATOR", sequenceName="GREG_D_SOTTOAREE_DET_ID_SOTTOAREA_DET_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_SOTTOAREE_DET_IDSOTTOAREADET_GENERATOR")
	@Column(name="id_sottoarea_det")
	private Integer idSottoareaDet;

	@Column(name="cod_sottoarea_det")
	private String codSottoareaDet;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_sottoarea_det")
	private String desSottoareaDet;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRSottoareeSottoareeDet
	@JsonIgnore
	@OneToMany(mappedBy="gregDSottoareeDet")
	private Set<GregRSottoareeSottoareeDet> gregRSottoareeSottoareeDets;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDSottoareeDet")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDSottoareeDet() {
	}

	public Integer getIdSottoareaDet() {
		return this.idSottoareaDet;
	}

	public void setIdSottoareaDet(Integer idSottoareaDet) {
		this.idSottoareaDet = idSottoareaDet;
	}

	public String getCodSottoareaDet() {
		return this.codSottoareaDet;
	}

	public void setCodSottoareaDet(String codSottoareaDet) {
		this.codSottoareaDet = codSottoareaDet;
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

	public String getDesSottoareaDet() {
		return this.desSottoareaDet;
	}

	public void setDesSottoareaDet(String desSottoareaDet) {
		this.desSottoareaDet = desSottoareaDet;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRSottoareeSottoareeDet> getGregRSottoareeSottoareeDets() {
		return this.gregRSottoareeSottoareeDets;
	}

	public void setGregRSottoareeSottoareeDets(Set<GregRSottoareeSottoareeDet> gregRSottoareeSottoareeDets) {
		this.gregRSottoareeSottoareeDets = gregRSottoareeSottoareeDets;
	}

	public GregRSottoareeSottoareeDet addGregRSottoareeSottoareeDet(GregRSottoareeSottoareeDet gregRSottoareeSottoareeDet) {
		getGregRSottoareeSottoareeDets().add(gregRSottoareeSottoareeDet);
		gregRSottoareeSottoareeDet.setGregDSottoareeDet(this);

		return gregRSottoareeSottoareeDet;
	}

	public GregRSottoareeSottoareeDet removeGregRSottoareeSottoareeDet(GregRSottoareeSottoareeDet gregRSottoareeSottoareeDet) {
		getGregRSottoareeSottoareeDets().remove(gregRSottoareeSottoareeDet);
		gregRSottoareeSottoareeDet.setGregDSottoareeDet(null);

		return gregRSottoareeSottoareeDet;
	}

	public Set<GregTNomenclatoreNazionale> getGregTNomenclatoreNazionales() {
		return this.gregTNomenclatoreNazionales;
	}

	public void setGregTNomenclatoreNazionales(Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales) {
		this.gregTNomenclatoreNazionales = gregTNomenclatoreNazionales;
	}

	public GregTNomenclatoreNazionale addGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().add(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDSottoareeDet(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDSottoareeDet(null);

		return gregTNomenclatoreNazionale;
	}

}