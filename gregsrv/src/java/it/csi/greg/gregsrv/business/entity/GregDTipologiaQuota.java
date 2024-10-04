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
 * The persistent class for the greg_d_tipologia_quota database table.
 * 
 */
@Entity
@Table(name="greg_d_tipologia_quota")
@NamedQueries({
	@NamedQuery(name="GregDTipologiaQuota.findAll", query="SELECT g FROM GregDTipologiaQuota g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDTipologiaQuota.findByCod", query="SELECT g FROM GregDTipologiaQuota g where g.dataCancellazione IS NULL and g.codTipologiaQuota = :codTipologia")
})

public class GregDTipologiaQuota implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPOLOGIA_QUOTA_IDTIPOLOGIAQUOTA_GENERATOR", sequenceName="GREG_D_TIPOLOGIA_QUOTA_ID_TIPOLOGIA_QUOTA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPOLOGIA_QUOTA_IDTIPOLOGIAQUOTA_GENERATOR")
	@Column(name="id_tipologia_quota")
	private Integer idTipologiaQuota;

	@Column(name="cod_tipologia_quota")
	private String codTipologiaQuota;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_tipologia_quota")
	private String descTipologiaQuota;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRTitoloTipologiaVoceModA
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipologiaQuota")
	private Set<GregRTitoloTipologiaVoceModA> gregRTitoloTipologiaVoceModAs;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipologiaQuota")
	private Set<GregTPrestazioniRegionali1> gregTPrestazioniRegionali1s;

	public GregDTipologiaQuota() {
	}

	public Integer getIdTipologiaQuota() {
		return this.idTipologiaQuota;
	}

	public void setIdTipologiaQuota(Integer idTipologiaQuota) {
		this.idTipologiaQuota = idTipologiaQuota;
	}

	public String getCodTipologiaQuota() {
		return this.codTipologiaQuota;
	}

	public void setCodTipologiaQuota(String codTipologiaQuota) {
		this.codTipologiaQuota = codTipologiaQuota;
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

	public String getDescTipologiaQuota() {
		return this.descTipologiaQuota;
	}

	public void setDescTipologiaQuota(String descTipologiaQuota) {
		this.descTipologiaQuota = descTipologiaQuota;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRTitoloTipologiaVoceModA> getGregRTitoloTipologiaVoceModAs() {
		return this.gregRTitoloTipologiaVoceModAs;
	}

	public void setGregRTitoloTipologiaVoceModAs(Set<GregRTitoloTipologiaVoceModA> gregRTitoloTipologiaVoceModAs) {
		this.gregRTitoloTipologiaVoceModAs = gregRTitoloTipologiaVoceModAs;
	}

	public GregRTitoloTipologiaVoceModA addGregRTitoloTipologiaVoceModA(GregRTitoloTipologiaVoceModA gregRTitoloTipologiaVoceModA) {
		getGregRTitoloTipologiaVoceModAs().add(gregRTitoloTipologiaVoceModA);
		gregRTitoloTipologiaVoceModA.setGregDTipologiaQuota(this);

		return gregRTitoloTipologiaVoceModA;
	}

	public GregRTitoloTipologiaVoceModA removeGregRTitoloTipologiaVoceModA(GregRTitoloTipologiaVoceModA gregRTitoloTipologiaVoceModA) {
		getGregRTitoloTipologiaVoceModAs().remove(gregRTitoloTipologiaVoceModA);
		gregRTitoloTipologiaVoceModA.setGregDTipologiaQuota(null);

		return gregRTitoloTipologiaVoceModA;
	}

	public Set<GregTPrestazioniRegionali1> getGregTPrestazioniRegionali1s() {
		return this.gregTPrestazioniRegionali1s;
	}

	public void setGregTPrestazioniRegionali1s(Set<GregTPrestazioniRegionali1> gregTPrestazioniRegionali1s) {
		this.gregTPrestazioniRegionali1s = gregTPrestazioniRegionali1s;
	}

	public GregTPrestazioniRegionali1 addGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		getGregTPrestazioniRegionali1s().add(gregTPrestazioniRegionali1);
		gregTPrestazioniRegionali1.setGregDTipologiaQuota(this);

		return gregTPrestazioniRegionali1;
	}

	public GregTPrestazioniRegionali1 removeGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		getGregTPrestazioniRegionali1s().remove(gregTPrestazioniRegionali1);
		gregTPrestazioniRegionali1.setGregDTipologiaQuota(null);

		return gregTPrestazioniRegionali1;
	}

}