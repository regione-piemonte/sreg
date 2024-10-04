/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.math.BigInteger;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.entity.GregDComuni;
import it.csi.greg.gregsrv.business.entity.GregDStatoEnte;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregDTipoEnte;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.ModelAzione;
import it.csi.greg.gregsrv.dto.ModelLista;
import it.csi.greg.gregsrv.dto.ModelProfilo;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.ModelRicercaEntiGestoriAttivi;
import it.csi.greg.gregsrv.dto.ModelStoricoEnte;
import it.csi.greg.gregsrv.dto.ResultCreaAnno;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;


@Repository("entiGestoriAttiviDao")
@Transactional(readOnly=true)
public class EntiGestoriAttiviDao  {
	@PersistenceContext
	private EntityManager em;
	
	
	public List<GregDComuni> findAllComuni(String codregione, Date dataValidita) {		
		TypedQuery<GregDComuni> query = null;
		if (dataValidita != null) {
		query = em.createNamedQuery("GregDComuni.findAllNotDeleted", GregDComuni.class);
		query.setParameter("dataValidita",dataValidita);
		query.setParameter("codregione",codregione);
		}
		else {
			query = em.createNamedQuery("GregDComuni.findAllStorico", GregDComuni.class);	
		}
		return query.getResultList();
	}
	
	public List<GregDStatoRendicontazione> findAllStatoRendicontazione() {
		TypedQuery<GregDStatoRendicontazione> query = em.createNamedQuery("GregDStatoRendicontazione.findAllNotDeletedNotSto", GregDStatoRendicontazione.class);
		return query.getResultList();
	}
	
	public List<GregDTipoEnte> findAllTipoEnte() {
		TypedQuery<GregDTipoEnte> query = em.createNamedQuery("GregDTipoEnte.findAllNotDeleted", GregDTipoEnte.class);
		return query.getResultList();
	}
	
