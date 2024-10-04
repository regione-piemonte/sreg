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
import it.csi.greg.gregsrv.business.services.ModelloA1Service;
import it.csi.greg.gregsrv.dto.EsportaModelliOutput;
import it.csi.greg.gregsrv.dto.EsportaModelloA1Input;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelDatiA1;
import it.csi.greg.gregsrv.dto.ModelVociA1;
import it.csi.greg.gregsrv.dto.SaveModelloA1Input;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.MODELLO_A1)
public class ModelloA1 {
	
	/**Constants Mapping**/
	public static final String GET_VOCI_A1 = "/getVociModelloA1";
	public static final String GET_DATI_A1 = "/getDatiModelloA1";
	public static final String SAVE_MODELLO_A1 = "/saveModelloA1";
	public static final String ESPORTA_MODELLO_A1 = "/esportaModelloA1";
	public static final String CHECK_MODELLO_A1 = "/checkModelloA1";

	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected ModelloA1Service modelloA1Service;
	@Autowired
	protected ControlloService controlloService;
	
	@RequestMapping(value= GET_VOCI_A1, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getVociA1(@Context HttpServletRequest httpRequest, @RequestParam(name = "id_ente", required = true) Integer idEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETVOCIA1 + " nella classe " + ModelloA1.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			List<ModelVociA1> vocia1 = modelloA1Service.getIntestazioniModA1(idEnte);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETVOCIA1 + " nella classe " + ModelloA1.class.getName());
			return new ResponseEntity<List<ModelVociA1>>(vocia1, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETVOCIA1 + " nella classe " + ModelloA1.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_DATI_A1, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getDatiA1(@Context HttpServletRequest httpRequest, @RequestParam(name = "id_ente", required = true) Integer idEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETDATIA1 + " nella classe " + ModelloA1.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			List<ModelDatiA1> datia1 = modelloA1Service.getDatiModelloA1(idEnte);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETDATIA1 + " nella classe " + ModelloA1.class.getName());
			return new ResponseEntity<List<ModelDatiA1>>(datia1, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETDATIA1 + " nella classe " + ModelloA1.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value= SAVE_MODELLO_A1, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveModelloA1(@Context HttpServletRequest httpRequest, 
			@RequestBody(required = true) SaveModelloA1Input body) throws Exception {
		log.debug("[Inizio operazione salvataggio Modello A1 ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA1 + " nella classe " + ModelloA1.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			//se il totale si discosta dalla somma errore
			if (!modelloA1Service.controllaImportoModA1(body.getDatiA1())) {
				String errortotale = listeService.getMessaggio(SharedConstants.ERROR_TOTALE).getTestoMessaggio();
				errortotale = errortotale.replace("SPECIFICARE", "Totale importi quota " + SharedConstants.OPERAZIONE_GETVOCIA1_I2 + " non corretto");
				errore.setId("400");
				errore.setDescrizione(errortotale);	
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
				log.debug("[Errore totale ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA1 + " nella classe " + ModelloA1.class.getName());
				return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
			}
			//verifica se ente deve esserci la nota 
			if(body.getProfilo() != null && body.getProfilo().getListaazioni().get("CronologiaRegionale")[1] 
					&& body.getProfilo().getListaazioni().get("CronologiaRegionale")[0] && (body.getCronologia().getNotaEnte()==null || body.getCronologia().getNotaEnte().equals(""))) {
			//if ((body.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || body.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE)) && body.getCronologia().getNotaEnte()==null){
				//manca la nota per l'ente
				String errornota = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE).getTestoMessaggio();
				errore.setId("400");
				errore.setDescrizione(errornota);	
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
				log.debug("[Errore manca nota ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA1 + " nella classe " + ModelloA1.class.getName());
				return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
			}
				//salva tutti i dati
			SaveModelloOutput response = modelloA1Service.saveModelloA1(body, userInfo);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione salva modello A1 ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA1 + " nella classe " + ModelloA1.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_ENTE_SALVA).getTestoMessaggio().replace("oggettosalvo", "Modello A1");
			response.setId("200");
			response.setDescrizione(errorMessage);
			return new ResponseEntity<SaveModelloOutput>(response, HttpStatus.OK);
		} catch (SessionException e) {
			e.printStackTrace();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch(IntegritaException e) {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getDescrizioneMsg());
			log.debug("[Errore valore numerico ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA1 + " nella classe " + ModelloA1.class.getName());
			return controlloService.integritaFailedResponse(e);	
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_SALVATAGGIO_MODELLO).getTestoMessaggio().replace("SPECIFICARE", "A1");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA1 + " nella classe " + ModelloA1.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value= ESPORTA_MODELLO_A1, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> esportaModelloA1(@Context HttpServletRequest httpRequest, 
			@RequestBody(required = true) EsportaModelloA1Input body) {
		log.debug("[Inizio operazione esportazione excel Modello A1 ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOA1 + " nella classe " + ModelloA1.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		try {
				//salva tutti i dati
			String filexls = modelloA1Service.esportaModelloA1(body);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Termine operazione esportazione excel modello A1 ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOA1 + " nella classe " + ModelloA1.class.getName());
			String errorMessage = listeService.getMessaggio(SharedConstants.MESSAGE_OK_MODELLO_ESPORTA).getTestoMessaggio().replace("oggettosalvo", "Modello A1");
			EsportaModelliOutput response = new EsportaModelliOutput();
			response.setId("200");
			response.setDescrizione(errorMessage);
			response.setExcel(filexls);
			String dateorastring =LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmm")).toString();
			String nomefile = "ExportModelloA1_" + dateorastring + ".xls";
			response.setMessaggio(nomefile);
			return new ResponseEntity<EsportaModelliOutput>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio().replace("SPECIFICARE", "A1");
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_ESPORTAMODELLOA1 + " nella classe " + ModelloA1.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= CHECK_MODELLO_A1, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> checkModelloA1(@Context HttpServletRequest httpRequest, @RequestBody(required = true) Integer idRendicontazione ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CHECKMODELLOA1 + " nella classe " + DatiRendicontazione.class.getName());
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
			GenericResponseWarnErr response  = modelloA1Service.checkModelloA1(idRendicontazione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CHECKMODELLOA1 + " nella classe " + DatiRendicontazione.class.getName());
			return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
		}	catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CHECKMODELLOA1 + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CHECKMODELLOA1 + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
