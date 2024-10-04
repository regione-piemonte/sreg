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
 * The persistent class for the greg_r_rend_mi_pro_tit_ente_gestore_mod_b database table.
 * 
 */
@Entity
@Table(name="greg_r_rend_mi_pro_tit_ente_gestore_mod_b")
@NamedQueries({
	@NamedQuery(name="GregRRendMiProTitEnteGestoreModB.findAll", query="SELECT g FROM GregRRendMiProTitEnteGestoreModB g"),
	@NamedQuery(name="GregRRendMiProTitEnteGestoreModB.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregRRendMiProTitEnteGestoreModB g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null AND g.valore is not null and g.valore > 0")
})
public class GregRRendMiProTitEnteGestoreModB implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_REND_MI_PRO_TIT_ENTE_GESTORE_MOD_B_IDRENDMIPROTITENTEGESTOREMODB_GENERATOR", sequenceName="GREG_R_REND_MI_PRO_TIT_ENTE_G_ID_REND_MI_PRO_TIT_ENTE_GESTO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_REND_MI_PRO_TIT_ENTE_GESTORE_MOD_B_IDRENDMIPROTITENTEGESTOREMODB_GENERATOR")
	@Column(name="id_rend_mi_pro_tit_ente_gestore_mod_b")
	private Integer idRendMiProTitEnteGestoreModB;

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

	//bi-directional many-to-one association to GregRProgrammaMissioneTitSottotit
	@ManyToOne
	@JoinColumn(name="id_programma_missione_tit_sottotit")
	private GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRRendMiProTitEnteGestoreModB() {
	}

	public Integer getIdRendMiProTitEnteGestoreModB() {
		return this.idRendMiProTitEnteGestoreModB;
	}

	public void setIdRendMiProTitEnteGestoreModB(Integer idRendMiProTitEnteGestoreModB) {
		this.idRendMiProTitEnteGestoreModB = idRendMiProTitEnteGestoreModB;
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

	public GregRProgrammaMissioneTitSottotit getGregRProgrammaMissioneTitSottotit() {
		return this.gregRProgrammaMissioneTitSottotit;
	}

	public void setGregRProgrammaMissioneTitSottotit(GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit) {
		this.gregRProgrammaMissioneTitSottotit = gregRProgrammaMissioneTitSottotit;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}