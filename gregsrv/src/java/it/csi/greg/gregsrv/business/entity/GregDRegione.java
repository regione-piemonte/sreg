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
 * The persistent class for the greg_d_regione database table.
 * 
 */
@Entity
@Table(name="greg_d_regione")
@NamedQuery(name="GregDRegione.findAll", query="SELECT g FROM GregDRegione g")
public class GregDRegione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_REGIONE_IDREGIONE_GENERATOR", sequenceName="GREG_D_REGIONE_ID_REGIONE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_REGIONE_IDREGIONE_GENERATOR")
	@Column(name="id_regione")
	private Integer idRegione;

	@Column(name="cod_regione")
	private String codRegione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_regione")
	private String desRegione;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDAsl
	@JsonIgnore
	@OneToMany(mappedBy="gregDRegione")
	private Set<GregDAsl> gregDAsls;

	//bi-directional many-to-one association to GregDProvince
	@JsonIgnore
	@OneToMany(mappedBy="gregDRegione")
	private Set<GregDProvince> gregDProvinces;

	public GregDRegione() {
	}

	public Integer getIdRegione() {
		return this.idRegione;
	}

	public void setIdRegione(Integer idRegione) {
		this.idRegione = idRegione;
	}

	public String getCodRegione() {
		return this.codRegione;
	}

	public void setCodRegione(String codRegione) {
		this.codRegione = codRegione;
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

	public String getDesRegione() {
		return this.desRegione;
	}

	public void setDesRegione(String desRegione) {
		this.desRegione = desRegione;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDAsl> getGregDAsls() {
		return this.gregDAsls;
	}

	public void setGregDAsls(Set<GregDAsl> gregDAsls) {
		this.gregDAsls = gregDAsls;
	}

	public GregDAsl addGregDAsl(GregDAsl gregDAsl) {
		getGregDAsls().add(gregDAsl);
		gregDAsl.setGregDRegione(this);

		return gregDAsl;
	}

	public GregDAsl removeGregDAsl(GregDAsl gregDAsl) {
		getGregDAsls().remove(gregDAsl);
		gregDAsl.setGregDRegione(null);

		return gregDAsl;
	}

	public Set<GregDProvince> getGregDProvinces() {
		return this.gregDProvinces;
	}

	public void setGregDProvinces(Set<GregDProvince> gregDProvinces) {
		this.gregDProvinces = gregDProvinces;
	}

	public GregDProvince addGregDProvince(GregDProvince gregDProvince) {
		getGregDProvinces().add(gregDProvince);
		gregDProvince.setGregDRegione(this);

		return gregDProvince;
	}

	public GregDProvince removeGregDProvince(GregDProvince gregDProvince) {
		getGregDProvinces().remove(gregDProvince);
		gregDProvince.setGregDRegione(null);

		return gregDProvince;
	}

}