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
 * The persistent class for the greg_r_responsabile_contatti database table.
 * 
 */
@Entity
@Table(name="greg_r_responsabile_contatti")
@NamedQueries({
	@NamedQuery(name="GregRResponsabileContatti.findAll", query="SELECT g FROM GregRResponsabileContatti g"),
	@NamedQuery(name="GregRResponsabileContatti.findLastContatti", query="SELECT g FROM GregRResponsabileContatti g WHERE g.gregTResponsabileEnteGestore.codiceFiscale = :codiceFiscale AND g.dataFineValidita IS NULL"),
	@NamedQuery(name="GregRResponsabileContatti.findLastContattiByCod", query="SELECT g FROM GregRResponsabileContatti g WHERE g.idResponsabileContatti = :idRespContatti AND g.dataFineValidita IS NULL")
})
public class GregRResponsabileContatti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_RESPONSABILE_CONTATTI_IDRESPONSABILECONTATTI_GENERATOR", sequenceName="GREG_R_RESPONSABILE_CONTATTI_ID_RESPONSABILE_CONTATTI_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_RESPONSABILE_CONTATTI_IDRESPONSABILECONTATTI_GENERATOR")
	@Column(name="id_responsabile_contatti")
	private Integer idResponsabileContatti;

	private String cellulare;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	private String email;

	private String telefono;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTResponsabileEnteGestore
	@ManyToOne
	@JoinColumn(name="id_responsabile_ente_gestore")
	private GregTResponsabileEnteGestore gregTResponsabileEnteGestore;
	
	//bi-directional many-to-one association to GregREnteGestoreContatti
	@JsonIgnore
	@OneToMany(mappedBy="gregRResponsabileContatti")
    private Set<GregREnteGestoreContatti> gregREnteGestoreContattis;

	public GregRResponsabileContatti() {
	}

	public Integer getIdResponsabileContatti() {
		return this.idResponsabileContatti;
	}

	public void setIdResponsabileContatti(Integer idResponsabileContatti) {
		this.idResponsabileContatti = idResponsabileContatti;
	}

	public String getCellulare() {
		return this.cellulare;
	}

	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregTResponsabileEnteGestore getGregTResponsabileEnteGestore() {
		return this.gregTResponsabileEnteGestore;
	}

	public void setGregTResponsabileEnteGestore(GregTResponsabileEnteGestore gregTResponsabileEnteGestore) {
		this.gregTResponsabileEnteGestore = gregTResponsabileEnteGestore;
	}
	
	public Set<GregREnteGestoreContatti> getGregREnteGestoreContattis() {
		return this.gregREnteGestoreContattis;
	}

	public void setGregREnteGestoreContattis(Set<GregREnteGestoreContatti> gregREnteGestoreContattis) {
		this.gregREnteGestoreContattis = gregREnteGestoreContattis;
	}

	public GregREnteGestoreContatti addGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().add(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregRResponsabileContatti(this);

		return gregREnteGestoreContatti;
	}

	public GregREnteGestoreContatti removeGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().remove(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregRResponsabileContatti(null);

		return gregREnteGestoreContatti;
	}

}