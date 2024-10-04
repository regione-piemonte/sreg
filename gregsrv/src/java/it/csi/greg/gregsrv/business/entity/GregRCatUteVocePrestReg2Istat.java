/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_cat_ute_voce_prest_reg2_istat database table.
 * 
 */
@Entity
@Table(name="greg_r_cat_ute_voce_prest_reg2_istat")
@NamedQuery(name="GregRCatUteVocePrestReg2Istat.findAll", query="SELECT g FROM GregRCatUteVocePrestReg2Istat g where g.dataCancellazione IS NULL")
public class GregRCatUteVocePrestReg2Istat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_CAT_UTE_VOCE_PREST_REG_IDCCATUTEVOCEPRESTREG2I_GENERATOR", sequenceName="GREG_R_CAT_UTE_VOCE_PREST_REG_ID_CCAT_UTE_VOCE_PREST_REG2_I_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_CAT_UTE_VOCE_PREST_REG_IDCCATUTEVOCEPRESTREG2I_GENERATOR")
	@Column(name="id_ccat_ute_voce_prest_reg2_istat")
	private Integer idCcatUteVocePrestReg2Istat;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;
	
	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza")
	private GregDTargetUtenza gregDTargetUtenza;

	//bi-directional many-to-one association to GregDVoceIstat
	@ManyToOne
	@JoinColumn(name="id_voce_istat")
	private GregDVoceIstat gregDVoceIstat;

	//bi-directional many-to-one association to GregTPrestazioniRegionali2
	@ManyToOne
	@JoinColumn(name="id_prest_reg_2")
	private GregTPrestazioniRegionali2 gregTPrestazioniRegionali2;

	public GregRCatUteVocePrestReg2Istat() {
	}

	public Integer getIdCcatUteVocePrestReg2Istat() {
		return this.idCcatUteVocePrestReg2Istat;
	}

	public void setIdCcatUteVocePrestReg2Istat(Integer idCcatUteVocePrestReg2Istat) {
		this.idCcatUteVocePrestReg2Istat = idCcatUteVocePrestReg2Istat;
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

	public GregDTargetUtenza getGregDTargetUtenza() {
		return this.gregDTargetUtenza;
	}

	public void setGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		this.gregDTargetUtenza = gregDTargetUtenza;
	}

	public GregDVoceIstat getGregDVoceIstat() {
		return this.gregDVoceIstat;
	}

	public void setGregDVoceIstat(GregDVoceIstat gregDVoceIstat) {
		this.gregDVoceIstat = gregDVoceIstat;
	}

	public GregTPrestazioniRegionali2 getGregTPrestazioniRegionali2() {
		return this.gregTPrestazioniRegionali2;
	}

	public void setGregTPrestazioniRegionali2(GregTPrestazioniRegionali2 gregTPrestazioniRegionali2) {
		this.gregTPrestazioniRegionali2 = gregTPrestazioniRegionali2;
	}

}