/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.business.services.MacroaggregatiService;
import it.csi.greg.gregsrv.dto.EsportaMacroaggregatiInput;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliSpeseMissioni;
import it.csi.greg.gregsrv.dto.ModelSpesaMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelSpesaMissione;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.MACROAGGREGATI)
public class Macroaggregati {

	/** Constants Mapping **/
	public static final String GET_MACROAGGREGATO = "/getMacroaggregati";
	public static final String GET_SPESA_MISSIONE = "/getSpesaMissione";
	public static final String GET_R_SPESA_MACRO = "/getRSpesaMacro";
	public static final String GET_RENDICONTAZIONE_SPESA_MACRO_BY_ID_SCHEDA = "/getRendicontazioneMacroaggregatiByIdScheda";
	public static final String SAVE_MACROAGGREGATI = "/saveMacroaggregati";	
	public static final String GET_RENDICONTAZIONE_TOTALE_SPESA_PER_MOD_B = "/getRendicontazioneTotaliSpese";
	public static final String GET_RENDICONTAZIONE_TOTALE_MACROAGGREGATI_PER_MOD_B1 = "/getRendicontazioneTotaliMacroaggregati";
	public static final String IS_RENDICONTAZIONE_TOTALE_MACROAGGREGATI_PER_MOD_B1_COMPILED = "/isRendicontazioneTotaliMacroaggregatiCompiled";
	public static final String ESPORTA_MACROAGGREGATI = "/esportaMacroaggregati";
	public static final String CHECK_MACROAGGREGATI = "/checkMacroaggregati";
	
	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected MacroaggregatiService macroaggregatiService;

