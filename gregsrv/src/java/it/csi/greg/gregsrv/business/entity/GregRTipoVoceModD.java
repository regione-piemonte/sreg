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
 * The persistent class for the greg_r_tipo_voce_mod_d database table.
 * 
 */
@Entity
@Table(name="greg_r_tipo_voce_mod_d")
@NamedQueries({
	@NamedQuery(name="GregRTipoVoceModD.findAll", query="SELECT g FROM GregRTipoVoceModD g"),
	@NamedQuery(name="GregRTipoVoceModD.findByVoceTipoVoce", query="SELECT g FROM GregRTipoVoceModD g WHERE g.gregDVoceModD.idVoceModD = :idVoce AND g.gregDTipoVoce.idTipoVoce = :idTipoVoce and g.dataCancellazione is null"),
	@NamedQuery(name="GregRTipoVoceModD.findDaCompilare", query="SELECT g FROM GregRTipoVoceModD g WHERE g.dataEntry=true and g.dataCancellazione is null")
})
public class GregRTipoVoceModD implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_TIPO_VOCE_MOD_D_IDTIPOVOCEMODD_GENERATOR", sequenceName="GREG_R_TIPO_VOCE_MOD_D_ID_TIPO_VOCE_MOD_D_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_TIPO_VOCE_MOD_D_IDTIPOVOCEMODD_GENERATOR")
	@Column(name="id_tipo_voce_mod_d")
	private Integer idTipoVoceModD;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_entry")
	private Boolean dataEntry;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRRendicontazioneModD
	@JsonIgnore
	@OneToMany(mappedBy="gregRTipoVoceModD")
	private Set<GregRRendicontazioneModD> gregRRendicontazioneModDs;

	//bi-directional many-to-one association to GregDTipoVoce
	@ManyToOne
	@JoinColumn(name="id_tipo_voce")
	private GregDTipoVoce gregDTipoVoce;

	//bi-directional many-to-one association to GregDVoceModD
	@ManyToOne
	@JoinColumn(name="id_voce_mod_d")
	private GregDVoceModD gregDVoceModD;

	public GregRTipoVoceModD() {
	}

	public Integer getIdTipoVoceModD() {
		return this.idTipoVoceModD;
	}

	public void setIdTipoVoceModD(Integer idTipoVoceModD) {
		this.idTipoVoceModD = idTipoVoceModD;
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

	public Boolean getDataEntry() {
		return this.dataEntry;
	}

	public void setDataEntry(Boolean dataEntry) {
		this.dataEntry = dataEntry;
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

	public Set<GregRRendicontazioneModD> getGregRRendicontazioneModDs() {
		return this.gregRRendicontazioneModDs;
	}

	public void setGregRRendicontazioneModDs(Set<GregRRendicontazioneModD> gregRRendicontazioneModDs) {
		this.gregRRendicontazioneModDs = gregRRendicontazioneModDs;
	}

	public GregRRendicontazioneModD addGregRRendicontazioneModD(GregRRendicontazioneModD gregRRendicontazioneModD) {
		getGregRRendicontazioneModDs().add(gregRRendicontazioneModD);
		gregRRendicontazioneModD.setGregRTipoVoceModD(this);

		return gregRRendicontazioneModD;
	}

	public GregRRendicontazioneModD removeGregRRendicontazioneModD(GregRRendicontazioneModD gregRRendicontazioneModD) {
		getGregRRendicontazioneModDs().remove(gregRRendicontazioneModD);
		gregRRendicontazioneModD.setGregRTipoVoceModD(null);

		return gregRRendicontazioneModD;
	}

	public GregDTipoVoce getGregDTipoVoce() {
		return this.gregDTipoVoce;
	}

	public void setGregDTipoVoce(GregDTipoVoce gregDTipoVoce) {
		this.gregDTipoVoce = gregDTipoVoce;
	}

	public GregDVoceModD getGregDVoceModD() {
		return this.gregDVoceModD;
	}

	public void setGregDVoceModD(GregDVoceModD gregDVoceModD) {
		this.gregDVoceModD = gregDVoceModD;
	}

}