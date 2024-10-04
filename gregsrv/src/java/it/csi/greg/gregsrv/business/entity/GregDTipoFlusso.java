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
 * The persistent class for the greg_d_tipo_flusso database table.
 * 
 */
@Entity
@Table(name="greg_d_tipo_flusso")
@NamedQuery(name="GregDTipoFlusso.findAll", query="SELECT g FROM GregDTipoFlusso g where g.dataCancellazione IS NULL")
public class GregDTipoFlusso implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPO_FLUSSO_IDTIPOFLUSSO_GENERATOR", sequenceName="GREG_D_TIPO_FLUSSO_ID_TIPO_FLUSSO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPO_FLUSSO_IDTIPOFLUSSO_GENERATOR")
	@Column(name="id_tipo_flusso")
	private Integer idTipoFlusso;

	@Column(name="cod_tipo_flusso")
	private String codTipoFlusso;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_tipo_flusso")
	private String desTipoFlusso;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDTargetUtenza
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipoFlusso")
	private Set<GregDTargetUtenza> gregDTargetUtenzas;

	public GregDTipoFlusso() {
	}

	public Integer getIdTipoFlusso() {
		return this.idTipoFlusso;
	}

	public void setIdTipoFlusso(Integer idTipoFlusso) {
		this.idTipoFlusso = idTipoFlusso;
	}

	public String getCodTipoFlusso() {
		return this.codTipoFlusso;
	}

	public void setCodTipoFlusso(String codTipoFlusso) {
		this.codTipoFlusso = codTipoFlusso;
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

	public String getDesTipoFlusso() {
		return this.desTipoFlusso;
	}

	public void setDesTipoFlusso(String desTipoFlusso) {
		this.desTipoFlusso = desTipoFlusso;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDTargetUtenza> getGregDTargetUtenzas() {
		return this.gregDTargetUtenzas;
	}

	public void setGregDTargetUtenzas(Set<GregDTargetUtenza> gregDTargetUtenzas) {
		this.gregDTargetUtenzas = gregDTargetUtenzas;
	}

	public GregDTargetUtenza addGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		getGregDTargetUtenzas().add(gregDTargetUtenza);
		gregDTargetUtenza.setGregDTipoFlusso(this);

		return gregDTargetUtenza;
	}

	public GregDTargetUtenza removeGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		getGregDTargetUtenzas().remove(gregDTargetUtenza);
		gregDTargetUtenza.setGregDTipoFlusso(null);

		return gregDTargetUtenza;
	}

}