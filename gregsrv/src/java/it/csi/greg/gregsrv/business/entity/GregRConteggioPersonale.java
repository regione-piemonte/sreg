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
 * The persistent class for the greg_r_conteggio_personale database table.
 * 
 */
@Entity
@Table(name="greg_r_conteggio_personale")
@NamedQuery(name="GregRConteggioPersonale.findAll", query="SELECT g FROM GregRConteggioPersonale g where g.dataCancellazione IS NULL")
public class GregRConteggioPersonale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_CONTEGGIO_PERSONALE_IDCONTEGGIOPERSONALE_GENERATOR", sequenceName="GREG_R_CONTEGGIO_PERSONALE_ID_CONTEGGIO_PERSONALE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_CONTEGGIO_PERSONALE_IDCONTEGGIOPERSONALE_GENERATOR")
	@Column(name="id_conteggio_personale")
	private Integer idConteggioPersonale;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDPersonaleEnte
	@ManyToOne
	@JoinColumn(name="id_personale_ente")
	private GregDPersonaleEnte gregDPersonaleEnte;

	//bi-directional many-to-one association to GregDProfiloProfessionale
	@ManyToOne
	@JoinColumn(name="id_profilo_professionale")
	private GregDProfiloProfessionale gregDProfiloProfessionale;

	//bi-directional many-to-one association to GregRRendicontazioneModFParte1
	@JsonIgnore
	@OneToMany(mappedBy="gregRConteggioPersonale")
	private Set<GregRRendicontazioneModFParte1> gregRRendicontazioneModFParte1s;

	public GregRConteggioPersonale() {
	}

	public Integer getIdConteggioPersonale() {
		return this.idConteggioPersonale;
	}

	public void setIdConteggioPersonale(Integer idConteggioPersonale) {
		this.idConteggioPersonale = idConteggioPersonale;
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

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDPersonaleEnte getGregDPersonaleEnte() {
		return this.gregDPersonaleEnte;
	}

	public void setGregDPersonaleEnte(GregDPersonaleEnte gregDPersonaleEnte) {
		this.gregDPersonaleEnte = gregDPersonaleEnte;
	}

	public GregDProfiloProfessionale getGregDProfiloProfessionale() {
		return this.gregDProfiloProfessionale;
	}

	public void setGregDProfiloProfessionale(GregDProfiloProfessionale gregDProfiloProfessionale) {
		this.gregDProfiloProfessionale = gregDProfiloProfessionale;
	}

	public Set<GregRRendicontazioneModFParte1> getGregRRendicontazioneModFParte1s() {
		return this.gregRRendicontazioneModFParte1s;
	}

	public void setGregRRendicontazioneModFParte1s(Set<GregRRendicontazioneModFParte1> gregRRendicontazioneModFParte1s) {
		this.gregRRendicontazioneModFParte1s = gregRRendicontazioneModFParte1s;
	}

	public GregRRendicontazioneModFParte1 addGregRRendicontazioneModFParte1(GregRRendicontazioneModFParte1 gregRRendicontazioneModFParte1) {
		getGregRRendicontazioneModFParte1s().add(gregRRendicontazioneModFParte1);
		gregRRendicontazioneModFParte1.setGregRConteggioPersonale(this);

		return gregRRendicontazioneModFParte1;
	}

	public GregRRendicontazioneModFParte1 removeGregRRendicontazioneModFParte1(GregRRendicontazioneModFParte1 gregRRendicontazioneModFParte1) {
		getGregRRendicontazioneModFParte1s().remove(gregRRendicontazioneModFParte1);
		gregRRendicontazioneModFParte1.setGregRConteggioPersonale(null);

		return gregRRendicontazioneModFParte1;
	}

}