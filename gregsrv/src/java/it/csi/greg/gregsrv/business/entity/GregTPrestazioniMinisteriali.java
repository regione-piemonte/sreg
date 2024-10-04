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
 * The persistent class for the greg_t_prestazioni_ministeriali database table.
 * 
 */
@Entity
@Table(name="greg_t_prestazioni_ministeriali")
@NamedQueries({
	@NamedQuery(name="GregTPrestazioniMinisteriali.findAll", query="SELECT g FROM GregTPrestazioniMinisteriali g where g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregTPrestazioniMinisteriali.findByIdMinNotDeleted", query="SELECT g FROM GregTPrestazioniMinisteriali g WHERE g.idPrestazioneMinisteriale= :idPrestazioneMin AND g.dataCancellazione IS NULL")
})
public class GregTPrestazioniMinisteriali implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_PRESTAZIONI_MINISTERIALI_IDPRESTAZIONEMINISTERIALE_GENERATOR", sequenceName="GREG_T_PRESTAZIONI_MINISTERIALI_ID_PRESTAZIONE_MINISTERIALE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_PRESTAZIONI_MINISTERIALI_IDPRESTAZIONEMINISTERIALE_GENERATOR")
	@Column(name="id_prestazione_ministeriale")
	private Integer idPrestazioneMinisteriale;

	@Column(name="cod_prestazione_ministeriale")
	private String codPrestazioneMinisteriale;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_prestazione_ministeriale")
	private String descPrestazioneMinisteriale;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRPrestMinistUtenzeMinist
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniMinisteriali")
	private Set<GregRPrestMinistUtenzeMinist> gregRPrestMinistUtenzeMinists;

	//bi-directional many-to-one association to GregRPrestReg1PrestMinist
	@JsonIgnore
	@OneToMany(mappedBy="gregTPrestazioniMinisteriali")
	private Set<GregRPrestReg1PrestMinist> gregRPrestReg1PrestMinists;

	//bi-directional many-to-one association to GregDMacroattivita
	@ManyToOne
	@JoinColumn(name="id_macroattivita")
	private GregDMacroattivita gregDMacroattivita;

	public GregTPrestazioniMinisteriali() {
	}

	public Integer getIdPrestazioneMinisteriale() {
		return this.idPrestazioneMinisteriale;
	}

	public void setIdPrestazioneMinisteriale(Integer idPrestazioneMinisteriale) {
		this.idPrestazioneMinisteriale = idPrestazioneMinisteriale;
	}

	public String getCodPrestazioneMinisteriale() {
		return this.codPrestazioneMinisteriale;
	}

	public void setCodPrestazioneMinisteriale(String codPrestazioneMinisteriale) {
		this.codPrestazioneMinisteriale = codPrestazioneMinisteriale;
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

	public String getDescPrestazioneMinisteriale() {
		return this.descPrestazioneMinisteriale;
	}

	public void setDescPrestazioneMinisteriale(String descPrestazioneMinisteriale) {
		this.descPrestazioneMinisteriale = descPrestazioneMinisteriale;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRPrestMinistUtenzeMinist> getGregRPrestMinistUtenzeMinists() {
		return this.gregRPrestMinistUtenzeMinists;
	}

	public void setGregRPrestMinistUtenzeMinists(Set<GregRPrestMinistUtenzeMinist> gregRPrestMinistUtenzeMinists) {
		this.gregRPrestMinistUtenzeMinists = gregRPrestMinistUtenzeMinists;
	}

	public GregRPrestMinistUtenzeMinist addGregRPrestMinistUtenzeMinist(GregRPrestMinistUtenzeMinist gregRPrestMinistUtenzeMinist) {
		getGregRPrestMinistUtenzeMinists().add(gregRPrestMinistUtenzeMinist);
		gregRPrestMinistUtenzeMinist.setGregTPrestazioniMinisteriali(this);

		return gregRPrestMinistUtenzeMinist;
	}

	public GregRPrestMinistUtenzeMinist removeGregRPrestMinistUtenzeMinist(GregRPrestMinistUtenzeMinist gregRPrestMinistUtenzeMinist) {
		getGregRPrestMinistUtenzeMinists().remove(gregRPrestMinistUtenzeMinist);
		gregRPrestMinistUtenzeMinist.setGregTPrestazioniMinisteriali(null);

		return gregRPrestMinistUtenzeMinist;
	}

	public Set<GregRPrestReg1PrestMinist> getGregRPrestReg1PrestMinists() {
		return this.gregRPrestReg1PrestMinists;
	}

	public void setGregRPrestReg1PrestMinists(Set<GregRPrestReg1PrestMinist> gregRPrestReg1PrestMinists) {
		this.gregRPrestReg1PrestMinists = gregRPrestReg1PrestMinists;
	}

	public GregRPrestReg1PrestMinist addGregRPrestReg1PrestMinist(GregRPrestReg1PrestMinist gregRPrestReg1PrestMinist) {
		getGregRPrestReg1PrestMinists().add(gregRPrestReg1PrestMinist);
		gregRPrestReg1PrestMinist.setGregTPrestazioniMinisteriali(this);

		return gregRPrestReg1PrestMinist;
	}

	public GregRPrestReg1PrestMinist removeGregRPrestReg1PrestMinist(GregRPrestReg1PrestMinist gregRPrestReg1PrestMinist) {
		getGregRPrestReg1PrestMinists().remove(gregRPrestReg1PrestMinist);
		gregRPrestReg1PrestMinist.setGregTPrestazioniMinisteriali(null);

		return gregRPrestReg1PrestMinist;
	}

	public GregDMacroattivita getGregDMacroattivita() {
		return this.gregDMacroattivita;
	}

	public void setGregDMacroattivita(GregDMacroattivita gregDMacroattivita) {
		this.gregDMacroattivita = gregDMacroattivita;
	}

}