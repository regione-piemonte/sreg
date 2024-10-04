/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the greg_c_parametri database table.
 * 
 */
@Entity
@Table(name="greg_c_parametri")
@NamedQueries({
	@NamedQuery(name="GregCParametri.findAll", query="SELECT g FROM GregCParametri g"),
	@NamedQuery(name="GregCParametri.findPerCodiceNotDeleted", query="SELECT g FROM GregCParametri g WHERE g.codParametro = :codParametro AND g.dataCancellazione IS NULL"),
	@NamedQuery(name="GregCParametri.findPerInformativaNotDeleted", query="SELECT g FROM GregCParametri g WHERE g.informativa = :informativa AND g.dataCancellazione IS NULL order by g.idParametro")
})
public class GregCParametri implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_C_PARAMETRI_IDPARAMETRO_GENERATOR", sequenceName="GREG_C_PARAMETRI_ID_PARAMETRO_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_C_PARAMETRI_IDPARAMETRO_GENERATOR")
	@Column(name="id_parametro")
	private Integer idParametro;

	@Column(name="cod_parametro")
	private String codParametro;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="des_parametro")
	private String desParametro;

	private String informativa;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	@Column(name="val_bool")
	private Boolean valBool;

	@Column(name="val_data_a")
	private Timestamp valDataA;

	@Column(name="val_data_da")
	private Timestamp valDataDa;

	@Column(name="val_int")
	private Integer valInt;

	@Column(name="val_num")
	private BigDecimal valNum;

	@Column(name="val_text")
	private String valText;

	//bi-directional many-to-one association to GregRParametroRendicontazioneEnte
	@JsonIgnore
	@OneToMany(mappedBy="gregCParametri")
	private Set<GregRParametroRendicontazioneEnte> gregRParametroRendicontazioneEntes;

	public GregCParametri() {
	}

	public Integer getIdParametro() {
		return this.idParametro;
	}

	public void setIdParametro(Integer idParametro) {
		this.idParametro = idParametro;
	}

	public String getCodParametro() {
		return this.codParametro;
	}

	public void setCodParametro(String codParametro) {
		this.codParametro = codParametro;
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

	public String getDesParametro() {
		return this.desParametro;
	}

	public void setDesParametro(String desParametro) {
		this.desParametro = desParametro;
	}

	public String getInformativa() {
		return this.informativa;
	}

	public void setInformativa(String informativa) {
		this.informativa = informativa;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Boolean getValBool() {
		return this.valBool;
	}

	public void setValBool(Boolean valBool) {
		this.valBool = valBool;
	}

	public Timestamp getValDataA() {
		return this.valDataA;
	}

	public void setValDataA(Timestamp valDataA) {
		this.valDataA = valDataA;
	}

	public Timestamp getValDataDa() {
		return this.valDataDa;
	}

	public void setValDataDa(Timestamp valDataDa) {
		this.valDataDa = valDataDa;
	}

	public Integer getValInt() {
		return this.valInt;
	}

	public void setValInt(Integer valInt) {
		this.valInt = valInt;
	}

	public BigDecimal getValNum() {
		return this.valNum;
	}

	public void setValNum(BigDecimal valNum) {
		this.valNum = valNum;
	}

	public String getValText() {
		return this.valText;
	}

	public void setValText(String valText) {
		this.valText = valText;
	}

	public Set<GregRParametroRendicontazioneEnte> getGregRParametroRendicontazioneEntes() {
		return this.gregRParametroRendicontazioneEntes;
	}

	public void setGregRParametroRendicontazioneEntes(Set<GregRParametroRendicontazioneEnte> gregRParametroRendicontazioneEntes) {
		this.gregRParametroRendicontazioneEntes = gregRParametroRendicontazioneEntes;
	}

	public GregRParametroRendicontazioneEnte addGregRParametroRendicontazioneEnte(GregRParametroRendicontazioneEnte gregRParametroRendicontazioneEnte) {
		getGregRParametroRendicontazioneEntes().add(gregRParametroRendicontazioneEnte);
		gregRParametroRendicontazioneEnte.setGregCParametri(this);

		return gregRParametroRendicontazioneEnte;
	}

	public GregRParametroRendicontazioneEnte removeGregRParametroRendicontazioneEnte(GregRParametroRendicontazioneEnte gregRParametroRendicontazioneEnte) {
		getGregRParametroRendicontazioneEntes().remove(gregRParametroRendicontazioneEnte);
		gregRParametroRendicontazioneEnte.setGregCParametri(null);

		return gregRParametroRendicontazioneEnte;
	}

}