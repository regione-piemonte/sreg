/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

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

import it.csi.greg.gregsrv.business.entity.GregDPersonaleEnte;
import it.csi.greg.gregsrv.business.entity.GregDProfiloProfessionale;
import it.csi.greg.gregsrv.business.entity.GregRConteggioMonteOreSett;
import it.csi.greg.gregsrv.business.entity.GregRConteggioPersonale;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModFParte1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModFParte2;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.util.Converter;

@Repository("modelloFSDao")
@Transactional(readOnly = true)
public class ModelloFDao {

	@PersistenceContext
	private EntityManager em;

	public List<GregDPersonaleEnte> findAllPersonaleEnte() {

		String hqlQuery = "SELECT f FROM GregDPersonaleEnte f " + "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codPersonaleEnte";

		TypedQuery<GregDPersonaleEnte> query = em.createQuery(hqlQuery, GregDPersonaleEnte.class);
		return query.getResultList();
	}

	public List<GregDProfiloProfessionale> findAllProfiloProfessionale() {

		String hqlQuery = "SELECT f FROM GregDProfiloProfessionale f " + "WHERE f.dataCancellazione is null "
				+ "ORDER BY f.codProfiloProfessionale";

		TypedQuery<GregDProfiloProfessionale> query = em.createQuery(hqlQuery, GregDProfiloProfessionale.class);
		return query.getResultList();
	}

	public List<GregRConteggioMonteOreSett> findAllConteggioMonteOreSett() {
		String hqlQuery = "SELECT f FROM GregRConteggioMonteOreSett f " + "WHERE f.dataCancellazione is null";

		TypedQuery<GregRConteggioMonteOreSett> query = em.createQuery(hqlQuery, GregRConteggioMonteOreSett.class);
		return query.getResultList();
	}

	public List<GregRConteggioPersonale> findAllConteggioPersonale() {

		String hqlQuery = "SELECT f FROM GregRConteggioPersonale f " + "WHERE f.dataCancellazione is null";

		TypedQuery<GregRConteggioPersonale> query = em.createQuery(hqlQuery, GregRConteggioPersonale.class);
		return query.getResultList();
	}
	
	public List<GregRRendicontazioneModFParte1> findAllRendicontazioneParte1ModFByEnte(Integer idScheda) {
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModFParte1 r "
					+ "	LEFT JOIN FETCH r.gregTRendicontazioneEnte t "
					+ "	WHERE t.idRendicontazioneEnte = :idScheda "
					+ "and r.dataCancellazione is null "
					+ "and t.dataCancellazione is null ";
//					+ " AND t.annoGestione = :annoGestione ";

			TypedQuery<GregRRendicontazioneModFParte1> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModFParte1.class);

			query.setParameter("idScheda", idScheda);
//			query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregRRendicontazioneModFParte2> findAllRendicontazioneParte2ModFByEnte(Integer idScheda) {
		try {
			String hqlQuery = "	SELECT r FROM GregRRendicontazioneModFParte2 r "
					+ "	LEFT JOIN FETCH r.gregTRendicontazioneEnte t "
					+ "	WHERE t.idRendicontazioneEnte = :idScheda "
					+ "and r.dataCancellazione is null "
					+ "and t.dataCancellazione is null ";
//					+ " AND t.annoGestione = :annoGestione ";

			TypedQuery<GregRRendicontazioneModFParte2> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModFParte2.class);

