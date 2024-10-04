/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.sql.Timestamp;
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

import it.csi.greg.gregsrv.business.entity.GregDAzione;
import it.csi.greg.gregsrv.business.entity.GregDProfilo;
import it.csi.greg.gregsrv.business.entity.GregDProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregDSpesaMissioneProgramma;
import it.csi.greg.gregsrv.business.entity.GregRListaEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1PrestMinist;
import it.csi.greg.gregsrv.business.entity.GregRProfiloAzione;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneSpesaMissioneProgrammaMacro;
import it.csi.greg.gregsrv.business.entity.GregRSpesaMissioneProgrammaMacro;
import it.csi.greg.gregsrv.business.entity.GregRUserProfilo;
import it.csi.greg.gregsrv.business.entity.GregTLista;
import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregTUser;
import it.csi.greg.gregsrv.dto.ModelListaAzione;
import it.csi.greg.gregsrv.dto.ModelListaEnti;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.RicercaProfili;
import it.csi.greg.gregsrv.dto.RicercaUtenti;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;

@Repository("gestioneUtentiDao")
@Transactional(readOnly = true)
public class GestioneUtentiDao {

	@PersistenceContext
	private EntityManager em;

	public List<GregTUser> findAllUtenti() {

		String hqlQuery = "SELECT u FROM GregTUser u " + "WHERE u.dataCancellazione is null "
				+ "ORDER BY u.cognome, u.nome, u.codiceFiscale";

		TypedQuery<GregTUser> query = em.createQuery(hqlQuery, GregTUser.class);
		return query.getResultList();
	}

	public List<GregTUser> findAllUtentiFilter(RicercaUtenti ricerca) {

		String hqlQuery = "SELECT DISTINCT u FROM GregTUser u "
				+ "LEFT JOIN GregRUserProfilo up on up.gregTUser.idUser=u.idUser and up.dataCancellazione is null "
				+ "LEFT JOIN GregDProfilo p on p.idProfilo=up.gregDProfilo.idProfilo and p.dataCancellazione is null "
				+ "LEFT JOIN GregTLista l on l.idLista=up.gregTLista.idLista and l.dataCancellazione is null "
				+ "LEFT JOIN GregRListaEntiGestori le on le.gregTLista.idLista=l.idLista and le.dataCancellazione is null "
				+ "LEFT JOIN GregTSchedeEntiGestori e on e.idSchedaEnteGestore=le.gregTSchedeEntiGestori.idSchedaEnteGestore and e.dataCancellazione is null "
				+ "WHERE u.dataCancellazione is null AND ";
		if (Checker.isValorizzato(ricerca.getNome())) {
			hqlQuery += " UPPER(u.nome) LIKE UPPER(:nome) AND ";
		}
		if (Checker.isValorizzato(ricerca.getCognome())) {
			hqlQuery += " UPPER(u.cognome) LIKE UPPER(:cognome) AND ";
		}
		if (Checker.isValorizzato(ricerca.getCodiceFiscale())) {
			hqlQuery += " UPPER(u.codiceFiscale) LIKE UPPER(:codiceFiscale) AND ";
		}
		if (Checker.isValorizzato(ricerca.getEmail())) {
			hqlQuery += " UPPER(u.email) LIKE UPPER(:email) AND ";
		}
		if (ricerca.getProfilo() != null) {
			hqlQuery += " p.idProfilo = :idProfilo AND ";
		}
		if (ricerca.getLista() != null) {
			hqlQuery += " l.idLista = :idLista AND ";
		}
		if (ricerca.getEnte() != null) {
			hqlQuery += " e.idSchedaEnteGestore = :idEnte AND ";
		}
		if(ricerca.isAttivo()) {
			hqlQuery += " now() between up.dataInizioValidita AND coalesce(up.dataFineValidita, now()) AND ";
		}
		hqlQuery += "1=1 " + "ORDER BY u.cognome, u.nome, u.codiceFiscale";
		
		TypedQuery<GregTUser> query = em.createQuery(hqlQuery, GregTUser.class);
		if (Checker.isValorizzato(ricerca.getNome())) {
			query.setParameter("nome", "%" + ricerca.getNome() + "%");
		}
		if (Checker.isValorizzato(ricerca.getCognome())) {
			query.setParameter("cognome", "%" + ricerca.getCognome() + "%");
		}
		if (Checker.isValorizzato(ricerca.getCodiceFiscale())) {
			query.setParameter("codiceFiscale", "%" + ricerca.getCodiceFiscale() + "%");
		}
		if (Checker.isValorizzato(ricerca.getEmail())) {
			query.setParameter("email", "%" + ricerca.getEmail() + "%");
		}
		if (ricerca.getProfilo() != null) {
			query.setParameter("idProfilo", ricerca.getProfilo());
		}
		if (ricerca.getLista() != null) {
			query.setParameter("idLista", ricerca.getLista());
		}
		if (ricerca.getEnte() != null) {
			query.setParameter("idEnte", ricerca.getEnte());
		}

		return query.getResultList();
	}

