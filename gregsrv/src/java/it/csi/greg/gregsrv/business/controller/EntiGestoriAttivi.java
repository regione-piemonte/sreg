/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ControlloService;
import it.csi.greg.gregsrv.business.services.EntiGestoriAttiviService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ListaEntiAnno;
import it.csi.greg.gregsrv.dto.ModelComune;
import it.csi.greg.gregsrv.dto.ModelCronologiaEnte;
import it.csi.greg.gregsrv.dto.ModelDatiEnte;
import it.csi.greg.gregsrv.dto.ModelDenominazioniComuniAssociati;
import it.csi.greg.gregsrv.dto.ModelDettaglioPrestazConfiguratore;
import it.csi.greg.gregsrv.dto.ModelRicercaEntiGestoriAttivi;
import it.csi.greg.gregsrv.dto.ModelStatiEnte;
import it.csi.greg.gregsrv.dto.ModelStatoRendicontazione;
import it.csi.greg.gregsrv.dto.ModelStoricoEnte;
import it.csi.greg.gregsrv.dto.ModelTipoEnte;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.dto.RicercaEntiGestoriAttiviOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.ENTI_GESTORI_ATTIVI)
public class EntiGestoriAttivi {	
	
	/**Constants Mapping**/
	public static final String GET_LISTA_STATO_RENDICONTAZIONE = "/getListaStatoRendicontazione";
	public static final String GET_LISTA_COMUNI = "/getListaComuni";
	public static final String GET_LISTA_TIPO_ENTE = "/getListaTipoEnte";
	public static final String SEARCH_SCHEDE_ENTI_GESTORI = "/searchSchedeEntiGestori";
	public static final String GET_LISTA_DENOMINAZIONI = "/getListaDenominazioni";
	public static final String GET_LISTA_DENOMINAZIONI_WITH_COMUNI_ASSOCIATI = "/getListaDenominazioniWithComuniAssociati";	
	public static final String GET_CRONOLOGIA= "/getCronologia";
	public static final String SEARCH_SCHEDE_MULTI_ENTI_GESTORI = "/searchSchedeMultiEntiGestori";
	public static final String GET_SCHEDA_ENTE_GESTORE = "/getSchedaEnteGestore";
	public static final String GET_STATI_ENTE = "/getStatiEnte";
	public static final String GET_ANNI_ESERCIZIO = "/getAnniEsercizio";
	public static final String GET_STORICO= "/getStorico";
	public static final String CREA_NUOVO_ANNO= "/creaNuovoAnno";	
	public static final String CONCLUDI_RENDICONTAZIONE= "/concludiRendicontazione";
	public static final String RIPRISTINA_RENDICONTAZIONE= "/ripristinaRendicontazione";
	public static final String RIPRISTINA_COMPILAZIONE= "/ripristinaCompilazione";
	public static final String STORICIZZA_MULTIPLO= "/storicizzaMultiplo";
	
	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	
	/**Dependency - Injection**/
	@Autowired
	protected EntiGestoriAttiviService entiGestoriAttiviService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected HttpServletRequest httpRequest;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= GET_LISTA_STATO_RENDICONTAZIONE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaStatoRendicontazione() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTASTATORENDICONTAZIONE + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelStatoRendicontazione> listaStatoRendicontazione = new ArrayList<ModelStatoRendicontazione>();
			listaStatoRendicontazione = entiGestoriAttiviService.findAllStatoRendicontazione();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTASTATORENDICONTAZIONE + " nella classe " + EntiGestoriAttivi.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelStatoRendicontazione>>(listaStatoRendicontazione, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTASTATORENDICONTAZIONE + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};	
	
	@RequestMapping(value= GET_LISTA_COMUNI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaComuni(@RequestParam(name = "dataValidita", required = true) String dataValidita) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTACOMUNI + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<ModelComune> listaComuni = new ArrayList<ModelComune>();
			String codregione = listeService.getParametro(SharedConstants.CODREGIONE).getValtext();
			listaComuni = entiGestoriAttiviService.findAllComuni(codregione,dataValidita);
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTACOMUNI + " nella classe " + EntiGestoriAttivi.class.getName());		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelComune>>(listaComuni, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTACOMUNI + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};	
	
