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
 * The persistent class for the greg_d_tipo_struttura database table.
 * 
 */
@Entity
@Table(name="greg_d_tipo_struttura")
@NamedQueries({
	@NamedQuery(name="GregDTipoStruttura.findAll", query="SELECT g FROM GregDTipoStruttura g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDTipoStruttura.findByCod", query="SELECT g FROM GregDTipoStruttura g where g.dataCancellazione IS NULL and g.codTipoStruttura = :codStruttura")
})

public class GregDTipoStruttura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPO_STRUTTURA_IDTIPOSTRUTTURA_GENERATOR", sequenceName="GREG_D_TIPO_STRUTTURA_ID_TIPO_STRUTTURA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPO_STRUTTURA_IDTIPOSTRUTTURA_GENERATOR")
	@Column(name="id_tipo_struttura")
	private Integer idTipoStruttura;

	@Column(name="cod_tipo_struttura")
	private String codTipoStruttura;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_tipo_struttura")
	private String descTipoStruttura;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRPrestazioniRegionali1TipoStruttura
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipoStruttura")
	private Set<GregRPrestazioniRegionali1TipoStruttura> gregRPrestazioniRegionali1TipoStrutturas;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipoStruttura")
	private Set<GregTPrestazioniRegionali1> gregTPrestazioniRegionali1s;

	public GregDTipoStruttura() {
	}

	public Integer getIdTipoStruttura() {
		return this.idTipoStruttura;
	}

	public void setIdTipoStruttura(Integer idTipoStruttura) {
		this.idTipoStruttura = idTipoStruttura;
	}

	public String getCodTipoStruttura() {
		return this.codTipoStruttura;
	}

	public void setCodTipoStruttura(String codTipoStruttura) {
		this.codTipoStruttura = codTipoStruttura;
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

	public String getDescTipoStruttura() {
		return this.descTipoStruttura;
	}

	public void setDescTipoStruttura(String descTipoStruttura) {
		this.descTipoStruttura = descTipoStruttura;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRPrestazioniRegionali1TipoStruttura> getGregRPrestazioniRegionali1TipoStrutturas() {
		return this.gregRPrestazioniRegionali1TipoStrutturas;
	}

	public void setGregRPrestazioniRegionali1TipoStrutturas(Set<GregRPrestazioniRegionali1TipoStruttura> gregRPrestazioniRegionali1TipoStrutturas) {
		this.gregRPrestazioniRegionali1TipoStrutturas = gregRPrestazioniRegionali1TipoStrutturas;
	}

	public GregRPrestazioniRegionali1TipoStruttura addGregRPrestazioniRegionali1TipoStruttura(GregRPrestazioniRegionali1TipoStruttura gregRPrestazioniRegionali1TipoStruttura) {
		getGregRPrestazioniRegionali1TipoStrutturas().add(gregRPrestazioniRegionali1TipoStruttura);
		gregRPrestazioniRegionali1TipoStruttura.setGregDTipoStruttura(this);

		return gregRPrestazioniRegionali1TipoStruttura;
	}

	public GregRPrestazioniRegionali1TipoStruttura removeGregRPrestazioniRegionali1TipoStruttura(GregRPrestazioniRegionali1TipoStruttura gregRPrestazioniRegionali1TipoStruttura) {
		getGregRPrestazioniRegionali1TipoStrutturas().remove(gregRPrestazioniRegionali1TipoStruttura);
		gregRPrestazioniRegionali1TipoStruttura.setGregDTipoStruttura(null);

		return gregRPrestazioniRegionali1TipoStruttura;
	}

	public Set<GregTPrestazioniRegionali1> getGregTPrestazioniRegionali1s() {
		return this.gregTPrestazioniRegionali1s;
	}

	public void setGregTPrestazioniRegionali1s(Set<GregTPrestazioniRegionali1> gregTPrestazioniRegionali1s) {
		this.gregTPrestazioniRegionali1s = gregTPrestazioniRegionali1s;
	}

	public GregTPrestazioniRegionali1 addGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		getGregTPrestazioniRegionali1s().add(gregTPrestazioniRegionali1);
		gregTPrestazioniRegionali1.setGregDTipoStruttura(this);

		return gregTPrestazioniRegionali1;
	}

	public GregTPrestazioniRegionali1 removeGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		getGregTPrestazioniRegionali1s().remove(gregTPrestazioniRegionali1);
		gregTPrestazioniRegionali1.setGregDTipoStruttura(null);

		return gregTPrestazioniRegionali1;
	}

}