	public List<GregRUserProfilo> findAllUtentiAbilitazione(Integer idUser) {

		String hqlQuery = "SELECT u FROM GregRUserProfilo u " + "WHERE u.dataCancellazione is null "
				+ "AND u.gregDProfilo.dataCancellazione is null "
				+ "AND u.gregTLista.dataCancellazione is null "
				+ "AND u.gregTUser.idUser = :idUser " + "ORDER BY u.gregDProfilo.idProfilo, u.gregTLista.idLista";

		TypedQuery<GregRUserProfilo> query = em.createQuery(hqlQuery, GregRUserProfilo.class);
		query.setParameter("idUser", idUser);
		return query.getResultList();
	}
	
	public List<GregRUserProfilo> findAllUtentiAbilitazionebyProfilo(Integer idProfilo) {

		String hqlQuery = "SELECT u FROM GregRUserProfilo u " + "WHERE u.dataCancellazione is null "
				+ "AND u.gregDProfilo.dataCancellazione is null "
				+ "AND u.gregTLista.dataCancellazione is null "
				+ "AND u.gregDProfilo.idProfilo = :idProfilo " + "ORDER BY u.gregDProfilo.idProfilo, u.gregTLista.idLista";

		TypedQuery<GregRUserProfilo> query = em.createQuery(hqlQuery, GregRUserProfilo.class);
		query.setParameter("idProfilo", idProfilo);
		return query.getResultList();
	}
	
	public List<GregRUserProfilo> findAllUtentiAbilitazionebyLista(Integer idLista) {

		String hqlQuery = "SELECT u FROM GregRUserProfilo u " + "WHERE u.dataCancellazione is null "
				+ "AND u.gregDProfilo.dataCancellazione is null "
				+ "AND u.gregTLista.dataCancellazione is null "
				+ "AND u.gregTLista.idLista = :idLista " + "ORDER BY u.gregDProfilo.idProfilo, u.gregTLista.idLista";

		TypedQuery<GregRUserProfilo> query = em.createQuery(hqlQuery, GregRUserProfilo.class);
		query.setParameter("idLista", idLista);
		return query.getResultList();
	}

	public List<GregRUserProfilo> findAllUtentiAbilitazioneFilter(Integer idUser, RicercaUtenti ricerca) {

		String hqlQuery = "SELECT DISTINCT up FROM GregRUserProfilo up "
				+ "LEFT JOIN GregDProfilo p on p.idProfilo=up.gregDProfilo.idProfilo and p.dataCancellazione is null "
				+ "LEFT JOIN GregTLista l on l.idLista=up.gregTLista.idLista and l.dataCancellazione is null "
				+ "LEFT JOIN GregRListaEntiGestori le on le.gregTLista.idLista=l.idLista and le.dataCancellazione is null "
				+ "LEFT JOIN GregTSchedeEntiGestori e on e.idSchedaEnteGestore=le.gregTSchedeEntiGestori.idSchedaEnteGestore and e.dataCancellazione is null "
				+ "WHERE up.dataCancellazione is null  " + "AND up.gregTUser.idUser = :idUser AND ";
		if (ricerca.getProfilo() != null) {
			hqlQuery += " p.idProfilo = :idProfilo AND ";
		}
		if (ricerca.getLista() != null) {
			hqlQuery += " l.idLista = :idLista AND ";
		}
		if (ricerca.getEnte() != null) {
			hqlQuery += " e.idSchedaEnteGestore = :idEnte AND ";
		}
		if(ricerca.isAttivo()) {
			hqlQuery += " now() between up.dataInizioValidita AND coalesce(up.dataFineValidita, now()) AND ";
		}
		hqlQuery += "1=1 " + "ORDER BY up.gregDProfilo.idProfilo, up.gregTLista.idLista";

		TypedQuery<GregRUserProfilo> query = em.createQuery(hqlQuery, GregRUserProfilo.class);
		query.setParameter("idUser", idUser);
		if (ricerca.getProfilo() != null) {
			query.setParameter("idProfilo", ricerca.getProfilo());
		}
		if (ricerca.getLista() != null) {
			query.setParameter("idLista", ricerca.getLista());
		}
		if (ricerca.getEnte() != null) {
			query.setParameter("idEnte", ricerca.getEnte());
		}
		return query.getResultList();
	}

