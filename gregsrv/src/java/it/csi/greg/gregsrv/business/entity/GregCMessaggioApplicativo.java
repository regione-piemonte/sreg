/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_c_messaggio_applicativo database table.
 * 
 */
@Entity
@Table(name="greg_c_messaggio_applicativo")
@NamedQueries({ 
	@NamedQuery(name="GregCMessaggioApplicativo.findAll", query="SELECT c FROM GregCMessaggioApplicativo c where c.dataCancellazione is null"),
	@NamedQuery(name = "GregCMessaggioApplicativo.findPerCodice", query = "SELECT c FROM GregCMessaggioApplicativo c WHERE c.codMessaggio = :codParam and c.dataCancellazione is null") 
})
public class GregCMessaggioApplicativo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_C_MESSAGGIO_APPLICATIVO_PKIDMESSAGGIOAPPLICATIVO_GENERATOR", sequenceName="GREG_C_MESSAGGIO_APPLICATIVO_PK_ID_MESSAGGIO_APPLICATIVO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_C_MESSAGGIO_APPLICATIVO_PKIDMESSAGGIOAPPLICATIVO_GENERATOR")
	@Column(name="pk_id_messaggio_applicativo")
	private Integer pkIdMessaggioApplicativo;

	@Column(name="cod_messaggio")
	private String codMessaggio;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="testo_messaggio")
	private String testoMessaggio;

	@Column(name="tipo_messaggio")
	private String tipoMessaggio;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	public GregCMessaggioApplicativo() {
	}

	public Integer getPkIdMessaggioApplicativo() {
		return this.pkIdMessaggioApplicativo;
	}

	public void setPkIdMessaggioApplicativo(Integer pkIdMessaggioApplicativo) {
		this.pkIdMessaggioApplicativo = pkIdMessaggioApplicativo;
	}

	public String getCodMessaggio() {
		return this.codMessaggio;
	}

	public void setCodMessaggio(String codMessaggio) {
		this.codMessaggio = codMessaggio;
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

	public String getTestoMessaggio() {
		return this.testoMessaggio;
	}

	public void setTestoMessaggio(String testoMessaggio) {
		this.testoMessaggio = testoMessaggio;
	}

	public String getTipoMessaggio() {
		return this.tipoMessaggio;
	}

	public void setTipoMessaggio(String tipoMessaggio) {
		this.tipoMessaggio = tipoMessaggio;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

}