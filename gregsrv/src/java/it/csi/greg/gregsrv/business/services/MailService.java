/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;


import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.greg.gregsrv.business.dao.impl.MailDao;
import it.csi.greg.gregsrv.business.entity.GregDMail;
import it.csi.greg.gregsrv.business.entity.GregRAzioneMail;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelReferenteEnte;
import it.csi.greg.gregsrv.dto.ModelResponsabileEnte;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.SharedConstants;

@Service("mailService")
public class MailService {
	
	@Autowired
	protected MailDao mailDao;
	@Autowired
	protected ListeService listeService;
	
	public GregDMail getMailByCod(String codMail) {
		return mailDao.getMailByCod(codMail);
	}
	
	public Integer sendEmailByText(String sendFrom, String sendTo, String subjectMail, String textMail) {
		return mailDao.sendEmailByText(sendFrom, sendTo, subjectMail, textMail);
	}
	
	public Integer sendEmailByHtmlBody(String sendFrom, String sendTo, String subjectMail, String htmlBody) {
		return mailDao.sendEmailByHtmlBody(sendFrom, sendTo, subjectMail, htmlBody);
	}
	
	public Integer sendEmailByTextHtmlBody(String sendFrom, String sendTo, String subjectMail, String textMail, String htmlBody) {
		return mailDao.sendEmailByTextHtmlBody(sendFrom, sendTo, subjectMail, textMail, htmlBody);
	}
	
	//Invio Mail a EG e ResponsabileEnte
	public EsitoMail sendEmailEGRespEnte(String emailEnte,String denominazioneEnte, ModelResponsabileEnte responsabile, String codMail) {
		ModelParametro paramMail = listeService.getParametro(SharedConstants.MAIL_FROM);
		String sendFrom = paramMail.getInformativa() + " <" + paramMail.getValtext() + ">";
		EsitoMail esito = new EsitoMail();
		
		if(paramMail.getValBool()) {
			String warning = listeService.getMessaggio(SharedConstants.ERROR_ASSENZA_MAIL).getTestoMessaggio();
			String mailEG = emailEnte;
			String mailRespEG = responsabile.geteMail();
			
			// Verifico se e' valorizzata la mail per EG e RespEG
			if(!Checker.isValorizzato(mailEG) && !Checker.isValorizzato(mailRespEG)) {
				// Non invio mail e stampo il warning
				esito.getWarnings().add(warning.replace("SOGGETTO", "dell'Ente"));
				esito.getWarnings().add(warning.replace("SOGGETTO", "del Responsabile dell'Ente"));
			}
			else {
				GregDMail mail = getMailByCod(codMail);
				String errorInvioMail = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MAIL).getTestoMessaggio();
				Integer esitoInvioMailEG = 0;
				Integer esitoInvioMailRespEG = 0;

				if(Checker.isValorizzato(mailEG)) {
					// Invio mail a EG
					String mailToEG = denominazioneEnte + " <" + mailEG + ">";
					esitoInvioMailEG = sendEmailByText(sendFrom, mailToEG, mail.getOggetto(), mail.getTesto());
					
					if(esitoInvioMailEG != 1) {
						esito.getErrors().add(errorInvioMail.replace("UTENTEMAIL", "Ente Gestore"));
					}
				}
				else {
					esito.getWarnings().add(warning.replace("SOGGETTO", "dell'Ente"));
				}
				
				if(Checker.isValorizzato(mailRespEG)) {
					// Invio mail a RespEG
					String mailToRespEG = responsabile.getCognome() + " " + 
							responsabile.getNome() + 
							" <" + mailRespEG + ">";
					esitoInvioMailRespEG = sendEmailByText(sendFrom, mailToRespEG, mail.getOggetto(), mail.getTesto());
					
					if(esitoInvioMailRespEG != 1) {
						esito.getErrors().add(errorInvioMail.replace("UTENTEMAIL", "Responsabile Ente"));
					}
				}
				else {
					esito.getWarnings().add(warning.replace("SOGGETTO", "del Responsabile dell'Ente"));
				}
			}
		}
		return esito;
	}
	
	//Invio Mail a EG e ResponsabileEnte
		public EsitoMail sendEmailEGRespEnteConferma(String emailEnte,String denominazioneEnte, ModelResponsabileEnte responsabile, String codMail, String referenti) {
			ModelParametro paramMail = listeService.getParametro(SharedConstants.MAIL_FROM);
			String sendFrom = paramMail.getInformativa() + " <" + paramMail.getValtext() + ">";
			EsitoMail esito = new EsitoMail();
			
			if(paramMail.getValBool()) {
				String warning = listeService.getMessaggio(SharedConstants.ERROR_ASSENZA_MAIL).getTestoMessaggio();
				String mailEG = emailEnte;
				String mailRespEG = responsabile.geteMail();
				
				// Verifico se e' valorizzata la mail per EG e RespEG
				if(!Checker.isValorizzato(mailEG) && !Checker.isValorizzato(mailRespEG)) {
					// Non invio mail e stampo il warning
					esito.getWarnings().add(warning.replace("SOGGETTO", "dell'Ente"));
					esito.getWarnings().add(warning.replace("SOGGETTO", "del Responsabile dell'Ente"));
				}
				else {
					GregDMail mail = getMailByCod(codMail);
					String errorInvioMail = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MAIL).getTestoMessaggio();
					Integer esitoInvioMailEG = 0;
					Integer esitoInvioMailRespEG = 0;

					if(Checker.isValorizzato(mailEG)) {
						// Invio mail a EG
						String mailToEG = denominazioneEnte + " <" + mailEG + ">";
						esitoInvioMailEG = sendEmailByText(sendFrom, mailToEG, mail.getOggetto(), mail.getTesto().replace("ENTE", denominazioneEnte).replace("REFERENTI", referenti));
						
						if(esitoInvioMailEG != 1) {
							esito.getErrors().add(errorInvioMail.replace("UTENTEMAIL", "Ente Gestore"));
						}
					}
					else {
						esito.getWarnings().add(warning.replace("SOGGETTO", "dell'Ente"));
					}
					
					if(Checker.isValorizzato(mailRespEG)) {
						// Invio mail a RespEG
						String mailToRespEG = responsabile.getCognome() + " " + 
								responsabile.getNome() + 
								" <" + mailRespEG + ">";
						esitoInvioMailRespEG = sendEmailByText(sendFrom, mailToRespEG, mail.getOggetto(), mail.getTesto().replace("ENTE", denominazioneEnte).replace("REFERENTI", referenti));
						
						if(esitoInvioMailRespEG != 1) {
							esito.getErrors().add(errorInvioMail.replace("UTENTEMAIL", "Responsabile Ente"));
						}
					}
					else {
						esito.getWarnings().add(warning.replace("SOGGETTO", "del Responsabile dell'Ente"));
					}
				}
			}
			return esito;
		}
	
	public EsitoMail sendEmailERefEnte(ModelReferenteEnte ref, String referenti, String codMail, String denominazione) {
		ModelParametro paramMail = listeService.getParametro(SharedConstants.MAIL_FROM);
		String sendFrom = paramMail.getInformativa() + " <" + paramMail.getValtext() + ">";
		EsitoMail esito = new EsitoMail();
		
		if(paramMail.getValBool()) {
			String warning = listeService.getMessaggio(SharedConstants.ERROR_ASSENZA_MAIL).getTestoMessaggio();

			// Verifico se e' valorizzata la mail per EG e RespEG
			if(!Checker.isValorizzato(ref.getEmail()) ) {
				// Non invio mail e stampo il warning
				if(ref.getProfilo().equals("EG-RESP")) {
					esito.getWarnings().add(warning.replace("SOGGETTO", "per uno dei Responsabili secondari dell'Ente"));
				}else {
					esito.getWarnings().add(warning.replace("SOGGETTO", "per uno dei Referenti dell'Ente"));
				}
			}
			else {
				GregDMail mail = getMailByCod(codMail);
				String errorInvioMail = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MAIL).getTestoMessaggio();
				Integer esitoInvioMailEG = 0;
				Integer esitoInvioMailRespEG = 0;
				
				
				if(Checker.isValorizzato(ref.getEmail())) {
					// Invio mail a RespEG
					String mailToRespEG = ref.getCognome() + " " + 
							ref.getNome() + 
							" <" + ref.getEmail() + ">";
					esitoInvioMailRespEG = sendEmailByText(sendFrom, mailToRespEG, mail.getOggetto(), mail.getTesto().replace("ENTE", denominazione).replace("REFERENTI", referenti));
					
					if(esitoInvioMailRespEG != 1) {
						esito.getErrors().add(errorInvioMail.replace("UTENTEMAIL", "Referente Ente"));
					}
				}
				else {
					esito.getWarnings().add(warning.replace("SOGGETTO", "del Referente dell'Ente"));
				}
			}
		}
		return esito;
	}
	