	public List<ModelListeConfiguratore> findProfili() {
		String hqlQuery = "select u " + "from GregDProfilo u " + "where u.dataCancellazione is null "
				+ "order by u.codProfilo ";

		TypedQuery<GregDProfilo> query = em.createQuery(hqlQuery, GregDProfilo.class);

		List<GregDProfilo> result = (ArrayList<GregDProfilo>) query.getResultList();

		List<ModelListeConfiguratore> macro = new ArrayList<ModelListeConfiguratore>();

		for (GregDProfilo m : result) {
			ModelListeConfiguratore ma = new ModelListeConfiguratore();
			ma.setId(m.getIdProfilo());
			ma.setCodice(m.getCodProfilo());
			ma.setDescrizione(m.getDescProfilo());
			macro.add(ma);
		}
		return macro;
	}

	public List<ModelListeConfiguratore> findListe() {
		String hqlQuery = "select u " + "from GregTLista u " + "where u.dataCancellazione is null "
				+ "order by u.codLista ";

		TypedQuery<GregTLista> query = em.createQuery(hqlQuery, GregTLista.class);

		List<GregTLista> result = (ArrayList<GregTLista>) query.getResultList();

		List<ModelListeConfiguratore> macro = new ArrayList<ModelListeConfiguratore>();

		for (GregTLista m : result) {
			ModelListeConfiguratore ma = new ModelListeConfiguratore();
			ma.setId(m.getIdLista());
			ma.setCodice(m.getCodLista());
			ma.setDescrizione(m.getDescLista());
			macro.add(ma);
		}
		return macro;
	}

	public List<ModelListeConfiguratore> findEnti() {
		String hqlQuery = "select gtseg.id_scheda_ente_gestore, gtseg.codice_regionale, gregc.denominazione  "
				+ "from greg_t_schede_enti_gestori gtseg, greg_r_ente_gestore_contatti gregc  "
				+ "where gtseg.id_scheda_ente_gestore = gregc.id_scheda_ente_gestore  "
				+ "and gtseg.data_cancellazione is null " + "and gregc.data_fine_validita is null  "
				+ "order by gtseg.codice_regionale";

		Query query = em.createNativeQuery(hqlQuery);
		ArrayList<Object[]> result = (ArrayList<Object[]>) query.getResultList();

		List<ModelListeConfiguratore> macro = new ArrayList<ModelListeConfiguratore>();

		for (Object[] m : result) {
			ModelListeConfiguratore ma = new ModelListeConfiguratore();
			ma.setId((Integer) m[0]);
			ma.setCodice((String) m[1]);
			ma.setDescrizione((String) m[2]);
			macro.add(ma);
		}
		return macro;
	}

	public GregRUserProfilo findUserProfilobyId(Integer idUserProfilo) {

		String hqlQuery = "SELECT u FROM GregRUserProfilo u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idUserProfilo = :idUserProfilo " + "ORDER BY u.gregDProfilo.idProfilo, u.gregTLista.idLista";

		TypedQuery<GregRUserProfilo> query = em.createQuery(hqlQuery, GregRUserProfilo.class);
		query.setParameter("idUserProfilo", idUserProfilo);
		return query.getSingleResult();
	}

