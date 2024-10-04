/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.csi.greg.gregsrv.business.entity.GregDRegione;

import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_d_province database table.
 * 
 */
@Entity
@Table(name="greg_d_province")
@NamedQueries({
	@NamedQuery(name="GregDProvince.findAll", query="SELECT g FROM GregDProvince g"),
	@NamedQuery(name="GregDProvince.findAllNotDeleted", query="SELECT g FROM GregDProvince g WHERE g.gregDRegione.codRegione = :codregione "
			+ "and ((g.dataInizioValidita <= :dataValidita and g.dataFineValidita >= :dataValidita) or "
			+ "(g.dataInizioValidita <= :dataValidita and g.dataFineValidita is null)) "
			+ "and g.dataCancellazione IS NULL ORDER BY g.desProvincia"),
	@NamedQuery(name="GregDProvince.findAllStorico", query="SELECT g FROM GregDProvince g WHERE "
			+ "g.dataCancellazione IS NULL ORDER BY g.desProvincia")
})

public class GregDProvince implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_PROVINCE_IDPROVINCIA_GENERATOR", sequenceName="GREG_D_PROVINCE_ID_PROVINCIA_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_PROVINCE_IDPROVINCIA_GENERATOR")
	@Column(name="id_provincia")
	private Integer idProvincia;

	@Column(name="cod_istat_provincia")
	private String codIstatProvincia;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_provincia")
	private String desProvincia;

	@Column(name="sigla_provincia")
	private String siglaProvincia;

	@Column(name="utente_operazione")
	private String utenteOperazione;
	
	@Column(name="data_fine_validita")
	private Timestamp dataFineValidita;

	@Column(name="data_inizio_validita")
	private Timestamp dataInizioValidita;

	//bi-directional many-to-one association to GregDComuni
	@JsonIgnore
	@OneToMany(mappedBy="gregDProvince")
	private Set<GregDComuni> gregDComunis;
	
	//bi-directional many-to-one association to GregDRegione
	@ManyToOne
	@JoinColumn(name="id_regione")
	private GregDRegione gregDRegione;

	public GregDProvince() {
	}

	public Integer getIdProvincia() {
		return this.idProvincia;
	}

	public void setIdProvincia(Integer idProvincia) {
		this.idProvincia = idProvincia;
	}

	public String getCodIstatProvincia() {
		return this.codIstatProvincia;
	}

	public void setCodIstatProvincia(String codIstatProvincia) {
		this.codIstatProvincia = codIstatProvincia;
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

	public String getDesProvincia() {
		return this.desProvincia;
	}

	public void setDesProvincia(String desProvincia) {
		this.desProvincia = desProvincia;
	}

	public String getSiglaProvincia() {
		return this.siglaProvincia;
	}

	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregDComuni> getGregDComunis() {
		return this.gregDComunis;
	}

	public void setGregDComunis(Set<GregDComuni> gregDComunis) {
		this.gregDComunis = gregDComunis;
	}

	public GregDComuni addGregDComuni(GregDComuni gregDComuni) {
		getGregDComunis().add(gregDComuni);
		gregDComuni.setGregDProvince(this);

		return gregDComuni;
	}

	public GregDComuni removeGregDComuni(GregDComuni gregDComuni) {
		getGregDComunis().remove(gregDComuni);
		gregDComuni.setGregDProvince(null);

		return gregDComuni;
	}

	public GregDRegione getGregDRegione() {
		return this.gregDRegione;
	}

	public void setGregDRegione(GregDRegione gregDRegione) {
		this.gregDRegione = gregDRegione;
	}
	
	public Timestamp getDataFineValidita() {
		return this.dataFineValidita;
	}

	public void setDataFineValidita(Timestamp dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	
	public Timestamp getDataInizioValidita() {
		return this.dataInizioValidita;
	}

	public void setDataInizioValidita(Timestamp dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}
}