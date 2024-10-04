/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDCausaleComuneEnteModA2;
import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneComuneEnteModA2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneEnteComuneModA2;
import it.csi.greg.gregsrv.business.entity.GregTCausaleEnteComuneModA2;
import it.csi.greg.gregsrv.dto.ModelCausali;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.util.SharedConstants;


@Repository("modelloA2Dao")
@Transactional(readOnly=true)
public class ModelloA2Dao  {
	@PersistenceContext
	private EntityManager em;
	
	
	public List<GregTCausaleEnteComuneModA2> getCausali(Integer idRendicontazione) {		
		TypedQuery<GregTCausaleEnteComuneModA2> query = 
				em.createNamedQuery("GregTCausaleEnteComuneModA2.findValideByIdRendicontazioneEnte", GregTCausaleEnteComuneModA2.class)
				.setParameter("idRendicontazione", idRendicontazione);
			
		return query.getResultList();
	}	

	public List<GregDCausaleComuneEnteModA2> getCausaliStatiche() {
		TypedQuery<GregDCausaleComuneEnteModA2> query = 
				em.createNamedQuery("GregDCausaleComuneEnteModA2.findAllValide", GregDCausaleComuneEnteModA2.class);		
		return query.getResultList();
	}

	public List<GregRRendicontazioneEnteComuneModA2> getTrasferimentiEnteComune(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneEnteComuneModA2> query = 
				em.createNamedQuery("GregRRendicontazioneEnteComuneModA2.findValideByIdRendicontazioneEnte", GregRRendicontazioneEnteComuneModA2.class)
				.setParameter("idRendicontazione", idRendicontazione);		
		return query.getResultList();
	}
	
	public List<GregRRendicontazioneComuneEnteModA2> getTrasferimentiComuneEnte(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneComuneEnteModA2> query = 
				em.createNamedQuery("GregRRendicontazioneComuneEnteModA2.findValideByIdRendicontazioneEnte", GregRRendicontazioneComuneEnteModA2.class)
				.setParameter("idRendicontazione", idRendicontazione);		
		return query.getResultList();
	}

