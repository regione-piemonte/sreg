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
 * The persistent class for the greg_d_voce_istat database table.
 * 
 */
@Entity
@Table(name="greg_d_voce_istat")
@NamedQuery(name="GregDVoceIstat.findAll", query="SELECT g FROM GregDVoceIstat g where g.dataCancellazione IS NULL")
public class GregDVoceIstat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_VOCE_ISTAT_IDVOCEISTAT_GENERATOR", sequenceName="GREG_D_VOCE_ISTAT_ID_VOCE_ISTAT_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_VOCE_ISTAT_IDVOCEISTAT_GENERATOR")
	@Column(name="id_voce_istat")
	private Integer idVoceIstat;

	@Column(name="cod_voce_istat")
	private String codVoceIstat;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_voce_istat")
	private String descVoceIstat;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRCatUteVocePrestReg2Istat
	@JsonIgnore
	@OneToMany(mappedBy="gregDVoceIstat")
	private Set<GregRCatUteVocePrestReg2Istat> gregRCatUteVocePrestReg2Istats;

	//bi-directional many-to-one association to GregRCategVoceIstat
	@JsonIgnore
	@OneToMany(mappedBy="gregDVoceIstat")
	private Set<GregRCategVoceIstat> gregRCategVoceIstats;

	//bi-directional many-to-one association to GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregDVoceIstat")
	private Set<GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit> gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits;

	public GregDVoceIstat() {
	}

	public Integer getIdVoceIstat() {
		return this.idVoceIstat;
	}

	public void setIdVoceIstat(Integer idVoceIstat) {
		this.idVoceIstat = idVoceIstat;
	}

	public String getCodVoceIstat() {
		return this.codVoceIstat;
	}

	public void setCodVoceIstat(String codVoceIstat) {
		this.codVoceIstat = codVoceIstat;
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

	public String getDescVoceIstat() {
		return this.descVoceIstat;
	}

	public void setDescVoceIstat(String descVoceIstat) {
		this.descVoceIstat = descVoceIstat;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRCatUteVocePrestReg2Istat> getGregRCatUteVocePrestReg2Istats() {
		return this.gregRCatUteVocePrestReg2Istats;
	}

	public void setGregRCatUteVocePrestReg2Istats(Set<GregRCatUteVocePrestReg2Istat> gregRCatUteVocePrestReg2Istats) {
		this.gregRCatUteVocePrestReg2Istats = gregRCatUteVocePrestReg2Istats;
	}

	public GregRCatUteVocePrestReg2Istat addGregRCatUteVocePrestReg2Istat(GregRCatUteVocePrestReg2Istat gregRCatUteVocePrestReg2Istat) {
		getGregRCatUteVocePrestReg2Istats().add(gregRCatUteVocePrestReg2Istat);
		gregRCatUteVocePrestReg2Istat.setGregDVoceIstat(this);

		return gregRCatUteVocePrestReg2Istat;
	}

	public GregRCatUteVocePrestReg2Istat removeGregRCatUteVocePrestReg2Istat(GregRCatUteVocePrestReg2Istat gregRCatUteVocePrestReg2Istat) {
		getGregRCatUteVocePrestReg2Istats().remove(gregRCatUteVocePrestReg2Istat);
		gregRCatUteVocePrestReg2Istat.setGregDVoceIstat(null);

		return gregRCatUteVocePrestReg2Istat;
	}

	public Set<GregRCategVoceIstat> getGregRCategVoceIstats() {
		return this.gregRCategVoceIstats;
	}

	public void setGregRCategVoceIstats(Set<GregRCategVoceIstat> gregRCategVoceIstats) {
		this.gregRCategVoceIstats = gregRCategVoceIstats;
	}

	public GregRCategVoceIstat addGregRCategVoceIstat(GregRCategVoceIstat gregRCategVoceIstat) {
		getGregRCategVoceIstats().add(gregRCategVoceIstat);
		gregRCategVoceIstat.setGregDVoceIstat(this);

		return gregRCategVoceIstat;
	}

	public GregRCategVoceIstat removeGregRCategVoceIstat(GregRCategVoceIstat gregRCategVoceIstat) {
		getGregRCategVoceIstats().remove(gregRCategVoceIstat);
		gregRCategVoceIstat.setGregDVoceIstat(null);

		return gregRCategVoceIstat;
	}

	public Set<GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit> getGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits() {
		return this.gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits;
	}

	public void setGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits(Set<GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit> gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits) {
		this.gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits = gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits;
	}

	public GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit addGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit(GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit) {
		getGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits().add(gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit);
		gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit.setGregDVoceIstat(this);

		return gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit;
	}

	public GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit removeGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit(GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit) {
		getGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits().remove(gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit);
		gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit.setGregDVoceIstat(null);

		return gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit;
	}

}