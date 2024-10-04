/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the greg_r_voci_sottovoci database table.
 * 
 */
@Entity
@Table(name="greg_r_voci_sottovoci")
@NamedQuery(name="GregRVociSottovoci.findAll", query="SELECT g FROM GregRVociSottovoci g where g.dataCancellazione IS NULL")
public class GregRVociSottovoci implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_R_VOCI_SOTTOVOCI_IDVOCESOTTOVOCE_GENERATOR", sequenceName="GREG_R_VOCI_SOTTOVOCI_ID_VOCE_SOTTOVOCE_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_R_VOCI_SOTTOVOCI_IDVOCESOTTOVOCE_GENERATOR")
	@Column(name="id_voce_sottovoce")
	private Integer idVoceSottovoce;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregDSottovoci
	@ManyToOne
	@JoinColumn(name="id_sottovoce")
	private GregDSottovoci gregDSottovoci;

	//bi-directional many-to-one association to GregDVoci
	@ManyToOne
	@JoinColumn(name="id_voce")
	private GregDVoci gregDVoci;

	public GregRVociSottovoci() {
	}

	public Integer getIdVoceSottovoce() {
		return this.idVoceSottovoce;
	}

	public void setIdVoceSottovoce(Integer idVoceSottovoce) {
		this.idVoceSottovoce = idVoceSottovoce;
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

	public GregDSottovoci getGregDSottovoci() {
		return this.gregDSottovoci;
	}

	public void setGregDSottovoci(GregDSottovoci gregDSottovoci) {
		this.gregDSottovoci = gregDSottovoci;
	}

	public GregDVoci getGregDVoci() {
		return this.gregDVoci;
	}

	public void setGregDVoci(GregDVoci gregDVoci) {
		this.gregDVoci = gregDVoci;
	}

}