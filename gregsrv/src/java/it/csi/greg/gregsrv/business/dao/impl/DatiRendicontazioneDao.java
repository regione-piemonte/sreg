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

import it.csi.greg.gregsrv.business.entity.GregDObbligo;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregDTab;
import it.csi.greg.gregsrv.business.entity.GregRCheck;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart1;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.ModelReferenteEnte;
import it.csi.greg.gregsrv.util.Converter;

@Repository("datiRendicontazioneDao")
@Transactional(readOnly = true)
public class DatiRendicontazioneDao {
	@PersistenceContext
	private EntityManager em;

	public GregTRendicontazioneEnte findRendicontazioneByIdScheda(Integer idRendicontazioneEnte) {
		try {
			TypedQuery<GregTRendicontazioneEnte> query = em.createNamedQuery(
					"GregTRendicontazioneEnte.findByIdRendNotDeleted", GregTRendicontazioneEnte.class);
			query.setParameter("idRendicontazioneEnte", idRendicontazioneEnte);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregTRendicontazioneEnte findRendicontazioneByIdSchedaAnno(Integer idSchedaEnte, Integer anno) {
		try {
			TypedQuery<GregTRendicontazioneEnte> query = em.createNamedQuery(
					"GregTRendicontazioneEnte.findByIdSchedaNotDeleted", GregTRendicontazioneEnte.class);
			query.setParameter("idSchedaEnteGestore", idSchedaEnte);
			query.setParameter("annoGestione", anno);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public String contaAllegati(String tipodocumentazione,int idrendicontazione) {
		try {
		 	String hqlQuery = "SELECT count(*) FROM GregTAllegatiRendicontazione s " +
		    "where s.dataCancellazione is null and s.gregTRendicontazioneEnte.idRendicontazioneEnte = " + idrendicontazione +
		 	" and s.tipoDocumentazione = '" + tipodocumentazione + "'";
			Query query = em.createQuery(hqlQuery);
			return query.getSingleResult().toString();
			
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregTSchedeEntiGestori findSchedaEnteByIdScheda(Integer idEnte) {

		TypedQuery<GregTSchedeEntiGestori> query = em
				.createNamedQuery("GregTSchedeEntiGestori.findById", GregTSchedeEntiGestori.class)
				.setParameter("idEnte", idEnte);

		return query.getSingleResult();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertCronologia(GregTCronologia cronologia) {
		em.persist(cronologia);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregTCronologia insertCronologiaCheck(GregTCronologia cronologia) {
		return em.merge(cronologia);
	}


	public GregDStatoRendicontazione getStatoRendicontazione(String cod) {
		try {
			TypedQuery<GregDStatoRendicontazione> query = em.createNamedQuery("GregDStatoRendicontazione.findByCodNotDeleted", GregDStatoRendicontazione.class);
			query.setParameter("cod", cod);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregTRendicontazioneEnte updateRendicontazione(GregTRendicontazioneEnte rendicontazione) {
		return em.merge(rendicontazione);
	}

	public GregRRendicontazioneModAPart1 getRendicontazioneModAByIdRendicontazione(Integer idRendicontazioneEnte, String codTitolo,  String codTipologia, String codVoce) {
		try {
			String hqlQuery = "SELECT g from GregRRendicontazioneModAPart1 g "
					+ "left join fetch g.gregRTitoloTipologiaVoceModA r "
					+ "WHERE r.gregDTitoloModA.codTitoloModA = :codTitolo "
					+ "AND r.gregDTipologiaModA.codTipologiaModA = :codTipologia "
					+ "AND r.gregDVoceModA.codVoceModA = :codVoce "
					+ "AND g.gregTRendicontazioneEnte.idRendicontazioneEnte = :id "
					+ "AND g.dataCancellazione is null "
					+ "AND r.dataCancellazione is null";
			TypedQuery<GregRRendicontazioneModAPart1> query = em
					.createQuery(hqlQuery, GregRRendicontazioneModAPart1.class)
					.setParameter("id", idRendicontazioneEnte).setParameter("codTitolo", codTitolo).setParameter("codVoce", codVoce).setParameter("codTipologia", codTipologia);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public ArrayList<Object> getTabTrancheEnte(Integer idRendicontazione,String codProfilo) {
		try {
		Query query = em.createNativeQuery("select " 
		+"case when c.cod_tranche ='PT' then a.des_tab || ' (I)' "
		+"when c.cod_tranche ='ST' then a.des_tab || ' (II)' "
		+"end title , "
		+"a.redirect link, "
		+"a.des_estesa_tab tooltip, "
		+"case when a.cod_tab ='MA' then 'ModelloMacroaggregati' "
		+"else 'Modello'||a.cod_tab "
		+"end azione, "
		+"a.cod_tab sigla,fragment "
		+"from greg_d_tab a,greg_r_tab_tranche b,greg_d_tranche c,greg_r_ente_tab d, "
		+"greg_d_azione e,greg_r_profilo_azione f,greg_d_profilo g "
		+"where a.id_tab = b.id_tab "
		+"and d.id_rendicontazione_ente = :idRendicontazione "
		+"and d.id_tab = a.id_tab "
		+"and c.id_tranche = b.id_tranche " 
		+"and g.cod_profilo = :codProfilo "
		+"and f.id_profilo = g.id_profilo " 
		+"and e.id_azione = f.id_azione "
		+"and e.cod_azione = case when a.cod_tab ='MA' then 'ModelloMacroaggregati' " 
		+"else 'Modello'||a.cod_tab "
		+"end "
		+"and a.data_cancellazione is null " 
		+"and b.data_cancellazione is null "
		+"and c.data_cancellazione is null "
		+"and d.data_cancellazione is null "
		+"and e.data_cancellazione is null "
		+"and f.data_cancellazione is null "
		+"and g.data_cancellazione is null order by a.ordinamento ");
		
		query.setParameter("codProfilo", codProfilo);
		query.setParameter("idRendicontazione", idRendicontazione);
	
		ArrayList<Object> resultList = (ArrayList<Object>) query.getResultList();
		return resultList;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//	public ArrayList<Object> getTrancheEnte(Integer idEnte) {
//		try {
//		String hqlQuery = "SELECT distinct b.codTranche,b.desTranche " +
//						  "FROM GregDTab a, GregDTranche b,GregDObbligo c,GregRTabTranche d,GregREnteTab e " + 
//						  "WHERE d.gregDTab.idTab = a.idTab and b.idTranche = d.gregDTranche.idTranche " + 
//						  "and e.gregDObbligo.idObbligo = c.idObbligo and e.gregDTab.idTab = a.idTab " +
//						  "and e.gregTSchedeEntiGestori.idSchedaEnteGestore = :idEnte "+
//						  "and e.annoRendicontazione = :anno " +
//						  "and a.dataCancellazione is null " +
//						  "and b.dataCancellazione is null " +
//						  "and c.dataCancellazione is null " +
//						  "and d.dataCancellazione is null " +
//						  "and e.dataCancellazione is null order by a.ordinamento";
//
//		Query query =null;
//		query = em.createQuery(hqlQuery,Object[].class);
//		query.setParameter("idEnte", idEnte);
//		query.setParameter("anno", Integer.parseInt(Converter.getAnno(new Date())) - 1);
//		ArrayList<Object> resultList = (ArrayList<Object>) query.getResultList();
//
//		return resultList;
//		} catch (NoResultException e) {
//			return null;
//		}
//	}
	
	public GregTCronologia findLastCronologiaEnte(Integer idRendicontazione) {
		
		try {			
			TypedQuery<GregTCronologia> query = em.createNamedQuery("GregTCronologia.findByIdNotDeletedDesc", GregTCronologia.class);
			query.setParameter("idRendicontazione", idRendicontazione);
			List<GregTCronologia> resultList = query.getResultList();
			if(resultList != null && !resultList.isEmpty()) return resultList.get(0);
			else return null;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public ArrayList<Object> getTranchePerModelloEnte(Integer idRendicontazione,String modello) {
		try {
		
			Query query = em.createNativeQuery("select b.id_tab,b.cod_tab ,b.des_tab ,b.des_estesa_tab ,b.redirect, "
					+ "b.ordinamento,d.cod_tranche,d.des_tranche ,c.id_obbligo,c.cod_obbligo ,c.des_obbligo,a.id_ente_tab "
			+ "from greg_r_ente_tab a,greg_d_tab b,greg_d_obbligo c,greg_d_tranche d,greg_r_tab_tranche e "
			+ "where a.id_rendicontazione_ente = :idRendicontazione "
			+ "and a.id_tab = b.id_tab "
			+ "and a.id_obbligo = c.id_obbligo "  
			+ "and d.id_tranche = e.id_tranche "
			+ "and a.id_tab = e.id_tab and b.cod_Tab = :modello "
			+ "and a.data_cancellazione is null "
			+ "and b.data_cancellazione is null "
			+ "and c.data_cancellazione is null "
			+ "and d.data_cancellazione is null "
			+ "and e.data_cancellazione is null order by b.ordinamento");
		
		query.setParameter("modello", modello);
		query.setParameter("idRendicontazione", idRendicontazione);
		
		ArrayList<Object> resultList = (ArrayList<Object>) query.getResultList();

		return resultList;
		} catch (NoResultException e) {
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public List<Object> findModelliAssociati(Integer idRendicontazione) {
		
		Query query = em.createNativeQuery("select b.id_tab,b.cod_tab ,b.des_tab ,b.des_estesa_tab ,b.redirect, "
				+ "b.ordinamento,d.cod_tranche ,d.des_tranche ,c.id_obbligo,c.cod_obbligo ,c.des_obbligo,a.id_ente_tab "
		+ "from greg_r_ente_tab a,greg_d_tab b,greg_d_obbligo c,greg_d_tranche d,greg_r_tab_tranche e "
		+ "where a.id_rendicontazione_ente = :idRendicontazione "
		+ "and a.id_tab = b.id_tab "
		+ "and a.id_obbligo = c.id_obbligo "  
		+ "and d.id_tranche = e.id_tranche "
		+ "and a.id_tab = e.id_tab "
		+ "and a.data_cancellazione is null "
		+ "and b.data_cancellazione is null "
		+ "and c.data_cancellazione is null "
		+ "and d.data_cancellazione is null "
		+ "and e.data_cancellazione is null order by b.ordinamento");

		query.setParameter("idRendicontazione", idRendicontazione);
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();

		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
public List<Object> findModelli() {
	
	Query query = em.createNativeQuery("select a.id_tab,a.cod_tab ,a.des_tab ,a.des_estesa_tab ,a.redirect,a.ordinamento "
				//+ "null as cod_tranche,null as des_tranche ,0 as id_obbligo,null as cod_obbligo ,null as des_obbligo,0 as id_ente_tab "
	+ "from greg_d_tab a,greg_r_tab_tranche b "
	+ "where a.id_tab = b.id_tab "
	+ "and a.data_cancellazione is null "
	+ "and b.data_cancellazione is null order by a.ordinamento");
	
	ArrayList<Object> result = (ArrayList<Object>) query.getResultList();

	return result;
}
	public List<GregDObbligo> findAllObbligo() {
		
		try {			
			TypedQuery<GregDObbligo> query = em.createNamedQuery("GregDObbligo.findAll", GregDObbligo.class);
			List<GregDObbligo> resultList = query.getResultList();
			return resultList;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	public List<GregRCheck> findAllMotivazioneByTab(String codTab, Integer idRendicontazione) {
		
		try {			
			TypedQuery<GregRCheck> query = em.createNamedQuery("GregRCheck.findMotivazioneByIdRendTab", GregRCheck.class);
			query.setParameter("codTab", codTab);
			query.setParameter("idRendicontazione", idRendicontazione);
			
			List<GregRCheck> resultList = query.getResultList();
			return resultList;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	public GregDTab getModelloByCod(String codModello) {
		try {
			TypedQuery<GregDTab> query = em.createNamedQuery("GregDTab.findByCod", GregDTab.class);
			query.setParameter("codTab", codModello);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRCheck insertCheck(GregRCheck check) {
		return em.merge(check);
	}

	public Object[] findModelliAssociatiByCod(Integer idRendicontazione, String codTab) {
		
		Query query = em.createNativeQuery("select b.id_tab,b.cod_tab ,b.des_tab ,b.des_estesa_tab ,b.redirect, "
				+ "b.ordinamento,d.cod_tranche ,d.des_tranche ,c.id_obbligo,c.cod_obbligo ,c.des_obbligo,a.id_ente_tab "
		+ "from greg_r_ente_tab a,greg_d_tab b,greg_d_obbligo c,greg_d_tranche d,greg_r_tab_tranche e "
		+ "where a.id_rendicontazione_ente = :idRendicontazione "
		+ "and b.cod_tab= :codTab "
		+ "and a.id_tab = b.id_tab "
		+ "and a.id_obbligo = c.id_obbligo "  
		+ "and d.id_tranche = e.id_tranche "
		+ "and a.id_tab = e.id_tab "
		+ "and a.data_cancellazione is null "
		+ "and b.data_cancellazione is null "
		+ "and c.data_cancellazione is null "
		+ "and d.data_cancellazione is null "
		+ "and e.data_cancellazione is null order by b.ordinamento");

		query.setParameter("idRendicontazione", idRendicontazione);
		query.setParameter("codTab", codTab);
		Object[] result = (Object[]) query.getSingleResult();

		return result;
	}
	
public List<ModelReferenteEnte> findReferenteEnte(Integer idScheda) {
		List<ModelReferenteEnte> referenti = new ArrayList<ModelReferenteEnte>();
		Query query = em.createNativeQuery("select distinct gtu.cognome, gtu.nome, gtu.codice_fiscale, gtu.email, p.cod_profilo "
				+ "from greg_r_lista_enti_gestori leg, greg_t_lista l, greg_d_profilo p, greg_r_user_profilo up, "
				+ "greg_t_user gtu  "
				+ "where  "
				+ "leg.id_scheda_ente_gestore = :idScheda "
				+ "and leg.id_lista = l.id_lista  "
				+ "and up.id_user = gtu.id_user  "
				+ "and up.id_lista = l.id_lista  "
				+ "and up.id_profilo = p.id_profilo  "
				+ "and (p.cod_profilo = 'EG-REF' or p.cod_profilo = 'EG-RESP') "
				+ "and up.data_cancellazione is null "
				+ "and gtu.data_cancellazione is null "
				+ "and leg.data_cancellazione is null "
				+ "and l.data_cancellazione is null "
				+ "order by gtu.cognome, gtu.nome ");

		query.setParameter("idScheda", idScheda);
		List<Object[]> result = (List<Object[]>) query.getResultList();

		for(Object[] o : result) {
			ModelReferenteEnte ref = new ModelReferenteEnte(o);
			referenti.add(ref);
		}
		
		return referenti;
	}
}
