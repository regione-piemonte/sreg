/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ControlloService;
import it.csi.greg.gregsrv.business.services.GestioneUtentiService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.business.services.MacroaggregatiService;
import it.csi.greg.gregsrv.dto.EsportaMacroaggregatiInput;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelAbilitazioni;
import it.csi.greg.gregsrv.dto.ModelListaAzione;
import it.csi.greg.gregsrv.dto.ModelListaEnti;
import it.csi.greg.gregsrv.dto.ModelListaLista;
import it.csi.greg.gregsrv.dto.ModelListaProfilo;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;
import it.csi.greg.gregsrv.dto.ModelMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelProfilo;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliSpeseMissioni;
import it.csi.greg.gregsrv.dto.ModelSpesaMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelSpesaMissione;
import it.csi.greg.gregsrv.dto.ModelUtenti;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.dto.RicercaProfili;
import it.csi.greg.gregsrv.dto.RicercaUtenti;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.GESTIONEUTENTI)
public class GestioneUtenti {

	/** Constants Mapping **/
	public static final String GET_UTENTI = "/getUtenti";
	public static final String GET_PROFILI = "/getProfili";
	public static final String GET_LISTE = "/getListe";
	public static final String GET_ENTI = "/getEnti";
	public static final String ELIMINA_ABILITAZIONE = "/eliminaAbilitazione";
	public static final String ELIMINA_UTENTE = "/eliminaUtente";
	public static final String MODIFICA_UTENTE = "/modificaUtente";
	public static final String MODIFICA_ABILITAZIONI = "/modificaAbilitazioni";
	public static final String GET_ABILITAZIONI = "/getAbilitazioni";
	public static final String CREA_UTENTE = "/creaUtente";
	public static final String GET_LISTAPROFILI = "/getListaProfili";
	public static final String GET_AZIONI = "/getAzioni";
	public static final String ELIMINA_AZIONE = "/eliminaAzione";
	public static final String ELIMINA_PROFILO = "/eliminaProfilo";
	public static final String MODIFICA_AZIONI = "/modificaAzioni";
	public static final String GET_AZIONIPROFILO = "/getAzioniProfilo";
	public static final String MODIFICA_PROFILO = "/modificaProfilo";
	public static final String CREA_PROFILO = "/creaProfilo";
	public static final String GET_LISTA = "/getLista";
	public static final String GET_ENTILISTA = "/getEntiLista";
	public static final String ELIMINA_LISTA = "/eliminaLista";
	public static final String MODIFICA_LISTA = "/modificaLista";
	public static final String GET_LISTAENTI = "/getListaEnti";
	public static final String ELIMINA_ENTE = "/eliminaEnte";
	public static final String MODIFICA_ENTI = "/modificaEnti";
	public static final String CREA_LISTA = "/creaLista";

	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected GestioneUtentiService gestioneUtentiService;
	@Autowired
	protected HttpServletRequest httpRequest;

