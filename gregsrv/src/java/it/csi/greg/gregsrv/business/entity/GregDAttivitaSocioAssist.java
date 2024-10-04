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
 * The persistent class for the greg_d_attivita_socio_assist database table.
 * 
 */
@Entity
@Table(name="greg_d_attivita_socio_assist")
@NamedQueries({
	@NamedQuery(name="GregDAttivitaSocioAssist.findAll", query="SELECT g FROM GregDAttivitaSocioAssist g"),
	@NamedQuery(name="GregDAttivitaSocioAssist.findByCodNotDeleted", query="SELECT g FROM GregDAttivitaSocioAssist g WHERE g.codAttivitaSocioAssist = :codAttivita AND g.dataCancellazione IS NULL")
})
public class GregDAttivitaSocioAssist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_ATTIVITA_SOCIO_ASSIST_IDATTIVITASOCIOASSIST_GENERATOR", sequenceName="GREG_D_ATTIVITA_SOCIO_ASSIST_ID_ATTIVITA_SOCIO_ASSIST_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_ATTIVITA_SOCIO_ASSIST_IDATTIVITASOCIOASSIST_GENERATOR")
	@Column(name="id_attivita_socio_assist")
	private Integer idAttivitaSocioAssist;

	@Column(name="cod_attivita_socio_assist")
	private String codAttivitaSocioAssist;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_attivita_socio_assist")
	private String descAttivitaSocioAssist;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRRendicontazioneModE
	@JsonIgnore
	@OneToMany(mappedBy="gregDAttivitaSocioAssist")
	private Set<GregRRendicontazioneModE> gregRRendicontazioneModEs;

	public GregDAttivitaSocioAssist() {
	}

	public Integer getIdAttivitaSocioAssist() {
		return this.idAttivitaSocioAssist;
	}

	public void setIdAttivitaSocioAssist(Integer idAttivitaSocioAssist) {
		this.idAttivitaSocioAssist = idAttivitaSocioAssist;
	}

	public String getCodAttivitaSocioAssist() {
		return this.codAttivitaSocioAssist;
	}

	public void setCodAttivitaSocioAssist(String codAttivitaSocioAssist) {
		this.codAttivitaSocioAssist = codAttivitaSocioAssist;
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

	public String getDescAttivitaSocioAssist() {
		return this.descAttivitaSocioAssist;
	}

	public void setDescAttivitaSocioAssist(String descAttivitaSocioAssist) {
		this.descAttivitaSocioAssist = descAttivitaSocioAssist;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRRendicontazioneModE> getGregRRendicontazioneModEs() {
		return this.gregRRendicontazioneModEs;
	}

	public void setGregRRendicontazioneModEs(Set<GregRRendicontazioneModE> gregRRendicontazioneModEs) {
		this.gregRRendicontazioneModEs = gregRRendicontazioneModEs;
	}

	public GregRRendicontazioneModE addGregRRendicontazioneModE(GregRRendicontazioneModE gregRRendicontazioneModE) {
		getGregRRendicontazioneModEs().add(gregRRendicontazioneModE);
		gregRRendicontazioneModE.setGregDAttivitaSocioAssist(this);

		return gregRRendicontazioneModE;
	}

	public GregRRendicontazioneModE removeGregRRendicontazioneModE(GregRRendicontazioneModE gregRRendicontazioneModE) {
		getGregRRendicontazioneModEs().remove(gregRRendicontazioneModE);
		gregRRendicontazioneModE.setGregDAttivitaSocioAssist(null);

		return gregRRendicontazioneModE;
	}

}