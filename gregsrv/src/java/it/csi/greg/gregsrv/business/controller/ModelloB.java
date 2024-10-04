/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

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
import it.csi.greg.gregsrv.business.services.ModelloBService;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.EsportaModelloBInput;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelBMissioni;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModB;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.MODELLO_B)
public class ModelloB {
	
	/**Constants Mapping**/
	public static final String GET_MISSIONI_MOD_B = "/getMissioniModB";
	public static final String GET_RENDICONTAZIONE_MOD_B = "/getRendicontazioneModB";
	public static final String CAN_ACTIVE_MOD_B = "/canActiveModB";
	public static final String SAVE_MODELLO_B = "/saveModelloB";
	public static final String ESPORTA_MODELLO_B = "/esportaModelloB";
	public static final String CHECK_MODELLO_B = "/checkModelloB";
	public static final String ESPORTA_ISTAT = "/esportaIstat";

	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ModelloBService modelloBService;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= GET_MISSIONI_MOD_B, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getMissioniModB(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETMISSIONIMODB + " nella classe " + ModelloB.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			List<ModelBMissioni> dati = modelloBService.getMissioni();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETMISSIONIMODB + " nella classe " + ModelloB.class.getName());
			return new ResponseEntity<List<ModelBMissioni>>(dati, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETMISSIONIMODB + " nella classe " + ModelloB.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value= GET_RENDICONTAZIONE_MOD_B, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getRendicontazioneModB(@Context HttpServletRequest httpRequest, @RequestParam(name = "idScheda", required = true) Integer idScheda ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODB + " nella classe " + ModelloB.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			ModelRendicontazioneModB dati = modelloBService.getRendicontazioneModB(idScheda);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODB + " nella classe " + ModelloB.class.getName());
			return new ResponseEntity<ModelRendicontazioneModB>(dati, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODB + " nella classe " + ModelloB.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= CAN_ACTIVE_MOD_B, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> canActiveModB(@Context HttpServletRequest httpRequest, @RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CANACTIVEMODB + " nella classe " + ModelloB.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			Boolean canActiveModB = modelloBService.canActiveModB(idScheda);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CANACTIVEMODB + " nella classe " + ModelloB.class.getName());
			return new ResponseEntity<Boolean>(canActiveModB, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CANACTIVEMODB + " nella classe " + ModelloB.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= SAVE_MODELLO_B, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveModelloB(@Context HttpServletRequest httpRequest, @RequestBody(required = true) ModelRendicontazioneModB body,
			@RequestParam(name = "notaEnte", required = true) String notaEnte, @RequestParam(name = "notaInterna", required = true) String notaInterna) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SAVEMODELLOB + " nella classe " + ModelloB.class.getName());
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
			SaveModelloOutput response  = modelloBService.saveModelloB(body, userInfo, notaEnte, notaInterna);
			
			// Controllo Eventuali Importi Negativi per Warnings
			List<String> listaWarnings = modelloBService.controllaImportiModelloB(body);
						
			String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio().replace("oggettosalvo", "Modello B");
			response.setEsito("OK");
			response.setMessaggio(message);
			if(response.getWarnings() != null) response.getWarnings().addAll(listaWarnings); 
			else response.setWarnings(listaWarnings);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_SAVEMODELLOB + " nella classe " + ModelloB.class.getName());
			return new ResponseEntity<SaveModelloOutput>(response, HttpStatus.OK);		
		}	catch (SessionException e) {
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
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SAVEMODELLOB + " nella classe " + ModelloB.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= ESPORTA_MODELLO_B, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> esportaModelloB(@Context HttpServletRequest httpRequest, @RequestBody(required = true) EsportaModelloBInput body) {
		log.debug("[Inizio operazione esportazione excel Modello B ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOB + " nella classe " + ModelloB.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		
		try {
			String filexls = modelloBService.esportaModelloB(body);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione esportazione excel modello B ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOB + " nella classe " + ModelloB.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_ESPORTA).getTestoMessaggio().replace("oggettosalvo", "Modello B");
			EsportaModelliOutput response = new EsportaModelliOutput();
			response.setId("200");
			response.setDescrizione(errorMessage);
			response.setExcel(filexls);
			String dateorastring =LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")).toString();
			String nomefile = "ExportModelloB_" + dateorastring + ".xls";
			response.setMessaggio(nomefile);
			return new ResponseEntity<EsportaModelliOutput>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio().replace("SPECIFICARE", "B");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOB + " nella classe " + ModelloB.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value= CHECK_MODELLO_B, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> checkModelloB(@Context HttpServletRequest httpRequest, @RequestBody(required = true) Integer idRendicontazione ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CHECKMODELLOB + " nella classe " + DatiRendicontazione.class.getName());
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
			GenericResponseWarnErr response  = modelloBService.checkModelloB(idRendicontazione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CHECKMODELLOB + " nella classe " + DatiRendicontazione.class.getName());
			return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
		}	catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CHECKMODELLOB + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CHECKMODELLOB + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= ESPORTA_ISTAT, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> esportaIstat(@Context HttpServletRequest httpRequest, @RequestBody(required = true) EsportaModelloBInput body) {
		log.debug("[Inizio operazione esportazione excel Modello B ] " + SharedConstants.OPERAZIONE_ESPORTAISTAT + " nella classe " + ModelloB.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		
		try {
			String filexls = modelloBService.esportaIstat(body);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione esportazione excel istat ] " + SharedConstants.OPERAZIONE_ESPORTAISTAT + " nella classe " + ModelloB.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_ESPORTA).getTestoMessaggio().replace("oggettosalvo", "Istat");
			EsportaModelliOutput response = new EsportaModelliOutput();
			response.setId("200");
			response.setDescrizione(errorMessage);
			response.setExcel(filexls);
			String dateorastring =LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")).toString();
			String nomefile = "ExportIstat_" + dateorastring + ".xls";
			response.setMessaggio(nomefile);
			return new ResponseEntity<EsportaModelliOutput>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio().replace("SPECIFICARE", "B");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ESPORTAISTAT + " nella classe " + ModelloB.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
