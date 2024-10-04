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
 * The persistent class for the greg_d_voce_mod_a1 database table.
 * 
 */
@Entity
@Table(name="greg_d_voce_mod_a1")
@NamedQueries({
@NamedQuery(name="GregDVoceModA1.findAll", query="SELECT g FROM GregDVoceModA1 g"),
@NamedQuery(name="GregDVoceModA1.soloImporti", query="SELECT g FROM GregDVoceModA1 g where g.codVoceModA1 "
		+ "not like 'T%' and g.dataCancellazione is null"),
@NamedQuery(name="GregDVoceModA1.byCodVoce", query="SELECT g FROM GregDVoceModA1 g where g.codVoceModA1= :codVoce "
		+ "and g.dataCancellazione is null"),
})
public class GregDVoceModA1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_VOCE_MOD_A1_IDVOCEMODA1_GENERATOR", sequenceName="GREG_D_VOCE_MOD_A1_ID_VOCE_MOD_A1_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_VOCE_MOD_A1_IDVOCEMODA1_GENERATOR")
	@Column(name="id_voce_mod_a1")
	private Integer idVoceModA1;

	@Column(name="cod_voce_mod_a1")
	private String codVoceModA1;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_voce_mod_a1")
	private String descVoceModA1;

	@Column(name="msg_informativo")
	private String msgInformativo;

	private Integer ordinamento;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRRendicontazioneModA1
	@JsonIgnore
	@OneToMany(mappedBy="gregDVoceModA1")
	private Set<GregRRendicontazioneModA1> gregRRendicontazioneModA1s;

	public GregDVoceModA1() {
	}

	public Integer getIdVoceModA1() {
		return this.idVoceModA1;
	}

	public void setIdVoceModA1(Integer idVoceModA1) {
		this.idVoceModA1 = idVoceModA1;
	}

	public String getCodVoceModA1() {
		return this.codVoceModA1;
	}

	public void setCodVoceModA1(String codVoceModA1) {
		this.codVoceModA1 = codVoceModA1;
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

	public String getDescVoceModA1() {
		return this.descVoceModA1;
	}

	public void setDescVoceModA1(String descVoceModA1) {
		this.descVoceModA1 = descVoceModA1;
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

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRRendicontazioneModA1> getGregRRendicontazioneModA1s() {
		return this.gregRRendicontazioneModA1s;
	}

	public void setGregRRendicontazioneModA1s(Set<GregRRendicontazioneModA1> gregRRendicontazioneModA1s) {
		this.gregRRendicontazioneModA1s = gregRRendicontazioneModA1s;
	}

	public GregRRendicontazioneModA1 addGregRRendicontazioneModA1(GregRRendicontazioneModA1 gregRRendicontazioneModA1) {
		getGregRRendicontazioneModA1s().add(gregRRendicontazioneModA1);
		gregRRendicontazioneModA1.setGregDVoceModA1(this);

		return gregRRendicontazioneModA1;
	}

	public GregRRendicontazioneModA1 removeGregRRendicontazioneModA1(GregRRendicontazioneModA1 gregRRendicontazioneModA1) {
		getGregRRendicontazioneModA1s().remove(gregRRendicontazioneModA1);
		gregRRendicontazioneModA1.setGregDVoceModA1(null);

		return gregRRendicontazioneModA1;
	}

}