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
 * The persistent class for the greg_r_prest_reg1_utenze_regionali1 database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_reg1_utenze_regionali1")
@NamedQuery(name="GregRPrestReg1UtenzeRegionali1.findAll", query="SELECT g FROM GregRPrestReg1UtenzeRegionali1 g where g.dataCancellazione IS NULL")
public class GregRPrestReg1UtenzeRegionali1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_REG1_UTENZE_REGIONALI1_IDPRESTREG1UTENZAREGIONALE1_GENERATOR", sequenceName="GREG_R_PREST_REG1_UTENZE_REGI_ID_PREST_REG1_UTENZA_REGIONAL_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_REG1_UTENZE_REGIONALI1_IDPRESTREG1UTENZAREGIONALE1_GENERATOR")
	@Column(name="id_prest_reg1_utenza_regionale1")
	private Integer idPrestReg1UtenzaRegionale1;

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

	//bi-directional many-to-one association to GregDColori
	@ManyToOne
	@JoinColumn(name="id_colore")
	private GregDColori gregDColori;

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza")
	private GregDTargetUtenza gregDTargetUtenza;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg_1")
	private GregTPrestazioniRegionali1 gregTPrestazioniRegionali1;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1ProgrammaMissione
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg1UtenzeRegionali1")
	private Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> gregRPrestReg1UtenzeRegionali1ProgrammaMissiones;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg1UtenzeRegionali1")
	private Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott> gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts;

	//bi-directional many-to-one association to GregRRendicontazioneModAPart2
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg1UtenzeRegionali1")
	private Set<GregRRendicontazioneModAPart2> gregRRendicontazioneModAPart2s;

	//bi-directional many-to-one association to GregRRendicontazionePreg1Utereg1
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg1UtenzeRegionali1")
	private Set<GregRRendicontazionePreg1Utereg1> gregRRendicontazionePreg1Utereg1s;

	//bi-directional many-to-one association to GregRTipologiaSpesaPrestReg1
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg1UtenzeRegionali1")
	private Set<GregRTipologiaSpesaPrestReg1> gregRTipologiaSpesaPrestReg1s;

	public GregRPrestReg1UtenzeRegionali1() {
	}

	public Integer getIdPrestReg1UtenzaRegionale1() {
		return this.idPrestReg1UtenzaRegionale1;
	}

	public void setIdPrestReg1UtenzaRegionale1(Integer idPrestReg1UtenzaRegionale1) {
		this.idPrestReg1UtenzaRegionale1 = idPrestReg1UtenzaRegionale1;
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

	public GregDColori getGregDColori() {
		return this.gregDColori;
	}

	public void setGregDColori(GregDColori gregDColori) {
		this.gregDColori = gregDColori;
	}

	public GregDTargetUtenza getGregDTargetUtenza() {
		return this.gregDTargetUtenza;
	}

	public void setGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		this.gregDTargetUtenza = gregDTargetUtenza;
	}

	public GregTPrestazioniRegionali1 getGregTPrestazioniRegionali1() {
		return this.gregTPrestazioniRegionali1;
	}

	public void setGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
	}

	public Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones() {
		return this.gregRPrestReg1UtenzeRegionali1ProgrammaMissiones;
	}

	public void setGregRPrestReg1UtenzeRegionali1ProgrammaMissiones(Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> gregRPrestReg1UtenzeRegionali1ProgrammaMissiones) {
		this.gregRPrestReg1UtenzeRegionali1ProgrammaMissiones = gregRPrestReg1UtenzeRegionali1ProgrammaMissiones;
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissione addGregRPrestReg1UtenzeRegionali1ProgrammaMissione(GregRPrestReg1UtenzeRegionali1ProgrammaMissione gregRPrestReg1UtenzeRegionali1ProgrammaMissione) {
		getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().add(gregRPrestReg1UtenzeRegionali1ProgrammaMissione);
		gregRPrestReg1UtenzeRegionali1ProgrammaMissione.setGregRPrestReg1UtenzeRegionali1(this);

		return gregRPrestReg1UtenzeRegionali1ProgrammaMissione;
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissione removeGregRPrestReg1UtenzeRegionali1ProgrammaMissione(GregRPrestReg1UtenzeRegionali1ProgrammaMissione gregRPrestReg1UtenzeRegionali1ProgrammaMissione) {
		getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().remove(gregRPrestReg1UtenzeRegionali1ProgrammaMissione);
		gregRPrestReg1UtenzeRegionali1ProgrammaMissione.setGregRPrestReg1UtenzeRegionali1(null);

		return gregRPrestReg1UtenzeRegionali1ProgrammaMissione;
	}

	public Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott> getGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts() {
		return this.gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts;
	}

	public void setGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts(Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott> gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts) {
		this.gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts = gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts;
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott addGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott(GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott) {
		getGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts().add(gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott);
		gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott.setGregRPrestReg1UtenzeRegionali1(this);

		return gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott;
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott removeGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott(GregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott) {
		getGregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSotts().remove(gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott);
		gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott.setGregRPrestReg1UtenzeRegionali1(null);

		return gregRPrestReg1UtenzeRegionali1ProgrammaMissioneTitSott;
	}

	public Set<GregRRendicontazioneModAPart2> getGregRRendicontazioneModAPart2s() {
		return this.gregRRendicontazioneModAPart2s;
	}

	public void setGregRRendicontazioneModAPart2s(Set<GregRRendicontazioneModAPart2> gregRRendicontazioneModAPart2s) {
		this.gregRRendicontazioneModAPart2s = gregRRendicontazioneModAPart2s;
	}

	public GregRRendicontazioneModAPart2 addGregRRendicontazioneModAPart2(GregRRendicontazioneModAPart2 gregRRendicontazioneModAPart2) {
		getGregRRendicontazioneModAPart2s().add(gregRRendicontazioneModAPart2);
		gregRRendicontazioneModAPart2.setGregRPrestReg1UtenzeRegionali1(this);

		return gregRRendicontazioneModAPart2;
	}

	public GregRRendicontazioneModAPart2 removeGregRRendicontazioneModAPart2(GregRRendicontazioneModAPart2 gregRRendicontazioneModAPart2) {
		getGregRRendicontazioneModAPart2s().remove(gregRRendicontazioneModAPart2);
		gregRRendicontazioneModAPart2.setGregRPrestReg1UtenzeRegionali1(null);

		return gregRRendicontazioneModAPart2;
	}

	public Set<GregRRendicontazionePreg1Utereg1> getGregRRendicontazionePreg1Utereg1s() {
		return this.gregRRendicontazionePreg1Utereg1s;
	}

	public void setGregRRendicontazionePreg1Utereg1s(Set<GregRRendicontazionePreg1Utereg1> gregRRendicontazionePreg1Utereg1s) {
		this.gregRRendicontazionePreg1Utereg1s = gregRRendicontazionePreg1Utereg1s;
	}

	public GregRRendicontazionePreg1Utereg1 addGregRRendicontazionePreg1Utereg1(GregRRendicontazionePreg1Utereg1 gregRRendicontazionePreg1Utereg1) {
		getGregRRendicontazionePreg1Utereg1s().add(gregRRendicontazionePreg1Utereg1);
		gregRRendicontazionePreg1Utereg1.setGregRPrestReg1UtenzeRegionali1(this);

		return gregRRendicontazionePreg1Utereg1;
	}

	public GregRRendicontazionePreg1Utereg1 removeGregRRendicontazionePreg1Utereg1(GregRRendicontazionePreg1Utereg1 gregRRendicontazionePreg1Utereg1) {
		getGregRRendicontazionePreg1Utereg1s().remove(gregRRendicontazionePreg1Utereg1);
		gregRRendicontazionePreg1Utereg1.setGregRPrestReg1UtenzeRegionali1(null);

		return gregRRendicontazionePreg1Utereg1;
	}

	public Set<GregRTipologiaSpesaPrestReg1> getGregRTipologiaSpesaPrestReg1s() {
		return this.gregRTipologiaSpesaPrestReg1s;
	}

	public void setGregRTipologiaSpesaPrestReg1s(Set<GregRTipologiaSpesaPrestReg1> gregRTipologiaSpesaPrestReg1s) {
		this.gregRTipologiaSpesaPrestReg1s = gregRTipologiaSpesaPrestReg1s;
	}

	public GregRTipologiaSpesaPrestReg1 addGregRTipologiaSpesaPrestReg1(GregRTipologiaSpesaPrestReg1 gregRTipologiaSpesaPrestReg1) {
		getGregRTipologiaSpesaPrestReg1s().add(gregRTipologiaSpesaPrestReg1);
		gregRTipologiaSpesaPrestReg1.setGregRPrestReg1UtenzeRegionali1(this);

		return gregRTipologiaSpesaPrestReg1;
	}

	public GregRTipologiaSpesaPrestReg1 removeGregRTipologiaSpesaPrestReg1(GregRTipologiaSpesaPrestReg1 gregRTipologiaSpesaPrestReg1) {
		getGregRTipologiaSpesaPrestReg1s().remove(gregRTipologiaSpesaPrestReg1);
		gregRTipologiaSpesaPrestReg1.setGregRPrestReg1UtenzeRegionali1(null);

		return gregRTipologiaSpesaPrestReg1;
	}

}