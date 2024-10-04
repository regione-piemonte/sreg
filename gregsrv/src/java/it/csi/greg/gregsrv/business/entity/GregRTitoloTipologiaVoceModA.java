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
 * The persistent class for the greg_r_titolo_tipologia_voce_mod_a database table.
 * 
 */
@Entity
@Table(name="greg_r_titolo_tipologia_voce_mod_a")
@NamedQuery(name="GregRTitoloTipologiaVoceModA.findAll", query="SELECT g FROM GregRTitoloTipologiaVoceModA g where g.dataCancellazione IS NULL")
public class GregRTitoloTipologiaVoceModA implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_TITOLO_TIPOLOGIA_VOCE_MOD_A_IDTITOLOTIPOLOGIAVOCEMODA_GENERATOR", sequenceName="GREG_R_TITOLO_TIPOLOGIA_VOCE__ID_TITOLO_TIPOLOGIA_VOCE_MOD__SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_TITOLO_TIPOLOGIA_VOCE_MOD_A_IDTITOLOTIPOLOGIAVOCEMODA_GENERATOR")
	@Column(name="id_titolo_tipologia_voce_mod_a")
	private Integer idTitoloTipologiaVoceModA;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_titolo_tipologia_voce_mod_a")
	private String desTitoloTipologiaVoceModA;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRRendicontazioneModAPart1
	@JsonIgnore
	@OneToMany(mappedBy="gregRTitoloTipologiaVoceModA")
	private Set<GregRRendicontazioneModAPart1> gregRRendicontazioneModAPart1s;

	//bi-directional many-to-one association to GregDTipologiaModA
	@ManyToOne
	@JoinColumn(name="id_tipologia_mod_a")
	private GregDTipologiaModA gregDTipologiaModA;

	//bi-directional many-to-one association to GregDTipologiaQuota
	@ManyToOne
	@JoinColumn(name="id_tipologia_quota")
	private GregDTipologiaQuota gregDTipologiaQuota;

	//bi-directional many-to-one association to GregDTitoloModA
	@ManyToOne
	@JoinColumn(name="id_titolo_mod_a")
	private GregDTitoloModA gregDTitoloModA;

	//bi-directional many-to-one association to GregDVoceModA
	@ManyToOne
	@JoinColumn(name="id_voce_mod_a")
	private GregDVoceModA gregDVoceModA;

	public GregRTitoloTipologiaVoceModA() {
	}

	public Integer getIdTitoloTipologiaVoceModA() {
		return this.idTitoloTipologiaVoceModA;
	}

	public void setIdTitoloTipologiaVoceModA(Integer idTitoloTipologiaVoceModA) {
		this.idTitoloTipologiaVoceModA = idTitoloTipologiaVoceModA;
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

	public String getDesTitoloTipologiaVoceModA() {
		return this.desTitoloTipologiaVoceModA;
	}

	public void setDesTitoloTipologiaVoceModA(String desTitoloTipologiaVoceModA) {
		this.desTitoloTipologiaVoceModA = desTitoloTipologiaVoceModA;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRRendicontazioneModAPart1> getGregRRendicontazioneModAPart1s() {
		return this.gregRRendicontazioneModAPart1s;
	}

	public void setGregRRendicontazioneModAPart1s(Set<GregRRendicontazioneModAPart1> gregRRendicontazioneModAPart1s) {
		this.gregRRendicontazioneModAPart1s = gregRRendicontazioneModAPart1s;
	}

	public GregRRendicontazioneModAPart1 addGregRRendicontazioneModAPart1(GregRRendicontazioneModAPart1 gregRRendicontazioneModAPart1) {
		getGregRRendicontazioneModAPart1s().add(gregRRendicontazioneModAPart1);
		gregRRendicontazioneModAPart1.setGregRTitoloTipologiaVoceModA(this);

		return gregRRendicontazioneModAPart1;
	}

	public GregRRendicontazioneModAPart1 removeGregRRendicontazioneModAPart1(GregRRendicontazioneModAPart1 gregRRendicontazioneModAPart1) {
		getGregRRendicontazioneModAPart1s().remove(gregRRendicontazioneModAPart1);
		gregRRendicontazioneModAPart1.setGregRTitoloTipologiaVoceModA(null);

		return gregRRendicontazioneModAPart1;
	}

	public GregDTipologiaModA getGregDTipologiaModA() {
		return this.gregDTipologiaModA;
	}

	public void setGregDTipologiaModA(GregDTipologiaModA gregDTipologiaModA) {
		this.gregDTipologiaModA = gregDTipologiaModA;
	}

	public GregDTipologiaQuota getGregDTipologiaQuota() {
		return this.gregDTipologiaQuota;
	}

	public void setGregDTipologiaQuota(GregDTipologiaQuota gregDTipologiaQuota) {
		this.gregDTipologiaQuota = gregDTipologiaQuota;
	}

	public GregDTitoloModA getGregDTitoloModA() {
		return this.gregDTitoloModA;
	}

	public void setGregDTitoloModA(GregDTitoloModA gregDTitoloModA) {
		this.gregDTitoloModA = gregDTitoloModA;
	}

	public GregDVoceModA getGregDVoceModA() {
		return this.gregDVoceModA;
	}

	public void setGregDVoceModA(GregDVoceModA gregDVoceModA) {
		this.gregDVoceModA = gregDVoceModA;
	}

}