	@RequestMapping(value= GET_LISTA_TIPO_ENTE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaTipoEnte() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTATIPOENTE + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelTipoEnte> listaTipoEnte = new ArrayList<ModelTipoEnte>();
			listaTipoEnte = entiGestoriAttiviService.findAllTipoEnte();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTATIPOENTE + " nella classe " + EntiGestoriAttivi.class.getName());			
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelTipoEnte>>(listaTipoEnte, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTATIPOENTE + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= GET_LISTA_DENOMINAZIONI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaDenominazioni() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTADENOMINAZIONI + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelDatiEnte> listaDenominazioni = new ArrayList<ModelDatiEnte>();
			listaDenominazioni = entiGestoriAttiviService.findAllDenominazioni();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTADENOMINAZIONI + " nella classe " + EntiGestoriAttivi.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelDatiEnte>>(listaDenominazioni, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTADENOMINAZIONI + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
	@RequestMapping(value= GET_LISTA_DENOMINAZIONI_WITH_COMUNI_ASSOCIATI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getListaDenominazioniWithComuniAssociati() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETLISTADENOMINAZIONI + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelDenominazioniComuniAssociati> listaDenominazioniWithComuniAss = new ArrayList<ModelDenominazioniComuniAssociati>();
			listaDenominazioniWithComuniAss = entiGestoriAttiviService.getListaDenominazioniWithComuniAssociati();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETLISTADENOMINAZIONI + " nella classe " + EntiGestoriAttivi.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelDenominazioniComuniAssociati>>(listaDenominazioniWithComuniAss, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETLISTADENOMINAZIONI + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};	
	
	@RequestMapping(value= SEARCH_SCHEDE_ENTI_GESTORI, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getSchedeEntiGestori(@RequestBody(required = true) RicercaEntiGestori ricerca) throws Exception {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
	
		try {
			List<ModelRicercaEntiGestoriAttivi> schedeEntiGestori = new ArrayList<ModelRicercaEntiGestoriAttivi>();
			schedeEntiGestori = entiGestoriAttiviService.findSchedeEntiGestori(ricerca);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + EntiGestoriAttivi.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelRicercaEntiGestoriAttivi>>(schedeEntiGestori, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_SCHEDE + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};	
	
	@RequestMapping(value= GET_CRONOLOGIA, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getCronologiaEnte(@RequestParam(value = "idRendicontazione", required = true) Integer idRendicontazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<ModelCronologiaEnte> listaCronologia = new ArrayList<ModelCronologiaEnte>();
			listaCronologia = entiGestoriAttiviService.findCronologiaEnte(idRendicontazione);
			
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName());
			return new ResponseEntity<List<ModelCronologiaEnte>>(listaCronologia, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};
	
//	@RequestMapping(value= SEARCH_SCHEDE_MULTI_ENTI_GESTORI, method= RequestMethod.POST)
//	@ResponseBody
//	public ResponseEntity<?> getSchedeMultiEntiGestori(@Context HttpServletRequest httpRequest,@RequestBody(required = true) ArrayList<Integer> listaentiprofili) {
//		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SCHEDAMULTIENTEGESTORE + " nella classe " + EntiGestoriAttivi.class.getName());
//		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
//		String idApp = SharedConstants.COMPONENT_NAME;
//		String ipAddress = httpRequest.getRemoteAddr();
//		String utente = userInfo.getCodFisc();
//		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
//		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
//		
//		try {
//			List<ModelRicercaEntiGestoriAttivi> schedeEntiGestori = new ArrayList<ModelRicercaEntiGestoriAttivi>();
//			schedeEntiGestori = entiGestoriAttiviService.findSchedeMultiEntiGestori(listaentiprofili);
//
//			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, SharedConstants.KEY_OPER_OK);
//			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_SCHEDAMULTIENTEGESTORE + " nella classe " + EntiGestoriAttivi.class.getName());
//			return new ResponseEntity<List<ModelRicercaEntiGestoriAttivi>>(schedeEntiGestori, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, SharedConstants.KEY_OPER_INTERNAL_SERVER_ERROR + " " + e.getMessage());
//			log.debug("[Errore operazione ] " + SharedConstants.OPERAZIONE_SCHEDAMULTIENTEGESTORE + " nella classe " + EntiGestoriAttivi.class.getName());
//			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	};
	
//	@RequestMapping(value= GET_SCHEDA_ENTE_GESTORE, method= RequestMethod.GET)
//	@ResponseBody
//	public ResponseEntity<?> getSchedaEnteGestore(@Context HttpServletRequest httpRequest,@RequestParam(value="idEnte", required = true) Integer idEnte,@RequestParam(name = "ruolo", required = true) String ruolo) {
//		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETSCHEDAENTEGESTORE + " nella classe " + EntiGestoriAttivi.class.getName());
//		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
//		String idApp = SharedConstants.COMPONENT_NAME;
//		String ipAddress = httpRequest.getRemoteAddr();
//		String utente = userInfo.getCodFisc();
//		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
//		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
//		try {
//			if(!controlloService.checkSessionValid(userInfo)) {
//				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
//			}
//			ModelRicercaEntiGestoriAttivi schedaEnteGestore = entiGestoriAttiviService.findSchedaEnteGestore(idEnte);
//			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, SharedConstants.KEY_OPER_OK);
//			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_GETSCHEDAENTEGESTORE + " nella classe " + EntiGestoriAttivi.class.getName());
//			return new ResponseEntity<ModelRicercaEntiGestoriAttivi>(schedaEnteGestore, HttpStatus.OK);
//		} catch (SessionException e) {
//			e.printStackTrace();
//			GenericResponse errore = new GenericResponse();
//			errore.setId(null);
//			errore.setDescrizione(e.getMessage());
//			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
//			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
//			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, SharedConstants.KEY_OPER_INTERNAL_SERVER_ERROR + " " + e.getMessage());
//			log.debug("[Errore operazione ] " + SharedConstants.OPERAZIONE_GETSCHEDAENTEGESTORE + " nella classe " + EntiGestoriAttivi.class.getName());
//			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
	
	@RequestMapping(value= GET_STATI_ENTE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getStatiEnte() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETSTATIENTE + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelStatiEnte> listaStatoRendicontazione = new ArrayList<ModelStatiEnte>();
			listaStatoRendicontazione = entiGestoriAttiviService.findAllStatoEnte();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETSTATIENTE + " nella classe " + EntiGestoriAttivi.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<ModelStatiEnte>>(listaStatoRendicontazione, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETSTATIENTE + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};	
	
	@RequestMapping(value= GET_ANNI_ESERCIZIO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getAnniEsercizio() {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETANNIESERCIZIO + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();		
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<Integer> listaStatoRendicontazione = new ArrayList<Integer>();
			listaStatoRendicontazione = entiGestoriAttiviService.findAllAnnoEsercizio();
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETANNIESERCIZIO + " nella classe " + EntiGestoriAttivi.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<List<Integer>>(listaStatoRendicontazione, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETANNIESERCIZIO + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_STORICO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getStoricoEnte(@RequestParam(value = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			List<ModelStoricoEnte> listaStorico = new ArrayList<ModelStoricoEnte>();
			listaStorico = entiGestoriAttiviService.findStoricoEnte(idScheda);
			
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName());
			return new ResponseEntity<List<ModelStoricoEnte>>(listaStorico, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICERCA_CRONOLOGIA + " nella classe " + EntiGestoriAttivi.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= CREA_NUOVO_ANNO, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> creaNuovoAnno(@RequestBody ListaEntiAnno entiAnno) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		String input = "";
		for(ModelRicercaEntiGestoriAttivi o : entiAnno.getEnti()) {
			input+=entiAnno.getAnno() +","+o.getIdSchedaEnteGestore()+","+userInfo.getCodFisc()+";";
		}
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, input);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = entiGestoriAttiviService.creaNuovoAnno(entiAnno, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
//		catch(IntegritaException e) {
//			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
//			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
//			return controlloService.integritaFailedResponse(e);
//		}
		catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponseWarnErr errore = new GenericResponseWarnErr();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponseWarnErr>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value= CONCLUDI_RENDICONTAZIONE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> concludiRendicontazione(@RequestBody List<ModelRicercaEntiGestoriAttivi> enti) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		String input = "";
		for(ModelRicercaEntiGestoriAttivi o : enti) {
			input+=o.getRendicontazioni().get(0).getIdRendicontazioneEnte() +","+o.getIdSchedaEnteGestore()+","+userInfo.getCodFisc()+";";
		}
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, input);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = entiGestoriAttiviService.concludiRendicontazione(enti, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
//		catch(IntegritaException e) {
//			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
//			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
//			return controlloService.integritaFailedResponse(e);
//		}
		catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponseWarnErr errore = new GenericResponseWarnErr();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponseWarnErr>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= RIPRISTINA_RENDICONTAZIONE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> ripristinaRendicontazione(@RequestBody List<ModelRicercaEntiGestoriAttivi> enti) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		String input = "";
		for(ModelRicercaEntiGestoriAttivi o : enti) {
			input+=o.getRendicontazioni().get(0).getIdRendicontazioneEnte() +","+o.getIdSchedaEnteGestore()+","+userInfo.getCodFisc()+";";
		}
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, input);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = entiGestoriAttiviService.ripristinaRendicontazione(enti, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
//		catch(IntegritaException e) {
//			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
//			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
//			return controlloService.integritaFailedResponse(e);
//		}
		catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponseWarnErr errore = new GenericResponseWarnErr();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponseWarnErr>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= RIPRISTINA_COMPILAZIONE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> ripristinaCompilazione(@RequestBody List<ModelRicercaEntiGestoriAttivi> enti) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		String input = "";
		for(ModelRicercaEntiGestoriAttivi o : enti) {
			input+=o.getRendicontazioni().get(0).getIdRendicontazioneEnte() +","+o.getIdSchedaEnteGestore()+","+userInfo.getCodFisc()+";";
		}
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, input);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = entiGestoriAttiviService.ripristinaCompilazione(enti, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
//		catch(IntegritaException e) {
//			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
//			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
//			return controlloService.integritaFailedResponse(e);
//		}
		catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponseWarnErr errore = new GenericResponseWarnErr();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponseWarnErr>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= STORICIZZA_MULTIPLO, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> storicizzaMultiplo(@RequestBody List<ModelRicercaEntiGestoriAttivi> enti) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		String input = "";
		for(ModelRicercaEntiGestoriAttivi o : enti) {
			input+=o.getRendicontazioni().get(0).getIdRendicontazioneEnte() +","+o.getIdSchedaEnteGestore()+","+userInfo.getCodFisc()+";";
		}
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, input);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = entiGestoriAttiviService.storicizzaMultiplo(enti, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
//		catch(IntegritaException e) {
//			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
//			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
//			return controlloService.integritaFailedResponse(e);
//		}
		catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponseWarnErr errore = new GenericResponseWarnErr();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponseWarnErr>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
