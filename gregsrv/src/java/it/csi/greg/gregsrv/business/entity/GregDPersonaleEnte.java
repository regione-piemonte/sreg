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
 * The persistent class for the greg_d_personale_ente database table.
 * 
 */
@Entity
@Table(name="greg_d_personale_ente")
@NamedQuery(name="GregDPersonaleEnte.findAll", query="SELECT g FROM GregDPersonaleEnte g where g.dataCancellazione IS NULL")
public class GregDPersonaleEnte implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_PERSONALE_ENTE_IDPERSONALEENTE_GENERATOR", sequenceName="GREG_D_PERSONALE_ENTE_ID_PERSONALE_ENTE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_PERSONALE_ENTE_IDPERSONALEENTE_GENERATOR")
	@Column(name="id_personale_ente")
	private Integer idPersonaleEnte;

	@Column(name="cod_personale_ente")
	private String codPersonaleEnte;

	@Column(name="conteggio_personale")
	private Boolean conteggioPersonale;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_personale_ente")
	private String descPersonaleEnte;

	@Column(name="dipendente_ente")
	private Boolean dipendenteEnte;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRConteggioMonteOreSett
	@JsonIgnore
	@OneToMany(mappedBy="gregDPersonaleEnte")
	private Set<GregRConteggioMonteOreSett> gregRConteggioMonteOreSetts;

	//bi-directional many-to-one association to GregRConteggioPersonale
	@JsonIgnore
	@OneToMany(mappedBy="gregDPersonaleEnte")
	private Set<GregRConteggioPersonale> gregRConteggioPersonales;

	public GregDPersonaleEnte() {
	}

	public Integer getIdPersonaleEnte() {
		return this.idPersonaleEnte;
	}

	public void setIdPersonaleEnte(Integer idPersonaleEnte) {
		this.idPersonaleEnte = idPersonaleEnte;
	}

	public String getCodPersonaleEnte() {
		return this.codPersonaleEnte;
	}

	public void setCodPersonaleEnte(String codPersonaleEnte) {
		this.codPersonaleEnte = codPersonaleEnte;
	}

	public Boolean getConteggioPersonale() {
		return this.conteggioPersonale;
	}

	public void setConteggioPersonale(Boolean conteggioPersonale) {
		this.conteggioPersonale = conteggioPersonale;
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

	public String getDescPersonaleEnte() {
		return this.descPersonaleEnte;
	}

	public void setDescPersonaleEnte(String descPersonaleEnte) {
		this.descPersonaleEnte = descPersonaleEnte;
	}

	public Boolean getDipendenteEnte() {
		return this.dipendenteEnte;
	}

	public void setDipendenteEnte(Boolean dipendenteEnte) {
		this.dipendenteEnte = dipendenteEnte;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRConteggioMonteOreSett> getGregRConteggioMonteOreSetts() {
		return this.gregRConteggioMonteOreSetts;
	}

	public void setGregRConteggioMonteOreSetts(Set<GregRConteggioMonteOreSett> gregRConteggioMonteOreSetts) {
		this.gregRConteggioMonteOreSetts = gregRConteggioMonteOreSetts;
	}

	public GregRConteggioMonteOreSett addGregRConteggioMonteOreSett(GregRConteggioMonteOreSett gregRConteggioMonteOreSett) {
		getGregRConteggioMonteOreSetts().add(gregRConteggioMonteOreSett);
		gregRConteggioMonteOreSett.setGregDPersonaleEnte(this);

		return gregRConteggioMonteOreSett;
	}

	public GregRConteggioMonteOreSett removeGregRConteggioMonteOreSett(GregRConteggioMonteOreSett gregRConteggioMonteOreSett) {
		getGregRConteggioMonteOreSetts().remove(gregRConteggioMonteOreSett);
		gregRConteggioMonteOreSett.setGregDPersonaleEnte(null);

		return gregRConteggioMonteOreSett;
	}

	public Set<GregRConteggioPersonale> getGregRConteggioPersonales() {
		return this.gregRConteggioPersonales;
	}

	public void setGregRConteggioPersonales(Set<GregRConteggioPersonale> gregRConteggioPersonales) {
		this.gregRConteggioPersonales = gregRConteggioPersonales;
	}

	public GregRConteggioPersonale addGregRConteggioPersonale(GregRConteggioPersonale gregRConteggioPersonale) {
		getGregRConteggioPersonales().add(gregRConteggioPersonale);
		gregRConteggioPersonale.setGregDPersonaleEnte(this);

		return gregRConteggioPersonale;
	}

	public GregRConteggioPersonale removeGregRConteggioPersonale(GregRConteggioPersonale gregRConteggioPersonale) {
		getGregRConteggioPersonales().remove(gregRConteggioPersonale);
		gregRConteggioPersonale.setGregDPersonaleEnte(null);

		return gregRConteggioPersonale;
	}

}