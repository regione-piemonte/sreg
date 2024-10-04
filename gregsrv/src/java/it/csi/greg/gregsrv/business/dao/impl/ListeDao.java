/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregCMessaggioApplicativo;
import it.csi.greg.gregsrv.business.entity.GregCParametri;
import it.csi.greg.gregsrv.business.entity.GregDMsgInformativo;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;

@Repository("listeDao")
@Transactional(readOnly=true)
public class ListeDao  {
	@PersistenceContext
	private EntityManager em;
	
	public GregCMessaggioApplicativo getMessaggio(String codParam) {
		TypedQuery<GregCMessaggioApplicativo> query = em.createNamedQuery("GregCMessaggioApplicativo.findPerCodice",GregCMessaggioApplicativo.class);
		query.setParameter("codParam", codParam);
		return query.getSingleResult();
	}
	
	public GregCParametri getParametro(String codParametro) {
		TypedQuery<GregCParametri> query = em.createNamedQuery("GregCParametri.findPerCodiceNotDeleted",GregCParametri.class);
		query.setParameter("codParametro", codParametro);
		return query.getSingleResult();
	}
	
	public List<GregCParametri> getParametroPerInformativa(String informativa) {
		TypedQuery<GregCParametri> query = em.createNamedQuery("GregCParametri.findPerInformativaNotDeleted",GregCParametri.class);
		query.setParameter("informativa", informativa);
		return query.getResultList();
	}
	
	public List<GregDMsgInformativo> getMsgInformativiBySection(String sezione) {
		TypedQuery<GregDMsgInformativo> query = em.createNamedQuery("GregDMsgInformativo.findBySection", GregDMsgInformativo.class);
		query.setParameter("sezione", sezione);
		return query.getResultList(); 
	}
	
	public GregDStatoRendicontazione getStatoRendicontazione(String codStatoRend) {
		TypedQuery<GregDStatoRendicontazione> query = em.createNamedQuery("GregDStatoRendicontazione.findByCodNotDeleted", GregDStatoRendicontazione.class);
		query.setParameter("cod", codStatoRend);
		return query.getSingleResult(); 
	}
	
	public GregDMsgInformativo getMsgInformativiByCodice(String codMsgInformativo) {
		TypedQuery<GregDMsgInformativo> query = em.createNamedQuery("GregDMsgInformativo.findByCodice", GregDMsgInformativo.class);
		query.setParameter("codMsgInformativo", codMsgInformativo);
		return query.getSingleResult(); 
	}
	
}