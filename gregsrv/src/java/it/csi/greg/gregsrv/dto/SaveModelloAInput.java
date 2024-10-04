/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;

public class SaveModelloAInput {

	private Integer idEnte;
	private Integer idRendicontazioneEnte;
	private List<ModelRendicontazioneModAPart3> rendicontazioneModAPart3;
	private VociModelloA rendicontazioneModAPart1;
	private ModelCronologiaEnte cronologia;
	private ModelProfilo profilo;
	
	public SaveModelloAInput() { }
	

	public Integer getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Integer idEnte) {
		this.idEnte = idEnte;
	}

	public ModelCronologiaEnte getCronologia() {
		return cronologia;
	}

	public void setCronologia(ModelCronologiaEnte cronologia) {
		this.cronologia = cronologia;
	}

	public List<ModelRendicontazioneModAPart3> getRendicontazioneModAPart3() {
		return rendicontazioneModAPart3;
	}



	public void setRendicontazioneModAPart3(List<ModelRendicontazioneModAPart3> rendicontazioneModAPart3) {
		this.rendicontazioneModAPart3 = rendicontazioneModAPart3;
	}


	public VociModelloA getRendicontazioneModAPart1() {
		return rendicontazioneModAPart1;
	}


	public void setRendicontazioneModAPart1(VociModelloA rendicontazioneModAPart1) {
		this.rendicontazioneModAPart1 = rendicontazioneModAPart1;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}


	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}


	public Integer getIdRendicontazioneEnte() {
		return idRendicontazioneEnte;
	}


	public void setIdRendicontazioneEnte(Integer idRendicontazioneEnte) {
		this.idRendicontazioneEnte = idRendicontazioneEnte;
	}
	
}

