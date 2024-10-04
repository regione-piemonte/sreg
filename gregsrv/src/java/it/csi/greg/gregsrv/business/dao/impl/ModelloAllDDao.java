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

import it.csi.greg.gregsrv.business.entity.GregDAree;
import it.csi.greg.gregsrv.business.entity.GregDMacroaree;
import it.csi.greg.gregsrv.business.entity.GregDMacroattivita;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRPrestMinistUtenzeMinist;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeModc;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneGiustificazioneFnps;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModuloFnps;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg1Utereg1;
import it.csi.greg.gregsrv.business.entity.GregRSpeseFnps;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniMinisteriali;
import it.csi.greg.gregsrv.dto.ModelPrestazioneRegoleAllD;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.util.Converter;

@Repository("modelloAllDDao")
@Transactional(readOnly = true)
public class ModelloAllDDao {

	@PersistenceContext
	private EntityManager em;

	public List<GregTPrestazioniMinisteriali> findAllPrestazioni(Integer annoGestione) {

		String hqlQuery = "SELECT f FROM GregTPrestazioniMinisteriali f " + "WHERE f.dataCancellazione is null "
				+ "and f.idPrestazioneMinisteriale in (select r.gregTPrestazioniMinisteriali.idPrestazioneMinisteriale from GregRPrestReg1PrestMinist r "
				+ "where r.dataCancellazione is null "
				+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null))) "
				+ "ORDER BY f.codPrestazioneMinisteriale";

		TypedQuery<GregTPrestazioniMinisteriali> query = em.createQuery(hqlQuery, GregTPrestazioniMinisteriali.class);
		query.setParameter("annoGestione", annoGestione);
		List<GregTPrestazioniMinisteriali> prestMin = query.getResultList();
		return prestMin;
	}

	public List<GregDTargetUtenza> findAllTarget(Integer annoGestione) {

		String hqlQuery = "SELECT f FROM GregDTargetUtenza f " 
						+ "WHERE f.dataCancellazione is null "
						+ "AND f.gregDTipoFlusso.codTipoFlusso = 'MIN' "
						+ "and f.idTargetUtenza in (select r.gregDTargetUtenza.idTargetUtenza from GregRPrestMinistUtenzeMinist r "
						+ "where r.dataCancellazione is null "
						+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
						+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null))) "
						+ "ORDER BY f.codUtenza";

		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}

	public List<GregRPrestMinistUtenzeMinist> findAllPrestazioniUtenza(Integer annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPrestMinistUtenzeMinist f " + "WHERE f.dataCancellazione is null "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) ";

		TypedQuery<GregRPrestMinistUtenzeMinist> query = em.createQuery(hqlQuery, GregRPrestMinistUtenzeMinist.class);
		query.setParameter("annoGestione", annoGestione);
		List<GregRPrestMinistUtenzeMinist> prestMinistUtenzeMinist = query.getResultList();
		return prestMinistUtenzeMinist;
	}

	public List<GregDAree> findAllAree(Integer annoGestione) {

		String hqlQuery = "SELECT f FROM GregDAree f " + "WHERE f.dataCancellazione is null "
				+ "and f.idArea in (select r.gregDAree.idArea from GregDTargetUtenza r "
				+ "where r.dataCancellazione is null "
				+ "and r.idTargetUtenza in (select t.gregDTargetUtenza.idTargetUtenza from GregRPrestMinistUtenzeMinist t "
				+ "where t.dataCancellazione is null "
				+ "and (:annoGestione - year(t.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(t.dataFineValidita)<=0 or t.dataFineValidita is null)))) "
				+ "ORDER BY f.codArea";

		TypedQuery<GregDAree> query = em.createQuery(hqlQuery, GregDAree.class);
		query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}
	
	public List<GregRRendicontazioneModuloFnps> findAllRendicontazioneModAllDByEnte(Integer idScheda) {
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModuloFnps r "
					+ "	WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
					+ "and r.dataCancellazione is null "
					+ "and r.gregTRendicontazioneEnte.dataCancellazione is null";

			TypedQuery<GregRRendicontazioneModuloFnps> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModuloFnps.class);

			query.setParameter("idScheda", idScheda);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<ModelPrestazioneRegoleAllD> findAllReg1byMin(String ministeriale, Integer annoGestione) {
		try {
			String hqlQuery = "SELECT new it.csi.greg.gregsrv.dto.ModelPrestazioneRegoleAllD(r.codPrestReg1, r.gregDTipologia.codTipologia, t.regola) "
					+ "FROM GregTPrestazioniRegionali1 r, "
					+ "GregRPrestReg1PrestMinist t "
					+ "WHERE t.gregTPrestazioniMinisteriali.codPrestazioneMinisteriale=:ministeriale "
					+ "AND t.gregTPrestazioniRegionali1.codPrestReg1=r.codPrestReg1 "
					+ "and r.dataCancellazione is null "
					+ "and t.dataCancellazione is null "
					+ " and (:annoGestione - year(r.dataInizioValidita)>=0 "
					+ " and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null)) "
					+ "ORDER BY r.codPrestReg1";
			TypedQuery<ModelPrestazioneRegoleAllD> query = em.createQuery(hqlQuery,
					ModelPrestazioneRegoleAllD.class);

			query.setParameter("ministeriale", ministeriale);
			query.setParameter("annoGestione", annoGestione);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregDTargetUtenza> findAllTargetUtenzeFiglie(String utenza) {
		try { 
			String hqlQuery = "SELECT r FROM GregDTargetUtenza r, "
					+ "GregDTargetUtenza t, GregRAlgoritmoTargetUtenza g "
					+ "WHERE g.gregDTargetUtenza1.codUtenza = :utenza "
					+ "AND g.gregDTargetUtenza2.codUtenza = r.codUtenza "
					+ "and r.dataCancellazione is null "
					+ "and t.dataCancellazione is null "
					+ "and g.dataCancellazione is null "
					+ "ORDER BY r.codUtenza";
			TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery,
					GregDTargetUtenza.class);

			query.setParameter("utenza", utenza);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRRendicontazionePreg1Utereg1 findAllRendicontazioneB1byReg1TargetUtenza(String reg1, String utenza, Integer idScheda) {
		try {  
			String hqlQuery = "SELECT r FROM GregRRendicontazionePreg1Utereg1 r "
					+ "WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
					+ "AND r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.codPrestReg1 = :reg1 "
					+ "AND r.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.codUtenza = :utenza "
					+ "and r.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.dataCancellazione is null "
					+ " and (r.gregTRendicontazione.annoGestione - year(r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (r.gregTRendicontazione.annoGestione - year(r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.dataFineValidita is null)) ";
			TypedQuery<GregRRendicontazionePreg1Utereg1> query = em.createQuery(hqlQuery,
					GregRRendicontazionePreg1Utereg1.class);
			
			query.setParameter("reg1", reg1);
			query.setParameter("utenza", utenza);
			query.setParameter("idScheda", idScheda);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneModAPart2 findAllRendicontazioneAbyReg1TargetUtenza(String reg1, String utenza, Integer idScheda) {
		try {  
			String hqlQuery = "SELECT r FROM GregRRendicontazioneModAPart2 r "
					+ "WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
					+ "AND r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.codPrestReg1 = :reg1 "
					+ "AND r.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.codUtenza = :utenza "
					+ "and r.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.dataCancellazione is null "
					+ " and (r.gregTRendicontazione.annoGestione - year(r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (r.gregTRendicontazione.annoGestione - year(r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.dataFineValidita is null)) ";
			TypedQuery<GregRRendicontazioneModAPart2> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModAPart2.class);
			
			query.setParameter("reg1", reg1);
			query.setParameter("utenza", utenza);
			query.setParameter("idScheda", idScheda);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
public List<Object> findRendicontazioneforRowB1(Integer idScheda) {
		Query query = em.createNativeQuery(""
				+ "with sumDifMA05 as ( "
				+ "select "
				+ "	t.cod_prestazione_ministeriale, "
				+ "	u.cod_utenza, "
				+ "	sum(rendru2.valore) as sommaDifMA05 "
				+ "from "
				+ "	greg_t_prestazioni_ministeriali t, "
				+ "	greg_d_target_utenza u, "
				+ "	greg_r_prest_minist_utenze_minist tu, "
				+ "	greg_t_prestazioni_regionali_1 r, "
				+ "	greg_r_prest_reg1_prest_minist rt, "
				+ "	greg_d_target_utenza u2, "
				+ "	greg_r_algoritmo_target_utenza uu2, "
				+ "	greg_r_prest_reg1_utenze_regionali1 ru2, "
				+ "	greg_r_rendicontazione_preg1_utereg1 rendru2, "
				+ "	greg_t_rendicontazione_ente rend, "
				+ "	greg_d_tipologia g, "
				+ "	greg_c_regola reg "
				+ "where "
				+ "	(t.id_prestazione_ministeriale = tu.id_prest_minist "
				+ "		and u.id_target_utenza = tu.id_target_utenza) "
				+ "	and (t.id_prestazione_ministeriale = rt.id_prest_minist "
				+ "		and r.id_prest_reg_1 = rt.id_prest_reg1) "
				+ "	and (u.id_target_utenza = uu2.id_target_utenza_padre "
				+ "		and u2.id_target_utenza = uu2.id_target_utenza_figlio) "
				+ "	and (r.id_prest_reg_1 = ru2.id_prest_reg_1 "
				+ "		and u2.id_target_utenza = ru2.id_target_utenza) "
				+ "	and (ru2.id_prest_reg1_utenza_regionale1 = rendru2.id_prest_reg1_utenza_regionale1 "
				+ "		and rendru2.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rend.id_rendicontazione_ente = :idScheda) "
				+ "		and (rend.anno_gestione - date_part('year', ru2.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', ru2.data_fine_validita) <=0  or ru2.data_fine_validita is null)) "
				+ "		and (rend.anno_gestione - date_part('year', r.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', r.data_fine_validita) <=0  or r.data_fine_validita is null)) "
				+ "		and (rend.anno_gestione - date_part('year', rt.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', rt.data_fine_validita) <=0  or rt.data_fine_validita is null)) "
				+ "	and (r.id_tipologia = g.id_tipologia "
				+ "		and g.cod_tipologia <> 'MA05') "
				+ "	and (rendru2.data_cancellazione is null) "
				+ "	and rt.id_regola = reg.id_regola "
				+ "	and reg.codice_regola = '1' "
				+ "and t.data_Cancellazione is null "
				+ "and u.data_Cancellazione is null "
				+ "and tu.data_Cancellazione is null "
				+ "and r.data_Cancellazione is null "
				+ "and rt.data_Cancellazione is null "
				+ "and u2.data_Cancellazione is null "
				+ "and uu2.data_Cancellazione is null "
				+ "and ru2.data_Cancellazione is null "
				+ "and rend.data_Cancellazione is null "
				+ "and g.data_Cancellazione is null "
				+ "group by "
				+ "	t.cod_prestazione_ministeriale, "
				+ "	u.cod_utenza "
				+ "order by "
				+ "	t.cod_prestazione_ministeriale, "
				+ "	u.cod_utenza), "
				+ "subDifMA05 as ( "
				+ "select "
				+ "	t.cod_prestazione_ministeriale, "
				+ "	u.cod_utenza, "
				+ "	sum(rendru2.valore) as sottrDifMA05 "
				+ "from "
				+ "	greg_t_prestazioni_ministeriali t, "
				+ "	greg_d_target_utenza u, "
				+ "	greg_r_prest_minist_utenze_minist tu, "
				+ "	greg_t_prestazioni_regionali_1 r, "
				+ "	greg_r_prest_reg1_prest_minist rt, "
				+ "	greg_d_target_utenza u2, "
				+ "	greg_r_algoritmo_target_utenza uu2, "
				+ "	greg_r_prest_reg1_utenze_regionali1 ru2, "
				+ "	greg_r_rendicontazione_preg1_utereg1 rendru2, "
				+ "	greg_t_rendicontazione_ente rend, "
				+ "	greg_d_tipologia g, "
				+ "	greg_c_regola reg "
				+ "where "
				+ "	(t.id_prestazione_ministeriale = tu.id_prest_minist "
				+ "		and u.id_target_utenza = tu.id_target_utenza) "
				+ "	and (t.id_prestazione_ministeriale = rt.id_prest_minist "
				+ "		and r.id_prest_reg_1 = rt.id_prest_reg1) "
				+ "	and (u.id_target_utenza = uu2.id_target_utenza_padre "
				+ "		and u2.id_target_utenza = uu2.id_target_utenza_figlio) "
				+ "	and (r.id_prest_reg_1 = ru2.id_prest_reg_1 "
				+ "		and u2.id_target_utenza = ru2.id_target_utenza) "
				+ "	and (ru2.id_prest_reg1_utenza_regionale1 = rendru2.id_prest_reg1_utenza_regionale1 "
				+ "		and rendru2.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rend.id_rendicontazione_ente = :idScheda) "
				+ "		and (rend.anno_gestione - date_part('year', ru2.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', ru2.data_fine_validita) <=0  or ru2.data_fine_validita is null)) "
				+ "		and (rend.anno_gestione - date_part('year', r.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', r.data_fine_validita) <=0  or r.data_fine_validita is null)) "
				+ "		and (rend.anno_gestione - date_part('year', rt.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', rt.data_fine_validita) <=0  or rt.data_fine_validita is null)) "
				+ "	and (r.id_tipologia = g.id_tipologia "
				+ "		and g.cod_tipologia <> 'MA05') "
				+ "	and (rendru2.data_cancellazione is null) "
				+ "		and rt.id_regola = reg.id_regola "
				+ "		and reg.codice_regola = '2' "
				+ "and t.data_Cancellazione is null "
				+ "and u.data_Cancellazione is null "
				+ "and tu.data_Cancellazione is null "
				+ "and r.data_Cancellazione is null "
				+ "and rt.data_Cancellazione is null "
				+ "and u2.data_Cancellazione is null "
				+ "and uu2.data_Cancellazione is null "
				+ "and ru2.data_Cancellazione is null "
				+ "and rend.data_Cancellazione is null "
				+ "and g.data_Cancellazione is null "
				+ "	group by "
				+ "		t.cod_prestazione_ministeriale, "
				+ "		u.cod_utenza "
				+ "	order by "
				+ "		t.cod_prestazione_ministeriale, "
				+ "		u.cod_utenza), "
				+ "sumMA05 as ( "
				+ "select "
				+ "	t.cod_prestazione_ministeriale, "
				+ "	u.cod_utenza, "
				+ "	sum(rendru2.valore) as sommaMA05 "
				+ "from "
				+ "	greg_t_prestazioni_ministeriali t, "
				+ "	greg_d_target_utenza u, "
				+ "	greg_r_prest_minist_utenze_minist tu, "
				+ "	greg_t_prestazioni_regionali_1 r, "
				+ "	greg_r_prest_reg1_prest_minist rt, "
				+ "	greg_d_target_utenza u2, "
				+ "	greg_r_algoritmo_target_utenza uu2, "
				+ "	greg_r_prest_reg1_utenze_regionali1 ru2, "
				+ "	greg_r_rendicontazione_mod_a_part2 rendru2, "
				+ "	greg_t_rendicontazione_ente rend, "
				+ "	greg_d_tipologia g, "
				+ "	greg_c_regola reg "
				+ "where "
				+ "	(t.id_prestazione_ministeriale = tu.id_prest_minist "
				+ "		and u.id_target_utenza = tu.id_target_utenza) "
				+ "	and (t.id_prestazione_ministeriale = rt.id_prest_minist "
				+ "		and r.id_prest_reg_1 = rt.id_prest_reg1) "
				+ "	and (u.id_target_utenza = uu2.id_target_utenza_padre "
				+ "		and u2.id_target_utenza = uu2.id_target_utenza_figlio) "
				+ "	and (r.id_prest_reg_1 = ru2.id_prest_reg_1 "
				+ "		and u2.id_target_utenza = ru2.id_target_utenza) "
				+ "	and (ru2.id_prest_reg1_utenza_regionale1 = rendru2.id_prest_reg1_utenza_regionale1 "
				+ "		and rendru2.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rend.id_rendicontazione_ente = :idScheda) "
				+ "		and (rend.anno_gestione - date_part('year', ru2.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', ru2.data_fine_validita) <=0  or ru2.data_fine_validita is null)) "
				+ "		and (rend.anno_gestione - date_part('year', r.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', r.data_fine_validita) <=0  or r.data_fine_validita is null)) "
				+ "		and (rend.anno_gestione - date_part('year', rt.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', rt.data_fine_validita) <=0  or rt.data_fine_validita is null)) "
				+ "	and (r.id_tipologia = g.id_tipologia "
				+ "		and g.cod_tipologia = 'MA05') "
				+ "	and (rendru2.data_cancellazione is null) "
				+ "		and rt.id_regola = reg.id_regola "
				+ "		and reg.codice_regola = '1' "
				+ "and t.data_Cancellazione is null "
				+ "and u.data_Cancellazione is null "
				+ "and tu.data_Cancellazione is null "
				+ "and r.data_Cancellazione is null "
				+ "and rt.data_Cancellazione is null "
				+ "and u2.data_Cancellazione is null "
				+ "and uu2.data_Cancellazione is null "
				+ "and ru2.data_Cancellazione is null "
				+ "and rend.data_Cancellazione is null "
				+ "and g.data_Cancellazione is null "
				+ "	group by "
				+ "		t.cod_prestazione_ministeriale, "
				+ "		u.cod_utenza "
				+ "	order by "
				+ "		t.cod_prestazione_ministeriale, "
				+ "		u.cod_utenza), "
				+ "subMA05 as ( "
				+ "select "
				+ "	t.cod_prestazione_ministeriale, "
				+ "	u.cod_utenza, "
				+ "	sum(rendru2.valore) as sottrMA05 "
				+ "from "
				+ "	greg_t_prestazioni_ministeriali t, "
				+ "	greg_d_target_utenza u, "
				+ "	greg_r_prest_minist_utenze_minist tu, "
				+ "	greg_t_prestazioni_regionali_1 r, "
				+ "	greg_r_prest_reg1_prest_minist rt, "
				+ "	greg_d_target_utenza u2, "
				+ "	greg_r_algoritmo_target_utenza uu2, "
				+ "	greg_r_prest_reg1_utenze_regionali1 ru2, "
				+ "	greg_r_rendicontazione_mod_a_part2 rendru2, "
				+ "	greg_t_rendicontazione_ente rend, "
				+ "	greg_d_tipologia g, "
				+ "	greg_c_regola reg "
				+ "where "
				+ "	(t.id_prestazione_ministeriale = tu.id_prest_minist "
				+ "		and u.id_target_utenza = tu.id_target_utenza) "
				+ "	and (t.id_prestazione_ministeriale = rt.id_prest_minist "
				+ "		and r.id_prest_reg_1 = rt.id_prest_reg1) "
				+ "	and (u.id_target_utenza = uu2.id_target_utenza_padre "
				+ "		and u2.id_target_utenza = uu2.id_target_utenza_figlio) "
				+ "	and (r.id_prest_reg_1 = ru2.id_prest_reg_1 "
				+ "		and u2.id_target_utenza = ru2.id_target_utenza) "
				+ "	and (ru2.id_prest_reg1_utenza_regionale1 = rendru2.id_prest_reg1_utenza_regionale1 "
				+ "		and rendru2.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "		and rend.id_rendicontazione_ente = :idScheda) "
				+ "		and (rend.anno_gestione - date_part('year', ru2.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', ru2.data_fine_validita) <=0  or ru2.data_fine_validita is null)) "
				+ "		and (rend.anno_gestione - date_part('year', r.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', r.data_fine_validita) <=0  or r.data_fine_validita is null)) "
				+ "		and (rend.anno_gestione - date_part('year', rt.data_inizio_validita) >=0  "
				+ "		and (rend.anno_gestione - date_part('year', rt.data_fine_validita) <=0  or rt.data_fine_validita is null)) "
				+ "	and (r.id_tipologia = g.id_tipologia "
				+ "		and g.cod_tipologia = 'MA05') "
				+ "	and (rendru2.data_cancellazione is null) "
				+ "		and rt.id_regola = reg.id_regola "
				+ "		and reg.codice_regola = '2' "
				+ "and t.data_Cancellazione is null "
				+ "and u.data_Cancellazione is null "
				+ "and tu.data_Cancellazione is null "
				+ "and r.data_Cancellazione is null "
				+ "and rt.data_Cancellazione is null "
				+ "and u2.data_Cancellazione is null "
				+ "and uu2.data_Cancellazione is null "
				+ "and ru2.data_Cancellazione is null "
				+ "and rend.data_Cancellazione is null "
				+ "and g.data_Cancellazione is null "
				+ "	group by "
				+ "		t.cod_prestazione_ministeriale, "
				+ "		u.cod_utenza "
				+ "	order by "
				+ "		t.cod_prestazione_ministeriale, "
				+ "		u.cod_utenza) "
				+ "select "
				+ "	t.cod_prestazione_ministeriale, "
				+ "	t.cod_utenza, "
				+ "	(coalesce(cast(t.sommaDifMA05 as numeric(14, 2)), 0) - coalesce(cast(g.sottrDifMA05 as numeric(14, 2)), 0)) + (coalesce(cast(p.sommaMA05 as numeric(14, 2)), 0) - coalesce(cast(q.sottrMA05 as numeric(14, 2)), 0)) as valoreB1 "
				+ "from "
				+ "	sumDifMA05 t "
				+ "left join subDifMA05 g on "
				+ "	t.cod_prestazione_ministeriale = g.cod_prestazione_ministeriale "
				+ "	and t.cod_utenza = g.cod_utenza "
				+ "left join sumMA05 p on "
				+ "	t.cod_prestazione_ministeriale = p.cod_prestazione_ministeriale "
				+ "	and t.cod_utenza = p.cod_utenza "
				+ "left join subMA05 q on "
				+ "	t.cod_prestazione_ministeriale = q.cod_prestazione_ministeriale "
				+ "	and t.cod_utenza = q.cod_utenza");
		
		query.setParameter("idScheda", idScheda);
		
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		
		return result;
	}
	
	public GregRRendicontazioneModuloFnps findRendincontazioneFnpsbyPrestTargUte(String codPrestazione, String codTarget,
			Integer idEnte) {
		try {
			String hqlQuery = "SELECT r FROM GregRRendicontazioneModuloFnps r " + "WHERE "
					+ "r.gregRPrestMinistUtenzeMinist.gregTPrestazioniMinisteriali.codPrestazioneMinisteriale=:codPrestazione "
					+ "AND "
					+ "r.gregRPrestMinistUtenzeMinist.gregDTargetUtenza.codUtenza=:codTarget "
					+ "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte " + "AND "
					+ "r.dataCancellazione is null ";
	
			TypedQuery<GregRRendicontazioneModuloFnps> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModuloFnps.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);
			query.setParameter("idEnte", idEnte);
	
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPrestMinistUtenzeMinist findPrestMinistUtenzeModFnpsbyPrestazioneUtenze(String codPrestazione,
			String codTarget) {
		try {
			String hqlQuery = "SELECT r FROM GregRPrestMinistUtenzeMinist r " + "WHERE "
					+ "r.gregTPrestazioniMinisteriali.codPrestazioneMinisteriale=:codPrestazione " + "AND "
					+ "r.gregDTargetUtenza.codUtenza=:codTarget " + "AND "
					+ "r.dataCancellazione is null ";

			TypedQuery<GregRPrestMinistUtenzeMinist> query = em.createQuery(hqlQuery,
					GregRPrestMinistUtenzeMinist.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);


			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneModuloFnps updateRendicontazioneModuloFnps(
			GregRRendicontazioneModuloFnps rendicontazione) {
		return em.merge(rendicontazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazioneModuloFnps(Integer idRendicontazione) {
		GregRRendicontazioneModuloFnps rendToDelete = em.find(GregRRendicontazioneModuloFnps.class,
				idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
			em.remove(rendToDelete);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazioneModuloFnps(GregRRendicontazioneModuloFnps rendicontazione) {
		em.persist(rendicontazione);
	}
	
	public List<GregDMacroattivita> findAllMacroAttivita() {
		String hqlQuery = "SELECT f FROM GregDMacroattivita f " + "WHERE f.dataCancellazione is null";

		TypedQuery<GregDMacroattivita> query = em.createQuery(hqlQuery, GregDMacroattivita.class);
		return query.getResultList();
	}
	
	public GregRRendicontazioneGiustificazioneFnps findGiustificazioneFnpsByEnte(Integer idScheda) {
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneGiustificazioneFnps r "
					+ "	WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
					+ "and r.dataCancellazione is null ";

			TypedQuery<GregRRendicontazioneGiustificazioneFnps> query = em.createQuery(hqlQuery,
					GregRRendicontazioneGiustificazioneFnps.class);

			query.setParameter("idScheda", idScheda);


			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneGiustificazioneFnps updateRendicontazioneGiustificazioneFnps(
			GregRRendicontazioneGiustificazioneFnps rendicontazione) {
		return em.merge(rendicontazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazioneGiustificazioneFnps(Integer idRendicontazione) {
		GregRRendicontazioneGiustificazioneFnps rendToDelete = em.find(GregRRendicontazioneGiustificazioneFnps.class,
				idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
			em.remove(rendToDelete);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazioneGiustificazioneFnps(GregRRendicontazioneGiustificazioneFnps rendicontazione) {
		em.persist(rendicontazione);
	}
	

	public ModelStatoMod getStatoModelloFNPS(Integer idRendicontazione) {
		String sqlQuery ="with a as(select  "
				+ "				 case   "
				+ "				 	when ( "
				+ "		select "
				+ "			count(fnps.*) "
				+ "		from "
				+ "			greg_r_rendicontazione_modulo_fnps fnps "
				+ "		where "
				+ "			fnps.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and fnps.valore>0 "
				+ "			and fnps.data_cancellazione is null)>0  "
				+ "				 		then true "
				+ "		else false "
				+ "	end as valorizzato,  "
				+ "				 case   "
				+ "				 	when ( "
				+ "		select "
				+ "			count(fnps.*) "
				+ "		from "
				+ "			greg_r_rendicontazione_modulo_fnps fnps "
				+ "		where "
				+ "			fnps.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and fnps.valore>0 "
				+ "			and fnps.data_cancellazione is null)= 0  "
				+ "				 		then 'NON_COMPILATO' "
				+ "		when ( "
				+ "		select "
				+ "			sum(fnps.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_modulo_fnps fnps,  "
				+ "				 		greg_r_prest_minist_utenze_minist prestminutenzemin,  "
				+ "				 		greg_d_target_utenza utenza "
				+ "		where "
				+ "			fnps.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and prestminutenzemin.id_prest_minist_utenze_minist = fnps.id_prest_minist_utenze_minist "
				+ "			and prestminutenzemin.id_target_utenza = utenza.id_target_utenza "
				+ "			and utefnps.id_target_utenza = utenza.id_target_utenza "
				+ "			and fnps.valore>0 "
				+ "			and fnps.data_cancellazione is null)<((( "
				+ "		select "
				+ "			sum(rfondi.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_fondi rfondi "
				+ "		left join greg_d_fondi fondi on "
				+ "			rfondi.id_fondo = fondi.id_fondo "
				+ "			and (rend.anno_gestione - date_part('year', "
				+ "			fondi.data_inizio_validita) >= 0 "
				+ "				and (rend.anno_gestione - date_part('year', "
				+ "				fondi.data_fine_validita) <= 0 "
				+ "					or fondi.data_fine_validita is null)) "
				+ "			and fondi.data_cancellazione is null "
				+ "		left join greg_d_tipologia_fondi tfondi on "
				+ "			 fondi.id_tipologia_fondo = tfondi.id_tipologia_fondo "
				+ "			and tfondi.data_cancellazione is null "
				+ "		where "
				+ "			(tfondi.cod_tipologia_fondo = 'FONDO' "
				+ "				or tfondi.cod_tipologia_fondo = 'ALQ') "
				+ "			and "
				+ "			rfondi.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and rfondi.data_cancellazione is null) * utefnps.valore_percentuale)/ 100) "
				+ "				 	then 'NON_CONFORME' "
				+ "		when ( "
				+ "		select "
				+ "			sum(fnps.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_modulo_fnps fnps,  "
				+ "				 		greg_r_prest_minist_utenze_minist prestminutenzemin,  "
				+ "				 		greg_d_target_utenza utenza "
				+ "		where "
				+ "			fnps.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and prestminutenzemin.id_prest_minist_utenze_minist = fnps.id_prest_minist_utenze_minist "
				+ "			and prestminutenzemin.id_target_utenza = utenza.id_target_utenza "
				+ "			and utefnps.id_target_utenza = utenza.id_target_utenza "
				+ "			and fnps.valore>0 "
				+ "			and fnps.data_cancellazione is null)>=(((( "
				+ "		select "
				+ "			sum(rfondi.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_fondi rfondi "
				+ "		left join greg_d_fondi fondi on "
				+ "			rfondi.id_fondo = fondi.id_fondo "
				+ "			and (rend.anno_gestione - date_part('year', "
				+ "			fondi.data_inizio_validita) >= 0 "
				+ "				and (rend.anno_gestione - date_part('year', "
				+ "				fondi.data_fine_validita) <= 0 "
				+ "					or fondi.data_fine_validita is null)) "
				+ "			and fondi.data_cancellazione is null "
				+ "		left join greg_d_tipologia_fondi tfondi on "
				+ "			fondi.id_tipologia_fondo = tfondi.id_tipologia_fondo "
				+ "			and tfondi.data_cancellazione is null "
				+ "		where "
				+ "			(tfondi.cod_tipologia_fondo = 'FONDO' "
				+ "				or tfondi.cod_tipologia_fondo = 'ALQ') "
				+ "			and rfondi.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and rfondi.data_cancellazione is null)) * utefnps.valore_percentuale)/ 100) "
				+ "		and   "
				+ "				 		( "
				+ "		select "
				+ "			sum(fnps.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_modulo_fnps fnps,  "
				+ "				 		greg_r_prest_minist_utenze_minist prestminutenzemin,  "
				+ "				 		greg_d_target_utenza utenza "
				+ "		where "
				+ "			fnps.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and prestminutenzemin.id_prest_minist_utenze_minist = fnps.id_prest_minist_utenze_minist "
				+ "			and prestminutenzemin.id_target_utenza = utenza.id_target_utenza "
				+ "			and fnps.valore>0 "
				+ "			and fnps.data_cancellazione is null)<( "
				+ "		select "
				+ "			sum(rfondi.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_fondi rfondi "
				+ "		left join greg_d_fondi fondi on "
				+ "			rfondi.id_fondo = fondi.id_fondo "
				+ "			and (rend.anno_gestione - date_part('year', "
				+ "			fondi.data_inizio_validita) >= 0 "
				+ "				and (rend.anno_gestione - date_part('year', "
				+ "				fondi.data_fine_validita) <= 0 "
				+ "					or fondi.data_fine_validita is null)) "
				+ "			and fondi.data_cancellazione is null "
				+ "		left join greg_d_tipologia_fondi tfondi on "
				+ "			fondi.id_tipologia_fondo = tfondi.id_tipologia_fondo "
				+ "			and tfondi.data_cancellazione is null "
				+ "		where "
				+ "			(tfondi.cod_tipologia_fondo = 'FONDO' "
				+ "				or tfondi.cod_tipologia_fondo = 'ALQ') "
				+ "			and rfondi.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and rfondi.data_cancellazione is null)  "
				+ "				 	then 'COMPILATO_PARZIALE' "
				+ "		when ( "
				+ "		select "
				+ "			sum(fnps.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_modulo_fnps fnps,  "
				+ "				 		greg_r_prest_minist_utenze_minist prestminutenzemin,  "
				+ "				 		greg_d_target_utenza utenza "
				+ "		where "
				+ "			fnps.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and prestminutenzemin.id_prest_minist_utenze_minist = fnps.id_prest_minist_utenze_minist "
				+ "			and prestminutenzemin.id_target_utenza = utenza.id_target_utenza "
				+ "			and utefnps.id_target_utenza = utenza.id_target_utenza "
				+ "			and fnps.valore>0 "
				+ "			and fnps.data_cancellazione is null)>=((( "
				+ "		select "
				+ "			sum(rfondi.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_fondi rfondi "
				+ "		left join greg_d_fondi fondi on "
				+ "			rfondi.id_fondo = fondi.id_fondo "
				+ "			and (rend.anno_gestione - date_part('year', "
				+ "			fondi.data_inizio_validita) >= 0 "
				+ "				and (rend.anno_gestione - date_part('year', "
				+ "				fondi.data_fine_validita) <= 0 "
				+ "					or fondi.data_fine_validita is null)) "
				+ "			and fondi.data_cancellazione is null "
				+ "		left join greg_d_tipologia_fondi tfondi on "
				+ "			fondi.id_tipologia_fondo = tfondi.id_tipologia_fondo "
				+ "			and tfondi.data_cancellazione is null "
				+ "		where "
				+ "			(tfondi.cod_tipologia_fondo = 'FONDO' "
				+ "				or tfondi.cod_tipologia_fondo = 'ALQ') "
				+ "			and rfondi.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and rfondi.data_cancellazione is null)* utefnps.valore_percentuale)/ 100) "
				+ "		and   "
				+ "				 		( "
				+ "		select "
				+ "			sum(fnps.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_modulo_fnps fnps,  "
				+ "				 		greg_r_prest_minist_utenze_minist prestminutenzemin,  "
				+ "				 		greg_d_target_utenza utenza "
				+ "		where "
				+ "			fnps.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and prestminutenzemin.id_prest_minist_utenze_minist = fnps.id_prest_minist_utenze_minist "
				+ "			and prestminutenzemin.id_target_utenza = utenza.id_target_utenza "
				+ "			and fnps.valore>0 "
				+ "			and fnps.data_cancellazione is null)=( "
				+ "		select "
				+ "			sum(rfondi.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_fondi rfondi "
				+ "		left join greg_d_fondi fondi on "
				+ "			rfondi.id_fondo = fondi.id_fondo "
				+ "			and (rend.anno_gestione - date_part('year', "
				+ "			fondi.data_inizio_validita) >= 0 "
				+ "				and (rend.anno_gestione - date_part('year', "
				+ "				fondi.data_fine_validita) <= 0 "
				+ "					or fondi.data_fine_validita is null)) "
				+ "			and fondi.data_cancellazione is null "
				+ "		left join greg_d_tipologia_fondi tfondi on "
				+ "			fondi.id_tipologia_fondo = tfondi.id_tipologia_fondo "
				+ "			and tfondi.data_cancellazione is null "
				+ "		where "
				+ "			(tfondi.cod_tipologia_fondo = 'FONDO' "
				+ "				or tfondi.cod_tipologia_fondo = 'ALQ') "
				+ "			and rfondi.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and rfondi.data_cancellazione is null) "
				+ "				 		then 'COMPILATO' "
				+ "	end as stato, "
				+ "	( "
				+ "		select "
				+ "			sum(fnps.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_modulo_fnps fnps,  "
				+ "				 		greg_r_prest_minist_utenze_minist prestminutenzemin,  "
				+ "				 		greg_d_target_utenza utenza "
				+ "		where "
				+ "			fnps.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and prestminutenzemin.id_prest_minist_utenze_minist = fnps.id_prest_minist_utenze_minist "
				+ "			and prestminutenzemin.id_target_utenza = utenza.id_target_utenza "
				+ "			and fnps.valore>0 "
				+ "			and fnps.data_cancellazione is null) as fnpsFNPS, "
				+ "			( "
				+ "		select "
				+ "			sum(rfondi.valore) "
				+ "		from "
				+ "			greg_r_rendicontazione_fondi rfondi "
				+ "		left join greg_d_fondi fondi on "
				+ "			rfondi.id_fondo = fondi.id_fondo "
				+ "			and (rend.anno_gestione - date_part('year', "
				+ "			fondi.data_inizio_validita) >= 0 "
				+ "				and (rend.anno_gestione - date_part('year', "
				+ "				fondi.data_fine_validita) <= 0 "
				+ "					or fondi.data_fine_validita is null)) "
				+ "			and fondi.data_cancellazione is null "
				+ "		left join greg_d_tipologia_fondi tfondi on "
				+ "			(tfondi.cod_tipologia_fondo = 'FONDO' "
				+ "				or tfondi.cod_tipologia_fondo = 'ALQ') "
				+ "			and fondi.id_tipologia_fondo = tfondi.id_tipologia_fondo "
				+ "			and tfondi.data_cancellazione is null "
				+ "		where "
				+ "			rfondi.id_rendicontazione_ente = rend.id_rendicontazione_ente "
				+ "			and rfondi.data_cancellazione is null) as fnpsStimato "
				+ "from "
				+ "	greg_t_rendicontazione_ente rend "
				+ "left join greg_t_schede_enti_gestori scheda on "
				+ "	scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore, "
				+ "	greg_r_fnps_utenza_calcolo utefnps "
				+ "where   "
				+ "				 rend.data_cancellazione is null "
				+ "	and scheda.data_cancellazione is null "
				+ "	and utefnps.utilizzato_per_calcolo "
				+ "	and utefnps.data_cancellazione is null "
				+ "	and (rend.anno_gestione - date_part('year', "
				+ "	utefnps.data_inizio_validita) >= 0 "
				+ "		and (rend.anno_gestione - date_part('year', "
				+ "		utefnps.data_fine_validita) <= 0 "
				+ "			or utefnps.data_fine_validita is null)) "
				+ "	and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "order by "
				+ "	rend.id_rendicontazione_ente) "
				+ "select case  "
				+ "		when EXISTS(select a.valorizzato from a where a.valorizzato=false ) "
				+ "		then false  "
				+ "		else true "
				+ "end as valorizzato, "
				+ "case  "
				+ "when EXISTS(select a.stato from a where a.stato='NON_COMPILATO') "
				+ "		then 'NON_COMPILATO'  "
				+ "		when EXISTS(select a.stato from a where a.stato='NON_CONFORME') "
				+ "		then 'NON_CONFORME'  "
				+ "		when EXISTS(select a.stato from a where a.stato='COMPILATO_PARZIALE') "
				+ "		then 'COMPILATO_PARZIALE'  "
				+ "		else 'COMPILATO' "
				+ "	end as stato ";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		Object[] result = (Object[]) query.getSingleResult();
		ModelStatoMod stato = new ModelStatoMod(result);
		return stato;
	}
	
	public GregRSpeseFnps findSpesaFnpsByEnte(Integer idScheda, Integer idFondo) {
		try {
			String hqlQuery = "	SELECT r FROM GregRSpeseFnps r "
					+ "	WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
					+ "and r.gregDFondi.idFondo = :idFondo "
					+ "and r.dataCancellazione is null ";

			TypedQuery<GregRSpeseFnps> query = em.createQuery(hqlQuery,
					GregRSpeseFnps.class);

			query.setParameter("idScheda", idScheda);
			query.setParameter("idFondo", idFondo);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteSpeseFnps(Integer idRendicontazione) {
		GregRSpeseFnps rendToDelete = em.find(GregRSpeseFnps.class,
				idRendicontazione);

		if ((idRendicontazione != null && idRendicontazione != 0)) {
			em.remove(rendToDelete);
		}
	}
	
	public GregRSpeseFnps updateSpeseFnps(
			GregRSpeseFnps rendicontazione) {
		return em.merge(rendicontazione);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertSpeseFnps(GregRSpeseFnps rendicontazione) {
		em.persist(rendicontazione);
	}
}