	@RequestMapping(value = GET_UTENTI, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getUtenti(@RequestBody(required = true) RicercaUtenti ricerca) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETUTENTI + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelUtenti> utenti = gestioneUtentiService.getUtenti(ricerca);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETUTENTI + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<List<ModelUtenti>>(utenti, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETUTENTI + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = GET_PROFILI, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getProfili(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETPROFILI + " nella classe "
				+ DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelListeConfiguratore> listaStrutture = new ArrayList<ModelListeConfiguratore>();
			listaStrutture = gestioneUtentiService.getProfili();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETPROFILI + " nella classe "
					+ DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaStrutture, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETPROFILI + " nella classe "
					+ DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = GET_LISTE, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getStrutture(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTE + " nella classe "
				+ DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelListeConfiguratore> listaStrutture = new ArrayList<ModelListeConfiguratore>();
			listaStrutture = gestioneUtentiService.getListe();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTE + " nella classe "
					+ DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaStrutture, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTE + " nella classe "
					+ DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = GET_ENTI, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getEnti(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETENTI + " nella classe "
				+ DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelListeConfiguratore> listaStrutture = new ArrayList<ModelListeConfiguratore>();
			listaStrutture = gestioneUtentiService.getEnti();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETENTI + " nella classe "
					+ DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaStrutture, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETENTI + " nella classe "
					+ DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = ELIMINA_ABILITAZIONE, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> eliminaAbilitazione(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelAbilitazioni body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_ELIMINAABILITAZIONE + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput elimina = gestioneUtentiService.eliminaAbilitazione(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOUTENTE)
					.getTestoMsgInformativo();
			elimina.setEsito("OK");
			elimina.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ELIMINAABILITAZIONE + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(elimina, HttpStatus.OK);
		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAABILITAZIONE + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAABILITAZIONE + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = ELIMINA_UTENTE, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> eliminaUtente(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelUtenti body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_ELIMINAUTENTE + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput elimina = gestioneUtentiService.eliminaUtente(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOUTENTE)
					.getTestoMsgInformativo();
			elimina.setEsito("OK");
			elimina.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ELIMINAUTENTE + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(elimina, HttpStatus.OK);
		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAUTENTE + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAUTENTE + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = MODIFICA_UTENTE, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> modificaUtente(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelUtenti body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODIFICAUTENTE + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput modifica = gestioneUtentiService.modificaUtente(body, userInfo);
			if (modifica.isObblMotivazione()) {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOUTENTE)
						.getTestoMsgInformativo();
				modifica.setEsito("OK");
				modifica.setMessaggio(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODIFICAUTENTE + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(modifica, HttpStatus.OK);
			} else {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.CODICEFISCALEESISTENTE)
						.getTestoMsgInformativo();
				modifica.setId("500");
				modifica.setDescrizione(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODIFICAUTENTE + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(modifica, HttpStatus.BAD_REQUEST);
			}

		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAUTENTE + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAUTENTE + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = MODIFICA_ABILITAZIONI, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> modificaAbilitazioni(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelUtenti body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODIFICAABILITAZIONI + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput modifica = gestioneUtentiService.modificaAbilitazioni(body, userInfo);
			if (modifica.isObblMotivazione()) {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOUTENTE)
						.getTestoMsgInformativo();
				modifica.setEsito("OK");
				modifica.setMessaggio(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODIFICAABILITAZIONI + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(modifica, HttpStatus.OK);
			} else {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.ABILITAZIONEESISTENTE)
						.getTestoMsgInformativo();
				modifica.setId("500");
				modifica.setDescrizione(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODIFICAABILITAZIONI + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(modifica, HttpStatus.BAD_REQUEST);
			}

		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAABILITAZIONI + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAABILITAZIONI + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = GET_ABILITAZIONI, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getAbilitazioni(@RequestBody(required = true) ModelUtenti u) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETABILITAZIONI + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelAbilitazioni> abilitazioni = gestioneUtentiService.getAbilitazioni(u);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETABILITAZIONI + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<List<ModelAbilitazioni>>(abilitazioni, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETABILITAZIONI + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = CREA_UTENTE, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> creaUtente(@RequestBody(required = true) ModelUtenti u) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CREAUTENTE + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			SaveModelloOutput crea = gestioneUtentiService.creaUtente(u, userInfo);
			if (crea.getIdEnte().equals(0)) {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMACREAUTENTE)
						.getTestoMsgInformativo();
				crea.setEsito("OK");
				crea.setMessaggio(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CREAUTENTE + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(crea, HttpStatus.OK);
			} else if (crea.getIdEnte().equals(1)) {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.CODICEFISCALEESISTENTE)
						.getTestoMsgInformativo();
				crea.setId("500");
				crea.setDescrizione(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CREAUTENTE + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(crea, HttpStatus.BAD_REQUEST);
			} else {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.ABILITAZIONEESISTENTE)
						.getTestoMsgInformativo();
				crea.setId("500");
				crea.setDescrizione(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CREAUTENTE + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(crea, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CREAUTENTE + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = GET_LISTAPROFILI, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getListaProfili(@RequestBody(required = true) RicercaProfili ricerca) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTAPROFILO + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelListaProfilo> profilo = gestioneUtentiService.getListaProfili(ricerca);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTAPROFILO + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<List<ModelListaProfilo>>(profilo, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTAPROFILO + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = GET_AZIONI, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getAzioni(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETAZIONI + " nella classe "
				+ DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelListaAzione> listaAzioni = new ArrayList<ModelListaAzione>();
			listaAzioni = gestioneUtentiService.getAzioni();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETAZIONI + " nella classe "
					+ DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListaAzione>>(listaAzioni, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETAZIONI + " nella classe "
					+ DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = ELIMINA_AZIONE, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> eliminaAzione(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelListaAzione body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_ELIMINAAZIONE + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput elimina = gestioneUtentiService.eliminaAzione(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOPROFILO)
					.getTestoMsgInformativo();
			elimina.setEsito("OK");
			elimina.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ELIMINAAZIONE + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(elimina, HttpStatus.OK);
		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAAZIONE + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAAZIONE + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = ELIMINA_PROFILO, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> eliminaProfilo(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelListaProfilo body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_ELIMINAPROFILO + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput elimina = gestioneUtentiService.eliminaProfilo(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAELIMINAZIONEPROFILO)
					.getTestoMsgInformativo();
			elimina.setEsito("OK");
			elimina.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ELIMINAPROFILO + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(elimina, HttpStatus.OK);
		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAPROFILO + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAPROFILO + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = MODIFICA_AZIONI, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> modificaAzioni(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelListaProfilo body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODIFICAAZIONI + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput modifica = gestioneUtentiService.modificaAzioni(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOPROFILO)
					.getTestoMsgInformativo();
			modifica.setEsito("OK");
			modifica.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODIFICAAZIONI + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(modifica, HttpStatus.OK);

		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAAZIONI + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAAZIONI + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = GET_AZIONIPROFILO, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getAzioniProfilo(@RequestBody(required = true) ModelListaProfilo u) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETAZIONIPROFILO + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelListaAzione> azioni = gestioneUtentiService.getAzioniProfilo(u);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETAZIONIPROFILO + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<List<ModelListaAzione>>(azioni, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETAZIONIPROFILO + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = MODIFICA_PROFILO, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> modificaProfilo(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelListaProfilo body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODIFICAPROFILO + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput modifica = gestioneUtentiService.modificaProfilo(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOPROFILO)
					.getTestoMsgInformativo();
			modifica.setEsito("OK");
			modifica.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODIFICAPROFILO + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(modifica, HttpStatus.OK);

		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAPROFILO + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAPROFILO + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = CREA_PROFILO, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> creaProfilo(@RequestBody(required = true) ModelListaProfilo p) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CREAPROFILO + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			SaveModelloOutput crea = gestioneUtentiService.creaProfilo(p, userInfo);
			if (crea.isObblMotivazione()) {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMACREAPROFILO)
						.getTestoMsgInformativo();
				crea.setEsito("OK");
				crea.setMessaggio(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CREAPROFILO + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(crea, HttpStatus.OK);
			} else {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.CODICEPROFILOESISTENTE)
						.getTestoMsgInformativo();
				crea.setId("500");
				crea.setDescrizione(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CREAPROFILO + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(crea, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CREAPROFILO + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = GET_LISTA, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getLista(@RequestBody(required = true) RicercaProfili ricerca) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTA + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelListaLista> liste = gestioneUtentiService.getLista(ricerca);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTA + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<List<ModelListaLista>>(liste, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTA + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = GET_ENTILISTA, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getEntiLista(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETENTILISTA + " nella classe "
				+ DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelListaEnti> listaEnti = new ArrayList<ModelListaEnti>();
			listaEnti = gestioneUtentiService.getEntiListe();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETENTILISTA + " nella classe "
					+ DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListaEnti>>(listaEnti, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETENTILISTA + " nella classe "
					+ DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = ELIMINA_LISTA, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> eliminaLista(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelListaLista body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_ELIMINALISTA + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput elimina = gestioneUtentiService.eliminaLista(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAELIMINAZIONELISTA)
					.getTestoMsgInformativo();
			elimina.setEsito("OK");
			elimina.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ELIMINALISTA + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(elimina, HttpStatus.OK);
		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINALISTA + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINALISTA + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = MODIFICA_LISTA, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> modificaLista(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelListaLista body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODIFICALISTA + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput modifica = gestioneUtentiService.modificaLista(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOLISTA)
					.getTestoMsgInformativo();
			modifica.setEsito("OK");
			modifica.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODIFICALISTA + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(modifica, HttpStatus.OK);

		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICALISTA + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAPROFILO + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = GET_LISTAENTI, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getListaEnti(@RequestBody(required = true) ModelListaLista u) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTAENTI + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelListaEnti> enti = gestioneUtentiService.getListaEnti(u);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTAENTI + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<List<ModelListaEnti>>(enti, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTAENTI + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = ELIMINA_ENTE, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> eliminaEnte(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelListaEnti body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_ELIMINAENTE + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput elimina = gestioneUtentiService.eliminaEnte(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOLISTA)
					.getTestoMsgInformativo();
			elimina.setEsito("OK");
			elimina.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ELIMINAENTE + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(elimina, HttpStatus.OK);
		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAENTE + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAENTE + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = MODIFICA_ENTI, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> modificaEnti(@Context HttpServletRequest httpRequest,
			@RequestBody(required = true) ModelListaLista body) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODIFICAENTI + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			if (!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			SaveModelloOutput modifica = gestioneUtentiService.modificaEnti(body, userInfo);

			String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMAAGGIORNAMENTOLISTA)
					.getTestoMsgInformativo();
			modifica.setEsito("OK");
			modifica.setMessaggio(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODIFICAENTI + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(modifica, HttpStatus.OK);

		} catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAENTI + " nella classe "
					+ DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}

		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODIFICAENTI + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = CREA_LISTA, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> creaLista(@RequestBody(required = true) ModelListaLista p) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CREALISTA + " nella classe "
				+ GestioneUtenti.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			SaveModelloOutput crea = gestioneUtentiService.creaLista(p, userInfo);
			if (crea.isObblMotivazione()) {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.CONFERMACREALISTA)
						.getTestoMsgInformativo();
				crea.setEsito("OK");
				crea.setMessaggio(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CREALISTA + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(crea, HttpStatus.OK);
			} else {
				String message = listeService.getMsgInformativiByCodice(SharedConstants.CODICELISTAESISTENTE)
						.getTestoMsgInformativo();
				crea.setId("500");
				crea.setDescrizione(message);
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
						SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CREALISTA + " nella classe "
						+ GestioneUtenti.class.getName());
				return new ResponseEntity<SaveModelloOutput>(crea, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CREALISTA + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
}
