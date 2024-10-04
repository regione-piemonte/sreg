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
 * The persistent class for the greg_r_rendicontazione_ente_comune_mod_a2 database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_ente_comune_mod_a2")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneEnteComuneModA2.findAll", query="SELECT g FROM GregRRendicontazioneEnteComuneModA2 g"),
	@NamedQuery(name="GregRRendicontazioneEnteComuneModA2.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneEnteComuneModA2 g "
			+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "AND g.dataCancellazione IS NULL "
			+ "Order by g.gregDComuni.desComune, g.gregTCausaleEnteComuneModA2.descCausaleEnteComuneModA2"),
	@NamedQuery(name="GregRRendicontazioneEnteComuneModA2.findByIdRendicontazioneEnte", query="SELECT g FROM GregRRendicontazioneEnteComuneModA2 g "
			+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione and g.dataCancellazione is null "),
	@NamedQuery(name="GregRRendicontazioneEnteComuneModA2.findValideByIdRendicontazioneEnteAnno", query="SELECT g FROM GregRRendicontazioneEnteComuneModA2 g "
			+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "AND g.dataCancellazione IS NULL AND g.valore is not null and g.valore > 0")
})
public class GregRRendicontazioneEnteComuneModA2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_ENTE_COMUNE_MOD_A2_IDRENDICONTAZIONEENTECOMUNEMODA2_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_ENTE_C_ID_RENDICONTAZIONE_ENTE_COMUN_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_ENTE_COMUNE_MOD_A2_IDRENDICONTAZIONEENTECOMUNEMODA2_GENERATOR")
	@Column(name="id_rendicontazione_ente_comune_mod_a2")
	private Integer idRendicontazioneEnteComuneModA2;

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

	//bi-directional many-to-one association to GregDComuni
	@ManyToOne
	@JoinColumn(name="id_comune")
	private GregDComuni gregDComuni;

	//bi-directional many-to-one association to GregTCausaleEnteComuneModA2
	@ManyToOne
	@JoinColumn(name="id_causale_ente_comune_mod_a2")
	private GregTCausaleEnteComuneModA2 gregTCausaleEnteComuneModA2;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneEnteComuneModA2() {
	}

	public Integer getIdRendicontazioneEnteComuneModA2() {
		return this.idRendicontazioneEnteComuneModA2;
	}

	public void setIdRendicontazioneEnteComuneModA2(Integer idRendicontazioneEnteComuneModA2) {
		this.idRendicontazioneEnteComuneModA2 = idRendicontazioneEnteComuneModA2;
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

	public GregDComuni getGregDComuni() {
		return this.gregDComuni;
	}

	public void setGregDComuni(GregDComuni gregDComuni) {
		this.gregDComuni = gregDComuni;
	}

	public GregTCausaleEnteComuneModA2 getGregTCausaleEnteComuneModA2() {
		return this.gregTCausaleEnteComuneModA2;
	}

	public void setGregTCausaleEnteComuneModA2(GregTCausaleEnteComuneModA2 gregTCausaleEnteComuneModA2) {
		this.gregTCausaleEnteComuneModA2 = gregTCausaleEnteComuneModA2;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}