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
 * The persistent class for the greg_d_tipologia_mod_a database table.
 * 
 */
@Entity
@Table(name="greg_d_tipologia_mod_a")
@NamedQueries({
	@NamedQuery(name="GregDTipologiaModA.findAll", query="SELECT g FROM GregDTipologiaModA g"),
	@NamedQuery(name="GregDTipologiaModA.findByIdTipologia", query="SELECT g FROM GregDTipologiaModA g WHERE g.idTipologiaModA = :idTipologia AND g.dataCancellazione IS NULL")
})
public class GregDTipologiaModA implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TIPOLOGIA_MOD_A_IDTIPOLOGIAMODA_GENERATOR", sequenceName="GREG_D_TIPOLOGIA_MOD_A_ID_TIPOLOGIA_MOD_A_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TIPOLOGIA_MOD_A_IDTIPOLOGIAMODA_GENERATOR")
	@Column(name="id_tipologia_mod_a")
	private Integer idTipologiaModA;

	@Column(name="cod_tipologia_mod_a")
	private String codTipologiaModA;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_cod_tipologia_mod_a")
	private String desCodTipologiaModA;

	@Column(name="des_tipologia_mod_a")
	private String desTipologiaModA;

	@Column(name="msg_informativo")
	private String msgInformativo;

	private Integer ordinamento;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRTitoloTipologiaVoceModA
	@JsonIgnore
	@OneToMany(mappedBy="gregDTipologiaModA")
	private Set<GregRTitoloTipologiaVoceModA> gregRTitoloTipologiaVoceModAs;

	public GregDTipologiaModA() {
	}

	public Integer getIdTipologiaModA() {
		return this.idTipologiaModA;
	}

	public void setIdTipologiaModA(Integer idTipologiaModA) {
		this.idTipologiaModA = idTipologiaModA;
	}

	public String getCodTipologiaModA() {
		return this.codTipologiaModA;
	}

	public void setCodTipologiaModA(String codTipologiaModA) {
		this.codTipologiaModA = codTipologiaModA;
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

	public String getDesCodTipologiaModA() {
		return this.desCodTipologiaModA;
	}

	public void setDesCodTipologiaModA(String desCodTipologiaModA) {
		this.desCodTipologiaModA = desCodTipologiaModA;
	}

	public String getDesTipologiaModA() {
		return this.desTipologiaModA;
	}

	public void setDesTipologiaModA(String desTipologiaModA) {
		this.desTipologiaModA = desTipologiaModA;
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

	public Set<GregRTitoloTipologiaVoceModA> getGregRTitoloTipologiaVoceModAs() {
		return this.gregRTitoloTipologiaVoceModAs;
	}

	public void setGregRTitoloTipologiaVoceModAs(Set<GregRTitoloTipologiaVoceModA> gregRTitoloTipologiaVoceModAs) {
		this.gregRTitoloTipologiaVoceModAs = gregRTitoloTipologiaVoceModAs;
	}

	public GregRTitoloTipologiaVoceModA addGregRTitoloTipologiaVoceModA(GregRTitoloTipologiaVoceModA gregRTitoloTipologiaVoceModA) {
		getGregRTitoloTipologiaVoceModAs().add(gregRTitoloTipologiaVoceModA);
		gregRTitoloTipologiaVoceModA.setGregDTipologiaModA(this);

		return gregRTitoloTipologiaVoceModA;
	}

	public GregRTitoloTipologiaVoceModA removeGregRTitoloTipologiaVoceModA(GregRTitoloTipologiaVoceModA gregRTitoloTipologiaVoceModA) {
		getGregRTitoloTipologiaVoceModAs().remove(gregRTitoloTipologiaVoceModA);
		gregRTitoloTipologiaVoceModA.setGregDTipologiaModA(null);

		return gregRTitoloTipologiaVoceModA;
	}

}