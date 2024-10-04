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
 * The persistent class for the greg_d_titolo_mod_a database table.
 * 
 */
@Entity
@Table(name="greg_d_titolo_mod_a")
@NamedQueries({
	@NamedQuery(name="GregDTitoloModA.findAll", query="SELECT g FROM GregDTitoloModA g"),
	@NamedQuery(name="GregDTitoloModA.findByIdTitolo", query="SELECT g FROM GregDTitoloModA g WHERE g.idTitoloModA = :idTitolo AND g.dataCancellazione IS NULL")
})
public class GregDTitoloModA implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_TITOLO_MOD_A_IDTITOLOMODA_GENERATOR", sequenceName="GREG_D_TITOLO_MOD_A_ID_TITOLO_MOD_A_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_TITOLO_MOD_A_IDTITOLOMODA_GENERATOR")
	@Column(name="id_titolo_mod_a")
	private Integer idTitoloModA;

	@Column(name="altra_des_titolo_mod_a")
	private String altraDesTitoloModA;

	@Column(name="cod_titolo_mod_a")
	private String codTitoloModA;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_titolo_mod_a")
	private String desTitoloModA;

	@Column(name="msg_informativo")
	private String msgInformativo;

	private Integer ordinamento;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRTitoloTipologiaVoceModA
	@JsonIgnore
	@OneToMany(mappedBy="gregDTitoloModA")
	private Set<GregRTitoloTipologiaVoceModA> gregRTitoloTipologiaVoceModAs;

	public GregDTitoloModA() {
	}

	public Integer getIdTitoloModA() {
		return this.idTitoloModA;
	}

	public void setIdTitoloModA(Integer idTitoloModA) {
		this.idTitoloModA = idTitoloModA;
	}

	public String getAltraDesTitoloModA() {
		return this.altraDesTitoloModA;
	}

	public void setAltraDesTitoloModA(String altraDesTitoloModA) {
		this.altraDesTitoloModA = altraDesTitoloModA;
	}

	public String getCodTitoloModA() {
		return this.codTitoloModA;
	}

	public void setCodTitoloModA(String codTitoloModA) {
		this.codTitoloModA = codTitoloModA;
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

	public String getDesTitoloModA() {
		return this.desTitoloModA;
	}

	public void setDesTitoloModA(String desTitoloModA) {
		this.desTitoloModA = desTitoloModA;
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
		gregRTitoloTipologiaVoceModA.setGregDTitoloModA(this);

		return gregRTitoloTipologiaVoceModA;
	}

	public GregRTitoloTipologiaVoceModA removeGregRTitoloTipologiaVoceModA(GregRTitoloTipologiaVoceModA gregRTitoloTipologiaVoceModA) {
		getGregRTitoloTipologiaVoceModAs().remove(gregRTitoloTipologiaVoceModA);
		gregRTitoloTipologiaVoceModA.setGregDTitoloModA(null);

		return gregRTitoloTipologiaVoceModA;
	}

}