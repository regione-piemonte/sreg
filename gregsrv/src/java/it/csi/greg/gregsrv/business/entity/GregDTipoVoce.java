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
 * The persistent class for the greg_d_tipo_voce database table.
 * 
 */
@Entity
@Table(name="greg_d_tipo_voce")
@NamedQueries({
	@NamedQuery(name="GregDTipoVoce.findAll", query="SELECT g FROM GregDTipoVoce g"),
	@NamedQuery(name="GregDTipoVoce.findByIdTipoVoceNotDeleted", query="SELECT g FROM GregDTipoVoce g WHERE g.idTipoVoce = :idTipoVoce AND g.dataCancellazione IS NULL")
})
public class GregDTipoVoce implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPO_VOCE_IDTIPOVOCE_GENERATOR", sequenceName="GREG_D_TIPO_VOCE_ID_TIPO_VOCE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPO_VOCE_IDTIPOVOCE_GENERATOR")
	@Column(name="id_tipo_voce")
	private Integer idTipoVoce;

	@Column(name="cod_tipo_voce")
	private String codTipoVoce;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_tipo_voce")
	private String descTipoVoce;

	private Integer ordinamento;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRTipoVoceModD
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipoVoce")
	private Set<GregRTipoVoceModD> gregRTipoVoceModDs;

	public GregDTipoVoce() {
	}

	public Integer getIdTipoVoce() {
		return this.idTipoVoce;
	}

	public void setIdTipoVoce(Integer idTipoVoce) {
		this.idTipoVoce = idTipoVoce;
	}

	public String getCodTipoVoce() {
		return this.codTipoVoce;
	}

	public void setCodTipoVoce(String codTipoVoce) {
		this.codTipoVoce = codTipoVoce;
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

	public String getDescTipoVoce() {
		return this.descTipoVoce;
	}

	public void setDescTipoVoce(String descTipoVoce) {
		this.descTipoVoce = descTipoVoce;
	}

	public Integer getOrdinamento() {
		return this.ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRTipoVoceModD> getGregRTipoVoceModDs() {
		return this.gregRTipoVoceModDs;
	}

	public void setGregRTipoVoceModDs(Set<GregRTipoVoceModD> gregRTipoVoceModDs) {
		this.gregRTipoVoceModDs = gregRTipoVoceModDs;
	}

	public GregRTipoVoceModD addGregRTipoVoceModD(GregRTipoVoceModD gregRTipoVoceModD) {
		getGregRTipoVoceModDs().add(gregRTipoVoceModD);
		gregRTipoVoceModD.setGregDTipoVoce(this);

		return gregRTipoVoceModD;
	}

	public GregRTipoVoceModD removeGregRTipoVoceModD(GregRTipoVoceModD gregRTipoVoceModD) {
		getGregRTipoVoceModDs().remove(gregRTipoVoceModD);
		gregRTipoVoceModD.setGregDTipoVoce(null);

		return gregRTipoVoceModD;
	}

}