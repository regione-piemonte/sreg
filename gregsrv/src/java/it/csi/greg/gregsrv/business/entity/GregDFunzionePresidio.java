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
 * The persistent class for the greg_d_funzione_presidio database table.
 * 
 */
@Entity
@Table(name="greg_d_funzione_presidio")
@NamedQuery(name="GregDFunzionePresidio.findAll", query="SELECT g FROM GregDFunzionePresidio g where g.dataCancellazione IS NULL")
public class GregDFunzionePresidio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_FUNZIONE_PRESIDIO_IDFUNZIONEPRESIDIO_GENERATOR", sequenceName="GREG_D_FUNZIONE_PRESIDIO_ID_FUNZIONE_PRESIDIO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_FUNZIONE_PRESIDIO_IDFUNZIONEPRESIDIO_GENERATOR")
	@Column(name="id_funzione_presidio")
	private Integer idFunzionePresidio;

	@Column(name="cod_funzione_presidio")
	private String codFunzionePresidio;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_funzione_presidio")
	private String desFunzionePresidio;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTNomenclatoreNazionale
	@JsonIgnore
	@OneToMany(mappedBy="gregDFunzionePresidio")
	private Set<GregTNomenclatoreNazionale> gregTNomenclatoreNazionales;

	public GregDFunzionePresidio() {
	}

	public Integer getIdFunzionePresidio() {
		return this.idFunzionePresidio;
	}

	public void setIdFunzionePresidio(Integer idFunzionePresidio) {
		this.idFunzionePresidio = idFunzionePresidio;
	}

	public String getCodFunzionePresidio() {
		return this.codFunzionePresidio;
	}

	public void setCodFunzionePresidio(String codFunzionePresidio) {
		this.codFunzionePresidio = codFunzionePresidio;
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

	public String getDesFunzionePresidio() {
		return this.desFunzionePresidio;
	}

	public void setDesFunzionePresidio(String desFunzionePresidio) {
		this.desFunzionePresidio = desFunzionePresidio;
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
		gregTNomenclatoreNazionale.setGregDFunzionePresidio(this);

		return gregTNomenclatoreNazionale;
	}

	public GregTNomenclatoreNazionale removeGregTNomenclatoreNazionale(GregTNomenclatoreNazionale gregTNomenclatoreNazionale) {
		getGregTNomenclatoreNazionales().remove(gregTNomenclatoreNazionale);
		gregTNomenclatoreNazionale.setGregDFunzionePresidio(null);

		return gregTNomenclatoreNazionale;
	}

}