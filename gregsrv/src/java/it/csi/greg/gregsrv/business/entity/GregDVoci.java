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
 * The persistent class for the greg_d_voci database table.
 * 
 */
@Entity
@Table(name="greg_d_voci")
@NamedQuery(name="GregDVoci.findAll", query="SELECT g FROM GregDVoci g where g.dataCancellazione IS NULL")
public class GregDVoci implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_VOCI_IDVOCE_GENERATOR", sequenceName="GREG_D_VOCI_ID_VOCE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_VOCI_IDVOCE_GENERATOR")
	@Column(name="id_voce")
	private Integer idVoce;

	@Column(name="cod_voce")
	private String codVoce;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_voce")
	private String desVoce;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRVociSottovoci
	@JsonIgnore
	@OneToMany(mappedBy="gregDVoci")
	private Set<GregRVociSottovoci> gregRVociSottovocis;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDVoci")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDVoci() {
	}

	public Integer getIdVoce() {
		return this.idVoce;
	}

	public void setIdVoce(Integer idVoce) {
		this.idVoce = idVoce;
	}

	public String getCodVoce() {
		return this.codVoce;
	}

	public void setCodVoce(String codVoce) {
		this.codVoce = codVoce;
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

	public String getDesVoce() {
		return this.desVoce;
	}

	public void setDesVoce(String desVoce) {
		this.desVoce = desVoce;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRVociSottovoci> getGregRVociSottovocis() {
		return this.gregRVociSottovocis;
	}

	public void setGregRVociSottovocis(Set<GregRVociSottovoci> gregRVociSottovocis) {
		this.gregRVociSottovocis = gregRVociSottovocis;
	}

	public GregRVociSottovoci addGregRVociSottovoci(GregRVociSottovoci gregRVociSottovoci) {
		getGregRVociSottovocis().add(gregRVociSottovoci);
		gregRVociSottovoci.setGregDVoci(this);

		return gregRVociSottovoci;
	}

	public GregRVociSottovoci removeGregRVociSottovoci(GregRVociSottovoci gregRVociSottovoci) {
		getGregRVociSottovocis().remove(gregRVociSottovoci);
		gregRVociSottovoci.setGregDVoci(null);

		return gregRVociSottovoci;
	}

	public Set<GregTNomenclatoreNazionale> getGregTNomenclatoreNazionales() {
		return this.gregTNomenclatoreNazionales;
	}

	public void setGregTNomenclatoreNazionales(Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales) {
		this.gregTNomenclatoreNazionales = gregTNomenclatoreNazionales;
	}

	public GregTNomenclatoreNazionale addGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().add(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDVoci(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDVoci(null);

		return gregTNomenclatoreNazionale;
	}

}