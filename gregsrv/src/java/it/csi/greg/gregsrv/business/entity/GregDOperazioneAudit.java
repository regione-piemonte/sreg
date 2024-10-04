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
 * The persistent class for the greg_d_operazione_audit database table.
 * 
 */
@Entity
@Table(name="greg_d_operazione_audit")
@NamedQueries({
	@NamedQuery(name="GregDOperazioneAudit.findAll", query="SELECT g FROM GregDOperazioneAudit g"),
	@NamedQuery(name="GregDOperazioneAudit.findByDescOper", query = "SELECT g FROM GregDOperazioneAudit g "
			+ "WHERE g.desOperazioneAudit = :operazione and g.dataCancellazione is null")
})
public class GregDOperazioneAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_OPERAZIONE_AUDIT_IDOPERAZIONEAUDIT_GENERATOR", sequenceName="GREG_D_OPERAZIONE_AUDIT_ID_OPERAZIONE_AUDIT_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_OPERAZIONE_AUDIT_IDOPERAZIONEAUDIT_GENERATOR")
	@Column(name="id_operazione_audit")
	private Integer idOperazioneAudit;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_operazione_audit")
	private String desOperazioneAudit;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTAudit
	@JsonIgnore
	@OneToMany(mappedBy="gregDOperazioneAudit")
	private Set<GregTAudit> gregTAudits;

	public GregDOperazioneAudit() {
	}

	public Integer getIdOperazioneAudit() {
		return this.idOperazioneAudit;
	}

	public void setIdOperazioneAudit(Integer idOperazioneAudit) {
		this.idOperazioneAudit = idOperazioneAudit;
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

	public String getDesOperazioneAudit() {
		return this.desOperazioneAudit;
	}

	public void setDesOperazioneAudit(String desOperazioneAudit) {
		this.desOperazioneAudit = desOperazioneAudit;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregTAudit> getGregTAudits() {
		return this.gregTAudits;
	}

	public void setGregTAudits(Set<GregTAudit> gregTAudits) {
		this.gregTAudits = gregTAudits;
	}

	public GregTAudit addGregTAudit(GregTAudit gregTAudit) {
		getGregTAudits().add(gregTAudit);
		gregTAudit.setGregDOperazioneAudit(this);

		return gregTAudit;
	}

	public GregTAudit removeGregTAudit(GregTAudit gregTAudit) {
		getGregTAudits().remove(gregTAudit);
		gregTAudit.setGregDOperazioneAudit(null);

		return gregTAudit;
	}

}