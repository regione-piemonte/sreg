/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import it.csi.greg.gregsrv.dto.ModelComune;
import it.csi.greg.gregsrv.dto.ModelDatiAsl;
import it.csi.greg.gregsrv.dto.ModelResponsabileEnte;
import it.csi.greg.gregsrv.dto.ModelTipoEnte;

import java.sql.Timestamp;

/**
 * The persistent class for the greg_r_ente_gestore_contatti database table.
 * 
 */
@Entity
@Table(name="greg_r_ente_gestore_contatti")
@NamedQueries({
	@NamedQuery(name="GregREnteGestoreContatti.findAll", query="SELECT g FROM GregREnteGestoreContatti g"),
	@NamedQuery(name="GregREnteGestoreContatti.findLastContatti", query="SELECT g FROM GregREnteGestoreContatti g WHERE g.gregTSchedeEntiGestori.idSchedaEnteGestore = :idEnte and"
			+ " g.dataFineValidita IS NULL"),
})
public class GregREnteGestoreContatti implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_ENTE_GESTORE_CONTATTI_IDENTEGESTORECONTATTI_GENERATOR", sequenceName="GREG_R_ENTE_GESTORE_CONTATTI_ID_ENTE_GESTORE_CONTATTI_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_ENTE_GESTORE_CONTATTI_IDENTEGESTORECONTATTI_GENERATOR")
	@Column(name="id_ente_gestore_contatti")
	private Integer idEnteGestoreContatti;

	@Column(name="cod_istat_ente")
	private String codIstatEnte;

	@Column(name="cod_scheda_ente_gestore")
	private String codSchedaEnteGestore;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	private String denominazione;

	private String email;
	
	private String email2;

	private String fax;

	private String indirizzo;
	
	private String note;

	@Column(name="partita_iva")
	private String partitaIva;

	private String pec;

	private String sitoweb;

	private String telefono;

	private String telefono2;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDAmbitiTerritoriali
	@ManyToOne
	@JoinColumn(name="id_ambito_territoriale")
	private GregDAmbitiTerritoriali gregDAmbitiTerritoriali;
		
	//bi-directional many-to-one association to GregDAsl
	@ManyToOne
	@JoinColumn(name="id_asl")
	private GregDAsl gregDAsl;

	//bi-directional many-to-one association to GregDComuni
	@ManyToOne
	@JoinColumn(name="id_comune")
	private GregDComuni gregDComuni;

	//bi-directional many-to-one association to GregDTipoEnte
	@ManyToOne
	@JoinColumn(name="id_tipo_ente")
	private GregDTipoEnte gregDTipoEnte;

	//bi-directional many-to-one association to GregTSchedeEntiGestori
	@ManyToOne
	@JoinColumn(name="id_scheda_ente_gestore")
	private GregTSchedeEntiGestori gregTSchedeEntiGestori;
	
	//bi-directional many-to-one association to GregTResponsabileEnteGestore
	@ManyToOne
	@JoinColumn(name="id_responsabile_contatti")
	private GregRResponsabileContatti gregRResponsabileContatti;
	
	//bi-directional many-to-one association to GregTPresidenteEnteGestore
	@ManyToOne
	@JoinColumn(name="id_presidente_ente_gestore")
	private GregTPresidenteEnteGestore gregTPresidenteEnteGestore;
	
	public GregREnteGestoreContatti() {
	}

	public Integer getIdEnteGestoreContatti() {
		return this.idEnteGestoreContatti;
	}

	public void setIdEnteGestoreContatti(Integer idEnteGestoreContatti) {
		this.idEnteGestoreContatti = idEnteGestoreContatti;
	}

	public String getCodIstatEnte() {
		return this.codIstatEnte;
	}

	public void setCodIstatEnte(String codIstatEnte) {
		this.codIstatEnte = codIstatEnte;
	}

	public String getCodSchedaEnteGestore() {
		return this.codSchedaEnteGestore;
	}

	public void setCodSchedaEnteGestore(String codSchedaEnteGestore) {
		this.codSchedaEnteGestore = codSchedaEnteGestore;
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

	public String getDenominazione() {
		return this.denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail2() {
		return this.email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getSitoweb() {
		return this.sitoweb;
	}

	public void setSitoweb(String sitoweb) {
		this.sitoweb = sitoweb;
	}
	
	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getTelefono2() {
		return this.telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getPartitaIva() {
		return this.partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	public String getPec() {
		return this.pec;
	}

	public void setPec(String pec) {
		this.pec = pec;
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

	public GregDAmbitiTerritoriali getGregDAmbitiTerritoriali() {
		return this.gregDAmbitiTerritoriali;
	}

	public void setGregDAmbitiTerritoriali(GregDAmbitiTerritoriali gregDAmbitiTerritoriali) {
		this.gregDAmbitiTerritoriali = gregDAmbitiTerritoriali;
	}
	
	public GregDAsl getGregDAsl() {
		return this.gregDAsl;
	}

	public void setGregDAsl(GregDAsl gregDAsl) {
		this.gregDAsl = gregDAsl;
	}
	
	public GregTPresidenteEnteGestore getGregTPresidenteEnteGestore() {
		return this.gregTPresidenteEnteGestore;
	}

	public void setGregTPresidenteEnteGestore(GregTPresidenteEnteGestore gregTPresidenteEnteGestore) {
		this.gregTPresidenteEnteGestore = gregTPresidenteEnteGestore;
	}

	public GregDComuni getGregDComuni() {
		return this.gregDComuni;
	}

	public void setGregDComuni(GregDComuni gregDComuni) {
		this.gregDComuni = gregDComuni;
	}

	public GregDTipoEnte getGregDTipoEnte() {
		return this.gregDTipoEnte;
	}

	public void setGregDTipoEnte(GregDTipoEnte gregDTipoEnte) {
		this.gregDTipoEnte = gregDTipoEnte;
	}

	public GregTSchedeEntiGestori getGregTSchedeEntiGestori() {
		return this.gregTSchedeEntiGestori;
	}

	public void setGregTSchedeEntiGestori(GregTSchedeEntiGestori gregTSchedeEntiGestori) {
		this.gregTSchedeEntiGestori = gregTSchedeEntiGestori;
	}
	
	public GregRResponsabileContatti getGregRResponsabileContatti() {
		return this.gregRResponsabileContatti;
	}

	public void setGregRResponsabileContatti(GregRResponsabileContatti gregRResponsabileContatti) {
		this.gregRResponsabileContatti = gregRResponsabileContatti;
	}

}