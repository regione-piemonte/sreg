/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDAttivitaSocioAssist;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModE;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.util.Converter;


@Repository("modelleEDao")
@Transactional(readOnly=true)
public class ModelloEDao  {
	
	@PersistenceContext
	private EntityManager em;
	
	
	public List<GregDAttivitaSocioAssist> getAttivitaSocioAssist(){
		
		String hqlQuery = "SELECT f FROM GregDAttivitaSocioAssist f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codAttivitaSocioAssist";
		
		TypedQuery<GregDAttivitaSocioAssist> query = em.createQuery(hqlQuery, GregDAttivitaSocioAssist.class);
		return query.getResultList();
	}
	
	public GregDAttivitaSocioAssist findAttivitaSocioAssistByCod(String codAttivita) {
		TypedQuery<GregDAttivitaSocioAssist> query = em.createNamedQuery("GregDAttivitaSocioAssist.findByCodNotDeleted", GregDAttivitaSocioAssist.class);
		query.setParameter("codAttivita", codAttivita);
		return query.getSingleResult();
	}
	
	public GregRRendicontazioneModE findByIdAttIdComune(Integer idRendEnte, Integer idAttivita, Integer idComune) {
		try {
			TypedQuery<GregRRendicontazioneModE> query = 
					em.createNamedQuery("GregRRendicontazioneModE.findByIdAttIdComune", GregRRendicontazioneModE.class);
			query.setParameter("idRendEnte", idRendEnte);
			query.setParameter("idAttivita", idAttivita);
			query.setParameter("idComune", idComune);
			return query.getSingleResult();			
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneModE updateRendicontazioneModelloE(GregRRendicontazioneModE rendicontazione) {
		return em.merge(rendicontazione);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazioneModelloE(Integer idRendicontazione) {
		GregRRendicontazioneModE rendToDelete = em.find(GregRRendicontazioneModE.class, idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
				em.remove(rendToDelete);
			}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazioneModelloE(GregRRendicontazioneModE rendicontazione) {
		em.persist(rendicontazione);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object> getRendicontazioneModE(Integer idScheda) {
		try {
			Query query = em.createNativeQuery(""
					+ "select rend.id_rendicontazione_ente, gtseg.id_scheda_ente_gestore, rend.anno_gestione, " +
					"gdc.cod_istat_comune, gdc.des_comune, gdc.id_comune, " +
					"grrme.id_rendicontazione_mod_e, gdasa.cod_attivita_socio_assist, grrme.valore " +
					"from greg_r_schede_enti_gestori_comuni grsegc left join greg_t_schede_enti_gestori gtseg on grsegc.id_scheda_ente_gestore = gtseg.id_scheda_ente_gestore " +
					"left join greg_d_comuni gdc on gdc.id_comune = grsegc.id_comune  " +
					"left join greg_t_rendicontazione_ente rend on rend.id_scheda_ente_gestore = gtseg.id_scheda_ente_gestore  " +
					"join greg_r_rendicontazione_mod_e grrme on grrme.id_comune = grsegc.id_comune  " +
					"left join greg_d_attivita_socio_assist gdasa on grrme.id_attivita_socio_assist = gdasa.id_attivita_socio_assist  " +
					"where rend.id_rendicontazione_ente = :idScheda  " +
					"and grrme.valore is not null  "+ 
					"and grrme.id_rendicontazione_ente = rend.id_rendicontazione_ente " +
					"and grsegc.data_cancellazione is null and gtseg.data_cancellazione is null and gdc.data_cancellazione is null " +
					"and rend.data_cancellazione is null and grrme.data_cancellazione is null and gdasa.data_cancellazione is null " +
					"order by gdasa.cod_attivita_socio_assist");

			query.setParameter("idScheda", idScheda);

			ArrayList<Object> result = (ArrayList<Object>) query.getResultList();

			return result;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getAllRendicontazioniModE(Integer idScheda) {
		try {
			Query query = em.createNativeQuery(""
					+ "select distinct id_rendicontazione_mod_e " +
					"from greg_r_schede_enti_gestori_comuni grsegc left join greg_t_schede_enti_gestori gtseg on grsegc.id_scheda_ente_gestore = gtseg.id_scheda_ente_gestore " +
					"left join greg_d_comuni gdc on gdc.id_comune = grsegc.id_comune  " +
					"left join greg_t_rendicontazione_ente rend on rend.id_scheda_ente_gestore = gtseg.id_scheda_ente_gestore  " +
					"join greg_r_rendicontazione_mod_e grrme on grrme.id_comune = grsegc.id_comune  " +
					"left join greg_d_attivita_socio_assist gdasa on grrme.id_attivita_socio_assist = gdasa.id_attivita_socio_assist  " +
					"where gtseg.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  " +
					"and rend.id_rendicontazione_ente = :idScheda  " +
					"and grrme.valore is not null  " +
					"and grsegc.data_cancellazione is null and gtseg.data_cancellazione is null and gdc.data_cancellazione is null " +
					"and rend.data_cancellazione is null and grrme.data_cancellazione is null and gdasa.data_cancellazione is null ");

			query.setParameter("idScheda", idScheda);
			ArrayList<Integer> result = (ArrayList<Integer>) query.getResultList();

			return result;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getAllComuniRendicontazioniModE(Integer idScheda) {
		try {
			Query query = em.createNativeQuery(""
					+ "select distinct grrme.id_comune  " +
					"from greg_r_schede_enti_gestori_comuni grsegc left join greg_t_schede_enti_gestori gtseg on grsegc.id_scheda_ente_gestore = gtseg.id_scheda_ente_gestore " +
					"left join greg_d_comuni gdc on gdc.id_comune = grsegc.id_comune  " +
					"left join greg_t_rendicontazione_ente rend on rend.id_scheda_ente_gestore = gtseg.id_scheda_ente_gestore  " +
					"join greg_r_rendicontazione_mod_e grrme on grrme.id_comune = grsegc.id_comune " + 
					" and grrme.id_rendicontazione_ente = rend.id_rendicontazione_ente  " +
					"left join greg_d_attivita_socio_assist gdasa on grrme.id_attivita_socio_assist = gdasa.id_attivita_socio_assist  " +
					"where gtseg.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  " +
					"and rend.id_rendicontazione_ente = :idScheda  " +
					"and grrme.valore is not null  " +
					"and grsegc.data_cancellazione is null and gtseg.data_cancellazione is null and gdc.data_cancellazione is null " +
					"and rend.data_cancellazione is null and grrme.data_cancellazione is null and gdasa.data_cancellazione is null ");

			query.setParameter("idScheda", idScheda);
			ArrayList<Integer> result = (ArrayList<Integer>) query.getResultList();

			return result;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> findRendicontazioniModEByComune(Integer idScheda, Integer idComune) {
		try {
			Query query = em.createNativeQuery(""
					+ "select distinct id_rendicontazione_mod_e " +
					"from greg_r_schede_enti_gestori_comuni grsegc left join greg_t_schede_enti_gestori gtseg on grsegc.id_scheda_ente_gestore = gtseg.id_scheda_ente_gestore " +
					"left join greg_d_comuni gdc on gdc.id_comune = grsegc.id_comune  " +
					"left join greg_t_rendicontazione_ente rend on rend.id_scheda_ente_gestore = gtseg.id_scheda_ente_gestore  " +
					"join greg_r_rendicontazione_mod_e grrme on grrme.id_comune = grsegc.id_comune  " +
					"left join greg_d_attivita_socio_assist gdasa on grrme.id_attivita_socio_assist = gdasa.id_attivita_socio_assist  " +
					"where gtseg.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  " +
					"and rend.id_rendicontazione_ente = :idScheda  " +
					"and grrme.id_comune = :idComune " +
					"and grrme.valore is not null  " +
					"and grsegc.data_cancellazione is null and gtseg.data_cancellazione is null and gdc.data_cancellazione is null " +
					"and rend.data_cancellazione is null and grrme.data_cancellazione is null and gdasa.data_cancellazione is null ");

			query.setParameter("idScheda", idScheda);
			query.setParameter("idComune", idComune);
			
			ArrayList<Integer> result = (ArrayList<Integer>) query.getResultList();

			return result;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	//procedure recupero dati per invio
	public List<GregRRendicontazioneModE> getAllDatiModelloEPerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneModE> query = 
				em.createNamedQuery("GregRRendicontazioneModE.findValideByIdRendicontazioneEnte", GregRRendicontazioneModE.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	
	public ModelStatoMod getStatoModelloE(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case "
				+ "	when (select count(rende.*)  "
				+ "		from greg_r_rendicontazione_mod_e rende "
				+ "		where rende.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and rende.data_cancellazione is null  "
				+ "		and rende.valore>0)>0 "
				+ "	then true  "
				+ "	else false  "
				+ "end as valorizzato, "
				+ "case "
				+ "	when (select count(rende.*)  "
				+ "		from greg_r_rendicontazione_mod_e rende "
				+ "		where rende.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and rende.data_cancellazione is null  "
				+ "		and rende.valore>0)=0 "
				+ "	then 'NON_COMPILATO'  "
				+ "	else 'COMPILATO_PARZIALE'   "
				+ "end as stato "
				+ "from greg_t_rendicontazione_ente rend "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  "
				+ "where  "
				+ "rend.data_cancellazione is null  "
				+ "and scheda.data_cancellazione is null  "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "group by rend.id_rendicontazione_ente, scheda.codice_regionale "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		Object[] result = (Object[]) query.getSingleResult();
		ModelStatoMod stato = new ModelStatoMod(result);
		return stato;
	}
	
	public boolean getValorizzatoModelloE(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case "
				+ "	when (select count(rende.*)  "
				+ "		from greg_r_rendicontazione_mod_e rende "
				+ "		where rende.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and rende.data_cancellazione is null  "
				+ "		and rende.valore>0)>0 "
				+ "	then true  "
				+ "	else false  "
				+ "end as valorizzato "
				+ "from greg_t_rendicontazione_ente rend "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  "
				+ "where  "
				+ "rend.data_cancellazione is null  "
				+ "and scheda.data_cancellazione is null  "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "group by rend.id_rendicontazione_ente, scheda.codice_regionale "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		boolean result = (boolean) query.getSingleResult();
		return result;
	}
	
}