	public List<GregTCausaleEnteComuneModA2> getAllCausaliByIdEnte(Integer idRendicontazione) {		
		TypedQuery<GregTCausaleEnteComuneModA2> query = 
				em.createNamedQuery("GregTCausaleEnteComuneModA2.findByIdRendicontazioneEnte", GregTCausaleEnteComuneModA2.class)
				.setParameter("idRendicontazione", idRendicontazione);
			
		return query.getResultList();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregTCausaleEnteComuneModA2 saveCausale(GregTCausaleEnteComuneModA2 cDb) {
		return em.merge(cDb);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void eliminaCausali(List<Integer> ids, Integer idEnte) {
		String hqlQuery = "SELECT g FROM GregTCausaleEnteComuneModA2 g " + 
						  "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idEnte " + 
						  "and g.dataCancellazione is null";
		if(ids.size() > 0) hqlQuery += " and g.idCausaleEnteComuneModA2 NOT IN (:ids)";
		TypedQuery<GregTCausaleEnteComuneModA2> query = 
				em.createQuery(hqlQuery, GregTCausaleEnteComuneModA2.class)
				.setParameter("idEnte", idEnte);
		if(ids.size() > 0) query.setParameter("ids", ids);
		List<GregTCausaleEnteComuneModA2> resultList = query.getResultList();
		for(GregTCausaleEnteComuneModA2 res : resultList) {
			res.setDataCancellazione(new Timestamp(System.currentTimeMillis()));
			res.setDataModifica(new Timestamp(System.currentTimeMillis()));
			em.merge(res);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void eliminaTrasferimentiEnteComune(List<Integer> ids, Integer idEnte) {
		String hqlQuery = "SELECT g FROM GregRRendicontazioneEnteComuneModA2 g "
				+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idEnte "
				+ "AND g.dataCancellazione IS NULL";
		if(ids.size() > 0) hqlQuery += " and g.idRendicontazioneEnteComuneModA2 NOT IN (:ids)";
		TypedQuery<GregRRendicontazioneEnteComuneModA2> query = 
				em.createQuery(hqlQuery, GregRRendicontazioneEnteComuneModA2.class)
				.setParameter("idEnte", idEnte);
		if(ids.size() > 0) query.setParameter("ids", ids);
		List<GregRRendicontazioneEnteComuneModA2> resultList = query.getResultList();
		for(GregRRendicontazioneEnteComuneModA2 res : resultList) {
			res.setDataCancellazione(new Timestamp(System.currentTimeMillis()));
			res.setDataModifica(new Timestamp(System.currentTimeMillis()));
			em.merge(res);
		}
	}

	public List<GregRRendicontazioneEnteComuneModA2> getAllTrasferimentiEnteByIdEnte(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneEnteComuneModA2> query = 
				em.createNamedQuery("GregRRendicontazioneEnteComuneModA2.findByIdRendicontazioneEnte", GregRRendicontazioneEnteComuneModA2.class)
				.setParameter("idRendicontazione", idRendicontazione);		
		return query.getResultList();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRRendicontazioneEnteComuneModA2 saveTrasferimentoEnteComune(GregRRendicontazioneEnteComuneModA2 tDb) {
		return em.merge(tDb);
	}

	public GregTCausaleEnteComuneModA2 getGregTCausaleEnteComuneModA2(ModelCausali causale, Integer idRendicontazione) {
		String hqlQuery = "SELECT g from GregTCausaleEnteComuneModA2 g WHERE 1=1"
						+ " and g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idRendicontazione "
						+ "and g.dataCancellazione is null ";
		if(causale.getId() != null) hqlQuery += " AND g.idCausaleEnteComuneModA2 = :id";
		if(causale.getDescrizione() != null) hqlQuery += " AND UPPER(g.descCausaleEnteComuneModA2) = UPPER(:descrizione)";
		try {
			TypedQuery<GregTCausaleEnteComuneModA2> query = em.createQuery(hqlQuery, GregTCausaleEnteComuneModA2.class);
			query.setParameter("idRendicontazione", idRendicontazione);
			if(causale.getId() != null) query.setParameter("id", causale.getId());
			if(causale.getDescrizione() != null) query.setParameter("descrizione", causale.getDescrizione());
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregDComuni getComuneById(Integer idComune) {
		try {
			TypedQuery<GregDComuni> query = 
			em.createNamedQuery("GregDComuni.findById", GregDComuni.class)
			.setParameter("idComune", idComune);		
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void eliminaTrasferimentiComuneEnte(List<Integer> ids, Integer idEnte) {
		String hqlQuery = "SELECT g FROM GregRRendicontazioneComuneEnteModA2 g "
				+ "WHERE g.gregTRendicontazioneEnte.idRendicontazioneEnte = :idEnte "
				+ "AND g.dataCancellazione IS NULL";
		if(ids.size() > 0) hqlQuery += " and g.idRendicontazioneComuneEnteModA2 NOT IN (:ids)";
		TypedQuery<GregRRendicontazioneComuneEnteModA2> query = 
				em.createQuery(hqlQuery, GregRRendicontazioneComuneEnteModA2.class)
				.setParameter("idEnte", idEnte);
		if(ids.size() > 0) query.setParameter("ids", ids);
		List<GregRRendicontazioneComuneEnteModA2> resultList = query.getResultList();
		for(GregRRendicontazioneComuneEnteModA2 res : resultList) {
			res.setDataCancellazione(new Timestamp(System.currentTimeMillis()));
			res.setDataModifica(new Timestamp(System.currentTimeMillis()));
			em.merge(res);
		}
	}

	public List<GregRRendicontazioneComuneEnteModA2> getAllTrasferimentiComuneByIdEnte(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneComuneEnteModA2> query = 
				em.createNamedQuery("GregRRendicontazioneComuneEnteModA2.findByIdRendicontazioneEnte", GregRRendicontazioneComuneEnteModA2.class)
				.setParameter("idRendicontazione", idRendicontazione);		
		return query.getResultList();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRRendicontazioneComuneEnteModA2 saveTrasferimentoComuneEnte(GregRRendicontazioneComuneEnteModA2 tDb) {
		return em.merge(tDb);
	}

	public GregDCausaleComuneEnteModA2 getGregDCausaleComuneEnteModA2(Integer id) {
		try {
			TypedQuery<GregDCausaleComuneEnteModA2> query = 
			em.createNamedQuery("GregDCausaleComuneEnteModA2.findById", GregDCausaleComuneEnteModA2.class)
			.setParameter("id", id);		
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

//	public GregTRendicontazioneEnte getRendicontazioneModA2(Integer idEnte) {
//		try {
//			TypedQuery<GregTRendicontazioneEnte> query = em.createQuery(
//					"SELECT g FROM GregTRendicontazioneEnte g "
//					+ "WHERE g.gregTSchedeEntiGestori.idSchedaEnteGestore = :idSchedaEnteGestore "
//					+ "AND g.dataCancellazione IS NULL "
//					+ "AND g.annoGestione = :annoGestione", GregTRendicontazioneEnte.class);
//			query.setParameter("idSchedaEnteGestore", idEnte);
//			query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);
//			return query.getSingleResult();
//		} catch (NoResultException e) {
//			return null;
//		}
//	}
//procedure per invio modello A2
	public List<GregRRendicontazioneEnteComuneModA2> getPerInvioModelloA2EnteComune(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneEnteComuneModA2> query = 
				em.createNamedQuery("GregRRendicontazioneEnteComuneModA2.findValideByIdRendicontazioneEnteAnno", GregRRendicontazioneEnteComuneModA2.class)
				.setParameter("idRendicontazione", idRendicontazione);
		return query.getResultList();
	}
	
	public List<GregRRendicontazioneComuneEnteModA2> getPerInvioModelloA2ComuneEnte(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneComuneEnteModA2> query = 
				em.createNamedQuery("GregRRendicontazioneComuneEnteModA2.findValideByIdRendicontazioneEnteAnno", GregRRendicontazioneComuneEnteModA2.class)
				.setParameter("idRendicontazione", idRendicontazione);		
		return query.getResultList();
	}
	

	public ModelStatoMod getStatoModelloA2(Integer idRendicontazione) {
		String sqlQuery ="select  "
				+ "case  "
				+ "	when (select count(moda2ce.*) "
				+ "		from greg_r_rendicontazione_comune_ente_mod_a2 moda2ce "
				+ "		where moda2ce.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda2ce.valore>0 "
				+ "		and moda2ce.data_cancellazione is null)+ "
				+ "		(select count(moda2ec.*) "
				+ "		from greg_r_rendicontazione_ente_comune_mod_a2 moda2ec "
				+ "		where moda2ec.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda2ec.valore>0 "
				+ "		and moda2ec.data_cancellazione is null)>0 "
				+ "		then true "
				+ "		else false  "
				+ "end as valorizzato, "
				+ "case "
				+ "	when (select count(distinct comune.*) "
				+ "		from greg_r_rendicontazione_comune_ente_mod_a2 moda2ce, greg_d_comuni comune "
				+ "		where moda2ce.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda2ce.id_comune = comune.id_comune  "
				+ "		and moda2ce.valore>0 "
				+ "		and moda2ce.data_cancellazione is null)+ "
				+ "		(select count(distinct comune.*) "
				+ "		from greg_r_rendicontazione_ente_comune_mod_a2 moda2ec, greg_d_comuni comune "
				+ "		where moda2ec.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda2ec.id_comune = comune.id_comune  "
				+ "		and comune.data_cancellazione is null "
				+ "		and moda2ec.valore>0 "
				+ "		and moda2ec.data_cancellazione is null)=0  "
				+ "	then 'NON_COMPILATO' "
				+ "	else 'COMPILATO_PARZIALE' "
				+ "end as stato "
				+ "from greg_t_rendicontazione_ente rend "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  "
				+ "left join greg_r_schede_enti_gestori_comuni sc on scheda.id_scheda_ente_gestore = sc.id_scheda_ente_gestore and  "
				+ "	(rend.anno_gestione - date_part('year', sc.data_inizio_validita)>=0 and (sc.data_fine_validita is null or rend.anno_gestione - date_part('year', sc.data_fine_validita)<=0)) "
				+ "where  "
				+ "rend.data_cancellazione is null  "
				+ "and scheda.data_cancellazione is null  "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "and sc.data_cancellazione is null "
				+ "group by rend.id_rendicontazione_ente, scheda.codice_regionale "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		ModelStatoMod stato = null;
		try {
			Object[] result = (Object[]) query.getSingleResult();
			stato = new ModelStatoMod(result);
		}catch(NoResultException e) {
			stato = new ModelStatoMod();
			stato.setValorizzato(false);
			stato.setStato(SharedConstants.NON_COMPILATO);
		}
		return stato;
	}
	
	public boolean getValorizzatoModelloA2(Integer idRendicontazione) {
		boolean result = false;
		String sqlQuery ="select  "
				+ "case  "
				+ "	when (select count(moda2ce.*) "
				+ "		from greg_r_rendicontazione_comune_ente_mod_a2 moda2ce "
				+ "		where moda2ce.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda2ce.valore>0 "
				+ "		and moda2ce.data_cancellazione is null)+ "
				+ "		(select count(moda2ec.*) "
				+ "		from greg_r_rendicontazione_ente_comune_mod_a2 moda2ec "
				+ "		where moda2ec.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda2ec.valore>0 "
				+ "		and moda2ec.data_cancellazione is null)>0 "
				+ "		then true "
				+ "		else false  "
				+ "end as valorizzato "
				+ "from greg_t_rendicontazione_ente rend "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  "
				+ "left join greg_r_schede_enti_gestori_comuni sc on scheda.id_scheda_ente_gestore = sc.id_scheda_ente_gestore and  "
				+ "	(rend.anno_gestione - date_part('year', sc.data_inizio_validita)>=0 and (sc.data_fine_validita is null or rend.anno_gestione - date_part('year', sc.data_fine_validita)<=0)) "
				+ "where  "
				+ "rend.data_cancellazione is null  "
				+ "and scheda.data_cancellazione is null  "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "and sc.data_cancellazione is null "
				+ "group by rend.id_rendicontazione_ente, scheda.codice_regionale "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
	
		try {
			return result = (boolean) query.getSingleResult();
		}catch(NoResultException e) {
			return result;
		}
	}
	
}
