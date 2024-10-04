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
 * The persistent class for the greg_d_sottovoci database table.
 * 
 */
@Entity
@Table(name="greg_d_sottovoci")
@NamedQuery(name="GregDSottovoci.findAll", query="SELECT g FROM GregDSottovoci g where g.dataCancellazione IS NULL")
public class GregDSottovoci implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_SOTTOVOCI_IDSOTTOVOCE_GENERATOR", sequenceName="GREG_D_SOTTOVOCI_ID_SOTTOVOCE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_SOTTOVOCI_IDSOTTOVOCE_GENERATOR")
	@Column(name="id_sottovoce")
	private Integer idSottovoce;

	@Column(name="cod_sottovoce")
	private String codSottovoce;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_sottovoce")
	private String desSottovoce;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRVociSottovoci
	@JsonIgnore
	@OneToMany(mappedBy="gregDSottovoci")
	private Set<GregRVociSottovoci> gregRVociSottovocis;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDSottovoci")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDSottovoci() {
	}

	public Integer getIdSottovoce() {
		return this.idSottovoce;
	}

	public void setIdSottovoce(Integer idSottovoce) {
		this.idSottovoce = idSottovoce;
	}

	public String getCodSottovoce() {
		return this.codSottovoce;
	}

	public void setCodSottovoce(String codSottovoce) {
		this.codSottovoce = codSottovoce;
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

	public String getDesSottovoce() {
		return this.desSottovoce;
	}

	public void setDesSottovoce(String desSottovoce) {
		this.desSottovoce = desSottovoce;
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
		gregRVociSottovoci.setGregDSottovoci(this);

		return gregRVociSottovoci;
	}

	public GregRVociSottovoci removeGregRVociSottovoci(GregRVociSottovoci gregRVociSottovoci) {
		getGregRVociSottovocis().remove(gregRVociSottovoci);
		gregRVociSottovoci.setGregDSottovoci(null);

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
		gregTNomenclatoreNazionale.setGregDSottovoci(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDSottovoci(null);

		return gregTNomenclatoreNazionale;
	}

}