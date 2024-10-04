/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ControlloService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.business.services.ModelloAllontanamentoZeroService;
import it.csi.greg.gregsrv.dto.DatiAllontanamentoZeroCSV_DTO;
import it.csi.greg.gregsrv.dto.DatiAllontanamentoZeroDTO;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.FondiEnteAllontanamentoZero;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.PrestazioniAlZeroPerFnpsDTO;
import it.csi.greg.gregsrv.dto.ToggleValidazioneAllontanamentoZeroDTO;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.MODELLO_AL_ZERO)
public class ModelloAllontanamentoZero {
	
	/**Constants Mapping**/
	public static final String GET_TESTI_PRESTAZIONI_AL_ZERO = "/getTestiPrestazioniAlZero";
	public static final String CAN_ACTIVE_MOD_FNPS = "/canActiveModFnps";
	public static final String GET_FONDI_ENTE_AL_ZERO = "/getFondiEnteAlZero";
	public static final String TOGGLE_VALIDAZIONE_AL_ZERO = "/toggleValidazioneAlZero";
	public static final String GET_VALIDAZIONE_AL_ZERO = "/getValidazioneAlZero";
	public static final String SALVA_MODELLO_AL_ZERO = "/postModelloAlZero";
	public static final String GET_STATUS_MODELLO_AL_ZERO = "/getStatusModelloAlZero";
	public static final String MAKE_CSV_AL_ZERO = "/makeCsvAlZero";
	public static final String GET_PRESTAZIONI_ADATTAMENTO_FNPS = "/getPrestazioniAdattamentoFnps";
	
	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected ModelloAllontanamentoZeroService modelloAllontanamentoZeroService;
	@Autowired
	protected HttpServletRequest httpRequest;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= GET_TESTI_PRESTAZIONI_AL_ZERO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getTestiPrestazioniAlZero(@Context HttpServletRequest httpRequest, @RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_FIND_ALL_PRESTAZIONI_AL_ZERO);
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			DatiAllontanamentoZeroDTO dati = modelloAllontanamentoZeroService.findAllPrestazioniAllontanamentoZero(idScheda);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_FIND_ALL_PRESTAZIONI_AL_ZERO);
			return new ResponseEntity<DatiAllontanamentoZeroDTO>(dati, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODALLD + " nella classe " + ModelloAllontanamentoZero.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_FONDI_ENTE_AL_ZERO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getFondiEnteAlZero(@Context HttpServletRequest httpRequest, @RequestParam(name = "idEnte", required = true) BigInteger idEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GET_FONDI_ENTE_AL_ZERO);
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			FondiEnteAllontanamentoZero fondi = modelloAllontanamentoZeroService.findFondiEnteAllontanamentoZero(idEnte);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_FIND_ALL_PRESTAZIONI_AL_ZERO);
			return new ResponseEntity<FondiEnteAllontanamentoZero>(fondi, HttpStatus.OK);		
		}
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GET_FONDI_ENTE_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= TOGGLE_VALIDAZIONE_AL_ZERO, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> toggleValidazioneAlZero(@RequestBody(required = true) ToggleValidazioneAllontanamentoZeroDTO toggle) throws Exception {
		log.debug("[Inizio operazione ] " + SharedConstants.TOGGLE_VALIDAZIONE_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
	
		try {
			modelloAllontanamentoZeroService.toggleValidazioneAlZero(toggle, userInfo);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.TOGGLE_VALIDAZIONE_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.TOGGLE_VALIDAZIONE_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_VALIDAZIONE_AL_ZERO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getValidazioneAlZero(@Context HttpServletRequest httpRequest, @RequestParam(name = "idRendicontazioneEnte", required = true) Integer idRendicontazioneEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.GET_VALIDAZIONE_AL_ZERO);
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			boolean isValidato = modelloAllontanamentoZeroService.getValidazioneAlZero(idRendicontazioneEnte);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_FIND_ALL_PRESTAZIONI_AL_ZERO);
			return new ResponseEntity<Boolean>(isValidato, HttpStatus.OK);	
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GET_FONDI_ENTE_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value= CAN_ACTIVE_MOD_FNPS, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> canActiveModFnps(@Context HttpServletRequest httpRequest, @RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CANACTIVEMODFNPS + " nella classe " + ModelloAllontanamentoZero.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			Boolean canActiveModB = modelloAllontanamentoZeroService.canActiveModFnps(idScheda);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CANACTIVEMODFNPS + " nella classe " + ModelloAllontanamentoZero.class.getName());
			return new ResponseEntity<Boolean>(canActiveModB, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CANACTIVEMODFNPS + " nella classe " + ModelloAllontanamentoZero.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= SALVA_MODELLO_AL_ZERO, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> postModelloAlZero(@RequestBody(required = true) DatiAllontanamentoZeroDTO dati, @RequestParam(name = "idRendicontazioneEnte", required = true) Integer idRendicontazioneEnte) throws Exception {
		log.debug("[Inizio operazione ] " + SharedConstants.SALVA_MODELLO_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
	
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			
			modelloAllontanamentoZeroService.postModelloAlZero(dati, userInfo, idRendicontazioneEnte);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.SALVA_MODELLO_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.SALVA_MODELLO_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_STATUS_MODELLO_AL_ZERO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getStatusModelloAlZero(@Context HttpServletRequest httpRequest, @RequestParam(name = "idRendicontazioneEnte", required = true) Integer idRendicontazioneEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.GET_STATUS_MODELLO_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			String colore = modelloAllontanamentoZeroService.getStatusModelloAlZero(idRendicontazioneEnte);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.GET_STATUS_MODELLO_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName());
			return new ResponseEntity<String>(colore, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.GET_STATUS_MODELLO_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= MAKE_CSV_AL_ZERO, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> makeCsvAlZero(@RequestBody(required = true) DatiAllontanamentoZeroCSV_DTO dati, @RequestParam(name = "idRendicontazioneEnte", required = true) Integer idRendicontazioneEnte) throws Exception {
		log.debug("[Inizio operazione ] " + SharedConstants.MAKE_CSV_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
	
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			
			String filexls = modelloAllontanamentoZeroService.makeCsvAlZero(dati, idRendicontazioneEnte);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, null);
			log.debug("[Fine operazione ] " + SharedConstants.MAKE_CSV_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, HttpStatus.OK.toString());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_ESPORTA).getTestoMessaggio().replace("oggettosalvo", "Modulo ALZERO");
			EsportaModelliOutput response = new EsportaModelliOutput();
			response.setId("200");
			response.setDescrizione(errorMessage);
			response.setExcel(filexls);
			String dateorastring =LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")).toString();
			String nomefile = "ExportModuloAlZero_" + dateorastring + ".xls";
			response.setMessaggio(nomefile);
			return new ResponseEntity<EsportaModelliOutput>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.MAKE_CSV_AL_ZERO + " nella classe " + ModelloAllontanamentoZero.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_PRESTAZIONI_ADATTAMENTO_FNPS, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioniAdattamentoFnps(@Context HttpServletRequest httpRequest, @RequestParam(name = "idRendicontazioneEnte", required = true) Integer idRendicontazioneEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_FIND_ALL_PRESTAZIONI_AL_ZERO);
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			List<PrestazioniAlZeroPerFnpsDTO> dati = modelloAllontanamentoZeroService.getPrestazioniAdattamentoFnps(idRendicontazioneEnte);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.GET_PRESTAZIONI_ADATTAMENTO_FNPS);
			return new ResponseEntity<List<PrestazioniAlZeroPerFnpsDTO>>(dati, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.GET_PRESTAZIONI_ADATTAMENTO_FNPS + " nella classe " + ModelloAllontanamentoZero.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
