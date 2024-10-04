/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_d_msg_informativo database table.
 * 
 */
@Entity
@Table(name="greg_d_msg_informativo")
@NamedQueries({
	@NamedQuery(name="GregDMsgInformativo.findAll", query="SELECT g FROM GregDMsgInformativo g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDMsgInformativo.findBySection", query="SELECT g FROM GregDMsgInformativo g WHERE g.descMsgInformativo = :sezione and g.dataCancellazione is null ORDER BY g.codMsgInformativo"),
	@NamedQuery(name="GregDMsgInformativo.findByCodice", query="SELECT g FROM GregDMsgInformativo g WHERE g.codMsgInformativo = :codMsgInformativo and g.dataCancellazione is null ORDER BY g.codMsgInformativo")
})
public class GregDMsgInformativo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_MSG_INFORMATIVO_IDMSGINFORMATIVO_GENERATOR", sequenceName="GREG_D_MSG_INFORMATIVO_ID_MSG_INFORMATIVO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_MSG_INFORMATIVO_IDMSGINFORMATIVO_GENERATOR")
	@Column(name="id_msg_informativo")
	private Integer idMsgInformativo;

	@Column(name="cod_msg_informativo")
	private String codMsgInformativo;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_msg_informativo")
	private String descMsgInformativo;

	@Column(name="testo_msg_informativo")
	private String testoMsgInformativo;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	public GregDMsgInformativo() {
	}

	public Integer getIdMsgInformativo() {
		return this.idMsgInformativo;
	}

	public void setIdMsgInformativo(Integer idMsgInformativo) {
		this.idMsgInformativo = idMsgInformativo;
	}

	public String getCodMsgInformativo() {
		return this.codMsgInformativo;
	}

	public void setCodMsgInformativo(String codMsgInformativo) {
		this.codMsgInformativo = codMsgInformativo;
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

	public String getDescMsgInformativo() {
		return this.descMsgInformativo;
	}

	public void setDescMsgInformativo(String descMsgInformativo) {
		this.descMsgInformativo = descMsgInformativo;
	}

	public String getTestoMsgInformativo() {
		return this.testoMsgInformativo;
	}

	public void setTestoMsgInformativo(String testoMsgInformativo) {
		this.testoMsgInformativo = testoMsgInformativo;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

}