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
 * The persistent class for the greg_r_rendicontazione_comune_ente_mod_a2 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_comune_ente_mod_a2")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneComuneEnteModA2.findAll", query="SELECT g FROM GregRRendicontazioneComuneEnteModA2 g"),
	@NamedQuery(name="GregRRendicontazioneComuneEnteModA2.findByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneComuneEnteModA2 g "
			+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione and g.dataCancellazione is null "),
	@NamedQuery(name="GregRRendicontazioneComuneEnteModA2.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneComuneEnteModA2 g "
			+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "AND g.dataCancellazione IS NULL "
			+ "Order by g.gregDComuni.desComune, g.gregDCausaleComuneEnteModA2.descCausaleComuneEnteModA2"),
	@NamedQuery(name="GregRRendicontazioneComuneEnteModA2.findValideByIdRendicontazioneEnteAnno", query="SELECT g FROM GregRRendicontazioneComuneEnteModA2 g "
			+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "AND g.dataCancellazione IS NULL AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazioneComuneEnteModA2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_COMUNE_ENTE_MOD_A2_IDRENDICONTAZIONECOMUNEENTEMODA2_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_COMUNE_ID_RENDICONTAZIONE_COMUNE_ENT_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_COMUNE_ENTE_MOD_A2_IDRENDICONTAZIONECOMUNEENTEMODA2_GENERATOR")
	@Column(name="id_rendicontazione_comune_ente_mod_a2")
	private Integer idRendicontazioneComuneEnteModA2;

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

	//bi-directional many-to-one association to GregDCausaleComuneEnteModA2
	@ManyToOne
	@JoinColumn(name="id_causale_comune_ente_mod_a2")
	private GregDCausaleComuneEnteModA2 gregDCausaleComuneEnteModA2;

	//bi-directional many-to-one association to GregDComuni
	@ManyToOne
	@JoinColumn(name="id_comune")
	private GregDComuni gregDComuni;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneComuneEnteModA2() {
	}

	public Integer getIdRendicontazioneComuneEnteModA2() {
		return this.idRendicontazioneComuneEnteModA2;
	}

	public void setIdRendicontazioneComuneEnteModA2(Integer idRendicontazioneComuneEnteModA2) {
		this.idRendicontazioneComuneEnteModA2 = idRendicontazioneComuneEnteModA2;
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

	public GregDCausaleComuneEnteModA2 getGregDCausaleComuneEnteModA2() {
		return this.gregDCausaleComuneEnteModA2;
	}

	public void setGregDCausaleComuneEnteModA2(GregDCausaleComuneEnteModA2 gregDCausaleComuneEnteModA2) {
		this.gregDCausaleComuneEnteModA2 = gregDCausaleComuneEnteModA2;
	}

	public GregDComuni getGregDComuni() {
		return this.gregDComuni;
	}

	public void setGregDComuni(GregDComuni gregDComuni) {
		this.gregDComuni = gregDComuni;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}