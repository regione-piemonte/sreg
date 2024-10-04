/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregDTipologiaModA;
import it.csi.greg.gregsrv.business.entity.GregDTitoloModA;
import it.csi.greg.gregsrv.business.entity.GregDVoceModA;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart3;
import it.csi.greg.gregsrv.business.entity.GregRTitoloTipologiaVoceModA;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.dto.ModelStatoMod;


@Repository("modelloADao")
@Transactional(readOnly=true)
public class ModelloADao  {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<GregDTitoloModA> findAllTitoliModA() {
		
		String hqlQuery = "SELECT f FROM GregDTitoloModA f "
				+ "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.ordinamento";
		
		TypedQuery<GregDTitoloModA> query = em.createQuery(hqlQuery, GregDTitoloModA.class);
		return query.getResultList();
	}
	
	public List<GregRRendicontazioneModAPart1> findAllValoriModAByEnte(Integer idScheda) {
	
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModAPart1 r "
					+ "	WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte  = :idScheda "
					+ "and r.dataCancellazione is null ";

			TypedQuery<GregRRendicontazioneModAPart1> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModAPart1.class);
			
			query.setParameter("idScheda", idScheda);
			
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregRRendicontazioneModAPart2> findAllValoriModAPart2ByEnte(Integer idScheda) {
		
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModAPart2 r "
					+ "	LEFT JOIN FETCH r.gregTRendicontazioneEnte t "
					+ " LEFT JOIN FETCH r.gregRPrestReg1UtenzeRegionali1 p "
					+ "	WHERE t.idRendicontazioneEnte  = :idScheda "
					+ "	AND (t.annoGestione - year(p.dataInizioValidita)>=0 "
					+ " AND (t.annoGestione - year(p.dataFineValidita)<=0 OR p.dataFineValidita is null)) "
					+ "and r.dataCancellazione is null "
					+ "and t.dataCancellazione is null "
					+ "and p.dataCancellazione is null ";

			TypedQuery<GregRRendicontazioneModAPart2> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModAPart2.class);
			
			query.setParameter("idScheda", idScheda);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregRRendicontazioneModAPart3> findAllValoriModAPart3ByEnte(Integer idScheda) {
		
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModAPart3 r "
					+ "	WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
					+ "and r.dataCancellazione is null ";
			
			TypedQuery<GregRRendicontazioneModAPart3> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModAPart3.class);
			
			query.setParameter("idScheda", idScheda);
			
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	

	
	public List<GregRTitoloTipologiaVoceModA> findAllVociModA() {
		
		try {
			String hqlQuery = "SELECT r FROM GregRTitoloTipologiaVoceModA r "
					+ "WHERE r.dataCancellazione is null "
					+ "ORDER BY r.gregDTitoloModA.ordinamento ";

			TypedQuery<GregRTitoloTipologiaVoceModA> query = em.createQuery(hqlQuery, GregRTitoloTipologiaVoceModA.class);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	public List<GregDVoceModA> findVociBySezioneModello(String sezione) {
		
		try {
			String hqlQuery = "SELECT r FROM GregDVoceModA r "
					+ " WHERE r.dataCancellazione is null "
					+ " AND r.sezioneModello = :sezione "
					+ " ORDER BY r.ordinamento";

			TypedQuery<GregDVoceModA> query = em.createQuery(hqlQuery, GregDVoceModA.class);
			query.setParameter("sezione", sezione);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object> getRendicontazioneModAPart3(Integer idScheda) {
		

		Query query = em.createNativeQuery( ""+ 
				" 	SELECT v.id_voce_mod_a as idVoce, v.cod_voce_mod_a as codVoce, v.des_voce_mod_a as descVoce," +
				"   r.valore as valore, r.id_rendicontazione_ente as idRendicontazioneEnte,  v.sezione_modello as sezioneModello, v.ordinamento as ordinamento" + 
				"	FROM greg_r_rendicontazione_mod_a_part3 r " + 
				"	RIGHT JOIN greg_d_voce_mod_a v ON v.id_voce_mod_a = r.id_voce_mod_a " + 
				"	WHERE v.data_cancellazione is null " + 
				"   AND r.id_voce_mod_a = v.id_voce_mod_a " +
				"	AND r.id_rendicontazione_ente = :idScheda "
				+ " and r.data_Cancellazione is null " + 
				"	ORDER BY v.ordinamento " );
		
		query.setParameter("idScheda", idScheda);
		
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		
		return result;
		
	}
	
	
	public List<GregRRendicontazioneModAPart3> getAllRendModAPart3ByIdEnte(Integer idEnte) {

		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModAPart3 r "
					+ "	WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
					+ "and r.dataCancellazione is null ";

			TypedQuery<GregRRendicontazioneModAPart3> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModAPart3.class);
			
			query.setParameter("idScheda", idEnte);
			
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}

	}
	
	
	public List<GregRRendicontazioneModAPart1> getAllRendModAPart1ByIdEnte(Integer idEnte) {

		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModAPart1 r "
					+ "	WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
					+ "and r.dataCancellazione is null ";
			
			TypedQuery<GregRRendicontazioneModAPart1> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModAPart1.class);
			
			query.setParameter("idScheda", idEnte);
			
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}

	}
	
