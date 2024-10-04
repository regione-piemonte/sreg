/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_fnps_utenza_calcolo database table.
 * 
 */
@Entity
@Table(name="greg_r_fnps_utenza_calcolo")
@NamedQuery(name="GregRFnpsUtenzaCalcolo.findAll", query="SELECT g FROM GregRFnpsUtenzaCalcolo g")
public class GregRFnpsUtenzaCalcolo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_FNPS_UTENZA_CALCOLO_IDFNPSUTENZACALCOLO_GENERATOR", sequenceName="GREG_R_FNPS_UTENZA_CALCOLO_ID_FNPS_UTENZA_CALCOLO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_FNPS_UTENZA_CALCOLO_IDFNPSUTENZACALCOLO_GENERATOR")
	@Column(name="id_fnps_utenza_calcolo")
	private Integer idFnpsUtenzaCalcolo;

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

	@Column(name="utilizzato_per_calcolo")
	private Boolean utilizzatoPerCalcolo;

	@Column(name="valore_percentuale")
	private double valorePercentuale;

	//bi-directional many-to-one association to GregDTargetUtenza
	@ManyToOne
	@JoinColumn(name="id_target_utenza")
	private GregDTargetUtenza gregDTargetUtenza;

	public GregRFnpsUtenzaCalcolo() {
	}

	public Integer getIdFnpsUtenzaCalcolo() {
		return this.idFnpsUtenzaCalcolo;
	}

	public void setIdFnpsUtenzaCalcolo(Integer idFnpsUtenzaCalcolo) {
		this.idFnpsUtenzaCalcolo = idFnpsUtenzaCalcolo;
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

	public Boolean getUtilizzatoPerCalcolo() {
		return this.utilizzatoPerCalcolo;
	}

	public void setUtilizzatoPerCalcolo(Boolean utilizzatoPerCalcolo) {
		this.utilizzatoPerCalcolo = utilizzatoPerCalcolo;
	}

	public double getValorePercentuale() {
		return this.valorePercentuale;
	}

	public void setValorePercentuale(double valorePercentuale) {
		this.valorePercentuale = valorePercentuale;
	}

	public GregDTargetUtenza getGregDTargetUtenza() {
		return this.gregDTargetUtenza;
	}

	public void setGregDTargetUtenza(GregDTargetUtenza gregDTargetUtenza) {
		this.gregDTargetUtenza = gregDTargetUtenza;
	}

}