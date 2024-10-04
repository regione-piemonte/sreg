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
 * The persistent class for the greg_r_preg1_disab_target_utenza_dett_disab database table.
 * 
 */
@Entity
@Table(name="greg_r_preg1_disab_target_utenza_dett_disab")
@NamedQuery(name="GregRPreg1DisabTargetUtenzaDettDisab.findAll", query="SELECT g FROM GregRPreg1DisabTargetUtenzaDettDisab g where g.dataCancellazione IS NULL")
public class GregRPreg1DisabTargetUtenzaDettDisab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREG1_DISAB_TARGET_UTENZA_DETT_DISAB_IDPREG1DISABTARGETUTENZADETTDISAB_GENERATOR", sequenceName="GREG_R_PREG1_DISAB_TARGET_UTENZA_DETT_DISAB_ID_PREG1_DISAB_TARGET_UTENZA_DETT_DISAB_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREG1_DISAB_TARGET_UTENZA_DETT_DISAB_IDPREG1DISABTARGETUTENZADETTDISAB_GENERATOR")
	@Column(name="id_preg1_disab_target_utenza_dett_disab")
	private Integer idPreg1DisabTargetUtenzaDettDisab;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDDettaglioDisabilita
	@ManyToOne
	@JoinColumn(name="id_dettaglio_disabilita")
	private GregDDettaglioDisabilita gregDDettaglioDisabilita;

	//bi-directional many-to-one association to GregRPreg1DisabilitaTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_preg1_disabilita_target_utenza")
	private GregRPreg1DisabilitaTargetUtenza gregRPreg1DisabilitaTargetUtenza;

	//bi-directional many-to-one association to GregRRendicontazioneModCParte4
	@JsonIgnore
	@OneToMany(mappedBy="gregRPreg1DisabTargetUtenzaDettDisab")
	private Set<GregRRendicontazioneModCParte4> gregRRendicontazioneModCParte4s;

	public GregRPreg1DisabTargetUtenzaDettDisab() {
	}

	public Integer getIdPreg1DisabTargetUtenzaDettDisab() {
		return this.idPreg1DisabTargetUtenzaDettDisab;
	}

	public void setIdPreg1DisabTargetUtenzaDettDisab(Integer idPreg1DisabTargetUtenzaDettDisab) {
		this.idPreg1DisabTargetUtenzaDettDisab = idPreg1DisabTargetUtenzaDettDisab;
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

	public GregDDettaglioDisabilita getGregDDettaglioDisabilita() {
		return this.gregDDettaglioDisabilita;
	}

	public void setGregDDettaglioDisabilita(GregDDettaglioDisabilita gregDDettaglioDisabilita) {
		this.gregDDettaglioDisabilita = gregDDettaglioDisabilita;
	}

	public GregRPreg1DisabilitaTargetUtenza getGregRPreg1DisabilitaTargetUtenza() {
		return this.gregRPreg1DisabilitaTargetUtenza;
	}

	public void setGregRPreg1DisabilitaTargetUtenza(GregRPreg1DisabilitaTargetUtenza gregRPreg1DisabilitaTargetUtenza) {
		this.gregRPreg1DisabilitaTargetUtenza = gregRPreg1DisabilitaTargetUtenza;
	}

	public Set<GregRRendicontazioneModCParte4> getGregRRendicontazioneModCParte4s() {
		return this.gregRRendicontazioneModCParte4s;
	}

	public void setGregRRendicontazioneModCParte4s(Set<GregRRendicontazioneModCParte4> gregRRendicontazioneModCParte4s) {
		this.gregRRendicontazioneModCParte4s = gregRRendicontazioneModCParte4s;
	}

	public GregRRendicontazioneModCParte4 addGregRRendicontazioneModCParte4(GregRRendicontazioneModCParte4 gregRRendicontazioneModCParte4) {
		getGregRRendicontazioneModCParte4s().add(gregRRendicontazioneModCParte4);
		gregRRendicontazioneModCParte4.setGregRPreg1DisabTargetUtenzaDettDisab(this);

		return gregRRendicontazioneModCParte4;
	}

	public GregRRendicontazioneModCParte4 removeGregRRendicontazioneModCParte4(GregRRendicontazioneModCParte4 gregRRendicontazioneModCParte4) {
		getGregRRendicontazioneModCParte4s().remove(gregRRendicontazioneModCParte4);
		gregRRendicontazioneModCParte4.setGregRPreg1DisabTargetUtenzaDettDisab(null);

		return gregRRendicontazioneModCParte4;
	}

}