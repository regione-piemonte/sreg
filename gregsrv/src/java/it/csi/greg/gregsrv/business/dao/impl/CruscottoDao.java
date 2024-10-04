/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDObbligo;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregDTab;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelStatoModelli;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.util.Checker;

@Repository("cruscottoDao")
@Transactional(readOnly = true)
public class CruscottoDao {
	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Object> findSchedeEntiGestoriByValue(RicercaEntiGestori ricerca) {

		List<Integer> anni = findAllAnnoEsercizio();

		String hqlQuery = "";

		hqlQuery += "select " + "	a.id_scheda_ente_gestore, " + "	a.codice_regionale, " + "	b.denominazione  "
				+ "from " + "	Greg_T_Schede_Enti_Gestori a, " + "	greg_d_stato_ente g, "
				+ "	greg_r_ente_gestore_stato_ente h, " + "	greg_d_tipo_ente i, ";
		if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) || ricerca.getAnnoEsercizio() != null) {
			hqlQuery += "greg_t_rendicontazione_ente e,  greg_d_stato_rendicontazione f, ";
		}
		hqlQuery += " greg_r_ente_gestore_contatti b " + "	left join greg_d_comuni j on j.id_comune = b.id_comune ";
		if (ricerca.getComune() != null) {
			hqlQuery += ", greg_r_schede_enti_gestori_comuni j1, greg_d_comuni j2 ";
		}
		hqlQuery += "where ";
		if (ricerca.getLista().size() >= 1) {
			hqlQuery += "a.id_scheda_ente_gestore in (";
			for (int ente : ricerca.getLista()) {
				hqlQuery += ente + ",";
			}
			hqlQuery = hqlQuery.substring(0, hqlQuery.length() - 1);
			hqlQuery += ") and ";
		}
		hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore   " + "and b.data_fine_validita is null "
				+ "and g.id_stato_ente = h.id_stato_ente "
				+ "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita > current_date and h.data_inizio_validita <= current_date)) "
				+ "and case " + "		when g.cod_stato_ente = 'APE' then h.data_inizio_validita <= current_date "
				+ "		else h.data_inizio_validita > current_date " + "end "
				+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   " 
				+ "and i.id_tipo_ente = b.id_tipo_ente ";
		if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) || ricerca.getAnnoEsercizio() != null) {
			hqlQuery += "and e.data_cancellazione is null ";
			hqlQuery += "and f.data_cancellazione is null ";
			hqlQuery += "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore ";
			hqlQuery += "and f.id_stato_rendicontazione = e.id_stato_rendicontazione ";
			if (ricerca.getStatoRendicontazione() != null) {
				hqlQuery += " and f.cod_stato_rendicontazione = :codStatoRend ";
			} else {
				hqlQuery += " and f.cod_stato_rendicontazione <>'CON' ";
			}
			if (ricerca.getAnnoEsercizio() != null) {
				hqlQuery += " and e.anno_gestione = :annoRend ";
			}
		}
		if (ricerca.getComune() != null) {
			hqlQuery += "and j1.id_scheda_ente_gestore = a.id_scheda_ente_gestore "
					+ "and j1.id_comune = j2.id_comune ";
			if (ricerca.getAnnoEsercizio() == null) {
				hqlQuery += "and j1.data_fine_validita is null ";
			} else {
				hqlQuery += "and (" + ricerca.getAnnoEsercizio()
						+ " - date_part('year', j1.data_inizio_validita)>=0 and (" + ricerca.getAnnoEsercizio()
						+ " - date_part('year', j1.data_fine_validita)<=0 or j1.data_fine_validita is null)) ";
			}
			hqlQuery += "and j2.data_cancellazione is null " + "and j1.data_cancellazione is null ";
		}
		hqlQuery += "and a.data_cancellazione is null  "
				+ "and g.data_cancellazione is null  " + "and h.data_cancellazione is null "
				+ "and i.data_Cancellazione is null " + "and j.data_Cancellazione is null " + "and ";
		if (ricerca.getTipoEnte() != null) {
			hqlQuery += "i.cod_Tipo_Ente = :codTipoEnte AND ";
		}
		if (Checker.isValorizzato(ricerca.getDenominazioneEnte())) {
			hqlQuery += " UPPER(b.denominazione) LIKE UPPER(:denominazione) AND ";
		}
		if (ricerca.getCodiceRegionale() != null) {
			hqlQuery += " a.codice_Regionale = :codiceRegionale AND ";
		}
		if (ricerca.getComune() != null) {
			hqlQuery += " j2.cod_istat_comune = :codComune AND ";
		}
		hqlQuery += "1=1 " + "union ";

		hqlQuery += "SELECT a.id_scheda_ente_gestore, a.codice_regionale, " + "b.denominazione "
				+ "FROM Greg_T_Schede_Enti_Gestori a, " + "greg_r_ente_gestore_contatti b2, "
				+ "greg_t_rendicontazione_ente e,  greg_d_stato_rendicontazione f, "
				+ "greg_d_stato_ente g,greg_r_ente_gestore_stato_ente h, " + "greg_d_tipo_ente i, greg_d_tipo_ente i2, "
				+ "greg_r_ente_gestore_contatti b " + "left join greg_d_comuni j on j.id_comune = b.id_comune ";
		if (ricerca.getComune() != null) {
			hqlQuery += ", greg_r_schede_enti_gestori_comuni j1, greg_d_comuni j2 ";
		}
		hqlQuery += "where ";
		if (ricerca.getLista().size() >= 1) {
			hqlQuery += "a.id_scheda_ente_gestore in (";
			for (int ente : ricerca.getLista()) {
				hqlQuery += ente + ",";
			}
			hqlQuery = hqlQuery.substring(0, hqlQuery.length() - 1);
			hqlQuery += ") and ";
		}
		hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore  " + "and b.data_fine_validita is null  "
				+ "and a.id_scheda_ente_gestore = b2.id_scheda_ente_gestore "
				+ "and g.id_stato_ente = h.id_stato_ente ";
		if (ricerca.getAnnoEsercizio() == null) {
			hqlQuery += "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita > current_date and h.data_inizio_validita <= current_date)) ";
		}
		hqlQuery += "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore  "
				+ "and i.id_tipo_ente = b.id_tipo_ente " + "and i2.id_tipo_ente = b2.id_tipo_ente and (";
		for (Integer anno : anni) {
			if (ricerca.getAnnoEsercizio() == null) {
				hqlQuery += "(" + anno.intValue() + " - date_part('year', b2.data_inizio_validita)>=0 and ("
						+ anno.intValue()
						+ " - date_part('year', b2.data_fine_validita)<=0 or b2.data_fine_validita is null) "
						+ "and b2.data_inizio_validita = (select MAX(b2.data_inizio_validita) from greg_r_ente_gestore_contatti b2 where a.id_scheda_ente_gestore = b2.id_scheda_ente_gestore and "
						+ anno.intValue() + " - date_part('year', b2.data_inizio_validita)=0)) or";
			} else {
				if (ricerca.getAnnoEsercizio().equals(anno)) {
					hqlQuery += "(" + anno.intValue() + " - date_part('year', b2.data_inizio_validita)>=0 and ("
							+ anno.intValue()
							+ " - date_part('year', b2.data_fine_validita)<=0 or b2.data_fine_validita is null) "
							+ "and case  " + "when b2.data_fine_validita is not null then "
							+ "b2.data_inizio_validita = ( " + "select " + "MAX(b2.data_inizio_validita) " + "from "
							+ "greg_r_ente_gestore_contatti b2 " + "where "
							+ "a.id_scheda_ente_gestore = b2.id_scheda_ente_gestore " + "and " + anno.intValue()
							+ " - date_part('year', b2.data_inizio_validita)>= 0) "
							+ "else h.data_fine_validita is null  " + "end"
							+ ") or";
				}
			}
		}
		hqlQuery += " 1=3) " + " ";
		if (ricerca.getComune() != null) {
			hqlQuery += "and j1.id_scheda_ente_gestore = b2.id_scheda_ente_gestore  "
					+ "and j2.id_comune = j1.id_comune " + "and j2.data_cancellazione is null "
					+ "and j1.data_cancellazione is null " + "and (";
			for (Integer anno : anni) {
				if (ricerca.getAnnoEsercizio() == null) {
					hqlQuery += "(" + anno.intValue() + " - date_part('year', j1.data_inizio_validita)>=0 and ("
							+ anno.intValue()
							+ " - date_part('year', j1.data_fine_validita)<=0 or j1.data_fine_validita is null)) or ";
				} else {
					if (ricerca.getAnnoEsercizio().equals(anno)) {
						hqlQuery += "(" + anno.intValue() + " - date_part('year', j1.data_inizio_validita)>=0 and ("
								+ anno.intValue()
								+ " - date_part('year', j1.data_fine_validita)<=0 or j1.data_fine_validita is null) "
								+ "and case  " + "when j1.data_fine_validita is not null then "
								+ "j1.data_inizio_validita = ( " + "select " + "MAX(j1.data_inizio_validita) " + "from "
								+ "greg_r_schede_enti_gestori_comuni j1 " + "where "
								+ "a.id_scheda_ente_gestore = j1.id_scheda_ente_gestore " + "and " + anno.intValue()
								+ " - date_part('year', j1.data_inizio_validita)= 0) "
								+ "else j1.data_fine_validita is null  " + "end) or ";
					}
				}
			}
			hqlQuery += "1=2) and j1.data_cancellazione is null ";
		}
		hqlQuery += "and a.data_cancellazione is null "
				+ "and e.data_cancellazione is null " + "and f.data_cancellazione is null "
				+ "and g.data_cancellazione is null " + "and i.data_Cancellazione is null "
				+ "and j.data_Cancellazione is null " + "and i2.data_Cancellazione is null "
				+ "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore "
				+ "and e.anno_gestione - date_part('year', b2.data_inizio_validita)>= 0 "
				+ "			and (e.anno_gestione - date_part('year', b2.data_fine_validita)<= 0 "
				+ "				or b2.data_fine_validita is null) "
				+ "and f.id_stato_rendicontazione = e.id_stato_rendicontazione ";
		if (ricerca.getStatoRendicontazione() != null) {
			hqlQuery += " and f.cod_stato_rendicontazione = :codStatoRend ";
		} else {
			hqlQuery += " and f.cod_stato_rendicontazione <>'CON' ";
		}
		if (ricerca.getAnnoEsercizio() != null) {
			hqlQuery += " and e.anno_gestione = :annoRend ";
		}
		hqlQuery += "and h.data_cancellazione is null AND ";

