/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.csi.greg.gregsrv.business.entity.GregDAsl;

import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_d_comuni database table.
 * 
 */
@Entity
@Table(name="greg_d_comuni")
@NamedQueries({
    @NamedQuery(name="GregDComuni.findAll", query="SELECT g FROM GregDComuni g WHERE g.dataCancellazione IS NULL ORDER BY g.desComune"),
	@NamedQuery(name="GregDComuni.findById", query="SELECT g FROM GregDComuni g WHERE g.idComune = :idComune and g.dataCancellazione is null  ORDER BY g.desComune"),
	@NamedQuery(name="GregDComuni.findByIdNotDeleted", query="SELECT g FROM GregDComuni g WHERE g.idComune = :idComune "
			+ "AND g.dataCancellazione IS NULL ORDER BY g.desComune"),
	@NamedQuery(name="GregDComuni.findByCod", query="SELECT g FROM GregDComuni g WHERE g.codIstatComune = :codComune "
			+ "AND g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDComuni.findAllNotDeleted", query="SELECT g FROM GregDComuni g WHERE "
			+ "g.gregDProvince.gregDRegione.codRegione = :codregione and "
			+ "((g.inizioValiditaComune <= :dataValidita and g.fineValiditaComune  >= :dataValidita) or "
			+ "(g.inizioValiditaComune <= :dataValidita and g.fineValiditaComune is null)) "
			+ "and g.dataCancellazione IS NULL ORDER BY g.desComune"),
    @NamedQuery(name="GregDComuni.findAllStorico", query="SELECT g FROM GregDComuni g WHERE "
			+ "g.dataCancellazione IS NULL ORDER BY g.desComune")
})
public class GregDComuni implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_COMUNI_IDCOMUNE_GENERATOR", sequenceName="GREG_D_COMUNI_ID_COMUNE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_COMUNI_IDCOMUNE_GENERATOR")
	@Column(name="id_comune")
	private Integer idComune;

	@Column(name="cod_istat_comune")
	private String codIstatComune;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_comune")
	private String desComune;

	@Column(name="utente_operazione")
	private String utenteOperazione;
	
	@Column(name="fine_validita_comune")
	private Timestamp fineValiditaComune;

	@Column(name="inizio_validita_comune")
	private Timestamp inizioValiditaComune;

	//bi-directional many-to-one association to GregDDistretti
	@ManyToOne
	@JoinColumn(name="id_distretto")
	private GregDDistretti gregDDistretti;

	//bi-directional many-to-one association to GregDProvince
	@ManyToOne
	@JoinColumn(name="id_provincia")
	private GregDProvince gregDProvince;

	//bi-directional many-to-one association to GregRRendicontazioneComuneEnteModA2
	@JsonIgnore
	@OneToMany(mappedBy="gregDComuni")
	private Set<GregRRendicontazioneComuneEnteModA2> gregRRendicontazioneComuneEnteModA2s;

	//bi-directional many-to-one association to GregRRendicontazioneEnteComuneModA2
	@JsonIgnore
	@OneToMany(mappedBy="gregDComuni")
	private Set<GregRRendicontazioneEnteComuneModA2> gregRRendicontazioneEnteComuneModA2s;

	//bi-directional many-to-one association to GregRRendicontazioneModA1
	@JsonIgnore
	@OneToMany(mappedBy="gregDComuni")
	private Set<GregRRendicontazioneModA1> gregRRendicontazioneModA1s;

	//bi-directional many-to-one association to GregRRendicontazioneModE
	@JsonIgnore
	@OneToMany(mappedBy="gregDComuni")
	private Set<GregRRendicontazioneModE> gregRRendicontazioneModEs;

	//bi-directional many-to-one association to GregRSchedeEntiGestoriComuni
	@JsonIgnore
	@OneToMany(mappedBy="gregDComuni")
	private Set<GregRSchedeEntiGestoriComuni> gregRSchedeEntiGestoriComunis;

	//bi-directional many-to-one association to GregREnteGestoreContatti
	@JsonIgnore
	@OneToMany(mappedBy="gregDComuni")
	private Set<GregREnteGestoreContatti> gregREnteGestoreContattis;
	
	//bi-directional many-to-one association to GregDAsl
	@ManyToOne
	@JoinColumn(name="id_asl")
	private GregDAsl gregDAsl;

	public GregDComuni() {
	}

	public Integer getIdComune() {
		return this.idComune;
	}

	public void setIdComune(Integer idComune) {
		this.idComune = idComune;
	}

	public String getCodIstatComune() {
		return this.codIstatComune;
	}

	public void setCodIstatComune(String codIstatComune) {
		this.codIstatComune = codIstatComune;
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

	public String getDesComune() {
		return this.desComune;
	}

	public void setDesComune(String desComune) {
		this.desComune = desComune;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDDistretti getGregDDistretti() {
		return this.gregDDistretti;
	}

	public void setGregDDistretti(GregDDistretti gregDDistretti) {
		this.gregDDistretti = gregDDistretti;
	}

	public GregDProvince getGregDProvince() {
		return this.gregDProvince;
	}

	public void setGregDProvince(GregDProvince gregDProvince) {
		this.gregDProvince = gregDProvince;
	}

	public Set<GregRRendicontazioneComuneEnteModA2> getGregRRendicontazioneComuneEnteModA2s() {
		return this.gregRRendicontazioneComuneEnteModA2s;
	}

	public void setGregRRendicontazioneComuneEnteModA2s(Set<GregRRendicontazioneComuneEnteModA2> gregRRendicontazioneComuneEnteModA2s) {
		this.gregRRendicontazioneComuneEnteModA2s = gregRRendicontazioneComuneEnteModA2s;
	}

	public GregRRendicontazioneComuneEnteModA2 addGregRRendicontazioneComuneEnteModA2(GregRRendicontazioneComuneEnteModA2 gregRRendicontazioneComuneEnteModA2) {
		getGregRRendicontazioneComuneEnteModA2s().add(gregRRendicontazioneComuneEnteModA2);
		gregRRendicontazioneComuneEnteModA2.setGregDComuni(this);

		return gregRRendicontazioneComuneEnteModA2;
	}

	public GregRRendicontazioneComuneEnteModA2 removeGregRRendicontazioneComuneEnteModA2(GregRRendicontazioneComuneEnteModA2 gregRRendicontazioneComuneEnteModA2) {
		getGregRRendicontazioneComuneEnteModA2s().remove(gregRRendicontazioneComuneEnteModA2);
		gregRRendicontazioneComuneEnteModA2.setGregDComuni(null);

		return gregRRendicontazioneComuneEnteModA2;
	}

	public Set<GregRRendicontazioneEnteComuneModA2> getGregRRendicontazioneEnteComuneModA2s() {
		return this.gregRRendicontazioneEnteComuneModA2s;
	}

	public void setGregRRendicontazioneEnteComuneModA2s(Set<GregRRendicontazioneEnteComuneModA2> gregRRendicontazioneEnteComuneModA2s) {
		this.gregRRendicontazioneEnteComuneModA2s = gregRRendicontazioneEnteComuneModA2s;
	}

	public GregRRendicontazioneEnteComuneModA2 addGregRRendicontazioneEnteComuneModA2(GregRRendicontazioneEnteComuneModA2 gregRRendicontazioneEnteComuneModA2) {
		getGregRRendicontazioneEnteComuneModA2s().add(gregRRendicontazioneEnteComuneModA2);
		gregRRendicontazioneEnteComuneModA2.setGregDComuni(this);

		return gregRRendicontazioneEnteComuneModA2;
	}

	public GregRRendicontazioneEnteComuneModA2 removeGregRRendicontazioneEnteComuneModA2(GregRRendicontazioneEnteComuneModA2 gregRRendicontazioneEnteComuneModA2) {
		getGregRRendicontazioneEnteComuneModA2s().remove(gregRRendicontazioneEnteComuneModA2);
		gregRRendicontazioneEnteComuneModA2.setGregDComuni(null);

		return gregRRendicontazioneEnteComuneModA2;
	}

	public Set<GregRRendicontazioneModA1> getGregRRendicontazioneModA1s() {
		return this.gregRRendicontazioneModA1s;
	}

	public void setGregRRendicontazioneModA1s(Set<GregRRendicontazioneModA1> gregRRendicontazioneModA1s) {
		this.gregRRendicontazioneModA1s = gregRRendicontazioneModA1s;
	}

	public GregRRendicontazioneModA1 addGregRRendicontazioneModA1(GregRRendicontazioneModA1 gregRRendicontazioneModA1) {
		getGregRRendicontazioneModA1s().add(gregRRendicontazioneModA1);
		gregRRendicontazioneModA1.setGregDComuni(this);

		return gregRRendicontazioneModA1;
	}

	public GregRRendicontazioneModA1 removeGregRRendicontazioneModA1(GregRRendicontazioneModA1 gregRRendicontazioneModA1) {
		getGregRRendicontazioneModA1s().remove(gregRRendicontazioneModA1);
		gregRRendicontazioneModA1.setGregDComuni(null);

		return gregRRendicontazioneModA1;
	}

	public Set<GregRRendicontazioneModE> getGregRRendicontazioneModEs() {
		return this.gregRRendicontazioneModEs;
	}

	public void setGregRRendicontazioneModEs(Set<GregRRendicontazioneModE> gregRRendicontazioneModEs) {
		this.gregRRendicontazioneModEs = gregRRendicontazioneModEs;
	}

	public GregRRendicontazioneModE addGregRRendicontazioneModE(GregRRendicontazioneModE gregRRendicontazioneModE) {
		getGregRRendicontazioneModEs().add(gregRRendicontazioneModE);
		gregRRendicontazioneModE.setGregDComuni(this);

		return gregRRendicontazioneModE;
	}

	public GregRRendicontazioneModE removeGregRRendicontazioneModE(GregRRendicontazioneModE gregRRendicontazioneModE) {
		getGregRRendicontazioneModEs().remove(gregRRendicontazioneModE);
		gregRRendicontazioneModE.setGregDComuni(null);

		return gregRRendicontazioneModE;
	}

	public Set<GregRSchedeEntiGestoriComuni> getGregRSchedeEntiGestoriComunis() {
		return this.gregRSchedeEntiGestoriComunis;
	}

	public void setGregRSchedeEntiGestoriComunis(Set<GregRSchedeEntiGestoriComuni> gregRSchedeEntiGestoriComunis) {
		this.gregRSchedeEntiGestoriComunis = gregRSchedeEntiGestoriComunis;
	}

	public GregRSchedeEntiGestoriComuni addGregRSchedeEntiGestoriComuni(GregRSchedeEntiGestoriComuni gregRSchedeEntiGestoriComuni) {
		getGregRSchedeEntiGestoriComunis().add(gregRSchedeEntiGestoriComuni);
		gregRSchedeEntiGestoriComuni.setGregDComuni(this);

		return gregRSchedeEntiGestoriComuni;
	}

	public GregRSchedeEntiGestoriComuni removeGregRSchedeEntiGestoriComuni(GregRSchedeEntiGestoriComuni gregRSchedeEntiGestoriComuni) {
		getGregRSchedeEntiGestoriComunis().remove(gregRSchedeEntiGestoriComuni);
		gregRSchedeEntiGestoriComuni.setGregDComuni(null);

		return gregRSchedeEntiGestoriComuni;
	}

	public Set<GregREnteGestoreContatti> getGregREnteGestoreContattis() {
		return this.gregREnteGestoreContattis;
	}

	public void setGregREnteGestoreContattis(Set<GregREnteGestoreContatti> gregREnteGestoreContattis) {
		this.gregREnteGestoreContattis = gregREnteGestoreContattis;
	}

	public GregREnteGestoreContatti addGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().add(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregDComuni(this);

		return gregREnteGestoreContatti;
	}

	public GregREnteGestoreContatti removeGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().remove(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregDComuni(null);

		return gregREnteGestoreContatti;
	}
	
	public Timestamp getFineValiditaComune() {
		return this.fineValiditaComune;
	}

	public void setFineValiditaComune(Timestamp fineValiditaComune) {
		this.fineValiditaComune = fineValiditaComune;
	}
	
	public Timestamp getInizioValiditaComune() {
		return this.inizioValiditaComune;
	}

	public void setInizioValiditaComune(Timestamp inizioValiditaComune) {
		this.inizioValiditaComune = inizioValiditaComune;
	}
	
	public GregDAsl getGregDAsl() {
		return this.gregDAsl;
	}

	public void setGregDAsl(GregDAsl gregDAsl) {
		this.gregDAsl = gregDAsl;
	}
}