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
 * The persistent class for the greg_r_prest_reg1_macroaggregati_bilancio database table.
 * 
 */
@Entity
@Table(name="greg_r_prest_reg1_macroaggregati_bilancio")
@NamedQuery(name="GregRPrestReg1MacroaggregatiBilancio.findAll", query="SELECT g FROM GregRPrestReg1MacroaggregatiBilancio g where g.dataCancellazione IS NULL")
public class GregRPrestReg1MacroaggregatiBilancio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PREST_REG1_MACROAGGREGATI_BILANCIO_IDPRESTREG1MACROAGGREGATIBILANCIO_GENERATOR", sequenceName="GREG_R_PREST_REG1_MACROAGGREG_ID_PREST_REG1_MACROAGGREGATI__SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PREST_REG1_MACROAGGREGATI_BILANCIO_IDPRESTREG1MACROAGGREGATIBILANCIO_GENERATOR")
	@Column(name="id_prest_reg1_macroaggregati_bilancio")
	private Integer idPrestReg1MacroaggregatiBilancio;

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

	//bi-directional many-to-one association to GregTMacroaggregatiBilancio
	@ManyToOne
	@JoinColumn(name="id_macroaggregato_bilancio")
	private GregTMacroaggregatiBilancio gregTMacroaggregatiBilancio;

	//bi-directional many-to-one association to GregTPrestazioniRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg_1")
	private GregTPrestazioniRegionali1 gregTPrestazioniRegionali1;

	//bi-directional many-to-one association to GregRRendicontazionePreg1Macro
	@JsonIgnore
	@OneToMany(mappedBy="gregRPrestReg1MacroaggregatiBilancio")
	private Set<GregRRendicontazionePreg1Macro> gregRRendicontazionePreg1Macros;

	public GregRPrestReg1MacroaggregatiBilancio() {
	}

	public Integer getIdPrestReg1MacroaggregatiBilancio() {
		return this.idPrestReg1MacroaggregatiBilancio;
	}

	public void setIdPrestReg1MacroaggregatiBilancio(Integer idPrestReg1MacroaggregatiBilancio) {
		this.idPrestReg1MacroaggregatiBilancio = idPrestReg1MacroaggregatiBilancio;
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

	public GregTMacroaggregatiBilancio getGregTMacroaggregatiBilancio() {
		return this.gregTMacroaggregatiBilancio;
	}

	public void setGregTMacroaggregatiBilancio(GregTMacroaggregatiBilancio gregTMacroaggregatiBilancio) {
		this.gregTMacroaggregatiBilancio = gregTMacroaggregatiBilancio;
	}

	public GregTPrestazioniRegionali1 getGregTPrestazioniRegionali1() {
		return this.gregTPrestazioniRegionali1;
	}

	public void setGregTPrestazioniRegionali1(GregTPrestazioniRegionali1 gregTPrestazioniRegionali1) {
		this.gregTPrestazioniRegionali1 = gregTPrestazioniRegionali1;
	}

	public Set<GregRRendicontazionePreg1Macro> getGregRRendicontazionePreg1Macros() {
		return this.gregRRendicontazionePreg1Macros;
	}

	public void setGregRRendicontazionePreg1Macros(Set<GregRRendicontazionePreg1Macro> gregRRendicontazionePreg1Macros) {
		this.gregRRendicontazionePreg1Macros = gregRRendicontazionePreg1Macros;
	}

	public GregRRendicontazionePreg1Macro addGregRRendicontazionePreg1Macro(GregRRendicontazionePreg1Macro gregRRendicontazionePreg1Macro) {
		getGregRRendicontazionePreg1Macros().add(gregRRendicontazionePreg1Macro);
		gregRRendicontazionePreg1Macro.setGregRPrestReg1MacroaggregatiBilancio(this);

		return gregRRendicontazionePreg1Macro;
	}

	public GregRRendicontazionePreg1Macro removeGregRRendicontazionePreg1Macro(GregRRendicontazionePreg1Macro gregRRendicontazionePreg1Macro) {
		getGregRRendicontazionePreg1Macros().remove(gregRRendicontazionePreg1Macro);
		gregRRendicontazionePreg1Macro.setGregRPrestReg1MacroaggregatiBilancio(null);

		return gregRRendicontazionePreg1Macro;
	}

}