//		+ "LEFT JOIN greg_R_Schede_Enti_Gestori_Comuni c1 on c1.id_Scheda_Ente_Gestore = g.id_Scheda_Ente_Gestore "
		if (Checker.isValorizzato(ricerca.getDenominazioneEnte())) {
			hqlQuery += " UPPER(b2.denominazione) LIKE UPPER(:denominazione) AND ";
		}
		if (ricerca.getComune() != null) {
			hqlQuery += " j2.cod_istat_comune = :codComune AND ";
		}
		if (ricerca.getTipoEnte() != null) {
			hqlQuery += " i2.cod_Tipo_Ente = :codTipoEnte AND ";
		}

		if (ricerca.getCodiceRegionale() != null) {
			hqlQuery += " a.codice_regionale = :codiceRegionale AND ";
		}
		if (ricerca.getStatoEnte() != null && ricerca.getAnnoEsercizio() != null) {
			hqlQuery += " g1.cod_stato_ente = :codStatoEnte AND ";
		}
		hqlQuery += " 1 = 1 and a.id_scheda_ente_gestore not in(";
		hqlQuery += "select " + "	a.id_scheda_ente_gestore " + "from " + "	Greg_T_Schede_Enti_Gestori a, "
				+ "	greg_d_stato_ente g, " + "	greg_r_ente_gestore_stato_ente h, " + "	greg_d_tipo_ente i, ";
		if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) || ricerca.getAnnoEsercizio() != null) {
			hqlQuery += "greg_t_rendicontazione_ente e,  greg_d_stato_rendicontazione f, ";
		}
		hqlQuery += " greg_r_ente_gestore_contatti b " + "	left join greg_d_comuni j on j.id_comune = b.id_comune ";
		if (ricerca.getComune() != null) {
			hqlQuery += ", greg_r_schede_enti_gestori_comuni j1, greg_d_comuni j2 ";
		}
		hqlQuery += "where ";
		if (ricerca.getLista().size() >= 1) {
			hqlQuery += "a.id_scheda_ente_gestore in (";
			for (int ente : ricerca.getLista()) {
				hqlQuery += ente + ",";
			}
			hqlQuery = hqlQuery.substring(0, hqlQuery.length() - 1);
			hqlQuery += ") and ";
		}
		hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore   " + "and b.data_fine_validita is null "
				+ "and g.id_stato_ente = h.id_stato_ente "
				+ "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita > current_date and h.data_inizio_validita <= current_date)) "
				+ "and case " + "		when g.cod_stato_ente = 'APE' then h.data_inizio_validita <= current_date "
				+ "		else h.data_inizio_validita > current_date " + "end "
				+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   "
				+ "and i.id_tipo_ente = b.id_tipo_ente ";
		if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) || ricerca.getAnnoEsercizio() != null) {
			hqlQuery += "and e.data_cancellazione is null ";
			hqlQuery += "and f.data_cancellazione is null ";
			hqlQuery += "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore ";
			hqlQuery += "and f.id_stato_rendicontazione = e.id_stato_rendicontazione ";
			if (ricerca.getStatoRendicontazione() != null) {
				hqlQuery += " and f.cod_stato_rendicontazione = :codStatoRend ";
			} else {
				hqlQuery += " and f.cod_stato_rendicontazione <>'CON' ";
			}
			if (ricerca.getAnnoEsercizio() != null) {
				hqlQuery += " and e.anno_gestione = :annoRend ";
			}
		}
		if (ricerca.getComune() != null) {
			hqlQuery += "and j1.id_scheda_ente_gestore = a.id_scheda_ente_gestore "
					+ "and j1.id_comune = j2.id_comune ";
			if (ricerca.getAnnoEsercizio() == null) {
				hqlQuery += "and j1.data_fine_validita is null ";
			} else {
				hqlQuery += "and (" + ricerca.getAnnoEsercizio()
						+ " - date_part('year', j1.data_inizio_validita)>=0 and (" + ricerca.getAnnoEsercizio()
						+ " - date_part('year', j1.data_fine_validita)<=0 or j1.data_fine_validita is null)) ";
			}
			hqlQuery += "and j2.data_cancellazione is null " + "and j1.data_cancellazione is null ";
		}
		hqlQuery += "and a.data_cancellazione is null  " 
				+ "and g.data_cancellazione is null  " + "and h.data_cancellazione is null "
				+ "and i.data_Cancellazione is null " + "and j.data_Cancellazione is null " + "and ";
		if (ricerca.getTipoEnte() != null) {
			hqlQuery += "i.cod_Tipo_Ente = :codTipoEnte AND ";
		}
		if (Checker.isValorizzato(ricerca.getDenominazioneEnte())) {
			hqlQuery += " UPPER(b.denominazione) LIKE UPPER(:denominazione) AND ";
		}
		if (ricerca.getStatoEnte() != null) {
			hqlQuery += " g.cod_stato_ente = :codStatoEnte AND ";
		}
		
		if (ricerca.getComune() != null) {
			hqlQuery += " j2.cod_istat_comune = :codComune AND ";
		}
		hqlQuery += "1=1) ";
		hqlQuery += "ORDER BY codice_Regionale ";
		
		Query query = em.createNativeQuery(hqlQuery);

