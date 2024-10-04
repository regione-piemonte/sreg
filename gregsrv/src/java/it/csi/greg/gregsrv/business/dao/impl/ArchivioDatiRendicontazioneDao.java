/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregDTipoEnte;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.RicercaArchivioRendicontazione;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.util.Checker;

@Repository("archivioDatiRendicontazioneDao")
@Transactional(readOnly = true)
public class ArchivioDatiRendicontazioneDao {
	@PersistenceContext
	private EntityManager em;

	public List<GregDComuni> findAllComuni() {
		TypedQuery<GregDComuni> query = em.createNamedQuery("GregDComuni.findAllStorico", GregDComuni.class);
		return query.getResultList();
	}

	public List<GregDStatoRendicontazione> findAllStatoRendicontazione() {
		TypedQuery<GregDStatoRendicontazione> query = em.createNamedQuery("GregDStatoRendicontazione.findAll",
				GregDStatoRendicontazione.class);
		return query.getResultList();
	}

	public List<GregDTipoEnte> findAllTipoEnte() {
		TypedQuery<GregDTipoEnte> query = em.createNamedQuery("GregDTipoEnte.findAll", GregDTipoEnte.class);
		return query.getResultList();
	}

	public List<GregTSchedeEntiGestori> findArchivioRendicontazione() {

		String hqlQuery = "SELECT g FROM GregTSchedeEntiGestori g " + "LEFT JOIN Fetch g.gregTRendicontazioneEntes t "
				+ "LEFT JOIN Fetch t.gregDStatoRendicontazione d " + "LEFT JOIN Fetch g.gregDTipoEnte dt "
				+ "WHERE g.dataCancellazione IS NULL AND t.dataCancellazione IS NULL "
				+ "AND d.dataCancellazione IS NULL AND dt.dataCancellazione IS NULL " + "ORDER BY g.codiceRegionale ";

		TypedQuery<GregTSchedeEntiGestori> query = em.createQuery(hqlQuery, GregTSchedeEntiGestori.class);

		return query.getResultList();
	}

	public List<GregTSchedeEntiGestori> findArchivioRendicontazioneByValue(RicercaArchivioRendicontazione ricerca) {

		String hqlQuery = "SELECT g FROM GregTSchedeEntiGestori g " + "LEFT JOIN Fetch g.gregTRendicontazioneEntes t "
				+ "LEFT JOIN Fetch t.gregDStatoRendicontazione d " + "LEFT JOIN Fetch g.gregDTipoEnte dt "
				+ "WHERE g.dataCancellazione IS NULL AND t.dataCancellazione IS NULL "
				+ "AND d.dataCancellazione IS NULL AND dt.dataCancellazione IS NULL AND ";

		if (ricerca.getAnno() != null) {
			hqlQuery += " t.annoGestione = :annoGestione AND ";
		}
		if (ricerca.getStatoRendicontazione() != null) {
			hqlQuery += " d.idStatoRendicontazione = :idRendicontazione AND ";
		}
		if (Checker.isValorizzato(ricerca.getDenominazioneEnte())) {
			hqlQuery += " g.denominazione = :denominazione AND ";
		}
		if (ricerca.getComune() != null) {
			hqlQuery += " g.gregDComuni.idComune = :idComune AND ";
		}
		if (ricerca.getTipoEnte() != null) {
			hqlQuery += " dt.idTipoEnte = :idTipoEnte AND ";
		}
		if (Checker.isValorizzato(ricerca.getPartitaIva())) {
			hqlQuery += " g.partitaIva = :partitaIva AND ";
		}
		hqlQuery += " 1 = 1 ORDER BY g.codiceRegionale ";
		
		TypedQuery<GregTSchedeEntiGestori> query = em.createQuery(hqlQuery, GregTSchedeEntiGestori.class);

		if (ricerca.getAnno() != null) {
			query.setParameter("annoGestione", ricerca.getAnno());
		}
		if (ricerca.getStatoRendicontazione() != null) {
			query.setParameter("idRendicontazione", ricerca.getStatoRendicontazione());
		}
		if (Checker.isValorizzato(ricerca.getDenominazioneEnte())) {
			query.setParameter("denominazione", ricerca.getDenominazioneEnte());
		}
		if (ricerca.getComune() != null) {
			query.setParameter("idComune", ricerca.getComune());
		}
		if (ricerca.getTipoEnte() != null) {
			query.setParameter("idTipoEnte", ricerca.getTipoEnte());
		}
		if (Checker.isValorizzato(ricerca.getPartitaIva())) {
			query.setParameter("partitaIva", ricerca.getPartitaIva());
		}

		return query.getResultList();
	}

