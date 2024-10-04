/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDDettaglioDisabilita;
import it.csi.greg.gregsrv.business.entity.GregDDettaglioUtenze;
import it.csi.greg.gregsrv.business.entity.GregDDisabilita;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRDisabilitaTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRPreg1DisabTargetUtenzaDettDisab;
import it.csi.greg.gregsrv.business.entity.GregRPreg1DisabilitaTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeModc;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeModcDettUtenze;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte3;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte4;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.dto.ModelStatoMod;

@Repository("modelloCDao")
@Transactional(readOnly = true)
public class ModelloCDao {

	@PersistenceContext
	private EntityManager em;

	public List<GregTPrestazioniRegionali1> findAllPrestazioni(Integer annoGestione) {

		String hqlQuery = "SELECT f FROM GregTPrestazioniRegionali1 f "
				+ "WHERE f.dataCancellazione is null "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) "
				+ "ORDER BY f.codPrestReg1";
		
		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
				query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}

	public List<GregDTargetUtenza> findAllTarget() {

		String hqlQuery = "SELECT f FROM GregDTargetUtenza f " + "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codUtenza";

		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		return query.getResultList();
	}

	public List<GregRPrestReg1UtenzeModc> findAllPrestazioniUtenza(Integer annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPrestReg1UtenzeModc f "
				+ "WHERE f.dataCancellazione is null "
				+ "and (:annoGestione - year(f.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(f.dataFineValidita)<=0 or f.dataFineValidita is null)) ";

		TypedQuery<GregRPrestReg1UtenzeModc> query = em.createQuery(hqlQuery, GregRPrestReg1UtenzeModc.class);
				query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}

	public List<GregDDettaglioUtenze> findAllDettagliUtenze() {

		String hqlQuery = "SELECT f FROM GregDDettaglioUtenze f " + "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codDettaglioUtenza";

		TypedQuery<GregDDettaglioUtenze> query = em.createQuery(hqlQuery, GregDDettaglioUtenze.class);
		return query.getResultList();
	}
	
	public List<GregRPrestReg1UtenzeModcDettUtenze> findAllPrestazioniUtenzaDettagliUtenze(Integer annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPrestReg1UtenzeModcDettUtenze f "
					+ "LEFT JOIN FETCH f.gregRPrestReg1UtenzeModc p "
					+ "LEFT JOIN FETCH p.gregTPrestazioniRegionali1 reg1 "
					+ "WHERE f.dataCancellazione is null "
					+ "and p.dataCancellazione is null "
					+ "and reg1.dataCancellazione is null "
					+ "and (:annoGestione - year(reg1.dataInizioValidita)>=0 "
					+ "and (:annoGestione - year(reg1.dataFineValidita)<=0 or reg1.dataFineValidita is null)) "
					+ "and (:annoGestione - year(p.dataInizioValidita)>=0 "
					+ "and (:annoGestione - year(p.dataFineValidita)<=0 or p.dataFineValidita is null)) ";

		TypedQuery<GregRPrestReg1UtenzeModcDettUtenze> query = em.createQuery(hqlQuery, GregRPrestReg1UtenzeModcDettUtenze.class);
				query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}
	
	public List<GregDDisabilita> findAllDisabilita() {

		String hqlQuery = "SELECT f FROM GregDDisabilita f " + "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codDisabilita";

		TypedQuery<GregDDisabilita> query = em.createQuery(hqlQuery, GregDDisabilita.class);
		return query.getResultList();
	}
	
	public List<GregRDisabilitaTargetUtenza> findAllTargetDisabilita() {
		String hqlQuery = "SELECT f FROM GregRDisabilitaTargetUtenza f " + "WHERE f.dataCancellazione is null";

		TypedQuery<GregRDisabilitaTargetUtenza> query = em.createQuery(hqlQuery, GregRDisabilitaTargetUtenza.class);
		return query.getResultList();
	}
	
	public List<GregRPreg1DisabilitaTargetUtenza> findAllTargetDisabilitaPrestazioni(Integer annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPreg1DisabilitaTargetUtenza f " 
					+ "LEFT JOIN FETCH f.gregTPrestazioniRegionali1 reg1 "
					+ "WHERE f.dataCancellazione is null "
					+ "and reg1.dataCancellazione is null "
					+ "and (:annoGestione - year(reg1.dataInizioValidita)>=0 "
					+ "and (:annoGestione - year(reg1.dataFineValidita)<=0 or reg1.dataFineValidita is null)) ";

		TypedQuery<GregRPreg1DisabilitaTargetUtenza> query = em.createQuery(hqlQuery, GregRPreg1DisabilitaTargetUtenza.class);
				query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}
	
	public List<GregDDettaglioDisabilita> findAllDettaglioDisabilita() {

		String hqlQuery = "SELECT f FROM GregDDettaglioDisabilita f " + "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codDettaglioDisabilita";

		TypedQuery<GregDDettaglioDisabilita> query = em.createQuery(hqlQuery, GregDDettaglioDisabilita.class);
		return query.getResultList();
	}

	public List<GregRPreg1DisabTargetUtenzaDettDisab> findAllTargetDisabilitaPrestazioniDettaglioDisabilita(Integer annoGestione) {
		String hqlQuery = "SELECT f FROM GregRPreg1DisabTargetUtenzaDettDisab f " 
					+ "LEFT JOIN FETCH f.gregRPreg1DisabilitaTargetUtenza p "
					+ "LEFT JOIN FETCH p.gregTPrestazioniRegionali1 reg1 "
					+ "WHERE f.dataCancellazione is null "
					+ "and p.dataCancellazione is null "
					+ "and reg1.dataCancellazione is null "
					+ "and (:annoGestione - year(reg1.dataInizioValidita)>=0 "
					+ "and (:annoGestione - year(reg1.dataFineValidita)<=0 or reg1.dataFineValidita is null)) ";
		
		TypedQuery<GregRPreg1DisabTargetUtenzaDettDisab> query = em.createQuery(hqlQuery, GregRPreg1DisabTargetUtenzaDettDisab.class);
				query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}
	
	public List<GregRRendicontazioneModCParte1> findAllRendicontazioneParte1ModCByEnte(Integer idScheda) {
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModCParte1 r "
					+ "	LEFT JOIN FETCH r.gregTRendicontazioneEnte t "
					+ " LEFT JOIN FETCH r.gregRPrestReg1UtenzeModc p "
					+ " LEFT JOIN FETCH p.gregTPrestazioniRegionali1 reg1 "
					+ "	WHERE t.idRendicontazioneEnte = :idScheda "
					+ " and (t.annoGestione - year(reg1.dataInizioValidita)>=0 "
					+ " and (t.annoGestione - year(reg1.dataFineValidita)<=0 or reg1.dataFineValidita is null)) "
					+ "and (t.annoGestione - year(p.dataInizioValidita)>=0 "
					+ "and (t.annoGestione - year(p.dataFineValidita)<=0 or p.dataFineValidita is null)) "
					+ "and t.dataCancellazione is null "
					+ "and p.dataCancellazione is null "
					+ "and reg1.dataCancellazione is null ";
//					+ " AND t.annoGestione = :annoGestione ";

			TypedQuery<GregRRendicontazioneModCParte1> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModCParte1.class);

			query.setParameter("idScheda", idScheda);
