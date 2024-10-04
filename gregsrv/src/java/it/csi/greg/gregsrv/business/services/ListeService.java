/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.dao.impl.ListeDao;
import it.csi.greg.gregsrv.business.entity.GregCMessaggioApplicativo;
import it.csi.greg.gregsrv.business.entity.GregCParametri;
import it.csi.greg.gregsrv.business.entity.GregDMsgInformativo;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;
import it.csi.greg.gregsrv.dto.ModelMsgInformativo;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelStatoRendicontazione;

@Service("listeService")
public class ListeService {

	@Autowired
	protected ListeDao listeDao;
	
	public GregCMessaggioApplicativo getMessaggio(String codParam) {
		try {
		GregCMessaggioApplicativo messaggio = new GregCMessaggioApplicativo();
	 
		messaggio = listeDao.getMessaggio(codParam);
	  
	  return messaggio;
	} catch (NoResultException e) {
		return null;
	}
	}
	
	public ModelParametro getParametro(String codParam) {
		
		GregCParametri parametro = listeDao.getParametro(codParam);
		
		ModelParametro modelParametro = new ModelParametro(parametro);
	  
	  return modelParametro;
	}
	
   public List<ModelParametro> getParametroPerInformativa(String informativa) {
		
		List<GregCParametri> parametri =  new ArrayList<GregCParametri>();
		
		parametri = listeDao.getParametroPerInformativa(informativa);
		
		List<ModelParametro> modelParametro = new ArrayList<ModelParametro>();
		
		for (GregCParametri parametro: parametri) {
			ModelParametro elemento = new ModelParametro(parametro);
			modelParametro.add(elemento);
			}
	  
	  return modelParametro;
	}
	
	public List<ModelMsgInformativo> findMsgInformativi(String sezione) {
		
		List<GregDMsgInformativo> resultList = new ArrayList<GregDMsgInformativo>();
		resultList = listeDao.getMsgInformativiBySection(sezione);
		
		List<ModelMsgInformativo> listaMsgInformativi = new ArrayList<ModelMsgInformativo>();
		for (GregDMsgInformativo msg: resultList) {
			ModelMsgInformativo elemento = new ModelMsgInformativo(msg);
			listaMsgInformativi.add(elemento);
	    }
	  
	  return listaMsgInformativi;
	}
	
	public ModelStatoRendicontazione getStatoRendicontazione(String codStatoRend) {
		
		GregDStatoRendicontazione statoRend = new GregDStatoRendicontazione();
		statoRend = listeDao.getStatoRendicontazione(codStatoRend);
		
		ModelStatoRendicontazione statoRendicontazione = new ModelStatoRendicontazione(statoRend);
	    	  
	  return statoRendicontazione;
	}
	
	public ModelMsgInformativo getMsgInformativiByCodice(String sezione) {
		try {
			GregDMsgInformativo resultList = new GregDMsgInformativo();
			resultList = listeDao.getMsgInformativiByCodice(sezione);
			ModelMsgInformativo elemento = new ModelMsgInformativo(resultList);
			return elemento;
		} catch (NoResultException e) {
			return null;
		}
	}
}