	public List<GregRRendicontazioneModAPart2> getAllRendModAPart2ByIdEnte(Integer idEnte) {

		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModAPart2 r "
					+ "	WHERE r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
					+ "and r.dataCancellazione is null ";

			TypedQuery<GregRRendicontazioneModAPart2> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModAPart2.class);
			
			query.setParameter("idScheda", idEnte);
			
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}

	}
	
	public GregDVoceModA findGregDVoceModAByIdVoce(Integer idVoce) {
		try {
			TypedQuery<GregDVoceModA> query = em.createNamedQuery("GregDVoceModA.findByIdVoce", GregDVoceModA.class);
			query.setParameter("idVoce", idVoce);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregDTipologiaModA findGregDTipologiaModAByIdTipologia(Integer idTipologia) {
		try {
			TypedQuery<GregDTipologiaModA> query = em.createNamedQuery("GregDTipologiaModA.findByIdTipologia", GregDTipologiaModA.class);
			query.setParameter("idTipologia", idTipologia);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregDTitoloModA findGregDTitoloModAByIdTitolo(Integer idTitolo) {
		try {
			TypedQuery<GregDTitoloModA> query = em.createNamedQuery("GregDTitoloModA.findByIdTitolo", GregDTitoloModA.class);
			query.setParameter("idTitolo", idTitolo);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
		
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRRendicontazioneModAPart3 saveRendicontazioneModAPart3(GregRRendicontazioneModAPart3 rp3) {
		return em.merge(rp3);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRRendicontazioneModAPart1 saveRendicontazioneModAPart1(GregRRendicontazioneModAPart1 rp1) {
		return em.merge(rp1);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRRendicontazioneModAPart2 saveRendicontazioneModAPart2(GregRRendicontazioneModAPart2 rp2) {
		return em.merge(rp2);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GregRTitoloTipologiaVoceModA saveTitoloTipologiaVoceModA(GregRTitoloTipologiaVoceModA rttv) {
		GregRTitoloTipologiaVoceModA entity = em.merge(rttv);
		return entity;
	}
	
	public GregRTitoloTipologiaVoceModA getRTitoloTipologiaVoceModA(Integer idTitolo, Integer idTipologia, Integer idVoce) {

		try {		
			String hqlQuery = "	SELECT r FROM GregRTitoloTipologiaVoceModA r "
					+ "	LEFT JOIN FETCH r.gregDTitoloModA titolo "
					+ "	LEFT JOIN FETCH r.gregDTipologiaModA tipologia "; 
			
			if(idVoce != null)
			{
				hqlQuery += " LEFT JOIN FETCH r.gregDVoceModA voce "; 
			}
			
			hqlQuery += " WHERE titolo.idTitoloModA = :idTitolo " 
					 + "  AND tipologia.idTipologiaModA = :idTipologia "
					 + " and titolo.dataCancellazione is null "
					 + " and tipologia.dataCancellazione is null " ;
			
			if(idVoce != null)
			{
				hqlQuery += "  AND voce.idVoceModA = :idVoce "
						+ "and voce.dataCancellazione is null "; 
			}

			TypedQuery<GregRTitoloTipologiaVoceModA> query = em.createQuery(hqlQuery,GregRTitoloTipologiaVoceModA.class);
			
			query.setParameter("idTitolo", idTitolo);
			query.setParameter("idTipologia", idTipologia);
			if(idVoce != null)
			{
				query.setParameter("idVoce", idVoce);
			}
			
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendModAPart1(Integer idRendModAPart1) {
		GregRRendicontazioneModAPart1 rendPart1ToDelete = em.find(GregRRendicontazioneModAPart1.class, idRendModAPart1);

		if (idRendModAPart1 != null && idRendModAPart1 != 0) {
				em.remove(rendPart1ToDelete);
			}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendModAPart2(Integer idRendModAPart2) {
		GregRRendicontazioneModAPart2 rendPart2ToDelete = em.find(GregRRendicontazioneModAPart2.class, idRendModAPart2);

		if (idRendModAPart2 != null && idRendModAPart2 != 0) {
				em.remove(rendPart2ToDelete);
			}
	}
	

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendModAPart3(Integer idRendModAPart3) {
		GregRRendicontazioneModAPart3 rendPart3ToDelete = em.find(GregRRendicontazioneModAPart3.class, idRendModAPart3);

		if (idRendModAPart3 != null && idRendModAPart3 != 0) {
				em.remove(rendPart3ToDelete);
			}
	}
	

	//procedure recupero dati per invio
	public List<GregRRendicontazioneModAPart1> getAllDatiModelloAPart1PerInvio(Integer idRendicontazione) {		
		TypedQuery<GregRRendicontazioneModAPart1> query = 
				em.createNamedQuery("GregRRendicontazioneModAPart1.findValideByIdRendicontazioneEnte", GregRRendicontazioneModAPart1.class)
				.setParameter("idRendicontazione", idRendicontazione);
			
		return query.getResultList();
	}
	
	public List<GregRRendicontazioneModAPart2> getAllDatiModelloAPart2PerInvio(Integer idRendicontazione) {		
		TypedQuery<GregRRendicontazioneModAPart2> query = 
				em.createNamedQuery("GregRRendicontazioneModAPart2.findValideByIdRendicontazioneEnte", GregRRendicontazioneModAPart2.class)
				.setParameter("idRendicontazione", idRendicontazione);
			
		return query.getResultList();
	}
	
	public List<GregRRendicontazioneModAPart3> getAllDatiModelloAPart3PerInvio(Integer idRendicontazione) {		
		TypedQuery<GregRRendicontazioneModAPart3> query = 
				em.createNamedQuery("GregRRendicontazioneModAPart3.findValideByIdRendicontazioneEnte", GregRRendicontazioneModAPart3.class)
				.setParameter("idRendicontazione", idRendicontazione);
			
		return query.getResultList();
	}

	
	@SuppressWarnings("unchecked")
	public List<Object> getDatiModelloAPerRendicontazioneECodVoce(Integer idrendicontazione,String codVoce) {
		String hqlQuery = "";
		 hqlQuery = "select a.codVoceModA,a.desVoceModA,a.sezioneModello,b.valoreNumb,d.codTipologiaModA, "
				+"d.desTipologiaModA from GregDVoceModA a, " 
				+"GregRRendicontazioneModAPart1 b, "
				+"GregRTitoloTipologiaVoceModA c, "
				+"GregDTipologiaModA d "
				+"where a.idVoceModA = c.gregDVoceModA.idVoceModA "
				+"and c.idTitoloTipologiaVoceModA = b.gregRTitoloTipologiaVoceModA.idTitoloTipologiaVoceModA "
				+"and d.idTipologiaModA=c.gregDTipologiaModA.idTipologiaModA "
				+"and b.gregTRendicontazioneEnte.idRendicontazioneEnte = :idrendicontazione ";
				if (codVoce!=null) {
				hqlQuery = hqlQuery +"and a.codVoceModA = :codVoce ";
				}
				hqlQuery = hqlQuery +"and a.dataCancellazione is null "
				+"and b.dataCancellazione is null "
				+"and c.dataCancellazione is null "
				+"and d.dataCancellazione is null";
	
		
		Query query =null;
		query = em.createQuery(hqlQuery,Object[].class);
		query.setParameter("idrendicontazione", idrendicontazione);
		if (codVoce!=null) {
		query.setParameter("codVoce", codVoce);
		}
		ArrayList<Object> entiresult = (ArrayList<Object>) query.getResultList();
		
		return entiresult;
	}
	
	public List<GregDVoceModA> findGregDVoceModAAll() {
		try {
			TypedQuery<GregDVoceModA> query = em.createNamedQuery("GregDVoceModA.findAll", GregDVoceModA.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}


	public List<GregTPrestazioniRegionali1> getPrestazioniMadriModA(Integer idScheda, String codTipologia, String codStruttura) {
		String hqlQuery = "Select t from GregTPrestazioniRegionali1 t "
						+ "left join fetch t.gregRCartaServiziPreg1s r "
						+ "where r.gregTRendicontazioneEnte.idRendicontazioneEnte = :idScheda "
						+ "and t.gregDTipologia.codTipologia = :codTipologia "
						+ "and t.gregDTipoStruttura.codTipoStruttura = :codStruttura "
						+ "and (r.gregTRendicontazioneEnte.annoGestione - year(t.dataInizioValidita)>=0 "
						+ "and (r.gregTRendicontazioneEnte.annoGestione - year(t.dataFineValidita)<=0 or t.dataFineValidita is null)) "
						+ "and t.dataCancellazione is null "
						+ "and r.dataCancellazione is null";
		
		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery,
				GregTPrestazioniRegionali1.class);
		
		query.setParameter("idScheda", idScheda);
		query.setParameter("codTipologia", codTipologia);
		query.setParameter("codStruttura", codStruttura);
		return query.getResultList();
	}

	public List<GregTPrestazioniRegionali1> getPrestazioniFiglieModA(Integer idPrestReg1, String codQuota, Integer annoGestione) {
		String hqlQuery = "Select distinct t from GregTPrestazioniRegionali1 t "
						+ "left join fetch t.gregRPrestReg1UtenzeRegionali1s r "
						+ "where t.gregTPrestazioniRegionali1.idPrestReg1 = :idPrestReg1 "
						+ "and t.gregDTipologiaQuota.codTipologiaQuota = :codQuota "
						+ "and (:annoGestione - year(t.dataInizioValidita)>=0 "
						+ "and (:annoGestione - year(t.dataFineValidita)<=0 or t.dataFineValidita is null)) "
						+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
						+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null)) "
						+ "and t.dataCancellazione is null "
						+ "and r.dataCancellazione is null ";
		
		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery,
				GregTPrestazioniRegionali1.class);
		
		query.setParameter("idPrestReg1", idPrestReg1);
		query.setParameter("codQuota", codQuota);
		query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}

	public List<GregDTargetUtenza> getTargetUtenzeByCodFlusso(String codFlusso, Integer annoGestione) {
		String hqlQuery = "Select d from GregDTargetUtenza d "
				+ "where d.gregDTipoFlusso.codTipoFlusso = :codFlusso "
				+ "and d.gregDTipoFlusso.dataCancellazione is null "
				+ "and d.idTargetUtenza in (select r.gregDTargetUtenza.idTargetUtenza from GregRPrestReg1UtenzeRegionali1 r "
				+ "where r.dataCancellazione is null "
				+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
				+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null))) "
				+ "and d.dataCancellazione is null ";

		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery,
				GregDTargetUtenza.class);

		query.setParameter("codFlusso", codFlusso);
		query.setParameter("annoGestione", annoGestione);
		return query.getResultList();
	}

	public GregRPrestReg1UtenzeRegionali1 getGregRPrestReg1UtenzeRegionali1(String codPrestazione,String codTargetUtenza, Integer annoGestione) {
		try {	
			String hqlQuery = "	SELECT r FROM GregRPrestReg1UtenzeRegionali1 r "
					+ " WHERE r.gregTPrestazioniRegionali1.codPrestReg1 = :codPrestazione " 
					+ "  AND r.gregDTargetUtenza.codUtenza = :codTargetUtenza "
					+ "and (:annoGestione - year(r.dataInizioValidita)>=0 "
					+ "and (:annoGestione - year(r.dataFineValidita)<=0 or r.dataFineValidita is null)) "
					+ "and r.dataCancellazione is null " ;
			

			TypedQuery<GregRPrestReg1UtenzeRegionali1> query = em.createQuery(hqlQuery,GregRPrestReg1UtenzeRegionali1.class);
			
			query.setParameter("codPrestazione", codPrestazione);
			query.setParameter("codTargetUtenza", codTargetUtenza);
			query.setParameter("annoGestione", annoGestione);
			
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	public ModelStatoMod getStatoModelloA(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case  "
				+ "	when (select count(moda1.*) "
				+ "		from greg_r_rendicontazione_mod_a_part1 moda1 "
				+ "		where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda1.valore_numb >0 "
				+ "		and moda1.data_cancellazione is null)+ "
				+ "		(select count(moda2.*) "
				+ "		from greg_r_rendicontazione_mod_a_part2 moda2 "
				+ "		where moda2.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda2.valore>0 "
				+ "		and moda2.data_cancellazione is null)+ "
				+ "		(select count(moda3.*) "
				+ "		from greg_r_rendicontazione_mod_a_part1 moda3 "
				+ "		where moda3.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda3.valore_numb>0 "
				+ "		and moda3.data_cancellazione is null)>0 "
				+ "		then true "
				+ "		else false  "
				+ "end as valorizzato, "
				+ "case "
				+ "	when (select sum(moda1.valore_numb) "
				+ "			from greg_r_rendicontazione_mod_a_part1 moda1, "
				+ "			greg_r_titolo_tipologia_voce_mod_a tittipvoc, "
				+ "			greg_d_titolo_mod_a titolo  "
				+ "			where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and moda1.id_titolo_tipologia_voce_mod_a = tittipvoc.id_titolo_tipologia_voce_mod_a  "
				+ "			and tittipvoc.id_titolo_mod_a = titolo.id_titolo_mod_a  "
				+ "			and titolo.cod_titolo_mod_a = '02' "
				+ "			and moda1.valore_numb>0 "
				+ "			and moda1.data_cancellazione is null)>0 and  "
				+ "		(select sum(moda1.valore_numb) "
				+ "			from greg_r_rendicontazione_mod_a_part1 moda1, "
				+ "			greg_r_titolo_tipologia_voce_mod_a tittipvoc, "
				+ "			greg_d_titolo_mod_a titolo  "
				+ "			where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and moda1.id_titolo_tipologia_voce_mod_a = tittipvoc.id_titolo_tipologia_voce_mod_a  "
				+ "			and tittipvoc.id_titolo_mod_a = titolo.id_titolo_mod_a  "
				+ "			and titolo.cod_titolo_mod_a = '03' "
				+ "			and moda1.valore_numb>0 "
				+ "			and moda1.data_cancellazione is null)>0 "
				+ "	then 'COMPILATO_PARZIALE' "
				+ "	when (select sum(moda1.valore_numb) "
				+ "			from greg_r_rendicontazione_mod_a_part1 moda1, "
				+ "			greg_r_titolo_tipologia_voce_mod_a tittipvoc, "
				+ "			greg_d_titolo_mod_a titolo  "
				+ "			where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and moda1.id_titolo_tipologia_voce_mod_a = tittipvoc.id_titolo_tipologia_voce_mod_a  "
				+ "			and tittipvoc.id_titolo_mod_a = titolo.id_titolo_mod_a  "
				+ "			and titolo.cod_titolo_mod_a = '02' "
				+ "			and moda1.valore_numb>0 "
				+ "			and moda1.data_cancellazione is null)>0 or  "
				+ "		(select sum(moda1.valore_numb) "
				+ "			from greg_r_rendicontazione_mod_a_part1 moda1, "
				+ "			greg_r_titolo_tipologia_voce_mod_a tittipvoc, "
				+ "			greg_d_titolo_mod_a titolo  "
				+ "			where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and moda1.id_titolo_tipologia_voce_mod_a = tittipvoc.id_titolo_tipologia_voce_mod_a  "
				+ "			and tittipvoc.id_titolo_mod_a = titolo.id_titolo_mod_a  "
				+ "			and titolo.cod_titolo_mod_a = '03' "
				+ "			and moda1.valore_numb>0 "
				+ "			and moda1.data_cancellazione is null)>0 or  "
				+ "		(select sum(moda1.valore_numb) "
				+ "			from greg_r_rendicontazione_mod_a_part1 moda1, "
				+ "			greg_r_titolo_tipologia_voce_mod_a tittipvoc, "
				+ "			greg_d_titolo_mod_a titolo  "
				+ "			where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and moda1.id_titolo_tipologia_voce_mod_a = tittipvoc.id_titolo_tipologia_voce_mod_a  "
				+ "			and tittipvoc.id_titolo_mod_a = titolo.id_titolo_mod_a  "
				+ "			and titolo.cod_titolo_mod_a = '04' "
				+ "			and moda1.valore_numb>0 "
				+ "			and moda1.data_cancellazione is null)>0 or  "
				+ "		(select sum(moda1.valore_numb) "
				+ "			from greg_r_rendicontazione_mod_a_part1 moda1, "
				+ "			greg_r_titolo_tipologia_voce_mod_a tittipvoc, "
				+ "			greg_d_titolo_mod_a titolo  "
				+ "			where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and moda1.id_titolo_tipologia_voce_mod_a = tittipvoc.id_titolo_tipologia_voce_mod_a  "
				+ "			and tittipvoc.id_titolo_mod_a = titolo.id_titolo_mod_a  "
				+ "			and titolo.cod_titolo_mod_a = '09' "
				+ "			and moda1.valore_numb>0 "
				+ "			and moda1.data_cancellazione is null) >0 or  "
				+ "		(select sum(moda1.valore_numb) "
				+ "			from greg_r_rendicontazione_mod_a_part1 moda1, "
				+ "			greg_r_titolo_tipologia_voce_mod_a tittipvoc, "
				+ "			greg_d_titolo_mod_a titolo  "
				+ "			where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and moda1.id_titolo_tipologia_voce_mod_a = tittipvoc.id_titolo_tipologia_voce_mod_a  "
				+ "			and tittipvoc.id_titolo_mod_a = titolo.id_titolo_mod_a  "
				+ "			and titolo.cod_titolo_mod_a = '02-ALTRO' "
				+ "			and moda1.valore_numb>0 "
				+ "			and moda1.data_cancellazione is null)>0 "
				+ "		then 'NON_COMPILATO' "
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
	
	
	public boolean getValorizzatoModelloA(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "case  "
				+ "	when (select count(moda1.*) "
				+ "		from greg_r_rendicontazione_mod_a_part1 moda1 "
				+ "		where moda1.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda1.valore_numb >0 "
				+ "		and moda1.data_cancellazione is null)+ "
				+ "		(select count(moda2.*) "
				+ "		from greg_r_rendicontazione_mod_a_part2 moda2 "
				+ "		where moda2.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda2.valore>0 "
				+ "		and moda2.data_cancellazione is null)+ "
				+ "		(select count(moda3.*) "
				+ "		from greg_r_rendicontazione_mod_a_part1 moda3 "
				+ "		where moda3.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and moda3.valore_numb>0 "
				+ "		and moda3.data_cancellazione is null)>0 "
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
