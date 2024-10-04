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
 * The persistent class for the greg_d_colori database table.
 * 
 */
@Entity
@Table(name="greg_d_colori")
@NamedQuery(name="GregDColori.findAll", query="SELECT g FROM GregDColori g WHERE g.dataCancellazione is null ")
public class GregDColori implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_COLORI_IDCOLORE_GENERATOR", sequenceName="GREG_D_COLORI_ID_COLORE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_COLORI_IDCOLORE_GENERATOR")
	@Column(name="id_colore")
	private Integer idColore;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_colore")
	private String desColore;

	private String rgb;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDProgrammaMissione
	@JsonIgnore
	@OneToMany(mappedBy="gregDColori")
	private Set<GregDProgrammaMissione> gregDProgrammaMissiones;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1
	@JsonIgnore
	@OneToMany(mappedBy="gregDColori")
	private Set<GregRPrestReg1UtenzeRegionali1> gregRPrestReg1UtenzeRegionali1s;

	public GregDColori() {
	}

	public Integer getIdColore() {
		return this.idColore;
	}

	public void setIdColore(Integer idColore) {
		this.idColore = idColore;
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

	public String getDesColore() {
		return this.desColore;
	}

	public void setDesColore(String desColore) {
		this.desColore = desColore;
	}

	public String getRgb() {
		return this.rgb;
	}

	public void setRgb(String rgb) {
		this.rgb = rgb;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDProgrammaMissione> getGregDProgrammaMissiones() {
		return this.gregDProgrammaMissiones;
	}

	public void setGregDProgrammaMissiones(Set<GregDProgrammaMissione> gregDProgrammaMissiones) {
		this.gregDProgrammaMissiones = gregDProgrammaMissiones;
	}

	public GregDProgrammaMissione addGregDProgrammaMissione(GregDProgrammaMissione gregDProgrammaMissione) {
		getGregDProgrammaMissiones().add(gregDProgrammaMissione);
		gregDProgrammaMissione.setGregDColori(this);

		return gregDProgrammaMissione;
	}

	public GregDProgrammaMissione removeGregDProgrammaMissione(GregDProgrammaMissione gregDProgrammaMissione) {
		getGregDProgrammaMissiones().remove(gregDProgrammaMissione);
		gregDProgrammaMissione.setGregDColori(null);

		return gregDProgrammaMissione;
	}

	public Set<GregRPrestReg1UtenzeRegionali1> getGregRPrestReg1UtenzeRegionali1s() {
		return this.gregRPrestReg1UtenzeRegionali1s;
	}

	public void setGregRPrestReg1UtenzeRegionali1s(Set<GregRPrestReg1UtenzeRegionali1> gregRPrestReg1UtenzeRegionali1s) {
		this.gregRPrestReg1UtenzeRegionali1s = gregRPrestReg1UtenzeRegionali1s;
	}

	public GregRPrestReg1UtenzeRegionali1 addGregRPrestReg1UtenzeRegionali1(GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1) {
		getGregRPrestReg1UtenzeRegionali1s().add(gregRPrestReg1UtenzeRegionali1);
		gregRPrestReg1UtenzeRegionali1.setGregDColori(this);

		return gregRPrestReg1UtenzeRegionali1;
	}

	public GregRPrestReg1UtenzeRegionali1 removeGregRPrestReg1UtenzeRegionali1(GregRPrestReg1UtenzeRegionali1 gregRPrestReg1UtenzeRegionali1) {
		getGregRPrestReg1UtenzeRegionali1s().remove(gregRPrestReg1UtenzeRegionali1);
		gregRPrestReg1UtenzeRegionali1.setGregDColori(null);

		return gregRPrestReg1UtenzeRegionali1;
	}

}