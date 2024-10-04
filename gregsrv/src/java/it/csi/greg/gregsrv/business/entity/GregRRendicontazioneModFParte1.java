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
 * The persistent class for the greg_r_rendicontazione_mod_f_parte1 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_mod_f_parte1")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneModFParte1.findAll", query="SELECT g FROM GregRRendicontazioneModFParte1 g"),
	@NamedQuery(name="GregRRendicontazioneModFParte1.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneModFParte1 g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazioneModFParte1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MOD_F_PARTE1_IDRENDICONTAZIONEMODFPARTE1_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MOD_F__ID_RENDICONTAZIONE_MOD_F_PART_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MOD_F_PARTE1_IDRENDICONTAZIONEMODFPARTE1_GENERATOR")
	@Column(name="id_rendicontazione_mod_f_parte1")
	private Integer idRendicontazioneModFParte1;

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

	//bi-directional many-to-one association to GregRConteggioPersonale
	@ManyToOne
	@JoinColumn(name="id_conteggio_personale")
	private GregRConteggioPersonale gregRConteggioPersonale;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModFParte1() {
	}

	public Integer getIdRendicontazioneModFParte1() {
		return this.idRendicontazioneModFParte1;
	}

	public void setIdRendicontazioneModFParte1(Integer idRendicontazioneModFParte1) {
		this.idRendicontazioneModFParte1 = idRendicontazioneModFParte1;
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

	public GregRConteggioPersonale getGregRConteggioPersonale() {
		return this.gregRConteggioPersonale;
	}

	public void setGregRConteggioPersonale(GregRConteggioPersonale gregRConteggioPersonale) {
		this.gregRConteggioPersonale = gregRConteggioPersonale;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}