	@RequestMapping(value = GET_MACROAGGREGATO, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMacroaggregato(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETMACROAGGREGATO + " nella classe "
				+ Macroaggregati.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelMacroaggregati> macroaggregati = macroaggregatiService.getMacroaggregati();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETMACROAGGREGATO + " nella classe "
					+ Macroaggregati.class.getName());
			return new ResponseEntity<List<ModelMacroaggregati>>(macroaggregati, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETMACROAGGREGATO + " nella classe "
					+ Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = GET_SPESA_MISSIONE, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getSpesaMissione(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETSPESAMISSIONE + " nella classe "
				+ Macroaggregati.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelSpesaMissione> spesaMissione = macroaggregatiService.getSpesaMissione();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETSPESAMISSIONE + " nella classe "
					+ Macroaggregati.class.getName());
			return new ResponseEntity<List<ModelSpesaMissione>>(spesaMissione, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETSPESAMISSIONE + " nella classe "
					+ Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = GET_R_SPESA_MACRO, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getRSpesaMacro(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETRSPESAMACRO + " nella classe "
				+ Macroaggregati.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			List<ModelSpesaMacroaggregati> spesaMacro = macroaggregatiService.getRSpesaMacro();
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETRSPESAMACRO + " nella classe "
					+ Macroaggregati.class.getName());
			return new ResponseEntity<List<ModelSpesaMacroaggregati>>(spesaMacro, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETRSPESAMACRO + " nella classe "
					+ Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = GET_RENDICONTAZIONE_SPESA_MACRO_BY_ID_SCHEDA, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getRendicontazioneSpesaMacroByIdScheda(@Context HttpServletRequest httpRequest,
			@RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONESPESAMACROBYIDSCHEDA
				+ " nella classe " + Macroaggregati.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			ModelRendicontazioneMacroaggregati rendicontazioneEnte = macroaggregatiService
					.getRendicontazioneMacroaggregatiByIdScheda(idScheda);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONESPESAMACROBYIDSCHEDA
					+ " nella classe " + Macroaggregati.class.getName());
			return new ResponseEntity<ModelRendicontazioneMacroaggregati>(rendicontazioneEnte, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONESPESAMACROBYIDSCHEDA
					+ " nella classe " + Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= SAVE_MACROAGGREGATI, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveMacroaggregati(@Context HttpServletRequest httpRequest, @RequestBody(required = true) ModelRendicontazioneMacroaggregati body,
			@RequestParam(name = "notaEnte", required = true) String notaEnte, @RequestParam(name = "notaInterna", required = true) String notaInterna) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SAVEMACROAGGREGATI + " nella classe " + Macroaggregati.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
	
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			// Effettuo il salvataggio dei dati del Macroaggregati
			//SaveMacroaggregatiOutput salvaMacroaggregati = new SaveMacroaggregatiOutput();	
			SaveModelloOutput salvaMacroaggregati = macroaggregatiService.saveMacroaggregati(body, userInfo, notaEnte, notaInterna);
			
			String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_SALVA_MACROAGGREGATI).getTestoMessaggio().replace("oggettosalvo", "Modello Macroaggregati");
			salvaMacroaggregati.setEsito("OK");
			salvaMacroaggregati.setMessaggio(message);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_SAVEMACROAGGREGATI + " nella classe " + Macroaggregati.class.getName());
			return new ResponseEntity<SaveModelloOutput>(salvaMacroaggregati, HttpStatus.OK);
		}
		catch(IntegritaException e) {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
			log.debug("[Errore valore numerico ] " + SharedConstants.OPERAZIONE_SAVEMACROAGGREGATI + " nella classe " + Macroaggregati.class.getName());
			return controlloService.integritaFailedResponse(e);
		}catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SAVEMACROAGGREGATI + " nella classe " + Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

	
	@RequestMapping(value = GET_RENDICONTAZIONE_TOTALE_SPESA_PER_MOD_B, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getRendicontazioneTotaleSpese(@Context HttpServletRequest httpRequest,
			@RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONETOTALESPESAPERMODB
				+ " nella classe " + Macroaggregati.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			ModelRendicontazioneTotaliSpeseMissioni rendicontazioneEnte = macroaggregatiService
					.getRendicontazioneTotaliSpesePerB(idScheda);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONETOTALESPESAPERMODB
					+ " nella classe " + Macroaggregati.class.getName());
			return new ResponseEntity<ModelRendicontazioneTotaliSpeseMissioni>(rendicontazioneEnte, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONETOTALESPESAPERMODB
					+ " nella classe " + Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = GET_RENDICONTAZIONE_TOTALE_MACROAGGREGATI_PER_MOD_B1, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getRendicontazioneTotaleMacroaggregati(@Context HttpServletRequest httpRequest,
			@RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONETOTALEMACROAGGREGATIPERMODB1
				+ " nella classe " + Macroaggregati.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			ModelRendicontazioneTotaliMacroaggregati rendicontazioneEnte = macroaggregatiService
					.getRendicontazioneTotaliMacroaggregatiPerB1(idScheda);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONETOTALEMACROAGGREGATIPERMODB1
					+ " nella classe " + Macroaggregati.class.getName());
			return new ResponseEntity<ModelRendicontazioneTotaliMacroaggregati>(rendicontazioneEnte, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONETOTALEMACROAGGREGATIPERMODB1
					+ " nella classe " + Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = IS_RENDICONTAZIONE_TOTALE_MACROAGGREGATI_PER_MOD_B1_COMPILED, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> isRendicontazioneTotaleMacroaggregatiCompiled(@Context HttpServletRequest httpRequest,
			@RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_ISRENDICONTAZIONESPESAMACROCOMPILEDBYIDSCHEDA
				+ " nella classe " + Macroaggregati.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {
		}.getClass().getEnclosingMethod().getName();
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
				SharedConstants.OPERAZIONE_REQUEST, null);

		try {
			Boolean is_compiled = macroaggregatiService.isRendicontazioneMacroaggregatiCompiledByIdScheda(idScheda);
			
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ISRENDICONTAZIONESPESAMACROCOMPILEDBYIDSCHEDA
					+ " nella classe " + Macroaggregati.class.getName());
			return new ResponseEntity<Boolean>(is_compiled, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ISRENDICONTAZIONESPESAMACROCOMPILEDBYIDSCHEDA
					+ " nella classe " + Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= ESPORTA_MACROAGGREGATI, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> esportaMacroaggregati(@Context HttpServletRequest httpRequest, 
			@RequestBody(required = true) EsportaMacroaggregatiInput body) {
		log.debug("[Inizio operazione esportazione excel Modello Macroaggregati ] " + SharedConstants.OPERAZIONE_ESPORTAMACROAGGREGATI + " nella classe " + Macroaggregati.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		try {
				//salva tutti i dati
			String filexls = macroaggregatiService.esportaMacroaggregati(body);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione esportazione excel modello Macroaggregati ] " + SharedConstants.OPERAZIONE_ESPORTAMACROAGGREGATI + " nella classe " + Macroaggregati.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_ESPORTA).getTestoMessaggio().replace("oggettosalvo", "Macroaggregati");
			EsportaModelliOutput response = new EsportaModelliOutput();
			response.setId("200");
			response.setDescrizione(errorMessage);
			response.setExcel(filexls);
			String dateorastring =LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")).toString();
			String nomefile = "ExportMacroaggregati_" + dateorastring + ".xls";
			response.setMessaggio(nomefile);
			return new ResponseEntity<EsportaModelliOutput>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio().replace("SPECIFICARE", "Macroaggregati");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ESPORTAMACROAGGREGATI + " nella classe " + Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= CHECK_MACROAGGREGATI, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> checkMacroaggregati(@Context HttpServletRequest httpRequest, @RequestBody(required = true) Integer idRendicontazione ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CHECKMACROAGGREGATI + " nella classe " + DatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {	
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response  = macroaggregatiService.checkMacroaggregati(idRendicontazione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CHECKMACROAGGREGATI + " nella classe " + DatiRendicontazione.class.getName());
			return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
		}	catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CHECKMACROAGGREGATI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CHECKMACROAGGREGATI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
