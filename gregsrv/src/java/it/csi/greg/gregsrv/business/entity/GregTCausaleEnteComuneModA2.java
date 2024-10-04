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
 * The persistent class for the greg_t_causale_ente_comune_mod_a2 database table.
 * 
 */
@Entity
@Table(name="greg_t_causale_ente_comune_mod_a2")
@NamedQueries({
	@NamedQuery(name="GregTCausaleEnteComuneModA2.findAll", query="SELECT g FROM GregTCausaleEnteComuneModA2 g"),
	@NamedQuery(name="GregTCausaleEnteComuneModA2.findByIdRendicontazioneEnte", query="SELECT g FROM GregTCausaleEnteComuneModA2 g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione and g.dataCancellazione is null "),
	@NamedQuery(name="GregTCausaleEnteComuneModA2.findValideByIdRendicontazioneEnte", query="SELECT g FROM GregTCausaleEnteComuneModA2 g "
			+ "where g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
			+ "and g.dataCancellazione is null"),
})
public class GregTCausaleEnteComuneModA2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_T_CAUSALE_ENTE_COMUNE_MOD_A2_IDCAUSALEENTECOMUNEMODA2_GENERATOR", sequenceName="GREG_T_CAUSALE_ENTE_COMUNE_MO_ID_CAUSALE_ENTE_COMUNE_MOD_A2_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_T_CAUSALE_ENTE_COMUNE_MOD_A2_IDCAUSALEENTECOMUNEMODA2_GENERATOR")
	@Column(name="id_causale_ente_comune_mod_a2")
	private Integer idCausaleEnteComuneModA2;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_causale_ente_comune_mod_a2")
	private String descCausaleEnteComuneModA2;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRRendicontazioneEnteComuneModA2
	@JsonIgnore
	@OneToMany(mappedBy="gregTCausaleEnteComuneModA2")
	private Set<GregRRendicontazioneEnteComuneModA2> gregRRendicontazioneEnteComuneModA2s;

	//bi-directional many-to-one association to GregTRendicontazioneEnte
	@ManyToOne
	@JoinColumn(name="id_rendicontazione_ente")
	private GregTRendicontazioneEnte gregTRendicontazioneEnte;

	public GregTCausaleEnteComuneModA2() {
	}

	public Integer getIdCausaleEnteComuneModA2() {
		return this.idCausaleEnteComuneModA2;
	}

	public void setIdCausaleEnteComuneModA2(Integer idCausaleEnteComuneModA2) {
		this.idCausaleEnteComuneModA2 = idCausaleEnteComuneModA2;
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

	public String getDescCausaleEnteComuneModA2() {
		return this.descCausaleEnteComuneModA2;
	}

	public void setDescCausaleEnteComuneModA2(String descCausaleEnteComuneModA2) {
		this.descCausaleEnteComuneModA2 = descCausaleEnteComuneModA2;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRRendicontazioneEnteComuneModA2> getGregRRendicontazioneEnteComuneModA2s() {
		return this.gregRRendicontazioneEnteComuneModA2s;
	}

	public void setGregRRendicontazioneEnteComuneModA2s(Set<GregRRendicontazioneEnteComuneModA2> gregRRendicontazioneEnteComuneModA2s) {
		this.gregRRendicontazioneEnteComuneModA2s = gregRRendicontazioneEnteComuneModA2s;
	}

	public GregRRendicontazioneEnteComuneModA2 addGregRRendicontazioneEnteComuneModA2(GregRRendicontazioneEnteComuneModA2 gregRRendicontazioneEnteComuneModA2) {
		getGregRRendicontazioneEnteComuneModA2s().add(gregRRendicontazioneEnteComuneModA2);
		gregRRendicontazioneEnteComuneModA2.setGregTCausaleEnteComuneModA2(this);

		return gregRRendicontazioneEnteComuneModA2;
	}

	public GregRRendicontazioneEnteComuneModA2 removeGregRRendicontazioneEnteComuneModA2(GregRRendicontazioneEnteComuneModA2 gregRRendicontazioneEnteComuneModA2) {
		getGregRRendicontazioneEnteComuneModA2s().remove(gregRRendicontazioneEnteComuneModA2);
		gregRRendicontazioneEnteComuneModA2.setGregTCausaleEnteComuneModA2(null);

		return gregRRendicontazioneEnteComuneModA2;
	}

	public GregTRendicontazioneEnte getGregTRendicontazioneEnte() {
		return this.gregTRendicontazioneEnte;
	}

	public void setGregTRendicontazioneEnte(GregTRendicontazioneEnte gregTRendicontazioneEnte) {
		this.gregTRendicontazioneEnte = gregTRendicontazioneEnte;
	}

}