//	//Invio Mail a Regione
//	public EsitoMail sendEmailRegione(GregTRendicontazioneEnte rendicontazione, UserInfo utente, String codMail) {
//		ModelParametro paramMail = listeService.getParametro(SharedConstants.MAIL_FROM);
//		String sendFrom = paramMail.getInformativa() + " <" + paramMail.getValtext() + ">";
//		EsitoMail esito = new EsitoMail();
//
//		if(paramMail.getValBool()) {
//			String warning = listeService.getMessaggio(SharedConstants.ERROR_ASSENZA_MAIL).getTestoMessaggio();
//			String mailRegione = mailDao.getMailRegione(utente.getCodFisc()).getEmail();
//
//			// Verifico se e' valorizzata la mail per Regione
//			if(!Checker.isValorizzato(mailRegione)) {
//				// Non invio mail e stampo il warning
//				esito.getWarnings().add(warning.replace("SOGGETTO", "dell'Operatore di Regione"));
//			}
//			else {
//				GregDMail mail = getMailByCod(codMail);
//				String errorInvioMail = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MAIL).getTestoMessaggio();
//				Integer esitoInvioMailRegione = 0;
//
//				// Invio mail a Regione
//				String mailToRegione = utente.getCognome() + " " + utente.getNome() + " <" + mailRegione + ">";
//				esitoInvioMailRegione = sendEmailByText(sendFrom, mailToRegione, mail.getOggetto(), mail.getTesto().replace("ENTEINVIO", rendicontazione.getGregTSchedeEntiGestori().getDenominazione()));
//
//				if(esitoInvioMailRegione != 1) {
//					esito.getErrors().add(errorInvioMail.replace("UTENTEMAIL", "Regione"));
//				}
//
//			}
//		}
//		return esito;
//	}
	
	public boolean verificaMailAzione(String codMail) {
		GregRAzioneMail email = mailDao.getMailByAzione(codMail);
        if (email!=null)
        	return true;
        else
        	return false;
	}
	
	public ModelUltimoContatto findDatiUltimoContatto (Integer idScheda) {
		
		ModelUltimoContatto lista = new ModelUltimoContatto();
		
		lista = mailDao.findDatiUltimoContatto(idScheda);

	  return lista;
	}
}
