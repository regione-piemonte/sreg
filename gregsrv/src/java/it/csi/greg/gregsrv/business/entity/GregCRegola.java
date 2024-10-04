/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;

/**
 * The persistent class for the greg_c_regola database table.
 * 
 */
@Entity
@Table(name = "greg_c_regola")
@NamedQuery(name = "GregCRegola.findAll", query = "SELECT g FROM GregCRegola g")
public class GregCRegola implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "GREG_C_REGOLA_IDREGOLA_GENERATOR", sequenceName = "GREG_C_REGOLA_ID_REGOLA_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GREG_C_REGOLA_IDREGOLA_GENERATOR")
	@Column(name = "id_regola")
	private Integer idRegola;

	@Column(name = "codice_regola")
	private String codiceRegola;

	@Column(name = "descrizione_regola")
	private String descrizioneRegola;

	@Column(name = "funzione1_regola")
	private String funzione1Regola;

	@Column(name = "funzione2_regola")
	private String funzione2Regola;

	@Column(name = "script_regola")
	private String scriptRegola;

	// bi-directional many-to-one association to GregRPrestReg1PrestMinist
	@JsonIgnore
	@OneToMany(mappedBy = "gregCRegola")
	private Set<GregRPrestReg1PrestMinist> gregRPrestReg1PrestMinists;

	// bi-directional many-to-one association to GregRRendicontazioneFondi

	@OneToMany(mappedBy = "gregCRegola")
	private Set<GregRRendicontazioneFondi> gregRRendicontazioneFondis;

	public GregCRegola() {
	}

	public Integer getIdRegola() {
		return this.idRegola;
	}

	public void setIdRegola(Integer idRegola) {
		this.idRegola = idRegola;
	}

	public String getCodiceRegola() {
		return this.codiceRegola;
	}

	public void setCodiceRegola(String codiceRegola) {
		this.codiceRegola = codiceRegola;
	}

	public String getDescrizioneRegola() {
		return this.descrizioneRegola;
	}

	public void setDescrizioneRegola(String descrizioneRegola) {
		this.descrizioneRegola = descrizioneRegola;
	}

	public String getFunzione1Regola() {
		return this.funzione1Regola;
	}

	public void setFunzione1Regola(String funzione1Regola) {
		this.funzione1Regola = funzione1Regola;
	}

	public String getFunzione2Regola() {
		return this.funzione2Regola;
	}

	public void setFunzione2Regola(String funzione2Regola) {
		this.funzione2Regola = funzione2Regola;
	}

	public String getScriptRegola() {
		return this.scriptRegola;
	}

	public void setScriptRegola(String scriptRegola) {
		this.scriptRegola = scriptRegola;
	}

	public Set<GregRPrestReg1PrestMinist> getGregRPrestReg1PrestMinists() {
		return this.gregRPrestReg1PrestMinists;
	}

	public void setGregRPrestReg1PrestMinists(Set<GregRPrestReg1PrestMinist> gregRPrestReg1PrestMinists) {
		this.gregRPrestReg1PrestMinists = gregRPrestReg1PrestMinists;
	}

	public GregRPrestReg1PrestMinist addGregRPrestReg1PrestMinist(GregRPrestReg1PrestMinist gregRPrestReg1PrestMinist) {
		getGregRPrestReg1PrestMinists().add(gregRPrestReg1PrestMinist);
		gregRPrestReg1PrestMinist.setGregCRegola(this);

		return gregRPrestReg1PrestMinist;
	}

	public GregRPrestReg1PrestMinist removeGregRPrestReg1PrestMinist(
			GregRPrestReg1PrestMinist gregRPrestReg1PrestMinist) {
		getGregRPrestReg1PrestMinists().remove(gregRPrestReg1PrestMinist);
		gregRPrestReg1PrestMinist.setGregCRegola(null);

		return gregRPrestReg1PrestMinist;
	}

	public Set<GregRRendicontazioneFondi> getGregRRendicontazioneFondis() {
		return this.gregRRendicontazioneFondis;
	}

	public void setGregRRendicontazioneFondis(Set<GregRRendicontazioneFondi> gregRRendicontazioneFondis) {
		this.gregRRendicontazioneFondis = gregRRendicontazioneFondis;
	}

	public GregRRendicontazioneFondi addGregRRendicontazioneFondi(GregRRendicontazioneFondi gregRRendicontazioneFondi) {
		getGregRRendicontazioneFondis().add(gregRRendicontazioneFondi);
		gregRRendicontazioneFondi.setGregCRegola(this);

		return gregRRendicontazioneFondi;
	}

	public GregRRendicontazioneFondi removeGregRRendicontazioneFondi(
			GregRRendicontazioneFondi gregRRendicontazioneFondi) {
		getGregRRendicontazioneFondis().remove(gregRRendicontazioneFondi);
		gregRRendicontazioneFondi.setGregCRegola(null);

		return gregRRendicontazioneFondi;
	}

}