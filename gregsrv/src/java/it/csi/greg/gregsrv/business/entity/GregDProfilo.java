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
 * The persistent class for the greg_d_profilo database table.
 * 
 */
@Entity
@Table(name="greg_d_profilo")
@NamedQuery(name="GregDProfilo.findAll", query="SELECT g FROM GregDProfilo g where g.dataCancellazione IS NULL")
public class GregDProfilo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_PROFILO_IDPROFILO_GENERATOR", sequenceName="GREG_D_PROFILO_ID_PROFILO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_PROFILO_IDPROFILO_GENERATOR")
	@Column(name="id_profilo")
	private Integer idProfilo;

	@Column(name="cod_profilo")
	private String codProfilo;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_profilo")
	private String descProfilo;

	@Column(name="info_profilo")
	private String infoProfilo;

	@Column(name="utente_operazione")
	private String utenteOperazione;
	
	@Column(name="tipo_profilo")
	private String tipoProfilo;

	//bi-directional many-to-one association to GregRProfiloAzione
	@JsonIgnore
	@OneToMany(mappedBy="gregDProfilo")
	private Set<GregRProfiloAzione> gregRProfiloAziones;

	//bi-directional many-to-one association to GregRUserProfilo
	@JsonIgnore
	@OneToMany(mappedBy="gregDProfilo")
	private Set<GregRUserProfilo> gregRUserProfilos;

	public GregDProfilo() {
	}

	public Integer getIdProfilo() {
		return this.idProfilo;
	}

	public void setIdProfilo(Integer idProfilo) {
		this.idProfilo = idProfilo;
	}

	public String getCodProfilo() {
		return this.codProfilo;
	}

	public void setCodProfilo(String codProfilo) {
		this.codProfilo = codProfilo;
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

	public String getDescProfilo() {
		return this.descProfilo;
	}

	public void setDescProfilo(String descProfilo) {
		this.descProfilo = descProfilo;
	}

	public String getInfoProfilo() {
		return this.infoProfilo;
	}

	public void setInfoProfilo(String infoProfilo) {
		this.infoProfilo = infoProfilo;
	}

	public String getTipoProfilo() {
		return this.tipoProfilo;
	}

	public void setTipoProfilo(String tipoProfilo) {
		this.tipoProfilo = tipoProfilo;
	}
	
	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRProfiloAzione> getGregRProfiloAziones() {
		return this.gregRProfiloAziones;
	}

	public void setGregRProfiloAziones(Set<GregRProfiloAzione> gregRProfiloAziones) {
		this.gregRProfiloAziones = gregRProfiloAziones;
	}

	public GregRProfiloAzione addGregRProfiloAzione(GregRProfiloAzione gregRProfiloAzione) {
		getGregRProfiloAziones().add(gregRProfiloAzione);
		gregRProfiloAzione.setGregDProfilo(this);

		return gregRProfiloAzione;
	}

	public GregRProfiloAzione removeGregRProfiloAzione(GregRProfiloAzione gregRProfiloAzione) {
		getGregRProfiloAziones().remove(gregRProfiloAzione);
		gregRProfiloAzione.setGregDProfilo(null);

		return gregRProfiloAzione;
	}

	public Set<GregRUserProfilo> getGregRUserProfilos() {
		return this.gregRUserProfilos;
	}

	public void setGregRUserProfilos(Set<GregRUserProfilo> gregRUserProfilos) {
		this.gregRUserProfilos = gregRUserProfilos;
	}

	public GregRUserProfilo addGregRUserProfilo(GregRUserProfilo gregRUserProfilo) {
		getGregRUserProfilos().add(gregRUserProfilo);
		gregRUserProfilo.setGregDProfilo(this);

		return gregRUserProfilo;
	}

	public GregRUserProfilo removeGregRUserProfilo(GregRUserProfilo gregRUserProfilo) {
		getGregRUserProfilos().remove(gregRUserProfilo);
		gregRUserProfilo.setGregDProfilo(null);

		return gregRUserProfilo;
	}

}