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
 * The persistent class for the greg_r_rendicontazione_mod_d database table.
 * 
 */
@Entity
@Table(name="greg_r_rendicontazione_mod_d")
@NamedQueries({
	@NamedQuery(name="GregRRendicontazioneModD.findAll", query="SELECT g FROM GregRRendicontazioneModD g"),
	@NamedQuery(name="GregRRendicontazioneModD.findByVoceTipoVoceEnte", query="SELECT g FROM GregRRendicontazioneModD g "
			+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendEnte AND g.gregRTipoVoceModD.gregDTipoVoce.idTipoVoce = :idTipoVoce "
			+ "AND g.gregRTipoVoceModD.gregDVoceModD.idVoceModD = :idVoce AND g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregRRendicontazioneModD.findValideByIdRendicontazioneAnno", query="SELECT g FROM GregRRendicontazioneModD g "
			+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione AND g.dataCancellazione IS NULL "
			+ "AND g.valore is not null and g.valore > 0"),
})
public class GregRRendicontazioneModD implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RENDICONTAZIONE_MOD_D_IDRENDICONTAZIONEMODD_GENERATOR", sequenceName="GREG_R_RENDICONTAZIONE_MOD_D_ID_RENDICONTAZIONE_MOD_D_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RENDICONTAZIONE_MOD_D_IDRENDICONTAZIONEMODD_GENERATOR")
	@Column(name="id_rendicontazione_mod_d")
	private Integer idRendicontazioneModD;

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

	//bi-directional many-to-one association to GregRTipoVoceModD
	@ManyToOne
	@JoinColumn(name="id_tipo_voce_mod_d")
	private GregRTipoVoceModD gregRTipoVoceModD;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendicontazioneModD() {
	}

	public Integer getIdRendicontazioneModD() {
		return this.idRendicontazioneModD;
	}

	public void setIdRendicontazioneModD(Integer idRendicontazioneModD) {
		this.idRendicontazioneModD = idRendicontazioneModD;
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

	public GregRTipoVoceModD getGregRTipoVoceModD() {
		return this.gregRTipoVoceModD;
	}

	public void setGregRTipoVoceModD(GregRTipoVoceModD gregRTipoVoceModD) {
		this.gregRTipoVoceModD = gregRTipoVoceModD;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}