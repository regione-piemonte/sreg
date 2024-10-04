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
 * The persistent class for the greg_d_categoria_istat database table.
 * 
 */
@Entity
@Table(name="greg_d_categoria_istat")
@NamedQuery(name="GregDCategoriaIstat.findAll", query="SELECT g FROM GregDCategoriaIstat g where g.dataCancellazione IS NULL")
public class GregDCategoriaIstat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_CATEGORIA_ISTAT_IDCATEGORIAISTAT_GENERATOR", sequenceName="GREG_D_CATEGORIA_ISTAT_ID_CATEGORIA_ISTAT_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_CATEGORIA_ISTAT_IDCATEGORIAISTAT_GENERATOR")
	@Column(name="id_categoria_istat")
	private Integer idCategoriaIstat;

	@Column(name="cod_categoria_istat")
	private String codCategoriaIstat;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_categoria_istat")
	private String descCategoriaIstat;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRCategVoceIstat
	@JsonIgnore
	@OneToMany(mappedBy="gregDCategoriaIstat")
	private Set<GregRCategVoceIstat> gregRCategVoceIstats;

	public GregDCategoriaIstat() {
	}

	public Integer getIdCategoriaIstat() {
		return this.idCategoriaIstat;
	}

	public void setIdCategoriaIstat(Integer idCategoriaIstat) {
		this.idCategoriaIstat = idCategoriaIstat;
	}

	public String getCodCategoriaIstat() {
		return this.codCategoriaIstat;
	}

	public void setCodCategoriaIstat(String codCategoriaIstat) {
		this.codCategoriaIstat = codCategoriaIstat;
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

	public String getDescCategoriaIstat() {
		return this.descCategoriaIstat;
	}

	public void setDescCategoriaIstat(String descCategoriaIstat) {
		this.descCategoriaIstat = descCategoriaIstat;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRCategVoceIstat> getGregRCategVoceIstats() {
		return this.gregRCategVoceIstats;
	}

	public void setGregRCategVoceIstats(Set<GregRCategVoceIstat> gregRCategVoceIstats) {
		this.gregRCategVoceIstats = gregRCategVoceIstats;
	}

	public GregRCategVoceIstat addGregRCategVoceIstat(GregRCategVoceIstat gregRCategVoceIstat) {
		getGregRCategVoceIstats().add(gregRCategVoceIstat);
		gregRCategVoceIstat.setGregDCategoriaIstat(this);

		return gregRCategVoceIstat;
	}

	public GregRCategVoceIstat removeGregRCategVoceIstat(GregRCategVoceIstat gregRCategVoceIstat) {
		getGregRCategVoceIstats().remove(gregRCategVoceIstat);
		gregRCategVoceIstat.setGregDCategoriaIstat(null);

		return gregRCategVoceIstat;
	}

}