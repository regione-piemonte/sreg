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
 * The persistent class for the greg_d_voce_mod_d database table.
 * 
 */
@Entity
@Table(name="greg_d_voce_mod_d")
@NamedQuery(name="GregDVoceModD.findAll", query="SELECT g FROM GregDVoceModD g where g.dataCancellazione IS NULL")
public class GregDVoceModD implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_VOCE_MOD_D_IDVOCEMODD_GENERATOR", sequenceName="GREG_D_VOCE_MOD_D_ID_VOCE_MOD_D_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_VOCE_MOD_D_IDVOCEMODD_GENERATOR")
	@Column(name="id_voce_mod_d")
	private Integer idVoceModD;

	@Column(name="cod_voce_mod_d")
	private String codVoceModD;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_voce_mod_d")
	private String descVoceModD;

	@Column(name="msg_informativo")
	private String msgInformativo;

	@Column(name="operatore_matematico")
	private String operatoreMatematico;

	private Integer ordinamento;

	@Column(name="sezione_modello")
	private String sezioneModello;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRTipoVoceModD
	@JsonIgnore
	@OneToMany(mappedBy="gregDVoceModD")
	private Set<GregRTipoVoceModD> gregRTipoVoceModDs;

	public GregDVoceModD() {
	}

	public Integer getIdVoceModD() {
		return this.idVoceModD;
	}

	public void setIdVoceModD(Integer idVoceModD) {
		this.idVoceModD = idVoceModD;
	}

	public String getCodVoceModD() {
		return this.codVoceModD;
	}

	public void setCodVoceModD(String codVoceModD) {
		this.codVoceModD = codVoceModD;
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

	public String getDescVoceModD() {
		return this.descVoceModD;
	}

	public void setDescVoceModD(String descVoceModD) {
		this.descVoceModD = descVoceModD;
	}

	public String getMsgInformativo() {
		return this.msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public String getOperatoreMatematico() {
		return this.operatoreMatematico;
	}

	public void setOperatoreMatematico(String operatoreMatematico) {
		this.operatoreMatematico = operatoreMatematico;
	}

	public Integer getOrdinamento() {
		return this.ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

	public String getSezioneModello() {
		return this.sezioneModello;
	}

	public void setSezioneModello(String sezioneModello) {
		this.sezioneModello = sezioneModello;
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
		gregRTipoVoceModD.setGregDVoceModD(this);

		return gregRTipoVoceModD;
	}

	public GregRTipoVoceModD removeGregRTipoVoceModD(GregRTipoVoceModD gregRTipoVoceModD) {
		getGregRTipoVoceModDs().remove(gregRTipoVoceModD);
		gregRTipoVoceModD.setGregDVoceModD(null);

		return gregRTipoVoceModD;
	}

}