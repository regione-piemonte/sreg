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
import it.csi.greg.gregsrv.business.services.ConfiguratoreUtenzeFnpsService;
import it.csi.greg.gregsrv.business.services.ControlloService;
import it.csi.greg.gregsrv.business.services.GestioneUtentiService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.business.services.MacroaggregatiService;
import it.csi.greg.gregsrv.dto.EsportaMacroaggregatiInput;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelAbilitazioni;
import it.csi.greg.gregsrv.dto.ModelFondi;
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
import it.csi.greg.gregsrv.dto.ModelUtentiFnps;
import it.csi.greg.gregsrv.dto.ModelUtenzaAllD;
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
@RequestMapping(URIConstants.CONFIGURATOREFNPS)
public class ConfiguratoreUtenzeFnps {

	/** Constants Mapping **/
	public static final String GET_UTENZE = "/getUtenze";
	public static final String GET_UTENZE_FNPS = "/getUtenzeFnps";
	public static final String CREA_UTENZE_FNPS = "/creaUtenzeFnps";
	public static final String ELIMINA_UTENZA_FNPS = "/eliminaUtenzaFnps";
	public static final String GET_UTENZE_ANNO_ESERCIZIO = "/getUtenzeByAnno";
	public static final String GET_FONDI_ENTE = "/getFondiEnte";
	public static final String GET_FONDI_ANNO = "/getFondiAnno";
	public static final String GET_REGOLE = "/getRegole";
	public static final String ELIMINA_FONDO = "/eliminaFondo";


	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected ConfiguratoreUtenzeFnpsService configuratoreUtenzeFnpsService;
	@Autowired
	protected HttpServletRequest httpRequest;

	@RequestMapping(value = GET_UTENZE, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getUtenze() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETUTENZE + " nella classe "
				+ ConfiguratoreUtenzeFnps.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelUtentiFnps> utenti = configuratoreUtenzeFnpsService.getUtenze();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETUTENZE + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName());
			return new ResponseEntity<List<ModelUtentiFnps>>(utenti, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETUTENZE + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = GET_UTENZE_FNPS, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getUtenzeFnps() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETUTENZEFNPS + " nella classe "
				+ ConfiguratoreUtenzeFnps.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelUtenzaAllD> utenti = configuratoreUtenzeFnpsService.getUtenzeFnps();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETUTENZEFNPS + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName());
			return new ResponseEntity<List<ModelUtenzaAllD>>(utenti, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETUTENZEFNPS + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = CREA_UTENZE_FNPS, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> creaUtente(@RequestBody(required = true) List<ModelUtentiFnps> u) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CREAUTENZEFNPS + " nella classe "
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
			SaveModelloOutput crea = configuratoreUtenzeFnpsService.creaUtenze(u, userInfo);

			String message = listeService.getMessaggio(SharedConstants.SALVATAGGIO_FNPS_UTENZE_CALCOLO)
					.getTestoMessaggio();
			crea.setEsito("OK");
			crea.setMessaggio(message);
			crea.setDescrizione(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CREAUTENZEFNPS + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(crea, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CREAUTENZEFNPS + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = ELIMINA_UTENZA_FNPS, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> creaUtente(@RequestBody(required = true) ModelUtentiFnps u) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_ELIMINAUTENZAFNPS + " nella classe "
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
			SaveModelloOutput crea = configuratoreUtenzeFnpsService.eliminaUtenza(u, userInfo);

			String message = listeService.getMessaggio(SharedConstants.ELIMINAZIONE_FNPS_UTENZE_CALCOLO)
					.getTestoMessaggio();
			crea.setEsito("OK");
			crea.setMessaggio(message);
			crea.setDescrizione(message);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ELIMINAUTENZAFNPS + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(crea, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ELIMINAUTENZAFNPS + " nella classe "
					+ GestioneUtenti.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = GET_UTENZE_ANNO_ESERCIZIO, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getUtenzeByAnno(@RequestParam(name = "annoEsercizio", required = true) Integer annoEsercizio) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETUTENZEANNOESERCIZIO + " nella classe "
				+ ConfiguratoreUtenzeFnps.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelUtentiFnps> utenti = configuratoreUtenzeFnpsService.recuperaUtenzePerCalcoloByAnnoEsercizio(annoEsercizio);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETUTENZEANNOESERCIZIO + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName());
			return new ResponseEntity<List<ModelUtentiFnps>>(utenti, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETUTENZEANNOESERCIZIO + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = GET_FONDI_ENTE, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getFondiEnte(@RequestParam(name = "idRendicontazione", required = true) Integer idRendicontazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
				+ ConfiguratoreUtenzeFnps.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelFondi> fondi = configuratoreUtenzeFnpsService.findFondiByidRendicontazione(idRendicontazione);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName());
			return new ResponseEntity<List<ModelFondi>>(fondi, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = GET_FONDI_ANNO, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getFondiAnno(@RequestParam(name = "annoEsercizio", required = true) Integer annoEsercizio) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
				+ ConfiguratoreUtenzeFnps.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelFondi> fondi = configuratoreUtenzeFnpsService.findFondiByAnnoEsercizio(annoEsercizio);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName());
			return new ResponseEntity<List<ModelFondi>>(fondi, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = GET_REGOLE, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getRegole() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
				+ ConfiguratoreUtenzeFnps.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelFondi> fondi = configuratoreUtenzeFnpsService.findRegole();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName());
			return new ResponseEntity<List<ModelFondi>>(fondi, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = ELIMINA_FONDO, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> eliminaFondo(@RequestBody(required = true) ModelFondi u) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
				+ ConfiguratoreUtenzeFnps.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			SaveModelloOutput elimina = configuratoreUtenzeFnpsService.eliminaFondo(u, userInfo);	
			elimina.setEsito("OK");
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_INSERT, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ELIMINAABILITAZIONE + " nella classe "
					+ GestioneUtenti.class.getName());
			return new ResponseEntity<SaveModelloOutput>(elimina, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETFONDIENTE + " nella classe "
					+ ConfiguratoreUtenzeFnps.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
		}
	}
}