//		query.setParameter("annoGestione", Integer.parseInt(Converter.getAnno(new Date())) - 1);

		if (Checker.isValorizzato(ricerca.getDenominazioneEnte())) {
			query.setParameter("denominazione", "%" + ricerca.getDenominazioneEnte() + "%");
		}
		if (ricerca.getComune() != null) {
			query.setParameter("codComune", ricerca.getComune());
		}
		if (ricerca.getTipoEnte() != null) {
			query.setParameter("codTipoEnte", ricerca.getTipoEnte());
		}
		if (ricerca.getCodiceRegionale() != null) {
			query.setParameter("codiceRegionale", ricerca.getCodiceRegionale());
		}
		if (ricerca.getStatoRendicontazione() != null) {
			query.setParameter("codStatoRend", ricerca.getStatoRendicontazione());
		}
		if (ricerca.getAnnoEsercizio() != null) {
			query.setParameter("annoRend", ricerca.getAnnoEsercizio());
		}
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		return result;
	}

	public List<GregTCronologia> findCronologiaEnte(Integer idRendicontazione) {
		TypedQuery<GregTCronologia> query = em.createNamedQuery("GregTCronologia.findByIdNotDeleted",
				GregTCronologia.class);
		query.setParameter("idRendicontazione", idRendicontazione);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Integer> findAllAnnoEsercizio() {

		Query query = em.createNativeQuery("" + "select distinct g.anno_gestione "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_d_stato_rendicontazione stato on stato.id_stato_rendicontazione = g.id_stato_rendicontazione "
				+ "where stato.cod_stato_rendicontazione <> 'CON' " + "order by g.anno_gestione");

		ArrayList<Integer> result = (ArrayList<Integer>) query.getResultList();

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<ModelRendicontazioneEnte> findAllRendicontazioniApertebyIdScheda(Integer idSchedaEnteGestore) {

		List<ModelRendicontazioneEnte> lista = new ArrayList<ModelRendicontazioneEnte>();

		Query query = em.createNativeQuery(""
				+ "select distinct g.id_rendicontazione_ente, g.anno_gestione, stato.cod_stato_rendicontazione, stato.des_stato_rendicontazione, g.id_Scheda_ente_gestore  "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_d_stato_rendicontazione stato on stato.id_stato_rendicontazione = g.id_stato_rendicontazione  "
				+ "where stato.cod_stato_rendicontazione <> 'CON' " + "and g.id_scheda_ente_gestore = :schedaEnte "
				+ "and g.data_Cancellazione is null " + "and stato.data_Cancellazione is null "
				+ "order by g.anno_Gestione desc");
		query.setParameter("schedaEnte", idSchedaEnteGestore);
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();

		Iterator<Object> itr = result.iterator();
		while (itr.hasNext()) {
			Object[] o = (Object[]) itr.next();
			ModelRendicontazioneEnte rendicontazione = new ModelRendicontazioneEnte(o);
			lista.add(rendicontazione);
		}

		return lista;
	}

	@SuppressWarnings("unchecked")
	public ModelRendicontazioneEnte findRendicontazioniApertebyIdSchedaAndValue(Integer idSchedaEnteGestore,
			RicercaEntiGestori ricerca) {

		String sqlQuery = "select distinct g.id_rendicontazione_ente, g.anno_gestione, stato.cod_stato_rendicontazione, stato.des_stato_rendicontazione,g.id_Scheda_ente_gestore "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_d_stato_rendicontazione stato on stato.id_stato_rendicontazione = g.id_stato_rendicontazione "
				+ "where stato.cod_stato_rendicontazione <> 'CON' " + "and g.id_scheda_ente_gestore = :schedaEnte "
				+ "and g.data_Cancellazione is null " + "and stato.data_Cancellazione is null " + "AND ";
		if (ricerca.getStatoRendicontazione() != null) {
			sqlQuery += "stato.cod_stato_rendicontazione = :codStatoRendicontazione AND ";
		}


		sqlQuery += "g.anno_Gestione = :annoGestione AND ";
		

		sqlQuery += "1=1 " + "order by g.anno_Gestione";
		Query query = em.createNativeQuery(sqlQuery);

		query.setParameter("schedaEnte", idSchedaEnteGestore);
		if (ricerca.getStatoRendicontazione() != null) {
			query.setParameter("codStatoRendicontazione", ricerca.getStatoRendicontazione());
		}

		query.setParameter("annoGestione", ricerca.getAnnoEsercizio());
		Object[] result = (Object[]) query.getSingleResult();

		ModelRendicontazioneEnte rendicontazione = new ModelRendicontazioneEnte(result);

		return rendicontazione;
	}

	public List<Object> findModelli() {

		Query query = em
				.createNativeQuery("select a.cod_tab ,a.des_tab, cod_tranche, a.redirect "
						// cod_obbligo ,null as des_obbligo,0 as id_ente_tab "
						+ "from greg_d_tab a,greg_r_tab_tranche b, greg_d_tranche c " 
						+ "where a.id_tab = b.id_tab "
						+ "and c.id_tranche = b.id_tranche "
						+ "and a.data_cancellazione is null "
						+ "and c.data_cancellazione is null "
						+ "and b.data_cancellazione is null order by a.ordinamento");

		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();

		return result;
	}
	
	public Integer findStati(RicercaEntiGestori ricerca, String codStato) {
		try {
		String sqlQuery = "select count(*)  "
				+ "from greg_d_stato_rendicontazione a  "
				+ "left join greg_t_rendicontazione_ente b on a.id_stato_rendicontazione = b.id_stato_rendicontazione  "
				+ "left join greg_t_schede_enti_gestori c on c.id_scheda_ente_gestore = b.id_scheda_ente_gestore  "
				+ "where ";
				if (ricerca.getLista().size() >= 1) {
					sqlQuery += "c.id_scheda_ente_gestore in (";
					for (int ente : ricerca.getLista()) {
						sqlQuery += ente + ",";
					}
					sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 1);
					sqlQuery += ") and ";
				}
				sqlQuery += "b.anno_gestione =:annoEsercizio "
				+ "and a.cod_stato_rendicontazione = :codStato "
				+ "and a.data_cancellazione is null  "
				+ "and b.data_cancellazione is null  "
				+ "and c.data_cancellazione is null  "
				+ "group by a.cod_stato_rendicontazione, a.des_stato_rendicontazione, a.ordinamento  "
				+ "order by a.ordinamento  ";
		
		Query query = em
				.createNativeQuery(sqlQuery);
		query.setParameter("annoEsercizio", ricerca.getAnnoEsercizio());
		query.setParameter("codStato", codStato);
		
		BigInteger result = (BigInteger) query.getSingleResult();

		return result.intValue();
		} catch (NoResultException e) {
			return 0;
		}
	}

	public List<GregDStatoRendicontazione> findAllStato() {

		try {
			TypedQuery<GregDStatoRendicontazione> query = em.createNamedQuery("GregDStatoRendicontazione.findAllNotDeletedNotSto", GregDStatoRendicontazione.class);
			List<GregDStatoRendicontazione> resultList = query.getResultList();
			return resultList;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GregDTab> findModelliAssegnati(Integer idRendicontazione) {
		String hqlQuery = "select c "
				+ "from GregDTab c  "
				+ "left join GregREnteTab b on c.idTab = b.gregDTab.idTab "
				+ "left join GregTRendicontazioneEnte a on a.idRendicontazioneEnte = b.gregTRendicontazioneEnte.idRendicontazioneEnte "
				+ "where a.idRendicontazioneEnte = :idRendicontazione  "
				+ "and a.dataCancellazione is null  "
				+ "and b.dataCancellazione is null  "
				+ "and c.dataCancellazione is null  "
				+ "order by c.ordinamento";

	
		TypedQuery<GregDTab> query = em.createQuery(hqlQuery, GregDTab.class);
		query.setParameter("idRendicontazione", idRendicontazione);
		
		List<GregDTab> result = (ArrayList<GregDTab>) query.getResultList();

		return result;
	}
	
	public List<ModelStatoModelli> getStatoModello(Integer idRendicontazione) {
		String sqlQuery ="select "
				+ "	tab.cod_tab, "
				+ "	tranche.cod_tranche, "
				+ "	true as trovato, "
				+ "	tab.ordinamento  "
				+ "from "
				+ "	greg_d_tab tab  "
				+ "	left join greg_r_tab_tranche tabtranche on tab.id_tab = tabtranche.id_tab  "
				+ "	left join greg_d_tranche tranche on tranche.id_tranche = tabtranche.id_tranche  "
				+ "where "
				+ "	exists ( "
				+ "	select "
				+ "		entetab.id_tab "
				+ "	from "
				+ "		greg_r_ente_tab entetab "
				+ "	where "
				+ "		entetab.id_tab = tab.id_tab and "
				+ "		entetab.data_cancellazione is null and "
				+ "entetab.id_rendicontazione_ente = :idRendicontazione) "
				+ "union  "
				+ "select "
				+ "	tab.cod_tab, "
				+ "	tranche.cod_tranche, "
				+ "	false as trovato, "
				+ "	tab.ordinamento  "
				+ "from "
				+ "	greg_d_tab tab "
				+ "	left join greg_r_tab_tranche tabtranche on tab.id_tab = tabtranche.id_tab  "
				+ "		left join greg_d_tranche tranche on tranche.id_tranche = tabtranche.id_tranche  "
				+ "where "
				+ "	not exists ( "
				+ "	select "
				+ "		entetab.id_tab "
				+ "	from "
				+ "		greg_r_ente_tab entetab "
				+ "	where "
				+ "		entetab.id_tab = tab.id_tab and "
				+ "		entetab.data_cancellazione is null and "
				+ "entetab.id_rendicontazione_ente = :idRendicontazione) "
				+ "order by ordinamento ";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idRendicontazione", idRendicontazione);
		ArrayList<Object[]> result = (ArrayList<Object[]>) query.getResultList();
		List<ModelStatoModelli> modelli = new ArrayList<ModelStatoModelli>();
		for(Object[] o : result) {
			ModelStatoModelli modello = new ModelStatoModelli(o);
			modelli.add(modello);
		}
		
		return modelli;
	}
	
	
	public Integer getMaxAnno() {
		String sqlQuery ="select max(r.anno_gestione) "
				+ "from greg_t_rendicontazione_ente r "
				+ "where r.data_cancellazione is null;";
		Query query = em.createNativeQuery(sqlQuery);
		Integer result = (Integer) query.getSingleResult();
		
		return result;
	}
}
