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
 * The persistent class for the greg_r_prest_minist_utenze_minist database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_minist_utenze_minist")
@NamedQuery(name="GregRPrestMinistUtenzeMinist.findAll", query="SELECT g FROM GregRPrestMinistUtenzeMinist g where g.dataCancellazione IS NULL")
public class GregRPrestMinistUtenzeMinist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_MINIST_UTENZE_MINIST_IDPRESTMINISTUTENZEMINIST_GENERATOR", sequenceName="GREG_R_PREST_MINIST_UTENZE_MI_ID_PREST_MINIST_UTENZE_MINIST_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_MINIST_UTENZE_MINIST_IDPRESTMINISTUTENZEMINIST_GENERATOR")
	@Column(name="id_prest_minist_utenze_minist")
	private Integer idPrestMinistUtenzeMinist;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza")
	private GregDTargetUtenza gregDTargetUtenza;

	//bi-directional many-to-one association to GregTPrestazioniMinisteriali
	@ManyToOne
	@JoinColumn(name="id_prest_minist")
	private GregTPrestazioniMinisteriali gregTPrestazioniMinisteriali;

	//bi-directional many-to-one association to GregRRendicontazioneModuloFnps
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestMinistUtenzeMinist")
	private Set<GregRRendicontazioneModuloFnps> gregRRendicontazioneModuloFnps;

	public GregRPrestMinistUtenzeMinist() {
	}

	public Integer getIdPrestMinistUtenzeMinist() {
		return this.idPrestMinistUtenzeMinist;
	}

	public void setIdPrestMinistUtenzeMinist(Integer idPrestMinistUtenzeMinist) {
		this.idPrestMinistUtenzeMinist = idPrestMinistUtenzeMinist;
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

	public Timestamp getDataFineValidita() {
		return this.dataFineValidita;
	}

	public void setDataFineValidita(Timestamp dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public Timestamp getDataInizioValidita() {
		return this.dataInizioValidita;
	}

	public void setDataInizioValidita(Timestamp dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
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

	public GregDTargetUtenza getGregDTargetUtenza() {
		return this.gregDTargetUtenza;
	}

	public void setGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		this.gregDTargetUtenza = gregDTargetUtenza;
	}

	public GregTPrestazioniMinisteriali getGregTPrestazioniMinisteriali() {
		return this.gregTPrestazioniMinisteriali;
	}

	public void setGregTPrestazioniMinisteriali(GregTPrestazioniMinisteriali gregTPrestazioniMinisteriali) {
		this.gregTPrestazioniMinisteriali = gregTPrestazioniMinisteriali;
	}

	public Set<GregRRendicontazioneModuloFnps> getGregRRendicontazioneModuloFnps() {
		return this.gregRRendicontazioneModuloFnps;
	}

	public void setGregRRendicontazioneModuloFnps(Set<GregRRendicontazioneModuloFnps> gregRRendicontazioneModuloFnps) {
		this.gregRRendicontazioneModuloFnps = gregRRendicontazioneModuloFnps;
	}

	public GregRRendicontazioneModuloFnps addGregRRendicontazioneModuloFnp(GregRRendicontazioneModuloFnps gregRRendicontazioneModuloFnp) {
		getGregRRendicontazioneModuloFnps().add(gregRRendicontazioneModuloFnp);
		gregRRendicontazioneModuloFnp.setGregRPrestMinistUtenzeMinist(this);

		return gregRRendicontazioneModuloFnp;
	}

	public GregRRendicontazioneModuloFnps removeGregRRendicontazioneModuloFnp(GregRRendicontazioneModuloFnps gregRRendicontazioneModuloFnp) {
		getGregRRendicontazioneModuloFnps().remove(gregRRendicontazioneModuloFnp);
		gregRRendicontazioneModuloFnp.setGregRPrestMinistUtenzeMinist(null);

		return gregRRendicontazioneModuloFnp;
	}

}