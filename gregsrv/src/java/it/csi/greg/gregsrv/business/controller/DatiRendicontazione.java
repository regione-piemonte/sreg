/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.controller;

import java.util.ArrayList;
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

import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.services.AuditService;
import it.csi.greg.gregsrv.business.services.ControlloService;
import it.csi.greg.gregsrv.business.services.DatiRendicontazioneService;
import it.csi.greg.gregsrv.business.services.ListeService;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.IviaModelli;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.ModelProfilo;
import it.csi.greg.gregsrv.dto.ModelCronologiaEnte;
import it.csi.greg.gregsrv.dto.ModelCronologiaProfilo;
import it.csi.greg.gregsrv.dto.ModelLink;
import it.csi.greg.gregsrv.dto.ModelObbligatorio;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.SaveModelloAInput;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.SaveMotivazioneCheck;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.SessionException;
import it.csi.greg.gregsrv.filter.IrideIdAdapterFilter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.URIConstants;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(URIConstants.DATI_RENDICONTAZIONE)
public class DatiRendicontazione {
	
	/**Constants Mapping**/
	public static final String INVIA_TRANCHE = "/inviaTranche";
	public static final String GET_MODELLI_TRANCHE = "/getModelliTranche";
	public static final String CONFERMA_DATI_1 = "/confermaDati1";
	public static final String RICHIESTA_RETTIFICA_1 = "/richiestaRettifica1";
	public static final String CONFERMA_DATI_2 = "/confermaDati2";
	public static final String RICHIESTA_RETTIFICA_2 = "/richiestaRettifica2";
	public static final String GET_TRANCHE_MODELLO = "/getTranchePerModello";
	public static final String VALIDA = "/valida";
	public static final String STORICIZZA = "/storicizza";
	public static final String GET_MODELLI_ASSOCIATI = "/getModelliAssociati";//GET
	public static final String GET_MODELLI = "/getModelli";//GET
	public static final String GET_OBBLIGO_MODELLI = "/getAllObbligo";
	public static final String GET_VERIFICA_MODELLI_VUOTO = "/getVerificaModelliVuoto";
	public static final String SAVE_MOTIVAZIONE_CHECK = "/saveMotivazioneCheck";
	
	public static final String GET_INFO_RENDICONTAZIONE_OPERATORE = "/getInfoRendicontazioneOperatore";

