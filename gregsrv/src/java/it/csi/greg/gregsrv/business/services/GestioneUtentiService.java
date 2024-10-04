/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.greg.gregsrv.business.dao.impl.DatiRendicontazioneDao;
import it.csi.greg.gregsrv.business.dao.impl.GestioneUtentiDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloBDao;
import it.csi.greg.gregsrv.business.entity.GregDAzione;
import it.csi.greg.gregsrv.business.entity.GregDProfilo;
import it.csi.greg.gregsrv.business.entity.GregRListaEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregRProfiloAzione;
import it.csi.greg.gregsrv.business.entity.GregRUserProfilo;
import it.csi.greg.gregsrv.business.entity.GregTLista;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregTUser;
import it.csi.greg.gregsrv.dto.ModelAbilitazioni;
import it.csi.greg.gregsrv.dto.ModelAzione;
import it.csi.greg.gregsrv.dto.ModelLista;
import it.csi.greg.gregsrv.dto.ModelListaAzione;
import it.csi.greg.gregsrv.dto.ModelListaEnti;
import it.csi.greg.gregsrv.dto.ModelListaLista;
import it.csi.greg.gregsrv.dto.ModelListaProfilo;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;
import it.csi.greg.gregsrv.dto.ModelProfilo;
import it.csi.greg.gregsrv.dto.ModelUtenti;
import it.csi.greg.gregsrv.dto.RicercaProfili;
import it.csi.greg.gregsrv.dto.RicercaUtenti;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.SharedConstants;

@Service("gestioneUtentiService")
public class GestioneUtentiService {

	@Autowired
	protected GestioneUtentiDao gestioneUtentiDao;
	@Autowired
	protected DatiRendicontazioneDao datiRendicontazioneDao;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected MailService mailService;
	@Autowired
	protected ModelloBDao modelloBDao;

	public List<ModelUtenti> getUtenti(RicercaUtenti ricerca) {
		List<GregTUser> utenti;
		if (Checker.isValorizzato(ricerca.getNome()) || Checker.isValorizzato(ricerca.getCognome())
				|| Checker.isValorizzato(ricerca.getCodiceFiscale()) || Checker.isValorizzato(ricerca.getEmail())
				|| ricerca.getProfilo() != null || ricerca.getLista() != null || ricerca.getEnte() != null || ricerca.isAttivo()) {
			utenti = gestioneUtentiDao.findAllUtentiFilter(ricerca);
		} else {
			utenti = gestioneUtentiDao.findAllUtenti();
		}

		List<ModelUtenti> listaUtenti = new ArrayList<ModelUtenti>();
		for (GregTUser utente : utenti) {
			if (ricerca.getProfilo() != null || ricerca.getLista() != null || ricerca.getEnte() != null || ricerca.isAttivo()) {
				listaUtenti.add(new ModelUtenti(utente,
						gestioneUtentiDao.findAllUtentiAbilitazioneFilter(utente.getIdUser(), ricerca)));
			} else {
				listaUtenti
						.add(new ModelUtenti(utente, gestioneUtentiDao.findAllUtentiAbilitazione(utente.getIdUser())));
			}
		}
		return listaUtenti;
	}

	public List<ModelListeConfiguratore> getProfili() {
		List<ModelListeConfiguratore> macro = gestioneUtentiDao.findProfili();
		return macro;
	}

	public List<ModelListeConfiguratore> getListe() {
		List<ModelListeConfiguratore> macro = gestioneUtentiDao.findListe();
		return macro;
	}

	public List<ModelListeConfiguratore> getEnti() {
		List<ModelListeConfiguratore> macro = gestioneUtentiDao.findEnti();
		return macro;
	}

	public SaveModelloOutput eliminaAbilitazione(ModelAbilitazioni abilitazione, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregRUserProfilo userProfilo = gestioneUtentiDao.findUserProfilobyId(abilitazione.getIdUserProfilo());
		userProfilo.setUtenteOperazione(userInfo.getCodFisc());
		userProfilo.setDataModifica(dataAttuale);
		userProfilo.setDataCancellazione(dataAttuale);
		gestioneUtentiDao.aggiornaRUserProfilo(userProfilo);
		return response;
	}

