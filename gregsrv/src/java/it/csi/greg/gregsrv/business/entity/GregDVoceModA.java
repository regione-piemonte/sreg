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
 * The persistent class for the greg_d_voce_mod_a database table.
 * 
 */
@Entity
@Table(name="greg_d_voce_mod_a")
@NamedQueries({
	@NamedQuery(name="GregDVoceModA.findAll", query="SELECT g FROM GregDVoceModA g WHERE g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDVoceModA.findByIdVoce", query="SELECT g FROM GregDVoceModA g WHERE g.idVoceModA = :idVoce AND g.dataCancellazione IS NULL")
})
public class GregDVoceModA implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_VOCE_MOD_A_IDVOCEMODA_GENERATOR", sequenceName="GREG_D_VOCE_MOD_A_ID_VOCE_MOD_A_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_VOCE_MOD_A_IDVOCEMODA_GENERATOR")
	@Column(name="id_voce_mod_a")
	private Integer idVoceModA;

	@Column(name="cod_voce_mod_a")
	private String codVoceModA;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_voce_mod_a")
	private String desVoceModA;

	@Column(name="msg_informativo")
	private String msgInformativo;

	private Integer ordinamento;

	@Column(name="sezione_modello")
	private String sezioneModello;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRRendicontazioneModAPart3
	@JsonIgnore
	@OneToMany(mappedBy="gregDVoceModA")
	private Set<GregRRendicontazioneModAPart3> gregRRendicontazioneModAPart3s;

	//bi-directional many-to-one association to GregRTitoloTipologiaVoceModA
	@JsonIgnore
	@OneToMany(mappedBy="gregDVoceModA")
	private Set<GregRTitoloTipologiaVoceModA> gregRTitoloTipologiaVoceModAs;

	public GregDVoceModA() {
	}

	public Integer getIdVoceModA() {
		return this.idVoceModA;
	}

	public void setIdVoceModA(Integer idVoceModA) {
		this.idVoceModA = idVoceModA;
	}

	public String getCodVoceModA() {
		return this.codVoceModA;
	}

	public void setCodVoceModA(String codVoceModA) {
		this.codVoceModA = codVoceModA;
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

	public String getDesVoceModA() {
		return this.desVoceModA;
	}

	public void setDesVoceModA(String desVoceModA) {
		this.desVoceModA = desVoceModA;
	}

	public String getMsgInformativo() {
		return this.msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
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

	public Set<GregRRendicontazioneModAPart3> getGregRRendicontazioneModAPart3s() {
		return this.gregRRendicontazioneModAPart3s;
	}

	public void setGregRRendicontazioneModAPart3s(Set<GregRRendicontazioneModAPart3> gregRRendicontazioneModAPart3s) {
		this.gregRRendicontazioneModAPart3s = gregRRendicontazioneModAPart3s;
	}

	public GregRRendicontazioneModAPart3 addGregRRendicontazioneModAPart3(GregRRendicontazioneModAPart3 gregRRendicontazioneModAPart3) {
		getGregRRendicontazioneModAPart3s().add(gregRRendicontazioneModAPart3);
		gregRRendicontazioneModAPart3.setGregDVoceModA(this);

		return gregRRendicontazioneModAPart3;
	}

	public GregRRendicontazioneModAPart3 removeGregRRendicontazioneModAPart3(GregRRendicontazioneModAPart3 gregRRendicontazioneModAPart3) {
		getGregRRendicontazioneModAPart3s().remove(gregRRendicontazioneModAPart3);
		gregRRendicontazioneModAPart3.setGregDVoceModA(null);

		return gregRRendicontazioneModAPart3;
	}

	public Set<GregRTitoloTipologiaVoceModA> getGregRTitoloTipologiaVoceModAs() {
		return this.gregRTitoloTipologiaVoceModAs;
	}

	public void setGregRTitoloTipologiaVoceModAs(Set<GregRTitoloTipologiaVoceModA> gregRTitoloTipologiaVoceModAs) {
		this.gregRTitoloTipologiaVoceModAs = gregRTitoloTipologiaVoceModAs;
	}

	public GregRTitoloTipologiaVoceModA addGregRTitoloTipologiaVoceModA(GregRTitoloTipologiaVoceModA gregRTitoloTipologiaVoceModA) {
		getGregRTitoloTipologiaVoceModAs().add(gregRTitoloTipologiaVoceModA);
		gregRTitoloTipologiaVoceModA.setGregDVoceModA(this);

		return gregRTitoloTipologiaVoceModA;
	}

	public GregRTitoloTipologiaVoceModA removeGregRTitoloTipologiaVoceModA(GregRTitoloTipologiaVoceModA gregRTitoloTipologiaVoceModA) {
		getGregRTitoloTipologiaVoceModAs().remove(gregRTitoloTipologiaVoceModA);
		gregRTitoloTipologiaVoceModA.setGregDVoceModA(null);

		return gregRTitoloTipologiaVoceModA;
	}

}