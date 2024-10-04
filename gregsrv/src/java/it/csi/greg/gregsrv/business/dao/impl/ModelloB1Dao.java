/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDMsgInformativo;
import it.csi.greg.gregsrv.business.entity.GregDProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRCartaServiziPreg1;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1MacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1PrestReg2;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg2UtenzeRegionali2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg1Macro;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg1Utereg1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg2Utereg2;
import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.util.Converter;

@Repository("modelloB1Dao")
@Transactional(readOnly=true)
public class ModelloB1Dao {

	@PersistenceContext
	private EntityManager em;
	
	public List<GregDProgrammaMissione> findAllProgrammaMissione() {
		
		String hqlQuery = "SELECT f FROM GregDProgrammaMissione f "
				+ "WHERE f.dataCancellazione is null AND "
				+ "f.gregDMissione.idMissione=:idMissione "
				+ "ORDER BY f.codProgrammaMissione";

		
		TypedQuery<GregDProgrammaMissione> query = em.createQuery(hqlQuery, GregDProgrammaMissione.class);
		query.setParameter("idMissione", 3);
		return query.getResultList();
	}
	
	public List<GregTMacroaggregatiBilancio> findAllMacroaggregati(Integer annoGestione) {
		String hqlQuery = "SELECT f FROM GregTMacroaggregatiBilancio f "
				+ "WHERE f.dataCancellazione is null "
				+ "and f.idMacroaggregatoBilancio in (select r.gregTMacroaggregatiBilancio.idMacroaggregatoBilancio from GregRPrestReg1MacroaggregatiBilancio r "
				+ "where r.dataCancellazione is null "
				+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null))) "
				+ "ORDER BY f.codMacroaggregatoBilancio";

		
		TypedQuery<GregTMacroaggregatiBilancio> query = em.createQuery(hqlQuery, GregTMacroaggregatiBilancio.class);
		query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}
	
	
	public List<GregDMsgInformativo> findAllMsgInformativi() {
		TypedQuery<GregDMsgInformativo> query = em.createNamedQuery("GregDMsgInformativo.findBySection", GregDMsgInformativo.class);
		query.setParameter("sezione", "MODELLO B1");
		return query.getResultList();
	}
	
	
	public List<GregDTargetUtenza> findAllLabelByFlusso(int id_tipo_flusso, Integer annoGestione) {
		
		String hqlQuery = "SELECT f FROM GregDTargetUtenza f "
				+ "WHERE f.dataCancellazione is null AND "
				+ "f.gregDTipoFlusso.idTipoFlusso=:id_tipo_flusso "
				+ "and f.idTargetUtenza in (select r.gregDTargetUtenza.idTargetUtenza from GregRPrestReg1UtenzeRegionali1 r "
				+ "where r.dataCancellazione is null "
				+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null))) "
				+ "ORDER BY f.codUtenza";

		
		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("id_tipo_flusso", id_tipo_flusso);
		query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}
	
	public List<GregRCartaServiziPreg1> findAllPrestazioni(int id_scheda_ente) {
		
		String hqlQuery = "SELECT f FROM GregRCartaServiziPreg1 f "
				+ "LEFT JOIN FETCH f.gregTPrestazioniRegionali1 r "
				+ "WHERE f.dataCancellazione is null "
				+ "and r.dataCancellazione is null "
				+ "and (f.gregTRendicontazioneEnte.annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (f.gregTRendicontazioneEnte.annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null)) "
				+ "and f.gregTRendicontazioneEnte.idRendicontazioneEnte = :id_scheda_ente AND "
				+ "f.gregTPrestazioniRegionali1.gregDTipologia.codTipologia<>'MA05' "
				+ "ORDER BY f.gregTPrestazioniRegionali1.ordinamento";

		
		TypedQuery<GregRCartaServiziPreg1> query = em.createQuery(hqlQuery, GregRCartaServiziPreg1.class);
		query.setParameter("id_scheda_ente", id_scheda_ente);
		return query.getResultList();
	}
	
