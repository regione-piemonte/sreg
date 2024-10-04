/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDColori;
import it.csi.greg.gregsrv.business.entity.GregDMissione;
import it.csi.greg.gregsrv.business.entity.GregDProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregDSottotitolo;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregDTitolo;
import it.csi.greg.gregsrv.business.entity.GregRProgrammaMissioneTitSottotit;
import it.csi.greg.gregsrv.business.entity.GregRRendMiProTitEnteGestoreModB;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneNonConformitaModB;
import it.csi.greg.gregsrv.dto.EsportaIstatPreg1Preg2;
import it.csi.greg.gregsrv.dto.EsportaIstatSpese;
import it.csi.greg.gregsrv.dto.ModelStatoMod;


@Repository("modelloBDao")
@Transactional(readOnly=true)
public class ModelloBDao  {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<GregDMissione> findAllMissioni(){
		
		String hqlQuery = "SELECT f FROM GregDMissione f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codMissione";

		
		TypedQuery<GregDMissione> query = em.createQuery(hqlQuery, GregDMissione.class);
		return query.getResultList();
	}
	
	public List<GregDProgrammaMissione> findAllProgrammaMissioni(){
		String hqlQuery = "SELECT f FROM GregDProgrammaMissione f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codProgrammaMissione";

		
		TypedQuery<GregDProgrammaMissione> query = em.createQuery(hqlQuery, GregDProgrammaMissione.class);
		return query.getResultList();
	}
	
	public GregDProgrammaMissione findProgrammaMissionibyProgrammaMissione(String codMissione, String codProgramma){
		String hqlQuery = "SELECT f FROM GregDProgrammaMissione f "
				+ "WHERE "
				+ "f.gregDMissione.codMissione=:codMissione "
				+ "AND "
				+ "f.codProgrammaMissione=:codProgramma "
				+ "AND "
				+ "f.dataCancellazione is null "
				+ "ORDER BY f.codProgrammaMissione";

		
		TypedQuery<GregDProgrammaMissione> query = em.createQuery(hqlQuery, GregDProgrammaMissione.class);
		query.setParameter("codMissione", codMissione);
		query.setParameter("codProgramma", codProgramma);
		
		return query.getSingleResult();
	}
	
	public List<GregDTitolo> findAllTitoli(){
		String hqlQuery = "SELECT f FROM GregDTitolo f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codTitolo";

		
		TypedQuery<GregDTitolo> query = em.createQuery(hqlQuery, GregDTitolo.class);
		return query.getResultList();
	}
	
	public List<GregDSottotitolo> findAllSottotitoli(){
		String hqlQuery = "SELECT f FROM GregDSottotitolo f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codSottotitolo";

		
		TypedQuery<GregDSottotitolo> query = em.createQuery(hqlQuery, GregDSottotitolo.class);
		return query.getResultList();
	}
	
	public List<GregDColori> findAllColori(){
		TypedQuery<GregDColori> query = em.createNamedQuery("GregDColori.findAll", GregDColori.class);
		return query.getResultList();
	}
	
