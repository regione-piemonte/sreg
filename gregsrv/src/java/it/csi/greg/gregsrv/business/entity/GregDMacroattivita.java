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
 * The persistent class for the greg_d_macroattivita database table.
 * 
 */
@Entity
@Table(name="greg_d_macroattivita")
@NamedQuery(name="GregDMacroattivita.findAll", query="SELECT g FROM GregDMacroattivita g where g.dataCancellazione IS NULL")
public class GregDMacroattivita implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_MACROATTIVITA_IDMACROATTIVITA_GENERATOR", sequenceName="GREG_D_MACROATTIVITA_ID_MACROATTIVITA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_MACROATTIVITA_IDMACROATTIVITA_GENERATOR")
	@Column(name="id_macroattivita")
	private Integer idMacroattivita;

	@Column(name="cod_macroattivita")
	private String codMacroattivita;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_macroattivita")
	private String descMacroattivita;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTPrestazioniMinisteriali
	@JsonIgnore
	@OneToMany(mappedBy="gregDMacroattivita")
	private Set<GregTPrestazioniMinisteriali> gregTPrestazioniMinisterialis;

	public GregDMacroattivita() {
	}

	public Integer getIdMacroattivita() {
		return this.idMacroattivita;
	}

	public void setIdMacroattivita(Integer idMacroattivita) {
		this.idMacroattivita = idMacroattivita;
	}

	public String getCodMacroattivita() {
		return this.codMacroattivita;
	}

	public void setCodMacroattivita(String codMacroattivita) {
		this.codMacroattivita = codMacroattivita;
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

	public String getDescMacroattivita() {
		return this.descMacroattivita;
	}

	public void setDescMacroattivita(String descMacroattivita) {
		this.descMacroattivita = descMacroattivita;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregTPrestazioniMinisteriali> getGregTPrestazioniMinisterialis() {
		return this.gregTPrestazioniMinisterialis;
	}

	public void setGregTPrestazioniMinisterialis(Set<GregTPrestazioniMinisteriali> gregTPrestazioniMinisterialis) {
		this.gregTPrestazioniMinisterialis = gregTPrestazioniMinisterialis;
	}

	public GregTPrestazioniMinisteriali addGregTPrestazioniMinisteriali(GregTPrestazioniMinisteriali gregTPrestazioniMinisteriali) {
		getGregTPrestazioniMinisterialis().add(gregTPrestazioniMinisteriali);
		gregTPrestazioniMinisteriali.setGregDMacroattivita(this);

		return gregTPrestazioniMinisteriali;
	}

	public GregTPrestazioniMinisteriali removeGregTPrestazioniMinisteriali(GregTPrestazioniMinisteriali gregTPrestazioniMinisteriali) {
		getGregTPrestazioniMinisterialis().remove(gregTPrestazioniMinisteriali);
		gregTPrestazioniMinisteriali.setGregDMacroattivita(null);

		return gregTPrestazioniMinisteriali;
	}

}