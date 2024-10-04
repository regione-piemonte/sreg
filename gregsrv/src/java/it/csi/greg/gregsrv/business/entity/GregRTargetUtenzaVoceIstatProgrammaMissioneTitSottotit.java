/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_target_utenza_voce_istat_programma_missione_tit_sottotit database table.
 * 
 */
@Entity
@Table(name="greg_r_target_utenza_voce_istat_programma_missione_tit_sottotit")
@NamedQuery(name="GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit.findAll", query="SELECT g FROM GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit g where g.dataCancellazione IS NULL")
public class GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_TARGET_UTENZA_VOCE_ISTAT_PROGRAMMA_MISSIONE_TIT_SOTTOTIT_IDTARGETUTENZAVOCEISTATPROGRAMMAMISSIONETITSOTTOTIT_GENERATOR", sequenceName="GREG_R_TARGET_UTENZA_VOCE_IST_ID_TARGET_UTENZA_VOCE_ISTAT_P_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_TARGET_UTENZA_VOCE_ISTAT_PROGRAMMA_MISSIONE_TIT_SOTTOTIT_IDTARGETUTENZAVOCEISTATPROGRAMMAMISSIONETITSOTTOTIT_GENERATOR")
	@Column(name="id_target_utenza_voce_istat_programma_missione_tit_sottotit")
	private Integer idTargetUtenzaVoceIstatProgrammaMissioneTitSottotit;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza")
	private GregDTargetUtenza gregDTargetUtenza;

	//bi-directional many-to-one association to GregDVoceIstat
	@ManyToOne
	@JoinColumn(name="id_voce_istat")
	private GregDVoceIstat gregDVoceIstat;

	//bi-directional many-to-one association to GregRProgrammaMissioneTitSottotit
	@ManyToOne
	@JoinColumn(name="id_programma_missione_tit_sottotit")
	private GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit;

	public GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit() {
	}

	public Integer getIdTargetUtenzaVoceIstatProgrammaMissioneTitSottotit() {
		return this.idTargetUtenzaVoceIstatProgrammaMissioneTitSottotit;
	}

	public void setIdTargetUtenzaVoceIstatProgrammaMissioneTitSottotit(Integer idTargetUtenzaVoceIstatProgrammaMissioneTitSottotit) {
		this.idTargetUtenzaVoceIstatProgrammaMissioneTitSottotit = idTargetUtenzaVoceIstatProgrammaMissioneTitSottotit;
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

	public GregDTargetUtenza getGregDTargetUtenza() {
		return this.gregDTargetUtenza;
	}

	public void setGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		this.gregDTargetUtenza = gregDTargetUtenza;
	}

	public GregDVoceIstat getGregDVoceIstat() {
		return this.gregDVoceIstat;
	}

	public void setGregDVoceIstat(GregDVoceIstat gregDVoceIstat) {
		this.gregDVoceIstat = gregDVoceIstat;
	}

	public GregRProgrammaMissioneTitSottotit getGregRProgrammaMissioneTitSottotit() {
		return this.gregRProgrammaMissioneTitSottotit;
	}

	public void setGregRProgrammaMissioneTitSottotit(GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit) {
		this.gregRProgrammaMissioneTitSottotit = gregRProgrammaMissioneTitSottotit;
	}

}