	public SaveModelloOutput eliminaUtente(ModelUtenti utente, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		List<GregRUserProfilo> abilitazioni = gestioneUtentiDao.findAllUtentiAbilitazione(utente.getIdUtente());
		for(GregRUserProfilo up : abilitazioni) {
			if(up.getDataFineValidita()==null) {
				up.setUtenteOperazione(userInfo.getCodFisc());
				up.setDataModifica(dataAttuale);
				up.setDataFineValidita(utente.getDataFineValidita());
				gestioneUtentiDao.aggiornaRUserProfilo(up);
			}
		}
		GregTUser user = gestioneUtentiDao.findUserbyId(utente.getIdUtente());
		user.setUtenteOperazione(userInfo.getCodFisc());
		user.setDataModifica(dataAttuale);
		user.setDataCancellazione(dataAttuale);
		gestioneUtentiDao.aggiornaTUser(user);
		
		return response;
	}

	public SaveModelloOutput modificaUtente(ModelUtenti utente, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregTUser user = gestioneUtentiDao.findUserbyId(utente.getIdUtente());
		GregTUser userCodFiscale = gestioneUtentiDao.findUserbyCodiceFiscale(utente.getCodiceFiscale());
		if (userCodFiscale == null || (userCodFiscale != null && user.getIdUser().equals(userCodFiscale.getIdUser()))) {
			user.setNome(utente.getNome().toUpperCase());
			user.setCognome(utente.getCognome().toUpperCase());
			user.setCodiceFiscale(utente.getCodiceFiscale().toUpperCase());
			user.setEmail(utente.getEmail());
			user.setUtenteOperazione(userInfo.getCodFisc());
			user.setDataModifica(dataAttuale);
			gestioneUtentiDao.aggiornaTUser(user);
			response.setObblMotivazione(true);
			return response;
		} else {
			response.setObblMotivazione(false);
			return response;
		}

	}

	public SaveModelloOutput modificaAbilitazioni(ModelUtenti utente, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		for (ModelAbilitazioni a : utente.getAbilitazioni()) {
//			if (a.getStato().equals(SharedConstants.MODIFICATO)) {
//				GregRUserProfilo userProfilo = gestioneUtentiDao.findUserProfilobyId(a.getIdUserProfilo());
//				GregRUserProfilo userProfiloCod = gestioneUtentiDao.findUserProfilobyCod(utente.getIdUtente(),
//						a.getProfilo().getId(), a.getLista().getId());
//				if (userProfiloCod != null
//						&& !userProfilo.getIdUserProfilo().equals(userProfiloCod.getIdUserProfilo())) {
//					response.setObblMotivazione(false);
//					return response;
//				}
//			} else 
				if (a.getStato().equals(SharedConstants.NUOVO)) {
				GregRUserProfilo userProfiloCod = gestioneUtentiDao.findUserProfilobyCod(utente.getIdUtente(),
						a.getProfilo().getId(), a.getLista().getId(), a.getDataInizioValidita());
				if (userProfiloCod != null) {
					response.setObblMotivazione(false);
					return response;
				}
			}
		}
		for (ModelAbilitazioni a : utente.getAbilitazioni()) {
			GregTUser user = gestioneUtentiDao.findUserbyId(utente.getIdUtente());
			GregDProfilo profilo = gestioneUtentiDao.findProfilobyId(a.getProfilo().getId());
			GregTLista lista = gestioneUtentiDao.findListabyId(a.getLista().getId());
			if (a.getStato().equals(SharedConstants.MODIFICATO)) {
				GregRUserProfilo userProfilo = gestioneUtentiDao.findUserProfilobyId(a.getIdUserProfilo());
				userProfilo.setUtenteOperazione(userInfo.getCodFisc());
				userProfilo.setDataModifica(dataAttuale);
				userProfilo.setDataFineValidita(a.getDataFineValidita());
				gestioneUtentiDao.aggiornaRUserProfilo(userProfilo);
//				GregRUserProfilo newUserProfilo = new GregRUserProfilo();
//				newUserProfilo.setDataCreazione(dataAttuale);
//				newUserProfilo.setDataModifica(dataAttuale);
//				newUserProfilo.setGregDProfilo(profilo);
//				newUserProfilo.setGregTLista(lista);
//				newUserProfilo.setGregTUser(user);
//				newUserProfilo.setUtenteOperazione(userInfo.getCodFisc());
//				gestioneUtentiDao.aggiornaRUserProfilo(newUserProfilo);
			} else if (a.getStato().equals(SharedConstants.NUOVO)) {
				GregRUserProfilo newUserProfilo = new GregRUserProfilo();
				newUserProfilo.setDataCreazione(dataAttuale);
				newUserProfilo.setDataModifica(dataAttuale);
				newUserProfilo.setGregDProfilo(profilo);
				newUserProfilo.setGregTLista(lista);
				newUserProfilo.setGregTUser(user);
				newUserProfilo.setDataInizioValidita(a.getDataInizioValidita());
				newUserProfilo.setDataFineValidita(a.getDataFineValidita());
				newUserProfilo.setUtenteOperazione(userInfo.getCodFisc());
				gestioneUtentiDao.aggiornaRUserProfilo(newUserProfilo);
			}
		}
		response.setObblMotivazione(true);
		return response;
	}