	public GregRUserProfilo aggiornaRUserProfilo(GregRUserProfilo userProfilo) {
		return em.merge(userProfilo);
	}

	public GregTUser findUserbyId(Integer idUser) {

		String hqlQuery = "SELECT u FROM GregTUser u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idUser = :idUser ";

		TypedQuery<GregTUser> query = em.createQuery(hqlQuery, GregTUser.class);
		query.setParameter("idUser", idUser);
		return query.getSingleResult();
	}

	public GregTUser findUserbyCodiceFiscale(String codiceFiscale) {
		try {
			String hqlQuery = "SELECT u FROM GregTUser u " + "WHERE u.dataCancellazione is null "
					+ "AND u.codiceFiscale = :codiceFiscale ";

			TypedQuery<GregTUser> query = em.createQuery(hqlQuery, GregTUser.class);
			query.setParameter("codiceFiscale", codiceFiscale);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregTUser aggiornaTUser(GregTUser user) {
		return em.merge(user);
	}

	public GregRUserProfilo findUserProfilobyCod(Integer idUser, Integer idProfilo, Integer idLista, Timestamp dataInizioValidita) {
		try {
			String hqlQuery = "SELECT u FROM GregRUserProfilo u " + "WHERE u.dataCancellazione is null "
					+ "AND u.gregTUser.idUser = :idUser "
					+ "AND u.gregTUser.dataCancellazione is null " + "AND u.gregDProfilo.idProfilo = :idProfilo "
					+ "AND u.gregDProfilo.dataCancellazione is null "
					+ "AND u.gregTLista.idLista = :idLista "
					+ "AND u.gregTLista.dataCancellazione is null "
					+ "AND (u.dataFineValidita >= :dataInizioValidita OR u.dataFineValidita is null) ";
			
			TypedQuery<GregRUserProfilo> query = em.createQuery(hqlQuery, GregRUserProfilo.class);
			query.setParameter("idUser", idUser);
			query.setParameter("idProfilo", idProfilo);
			query.setParameter("idLista", idLista);
			query.setParameter("dataInizioValidita", dataInizioValidita);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public GregTUser aggiornaRUserProfilo(GregTUser user) {
		return em.merge(user);
	}

	public GregDProfilo findProfilobyId(Integer idProfilo) {

		String hqlQuery = "SELECT u FROM GregDProfilo u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idProfilo = :idProfilo ";

		TypedQuery<GregDProfilo> query = em.createQuery(hqlQuery, GregDProfilo.class);
		query.setParameter("idProfilo", idProfilo);
		return query.getSingleResult();
	}

	public GregTLista findListabyId(Integer idLista) {

		String hqlQuery = "SELECT u FROM GregTLista u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idLista = :idLista ";

		TypedQuery<GregTLista> query = em.createQuery(hqlQuery, GregTLista.class);
		query.setParameter("idLista", idLista);
		return query.getSingleResult();
	}

	public List<GregDProfilo> findAllProfili() {

		String hqlQuery = "SELECT u FROM GregDProfilo u " + "WHERE u.dataCancellazione is null "
				+ "ORDER BY u.codProfilo, u.descProfilo";

		TypedQuery<GregDProfilo> query = em.createQuery(hqlQuery, GregDProfilo.class);
		return query.getResultList();
	}

	public List<GregRProfiloAzione> findAllAzioneProfilo(Integer idProfilo) {

		String hqlQuery = "SELECT u FROM GregRProfiloAzione u " + "WHERE u.dataCancellazione is null "
				+ "AND u.gregDProfilo.idProfilo = :idProfilo "
				+ "AND u.gregDAzione.dataCancellazione is null " + "ORDER BY u.gregDAzione.descAzione";

		TypedQuery<GregRProfiloAzione> query = em.createQuery(hqlQuery, GregRProfiloAzione.class);
		query.setParameter("idProfilo", idProfilo);
		return query.getResultList();
	}

	public List<ModelListaAzione> findAzioni() {
		String hqlQuery = "select u " + "from GregDAzione u " + "where u.dataCancellazione is null "
				+ "order by u.descAzione ";

		TypedQuery<GregDAzione> query = em.createQuery(hqlQuery, GregDAzione.class);

		List<GregDAzione> result = (ArrayList<GregDAzione>) query.getResultList();

		List<ModelListaAzione> macro = new ArrayList<ModelListaAzione>();

		for (GregDAzione m : result) {
			ModelListaAzione ma = new ModelListaAzione();
			ma.setIdAzione(m.getIdAzione());
			ma.setCodAzione(m.getCodAzione());
			ma.setDescAzione(m.getDescAzione());
			macro.add(ma);
		}
		return macro;
	}

	public List<GregDProfilo> findAllProfiliFilter(RicercaProfili ricerca) {

		String hqlQuery = "SELECT DISTINCT u FROM GregDProfilo u, GregRProfiloAzione pa "
				+ "WHERE u.dataCancellazione is null " + "AND pa.dataCancellazione is null "
				+ "AND pa.gregDProfilo.idProfilo = u.idProfilo AND ";

		if (ricerca.getProfilo() != null) {
			hqlQuery += " u.idProfilo = :idProfilo AND ";
		}
		if (ricerca.getAzione() != null) {
			hqlQuery += " pa.gregDAzione.idAzione = :idAzione AND ";
		}
		hqlQuery += "1=1 " + "ORDER BY u.codProfilo, u.descProfilo";

		TypedQuery<GregDProfilo> query = em.createQuery(hqlQuery, GregDProfilo.class);

		if (ricerca.getProfilo() != null) {
			query.setParameter("idProfilo", ricerca.getProfilo());
		}
		if (ricerca.getAzione() != null) {
			query.setParameter("idAzione", ricerca.getAzione());
		}

		return query.getResultList();
	}

	public GregRProfiloAzione findProfiloAzionebyId(Integer idProfiloAzione) {

		String hqlQuery = "SELECT u FROM GregRProfiloAzione u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idProfiloAzione = :idProfiloAzione "
				+ "AND u.gregDProfilo.dataCancellazione is null "
				+ "AND u.gregDAzione.dataCancellazione is null";

		TypedQuery<GregRProfiloAzione> query = em.createQuery(hqlQuery, GregRProfiloAzione.class);
		query.setParameter("idProfiloAzione", idProfiloAzione);
		return query.getSingleResult();
	}

	public GregRProfiloAzione aggiornaRProfiloAzione(GregRProfiloAzione profiloAzione) {
		return em.merge(profiloAzione);
	}

	public GregDProfilo aggiornaDProfilo(GregDProfilo profilo) {
		return em.merge(profilo);
	}

	public GregDAzione findAzionebyId(Integer idAzione) {

		String hqlQuery = "SELECT u FROM GregDAzione u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idAzione = :idAzione ";

		TypedQuery<GregDAzione> query = em.createQuery(hqlQuery, GregDAzione.class);
		query.setParameter("idAzione", idAzione);
		return query.getSingleResult();
	}

	public GregDProfilo findProfilobyCod(String codProfilo) {
		try {
			String hqlQuery = "SELECT u FROM GregDProfilo u " + "WHERE u.dataCancellazione is null "
					+ "AND u.codProfilo = :codProfilo ";

			TypedQuery<GregDProfilo> query = em.createQuery(hqlQuery, GregDProfilo.class);
			query.setParameter("codProfilo", codProfilo);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<GregTLista> findAllListe() {

		String hqlQuery = "SELECT u FROM GregTLista u " + "WHERE u.dataCancellazione is null "
				+ "ORDER BY u.codLista, u.descLista";

		TypedQuery<GregTLista> query = em.createQuery(hqlQuery, GregTLista.class);
		return query.getResultList();
	}

	public List<GregTLista> findAllListeFilter(RicercaProfili ricerca) {

		String hqlQuery = "SELECT DISTINCT u FROM GregTLista u "
				+ "Left join GregRListaEntiGestori pa on pa.gregTLista.idLista = u.idLista "
				+ "Left join GregTSchedeEntiGestori e on pa.gregTSchedeEntiGestori.idSchedaEnteGestore = e.idSchedaEnteGestore "
				+ "WHERE u.dataCancellazione is null "
						+ "AND e.dataCancellazione is null "
				 + " AND ";
		if (ricerca.getLista() != null) {
			hqlQuery += " u.idLista = :idLista AND ";
		}
		if (ricerca.getEnte() != null) {
			hqlQuery += " e.idSchedaEnteGestore = :idEnte AND ";
		}
		hqlQuery += "1=1 " + "ORDER BY u.codLista, u.descLista";

		TypedQuery<GregTLista> query = em.createQuery(hqlQuery, GregTLista.class);

		if (ricerca.getLista() != null) {
			query.setParameter("idLista", ricerca.getLista());
		}
		if (ricerca.getEnte() != null) {
			query.setParameter("idEnte", ricerca.getEnte());
		}

		return query.getResultList();
	}

	public List<ModelListaEnti> findEntiListe() {

		String hqlQuery = "select u " + "from GregTSchedeEntiGestori u " + "where u.dataCancellazione is null "
				+ "order by u.codiceRegionale";

		TypedQuery<GregTSchedeEntiGestori> query = em.createQuery(hqlQuery, GregTSchedeEntiGestori.class);

		List<GregTSchedeEntiGestori> result = (ArrayList<GregTSchedeEntiGestori>) query.getResultList();

		List<ModelListaEnti> macro = new ArrayList<ModelListaEnti>();

		for (GregTSchedeEntiGestori m : result) {
			ModelListaEnti ma = new ModelListaEnti();
			ma.setIdEnte(m.getIdSchedaEnteGestore());
			ma.setCodEnte(m.getCodiceRegionale());
			ma.setDescEnte(m.getGregREnteGestoreContattis().stream().filter((c) -> c.getDataFineValidita() == null)
					.findFirst().get().getDenominazione());
			macro.add(ma);
		}
		return macro;
	}

	public List<GregRListaEntiGestori> findAllListaEnti(Integer idLista) {

		String hqlQuery = "SELECT distinct u FROM GregRListaEntiGestori u "
				+ "Left Join Fetch u.gregTSchedeEntiGestori e "
				+ "Left Join Fetch e.gregREnteGestoreContattis c "
				+ "WHERE u.dataCancellazione is null "
				+ "AND u.gregTLista.idLista = :idLista "
				+ "AND u.gregTLista.dataCancellazione is null "
				+ "AND u.gregTSchedeEntiGestori.dataCancellazione is null " + "ORDER BY u.gregTSchedeEntiGestori.codiceRegionale";

		TypedQuery<GregRListaEntiGestori> query = em.createQuery(hqlQuery, GregRListaEntiGestori.class);
		query.setParameter("idLista", idLista);
		return query.getResultList();
	}
	
	public GregTLista aggiornaTLista(GregTLista lista) {
		return em.merge(lista);
	}
	
	public GregTSchedeEntiGestori findEntebyId(Integer idEnte) {

		String hqlQuery = "SELECT u FROM GregTSchedeEntiGestori u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idSchedaEnteGestore = :idEnte ";

		TypedQuery<GregTSchedeEntiGestori> query = em.createQuery(hqlQuery, GregTSchedeEntiGestori.class);
		query.setParameter("idEnte", idEnte);
		return query.getSingleResult();
	}
	
	public GregRListaEntiGestori aggiornaRListaEntiGestori(GregRListaEntiGestori listaEnte) {
		return em.merge(listaEnte);
	}
	
	public GregRListaEntiGestori findListaEntebyId(Integer idListaEnte) {

		String hqlQuery = "SELECT u FROM GregRListaEntiGestori u " + "WHERE u.dataCancellazione is null "
				+ "AND u.idListaEntiGestori = :idListaEnte "
				+ "AND u.gregTLista.dataCancellazione is null";

		TypedQuery<GregRListaEntiGestori> query = em.createQuery(hqlQuery, GregRListaEntiGestori.class);
		query.setParameter("idListaEnte", idListaEnte);
		return query.getSingleResult();
	}
	
	public GregTLista findListabyCod(String codLista) {
		try {
			String hqlQuery = "SELECT u FROM GregTLista u " + "WHERE u.dataCancellazione is null "
					+ "AND u.codLista = :codLista ";

			TypedQuery<GregTLista> query = em.createQuery(hqlQuery, GregTLista.class);
			query.setParameter("codLista", codLista);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