	public List<GregTSchedeEntiGestori> findAllDenominazioni() {
		TypedQuery<GregTSchedeEntiGestori> query = em.createNamedQuery("GregTSchedeEntiGestori.findAllNotDeleted", GregTSchedeEntiGestori.class);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getListaDenominazioniWithComuniAssociati () {
		
		Query query = em.createNativeQuery(""
		+ "select g.id_scheda_ente_gestore, d.id_comune, a.denominazione,d.des_comune "
		+ "FROM Greg_R_Schede_Enti_Gestori_Comuni g "
		+ "LEFT JOIN greg_t_schede_enti_gestori t on g.id_scheda_ente_gestore = t.id_scheda_ente_gestore " 
		+ "LEFT JOIN greg_d_comuni d on g.id_comune = d.id_comune, "
		+ "greg_r_ente_gestore_contatti a "
		+ "WHERE t.data_cancellazione is null " 
		+ "AND g.data_cancellazione is null "
		+ "and a.id_scheda_ente_gestore = g.id_scheda_ente_gestore " 
		+ "and a.data_fine_validita is null "
		+ "and g.data_fine_validita is null "
		+ "and d.data_Cancellazione is null "
		+ "ORDER BY a.denominazione ");
		
		
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		
		return result;
	}	
	
@SuppressWarnings("unchecked")
	public List<Object> findSchedeEntiGestori(RicercaEntiGestori ricerca) {
		String hqlQuery = "SELECT a.id_scheda_ente_gestore, a.codice_regionale,  "
				+ "b.denominazione, j.des_comune, i.cod_tipo_ente, i.des_tipo_ente, g.cod_stato_ente, g.desc_stato_ente  "
				+ "FROM Greg_T_Schede_Enti_Gestori a,  "
				+ "greg_d_stato_ente g,greg_r_ente_gestore_stato_ente h,  "
				+ "greg_d_tipo_ente i, "
				+ "greg_r_ente_gestore_contatti b "
				+ "left join greg_d_comuni j on j.id_comune = b.id_comune "
				+ "where ";
				if (ricerca.getLista().size()>=1) {
					hqlQuery +="a.id_scheda_ente_gestore in (";
					for (int ente : ricerca.getLista()) {
						hqlQuery += ente + ",";
					}
					hqlQuery = hqlQuery.substring(0, hqlQuery.length()-1);
					hqlQuery +=") and ";
				}
				hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore   "
				+ "and b.data_fine_validita is null   "
				+ "and g.id_stato_ente = h.id_stato_ente  "
				+ "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita >= current_date and h.data_inizio_validita <= current_date)) "
				+ "and case  "
				+ "		when g.cod_stato_ente = 'APE' then h.data_inizio_validita <= current_date  "
				+ "		else h.data_inizio_validita > current_date  "
				+ "end  "
				+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   "
				+ "and i.id_tipo_ente = b.id_tipo_ente   "
				+ "and a.data_cancellazione is null  "
				+ "and g.data_cancellazione is null  "
				+ "and h.data_cancellazione is null  "
				+ "and i.data_Cancellazione is null "
				+ "and j.data_Cancellazione is null "
				+ "union  "
				+ "SELECT a.id_scheda_ente_gestore, a.codice_regionale,  "
				+ "b.denominazione, j.des_comune, i.cod_tipo_ente, i.des_tipo_ente, g.cod_stato_ente, g.desc_stato_ente  "
				+ "FROM Greg_T_Schede_Enti_Gestori a,  "
				+ "greg_t_rendicontazione_ente e, greg_d_stato_rendicontazione f, "
				+ "greg_d_stato_ente g,greg_r_ente_gestore_stato_ente h,  "
				+ "greg_d_tipo_ente i,"
				+ "greg_r_ente_gestore_contatti b "
				+ "left join greg_d_comuni j on j.id_comune = b.id_comune "
				+ "where ";
				if (ricerca.getLista().size()>=1) {
					hqlQuery +="a.id_scheda_ente_gestore in (";
					for (int ente : ricerca.getLista()) {
						hqlQuery += ente + ",";
					}
					hqlQuery = hqlQuery.substring(0, hqlQuery.length()-1);
					hqlQuery +=") and ";
				}
				hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore "
				+ "and b.data_fine_validita is null "
				+ "and g.id_stato_ente = h.id_stato_ente  "
				+ "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita >= current_date and h.data_inizio_validita <= current_date))  "
				+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   "
				+ "and i.id_tipo_ente = b.id_tipo_ente   "
				+ "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore "
				+ "and f.id_stato_rendicontazione = e.id_stato_rendicontazione  "
				+ "and f.cod_stato_rendicontazione <> 'CON' "
				+ "and a.data_cancellazione is null  "
				+ "and g.data_cancellazione is null  "
				+ "and h.data_cancellazione is null  "
				+ "and f.data_Cancellazione is null "
				+ "and i.data_Cancellazione is null "
				+ "and j.data_Cancellazione is null "
				+ "and e.data_Cancellazione is null "
				+ "and a.id_scheda_ente_gestore not in ( select "
				+ "	a.id_scheda_ente_gestore "
				+ "from "
				+ "	Greg_T_Schede_Enti_Gestori a, "
				+ "	greg_d_stato_ente g, "
				+ "	greg_r_ente_gestore_stato_ente h, "
				+ "	greg_d_tipo_ente i, "
				+ "	greg_r_ente_gestore_contatti b "
				+ "left join greg_d_comuni j on "
				+ "	j.id_comune = b.id_comune "
				+ "where ";
				if (ricerca.getLista().size()>=1) {
					hqlQuery +="a.id_scheda_ente_gestore in (";
					for (int ente : ricerca.getLista()) {
						hqlQuery += ente + ",";
					}
					hqlQuery = hqlQuery.substring(0, hqlQuery.length()-1);
					hqlQuery +=") and ";
				}
				hqlQuery += " a.id_scheda_ente_gestore = b.id_scheda_ente_gestore "
				+ "	and b.data_fine_validita is null "
				+ "	and g.id_stato_ente = h.id_stato_ente "
				+ "	and ((h.data_fine_validita is null "
				+ "		and h.data_inizio_validita <= current_date) "
				+ "	or (h.data_fine_validita >= current_date "
				+ "		and h.data_inizio_validita <= current_date)) "
				+ "	and "
				+ "	case "
				+ "		when g.cod_stato_ente = 'APE' then h.data_inizio_validita <= current_date "
				+ "		else h.data_inizio_validita > current_date "
				+ "	end "
				+ "	and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore "
				+ "	and i.id_tipo_ente = b.id_tipo_ente "
				+ "	and a.data_cancellazione is null "
				+ "	and g.data_cancellazione is null "
				+ "	and h.data_cancellazione is null "
				+ "	and i.data_Cancellazione is null "
				+ "	and j.data_Cancellazione is null)"
				+ "order by codice_regionale ";
			
		Query query = em.createNativeQuery(hqlQuery);
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		return result;
	}
	
	public List<GregTSchedeEntiGestori> findSchedeMultiEntiGestori(ArrayList<Integer> listaenti) {
		
		String hqlQuery = "SELECT g FROM GregTSchedeEntiGestori g "
				+ "WHERE ";
			if (listaenti.size()>1) {
				hqlQuery +="g.idSchedaEnteGestore in (";
				for (int ente : listaenti) {
					hqlQuery += ente + ",";
				}
				hqlQuery = hqlQuery.substring(0, hqlQuery.length()-1);
				hqlQuery +=") ";
			}
			hqlQuery += "and g.dataCancellazione is null ";
			hqlQuery += "ORDER BY g.codiceRegionale ";
		
		
		TypedQuery<GregTSchedeEntiGestori> query = em.createQuery(hqlQuery, GregTSchedeEntiGestori.class);	
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> findSchedeEntiGestoriByValue(RicercaEntiGestori ricerca) {
		
		List<Integer> anni = findAllAnnoEsercizio();
		
		String hqlQuery = "";
				
					hqlQuery += "select "
					+ "	a.id_scheda_ente_gestore, "
					+ "	a.codice_regionale, "
					+ "	b.denominazione, "
					+ "	j.des_comune, "
					+ "	i.cod_tipo_ente, "
					+ "	i.des_tipo_ente, "
					+ "	g.cod_stato_ente, "
					+ "	g.desc_stato_ente "
					+ "from "
					+ "	Greg_T_Schede_Enti_Gestori a, "
					+ "	greg_d_stato_ente g, "
					+ "	greg_r_ente_gestore_stato_ente h, "
					+ "	greg_d_tipo_ente i, ";
					if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) ||ricerca.getAnnoEsercizio() != null) {
						hqlQuery +=  "greg_t_rendicontazione_ente e,  greg_d_stato_rendicontazione f, ";
					}
					hqlQuery +=" greg_r_ente_gestore_contatti b "
					+ "	left join greg_d_comuni j on j.id_comune = b.id_comune ";
					if (ricerca.getComune() != null) {
						hqlQuery += ", greg_r_schede_enti_gestori_comuni j1, greg_d_comuni j2 ";
					}
					hqlQuery += "where ";
					if (ricerca.getLista().size()>=1) {
						hqlQuery +="a.id_scheda_ente_gestore in (";
						for (int ente : ricerca.getLista()) {
							hqlQuery += ente + ",";
						}
						hqlQuery = hqlQuery.substring(0, hqlQuery.length()-1);
						hqlQuery +=") and ";
					}
					hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore   "
					+ "and b.data_fine_validita is null "
					+ "and g.id_stato_ente = h.id_stato_ente "
					+ "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita > current_date and h.data_inizio_validita <= current_date)) "
					+ "and case "
					+ "		when g.cod_stato_ente = 'APE' then h.data_inizio_validita <= current_date "
					+ "		else h.data_inizio_validita > current_date "
					+ "end "
					+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   "
					+ "and i.id_tipo_ente = b.id_tipo_ente ";
					if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) ||ricerca.getAnnoEsercizio() != null) {
						hqlQuery += "and e.data_cancellazione is null ";
						hqlQuery += "and f.data_cancellazione is null ";
						hqlQuery +=  "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore ";
						hqlQuery +=  "and f.id_stato_rendicontazione = e.id_stato_rendicontazione ";
						if (ricerca.getStatoRendicontazione() != null) {
							hqlQuery +=  " and f.cod_stato_rendicontazione = :codStatoRend ";
						}
						else {
							hqlQuery +=  " and f.cod_stato_rendicontazione <>'CON' ";
						}
						if (ricerca.getAnnoEsercizio() != null) {
							hqlQuery +=  " and e.anno_gestione = :annoRend ";
						}
					}	
					if (ricerca.getComune() != null) {
						hqlQuery +=  "and j1.id_scheda_ente_gestore = a.id_scheda_ente_gestore "
								+ "and j1.id_comune = j2.id_comune "; 
						if(ricerca.getAnnoEsercizio()==null) {
							hqlQuery += "and j1.data_fine_validita is null ";
						} else {
							hqlQuery += "and (" + ricerca.getAnnoEsercizio() + " - date_part('year', j1.data_inizio_validita)>=0 and (" + ricerca.getAnnoEsercizio() + " - date_part('year', j1.data_fine_validita)<=0 or j1.data_fine_validita is null)) ";
						}
						hqlQuery += "and j2.data_cancellazione is null "
								+ "and j1.data_cancellazione is null ";
					}
					hqlQuery += "and a.data_cancellazione is null  "
					+ "and g.data_cancellazione is null  "
					+ "and h.data_cancellazione is null "
					+ "and i.data_Cancellazione is null "
					+ "and j.data_Cancellazione is null "
					+ "and ";
					if (ricerca.getTipoEnte() != null) {
						hqlQuery +=  "i.cod_Tipo_Ente = :codTipoEnte AND ";
					}
					if (Checker.isValorizzato(ricerca.getDenominazioneEnte())) {
						hqlQuery +=  " UPPER(b.denominazione) LIKE UPPER(:denominazione) AND ";
					}
					if (ricerca.getStatoEnte() != null) {
						hqlQuery +=  " g.cod_stato_ente = :codStatoEnte AND ";
					}
					if (ricerca.getComune() != null) {
						hqlQuery +=  " j2.cod_istat_comune = :codComune AND ";
					}
					hqlQuery += "1=1 "
						+ "union ";
				
				hqlQuery += "SELECT a.id_scheda_ente_gestore, a.codice_regionale, "
				+ "b.denominazione, j.des_comune, i.cod_tipo_ente, i.des_tipo_ente, g.cod_stato_ente, g.desc_stato_ente "
				+ "FROM Greg_T_Schede_Enti_Gestori a, "
				+ "greg_r_ente_gestore_contatti b2, "
				+ "greg_t_rendicontazione_ente e,  greg_d_stato_rendicontazione f, "
				+ "greg_d_stato_ente g,greg_r_ente_gestore_stato_ente h, "
				+ "greg_d_tipo_ente i, greg_d_tipo_ente i2, "
				+ "greg_r_ente_gestore_contatti b "
				+ "left join greg_d_comuni j on j.id_comune = b.id_comune ";
				if (ricerca.getComune() != null) {
					hqlQuery += ", greg_r_schede_enti_gestori_comuni j1, greg_d_comuni j2 ";
				}
				if (ricerca.getStatoEnte() != null) {
					hqlQuery += ", greg_r_ente_gestore_stato_ente h1, greg_d_stato_ente g1 ";
				}
				hqlQuery += "where ";
				if (ricerca.getLista().size()>=1) {
					hqlQuery +="a.id_scheda_ente_gestore in (";
					for (int ente : ricerca.getLista()) {
						hqlQuery += ente + ",";
					}
					hqlQuery = hqlQuery.substring(0, hqlQuery.length()-1);
					hqlQuery +=") and ";
				}
				hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore  "
				+ "and b.data_fine_validita is null  "
				+ "and a.id_scheda_ente_gestore = b2.id_scheda_ente_gestore "
				+ "and g.id_stato_ente = h.id_stato_ente ";
				if(ricerca.getAnnoEsercizio()==null) {
					hqlQuery += "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita > current_date and h.data_inizio_validita <= current_date)) ";
				}
				hqlQuery += "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore  "
				+ "and i.id_tipo_ente = b.id_tipo_ente "
				+ "and i2.id_tipo_ente = b2.id_tipo_ente and (";
				for(Integer anno : anni) {
					if(ricerca.getAnnoEsercizio()==null) {
						hqlQuery += "(" + anno.intValue() + " - date_part('year', b2.data_inizio_validita)>=0 and (" + anno.intValue() + " - date_part('year', b2.data_fine_validita)<=0 or b2.data_fine_validita is null) "
								+ "and b2.data_inizio_validita = (select MAX(b2.data_inizio_validita) from greg_r_ente_gestore_contatti b2 where a.id_scheda_ente_gestore = b2.id_scheda_ente_gestore and " + anno.intValue() + " - date_part('year', b2.data_inizio_validita)>=0)) or";
					} else {
						if(ricerca.getAnnoEsercizio().equals(anno)) {
							hqlQuery += "(" + anno.intValue() + " - date_part('year', b2.data_inizio_validita)>=0 and (" + anno.intValue() + " - date_part('year', b2.data_fine_validita)<=0 or b2.data_fine_validita is null) "
									+ "and case  "
									+ "when b2.data_fine_validita is not null then "
									+ "b2.data_inizio_validita = ( "
									+ "select "
									+ "MAX(b2.data_inizio_validita) "
									+ "from "
									+ "greg_r_ente_gestore_contatti b2 "
									+ "where "
									+ "a.id_scheda_ente_gestore = b2.id_scheda_ente_gestore "
									+ "and " + anno.intValue() + " - date_part('year', b2.data_inizio_validita)>= 0) "
									+ "else h.data_fine_validita is null  "
									+ "end"
									+ ") or";
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
					+ "and j2.id_comune = j1.id_comune "
					+ "and j2.data_cancellazione is null "
					+ "and j1.data_cancellazione is null "
					+ "and (";
					for(Integer anno : anni) {
						if(ricerca.getAnnoEsercizio()==null) {
							hqlQuery += "(" + anno.intValue() + " - date_part('year', j1.data_inizio_validita)>=0 and (" + anno.intValue() + " - date_part('year', j1.data_fine_validita)<=0 or j1.data_fine_validita is null)) or ";
						} else {
							if(ricerca.getAnnoEsercizio().equals(anno)) {
								hqlQuery += "(" + anno.intValue() + " - date_part('year', j1.data_inizio_validita)>=0 and (" + anno.intValue() + " - date_part('year', j1.data_fine_validita)<=0 or j1.data_fine_validita is null) "
										+ "and case  "
										+ "when j1.data_fine_validita is not null then "
										+ "j1.data_inizio_validita = ( "
										+ "select "
										+ "MAX(j1.data_inizio_validita) "
										+ "from "
										+ "greg_r_schede_enti_gestori_comuni j1 "
										+ "where "
										+ "a.id_scheda_ente_gestore = j1.id_scheda_ente_gestore "
										+ "and " + anno.intValue() + " - date_part('year', j1.data_inizio_validita)>= 0) "
										+ "else j1.data_fine_validita is null  "
										+ "end) or ";
							}
						}
					}
					hqlQuery += "1=2) and j1.data_cancellazione is null ";
				}
				if (ricerca.getStatoEnte() != null && ricerca.getAnnoEsercizio()!=null) {
					hqlQuery += "and g1.id_stato_ente = h1.id_stato_ente  "
					+ "and a.id_scheda_ente_gestore = h1.id_scheda_ente_gestore "
					+ "and "+ ricerca.getAnnoEsercizio() + " - date_part('year', h1.data_inizio_validita)>= 0 "
					+ "and (" + ricerca.getAnnoEsercizio() + "  - date_part('year', h1.data_fine_validita)<= 0 "
					+ "or h1.data_fine_validita is null)"
					+ "and g1.data_cancellazione is null "
					+ "and h1.data_cancellazione is null ";
				}
				hqlQuery += "and a.data_cancellazione is null "
				+ "and e.data_cancellazione is null "
				+ "and f.data_cancellazione is null "
				+ "and g.data_cancellazione is null "
				+ "and i.data_Cancellazione is null "
				+ "and j.data_Cancellazione is null "
				+ "and i2.data_Cancellazione is null "
				+ "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore "
				+ "and e.anno_gestione - date_part('year', b2.data_inizio_validita)>= 0 "
				+ "			and (e.anno_gestione - date_part('year', b2.data_fine_validita)<= 0 "
				+ "				or b2.data_fine_validita is null) "
				+ "and f.id_stato_rendicontazione = e.id_stato_rendicontazione ";
				if (ricerca.getStatoRendicontazione() != null) {
					hqlQuery +=  " and f.cod_stato_rendicontazione = :codStatoRend ";
				}
				else {
					hqlQuery +=  " and f.cod_stato_rendicontazione <>'CON' ";
				}
				if (ricerca.getAnnoEsercizio() != null) {
					hqlQuery +=  " and e.anno_gestione = :annoRend ";
				}
				hqlQuery += "and h.data_cancellazione is null AND ";
		
		
//		+ "LEFT JOIN greg_R_Schede_Enti_Gestori_Comuni c1 on c1.id_Scheda_Ente_Gestore = g.id_Scheda_Ente_Gestore "
		if (Checker.isValorizzato(ricerca.getDenominazioneEnte())) {
			hqlQuery +=  " UPPER(b2.denominazione) LIKE UPPER(:denominazione) AND ";
		}
		if (ricerca.getComune() != null) {
			hqlQuery +=  " j2.cod_istat_comune = :codComune AND ";
		}
		if (ricerca.getTipoEnte() != null) {
			hqlQuery +=  " i2.cod_Tipo_Ente = :codTipoEnte AND ";
		}
		
		if (ricerca.getStatoEnte() != null && ricerca.getAnnoEsercizio()==null) {
			hqlQuery +=  " g.cod_stato_ente = :codStatoEnte AND ";
		}
		if (ricerca.getStatoEnte() != null && ricerca.getAnnoEsercizio()!=null) {
			hqlQuery +=  " g1.cod_stato_ente = :codStatoEnte AND ";
		}
		hqlQuery +=  " 1 = 1 and a.id_scheda_ente_gestore not in(";
				hqlQuery += "select "
				+ "	a.id_scheda_ente_gestore "
				+ "from "
				+ "	Greg_T_Schede_Enti_Gestori a, "
				+ "	greg_t_responsabile_ente_gestore c, "
				+ "	greg_r_responsabile_contatti d, "
				+ "	greg_d_stato_ente g, "
				+ "	greg_r_ente_gestore_stato_ente h, "
				+ "	greg_d_tipo_ente i, ";
				if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) ||ricerca.getAnnoEsercizio() != null) {
					hqlQuery +=  "greg_t_rendicontazione_ente e,  greg_d_stato_rendicontazione f, ";
				}
				hqlQuery +=" greg_r_ente_gestore_contatti b "
				+ "	left join greg_d_comuni j on j.id_comune = b.id_comune ";
				if (ricerca.getComune() != null) {
					hqlQuery += ", greg_r_schede_enti_gestori_comuni j1, greg_d_comuni j2 ";
				}
				hqlQuery += "where ";
				if (ricerca.getLista().size()>=1) {
					hqlQuery +="a.id_scheda_ente_gestore in (";
					for (int ente : ricerca.getLista()) {
						hqlQuery += ente + ",";
					}
					hqlQuery = hqlQuery.substring(0, hqlQuery.length()-1);
					hqlQuery +=") and ";
				}
				hqlQuery += "a.id_scheda_ente_gestore = b.id_scheda_ente_gestore   "
				+ "and b.data_fine_validita is null "
				+ "and g.id_stato_ente = h.id_stato_ente "
				+ "and ((h.data_fine_validita is null and h.data_inizio_validita <= current_date) or (h.data_fine_validita > current_date and h.data_inizio_validita <= current_date)) "
				+ "and case "
				+ "		when g.cod_stato_ente = 'APE' then h.data_inizio_validita <= current_date "
				+ "		else h.data_inizio_validita > current_date "
				+ "end "
				+ "and a.id_scheda_ente_gestore = h.id_scheda_ente_gestore   "
				+ "and i.id_tipo_ente = b.id_tipo_ente ";
				if (Checker.isValorizzato(ricerca.getStatoRendicontazione()) ||ricerca.getAnnoEsercizio() != null) {
					hqlQuery += "and e.data_cancellazione is null ";
					hqlQuery += "and f.data_cancellazione is null ";
					hqlQuery +=  "and e.id_scheda_ente_gestore = a.id_scheda_ente_gestore ";
					hqlQuery +=  "and f.id_stato_rendicontazione = e.id_stato_rendicontazione ";
					if (ricerca.getStatoRendicontazione() != null) {
						hqlQuery +=  " and f.cod_stato_rendicontazione = :codStatoRend ";
					}
					else {
						hqlQuery +=  " and f.cod_stato_rendicontazione <>'CON' ";
					}
					if (ricerca.getAnnoEsercizio() != null) {
						hqlQuery +=  " and e.anno_gestione = :annoRend ";
					}
				}	
				if (ricerca.getComune() != null) {
					hqlQuery +=  "and j1.id_scheda_ente_gestore = a.id_scheda_ente_gestore "
							+ "and j1.id_comune = j2.id_comune "; 
					if(ricerca.getAnnoEsercizio()==null) {
						hqlQuery += "and j1.data_fine_validita is null ";
					} else {
						hqlQuery += "and (" + ricerca.getAnnoEsercizio() + " - date_part('year', j1.data_inizio_validita)>=0 and (" + ricerca.getAnnoEsercizio() + " - date_part('year', j1.data_fine_validita)<=0 or j1.data_fine_validita is null)) ";
					}
					hqlQuery += "and j2.data_cancellazione is null "
							+ "and j1.data_cancellazione is null ";
				}
				hqlQuery += "and a.data_cancellazione is null  "
				+ "and g.data_cancellazione is null  "
				+ "and h.data_cancellazione is null "
				+ "and i.data_Cancellazione is null "
				+ "and j.data_Cancellazione is null "
				+ "and ";
				if (ricerca.getTipoEnte() != null) {
					hqlQuery +=  "i.cod_Tipo_Ente = :codTipoEnte AND ";
				}
				if (Checker.isValorizzato(ricerca.getDenominazioneEnte())) {
					hqlQuery +=  " UPPER(b.denominazione) LIKE UPPER(:denominazione) AND ";
				}
				if (ricerca.getStatoEnte() != null) {
					hqlQuery +=  " g.cod_stato_ente = :codStatoEnte AND ";
				}
				if (ricerca.getComune() != null) {
					hqlQuery +=  " j2.cod_istat_comune = :codComune AND ";
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
	
	public List<GregTCronologia> findCronologiaEnte(Integer idRendicontazione) {
		TypedQuery<GregTCronologia> query = em.createNamedQuery("GregTCronologia.findByIdNotDeleted", GregTCronologia.class);
		query.setParameter("idRendicontazione", idRendicontazione);
		return query.getResultList();
	}
		
	
	@SuppressWarnings("unchecked")
	public ArrayList<Object> findUtenteProfilo(String codFiscale) {
		
		String hqlQuery = "select distinct d.idSchedaEnteGestore,a.codProfilo "
				//+ ", b.codiceFiscale, b.nome ,b.cognome ,b.email "
				//+ ", d.idSchedaEnteGestore,e.codAzione " 
				+ "from GregDProfilo a, GregTUser b,GregDAzione e,GregRProfiloAzione f,GregRUserProfilo c "
				+ "left join GregTSchedeEntiGestori d on d.idSchedaEnteGestore =  c.gregTSchedeEntiGestori.idSchedaEnteGestore " 
				+ "where a.idProfilo = c.gregDProfilo.idProfilo " 
				+ "and b.idUser = c.gregTUser.idUser "
				+ "and b.codiceFiscale = : codFiscale "
				+ "and e.idAzione = f.gregDAzione.idAzione "
				+ "and a.idProfilo = f.gregDProfilo.idProfilo " 
				+ "and a.dataCancellazione is null " 
				+ "and b.dataCancellazione is null "
				+ "and c.dataCancellazione is null "
				+ "and d.dataCancellazione is null "
				+ "and e.dataCancellazione is null "
				+ "and f.dataCancellazione is null";
	
		
		Query query =null;
		query = em.createQuery(hqlQuery,Object[].class);
		query.setParameter("codFiscale", codFiscale);
		
		ArrayList<Object> entiresult = (ArrayList<Object>) query.getResultList();
		return entiresult;
	
	}	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Object> getElencoEntiAzioni(String codFiscale) {

		String hqlQuery = "select distinct d.idSchedaEnteGestore,e.codAzione  " 
				+ "from GregDProfilo a, GregTUser b,GregDAzione e,GregRProfiloAzione f,GregRUserProfilo c "
				+ "left join GregTSchedeEntiGestori d on d.idSchedaEnteGestore =  c.gregTSchedeEntiGestori.idSchedaEnteGestore " 
				+ "where a.idProfilo = c.gregDProfilo.idProfilo " 
				+ "and b.idUser = c.gregTUser.idUser "
				+ "and b.codiceFiscale = : codFiscale "
				+ "and e.idAzione = f.gregDAzione.idAzione "
				+ "and a.idProfilo = f.gregDProfilo.idProfilo " 
				+ "and a.dataCancellazione is null " 
				+ "and b.dataCancellazione is null "
				+ "and c.dataCancellazione is null "
				+ "and d.dataCancellazione is null "
				+ "and e.dataCancellazione is null "
				+ "and f.dataCancellazione is null";


		Query query =null;
		query = em.createQuery(hqlQuery,Object[].class);
		query.setParameter("codFiscale", codFiscale);

		ArrayList<Object> entiresult = (ArrayList<Object>) query.getResultList();
		return entiresult;

	}	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Object> getElencoProfiliEnte(String codFiscale,int idente) {

		String hqlQuery = "select distinct a.codProfilo " 
				+ "from GregDProfilo a, GregTUser b,GregDAzione e,GregRProfiloAzione f,GregRUserProfilo c "
				+ "left join GregTSchedeEntiGestori d on d.idSchedaEnteGestore =  c.gregTSchedeEntiGestori.idSchedaEnteGestore " 
				+ "where a.idProfilo = c.gregDProfilo.idProfilo " 
				+ "and b.idUser = c.gregTUser.idUser "
				+ "and b.codiceFiscale = : codFiscale "
				+ "and d.idSchedaEnteGestore = : idente or d.idSchedaEnteGestore is null "
				+ "and e.idAzione = f.gregDAzione.idAzione "
				+ "and a.idProfilo = f.gregDProfilo.idProfilo " 
				+ "and a.dataCancellazione is null " 
				+ "and b.dataCancellazione is null "
				+ "and c.dataCancellazione is null "
				+ "and d.dataCancellazione is null "
				+ "and e.dataCancellazione is null "
				+ "and f.dataCancellazione is null";


		Query query =null;
		query = em.createQuery(hqlQuery,Object[].class);
		query.setParameter("codFiscale", codFiscale);
		query.setParameter("idente", idente);

		ArrayList<Object> entiresult = (ArrayList<Object>) query.getResultList();
		return entiresult;

	}	

//	@SuppressWarnings("unchecked")
//	public ArrayList<String> getElencoAzioniEnte(String codFiscale,int idente,String ruolo) {
//		String hqlQuery = null;
//		if (ruolo.equalsIgnoreCase(SharedConstants.OP_REGIONALE)) {
//			hqlQuery = "select distinct e.codAzione " 
//					+ "from GregDProfilo a, GregTUser b,GregDAzione e,GregRProfiloAzione f,GregRUserProfilo c " 
//					+ "where a.idProfilo = c.gregDProfilo.idProfilo " 
//					+ "and b.idUser = c.gregTUser.idUser "
//					+ "and b.codiceFiscale = : codFiscale "
//					+ "and e.idAzione = f.gregDAzione.idAzione "
//					+ "and a.idProfilo = f.gregDProfilo.idProfilo " 
//					+ "and a.dataCancellazione is null " 
//					+ "and b.dataCancellazione is null "
//					+ "and c.dataCancellazione is null "
//					+ "and e.dataCancellazione is null "
//					+ "and f.dataCancellazione is null";
//		}
//		else {
//			hqlQuery = "select distinct e.codAzione " 
//					+ "from GregDProfilo a, GregTUser b,GregDAzione e,GregRProfiloAzione f,GregRUserProfilo c,GregTSchedeEntiGestori d "
//					+ "where a.idProfilo = c.gregDProfilo.idProfilo " 
//					+ "and b.idUser = c.gregTUser.idUser "
//					+ "and b.codiceFiscale = : codFiscale "
//					+ "and e.idAzione = f.gregDAzione.idAzione "
//					+ "and a.idProfilo = f.gregDProfilo.idProfilo " 
//					+ "and d.idSchedaEnteGestore = : idente "
//					+ "and d.idSchedaEnteGestore =  c.gregTSchedeEntiGestori.idSchedaEnteGestore "
//					+ "and a.dataCancellazione is null " 
//					+ "and b.dataCancellazione is null "
//					+ "and c.dataCancellazione is null "
//					+ "and d.dataCancellazione is null "
//					+ "and e.dataCancellazione is null "
//					+ "and f.dataCancellazione is null";
//		}
//
//		Query query =null;
//		query = em.createQuery(hqlQuery);
//		query.setParameter("codFiscale", codFiscale);
//		if (!ruolo.equalsIgnoreCase(SharedConstants.OP_REGIONALE)) 
//			query.setParameter("idente", idente);
//
//		ArrayList<String> entiresult = (ArrayList<String>) query.getResultList();
//		return entiresult;
//
//	}
//	
	@SuppressWarnings("unchecked")
	public UserInfo findUtenteProfiloLista(String codFiscale) {
		
		String hqlQuery = "select distinct a.codProfilo ,a.descProfilo ,a.infoProfilo , "
				+ "a.tipoProfilo ,c.codiceFiscale ,c.nome ,c.cognome "
				+ "from GregDProfilo a, GregTUser c,GregRUserProfilo e " 
				+ "where a.idProfilo = e.gregDProfilo.idProfilo " 
				+ "and c.idUser = e.gregTUser.idUser "
				+ "and c.codiceFiscale = : codFiscale "
				+ "and a.dataCancellazione is null " 
				+ "and c.dataCancellazione is null "
				+ "and e.dataCancellazione is null "
				+ "and now() between e.dataInizioValidita and coalesce(e.dataFineValidita, now()) "
				+ "order by a.codProfilo";
		
		Query query =null;
		query = em.createQuery(hqlQuery,Object[].class);
		query.setParameter("codFiscale", codFiscale);
		
		ArrayList<Object> entiresult = (ArrayList<Object>) query.getResultList();
		UserInfo utente = new UserInfo();
		
		Iterator itr = entiresult.iterator();
		Object[] obj = (Object[]) itr.next();
		utente.setCodFisc(String.valueOf(obj[4]));
		utente.setNome(String.valueOf(obj[5]));
		utente.setCognome(String.valueOf(obj[6]));
		ModelProfilo profilo = new ModelProfilo();
		ArrayList<ModelProfilo> listaprofili = new ArrayList<ModelProfilo>();
		profilo = new ModelProfilo();
		profilo.setCodProfilo(String.valueOf(obj[0]));
		profilo.setDescProfilo(String.valueOf(obj[1]));
		profilo.setInfoProfilo(String.valueOf(obj[2]));
		profilo.setTipoProfilo(String.valueOf(obj[3]));
		listaprofili.add(profilo);
		while(itr.hasNext()){
			obj = (Object[]) itr.next();
			profilo = new ModelProfilo();
			profilo.setCodProfilo(String.valueOf(obj[0]));
			profilo.setDescProfilo(String.valueOf(obj[1]));
			profilo.setInfoProfilo(String.valueOf(obj[2]));
			profilo.setTipoProfilo(String.valueOf(obj[3]));
			listaprofili.add(profilo);
			}
          
		for (ModelProfilo profilosin : listaprofili) {
		hqlQuery = "select distinct d.codLista ,d.descLista " 
				+ "from GregDProfilo a, GregTUser c,GregTLista d,GregRUserProfilo e " 
				+ "where a.idProfilo = e.gregDProfilo.idProfilo " 
				+ "and c.idUser = e.gregTUser.idUser "
				+ "and c.codiceFiscale = : codFiscale "
				+ "and d.idLista = e.gregTLista.idLista "
				+ "and a.codProfilo = :codProfilo "
				+ "and a.dataCancellazione is null " 
				+ "and c.dataCancellazione is null "
				+ "and d.dataCancellazione is null "
				+ "and e.dataCancellazione is null "
				+ "and now() between e.dataInizioValidita and coalesce(e.dataFineValidita, now()) "
				+ "order by d.codLista";
		
		query = em.createQuery(hqlQuery,Object[].class);
		query.setParameter("codFiscale", codFiscale);
		query.setParameter("codProfilo", profilosin.getCodProfilo());
		ArrayList<Object> modellistaesito = (ArrayList<Object>) query.getResultList();
		ArrayList<ModelLista> modellista = new ArrayList<ModelLista>();
		itr = modellistaesito.iterator();
		while(itr.hasNext()){
			obj = (Object[]) itr.next();
			ModelLista listasel = new ModelLista();
			listasel.setCodLista(String.valueOf(obj[0]));
			listasel.setDescLista(String.valueOf(obj[1]));
			modellista.add(listasel);
		}	
		profilosin.setListaenti(modellista);
		
		for (ModelLista listasel : modellista) {
		query = em.createNativeQuery("" 
		+ "with tab1 as (select distinct b.id_Scheda_Ente_Gestore " 
		+ "from Greg_T_Lista a,Greg_R_Lista_Enti_Gestori b "
		+ "where a.id_Lista = b.id_Lista "
		+ "and a.cod_Lista = :codlista "
		+ "and a.data_Cancellazione is null " 
		+ "and b.data_Cancellazione is null) "
		+ "select id_ente_da_unire from greg_r_merge_enti a,tab1 b "
		+ "where a.id_ente_destinazione = b.id_Scheda_Ente_Gestore "
		+ "and a.data_cancellazione is null and a.data_merge <= current_timestamp "
		+ "union "
		+ "select id_Scheda_Ente_Gestore from tab1");
		
		query.setParameter("codlista", listasel.getCodLista());
		ArrayList<Integer> elencoenti = (ArrayList<Integer>) query.getResultList();
		listasel.setIdente(elencoenti);
		}
		hqlQuery = "select distinct b.codAzione ,"
				+ "case when b.visualizza=true and b.modifica=false then true " 
				+ "when b.visualizza=false and b.modifica=true then false "
				+ "when b.visualizza is null and b.modifica is null and b.visibile=true then false "
				+ "end as disabilitato, "
				+ "b.visibile "
				+ "from GregDProfilo a,GregDAzione b,GregRProfiloAzione c "
				+ "where a.idProfilo = c.gregDProfilo.idProfilo "
				+ "and b.idAzione = c.gregDAzione.idAzione "
				+ "and a.codProfilo  = :codProfilo "
				+ "and a.dataCancellazione is null "
				+ "and b.dataCancellazione is null "
				+ "and c.dataCancellazione is null "
				+ "order by b.codAzione";
		query = em.createQuery(hqlQuery,Object[].class);
		query.setParameter("codProfilo", profilosin.getCodProfilo());
		ArrayList<Object> modelobject = (ArrayList<Object>) query.getResultList();
		Map<String,boolean[]> azioni = new HashMap<String,boolean[]>();
		itr = modelobject.iterator();
		while(itr.hasNext()){
			obj = (Object[]) itr.next();
			boolean[] valori = new boolean[2];
			valori[0] = (Boolean)(obj[1]);
			valori[1] = (Boolean)(obj[2]);
			azioni.put(String.valueOf(obj[0]), valori);
		}	
		ArrayList<String> elencoAzioni = elencoAzioni();
		for (String azione : elencoAzioni) {
				if (!azioni.containsKey(azione)) {
					boolean[] valori = new boolean[2];
					valori[0] = true;
					valori[1] = false;
					azioni.put(azione, valori);
				}
		}
		profilosin.setListaazioni(azioni);
		}
		utente.setListaprofili(listaprofili);	
		return utente;
	}	
	



	public ArrayList<String> elencoAzioni() {
		
		String hqlQuery = "SELECT distinct a.codAzione FROM GregDAzione a "
				+ "where a.dataCancellazione is null order by a.codAzione";
		Query query =null;
		query = em.createQuery(hqlQuery);
		
		ArrayList<String> azioni = (ArrayList<String>) query.getResultList();
		return azioni;
	}

	public List<GregDStatoEnte> findAllStatoEnte() {
		TypedQuery<GregDStatoEnte> query = em.createNamedQuery("GregDStatoEnte.findAllNotDeleted", GregDStatoEnte.class);
		return query.getResultList();
	}
	
	public List<Integer> findAllAnnoEsercizio() {
		
		Query query = em.createNativeQuery("" 
				+ "select distinct g.anno_gestione "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_d_stato_rendicontazione stato on stato.id_stato_rendicontazione = g.id_stato_rendicontazione "
				+ "where stato.cod_stato_rendicontazione <> 'CON' "
				+ "order by g.anno_gestione"
				);
		
		ArrayList<Integer> result = (ArrayList<Integer>) query.getResultList();
		
		return result;
	}

	public List<ModelRendicontazioneEnte> findAllRendicontazioniApertebyIdScheda(Integer idSchedaEnteGestore) {
		
		List<ModelRendicontazioneEnte> lista = new ArrayList<ModelRendicontazioneEnte>();
		
		Query query = em.createNativeQuery("" 
				+ "select distinct g.id_rendicontazione_ente, g.anno_gestione, stato.cod_stato_rendicontazione, stato.des_stato_rendicontazione, g.id_Scheda_ente_gestore  "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_d_stato_rendicontazione stato on stato.id_stato_rendicontazione = g.id_stato_rendicontazione  "
				+ "where stato.cod_stato_rendicontazione <> 'CON' "
				+ "and g.id_scheda_ente_gestore = :schedaEnte "
				+ "and g.data_Cancellazione is null "
				+ "and stato.data_Cancellazione is null " 
				+ "order by g.anno_Gestione desc"
				);
		query.setParameter("schedaEnte", idSchedaEnteGestore);
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		
		Iterator<Object> itr = result.iterator();
		while(itr.hasNext()){
			Object[] o = (Object[]) itr.next();
			ModelRendicontazioneEnte rendicontazione = new ModelRendicontazioneEnte(o); 
			lista.add(rendicontazione);
		}
		
		return lista;
	}	
	
public List<ModelRendicontazioneEnte> findRendicontazioniApertebyIdSchedaAndValue(Integer idSchedaEnteGestore, RicercaEntiGestori ricerca) {
		
		List<ModelRendicontazioneEnte> lista = new ArrayList<ModelRendicontazioneEnte>();
		
		String sqlQuery ="" 
				+ "select distinct g.id_rendicontazione_ente, g.anno_gestione, stato.cod_stato_rendicontazione, stato.des_stato_rendicontazione,g.id_Scheda_ente_gestore "
				+ "from greg_t_rendicontazione_ente g "
				+ "left join greg_d_stato_rendicontazione stato on stato.id_stato_rendicontazione = g.id_stato_rendicontazione  "
				+ "where stato.cod_stato_rendicontazione <> 'CON' "
				+ "and g.id_scheda_ente_gestore = :schedaEnte "
				+ "and g.data_Cancellazione is null "
				+ "and stato.data_Cancellazione is null "
				+ "AND ";
		if(ricerca.getStatoRendicontazione()!=null) {
			sqlQuery += "stato.cod_stato_rendicontazione = :codStatoRendicontazione AND ";
		}
		
		if(ricerca.getAnnoEsercizio()!=null) {
			sqlQuery += "g.anno_Gestione = :annoGestione AND ";
		}
		
		sqlQuery +="1=1 "
				+ "order by g.anno_Gestione DESC";
		
		Query query = em.createNativeQuery(sqlQuery);
		
		query.setParameter("schedaEnte", idSchedaEnteGestore);
		if(ricerca.getStatoRendicontazione()!=null) {
			query.setParameter("codStatoRendicontazione", ricerca.getStatoRendicontazione());
		}
		if(ricerca.getAnnoEsercizio()!=null) {
			query.setParameter("annoGestione", ricerca.getAnnoEsercizio());
		}
		ArrayList<Object> result = (ArrayList<Object>) query.getResultList();
		
		Iterator<Object> itr = result.iterator();
		while(itr.hasNext()){
			Object[] o = (Object[]) itr.next();
			ModelRendicontazioneEnte rendicontazione = new ModelRendicontazioneEnte(o); 
			lista.add(rendicontazione);
		}
		
		return lista;
	}	
	
	public List<ModelStoricoEnte> findStoricoEnte(Integer idScheda) {
		String sqlQuery ="select e.data_inizio_validita, e.data_fine_validita, u.nome || ' ' || u.cognome  "
				+ "from greg_t_schede_enti_gestori s, "
				+ "greg_r_ente_gestore_contatti e, "
				+ "greg_t_user u  "
				+ "where s.id_scheda_ente_gestore = :idScheda  "
				+ "and  "
				+ "e.id_scheda_ente_gestore = s.id_scheda_ente_gestore  "
				+ "and  "
				+ "u.codice_fiscale = e.utente_operazione  "
				+ "and "
				+ "e.data_fine_validita is not null  "
				+ "and s.data_Cancellazione is null "
				+ "union  "
				+ "select r.data_inizio_validita, r.data_fine_validita, u.nome || ' ' || u.cognome  "
				+ "from greg_t_schede_enti_gestori s, "
				+ "greg_r_ente_gestore_contatti e, "
				+ "greg_r_responsabile_contatti r, "
				+ "greg_t_user u  "
				+ "where s.id_scheda_ente_gestore = :idScheda  "
				+ "and  "
				+ "e.id_scheda_ente_gestore = s.id_scheda_ente_gestore  "
				+ "and  "
				+ "r.id_responsabile_contatti  = e.id_responsabile_contatti   "
				+ "and  "
				+ "u.codice_fiscale = r.utente_operazione  "
				+ "and "
				+ "r.data_fine_validita is not null  "
				+ "and s.data_Cancellazione is null "
				+ "union "
				+ "select c.data_inizio_validita, c.data_fine_validita, u.nome || ' ' || u.cognome  "
				+ "from greg_t_schede_enti_gestori s, "
				+ "greg_r_schede_enti_gestori_comuni c, "
				+ "greg_t_user u  "
				+ "where s.id_scheda_ente_gestore = :idScheda  "
				+ "and  "
				+ "c.id_scheda_ente_gestore = s.id_scheda_ente_gestore  "
				+ "and "
				+ "u.codice_fiscale = c.utente_operazione  "
				+ "and "
				+ "c.data_fine_validita is not null  "
				+ "and s.data_Cancellazione is null "
				+ "and c.data_Cancellazione is null "
				+ "order by data_fine_validita,data_inizio_validita";
		Query query = em.createNativeQuery(sqlQuery);
		query.setParameter("idScheda", idScheda);
		List<Object[]> result = (ArrayList<Object[]>) query.getResultList();
		List<ModelStoricoEnte> lista = new ArrayList<ModelStoricoEnte>();
		
		for(Object[] o : result) {
			lista.add(new ModelStoricoEnte(o));
		}
		
		return lista;
	}

	public List<ResultCreaAnno> creaNuovoAnno(String testo) {
		
		Query query = em.createNativeQuery("with a as "
				+ "( "
				+ "select fnc_crea_anno_contabile(:testo) as x "
				+ "),  "
				+ "b as (select x as w from a) "
				+ "select "
				+ "(w).* "
				+ "from b"
				);
		query.setParameter("testo", testo);
		ArrayList<Object[]> result = (ArrayList<Object[]>) query.getResultList();
		List<ResultCreaAnno> risultato = new ArrayList<ResultCreaAnno>();
		
		for(Object[] o : result) {
			ResultCreaAnno r = new ResultCreaAnno(o);
			risultato.add(r);
		}
		return risultato;
	}
	
	public GregTRendicontazioneEnte saveRendicontazione(GregTRendicontazioneEnte rend) {
		return em.merge(rend);
	}
	
}