	public List<ModelAbilitazioni> getAbilitazioni(ModelUtenti utente) {
		List<ModelAbilitazioni> listaAbilitazione = new ArrayList<ModelAbilitazioni>();
		List<GregRUserProfilo> userProfilo = gestioneUtentiDao.findAllUtentiAbilitazione(utente.getIdUtente());
		for (GregRUserProfilo up : userProfilo) {
			ModelAbilitazioni a = new ModelAbilitazioni(up);
			listaAbilitazione.add(a);
		}

		return listaAbilitazione;
	}

	public SaveModelloOutput creaUtente(ModelUtenti utente, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregTUser user = new GregTUser();
		GregTUser userCodFiscale = gestioneUtentiDao.findUserbyCodiceFiscale(utente.getCodiceFiscale());
		boolean trovato = false;
		for (int i = 0; i < utente.getAbilitazioni().size(); i++) {
			for (int j = 0; j < utente.getAbilitazioni().size(); j++) {
				if (i != j) {
					if (utente.getAbilitazioni().get(i).getProfilo().getCodice()
							.equals(utente.getAbilitazioni().get(j).getProfilo().getCodice())
							&& utente.getAbilitazioni().get(i).getLista().getCodice()
									.equals(utente.getAbilitazioni().get(j).getLista().getCodice())) {
						trovato = true;
					}
				}
			}
		}
		if (!trovato) {
			if (userCodFiscale == null) {
				user.setNome(utente.getNome().toUpperCase());
				user.setCognome(utente.getCognome().toUpperCase());
				user.setCodiceFiscale(utente.getCodiceFiscale().toUpperCase());
				user.setEmail(utente.getEmail());
				user.setUtenteOperazione(userInfo.getCodFisc());
				user.setDataCreazione(dataAttuale);
				user.setDataModifica(dataAttuale);
				user = gestioneUtentiDao.aggiornaTUser(user);

				for (ModelAbilitazioni a : utente.getAbilitazioni()) {
					GregDProfilo profilo = gestioneUtentiDao.findProfilobyId(a.getProfilo().getId());
					GregTLista lista = gestioneUtentiDao.findListabyId(a.getLista().getId());

					GregRUserProfilo newUserProfilo = new GregRUserProfilo();
					newUserProfilo.setDataCreazione(dataAttuale);
					newUserProfilo.setDataModifica(dataAttuale);
					newUserProfilo.setGregDProfilo(profilo);
					newUserProfilo.setGregTLista(lista);
					newUserProfilo.setGregTUser(user);
					newUserProfilo.setUtenteOperazione(userInfo.getCodFisc());
					newUserProfilo.setDataInizioValidita(a.getDataInizioValidita());
					newUserProfilo.setDataFineValidita(a.getDataFineValidita());
					
					gestioneUtentiDao.aggiornaRUserProfilo(newUserProfilo);
				}
				response.setIdEnte(0);
				response.setIdPrestazione(user.getIdUser());
				return response;
			} else {
				response.setIdEnte(1);
				return response;
			}
		} else {
			response.setIdEnte(2);
			return response;
		}
	}

