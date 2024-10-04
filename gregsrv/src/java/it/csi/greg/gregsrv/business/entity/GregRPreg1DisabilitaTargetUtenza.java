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
 * The persistent class for the greg_r_preg1_disabilita_target_utenza database table.
 * 
 */
@Entity
@Table(name="greg_r_preg1_disabilita_target_utenza")
@NamedQuery(name="GregRPreg1DisabilitaTargetUtenza.findAll", query="SELECT g FROM GregRPreg1DisabilitaTargetUtenza g where g.dataCancellazione IS NULL")
public class GregRPreg1DisabilitaTargetUtenza implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREG1_DISABILITA_TARGET_UTENZA_IDPREG1DISABILITATARGETUTENZA_GENERATOR", sequenceName="GREG_R_PREG1_DISABILITA_TARGET_UTENZA_ID_PREG1_DISABILITA_TARGET_UTENZA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREG1_DISABILITA_TARGET_UTENZA_IDPREG1DISABILITATARGETUTENZA_GENERATOR")
	@Column(name="id_preg1_disabilita_target_utenza")
	private Integer idPreg1DisabilitaTargetUtenza;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRPreg1DisabTargetUtenzaDettDisab
	@JsonIgnore
	@OneToMany(mappedBy="gregRPreg1DisabilitaTargetUtenza")
	private Set<GregRPreg1DisabTargetUtenzaDettDisab> gregRPreg1DisabTargetUtenzaDettDisabs;

	//bi-directional many-to-one association to GregRDisabilitaTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_disabilita_target_utenza")
	private GregRDisabilitaTargetUtenza gregRDisabilitaTargetUtenza;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg1")
	private GregTPrestazioniRegionali1 gregTPrestazioniRegionali1;

	//bi-directional many-to-one association to GregRRendicontazioneModCParte3
	@JsonIgnore
	@OneToMany(mappedBy="gregRPreg1DisabilitaTargetUtenza")
	private Set<GregRRendicontazioneModCParte3> gregRRendicontazioneModCParte3s;

	public GregRPreg1DisabilitaTargetUtenza() {
	}

	public Integer getIdPreg1DisabilitaTargetUtenza() {
		return this.idPreg1DisabilitaTargetUtenza;
	}

	public void setIdPreg1DisabilitaTargetUtenza(Integer idPreg1DisabilitaTargetUtenza) {
		this.idPreg1DisabilitaTargetUtenza = idPreg1DisabilitaTargetUtenza;
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

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRPreg1DisabTargetUtenzaDettDisab> getGregRPreg1DisabTargetUtenzaDettDisabs() {
		return this.gregRPreg1DisabTargetUtenzaDettDisabs;
	}

	public void setGregRPreg1DisabTargetUtenzaDettDisabs(Set<GregRPreg1DisabTargetUtenzaDettDisab> gregRPreg1DisabTargetUtenzaDettDisabs) {
		this.gregRPreg1DisabTargetUtenzaDettDisabs = gregRPreg1DisabTargetUtenzaDettDisabs;
	}

	public GregRPreg1DisabTargetUtenzaDettDisab addGregRPreg1DisabTargetUtenzaDettDisab(GregRPreg1DisabTargetUtenzaDettDisab gregRPreg1DisabTargetUtenzaDettDisab) {
		getGregRPreg1DisabTargetUtenzaDettDisabs().add(gregRPreg1DisabTargetUtenzaDettDisab);
		gregRPreg1DisabTargetUtenzaDettDisab.setGregRPreg1DisabilitaTargetUtenza(this);

		return gregRPreg1DisabTargetUtenzaDettDisab;
	}

	public GregRPreg1DisabTargetUtenzaDettDisab removeGregRPreg1DisabTargetUtenzaDettDisab(GregRPreg1DisabTargetUtenzaDettDisab gregRPreg1DisabTargetUtenzaDettDisab) {
		getGregRPreg1DisabTargetUtenzaDettDisabs().remove(gregRPreg1DisabTargetUtenzaDettDisab);
		gregRPreg1DisabTargetUtenzaDettDisab.setGregRPreg1DisabilitaTargetUtenza(null);

		return gregRPreg1DisabTargetUtenzaDettDisab;
	}

	public GregRDisabilitaTargetUtenza getGregRDisabilitaTargetUtenza() {
		return this.gregRDisabilitaTargetUtenza;
	}

	public void setGregRDisabilitaTargetUtenza(GregRDisabilitaTargetUtenza gregRDisabilitaTargetUtenza) {
		this.gregRDisabilitaTargetUtenza = gregRDisabilitaTargetUtenza;
	}

	public GregTPrestazioniRegionali1 getGregTPrestazioniRegionali1() {
		return this.gregTPrestazioniRegionali1;
	}

	public void setGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
	}

	public Set<GregRRendicontazioneModCParte3> getGregRRendicontazioneModCParte3s() {
		return this.gregRRendicontazioneModCParte3s;
	}

	public void setGregRRendicontazioneModCParte3s(Set<GregRRendicontazioneModCParte3> gregRRendicontazioneModCParte3s) {
		this.gregRRendicontazioneModCParte3s = gregRRendicontazioneModCParte3s;
	}

	public GregRRendicontazioneModCParte3 addGregRRendicontazioneModCParte3(GregRRendicontazioneModCParte3 gregRRendicontazioneModCParte3) {
		getGregRRendicontazioneModCParte3s().add(gregRRendicontazioneModCParte3);
		gregRRendicontazioneModCParte3.setGregRPreg1DisabilitaTargetUtenza(this);

		return gregRRendicontazioneModCParte3;
	}

	public GregRRendicontazioneModCParte3 removeGregRRendicontazioneModCParte3(GregRRendicontazioneModCParte3 gregRRendicontazioneModCParte3) {
		getGregRRendicontazioneModCParte3s().remove(gregRRendicontazioneModCParte3);
		gregRRendicontazioneModCParte3.setGregRPreg1DisabilitaTargetUtenza(null);

		return gregRRendicontazioneModCParte3;
	}

}