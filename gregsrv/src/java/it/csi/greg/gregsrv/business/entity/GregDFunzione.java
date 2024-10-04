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
 * The persistent class for the greg_d_funzione database table.
 * 
 */
@Entity
@Table(name="greg_d_funzione")
@NamedQuery(name="GregDFunzione.findAll", query="SELECT g FROM GregDFunzione g where g.dataCancellazione IS NULL")
public class GregDFunzione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_FUNZIONE_IDFUNZIONE_GENERATOR", sequenceName="GREG_D_FUNZIONE_ID_FUNZIONE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_FUNZIONE_IDFUNZIONE_GENERATOR")
	@Column(name="id_funzione")
	private Integer idFunzione;

	@Column(name="cod_funzione")
	private String codFunzione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_funzione")
	private String descFunzione;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDAttivita
	@JsonIgnore
	@OneToMany(mappedBy="gregDFunzione")
	private Set<GregDAttivita> gregDAttivitas;

	public GregDFunzione() {
	}

	public Integer getIdFunzione() {
		return this.idFunzione;
	}

	public void setIdFunzione(Integer idFunzione) {
		this.idFunzione = idFunzione;
	}

	public String getCodFunzione() {
		return this.codFunzione;
	}

	public void setCodFunzione(String codFunzione) {
		this.codFunzione = codFunzione;
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

	public String getDescFunzione() {
		return this.descFunzione;
	}

	public void setDescFunzione(String descFunzione) {
		this.descFunzione = descFunzione;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDAttivita> getGregDAttivitas() {
		return this.gregDAttivitas;
	}

	public void setGregDAttivitas(Set<GregDAttivita> gregDAttivitas) {
		this.gregDAttivitas = gregDAttivitas;
	}

	public GregDAttivita addGregDAttivita(GregDAttivita gregDAttivita) {
		getGregDAttivitas().add(gregDAttivita);
		gregDAttivita.setGregDFunzione(this);

		return gregDAttivita;
	}

	public GregDAttivita removeGregDAttivita(GregDAttivita gregDAttivita) {
		getGregDAttivitas().remove(gregDAttivita);
		gregDAttivita.setGregDFunzione(null);

		return gregDAttivita;
	}

}