	public List<ModelListaProfilo> getListaProfili(RicercaProfili ricerca) {
		List<GregDProfilo> profili;
		if (ricerca.getProfilo() != null || ricerca.getAzione() != null) {
			profili = gestioneUtentiDao.findAllProfiliFilter(ricerca);
		} else {
			profili = gestioneUtentiDao.findAllProfili();
		}
		List<ModelListaAzione> azioni = getAzioni();
		List<ModelListaProfilo> listaProfilo = new ArrayList<ModelListaProfilo>();
		for (GregDProfilo profilo : profili) {
			listaProfilo.add(new ModelListaProfilo(profilo,
					gestioneUtentiDao.findAllAzioneProfilo(profilo.getIdProfilo()), azioni));
		}
		return listaProfilo;
	}

	public List<ModelListaAzione> getAzioni() {
		List<ModelListaAzione> azioni = gestioneUtentiDao.findAzioni();
		return azioni;
	}

	public SaveModelloOutput eliminaAzione(ModelListaAzione azione, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregRProfiloAzione userProfilo = gestioneUtentiDao.findProfiloAzionebyId(azione.getIdProfiloAzione());
		userProfilo.setUtenteOperazione(userInfo.getCodFisc());
		userProfilo.setDataModifica(dataAttuale);
		userProfilo.setDataCancellazione(dataAttuale);
		gestioneUtentiDao.aggiornaRProfiloAzione(userProfilo);
		return response;
	}

	public SaveModelloOutput eliminaProfilo(ModelListaProfilo lista, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		List<GregRUserProfilo> abilitazioni = gestioneUtentiDao.findAllUtentiAbilitazionebyProfilo(lista.getIdProfilo());
		for(GregRUserProfilo up : abilitazioni) {
			up.setDataModifica(dataAttuale);
			up.setDataCancellazione(dataAttuale);
			gestioneUtentiDao.aggiornaRUserProfilo(up);
		}
		GregDProfilo profilo = gestioneUtentiDao.findProfilobyId(lista.getIdProfilo());
		profilo.setUtenteOperazione(userInfo.getCodFisc());
		profilo.setDataModifica(dataAttuale);
		profilo.setDataCancellazione(dataAttuale);
		gestioneUtentiDao.aggiornaDProfilo(profilo);
		
		return response;
	}

	public SaveModelloOutput modificaAzioni(ModelListaProfilo profilo, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		for (ModelListaAzione a : profilo.getAzioni()) {
			GregDProfilo p = gestioneUtentiDao.findProfilobyId(profilo.getIdProfilo());
			GregDAzione azione = gestioneUtentiDao.findAzionebyId(a.getIdAzione());
			if (a.getStato().equals(SharedConstants.NUOVO)) {
				GregRProfiloAzione newProfiloAzione = new GregRProfiloAzione();
				newProfiloAzione.setDataCreazione(dataAttuale);
				newProfiloAzione.setDataModifica(dataAttuale);
				newProfiloAzione.setGregDProfilo(p);
				newProfiloAzione.setGregDAzione(azione);
				newProfiloAzione.setUtenteOperazione(userInfo.getCodFisc());
				gestioneUtentiDao.aggiornaRProfiloAzione(newProfiloAzione);
			}
		}
		return response;
	}

	public List<ModelListaAzione> getAzioniProfilo(ModelListaProfilo profilo) {
		List<ModelListaAzione> listaAzioni = new ArrayList<ModelListaAzione>();
		List<GregRProfiloAzione> profiloAzioni = gestioneUtentiDao.findAllAzioneProfilo(profilo.getIdProfilo());
		for (GregRProfiloAzione up : profiloAzioni) {
			ModelListaAzione a = new ModelListaAzione(up);
			listaAzioni.add(a);
		}

		return listaAzioni;
	}

