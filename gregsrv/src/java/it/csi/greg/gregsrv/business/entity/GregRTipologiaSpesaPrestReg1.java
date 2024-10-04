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
 * The persistent class for the greg_r_tipologia_spesa_prest_reg1 database table.
 * 
 */
@Entity
@Table(name="greg_r_tipologia_spesa_prest_reg1")
@NamedQuery(name="GregRTipologiaSpesaPrestReg1.findAll", query="SELECT g FROM GregRTipologiaSpesaPrestReg1 g where g.dataCancellazione IS NULL")
public class GregRTipologiaSpesaPrestReg1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_TIPOLOGIA_SPESA_PREST_REG1_IDTIPOLOGIASPESAPRESTREG1_GENERATOR", sequenceName="GREG_R_TIPOLOGIA_SPESA_PREST__ID_TIPOLOGIA_SPESA_PREST_REG1_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_TIPOLOGIA_SPESA_PREST_REG1_IDTIPOLOGIASPESAPRESTREG1_GENERATOR")
	@Column(name="id_tipologia_spesa_prest_reg1")
	private Integer idTipologiaSpesaPrestReg1;

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

	//bi-directional many-to-one association to GregRRendicontazioneReportReg1
	@JsonIgnore
	@OneToMany(mappedBy="gregRTipologiaSpesaPrestReg1")
	private Set<GregRRendicontazioneReportReg1> gregRRendicontazioneReportReg1s;

	//bi-directional many-to-one association to GregDTipologiaSpesa
	@ManyToOne
	@JoinColumn(name="id_tipologia_spesa")
	private GregDTipologiaSpesa gregDTipologiaSpesa;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1
	@ManyToOne
	@JoinColumn(name="id_prest_reg1_utenza_regionale1")
	private GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1;

	public GregRTipologiaSpesaPrestReg1() {
	}

	public Integer getIdTipologiaSpesaPrestReg1() {
		return this.idTipologiaSpesaPrestReg1;
	}

	public void setIdTipologiaSpesaPrestReg1(Integer idTipologiaSpesaPrestReg1) {
		this.idTipologiaSpesaPrestReg1 = idTipologiaSpesaPrestReg1;
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

	public Set<GregRRendicontazioneReportReg1> getGregRRendicontazioneReportReg1s() {
		return this.gregRRendicontazioneReportReg1s;
	}

	public void setGregRRendicontazioneReportReg1s(Set<GregRRendicontazioneReportReg1> gregRRendicontazioneReportReg1s) {
		this.gregRRendicontazioneReportReg1s = gregRRendicontazioneReportReg1s;
	}

	public GregRRendicontazioneReportReg1 addGregRRendicontazioneReportReg1(GregRRendicontazioneReportReg1 gregRRendicontazioneReportReg1) {
		getGregRRendicontazioneReportReg1s().add(gregRRendicontazioneReportReg1);
		gregRRendicontazioneReportReg1.setGregRTipologiaSpesaPrestReg1(this);

		return gregRRendicontazioneReportReg1;
	}

	public GregRRendicontazioneReportReg1 removeGregRRendicontazioneReportReg1(GregRRendicontazioneReportReg1 gregRRendicontazioneReportReg1) {
		getGregRRendicontazioneReportReg1s().remove(gregRRendicontazioneReportReg1);
		gregRRendicontazioneReportReg1.setGregRTipologiaSpesaPrestReg1(null);

		return gregRRendicontazioneReportReg1;
	}

	public GregDTipologiaSpesa getGregDTipologiaSpesa() {
		return this.gregDTipologiaSpesa;
	}

	public void setGregDTipologiaSpesa(GregDTipologiaSpesa gregDTipologiaSpesa) {
		this.gregDTipologiaSpesa = gregDTipologiaSpesa;
	}

	public GregRPrestReg1UtenzeRegionali1 getGregRPrestReg1UtenzeRegionali1() {
		return this.gregRPrestReg1UtenzeRegionali1;
	}

	public void setGregRPrestReg1UtenzeRegionali1(GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1) {
		this.gregRPrestReg1UtenzeRegionali1 = gregRPrestReg1UtenzeRegionali1;
	}

}