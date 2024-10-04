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
 * The persistent class for the greg_d_sottoaree database table.
 * 
 */
@Entity
@Table(name="greg_d_sottoaree")
@NamedQuery(name="GregDSottoaree.findAll", query="SELECT g FROM GregDSottoaree g where g.dataCancellazione IS NULL")
public class GregDSottoaree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_SOTTOAREE_IDSOTTOAREA_GENERATOR", sequenceName="GREG_D_SOTTOAREE_ID_SOTTOAREA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_SOTTOAREE_IDSOTTOAREA_GENERATOR")
	@Column(name="id_sottoarea")
	private Integer idSottoarea;

	@Column(name="cod_sottoarea")
	private String codSottoarea;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_sottoarea")
	private String desSottoarea;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRMacroareeSottoaree
	@JsonIgnore
	@OneToMany(mappedBy="gregDSottoaree")
	private Set<GregRMacroareeSottoaree> gregRMacroareeSottoarees;

	//bi-directional many-to-one association to GregRSottoareeSottoareeDet
	@JsonIgnore
	@OneToMany(mappedBy="gregDSottoaree")
	private Set<GregRSottoareeSottoareeDet> gregRSottoareeSottoareeDets;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDSottoaree")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDSottoaree() {
	}

	public Integer getIdSottoarea() {
		return this.idSottoarea;
	}

	public void setIdSottoarea(Integer idSottoarea) {
		this.idSottoarea = idSottoarea;
	}

	public String getCodSottoarea() {
		return this.codSottoarea;
	}

	public void setCodSottoarea(String codSottoarea) {
		this.codSottoarea = codSottoarea;
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

	public String getDesSottoarea() {
		return this.desSottoarea;
	}

	public void setDesSottoarea(String desSottoarea) {
		this.desSottoarea = desSottoarea;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRMacroareeSottoaree> getGregRMacroareeSottoarees() {
		return this.gregRMacroareeSottoarees;
	}

	public void setGregRMacroareeSottoarees(Set<GregRMacroareeSottoaree> gregRMacroareeSottoarees) {
		this.gregRMacroareeSottoarees = gregRMacroareeSottoarees;
	}

	public GregRMacroareeSottoaree addGregRMacroareeSottoaree(GregRMacroareeSottoaree gregRMacroareeSottoaree) {
		getGregRMacroareeSottoarees().add(gregRMacroareeSottoaree);
		gregRMacroareeSottoaree.setGregDSottoaree(this);

		return gregRMacroareeSottoaree;
	}

	public GregRMacroareeSottoaree removeGregRMacroareeSottoaree(GregRMacroareeSottoaree gregRMacroareeSottoaree) {
		getGregRMacroareeSottoarees().remove(gregRMacroareeSottoaree);
		gregRMacroareeSottoaree.setGregDSottoaree(null);

		return gregRMacroareeSottoaree;
	}

	public Set<GregRSottoareeSottoareeDet> getGregRSottoareeSottoareeDets() {
		return this.gregRSottoareeSottoareeDets;
	}

	public void setGregRSottoareeSottoareeDets(Set<GregRSottoareeSottoareeDet> gregRSottoareeSottoareeDets) {
		this.gregRSottoareeSottoareeDets = gregRSottoareeSottoareeDets;
	}

	public GregRSottoareeSottoareeDet addGregRSottoareeSottoareeDet(GregRSottoareeSottoareeDet gregRSottoareeSottoareeDet) {
		getGregRSottoareeSottoareeDets().add(gregRSottoareeSottoareeDet);
		gregRSottoareeSottoareeDet.setGregDSottoaree(this);

		return gregRSottoareeSottoareeDet;
	}

	public GregRSottoareeSottoareeDet removeGregRSottoareeSottoareeDet(GregRSottoareeSottoareeDet gregRSottoareeSottoareeDet) {
		getGregRSottoareeSottoareeDets().remove(gregRSottoareeSottoareeDet);
		gregRSottoareeSottoareeDet.setGregDSottoaree(null);

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
		gregTNomenclatoreNazionale.setGregDSottoaree(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDSottoaree(null);

		return gregTNomenclatoreNazionale;
	}

}