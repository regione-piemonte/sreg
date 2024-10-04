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

import it.csi.greg.gregsrv.business.entity.GregDSpesaMissioneProgramma;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneSpesaMissioneProgrammaMacro;
import it.csi.greg.gregsrv.business.entity.GregRSpesaMissioneProgrammaMacro;
import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.util.Converter;


@Repository("macroaggregatiDao")
@Transactional(readOnly=true)
public class MacroaggregatiDao  {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<GregTMacroaggregatiBilancio> findAllMacroaggregati() {
		
		String hqlQuery = "SELECT f FROM GregTMacroaggregatiBilancio f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.ordinamento";

		
		TypedQuery<GregTMacroaggregatiBilancio> query = em.createQuery(hqlQuery, GregTMacroaggregatiBilancio.class);
		return query.getResultList();
	}
	
	public List<GregDSpesaMissioneProgramma> findAllSpesaMissione() {
		
		String hqlQuery = "SELECT f FROM GregDSpesaMissioneProgramma f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.ordinamento";

		
		TypedQuery<GregDSpesaMissioneProgramma> query = em.createQuery(hqlQuery, GregDSpesaMissioneProgramma.class);
		return query.getResultList();
	}
	
	public List<GregRSpesaMissioneProgrammaMacro> findAllRSpesaMacro() {
		
//		String hqlQuery = "SELECT f FROM GregRSpesaMissioneProgrammaMacro f "
//				+ "LEFT JOIN GregDSpesaMissioneProgramma t on f.gregDSpesaMissioneProgramma.idSpesaMissioneProgramma = t.idSpesaMissioneProgramma "
//				+ "LEFT JOIN GregTMacroaggregatiBilancio d on f.gregTMacroaggregatiBilancio.idMacroaggregatoBilancio = d.idMacroaggregatoBilancio "
//				+ "WHERE f.dataCancellazione is null "
//				+ "AND t.dataCancellazione is null AND d.dataCancellazione is null "
//				+ "ORDER BY t.ordinamento, d.ordinamento";

		
		String hqlQuery = "SELECT f FROM GregRSpesaMissioneProgrammaMacro f "
				+ "WHERE f.dataCancellazione is null "
				+ "AND f.gregDSpesaMissioneProgramma.dataCancellazione is null AND f.gregTMacroaggregatiBilancio.dataCancellazione is null "
				+ "ORDER BY f.gregDSpesaMissioneProgramma.ordinamento, f.gregTMacroaggregatiBilancio.ordinamento";

		
		
		TypedQuery<GregRSpesaMissioneProgrammaMacro> query = em.createQuery(hqlQuery, GregRSpesaMissioneProgrammaMacro.class);
		return query.getResultList();
	}
	