	final static Logger log = Logger.getLogger(SharedConstants.GREG_ROOT_LOG_CATEGORY);
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected AuditService auditService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected ControlloService controlloService;
	@RequestMapping(value= GET_MODELLI_TRANCHE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getModelliPerTrache(@Context HttpServletRequest httpRequest, @RequestParam(name = "idRendicontazione", required = true) Integer idRendicontazione,
			  @RequestParam(name = "codProfilo", required = true) String codProfilo) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETMODELLOTRANCHE + " nella classe " + DatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			ArrayList<ModelLink> modelli = datiRendicontazioneService.getTabTrancheEnte(idRendicontazione,codProfilo);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETMODELLOTRANCHE + " nella classe " + DatiRendicontazione.class.getName());
			return new ResponseEntity<ArrayList<ModelLink>>(modelli, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETMODELLOTRANCHE + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

		
	@RequestMapping(value= INVIA_TRANCHE, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> invioTranche(@Context HttpServletRequest httpRequest, 
			@RequestBody(required = true) IviaModelli body) throws Exception{
		log.debug("[Inizio operazione controllo invio tranche ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		//TODO DA ELIMINARE//
		//CHECK ERRORE FE//
		System.out.println("UserInfo: " + userInfo.toString());
		System.out.println("Body->Tranche: " + body.getTranche());
		System.out.println("Body->idEnte: " + body.getIdEnte());
		/////////////////
		/////////////////
		
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp, ipAddress, utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		GenericResponse errore = new GenericResponse();
		//prendo i modelli
		ArrayList<ModelTabTranche> modelli = datiRendicontazioneService.findModelliAssociati(body.getIdEnte());
		String destranche = body.getTranche().equalsIgnoreCase(SharedConstants.TRANCHEII) ? SharedConstants.SECONDA_TRANCHE : SharedConstants.PRIMA_TRANCHE;
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			if (modelli.size()>0) {
				//prendo la rendicontazione
				GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
				String statoprec = rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione();
				//TODO DA ELIMINARE//
				//CHECK ERRORE FE//
				System.out.println("Controllo esiste GregDStatoRendicontazione: " + rendicontazione.getGregDStatoRendicontazione()==null?true:false);
				System.out.println("Codice stato rendicontazione: " + rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione());
				/////////////////
				/////////////////
				if (body.getOperazione().equalsIgnoreCase(SharedConstants.OPERAZIONE_INVIAMODELLI)) {
					//verifica se lo stato e' corretto
					if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I)
							//&& !userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) && body.getTranche().equalsIgnoreCase(SharedConstants.TRANCHEI)) {
									 && body.getProfilo().getListaazioni().get("InviaI")[1]) {
						//verifica modelli prima tranche
						GenericResponseWarnErr response = datiRendicontazioneService.attivaControlliModelloPerTranche(modelli,body.getTranche(),rendicontazione);
						auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
						log.debug("[Termine operazione controllo invia I tranche] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());
						return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);	
					}
					else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II) 
							//&& !userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) && body.getTranche().equalsIgnoreCase(SharedConstants.TRANCHEII)) {
							&& body.getProfilo().getListaazioni().get("InviaII")[1]) {
						//verifica modelli seconda tranche
						GenericResponseWarnErr response = datiRendicontazioneService.attivaControlliModelloPerTranche(modelli,body.getTranche(),rendicontazione);
						auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
						log.debug("[Termine operazione controllo invia II tranche] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());
						return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
					}
					else {
						//errore stato non valido per invia
						String errorstato = null;
						if ((!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I)
								 && !body.getProfilo().getListaazioni().get("InviaI")[1]))
//								|| (userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE)))
//								&& body.getTranche().equalsIgnoreCase(SharedConstants.TRANCHEI)) 
						{
							errorstato = listeService.getMessaggio(SharedConstants.ERROR_INVIO_STATO).getTestoMessaggio()
									.replace("TRANCHE", destranche)
									.replace("STATOREND", listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I).getDescStatoRendicontazione());
							log.debug("[Errore stato non valido Invia I tranche] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());
						}
						else if ((!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
								 && !body.getProfilo().getListaazioni().get("InviaII")[1]))
//								|| (userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE)))
//								&& body.getTranche().equalsIgnoreCase(SharedConstants.TRANCHEII))
								{
							
							errorstato = listeService.getMessaggio(SharedConstants.ERROR_INVIO_STATO).getTestoMessaggio()
									.replace("TRANCHE",  destranche)
									.replace("STATOREND", listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II).getDescStatoRendicontazione());
							log.debug("[Errore stato non valido Invia II tranche] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());
						}				
						errore.setId("400");
						errore.setDescrizione(errorstato);
						auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
						return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
					}
				}
				else if (body.getOperazione().equalsIgnoreCase(SharedConstants.OPERAZIONE_CONFERMAINVIAMODELLI)) {
					String errorstato = null;
					if (body.getTranche().equalsIgnoreCase(SharedConstants.TRANCHEI) && (!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I)
							 && !body.getProfilo().getListaazioni().get("InviaI")[1]))
//							|| (userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE)))
//							&& body.getTranche().equalsIgnoreCase(SharedConstants.TRANCHEI)) 
					{
						errorstato = listeService.getMessaggio(SharedConstants.ERROR_INVIO_STATO).getTestoMessaggio()
								.replace("TRANCHE", destranche)
								.replace("STATOREND", listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I).getDescStatoRendicontazione());
						log.debug("[Errore stato non valido Invia I tranche] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());
						errore.setId("400");
						errore.setDescrizione(errorstato);
						auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
						return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
					}
					else if (body.getTranche().equalsIgnoreCase(SharedConstants.TRANCHEII) && (!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
							 && !body.getProfilo().getListaazioni().get("InviaII")[1]))
//							|| (userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE)))
//							&& body.getTranche().equalsIgnoreCase(SharedConstants.TRANCHEII))
							{
						errorstato = listeService.getMessaggio(SharedConstants.ERROR_INVIO_STATO).getTestoMessaggio()
								.replace("TRANCHE",  destranche)
								.replace("STATOREND", listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II).getDescStatoRendicontazione());
						log.debug("[Errore stato non valido Invia II tranche] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());
						errore.setId("400");
						errore.setDescrizione(errorstato);
						auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
						return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
					}		
					GenericResponse response = datiRendicontazioneService.confermaInvioModelli(rendicontazione, userInfo, body.getNota(), body.getEsito(),body.getTranche(),body.getProfilo(), body.getModello());
					if (body.getEsito().equalsIgnoreCase("OK")) {
						String message = "";
						if (statoprec.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I)){
							message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_INVIA_CONFERMA_RETTIFICA_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
									.replace("OPERAZIONE", SharedConstants.OPERAZIONE_INVIA)
									.replace("STATOREND", listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I).getDescStatoRendicontazione());

						}
						else if (statoprec.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)){
							message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_INVIA_CONFERMA_RETTIFICA_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
									.replace("OPERAZIONE", SharedConstants.OPERAZIONE_INVIA)
									.replace("STATOREND", listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II).getDescStatoRendicontazione());
						}
						response.setDescrizione(message);
					}
					else {
						response.setDescrizione(null);
					}
					response.setId("OK");
					auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
					log.debug("[Termine operazione invia modelli] " + SharedConstants.OPERAZIONE_CONFERMAINVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());				
					return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);

				}
			}
			else {
				errore.setId("400");
				String errorMessage = listeService.getMessaggio(SharedConstants.ERROR_NO_MODELLO).getTestoMessaggio();
				errore.setDescrizione(errorMessage);
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
				log.debug("[Termine operazione invia modelli] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName());				
				return new ResponseEntity<GenericResponse>(errore, HttpStatus.BAD_REQUEST);
			}
		}catch (SessionException e) {
			e.printStackTrace();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERROR_INVIO_GENERICO).getTestoMessaggio().replace("SPECIFICARE", destranche);
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_INVIAMODELLI + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}
	
	
	
	@RequestMapping(value= CONFERMA_DATI_1, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> confermaDati1(@Context HttpServletRequest httpRequest, 
			@RequestParam(name = "idSchedaEnte", required = true) Integer idSchedaEnte,
			@RequestBody ModelCronologiaProfilo cronologiaprofilo) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CONFERMADATI1 + " nella classe " + DatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
				
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = datiRendicontazioneService.rendicontazioneConferma1(idSchedaEnte, cronologiaprofilo, userInfo);
			
			if(response.getId().equals(HttpStatus.BAD_REQUEST.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore stato non valido ] " + SharedConstants.OPERAZIONE_CONFERMADATI1 + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_CONFERMADATI1 + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else {
				String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_INVIA_CONFERMA_RETTIFICA_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
						.replace("OPERAZIONE", SharedConstants.OPERAZIONE_CONFERMADATI1)
						.replace("STATOREND", "'"+listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II).getDescStatoRendicontazione()+"'");
				response.setId("OK");
				response.setDescrizione(message);
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CONFERMADATI1 + " nella classe " + DatiRendicontazione.class.getName());				
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
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
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CONFERMADATI1 + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= RICHIESTA_RETTIFICA_1, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> richiestaRettifica1(@Context HttpServletRequest httpRequest, 
			@RequestParam(name = "idSchedaEnte", required = true) Integer idSchedaEnte,
			@RequestBody ModelCronologiaProfilo cronologiaprofilo) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA1 + " nella classe " + DatiRendicontazione.class.getName());
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
			GenericResponseWarnErr response = datiRendicontazioneService.rendicontazioneRettifica1(idSchedaEnte, cronologiaprofilo, userInfo);
			
			if(response.getId().equals(HttpStatus.BAD_REQUEST.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore stato non valido ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA1 + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA1 + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.INTERNAL_SERVER_ERROR);				
			}
			else {				
				String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_INVIA_CONFERMA_RETTIFICA_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
						.replace("OPERAZIONE", SharedConstants.OPERAZIONE_RICHIESTARETTIFICA1)
						.replace("STATOREND", "'"+listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I).getDescStatoRendicontazione()+"'");
				response.setId("OK");
				response.setDescrizione(message);
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA1 + " nella classe " + DatiRendicontazione.class.getName());				
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
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
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA1 + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= CONFERMA_DATI_2, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> confermaDati2(@Context HttpServletRequest httpRequest, @RequestParam(name = "idSchedaEnte", required = true) Integer idSchedaEnte,
			@RequestBody ModelCronologiaProfilo cronologiaprofilo) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_CONFERMADATI2 + " nella classe " + DatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
				
		try {
			if(!controlloService.checkSessionValid(userInfo)) {
				throw new SessionException(SharedConstants.MESSAGE_SESSION_DOWN);
			}
			GenericResponseWarnErr response = datiRendicontazioneService.rendicontazioneConferma2(idSchedaEnte, cronologiaprofilo, userInfo);
			
			if(response.getId().equals(HttpStatus.BAD_REQUEST.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore stato non valido ] " + SharedConstants.OPERAZIONE_CONFERMADATI2 + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_CONFERMADATI2 + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else {
				String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_INVIA_CONFERMA_RETTIFICA_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
						.replace("OPERAZIONE", SharedConstants.OPERAZIONE_CONFERMADATI2)
						.replace("STATOREND", "'"+listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE).getDescStatoRendicontazione()+"'");
				response.setId("OK");
				response.setDescrizione(message);
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_CONFERMADATI2 + " nella classe " + DatiRendicontazione.class.getName());				
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
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
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_CONFERMADATI2 + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= RICHIESTA_RETTIFICA_2, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> richiestaRettifica2(@Context HttpServletRequest httpRequest, @RequestParam(name = "idSchedaEnte", required = true) Integer idSchedaEnte,
			@RequestBody ModelCronologiaProfilo cronologiaprofilo) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA2 + " nella classe " + DatiRendicontazione.class.getName());
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
			GenericResponseWarnErr response = datiRendicontazioneService.rendicontazioneRettifica2(idSchedaEnte, cronologiaprofilo, userInfo);
			
			if(response.getId().equals(HttpStatus.BAD_REQUEST.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore stato non valido ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA2 + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.BAD_REQUEST);				
			}
			else if(response.getId().equals(HttpStatus.INTERNAL_SERVER_ERROR.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore Invio Mail ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA2 + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.INTERNAL_SERVER_ERROR);				
			}
			else {				
				String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_INVIA_CONFERMA_RETTIFICA_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
						.replace("OPERAZIONE", SharedConstants.OPERAZIONE_RICHIESTARETTIFICA2)
						.replace("STATOREND", "'"+listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II).getDescStatoRendicontazione()+"'");
				response.setId("OK");
				response.setDescrizione(message);
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA2 + " nella classe " + DatiRendicontazione.class.getName());				
				return new ResponseEntity<GenericResponseWarnErr>(response, HttpStatus.OK);
			}
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
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_RICHIESTARETTIFICA2 + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_INFO_RENDICONTAZIONE_OPERATORE, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getInfoRendicontazioneOperatore(@Context HttpServletRequest httpRequest, @RequestParam(name = "id_ente", required = true) Integer idEnte) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETINFORENDICONTAZIONEOPERATORE + " nella classe " + DatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idEnte);
			ModelRendicontazioneEnte info = new ModelRendicontazioneEnte(rendicontazione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETINFORENDICONTAZIONEOPERATORE + " nella classe " + DatiRendicontazione.class.getName());
			return new ResponseEntity<ModelRendicontazioneEnte>(info, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETINFORENDICONTAZIONEOPERATORE + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_TRANCHE_MODELLO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getTrachePerModello(@Context HttpServletRequest httpRequest, @RequestParam(name = "id_ente", required = true) Integer idEnte,@RequestParam(name = "modello", required = true) String modello) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_GETTRANCHEMODELLO + " nella classe " + DatiRendicontazione.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {					
			ModelTabTranche modelli = datiRendicontazioneService.getTranchePerModelloEnte(idEnte,modello);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_GETTRANCHEMODELLO + " nella classe " + DatiRendicontazione.class.getName());
			return new ResponseEntity<ModelTabTranche>(modelli, HttpStatus.OK);		
		}	
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_GETTRANCHEMODELLO + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= VALIDA, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> valida(@Context HttpServletRequest httpRequest, @RequestParam(name = "idSchedaEnte", required = true) Integer idSchedaEnte,
			 @RequestBody ModelCronologiaProfilo cronologiaprofilo) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_OPERAZIONEVALIDA + " nella classe " + DatiRendicontazione.class.getName());
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
			GenericResponse response = datiRendicontazioneService.rendicontazioneValida(idSchedaEnte,cronologiaprofilo, userInfo);
			
			if(response.getId().equals(HttpStatus.BAD_REQUEST.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore stato non valido ] " + SharedConstants.OPERAZIONE_OPERAZIONEVALIDA + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);				
			}
			else {				
				String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_INVIA_CONFERMA_RETTIFICA_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
						.replace("OPERAZIONE", SharedConstants.OPERAZIONE_OPERAZIONEVALIDA)
						.replace("STATOREND", "'"+listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA).getDescStatoRendicontazione()+"'");
				response.setId("OK");
				response.setDescrizione(message);
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_OPERAZIONEVALIDA + " nella classe " + DatiRendicontazione.class.getName());				
				return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
			}
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
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_OPERAZIONEVALIDA + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= STORICIZZA, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> storicizza(@Context HttpServletRequest httpRequest, @RequestParam(name = "idSchedaEnte", required = true) Integer idSchedaEnte,
			 @RequestBody ModelCronologiaProfilo cronologiaprofilo) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_OPERAZIONESTORICIZZA + " nella classe " + DatiRendicontazione.class.getName());
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
			GenericResponse response = datiRendicontazioneService.rendicontazioneStoricizza(idSchedaEnte, cronologiaprofilo, userInfo);
			
			if(response.getId().equals(HttpStatus.BAD_REQUEST.toString())) {
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Errore stato non valido ] " + SharedConstants.OPERAZIONE_OPERAZIONESTORICIZZA + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<GenericResponse>(response, HttpStatus.BAD_REQUEST);				
			}
			else {				
				String message = listeService.getMessaggio(SharedConstants.MESSAGE_OK_INVIA_CONFERMA_RETTIFICA_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
						.replace("OPERAZIONE", SharedConstants.OPERAZIONE_OPERAZIONESTORICIZZA)
						.replace("STATOREND", "'"+listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA).getDescStatoRendicontazione()+"'");
				response.setId("OK");
				response.setDescrizione(message);
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, response.getDescrizione());
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_OPERAZIONESTORICIZZA + " nella classe " + DatiRendicontazione.class.getName());				
				return new ResponseEntity<GenericResponse>(response, HttpStatus.OK);
			}
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
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_OPERAZIONESTORICIZZA + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= GET_MODELLI_ASSOCIATI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getModelliAssociati(@Context HttpServletRequest httpRequest,@RequestParam(name = "idRendicontazione", required = true) Integer idRendicontazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			ArrayList<ModelTabTranche> listaModelliAssociati = new ArrayList<ModelTabTranche>();
			listaModelliAssociati = datiRendicontazioneService.findModelliAssociati(idRendicontazione);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelTabTranche>>(listaModelliAssociati, HttpStatus.OK);
			
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
	
	@RequestMapping(value= GET_MODELLI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getModelli(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelTabTranche> listaModelliAssociati = new ArrayList<ModelTabTranche>();
			listaModelliAssociati = datiRendicontazioneService.findModelli();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelTabTranche>>(listaModelliAssociati, HttpStatus.OK);
			
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
	
	@RequestMapping(value= GET_OBBLIGO_MODELLI, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getObbligoModelli(@Context HttpServletRequest httpRequest) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_OBBLIGO_MODELLI + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();	
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			List<ModelObbligatorio> listaObbligo = new ArrayList<ModelObbligatorio>();
			listaObbligo = datiRendicontazioneService.findAllObbligo();
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_OBBLIGO_MODELLI + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<List<ModelObbligatorio>>(listaObbligo, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_OBBLIGO_MODELLI + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value= GET_VERIFICA_MODELLI_VUOTO, method= RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getVerificaModelliVuoto(@Context HttpServletRequest httpRequest,
			@RequestParam(name = "modello", required = true) String modello,
			@RequestParam(name = "idRendicontazione", required = true) Integer idRendicontazione) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_VERIFICA_MODELLI_VUOTO + " nella classe " + DatiEnte.class.getName());
		UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String idApp = SharedConstants.COMPONENT_NAME;
		String ipAddress = httpRequest.getRemoteAddr();
		String utente = userInfo.getCodFisc();
		String oggOper = new Object() {}.getClass().getEnclosingMethod().getName();
		GenericResponse ok = new GenericResponse();
		auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_REQUEST, null);
		
		try {			
			String condati = datiRendicontazioneService.controllaModelloVuoto(modello, idRendicontazione);
			ok.setDescrizione(condati);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_VERIFICA_MODELLI_VUOTO + " nella classe " + DatiEnte.class.getName());
			return new ResponseEntity<GenericResponse>(ok, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_READ, oggOper, SharedConstants.OPERAZIONE_RESPONSE, errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_VERIFICA_MODELLI_VUOTO + " nella classe " + DatiEnte.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= SAVE_MOTIVAZIONE_CHECK, method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> saveMotivazioneCheck(@Context HttpServletRequest httpRequest, @RequestBody(required = true) SaveMotivazioneCheck body ) {
		log.debug("[Inizio operazione ] " + SharedConstants.OPERAZIONE_SAVEMOTIVAZIONECHECK + " nella classe " + DatiRendicontazione.class.getName());
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
			SaveModelloOutput response = datiRendicontazioneService.saveMotivazioneCheck(body, userInfo);
			if (response.getEsito().equalsIgnoreCase("KO")) {
				response.setId("400");
				auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "KO");
				log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_SAVEMOTIVAZIONECHECK + " nella classe " + DatiRendicontazione.class.getName());
				return new ResponseEntity<SaveModelloOutput>(response, HttpStatus.BAD_REQUEST);	
			}
			else {
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE, "OK");
			log.debug("[Termine operazione ] " + SharedConstants.OPERAZIONE_SAVEMOTIVAZIONECHECK + " nella classe " + DatiRendicontazione.class.getName());
			return new ResponseEntity<SaveModelloOutput>(response, HttpStatus.OK);
			}
		}	catch (SessionException e) {
			e.printStackTrace();
			GenericResponse errore = new GenericResponse();
			errore.setId(null);
			errore.setDescrizione(e.getMessage());
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_UPDATE, oggOper, SharedConstants.OPERAZIONE_RESPONSE, e.getMessage());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SAVEMOTIVAZIONECHECK + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch (Exception e) {
			e.printStackTrace();
			String errorMessage = listeService.getMessaggio(SharedConstants.ERRORE_GENERICO).getTestoMessaggio();
			GenericResponse errore = new GenericResponse();
			errore.setId("500");
			errore.setDescrizione(errorMessage);		
			auditService.ScriviAudit(idApp,ipAddress,utente, SharedConstants.OPERAZIONE_INSERT, oggOper, SharedConstants.OPERAZIONE_RESPONSE,errore.getDescrizione());
			log.debug("[Errore generico ] " + SharedConstants.OPERAZIONE_SAVEMODELLOA + " nella classe " + DatiRendicontazione.class.getName() + " errore " + e.getMessage());
			return new ResponseEntity<GenericResponse>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}