			query.setParameter("idScheda", idScheda);
//			query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);

			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	//Da vedere
	public GregRRendicontazioneModFParte1 findRendincontazione1byProfProfPersEnte(String codProfiloProfessionale, String codPersonaleEnte,
			Integer idEnte) {
		
		try {
			String hqlQuery = "SELECT r FROM GregRRendicontazioneModFParte1 r " + "WHERE "
					+ "r.gregRConteggioPersonale.gregDProfiloProfessionale.codProfiloProfessionale=:codProfiloProfessionale "
					+ "AND "
					+ "r.gregRConteggioPersonale.gregDPersonaleEnte.codPersonaleEnte=:codPersonaleEnte "
					+ "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte " + "AND "
					+ "r.dataCancellazione is null ";

			TypedQuery<GregRRendicontazioneModFParte1> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModFParte1.class);
			query.setParameter("codProfiloProfessionale", codProfiloProfessionale);
			query.setParameter("codPersonaleEnte", codPersonaleEnte);
			query.setParameter("idEnte", idEnte);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRRendicontazioneModFParte2 findRendincontazione2byProfProfPersEnte(String codProfiloProfessionale, String codPersonaleEnte,
			Integer idEnte) {
		
		try {
			String hqlQuery = "SELECT r FROM GregRRendicontazioneModFParte2 r " + "WHERE "
					+ "r.gregRConteggioMonteOreSett.gregDProfiloProfessionale.codProfiloProfessionale=:codProfiloProfessionale "
					+ "AND "
					+ "r.gregRConteggioMonteOreSett.gregDPersonaleEnte.codPersonaleEnte=:codPersonaleEnte "
					+ "AND "
					+ "r.gregTRendicontazioneEnte.idRendicontazioneEnte=:idEnte " + "AND "
					+ "r.dataCancellazione is null ";

			TypedQuery<GregRRendicontazioneModFParte2> query = em.createQuery(hqlQuery,
					GregRRendicontazioneModFParte2.class);
			query.setParameter("codProfiloProfessionale", codProfiloProfessionale);
			query.setParameter("codPersonaleEnte", codPersonaleEnte);
			query.setParameter("idEnte", idEnte);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	
	public GregRConteggioPersonale findConteggioPersonaleModFbyProfProfPers(String codProfiloProfessionale,
			String codPersonaleEnte) {
		try {
			String hqlQuery = "SELECT r FROM GregRConteggioPersonale r " + "WHERE "
					+ "r.gregDProfiloProfessionale.codProfiloProfessionale=:codProfiloProfessionale " + "AND "
					+ "r.gregDPersonaleEnte.codPersonaleEnte=:codPersonaleEnte " + "AND "
					+ "r.dataCancellazione is null ";

			TypedQuery<GregRConteggioPersonale> query = em.createQuery(hqlQuery,
					GregRConteggioPersonale.class);
			query.setParameter("codProfiloProfessionale", codProfiloProfessionale);
			query.setParameter("codPersonaleEnte", codPersonaleEnte);


			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public GregRConteggioMonteOreSett findConteggioMonteOreSettModFbyProfProfPers(String codProfiloProfessionale,
			String codPersonaleEnte) {
		try {
			String hqlQuery = "SELECT r FROM GregRConteggioMonteOreSett r " + "WHERE "
					+ "r.gregDProfiloProfessionale.codProfiloProfessionale=:codProfiloProfessionale " + "AND "
					+ "r.gregDPersonaleEnte.codPersonaleEnte=:codPersonaleEnte " + "AND "
					+ "r.dataCancellazione is null ";

			TypedQuery<GregRConteggioMonteOreSett> query = em.createQuery(hqlQuery,
					GregRConteggioMonteOreSett.class);
			query.setParameter("codProfiloProfessionale", codProfiloProfessionale);
			query.setParameter("codPersonaleEnte", codPersonaleEnte);


			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	

	public GregRRendicontazioneModFParte1 updateRendicontazione1ModF(
			GregRRendicontazioneModFParte1 rendicontazione) {
		return em.merge(rendicontazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazione1ModF(Integer idRendicontazione) {
		GregRRendicontazioneModFParte1 rendToDelete = em.find(GregRRendicontazioneModFParte1.class,
				idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
			em.remove(rendToDelete);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazione1ModF(GregRRendicontazioneModFParte1 rendicontazione) {
		em.persist(rendicontazione);
	}

	public GregRRendicontazioneModFParte2 updateRendicontazione2ModF(
			GregRRendicontazioneModFParte2 rendicontazione) {
		return em.merge(rendicontazione);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRendicontazione2ModF(Integer idRendicontazione) {
		GregRRendicontazioneModFParte2 rendToDelete = em.find(GregRRendicontazioneModFParte2.class,
				idRendicontazione);

		if (idRendicontazione != null && idRendicontazione != 0) {
			em.remove(rendToDelete);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void insertRendicontazione2ModF(GregRRendicontazioneModFParte2 rendicontazione) {
		em.persist(rendicontazione);
	}
	
	//procedure recupero dati per invio
	public List<GregRRendicontazioneModFParte1> getAllDatiModelloFpart1PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneModFParte1> query = 
				em.createNamedQuery("GregRRendicontazioneModFParte1.findValideByIdRendicontazioneEnte", GregRRendicontazioneModFParte1.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	public List<GregRRendicontazioneModFParte2> getAllDatiModelloFpart2PerInvio(Integer idRendicontazione) {
		TypedQuery<GregRRendicontazioneModFParte2> query = 
				em.createNamedQuery("GregRRendicontazioneModFParte2.findValideByIdRendicontazioneEnte", GregRRendicontazioneModFParte2.class)
				.setParameter("idRendicontazione", idRendicontazione);

		return query.getResultList();
	}
	

	public GregDProfiloProfessionale findProfiloProfessionaleByCod(String codProfilo) {

		String hqlQuery = "SELECT f FROM GregDProfiloProfessionale f " + "WHERE f.codProfiloProfessionale = :codProfilo AND f.dataCancellazione is null "
				+ "ORDER BY f.codProfiloProfessionale";

		TypedQuery<GregDProfiloProfessionale> query = em.createQuery(hqlQuery, GregDProfiloProfessionale.class);
		query.setParameter("codProfilo", codProfilo);
		return query.getSingleResult();
	}
	
	
	public ModelStatoMod getStatoModelloF(Integer idRendicontazione) {
		String sqlQuery ="select  "
				+ "case "
				+ "	when (select count(pers.*)  "
				+ "		from greg_r_rendicontazione_mod_f_parte1 pers "
				+ "		where pers.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and pers.data_cancellazione is null  "
				+ "		and pers.valore>0)+ "
				+ "		(select count(ore.*)  "
				+ "		from greg_r_rendicontazione_mod_f_parte2 ore "
				+ "		where ore.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and ore.data_cancellazione is null  "
				+ "		and ore.valore>0)>0 "
				+ "	then true  "
				+ "	else false  "
				+ "end as valorizzato,  "
				+ "case  "
				+ "	when (select count(pers.*)  "
				+ "		from greg_r_rendicontazione_mod_f_parte1 pers "
				+ "		where pers.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and pers.data_cancellazione is null  "
				+ "		and pers.valore>0)+ "
				+ "		(select count(ore.*)  "
				+ "		from greg_r_rendicontazione_mod_f_parte2 ore "
				+ "		where ore.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and ore.data_cancellazione is null  "
				+ "		and ore.valore>0)<=0 "
				+ "		then 'NON_COMPILATO' "
				+ "	when (select sum(rpers.valore)  "
				+ "			from greg_r_rendicontazione_mod_f_parte1 rpers,  "
				+ "			greg_r_conteggio_personale cpers, "
				+ "			greg_d_profilo_professionale profilo, "
				+ "			greg_d_personale_ente personale "
				+ "			where rpers.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and rpers.id_conteggio_personale = cpers.id_conteggio_personale "
				+ "			and cpers.id_profilo_professionale=profilo.id_profilo_professionale "
				+ "			and cpers.id_personale_ente = personale.id_personale_ente "
				+ "			and (personale.cod_personale_ente='01'  "
				+ "			or personale.cod_personale_ente='02'  "
				+ "			or personale.cod_personale_ente='03'  "
				+ "			or personale.cod_personale_ente='04'))>0 or "
				+ "		(select sum(rpers.valore)  "
				+ "			from greg_r_rendicontazione_mod_f_parte1 rpers,  "
				+ "			greg_r_conteggio_personale cpers, "
				+ "			greg_d_profilo_professionale profilo, "
				+ "			greg_d_personale_ente personale "
				+ "			where rpers.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and rpers.id_conteggio_personale = cpers.id_conteggio_personale "
				+ "			and cpers.id_profilo_professionale=profilo.id_profilo_professionale "
				+ "			and cpers.id_personale_ente = personale.id_personale_ente "
				+ "			and (personale.cod_personale_ente='05'))>0 or "
				+ "		(select sum(rore.valore)  "
				+ "			from greg_r_rendicontazione_mod_f_parte2 rore,  "
				+ "			greg_r_conteggio_monte_ore_sett core, "
				+ "			greg_d_profilo_professionale profilo, "
				+ "			greg_d_personale_ente personale "
				+ "			where rore.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and rore.id_conteggio_monte_ore_sett = core.id_conteggio_monte_ore_sett  "
				+ "			and core.id_profilo_professionale=profilo.id_profilo_professionale "
				+ "			and core.id_personale_ente = personale.id_personale_ente "
				+ "			and (personale.cod_personale_ente='09'  "
				+ "			or personale.cod_personale_ente='10'  "
				+ "			or personale.cod_personale_ente='11'  "
				+ "			or personale.cod_personale_ente='12' "
				+ "			or personale.cod_personale_ente='13' "
				+ "			or personale.cod_personale_ente='14'))>0 or "
				+ "			(select sum(rore.valore)  "
				+ "			from greg_r_rendicontazione_mod_f_parte2 rore,  "
				+ "			greg_r_conteggio_monte_ore_sett core, "
				+ "			greg_d_profilo_professionale profilo, "
				+ "			greg_d_personale_ente personale "
				+ "			where rore.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "			and rore.id_conteggio_monte_ore_sett = core.id_conteggio_monte_ore_sett "
				+ "			and core.id_profilo_professionale=profilo.id_profilo_professionale "
				+ "			and core.id_personale_ente = personale.id_personale_ente "
				+ "			and (personale.cod_personale_ente='15'))>0  "
				+ "	then 'COMPILATO_PARZIALE' "
				+ "end as stato "
				+ "from  "
				+ "greg_t_rendicontazione_ente rend "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore "
				+ "where "
				+ "rend.data_cancellazione is null "
				+ "and scheda.data_cancellazione is null "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "group by rend.id_rendicontazione_ente, scheda.codice_regionale "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		Object[] result = (Object[]) query.getSingleResult();
		ModelStatoMod stato = new ModelStatoMod(result);
		return stato;
	}
	
	public boolean getValorizzatoModelloF(Integer idRendicontazione) {
		String sqlQuery ="select  "
				+ "case "
				+ "	when (select count(pers.*)  "
				+ "		from greg_r_rendicontazione_mod_f_parte1 pers "
				+ "		where pers.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and pers.data_cancellazione is null  "
				+ "		and pers.valore>0)+ "
				+ "		(select count(ore.*)  "
				+ "		from greg_r_rendicontazione_mod_f_parte2 ore "
				+ "		where ore.id_rendicontazione_ente=rend.id_rendicontazione_ente "
				+ "		and ore.data_cancellazione is null  "
				+ "		and ore.valore>0)>0 "
				+ "	then true  "
				+ "	else false  "
				+ "end as valorizzato  "
				+ "from  "
				+ "greg_t_rendicontazione_ente rend "
				+ "left join greg_t_schede_enti_gestori scheda on scheda.id_scheda_ente_gestore = rend.id_scheda_ente_gestore "
				+ "where "
				+ "rend.data_cancellazione is null "
				+ "and scheda.data_cancellazione is null "
				+ "and rend.id_rendicontazione_ente = :idRendicontazione "
				+ "group by rend.id_rendicontazione_ente, scheda.codice_regionale "
				+ "order by rend.id_rendicontazione_ente";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		boolean result = (boolean) query.getSingleResult();
		return result;
	}
}
