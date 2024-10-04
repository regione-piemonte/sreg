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
 * The persistent class for the greg_d_causale_comune_ente_mod_a2 database table.
 * 
 */
@Entity
@Table(name="greg_d_causale_comune_ente_mod_a2")
@NamedQueries({
	@NamedQuery(name="GregDCausaleComuneEnteModA2.findAll", query="SELECT g FROM GregDCausaleComuneEnteModA2 g"),
	@NamedQuery(name="GregDCausaleComuneEnteModA2.findAllValide", query="SELECT g FROM GregDCausaleComuneEnteModA2 g where g.dataCancellazione is null"),
	@NamedQuery(name="GregDCausaleComuneEnteModA2.findById", query="SELECT g FROM GregDCausaleComuneEnteModA2 g WHERE g.idCausaleComuneEnteModA2 = :id "
			+ "and g.dataCancellazione is null ")
})
public class GregDCausaleComuneEnteModA2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="GREG_D_CAUSALE_COMUNE_ENTE_MOD_A2_IDCAUSALECOMUNEENTEMODA2_GENERATOR", sequenceName="GREG_D_CAUSALE_COMUNE_ENTE_MO_ID_CAUSALE_COMUNE_ENTE_MOD_A2_SEQ", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GREG_D_CAUSALE_COMUNE_ENTE_MOD_A2_IDCAUSALECOMUNEENTEMODA2_GENERATOR")
	@Column(name="id_causale_comune_ente_mod_a2")
	private Integer idCausaleComuneEnteModA2;

	@Column(name="cod_causale_comune_ente_mod_a2")
	private String codCausaleComuneEnteModA2;

	@Column(name="data_cancellazione")
	private Timestamp dataCancellazione;

	@Column(name="data_creazione")
	private Timestamp dataCreazione;

	@Column(name="data_modifica")
	private Timestamp dataModifica;

	@Column(name="desc_causale_comune_ente_mod_a2")
	private String descCausaleComuneEnteModA2;

	@Column(name="utente_operazione")
	private String utenteOperazione;

	//bi-directional many-to-one association to GregRRendicontazioneComuneEnteModA2
	@JsonIgnore
	@OneToMany(mappedBy="gregDCausaleComuneEnteModA2")
	private Set<GregRRendicontazioneComuneEnteModA2> gregRRendicontazioneComuneEnteModA2s;

	public GregDCausaleComuneEnteModA2() {
	}

	public Integer getIdCausaleComuneEnteModA2() {
		return this.idCausaleComuneEnteModA2;
	}

	public void setIdCausaleComuneEnteModA2(Integer idCausaleComuneEnteModA2) {
		this.idCausaleComuneEnteModA2 = idCausaleComuneEnteModA2;
	}

	public String getCodCausaleComuneEnteModA2() {
		return this.codCausaleComuneEnteModA2;
	}

	public void setCodCausaleComuneEnteModA2(String codCausaleComuneEnteModA2) {
		this.codCausaleComuneEnteModA2 = codCausaleComuneEnteModA2;
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

	public String getDescCausaleComuneEnteModA2() {
		return this.descCausaleComuneEnteModA2;
	}

	public void setDescCausaleComuneEnteModA2(String descCausaleComuneEnteModA2) {
		this.descCausaleComuneEnteModA2 = descCausaleComuneEnteModA2;
	}

	public String getUtenteOperazione() {
		return this.utenteOperazione;
	}

	public void setUtenteOperazione(String utenteOperazione) {
		this.utenteOperazione = utenteOperazione;
	}

	public Set<GregRRendicontazioneComuneEnteModA2> getGregRRendicontazioneComuneEnteModA2s() {
		return this.gregRRendicontazioneComuneEnteModA2s;
	}

	public void setGregRRendicontazioneComuneEnteModA2s(Set<GregRRendicontazioneComuneEnteModA2> gregRRendicontazioneComuneEnteModA2s) {
		this.gregRRendicontazioneComuneEnteModA2s = gregRRendicontazioneComuneEnteModA2s;
	}

	public GregRRendicontazioneComuneEnteModA2 addGregRRendicontazioneComuneEnteModA2(GregRRendicontazioneComuneEnteModA2 gregRRendicontazioneComuneEnteModA2) {
		getGregRRendicontazioneComuneEnteModA2s().add(gregRRendicontazioneComuneEnteModA2);
		gregRRendicontazioneComuneEnteModA2.setGregDCausaleComuneEnteModA2(this);

		return gregRRendicontazioneComuneEnteModA2;
	}

	public GregRRendicontazioneComuneEnteModA2 removeGregRRendicontazioneComuneEnteModA2(GregRRendicontazioneComuneEnteModA2 gregRRendicontazioneComuneEnteModA2) {
		getGregRRendicontazioneComuneEnteModA2s().remove(gregRRendicontazioneComuneEnteModA2);
		gregRRendicontazioneComuneEnteModA2.setGregDCausaleComuneEnteModA2(null);

		return gregRRendicontazioneComuneEnteModA2;
	}

}