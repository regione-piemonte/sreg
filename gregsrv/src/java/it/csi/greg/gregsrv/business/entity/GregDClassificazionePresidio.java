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
 * The persistent class for the greg_d_classificazione_presidio database table.
 * 
 */
@Entity
@Table(name="greg_d_classificazione_presidio")
@NamedQuery(name="GregDClassificazionePresidio.findAll", query="SELECT g FROM GregDClassificazionePresidio g where g.dataCancellazione IS NULL")
public class GregDClassificazionePresidio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_CLASSIFICAZIONE_PRESIDIO_IDCLASSIFICAZIONEPRESIDIO_GENERATOR", sequenceName="GREG_D_CLASSIFICAZIONE_PRESIDIO_ID_CLASSIFICAZIONE_PRESIDIO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_CLASSIFICAZIONE_PRESIDIO_IDCLASSIFICAZIONEPRESIDIO_GENERATOR")
	@Column(name="id_classificazione_presidio")
	private Integer idClassificazionePresidio;

	@Column(name="cod_classificazione_presidio")
	private String codClassificazionePresidio;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_classificazione_presidio")
	private String desClassificazionePresidio;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDClassificazionePresidio")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDClassificazionePresidio() {
	}

	public Integer getIdClassificazionePresidio() {
		return this.idClassificazionePresidio;
	}

	public void setIdClassificazionePresidio(Integer idClassificazionePresidio) {
		this.idClassificazionePresidio = idClassificazionePresidio;
	}

	public String getCodClassificazionePresidio() {
		return this.codClassificazionePresidio;
	}

	public void setCodClassificazionePresidio(String codClassificazionePresidio) {
		this.codClassificazionePresidio = codClassificazionePresidio;
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

	public String getDesClassificazionePresidio() {
		return this.desClassificazionePresidio;
	}

	public void setDesClassificazionePresidio(String desClassificazionePresidio) {
		this.desClassificazionePresidio = desClassificazionePresidio;
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
		gregTNomenclatoreNazionale.setGregDClassificazionePresidio(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDClassificazionePresidio(null);

		return gregTNomenclatoreNazionale;
	}

}