/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.math.BigDecimal;
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

import it.csi.greg.gregsrv.business.entity.GregDTipoVoce;
import it.csi.greg.gregsrv.business.entity.GregDVoceModD;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModD;
import it.csi.greg.gregsrv.business.entity.GregRTipoVoceModD;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.util.Converter;


@Repository("modelloDDao")
@Transactional(readOnly=true)
public class ModelloDDao  {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<GregDTipoVoce> findAllTipoVociModelloD() {
		
		String hqlQuery = "SELECT f FROM GregDTipoVoce f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.ordinamento";

		
		TypedQuery<GregDTipoVoce> query = em.createQuery(hqlQuery, GregDTipoVoce.class);
		return query.getResultList();
	}
	
	public List<GregDVoceModD> findAllVociModelloD() {
		
		String hqlQuery = "SELECT f FROM GregDVoceModD f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.ordinamento";

		
		TypedQuery<GregDVoceModD> query = em.createQuery(hqlQuery, GregDVoceModD.class);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findSezioniVociModelloD() {
		
		Query query = em.createNativeQuery(" SELECT distinct sezione_modello "
				+ "FROM greg_d_voce_mod_d "
				+ "WHERE data_cancellazione IS NULL "
				+ "ORDER BY sezione_modello");
		
		ArrayList<String> result = (ArrayList<String>) query.getResultList();
		return result;
	}
	
	public List<GregRTipoVoceModD> findAllRTipoVociModelloD() {
		
		String hqlQuery = "SELECT f FROM GregRTipoVoceModD f "
				+ "LEFT JOIN GregDVoceModD t on f.gregDVoceModD.idVoceModD = t.idVoceModD "
				+ "LEFT JOIN GregDTipoVoce d on f.gregDTipoVoce.idTipoVoce = d.idTipoVoce "
				+ "WHERE f.dataCancellazione is null "
				+ "AND t.dataCancellazione is null AND d.dataCancellazione is null "
				+ "ORDER BY t.ordinamento, d.ordinamento";

		
		TypedQuery<GregRTipoVoceModD> query = em.createQuery(hqlQuery, GregRTipoVoceModD.class);
		return query.getResultList();
	}
	
	public GregDTipoVoce findTipoVoceById(Integer idTipoVoce) {
		
		TypedQuery<GregDTipoVoce> query = em.createNamedQuery("GregDTipoVoce.findByIdTipoVoceNotDeleted", GregDTipoVoce.class);
		query.setParameter("idTipoVoce", idTipoVoce);
		return query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> findRendicontazioneEnteByIdScheda(Integer idScheda) {
		
		Query query = em.createNativeQuery(""
				+ "with tab1 as( " + 
				"SELECT distinct d.desc_voce_mod_d, d.cod_voce_mod_d, d.msg_informativo, d.operatore_matematico, d.sezione_modello, " +
				"t.desc_tipo_voce, t.cod_tipo_voce, r.data_entry, d.ordinamento ord_rows, t.ordinamento ord_cols, r.id_tipo_voce_mod_d " + 
				"from greg_d_voce_mod_d d, greg_d_tipo_voce t, greg_r_tipo_voce_mod_d r " + 
				"left join greg_r_rendicontazione_mod_d dati on dati.id_tipo_voce_mod_d = r.id_tipo_voce_mod_d " + 
				"where r.id_tipo_voce = t.id_tipo_voce and r.id_voce_mod_d = d.id_voce_mod_d  " +
				"and d.data_cancellazione is null and t.data_cancellazione is null " + 
				"and r.data_cancellazione is null and dati.data_cancellazione is null " + 
				"order by d.ordinamento , t.ordinamento  " + 
				"), " + 
				"tab2 as ( " + 
				"SELECT distinct dati.valore , r.id_tipo_voce_mod_d, rend.id_rendicontazione_ente, " +
				"rend.id_scheda_ente_gestore, rend.anno_gestione " + 
				"from greg_t_rendicontazione_ente rend,  greg_r_tipo_voce_mod_d r, greg_r_rendicontazione_mod_d dati " + 
				"where dati.id_tipo_voce_mod_d = r.id_tipo_voce_mod_d " + 
//				"and rend.anno_gestione = :annoGestione " +
				"and rend.id_rendicontazione_ente = :idScheda " + 
				"and rend.id_rendicontazione_ente = dati.id_rendicontazione_ente " +
				"and rend.data_cancellazione is null and r.data_cancellazione is null " + 
				"and dati.data_cancellazione is null " + 
				"order by 2 " + 
				") " + 
				"select tab1.id_tipo_voce_mod_d, tab1.desc_voce_mod_d, tab1.cod_voce_mod_d, tab1.msg_informativo, tab1.operatore_matematico, " + 
				"tab1.sezione_modello, tab1.desc_tipo_voce, tab1.cod_tipo_voce, tab1.data_entry, tab2.valore, " +
				"tab2.id_rendicontazione_ente, tab2.id_scheda_ente_gestore, tab2.anno_gestione " + 
				"from tab1 left join tab2 on tab1.id_tipo_voce_mod_d = tab2.id_tipo_voce_mod_d");
		
		query.setParameter("idScheda", idScheda);
//		query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);
		
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		
		return result;
	}

	public GregRRendicontazioneModD findRendicontazioneByVoceEnte(Integer idVoce, Integer idTipoVoce, Integer idRendEnte) {
		try {
			TypedQuery<GregRRendicontazioneModD> query = 
					em.createNamedQuery("GregRRendicontazioneModD.findByVoceTipoVoceEnte", GregRRendicontazioneModD.class);
			query.setParameter("idVoce", idVoce);
			query.setParameter("idTipoVoce", idTipoVoce);
			query.setParameter("idRendEnte", idRendEnte);
			return query.getSingleResult();			
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	public GregRTipoVoceModD findVoceTipoVoce(Integer idVoce, Integer idTipoVoce) {
		TypedQuery<GregRTipoVoceModD> query = 
				em.createNamedQuery("GregRTipoVoceModD.findByVoceTipoVoce", GregRTipoVoceModD.class);
		query.setParameter("idVoce", idVoce);
		query.setParameter("idTipoVoce", idTipoVoce);
		return query.getSingleResult();	
	}
	
	public GregRRendicontazioneModD updateRendicontazioneModelloD(GregRRendicontazioneModD rendicontazione) {
		return em.merge(rendicontazione);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazioneModelloD(Integer idRendicontazione) {
		GregRRendicontazioneModD rendToDelete = em.find(GregRRendicontazioneModD.class, idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
				em.remove(rendToDelete);
			}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazioneModelloD(GregRRendicontazioneModD rendicontazione) {
		em.persist(rendicontazione);
	}
	
	//procedura per invio modello D
	public List<GregRRendicontazioneModD> getModelloDPerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneModD> query = 
				em.createNamedQuery("GregRRendicontazioneModD.findValideByIdRendicontazioneAnno", GregRRendicontazioneModD.class);
		query.setParameter("idRendicontazione", idRendicontazione);
		return query.getResultList();	
	}
	
public List<GregRTipoVoceModD> findTipoVociModelloDInput() {
		
	TypedQuery<GregRTipoVoceModD> query = 
			em.createNamedQuery("GregRTipoVoceModD.findDaCompilare", GregRTipoVoceModD.class);
	
		return query.getResultList();
	}

	
	public BigDecimal calcolaRisultatoAmministrazione(Integer idRendicontazione) {
	
		Query query = em.createNativeQuery(""
				+ "with tab1 as (select coalesce(sum(b.valore),0) val1 " + 
				"from greg_d_voce_mod_d a,greg_r_rendicontazione_mod_d b,greg_r_tipo_voce_mod_d c " +
				"where " +
				"c.id_tipo_voce_mod_d = b.id_tipo_voce_mod_d and a.id_voce_mod_d =c.id_voce_mod_d " + 
				"and b.id_rendicontazione_ente = :idRendicontazione " +
				"and cod_voce_mod_d in ('07','01','02') " +
				"and a.data_cancellazione is null and b.data_cancellazione is null and c.data_cancellazione is null), " +
				"tab2 as( " +
				"select coalesce(sum(b.valore),0) val2 " +
				"from greg_d_voce_mod_d a,greg_r_rendicontazione_mod_d b,greg_r_tipo_voce_mod_d c " +
				"where " +
				"c.id_tipo_voce_mod_d = b.id_tipo_voce_mod_d and a.id_voce_mod_d =c.id_voce_mod_d " + 
				"and b.id_rendicontazione_ente = :idRendicontazione " +
				"and cod_voce_mod_d in ('10','11','09','05','03') " +
				"and a.data_cancellazione is null and b.data_cancellazione is null and c.data_cancellazione is null) " +
				"select val1-val2 from tab1,tab2");
	
		query.setParameter("idRendicontazione", idRendicontazione);
		BigDecimal result = (BigDecimal) query.getSingleResult();
	
		return result;
	}
	
	
	public BigDecimal calcolaTotaleParteDisponibile(Integer idRendicontazione) {
	
		Query query = em.createNativeQuery(""
				+ "select coalesce(sum(b.valore),0) val3 " +
				"from greg_d_voce_mod_d a,greg_r_rendicontazione_mod_d b,greg_r_tipo_voce_mod_d c " +
				"where " +
				"cod_voce_mod_d in ('14','15','16','17','18','21','22','23','24','25','28') and " +
				"c.id_tipo_voce_mod_d = b.id_tipo_voce_mod_d and a.id_voce_mod_d =c.id_voce_mod_d " +
				"and a.data_cancellazione is null and b.data_cancellazione is null and c.data_cancellazione is null " +
				"and b.id_rendicontazione_ente = :idRendicontazione");
	
		query.setParameter("idRendicontazione", idRendicontazione);
		BigDecimal result = (BigDecimal) query.getSingleResult();
	
		return result;
	}
	

	public ModelStatoMod getStatoModelloD(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case  "
				+ "	when (select count(modd.*) "
				+ "		from greg_r_rendicontazione_mod_d modd "
				+ "		where modd.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and modd.valore>0 "
				+ "		and modd.data_cancellazione is null)>0 "
				+ "		then true "
				+ "		else false  "
				+ "end as valorizzato, "
				+ "case "
				+ "	when (select modd.valore  "
				+ "		from greg_r_rendicontazione_mod_d modd, "
				+ "		greg_r_tipo_voce_mod_d rvoce, "
				+ "		greg_d_tipo_voce vocet, "
				+ "		greg_d_voce_mod_d voced  "
				+ "		where modd.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and rvoce.id_tipo_voce_mod_d = modd.id_tipo_voce_mod_d  "
				+ "		and rvoce.id_tipo_voce = vocet.id_tipo_voce  "
				+ "		and rvoce.id_voce_mod_d = voced.id_voce_mod_d  "
				+ "		and vocet.cod_tipo_voce ='03' "
				+ "		and voced.cod_voce_mod_d ='12' "
				+ "		and modd.valore>0 "
				+ "		and modd.data_cancellazione is null)=0  "
				+ "	then 'NON_COMPILATO' "
				+ "	else 'COMPILATO_PARZIALE' "
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
	
	
	public boolean getValorizzatoModelloD(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case  "
				+ "	when (select count(modd.*) "
				+ "		from greg_r_rendicontazione_mod_d modd "
				+ "		where modd.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and modd.valore>0 "
				+ "		and modd.data_cancellazione is null)>0 "
				+ "		then true "
				+ "		else false  "
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