	public SaveModelloOutput modificaProfilo(ModelListaProfilo profilo, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregDProfilo p = gestioneUtentiDao.findProfilobyId(profilo.getIdProfilo());
		p.setDescProfilo(profilo.getDescProfilo());
		p.setUtenteOperazione(userInfo.getCodFisc());
		p.setDataModifica(dataAttuale);
		gestioneUtentiDao.aggiornaDProfilo(p);
		if (profilo.getAzioniDaCopiare() != null) {
			for (ModelListaAzione a : profilo.getAzioniDaCopiare()) {
				GregDAzione azione = gestioneUtentiDao.findAzionebyId(a.getIdAzione());

				GregRProfiloAzione newProfiloAzione = new GregRProfiloAzione();
				newProfiloAzione.setDataCreazione(dataAttuale);
				newProfiloAzione.setDataModifica(dataAttuale);
				newProfiloAzione.setGregDProfilo(p);
				newProfiloAzione.setGregDAzione(azione);
				newProfiloAzione.setUtenteOperazione(userInfo.getCodFisc());
				gestioneUtentiDao.aggiornaRProfiloAzione(newProfiloAzione);

			}
		}
		return response;
	}

	public SaveModelloOutput creaProfilo(ModelListaProfilo profilo, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregDProfilo p = new GregDProfilo();
		GregDProfilo profiloCod = gestioneUtentiDao.findProfilobyCod(profilo.getCodProfilo());

		if (profiloCod == null) {
			p.setCodProfilo(profilo.getCodProfilo());
			p.setDescProfilo(profilo.getDescProfilo());
			p.setUtenteOperazione(userInfo.getCodFisc());
			p.setDataCreazione(dataAttuale);
			p.setDataModifica(dataAttuale);
			p = gestioneUtentiDao.aggiornaDProfilo(p);
			if (profilo.getAzioni() != null) {
				for (ModelListaAzione a : profilo.getAzioni()) {
					GregDAzione azione = gestioneUtentiDao.findAzionebyId(a.getIdAzione());
					GregRProfiloAzione newProfiloAzione = new GregRProfiloAzione();
					newProfiloAzione.setDataCreazione(dataAttuale);
					newProfiloAzione.setDataModifica(dataAttuale);
					newProfiloAzione.setGregDProfilo(p);
					newProfiloAzione.setGregDAzione(azione);
					newProfiloAzione.setUtenteOperazione(userInfo.getCodFisc());
					gestioneUtentiDao.aggiornaRProfiloAzione(newProfiloAzione);
				}
			}
			response.setIdEnte(p.getIdProfilo());
			response.setObblMotivazione(true);
			return response;
		} else {
			response.setObblMotivazione(false);
			return response;
		}

	}
	
	public List<ModelListaLista> getLista(RicercaProfili ricerca) {
		List<GregTLista> liste;
		if (ricerca.getLista() != null || ricerca.getEnte() != null) {
			liste = gestioneUtentiDao.findAllListeFilter(ricerca);
		} else {
			liste = gestioneUtentiDao.findAllListe();
		}
		List<ModelListaEnti> enti = getEntiListe();
		List<ModelListaLista> listona = new ArrayList<ModelListaLista>();
		for (GregTLista lista : liste) {
			listona.add(new ModelListaLista(lista,
					gestioneUtentiDao.findAllListaEnti(lista.getIdLista()), enti));
		}
		return listona;
	}
	
	public List<ModelListaEnti> getEntiListe() {
		List<ModelListaEnti> enti = gestioneUtentiDao.findEntiListe();
		return enti;
	}
	
	public SaveModelloOutput eliminaLista(ModelListaLista lista, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		List<GregRUserProfilo> abilitazioni = gestioneUtentiDao.findAllUtentiAbilitazionebyLista(lista.getIdLista());
		for(GregRUserProfilo up : abilitazioni) {
			up.setDataModifica(dataAttuale);
			up.setDataCancellazione(dataAttuale);
			gestioneUtentiDao.aggiornaRUserProfilo(up);
		}
		GregTLista l = gestioneUtentiDao.findListabyId(lista.getIdLista());
		l.setUtenteOperazione(userInfo.getCodFisc());
		l.setDataModifica(dataAttuale);
		l.setDataCancellazione(dataAttuale);
		gestioneUtentiDao.aggiornaTLista(l);
		return response;
	}
	