	public List<GregRRendMiProTitEnteGestoreModB> findAllValoriModBByEnte(Integer idScheda) {	
		try {
			String hqlQuery = "	SELECT r FROM GregRRendMiProTitEnteGestoreModB r "
					+ "	LEFT JOIN FETCH r.gregTRendicontazioneEnte t "
					+ "	WHERE t.idRendicontazioneEnte = :idScheda "
					+ "and r.dataCancellazione is null "
					+ "and t.dataCancellazione is null ";
//					+ " AND t.annoGestione = :annoGestione ";

			TypedQuery<GregRRendMiProTitEnteGestoreModB> query = em.createQuery(hqlQuery,
					GregRRendMiProTitEnteGestoreModB.class);
			
			query.setParameter("idScheda", idScheda);
//			query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);
			
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregRProgrammaMissioneTitSottotit> findAllTitoliMissioneModB() {	
		try {
			String hqlQuery = "SELECT r FROM GregRProgrammaMissioneTitSottotit r "
					+ "WHERE r.dataCancellazione is null "
					+ "Order by r.codProgrammaMissioneTitSottotit";
			
			TypedQuery<GregRProgrammaMissioneTitSottotit> query = 
					em.createQuery(hqlQuery, GregRProgrammaMissioneTitSottotit.class);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRProgrammaMissioneTitSottotit findTitoliMissioneModBbyMissProTitSottotit(String codMissione, String codProgramma, String codTitolo, String codSottotitolo) {	
		try {
			String hqlQuery = "SELECT r FROM GregRProgrammaMissioneTitSottotit r "
					+ "WHERE "
					+ "r.gregDProgrammaMissione.gregDMissione.codMissione=:codMissione "
					+ "AND "
					+ "r.gregDProgrammaMissione.codProgrammaMissione=:codProgramma "
					+ "AND "
					+ "r.gregDTitolo.codTitolo=:codTitolo "
					+ "AND "
					+ "r.gregDSottotitolo.codSottotitolo=:codSottotitolo "
					+ "AND "
					+ "r.dataCancellazione is null ";
			
			TypedQuery<GregRProgrammaMissioneTitSottotit> query = 
					em.createQuery(hqlQuery, GregRProgrammaMissioneTitSottotit.class);
			query.setParameter("codMissione", codMissione);
			query.setParameter("codProgramma", codProgramma);
			query.setParameter("codTitolo", codTitolo);
			query.setParameter("codSottotitolo", codSottotitolo);
			
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRProgrammaMissioneTitSottotit findTitoliMissioneModBbyMissProTitNoSottotit(String codMissione, String codProgramma, String codTitolo) {	
		try {
			String hqlQuery = "SELECT r FROM GregRProgrammaMissioneTitSottotit r "
					+ "WHERE "
					+ "r.gregDProgrammaMissione.gregDMissione.codMissione=:codMissione "
					+ "AND "
					+ "r.gregDProgrammaMissione.codProgrammaMissione=:codProgramma "
					+ "AND "
					+ "r.gregDTitolo.codTitolo=:codTitolo "
					+ "AND "
					+ "r.gregDSottotitolo is null "
					+ "AND "
					+ "r.dataCancellazione is null ";
			
			TypedQuery<GregRProgrammaMissioneTitSottotit> query = 
					em.createQuery(hqlQuery, GregRProgrammaMissioneTitSottotit.class);
			query.setParameter("codMissione", codMissione);
			query.setParameter("codProgramma", codProgramma);
			query.setParameter("codTitolo", codTitolo);
			
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendMiProTitEnteGestoreModB findRendModBbyMissProTitSottotit(String codMissione, String codProgramma, String codTitolo, String codSottotitolo, Integer idEnte) {	
		try {
			String hqlQuery = "SELECT r FROM GregRRendMiProTitEnteGestoreModB r "
					+ "WHERE "
					+ "r.gregRProgrammaMissioneTitSottotit.gregDProgrammaMissione.gregDMissione.codMissione=:codMissione "
					+ "AND "
					+ "r.gregRProgrammaMissioneTitSottotit.gregDProgrammaMissione.codProgrammaMissione=:codProgramma "
					+ "AND "
					+ "r.gregRProgrammaMissioneTitSottotit.gregDTitolo.codTitolo=:codTitolo "
					+ "AND "
					+ "r.gregRProgrammaMissioneTitSottotit.gregDSottotitolo.codSottotitolo=:codSottotitolo "
					+ "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte "
					+ "AND "
					+ "r.dataCancellazione is null ";
			
			TypedQuery<GregRRendMiProTitEnteGestoreModB> query = 
					em.createQuery(hqlQuery, GregRRendMiProTitEnteGestoreModB.class);
			query.setParameter("codMissione", codMissione);
			query.setParameter("codProgramma", codProgramma);
			query.setParameter("codTitolo", codTitolo);
			query.setParameter("codSottotitolo", codSottotitolo);
			query.setParameter("idEnte", idEnte);
			
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendMiProTitEnteGestoreModB findRendModBbyMissProTitNoSottotit(String codMissione, String codProgramma, String codTitolo, Integer idEnte) {	
		try {
			String hqlQuery = "SELECT r FROM GregRRendMiProTitEnteGestoreModB r "
					+ "WHERE "
					+ "r.gregRProgrammaMissioneTitSottotit.gregDProgrammaMissione.gregDMissione.codMissione=:codMissione "
					+ "AND "
					+ "r.gregRProgrammaMissioneTitSottotit.gregDProgrammaMissione.codProgrammaMissione=:codProgramma "
					+ "AND "
					+ "r.gregRProgrammaMissioneTitSottotit.gregDTitolo.codTitolo=:codTitolo "
					+ "AND "
					+ "r.gregRProgrammaMissioneTitSottotit.gregDSottotitolo is null "
					+ "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte "
					+ "AND "
					+ "r.dataCancellazione is null ";
			
			TypedQuery<GregRRendMiProTitEnteGestoreModB> query = 
					em.createQuery(hqlQuery, GregRRendMiProTitEnteGestoreModB.class);
			query.setParameter("codMissione", codMissione);
			query.setParameter("codProgramma", codProgramma);
			query.setParameter("codTitolo", codTitolo);
			query.setParameter("idEnte", idEnte);
			
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneNonConformitaModB findConformitabyProMissEnte(String codMissione, String codProgramma, Integer idEnte){
		try {
			String hqlQuery = "SELECT r FROM GregRRendicontazioneNonConformitaModB r "
					+ "WHERE "
					+ "r.gregDProgrammaMissione.gregDMissione.codMissione=:codMissione "
					+ "AND "
					+ "r.gregDProgrammaMissione.codProgrammaMissione=:codProgramma "
					+ "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte "
					+ "AND "
					+ "r.dataCancellazione is null ";
			
			TypedQuery<GregRRendicontazioneNonConformitaModB> query = 
					em.createQuery(hqlQuery, GregRRendicontazioneNonConformitaModB.class);
			query.setParameter("codMissione", codMissione);
			query.setParameter("codProgramma", codProgramma);
			query.setParameter("idEnte", idEnte);
			
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendMiProTitEnteGestoreModB updateRendicontazioneProMissTitSottotit(GregRRendMiProTitEnteGestoreModB rendicontazione) {
		return em.merge(rendicontazione);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazioneProMissTitSottotit(Integer idRendicontazione) {
		GregRRendMiProTitEnteGestoreModB rendToDelete = em.find(GregRRendMiProTitEnteGestoreModB.class, idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
				em.remove(rendToDelete);
			}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazioneProMissTitSottotit(GregRRendMiProTitEnteGestoreModB rendicontazione) {
			em.persist(rendicontazione);
	}
	
	public GregRRendicontazioneNonConformitaModB updateRendicontazioneConformita(GregRRendicontazioneNonConformitaModB rendicontazione) {
		return em.merge(rendicontazione);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazioneConformita(Integer idRendicontazione) {
		GregRRendicontazioneNonConformitaModB rendToDelete = em.find(GregRRendicontazioneNonConformitaModB.class, idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
				em.remove(rendToDelete);
			}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazioneConformita(GregRRendicontazioneNonConformitaModB rendicontazione) {
			em.persist(rendicontazione);
	}
	
	//procedure recupero dati per invio
	public List<GregRRendMiProTitEnteGestoreModB> getAllDatiModelloBPerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendMiProTitEnteGestoreModB> query = 
				em.createNamedQuery("GregRRendMiProTitEnteGestoreModB.findValideByIdRendicontazioneEnte", GregRRendMiProTitEnteGestoreModB.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	public List<GregRRendicontazioneNonConformitaModB> getAllDatiConformitaModelloBPerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneNonConformitaModB> query = 
				em.createNamedQuery("GregRRendicontazioneNonConformitaModB.findValideByIdRendicontazioneEnte", GregRRendicontazioneNonConformitaModB.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<EsportaIstatSpese> findSpeseIstat(Integer idRendicontazione, List<GregDTargetUtenza> utenzeIstat){
		try {
			String sqlQuery = "select r.\"Anno di esercizio\", r.\"Codice Regionale Ente\", r.\"Denominazione Ente\", r.\"Modello\", r.\"Codice Cat. ISTAT\", r.\"Descrizione Cat. ISTAT\", r.\"Codice ISTAT\", r.\"Descrizione ISTAT\" ";
				for(GregDTargetUtenza utenza : utenzeIstat) {
					sqlQuery+=", r.\""+utenza.getCodUtenza()+"\" "; 
				}
				sqlQuery+= "from export_utenza_voci_istat_b1_b_app r, greg_t_rendicontazione_ente t, greg_t_schede_enti_gestori s "
					+ "where t.id_rendicontazione_ente = :idRendicontazione "
					+ "and t.id_scheda_ente_gestore = s.id_scheda_ente_gestore  "
					+ "and r.\"Codice Regionale Ente\" = s.codice_regionale  "
					+ "and t.anno_gestione = r.\"Anno di esercizio\" ";
			
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		List<EsportaIstatSpese> spese = new ArrayList<EsportaIstatSpese>();
		
		Iterator<Object> itr = result.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			EsportaIstatSpese dati = new EsportaIstatSpese(obj);
			spese.add(dati);
		}
		
		return spese;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregDTargetUtenza> findAllTargetIstat(Integer annoGestione) {

		String hqlQuery = "SELECT f FROM GregDTargetUtenza f " 
						+ "WHERE f.dataCancellazione is null "
						+ "AND f.gregDTipoFlusso.codTipoFlusso = 'ISTAT' "
						+ "and f.idTargetUtenza in (select r.gregDTargetUtenza.idTargetUtenza from GregRCatUteVocePrestReg2Istat r "
						+ "where r.dataCancellazione is null "
						+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
						+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null))) "
						+ "ORDER BY f.desUtenza";

		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}
	
	public List<GregDTargetUtenza> findAllTarget(Integer annoGestione) {
		
		String hqlQuery = "SELECT f FROM GregDTargetUtenza f "
				+ "WHERE f.dataCancellazione is null AND "
				+ "f.gregDTipoFlusso.codTipoFlusso= 'REG'"
				+ "and f.idTargetUtenza in (select r.gregDTargetUtenza.idTargetUtenza from GregRPrestReg2UtenzeRegionali2 r "
				+ "where r.dataCancellazione is null "
				+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null))) "
				+ "ORDER BY f.codUtenza";

		
		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<EsportaIstatPreg1Preg2> findPreg1Preg2Istat(Integer idRendicontazione, Integer annoGestione, List<GregDTargetUtenza> utenzeIstat, List<GregDTargetUtenza> utenze){
		try {
			String sqlQuery = "select r.\"Anno di esercizio\",r.\"Codice Regionale Ente\",r.\"Denominazione Ente\",r.\"Codice Preg 1\",r.\"Descrizione Preg 1\", r.\"Codice Preg 2\",r.\"Descrizione Preg 2\" ";
				for(GregDTargetUtenza utenza : utenze) {
					sqlQuery+=", r.\""+utenza.getCodUtenza()+"\" "; 
				}
				sqlQuery += ", r.\"Codice Cat. ISTAT\",r.\"Descrizione Cat. ISTAT\",r.\"Codice ISTAT\",r.\"Descrizione ISTAT\" ";
				for(GregDTargetUtenza utenza : utenzeIstat) {
					sqlQuery+=", r.\""+utenza.getCodUtenza()+"\" "; 
				}
				sqlQuery+= "from export_preg1_preg2_utenza_voci_istat_b1_app r, greg_t_rendicontazione_ente t, greg_t_schede_enti_gestori s "
					+ "where t.id_rendicontazione_ente = :idRendicontazione "
					+ "and t.id_scheda_ente_gestore = s.id_scheda_ente_gestore  "
					+ "and r.\"Codice Regionale Ente\" = s.codice_regionale  "
					+ "and t.anno_gestione = r.\"Anno di esercizio\" ";
			
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		List<EsportaIstatPreg1Preg2> spese = new ArrayList<EsportaIstatPreg1Preg2>();
		
		
		Iterator<Object> itr = result.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			EsportaIstatPreg1Preg2 dati = new EsportaIstatPreg1Preg2(obj, findAllTarget(annoGestione).size());
			spese.add(dati);
		}
		
		return spese;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public ModelStatoMod getStatoModelloB(Integer idRendicontazione) {
		String sqlQuery ="select  "
				+ "case  "
				+ "	when (select count(modb.*) "
				+ "		from greg_r_rend_mi_pro_tit_ente_gestore_mod_b modb "
				+ "		where modb.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and modb.valore>0 "
				+ "		and modb.data_cancellazione is null)>0 "
				+ "		then true "
				+ "		else false  "
				+ "end as valorizzato, "
				+ "case  "
				+ "	when (select count(modb.*) "
				+ "		from greg_r_rend_mi_pro_tit_ente_gestore_mod_b modb "
				+ "		where modb.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and modb.valore>0 "
				+ "		and modb.data_cancellazione is null)=0 "
				+ "		then 'NON_COMPILATO' "
				+ "	when (select sum(modb.valore) "
				+ "		from greg_r_rend_mi_pro_tit_ente_gestore_mod_b modb, "
				+ "		greg_r_programma_missione_tit_sottotit promistitsot, "
				+ "		greg_d_programma_missione promiss, "
				+ "		greg_d_missione missione "
				+ "		where modb.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and modb.id_programma_missione_tit_sottotit = promistitsot.id_programma_missione_tit_sottotit  "
				+ "		and promistitsot.id_programma_missione = promiss.id_programma_missione  "
				+ "		and promiss.id_missione = missione.id_missione  "
				+ "		and missione.cod_missione = '01' "
				+ "		and modb.valore>0 "
				+ "		and modb.data_cancellazione is null)>0 and  "
				+ "		(select sum(modb.valore) "
				+ "		from greg_r_rend_mi_pro_tit_ente_gestore_mod_b modb, "
				+ "		greg_r_programma_missione_tit_sottotit promistitsot, "
				+ "		greg_d_programma_missione promiss, "
				+ "		greg_d_missione missione "
				+ "		where modb.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and modb.id_programma_missione_tit_sottotit = promistitsot.id_programma_missione_tit_sottotit  "
				+ "		and promistitsot.id_programma_missione = promiss.id_programma_missione  "
				+ "		and promiss.id_missione = missione.id_missione  "
				+ "		and missione.cod_missione = '12' "
				+ "		and modb.valore>0 "
				+ "		and modb.data_cancellazione is null)>0 "
				+ "		then 'COMPILATO_PARZIALE' "
				+ "		else 'NON_COMPILATO' "
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

	public boolean getValorizzatoModelloB(Integer idRendicontazione) {
		String sqlQuery ="select  "
				+ "case  "
				+ "	when (select count(modb.*) "
				+ "		from greg_r_rend_mi_pro_tit_ente_gestore_mod_b modb "
				+ "		where modb.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and modb.valore>0 "
				+ "		and modb.data_cancellazione is null)>0 "
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
