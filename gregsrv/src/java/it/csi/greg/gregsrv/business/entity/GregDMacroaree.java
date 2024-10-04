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
 * The persistent class for the greg_d_macroaree database table.
 * 
 */
@Entity
@Table(name="greg_d_macroaree")
@NamedQuery(name="GregDMacroaree.findAll", query="SELECT g FROM GregDMacroaree g where g.dataCancellazione IS NULL")
public class GregDMacroaree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_MACROAREE_IDMACROAREA_GENERATOR", sequenceName="GREG_D_MACROAREE_ID_MACROAREA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_MACROAREE_IDMACROAREA_GENERATOR")
	@Column(name="id_macroarea")
	private Integer idMacroarea;

	@Column(name="cod_macroarea")
	private String codMacroarea;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_macroarea")
	private String desMacroarea;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRMacroareeSottoaree
	@JsonIgnore
	@OneToMany(mappedBy="gregDMacroaree")
	private Set<GregRMacroareeSottoaree> gregRMacroareeSottoarees;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDMacroaree")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDMacroaree() {
	}

	public Integer getIdMacroarea() {
		return this.idMacroarea;
	}

	public void setIdMacroarea(Integer idMacroarea) {
		this.idMacroarea = idMacroarea;
	}

	public String getCodMacroarea() {
		return this.codMacroarea;
	}

	public void setCodMacroarea(String codMacroarea) {
		this.codMacroarea = codMacroarea;
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

	public String getDesMacroarea() {
		return this.desMacroarea;
	}

	public void setDesMacroarea(String desMacroarea) {
		this.desMacroarea = desMacroarea;
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
		gregRMacroareeSottoaree.setGregDMacroaree(this);

		return gregRMacroareeSottoaree;
	}

	public GregRMacroareeSottoaree removeGregRMacroareeSottoaree(GregRMacroareeSottoaree gregRMacroareeSottoaree) {
		getGregRMacroareeSottoarees().remove(gregRMacroareeSottoaree);
		gregRMacroareeSottoaree.setGregDMacroaree(null);

		return gregRMacroareeSottoaree;
	}

	public Set<GregTNomenclatoreNazionale> getGregTNomenclatoreNazionales() {
		return this.gregTNomenclatoreNazionales;
	}

	public void setGregTNomenclatoreNazionales(Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales) {
		this.gregTNomenclatoreNazionales = gregTNomenclatoreNazionales;
	}

	public GregTNomenclatoreNazionale addGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().add(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDMacroaree(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDMacroaree(null);

		return gregTNomenclatoreNazionale;
	}

}