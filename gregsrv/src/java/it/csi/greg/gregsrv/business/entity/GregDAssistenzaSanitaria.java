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
 * The persistent class for the greg_d_assistenza_sanitaria database table.
 * 
 */
@Entity
@Table(name="greg_d_assistenza_sanitaria")
@NamedQuery(name="GregDAssistenzaSanitaria.findAll", query="SELECT g FROM GregDAssistenzaSanitaria g where g.dataCancellazione IS NULL")
public class GregDAssistenzaSanitaria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_ASSISTENZA_SANITARIA_IDASSISTENZASANITARIA_GENERATOR", sequenceName="GREG_D_ASSISTENZA_SANITARIA_ID_ASSISTENZA_SANITARIA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_ASSISTENZA_SANITARIA_IDASSISTENZASANITARIA_GENERATOR")
	@Column(name="id_assistenza_sanitaria")
	private Integer idAssistenzaSanitaria;

	@Column(name="cod_assistenza_sanitaria")
	private String codAssistenzaSanitaria;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_assistenza_sanitaria")
	private String desAssistenzaSanitaria;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDAssistenzaSanitaria")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDAssistenzaSanitaria() {
	}

	public Integer getIdAssistenzaSanitaria() {
		return this.idAssistenzaSanitaria;
	}

	public void setIdAssistenzaSanitaria(Integer idAssistenzaSanitaria) {
		this.idAssistenzaSanitaria = idAssistenzaSanitaria;
	}

	public String getCodAssistenzaSanitaria() {
		return this.codAssistenzaSanitaria;
	}

	public void setCodAssistenzaSanitaria(String codAssistenzaSanitaria) {
		this.codAssistenzaSanitaria = codAssistenzaSanitaria;
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

	public String getDesAssistenzaSanitaria() {
		return this.desAssistenzaSanitaria;
	}

	public void setDesAssistenzaSanitaria(String desAssistenzaSanitaria) {
		this.desAssistenzaSanitaria = desAssistenzaSanitaria;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregTNomenclatoreNazionale> getGregTNomenclatoreNazionales() {
		return this.gregTNomenclatoreNazionales;
	}

	public void setGregTNomenclatoreNazionales(Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales) {
		this.gregTNomenclatoreNazionales = gregTNomenclatoreNazionales;
	}

	public GregTNomenclatoreNazionale addGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().add(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDAssistenzaSanitaria(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDAssistenzaSanitaria(null);

		return gregTNomenclatoreNazionale;
	}

}