	public List<GregTCronologia> findCronologiaEnte(Integer idRendicontazione) {
		TypedQuery<GregTCronologia> query = em.createNamedQuery("GregTCronologia.findByIdNotDeleted",
				GregTCronologia.class);
		query.setParameter("idRendicontazione", idRendicontazione);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object> findSchedeEntiGestoriRendicontaioneConclusaByValue(RicercaEntiGestori ricerca) {

		List<Integer> anni = findAllAnnoEsercizio();

		String hqlQuery = "";

		hqlQuery += "select " + "	a.id_scheda_ente_gestore, " + "	a.codice_regionale, " + "	b.denominazione, "
				+ "	j.des_comune, " + "	i.cod_tipo_ente, " + "	i.des_tipo_ente, " + "	g.cod_stato_ente, "
				+ "	g.desc_stato_ente " + "from " + "	Greg_T_Schede_Enti_Gestori a, " + "	greg_d_stato_ente g, "
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
				+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   " + "and i.id_tipo_ente = b.id_tipo_ente ";
		if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) || ricerca.getAnnoEsercizio() != null) {
			hqlQuery += "and e.data_cancellazione is null ";
			hqlQuery += "and f.data_cancellazione is null ";
			hqlQuery += "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore ";
			hqlQuery += "and f.id_stato_rendicontazione = e.id_stato_rendicontazione ";
			if (ricerca.getStatoRendicontazione() != null) {
				hqlQuery += " and f.cod_stato_rendicontazione = :codStatoRend ";
			} else {
				hqlQuery += " and f.cod_stato_rendicontazione = 'CON' ";
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
		hqlQuery += "and a.data_cancellazione is null  " + "and g.data_cancellazione is null  "
				+ "and h.data_cancellazione is null " + "and i.data_Cancellazione is null "
				+ "and j.data_Cancellazione is null " + "and ";
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
		hqlQuery += "1=1 " + "union ";

		hqlQuery += "SELECT a.id_scheda_ente_gestore, a.codice_regionale, "
				+ "b.denominazione, j.des_comune, i.cod_tipo_ente, i.des_tipo_ente, g.cod_stato_ente, g.desc_stato_ente "
				+ "FROM Greg_T_Schede_Enti_Gestori a, " + "greg_r_ente_gestore_contatti b2, "
				+ "greg_t_rendicontazione_ente e,  greg_d_stato_rendicontazione f, "
				+ "greg_d_stato_ente g,greg_r_ente_gestore_stato_ente h, " + "greg_d_tipo_ente i, greg_d_tipo_ente i2, "
				+ "greg_r_ente_gestore_contatti b " + "left join greg_d_comuni j on j.id_comune = b.id_comune ";
		if (ricerca.getComune() != null) {
			hqlQuery += ", greg_r_schede_enti_gestori_comuni j1, greg_d_comuni j2 ";
		}
		if (ricerca.getStatoEnte() != null) {
			hqlQuery += ", greg_r_ente_gestore_stato_ente h1, greg_d_stato_ente g1 ";
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
		hqlQuery += "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore  " + "and i.id_tipo_ente = b.id_tipo_ente "
				+ "and i2.id_tipo_ente = b2.id_tipo_ente and (";
		for (Integer anno : anni) {
			if (ricerca.getAnnoEsercizio() == null) {
				hqlQuery += "(" + anno.intValue() + " - date_part('year', b2.data_inizio_validita)>=0 and ("
						+ anno.intValue()
						+ " - date_part('year', b2.data_fine_validita)<=0 or b2.data_fine_validita is null) "
						+ "and b2.data_inizio_validita = (select MAX(b2.data_inizio_validita) from greg_r_ente_gestore_contatti b2 where a.id_scheda_ente_gestore = b2.id_scheda_ente_gestore and "
						+ anno.intValue() + " - date_part('year', b2.data_inizio_validita)>=0)) or";
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
							+ "else h.data_fine_validita is null  " + "end) or";
				}
			}
		}
		hqlQuery += " 1=3) "
				+ "	and ((h.data_fine_validita is null "
				+ "				and h.data_inizio_validita <= current_date) "
				+ "			or (h.data_fine_validita > current_date "
				+ "				and h.data_inizio_validita <= current_date))"
		+ " ";
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
								+ " - date_part('year', j1.data_inizio_validita)>= 0) "
								+ "else j1.data_fine_validita is null  " + "end) or ";
					}
				}
			}
			hqlQuery += "1=2) and j1.data_cancellazione is null ";
		}
		if (ricerca.getStatoEnte() != null && ricerca.getAnnoEsercizio() != null) {
			hqlQuery += "and g1.id_stato_ente = h1.id_stato_ente  "
					+ "and a.id_scheda_ente_gestore = h1.id_scheda_ente_gestore " + "and " + ricerca.getAnnoEsercizio()
					+ " - date_part('year', h1.data_inizio_validita)>= 0 " + "and (" + ricerca.getAnnoEsercizio()
					+ "  - date_part('year', h1.data_fine_validita)<= 0 " + "or h1.data_fine_validita is null)"
					+ "and g1.data_cancellazione is null " + "and h1.data_cancellazione is null ";
		}
		hqlQuery += "and a.data_cancellazione is null " + "and e.data_cancellazione is null "
				+ "and f.data_cancellazione is null " + "and g.data_cancellazione is null "
				+ "and i.data_Cancellazione is null " + "and j.data_Cancellazione is null "
				+ "and i2.data_Cancellazione is null " + "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore "
				+ "and e.anno_gestione - date_part('year', b2.data_inizio_validita)>= 0 "
				+ "			and (e.anno_gestione - date_part('year', b2.data_fine_validita)<= 0 "
				+ "				or b2.data_fine_validita is null) "
				+ "and f.id_stato_rendicontazione = e.id_stato_rendicontazione ";
		if (ricerca.getStatoRendicontazione() != null) {
			hqlQuery += " and f.cod_stato_rendicontazione = :codStatoRend ";
		} else {
			hqlQuery += " and f.cod_stato_rendicontazione = 'CON' ";
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

		if (ricerca.getStatoEnte() != null && ricerca.getAnnoEsercizio() == null) {
			hqlQuery += " g.cod_stato_ente = :codStatoEnte AND ";
		}
		if (ricerca.getStatoEnte() != null && ricerca.getAnnoEsercizio() != null) {
			hqlQuery += " g1.cod_stato_ente = :codStatoEnte AND ";
		}
		hqlQuery += " 1 = 1 and a.id_scheda_ente_gestore not in(";
		hqlQuery += "select " + "	a.id_scheda_ente_gestore " + "from " + "	Greg_T_Schede_Enti_Gestori a, "
				+ "	greg_t_responsabile_ente_gestore c, " + "	greg_r_responsabile_contatti d, "
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
				+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   " + "and i.id_tipo_ente = b.id_tipo_ente ";
		if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) || ricerca.getAnnoEsercizio() != null) {
			hqlQuery += "and e.data_cancellazione is null ";
			hqlQuery += "and f.data_cancellazione is null ";
			hqlQuery += "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore ";
			hqlQuery += "and f.id_stato_rendicontazione = e.id_stato_rendicontazione ";
			if (ricerca.getStatoRendicontazione() != null) {
				hqlQuery += " and f.cod_stato_rendicontazione = :codStatoRend ";
			} else {
				hqlQuery += " and f.cod_stato_rendicontazione = 'CON' ";
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
		hqlQuery += "and a.data_cancellazione is null  " + "and g.data_cancellazione is null  "
				+ "and h.data_cancellazione is null " + "and i.data_Cancellazione is null "
				+ "and j.data_Cancellazione is null " + "and ";
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
		if (ricerca.getStatoEnte() != null) {
			query.setParameter("codStatoEnte", ricerca.getStatoEnte());
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

	@SuppressWarnings("unchecked")
	public List<Object> findSchedeEntiGestoriRendicontazioneConclusa(RicercaEntiGestori ricerca) {
		String hqlQuery = "SELECT a.id_scheda_ente_gestore, a.codice_regionale,  "
				+ "b.denominazione, j.des_comune, i.cod_tipo_ente, i.des_tipo_ente, g.cod_stato_ente, g.desc_stato_ente  "
				+ "FROM Greg_T_Schede_Enti_Gestori a,  " + "greg_d_stato_ente g,greg_r_ente_gestore_stato_ente h,  "
				+ "greg_d_tipo_ente i, " + "greg_r_ente_gestore_contatti b "
				+ "left join greg_d_comuni j on j.id_comune = b.id_comune " + "where ";
		if (ricerca.getLista().size() >= 1) {
			hqlQuery += "a.id_scheda_ente_gestore in (";
			for (int ente : ricerca.getLista()) {
				hqlQuery += ente + ",";
			}
			hqlQuery = hqlQuery.substring(0, hqlQuery.length() - 1);
			hqlQuery += ") and ";
		}
		hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore   " + "and b.data_fine_validita is null   "
				+ "and g.id_stato_ente = h.id_stato_ente  "
				+ "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita >= current_date and h.data_inizio_validita <= current_date)) "
				+ "and case  " + "		when g.cod_stato_ente = 'APE' then h.data_inizio_validita <= current_date  "
				+ "		else h.data_inizio_validita > current_date  " + "end  "
				+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   "
				+ "and i.id_tipo_ente = b.id_tipo_ente   " + "and a.data_cancellazione is null  "
				+ "and g.data_cancellazione is null  " + "and h.data_cancellazione is null  "
				+ "and i.data_Cancellazione is null " + "and j.data_Cancellazione is null " + "union  "
				+ "SELECT a.id_scheda_ente_gestore, a.codice_regionale,  "
				+ "b.denominazione, j.des_comune, i.cod_tipo_ente, i.des_tipo_ente, g.cod_stato_ente, g.desc_stato_ente  "
				+ "FROM Greg_T_Schede_Enti_Gestori a,  "
				+ "greg_t_rendicontazione_ente e, greg_d_stato_rendicontazione f, "
				+ "greg_d_stato_ente g,greg_r_ente_gestore_stato_ente h,  " + "greg_d_tipo_ente i,"
				+ "greg_r_ente_gestore_contatti b " + "left join greg_d_comuni j on j.id_comune = b.id_comune "
				+ "where ";
		if (ricerca.getLista().size() >= 1) {
			hqlQuery += "a.id_scheda_ente_gestore in (";
			for (int ente : ricerca.getLista()) {
				hqlQuery += ente + ",";
			}
			hqlQuery = hqlQuery.substring(0, hqlQuery.length() - 1);
			hqlQuery += ") and ";
		}
		hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore " + "and b.data_fine_validita is null "
				+ "and g.id_stato_ente = h.id_stato_ente  "
				+ "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita >= current_date and h.data_inizio_validita <= current_date))  "
				+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   "
				+ "and i.id_tipo_ente = b.id_tipo_ente   " + "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore "
				+ "and f.id_stato_rendicontazione = e.id_stato_rendicontazione  "
				+ "and f.cod_stato_rendicontazione = 'CON' " + "and a.data_cancellazione is null  "
				+ "and g.data_cancellazione is null  " + "and h.data_cancellazione is null  "
				+ "and f.data_Cancellazione is null " + "and i.data_Cancellazione is null "
				+ "and j.data_Cancellazione is null " + "and e.data_Cancellazione is null "
				+ "and a.id_scheda_ente_gestore not in ( select " + "	a.id_scheda_ente_gestore " + "from "
				+ "	Greg_T_Schede_Enti_Gestori a, " + "	greg_d_stato_ente g, " + "	greg_r_ente_gestore_stato_ente h, "
				+ "	greg_d_tipo_ente i, " + "	greg_r_ente_gestore_contatti b " + "left join greg_d_comuni j on "
				+ "	j.id_comune = b.id_comune " + "where ";
		if (ricerca.getLista().size() >= 1) {
			hqlQuery += "a.id_scheda_ente_gestore in (";
			for (int ente : ricerca.getLista()) {
				hqlQuery += ente + ",";
			}
			hqlQuery = hqlQuery.substring(0, hqlQuery.length() - 1);
			hqlQuery += ") and ";
		}
		hqlQuery += " a.id_scheda_ente_gestore = b.id_scheda_ente_gestore " + "	and b.data_fine_validita is null "
				+ "	and g.id_stato_ente = h.id_stato_ente " + "	and ((h.data_fine_validita is null "
				+ "		and h.data_inizio_validita <= current_date) " + "	or (h.data_fine_validita >= current_date "
				+ "		and h.data_inizio_validita <= current_date)) " + "	and " + "	case "
				+ "		when g.cod_stato_ente = 'APE' then h.data_inizio_validita <= current_date "
				+ "		else h.data_inizio_validita > current_date " + "	end "
				+ "	and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore "
				+ "	and i.id_tipo_ente = b.id_tipo_ente " + "	and a.data_cancellazione is null "
				+ "	and g.data_cancellazione is null " + "	and h.data_cancellazione is null "
				+ "	and i.data_Cancellazione is null " + "	and j.data_Cancellazione is null)"
				+ "order by codice_regionale ";

		Query query = em.createNativeQuery(hqlQuery);
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		return result;
	}

	public List<Integer> findAllAnnoEsercizio() {

		Query query = em.createNativeQuery("" + "select distinct g.anno_gestione "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_d_stato_rendicontazione stato on stato.id_stato_rendicontazione = g.id_stato_rendicontazione "
				+ "where stato.cod_stato_rendicontazione = 'CON' " + "order by g.anno_gestione");

		ArrayList<Integer> result = (ArrayList<Integer>) query.getResultList();

		return result;
	}

	public GregDStatoRendicontazione findStatoRendicontazioneConclusa() {
		TypedQuery<GregDStatoRendicontazione> query = em.createNamedQuery("GregDStatoRendicontazione.findStatoConcluso",
				GregDStatoRendicontazione.class);
		return query.getSingleResult();
	}

	public List<ModelRendicontazioneEnte> findRendicontazioniApertebyIdSchedaAndValue(Integer idSchedaEnteGestore,
			RicercaEntiGestori ricerca) {

		List<ModelRendicontazioneEnte> lista = new ArrayList<ModelRendicontazioneEnte>();

		String sqlQuery = ""
				+ "select distinct g.id_rendicontazione_ente, g.anno_gestione, stato.cod_stato_rendicontazione, stato.des_stato_rendicontazione,g.id_Scheda_ente_gestore "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_d_stato_rendicontazione stato on stato.id_stato_rendicontazione = g.id_stato_rendicontazione  "
				+ "where stato.cod_stato_rendicontazione = 'CON' " + "and g.id_scheda_ente_gestore = :schedaEnte "
				+ "and g.data_Cancellazione is null " + "and stato.data_Cancellazione is null " + "AND ";
		if (ricerca.getStatoRendicontazione() != null) {
			sqlQuery += "stato.cod_stato_rendicontazione = :codStatoRendicontazione AND ";
		}

		if (ricerca.getAnnoEsercizio() != null) {
			sqlQuery += "g.anno_Gestione = :annoGestione AND ";
		}

		sqlQuery += "1=1 " + "order by g.anno_Gestione desc";

		Query query = em.createNativeQuery(sqlQuery);

		query.setParameter("schedaEnte", idSchedaEnteGestore);
		if (ricerca.getStatoRendicontazione() != null) {
			query.setParameter("codStatoRendicontazione", ricerca.getStatoRendicontazione());
		}
		if (ricerca.getAnnoEsercizio() != null) {
			query.setParameter("annoGestione", ricerca.getAnnoEsercizio());
		}
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();

		Iterator<Object> itr = result.iterator();
		while (itr.hasNext()) {
			Object[] o = (Object[]) itr.next();
			ModelRendicontazioneEnte rendicontazione = new ModelRendicontazioneEnte(o);
			lista.add(rendicontazione);
		}

		return lista;
	}

	public List<ModelRendicontazioneEnte> findAllRendicontazioniApertebyIdScheda(Integer idSchedaEnteGestore) {

		List<ModelRendicontazioneEnte> lista = new ArrayList<ModelRendicontazioneEnte>();

		Query query = em.createNativeQuery(""
				+ "select distinct g.id_rendicontazione_ente, g.anno_gestione, stato.cod_stato_rendicontazione, stato.des_stato_rendicontazione, g.id_Scheda_ente_gestore  "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_d_stato_rendicontazione stato on stato.id_stato_rendicontazione = g.id_stato_rendicontazione  "
				+ "where stato.cod_stato_rendicontazione = 'CON' " + "and g.id_scheda_ente_gestore = :schedaEnte "
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

}
