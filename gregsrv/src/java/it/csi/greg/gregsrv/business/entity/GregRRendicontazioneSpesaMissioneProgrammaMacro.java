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
 * The persistent class for the greg_r_rendicontazione_spesa_missione_programma_macro database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_spesa_missione_programma_macro")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneSpesaMissioneProgrammaMacro.findAll", query="SELECT g FROM GregRRendicontazioneSpesaMissioneProgrammaMacro g"),
	@NamedQuery(name="GregRRendicontazioneSpesaMissioneProgrammaMacro.findBySpesaMacroaggregatoEnte", query="SELECT g FROM GregRRendicontazioneSpesaMissioneProgrammaMacro g WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendEnte AND g.gregRSpesaMissioneProgrammaMacro.gregTMacroaggregatiBilancio.idMacroaggregatoBilancio = :idMacroaggregato AND g.gregRSpesaMissioneProgrammaMacro.gregDSpesaMissioneProgramma.idSpesaMissioneProgramma = :idSpesa AND g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregRRendicontazioneSpesaMissioneProgrammaMacro.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneSpesaMissioneProgrammaMacro g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendiconatazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazioneSpesaMissioneProgrammaMacro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_SPESA_MISSIONE_PROGRAMMA_MACRO_IDRENDICONTAZIONESPESAMISSIONEPROGRAMMAMACRO_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_SPESA__ID_RENDICONTAZIONE_SPESA_MISS_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_SPESA_MISSIONE_PROGRAMMA_MACRO_IDRENDICONTAZIONESPESAMISSIONEPROGRAMMAMACRO_GENERATOR")
	@Column(name="id_rendicontazione_spesa_missione_programma_macro")
	private Integer idRendicontazioneSpesaMissioneProgrammaMacro;

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

	//bi-directional many-to-one association to GregRSpesaMissioneProgrammaMacro
	@ManyToOne
	@JoinColumn(name="id_spesa_missione_programma_macro")
	private GregRSpesaMissioneProgrammaMacro gregRSpesaMissioneProgrammaMacro;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneSpesaMissioneProgrammaMacro() {
	}

	public Integer getIdRendicontazioneSpesaMissioneProgrammaMacro() {
		return this.idRendicontazioneSpesaMissioneProgrammaMacro;
	}

	public void setIdRendicontazioneSpesaMissioneProgrammaMacro(Integer idRendicontazioneSpesaMissioneProgrammaMacro) {
		this.idRendicontazioneSpesaMissioneProgrammaMacro = idRendicontazioneSpesaMissioneProgrammaMacro;
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

	public GregRSpesaMissioneProgrammaMacro getGregRSpesaMissioneProgrammaMacro() {
		return this.gregRSpesaMissioneProgrammaMacro;
	}

	public void setGregRSpesaMissioneProgrammaMacro(GregRSpesaMissioneProgrammaMacro gregRSpesaMissioneProgrammaMacro) {
		this.gregRSpesaMissioneProgrammaMacro = gregRSpesaMissioneProgrammaMacro;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}