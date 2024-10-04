/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_parametro_rendicontazione_ente database table.
 * 
 */
@Entity
@Table(name="greg_r_parametro_rendicontazione_ente")
@NamedQuery(name="GregRParametroRendicontazioneEnte.findAll", query="SELECT g FROM GregRParametroRendicontazioneEnte g where g.dataCancellazione IS NULL")
public class GregRParametroRendicontazioneEnte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_PARAMETRO_RENDICONTAZIONE_ENTE_IDPARAMETRORENDICONTAZIONEENTE_GENERATOR", sequenceName="GREG_R_PARAMETRO_RENDICONTAZI_ID_PARAMETRO_RENDICONTAZIONE__SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_PARAMETRO_RENDICONTAZIONE_ENTE_IDPARAMETRORENDICONTAZIONEENTE_GENERATOR")
	@Column(name="id_parametro_rendicontazione_ente")
	private Integer idParametroRendicontazioneEnte;

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

	//bi-directional many-to-one association to GregCParametri
	@ManyToOne
	@JoinColumn(name="id_parametro")
	private GregCParametri gregCParametri;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregRParametroRendicontazioneEnte() {
	}

	public Integer getIdParametroRendicontazioneEnte() {
		return this.idParametroRendicontazioneEnte;
	}

	public void setIdParametroRendicontazioneEnte(Integer idParametroRendicontazioneEnte) {
		this.idParametroRendicontazioneEnte = idParametroRendicontazioneEnte;
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

	public GregCParametri getGregCParametri() {
		return this.gregCParametri;
	}

	public void setGregCParametri(GregCParametri gregCParametri) {
		this.gregCParametri = gregCParametri;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}