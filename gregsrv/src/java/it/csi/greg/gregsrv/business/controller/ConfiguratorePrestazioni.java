/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;

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

import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ConfiguratorePrestazioniService;
import it.csi.greg.gregsrv.business.services.ControlloService;
import it.csi.greg.gregsrv.business.services.DatiRendicontazioneService;
import it.csi.greg.gregsrv.business.services.EntiGestoriAttiviService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelDatiAnagraficiToSave;
import it.csi.greg.gregsrv.dto.ModelDettaglioPrestRegionale;
import it.csi.greg.gregsrv.dto.ModelDettaglioPrestazConfiguratore;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;
import it.csi.greg.gregsrv.dto.ModelNomenclatore;
import it.csi.greg.gregsrv.dto.ModelPrest1Prest2;
import it.csi.greg.gregsrv.dto.ModelPrest1PrestMin;
import it.csi.greg.gregsrv.dto.ModelPrestUtenza;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.CONFIGURATORE)
public class ConfiguratorePrestazioni {	
	
	/**Constants Mapping**/
	public static final String GET_PRESTAZIONI = "/getPrestazioni";
	public static final String GET_PRESTAZIONE_REGIONALE = "/getPrestazioneRegionale1";
	public static final String GET_TIPOLOGIE = "/getTipologie";
	public static final String GET_STRUTTURE = "/getStrutture";
	public static final String GET_QUOTE = "/getQuote";
	public static final String GET_PRESTCOLL = "/getPrestColl";
	public static final String GET_MACROAGGREGATI = "/getMacroaggregati";
	public static final String GET_UTENZE = "/getUtenze";
	public static final String GET_MISSIONEPROG = "/getMissioneProg";
	public static final String GET_SPESE = "/getSpese";
	public static final String GET_PRESTAZIONI2 = "/getPrestazioni2";
	public static final String GET_PRESTAZIONIMIN = "/getPrestazioniMin";
	public static final String GET_PRESTAZIONIISTAT = "/getIstatConf";
	public static final String GET_UTENZEISTAT = "/getUtenzeIstatConf";
	public static final String GET_NOMENCLATORE = "/getNomenclatoreConf";
	public static final String SAVE_PRESTAZIONE = "/savePrestazione";
	public static final String SAVE_PRESTAZIONE2 = "/savePrestazione2";
	public static final String MODIFICA_PRESTAZIONE2 = "/modificaPrestazione2";
	public static final String MODIFICA_PRESTAZIONE = "/modificaPrestazione";
	public static final String GET_PRESTAZIONE_REGIONALE2 = "/getPrestazioneRegionale2";
	public static final String GET_UTENZEISTAT_TRANS = "/getUtenzeIstatTransConf";
	
	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	
	/**Dependency - Injection**/
	@Autowired
	protected EntiGestoriAttiviService entiGestoriAttiviService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected ConfiguratorePrestazioniService configuratorePrestazioniService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected HttpServletRequest httpRequest;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= GET_PRESTAZIONI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioni(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelDettaglioPrestazConfiguratore> listaPrestazioniReg1 = new ArrayList<ModelDettaglioPrestazConfiguratore>();
			listaPrestazioniReg1 = configuratorePrestazioniService.findPrestazioniRegionali1();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelDettaglioPrestazConfiguratore>>(listaPrestazioniReg1, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	@RequestMapping(value=GET_PRESTAZIONE_REGIONALE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioneRegionale(@RequestParam(name = "idPrestazione", required = true) Integer idPrestazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETPRESTAZIONEREGIONALE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ModelDettaglioPrestazConfiguratore dettPrest = configuratorePrestazioniService.getPrestazioneRegionale(idPrestazione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETPRESTAZIONEREGIONALE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<ModelDettaglioPrestazConfiguratore>(dettPrest, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETPRESTAZIONEREGIONALE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	@RequestMapping(value= GET_TIPOLOGIE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getTipologie(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaTipologie = new ArrayList<ModelListeConfiguratore>();
			listaTipologie = configuratorePrestazioniService.findTipologie();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaTipologie, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_STRUTTURE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getStrutture(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaStrutture = new ArrayList<ModelListeConfiguratore>();
			listaStrutture = configuratorePrestazioniService.findStrutture();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaStrutture, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_QUOTE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getQuote(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaQuote = new ArrayList<ModelListeConfiguratore>();
			listaQuote = configuratorePrestazioniService.findQuote();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaQuote, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PRESTCOLL, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestColl(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaPrestCollegate = new ArrayList<ModelListeConfiguratore>();
			listaPrestCollegate = configuratorePrestazioniService.findPrestazioniColl();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaPrestCollegate, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_MACROAGGREGATI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMacroaggregati(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaMacroaggregati = new ArrayList<ModelListeConfiguratore>();
			listaMacroaggregati = configuratorePrestazioniService.findMacroaggregati();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_UTENZE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getUtenze(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaMacroaggregati = new ArrayList<ModelListeConfiguratore>();
			listaMacroaggregati = configuratorePrestazioniService.findUtenza();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_MISSIONEPROG, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMissioneProg(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaMacroaggregati = new ArrayList<ModelListeConfiguratore>();
			listaMacroaggregati = configuratorePrestazioniService.findMissioneProg();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_SPESE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSpese(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaMacroaggregati = new ArrayList<ModelListeConfiguratore>();
			listaMacroaggregati = configuratorePrestazioniService.findSpesa();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PRESTAZIONI2, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioni2(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelPrest1Prest2> listaMacroaggregati = new ArrayList<ModelPrest1Prest2>();
			listaMacroaggregati = configuratorePrestazioniService.findPrestazioniRegionali2();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelPrest1Prest2>>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PRESTAZIONIMIN, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioniMin(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelPrest1PrestMin> listaMacroaggregati = new ArrayList<ModelPrest1PrestMin>();
			listaMacroaggregati = configuratorePrestazioniService.findPrestazioniMinisteriali();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelPrest1PrestMin>>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PRESTAZIONIISTAT, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioniIstat(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaMacroaggregati = new ArrayList<ModelListeConfiguratore>();
			listaMacroaggregati = configuratorePrestazioniService.findPrestIstat();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_UTENZEISTAT, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getUtenzeIstat(@RequestBody List<ModelPrestUtenza> utenze) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelListeConfiguratore> listaMacroaggregati = new ArrayList<ModelListeConfiguratore>();
			listaMacroaggregati = configuratorePrestazioniService.findUtenzaIstat(utenze);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelListeConfiguratore>>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_NOMENCLATORE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getNomenclatore(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelNomenclatore> listaMacroaggregati = new ArrayList<ModelNomenclatore>();
			listaMacroaggregati = configuratorePrestazioniService.findNomenclatore();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelNomenclatore>>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= SAVE_PRESTAZIONE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> savePrestazione(@RequestBody ModelDettaglioPrestazConfiguratore prestazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = configuratorePrestazioniService.savePrestazione(prestazione, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
		catch(IntegritaException e) {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
			return controlloService.integritaFailedResponse(e);
		}catch (SessionException e) {
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
	
	@RequestMapping(value= MODIFICA_PRESTAZIONE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> modificaPrestazione(@RequestBody ModelDettaglioPrestazConfiguratore prestazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = configuratorePrestazioniService.modificaPrestazione(prestazione, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
		catch(IntegritaException e) {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
			return controlloService.integritaFailedResponse(e);
		}catch (SessionException e) {
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
	
	@RequestMapping(value= SAVE_PRESTAZIONE2, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> savePrestazione2(@RequestBody ModelPrest1Prest2 prestazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = configuratorePrestazioniService.savePrestazione2(prestazione, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
		catch(IntegritaException e) {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
			return controlloService.integritaFailedResponse(e);
		}catch (SessionException e) {
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
	
	@RequestMapping(value= MODIFICA_PRESTAZIONE2, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> modificaPrestazione2(@RequestBody ModelPrest1Prest2 prestazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = configuratorePrestazioniService.modificaPrestazione2(prestazione, userInfo);
			if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else {
				auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
				log.debug("[Termine operazione salvataggio Ente ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
		} 
		catch(IntegritaException e) {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
			log.debug("[Errore anno contabile ] " + SharedConstants.OPERAZIONE_SALVAENTE + " nella classe " + DatiEnte.class.getName());
			return controlloService.integritaFailedResponse(e);
		}catch (SessionException e) {
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
	
	@RequestMapping(value=GET_PRESTAZIONE_REGIONALE2, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioneRegionale2(@RequestParam(name = "idPrestazione", required = true) Integer idPrestazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETPRESTAZIONEREGIONALE + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ModelPrest1Prest2 dettPrest = configuratorePrestazioniService.findPrestazioneRegionale2(idPrestazione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETPRESTAZIONEREGIONALE + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<ModelPrest1Prest2>(dettPrest, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETPRESTAZIONEREGIONALE + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_UTENZEISTAT_TRANS, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getUtenzeIstatTransConf(@RequestParam(name = "codUtenza", required = true) String codUtenza) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ModelListeConfiguratore listaMacroaggregati = new ModelListeConfiguratore();
			listaMacroaggregati = configuratorePrestazioniService.findUtenzaIstatForTrans(codUtenza);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<ModelListeConfiguratore>(listaMacroaggregati, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
