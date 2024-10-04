/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDMail;
import it.csi.greg.gregsrv.business.entity.GregRAzioneMail;
import it.csi.greg.gregsrv.business.entity.GregTUser;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;


@Repository("mailDao")
@Transactional(readOnly=true)
public class MailDao  {
	
	@PersistenceContext
	private EntityManager em;
	
	public GregDMail getMailByCod(String codMail) {
		
		TypedQuery<GregDMail> query = em.createNamedQuery("GregDMail.findByCodNotDeleted", GregDMail.class);
		query.setParameter("codMail", codMail);
		return query.getSingleResult();
	}
	
	public GregRAzioneMail getMailByAzione(String codmail) {
		try {
		String hqlQuery ="select a from GregRAzioneMail a,GregDMail b, GregDAzione c "
			+ "where a.gregDAzione.idAzione = c.idAzione "
			+ "and b.idMail = a.gregDMail.idMail "
			+ "and b.codMail = :codmail "
			+ "and a.dataCancellazione is null "
			+ "and b.dataCancellazione is null "
			+ "and c.dataCancellazione is null ";
		
		TypedQuery<GregRAzioneMail> query = em.createQuery(hqlQuery, GregRAzioneMail.class);
		query.setParameter("codmail", codmail);
		GregRAzioneMail email = query.getSingleResult();
		return email;
		}
		catch (NoResultException e) {
		 return null;
		}
	}

	public GregTUser getMailRegione(String codfiscale) {
		
		TypedQuery<GregTUser> query = em.createNamedQuery("GregTUser.findUtenteValido", GregTUser.class);
		query.setParameter("codfiscale", codfiscale);
		return query.getSingleResult();
	}
	
	public Integer sendEmailByText(String sendFrom, String sendTo, String subjectMail, String textMail) {
		
		Query query = em.createNativeQuery(""
				+ "select pgmail(:sendFrom, :sendTo, :subjectMail, :textMail) ");
		
		query.setParameter("sendFrom", sendFrom);
		query.setParameter("sendTo", sendTo);
		query.setParameter("subjectMail", subjectMail);
		query.setParameter("textMail", textMail);
		
		Integer result = (Integer) query.getSingleResult();
		return result;
	}
	
	public Integer sendEmailByHtmlBody(String sendFrom, String sendTo, String subjectMail, String htmlBody) {
		
		Query query = em.createNativeQuery(""
				+ "select pgmail(:sendFrom, :sendTo, :subjectMail, '', :htmlBody) ");
		
		query.setParameter("sendFrom", sendFrom);
		query.setParameter("sendTo", sendTo);
		query.setParameter("subjectMail", subjectMail);
		query.setParameter("htmlBody", htmlBody);
		
		Integer result = (Integer) query.getSingleResult();		
		return result;
	}
	
	public Integer sendEmailByTextHtmlBody(String sendFrom, String sendTo, String subjectMail, String textMail, String htmlBody) {
		
		Query query = em.createNativeQuery(""
				+ "select pgmail(:sendFrom, :sendTo, :subjectMail, :textMail, :htmlBody);");
		
		query.setParameter("sendFrom", sendFrom);
		query.setParameter("sendTo", sendTo);
		query.setParameter("subjectMail", subjectMail);
		query.setParameter("textMail", textMail);
		query.setParameter("htmlBody", htmlBody);
		
		Integer result = (Integer) query.getSingleResult();
		return result;
	}
	
    public ModelUltimoContatto findDatiUltimoContatto(Integer idEnte) {
		
		Query query = em.createNativeQuery(""
				+ "select a.denominazione ,a.email,b.cognome ,b.nome,c.email EmailResponsabile, b.codice_fiscale " +
				"from greg_t_responsabile_ente_gestore b,greg_r_responsabile_contatti c, " +
				"greg_r_ente_gestore_contatti a " +
				"where a.data_fine_validita is null " + 
				"and c.data_fine_validita is null " +
				"and a.id_responsabile_contatti =c.id_responsabile_contatti " + 
				"and b.id_responsabile_ente_gestore = c.id_responsabile_ente_gestore " +
				"and a.id_scheda_ente_gestore = :idEnte " +
				"and b.data_cancellazione is null");
		
		query.setParameter("idEnte", idEnte);
		Object[] obj = (Object[]) query.getSingleResult();
		
		ModelUltimoContatto datiente = new ModelUltimoContatto(obj);
		
		return datiente;
	}
}
