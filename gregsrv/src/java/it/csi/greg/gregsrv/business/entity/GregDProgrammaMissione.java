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
 * The persistent class for the greg_d_programma_missione database table.
 * 
 */
@Entity
@Table(name="greg_d_programma_missione")
@NamedQueries({
	@NamedQuery(name="GregDProgrammaMissione.findAll", query="SELECT g FROM GregDProgrammaMissione g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregDProgrammaMissione.findBySiglaProgMissNotDeleted", query="SELECT g FROM GregDProgrammaMissione g WHERE g.siglaProgrammaMissione= :codMissione AND g.dataCancellazione IS NULL")
})

public class GregDProgrammaMissione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_PROGRAMMA_MISSIONE_IDPROGRAMMAMISSIONE_GENERATOR", sequenceName="GREG_D_PROGRAMMA_MISSIONE_ID_PROGRAMMA_MISSIONE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_PROGRAMMA_MISSIONE_IDPROGRAMMAMISSIONE_GENERATOR")
	@Column(name="id_programma_missione")
	private Integer idProgrammaMissione;

	@Column(name="cod_programma_missione")
	private String codProgrammaMissione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_programma_missione")
	private String desProgrammaMissione;

	@Column(name="flag_conforme_cartaceo")
	private Boolean flagConformeCartaceo;

	private String informativa;

	@Column(name="msg_informativo")
	private String msgInformativo;

	private String nota;

	@Column(name="sigla_programma_missione")
	private String siglaProgrammaMissione;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDColori
	@ManyToOne
	@JoinColumn(name="id_colore")
	private GregDColori gregDColori;

	//bi-directional many-to-one association to GregDMissione
	@ManyToOne
	@JoinColumn(name="id_missione")
	private GregDMissione gregDMissione;

	//bi-directional many-to-one association to GregRPrestReg1UtenzeRegionali1ProgrammaMissione
	@JsonIgnore
	@OneToMany(mappedBy="gregDProgrammaMissione")
	private Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> gregRPrestReg1UtenzeRegionali1ProgrammaMissiones;

	//bi-directional many-to-one association to GregRProgrammaMissioneTitSottotit
	@JsonIgnore
	@OneToMany(mappedBy="gregDProgrammaMissione")
	private Set<GregRProgrammaMissioneTitSottotit> gregRProgrammaMissioneTitSottotits;

	//bi-directional many-to-one association to GregRRendicontazioneNonConformitaModB
	@JsonIgnore
	@OneToMany(mappedBy="gregDProgrammaMissione")
	private Set<GregRRendicontazioneNonConformitaModB> gregRRendicontazioneNonConformitaModBs;

	public GregDProgrammaMissione() {
	}

	public Integer getIdProgrammaMissione() {
		return this.idProgrammaMissione;
	}

	public void setIdProgrammaMissione(Integer idProgrammaMissione) {
		this.idProgrammaMissione = idProgrammaMissione;
	}

	public String getCodProgrammaMissione() {
		return this.codProgrammaMissione;
	}

	public void setCodProgrammaMissione(String codProgrammaMissione) {
		this.codProgrammaMissione = codProgrammaMissione;
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

	public String getDesProgrammaMissione() {
		return this.desProgrammaMissione;
	}

	public void setDesProgrammaMissione(String desProgrammaMissione) {
		this.desProgrammaMissione = desProgrammaMissione;
	}

	public Boolean getFlagConformeCartaceo() {
		return this.flagConformeCartaceo;
	}

	public void setFlagConformeCartaceo(Boolean flagConformeCartaceo) {
		this.flagConformeCartaceo = flagConformeCartaceo;
	}

	public String getInformativa() {
		return this.informativa;
	}

	public void setInformativa(String informativa) {
		this.informativa = informativa;
	}

	public String getMsgInformativo() {
		return this.msgInformativo;
	}

	public void setMsgInformativo(String msgInformativo) {
		this.msgInformativo = msgInformativo;
	}

	public String getNota() {
		return this.nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getSiglaProgrammaMissione() {
		return this.siglaProgrammaMissione;
	}

	public void setSiglaProgrammaMissione(String siglaProgrammaMissione) {
		this.siglaProgrammaMissione = siglaProgrammaMissione;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public GregDColori getGregDColori() {
		return this.gregDColori;
	}

	public void setGregDColori(GregDColori gregDColori) {
		this.gregDColori = gregDColori;
	}

	public GregDMissione getGregDMissione() {
		return this.gregDMissione;
	}

	public void setGregDMissione(GregDMissione gregDMissione) {
		this.gregDMissione = gregDMissione;
	}

	public Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones() {
		return this.gregRPrestReg1UtenzeRegionali1ProgrammaMissiones;
	}

	public void setGregRPrestReg1UtenzeRegionali1ProgrammaMissiones(Set<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> gregRPrestReg1UtenzeRegionali1ProgrammaMissiones) {
		this.gregRPrestReg1UtenzeRegionali1ProgrammaMissiones = gregRPrestReg1UtenzeRegionali1ProgrammaMissiones;
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissione addGregRPrestReg1UtenzeRegionali1ProgrammaMissione(GregRPrestReg1UtenzeRegionali1ProgrammaMissione gregRPrestReg1UtenzeRegionali1ProgrammaMissione) {
		getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().add(gregRPrestReg1UtenzeRegionali1ProgrammaMissione);
		gregRPrestReg1UtenzeRegionali1ProgrammaMissione.setGregDProgrammaMissione(this);

		return gregRPrestReg1UtenzeRegionali1ProgrammaMissione;
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissione removeGregRPrestReg1UtenzeRegionali1ProgrammaMissione(GregRPrestReg1UtenzeRegionali1ProgrammaMissione gregRPrestReg1UtenzeRegionali1ProgrammaMissione) {
		getGregRPrestReg1UtenzeRegionali1ProgrammaMissiones().remove(gregRPrestReg1UtenzeRegionali1ProgrammaMissione);
		gregRPrestReg1UtenzeRegionali1ProgrammaMissione.setGregDProgrammaMissione(null);

		return gregRPrestReg1UtenzeRegionali1ProgrammaMissione;
	}

	public Set<GregRProgrammaMissioneTitSottotit> getGregRProgrammaMissioneTitSottotits() {
		return this.gregRProgrammaMissioneTitSottotits;
	}

	public void setGregRProgrammaMissioneTitSottotits(Set<GregRProgrammaMissioneTitSottotit> gregRProgrammaMissioneTitSottotits) {
		this.gregRProgrammaMissioneTitSottotits = gregRProgrammaMissioneTitSottotits;
	}

	public GregRProgrammaMissioneTitSottotit addGregRProgrammaMissioneTitSottotit(GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit) {
		getGregRProgrammaMissioneTitSottotits().add(gregRProgrammaMissioneTitSottotit);
		gregRProgrammaMissioneTitSottotit.setGregDProgrammaMissione(this);

		return gregRProgrammaMissioneTitSottotit;
	}

	public GregRProgrammaMissioneTitSottotit removeGregRProgrammaMissioneTitSottotit(GregRProgrammaMissioneTitSottotit gregRProgrammaMissioneTitSottotit) {
		getGregRProgrammaMissioneTitSottotits().remove(gregRProgrammaMissioneTitSottotit);
		gregRProgrammaMissioneTitSottotit.setGregDProgrammaMissione(null);

		return gregRProgrammaMissioneTitSottotit;
	}

	public Set<GregRRendicontazioneNonConformitaModB> getGregRRendicontazioneNonConformitaModBs() {
		return this.gregRRendicontazioneNonConformitaModBs;
	}

	public void setGregRRendicontazioneNonConformitaModBs(Set<GregRRendicontazioneNonConformitaModB> gregRRendicontazioneNonConformitaModBs) {
		this.gregRRendicontazioneNonConformitaModBs = gregRRendicontazioneNonConformitaModBs;
	}

	public GregRRendicontazioneNonConformitaModB addGregRRendicontazioneNonConformitaModB(GregRRendicontazioneNonConformitaModB gregRRendicontazioneNonConformitaModB) {
		getGregRRendicontazioneNonConformitaModBs().add(gregRRendicontazioneNonConformitaModB);
		gregRRendicontazioneNonConformitaModB.setGregDProgrammaMissione(this);

		return gregRRendicontazioneNonConformitaModB;
	}

	public GregRRendicontazioneNonConformitaModB removeGregRRendicontazioneNonConformitaModB(GregRRendicontazioneNonConformitaModB gregRRendicontazioneNonConformitaModB) {
		getGregRRendicontazioneNonConformitaModBs().remove(gregRRendicontazioneNonConformitaModB);
		gregRRendicontazioneNonConformitaModB.setGregDProgrammaMissione(null);

		return gregRRendicontazioneNonConformitaModB;
	}

}