	public SaveModelloOutput modificaLista(ModelListaLista lista, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregTLista l = gestioneUtentiDao.findListabyId(lista.getIdLista());
		l.setDescLista(lista.getDescLista());
		l.setUtenteOperazione(userInfo.getCodFisc());
		l.setDataModifica(dataAttuale);
		gestioneUtentiDao.aggiornaTLista(l);
		if (lista.getEntiDaCopiare() != null) {
			for (ModelListaEnti a : lista.getEntiDaCopiare()) {
				GregTSchedeEntiGestori ente = gestioneUtentiDao.findEntebyId(a.getIdEnte());

				GregRListaEntiGestori newListaEnti = new GregRListaEntiGestori();
				newListaEnti.setDataCreazione(dataAttuale);
				newListaEnti.setDataModifica(dataAttuale);
				newListaEnti.setGregTLista(l);
				newListaEnti.setGregTSchedeEntiGestori(ente);
				newListaEnti.setUtenteOperazione(userInfo.getCodFisc());
				gestioneUtentiDao.aggiornaRListaEntiGestori(newListaEnti);

			}
		}
		return response;
	}
	
	public List<ModelListaEnti> getListaEnti(ModelListaLista lista) {
		List<ModelListaEnti> listaEnti = new ArrayList<ModelListaEnti>();
		List<GregRListaEntiGestori> profiloAzioni = gestioneUtentiDao.findAllListaEnti(lista.getIdLista());
		for (GregRListaEntiGestori up : profiloAzioni) {
			ModelListaEnti a = new ModelListaEnti(up);
			listaEnti.add(a);
		}

		return listaEnti;
	}
	
	public SaveModelloOutput eliminaEnte(ModelListaEnti ente, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregRListaEntiGestori listaEnte = gestioneUtentiDao.findListaEntebyId(ente.getIdListaEnte());
		listaEnte.setUtenteOperazione(userInfo.getCodFisc());
		listaEnte.setDataModifica(dataAttuale);
		listaEnte.setDataCancellazione(dataAttuale);
		gestioneUtentiDao.aggiornaRListaEntiGestori(listaEnte);
		return response;
	}
	
	public SaveModelloOutput modificaEnti(ModelListaLista lista, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		for (ModelListaEnti a : lista.getEnti()) {
			GregTLista p = gestioneUtentiDao.findListabyId(lista.getIdLista());
			GregTSchedeEntiGestori ente = gestioneUtentiDao.findEntebyId(a.getIdEnte());
			if (a.getStato().equals(SharedConstants.NUOVO)) {
				GregRListaEntiGestori newListaEnte = new GregRListaEntiGestori();
				newListaEnte.setDataCreazione(dataAttuale);
				newListaEnte.setDataModifica(dataAttuale);
				newListaEnte.setGregTLista(p);
				newListaEnte.setGregTSchedeEntiGestori(ente);
				newListaEnte.setUtenteOperazione(userInfo.getCodFisc());
				gestioneUtentiDao.aggiornaRListaEntiGestori(newListaEnte);
			}
		}
		return response;
	}
	
	public SaveModelloOutput creaLista(ModelListaLista lista, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregTLista l = new GregTLista();
		GregTLista listaCod = gestioneUtentiDao.findListabyCod(lista.getCodLista());

		if (listaCod == null) {
			l.setCodLista(lista.getCodLista());
			l.setDescLista(lista.getDescLista());
			l.setUtenteOperazione(userInfo.getCodFisc());
			l.setDataCreazione(dataAttuale);
			l.setDataModifica(dataAttuale);
			l = gestioneUtentiDao.aggiornaTLista(l);
			if (lista.getEnti() != null) {
				for (ModelListaEnti a : lista.getEnti()) {
					GregTSchedeEntiGestori ente = gestioneUtentiDao.findEntebyId(a.getIdEnte());
					GregRListaEntiGestori newListaEnte = new GregRListaEntiGestori();
					newListaEnte.setDataCreazione(dataAttuale);
					newListaEnte.setDataModifica(dataAttuale);
					newListaEnte.setGregTLista(l);
					newListaEnte.setGregTSchedeEntiGestori(ente);
					newListaEnte.setUtenteOperazione(userInfo.getCodFisc());
					gestioneUtentiDao.aggiornaRListaEntiGestori(newListaEnte);
				}
			}
			response.setIdEnte(l.getIdLista());
			response.setObblMotivazione(true);
			return response;
		} else {
			response.setObblMotivazione(false);
			return response;
		}

	}
}
