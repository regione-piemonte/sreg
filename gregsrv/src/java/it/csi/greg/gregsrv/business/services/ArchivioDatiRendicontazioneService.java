/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.dao.impl.ArchivioDatiRendicontazioneDao;
import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregDTipoEnte;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.ModelCronologiaEnte;
import it.csi.greg.gregsrv.dto.ModelRicercaEntiGestoriAttivi;
import it.csi.greg.gregsrv.dto.ModelStatoRendicontazione;
import it.csi.greg.gregsrv.dto.RicercaArchivioRendicontazione;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.util.Checker;

@Service("archivioDatiRendicontazioneService")
public class ArchivioDatiRendicontazioneService {
	
	@Autowired
	protected ArchivioDatiRendicontazioneDao archivioDatiRendicontazioneDao;
	
	public List<ModelStatoRendicontazione> findAllStatoRendicontazione() {
		
		List<GregDStatoRendicontazione> resultList = new ArrayList<GregDStatoRendicontazione>();
	 
	    resultList = archivioDatiRendicontazioneDao.findAllStatoRendicontazione();
	    
	    List<ModelStatoRendicontazione> lista = new ArrayList<ModelStatoRendicontazione>();
	    
	    for(GregDStatoRendicontazione stato : resultList) {
	    	ModelStatoRendicontazione elemento = new ModelStatoRendicontazione(stato);
			lista.add(elemento);
		}
	  
	  return lista;
	} 

	public List<GregDComuni> findAllComuni() {
		
		List<GregDComuni> resultList = new ArrayList<GregDComuni>();
	 
	    resultList = archivioDatiRendicontazioneDao.findAllComuni();
	  
	  return resultList;
	} 
	
	public List<GregDTipoEnte> findAllTipoEnte() {
		
		List<GregDTipoEnte> resultList = new ArrayList<GregDTipoEnte>();
	 
	    resultList = archivioDatiRendicontazioneDao.findAllTipoEnte();
	  
	  return resultList;
	} 
	
	public List<ModelRicercaEntiGestoriAttivi> findArchivioRendicontazione(RicercaArchivioRendicontazione ricerca) {
		
		List<GregTSchedeEntiGestori> resultList = new ArrayList<GregTSchedeEntiGestori>();
		
		if(ricerca.getStatoRendicontazione() != null || 
				ricerca.getDenominazioneEnte() != null ||
				ricerca.getComune() != null ||
				ricerca.getTipoEnte() != null ||
				ricerca.getPartitaIva() != null ||
				ricerca.getAnno() != null) {
			resultList = archivioDatiRendicontazioneDao.findArchivioRendicontazioneByValue(ricerca);	
		}
		else {			
			resultList = archivioDatiRendicontazioneDao.findArchivioRendicontazione();
		}	    
	    
	    List<ModelRicercaEntiGestoriAttivi> lista = new ArrayList<ModelRicercaEntiGestoriAttivi>();
	    
//	    for(GregTSchedeEntiGestori scheda : resultList) {
//	    	ModelRicercaEntiGestoriAttivi elemento = new ModelRicercaEntiGestoriAttivi(scheda);
//			lista.add(elemento);
//		}
	  
	  return lista;
	} 
	
	public List<ModelCronologiaEnte> findCronologiaEnte(Integer idScheda) {
		
		List<GregTCronologia> resultList = new ArrayList<GregTCronologia>();
	 
	    resultList = archivioDatiRendicontazioneDao.findCronologiaEnte(idScheda);
	    
	    List<ModelCronologiaEnte> lista = new ArrayList<ModelCronologiaEnte>();
	    
	    for(GregTCronologia cronologia : resultList) {
	    	ModelCronologiaEnte elemento = new ModelCronologiaEnte(cronologia);
			lista.add(elemento);
		}
	  
	  return lista;
	}

	public List<ModelRicercaEntiGestoriAttivi> findSchedeEntiGestoriRendicontazioneConclusa(RicercaEntiGestori ricerca) {

		List<Object> resultList = new ArrayList<Object>();
		List<ModelRicercaEntiGestoriAttivi> lista = new ArrayList<ModelRicercaEntiGestoriAttivi>();
		
		if((ricerca.getStatoRendicontazione()!=null && !ricerca.getStatoRendicontazione().equals(""))  || 
				Checker.isValorizzato(ricerca.getDenominazioneEnte()) ||
				(ricerca.getComune() !=null && !ricerca.getComune().equals("")) ||
				(ricerca.getTipoEnte()!=null && !ricerca.getTipoEnte().equals("")) ||
				(ricerca.getStatoEnte()!=null && !ricerca.getStatoEnte().equals("")) || 
				(ricerca.getAnnoEsercizio() != null && !ricerca.getAnnoEsercizio().equals(""))) {
			resultList = archivioDatiRendicontazioneDao.findSchedeEntiGestoriRendicontaioneConclusaByValue(ricerca);
		}
		
		else {			
			resultList = archivioDatiRendicontazioneDao.findSchedeEntiGestoriRendicontazioneConclusa(ricerca);
		}
		
		Iterator<Object> itr = resultList.iterator();
		while(itr.hasNext()){
			Object[] obj = (Object[]) itr.next();
			lista.add(new ModelRicercaEntiGestoriAttivi(obj));
		}
		
		if((ricerca.getStatoRendicontazione()!=null && !ricerca.getStatoRendicontazione().equals("")) || 
				(ricerca.getAnnoEsercizio() != null && !ricerca.getAnnoEsercizio().equals(""))) {
			for(ModelRicercaEntiGestoriAttivi ente : lista) {
				ente.setRendicontazioni(archivioDatiRendicontazioneDao.findRendicontazioniApertebyIdSchedaAndValue(ente.getIdSchedaEnteGestore(), ricerca));
			}
		} else {
			for(ModelRicercaEntiGestoriAttivi ente : lista) {
				ente.setRendicontazioni(archivioDatiRendicontazioneDao.findAllRendicontazioniApertebyIdScheda(ente.getIdSchedaEnteGestore()));
			}
		}

		
		return lista;
	} 
	
	public ModelStatoRendicontazione findStatoRendicontazioneConclusa() {

		GregDStatoRendicontazione resultList = new GregDStatoRendicontazione();

		resultList = archivioDatiRendicontazioneDao.findStatoRendicontazioneConclusa();

		ModelStatoRendicontazione res = new ModelStatoRendicontazione(resultList);

		
		return res;
	}

	public List<Integer> findAllAnnoEsercizio() {
		List<Integer> resultList = new ArrayList<Integer>();

		resultList = archivioDatiRendicontazioneDao.findAllAnnoEsercizio();

		return resultList;
	} 

}