//			query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregRRendicontazioneModCParte2> findAllRendicontazioneParte2ModCByEnte(Integer idScheda) {
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModCParte2 r "
					+ "	LEFT JOIN FETCH r.gregTRendicontazioneEnte t "
					+ " LEFT JOIN FETCH r.gregRPrestReg1UtenzeModcDettUtenze p "
					+ " LEFT JOIN FETCH p.gregRPrestReg1UtenzeModc g "
					+ " LEFT JOIN FETCH g.gregTPrestazioniRegionali1 reg1 "
					+ "	WHERE t.idRendicontazioneEnte = :idScheda "
					+ " and (t.annoGestione - year(reg1.dataInizioValidita)>=0 "
					+ " and (t.annoGestione - year(reg1.dataFineValidita)<=0 or reg1.dataFineValidita is null)) "
					+ " and (t.annoGestione - year(g.dataInizioValidita)>=0 "
					+ " and (t.annoGestione - year(g.dataFineValidita)<=0 or g.dataFineValidita is null)) "
					+ "and t.dataCancellazione is null "
					+ "and p.dataCancellazione is null "
					+ "and g.dataCancellazione is null "
					+ "and reg1.dataCancellazione is null";

			TypedQuery<GregRRendicontazioneModCParte2> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModCParte2.class);

			query.setParameter("idScheda", idScheda);
