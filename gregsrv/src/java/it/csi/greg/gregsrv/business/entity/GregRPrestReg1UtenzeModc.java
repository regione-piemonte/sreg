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
 * The persistent class for the greg_r_prest_reg1_utenze_modc database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_reg1_utenze_modc")
@NamedQuery(name="GregRPrestReg1UtenzeModc.findAll", query="SELECT g FROM GregRPrestReg1UtenzeModc g where g.dataCancellazione IS NULL")
public class GregRPrestReg1UtenzeModc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_REG1_UTENZE_MODC_IDPRESTREG1UTENZAMODC_GENERATOR", sequenceName="GREG_R_PREST_REG1_UTENZE_MODC_ID_PREST_REG1_UTENZA_MODC_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_REG1_UTENZE_MODC_IDPRESTREG1UTENZAMODC_GENERATOR")
	@Column(name="id_prest_reg1_utenza_modc")
	private Integer idPrestReg1UtenzaModc;

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

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg_1")
	private GregTPrestazioniRegionali1 gregTPrestazioniRegionali1;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeModcDettUtenze
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg1UtenzeModc")
	private Set<GregRPrestReg1UtenzeModcDettUtenze> gregRPrestReg1UtenzeModcDettUtenzes;

	//bi-directional many-to-one association to GregRRendicontazioneModCParte1
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg1UtenzeModc")
	private Set<GregRRendicontazioneModCParte1> gregRRendicontazioneModCParte1s;

	public GregRPrestReg1UtenzeModc() {
	}

	public Integer getIdPrestReg1UtenzaModc() {
		return this.idPrestReg1UtenzaModc;
	}

	public void setIdPrestReg1UtenzaModc(Integer idPrestReg1UtenzaModc) {
		this.idPrestReg1UtenzaModc = idPrestReg1UtenzaModc;
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

	public GregTPrestazioniRegionali1 getGregTPrestazioniRegionali1() {
		return this.gregTPrestazioniRegionali1;
	}

	public void setGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
	}

	public Set<GregRPrestReg1UtenzeModcDettUtenze> getGregRPrestReg1UtenzeModcDettUtenzes() {
		return this.gregRPrestReg1UtenzeModcDettUtenzes;
	}

	public void setGregRPrestReg1UtenzeModcDettUtenzes(Set<GregRPrestReg1UtenzeModcDettUtenze> gregRPrestReg1UtenzeModcDettUtenzes) {
		this.gregRPrestReg1UtenzeModcDettUtenzes = gregRPrestReg1UtenzeModcDettUtenzes;
	}

	public GregRPrestReg1UtenzeModcDettUtenze addGregRPrestReg1UtenzeModcDettUtenze(GregRPrestReg1UtenzeModcDettUtenze gregRPrestReg1UtenzeModcDettUtenze) {
		getGregRPrestReg1UtenzeModcDettUtenzes().add(gregRPrestReg1UtenzeModcDettUtenze);
		gregRPrestReg1UtenzeModcDettUtenze.setGregRPrestReg1UtenzeModc(this);

		return gregRPrestReg1UtenzeModcDettUtenze;
	}

	public GregRPrestReg1UtenzeModcDettUtenze removeGregRPrestReg1UtenzeModcDettUtenze(GregRPrestReg1UtenzeModcDettUtenze gregRPrestReg1UtenzeModcDettUtenze) {
		getGregRPrestReg1UtenzeModcDettUtenzes().remove(gregRPrestReg1UtenzeModcDettUtenze);
		gregRPrestReg1UtenzeModcDettUtenze.setGregRPrestReg1UtenzeModc(null);

		return gregRPrestReg1UtenzeModcDettUtenze;
	}

	public Set<GregRRendicontazioneModCParte1> getGregRRendicontazioneModCParte1s() {
		return this.gregRRendicontazioneModCParte1s;
	}

	public void setGregRRendicontazioneModCParte1s(Set<GregRRendicontazioneModCParte1> gregRRendicontazioneModCParte1s) {
		this.gregRRendicontazioneModCParte1s = gregRRendicontazioneModCParte1s;
	}

	public GregRRendicontazioneModCParte1 addGregRRendicontazioneModCParte1(GregRRendicontazioneModCParte1 gregRRendicontazioneModCParte1) {
		getGregRRendicontazioneModCParte1s().add(gregRRendicontazioneModCParte1);
		gregRRendicontazioneModCParte1.setGregRPrestReg1UtenzeModc(this);

		return gregRRendicontazioneModCParte1;
	}

	public GregRRendicontazioneModCParte1 removeGregRRendicontazioneModCParte1(GregRRendicontazioneModCParte1 gregRRendicontazioneModCParte1) {
		getGregRRendicontazioneModCParte1s().remove(gregRRendicontazioneModCParte1);
		gregRRendicontazioneModCParte1.setGregRPrestReg1UtenzeModc(null);

		return gregRRendicontazioneModCParte1;
	}

}