//	public GregTRendicontazioneEnte findRendicontazioneEnteGestore(int id_scheda_ente, int anno) {
//		TypedQuery<GregTRendicontazioneEnte> query = em.createNamedQuery("GregTRendicontazioneEnte.findByIdSchedaNotDeleted", GregTRendicontazioneEnte.class);
//		query.setParameter("idSchedaEnteGestore", id_scheda_ente);
//		query.setParameter("annoGestione", anno);
//		List<GregTRendicontazioneEnte> results = query.getResultList();
//	    if (!results.isEmpty() && results.size() == 1) {
//	    	return results.get(0);
//	    } 
//		return null;
//	}
	
	public List<GregRPrestReg1MacroaggregatiBilancio> findAllVociMacroaggregati(int id_prest_reg_1, int annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPrestReg1MacroaggregatiBilancio f "
				+ "LEFT JOIN FETCH f.gregTPrestazioniRegionali1 r "
				+ "WHERE f.dataCancellazione is null "
				+ "and r.dataCancellazione is null "
				+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null)) "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) "
				+ "and f.gregTPrestazioniRegionali1.idPrestReg1 = :id_prest_reg_1 "
				+ "ORDER BY f.gregTMacroaggregatiBilancio.codMacroaggregatoBilancio";
		
		TypedQuery<GregRPrestReg1MacroaggregatiBilancio> query = em.createQuery(hqlQuery, GregRPrestReg1MacroaggregatiBilancio.class);
		query.setParameter("id_prest_reg_1", id_prest_reg_1);
		query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}
	
	public GregRRendicontazionePreg1Macro findValoreMacroaggregato(int id_prest_reg_1_macroaggregati_bilancio, int id_rendicontazione_ente) {
		String hqlQuery = "SELECT f FROM GregRRendicontazionePreg1Macro f "
				+ "WHERE f.dataCancellazione is null AND "
				+ "f.gregTRendicontazioneEnte.idRendicontazioneEnte = :id_rendicontazione_ente AND "
				+ "f.gregRPrestReg1MacroaggregatiBilancio.idPrestReg1MacroaggregatiBilancio = :id_prest_reg_1_macroaggregati_bilancio "
				+ "and f.gregRPrestReg1MacroaggregatiBilancio.dataCancellazione is null "
				+ "and (f.gregTRendicontazioneEnte.annoGestione - year(f.gregRPrestReg1MacroaggregatiBilancio.dataInizioValidita)>=0 "
				+ "and (f.gregTRendicontazioneEnte.annoGestione - year(f.gregRPrestReg1MacroaggregatiBilancio.dataFineValidita)<=0 or f.gregRPrestReg1MacroaggregatiBilancio.dataFineValidita is null)) ";

		
		TypedQuery<GregRRendicontazionePreg1Macro> query = em.createQuery(hqlQuery, GregRRendicontazionePreg1Macro.class);
		query.setParameter("id_prest_reg_1_macroaggregati_bilancio", id_prest_reg_1_macroaggregati_bilancio);
		query.setParameter("id_rendicontazione_ente", id_rendicontazione_ente);
		
		List<GregRRendicontazionePreg1Macro> results = query.getResultList();
	    if (!results.isEmpty() && results.size() == 1) {
	    	return results.get(0);
	    } 
		return null;
	}
	
	public GregRRendicontazionePreg1Utereg1 findValorePreg1UtenzaRegionale1(int id_prest_reg_1_utenza_regionale_1, int id_rendicontazione_ente) {
		String hqlQuery = "SELECT f FROM GregRRendicontazionePreg1Utereg1 f "
				+ "WHERE f.dataCancellazione is null AND "
				+ "f.gregTRendicontazioneEnte.idRendicontazioneEnte = :id_rendicontazione_ente AND "
				+ "f.gregRPrestReg1UtenzeRegionali1.idPrestReg1UtenzaRegionale1 = :id_prest_reg_1_utenza_regionale_1 "
				+ "and f.gregRPrestReg1UtenzeRegionali1.dataCancellazione is null "
				+ "and (f.gregTRendicontazioneEnte.annoGestione - year(f.gregRPrestReg1UtenzeRegionali1.dataInizioValidita)>=0 "
				+ "and (f.gregTRendicontazioneEnte.annoGestione - year(f.gregRPrestReg1UtenzeRegionali1.dataFineValidita)<=0 or f.gregRPrestReg1UtenzeRegionali1.dataFineValidita is null)) ";

		
		TypedQuery<GregRRendicontazionePreg1Utereg1> query = em.createQuery(hqlQuery, GregRRendicontazionePreg1Utereg1.class);
		query.setParameter("id_prest_reg_1_utenza_regionale_1", id_prest_reg_1_utenza_regionale_1);
		query.setParameter("id_rendicontazione_ente", id_rendicontazione_ente);
		
		List<GregRRendicontazionePreg1Utereg1> results = query.getResultList();
	    if (!results.isEmpty() && results.size() == 1) {
	    	return results.get(0);
	    } 
		return null;
	}
	
	public List<GregRPrestReg1PrestReg2> findAllPrestReg2(int id_prestazione_regionale_1, int annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPrestReg1PrestReg2 f "
				+ "LEFT JOIN FETCH f.gregTPrestazioniRegionali1 r "
				+ "LEFT JOIN FETCH f.gregTPrestazioniRegionali2 g "
				+ "WHERE f.dataCancellazione is null "
				+ "and r.dataCancellazione is null "
				+ "and g.dataCancellazione is null "
				+ "and f.dataCancellazione is null "
				+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null)) "
				+ "and (:annoGestione - year(g.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(g.dataFineValidita)<=0 or g.dataFineValidita is null)) "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) "
				+ "and f.gregTPrestazioniRegionali1.idPrestReg1=:id_prestazione_regionale_1 "
				+ "ORDER BY f.gregTPrestazioniRegionali2.ordinamento";
	
		
		TypedQuery<GregRPrestReg1PrestReg2> query = em.createQuery(hqlQuery, GregRPrestReg1PrestReg2.class);
		query.setParameter("id_prestazione_regionale_1", id_prestazione_regionale_1);
		query.setParameter("annoGestione", annoGestione);
		
		return query.getResultList();
	}
	
	public List<GregRPrestReg2UtenzeRegionali2> findAllUtenzePrestazioniRegionali2(int id_prestazione_regionale_2, int annoGestione) {
		
		String hqlQuery = "SELECT f FROM GregRPrestReg2UtenzeRegionali2 f "
				+ "LEFT JOIN FETCH f.gregTPrestazioniRegionali2 r "
				+ "WHERE f.dataCancellazione is null "
				+ "and r.dataCancellazione is null "
				+ "and f.dataCancellazione is null "
				+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null)) "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) "
				+ "and f.gregTPrestazioniRegionali2.idPrestReg2=:id_prestazione_regionale_2 "
				+ "ORDER BY f.gregDTargetUtenza.codUtenza";
	
		
		TypedQuery<GregRPrestReg2UtenzeRegionali2> query = em.createQuery(hqlQuery, GregRPrestReg2UtenzeRegionali2.class);
		query.setParameter("id_prestazione_regionale_2", id_prestazione_regionale_2);
		query.setParameter("annoGestione", annoGestione);
		
		return query.getResultList();
	}
	
	public GregRRendicontazionePreg2Utereg2 findValorePreg2UtenzaRegionale2(int id_prest_reg_2_utenza_regionale_2, int id_rendicontazione_ente) {
		String hqlQuery = "SELECT f FROM GregRRendicontazionePreg2Utereg2 f "
				+ "WHERE f.dataCancellazione is null AND "
				+ "f.gregTRendicontazioneEnte.idRendicontazioneEnte = :id_rendicontazione_ente AND "
				+ "f.gregRPrestReg2UtenzeRegionali2.idPrestReg2UtenzaRegionale2 = :id_prest_reg_2_utenza_regionale_2 "
				+ "and f.gregRPrestReg1UtenzeRegionali1.dataCancellazione is null "
				+ "and (f.gregTRendicontazioneEnte.annoGestione - year(f.gregRPrestReg2UtenzeRegionali2.dataInizioValidita)>=0 "
				+ "and (f.gregTRendicontazioneEnte.annoGestione - year(f.gregRPrestReg2UtenzeRegionali2.dataFineValidita)<=0 or f.gregRPrestReg2UtenzeRegionali2.dataFineValidita is null)) ";

		
		TypedQuery<GregRRendicontazionePreg2Utereg2> query = em.createQuery(hqlQuery, GregRRendicontazionePreg2Utereg2.class);
		query.setParameter("id_prest_reg_2_utenza_regionale_2", id_prest_reg_2_utenza_regionale_2);
		query.setParameter("id_rendicontazione_ente", id_rendicontazione_ente);
		
		List<GregRRendicontazionePreg2Utereg2> results = query.getResultList();
	    if (!results.isEmpty() && results.size() == 1) {
	    	return results.get(0);
	    } 
		return null;
	}
	
	public List<GregTPrestazioniRegionali1> findPrestReg1QuotaSocioAssistenziale(int id_prest_regionale_1, int annoGestione) {
		String hqlQuery = "SELECT f FROM GregTPrestazioniRegionali1 f "
				+ "LEFT JOIN FETCH f.gregTPrestazioniRegionali1 r "
				+ "WHERE f.dataCancellazione is null "
				+ "and r.dataCancellazione is null "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) "
				+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null)) "
				+ "and f.gregTPrestazioniRegionali1.idPrestReg1 = :id_prest_regionale_1 "
				+ "ORDER BY f.gregDTipologiaQuota.codTipologiaQuota";
				
		
		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
		query.setParameter("id_prest_regionale_1", id_prest_regionale_1);
		query.setParameter("annoGestione", annoGestione);
		 
		return query.getResultList();
	}
	
	public GregRPrestReg1MacroaggregatiBilancio findRPrestReg1MacroaggregatiBilancio (int id_prest_reg_1_macroggregati_bilancio, Integer annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPrestReg1MacroaggregatiBilancio f "
				+ "WHERE f.dataCancellazione is null AND "
				+ "f.idPrestReg1MacroaggregatiBilancio = :id_prest_reg_1_macroggregati_bilancio "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) ";
		
		TypedQuery<GregRPrestReg1MacroaggregatiBilancio> query = em.createQuery(hqlQuery, GregRPrestReg1MacroaggregatiBilancio.class);
		query.setParameter("id_prest_reg_1_macroggregati_bilancio", id_prest_reg_1_macroggregati_bilancio);
		query.setParameter("annoGestione", annoGestione);
		
		List<GregRPrestReg1MacroaggregatiBilancio> results = query.getResultList();
	    if (!results.isEmpty() && results.size()==1) {
	    	return results.get(0);
	    } 
		return null;
	}
	
	public GregRPrestReg1UtenzeRegionali1 findRPrestReg1Utereg1 (int id_prest_reg_1_ute_reg_1, Integer annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPrestReg1UtenzeRegionali1 f "
				+ "WHERE f.dataCancellazione is null AND "
				+ "f.idPrestReg1UtenzaRegionale1 = :id_prest_reg_1_ute_reg_1 "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) ";
		
		TypedQuery<GregRPrestReg1UtenzeRegionali1> query = em.createQuery(hqlQuery, GregRPrestReg1UtenzeRegionali1.class);
		query.setParameter("id_prest_reg_1_ute_reg_1", id_prest_reg_1_ute_reg_1);
		query.setParameter("annoGestione", annoGestione);
		
		List<GregRPrestReg1UtenzeRegionali1> results = query.getResultList();
		if (!results.isEmpty() && results.size()==1) {
			return results.get(0);
		} 
		return null;
	}
	
	public GregRPrestReg2UtenzeRegionali2 findRPrestReg2Utereg2 (int id_prest_reg_2_ute_reg_2, Integer annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPrestReg2UtenzeRegionali2 f "
				+ "WHERE f.dataCancellazione is null AND "
				+ "f.idPrestReg2UtenzaRegionale2 = :id_prest_reg_2_ute_reg_2 "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) ";
		
		TypedQuery<GregRPrestReg2UtenzeRegionali2> query = em.createQuery(hqlQuery, GregRPrestReg2UtenzeRegionali2.class);
		query.setParameter("id_prest_reg_2_ute_reg_2", id_prest_reg_2_ute_reg_2);
		query.setParameter("annoGestione", annoGestione);
		
		List<GregRPrestReg2UtenzeRegionali2> results = query.getResultList();
		if (!results.isEmpty() && results.size()==1) {
			return results.get(0);
		} 
		return null;
	}
	
	//MACROAGGREGATI
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRRendicontazionePreg1Macro updateMacroaggregati(GregRRendicontazionePreg1Macro item) {
		return em.merge(item);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteMacroaggregati(Integer idvoce) {
		GregRRendicontazionePreg1Macro vocedel = em.find(GregRRendicontazionePreg1Macro.class, idvoce);
		if (idvoce != null && idvoce != 0) {
				em.remove(vocedel);
			}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertMacroaggregati(GregRRendicontazionePreg1Macro item) {		
		em.persist(item);
	}
	
	//PREG1 UTEREG1
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRRendicontazionePreg1Utereg1 updatePreg1Utereg1(GregRRendicontazionePreg1Utereg1 item) {
		return em.merge(item);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deletePreg1Utereg1(Integer idvoce) {
		GregRRendicontazionePreg1Utereg1 vocedel = em.find(GregRRendicontazionePreg1Utereg1.class, idvoce);
		if (idvoce != null && idvoce != 0) {
				em.remove(vocedel);
			}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertPreg1Utereg1(GregRRendicontazionePreg1Utereg1 item) {		
		em.persist(item);
	}
	
	//PREG2 UTEREG2
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRRendicontazionePreg2Utereg2 updatePreg2Utereg2(GregRRendicontazionePreg2Utereg2 item) {
		return em.merge(item);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deletePreg2Utereg2(Integer idvoce) {
		GregRRendicontazionePreg2Utereg2 vocedel = em.find(GregRRendicontazionePreg2Utereg2.class, idvoce);
		if (idvoce != null && idvoce != 0) {
				em.remove(vocedel);
			}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertPreg2Utereg2(GregRRendicontazionePreg2Utereg2 item) {		
		em.persist(item);
	}
	
	//procedure recupero dati per invio
	public List<GregRRendicontazionePreg1Macro> getAllDatiModelloB1part1PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazionePreg1Macro> query = 
				em.createNamedQuery("GregRRendicontazionePreg1Macro.findValideByIdRendicontazioneEnte", GregRRendicontazionePreg1Macro.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	public List<GregRRendicontazionePreg1Utereg1> getAllDatiModelloB1part2PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazionePreg1Utereg1> query = 
				em.createNamedQuery("GregRRendicontazionePreg1Utereg1.findValideByIdRendicontazioneEnte", GregRRendicontazionePreg1Utereg1.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	public List<GregRRendicontazionePreg2Utereg2> getAllDatiModelloB1part3PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazionePreg2Utereg2> query = 
				em.createNamedQuery("GregRRendicontazionePreg2Utereg2.findValideByIdRendicontazioneEnte", GregRRendicontazionePreg2Utereg2.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	
	public ModelStatoMod getStatoModelloB1(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case  "
				+ "	when (select count(rendmacro.*)  "
				+ "		from greg_r_rendicontazione_preg1_macro rendmacro  "
				+ "		where rendmacro.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rendmacro.data_cancellazione is null "
				+ "		and rendmacro.valore>0) "
				+ "		+ "
				+ "		(select count(rendutenza1.*)  "
				+ "		from greg_r_rendicontazione_preg1_utereg1 rendutenza1  "
				+ "		where rendutenza1.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rendutenza1.data_cancellazione is null "
				+ "		and rendutenza1.valore>0)+ "
				+ "		(select count(rendutenza2.*)  "
				+ "		from greg_r_rendicontazione_preg2_utereg2 rendutenza2  "
				+ "		where rendutenza2.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rendutenza2.data_cancellazione is null "
				+ "		and rendutenza2.valore>0) > 0 "
				+ "	then true "
				+ "	else false "
				+ "end as valorizzate, "
				+ "case  "
				+ "	when (select count(distinct prest1.*)  "
				+ "		from greg_r_rendicontazione_preg1_macro rendmacro, "
				+ "		greg_r_prest_reg1_macroaggregati_bilancio preg1macro, "
				+ "		greg_t_prestazioni_regionali_1 prest1 "
				+ "		where rendmacro.id_rendicontazione_ente = rend.id_rendicontazione_ente  "
				+ "		and preg1macro.id_prest_reg1_macroaggregati_bilancio = rendmacro.id_prest_reg1_macroaggregati_bilancio  "
				+ "		and preg1macro.id_prest_reg_1 = prest1.id_prest_reg_1  "
				+ "		and rendmacro.valore is not null and rendmacro.valore>0 "
				+ "		and rendmacro.data_cancellazione is null) < (cast(count(distinct carta.id_prest_reg_1)as numeric)/2) "
				+ "	then 'NON_COMPILATO' "
				+ "	else 'COMPILATO_PARZIALE' "
				+ "end as stato "
				+ "from greg_t_rendicontazione_ente rend  "
				+ "left join greg_r_carta_servizi_preg1 carta on carta.id_rendicontazione_ente = rend.id_rendicontazione_ente   "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  "
				+ "where carta.data_cancellazione is null "
				+ "and rend.data_cancellazione is null "
				+ "and scheda.data_cancellazione is null "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "group by rend.id_rendicontazione_ente "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		Object[] result = (Object[]) query.getSingleResult();
		ModelStatoMod stato = new ModelStatoMod(result);
		return stato;
	}
	
	
	public boolean getValorizzatoModelloB1(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case  "
				+ "	when (select count(rendmacro.*)  "
				+ "		from greg_r_rendicontazione_preg1_macro rendmacro  "
				+ "		where rendmacro.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rendmacro.data_cancellazione is null "
				+ "		and rendmacro.valore>0) "
				+ "		+ "
				+ "		(select count(rendutenza1.*)  "
				+ "		from greg_r_rendicontazione_preg1_utereg1 rendutenza1  "
				+ "		where rendutenza1.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rendutenza1.data_cancellazione is null "
				+ "		and rendutenza1.valore>0)+ "
				+ "		(select count(rendutenza2.*)  "
				+ "		from greg_r_rendicontazione_preg2_utereg2 rendutenza2  "
				+ "		where rendutenza2.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rendutenza2.data_cancellazione is null "
				+ "		and rendutenza2.valore>0) > 0 "
				+ "	then true "
				+ "	else false "
				+ "end as valorizzate "
				+ "from greg_t_rendicontazione_ente rend  "
				+ "left join greg_r_carta_servizi_preg1 carta on carta.id_rendicontazione_ente = rend.id_rendicontazione_ente   "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore  "
				+ "where carta.data_cancellazione is null "
				+ "and rend.data_cancellazione is null "
				+ "and scheda.data_cancellazione is null "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "group by rend.id_rendicontazione_ente "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		boolean result = (boolean) query.getSingleResult();
		return result;
	}
}





