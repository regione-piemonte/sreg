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
 * The persistent class for the greg_t_schede_enti_gestori database table.
 * 
 */
@Entity
@Table(name="greg_t_schede_enti_gestori")
@NamedQueries({
	@NamedQuery(name="GregTSchedeEntiGestori.findAll", query="SELECT g FROM GregTSchedeEntiGestori g ORDER BY g.codiceRegionale"),
	@NamedQuery(name="GregTSchedeEntiGestori.findAllNotDeleted", query="SELECT g FROM GregTSchedeEntiGestori g WHERE g.dataCancellazione IS NULL ORDER BY g.codiceRegionale"),
	@NamedQuery(name="GregTSchedeEntiGestori.findByValue", query = "SELECT g FROM GregTSchedeEntiGestori g "
			+ "WHERE g.codiceRegionale = :codReg "
			),
	@NamedQuery(name="GregTSchedeEntiGestori.findByCodReg", query = "SELECT g FROM GregTSchedeEntiGestori g WHERE g.codiceRegionale = :codReg and g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregTSchedeEntiGestori.findByCodFis", query = "SELECT g FROM GregTSchedeEntiGestori g WHERE g.codiceFiscale = :codFisc and g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregTSchedeEntiGestori.findById", query = "SELECT g FROM GregTSchedeEntiGestori g WHERE g.idSchedaEnteGestore = :idEnte and g.dataCancellazione is null "),
	@NamedQuery(name="GregTSchedeEntiGestori.findByIdNotDeleted", query = "SELECT g FROM GregTSchedeEntiGestori g WHERE g.idSchedaEnteGestore = :idEnte AND g.dataCancellazione IS NULL")
})
public class GregTSchedeEntiGestori implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_SCHEDE_ENTI_GESTORI_IDSCHEDAENTEGESTORE_GENERATOR", sequenceName="GREG_T_SCHEDE_ENTI_GESTORI_ID_SCHEDA_ENTE_GESTORE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_SCHEDE_ENTI_GESTORI_IDSCHEDAENTEGESTORE_GENERATOR")
	@Column(name="id_scheda_ente_gestore")
	private Integer idSchedaEnteGestore;

	@Column(name="codice_fiscale")
	private String codiceFiscale;

	@Column(name="codice_regionale")
	private String codiceRegionale;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRSchedeEntiGestoriComuni
	@JsonIgnore
	@OneToMany(mappedBy="gregTSchedeEntiGestori")
	private Set<GregRSchedeEntiGestoriComuni> gregRSchedeEntiGestoriComunis;

	//bi-directional many-to-one association to GregRSchedeEntiGestoriDistretti
	@JsonIgnore
	@OneToMany(mappedBy="gregTSchedeEntiGestori")
	private Set<GregRSchedeEntiGestoriDistretti> gregRSchedeEntiGestoriDistrettis;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@JsonIgnore
	@OneToMany(mappedBy="gregTSchedeEntiGestori",fetch = FetchType.EAGER)
	private Set<GregTRendicontazioneEnte> gregTRendicontazioneEntes;

	//bi-directional many-to-one association to GregREnteGestoreContatti
	@JsonIgnore
	@OneToMany(mappedBy="gregTSchedeEntiGestori")
	private Set<GregREnteGestoreContatti> gregREnteGestoreContattis;

	//bi-directional many-to-one association to GregREnteGestoreStatoEnte
	@JsonIgnore
	@OneToMany(mappedBy="gregTSchedeEntiGestori")
	private Set<GregREnteGestoreStatoEnte> gregREnteGestoreStatoEntes;

	//bi-directional many-to-one association to GregRListaEntiGestori
	@JsonIgnore
	@OneToMany(mappedBy="gregTSchedeEntiGestori")
	private Set<GregRListaEntiGestori> gregRListaEntiGestoris;

	//bi-directional many-to-one association to GregRMergeEnti
	@JsonIgnore
	@OneToMany(mappedBy="gregTSchedeEntiGestori1")
	private Set<GregRMergeEnti> gregRMergeEntis1;

	//bi-directional many-to-one association to GregRMergeEnti
	@JsonIgnore
	@OneToMany(mappedBy="gregTSchedeEntiGestori2")
	private Set<GregRMergeEnti> gregRMergeEntis2;

	public GregTSchedeEntiGestori() {
	}

	public Integer getIdSchedaEnteGestore() {
		return this.idSchedaEnteGestore;
	}

	public void setIdSchedaEnteGestore(Integer idSchedaEnteGestore) {
		this.idSchedaEnteGestore = idSchedaEnteGestore;
	}

	public String getCodiceFiscale() {
		return this.codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCodiceRegionale() {
		return this.codiceRegionale;
	}

	public void setCodiceRegionale(String codiceRegionale) {
		this.codiceRegionale = codiceRegionale;
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

	public Set<GregRSchedeEntiGestoriComuni> getGregRSchedeEntiGestoriComunis() {
		return this.gregRSchedeEntiGestoriComunis;
	}

	public void setGregRSchedeEntiGestoriComunis(Set<GregRSchedeEntiGestoriComuni> gregRSchedeEntiGestoriComunis) {
		this.gregRSchedeEntiGestoriComunis = gregRSchedeEntiGestoriComunis;
	}

	public GregRSchedeEntiGestoriComuni addGregRSchedeEntiGestoriComuni(GregRSchedeEntiGestoriComuni gregRSchedeEntiGestoriComuni) {
		getGregRSchedeEntiGestoriComunis().add(gregRSchedeEntiGestoriComuni);
		gregRSchedeEntiGestoriComuni.setGregTSchedeEntiGestori(this);

		return gregRSchedeEntiGestoriComuni;
	}

	public GregRSchedeEntiGestoriComuni removeGregRSchedeEntiGestoriComuni(GregRSchedeEntiGestoriComuni gregRSchedeEntiGestoriComuni) {
		getGregRSchedeEntiGestoriComunis().remove(gregRSchedeEntiGestoriComuni);
		gregRSchedeEntiGestoriComuni.setGregTSchedeEntiGestori(null);

		return gregRSchedeEntiGestoriComuni;
	}

	public Set<GregRSchedeEntiGestoriDistretti> getGregRSchedeEntiGestoriDistrettis() {
		return this.gregRSchedeEntiGestoriDistrettis;
	}

	public void setGregRSchedeEntiGestoriDistrettis(Set<GregRSchedeEntiGestoriDistretti> gregRSchedeEntiGestoriDistrettis) {
		this.gregRSchedeEntiGestoriDistrettis = gregRSchedeEntiGestoriDistrettis;
	}

	public GregRSchedeEntiGestoriDistretti addGregRSchedeEntiGestoriDistretti(GregRSchedeEntiGestoriDistretti gregRSchedeEntiGestoriDistretti) {
		getGregRSchedeEntiGestoriDistrettis().add(gregRSchedeEntiGestoriDistretti);
		gregRSchedeEntiGestoriDistretti.setGregTSchedeEntiGestori(this);

		return gregRSchedeEntiGestoriDistretti;
	}

	public GregRSchedeEntiGestoriDistretti removeGregRSchedeEntiGestoriDistretti(GregRSchedeEntiGestoriDistretti gregRSchedeEntiGestoriDistretti) {
		getGregRSchedeEntiGestoriDistrettis().remove(gregRSchedeEntiGestoriDistretti);
		gregRSchedeEntiGestoriDistretti.setGregTSchedeEntiGestori(null);

		return gregRSchedeEntiGestoriDistretti;
	}

	public Set<GregTRendicontazioneEnte> getGregTRendicontazioneEntes() {
		return this.gregTRendicontazioneEntes;
	}

	public void setGregTRendicontazioneEntes(Set<GregTRendicontazioneEnte> gregTRendicontazioneEntes) {
		this.gregTRendicontazioneEntes = gregTRendicontazioneEntes;
	}

	public GregTRendicontazioneEnte addGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		getGregTRendicontazioneEntes().add(gregTRendicontazioneEnte);
		gregTRendicontazioneEnte.setGregTSchedeEntiGestori(this);

		return gregTRendicontazioneEnte;
	}

	public GregTRendicontazioneEnte removeGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		getGregTRendicontazioneEntes().remove(gregTRendicontazioneEnte);
		gregTRendicontazioneEnte.setGregTSchedeEntiGestori(null);

		return gregTRendicontazioneEnte;
	}

	public Set<GregREnteGestoreContatti> getGregREnteGestoreContattis() {
		return this.gregREnteGestoreContattis;
	}

	public void setGregREnteGestoreContattis(Set<GregREnteGestoreContatti> gregREnteGestoreContattis) {
		this.gregREnteGestoreContattis = gregREnteGestoreContattis;
	}

	public GregREnteGestoreContatti addGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().add(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregTSchedeEntiGestori(this);

		return gregREnteGestoreContatti;
	}

	public GregREnteGestoreContatti removeGregREnteGestoreContatti(GregREnteGestoreContatti gregREnteGestoreContatti) {
		getGregREnteGestoreContattis().remove(gregREnteGestoreContatti);
		gregREnteGestoreContatti.setGregTSchedeEntiGestori(null);

		return gregREnteGestoreContatti;
	}

	public Set<GregREnteGestoreStatoEnte> getGregREnteGestoreStatoEntes() {
		return this.gregREnteGestoreStatoEntes;
	}

	public void setGregREnteGestoreStatoEntes(Set<GregREnteGestoreStatoEnte> gregREnteGestoreStatoEntes) {
		this.gregREnteGestoreStatoEntes = gregREnteGestoreStatoEntes;
	}

	public GregREnteGestoreStatoEnte addGregREnteGestoreStatoEnte(GregREnteGestoreStatoEnte gregREnteGestoreStatoEnte) {
		getGregREnteGestoreStatoEntes().add(gregREnteGestoreStatoEnte);
		gregREnteGestoreStatoEnte.setGregTSchedeEntiGestori(this);

		return gregREnteGestoreStatoEnte;
	}

	public GregREnteGestoreStatoEnte removeGregREnteGestoreStatoEnte(GregREnteGestoreStatoEnte gregREnteGestoreStatoEnte) {
		getGregREnteGestoreStatoEntes().remove(gregREnteGestoreStatoEnte);
		gregREnteGestoreStatoEnte.setGregTSchedeEntiGestori(null);

		return gregREnteGestoreStatoEnte;
	}

	public Set<GregRListaEntiGestori> getGregRListaEntiGestoris() {
		return this.gregRListaEntiGestoris;
	}

	public void setGregRListaEntiGestoris(Set<GregRListaEntiGestori> gregRListaEntiGestoris) {
		this.gregRListaEntiGestoris = gregRListaEntiGestoris;
	}

	public GregRListaEntiGestori addGregRListaEntiGestori(GregRListaEntiGestori gregRListaEntiGestori) {
		getGregRListaEntiGestoris().add(gregRListaEntiGestori);
		gregRListaEntiGestori.setGregTSchedeEntiGestori(this);

		return gregRListaEntiGestori;
	}

	public GregRListaEntiGestori removeGregRListaEntiGestori(GregRListaEntiGestori gregRListaEntiGestori) {
		getGregRListaEntiGestoris().remove(gregRListaEntiGestori);
		gregRListaEntiGestori.setGregTSchedeEntiGestori(null);

		return gregRListaEntiGestori;
	}

	public Set<GregRMergeEnti> getGregRMergeEntis1() {
		return this.gregRMergeEntis1;
	}

	public void setGregRMergeEntis1(Set<GregRMergeEnti> gregRMergeEntis1) {
		this.gregRMergeEntis1 = gregRMergeEntis1;
	}

	public GregRMergeEnti addGregRMergeEntis1(GregRMergeEnti gregRMergeEntis1) {
		getGregRMergeEntis1().add(gregRMergeEntis1);
		gregRMergeEntis1.setGregTSchedeEntiGestori1(this);

		return gregRMergeEntis1;
	}

	public GregRMergeEnti removeGregRMergeEntis1(GregRMergeEnti gregRMergeEntis1) {
		getGregRMergeEntis1().remove(gregRMergeEntis1);
		gregRMergeEntis1.setGregTSchedeEntiGestori1(null);

		return gregRMergeEntis1;
	}

	public Set<GregRMergeEnti> getGregRMergeEntis2() {
		return this.gregRMergeEntis2;
	}

	public void setGregRMergeEntis2(Set<GregRMergeEnti> gregRMergeEntis2) {
		this.gregRMergeEntis2 = gregRMergeEntis2;
	}

	public GregRMergeEnti addGregRMergeEntis2(GregRMergeEnti gregRMergeEntis2) {
		getGregRMergeEntis2().add(gregRMergeEntis2);
		gregRMergeEntis2.setGregTSchedeEntiGestori2(this);

		return gregRMergeEntis2;
	}

	public GregRMergeEnti removeGregRMergeEntis2(GregRMergeEnti gregRMergeEntis2) {
		getGregRMergeEntis2().remove(gregRMergeEntis2);
		gregRMergeEntis2.setGregTSchedeEntiGestori2(null);

		return gregRMergeEntis2;
	}

}