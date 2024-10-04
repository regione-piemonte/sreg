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
 * The persistent class for the greg_r_rendicontazione_mod_f_parte2 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_mod_f_parte2")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneModFParte2.findAll", query="SELECT g FROM GregRRendicontazioneModFParte2 g"),
	@NamedQuery(name="GregRRendicontazioneModFParte2.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneModFParte2 g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazioneModFParte2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MOD_F_PARTE2_IDRENDICONTAZIONEMODFPARTE2_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MOD_F__ID_RENDICONTAZIONE_MOD_F_PAR_SEQ1", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MOD_F_PARTE2_IDRENDICONTAZIONEMODFPARTE2_GENERATOR")
	@Column(name="id_rendicontazione_mod_f_parte2")
	private Integer idRendicontazioneModFParte2;

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

	//bi-directional many-to-one association to GregRConteggioMonteOreSett
	@ManyToOne
	@JoinColumn(name="id_conteggio_monte_ore_sett")
	private GregRConteggioMonteOreSett gregRConteggioMonteOreSett;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModFParte2() {
	}

	public Integer getIdRendicontazioneModFParte2() {
		return this.idRendicontazioneModFParte2;
	}

	public void setIdRendicontazioneModFParte2(Integer idRendicontazioneModFParte2) {
		this.idRendicontazioneModFParte2 = idRendicontazioneModFParte2;
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

	public GregRConteggioMonteOreSett getGregRConteggioMonteOreSett() {
		return this.gregRConteggioMonteOreSett;
	}

	public void setGregRConteggioMonteOreSett(GregRConteggioMonteOreSett gregRConteggioMonteOreSett) {
		this.gregRConteggioMonteOreSett = gregRConteggioMonteOreSett;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}