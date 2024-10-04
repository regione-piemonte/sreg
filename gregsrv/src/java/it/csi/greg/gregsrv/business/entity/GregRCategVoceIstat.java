/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_categ_voce_istat database table.
 * 
 */
@Entity
@Table(name="greg_r_categ_voce_istat")
@NamedQuery(name="GregRCategVoceIstat.findAll", query="SELECT g FROM GregRCategVoceIstat g where g.dataCancellazione IS NULL")
public class GregRCategVoceIstat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_CATEG_VOCE_ISTAT_IDCATEGSOTTOCATEGVOCEISTAT_GENERATOR", sequenceName="GREG_R_CATEG_VOCE_ISTAT_ID_CATEG_SOTTOCATEG_VOCE_ISTAT_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_CATEG_VOCE_ISTAT_IDCATEGSOTTOCATEGVOCEISTAT_GENERATOR")
	@Column(name="id_categ_sottocateg_voce_istat")
	private Integer idCategSottocategVoceIstat;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_categ_sottocateg_voce_istat")
	private String desCategSottocategVoceIstat;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDCategoriaIstat
	@ManyToOne
	@JoinColumn(name="id_categoria_istat")
	private GregDCategoriaIstat gregDCategoriaIstat;

	//bi-directional many-to-one association to GregDVoceIstat
	@ManyToOne
	@JoinColumn(name="id_voce_istat")
	private GregDVoceIstat gregDVoceIstat;

	public GregRCategVoceIstat() {
	}

	public Integer getIdCategSottocategVoceIstat() {
		return this.idCategSottocategVoceIstat;
	}

	public void setIdCategSottocategVoceIstat(Integer idCategSottocategVoceIstat) {
		this.idCategSottocategVoceIstat = idCategSottocategVoceIstat;
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

	public String getDesCategSottocategVoceIstat() {
		return this.desCategSottocategVoceIstat;
	}

	public void setDesCategSottocategVoceIstat(String desCategSottocategVoceIstat) {
		this.desCategSottocategVoceIstat = desCategSottocategVoceIstat;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDCategoriaIstat getGregDCategoriaIstat() {
		return this.gregDCategoriaIstat;
	}

	public void setGregDCategoriaIstat(GregDCategoriaIstat gregDCategoriaIstat) {
		this.gregDCategoriaIstat = gregDCategoriaIstat;
	}

	public GregDVoceIstat getGregDVoceIstat() {
		return this.gregDVoceIstat;
	}

	public void setGregDVoceIstat(GregDVoceIstat gregDVoceIstat) {
		this.gregDVoceIstat = gregDVoceIstat;
	}

}