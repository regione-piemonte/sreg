/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_rendicontazione_preg1_macro database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_preg1_macro")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazionePreg1Macro.findAll", query="SELECT g FROM GregRRendicontazionePreg1Macro g"),
	@NamedQuery(name="GregRRendicontazionePreg1Macro.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazionePreg1Macro g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazionePreg1Macro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_PREG1_MACRO_IDRENDICONTAZIONEPREG1MACRO_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_PREG1__ID_RENDICONTAZIONE_PREG1_MACR_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_PREG1_MACRO_IDRENDICONTAZIONEPREG1MACRO_GENERATOR")
	@Column(name="id_rendicontazione_preg1_macro")
	private Integer idRendicontazionePreg1Macro;

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

	private BigDecimal valore;

	//bi-directional many-to-one association to GregRPrestReg1MacroaggregatiBilancio
	@ManyToOne
	@JoinColumn(name="id_prest_reg1_macroaggregati_bilancio")
	private GregRPrestReg1MacroaggregatiBilancio gregRPrestReg1MacroaggregatiBilancio;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazionePreg1Macro() {
	}

	public Integer getIdRendicontazionePreg1Macro() {
		return this.idRendicontazionePreg1Macro;
	}

	public void setIdRendicontazionePreg1Macro(Integer idRendicontazionePreg1Macro) {
		this.idRendicontazionePreg1Macro = idRendicontazionePreg1Macro;
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

	public BigDecimal getValore() {
		return this.valore;
	}

	public void setValore(BigDecimal valore) {
		this.valore = valore;
	}

	public GregRPrestReg1MacroaggregatiBilancio getGregRPrestReg1MacroaggregatiBilancio() {
		return this.gregRPrestReg1MacroaggregatiBilancio;
	}

	public void setGregRPrestReg1MacroaggregatiBilancio(GregRPrestReg1MacroaggregatiBilancio gregRPrestReg1MacroaggregatiBilancio) {
		this.gregRPrestReg1MacroaggregatiBilancio = gregRPrestReg1MacroaggregatiBilancio;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}