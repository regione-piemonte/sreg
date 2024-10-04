/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_t_audit database table.
 * 
 */
@Entity
@Table(name="greg_t_audit")
@NamedQuery(name="GregTAudit.findAll", query="SELECT g FROM GregTAudit g")
public class GregTAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_AUDIT_IDAUDIT_GENERATOR", sequenceName="GREG_T_AUDIT_ID_AUDIT_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_AUDIT_IDAUDIT_GENERATOR")
	@Column(name="id_audit")
	private Integer idAudit;

	@Column(name="chiave_operazione")
	private String chiaveOperazione;

	@Column(name="data_ora")
	private Timestamp dataOra;

	@Column(name="id_app")
	private String idApp;

	@Column(name="indirizzo_ip")
	private String indirizzoIp;

	@Column(name="oggetto_operazione")
	private String oggettoOperazione;

	private String utente;

	//bi-directional many-to-one association to GregDOperazioneAudit
	@ManyToOne
	@JoinColumn(name="id_operazione")
	private GregDOperazioneAudit gregDOperazioneAudit;

	public GregTAudit() {
	}

	public Integer getIdAudit() {
		return this.idAudit;
	}

	public void setIdAudit(Integer idAudit) {
		this.idAudit = idAudit;
	}

	public String getChiaveOperazione() {
		return this.chiaveOperazione;
	}

	public void setChiaveOperazione(String chiaveOperazione) {
		this.chiaveOperazione = chiaveOperazione;
	}

	public Timestamp getDataOra() {
		return this.dataOra;
	}

	public void setDataOra(Timestamp dataOra) {
		this.dataOra = dataOra;
	}

	public String getIdApp() {
		return this.idApp;
	}

	public void setIdApp(String idApp) {
		this.idApp = idApp;
	}

	public String getIndirizzoIp() {
		return this.indirizzoIp;
	}

	public void setIndirizzoIp(String indirizzoIp) {
		this.indirizzoIp = indirizzoIp;
	}

	public String getOggettoOperazione() {
		return this.oggettoOperazione;
	}

	public void setOggettoOperazione(String oggettoOperazione) {
		this.oggettoOperazione = oggettoOperazione;
	}

	public String getUtente() {
		return this.utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public GregDOperazioneAudit getGregDOperazioneAudit() {
		return this.gregDOperazioneAudit;
	}

	public void setGregDOperazioneAudit(GregDOperazioneAudit gregDOperazioneAudit) {
		this.gregDOperazioneAudit = gregDOperazioneAudit;
	}
	
	public GregTAudit (
			String idApp,
			String indirizzoIp, 
			String user,
			GregDOperazioneAudit operation, 
			String operationObject, 
			String keyOper) {
		this.idApp = idApp;
		this.indirizzoIp = indirizzoIp;
		this.utente = user;
		this.gregDOperazioneAudit = operation;
		this.oggettoOperazione = operationObject;
		this.chiaveOperazione = keyOper;
	}

	public GregTAudit(GregTAudit audit) {
		this.dataOra = audit.getDataOra();
		this.idApp = audit.getIdApp();
		this.indirizzoIp = audit.getIndirizzoIp();
		this.chiaveOperazione = audit.getChiaveOperazione();
		this.oggettoOperazione = audit.getOggettoOperazione();
		this.gregDOperazioneAudit = audit.getGregDOperazioneAudit();
		this.utente = audit.getUtente();
	}
}