	public GregTMacroaggregatiBilancio findMacroaggregatoById(Integer idMacroaggregato) {
		
		TypedQuery<GregTMacroaggregatiBilancio> query = em.createNamedQuery("GregTMacroaggregatiBilancio.findByIdMacroaggregatoBilancioNotDeleted", GregTMacroaggregatiBilancio.class);
		query.setParameter("idMacroaggregatoBilancio", idMacroaggregato);
		return query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> findRendicontazioneEnteByIdScheda(Integer idScheda) {
		
		Query query = em.createNativeQuery(""
				+ "with tab1 as( " + 
				"SELECT distinct d.desc_missione_cartacea, d.cod_spesa_missione_programma, t.cod_macroaggregato_bilancio, d.desc_programma_cartaceo, d.msg_informativo, " +
				"t.des_macroaggregato_bilancio, d.ordinamento ord_rows, t.ordinamento ord_cols, r.id_spesa_missione_programma_macro " + 
				"from greg_d_spesa_missione_programma d, greg_t_macroaggregati_bilancio t, greg_r_spesa_missione_programma_macro r " + 
				"left join greg_r_rendicontazione_spesa_missione_programma_macro dati on dati.id_spesa_missione_programma_macro = r.id_spesa_missione_programma_macro " + 
				"where r.id_macroaggregato_bilancio = t.id_macroaggregato_bilancio and r.id_spesa_missione_programma = d.id_spesa_missione_programma  " +
				"and d.data_cancellazione is null and t.data_cancellazione is null " + 
				"and r.data_cancellazione is null and dati.data_cancellazione is null " + 
				"order by d.ordinamento , t.ordinamento  " + 
				"), " + 
				"tab2 as ( " + 
				"SELECT distinct dati.valore , r.id_spesa_missione_programma_macro, rend.id_rendicontazione_ente, " +
				"rend.id_scheda_ente_gestore, rend.anno_gestione " + 
				"from greg_t_rendicontazione_ente rend,  greg_r_spesa_missione_programma_macro r, greg_r_rendicontazione_spesa_missione_programma_macro dati " + 
				"where dati.id_spesa_missione_programma_macro = r.id_spesa_missione_programma_macro " + 
				"and rend.id_rendicontazione_ente = :idScheda " + 
				"and rend.id_rendicontazione_ente = dati.id_rendicontazione_ente " +
				"and rend.data_cancellazione is null and r.data_cancellazione is null " + 
				"and dati.data_cancellazione is null " + 
				"order by 2 " + 
				") " + 
				"select tab1.id_spesa_missione_programma_macro, tab1.cod_spesa_missione_programma, tab1.cod_macroaggregato_bilancio, tab1.desc_missione_cartacea, tab1.desc_programma_cartaceo, tab1.msg_informativo, " + 
				"tab1.des_macroaggregato_bilancio, tab2.valore, " +
				"tab2.id_rendicontazione_ente, tab2.id_scheda_ente_gestore, tab2.anno_gestione " + 
				"from tab1 left join tab2 on tab1.id_spesa_missione_programma_macro = tab2.id_spesa_missione_programma_macro");
		
		query.setParameter("idScheda", idScheda);
//		query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);
		
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		
		return result;
	}

	public GregRRendicontazioneSpesaMissioneProgrammaMacro findRendicontazioneBySpesaMacroaggregatoEnte(Integer idSpesa, Integer idMacroaggregato, Integer idRendEnte) {
		try {
			TypedQuery<GregRRendicontazioneSpesaMissioneProgrammaMacro> query = 
					em.createNamedQuery("GregRRendicontazioneSpesaMissioneProgrammaMacro.findBySpesaMacroaggregatoEnte", GregRRendicontazioneSpesaMissioneProgrammaMacro.class);
			query.setParameter("idSpesa", idSpesa);
			query.setParameter("idMacroaggregato", idMacroaggregato);
			query.setParameter("idRendEnte", idRendEnte);
			return query.getSingleResult();			
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	public GregRSpesaMissioneProgrammaMacro findSpesaMacroaggregato(Integer idSpesa, Integer idMacroaggregato) {
		TypedQuery<GregRSpesaMissioneProgrammaMacro> query = 
				em.createNamedQuery("GregRSpesaMissioneProgrammaMacro.findBySpesaMacroaggregato", GregRSpesaMissioneProgrammaMacro.class);
		query.setParameter("idSpesa", idSpesa);
		query.setParameter("idMacroaggregato", idMacroaggregato);
		return query.getSingleResult();	
	}
	
	public GregRRendicontazioneSpesaMissioneProgrammaMacro updateRendicontazioneSpesaMissioneProgrammaMacro(GregRRendicontazioneSpesaMissioneProgrammaMacro rendicontazione) {
		return em.merge(rendicontazione);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazioneSpesaMissioneProgrammaMacro(Integer idRendicontazione) {
		GregRRendicontazioneSpesaMissioneProgrammaMacro rendToDelete = em.find(GregRRendicontazioneSpesaMissioneProgrammaMacro.class, idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
				em.remove(rendToDelete);
			}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazioneSpesaMissioneProgrammaMacro(GregRRendicontazioneSpesaMissioneProgrammaMacro rendicontazione) {
			em.persist(rendicontazione);
	}
	
	//procedure recupero dati per invio
	public List<GregRRendicontazioneSpesaMissioneProgrammaMacro> getAllDatiModelloMacroaggregatiPerInvio(Integer idRendiconatazione) {
		TypedQuery<GregRRendicontazioneSpesaMissioneProgrammaMacro> query = 
				em.createNamedQuery("GregRRendicontazioneSpesaMissioneProgrammaMacro.findValideByIdRendicontazioneEnte", GregRRendicontazioneSpesaMissioneProgrammaMacro.class)
				.setParameter("idRendiconatazione", idRendiconatazione);
			
		return query.getResultList();
	}
	
	public ModelStatoMod getStatoModelloMA(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case  "
				+ "	when (select count(rspesamacro.*) "
				+ "		from greg_r_rendicontazione_spesa_missione_programma_macro rspesamacro "
				+ "		where rspesamacro.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and rspesamacro.valore>0 "
				+ "		and rspesamacro.data_cancellazione is null)>0 "
				+ "		then true "
				+ "		else false  "
				+ "end as valorizzato, "
				+ "case "
				+ "	when (select sum(rspesamacro.valore) "
				+ "		from greg_r_rendicontazione_spesa_missione_programma_macro rspesamacro, "
				+ "		greg_r_spesa_missione_programma_macro spesamacro, "
				+ "		greg_d_spesa_missione_programma missione "
				+ "		where rspesamacro.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and	rspesamacro.id_spesa_missione_programma_macro = spesamacro.id_spesa_missione_programma_macro "
				+ "		and spesamacro.id_spesa_missione_programma = missione.id_spesa_missione_programma  "
				+ "		and (missione.cod_spesa_missione_programma = '1' or missione.cod_spesa_missione_programma = '2') "
				+ "		and rspesamacro.valore>0 "
				+ "		and rspesamacro.data_cancellazione is null)>0 and  "
				+ "		(select sum(rspesamacro.valore) "
				+ "		from greg_r_rendicontazione_spesa_missione_programma_macro rspesamacro, "
				+ "		greg_r_spesa_missione_programma_macro spesamacro, "
				+ "		greg_d_spesa_missione_programma missione "
				+ "		where rspesamacro.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and	rspesamacro.id_spesa_missione_programma_macro = spesamacro.id_spesa_missione_programma_macro "
				+ "		and spesamacro.id_spesa_missione_programma = missione.id_spesa_missione_programma  "
				+ "		and (missione.cod_spesa_missione_programma = '3') "
				+ "		and rspesamacro.valore>0 "
				+ "		and rspesamacro.data_cancellazione is null)>0 "
				+ "	then 'COMPILATO_PARZIALE' "
				+ "	else 'NON_COMPILATO' "
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
	
	public boolean getValorizzatoModelloMA(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case  "
				+ "	when (select count(rspesamacro.*) "
				+ "		from greg_r_rendicontazione_spesa_missione_programma_macro rspesamacro "
				+ "		where rspesamacro.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and rspesamacro.valore>0 "
				+ "		and rspesamacro.data_cancellazione is null)>0 "
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
