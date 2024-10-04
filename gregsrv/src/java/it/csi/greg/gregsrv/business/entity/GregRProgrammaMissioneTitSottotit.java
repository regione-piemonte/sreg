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
 * The persistent class for the greg_r_programma_missione_tit_sottotit database table.
 * 
 */
@Entity
@Table(name="greg_r_programma_missione_tit_sottotit")
@NamedQuery(name="GregRProgrammaMissioneTitSottotit.findAll", query="SELECT g FROM GregRProgrammaMissioneTitSottotit g where g.dataCancellazione IS NULL")
public class GregRProgrammaMissioneTitSottotit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PROGRAMMA_MISSIONE_TIT_SOTTOTIT_IDPROGRAMMAMISSIONETITSOTTOTIT_GENERATOR", sequenceName="GREG_R_PROGRAMMA_MISSIONE_TIT_ID_PROGRAMMA_MISSIONE_TIT_SOT_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PROGRAMMA_MISSIONE_TIT_SOTTOTIT_IDPROGRAMMAMISSIONETITSOTTOTIT_GENERATOR")
	@Column(name="id_programma_missione_tit_sottotit")
	private Integer idProgrammaMissioneTitSottotit;

	@Column(name="cod_programma_missione_tit_sottotit")
	private String codProgrammaMissioneTitSottotit;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="msg_informativo")
	private String msgInformativo;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRAttivitaProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregRProgrammaMissioneTitSottotit")
	private Set<GregRAttivitaProgrammaMissioneTitSottotit> gregRAttivitaProgrammaMissioneTitSottotits;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott
	@JsonIgnore
	@OneToMany(mappedBy="gregRProgrammaMissioneTitSottotit")
	private Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott> gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts;

	//bi-directional many-to-one association to GregDProgrammaMissione
	@ManyToOne
	@JoinColumn(name="id_programma_missione")
	private GregDProgrammaMissione gregDProgrammaMissione;

	//bi-directional many-to-one association to GregDSottotitolo
	@ManyToOne
	@JoinColumn(name="id_sottotitolo")
	private GregDSottotitolo gregDSottotitolo;

	//bi-directional many-to-one association to GregDTitolo
	@ManyToOne
	@JoinColumn(name="id_titolo")
	private GregDTitolo gregDTitolo;

	//bi-directional many-to-one association to GregRRendMiProTitEnteGestoreModB
	@JsonIgnore
	@OneToMany(mappedBy="gregRProgrammaMissioneTitSottotit")
	private Set<GregRRendMiProTitEnteGestoreModB> gregRRendMiProTitEnteGestoreModBs;

	//bi-directional many-to-one association to GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregRProgrammaMissioneTitSottotit")
	private Set<GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit> gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits;

	//bi-directional many-to-one association to GregRTipologiaSpesaProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregRProgrammaMissioneTitSottotit")
	private Set<GregRTipologiaSpesaProgrammaMissioneTitSottotit> gregRTipologiaSpesaProgrammaMissioneTitSottotits;

	public GregRProgrammaMissioneTitSottotit() {
	}

	public Integer getIdProgrammaMissioneTitSottotit() {
		return this.idProgrammaMissioneTitSottotit;
	}

	public void setIdProgrammaMissioneTitSottotit(Integer idProgrammaMissioneTitSottotit) {
		this.idProgrammaMissioneTitSottotit = idProgrammaMissioneTitSottotit;
	}

	public String getCodProgrammaMissioneTitSottotit() {
		return this.codProgrammaMissioneTitSottotit;
	}

	public void setCodProgrammaMissioneTitSottotit(String codProgrammaMissioneTitSottotit) {
		this.codProgrammaMissioneTitSottotit = codProgrammaMissioneTitSottotit;
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

	public String getMsgInformativo() {
		return this.msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRAttivitaProgrammaMissioneTitSottotit> getGregRAttivitaProgrammaMissioneTitSottotits() {
		return this.gregRAttivitaProgrammaMissioneTitSottotits;
	}

	public void setGregRAttivitaProgrammaMissioneTitSottotits(Set<GregRAttivitaProgrammaMissioneTitSottotit> gregRAttivitaProgrammaMissioneTitSottotits) {
		this.gregRAttivitaProgrammaMissioneTitSottotits = gregRAttivitaProgrammaMissioneTitSottotits;
	}

	public GregRAttivitaProgrammaMissioneTitSottotit addGregRAttivitaProgrammaMissioneTitSottotit(GregRAttivitaProgrammaMissioneTitSottotit gregRAttivitaProgrammaMissioneTitSottotit) {
		getGregRAttivitaProgrammaMissioneTitSottotits().add(gregRAttivitaProgrammaMissioneTitSottotit);
		gregRAttivitaProgrammaMissioneTitSottotit.setGregRProgrammaMissioneTitSottotit(this);

		return gregRAttivitaProgrammaMissioneTitSottotit;
	}

	public GregRAttivitaProgrammaMissioneTitSottotit removeGregRAttivitaProgrammaMissioneTitSottotit(GregRAttivitaProgrammaMissioneTitSottotit gregRAttivitaProgrammaMissioneTitSottotit) {
		getGregRAttivitaProgrammaMissioneTitSottotits().remove(gregRAttivitaProgrammaMissioneTitSottotit);
		gregRAttivitaProgrammaMissioneTitSottotit.setGregRProgrammaMissioneTitSottotit(null);

		return gregRAttivitaProgrammaMissioneTitSottotit;
	}

	public Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott> getGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts() {
		return this.gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts;
	}

	public void setGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts(Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott> gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts) {
		this.gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts = gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts;
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott addGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott(GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott) {
		getGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts().add(gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott);
		gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott.setGregRProgrammaMissioneTitSottotit(this);

		return gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott;
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott removeGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott(GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott) {
		getGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts().remove(gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott);
		gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott.setGregRProgrammaMissioneTitSottotit(null);

		return gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott;
	}

	public GregDProgrammaMissione getGregDProgrammaMissione() {
		return this.gregDProgrammaMissione;
	}

	public void setGregDProgrammaMissione(GregDProgrammaMissione gregDProgrammaMissione) {
		this.gregDProgrammaMissione = gregDProgrammaMissione;
	}

	public GregDSottotitolo getGregDSottotitolo() {
		return this.gregDSottotitolo;
	}

	public void setGregDSottotitolo(GregDSottotitolo gregDSottotitolo) {
		this.gregDSottotitolo = gregDSottotitolo;
	}

	public GregDTitolo getGregDTitolo() {
		return this.gregDTitolo;
	}

	public void setGregDTitolo(GregDTitolo gregDTitolo) {
		this.gregDTitolo = gregDTitolo;
	}

	public Set<GregRRendMiProTitEnteGestoreModB> getGregRRendMiProTitEnteGestoreModBs() {
		return this.gregRRendMiProTitEnteGestoreModBs;
	}

	public void setGregRRendMiProTitEnteGestoreModBs(Set<GregRRendMiProTitEnteGestoreModB> gregRRendMiProTitEnteGestoreModBs) {
		this.gregRRendMiProTitEnteGestoreModBs = gregRRendMiProTitEnteGestoreModBs;
	}

	public GregRRendMiProTitEnteGestoreModB addGregRRendMiProTitEnteGestoreModB(GregRRendMiProTitEnteGestoreModB gregRRendMiProTitEnteGestoreModB) {
		getGregRRendMiProTitEnteGestoreModBs().add(gregRRendMiProTitEnteGestoreModB);
		gregRRendMiProTitEnteGestoreModB.setGregRProgrammaMissioneTitSottotit(this);

		return gregRRendMiProTitEnteGestoreModB;
	}

	public GregRRendMiProTitEnteGestoreModB removeGregRRendMiProTitEnteGestoreModB(GregRRendMiProTitEnteGestoreModB gregRRendMiProTitEnteGestoreModB) {
		getGregRRendMiProTitEnteGestoreModBs().remove(gregRRendMiProTitEnteGestoreModB);
		gregRRendMiProTitEnteGestoreModB.setGregRProgrammaMissioneTitSottotit(null);

		return gregRRendMiProTitEnteGestoreModB;
	}

	public Set<GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit> getGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits() {
		return this.gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits;
	}

	public void setGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits(Set<GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit> gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits) {
		this.gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits = gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits;
	}

	public GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit addGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit(GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit) {
		getGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits().add(gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit);
		gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit.setGregRProgrammaMissioneTitSottotit(this);

		return gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit;
	}

	public GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit removeGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit(GregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit) {
		getGregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotits().remove(gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit);
		gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit.setGregRProgrammaMissioneTitSottotit(null);

		return gregRTargetUtenzaVoceIstatProgrammaMissioneTitSottotit;
	}

	public Set<GregRTipologiaSpesaProgrammaMissioneTitSottotit> getGregRTipologiaSpesaProgrammaMissioneTitSottotits() {
		return this.gregRTipologiaSpesaProgrammaMissioneTitSottotits;
	}

	public void setGregRTipologiaSpesaProgrammaMissioneTitSottotits(Set<GregRTipologiaSpesaProgrammaMissioneTitSottotit> gregRTipologiaSpesaProgrammaMissioneTitSottotits) {
		this.gregRTipologiaSpesaProgrammaMissioneTitSottotits = gregRTipologiaSpesaProgrammaMissioneTitSottotits;
	}

	public GregRTipologiaSpesaProgrammaMissioneTitSottotit addGregRTipologiaSpesaProgrammaMissioneTitSottotit(GregRTipologiaSpesaProgrammaMissioneTitSottotit gregRTipologiaSpesaProgrammaMissioneTitSottotit) {
		getGregRTipologiaSpesaProgrammaMissioneTitSottotits().add(gregRTipologiaSpesaProgrammaMissioneTitSottotit);
		gregRTipologiaSpesaProgrammaMissioneTitSottotit.setGregRProgrammaMissioneTitSottotit(this);

		return gregRTipologiaSpesaProgrammaMissioneTitSottotit;
	}

	public GregRTipologiaSpesaProgrammaMissioneTitSottotit removeGregRTipologiaSpesaProgrammaMissioneTitSottotit(GregRTipologiaSpesaProgrammaMissioneTitSottotit gregRTipologiaSpesaProgrammaMissioneTitSottotit) {
		getGregRTipologiaSpesaProgrammaMissioneTitSottotits().remove(gregRTipologiaSpesaProgrammaMissioneTitSottotit);
		gregRTipologiaSpesaProgrammaMissioneTitSottotit.setGregRProgrammaMissioneTitSottotit(null);

		return gregRTipologiaSpesaProgrammaMissioneTitSottotit;
	}

}