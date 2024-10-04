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
 * The persistent class for the greg_d_distretti database table.
 * 
 */
@Entity
@Table(name="greg_d_distretti")
@NamedQuery(name="GregDDistretti.findAll", query="SELECT g FROM GregDDistretti g where g.dataCancellazione IS NULL")
public class GregDDistretti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_DISTRETTI_IDDISTRETTO_GENERATOR", sequenceName="GREG_D_DISTRETTI_ID_DISTRETTO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_DISTRETTI_IDDISTRETTO_GENERATOR")
	@Column(name="id_distretto")
	private Integer idDistretto;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	private String distretto;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDComuni
	@JsonIgnore
	@OneToMany(mappedBy="gregDDistretti")
	private Set<GregDComuni> gregDComunis;

	//bi-directional many-to-one association to GregDAsl
	@ManyToOne
	@JoinColumn(name="id_asl")
	private GregDAsl gregDAsl;

	//bi-directional many-to-one association to GregRSchedeEntiGestoriDistretti
	@JsonIgnore
	@OneToMany(mappedBy="gregDDistretti")
	private Set<GregRSchedeEntiGestoriDistretti> gregRSchedeEntiGestoriDistrettis;

	public GregDDistretti() {
	}

	public Integer getIdDistretto() {
		return this.idDistretto;
	}

	public void setIdDistretto(Integer idDistretto) {
		this.idDistretto = idDistretto;
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

	public String getDistretto() {
		return this.distretto;
	}

	public void setDistretto(String distretto) {
		this.distretto = distretto;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDComuni> getGregDComunis() {
		return this.gregDComunis;
	}

	public void setGregDComunis(Set<GregDComuni> gregDComunis) {
		this.gregDComunis = gregDComunis;
	}

	public GregDComuni addGregDComuni(GregDComuni gregDComuni) {
		getGregDComunis().add(gregDComuni);
		gregDComuni.setGregDDistretti(this);

		return gregDComuni;
	}

	public GregDComuni removeGregDComuni(GregDComuni gregDComuni) {
		getGregDComunis().remove(gregDComuni);
		gregDComuni.setGregDDistretti(null);

		return gregDComuni;
	}

	public GregDAsl getGregDAsl() {
		return this.gregDAsl;
	}

	public void setGregDAsl(GregDAsl gregDAsl) {
		this.gregDAsl = gregDAsl;
	}

	public Set<GregRSchedeEntiGestoriDistretti> getGregRSchedeEntiGestoriDistrettis() {
		return this.gregRSchedeEntiGestoriDistrettis;
	}

	public void setGregRSchedeEntiGestoriDistrettis(Set<GregRSchedeEntiGestoriDistretti> gregRSchedeEntiGestoriDistrettis) {
		this.gregRSchedeEntiGestoriDistrettis = gregRSchedeEntiGestoriDistrettis;
	}

	public GregRSchedeEntiGestoriDistretti addGregRSchedeEntiGestoriDistretti(GregRSchedeEntiGestoriDistretti gregRSchedeEntiGestoriDistretti) {
		getGregRSchedeEntiGestoriDistrettis().add(gregRSchedeEntiGestoriDistretti);
		gregRSchedeEntiGestoriDistretti.setGregDDistretti(this);

		return gregRSchedeEntiGestoriDistretti;
	}

	public GregRSchedeEntiGestoriDistretti removeGregRSchedeEntiGestoriDistretti(GregRSchedeEntiGestoriDistretti gregRSchedeEntiGestoriDistretti) {
		getGregRSchedeEntiGestoriDistrettis().remove(gregRSchedeEntiGestoriDistretti);
		gregRSchedeEntiGestoriDistretti.setGregDDistretti(null);

		return gregRSchedeEntiGestoriDistretti;
	}

}