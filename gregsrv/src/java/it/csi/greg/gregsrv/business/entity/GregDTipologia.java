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
 * The persistent class for the greg_d_tipologia database table.
 * 
 */
@Entity
@Table(name="greg_d_tipologia")
@NamedQueries({
	@NamedQuery(name="GregDTipologia.findAll", query="SELECT g FROM GregDTipologia g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDTipologia.findByCod", query="SELECT g FROM GregDTipologia g where g.dataCancellazione IS NULL and g.codTipologia = :codTipologia")
})

public class GregDTipologia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPOLOGIA_IDTIPOLOGIA_GENERATOR", sequenceName="GREG_D_TIPOLOGIA_ID_TIPOLOGIA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPOLOGIA_IDTIPOLOGIA_GENERATOR")
	@Column(name="id_tipologia")
	private Integer idTipologia;

	@Column(name="cod_tipologia")
	private String codTipologia;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_tipologia")
	private String desTipologia;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipologia")
	private Set<GregTPrestazioniRegionali1> gregTPrestazioniRegionali1s;

	//bi-directional many-to-one association to GregTPrestazioniRegionali2
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipologia")
	private Set<GregTPrestazioniRegionali2> gregTPrestazioniRegionali2s;

	public GregDTipologia() {
	}

	public Integer getIdTipologia() {
		return this.idTipologia;
	}

	public void setIdTipologia(Integer idTipologia) {
		this.idTipologia = idTipologia;
	}

	public String getCodTipologia() {
		return this.codTipologia;
	}

	public void setCodTipologia(String codTipologia) {
		this.codTipologia = codTipologia;
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

	public String getDesTipologia() {
		return this.desTipologia;
	}

	public void setDesTipologia(String desTipologia) {
		this.desTipologia = desTipologia;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregTPrestazioniRegionali1> getGregTPrestazioniRegionali1s() {
		return this.gregTPrestazioniRegionali1s;
	}

	public void setGregTPrestazioniRegionali1s(Set<GregTPrestazioniRegionali1> gregTPrestazioniRegionali1s) {
		this.gregTPrestazioniRegionali1s = gregTPrestazioniRegionali1s;
	}

	public GregTPrestazioniRegionali1 addGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		getGregTPrestazioniRegionali1s().add(gregTPrestazioniRegionali1);
		gregTPrestazioniRegionali1.setGregDTipologia(this);

		return gregTPrestazioniRegionali1;
	}

	public GregTPrestazioniRegionali1 removeGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		getGregTPrestazioniRegionali1s().remove(gregTPrestazioniRegionali1);
		gregTPrestazioniRegionali1.setGregDTipologia(null);

		return gregTPrestazioniRegionali1;
	}

	public Set<GregTPrestazioniRegionali2> getGregTPrestazioniRegionali2s() {
		return this.gregTPrestazioniRegionali2s;
	}

	public void setGregTPrestazioniRegionali2s(Set<GregTPrestazioniRegionali2> gregTPrestazioniRegionali2s) {
		this.gregTPrestazioniRegionali2s = gregTPrestazioniRegionali2s;
	}

	public GregTPrestazioniRegionali2 addGregTPrestazioniRegionali2(GregTPrestazioniRegionali2 gregTPrestazioniRegionali2) {
		getGregTPrestazioniRegionali2s().add(gregTPrestazioniRegionali2);
		gregTPrestazioniRegionali2.setGregDTipologia(this);

		return gregTPrestazioniRegionali2;
	}

	public GregTPrestazioniRegionali2 removeGregTPrestazioniRegionali2(GregTPrestazioniRegionali2 gregTPrestazioniRegionali2) {
		getGregTPrestazioniRegionali2s().remove(gregTPrestazioniRegionali2);
		gregTPrestazioniRegionali2.setGregDTipologia(null);

		return gregTPrestazioniRegionali2;
	}

}