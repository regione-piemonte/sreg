/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import it.csi.greg.gregsrv.business.services.ModelloAllDService;
import it.csi.greg.gregsrv.dto.EsportaModAllDInput;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModAllD;
import it.csi.greg.gregsrv.dto.ModelVociAllD;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.MODELLO_ALL_D)
public class ModelloAllD {
	
	/**Constants Mapping**/
	public static final String GET_VOCI_ALL_D = "/getVociAllD";
	public static final String GET_RENDICONTAZIONE_MOD_ALLD = "/getRendicontazioneModAllD";
	public static final String IS_MODULO_FNPS_COMPILED = "/compilabileModelloAllD";
	public static final String SAVE_MODELLO_ALLD = "/saveModelloAllD";
	public static final String ESPORTA_MODULO_FNPS = "/esportaModuloFnps";

	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ModelloAllDService modelloAllDService;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= GET_VOCI_ALL_D, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getPrestazioniModC(@Context HttpServletRequest httpRequest, @RequestParam(name = "idScheda", required = true) Integer idScheda ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETVOCIALLD + " nella classe " + ModelloAllD.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			ModelVociAllD dati = modelloAllDService.getVoci(idScheda);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETVOCIALLD + " nella classe " + ModelloAllD.class.getName());
			return new ResponseEntity<ModelVociAllD>(dati, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETVOCIALLD + " nella classe " + ModelloAllD.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@RequestMapping(value= GET_RENDICONTAZIONE_MOD_ALLD, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getRendicontazioneModAllDS(@Context HttpServletRequest httpRequest, @RequestParam(name = "idScheda", required = true) Integer idScheda ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODALLD + " nella classe " + ModelloAllD.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			ModelRendicontazioneModAllD dati = modelloAllDService.getRendicontazioneModAllD(idScheda);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODALLD + " nella classe " + ModelloAllD.class.getName());
			return new ResponseEntity<ModelRendicontazioneModAllD>(dati, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETRENDICONTAZIONEMODALLD + " nella classe " + ModelloAllD.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = IS_MODULO_FNPS_COMPILED, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> isModuloFnpsIsCompiled(@Context HttpServletRequest httpRequest,
			@RequestParam(name = "idScheda", required = true) Integer idScheda) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_ISMODULOFNPSCOMPILED
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
			Boolean is_compiled = modelloAllDService.controlloCompilazioneAllD(idScheda);
			
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_ISMODULOFNPSCOMPILED
					+ " nella classe " + ModelloAllD.class.getName());
			return new ResponseEntity<Boolean>(is_compiled, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper,
					SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ISMODULOFNPSCOMPILED
					+ " nella classe " + Macroaggregati.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@RequestMapping(value= SAVE_MODELLO_ALLD, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveModelloAllD(@Context HttpServletRequest httpRequest, @RequestBody(required = true) ModelRendicontazioneModAllD body,
			@RequestParam(name = "notaEnte", required = true) String notaEnte, @RequestParam(name = "notaInterna", required = true) String notaInterna) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SAVEMODELLOALLD + " nella classe " + ModelloAllD.class.getName());
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
			SaveModelloOutput response  = modelloAllDService.saveModelloAllD(body, userInfo, notaEnte, notaInterna);
			
//			 Controllo Eventuali Importi Negativi per Warnings
//			List<String> listaWarnings = modelloCService.controllaValoriModelloC(body);
						
			String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio().replace("oggettosalvo", "Modulo FNPS");
			response.setEsito("OK");
			response.setMessaggio(message);
//			if(response.getWarnings() != null) response.getWarnings().addAll(listaWarnings); 
//			else response.setWarnings(listaWarnings);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_SAVEMODELLOALLD + " nella classe " + ModelloAllD.class.getName());
			return new ResponseEntity<SaveModelloOutput>(response, HttpStatus.OK);		
		}	catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SAVEMODELLOALLD + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SAVEMODELLOALLD + " nella classe " + ModelloAllD.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@RequestMapping(value= ESPORTA_MODULO_FNPS, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> esportaModelloC(@Context HttpServletRequest httpRequest, @RequestBody(required = true) EsportaModAllDInput body) {
		log.debug("[Inizio operazione esportazione excel Modello C ] " + SharedConstants.OPERAZIONE_ESPORTAMODFNPS + " nella classe " + ModelloB.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		
		try {
			String filexls = modelloAllDService.esportaModuloFnps(body);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione esportazione excel modello C ] " + SharedConstants.OPERAZIONE_ESPORTAMODFNPS + " nella classe " + ModelloB.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_ESPORTA).getTestoMessaggio().replace("oggettosalvo", "Modulo FNPS");
			EsportaModelliOutput response = new EsportaModelliOutput();
			response.setId("200");
			response.setDescrizione(errorMessage);
			response.setExcel(filexls);
			String dateorastring =LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")).toString();
			String nomefile = "ExportModuloFNPS_" + dateorastring + ".xls";
			response.setMessaggio(nomefile);
			return new ResponseEntity<EsportaModelliOutput>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio().replace("SPECIFICARE", "C");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ESPORTAMODFNPS + " nella classe " + ModelloB.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
