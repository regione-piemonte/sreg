/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.controller.ConfiguratoreUtenzeFnps;
import it.csi.greg.gregsrv.business.dao.impl.ConfiguratorePrestazioniDao;
import it.csi.greg.gregsrv.business.dao.impl.ConfiguratoreUtenzeFnpsDao;
import it.csi.greg.gregsrv.business.dao.impl.DatiRendicontazioneDao;
import it.csi.greg.gregsrv.business.dao.impl.GestioneUtentiDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloBDao;
import it.csi.greg.gregsrv.business.entity.GregDAzione;
import it.csi.greg.gregsrv.business.entity.GregDProfilo;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregRFnpsUtenzaCalcolo;
import it.csi.greg.gregsrv.business.entity.GregRListaEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregRProfiloAzione;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneFondi;
import it.csi.greg.gregsrv.business.entity.GregRSpeseFnps;
import it.csi.greg.gregsrv.business.entity.GregRUserProfilo;
import it.csi.greg.gregsrv.business.entity.GregTLista;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.business.entity.GregTUser;
import it.csi.greg.gregsrv.dto.ModelAbilitazioni;
import it.csi.greg.gregsrv.dto.ModelAzione;
import it.csi.greg.gregsrv.dto.ModelFondi;
import it.csi.greg.gregsrv.dto.ModelLista;
import it.csi.greg.gregsrv.dto.ModelListaAzione;
import it.csi.greg.gregsrv.dto.ModelListaEnti;
import it.csi.greg.gregsrv.dto.ModelListaLista;
import it.csi.greg.gregsrv.dto.ModelListaProfilo;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;
import it.csi.greg.gregsrv.dto.ModelProfilo;
import it.csi.greg.gregsrv.dto.ModelUtenti;
import it.csi.greg.gregsrv.dto.ModelUtentiFnps;
import it.csi.greg.gregsrv.dto.ModelUtenzaAllD;
import it.csi.greg.gregsrv.dto.RicercaProfili;
import it.csi.greg.gregsrv.dto.RicercaUtenti;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.SharedConstants;

@Service("configuratoreUtenzeFnpsService")
public class ConfiguratoreUtenzeFnpsService {

	@Autowired
	protected ConfiguratoreUtenzeFnpsDao configuratoreUtenzeFnpsDao;
	@Autowired
	protected DatiRendicontazioneDao datiRendicontazioneDao;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected MailService mailService;
	@Autowired
	protected ConfiguratorePrestazioniDao configuratorePrestazioniDao;

	public List<ModelUtentiFnps> getUtenze() {

		List<ModelUtentiFnps> listaUtenti = configuratoreUtenzeFnpsDao.findUtenzePerCalcolo();
		for (ModelUtentiFnps u : listaUtenti) {
			u.setModificabile(configuratoreUtenzeFnpsDao.utenzaIsModificabile(u.getIdUtenzaFnps()));
		}
		return listaUtenti;
	}

	public List<ModelUtenzaAllD> getUtenzeFnps() {

		List<GregDTargetUtenza> listaUtenti = configuratoreUtenzeFnpsDao.findAllTarget();

		List<ModelUtenzaAllD> utenze = new ArrayList<ModelUtenzaAllD>();

		for (GregDTargetUtenza u : listaUtenti) {
			ModelUtenzaAllD ute = new ModelUtenzaAllD();
			ute.setIdUtenza(u.getIdTargetUtenza());
			ute.setCodUtenza(u.getCodUtenza());
			ute.setDescUtenza(u.getDesUtenza());
			utenze.add(ute);
		}

		return utenze;
	}

