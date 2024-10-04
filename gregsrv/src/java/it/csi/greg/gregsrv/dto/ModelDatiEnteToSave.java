/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.dto;

import java.util.List;


public class ModelDatiEnteToSave {
	
	
	private List<ModelTabTranche> modelli;
	private ModelProfilo profilo;
	private ModelRendicontazioneEnte rendicontazioneEnte;
	private List<ModelPrestazioniAssociate> prestazioniAssociate;
	private ModelCronologiaEnte cronologia;
	private List<ModelAllegatiAssociati> allegatiAssociati;
	private ModelFileAllegatoToUpload fileIniziale;
	private ModelFileAllegatoToUpload fileFinale;
	private List<ModelFondi> fondi;

	public ModelDatiEnteToSave() { }

	public List<ModelTabTranche> getModelli() {
		return modelli;
	}

	public void setModelli(List<ModelTabTranche> modelli) {
		this.modelli = modelli;
	}

	public ModelProfilo getProfilo() {
		return profilo;
	}

	public void setProfilo(ModelProfilo profilo) {
		this.profilo = profilo;
	}

	public List<ModelPrestazioniAssociate> getPrestazioniAssociate() {
		return prestazioniAssociate;
	}

	public void setPrestazioniAssociate(List<ModelPrestazioniAssociate> prestazioniAssociate) {
		this.prestazioniAssociate = prestazioniAssociate;
	}

	public ModelCronologiaEnte getCronologia() {
		return cronologia;
	}

	public void setCronologia(ModelCronologiaEnte cronologia) {
		this.cronologia = cronologia;
	}

	public List<ModelAllegatiAssociati> getAllegatiAssociati() {
		return allegatiAssociati;
	}

	public void setAllegatiAssociati(List<ModelAllegatiAssociati> allegatiAssociati) {
		this.allegatiAssociati = allegatiAssociati;
	}

	public ModelFileAllegatoToUpload getFileIniziale() {
		return fileIniziale;
	}

	public void setFileIniziale(ModelFileAllegatoToUpload fileIniziale) {
		this.fileIniziale = fileIniziale;
	}

	public ModelFileAllegatoToUpload getFileFinale() {
		return fileFinale;
	}

	public void setFileFinale(ModelFileAllegatoToUpload fileFinale) {
		this.fileFinale = fileFinale;
	}

	public ModelRendicontazioneEnte getRendicontazioneEnte() {
		return rendicontazioneEnte;
	}

	public void setRendicontazioneEnte(ModelRendicontazioneEnte rendicontazioneEnte) {
		this.rendicontazioneEnte = rendicontazioneEnte;
	}

	public List<ModelFondi> getFondi() {
		return fondi;
	}

	public void setFondi(List<ModelFondi> fondi) {
		this.fondi = fondi;
	}

}
