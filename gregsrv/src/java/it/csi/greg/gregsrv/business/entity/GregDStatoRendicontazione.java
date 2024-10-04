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
 * The persistent class for the greg_d_stato_rendicontazione database table.
 * 
 */
@Entity
@Table(name="greg_d_stato_rendicontazione")
@NamedQueries({
	@NamedQuery(name="GregDStatoRendicontazione.findAll", query="SELECT g FROM GregDStatoRendicontazione g ORDER BY g.ordinamento"),
	@NamedQuery(name="GregDStatoRendicontazione.findAllNotDeleted", query="SELECT g FROM GregDStatoRendicontazione g WHERE g.dataCancellazione IS NULL ORDER BY g.ordinamento"),
	@NamedQuery(name="GregDStatoRendicontazione.findAllNotDeletedNotSto", query="SELECT g FROM GregDStatoRendicontazione g WHERE g.dataCancellazione IS NULL and g.codStatoRendicontazione<>'CON' ORDER BY g.ordinamento"),
	@NamedQuery(name="GregDStatoRendicontazione.findByDescNotDeleted", query="SELECT g FROM GregDStatoRendicontazione g WHERE g.dataCancellazione IS NULL AND g.desStatoRendicontazione = :desc"),
	@NamedQuery(name="GregDStatoRendicontazione.findByCodNotDeleted", query="SELECT g FROM GregDStatoRendicontazione g WHERE g.dataCancellazione IS NULL AND g.codStatoRendicontazione = :cod"),
	@NamedQuery(name="GregDStatoRendicontazione.findStatoConcluso", query="SELECT g FROM GregDStatoRendicontazione g WHERE g.codStatoRendicontazione='CON'")
})
public class GregDStatoRendicontazione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_STATO_RENDICONTAZIONE_IDSTATORENDICONTAZIONE_GENERATOR", sequenceName="GREG_D_STATO_RENDICONTAZIONE_ID_STATO_RENDICONTAZIONE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_STATO_RENDICONTAZIONE_IDSTATORENDICONTAZIONE_GENERATOR")
	@Column(name="id_stato_rendicontazione")
	private Integer idStatoRendicontazione;

	@Column(name="cod_stato_rendicontazione")
	private String codStatoRendicontazione;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_stato_rendicontazione")
	private String desStatoRendicontazione;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregTCronologia
	@JsonIgnore
	@OneToMany(mappedBy="gregDStatoRendicontazione")
	private Set<GregTCronologia> gregTCronologias;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@JsonIgnore
	@OneToMany(mappedBy="gregDStatoRendicontazione")
	private Set<GregTRendicontazioneEnte> gregTRendicontazioneEntes;
	
	private Integer ordinamento;

	public GregDStatoRendicontazione() {
	}

	public Integer getIdStatoRendicontazione() {
		return this.idStatoRendicontazione;
	}

	public void setIdStatoRendicontazione(Integer idStatoRendicontazione) {
		this.idStatoRendicontazione = idStatoRendicontazione;
	}

	public String getCodStatoRendicontazione() {
		return this.codStatoRendicontazione;
	}

	public void setCodStatoRendicontazione(String codStatoRendicontazione) {
		this.codStatoRendicontazione = codStatoRendicontazione;
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

	public String getDesStatoRendicontazione() {
		return this.desStatoRendicontazione;
	}

	public void setDesStatoRendicontazione(String desStatoRendicontazione) {
		this.desStatoRendicontazione = desStatoRendicontazione;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregTCronologia> getGregTCronologias() {
		return this.gregTCronologias;
	}

	public void setGregTCronologias(Set<GregTCronologia> gregTCronologias) {
		this.gregTCronologias = gregTCronologias;
	}

	public GregTCronologia addGregTCronologia(GregTCronologia gregTCronologia) {
		getGregTCronologias().add(gregTCronologia);
		gregTCronologia.setGregDStatoRendicontazione(this);

		return gregTCronologia;
	}

	public GregTCronologia removeGregTCronologia(GregTCronologia gregTCronologia) {
		getGregTCronologias().remove(gregTCronologia);
		gregTCronologia.setGregDStatoRendicontazione(null);

		return gregTCronologia;
	}

	public Set<GregTRendicontazioneEnte> getGregTRendicontazioneEntes() {
		return this.gregTRendicontazioneEntes;
	}

	public void setGregTRendicontazioneEntes(Set<GregTRendicontazioneEnte> gregTRendicontazioneEntes) {
		this.gregTRendicontazioneEntes = gregTRendicontazioneEntes;
	}

	public GregTRendicontazioneEnte addGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		getGregTRendicontazioneEntes().add(gregTRendicontazioneEnte);
		gregTRendicontazioneEnte.setGregDStatoRendicontazione(this);

		return gregTRendicontazioneEnte;
	}

	public GregTRendicontazioneEnte removeGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		getGregTRendicontazioneEntes().remove(gregTRendicontazioneEnte);
		gregTRendicontazioneEnte.setGregDStatoRendicontazione(null);

		return gregTRendicontazioneEnte;
	}

	public Integer getOrdinamento() {
		return ordinamento;
	}

	public void setOrdinamento(Integer ordinamento) {
		this.ordinamento = ordinamento;
	}

}