	public SaveModelloOutput creaUtenze(List<ModelUtentiFnps> lista, UserInfo user) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(new Date().getTime());
		for (ModelUtentiFnps utente : lista) {
			GregRFnpsUtenzaCalcolo fnps = null;
			if (utente.getIdUtenzaFnps() != null) {
				fnps = configuratoreUtenzeFnpsDao.findUtenzaCalcolobyId(utente.getIdUtenzaFnps());
			} else {
				fnps = new GregRFnpsUtenzaCalcolo();
				GregDTargetUtenza utenza = configuratorePrestazioniDao.findUtenzaById(utente.getIdUtenza());
				fnps.setGregDTargetUtenza(utenza);
				fnps.setUtilizzatoPerCalcolo(utente.isUtilizzatoPerCalcolo());
				fnps.setValorePercentuale(utente.getValorePercentuale());
				GregorianCalendar cal = new GregorianCalendar(utente.getAnnoInizioValidita(), 0, 1);
				fnps.setDataInizioValidita(new Timestamp(cal.getTimeInMillis()));
				fnps.setDataCreazione(dataAttuale);
			}
			if (utente.getAnnoFineValidita() != null) {
				GregorianCalendar cal = new GregorianCalendar(utente.getAnnoFineValidita(), 11, 31);
				fnps.setDataFineValidita(new Timestamp(cal.getTimeInMillis()));
			} else {
				fnps.setDataFineValidita(null);
			}
			fnps.setDataModifica(dataAttuale);
			fnps.setUtenteOperazione(user.getCodFisc());
			configuratoreUtenzeFnpsDao.aggiornaRFnpsCalcolo(fnps);
		}
		return response;
	}

	public SaveModelloOutput eliminaUtenza(ModelUtentiFnps utenza, UserInfo user) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(new Date().getTime());

		GregRFnpsUtenzaCalcolo fnps = configuratoreUtenzeFnpsDao.findUtenzaCalcolobyId(utenza.getIdUtenzaFnps());

		fnps.setDataCancellazione(dataAttuale);
		fnps.setUtenteOperazione(user.getCodFisc());
		configuratoreUtenzeFnpsDao.aggiornaRFnpsCalcolo(fnps);

		return response;
	}

	public List<ModelUtentiFnps> recuperaUtenzePerCalcoloByAnnoEsercizio(Integer annoEsercizio) {

		List<ModelUtentiFnps> listaUtenti = configuratoreUtenzeFnpsDao
				.recuperaUtenzePerCalcoloByAnnoEsercizio(annoEsercizio);
		return listaUtenti;
	}

	public List<ModelFondi> findFondiByidRendicontazione(Integer idRendicontazione) {

		List<ModelFondi> listaFondi = configuratoreUtenzeFnpsDao.findFondiByidRendicontazione(idRendicontazione);
		for (ModelFondi fondo : listaFondi) {
			if (fondo.isLeps()) {
				fondo.setNotModificabile(
						configuratoreUtenzeFnpsDao.azioneIsModificabile(idRendicontazione, fondo.getIdFondo()));
				GregRSpeseFnps speseFnps = configuratoreUtenzeFnpsDao.findSpesaFnpsbyId(fondo.getIdFondo(),
						idRendicontazione);
				if (speseFnps != null) {
					fondo.setIdSpesaFnps(speseFnps.getIdSpeseFnps());
					fondo.setValoreSpesaFnps(speseFnps.getValore());
				}
			} else {
				fondo.setNotModificabile(false);
			}
		}
		return listaFondi;
	}

	public List<ModelFondi> findFondiByAnnoEsercizio(Integer annoEsercizio) {

		List<ModelFondi> listaFondi = configuratoreUtenzeFnpsDao.findFondiByAnnoEsercizio(annoEsercizio);
		return listaFondi;
	}

	public List<ModelFondi> findRegole() {

		List<ModelFondi> listaFondi = configuratoreUtenzeFnpsDao.findRegole();
		return listaFondi;
	}

	public SaveModelloOutput eliminaFondo(ModelFondi fondo, UserInfo userInfo) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		GregRRendicontazioneFondi f = configuratoreUtenzeFnpsDao
				.findFondoRendiscontazionebyId(fondo.getIdFondoRendicontazione());
		f.setDataModifica(dataAttuale);
		f.setDataCancellazione(dataAttuale);
		f.setUtenteOperazione(userInfo.getCodFisc());
		configuratoreUtenzeFnpsDao.aggiornaFondoRendicontazione(f);
		return response;
	}

	public SaveModelloOutput salvaFondi(List<ModelFondi> fondi, UserInfo userInfo,
			GregTRendicontazioneEnte rendicontazione) {
		SaveModelloOutput response = new SaveModelloOutput();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		for (ModelFondi fondo : fondi) {
			GregRRendicontazioneFondi f = configuratoreUtenzeFnpsDao
					.findFondoRendiscontazionebyId(fondo.getIdFondoRendicontazione());
			if(fondo.getValore()!=null) {
				if (f == null) {
					f = new GregRRendicontazioneFondi();
					f.setDataCreazione(dataAttuale);
					f.setGregCRegola(
							fondo.getIdRegola()!=null && !fondo.getIdRegola().equals(0) ? configuratoreUtenzeFnpsDao.findRegolabyId(fondo.getIdRegola())
									: null);
					f.setGregDFondi(configuratoreUtenzeFnpsDao.findFondobyId(fondo.getIdFondo()));
					f.setGregTRendicontazioneEnte(rendicontazione);
				}
				f.setValore(fondo.getValore());
				f.setDataModifica(dataAttuale);
				f.setUtenteOperazione(userInfo.getCodFisc());
				configuratoreUtenzeFnpsDao.aggiornaFondoRendicontazione(f);
			}else {
				if (f != null) {
					f.setDataModifica(dataAttuale);
					f.setDataCancellazione(dataAttuale);
					f.setUtenteOperazione(userInfo.getCodFisc());
					configuratoreUtenzeFnpsDao.aggiornaFondoRendicontazione(f);
				}
			}
		}

		return response;
	}
}
