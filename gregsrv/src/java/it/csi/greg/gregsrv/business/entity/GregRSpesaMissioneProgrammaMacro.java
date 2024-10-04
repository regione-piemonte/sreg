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
 * The persistent class for the greg_r_spesa_missione_programma_macro database table.
 * 
 */
@Entity
@Table(name="greg_r_spesa_missione_programma_macro")
@NamedQueries({
	@NamedQuery(name="GregRSpesaMissioneProgrammaMacro.findAll", query="SELECT g FROM GregRSpesaMissioneProgrammaMacro g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregRSpesaMissioneProgrammaMacro.findBySpesaMacroaggregato", query="SELECT g FROM GregRSpesaMissioneProgrammaMacro g WHERE g.gregDSpesaMissioneProgramma.idSpesaMissioneProgramma = :idSpesa "
			+ "AND g.gregTMacroaggregatiBilancio.idMacroaggregatoBilancio = :idMacroaggregato and g.dataCancellazione is null")
})
public class GregRSpesaMissioneProgrammaMacro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_SPESA_MISSIONE_PROGRAMMA_MACRO_IDSPESAMISSIONEPROGRAMMAMACRO_GENERATOR", sequenceName="GREG_R_SPESA_MISSIONE_PROGRAM_ID_SPESA_MISSIONE_PROGRAMMA_M_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_SPESA_MISSIONE_PROGRAMMA_MACRO_IDSPESAMISSIONEPROGRAMMAMACRO_GENERATOR")
	@Column(name="id_spesa_missione_programma_macro")
	private Integer idSpesaMissioneProgrammaMacro;

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

	//bi-directional many-to-one association to GregRRendicontazioneSpesaMissioneProgrammaMacro
	@JsonIgnore
	@OneToMany(mappedBy="gregRSpesaMissioneProgrammaMacro")
	private Set<GregRRendicontazioneSpesaMissioneProgrammaMacro> gregRRendicontazioneSpesaMissioneProgrammaMacros;

	//bi-directional many-to-one association to GregDSpesaMissioneProgramma
	@ManyToOne
	@JoinColumn(name="id_spesa_missione_programma")
	private GregDSpesaMissioneProgramma gregDSpesaMissioneProgramma;

	//bi-directional many-to-one association to GregTMacroaggregatiBilancio
	@ManyToOne
	@JoinColumn(name="id_macroaggregato_bilancio")
	private GregTMacroaggregatiBilancio gregTMacroaggregatiBilancio;

	public GregRSpesaMissioneProgrammaMacro() {
	}

	public Integer getIdSpesaMissioneProgrammaMacro() {
		return this.idSpesaMissioneProgrammaMacro;
	}

	public void setIdSpesaMissioneProgrammaMacro(Integer idSpesaMissioneProgrammaMacro) {
		this.idSpesaMissioneProgrammaMacro = idSpesaMissioneProgrammaMacro;
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

	public Set<GregRRendicontazioneSpesaMissioneProgrammaMacro> getGregRRendicontazioneSpesaMissioneProgrammaMacros() {
		return this.gregRRendicontazioneSpesaMissioneProgrammaMacros;
	}

	public void setGregRRendicontazioneSpesaMissioneProgrammaMacros(Set<GregRRendicontazioneSpesaMissioneProgrammaMacro> gregRRendicontazioneSpesaMissioneProgrammaMacros) {
		this.gregRRendicontazioneSpesaMissioneProgrammaMacros = gregRRendicontazioneSpesaMissioneProgrammaMacros;
	}

	public GregRRendicontazioneSpesaMissioneProgrammaMacro addGregRRendicontazioneSpesaMissioneProgrammaMacro(GregRRendicontazioneSpesaMissioneProgrammaMacro gregRRendicontazioneSpesaMissioneProgrammaMacro) {
		getGregRRendicontazioneSpesaMissioneProgrammaMacros().add(gregRRendicontazioneSpesaMissioneProgrammaMacro);
		gregRRendicontazioneSpesaMissioneProgrammaMacro.setGregRSpesaMissioneProgrammaMacro(this);

		return gregRRendicontazioneSpesaMissioneProgrammaMacro;
	}

	public GregRRendicontazioneSpesaMissioneProgrammaMacro removeGregRRendicontazioneSpesaMissioneProgrammaMacro(GregRRendicontazioneSpesaMissioneProgrammaMacro gregRRendicontazioneSpesaMissioneProgrammaMacro) {
		getGregRRendicontazioneSpesaMissioneProgrammaMacros().remove(gregRRendicontazioneSpesaMissioneProgrammaMacro);
		gregRRendicontazioneSpesaMissioneProgrammaMacro.setGregRSpesaMissioneProgrammaMacro(null);

		return gregRRendicontazioneSpesaMissioneProgrammaMacro;
	}

	public GregDSpesaMissioneProgramma getGregDSpesaMissioneProgramma() {
		return this.gregDSpesaMissioneProgramma;
	}

	public void setGregDSpesaMissioneProgramma(GregDSpesaMissioneProgramma gregDSpesaMissioneProgramma) {
		this.gregDSpesaMissioneProgramma = gregDSpesaMissioneProgramma;
	}

	public GregTMacroaggregatiBilancio getGregTMacroaggregatiBilancio() {
		return this.gregTMacroaggregatiBilancio;
	}

	public void setGregTMacroaggregatiBilancio(GregTMacroaggregatiBilancio gregTMacroaggregatiBilancio) {
		this.gregTMacroaggregatiBilancio = gregTMacroaggregatiBilancio;
	}

}