//			query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregRRendicontazioneModCParte3> findAllRendicontazioneParte3ModCByEnte(Integer idScheda) {
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModCParte3 r "
					+ "	LEFT JOIN FETCH r.gregTRendicontazioneEnte t "
					+ " LEFT JOIN FETCH r.gregRPreg1DisabilitaTargetUtenza p "
					+ " LEFT JOIN FETCH p.gregTPrestazioniRegionali1 reg1 "
					+ "	WHERE t.idRendicontazioneEnte = :idScheda "
					+ " and (t.annoGestione - year(reg1.dataInizioValidita)>=0 "
					+ " and (t.annoGestione - year(reg1.dataFineValidita)<=0 or reg1.dataFineValidita is null)) "
					+ "and t.dataCancellazione is null "
					+ "and p.dataCancellazione is null "
					+ "and reg1.dataCancellazione is null ";
//					+ " AND t.annoGestione = :annoGestione ";

			TypedQuery<GregRRendicontazioneModCParte3> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModCParte3.class);

			query.setParameter("idScheda", idScheda);
//			query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregRRendicontazioneModCParte4> findAllRendicontazioneParte4ModCByEnte(Integer idScheda) {
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModCParte4 r "
					+ "	LEFT JOIN FETCH r.gregTRendicontazioneEnte t "
					+ " LEFT JOIN FETCH r.gregRPreg1DisabTargetUtenzaDettDisab p "
					+ " LEFT JOIN FETCH p.gregRPreg1DisabilitaTargetUtenza g "
					+ " LEFT JOIN FETCH g.gregTPrestazioniRegionali1 reg1 "
					+ "	WHERE t.idRendicontazioneEnte = :idScheda "
					+ " and (t.annoGestione - year(reg1.dataInizioValidita)>=0 "
					+ " and (t.annoGestione - year(reg1.dataFineValidita)<=0 or reg1.dataFineValidita is null)) "
					+ "and t.dataCancellazione is null "
					+ "and p.dataCancellazione is null "
					+ "and g.dataCancellazione is null "
					+ "and reg1.dataCancellazione is null ";
//					+ " AND t.annoGestione = :annoGestione ";

			TypedQuery<GregRRendicontazioneModCParte4> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModCParte4.class);

			query.setParameter("idScheda", idScheda);
