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

import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregDVoceModA1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModA1;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.util.Converter;


@Repository("modelloA1Dao")
@Transactional(readOnly=true)
public class ModelloA1Dao  {
	@PersistenceContext
	private EntityManager em;
	
	
	public List<GregDVoceModA1> findAllVociModelloA1() {
		
		String hqlQuery = "SELECT distinct f FROM GregDVoceModA1 f "
				+ "LEFT JOIN GregRRendicontazioneModA1 d on f.idVoceModA1 = d.gregDVoceModA1.idVoceModA1 "
				+ "WHERE f.dataCancellazione is null and d.dataCancellazione is null "
				+ "ORDER BY f.ordinamento";

		
		TypedQuery<GregDVoceModA1> query = em.createQuery(hqlQuery, GregDVoceModA1.class);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getDatiModelloA1(Integer idente) {
		
		String hqlQuery = "select distinct f.ordinamento, f.codVoceModA1, e.codIstatComune, e.desComune, d.valore, a.dataInizioValidita, a.dataFineValidita, e.idComune "
				+ "from GregRSchedeEntiGestoriComuni a "
				+ "left join GregDComuni e on e.idComune = a.gregDComuni.idComune "
				+ "left join GregTRendicontazioneEnte c on c.gregTSchedeEntiGestori.idSchedaEnteGestore = a.gregTSchedeEntiGestori.idSchedaEnteGestore "
				+ "left join GregRRendicontazioneModA1 d on e.idComune = d.gregDComuni.idComune and d.gregTRendicontazioneEnte.idRendicontazioneEnte = c.idRendicontazioneEnte "
				+ "left join GregDVoceModA1 f on f.idVoceModA1 = d.gregDVoceModA1.idVoceModA1 "
				+ "where c.idRendicontazioneEnte = :idente "
				+ "and (f.codVoceModA1 not like 'T%' or f.codVoceModA1 is null) "
				+ "and ((c.annoGestione - date_part('year', a.dataInizioValidita)>= 0 "
				+ "and (c.annoGestione - date_part('year', a.dataFineValidita)<= 0 or a.dataFineValidita is null)) " 
				+ "or ((c.annoGestione+1) - date_part('year', a.dataInizioValidita)>= 0 "
				+ "and ((c.annoGestione+1) - date_part('year', a.dataFineValidita)<= 0 or a.dataFineValidita is null)"
				+ "and e.idComune not in ("  //Provare con idComune
				+ "select distinct e.idComune "
				+ "from GregRSchedeEntiGestoriComuni a "
				+ "left join GregDComuni e on e.idComune = a.gregDComuni.idComune "
				+ "left join GregTRendicontazioneEnte c on c.gregTSchedeEntiGestori.idSchedaEnteGestore = a.gregTSchedeEntiGestori.idSchedaEnteGestore "
				+ "left join GregRRendicontazioneModA1 d on e.idComune = d.gregDComuni.idComune and d.gregTRendicontazioneEnte.idRendicontazioneEnte = c.idRendicontazioneEnte "
				+ "left join GregDVoceModA1 f on f.idVoceModA1 = d.gregDVoceModA1.idVoceModA1 "
				+ "where c.idRendicontazioneEnte = :idente "
				+ "and (f.codVoceModA1 not like 'T%' or f.codVoceModA1 is null) "
				+ "and ((c.annoGestione - date_part('year', a.dataInizioValidita)>= 0 "
				+ "and (c.annoGestione - date_part('year', a.dataFineValidita)<= 0 or a.dataFineValidita is null))) " 
				+ "and ((c.annoGestione - date_part('year', e.inizioValiditaComune)>= 0 "
				+ "and (c.annoGestione - date_part('year', e.fineValiditaComune)<= 0 or e.fineValiditaComune is null))) " 
				+ "and a.dataCancellazione is null "
				+ "and c.dataCancellazione is null and d.dataCancellazione is null "
				+ "and e.dataCancellazione is null and f.dataCancellazione is null "
				+ ")"
				+ "))"
				+ "and (((c.annoGestione - date_part('year', e.inizioValiditaComune)>= 0 "
				+ "and (c.annoGestione - date_part('year', e.fineValiditaComune)<= 0 or e.fineValiditaComune is null))) " 
				+ "or ((c.annoGestione+1) - date_part('year', e.inizioValiditaComune)>= 0 "
				+ "and ((c.annoGestione+1) - date_part('year', e.fineValiditaComune)<= 0 or e.fineValiditaComune is null))) "
				+ "and a.dataCancellazione is null "
				+ "and c.dataCancellazione is null and d.dataCancellazione is null "
				+ "and e.dataCancellazione is null and f.dataCancellazione is null "
				+ "order by 4,1 ";
		
		Query query =null;
		query = em.createQuery(hqlQuery,Object[].class);
		query.setParameter("idente", idente);
		ArrayList<Object> entiresult = (ArrayList<Object>) query.getResultList();
		
		return entiresult;
	}
	
    public Object getDatiModelloA1totale(Integer idente) {
    	try {
    		Query query = em.createNativeQuery(""
    				+ " with tab1 as ( " + 
    						"select distinct f.id_voce_mod_a1, f.cod_Voce_Mod_A1 " + 
    						"from Greg_D_Voce_Mod_A1 f " + 
    						"where f.cod_Voce_Mod_A1 like 'T%' " + 
    						"and f.data_cancellazione is NULL ), " + 
    						"tab2 as ( " + 
    						"select id_Voce_Mod_A1, valore, c.id_rendicontazione_ente, c.id_scheda_ente_gestore, c.anno_gestione  " + 
    						"from Greg_R_Rendicontazione_Mod_A1 a " + 
    						"left join greg_t_rendicontazione_ente c on a.id_rendicontazione_ente = c.id_rendicontazione_ente  " + 
    						"where c.id_rendicontazione_ente = :idente " + 
    						"and a.data_cancellazione is null " + 
    						"and c.data_cancellazione is null ) " + 
    						"select distinct tab1.cod_Voce_Mod_A1, tab2.valore " + 
    						"from tab1 left join tab2 on tab1.id_Voce_Mod_A1 = tab2.id_voce_mod_a1 ");    		
    		
//    		String hqlQuery = "select distinct f.codVoceModA1, "
//    				+ "d.valore "
//            		+ "from GregRRendicontazioneModA1 d "
//        			+ "left join GregDVoceModA1 f on f.idVoceModA1 = d.gregDVoceModA1.idVoceModA1 "
//            		+ "left join GregTRendicontazioneEnte c on c.idRendicontazioneEnte = d.gregTRendicontazioneEnte.idRendicontazioneEnte "
//        			+ "where c.gregTSchedeEntiGestori.idSchedaEnteGestore = :idente "
//        			+ "and f.codVoceModA1 like 'T%' " 
//        			+ "and c.annoGestione = :annogestione "
//        			+ "and c.dataCancellazione is null "
//        			+ "and d.dataCancellazione is null "
//        			+ "and f.dataCancellazione is null";
    		
    		
//    		Query query =null;
//    		query = em.createQuery(hqlQuery,Object[].class);
    		query.setParameter("idente", idente);
//    		query.setParameter("annogestione",  Integer.parseInt(Converter.getAnno(new Date())) - 1);
    		Object entiresult = (Object) query.getSingleResult();
    		return entiresult;
      	
    	} catch (NoResultException e) {
    		return null;
    	}
	}
	
	
	public GregDVoceModA1 getVoceByCodVoce(String codVoce) {
		try {
			TypedQuery<GregDVoceModA1> query = 
			em.createNamedQuery("GregDVoceModA1.byCodVoce", GregDVoceModA1.class)
			.setParameter("codVoce", codVoce);		
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregDComuni getComuneByCod(String codComune) {
		try {
			String hqlQuery = "SELECT g FROM GregDComuni g  "
					+ "WHERE g.codIstatComune = :codComune  "
					+ "AND g.dataCancellazione IS NULL";
			
			TypedQuery<GregDComuni> query = 
					em.createQuery(hqlQuery, GregDComuni.class);
			query.setParameter("codComune", codComune);			
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregDComuni getComuneById(Integer idComune) {
		try {
			String hqlQuery = "SELECT g FROM GregDComuni g  "
					+ "WHERE g.idComune = :idComune  "
					+ "AND g.dataCancellazione IS NULL";
			
			TypedQuery<GregDComuni> query = 
					em.createQuery(hqlQuery, GregDComuni.class);
			query.setParameter("idComune", idComune);			
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregTRendicontazioneEnte updateRendicontazione(GregTRendicontazioneEnte rendicontazione) {
		return em.merge(rendicontazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRRendicontazioneModA1 updateModelloA1(GregRRendicontazioneModA1 modelloA1) {
		return em.merge(modelloA1);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertModelloA1(GregRRendicontazioneModA1 modelloA1) {		
		em.persist(modelloA1);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteVoceModelloA1(Integer idvoce) {
		GregRRendicontazioneModA1 vocedel = em.find(GregRRendicontazioneModA1.class, idvoce);

		if (idvoce != null && idvoce != 0) {
				em.remove(vocedel);
			}
	}

	public List<GregDVoceModA1> findSoloImporti() {
		TypedQuery<GregDVoceModA1> query = em.createNamedQuery("GregDVoceModA1.soloImporti", GregDVoceModA1.class);
		return query.getResultList();
	}
	
	public List<GregRRendicontazioneModA1> getAllDatiModelloA1PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneModA1> query = em.createNamedQuery("GregRRendicontazioneModA1.findValideByIdRendicontazioneEnte", GregRRendicontazioneModA1.class);
		query.setParameter("idRendicontazione", idRendicontazione);
		return query.getResultList();
	}
	

	public ModelStatoMod getStatoModelloA1(Integer idRendicontazione) {
		String sqlQuery ="select  "
				+ "case  "
				+ "	when (select count(moda1.*) "
				+ "		from greg_r_rendicontazione_mod_a1 moda1 "
				+ "		where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda1.valore>0 "
				+ "		and moda1.data_cancellazione is null)>0 "
				+ "		then true "
				+ "		else false  "
				+ "end as valorizzato, "
				+ "case "
				+ "	when (select count(distinct comune.*) "
				+ "		from greg_r_rendicontazione_mod_a1 moda1, greg_d_comuni comune "
				+ "		where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda1.id_comune = comune.id_comune  "
				+ "		and moda1.valore>0 "
				+ "		and moda1.data_cancellazione is null)<(cast((select count(distinct comune.*)  "
				+ "		from greg_d_comuni comune left join greg_r_schede_enti_gestori_comuni sc on comune.id_comune = sc.id_comune and  "
				+ "		(rend.anno_gestione - date_part('year', sc.data_inizio_validita)>=0 and (sc.data_fine_validita is null or rend.anno_gestione - date_part('year', sc.data_fine_validita)<=0)) "
				+ "		where comune.data_cancellazione is null "
				+ "		and sc.id_comune = comune.id_comune "
				+ "		and sc.data_cancellazione is null "
				+ "		and sc.id_scheda_ente_gestore = scheda.id_scheda_ente_gestore) as numeric)/2) "
				+ "	then 'NON_COMPILATO' "
				+ "	else 'COMPILATO_PARZIALE' "
				+ "end as stato "
				+ "from greg_t_rendicontazione_ente rend "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  "
				+ "where  "
				+ "rend.data_cancellazione is null  "
				+ "and scheda.data_cancellazione is null  "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "group by rend.id_rendicontazione_ente, scheda.id_scheda_ente_gestore  "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		Object[] result = (Object[]) query.getSingleResult();
		ModelStatoMod stato = new ModelStatoMod(result);
		return stato;
	}
	
	public boolean getValorizzatoModelloA1(Integer idRendicontazione) {
		String sqlQuery ="select  "
				+ "case  "
				+ "	when (select count(moda1.*) "
				+ "		from greg_r_rendicontazione_mod_a1 moda1 "
				+ "		where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda1.valore>0 "
				+ "		and moda1.data_cancellazione is null)>0 "
				+ "		then true "
				+ "		else false  "
				+ "end as valorizzato "
				+ "from greg_t_rendicontazione_ente rend "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  "
				+ "where  "
				+ "rend.data_cancellazione is null  "
				+ "and scheda.data_cancellazione is null  "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "group by rend.id_rendicontazione_ente, scheda.id_scheda_ente_gestore  "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		boolean result = (boolean) query.getSingleResult();
		return result;
	}
}
