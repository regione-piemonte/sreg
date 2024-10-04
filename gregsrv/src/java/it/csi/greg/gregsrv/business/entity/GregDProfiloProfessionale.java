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
 * The persistent class for the greg_d_profilo_professionale database table.
 * 
 */
@Entity
@Table(name="greg_d_profilo_professionale")
@NamedQuery(name="GregDProfiloProfessionale.findAll", query="SELECT g FROM GregDProfiloProfessionale g where g.dataCancellazione IS NULL")
public class GregDProfiloProfessionale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_PROFILO_PROFESSIONALE_IDPROFILOPROFESSIONALE_GENERATOR", sequenceName="GREG_D_PROFILO_PROFESSIONALE_ID_PROFILO_PROFESSIONALE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_PROFILO_PROFESSIONALE_IDPROFILOPROFESSIONALE_GENERATOR")
	@Column(name="id_profilo_professionale")
	private Integer idProfiloProfessionale;

	@Column(name="cod_profilo_professionale")
	private String codProfiloProfessionale;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_profilo_professionale")
	private String descProfiloProfessionale;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRConteggioMonteOreSett
	@JsonIgnore
	@OneToMany(mappedBy="gregDProfiloProfessionale")
	private Set<GregRConteggioMonteOreSett> gregRConteggioMonteOreSetts;

	//bi-directional many-to-one association to GregRConteggioPersonale
	@JsonIgnore
	@OneToMany(mappedBy="gregDProfiloProfessionale")
	private Set<GregRConteggioPersonale> gregRConteggioPersonales;

	public GregDProfiloProfessionale() {
	}

	public Integer getIdProfiloProfessionale() {
		return this.idProfiloProfessionale;
	}

	public void setIdProfiloProfessionale(Integer idProfiloProfessionale) {
		this.idProfiloProfessionale = idProfiloProfessionale;
	}

	public String getCodProfiloProfessionale() {
		return this.codProfiloProfessionale;
	}

	public void setCodProfiloProfessionale(String codProfiloProfessionale) {
		this.codProfiloProfessionale = codProfiloProfessionale;
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

	public String getDescProfiloProfessionale() {
		return this.descProfiloProfessionale;
	}

	public void setDescProfiloProfessionale(String descProfiloProfessionale) {
		this.descProfiloProfessionale = descProfiloProfessionale;
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
		gregRConteggioMonteOreSett.setGregDProfiloProfessionale(this);

		return gregRConteggioMonteOreSett;
	}

	public GregRConteggioMonteOreSett removeGregRConteggioMonteOreSett(GregRConteggioMonteOreSett gregRConteggioMonteOreSett) {
		getGregRConteggioMonteOreSetts().remove(gregRConteggioMonteOreSett);
		gregRConteggioMonteOreSett.setGregDProfiloProfessionale(null);

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
		gregRConteggioPersonale.setGregDProfiloProfessionale(this);

		return gregRConteggioPersonale;
	}

	public GregRConteggioPersonale removeGregRConteggioPersonale(GregRConteggioPersonale gregRConteggioPersonale) {
		getGregRConteggioPersonales().remove(gregRConteggioPersonale);
		gregRConteggioPersonale.setGregDProfiloProfessionale(null);

		return gregRConteggioPersonale;
	}

}