//			query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneModCParte1 findRendincontazione1byPrestTargUte(String codPrestazione, String codTarget,
			Integer idEnte) {
		try {
			String hqlQuery = "SELECT r FROM GregRRendicontazioneModCParte1 r " + "WHERE "
					+ "r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.codPrestReg1=:codPrestazione "
					+ "AND "
					+ "r.gregRPrestReg1UtenzeModc.gregDTargetUtenza.codUtenza=:codTarget "
					+ "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte " + "AND "
					+ "r.dataCancellazione is null "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataFineValidita is null)) "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPrestReg1UtenzeModc.dataInizioValidita)>=0 "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPrestReg1UtenzeModc.dataFineValidita)<=0 or r.gregRPrestReg1UtenzeModc.dataFineValidita is null)) "
					+ "and r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeModc.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeModc.gregDTargetUtenza.dataCancellazione is null";

			TypedQuery<GregRRendicontazioneModCParte1> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModCParte1.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);
			query.setParameter("idEnte", idEnte);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPrestReg1UtenzeModc findPrestReg1UtenzeModCbyPrestazioneUtenze(String codPrestazione,
			String codTarget, Integer annoGestione) {
		try {
			String hqlQuery = "SELECT r FROM GregRPrestReg1UtenzeModc r " + "WHERE "
					+ "r.gregTPrestazioniRegionali1.codPrestReg1=:codPrestazione " + "AND "
					+ "r.gregDTargetUtenza.codUtenza=:codTarget " + "AND "
					+ "r.dataCancellazione is null "
					+ " and (:annoGestione - year(r.dataInizioValidita)>=0 "
					+ " and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null)) "
					+ " and (:annoGestione - year(r.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (:annoGestione - year(r.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregTPrestazioniRegionali1.dataFineValidita is null)) "
					+ "and r.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregDTargetUtenza.dataCancellazione is null";

			TypedQuery<GregRPrestReg1UtenzeModc> query = em.createQuery(hqlQuery,
					GregRPrestReg1UtenzeModc.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);
			query.setParameter("annoGestione", annoGestione);


			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneModCParte2 findRendincontazione2byPrestTargUteDettUte(String codPrestazione, String codTarget,
			String codDettaglio, Integer idEnte) {
		
		try {
			String hqlQuery = "SELECT r FROM GregRRendicontazioneModCParte2 r " + "WHERE "
					+ "r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.codPrestReg1=:codPrestazione "
					+ "AND "
					+ "r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.gregDTargetUtenza.codUtenza=:codTarget "
					+ "AND "
					+ "r.gregRPrestReg1UtenzeModcDettUtenze.gregDDettaglioUtenze.codDettaglioUtenza=:codDettaglio " + "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte " + "AND "
					+ "r.dataCancellazione is null "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataFineValidita is null)) "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.dataInizioValidita)>=0 "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.dataFineValidita)<=0 or r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.dataFineValidita is null)) "
					+ "and r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeModcDettUtenze.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeModcDettUtenze.gregRPrestReg1UtenzeModc.gregDTargetUtenza.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeModcDettUtenze.gregDDettaglioUtenze.dataCancellazione is null";

			TypedQuery<GregRRendicontazioneModCParte2> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModCParte2.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);
			query.setParameter("codDettaglio", codDettaglio);
			query.setParameter("idEnte", idEnte);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPrestReg1UtenzeModcDettUtenze findPrestReg1UtenzeDettUtenzeModCbyPrestazioneUtenzeDettUtenze(String codPrestazione,
			String codTarget, String codDettaglio, Integer annoGestione) {
		try {
			String hqlQuery = "SELECT r FROM GregRPrestReg1UtenzeModcDettUtenze r " + "WHERE "
					+ "r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.codPrestReg1=:codPrestazione " + "AND "
					+ "r.gregRPrestReg1UtenzeModc.gregDTargetUtenza.codUtenza=:codTarget " + "AND "
					+ "r.gregDDettaglioUtenze.codDettaglioUtenza=:codDettaglio " + "AND "
					+ "r.dataCancellazione is null "
					+ " and (:annoGestione - year(r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (:annoGestione - year(r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataFineValidita is null)) "
					+ " and (:annoGestione - year(r.gregRPrestReg1UtenzeModc.dataInizioValidita)>=0 "
					+ " and (:annoGestione - year(r.gregRPrestReg1UtenzeModc.dataFineValidita)<=0 or r.gregRPrestReg1UtenzeModc.dataFineValidita is null)) "
					+ "and r.gregRPrestReg1UtenzeModc.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeModc.dataCancellazione is null "
					+ "and r.gregRPrestReg1UtenzeModc.gregDTargetUtenza.dataCancellazione is null ";

			TypedQuery<GregRPrestReg1UtenzeModcDettUtenze> query = em.createQuery(hqlQuery,
					GregRPrestReg1UtenzeModcDettUtenze.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);
			query.setParameter("codDettaglio", codDettaglio);
			query.setParameter("annoGestione", annoGestione);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneModCParte3 findRendincontazione3byPrestTargUteDisa(String codPrestazione, String codTarget,
			String codDisabilita, Integer idEnte) {
		
		try {
			String hqlQuery = "SELECT r FROM GregRRendicontazioneModCParte3 r " + "WHERE "
					+ "r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.codPrestReg1=:codPrestazione "
					+ "AND "
					+ "r.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDTargetUtenza.codUtenza=:codTarget "
					+ "AND "
					+ "r.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDDisabilita.codDisabilita=:codDisabilita " + "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte " + "AND "
					+ "r.dataCancellazione is null "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataFineValidita is null)) "
					+ "and r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregRPreg1DisabilitaTargetUtenza.dataCancellazione is null "
					+ "and r.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDTargetUtenza.dataCancellazione is null "
					+ "and r.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDDisabilita.dataCancellazione is null";

			TypedQuery<GregRRendicontazioneModCParte3> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModCParte3.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);
			query.setParameter("codDisabilita", codDisabilita);
			query.setParameter("idEnte", idEnte);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPreg1DisabilitaTargetUtenza findPreg1DisabilitaTargetUtenzaModCbyPrestazioneUtenzeDisabilita(String codPrestazione,
			String codTarget, String codDisabilita, Integer annoGestione) {
		try {
			String hqlQuery = "SELECT r FROM GregRPreg1DisabilitaTargetUtenza r " + "WHERE "
					+ "r.gregTPrestazioniRegionali1.codPrestReg1=:codPrestazione "
					+ "AND "
					+ "r.gregRDisabilitaTargetUtenza.gregDTargetUtenza.codUtenza=:codTarget "
					+ "AND "
					+ "r.gregRDisabilitaTargetUtenza.gregDDisabilita.codDisabilita=:codDisabilita " + "AND "
					+ "r.dataCancellazione is null "
					+ "and r.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregRDisabilitaTargetUtenza.gregDTargetUtenza.dataCancellazione is null "
					+ "and r.gregRDisabilitaTargetUtenza.dataCancellazione is null "
					+ " and (:annoGestione - year(r.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (:annoGestione - year(r.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregTPrestazioniRegionali1.dataFineValidita is null)) ";

			TypedQuery<GregRPreg1DisabilitaTargetUtenza> query = em.createQuery(hqlQuery,
					GregRPreg1DisabilitaTargetUtenza.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);
			query.setParameter("codDisabilita", codDisabilita);
			query.setParameter("annoGestione", annoGestione);


			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRRendicontazioneModCParte4 findRendincontazione4byPrestTargUteDisaDettaglioDisa(String codPrestazione, String codTarget,
			String codDisabilita, String codDettaglioDisabilita, Integer idEnte) {
		
		try {
			String hqlQuery = "SELECT r FROM GregRRendicontazioneModCParte4 r " + "WHERE "
					+ "r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.codPrestReg1=:codPrestazione "
					+ "AND "
					+ "r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDTargetUtenza.codUtenza=:codTarget "
					+ "AND "
					+ "r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDDisabilita.codDisabilita=:codDisabilita " + "AND "
					+ "r.gregRPreg1DisabTargetUtenzaDettDisab.gregDDettaglioDisabilita.codDettaglioDisabilita=:codDettaglioDisabilita " + "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte " + "AND "
					+ "r.dataCancellazione is null "
					+ "and r.gregRPreg1DisabTargetUtenzaDettDisab.dataCancellazione is null "
					+ "and r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.dataCancellazione is null "
					+ "and r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.dataCancellazione is null "
					+ "and r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDTargetUtenza.dataCancellazione is null "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (r.gregTRendicontazioneEnte.annoGestione - year(r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregRPreg1DisabTargetUtenzaDettDisab.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataFineValidita is null)) ";
;

			TypedQuery<GregRRendicontazioneModCParte4> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModCParte4.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);
			query.setParameter("codDisabilita", codDisabilita);
			query.setParameter("codDettaglioDisabilita", codDettaglioDisabilita);
			query.setParameter("idEnte", idEnte);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPreg1DisabTargetUtenzaDettDisab findPreg1DisabTargetUtenzaDettDisabModCbyPrestazioneUtenzeDisabilitaDettagli(String codPrestazione,
			String codTarget, String codDisabilita, String codDettaglioDisabilita, Integer annoGestione) {
		try {
			String hqlQuery = "SELECT r FROM GregRPreg1DisabTargetUtenzaDettDisab r " + "WHERE "
					+ "r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.codPrestReg1=:codPrestazione "
					+ "AND "
					+ "r.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDTargetUtenza.codUtenza=:codTarget "
					+ "AND "
					+ "r.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDDisabilita.codDisabilita=:codDisabilita " + "AND "
					+ "r.gregDDettaglioDisabilita.codDettaglioDisabilita=:codDettaglioDisabilita " + "AND "
					+ "r.dataCancellazione is null "
					+ "and r.gregRPreg1DisabilitaTargetUtenza.dataCancellazione is null "
					+ "and r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataCancellazione is null "
					+ "and r.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.dataCancellazione is null "
					+ "and r.gregRPreg1DisabilitaTargetUtenza.gregRDisabilitaTargetUtenza.gregDTargetUtenza.dataCancellazione is null "
					+ " and (:annoGestione - year(r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataInizioValidita)>=0 "
					+ " and (:annoGestione - year(r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataFineValidita)<=0 or r.gregRPreg1DisabilitaTargetUtenza.gregTPrestazioniRegionali1.dataFineValidita is null)) ";

			TypedQuery<GregRPreg1DisabTargetUtenzaDettDisab> query = em.createQuery(hqlQuery,
					GregRPreg1DisabTargetUtenzaDettDisab.class);
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTarget", codTarget);
			query.setParameter("codDisabilita", codDisabilita);
			query.setParameter("codDettaglioDisabilita", codDettaglioDisabilita);
			query.setParameter("annoGestione", annoGestione);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	public GregRRendicontazioneModCParte1 updateRendicontazione1ModC(
			GregRRendicontazioneModCParte1 rendicontazione) {
		return em.merge(rendicontazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazione1ModC(Integer idRendicontazione) {
		GregRRendicontazioneModCParte1 rendToDelete = em.find(GregRRendicontazioneModCParte1.class,
				idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
			em.remove(rendToDelete);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazione1ModC(GregRRendicontazioneModCParte1 rendicontazione) {
		em.persist(rendicontazione);
	}

	public GregRRendicontazioneModCParte2 updateRendicontazione2ModC(
			GregRRendicontazioneModCParte2 rendicontazione) {
		return em.merge(rendicontazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazione2ModC(Integer idRendicontazione) {
		GregRRendicontazioneModCParte2 rendToDelete = em.find(GregRRendicontazioneModCParte2.class,
				idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
			em.remove(rendToDelete);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazione2ModC(GregRRendicontazioneModCParte2 rendicontazione) {
		em.persist(rendicontazione);
	}
	
	public GregRRendicontazioneModCParte3 updateRendicontazione3ModC(
			GregRRendicontazioneModCParte3 rendicontazione) {
		return em.merge(rendicontazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazione3ModC(Integer idRendicontazione) {
		GregRRendicontazioneModCParte3 rendToDelete = em.find(GregRRendicontazioneModCParte3.class,
				idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
			em.remove(rendToDelete);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazione3ModC(GregRRendicontazioneModCParte3 rendicontazione) {
		em.persist(rendicontazione);
	}
	
	public GregRRendicontazioneModCParte4 updateRendicontazione4ModC(
			GregRRendicontazioneModCParte4 rendicontazione) {
		return em.merge(rendicontazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazione4ModC(Integer idRendicontazione) {
		GregRRendicontazioneModCParte4 rendToDelete = em.find(GregRRendicontazioneModCParte4.class,
				idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
			em.remove(rendToDelete);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazione4ModC(GregRRendicontazioneModCParte4 rendicontazione) {
		em.persist(rendicontazione);
	}
	
	//procedure recupero dati per invio
	public List<GregRRendicontazioneModCParte1> getAllDatiModelloCpart1PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneModCParte1> query = 
				em.createNamedQuery("GregRRendicontazioneModCParte1.findValideByIdRendicontazioneEnte", GregRRendicontazioneModCParte1.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	public List<GregRRendicontazioneModCParte2> getAllDatiModelloCpart2PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneModCParte2> query = 
				em.createNamedQuery("GregRRendicontazioneModCParte2.findValideByIdRendicontazioneEnte", GregRRendicontazioneModCParte2.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	public List<GregRRendicontazioneModCParte3> getAllDatiModelloCpart3PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneModCParte3> query = 
				em.createNamedQuery("GregRRendicontazioneModCParte3.findValideByIdRendicontazioneEnte", GregRRendicontazioneModCParte3.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	public List<GregRRendicontazioneModCParte4> getAllDatiModelloCpart4PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneModCParte4> query = 
				em.createNamedQuery("GregRRendicontazioneModCParte4.findValideByIdRendicontazioneEnte", GregRRendicontazioneModCParte4.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	
	
	public ModelStatoMod getStatoModelloC(Integer idRendicontazione) {
		String sqlQuery ="select  "
				+ "case "
				+ "	when (select count(prest.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte1 prest "
				+ "		where prest.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and prest.data_cancellazione is null  "
				+ "		and prest.valore>0)+ "
				+ "		(select count(dettprest.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte2 dettprest "
				+ "		where dettprest.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and dettprest.data_cancellazione is null  "
				+ "		and dettprest.valore>0)+ "
				+ "		(select count(disab.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte3 disab "
				+ "		where disab.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and disab.data_cancellazione is null  "
				+ "		and disab.valore>0)+ "
				+ "		(select count(dettdisab.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte4 dettdisab "
				+ "		where dettdisab.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and dettdisab.data_cancellazione is null  "
				+ "		and dettdisab.valore>0)>0 "
				+ "	then true  "
				+ "	else false  "
				+ "end as valorizzato, "
				+ "case  "
				+ "	when (select count(prest.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte1 prest "
				+ "		where prest.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and prest.data_cancellazione is null  "
				+ "		and prest.valore>0)+ "
				+ "		(select count(dettprest.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte2 dettprest "
				+ "		where dettprest.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and dettprest.data_cancellazione is null  "
				+ "		and dettprest.valore>0)+ "
				+ "		(select count(disab.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte3 disab "
				+ "		where disab.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and disab.data_cancellazione is null  "
				+ "		and disab.valore>0)+ "
				+ "		(select count(dettdisab.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte4 dettdisab "
				+ "		where dettdisab.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and dettdisab.data_cancellazione is null  "
				+ "		and dettdisab.valore>0)=0 "
				+ "	then 'NON_COMPILATO' "
				+ "	when (select sum(rprest.valore)  "
				+ "			from greg_r_rendicontazione_mod_c_parte1 rprest, "
				+ "			greg_r_prest_reg1_utenze_modc prest1utenzec, "
				+ "			greg_t_prestazioni_regionali_1 prest1, "
				+ "			greg_d_target_utenza utenza "
				+ "			where rprest.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and rprest.id_prest_reg1_utenza_modc = prest1utenzec.id_prest_reg1_utenza_modc  "
				+ "			and prest1utenzec.id_prest_reg_1 = prest1.id_prest_reg_1  "
				+ "			and prest1utenzec.data_cancellazione is null  "
				+ "			and prest1utenzec.id_target_utenza = utenza.id_target_utenza  "
				+ "			and utenza.data_cancellazione is null "
				+ "			and prest1.data_cancellazione is null  "
				+ "			and prest1.cod_prest_reg_1 = 'R_A.1.1' "
				+ "			and utenza.cod_utenza <> 'U28' "
				+ "			and rprest.data_cancellazione is null  "
				+ "			and rprest.valore>0)>0 or  "
				+ "		(select sum(rprest.valore)  "
				+ "			from greg_r_rendicontazione_mod_c_parte1 rprest, "
				+ "			greg_r_prest_reg1_utenze_modc prest1utenzec, "
				+ "			greg_t_prestazioni_regionali_1 prest1, "
				+ "			greg_d_target_utenza utenza "
				+ "			where rprest.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and rprest.id_prest_reg1_utenza_modc = prest1utenzec.id_prest_reg1_utenza_modc  "
				+ "			and prest1utenzec.id_prest_reg_1 = prest1.id_prest_reg_1  "
				+ "			and prest1utenzec.data_cancellazione is null  "
				+ "			and prest1utenzec.id_target_utenza = utenza.id_target_utenza  "
				+ "			and utenza.data_cancellazione is null "
				+ "			and prest1.data_cancellazione is null  "
				+ "			and prest1.cod_prest_reg_1 = 'R_A.2.1' "
				+ "			and utenza.cod_utenza <> 'U28' "
				+ "			and rprest.data_cancellazione is null  "
				+ "			and rprest.valore>0)>0 "
				+ "		then 'COMPILATO_PARZIALE' "
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

	public boolean getValorizzatoModelloC(Integer idRendicontazione) {
		String sqlQuery ="select  "
				+ "case "
				+ "	when (select count(prest.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte1 prest "
				+ "		where prest.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and prest.data_cancellazione is null  "
				+ "		and prest.valore>0)+ "
				+ "		(select count(dettprest.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte2 dettprest "
				+ "		where dettprest.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and dettprest.data_cancellazione is null  "
				+ "		and dettprest.valore>0)+ "
				+ "		(select count(disab.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte3 disab "
				+ "		where disab.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and disab.data_cancellazione is null  "
				+ "		and disab.valore>0)+ "
				+ "		(select count(dettdisab.*)  "
				+ "		from greg_r_rendicontazione_mod_c_parte4 dettdisab "
				+ "		where dettdisab.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and dettdisab.data_cancellazione is null  "
				+ "		and dettdisab.valore>0)>0 "
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
