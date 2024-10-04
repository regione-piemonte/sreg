/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.dao.impl.ConfiguratorePrestazioniDao;
import it.csi.greg.gregsrv.business.dao.impl.DatiRendicontazioneDao;
import it.csi.greg.gregsrv.business.dao.impl.MacroaggregatiDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloA1Dao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloA2Dao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloADao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloB1Dao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloBDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloCDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloDDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloEDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloFDao;
import it.csi.greg.gregsrv.business.entity.GregDObbligo;
import it.csi.greg.gregsrv.business.entity.GregDProfiloProfessionale;
import it.csi.greg.gregsrv.business.entity.GregDTab;
import it.csi.greg.gregsrv.business.entity.GregDVoceModA;
import it.csi.greg.gregsrv.business.entity.GregRCheck;
import it.csi.greg.gregsrv.business.entity.GregRRendMiProTitEnteGestoreModB;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneComuneEnteModA2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneEnteComuneModA2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModA1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModAPart3;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte3;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModCParte4;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModD;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModE;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModFParte1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModFParte2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneNonConformitaModB;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg1Macro;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg1Utereg1;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazionePreg2Utereg2;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneSpesaMissioneProgrammaMacro;
import it.csi.greg.gregsrv.business.entity.GregRTitoloTipologiaVoceModA;
import it.csi.greg.gregsrv.business.entity.GregTCausaleEnteComuneModA2;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.business.entity.GregTSchedeEntiGestori;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.FondiEnteAllontanamentoZero;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelB1Macroaggregati;
import it.csi.greg.gregsrv.dto.ModelB1ProgrammiMissioneTotali;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg1;
import it.csi.greg.gregsrv.dto.ModelB1UtenzaPrestReg2;
import it.csi.greg.gregsrv.dto.ModelB1Voci;
import it.csi.greg.gregsrv.dto.ModelB1VociPrestReg2;
import it.csi.greg.gregsrv.dto.ModelBMissioni;
import it.csi.greg.gregsrv.dto.ModelBProgramma;
import it.csi.greg.gregsrv.dto.ModelBSottotitolo;
import it.csi.greg.gregsrv.dto.ModelBTitolo;
import it.csi.greg.gregsrv.dto.ModelCDettagliDisabilita;
import it.csi.greg.gregsrv.dto.ModelCDettagliUtenze;
import it.csi.greg.gregsrv.dto.ModelCDisabilita;
import it.csi.greg.gregsrv.dto.ModelCPrestazioni;
import it.csi.greg.gregsrv.dto.ModelCTargetUtenze;
import it.csi.greg.gregsrv.dto.ModelCampiMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelCronologiaEnte;
import it.csi.greg.gregsrv.dto.ModelCronologiaProfilo;
import it.csi.greg.gregsrv.dto.ModelDatiA1;
import it.csi.greg.gregsrv.dto.ModelDatiEnteRendicontazione;
import it.csi.greg.gregsrv.dto.ModelFConteggioOre;
import it.csi.greg.gregsrv.dto.ModelFConteggioPersonale;
import it.csi.greg.gregsrv.dto.ModelFPersonaleEnte;
import it.csi.greg.gregsrv.dto.ModelFProfiloProfessionale;
import it.csi.greg.gregsrv.dto.ModelFValori;
import it.csi.greg.gregsrv.dto.ModelFondi;
import it.csi.greg.gregsrv.dto.ModelLink;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;
import it.csi.greg.gregsrv.dto.ModelObbligatorio;
import it.csi.greg.gregsrv.dto.ModelPrestazioneUtenzaModA;
import it.csi.greg.gregsrv.dto.ModelProfilo;
import it.csi.greg.gregsrv.dto.ModelReferenteEnte;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModAPart3;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModB;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModC;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModF;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneTotaliSpeseMissioni;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTargetUtenza;
import it.csi.greg.gregsrv.dto.ModelTipologiaModA;
import it.csi.greg.gregsrv.dto.ModelTitoloModA;
import it.csi.greg.gregsrv.dto.ModelTotaleMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelTotalePrestazioniB1;
import it.csi.greg.gregsrv.dto.ModelTranche;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriMacroaggregati;
import it.csi.greg.gregsrv.dto.ModelValoriModA;
import it.csi.greg.gregsrv.dto.ModelVoceModA;
import it.csi.greg.gregsrv.dto.ModelliInvio;
import it.csi.greg.gregsrv.dto.MonteOre;
import it.csi.greg.gregsrv.dto.SaveModelloAInput;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.SaveMotivazioneCheck;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("datiRendicontazioneService")
public class DatiRendicontazioneService {

	@Autowired
	protected DatiRendicontazioneDao datiRendicontazioneDao;
	@Autowired
	protected DatiEnteService datiEnteService;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected ModelloA1Service modelloA1Service;
	@Autowired
	protected ModelloAService modelloAService;
	@Autowired
	protected ModelloDService modelloDService;
	@Autowired
	protected MacroaggregatiService macroaggregatiService;
	@Autowired
	protected ModelloB1Service modelloB1Service;
	@Autowired
	protected ModelloBService modelloBService;
	@Autowired
	protected ModelloCService modelloCService;
	@Autowired
	protected ModelloFService modelloFService;
	@Autowired
	protected MailService mailService;
	@Autowired
	protected ModelloA1Dao modelloA1Dao;
	@Autowired
	protected ModelloA2Dao modelloA2Dao;
	@Autowired
	protected ModelloADao modelloADao;
	@Autowired
	protected ModelloDDao modelloDDao;
	@Autowired
	protected MacroaggregatiDao macroaggregatiDao;
	@Autowired
	protected ModelloB1Dao modelloB1Dao;
	@Autowired
	protected ModelloBDao modelloBDao;
	@Autowired
	protected ModelloCDao modelloCDao;
	@Autowired
	protected ModelloEDao modelloEDao;
	@Autowired
	protected ModelloFDao modelloFDao;
	@Autowired
	protected ConfiguratorePrestazioniDao configuratorePrestazioniDao;
	@Autowired
	protected ConfiguratoreUtenzeFnpsService configuratoreUtenzeFnpsService;
	@Autowired
	protected ModelloAllontanamentoZeroService modelloAllontanamentoZeroService;

	@Transactional
	public void insertCronologia(GregTCronologia cronologia) {
		datiRendicontazioneDao.insertCronologia(cronologia);
	}

	public GregTCronologia findLastCronologiaEnte(Integer idRendicontazione) {
		return datiRendicontazioneDao.findLastCronologiaEnte(idRendicontazione);
	}

	@Transactional
	public GregTRendicontazioneEnte modificaStatoRendicontazione(GregTRendicontazioneEnte rendicontazione,
			UserInfo utente, String operazione, ModelProfilo profilo) throws Exception {

		if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_I)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_SALVA)
				&& profilo.getListaazioni().get("SalvaDaCompilareI")[1]) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_INVIA)) {
			rendicontazione.setGregDStatoRendicontazione(
					datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_RICHIESTA_RETTIFICA_I)) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_SALVA)
				&& profilo.getListaazioni().get("SalvaInAttesaRettificaI")[1]) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_CONFERMA_DATI_I)) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_SALVA)
				&& profilo.getListaazioni().get("SalvaDaCompilareIICambioStato")[1]) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_RICHIESTA_RETTIFICA_I)) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_INVIA)) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_RICHIESTA_RETTIFICA_I)) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_RICHIESTA_RETTIFICA_II)) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_SALVA)
				&& profilo.getListaazioni().get("SalvaInAttesaRettificaII")[1]) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_CONFERMA_DATI_II)) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_VALIDA)) {
			rendicontazione.setGregDStatoRendicontazione(
					datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_SALVA)
				&& profilo.getListaazioni().get("SalvaValidata")[1]) {
			rendicontazione.setGregDStatoRendicontazione(datiRendicontazioneDao
					.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE));
		} else if (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)
				&& operazione.equalsIgnoreCase(SharedConstants.OPERAZIONE_STORICIZZA)) {
			rendicontazione.setGregDStatoRendicontazione(
					datiRendicontazioneDao.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA));
		}
		rendicontazione.setDataModifica(new Timestamp(System.currentTimeMillis()));
		rendicontazione.setUtenteOperazione(utente.getCodFisc());
		return datiRendicontazioneDao.updateRendicontazione(rendicontazione);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<ModelLink> getTabTrancheEnte(Integer idRendicontazione, String codProfilo) {

		List<Object> entiresult = datiRendicontazioneDao.getTabTrancheEnte(idRendicontazione, codProfilo);
		ArrayList<ModelLink> elencotab = new ArrayList<ModelLink>();
		Iterator itr = entiresult.iterator();
		try {
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				ModelLink singolatab = new ModelLink();
				singolatab.setTitle(String.valueOf(obj[0]));
				singolatab.setLink(String.valueOf(obj[1]));
				singolatab.setTooltip(String.valueOf(obj[2]));
				singolatab.setAzione(String.valueOf(obj[3]));
				singolatab.setSigla(String.valueOf(obj[4]));
//				if (!codTipoEnte.equalsIgnoreCase(SharedConstants.OPERAZIONE_COMUNE_CAPOLUOGO))
//					singolatab.setActive(true);
//				else
//				singolatab.setActive(!(boolean) obj[5]);
				String[] fragment = String.valueOf(obj[5]).split(",");
				singolatab.setFragment(fragment);
				elencotab.add(singolatab);
			}
			return elencotab;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public GregTRendicontazioneEnte getRendicontazione(Integer idEnte) {
		try {
			return datiRendicontazioneDao.findRendicontazioneByIdScheda(idEnte);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public GregTRendicontazioneEnte getRendicontazionePassata(Integer idScheda, Integer anno) {
		try {
			return datiRendicontazioneDao.findRendicontazioneByIdSchedaAnno(idScheda, anno);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public GregTSchedeEntiGestori getSchedaEnte(Integer idScheda) {
		try {
			return datiRendicontazioneDao.findSchedaEnteByIdScheda(idScheda);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public GenericResponseWarnErr attivaControlliModello(ArrayList<ModelTabTranche> modelli, String tranche,
			GregTRendicontazioneEnte rendicontazione, String codregione) {
		GenericResponseWarnErr elencoerrori = new GenericResponseWarnErr();
		elencoerrori.setWarnings(new ArrayList<String>());
		elencoerrori.setErrors(new ArrayList<String>());
		int anno = rendicontazione.getAnnoGestione();
		String messaggio = "";

		// boolean comunecapoluogo =
		// rendicontazione.getGregTSchedeEntiGestori().getGregDTipoEnte().getCodTipoEnte().equalsIgnoreCase(SharedConstants.OPERAZIONE_COMUNE_CAPOLUOGO);
		// boolean convenzionecomuni =
		// rendicontazione.getGregTSchedeEntiGestori().getGregDTipoEnte().getCodTipoEnte().equalsIgnoreCase(SharedConstants.OPERAZIONE_CONVENZIONE_COMUNI);
		// controllo presenza allegati
		// prelevo gli allegati dove alegato 1 obbligatorio prima tranche
		boolean trovatoallegato = false;
		String numallegati = datiRendicontazioneDao.contaAllegati(tranche, rendicontazione.getIdRendicontazioneEnte());
		trovatoallegato = Converter.getInt(numallegati) == 0 ? false : true;
		if (!trovatoallegato) {
			elencoerrori.getErrors().add(listeService.getMessaggio(SharedConstants.ERRORE_MANCA_ALLEGATO_1)
					.getTestoMessaggio().replace("TRANCHE", SharedConstants.PRIMA_TRANCHE));
			elencoerrori.setDescrizione(
					listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIA_KO).getTestoMessaggio());
			elencoerrori.setId("OK");
			return elencoerrori;
		} else {
			// se trovo allegato allora controllo se ci sono dati nei modelli se vuoti
			// chiedo se procedere se anche uno fosse pieno mi blocco
			String modellitranche = "";
			String mancanzamodello = "";
			String condati = "";
			// valori diversi da null e da 0
			List<GregRRendicontazioneModAPart1> listaparte1 = modelloADao
					.getAllDatiModelloAPart1PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModAPart2> listaparte2 = modelloADao
					.getAllDatiModelloAPart2PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModAPart3> listaparte3 = modelloADao
					.getAllDatiModelloAPart3PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregTCausaleEnteComuneModA2> causali = modelloA2Dao
					.getCausali(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneEnteComuneModA2> entecomune = modelloA2Dao
					.getPerInvioModelloA2EnteComune(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneComuneEnteModA2> comuneente = modelloA2Dao
					.getPerInvioModelloA2ComuneEnte(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModD> datimodD = modelloDDao
					.getModelloDPerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModA1> datimodelloA1 = modelloA1Dao
					.getAllDatiModelloA1PerInvio(rendicontazione.getIdRendicontazioneEnte());
			for (ModelTabTranche modello : modelli) {
				if (modello.getCodObbligo().equalsIgnoreCase(SharedConstants.OBBLIGATORIO)) {
					if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_A)) {
						// preleva i dati dalle tabelle per vedere se esiste una rendicontazione sui
						// modelli
						if (listaparte1.size() == 0 && listaparte2.size() == 0 && listaparte3.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getCodTab() + ", ";
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "1";
						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_A1)) {
						// if (!comunecapoluogo) {
						if (datimodelloA1.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getCodTab() + ", ";
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "1";
						}
						// }
//						else {
//							condati = condati + modello.getCodTab() + ", ";
//							modellitranche = modellitranche + "2";
//						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_A2)) {
						// if (!comunecapoluogo) {
						if (comuneente.size() == 0 && entecomune.size() == 0 && causali.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getCodTab() + ", ";
//							}
//							else {
//								condati = condati + modello.getCodTab() + ", ";
//								modellitranche = modellitranche + "1";
//							}
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "2";
						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_D)) {
						if (datimodD.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getCodTab() + ", ";
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "1";
						}
					}
				} else {
					modellitranche = modellitranche + "2";
				}
			}
			if (!modellitranche.contains("1")) {
				// ci sono gli allegati e sono tutti vuoti popup messaggio per continuare esci
				messaggio = listeService.getMessaggio(SharedConstants.ERROR_INVIO_ALLEGATO_NO_MODELLISIDOC)
						.getTestoMessaggio() + ";";
				messaggio = messaggio + listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_TRANCHE)
						.getTestoMessaggio().replace("TRANCHE", SharedConstants.PRIMA_TRANCHE);
				elencoerrori.setDescrizione(messaggio);
				elencoerrori.setId("KO");
				return elencoerrori;
			} else if (!modellitranche.contains("0")) {

				// ci sono gli allegati e sono tutti pieni ok continua controlli
				List<ModelValoriModA> cella06 = new ArrayList<ModelValoriModA>();
				List<ModelValoriModA> cella07 = new ArrayList<ModelValoriModA>();
				List<ModelValoriModA> cella08 = new ArrayList<ModelValoriModA>();
				BigDecimal valorecella06 = null;
				BigDecimal valorecella07 = null;
				for (ModelTabTranche modello : modelli) {
					if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_A)) {
						// chiama funzione modello a
						cella06 = modelloAService.getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(),
								SharedConstants.MODELLO_A_06);
						// verifica se il campo avvalorato
						if (cella06.size() != 0) {
							valorecella06 = cella06.get(0).getValore();
						}
						cella07 = modelloAService.getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(),
								SharedConstants.MODELLO_A_07);
						if (cella07.size() != 0) {
							valorecella07 = cella07.get(0).getValore();
						}
						cella08 = modelloAService.getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(),
								SharedConstants.MODELLO_A_08);
						// controllo che se comune capoluogo allora obbligatorio anche 0
//						if ((comunecapoluogo || convenzionecomuni) && cella08.size()==0) {
//							elencoerrori.getErrors().add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_08).getTestoMessaggio());
//						}
						// if (!comunecapoluogo && !convenzionecomuni && cella08.size()!=0) {
						if (cella08.size() != 0) {
							elencoerrori.getErrors().add(listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_NO08).getTestoMessaggio());
						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_A1)) {
						// chiama funzione modello a1
						// prendo i dati del modello A1
						int contavalorimodelloA1 = 0;
						// if (!comunecapoluogo) {
						List<ModelDatiA1> modelloa1 = modelloA1Service
								.getDatiModelloA1(rendicontazione.getIdRendicontazioneEnte());
						for (ModelDatiA1 datimodel : modelloa1) {
							if (datimodel.getCodcomune() != null) {
								if (!datimodel.getCodcomune().equalsIgnoreCase("Somma")) {
									// se tutti i dati sono obbligatori
									// verificare se il campo puo' essere 0
									for (Map.Entry<String, String> entry : datimodel.getValore().entrySet()) {
										if (entry.getValue() != null) {
											contavalorimodelloA1 = contavalorimodelloA1 + 1;
											String warning = controlloService.numberDecimalValidate(
													entry.getValue() == null ? "0" : entry.getValue().replace(".", ""),
													"Importo " + entry.getKey() + " " + datimodel.getDesccomune(), 14,
													2);
											if (warning != null) {
												elencoerrori.getErrors().add(warning);
											}
										}
									}
								} else {
									// campo somma da controllare uguale al valore del modello A se presente
									for (Map.Entry<String, String> entry : datimodel.getValore().entrySet()) {
										if (entry.getValue() != null && !entry.getValue().equalsIgnoreCase(",00")) {
											if (valorecella06 != null) {
												if (!valorecella06.setScale(2, RoundingMode.HALF_UP)
														.equals(new BigDecimal(
																entry.getValue().replace(".", "").replace(",", "."))
																.setScale(2, RoundingMode.HALF_UP))) {
													// valori differenti
													elencoerrori.getErrors()
															.add(listeService
																	.getMessaggio(
																			SharedConstants.ERROR_INVIO_MODELLIA_06)
																	.getTestoMessaggio());
												} else if (valorecella06.setScale(2, RoundingMode.HALF_UP)
														.equals(new BigDecimal(
																entry.getValue().replace(".", "").replace(",", "."))
																.setScale(2, RoundingMode.HALF_UP))
														&& valorecella06.setScale(2, RoundingMode.HALF_UP).equals(
																BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))) {
													// uguale zero
													elencoerrori.getErrors()
															.add(listeService
																	.getMessaggio(
																			SharedConstants.ERROR_INVIO_MODELLIA_NO06)
																	.getTestoMessaggio());
												}
											} else {
												elencoerrori.getErrors()
														.add(listeService
																.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_06)
																.getTestoMessaggio());
											}
										} else {
											if (valorecella06 != null && !valorecella06.equals(BigDecimal.ZERO)) {
												// valori differenti
												elencoerrori.getErrors()
														.add(listeService
																.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_06)
																.getTestoMessaggio());
											} else {
												elencoerrori.getErrors()
														.add(listeService
																.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_NO06)
																.getTestoMessaggio());
											}
										}
									}

								}
							} else {
								// caso totale
								for (Map.Entry<String, String> entry : datimodel.getValore().entrySet()) {
									if (entry.getValue() != null) {
										contavalorimodelloA1 = contavalorimodelloA1 + 1;
										String warning = controlloService.numberDecimalValidate(
												entry.getValue() == null ? "0" : entry.getValue().replace(".", ""),
												"Importo " + entry.getKey() + " " + datimodel.getDesccomune(), 14, 2);
										if (warning != null) {
											elencoerrori.getErrors().add(warning);
										}
									} else {
										if (modello.getCodObbligo().equalsIgnoreCase(SharedConstants.OBBLIGATORIO)) {

											if (entry.getKey().equalsIgnoreCase("T1")) {
												elencoerrori.getErrors().add(listeService
														.getMessaggio(SharedConstants.ERROR_CODE_DATI_OBBLIGATORI)
														.getTestoMessaggio().replace("MODELLODEF", "A1")
														.replace("SPECIFICARE",
																modelloA1Dao.getVoceByCodVoce(entry.getKey())
																		.getDescVoceModA1()
																		.replace("anno", String.valueOf(anno))));
											}
										}
									}
								}
							}
						}
						// }
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_A2)) {
						// if (!comunecapoluogo) {
						// chiama funzione modello a2
						if (entecomune.size() == 0) {
							elencoerrori.getWarnings().add(
									listeService.getMessaggio(SharedConstants.WARNING_MOD_A2_01).getTestoMessaggio());
						}
						// Controllo il totale trasferimenti comune->ente == totale Mod A cella G9
						if (valorecella07 == null) {
							elencoerrori.getErrors().add(listeService
									.getMessaggio(SharedConstants.ERROR_CODE_DATI_OBBLIGATORI).getTestoMessaggio()
									.replace("MODELLODEF", "A").replace("SPECIFICARE",
											"Valore delle entrate relative a contributi e trasferimenti da comuni all'ente gestore per causali diverse dalla quota pro-capite"));
						} else {
							if (comuneente.size() == 0 && !valorecella07.equals(BigDecimal.ZERO)) {
								elencoerrori.getErrors().add(listeService
										.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_07).getTestoMessaggio());
							} else if (modello.getCodObbligo().equalsIgnoreCase(SharedConstants.OBBLIGATORIO)
									|| comuneente.size() != 0 || !valorecella07.equals(BigDecimal.ZERO)) {
								// calcolo il totale
								BigDecimal totale = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
								for (GregRRendicontazioneComuneEnteModA2 val : comuneente) {
									totale = totale.add(val.getValore());
								}
								if (!valorecella07.setScale(2, RoundingMode.HALF_UP).equals(totale)) {
									elencoerrori.getErrors().add(listeService
											.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_07).getTestoMessaggio());
								}
							}
						}
						// }
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_D)) {
						// aggiunta dei warning valori negativi
						BigDecimal risultatoamm = modelloDDao
								.calcolaRisultatoAmministrazione(rendicontazione.getIdRendicontazioneEnte());
						BigDecimal totalepartedisp = modelloDDao
								.calcolaTotaleParteDisponibile(rendicontazione.getIdRendicontazioneEnte());
						totalepartedisp = risultatoamm.add(totalepartedisp.negate());
						try {
							String warn = controlloService.isNegativeNumber(risultatoamm,
									listeService.getMessaggio(SharedConstants.WARNING_MOD_D_03).getTestoMessaggio());
							if (warn != null)
								elencoerrori.getWarnings().add(warn);
						} catch (IntegritaException e) {
							e.printStackTrace();
						}
						try {
							String warn = controlloService.isNegativeNumber(totalepartedisp,
									listeService.getMessaggio(SharedConstants.WARNING_MOD_D_04).getTestoMessaggio());
							if (warn != null)
								elencoerrori.getWarnings().add(warn);
						} catch (IntegritaException e) {
							e.printStackTrace();
						}
					}
				}
			} else if (modellitranche.contains("1") && modellitranche.contains("0")) {
				// se sono misti dai errore perche' non tutti valorizzati ed esci
				elencoerrori.getErrors()
						.add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO).getTestoMessaggio()
								.replace("MODELLINO", mancanzamodello.substring(0, mancanzamodello.length() - 2)));
			}
		}
		if (elencoerrori.getErrors().size() > 0) {
			// ci sono errori bloccanti devo mettere anche l'eerore finale generico
			elencoerrori.setDescrizione(
					listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIA_KO).getTestoMessaggio());
		}
		if (elencoerrori.getErrors().size() == 0 && elencoerrori.getWarnings().size() > 0) {
			// ci sono errori bloccanti devo mettere anche l'errore finale generico
			elencoerrori.setDescrizione(listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_TRANCHE)
					.getTestoMessaggio().replace("TRANCHE", SharedConstants.PRIMA_TRANCHE));
		}
		if (elencoerrori.getErrors().size() == 0 && elencoerrori.getWarnings().size() == 0) {
			messaggio = listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_OK).getTestoMessaggio()
					.replace("TRANCHE", SharedConstants.PRIMA_TRANCHE) + ";";
			messaggio = messaggio + listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_TRANCHE)
					.getTestoMessaggio().replace("TRANCHE", SharedConstants.PRIMA_TRANCHE);
			elencoerrori.setDescrizione(messaggio);
		}
		elencoerrori.setId("OK");
		return elencoerrori;
	}

	@Transactional
	public GenericResponseWarnErr rendicontazioneConferma1(Integer idSchedaEnte,
			ModelCronologiaProfilo cronologiaprofilo, UserInfo userInfo) throws Exception {

		GenericResponseWarnErr response = new GenericResponseWarnErr();

		// prendo la rendicontazione
		GregTRendicontazioneEnte rendicontazione = getRendicontazione(idSchedaEnte);
		if (cronologiaprofilo.getProfilo() != null
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[1]
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[0]
				&& (cronologiaprofilo.getCronologia().getNotaEnte() == null
						|| cronologiaprofilo.getCronologia().getNotaEnte().equals(""))) {
//		if ((userInfo.getRuolo() != null && (userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE))) && 
//				(cronologiaprofilo.getCronologia().getNotaEnte() == null || cronologiaprofilo.getCronologia().getNotaEnte().equals(""))){
			// manca la nota per l'ente
			String errornota = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE).getTestoMessaggio();
			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errornota);
			return response;
		}

		// verifico se lo stato della rendicontazione attuale e' corretto
		if (!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
				|| (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
						.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
						&& cronologiaprofilo.getProfilo() != null
						&& !cronologiaprofilo.getProfilo().getListaazioni().get("ConfermaDatiI")[1])) {

			// errore stato non valido per conferma
			String errorstato = null;
			errorstato = listeService.getMessaggio(SharedConstants.ERROR_CONFERMA_RETTIFICA_DATI).getTestoMessaggio()
					.replace("OPERAZIONE", "CONFERMA DATI I").replace("TRANCHE", SharedConstants.PRIMA_TRANCHE)
					.replace("STATOREND",
							"'" + listeService
									.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
									.getDescStatoRendicontazione() + "'");

			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errorstato);
			return response;
		} else {
			// Effettuo le dovute operazioni

			// Controllo e aggiorno lo stato della rendicontazione
			modificaStatoRendicontazione(rendicontazione, userInfo, SharedConstants.OPERAZIONE_CONFERMA_DATI_I,
					cronologiaprofilo.getProfilo());

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaEnte())
					|| Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaInterna())) {
				GregTCronologia crono = new GregTCronologia();
				Timestamp dataModifica = new Timestamp(new Date().getTime());
				crono.setGregTRendicontazioneEnte(rendicontazione);
				crono.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
				crono.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				crono.setModello(cronologiaprofilo.getModello());
				crono.setUtenteOperazione(userInfo.getCodFisc());
				crono.setNotaInterna(cronologiaprofilo.getCronologia().getNotaInterna());
				crono.setNotaPerEnte(cronologiaprofilo.getCronologia().getNotaEnte());
				crono.setDataOra(dataModifica);
				crono.setDataCreazione(dataModifica);
				crono.setDataModifica(dataModifica);
				insertCronologia(crono);
			}
			if (cronologiaprofilo.getProfilo() != null
					&& cronologiaprofilo.getProfilo().getListaazioni().get("InviaEmail")[1]) {
				ModelUltimoContatto ultimoContatto = mailService
						.findDatiUltimoContatto(rendicontazione.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
				// Invio Mail a EG e ResponsabileEnte
				boolean trovataemail = mailService
						.verificaMailAzione(SharedConstants.MAIL_CONFERMA_DATI_REND_I_TRANCHE);
				if (trovataemail) {
					// Invio mail
					EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
							ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
							SharedConstants.MAIL_CONFERMA_DATI_REND_I_TRANCHE);
					response.setId(HttpStatus.OK.toString());
					response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
					response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
					return response;
				}
			}
			response.setId(HttpStatus.OK.toString());
			response.setWarnings(null);
			response.setErrors(null);
			return response;
		}
	}

	@Transactional
	public GenericResponseWarnErr rendicontazioneConferma2(Integer idSchedaEnte,
			ModelCronologiaProfilo cronologiaprofilo, UserInfo userInfo) throws Exception {

		GenericResponseWarnErr response = new GenericResponseWarnErr();
		// prendo la rendicontazione
		GregTRendicontazioneEnte rendicontazione = getRendicontazione(idSchedaEnte);
		if (cronologiaprofilo.getProfilo() != null
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[1]
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[0]
				&& (cronologiaprofilo.getCronologia().getNotaEnte() == null
						|| cronologiaprofilo.getCronologia().getNotaEnte().equals(""))) {
//		if ((userInfo.getRuolo() != null && (userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE))) && 
//				(cronologiaprofilo.getCronologia().getNotaEnte() == null || cronologiaprofilo.getCronologia().getNotaEnte().equals(""))){
//			//manca la nota per l'ente
			String errornota = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE).getTestoMessaggio();
			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errornota);
			return response;
		}

		// verifico se lo stato della rendicontazione attuale e' corretto
		if (!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
				|| (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
						.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
						&& cronologiaprofilo.getProfilo() != null
						&& !cronologiaprofilo.getProfilo().getListaazioni().get("ConfermaDatiII")[1])) {

			// errore stato non valido per conferma
			String errorstato = null;
			errorstato = listeService.getMessaggio(SharedConstants.ERROR_CONFERMA_RETTIFICA_DATI).getTestoMessaggio()
					.replace("OPERAZIONE", "CONFERMA DATI II").replace("TRANCHE", SharedConstants.SECONDA_TRANCHE)
					.replace("STATOREND",
							"'" + listeService
									.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
									.getDescStatoRendicontazione() + "'");

			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errorstato);
			return response;
		} else {
			// Effettuo le dovute operazioni

			// Controllo e aggiorno lo stato della rendicontazione
			modificaStatoRendicontazione(rendicontazione, userInfo, SharedConstants.OPERAZIONE_CONFERMA_DATI_II,
					cronologiaprofilo.getProfilo());

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaEnte())
					|| Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaInterna())) {
				GregTCronologia crono = new GregTCronologia();
				Timestamp dataModifica = new Timestamp(new Date().getTime());
				crono.setGregTRendicontazioneEnte(rendicontazione);
				crono.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
				crono.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				crono.setModello(cronologiaprofilo.getModello());
				crono.setUtenteOperazione(userInfo.getCodFisc());
				crono.setNotaInterna(cronologiaprofilo.getCronologia().getNotaInterna());
				crono.setNotaPerEnte(cronologiaprofilo.getCronologia().getNotaEnte());
				crono.setDataOra(dataModifica);
				crono.setDataCreazione(dataModifica);
				crono.setDataModifica(dataModifica);
				insertCronologia(crono);
			}
			if (cronologiaprofilo.getProfilo() != null
					&& cronologiaprofilo.getProfilo().getListaazioni().get("InviaEmail")[1]) {
				List<ModelReferenteEnte> ref = datiRendicontazioneDao
						.findReferenteEnte(rendicontazione.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
				List<ModelReferenteEnte> onlyRef = new ArrayList<ModelReferenteEnte>();
				for (ModelReferenteEnte r : ref) {
					if (r.getProfilo().equals("EG-REF")) {
						onlyRef.add(r);
					}
				}
				String referenti = "";
				for (int i = 0; i < onlyRef.size(); i++) {
					if (i == (onlyRef.size() - 1)) {
						referenti += onlyRef.get(i).getNome().toUpperCase() + " "
								+ onlyRef.get(i).getCognome().toUpperCase();
					} else {
						referenti += onlyRef.get(i).getNome().toUpperCase() + " "
								+ onlyRef.get(i).getCognome().toUpperCase() + ", ";
					}
				}
				ModelUltimoContatto ultimoContatto = mailService
						.findDatiUltimoContatto(rendicontazione.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
				// Invio Mail a EG e ResponsabileEnte
				boolean trovataemail = mailService
						.verificaMailAzione(SharedConstants.MAIL_CONFERMA_DATI_REND_II_TRANCHE);
				if (trovataemail) {

					EsitoMail esitoMail = mailService.sendEmailEGRespEnteConferma(ultimoContatto.getEmail(),
							ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
							SharedConstants.MAIL_CONFERMA_DATI_REND_II_TRANCHE, referenti);
					response.setId(HttpStatus.OK.toString());
					response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
					response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
					for (ModelReferenteEnte r : ref) {
						if (!r.getCodiceFiscale().equals(ultimoContatto.getResponsabileEnte().getCodiceFiscale())) {
							EsitoMail esitoMailER = mailService.sendEmailERefEnte(r, referenti,
									SharedConstants.MAIL_CONFERMA_DATI_REND_II_TRANCHE,
									ultimoContatto.getDenominazione());
							response.getWarnings()
									.addAll(esitoMailER != null ? esitoMailER.getWarnings() : new ArrayList<String>());
							response.getErrors()
									.addAll(esitoMailER != null ? esitoMailER.getErrors() : new ArrayList<String>());
						}
					}
					return response;
				}
			}
			response.setId(HttpStatus.OK.toString());
			response.setWarnings(null);
			response.setErrors(null);
			return response;
		}
	}

	public GenericResponseWarnErr confermaInvioModelli(GregTRendicontazioneEnte rendicontazione, UserInfo userInfo,
			String nota, String esito, String tranche, ModelProfilo profilo, String modello) throws Exception {
		// Controllo e aggiorno lo stato della rendicontazione
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		String statoold = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
		if (esito.equalsIgnoreCase("OK")) {
			modificaStatoRendicontazione(rendicontazione, userInfo, SharedConstants.OPERAZIONE_INVIA, profilo);
			String statonew = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
			if (!Checker.isValorizzato(nota)) {
				nota = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO).getTestoMessaggio()
						.replace("OPERAZIONE", "INVIA").replace("STATOOLD", statoold).replace("STATONEW", statonew);
			}
		}
		if (Checker.isValorizzato(nota)) {
			GregTCronologia crono = new GregTCronologia();
			Timestamp dataModifica = new Timestamp(new Date().getTime());
			crono.setGregTRendicontazioneEnte(rendicontazione);
			crono.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
			crono.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
			crono.setModello(modello);
			crono.setUtenteOperazione(userInfo.getCodFisc());
			crono.setNotaInterna(null);
			crono.setNotaPerEnte(nota);
			crono.setDataOra(dataModifica);
			crono.setDataCreazione(dataModifica);
			crono.setDataModifica(dataModifica);
			insertCronologia(crono);
		}
		response.setId(HttpStatus.OK.toString());
		response.setDescrizione(null);
		response.setWarnings(null);
		response.setErrors(null);
		return response;
	}

	@Transactional
	public GenericResponseWarnErr rendicontazioneRettifica1(Integer idSchedaEnte,
			ModelCronologiaProfilo cronologiaprofilo, UserInfo userInfo) throws Exception {

		GenericResponseWarnErr response = new GenericResponseWarnErr();

		// prendo la rendicontazione
		GregTRendicontazioneEnte rendicontazione = getRendicontazione(idSchedaEnte);
		if (cronologiaprofilo.getProfilo() != null
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[1]
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[0]
				&& (cronologiaprofilo.getCronologia().getNotaEnte() == null
						|| cronologiaprofilo.getCronologia().getNotaEnte().equals(""))) {
//		if ((userInfo.getRuolo() != null && (userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE))) && 
//				(cronologiaprofilo.getCronologia().getNotaEnte() == null || cronologiaprofilo.getCronologia().getNotaEnte().equals(""))){
			// manca la nota per l'ente
			String errornota = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE).getTestoMessaggio();
			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errornota);
			return response;
		}

		// verifico se lo stato della rendicontazione attuale e' corretto
		if (!((rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
				|| rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
						.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
				|| rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
						.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II))
				&& cronologiaprofilo.getProfilo() != null
				&& cronologiaprofilo.getProfilo().getListaazioni().get("RichiestaRettificaI")[1])) {
//		if ((!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I) ||  
//				(rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I) 
//						&& userInfo.getRuolo()!= null && (!userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) && !userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE)))) && 
//				(!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II) ||  
//						(rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II) 
//								&& userInfo.getRuolo()!= null && (!userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) && !userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE)))) &&
//				(!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II) ||  
//						(rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione().equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II) 
//								&& userInfo.getRuolo()!= null && (!userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) && !userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE))))
//				){

			// errore stato non valido per rettifica
			String errorstato = null;
			String msgToReplace = "'"
					+ listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
							.getDescStatoRendicontazione()
					+ "' o '"
					+ listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
							.getDescStatoRendicontazione()
					+ "' o '"
					+ listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
							.getDescStatoRendicontazione()
					+ "'";
			errorstato = listeService.getMessaggio(SharedConstants.ERROR_CONFERMA_RETTIFICA_DATI).getTestoMessaggio()
					.replace("OPERAZIONE", "RICHIESTA RETTIFICA DATI I")
					.replace("TRANCHE", SharedConstants.PRIMA_TRANCHE).replace("STATOREND", msgToReplace);

			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errorstato);
			return response;
		} else {
			// Effettuo le dovute operazioni

			// Controllo e aggiorno lo stato della rendicontazione
			modificaStatoRendicontazione(rendicontazione, userInfo, SharedConstants.OPERAZIONE_RICHIESTA_RETTIFICA_I,
					cronologiaprofilo.getProfilo());

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaEnte())
					|| Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaInterna())) {
				GregTCronologia crono = new GregTCronologia();
				Timestamp dataModifica = new Timestamp(new Date().getTime());
				crono.setGregTRendicontazioneEnte(rendicontazione);
				crono.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
				crono.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				crono.setModello(cronologiaprofilo.getModello());
				crono.setUtenteOperazione(userInfo.getCodFisc());
				crono.setNotaInterna(cronologiaprofilo.getCronologia().getNotaInterna());
				crono.setNotaPerEnte(cronologiaprofilo.getCronologia().getNotaEnte());
				crono.setDataOra(dataModifica);
				crono.setDataCreazione(dataModifica);
				crono.setDataModifica(dataModifica);
				insertCronologia(crono);
			}
			if (cronologiaprofilo.getProfilo() != null
					&& cronologiaprofilo.getProfilo().getListaazioni().get("InviaEmail")[1]) {
				ModelUltimoContatto ultimoContatto = mailService
						.findDatiUltimoContatto(rendicontazione.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
				// Invio Mail a EG e ResponsabileEnte
				boolean trovataemail = mailService
						.verificaMailAzione(SharedConstants.MAIL_RICHIESTA_RETTIFICA_I_TRANCHE);
				if (trovataemail) {
					EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
							ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
							SharedConstants.MAIL_RICHIESTA_RETTIFICA_I_TRANCHE);
					response.setId(HttpStatus.OK.toString());
					response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
					response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
					return response;
				}
			}
			response.setId(HttpStatus.OK.toString());
			response.setWarnings(null);
			response.setErrors(null);
			return response;
		}
	}

	@Transactional
	public GenericResponseWarnErr rendicontazioneRettifica2(Integer idSchedaEnte,
			ModelCronologiaProfilo cronologiaprofilo, UserInfo userInfo) throws Exception {

		GenericResponseWarnErr response = new GenericResponseWarnErr();

		// prendo la rendicontazione
		GregTRendicontazioneEnte rendicontazione = getRendicontazione(idSchedaEnte);
		if (cronologiaprofilo.getProfilo() != null
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[1]
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[0]
				&& (cronologiaprofilo.getCronologia().getNotaEnte() == null
						|| cronologiaprofilo.getCronologia().getNotaEnte().equals(""))) {
//		if ((userInfo.getRuolo() != null && (userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_REGIONALE) || userInfo.getRuolo().equalsIgnoreCase(SharedConstants.AMMINISTRATORE))) && 
//				(cronologiaprofilo.getCronologia().getNotaEnte() == null || cronologiaprofilo.getCronologia().getNotaEnte().equals(""))){
			// manca la nota per l'ente
			String errornota = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE).getTestoMessaggio();
			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errornota);
			return response;
		}

		// verifico se lo stato della rendicontazione attuale e' corretto
		if (!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
				|| (rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
						.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
						&& cronologiaprofilo.getProfilo() != null
						&& !cronologiaprofilo.getProfilo().getListaazioni().get("RichiestaRettificaII")[1])) {

			// errore stato non valido per rettifica
			String errorstato = null;
			errorstato = listeService.getMessaggio(SharedConstants.ERROR_CONFERMA_RETTIFICA_DATI).getTestoMessaggio()
					.replace("OPERAZIONE", "RICHIESTA RETTIFICA DATI II")
					.replace("TRANCHE", SharedConstants.SECONDA_TRANCHE).replace("STATOREND",
							"'" + listeService
									.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
									.getDescStatoRendicontazione() + "'");

			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errorstato);
			return response;
		} else {
			// Effettuo le dovute operazioni

			// Controllo e aggiorno lo stato della rendicontazione
			modificaStatoRendicontazione(rendicontazione, userInfo, SharedConstants.OPERAZIONE_RICHIESTA_RETTIFICA_II,
					cronologiaprofilo.getProfilo());

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaEnte())
					|| Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaInterna())) {
				GregTCronologia crono = new GregTCronologia();
				Timestamp dataModifica = new Timestamp(new Date().getTime());
				crono.setGregTRendicontazioneEnte(rendicontazione);
				crono.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
				crono.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				crono.setModello(cronologiaprofilo.getModello());
				crono.setUtenteOperazione(userInfo.getCodFisc());
				crono.setNotaInterna(cronologiaprofilo.getCronologia().getNotaInterna());
				crono.setNotaPerEnte(cronologiaprofilo.getCronologia().getNotaEnte());
				crono.setDataOra(dataModifica);
				crono.setDataCreazione(dataModifica);
				crono.setDataModifica(dataModifica);
				insertCronologia(crono);
			}
			if (cronologiaprofilo.getProfilo() != null
					&& cronologiaprofilo.getProfilo().getListaazioni().get("InviaEmail")[1]) {
				ModelUltimoContatto ultimoContatto = mailService
						.findDatiUltimoContatto(rendicontazione.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
				// Invio Mail a EG e ResponsabileEnte
				boolean trovataemail = mailService
						.verificaMailAzione(SharedConstants.MAIL_RICHIESTA_RETTIFICA_II_TRANCHE);
				if (trovataemail) {
					EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
							ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
							SharedConstants.MAIL_RICHIESTA_RETTIFICA_II_TRANCHE);
					response.setId(HttpStatus.OK.toString());
					response.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
					response.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
					return response;
				}
			}
			response.setId(HttpStatus.OK.toString());
			response.setWarnings(null);
			response.setErrors(null);
			return response;

		}
	}

	@Transactional
	public GenericResponse rendicontazioneValida(Integer idSchedaEnte, ModelCronologiaProfilo cronologiaprofilo,
			UserInfo userInfo) throws Exception {

		GenericResponse response = new GenericResponse();

		// prendo la rendicontazione
		GregTRendicontazioneEnte rendicontazione = getRendicontazione(idSchedaEnte);
		if (cronologiaprofilo.getProfilo() != null
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[1]
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[0]
				&& (cronologiaprofilo.getCronologia().getNotaEnte() == null
						|| cronologiaprofilo.getCronologia().getNotaEnte().equals(""))) {
//		if ((cronologiaprofilo.getProfilo().listaazioni.get("CronologiaEnte")[1] && !cronologiaprofilo.getProfilo().listaazioni.get("CronologiaEnte")[0]) &&
//		//if ((ruoloEnte != null && ruoloEnte.equalsIgnoreCase(SharedConstants.RESPONSABILE_ENTE)) && 
//				(cronologiaprofilo.getCronologia().getNotaEnte() == null || cronologiaprofilo.getCronologia().getNotaEnte().equals(""))){
			// manca la nota per l'ente
			String errornota = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE).getTestoMessaggio();
			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errornota);
			return response;
		}

		// verifico se lo stato della rendicontazione attuale e' corretto
		if (!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
				|| (!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
						.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
						&& !cronologiaprofilo.getProfilo().getListaazioni().get("Valida")[1])) {

			// errore stato non valido per rettifica
			String errorstato = null;
			errorstato = listeService.getMessaggio(SharedConstants.ERROR_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
					.replace("OPERAZIONE", "VALIDA").replace("STATOREND",
							"'" + listeService
									.getStatoRendicontazione(
											SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
									.getDescStatoRendicontazione() + "'");

			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errorstato);
			return response;
		} else {
			// Effettuo le dovute operazioni
			String newNotaEnte = "";
			String statoOld = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
			// Controllo e aggiorno lo stato della rendicontazione
			modificaStatoRendicontazione(rendicontazione, userInfo, SharedConstants.OPERAZIONE_VALIDA,
					cronologiaprofilo.getProfilo());
			String statoNew = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();

			if (!statoOld.equals(statoNew)) {
				newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
						.getTestoMessaggio().replace("OPERAZIONE", "VALIDA").replace("STATOOLD", "'" + statoOld + "'")
						.replace("STATONEW", "'" + statoNew + "'");
			}
//			if (cronologiaprofilo.getProfilo() != null
//					&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {
			if (Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaEnte())) {
				newNotaEnte = cronologiaprofilo.getCronologia().getNotaEnte();
			}
//			}
			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte)
					|| Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaInterna())) {
				GregTCronologia crono = new GregTCronologia();
				Timestamp dataModifica = new Timestamp(new Date().getTime());
				crono.setGregTRendicontazioneEnte(rendicontazione);
				crono.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
				crono.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				crono.setModello(cronologiaprofilo.getModello());
				crono.setUtenteOperazione(userInfo.getCodFisc());
				crono.setNotaInterna(cronologiaprofilo.getCronologia().getNotaInterna());
				crono.setNotaPerEnte(newNotaEnte);
				crono.setDataOra(dataModifica);
				crono.setDataCreazione(dataModifica);
				crono.setDataModifica(dataModifica);
				insertCronologia(crono);
			}

			response.setId(HttpStatus.OK.toString());
			return response;
		}
	}

	@Transactional
	public GenericResponse rendicontazioneStoricizza(Integer idSchedaEnte, ModelCronologiaProfilo cronologiaprofilo,
			UserInfo userInfo) throws Exception {

		GenericResponse response = new GenericResponse();

		// prendo la rendicontazione
		GregTRendicontazioneEnte rendicontazione = getRendicontazione(idSchedaEnte);
		if (cronologiaprofilo.getProfilo() != null
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[1]
				&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaRegionale")[0]
				&& (cronologiaprofilo.getCronologia().getNotaEnte() == null
						|| cronologiaprofilo.getCronologia().getNotaEnte().equals(""))) {
//		if ((ruoloEnte != null && ruoloEnte.equalsIgnoreCase(SharedConstants.RESPONSABILE_ENTE)) && 
//				(cronologia.getNotaEnte() == null || cronologia.getNotaEnte().equals(""))){
			// manca la nota per l'ente
			String errornota = listeService.getMessaggio(SharedConstants.ERROR_MANCA_NOTA_ENTE).getTestoMessaggio();
			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errornota);
			return response;
		}

		// verifico se lo stato della rendicontazione attuale e' corretto
		if (!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
				.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)
				|| (!rendicontazione.getGregDStatoRendicontazione().getCodStatoRendicontazione()
						.equalsIgnoreCase(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)
						&& !cronologiaprofilo.getProfilo().getListaazioni().get("Storicizza")[1])) {
			// errore stato non valido per rettifica
			String errorstato = null;
			errorstato = listeService.getMessaggio(SharedConstants.ERROR_VALIDA_STORICIZZA_DATI).getTestoMessaggio()
					.replace("OPERAZIONE", "STORICIZZA").replace("STATOREND",
							"'" + listeService.getStatoRendicontazione(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)
									.getDescStatoRendicontazione() + "'");

			response.setId(HttpStatus.BAD_REQUEST.toString());
			response.setDescrizione(errorstato);
			return response;
		} else {
			// Effettuo le dovute operazioni
			String newNotaEnte = "";
			String statoOld = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
			// Controllo e aggiorno lo stato della rendicontazione
			modificaStatoRendicontazione(rendicontazione, userInfo, SharedConstants.OPERAZIONE_STORICIZZA,
					cronologiaprofilo.getProfilo());

			String statoNew = rendicontazione.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();

			if (!statoOld.equals(statoNew)) {
				newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
						.getTestoMessaggio().replace("OPERAZIONE", "STORICIZZA")
						.replace("STATOOLD", "'" + statoOld + "'").replace("STATONEW", "'" + statoNew + "'");
			}
//			if (cronologiaprofilo.getProfilo() != null
//					&& cronologiaprofilo.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {
			if (Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaEnte())) {
				newNotaEnte = cronologiaprofilo.getCronologia().getNotaEnte();
			}
//			}
			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte)
					|| Checker.isValorizzato(cronologiaprofilo.getCronologia().getNotaInterna())) {
				GregTCronologia crono = new GregTCronologia();
				Timestamp dataModifica = new Timestamp(new Date().getTime());
				crono.setGregTRendicontazioneEnte(rendicontazione);
				crono.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
				crono.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				crono.setModello(cronologiaprofilo.getModello());
				crono.setUtenteOperazione(userInfo.getCodFisc());
				crono.setNotaInterna(cronologiaprofilo.getCronologia().getNotaInterna());
				crono.setNotaPerEnte(newNotaEnte);
				crono.setDataOra(dataModifica);
				crono.setDataCreazione(dataModifica);
				crono.setDataModifica(dataModifica);
				insertCronologia(crono);
			}

			response.setId(HttpStatus.OK.toString());
			return response;
		}
	}

//	@SuppressWarnings("rawtypes")
//	public ArrayList<ModelTranche> getTrancheEnte(Integer idEnte) {
//
//		List<Object> entiresult =  datiRendicontazioneDao.getTrancheEnte(idEnte);
//		ArrayList<ModelTranche> elencotab = new ArrayList<ModelTranche>();
//		Iterator itr = entiresult.iterator();
//		try {
//			while(itr.hasNext()){
//				Object[] obj = (Object[]) itr.next();
//				ModelTranche singolatab = new ModelTranche();
//				singolatab.setCodTranche(String.valueOf(obj[0]));
//				singolatab.setDesTranche(String.valueOf(obj[1]));
//				elencotab.add(singolatab);
//			}
//			return elencotab;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	@SuppressWarnings("rawtypes")
	public ModelTabTranche getTranchePerModelloEnte(Integer idRendicontazione, String modello) {

		List<Object> entiresult = datiRendicontazioneDao.getTranchePerModelloEnte(idRendicontazione, modello);

		ArrayList<ModelTabTranche> listaModelliAssociati = new ArrayList<ModelTabTranche>();
		Iterator<Object> itr = entiresult.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			listaModelliAssociati.add(new ModelTabTranche(obj, true));
		}
		if (listaModelliAssociati.size() > 0) {
			return listaModelliAssociati.get(0);
		} else {
			return null;
		}
	}

	@Transactional
	public GenericResponseWarnErr attivaControlliModelloII(ArrayList<ModelTabTranche> modelli, String tranche,
			GregTRendicontazioneEnte rendicontazione) {
		GenericResponseWarnErr elencoerrori = new GenericResponseWarnErr();
		elencoerrori.setWarnings(new ArrayList<String>());
		elencoerrori.setErrors(new ArrayList<String>());
		int idSchedaEnte = rendicontazione.getIdRendicontazioneEnte();
		String messaggio = "";

		// boolean comunecapoluogo =
		// rendicontazione.getGregTSchedeEntiGestori().getGregDTipoEnte().getCodTipoEnte().equalsIgnoreCase(SharedConstants.OPERAZIONE_COMUNE_CAPOLUOGO);

		// controllo presenza allegati, obbligatorio allegato I tranche
		// controllo allegato prima tranche
		boolean trovatoallegato = false;
		String numallegati = datiRendicontazioneDao.contaAllegati(SharedConstants.TRANCHEI,
				rendicontazione.getIdRendicontazioneEnte());
		trovatoallegato = Converter.getInt(numallegati) == 0 ? false : true;
		if (!trovatoallegato) {
			elencoerrori.getErrors().add(listeService.getMessaggio(SharedConstants.ERRORE_MANCA_ALLEGATO_1)
					.getTestoMessaggio().replace("TRANCHE", SharedConstants.PRIMA_TRANCHE));
			elencoerrori.setDescrizione(
					listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIA_KO).getTestoMessaggio());
			elencoerrori.setId("OK");
			return elencoerrori;
		} else {
			// se trovo allegato 1 perche' 2 non obbligatorio allora controllo se ci sono
			// dati nei modelli se vuoti chiedo se procedere se anche uno fosse pieno mi
			// blocco
			String modellitranche = "";
			String mancanzamodello = "";
			String condati = "";

			// valori diversi da null e da 0
			List<GregRRendicontazioneSpesaMissioneProgrammaMacro> listaMacroaggregati = macroaggregatiDao
					.getAllDatiModelloMacroaggregatiPerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazionePreg1Macro> listaModelloB1part1 = modelloB1Dao
					.getAllDatiModelloB1part1PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazionePreg1Utereg1> listaModelloB1part2 = modelloB1Dao
					.getAllDatiModelloB1part2PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazionePreg2Utereg2> listaModelloB1part3 = modelloB1Dao
					.getAllDatiModelloB1part3PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendMiProTitEnteGestoreModB> listaModelloB = modelloBDao
					.getAllDatiModelloBPerInvio(rendicontazione.getIdRendicontazioneEnte());
			// List<GregRRendicontazioneNonConformitaModB> listaConformitaModelloB =
			// modelloBDao.getAllDatiConformitaModelloBPerInvio(idSchedaEnte);
			List<GregRRendicontazioneModCParte1> listaModelloCpart1 = modelloCDao
					.getAllDatiModelloCpart1PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModCParte2> listaModelloCpart2 = modelloCDao
					.getAllDatiModelloCpart2PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModCParte3> listaModelloCpart3 = modelloCDao
					.getAllDatiModelloCpart3PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModCParte4> listaModelloCpart4 = modelloCDao
					.getAllDatiModelloCpart4PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModE> listaModelloE = modelloEDao
					.getAllDatiModelloEPerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModFParte1> listaModelloFpart1 = modelloFDao
					.getAllDatiModelloFpart1PerInvio(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneModFParte2> listaModelloFpart2 = modelloFDao
					.getAllDatiModelloFpart2PerInvio(rendicontazione.getIdRendicontazioneEnte());

			for (ModelTabTranche modello : modelli) {
				if (modello.getCodObbligo().equalsIgnoreCase(SharedConstants.OBBLIGATORIO)) {
					// preleva i dati dalle tabelle per vedere se esiste una rendicontazione sui
					// modelli
					if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_MA)) {
						if (listaMacroaggregati.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getDesTab() + ", ";
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "1";
						}

					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_B1)) {
						if (listaModelloB1part1.size() == 0 && listaModelloB1part2.size() == 0
								&& listaModelloB1part3.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getCodTab() + ", ";
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "1";
						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_B)) {
						if (listaModelloB.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getCodTab() + ", ";
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "1";
						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_C)) {
						if (listaModelloCpart1.size() == 0 && listaModelloCpart2.size() == 0
								&& listaModelloCpart3.size() == 0 && listaModelloCpart4.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getCodTab() + ", ";
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "1";
						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_E)) {
						// if (!comunecapoluogo) {
						if (listaModelloE.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getCodTab() + ", ";
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "1";
						}
//						}
//						else {
//							condati = condati + modello.getCodTab() + ", ";
//							modellitranche = modellitranche + "2";
//						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_F)) {
						if (listaModelloFpart1.size() == 0 && listaModelloFpart2.size() == 0) {
							modellitranche = modellitranche + "0";
							mancanzamodello = mancanzamodello + modello.getCodTab() + ", ";
						} else {
							condati = condati + modello.getCodTab() + ", ";
							modellitranche = modellitranche + "1";
						}
					}
				} else {
					modellitranche = modellitranche + "2";
				}
			}
			if (!modellitranche.contains("1")) {
				// ci sono gli allegati e sono tutti vuoti popup messaggio per continuare esci
				messaggio = listeService.getMessaggio(SharedConstants.ERROR_INVIO_ALLEGATO_NO_MODELLISIDOC)
						.getTestoMessaggio() + ";";
				messaggio = messaggio + listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_TRANCHE)
						.getTestoMessaggio().replace("TRANCHE", SharedConstants.PRIMA_TRANCHE);
				elencoerrori.setDescrizione(messaggio);
				elencoerrori.setId("KO");
				return elencoerrori;
			} else if (!modellitranche.contains("0")) {
				// ci sono gli allegati e sono tutti pieni ok continua controlli
				for (ModelTabTranche modello : modelli) {
					if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_MA)) {
						// Controlli Modello Macroaggregati
						Boolean trovataIncongRedditoAcquistoB = false;
						ModelRendicontazioneMacroaggregati rendicontazioneMacro = macroaggregatiService
								.getRendicontazioneMacroaggregatiByIdScheda(idSchedaEnte);
						for (ModelValoriMacroaggregati missioneMacro : rendicontazioneMacro.getValoriMacroaggregati()) {
							BigDecimal valoreReddito = null;
							BigDecimal valoreAcquistoB = null;
							if (missioneMacro.getCampi() != null && missioneMacro.getCampi().size() > 0) {
								for (ModelCampiMacroaggregati campoMacro : missioneMacro.getCampi()) {
									if (campoMacro.getCod().equals("1VB") && campoMacro.getValue() != null) {
										valoreReddito = campoMacro.getValue();
									}
									if (campoMacro.getCod().equals("4VB") && campoMacro.getValue() != null) {
										valoreAcquistoB = campoMacro.getValue();
									}
								}
								// verifico se per ogni missione e' valorizzato il campo acquisto B ma campo
								// reddito non e' valorizzato
								if (valoreReddito == null && valoreAcquistoB != null) {
									trovataIncongRedditoAcquistoB = true;
								}
							}
						}
						if (trovataIncongRedditoAcquistoB) {
							elencoerrori.getErrors().add(listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMACRO_01).getTestoMessaggio());
						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_B1)) {
						// Controlli Modello B1
						List<ModelB1Voci> rendModB1 = modelloB1Service.getVoci(idSchedaEnte);

						// CONTROLLO 1, per ogni Prestazione il campo acquisto beni B deve essere
						// valorizzato se campo reddito e' valorizzato
						for (ModelB1Voci prestazione : rendModB1) {
							BigDecimal valReddito = null;
							BigDecimal valAcquistoB = null;
							for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
								if (macroaggregato.getCodice().equals("1VB") && macroaggregato.getValore() != null) {
									valReddito = Util
											.convertStringToBigDecimal(macroaggregato.getValore().replace(".", ","));
								}
								if (macroaggregato.getCodice().equals("4VB") && macroaggregato.getValore() != null) {
									valAcquistoB = Util
											.convertStringToBigDecimal(macroaggregato.getValore().replace(".", ","));
								}
							}
							if (valReddito == null && valAcquistoB != null) {
								elencoerrori.getErrors()
										.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_01)
												.getTestoMessaggio()
												.replace("PRESTAZIONE", "'" + prestazione.getCodPrestazione() + " "
														+ prestazione.getDescPrestazione() + "'"));
							}
						}

						// CONTROLLO 2, verifica Totali Progressivi Automatici coincidano con Totali
						// Macroaggregati
						List<ModelTotaleMacroaggregati> totaliMacro = macroaggregatiService
								.getRendicontazioneTotaliMacroaggregatiPerB1(idSchedaEnte).getValoriMacroaggregati();
						BigDecimal totaleRedditoDipendente = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
						BigDecimal totaleAltreImposte = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
						BigDecimal totaleAcquistoBeniA = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
						BigDecimal totaleAcquistoBeniB = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
						BigDecimal totaleTrasferimentiCorrenti = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
						BigDecimal totaleInteressiPassivi = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
						BigDecimal totaleAltreSpese = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

						for (ModelB1Voci prestazione : rendModB1) {
							for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
								if (macroaggregato.getCodice().equals("1VB") && macroaggregato.getValore() != null) {
									totaleRedditoDipendente = totaleRedditoDipendente
											.add(new BigDecimal(macroaggregato.getValore())
													.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								}
								if (macroaggregato.getCodice().equals("2VB") && macroaggregato.getValore() != null) {
									totaleAltreImposte = totaleAltreImposte
											.add(new BigDecimal(macroaggregato.getValore())
													.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								}
								if (macroaggregato.getCodice().equals("3VB") && macroaggregato.getValore() != null) {
									totaleAcquistoBeniA = totaleAcquistoBeniA
											.add(new BigDecimal(macroaggregato.getValore())
													.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								}
								if (macroaggregato.getCodice().equals("4VB") && macroaggregato.getValore() != null) {
									totaleAcquistoBeniB = totaleAcquistoBeniB
											.add(new BigDecimal(macroaggregato.getValore())
													.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								}
								if (macroaggregato.getCodice().equals("5VB") && macroaggregato.getValore() != null) {
									totaleTrasferimentiCorrenti = totaleTrasferimentiCorrenti
											.add(new BigDecimal(macroaggregato.getValore())
													.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								}
								if (macroaggregato.getCodice().equals("6VB") && macroaggregato.getValore() != null) {
									totaleInteressiPassivi = totaleInteressiPassivi
											.add(new BigDecimal(macroaggregato.getValore())
													.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								}
								if (macroaggregato.getCodice().equals("7VB") && macroaggregato.getValore() != null) {
									totaleAltreSpese = totaleAltreSpese.add(new BigDecimal(macroaggregato.getValore())
											.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
								}
							}
						}
						if (!totaleRedditoDipendente.toPlainString().equals(
								totaliMacro.get(0).getTotale().setScale(2, RoundingMode.HALF_UP).toPlainString())
								|| !totaleAltreImposte.toPlainString()
										.equals(totaliMacro.get(1).getTotale().setScale(2, RoundingMode.HALF_UP)
												.toPlainString())
								|| !totaleAcquistoBeniA.toPlainString()
										.equals(totaliMacro.get(2).getTotale().setScale(2, RoundingMode.HALF_UP)
												.toPlainString())
								|| !totaleAcquistoBeniB.toPlainString()
										.equals(totaliMacro.get(3).getTotale().setScale(2, RoundingMode.HALF_UP)
												.toPlainString())
								|| !totaleTrasferimentiCorrenti.toPlainString()
										.equals(totaliMacro.get(4).getTotale().setScale(2, RoundingMode.HALF_UP)
												.toPlainString())
								|| !totaleInteressiPassivi.toPlainString()
										.equals(totaliMacro.get(5).getTotale().setScale(2, RoundingMode.HALF_UP)
												.toPlainString())
								|| !totaleAltreSpese.toPlainString().equals(totaliMacro.get(6).getTotale()
										.setScale(2, RoundingMode.HALF_UP).toPlainString())) {
							elencoerrori.getErrors().add(listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_02).getTestoMessaggio());

						}

						// CONTROLLO 3, verifica Totali Prestazioni coincidente con Totali Utenze
						for (ModelB1Voci prestazione : rendModB1) {
							BigDecimal totalePrestazione = BigDecimal.ZERO;
							BigDecimal totaleUtenza = BigDecimal.ZERO;
							// Totale Prestazione
							for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
								if (macroaggregato.getValore() != null) {
									totalePrestazione = totalePrestazione.add(Util
											.convertStringToBigDecimal(macroaggregato.getValore().replace(".", ",")));
								}
							}
							// Totale Utenza
							if (prestazione.getTipoPrestazione().equals("MA03")) {
								for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
									if (utenza.getValore() != null) {
										totaleUtenza = totaleUtenza.add(
												Util.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
									}
								}
							} else {
								for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
									if (utenza.getValore() != null) {
										totaleUtenza = totaleUtenza.add(
												Util.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
									}
								}
							}

							if (!totalePrestazione.equals(totaleUtenza)) {
								elencoerrori.getErrors()
										.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_03)
												.getTestoMessaggio()
												.replace("PRESTAZIONE", "'" + prestazione.getCodPrestazione() + " "
														+ prestazione.getDescPrestazione() + "'"));
							}
						}

						// Verifica somma utenze prestazioni regionali 2 con utenze prestazioni
						// regionali 1
						for (ModelB1Voci prestazione : rendModB1) {
							if (prestazione.getUtenze().size() > 0) {
								BigDecimal[] totaliUtenzePrest2 = new BigDecimal[prestazione.getUtenze().size()];
								for (int i = 0; i < prestazione.getUtenze().size(); i++) {
									totaliUtenzePrest2[i] = BigDecimal.ZERO.setScale(2);
								}
								for (ModelB1VociPrestReg2 prest2 : prestazione.getPrestazioniRegionali2()) {
									for (int i = 0; i < prest2.getUtenze().size(); i++) {
										totaliUtenzePrest2[i] = totaliUtenzePrest2[i].add(Util
												.convertStringToBigDecimal(prest2.getUtenze().get(i).getValore() != null
														? prest2.getUtenze().get(i).getValore().replace(".", ",")
														: "0,00"));
									}
								}
								for (int i = 0; i < prestazione.getUtenze().size(); i++) {
									if (!totaliUtenzePrest2[i].equals(BigDecimal.ZERO.setScale(2))
											&& totaliUtenzePrest2[i] != null
											&& !Util.convertStringToBigDecimal(
													prestazione.getUtenze().get(i).getValore() != null
															? prestazione.getUtenze().get(i).getValore().replace(".",
																	",")
															: "0,00")
													.equals(totaliUtenzePrest2[i])) {
										elencoerrori.getWarnings().add(listeService
												.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_10)
												.getTestoMessaggio()
												.replace("PRESTAZIONE",
														"'" + prestazione.getCodPrestazione() + " "
																+ prestazione.getDescPrestazione() + "'")
												.replace("UTENZA",
														"'" + prestazione.getUtenze().get(i).getDescUtenza() + "'"));
									}
								}
							} else if (prestazione.getUtenzeCostoTotale().size() > 0) {
								BigDecimal[] totaliUtenzePrest2 = new BigDecimal[prestazione.getUtenzeCostoTotale()
										.size()];
								for (int i = 0; i < prestazione.getUtenzeCostoTotale().size(); i++) {
									totaliUtenzePrest2[i] = BigDecimal.ZERO.setScale(2);
								}
								for (ModelB1VociPrestReg2 prest2 : prestazione.getPrestazioniRegionali2()) {
									for (int i = 0; i < prest2.getUtenze().size(); i++) {
										totaliUtenzePrest2[i] = totaliUtenzePrest2[i].add(Util
												.convertStringToBigDecimal(prest2.getUtenze().get(i).getValore() != null
														? prest2.getUtenze().get(i).getValore().replace(".", ",")
														: "0,00"));
									}
								}
								for (int i = 0; i < prestazione.getUtenzeCostoTotale().size(); i++) {
									if (!totaliUtenzePrest2[i].equals(BigDecimal.ZERO.setScale(2))
											&& totaliUtenzePrest2[i] != null
											&& !Util.convertStringToBigDecimal(
													prestazione.getUtenzeCostoTotale().get(i).getValore() != null
															? prestazione.getUtenzeCostoTotale().get(i).getValore()
																	.replace(".", ",")
															: "0,00")
													.equals(totaliUtenzePrest2[i])) {
										elencoerrori.getWarnings().add(listeService
												.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_10)
												.getTestoMessaggio()
												.replace("PRESTAZIONE",
														"'" + prestazione.getCodPrestazione() + " "
																+ prestazione.getDescPrestazione() + "'")
												.replace("UTENZA",
														"'" + prestazione.getUtenzeCostoTotale().get(i).getDescUtenza()
																+ "'"));
									}
								}
							}
						}

						// CONTROLLO 4, verifica tra Entrate (ASR,UT) e Spese (QS) tra Modello A e
						// Modello B1
						Map<String, Map<String, BigDecimal>> prestUteValModA = new HashMap<String, Map<String, BigDecimal>>();
						Map<String, BigDecimal> uteValModA = null;
						// Dal Modello A calcolo le entrate (ASR e UT) per ogni utenza
						List<ModelTitoloModA> titoliModA = modelloAService.getListaVociModA(idSchedaEnte)
								.getListaTitoli();
						for (ModelTitoloModA titolo : titoliModA) {
							for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
								for (ModelVoceModA voce : tipologia.getListaVoci()) {
									if (voce.getPrestazioni() != null) {
										// Considero le Prestazioni ASR
										List<ModelPrestazioneUtenzaModA> prestazioniASR = voce.getPrestazioni()
												.getPrestazioniRS();
										for (ModelPrestazioneUtenzaModA prestazione : prestazioniASR) {
											String codPrestASR = prestazione.getCodPrestazione().replace("_ASR", "")
													.replace("_UT", "");
											// Creo o prelevo la Mappa <CodUtenza, Valore> e ciclo sulle utenze
											if (prestUteValModA.get(codPrestASR) != null) {
												uteValModA = prestUteValModA.get(codPrestASR);
											} else {
												uteValModA = new HashMap<String, BigDecimal>();
											}
											for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
												if (utenza.getValore() != null) {
													if (uteValModA.get(utenza.getCodTargetUtenza()) != null) {
														BigDecimal oldValue = uteValModA
																.get(utenza.getCodTargetUtenza());
														BigDecimal newValue = utenza.getValore();
														uteValModA.replace(utenza.getCodTargetUtenza(),
																oldValue.add(newValue));
													} else {
														uteValModA.put(utenza.getCodTargetUtenza(), utenza.getValore());
													}
												}
											}
											if (prestUteValModA.get(codPrestASR) != null) {
												prestUteValModA.replace(codPrestASR, uteValModA);
											} else {
												prestUteValModA.put(codPrestASR, uteValModA);
											}
										}
										// Considero le Prestazioni UT
										List<ModelPrestazioneUtenzaModA> prestazioniUT = voce.getPrestazioni()
												.getPrestazioniCD();
										for (ModelPrestazioneUtenzaModA prestazione : prestazioniUT) {
											String codPrestUT = prestazione.getCodPrestazione().replace("_ASR", "")
													.replace("_UT", "");
											// Creo o prelevo la Mappa <CodUtenza, Valore> e ciclo sulle utenze
											if (prestUteValModA.get(codPrestUT) != null) {
												uteValModA = prestUteValModA.get(codPrestUT);
											} else {
												uteValModA = new HashMap<String, BigDecimal>();
											}
											for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
												if (utenza.getValore() != null) {
													if (uteValModA.get(utenza.getCodTargetUtenza()) != null) {
														BigDecimal oldValue = uteValModA
																.get(utenza.getCodTargetUtenza());
														BigDecimal newValue = utenza.getValore();
														uteValModA.replace(utenza.getCodTargetUtenza(),
																oldValue.add(newValue));
													} else {
														uteValModA.put(utenza.getCodTargetUtenza(), utenza.getValore());
													}
												}
											}
											if (prestUteValModA.get(codPrestUT) != null) {
												prestUteValModA.replace(codPrestUT, uteValModA);
											} else {
												prestUteValModA.put(codPrestUT, uteValModA);
											}
										}
									}
								}
							}
						}

						for (String uteMapKey : prestUteValModA.keySet()) {
							Map<String, BigDecimal> mappa = prestUteValModA.get(uteMapKey);
							ModelB1Voci prestModB1 = new ModelB1Voci();
							for (ModelB1Voci prestazione : rendModB1) {
								if (prestazione.getCodPrestazione().equals(uteMapKey)) {
									prestModB1 = prestazione;
								}
							}
							// Ciclo sui valori ed effettuo il controllo
							// Ciclo sulle entrate del Mod.A
							Boolean entrateVal = false;
							BigDecimal entrateModA = BigDecimal.ZERO;
							for (BigDecimal value : mappa.values()) {
								if (value != null) {
									entrateVal = true;
									entrateModA = entrateModA.add(value);
								}
							}
							// Ciclo sulle spese del Mod.B1
							Boolean usciteVal = false;
							Boolean usciteObbligVal = true;
							BigDecimal usciteModB1 = BigDecimal.ZERO;
							if (prestModB1.getTipoPrestazione().equals("MA03")) {
								for (ModelB1UtenzaPrestReg1 entryValue : prestModB1.getUtenzeCostoTotale()) {
									if (entryValue.getValore() != null) {
										if (entryValue.isMandatory()) {
											usciteObbligVal = usciteObbligVal && true;
										}
										usciteVal = true;
										usciteModB1 = usciteModB1.add(Util
												.convertStringToBigDecimal(entryValue.getValore().replace(".", ",")));
									} else {
										if (entryValue.isMandatory()) {
											usciteObbligVal = usciteObbligVal && false;
										}
									}
								}
							} else {
								for (ModelB1UtenzaPrestReg1 entryValue : prestModB1.getUtenze()) {
									if (entryValue.getValore() != null) {
										usciteVal = true;
										usciteModB1 = usciteModB1.add(Util
												.convertStringToBigDecimal(entryValue.getValore().replace(".", ",")));
									}
								}
							}
							// CONTROLLO 4.1
							if (!usciteObbligVal) {
								elencoerrori.getErrors()
										.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_04)
												.getTestoMessaggio()
												.replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione() + " "
														+ prestModB1.getDescPrestazione() + "'"));
							}

							// CONTROLLO 4.2
							if (!entrateModA.equals(BigDecimal.ZERO) && !usciteModB1.equals(BigDecimal.ZERO)
									&& usciteModB1.compareTo(entrateModA) == -1) {
								elencoerrori.getErrors()
										.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_05)
												.getTestoMessaggio()
												.replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione() + " "
														+ prestModB1.getDescPrestazione() + "'"));
							}

							// CONTROLLO 4.3
							BigDecimal diffUsciteEntrate = usciteModB1.subtract(entrateModA).setScale(2,
									RoundingMode.HALF_UP);
							if (!entrateModA.equals(BigDecimal.ZERO) && !usciteModB1.equals(BigDecimal.ZERO)
									&& diffUsciteEntrate.compareTo(BigDecimal.ZERO) == -1) {
								elencoerrori.getErrors()
										.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_06)
												.getTestoMessaggio()
												.replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione() + " "
														+ prestModB1.getDescPrestazione() + "'"));
							}

							// CONTROLLO 4.4 WARNING
							if (!entrateVal && usciteVal) {
								elencoerrori.getWarnings()
										.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_08)
												.getTestoMessaggio()
												.replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione() + " "
														+ prestModB1.getDescPrestazione() + "'"));
							}
						}

						// CONTROLLO 5, Controllo su Spese Prestazioni e 1% del Totale Macroaggregati
						// Calcolo 1% del Totale Macroaggregati
						BigDecimal percentValue = BigDecimal.ZERO;
						List<ModelTotaleMacroaggregati> totaliMacroaggregati = macroaggregatiService
								.getRendicontazioneTotaliMacroaggregatiPerB1(idSchedaEnte).getValoriMacroaggregati();
						for (ModelTotaleMacroaggregati totale : totaliMacroaggregati) {
							if (totale.getCodMacroaggregati().equals("")
									&& totale.getDescMacroaggregati().equals("TOTALE")) {
								percentValue = totale.getTotale().multiply(new BigDecimal(0.01)).setScale(2,
										RoundingMode.HALF_UP);
							}
						}
						for (ModelB1Voci prestazione : rendModB1) {
							if (prestazione.getCodPrestazione().equals("R_A.2.3")
									|| prestazione.getCodPrestazione().equals("R_B.8.2")
									|| prestazione.getCodPrestazione().equals("R_C.3.4")
									|| prestazione.getCodPrestazione().equals("R_D.1.2")
									|| prestazione.getCodPrestazione().equals("R_E.4.2")) {

								BigDecimal totaleUtenza = BigDecimal.ZERO;

								if (prestazione.getTipoPrestazione().equals("MA03")) {
									for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
										if (utenza.getValore() != null) {
											totaleUtenza = totaleUtenza.add(Util
													.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
										}
									}
								} else {
									for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
										if (utenza.getValore() != null) {
											totaleUtenza = totaleUtenza.add(Util
													.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
										}
									}
								}

								if (totaleUtenza.compareTo(percentValue) == 1) {
									elencoerrori.getWarnings()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_07)
													.getTestoMessaggio()
													.replace("PRESTAZIONE", "'" + prestazione.getCodPrestazione() + " "
															+ prestazione.getDescPrestazione() + "'"));
								}
							}
						}

						// CONTROLLO 6, verifica valorizzazioni per Macroaggregati e Utenze, per ogni
						// Prestazione
						String prestNotVal = "";
						for (ModelB1Voci prestazione : rendModB1) {
							Boolean macroValorizzato = false;
							Boolean utenzaValorizzata = false;
							// Valorizzato Macroaggregato
							for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
								if (macroaggregato.getValore() != null) {
									macroValorizzato = true;
								}
							}
							// Valorizzata Utenza
							if (prestazione.getTipoPrestazione().equals("MA03")) {
								for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
									if (utenza.getValore() != null) {
										utenzaValorizzata = true;
									}
								}
							} else {
								for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
									if (utenza.getValore() != null) {
										utenzaValorizzata = true;
									}
								}
							}

							if (!macroValorizzato && !utenzaValorizzata) {
								prestNotVal = prestNotVal + prestazione.getCodPrestazione() + ", ";
							}
						}
						if (!prestNotVal.equals("")) {
							elencoerrori.getErrors()
									.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_09)
											.getTestoMessaggio().replace("PRESTAZIONI",
													prestNotVal.substring(0, prestNotVal.length() - 2)));
						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_B)) {
						// Controlli Modello B
						ModelRendicontazioneModB rendModB = modelloBService.getRendicontazioneModB(idSchedaEnte);
						ModelRendicontazioneTotaliMacroaggregati totaliMacroaggregati = macroaggregatiService
								.getRendicontazioneTotaliMacroaggregatiPerB1(idSchedaEnte);
						ModelRendicontazioneTotaliSpeseMissioni totaliSpeseMissione = macroaggregatiService
								.getRendicontazioneTotaliSpesePerB(idSchedaEnte);
						String errorMessageB = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMACRO_02)
								.getTestoMessaggio();

						// CONTROLLO 1, Verifica TotaleSpesaCorrenteMis12 e Mis04(dicui)
						// prelevo gli importi di cui, Missione 04 Programma 06 Titolo 1
						BigDecimal sommaDicuiMiss04Prog06Tit1 = BigDecimal.ZERO.setScale(2);
						for (ModelBMissioni missione : rendModB.getListaMissioni()) {
							if (missione.getCodMissione().equals("04")) {
								for (ModelBProgramma programma : missione.getListaProgramma()) {
									if (programma.getCodProgramma().equals("0406")) {
										for (ModelBTitolo titolo : programma.getListaTitolo()) {
											if (titolo.getCodTitolo().equals("1")) {
												for (ModelBSottotitolo sottotitolo : titolo.getListaSottotitolo()) {
													if (sottotitolo.getCodSottotitolo().equals("11")
															|| sottotitolo.getCodSottotitolo().equals("12")) {
														if (sottotitolo.getValore() != null) {
															sommaDicuiMiss04Prog06Tit1 = sommaDicuiMiss04Prog06Tit1
																	.add(sottotitolo.getValore());
														}
													}
												}
											}
										}
									}
								}
							}
						}

						// Calcolo Totale Missione 12, partendo dagli importi del Modello B1 (devo poi
						// sottrarre i due di cui come come nella webapp per Prog1/Tit1 e Prog2/Tit1)
						BigDecimal totaleMissione12 = BigDecimal.ZERO.setScale(2);
						List<ModelB1ProgrammiMissioneTotali> importiMissione12FromModB1 = modelloB1Service
								.getProgrammiMissioneTotali(idSchedaEnte);
						for (ModelB1ProgrammiMissioneTotali importoMissione : importiMissione12FromModB1) {
							if (importoMissione.getValore() != null) {
								totaleMissione12 = totaleMissione12.add(importoMissione.getValore());
							}
						}
						totaleMissione12 = totaleMissione12.subtract(sommaDicuiMiss04Prog06Tit1).setScale(2,
								RoundingMode.HALF_UP);

						// Calcolo il TotaleSpesaCorrenteMis12 e lo confronto con il
						// TotaleMacroaggregati
						BigDecimal totaleSpesaCorrenteMis12Mis04Dicui = BigDecimal.ZERO.setScale(2);
						totaleSpesaCorrenteMis12Mis04Dicui = totaleMissione12.add(sommaDicuiMiss04Prog06Tit1);
						// Prelevo il totale Macroaggregati
						BigDecimal totaleMissioniMacro = BigDecimal.ZERO.setScale(2);
						totaleMissioniMacro = totaleMissioniMacro
								.add(macroaggregatiService.getRendicontazioneTotaliMacroaggregatiPerB1(idSchedaEnte)
										.getValoriMacroaggregati().get(7).getTotale());
						// verifico che il totaleMissioni Macro sia uguale al (totaleSpesaCorrenteMis12
						// + di cui Miss04)
						if (!totaleSpesaCorrenteMis12Mis04Dicui.equals(totaleMissioniMacro)) {
							String errMsgB1 = errorMessageB;
							elencoerrori.getErrors().add(errMsgB1.replace("QUALEIMPORTO",
									"TOTALE SPESA CORRENTE della MISSIONE 12 E della MISSIONE 04 (solo la parte concernente il Sostegno Socio Educativo Scolastico per minori stranieri e minori disabili - compreso Trasporto scolastico Disabili)")
									.replace("IMPORTO",
											Util.convertBigDecimalToString(
													totaliMacroaggregati.getValoriMacroaggregati().get(7).getTotale()))
									.replace("MODELLO", "Macroaggregati"));
						}

						// CONTROLLO 2, Verifico TotaleSpesaCorrenteTutteMissioni
						BigDecimal totaleSpesaCorrenteTutteMissioni = BigDecimal.ZERO.setScale(2);
						for (ModelBMissioni missione : rendModB.getListaMissioni()) {
							for (ModelBProgramma programma : missione.getListaProgramma()) {
								for (ModelBTitolo titolo : programma.getListaTitolo()) {
									if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
										totaleSpesaCorrenteTutteMissioni = totaleSpesaCorrenteTutteMissioni
												.add(titolo.getValore());
									}
								}
							}
						}
						BigDecimal totaliTutteMissioniMacroaggregati = totaliSpeseMissione.getValoriSpese().get(3)
								.getTotale().setScale(2);
						if (!totaleSpesaCorrenteTutteMissioni.equals(totaliTutteMissioniMacroaggregati)) {
							String errMsgB2 = errorMessageB;
							elencoerrori.getErrors()
									.add(errMsgB2.replace("QUALEIMPORTO", "TOTALE SPESA CORRENTE DI TUTTE LE MISSIONI")
											.replace("IMPORTO",
													Util.convertBigDecimalToString(
															totaliSpeseMissione.getValoriSpese().get(3).getTotale()))
											.replace("MODELLO", "Macroaggregati"));
						}

						// CONTROLLO 3, Verifica TotaleSpesaCorrenteMissione01
						BigDecimal totaleSpesaCorrenteMissione1 = BigDecimal.ZERO.setScale(2);
						for (ModelBMissioni missione : rendModB.getListaMissioni()) {
							if (missione.getCodMissione().equals("01")) {
								for (ModelBProgramma programma : missione.getListaProgramma()) {
									for (ModelBTitolo titolo : programma.getListaTitolo()) {
										if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
											totaleSpesaCorrenteMissione1 = totaleSpesaCorrenteMissione1
													.add(titolo.getValore());
										}
									}
								}
							}
						}
						BigDecimal totaleMissione1Macroaggregati = totaliSpeseMissione.getValoriSpese().get(0)
								.getTotale().setScale(2);
						if (!totaleSpesaCorrenteMissione1.equals(totaleMissione1Macroaggregati)) {
							String errMsgB3 = errorMessageB;
							elencoerrori.getErrors()
									.add(errMsgB3.replace("QUALEIMPORTO", "TOTALE SPESE CORRENTI della MISSIONE 01")
											.replace("IMPORTO",
													Util.convertBigDecimalToString(
															totaliSpeseMissione.getValoriSpese().get(0).getTotale()))
											.replace("MODELLO", "Macroaggregati"));
						}

						// CONTROLLO 4, Verifica Missione04Programma06Titolo1
						BigDecimal totaleSpesaMissione = BigDecimal.ZERO.setScale(2);
						totaleSpesaMissione = totaleSpesaMissione
								.add(totaliSpeseMissione.getValoriSpese().get(2).getTotale().setScale(2));
						if (!sommaDicuiMiss04Prog06Tit1.equals(totaleSpesaMissione)) {
							String errMsgB4 = errorMessageB;
							elencoerrori.getErrors()
									.add(errMsgB4.replace("QUALEIMPORTO", "valore di Missione04/Programma06/Titolo1")
											.replace("IMPORTO",
													Util.convertBigDecimalToString(
															totaliSpeseMissione.getValoriSpese().get(2).getTotale()))
											.replace("MODELLO", "Macroaggregati"));
						}

						// CONTROLLO 5, Verifica TotaleSpesaCorrenteMissione12
						BigDecimal totaleSpesaCorrenteMissione12 = BigDecimal.ZERO.setScale(2);
						for (ModelBMissioni missione : rendModB.getListaMissioni()) {
							if (missione.getCodMissione().equals("12")) {
								for (ModelBProgramma programma : missione.getListaProgramma()) {
									for (ModelBTitolo titolo : programma.getListaTitolo()) {
										if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
											totaleSpesaCorrenteMissione12 = totaleSpesaCorrenteMissione12
													.add(titolo.getValore());
										}
									}
								}
							}

						}
						BigDecimal totaleMissione12Macroaggregati = totaliSpeseMissione.getValoriSpese().get(1)
								.getTotale().setScale(2);
						if (!totaleSpesaCorrenteMissione12.equals(totaleMissione12Macroaggregati)) {
							String errMsgB5 = errorMessageB;
							elencoerrori.getErrors()
									.add(errMsgB5.replace("QUALEIMPORTO", "TOTALE SPESE CORRENTI della MISSIONE 12")
											.replace("IMPORTO",
													Util.convertBigDecimalToString(
															totaliSpeseMissione.getValoriSpese().get(1).getTotale()))
											.replace("MODELLO",
													"Macroaggregati (valore Missione 01 - parte spesa socio-assistenziale sommato a valore Missione 12)"));
						}

						// CONTROLLI WARNINGS AL SALVATAGGIO DA RIPROPORRE ALL'INVIA (all'Invia solo
						// Bloccanti)
//						List<String> warningsModB = new ArrayList<String>();
//						try {
//							warningsModB = controllaImportiModelloBPerWarningInviaII(rendModB, idSchedaEnte);
//						} catch (IntegritaException e) {
//							e.printStackTrace();
//						}
//						if(warningsModB != null && warningsModB.size() != 0) {
//							for(String warning : warningsModB) {
//								elencoerrori.getWarnings().add(warning);
//							}
//						}
					} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_C)) {
						// Controlli Modello C
						ModelRendicontazioneModC rendModC = modelloCService.getRendicontazioneModC(idSchedaEnte);
						String errorMessageC1 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_01)
								.getTestoMessaggio();
						String errorMessageC2 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_02)
								.getTestoMessaggio();
						String errorMessageC3 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_03)
								.getTestoMessaggio();
						String errorMessageC4 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_04)
								.getTestoMessaggio();
						String errorMessageC5 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_05)
								.getTestoMessaggio();
						String errorMessageC6 = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_06)
								.getTestoMessaggio();

						// CONTROLLO 1, Controllo NucleiFamiliari non deve superare somma target utenza
						for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
							BigDecimal numNucleiFamiliari = BigDecimal.ZERO;
							BigDecimal totaleTargetUtenze = BigDecimal.ZERO;
							for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
								if (!targetUtenza.getCodTargetUtenze().equals("U28")
										&& targetUtenza.getValore() != null) {
									totaleTargetUtenze = totaleTargetUtenze.add(targetUtenza.getValore());
								} else if (targetUtenza.getCodTargetUtenze().equals("U28")
										&& targetUtenza.getValore() != null) {
									numNucleiFamiliari = numNucleiFamiliari.add(targetUtenza.getValore());
								}
							}
							if (!numNucleiFamiliari.equals(BigDecimal.ZERO)
									&& !totaleTargetUtenze.equals(BigDecimal.ZERO)
									&& numNucleiFamiliari.compareTo(totaleTargetUtenze) == 1) {
								String errMsgC1 = errorMessageC1;
								elencoerrori.getErrors()
										.add(errMsgC1.replace("PRESTAZIONE", prestazione.getDescPrestazione()));
							}
						}

						// CONTROLLO 2, Controllo somma valori di cui inferiore al totale di
						// riferimento, Prest. Serv. Soc. Professionale
						for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
							if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
								for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
									BigDecimal totaleTargetUtenza = targetUtenza.getValore() != null
											? targetUtenza.getValore()
											: BigDecimal.ZERO;
									for (ModelCDettagliUtenze dettaglioUtenza : targetUtenza.getListaDettagliUtenze()) {
										if (dettaglioUtenza.getValore() != null
												&& dettaglioUtenza.getValore().compareTo(totaleTargetUtenza) == 1) {
											String errMsgC2 = errorMessageC2;
											elencoerrori.getErrors()
													.add(errMsgC2
															.replace("DICUI", dettaglioUtenza.getDescDettagliUtenze())
															.replace("UTENZA", targetUtenza.getDescTargetUtenze()));
										}

									}
								}
							}
						}

						// CONTROLLO 3, Controllo TotaleMinoriDisabili con TotalePerDisabilita
						for (ModelCPrestazioni prestazione1 : rendModC.getListaPrestazioni()) {
							if (prestazione1.getCodPrestazione().equals("R_A.2.1")) {
								for (ModelCTargetUtenze targetUtenza1 : prestazione1.getListaTargetUtenze()) {
									if (targetUtenza1.getCodTargetUtenze().equals("U23")) {
										BigDecimal totaleTargetUtenzaDisabilitaMinori = targetUtenza1
												.getValore() != null ? targetUtenza1.getValore() : BigDecimal.ZERO;
										BigDecimal totaleSpecificaDisabilitaMinori = BigDecimal.ZERO;
										for (ModelCDisabilita disabilita1 : targetUtenza1.getListaDisabilita()) {
											if (!disabilita1.getCodDisabilita().equals("DIS08")
													&& disabilita1.getValore() != null) {
												totaleSpecificaDisabilitaMinori = totaleSpecificaDisabilitaMinori
														.add(disabilita1.getValore());
											}
										}
										if (!totaleSpecificaDisabilitaMinori.equals(BigDecimal.ZERO)
												&& !totaleTargetUtenzaDisabilitaMinori.equals(BigDecimal.ZERO)
												&& !totaleSpecificaDisabilitaMinori
														.equals(totaleTargetUtenzaDisabilitaMinori)) {
											String errMsgC3 = errorMessageC3;
											elencoerrori.getErrors().add(errMsgC3.replaceAll("MINADUL", "Minori")
													.replace("PRESTAZIONE", prestazione1.getDescPrestazione()));
										}
									}
								}
							}
						}

						// CONTROLLO 4, Controllo TotaleAdultiDisabili con TotalePerDisabilita
						for (ModelCPrestazioni prestazione2 : rendModC.getListaPrestazioni()) {
							if (prestazione2.getCodPrestazione().equals("R_A.2.1")) {
								for (ModelCTargetUtenze targetUtenza2 : prestazione2.getListaTargetUtenze()) {
									if (targetUtenza2.getCodTargetUtenze().equals("U25")) {
										BigDecimal totaleTargetUtenzaDisabilitaAdulti = targetUtenza2
												.getValore() != null ? targetUtenza2.getValore() : BigDecimal.ZERO;
										BigDecimal totaleSpecificaDisabilitaAdulti = BigDecimal.ZERO;
										for (ModelCDisabilita disabilita2 : targetUtenza2.getListaDisabilita()) {
											if (!disabilita2.getCodDisabilita().equals("DIS09")
													&& disabilita2.getValore() != null) {
												totaleSpecificaDisabilitaAdulti = totaleSpecificaDisabilitaAdulti
														.add(disabilita2.getValore());
											}
										}
										if (!totaleSpecificaDisabilitaAdulti.equals(BigDecimal.ZERO)
												&& !totaleTargetUtenzaDisabilitaAdulti.equals(BigDecimal.ZERO)
												&& !totaleSpecificaDisabilitaAdulti
														.equals(totaleTargetUtenzaDisabilitaAdulti)) {

											String errMsgC4 = errorMessageC3;
											elencoerrori.getErrors().add(errMsgC4.replaceAll("MINADUL", "Adulti")
													.replace("PRESTAZIONE", prestazione2.getDescPrestazione()));
										}
									}
								}
							}
						}

						// CONTROLLO 5, Controllo NucleiFamiliari con MinoriDisabili per disabilita
						for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
							if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
								for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
									if (targetUtenza.getCodTargetUtenze().equals("U23")) {
										BigDecimal nucleiFamiliariConMinoriDisabili = BigDecimal.ZERO;
										BigDecimal totSpecDisabMinori = BigDecimal.ZERO;
										for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
											if (!disabilita.getCodDisabilita().equals("DIS08")
													&& disabilita.getValore() != null) {
												totSpecDisabMinori = totSpecDisabMinori.add(disabilita.getValore());
											}
											if (disabilita.getCodDisabilita().equals("DIS08")
													&& disabilita.getValore() != null) {
												nucleiFamiliariConMinoriDisabili = nucleiFamiliariConMinoriDisabili
														.add(disabilita.getValore());
											}
										}
										if ((!nucleiFamiliariConMinoriDisabili.equals(BigDecimal.ZERO)
												&& !totSpecDisabMinori.equals(BigDecimal.ZERO)
												&& nucleiFamiliariConMinoriDisabili.compareTo(totSpecDisabMinori) > 0)
												|| nucleiFamiliariConMinoriDisabili.compareTo(totSpecDisabMinori) > 0) {
											String errMsgC5 = errorMessageC4;
											elencoerrori.getErrors().add(errMsgC5.replaceAll("MINADUL", "Minori"));
										}
									}
								}
							}
						}

						// CONTROLLO 6, Controllo NucleiFamiliari con AdultiDisabili per disabilita
						for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
							if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
								for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
									if (targetUtenza.getCodTargetUtenze().equals("U25")) {
										BigDecimal nucleiFamiliariConAdultiDisabili = BigDecimal.ZERO;
										BigDecimal totSpecDisabAdulti = BigDecimal.ZERO;
										for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
											if (!disabilita.getCodDisabilita().equals("DIS09")
													&& disabilita.getValore() != null) {
												totSpecDisabAdulti = totSpecDisabAdulti.add(disabilita.getValore());
											}
											if (disabilita.getCodDisabilita().equals("DIS09")
													&& disabilita.getValore() != null) {
												nucleiFamiliariConAdultiDisabili = nucleiFamiliariConAdultiDisabili
														.add(disabilita.getValore());
											}
										}
										if ((!nucleiFamiliariConAdultiDisabili.equals(BigDecimal.ZERO)
												&& !totSpecDisabAdulti.equals(BigDecimal.ZERO)
												&& nucleiFamiliariConAdultiDisabili.compareTo(totSpecDisabAdulti) > 0)
												|| nucleiFamiliariConAdultiDisabili.compareTo(totSpecDisabAdulti) > 0) {
											String errMsgC6 = errorMessageC4;
											elencoerrori.getErrors().add(errMsgC6.replaceAll("MINADUL", "Adulti"));
										}
									}
								}
							}
						}

						// CONTROLLO 7, Controllo NucleiFamiliari minore di somma target utenza
						for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
							if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
								for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
									// di cui Minori disabili
									if (targetUtenza.getCodTargetUtenze().equals("U23")) {
										for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
											if (disabilita.getCodDisabilita().equals("DIS01")
													|| disabilita.getCodDisabilita().equals("DIS02")
													|| disabilita.getCodDisabilita().equals("DIS03")) {
												BigDecimal valueDisabilitaMinori = disabilita.getValore() != null
														? disabilita.getValore()
														: BigDecimal.ZERO;
												for (ModelCDettagliDisabilita dettaglioDisabilita : disabilita
														.getListaDettagliDisabilita()) {
													if (dettaglioDisabilita.getValore() != null && dettaglioDisabilita
															.getValore().compareTo(valueDisabilitaMinori) == 1) {
														String errMsgC7 = errorMessageC5;
														elencoerrori.getErrors().add(errMsgC7
																.replace("PRESTAZIONE",
																		prestazione.getDescPrestazione())
																.replace("DICUI",
																		dettaglioDisabilita.getDescDettagliDisabilita())
																.replace("DISABILITA", disabilita.getDescDisabilita())
																.replace("MINADUL", "Minori"));
													}
												}
											}
										}
									}
									// di cui Adulti disabili
									if (targetUtenza.getCodTargetUtenze().equals("U25")) {
										for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
											if (disabilita.getCodDisabilita().equals("DIS01")
													|| disabilita.getCodDisabilita().equals("DIS02")
													|| disabilita.getCodDisabilita().equals("DIS03")) {
												BigDecimal valueDisabilitaAdulti = disabilita.getValore() != null
														? disabilita.getValore()
														: BigDecimal.ZERO;
												for (ModelCDettagliDisabilita dettaglioDisabilita : disabilita
														.getListaDettagliDisabilita()) {
													if (dettaglioDisabilita.getValore() != null && dettaglioDisabilita
															.getValore().compareTo(valueDisabilitaAdulti) == 1) {
														String errMsgC8 = errorMessageC5;
														elencoerrori.getErrors().add(errMsgC8
																.replace("PRESTAZIONE",
																		prestazione.getDescPrestazione())
																.replace("DICUI",
																		dettaglioDisabilita.getDescDettagliDisabilita())
																.replace("DISABILITA", disabilita.getDescDisabilita())
																.replace("MINADUL", "Adulti"));
													}
												}
											}
										}
									}
								}
							}
						}

						// CONTROLLO 8, Nuclei Totali < (Nuclei Totali Adulti + Nuclei Totali Minori)
						for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
							if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
								BigDecimal totaleNucleiFamiliari = BigDecimal.ZERO;
								BigDecimal totaleNucleiDisabiliAdultiMinori = BigDecimal.ZERO;

								for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
									if (targetUtenza.getCodTargetUtenze().equals("U28")) {
										totaleNucleiFamiliari = targetUtenza.getValore() != null
												? targetUtenza.getValore()
												: BigDecimal.ZERO;
									}
									if (targetUtenza.getCodTargetUtenze().equals("U23")
											|| targetUtenza.getCodTargetUtenze().equals("U25")) {
										for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
											if (disabilita.getCodDisabilita().equals("DIS08")
													|| disabilita.getCodDisabilita().equals("DIS09")) {
												totaleNucleiDisabiliAdultiMinori = totaleNucleiDisabiliAdultiMinori
														.add(disabilita.getValore() != null ? disabilita.getValore()
																: BigDecimal.ZERO);
											}
										}
									}
								}
								if (!totaleNucleiFamiliari.equals(BigDecimal.ZERO)
										&& !totaleNucleiDisabiliAdultiMinori.equals(BigDecimal.ZERO)
										&& totaleNucleiDisabiliAdultiMinori.compareTo(totaleNucleiFamiliari) == 1) {
									elencoerrori.getErrors().add(errorMessageC6);
								}
							}
						}
					}
//					else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_E)) {
//						// Non ci sono controlli relativi al Modello E nell'invia
//					}
					else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_F)) {
						// Controlli Modello F
						ModelRendicontazioneModF rendModF = modelloFService.getRendicontazioneModF(idSchedaEnte);
						ModelFConteggioPersonale conteggioPersonale = rendModF.getConteggi().getConteggioPersonale();
						String errorMessageF = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_01)
								.getTestoMessaggio();

						// CONTROLLO 1, Controllo NucleiFamiliari minore di somma target utenza
						BigDecimal totaleAssistenzaSociale = BigDecimal.ZERO;
						BigDecimal totaleEducatore = BigDecimal.ZERO;
						BigDecimal totaleMediatoreCulturale = BigDecimal.ZERO;
						BigDecimal totalePsicologo = BigDecimal.ZERO;
						BigDecimal totalePedagogista = BigDecimal.ZERO;
						BigDecimal totaleSociologo = BigDecimal.ZERO;
						BigDecimal totaleOssAdbOta = BigDecimal.ZERO;
						BigDecimal totaleInfermiere = BigDecimal.ZERO;
						BigDecimal totaleAltro = BigDecimal.ZERO;

						for (ModelFPersonaleEnte personaleEnte : conteggioPersonale.getListaPersonaleEnte()) {
							if (personaleEnte.getCodPersonaleEnte().equals("01")
									|| personaleEnte.getCodPersonaleEnte().equals("02")
									|| personaleEnte.getCodPersonaleEnte().equals("03")
									|| personaleEnte.getCodPersonaleEnte().equals("04")
									|| personaleEnte.getCodPersonaleEnte().equals("05")) {
								for (ModelFValori valore : personaleEnte.getListaValori()) {
									if (valore.getCodProfiloProfessionale().equals("1")) {
										totaleAssistenzaSociale = totaleAssistenzaSociale
												.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
									}
									if (valore.getCodProfiloProfessionale().equals("2")) {
										totaleEducatore = totaleEducatore
												.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
									}
									if (valore.getCodProfiloProfessionale().equals("3")) {
										totaleMediatoreCulturale = totaleMediatoreCulturale
												.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
									}
									if (valore.getCodProfiloProfessionale().equals("4")) {
										totalePsicologo = totalePsicologo
												.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
									}
									if (valore.getCodProfiloProfessionale().equals("5")) {
										totalePedagogista = totalePedagogista
												.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
									}
									if (valore.getCodProfiloProfessionale().equals("6")) {
										totaleSociologo = totaleSociologo
												.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
									}
									if (valore.getCodProfiloProfessionale().equals("7")) {
										totaleOssAdbOta = totaleOssAdbOta
												.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
									}
									if (valore.getCodProfiloProfessionale().equals("8")) {
										totaleInfermiere = totaleInfermiere
												.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
									}
									if (valore.getCodProfiloProfessionale().equals("9")) {
										totaleAltro = totaleAltro
												.add(valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
									}
								}
							}
							// di cui
							else if (personaleEnte.getCodPersonaleEnte().equals("06")
									|| personaleEnte.getCodPersonaleEnte().equals("07")
									|| personaleEnte.getCodPersonaleEnte().equals("08")) {
								for (ModelFValori valore : personaleEnte.getListaValori()) {
									if (valore.getCodProfiloProfessionale().equals("1")) {
										String descProfiloProfessionale = "";
										if (valore.getValore() != null
												&& !totaleAssistenzaSociale.equals(BigDecimal.ZERO)
												&& valore.getValore().compareTo(totaleAssistenzaSociale) == 1) {
											for (ModelFProfiloProfessionale profilo : conteggioPersonale
													.getListaProfiloProfessionale()) {
												if (profilo.getCodProfiloProfessionale().equals("1")) {
													descProfiloProfessionale = profilo.getDescProfiloProfessionale();
												}
											}
											String errMsgC6 = errorMessageF;
											elencoerrori.getErrors()
													.add(errMsgC6.replace("DICUI", personaleEnte.getDescPersonaleEnte())
															.replace("PROFILO", descProfiloProfessionale));
										}
									}
									if (valore.getCodProfiloProfessionale().equals("2")) {
										String descProfiloProfessionale = "";
										if (valore.getValore() != null && !totaleEducatore.equals(BigDecimal.ZERO)
												&& valore.getValore().compareTo(totaleEducatore) == 1) {
											for (ModelFProfiloProfessionale profilo : conteggioPersonale
													.getListaProfiloProfessionale()) {
												if (profilo.getCodProfiloProfessionale().equals("2")) {
													descProfiloProfessionale = profilo.getDescProfiloProfessionale();
												}
											}
											String errMsgC7 = errorMessageF;
											elencoerrori.getErrors()
													.add(errMsgC7.replace("DICUI", personaleEnte.getDescPersonaleEnte())
															.replace("PROFILO", descProfiloProfessionale));
										}
									}
									if (valore.getCodProfiloProfessionale().equals("3")) {
										String descProfiloProfessionale = "";
										if (valore.getValore() != null
												&& !totaleMediatoreCulturale.equals(BigDecimal.ZERO)
												&& valore.getValore().compareTo(totaleMediatoreCulturale) == 1) {
											for (ModelFProfiloProfessionale profilo : conteggioPersonale
													.getListaProfiloProfessionale()) {
												if (profilo.getCodProfiloProfessionale().equals("3")) {
													descProfiloProfessionale = profilo.getDescProfiloProfessionale();
												}
											}
											String errMsgC8 = errorMessageF;
											elencoerrori.getErrors()
													.add(errMsgC8.replace("DICUI", personaleEnte.getDescPersonaleEnte())
															.replace("PROFILO", descProfiloProfessionale));
										}
									}
									if (valore.getCodProfiloProfessionale().equals("4")) {
										String descProfiloProfessionale = "";
										if (valore.getValore() != null && !totalePsicologo.equals(BigDecimal.ZERO)
												&& valore.getValore().compareTo(totalePsicologo) == 1) {
											for (ModelFProfiloProfessionale profilo : conteggioPersonale
													.getListaProfiloProfessionale()) {
												if (profilo.getCodProfiloProfessionale().equals("4")) {
													descProfiloProfessionale = profilo.getDescProfiloProfessionale();
												}
											}
											String errMsgC9 = errorMessageF;
											elencoerrori.getErrors()
													.add(errMsgC9.replace("DICUI", personaleEnte.getDescPersonaleEnte())
															.replace("PROFILO", descProfiloProfessionale));
										}
									}
									if (valore.getCodProfiloProfessionale().equals("5")) {
										String descProfiloProfessionale = "";
										if (valore.getValore() != null && !totalePedagogista.equals(BigDecimal.ZERO)
												&& valore.getValore().compareTo(totalePedagogista) == 1) {
											for (ModelFProfiloProfessionale profilo : conteggioPersonale
													.getListaProfiloProfessionale()) {
												if (profilo.getCodProfiloProfessionale().equals("5")) {
													descProfiloProfessionale = profilo.getDescProfiloProfessionale();
												}
											}
											String errMsgC10 = errorMessageF;
											elencoerrori.getErrors()
													.add(errMsgC10
															.replace("DICUI", personaleEnte.getDescPersonaleEnte())
															.replace("PROFILO", descProfiloProfessionale));
										}
									}
									if (valore.getCodProfiloProfessionale().equals("6")) {
										String descProfiloProfessionale = "";
										if (valore.getValore() != null && !totaleSociologo.equals(BigDecimal.ZERO)
												&& valore.getValore().compareTo(totaleSociologo) == 1) {
											for (ModelFProfiloProfessionale profilo : conteggioPersonale
													.getListaProfiloProfessionale()) {
												if (profilo.getCodProfiloProfessionale().equals("6")) {
													descProfiloProfessionale = profilo.getDescProfiloProfessionale();
												}
											}
											String errMsgC11 = errorMessageF;
											elencoerrori.getErrors()
													.add(errMsgC11
															.replace("DICUI", personaleEnte.getDescPersonaleEnte())
															.replace("PROFILO", descProfiloProfessionale));
										}
									}
									if (valore.getCodProfiloProfessionale().equals("7")) {
										String descProfiloProfessionale = "";
										if (valore.getValore() != null && !totaleOssAdbOta.equals(BigDecimal.ZERO)
												&& valore.getValore().compareTo(totaleOssAdbOta) == 1) {
											for (ModelFProfiloProfessionale profilo : conteggioPersonale
													.getListaProfiloProfessionale()) {
												if (profilo.getCodProfiloProfessionale().equals("7")) {
													descProfiloProfessionale = profilo.getDescProfiloProfessionale();
												}
											}
											String errMsgC12 = errorMessageF;
											elencoerrori.getErrors()
													.add(errMsgC12
															.replace("DICUI", personaleEnte.getDescPersonaleEnte())
															.replace("PROFILO", descProfiloProfessionale));
										}
									}
									if (valore.getCodProfiloProfessionale().equals("8")) {
										String descProfiloProfessionale = "";
										if (valore.getValore() != null && !totaleInfermiere.equals(BigDecimal.ZERO)
												&& valore.getValore().compareTo(totaleInfermiere) == 1) {
											for (ModelFProfiloProfessionale profilo : conteggioPersonale
													.getListaProfiloProfessionale()) {
												if (profilo.getCodProfiloProfessionale().equals("8")) {
													descProfiloProfessionale = profilo.getDescProfiloProfessionale();
												}
											}
											String errMsgC13 = errorMessageF;
											elencoerrori.getErrors()
													.add(errMsgC13
															.replace("DICUI", personaleEnte.getDescPersonaleEnte())
															.replace("PROFILO", descProfiloProfessionale));
										}
									}
									if (valore.getCodProfiloProfessionale().equals("9")) {
										String descProfiloProfessionale = "";
										if (valore.getValore() != null && !totaleAltro.equals(BigDecimal.ZERO)
												&& valore.getValore().compareTo(totaleAltro) == 1) {
											for (ModelFProfiloProfessionale profilo : conteggioPersonale
													.getListaProfiloProfessionale()) {
												if (profilo.getCodProfiloProfessionale().equals("9")) {
													descProfiloProfessionale = profilo.getDescProfiloProfessionale();
												}
											}
											String errMsgC13 = errorMessageF;
											elencoerrori.getErrors()
													.add(errMsgC13
															.replace("DICUI", personaleEnte.getDescPersonaleEnte())
															.replace("PROFILO", descProfiloProfessionale));
										}
									}
								}
							}
						}
					}
				}
			} else if (modellitranche.contains("1") && modellitranche.contains("0")) {
				// se sono misti dai errore perche' non tutti valorizzati ed esci
				elencoerrori.getErrors()
						.add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO).getTestoMessaggio()
								.replace("MODELLINO", mancanzamodello.substring(0, mancanzamodello.length() - 2)));
			}
		}
		if (elencoerrori.getErrors().size() > 0) {
			// ci sono errori bloccanti devo mettere anche l'errore finale generico
			elencoerrori.setDescrizione(
					listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIA_KO).getTestoMessaggio());
		}
		if (elencoerrori.getErrors().size() == 0 && elencoerrori.getWarnings().size() > 0) {
			// ci sono errori bloccanti devo mettere anche l'errore finale generico
			elencoerrori.setDescrizione(listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_TRANCHE)
					.getTestoMessaggio().replace("TRANCHE", SharedConstants.SECONDA_TRANCHE));
		}
		if (elencoerrori.getErrors().size() == 0 && elencoerrori.getWarnings().size() == 0) {
			messaggio = listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_OK).getTestoMessaggio()
					.replace("TRANCHE", SharedConstants.SECONDA_TRANCHE) + ";";
			messaggio = messaggio + listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_TRANCHE)
					.getTestoMessaggio().replace("TRANCHE", SharedConstants.SECONDA_TRANCHE);
			elencoerrori.setDescrizione(messaggio);
		}
		elencoerrori.setId("OK");
		return elencoerrori;
	}

	public ArrayList<ModelTabTranche> findModelliAssociati(Integer idRendicontazione) {

		List<Object> resultList = new ArrayList<Object>();
		resultList = datiRendicontazioneDao.findModelliAssociati(idRendicontazione);

		ArrayList<ModelTabTranche> listaModelliAssociati = new ArrayList<ModelTabTranche>();
		Iterator<Object> itr = resultList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			listaModelliAssociati.add(new ModelTabTranche(obj, true));
		}

		return listaModelliAssociati;
	}

	public List<ModelTabTranche> findModelli() {

		List<Object> resultList = new ArrayList<Object>();
		resultList = datiRendicontazioneDao.findModelli();

		List<ModelTabTranche> listaModelliAssociati = new ArrayList<ModelTabTranche>();
		Iterator<Object> itr = resultList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			listaModelliAssociati.add(new ModelTabTranche(obj, false));
		}

		return listaModelliAssociati;
	}

	public List<ModelObbligatorio> findAllObbligo() {

		List<GregDObbligo> resultList = new ArrayList<GregDObbligo>();
		resultList = datiRendicontazioneDao.findAllObbligo();

		List<ModelObbligatorio> listaObbligo = new ArrayList<ModelObbligatorio>();
		for (GregDObbligo obbligo : resultList) {
			listaObbligo.add(new ModelObbligatorio(obbligo));
		}

		return listaObbligo;
	}

	@Transactional
	public String controllaModelloVuoto(String modello, Integer idRendicontazione) {
		if (modello.equalsIgnoreCase(SharedConstants.MODELLO_A)) {
			List<GregRRendicontazioneModAPart1> listaparte1 = modelloADao
					.getAllDatiModelloAPart1PerInvio(idRendicontazione);
			if (listaparte1.size() != 0)
				return "pieno";
			List<GregRRendicontazioneModAPart2> listaparte2 = modelloADao
					.getAllDatiModelloAPart2PerInvio(idRendicontazione);
			if (listaparte2.size() != 0)
				return "pieno";
			List<GregRRendicontazioneModAPart3> listaparte3 = modelloADao
					.getAllDatiModelloAPart3PerInvio(idRendicontazione);
			if (listaparte3.size() != 0)
				return "pieno";
		} else if (modello.equalsIgnoreCase(SharedConstants.MODELLO_A1)) {
			List<GregRRendicontazioneModA1> datimodelloA1 = modelloA1Dao.getAllDatiModelloA1PerInvio(idRendicontazione);
			if (datimodelloA1.size() != 0)
				return "pieno";
		} else if (modello.equalsIgnoreCase(SharedConstants.MODELLO_A2)) {
			List<GregTCausaleEnteComuneModA2> causali = modelloA2Dao.getCausali(idRendicontazione);
			if (causali.size() != 0)
				return "pieno";
			List<GregRRendicontazioneEnteComuneModA2> entecomune = modelloA2Dao
					.getPerInvioModelloA2EnteComune(idRendicontazione);
			if (entecomune.size() != 0)
				return "pieno";
			List<GregRRendicontazioneComuneEnteModA2> comuneente = modelloA2Dao
					.getPerInvioModelloA2ComuneEnte(idRendicontazione);
			if (comuneente.size() != 0)
				return "pieno";
		} else if (modello.equalsIgnoreCase(SharedConstants.MODELLO_D)) {
			List<GregRRendicontazioneModD> datimodD = modelloDDao.getModelloDPerInvio(idRendicontazione);
			if (datimodD.size() != 0)
				return "pieno";
		} else if (modello.equalsIgnoreCase(SharedConstants.MODELLO_MA)) {
			List<GregRRendicontazioneSpesaMissioneProgrammaMacro> listaMacroaggregati = macroaggregatiDao
					.getAllDatiModelloMacroaggregatiPerInvio(idRendicontazione);
			if (listaMacroaggregati.size() != 0)
				return "pieno";
		} else if (modello.equalsIgnoreCase(SharedConstants.MODELLO_B1)) {
			List<GregRRendicontazionePreg1Macro> listaModelloB1part1 = modelloB1Dao
					.getAllDatiModelloB1part1PerInvio(idRendicontazione);
			if (listaModelloB1part1.size() != 0)
				return "pieno";
			List<GregRRendicontazionePreg1Utereg1> listaModelloB1part2 = modelloB1Dao
					.getAllDatiModelloB1part2PerInvio(idRendicontazione);
			if (listaModelloB1part2.size() != 0)
				return "pieno";
			List<GregRRendicontazionePreg2Utereg2> listaModelloB1part3 = modelloB1Dao
					.getAllDatiModelloB1part3PerInvio(idRendicontazione);
			if (listaModelloB1part3.size() != 0)
				return "pieno";
		} else if (modello.equalsIgnoreCase(SharedConstants.MODELLO_B)) {
			List<GregRRendMiProTitEnteGestoreModB> listaModelloB = modelloBDao
					.getAllDatiModelloBPerInvio(idRendicontazione);
			if (listaModelloB.size() != 0)
				return "pieno";
			List<GregRRendicontazioneNonConformitaModB> listaConformitaModelloB = modelloBDao
					.getAllDatiConformitaModelloBPerInvio(idRendicontazione);
			if (listaConformitaModelloB.size() != 0)
				return "pieno";
		} else if (modello.equalsIgnoreCase(SharedConstants.MODELLO_C)) {
			List<GregRRendicontazioneModCParte1> listaModelloCpart1 = modelloCDao
					.getAllDatiModelloCpart1PerInvio(idRendicontazione);
			if (listaModelloCpart1.size() != 0)
				return "pieno";
			List<GregRRendicontazioneModCParte2> listaModelloCpart2 = modelloCDao
					.getAllDatiModelloCpart2PerInvio(idRendicontazione);
			if (listaModelloCpart2.size() != 0)
				return "pieno";
			List<GregRRendicontazioneModCParte3> listaModelloCpart3 = modelloCDao
					.getAllDatiModelloCpart3PerInvio(idRendicontazione);
			if (listaModelloCpart3.size() != 0)
				return "pieno";
			List<GregRRendicontazioneModCParte4> listaModelloCpart4 = modelloCDao
					.getAllDatiModelloCpart4PerInvio(idRendicontazione);
			if (listaModelloCpart4.size() != 0)
				return "pieno";
		} else if (modello.equalsIgnoreCase(SharedConstants.MODELLO_E)) {
			List<GregRRendicontazioneModE> listaModelloE = modelloEDao.getAllDatiModelloEPerInvio(idRendicontazione);
			if (listaModelloE.size() != 0)
				return "pieno";
		} else if (modello.equalsIgnoreCase(SharedConstants.MODELLO_F)) {
			List<GregRRendicontazioneModFParte1> listaModelloFpart1 = modelloFDao
					.getAllDatiModelloFpart1PerInvio(idRendicontazione);
			if (listaModelloFpart1.size() != 0)
				return "pieno";
			List<GregRRendicontazioneModFParte2> listaModelloFpart2 = modelloFDao
					.getAllDatiModelloFpart2PerInvio(idRendicontazione);
			if (listaModelloFpart2.size() != 0)
				return "pieno";
		}
		return "vuoto";
	}

	@Transactional
	public GenericResponseWarnErr attivaControlliModelloPerTranche(ArrayList<ModelTabTranche> modelli, String tranche,
			GregTRendicontazioneEnte rendicontazione) throws Exception {
		GenericResponseWarnErr elencoerrori = new GenericResponseWarnErr();
		elencoerrori.setWarnings(new ArrayList<String>());
		elencoerrori.setErrors(new ArrayList<String>());
		elencoerrori.setObblMotivazione(false);
		String modelliCheck = "";
		String modellimotivazioneEsisteCheck = "";
		boolean obblCheck = false;
		boolean motivazioneEsiste = false;
		int anno = rendicontazione.getAnnoGestione();
		String messaggio = "";
		// prelevo gli allegati dove alegato 1 obbligatorio prima tranche
		boolean trovatoallegato = false;
		String numallegati = datiRendicontazioneDao.contaAllegati(SharedConstants.TRANCHEI,
				rendicontazione.getIdRendicontazioneEnte());
		trovatoallegato = Converter.getInt(numallegati) == 0 ? false : true;
		if (!trovatoallegato) {
			elencoerrori.getErrors().add(listeService.getMessaggio(SharedConstants.ERRORE_MANCA_ALLEGATO_1)
					.getTestoMessaggio().replace("TRANCHE", SharedConstants.PRIMA_TRANCHE));
			elencoerrori.setDescrizione(
					listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIA_KO).getTestoMessaggio());
			elencoerrori.setId("OK");
			return elencoerrori;
		} else {
			// se trovo allegato allora controllo se ci sono dati nei modelli se vuoti
			// chiedo se procedere se anche uno fosse pieno mi blocco
			// mappa modello facoltativo/obbligatorio pieno vuoto
			Map<ModelliInvio, Boolean> modellitranche = new LinkedHashMap<ModelliInvio, Boolean>();
			boolean pieno = false;
			// valori diversi da null e da 0
			List<GregRRendicontazioneEnteComuneModA2> entecomune = modelloA2Dao
					.getPerInvioModelloA2EnteComune(rendicontazione.getIdRendicontazioneEnte());
			List<GregRRendicontazioneComuneEnteModA2> comuneente = modelloA2Dao
					.getPerInvioModelloA2ComuneEnte(rendicontazione.getIdRendicontazioneEnte());
			for (ModelTabTranche modello : modelli) {
				ModelliInvio valori = new ModelliInvio();
				valori.setModello(modello.getCodTab());
				valori.setOrdinamento(modello.getOrdinamento());
				if (modello.getCodObbligo().equalsIgnoreCase(SharedConstants.OBBLIGATORIO)) {
					valori.setObbligo(SharedConstants.OBBLIGATORIO);
				} else {
					valori.setObbligo(SharedConstants.FACOLTATIVO);
				}
				if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_A)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					// preleva i dati dalle tabelle per vedere se esiste una rendicontazione sui
					// modelli
					List<GregRRendicontazioneModAPart1> listaparte1 = modelloADao
							.getAllDatiModelloAPart1PerInvio(rendicontazione.getIdRendicontazioneEnte());
					List<GregRRendicontazioneModAPart2> listaparte2 = modelloADao
							.getAllDatiModelloAPart2PerInvio(rendicontazione.getIdRendicontazioneEnte());
					List<GregRRendicontazioneModAPart3> listaparte3 = modelloADao
							.getAllDatiModelloAPart3PerInvio(rendicontazione.getIdRendicontazioneEnte());
					if (listaparte1.size() == 0 && listaparte2.size() == 0 && listaparte3.size() == 0) {
						pieno = false;
					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_A1)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					List<GregRRendicontazioneModA1> datimodelloA1 = modelloA1Dao
							.getAllDatiModelloA1PerInvio(rendicontazione.getIdRendicontazioneEnte());
					if (datimodelloA1.size() == 0) {
						pieno = false;

					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_A2)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					List<GregTCausaleEnteComuneModA2> causali = modelloA2Dao
							.getCausali(rendicontazione.getIdRendicontazioneEnte());
					if (comuneente.size() == 0 && entecomune.size() == 0 && causali.size() == 0) {
						pieno = false;
					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_D)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					List<GregRRendicontazioneModD> datimodD = modelloDDao
							.getModelloDPerInvio(rendicontazione.getIdRendicontazioneEnte());
					if (datimodD.size() == 0) {
						pieno = false;
					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_MA)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					List<GregRRendicontazioneSpesaMissioneProgrammaMacro> listaMacroaggregati = macroaggregatiDao
							.getAllDatiModelloMacroaggregatiPerInvio(rendicontazione.getIdRendicontazioneEnte());
					if (listaMacroaggregati.size() == 0) {
						pieno = false;
					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_B1)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					List<GregRRendicontazionePreg1Macro> listaModelloB1part1 = modelloB1Dao
							.getAllDatiModelloB1part1PerInvio(rendicontazione.getIdRendicontazioneEnte());
					List<GregRRendicontazionePreg1Utereg1> listaModelloB1part2 = modelloB1Dao
							.getAllDatiModelloB1part2PerInvio(rendicontazione.getIdRendicontazioneEnte());
					List<GregRRendicontazionePreg2Utereg2> listaModelloB1part3 = modelloB1Dao
							.getAllDatiModelloB1part3PerInvio(rendicontazione.getIdRendicontazioneEnte());
					if (listaModelloB1part1.size() == 0 && listaModelloB1part2.size() == 0
							&& listaModelloB1part3.size() == 0) {
						pieno = false;
					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_B)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					List<GregRRendMiProTitEnteGestoreModB> listaModelloB = modelloBDao
							.getAllDatiModelloBPerInvio(rendicontazione.getIdRendicontazioneEnte());
					if (listaModelloB.size() == 0) {
						pieno = false;
					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_C)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					List<GregRRendicontazioneModCParte1> listaModelloCpart1 = modelloCDao
							.getAllDatiModelloCpart1PerInvio(rendicontazione.getIdRendicontazioneEnte());
					List<GregRRendicontazioneModCParte2> listaModelloCpart2 = modelloCDao
							.getAllDatiModelloCpart2PerInvio(rendicontazione.getIdRendicontazioneEnte());
					List<GregRRendicontazioneModCParte3> listaModelloCpart3 = modelloCDao
							.getAllDatiModelloCpart3PerInvio(rendicontazione.getIdRendicontazioneEnte());
					List<GregRRendicontazioneModCParte4> listaModelloCpart4 = modelloCDao
							.getAllDatiModelloCpart4PerInvio(rendicontazione.getIdRendicontazioneEnte());
					if (listaModelloCpart1.size() == 0 && listaModelloCpart2.size() == 0
							&& listaModelloCpart3.size() == 0 && listaModelloCpart4.size() == 0) {
						pieno = false;
					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_E)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					List<GregRRendicontazioneModE> listaModelloE = modelloEDao
							.getAllDatiModelloEPerInvio(rendicontazione.getIdRendicontazioneEnte());
					if (listaModelloE.size() == 0) {
						pieno = false;
					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				} else if (modello.getCodTab().equalsIgnoreCase(SharedConstants.MODELLO_F)
						&& modello.getCodTranche().equalsIgnoreCase(tranche)) {
					List<GregRRendicontazioneModFParte1> listaModelloFpart1 = modelloFDao
							.getAllDatiModelloFpart1PerInvio(rendicontazione.getIdRendicontazioneEnte());
					List<GregRRendicontazioneModFParte2> listaModelloFpart2 = modelloFDao
							.getAllDatiModelloFpart2PerInvio(rendicontazione.getIdRendicontazioneEnte());
					if (listaModelloFpart1.size() == 0 && listaModelloFpart2.size() == 0) {
						pieno = false;
					} else {
						pieno = true;
					}
					modellitranche.put(valori, pieno);
				}
			}
			// se tutti i modelli sono vuoti vai in errore anche se sono facoltativi
			String mancanzamodello = "";
			if (!modellitranche.containsValue(true) && tranche.equals(SharedConstants.TRANCHEI)) {
				// ci sono gli allegati e sono tutti vuoti popup messaggio per continuare esci
				messaggio = listeService.getMessaggio(SharedConstants.ERROR_INVIO_ALLEGATO_NO_MODELLISIDOC)
						.getTestoMessaggio() + ";";
				messaggio = messaggio + listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_TRANCHE)
						.getTestoMessaggio().replace("TRANCHE", SharedConstants.PRIMA_TRANCHE);
				elencoerrori.setDescrizione(messaggio);
				elencoerrori.setId("KO");
				return elencoerrori;
			} else {
				// se i modelli obbligatori sono vuoti allora segnala solo quei modelli
				for (ModelliInvio modello : modellitranche.keySet()) {
					if (modello.getObbligo().equalsIgnoreCase(SharedConstants.OBBLIGATORIO)
							&& !modellitranche.get(modello)) {
						mancanzamodello = mancanzamodello + modello.getModello() + ", ";
					}
				}
			}
			if (Checker.isValorizzato(mancanzamodello)) {
				elencoerrori.getErrors()
						.add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO).getTestoMessaggio()
								.replace("MODELLINO", mancanzamodello.substring(0, mancanzamodello.length() - 2)));
			}
			// se i modelli pieni obbligatori o no
			else {
				// ci sono gli allegati e sono tutti pieni ok continua controlli
				ModelDatiEnteRendicontazione contatti = datiEnteService.findDatiEntexAnno(
						rendicontazione.getIdRendicontazioneEnte(), rendicontazione.getAnnoGestione());
				List<ModelValoriModA> cella06 = new ArrayList<ModelValoriModA>();
				List<ModelValoriModA> cella07 = new ArrayList<ModelValoriModA>();
				List<ModelValoriModA> cella08 = new ArrayList<ModelValoriModA>();
				BigDecimal valorecella06 = null;
				BigDecimal valorecella07 = null;
				boolean controlloModAModA2 = false;
				for (ModelliInvio modello : modellitranche.keySet()) {
					if (modellitranche.get(modello)) {
						if (modello.getModello().equalsIgnoreCase(SharedConstants.MODELLO_A)) {
							// chiama funzione modello a
							cella06 = modelloAService.getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(),
									SharedConstants.MODELLO_A_06);
							// verifica se il campo avvalorato
							if (cella06.size() != 0) {
								valorecella06 = cella06.get(0).getValore();
							}
							cella07 = modelloAService.getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(),
									SharedConstants.MODELLO_A_07);
							if (cella07.size() != 0) {
								valorecella07 = cella07.get(0).getValore();
							}

							if (comuneente.size() == 0 && (valorecella07!=null && !valorecella07.equals(BigDecimal.ZERO))) {
								controlloModAModA2 = true;
								elencoerrori.getErrors().add(listeService
										.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_07).getTestoMessaggio());
							}
							cella08 = modelloAService.getDatiModelloA(rendicontazione.getIdRendicontazioneEnte(),
									SharedConstants.MODELLO_A_08);
							if (!(contatti.getCodTipoEnte().equals(SharedConstants.OPERAZIONE_COMUNE_CAPOLUOGO)
									|| contatti.getCodTipoEnte()
											.equals(SharedConstants.OPERAZIONE_CONVENZIONE_COMUNI))) {
								if (cella08.size() != 0) {
									elencoerrori.getErrors()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_NO08)
													.getTestoMessaggio());
								}
							}
							GenericResponseWarnErr response = modelloAService
									.checkModelloA(rendicontazione.getIdRendicontazioneEnte());
							if (response.getWarnings().size() > 0) {
								List<GregRCheck> checkA = datiRendicontazioneDao.findAllMotivazioneByTab(
										SharedConstants.MODELLO_A, rendicontazione.getIdRendicontazioneEnte());
								if (checkA.size() > 0) {
									modellimotivazioneEsisteCheck = modellimotivazioneEsisteCheck
											+ SharedConstants.MODELLO_A + ", ";
									motivazioneEsiste = true;
								} else {
									modelliCheck = modelliCheck + SharedConstants.MODELLO_A + ", ";
									obblCheck = true;
								}
							}

						} else if (modello.getModello().equalsIgnoreCase(SharedConstants.MODELLO_A1)) {
							// chiama funzione modello a1
							// prendo i dati del modello A1
							int contavalorimodelloA1 = 0;
							// if (!comunecapoluogo) {
							List<ModelDatiA1> modelloa1 = modelloA1Service
									.getDatiModelloA1(rendicontazione.getIdRendicontazioneEnte());
							for (ModelDatiA1 datimodel : modelloa1) {
								if (datimodel.getCodcomune() != null) {
									if (!datimodel.getCodcomune().equalsIgnoreCase("Somma")) {
										// se tutti i dati sono obbligatori
										// verificare se il campo puo' essere 0
										for (Map.Entry<String, String> entry : datimodel.getValore().entrySet()) {
											if (entry.getValue() != null) {
												contavalorimodelloA1 = contavalorimodelloA1 + 1;
												String warning = controlloService.numberDecimalValidate(
														entry.getValue() == null ? "0"
																: entry.getValue().replace(".", ""),
														"Importo " + entry.getKey() + " " + datimodel.getDesccomune(),
														14, 2);
												if (warning != null) {
													elencoerrori.getErrors().add(warning);
												}
											}
										}
									} else {
										// campo somma da controllare uguale al valore del modello A se presente
										for (Map.Entry<String, String> entry : datimodel.getValore().entrySet()) {
											if (entry.getValue() != null && !entry.getValue().equalsIgnoreCase(",00")) {
												if (valorecella06 != null) {
													if (!valorecella06.setScale(2, RoundingMode.HALF_UP)
															.equals(new BigDecimal(
																	entry.getValue().replace(".", "").replace(",", "."))
																	.setScale(2, RoundingMode.HALF_UP))) {
														// valori differenti
														elencoerrori.getErrors()
																.add(listeService
																		.getMessaggio(
																				SharedConstants.ERROR_INVIO_MODELLIA_06)
																		.getTestoMessaggio());
													} else if (valorecella06.setScale(2, RoundingMode.HALF_UP)
															.equals(new BigDecimal(
																	entry.getValue().replace(".", "").replace(",", "."))
																	.setScale(2, RoundingMode.HALF_UP))
															&& valorecella06.setScale(2, RoundingMode.HALF_UP)
																	.equals(BigDecimal.ZERO.setScale(2,
																			RoundingMode.HALF_UP))) {
														// uguale zero
														elencoerrori.getErrors()
																.add(listeService.getMessaggio(
																		SharedConstants.ERROR_INVIO_MODELLIA_NO06)
																		.getTestoMessaggio());
													}
												} else {
													elencoerrori.getErrors()
															.add(listeService
																	.getMessaggio(
																			SharedConstants.ERROR_INVIO_MODELLIA_06)
																	.getTestoMessaggio());
												}
											} else {
												if (valorecella06 != null && !valorecella06.equals(BigDecimal.ZERO)) {
													// valori differenti
													elencoerrori.getErrors()
															.add(listeService
																	.getMessaggio(
																			SharedConstants.ERROR_INVIO_MODELLIA_06)
																	.getTestoMessaggio());
												} else {
													elencoerrori.getErrors()
															.add(listeService
																	.getMessaggio(
																			SharedConstants.ERROR_INVIO_MODELLIA_NO06)
																	.getTestoMessaggio());
												}
											}
										}

									}
								} else {
									// caso totale
									for (Map.Entry<String, String> entry : datimodel.getValore().entrySet()) {
										if (entry.getValue() != null) {
											contavalorimodelloA1 = contavalorimodelloA1 + 1;
											String warning = controlloService.numberDecimalValidate(
													entry.getValue() == null ? "0" : entry.getValue().replace(".", ""),
													"Importo " + entry.getKey() + " " + datimodel.getDesccomune(), 14,
													2);
											if (warning != null) {
												elencoerrori.getErrors().add(warning);
											}
										} else {
											if (modello.getObbligo().equalsIgnoreCase(SharedConstants.OBBLIGATORIO)) {

												if (entry.getKey().equalsIgnoreCase("T1")) {
													elencoerrori.getErrors().add(listeService
															.getMessaggio(SharedConstants.ERROR_CODE_DATI_OBBLIGATORI)
															.getTestoMessaggio().replace("MODELLODEF", "A1")
															.replace("SPECIFICARE",
																	modelloA1Dao.getVoceByCodVoce(entry.getKey())
																			.getDescVoceModA1()
																			.replace("anno", String.valueOf(anno))));
												}
											}
										}
									}
								}
							}
							GenericResponseWarnErr response = modelloA1Service
									.checkModelloA1(rendicontazione.getIdRendicontazioneEnte());
							if (response.getWarnings().size() > 0) {
								List<GregRCheck> checkA1 = datiRendicontazioneDao.findAllMotivazioneByTab(
										SharedConstants.MODELLO_A1, rendicontazione.getIdRendicontazioneEnte());
								if (checkA1.size() > 0) {
									modellimotivazioneEsisteCheck = modellimotivazioneEsisteCheck
											+ SharedConstants.MODELLO_A1 + ", ";
									motivazioneEsiste = true;
								} else {
									modelliCheck = modelliCheck + SharedConstants.MODELLO_A1 + ", ";
									obblCheck = true;
								}
							}

						} else if (modello.getModello().equalsIgnoreCase(SharedConstants.MODELLO_A2)) {
							// if (!comunecapoluogo) {
							// chiama funzione modello a2
							if (entecomune.size() == 0) {
								elencoerrori.getWarnings().add(listeService
										.getMessaggio(SharedConstants.WARNING_MOD_A2_01).getTestoMessaggio());
							}
							if (comuneente.size() != 0 && (valorecella07 == null || valorecella07.compareTo(BigDecimal.ZERO)==0)) {
								elencoerrori.getErrors().add(listeService
										.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_07).getTestoMessaggio());
							} else {
								if (comuneente.size() == 0 && (valorecella07!=null && !valorecella07.equals(BigDecimal.ZERO))) {
									if (!controlloModAModA2) {
										elencoerrori.getErrors()
												.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_07)
														.getTestoMessaggio());
									}
								} else if (comuneente.size() != 0 && valorecella07!=null && !valorecella07.equals(BigDecimal.ZERO)) {
									// calcolo il totale
									BigDecimal totale = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
									for (GregRRendicontazioneComuneEnteModA2 val : comuneente) {
										totale = totale.add(val.getValore());
									}
									if (!valorecella07.setScale(2, RoundingMode.HALF_UP).equals(totale)) {
										elencoerrori.getErrors()
												.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIA_07)
														.getTestoMessaggio());
									}
								}
							}
						} else if (modello.getModello().equalsIgnoreCase(SharedConstants.MODELLO_D)) {
							// aggiunta dei warning valori negativi
							BigDecimal risultatoamm = modelloDDao
									.calcolaRisultatoAmministrazione(rendicontazione.getIdRendicontazioneEnte());
							BigDecimal totalepartedisp = modelloDDao
									.calcolaTotaleParteDisponibile(rendicontazione.getIdRendicontazioneEnte());
							totalepartedisp = risultatoamm.add(totalepartedisp.negate());
							try {
								String warn = controlloService.isNegativeNumber(risultatoamm, listeService
										.getMessaggio(SharedConstants.WARNING_MOD_D_03).getTestoMessaggio());
								if (warn != null)
									elencoerrori.getWarnings().add(warn);
							} catch (IntegritaException e) {
								e.printStackTrace();
							}
							try {
								String warn = controlloService.isNegativeNumber(totalepartedisp, listeService
										.getMessaggio(SharedConstants.WARNING_MOD_D_04).getTestoMessaggio());
								if (warn != null)
									elencoerrori.getWarnings().add(warn);
							} catch (IntegritaException e) {
								e.printStackTrace();
							}
						} else if (modello.getModello().equalsIgnoreCase(SharedConstants.MODELLO_MA)) {
							// Controlli Modello Macroaggregati
							Boolean trovataIncongRedditoAcquistoB = false;
							ModelRendicontazioneMacroaggregati rendicontazioneMacro = macroaggregatiService
									.getRendicontazioneMacroaggregatiByIdScheda(
											rendicontazione.getIdRendicontazioneEnte());

							GregRRendicontazioneModAPart1 titolo2 = datiRendicontazioneDao
									.getRendicontazioneModAByIdRendicontazione(
											rendicontazione.getIdRendicontazioneEnte(), "02-ALTRO", "Tipo_altro", "31");
							BigDecimal totaleMissioneAltro = BigDecimal.ZERO.setScale(2);
							for (ModelValoriMacroaggregati missioneMacro : rendicontazioneMacro
									.getValoriMacroaggregati()) {
								BigDecimal valoreReddito = null;
								BigDecimal valoreAcquistoB = null;
								if (missioneMacro.getCampi() != null && missioneMacro.getCampi().size() > 0) {
									for (ModelCampiMacroaggregati campoMacro : missioneMacro.getCampi()) {
										if (campoMacro.getCod().equals("1VB") && campoMacro.getValue() != null) {
											valoreReddito = campoMacro.getValue();
										}
										if (campoMacro.getCod().equals("4VB") && campoMacro.getValue() != null) {
											valoreAcquistoB = campoMacro.getValue();
										}
										if (missioneMacro.getCodMissione().equals("5")) {
											if (campoMacro.getValue() != null) {
												totaleMissioneAltro = totaleMissioneAltro
														.add(campoMacro.getValue().setScale(2));
											}
										}
									}
									// verifico se per ogni missione e' valorizzato il campo acquisto B ma campo
									// reddito non e' valorizzato
									if (valoreReddito == null && valoreAcquistoB != null) {
										trovataIncongRedditoAcquistoB = true;
									}
								}
							}
							if (trovataIncongRedditoAcquistoB) {
								elencoerrori.getErrors().add(listeService
										.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMACRO_01).getTestoMessaggio());
							}
							if (titolo2 != null) {
								if (titolo2.getValoreNumb() != null
										&& !titolo2.getValoreNumb().equals(BigDecimal.ZERO)) {
									if (totaleMissioneAltro == null
											|| totaleMissioneAltro.equals(BigDecimal.ZERO.setScale(2))) {
										elencoerrori.getErrors()
												.add(listeService
														.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMACRO_03)
														.getTestoMessaggio());
									}
								}
							}

							GenericResponseWarnErr response = macroaggregatiService
									.checkMacroaggregati(rendicontazione.getIdRendicontazioneEnte());
							if (response.getWarnings().size() > 0) {
								List<GregRCheck> checkA = datiRendicontazioneDao.findAllMotivazioneByTab(
										SharedConstants.MODELLO_MA, rendicontazione.getIdRendicontazioneEnte());
								if (checkA.size() > 0) {
									modellimotivazioneEsisteCheck = modellimotivazioneEsisteCheck
											+ SharedConstants.MACROAGGREGATI + ", ";
									motivazioneEsiste = true;
								} else {
									modelliCheck = modelliCheck + SharedConstants.MACROAGGREGATI + ", ";
									obblCheck = true;
								}
							}
						} else if (modello.getModello().equalsIgnoreCase(SharedConstants.MODELLO_B1)) {
							// Controlli Modello B1
							List<ModelB1Voci> rendModB1 = modelloB1Service
									.getVoci(rendicontazione.getIdRendicontazioneEnte());

							// CONTROLLO 1, per ogni Prestazione il campo acquisto beni B deve essere
							// valorizzato se campo reddito e' valorizzato
							for (ModelB1Voci prestazione : rendModB1) {
								BigDecimal valReddito = null;
								BigDecimal valAcquistoB = null;
								for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
									if (macroaggregato.getCodice().equals("1VB")
											&& macroaggregato.getValore() != null) {
										valReddito = Util.convertStringToBigDecimal(
												macroaggregato.getValore().replace(".", ","));
									}
									if (macroaggregato.getCodice().equals("4VB")
											&& macroaggregato.getValore() != null) {
										valAcquistoB = Util.convertStringToBigDecimal(
												macroaggregato.getValore().replace(".", ","));
									}
								}
								if (valReddito == null && valAcquistoB != null) {
									elencoerrori.getErrors()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_01)
													.getTestoMessaggio()
													.replace("PRESTAZIONE", "'" + prestazione.getCodPrestazione() + " "
															+ prestazione.getDescPrestazione() + "'"));
								}
							}

							// CONTROLLO 2, verifica Totali Progressivi Automatici coincidano con Totali
							// Macroaggregati
							List<ModelTotaleMacroaggregati> totaliMacro = macroaggregatiService
									.getRendicontazioneTotaliMacroaggregatiPerB1(
											rendicontazione.getIdRendicontazioneEnte())
									.getValoriMacroaggregati();
							BigDecimal totaleRedditoDipendente = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
							BigDecimal totaleAltreImposte = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
							BigDecimal totaleAcquistoBeniA = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
							BigDecimal totaleAcquistoBeniB = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
							BigDecimal totaleTrasferimentiCorrenti = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
							BigDecimal totaleInteressiPassivi = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
							BigDecimal totaleAltreSpese = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

							for (ModelB1Voci prestazione : rendModB1) {
								for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
									if (macroaggregato.getCodice().equals("1VB")
											&& macroaggregato.getValore() != null) {
										totaleRedditoDipendente = totaleRedditoDipendente
												.add(new BigDecimal(macroaggregato.getValore())
														.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
									}
									if (macroaggregato.getCodice().equals("2VB")
											&& macroaggregato.getValore() != null) {
										totaleAltreImposte = totaleAltreImposte
												.add(new BigDecimal(macroaggregato.getValore())
														.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
									}
									if (macroaggregato.getCodice().equals("3VB")
											&& macroaggregato.getValore() != null) {
										totaleAcquistoBeniA = totaleAcquistoBeniA
												.add(new BigDecimal(macroaggregato.getValore())
														.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
									}
									if (macroaggregato.getCodice().equals("4VB")
											&& macroaggregato.getValore() != null) {
										totaleAcquistoBeniB = totaleAcquistoBeniB
												.add(new BigDecimal(macroaggregato.getValore())
														.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
									}
									if (macroaggregato.getCodice().equals("5VB")
											&& macroaggregato.getValore() != null) {
										totaleTrasferimentiCorrenti = totaleTrasferimentiCorrenti
												.add(new BigDecimal(macroaggregato.getValore())
														.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
									}
									if (macroaggregato.getCodice().equals("6VB")
											&& macroaggregato.getValore() != null) {
										totaleInteressiPassivi = totaleInteressiPassivi
												.add(new BigDecimal(macroaggregato.getValore())
														.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
									}
									if (macroaggregato.getCodice().equals("7VB")
											&& macroaggregato.getValore() != null) {
										totaleAltreSpese = totaleAltreSpese
												.add(new BigDecimal(macroaggregato.getValore())
														.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros());
									}
								}
							}
							if (!totaleRedditoDipendente.toPlainString().equals(
									totaliMacro.get(0).getTotale().setScale(2, RoundingMode.HALF_UP).toPlainString())
									|| !totaleAltreImposte.toPlainString()
											.equals(totaliMacro.get(1).getTotale().setScale(2, RoundingMode.HALF_UP)
													.toPlainString())
									|| !totaleAcquistoBeniA.toPlainString()
											.equals(totaliMacro.get(2).getTotale().setScale(2, RoundingMode.HALF_UP)
													.toPlainString())
									|| !totaleAcquistoBeniB.toPlainString()
											.equals(totaliMacro.get(3).getTotale().setScale(2, RoundingMode.HALF_UP)
													.toPlainString())
									|| !totaleTrasferimentiCorrenti.toPlainString()
											.equals(totaliMacro.get(4).getTotale().setScale(2, RoundingMode.HALF_UP)
													.toPlainString())
									|| !totaleInteressiPassivi.toPlainString()
											.equals(totaliMacro.get(5).getTotale().setScale(2, RoundingMode.HALF_UP)
													.toPlainString())
									|| !totaleAltreSpese.toPlainString().equals(totaliMacro.get(6).getTotale()
											.setScale(2, RoundingMode.HALF_UP).toPlainString())) {
								elencoerrori.getErrors().add(listeService
										.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_02).getTestoMessaggio());

							}

							// CONTROLLO 3, verifica Totali Prestazioni coincidente con Totali Utenze
							for (ModelB1Voci prestazione : rendModB1) {
								BigDecimal totalePrestazione = BigDecimal.ZERO;
								BigDecimal totaleUtenza = BigDecimal.ZERO;
								// Totale Prestazione
								for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
									if (macroaggregato.getValore() != null) {
										totalePrestazione = totalePrestazione.add(Util.convertStringToBigDecimal(
												macroaggregato.getValore().replace(".", ",")));
									}
								}
								// Totale Utenza
								if (prestazione.getTipoPrestazione().equals("MA03")) {
									for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
										if (utenza.getValore() != null) {
											totaleUtenza = totaleUtenza.add(Util
													.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
										}
									}
								} else {
									for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
										if (utenza.getValore() != null) {
											totaleUtenza = totaleUtenza.add(Util
													.convertStringToBigDecimal(utenza.getValore().replace(".", ",")));
										}
									}
								}

								if (!totalePrestazione.equals(totaleUtenza)) {
									elencoerrori.getErrors()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_03)
													.getTestoMessaggio()
													.replace("PRESTAZIONE", "'" + prestazione.getCodPrestazione() + " "
															+ prestazione.getDescPrestazione() + "'"));
								}
							}

							for (ModelB1Voci prestazione : rendModB1) {
								if (prestazione.getUtenzeQuotaSocioAssistenziale().size() > 0) {
									for (int i = 0; i < prestazione.getUtenzeQuotaSocioAssistenziale().size(); i++) {
										if (prestazione.getUtenzeCostoTotale().get(i).getValore() != null
												&& (prestazione.getUtenzeQuotaSocioAssistenziale().get(i)
														.getValore() == null
														|| Util.convertStringToBigDecimal(
																prestazione.getUtenzeQuotaSocioAssistenziale().get(i)
																		.getValore() != null ? prestazione
																				.getUtenzeQuotaSocioAssistenziale()
																				.get(i).getValore().replace(".", ",")
																				: "0,00")
																.equals(BigDecimal.ZERO.setScale(2)))) {
											elencoerrori.getWarnings().add(listeService
													.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_11)
													.getTestoMessaggio()
													.replace("PRESTAZIONE",
															"'" + prestazione.getCodPrestazione() + " "
																	+ prestazione.getDescPrestazione() + "'")
													.replace("TARGET",
															"'" + prestazione.getUtenzeCostoTotale().get(i).getCodice()
																	+ " " + prestazione.getUtenzeCostoTotale().get(i)
																			.getDescUtenza()
																	+ "'"));
											elencoerrori.setObblMotivazione(true);
										}
									}
								}

							}

							// Verifica somma utenze prestazioni regionali 2 con utenze prestazioni
							// regionali 1
							List<ModelListeConfiguratore> utenze = configuratorePrestazioniDao.findUtenza();
							for (ModelB1Voci prestazione : rendModB1) {
								if (prestazione.getUtenze().size() > 0) {
									ModelTotalePrestazioniB1[] totaliUtenzePrest2 = new ModelTotalePrestazioniB1[utenze
											.size()];
									for (int i = 0; i < utenze.size(); i++) {
										totaliUtenzePrest2[i] = new ModelTotalePrestazioniB1();
										totaliUtenzePrest2[i].setCodPrestazione(utenze.get(i).getCodice());
										totaliUtenzePrest2[i].setTotale(BigDecimal.ZERO.setScale(2));
									}
									for (ModelB1VociPrestReg2 prest2 : prestazione.getPrestazioniRegionali2()) {
										for (ModelB1UtenzaPrestReg2 utenza : prest2.getUtenze()) {
											for (int i = 0; i < utenze.size(); i++) {
												if (totaliUtenzePrest2[i].getCodPrestazione()
														.equals(utenza.getCodice())) {
													totaliUtenzePrest2[i].setTotale(totaliUtenzePrest2[i].getTotale()
															.add(Util.convertStringToBigDecimal(
																	utenza.getValore() != null
																			? utenza.getValore().replace(".", ",")
																			: "0,00")));
												}
											}
										}
									}
									for (ModelB1UtenzaPrestReg1 u : prestazione.getUtenze()) {
										for (int i = 0; i < utenze.size(); i++) {
											if (u.getCodice().equals(totaliUtenzePrest2[i].getCodPrestazione())) {
												if (!totaliUtenzePrest2[i].getTotale()
														.equals(BigDecimal.ZERO.setScale(2))
														&& totaliUtenzePrest2[i] != null
														&& !Util.convertStringToBigDecimal(
																u.getValore() != null ? u.getValore().replace(".", ",")
																		: "0,00")
																.equals(totaliUtenzePrest2[i].getTotale())) {
													elencoerrori.getWarnings().add(listeService
															.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_10)
															.getTestoMessaggio()
															.replace("PRESTAZIONE",
																	"'" + prestazione.getCodPrestazione() + " "
																			+ prestazione.getDescPrestazione() + "'")
															.replace("UTENZA", "'" + u.getDescUtenza() + "'"));
												}
											}
										}
									}
								} else if (prestazione.getUtenzeCostoTotale().size() > 0) {
									ModelTotalePrestazioniB1[] totaliUtenzePrest2 = new ModelTotalePrestazioniB1[utenze
											.size()];
									for (int i = 0; i < utenze.size(); i++) {
										totaliUtenzePrest2[i] = new ModelTotalePrestazioniB1();
										totaliUtenzePrest2[i].setCodPrestazione(utenze.get(i).getCodice());
										totaliUtenzePrest2[i].setTotale(BigDecimal.ZERO.setScale(2));
									}
									for (ModelB1VociPrestReg2 prest2 : prestazione.getPrestazioniRegionali2()) {
										for (ModelB1UtenzaPrestReg2 utenza : prest2.getUtenze()) {
											for (int i = 0; i < utenze.size(); i++) {
												if (totaliUtenzePrest2[i].getCodPrestazione()
														.equals(utenza.getCodice())) {
													totaliUtenzePrest2[i].setTotale(totaliUtenzePrest2[i].getTotale()
															.add(Util.convertStringToBigDecimal(
																	utenza.getValore() != null
																			? utenza.getValore().replace(".", ",")
																			: "0,00")));
												}
											}
										}
									}
									for (ModelB1UtenzaPrestReg1 u : prestazione.getUtenzeCostoTotale()) {
										for (int i = 0; i < utenze.size(); i++) {
											if (u.getCodice().equals(totaliUtenzePrest2[i].getCodPrestazione())) {

												if (!totaliUtenzePrest2[i].getTotale()
														.equals(BigDecimal.ZERO.setScale(2))
														&& totaliUtenzePrest2[i] != null
														&& !Util.convertStringToBigDecimal(
																u.getValore() != null ? u.getValore().replace(".", ",")
																		: "0,00")
																.equals(totaliUtenzePrest2[i].getTotale())) {
													elencoerrori.getWarnings().add(listeService
															.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_10)
															.getTestoMessaggio()
															.replace("PRESTAZIONE",
																	"'" + prestazione.getCodPrestazione() + " "
																			+ prestazione.getDescPrestazione() + "'")
															.replace("UTENZA", "'" + u.getDescUtenza() + "'"));
												}
											}
										}
									}
								}
							}

							// CONTROLLO 4, verifica tra Entrate (ASR,UT) e Spese (QS) tra Modello A e
							// Modello B1
							Map<String, Map<String, BigDecimal>> prestUteValModA = new HashMap<String, Map<String, BigDecimal>>();
							Map<String, BigDecimal> uteValModA = null;
							// Dal Modello A calcolo le entrate (ASR e UT) per ogni utenza
							List<ModelTitoloModA> titoliModA = modelloAService
									.getListaVociModA(rendicontazione.getIdRendicontazioneEnte()).getListaTitoli();
							for (ModelTitoloModA titolo : titoliModA) {
								for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
									for (ModelVoceModA voce : tipologia.getListaVoci()) {
										if (voce.getPrestazioni() != null) {
											// Considero le Prestazioni ASR
											List<ModelPrestazioneUtenzaModA> prestazioniASR = voce.getPrestazioni()
													.getPrestazioniRS();
											for (ModelPrestazioneUtenzaModA prestazione : prestazioniASR) {
												String codPrestASR = prestazione.getCodPrestazione().replace("_ASR", "")
														.replace("_UT", "");
												// Creo o prelevo la Mappa <CodUtenza, Valore> e ciclo sulle utenze
												if (prestUteValModA.get(codPrestASR) != null) {
													uteValModA = prestUteValModA.get(codPrestASR);
												} else {
													uteValModA = new HashMap<String, BigDecimal>();
												}
												for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
													if (utenza.getValore() != null) {
														if (uteValModA.get(utenza.getCodTargetUtenza()) != null) {
															BigDecimal oldValue = uteValModA
																	.get(utenza.getCodTargetUtenza());
															BigDecimal newValue = utenza.getValore();
															uteValModA.replace(utenza.getCodTargetUtenza(),
																	oldValue.add(newValue));
														} else {
															uteValModA.put(utenza.getCodTargetUtenza(),
																	utenza.getValore());
														}
													}
												}
												if (prestUteValModA.get(codPrestASR) != null) {
													prestUteValModA.replace(codPrestASR, uteValModA);
												} else {
													prestUteValModA.put(codPrestASR, uteValModA);
												}
											}
											// Considero le Prestazioni UT
											List<ModelPrestazioneUtenzaModA> prestazioniUT = voce.getPrestazioni()
													.getPrestazioniCD();
											for (ModelPrestazioneUtenzaModA prestazione : prestazioniUT) {
												String codPrestUT = prestazione.getCodPrestazione().replace("_ASR", "")
														.replace("_UT", "");
												// Creo o prelevo la Mappa <CodUtenza, Valore> e ciclo sulle utenze
												if (prestUteValModA.get(codPrestUT) != null) {
													uteValModA = prestUteValModA.get(codPrestUT);
												} else {
													uteValModA = new HashMap<String, BigDecimal>();
												}
												for (ModelTargetUtenza utenza : prestazione.getListaTargetUtenza()) {
													if (utenza.getValore() != null) {
														if (uteValModA.get(utenza.getCodTargetUtenza()) != null) {
															BigDecimal oldValue = uteValModA
																	.get(utenza.getCodTargetUtenza());
															BigDecimal newValue = utenza.getValore();
															uteValModA.replace(utenza.getCodTargetUtenza(),
																	oldValue.add(newValue));
														} else {
															uteValModA.put(utenza.getCodTargetUtenza(),
																	utenza.getValore());
														}
													}
												}
												if (prestUteValModA.get(codPrestUT) != null) {
													prestUteValModA.replace(codPrestUT, uteValModA);
												} else {
													prestUteValModA.put(codPrestUT, uteValModA);
												}
											}
										}
									}
								}
							}

							for (String uteMapKey : prestUteValModA.keySet()) {
								Map<String, BigDecimal> mappa = prestUteValModA.get(uteMapKey);
								ModelB1Voci prestModB1 = new ModelB1Voci();
								for (ModelB1Voci prestazione : rendModB1) {
									if (prestazione.getCodPrestazione().equals(uteMapKey)) {
										prestModB1 = prestazione;
									}
								}
								// Ciclo sui valori ed effettuo il controllo
								// Ciclo sulle entrate del Mod.A
								Boolean entrateVal = false;
								BigDecimal entrateModA = BigDecimal.ZERO;
								for (BigDecimal value : mappa.values()) {
									if (value != null) {
										entrateVal = true;
										entrateModA = entrateModA.add(value);
									}
								}
								// Ciclo sulle spese del Mod.B1
								Boolean usciteVal = false;
								Boolean usciteObbligVal = true;
								BigDecimal usciteModB1 = BigDecimal.ZERO;
								if (prestModB1.getTipoPrestazione().equals("MA03")) {
									for (ModelB1UtenzaPrestReg1 entryValue : prestModB1.getUtenzeCostoTotale()) {
										if (entryValue.getValore() != null) {
											if (entryValue.isMandatory()) {
												usciteObbligVal = usciteObbligVal && true;
											}
											usciteVal = true;
											usciteModB1 = usciteModB1.add(Util.convertStringToBigDecimal(
													entryValue.getValore().replace(".", ",")));
										} else {
											if (entryValue.isMandatory()) {
												usciteObbligVal = usciteObbligVal && false;
											}
										}
									}
								} else {
									for (ModelB1UtenzaPrestReg1 entryValue : prestModB1.getUtenze()) {
										if (entryValue.getValore() != null) {
											usciteVal = true;
											usciteModB1 = usciteModB1.add(Util.convertStringToBigDecimal(
													entryValue.getValore().replace(".", ",")));
										}
									}
								}
								// CONTROLLO 4.1
								if (!usciteObbligVal) {
									elencoerrori.getErrors()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_04)
													.getTestoMessaggio()
													.replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione() + " "
															+ prestModB1.getDescPrestazione() + "'"));
								}

								// CONTROLLO 4.2
								if (!entrateModA.equals(BigDecimal.ZERO) && !usciteModB1.equals(BigDecimal.ZERO)
										&& usciteModB1.compareTo(entrateModA) == -1) {
									elencoerrori.getErrors()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_05)
													.getTestoMessaggio()
													.replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione() + " "
															+ prestModB1.getDescPrestazione() + "'"));
								}

								// CONTROLLO 4.3
								BigDecimal diffUsciteEntrate = usciteModB1.subtract(entrateModA).setScale(2,
										RoundingMode.HALF_UP);
								if (!entrateModA.equals(BigDecimal.ZERO) && !usciteModB1.equals(BigDecimal.ZERO)
										&& diffUsciteEntrate.compareTo(BigDecimal.ZERO) == -1) {
									elencoerrori.getErrors()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_06)
													.getTestoMessaggio()
													.replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione() + " "
															+ prestModB1.getDescPrestazione() + "'"));
								}

								// CONTROLLO 4.4 WARNING
								if (!entrateVal && usciteVal) {
									elencoerrori.getWarnings()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_08)
													.getTestoMessaggio()
													.replace("PRESTAZIONE", "'" + prestModB1.getCodPrestazione() + " "
															+ prestModB1.getDescPrestazione() + "'"));
								}
							}

							// CONTROLLO 5, Controllo su Spese Prestazioni e 1% del Totale Macroaggregati
							// Calcolo 1% del Totale Macroaggregati
							BigDecimal percentValue = BigDecimal.ZERO;
							List<ModelTotaleMacroaggregati> totaliMacroaggregati = macroaggregatiService
									.getRendicontazioneTotaliMacroaggregatiPerB1(
											rendicontazione.getIdRendicontazioneEnte())
									.getValoriMacroaggregati();
							for (ModelTotaleMacroaggregati totale : totaliMacroaggregati) {
								if (totale.getCodMacroaggregati().equals("")
										&& totale.getDescMacroaggregati().equals("TOTALE")) {
									percentValue = totale.getTotale().multiply(new BigDecimal(0.01)).setScale(2,
											RoundingMode.HALF_UP);
								}
							}
							for (ModelB1Voci prestazione : rendModB1) {
								if (prestazione.getCodPrestazione().equals("R_A.2.3")
										|| prestazione.getCodPrestazione().equals("R_B.8.2")
										|| prestazione.getCodPrestazione().equals("R_C.3.4")
										|| prestazione.getCodPrestazione().equals("R_D.1.2")
										|| prestazione.getCodPrestazione().equals("R_E.4.2")) {

									BigDecimal totaleUtenza = BigDecimal.ZERO;

									if (prestazione.getTipoPrestazione().equals("MA03")) {
										for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
											if (utenza.getValore() != null) {
												totaleUtenza = totaleUtenza.add(Util.convertStringToBigDecimal(
														utenza.getValore().replace(".", ",")));
											}
										}
									} else {
										for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
											if (utenza.getValore() != null) {
												totaleUtenza = totaleUtenza.add(Util.convertStringToBigDecimal(
														utenza.getValore().replace(".", ",")));
											}
										}
									}

									if (totaleUtenza.compareTo(percentValue) == 1) {
										elencoerrori.getWarnings()
												.add(listeService
														.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_07)
														.getTestoMessaggio()
														.replace("PRESTAZIONE", "'" + prestazione.getCodPrestazione()
																+ " " + prestazione.getDescPrestazione() + "'"));
									}
								}
							}

							// CONTROLLO 6, verifica valorizzazioni per Macroaggregati e Utenze, per ogni
							// Prestazione
							String prestNotVal = "";
							for (ModelB1Voci prestazione : rendModB1) {
								Boolean macroValorizzato = false;
								Boolean utenzaValorizzata = false;
								// Valorizzato Macroaggregato
								for (ModelB1Macroaggregati macroaggregato : prestazione.getMacroaggregati()) {
									if (macroaggregato.getValore() != null) {
										macroValorizzato = true;
									}
								}
								// Valorizzata Utenza
								if (prestazione.getTipoPrestazione().equals("MA03")) {
									for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenzeCostoTotale()) {
										if (utenza.getValore() != null) {
											utenzaValorizzata = true;
										}
									}
								} else {
									for (ModelB1UtenzaPrestReg1 utenza : prestazione.getUtenze()) {
										if (utenza.getValore() != null) {
											utenzaValorizzata = true;
										}
									}
								}

								if (!macroValorizzato && !utenzaValorizzata) {
									prestNotVal = prestNotVal + prestazione.getCodPrestazione() + ", ";
								}
							}
							if (!prestNotVal.equals("")) {
								elencoerrori.getErrors()
										.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_09)
												.getTestoMessaggio().replace("PRESTAZIONI",
														prestNotVal.substring(0, prestNotVal.length() - 2)));
							}
							// CONTROLLO 7 SFU V.05
							BigDecimal sommaValoriB1TargetFamiglia = BigDecimal.ZERO;
							for (ModelB1Voci prestazione : rendModB1) {
								for (int i = 0; i < prestazione.getUtenze().size(); i++) {
									if (prestazione.getUtenze().get(i).getCodice().equals("U01")) {
										String valore = prestazione.getUtenze().get(i).getValore();
										if (valore != null) {
											sommaValoriB1TargetFamiglia = sommaValoriB1TargetFamiglia
													.add(Util.convertStringToBigDecimal(valore.replace(".", ",")));
										}
									}
								}
								for (int i = 0; i < prestazione.getUtenzeCostoTotale().size(); i++) {
									if (prestazione.getUtenzeCostoTotale().get(i).getCodice().equals("U01")) {
										String valore = prestazione.getUtenzeCostoTotale().get(i).getValore();
										if (valore != null) {
											sommaValoriB1TargetFamiglia = sommaValoriB1TargetFamiglia
													.add(Util.convertStringToBigDecimal(valore.replace(".", ",")));
										}
									}
								}
							}
							BigDecimal fondoFNPS = BigDecimal.ZERO;
							List<ModelFondi> fondi = configuratoreUtenzeFnpsService
									.findFondiByidRendicontazione(rendicontazione.getIdRendicontazioneEnte());
							ModelFondi fondo = fondi.stream()
									.filter(item -> item.getCodTipologiaFondo().equals("FONDO")).findFirst()
									.orElse(null);
							if (fondo == null || (fondo != null && fondo.getValore().equals(BigDecimal.ZERO))) {
								fondoFNPS = BigDecimal.ZERO;
							} else {
								fondoFNPS = fondo.getValore().divide(new BigDecimal(2));
							}
							FondiEnteAllontanamentoZero fondiAlZero = modelloAllontanamentoZeroService
									.findFondiEnteAllontanamentoZero(
											BigInteger.valueOf(rendicontazione.getIdRendicontazioneEnte()));
							BigDecimal quotaAlZero = fondiAlZero.getQuotaAllontanamentoZero();
							// GET VOCI MODELLO A
							List<ModelTitoloModA> titoliModAControllo7 = modelloAService
									.getListaVociModA(rendicontazione.getIdRendicontazioneEnte()).getListaTitoli();
							for (ModelTitoloModA titolo : titoliModAControllo7) {
								for (ModelTipologiaModA tipologia : titolo.getListaTipologie()) {
									for (ModelVoceModA voce : tipologia.getListaVoci()) {
										if (voce.getPrestazioni() != null) {
											for (ModelPrestazioneUtenzaModA prestazioneCD : voce.getPrestazioni()
													.getPrestazioniCD()) {
												for (ModelTargetUtenza targetUtenza : prestazioneCD
														.getListaTargetUtenza()) {
													if (targetUtenza.getCodTargetUtenza().equals("U01")) {
														if (targetUtenza.getValore() != null) {
															sommaValoriB1TargetFamiglia = sommaValoriB1TargetFamiglia
																	.subtract(targetUtenza.getValore());
														}
													}
												}
											}
											for (ModelPrestazioneUtenzaModA prestazioneRS : voce.getPrestazioni()
													.getPrestazioniRS()) {
												for (ModelTargetUtenza targetUtenza : prestazioneRS
														.getListaTargetUtenza()) {
													if (targetUtenza.getCodTargetUtenza().equals("U01")) {
														if (targetUtenza.getValore() != null) {
															sommaValoriB1TargetFamiglia = sommaValoriB1TargetFamiglia
																	.subtract(targetUtenza.getValore());
														}
													}
												}
											}
										}
									}
								}
							}
							// CONTROLLO
							if (quotaAlZero != null) {
								if (!quotaAlZero.equals(BigDecimal.ZERO) && !fondoFNPS.equals(BigDecimal.ZERO)) {
									BigDecimal sommaFNPSALZERO = fondoFNPS.add(quotaAlZero);
									if (sommaValoriB1TargetFamiglia.compareTo(sommaFNPSALZERO) < 0) {
										elencoerrori.getWarnings()
												.add(listeService
														.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODB1_12)
														.getTestoMessaggio());
									}
								}
							}

							// CHECK
							GenericResponseWarnErr response = modelloB1Service
									.checkModelloB1(rendicontazione.getIdRendicontazioneEnte());
							if (response.getWarnings().size() > 0) {
								List<GregRCheck> checkA = datiRendicontazioneDao.findAllMotivazioneByTab(
										SharedConstants.MODELLO_B1, rendicontazione.getIdRendicontazioneEnte());
								if (checkA.size() > 0) {
									modellimotivazioneEsisteCheck = modellimotivazioneEsisteCheck
											+ SharedConstants.MODELLO_B1 + ", ";
									motivazioneEsiste = true;
								} else {
									modelliCheck = modelliCheck + SharedConstants.MODELLO_B1 + ", ";
									obblCheck = true;
								}
							}
						} else if (modello.getModello().equalsIgnoreCase(SharedConstants.MODELLO_B)) {
							// Controlli Modello B
							ModelRendicontazioneModB rendModB = modelloBService
									.getRendicontazioneModB(rendicontazione.getIdRendicontazioneEnte());
							ModelRendicontazioneTotaliMacroaggregati totaliMacroaggregati = macroaggregatiService
									.getRendicontazioneTotaliMacroaggregatiPerB1(
											rendicontazione.getIdRendicontazioneEnte());
							ModelRendicontazioneTotaliSpeseMissioni totaliSpeseMissione = macroaggregatiService
									.getRendicontazioneTotaliSpesePerB(rendicontazione.getIdRendicontazioneEnte());
							List<GregRRendicontazioneModAPart1> rendModA = modelloADao
									.findAllValoriModAByEnte(rendicontazione.getIdRendicontazioneEnte());
							BigDecimal totaleTitolo2e3 = BigDecimal.ZERO.setScale(2);
							BigDecimal totaleMissione01041215 = BigDecimal.ZERO.setScale(2);
							String errorMessageB = listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMACRO_02).getTestoMessaggio();
							// Controllo Totale Titolo 02 e 03 ModA e totale Missione 01, 04, 12, 15 modB
							for (GregRRendicontazioneModAPart1 rend : rendModA) {
								if (rend.getGregRTitoloTipologiaVoceModA().getGregDTitoloModA().getCodTitoloModA()
										.equals("02")
										|| rend.getGregRTitoloTipologiaVoceModA().getGregDTitoloModA()
												.getCodTitoloModA().equals("03")) {
									totaleTitolo2e3 = totaleTitolo2e3.add(rend.getValoreNumb().setScale(2));
								}
							}
							for (ModelBMissioni missione : rendModB.getListaMissioni()) {
								if (missione.getCodMissione().equals("01") || missione.getCodMissione().equals("04")
										|| missione.getCodMissione().equals("12")
										|| missione.getCodMissione().equals("15")) {
									for (ModelBProgramma programma : missione.getListaProgramma()) {
										for (ModelBTitolo titolo : programma.getListaTitolo()) {
											if (titolo.getCodTitolo().equals("1")) {
												if (titolo.getValore() != null) {
													totaleMissione01041215 = totaleMissione01041215
															.add(titolo.getValore().setScale(2));
												}
											}
										}
									}
								}
							}
							BigDecimal diff = totaleTitolo2e3.subtract(totaleMissione01041215).abs().setScale(2);
							if (diff.compareTo(new BigDecimal(100000).setScale(2)) > 0) {
								BigDecimal fraz = diff.divide(totaleTitolo2e3, 2, RoundingMode.UP).setScale(2);
								float r = (float) 0.20;
								BigDecimal res = new BigDecimal(r).setScale(2, RoundingMode.HALF_DOWN);
								if (fraz.compareTo(res) > 0) {
									elencoerrori.getWarnings().add(listeService
											.getMessaggio(SharedConstants.WARNING_MOD_B_03).getTestoMessaggio());
									elencoerrori.setObblMotivazione(true);
								}
							}

							// Controllo missione Altro
							GregRRendicontazioneModAPart1 titolo2 = datiRendicontazioneDao
									.getRendicontazioneModAByIdRendicontazione(
											rendicontazione.getIdRendicontazioneEnte(), "02-ALTRO", "Tipo_altro", "31");
							BigDecimal totaleAltro = BigDecimal.ZERO.setScale(2);
							for (ModelBMissioni missione : rendModB.getListaMissioni()) {
								if (missione.getCodMissione().equals("ALT")) {
									for (ModelBProgramma programma : missione.getListaProgramma()) {
										for (ModelBTitolo titolo : programma.getListaTitolo()) {
											if (titolo.getValore() != null) {
												totaleAltro = totaleAltro.add(titolo.getValore().setScale(2));
											}
										}
									}
								}
							}

							if (titolo2 != null) {
								if (titolo2.getValoreNumb() != null
										&& !titolo2.getValoreNumb().equals(BigDecimal.ZERO)) {
									if (totaleAltro == null || totaleAltro.equals(BigDecimal.ZERO.setScale(2))) {
										elencoerrori.getErrors()
												.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIB_01)
														.getTestoMessaggio());
									}
								}
							}

							// CONTROLLO 1, Verifica TotaleSpesaCorrenteMis12 e Mis04(dicui)
							// prelevo gli importi di cui, Missione 04 Programma 06 Titolo 1
							BigDecimal sommaDicuiMiss04Prog06Tit1 = BigDecimal.ZERO.setScale(2);
							for (ModelBMissioni missione : rendModB.getListaMissioni()) {
								if (missione.getCodMissione().equals("04")) {
									for (ModelBProgramma programma : missione.getListaProgramma()) {
										if (programma.getCodProgramma().equals("0406")) {
											for (ModelBTitolo titolo : programma.getListaTitolo()) {
												if (titolo.getCodTitolo().equals("1")) {
													for (ModelBSottotitolo sottotitolo : titolo.getListaSottotitolo()) {
														if (sottotitolo.getCodSottotitolo().equals("11")
																|| sottotitolo.getCodSottotitolo().equals("12")) {
															if (sottotitolo.getValore() != null) {
																sommaDicuiMiss04Prog06Tit1 = sommaDicuiMiss04Prog06Tit1
																		.add(sottotitolo.getValore());
															}
														}
													}
												}
											}
										}
									}
								}
							}

							// Calcolo Totale Missione 12, partendo dagli importi del Modello B1 (devo poi
							// sottrarre i due di cui come come nella webapp per Prog1/Tit1 e Prog2/Tit1)
							BigDecimal totaleMissione12 = BigDecimal.ZERO.setScale(2);
							List<ModelB1ProgrammiMissioneTotali> importiMissione12FromModB1 = modelloB1Service
									.getProgrammiMissioneTotali(rendicontazione.getIdRendicontazioneEnte());
							for (ModelB1ProgrammiMissioneTotali importoMissione : importiMissione12FromModB1) {
								if (importoMissione.getValore() != null) {
									totaleMissione12 = totaleMissione12.add(importoMissione.getValore());
								}
							}
							totaleMissione12 = totaleMissione12.subtract(sommaDicuiMiss04Prog06Tit1).setScale(2,
									RoundingMode.HALF_UP);

							// Calcolo il TotaleSpesaCorrenteMis12 e lo confronto con il
							// TotaleMacroaggregati
							BigDecimal totaleSpesaCorrenteMis12Mis04Dicui = BigDecimal.ZERO.setScale(2);
							totaleSpesaCorrenteMis12Mis04Dicui = totaleMissione12.add(sommaDicuiMiss04Prog06Tit1);
							// Prelevo il totale Macroaggregati
							BigDecimal totaleMissioniMacro = BigDecimal.ZERO.setScale(2);
							totaleMissioniMacro = totaleMissioniMacro.add(macroaggregatiService
									.getRendicontazioneTotaliMacroaggregatiPerB1(
											rendicontazione.getIdRendicontazioneEnte())
									.getValoriMacroaggregati().get(7).getTotale());
							// verifico che il totaleMissioni Macro sia uguale al (totaleSpesaCorrenteMis12
							// + di cui Miss04)
							if (!totaleSpesaCorrenteMis12Mis04Dicui.equals(totaleMissioniMacro)) {
								String errMsgB1 = errorMessageB;
								elencoerrori.getErrors().add(errMsgB1.replace("QUALEIMPORTO",
										"TOTALE SPESA CORRENTE della MISSIONE 12 E della MISSIONE 04 (solo la parte concernente il Sostegno Socio Educativo Scolastico per minori stranieri e minori disabili - compreso Trasporto scolastico Disabili)")
										.replace("IMPORTO",
												Util.convertBigDecimalToString(totaliMacroaggregati
														.getValoriMacroaggregati().get(7).getTotale()))
										.replace("MODELLO", "Macroaggregati"));
							}

							// CONTROLLO 2, Verifico TotaleSpesaCorrenteTutteMissioni
							BigDecimal totaleSpesaCorrenteTutteMissioni = BigDecimal.ZERO.setScale(2);
							for (ModelBMissioni missione : rendModB.getListaMissioni()) {
								for (ModelBProgramma programma : missione.getListaProgramma()) {
									for (ModelBTitolo titolo : programma.getListaTitolo()) {
										if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
											totaleSpesaCorrenteTutteMissioni = totaleSpesaCorrenteTutteMissioni
													.add(titolo.getValore());
										}
									}
								}
							}
							BigDecimal totaliTutteMissioniMacroaggregati = totaliSpeseMissione.getValoriSpese().get(3)
									.getTotale().setScale(2);
							if (!totaleSpesaCorrenteTutteMissioni.equals(totaliTutteMissioniMacroaggregati)) {
								String errMsgB2 = errorMessageB;
								elencoerrori.getErrors().add(errMsgB2
										.replace("QUALEIMPORTO", "TOTALE SPESA CORRENTE DI TUTTE LE MISSIONI")
										.replace("IMPORTO",
												Util.convertBigDecimalToString(
														totaliSpeseMissione.getValoriSpese().get(3).getTotale()))
										.replace("MODELLO", "Macroaggregati"));
							}

							// CONTROLLO 3, Verifica TotaleSpesaCorrenteMissione01
							BigDecimal totaleSpesaCorrenteMissione1 = BigDecimal.ZERO.setScale(2);
							for (ModelBMissioni missione : rendModB.getListaMissioni()) {
								if (missione.getCodMissione().equals("01")) {
									for (ModelBProgramma programma : missione.getListaProgramma()) {
										for (ModelBTitolo titolo : programma.getListaTitolo()) {
											if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
												totaleSpesaCorrenteMissione1 = totaleSpesaCorrenteMissione1
														.add(titolo.getValore());
											}
										}
									}
								}
							}
							BigDecimal totaleMissione1Macroaggregati = totaliSpeseMissione.getValoriSpese().get(0)
									.getTotale().setScale(2);
							if (!totaleSpesaCorrenteMissione1.equals(totaleMissione1Macroaggregati)) {
								String errMsgB3 = errorMessageB;
								elencoerrori.getErrors().add(errMsgB3
										.replace("QUALEIMPORTO", "TOTALE SPESE CORRENTI della MISSIONE 01")
										.replace("IMPORTO",
												Util.convertBigDecimalToString(
														totaliSpeseMissione.getValoriSpese().get(0).getTotale()))
										.replace("MODELLO", "Macroaggregati"));
							}

							// CONTROLLO 4, Verifica Missione04Programma06Titolo1
							BigDecimal totaleSpesaMissione = BigDecimal.ZERO.setScale(2);
							totaleSpesaMissione = totaleSpesaMissione
									.add(totaliSpeseMissione.getValoriSpese().get(2).getTotale().setScale(2));
							if (!sommaDicuiMiss04Prog06Tit1.equals(totaleSpesaMissione)) {
								String errMsgB4 = errorMessageB;
								elencoerrori.getErrors().add(errMsgB4
										.replace("QUALEIMPORTO", "valore di Missione04/Programma06/Titolo1")
										.replace("IMPORTO",
												Util.convertBigDecimalToString(
														totaliSpeseMissione.getValoriSpese().get(2).getTotale()))
										.replace("MODELLO", "Macroaggregati"));
							}

							// CONTROLLO 5, Verifica TotaleSpesaCorrenteMissione12
							BigDecimal totaleSpesaCorrenteMissione12 = BigDecimal.ZERO.setScale(2);
							for (ModelBMissioni missione : rendModB.getListaMissioni()) {
								if (missione.getCodMissione().equals("12")) {
									for (ModelBProgramma programma : missione.getListaProgramma()) {
										for (ModelBTitolo titolo : programma.getListaTitolo()) {
											if (titolo.getCodTitolo().equals("1") && titolo.getValore() != null) {
												totaleSpesaCorrenteMissione12 = totaleSpesaCorrenteMissione12
														.add(titolo.getValore());
											}
										}
									}
								}

							}
							BigDecimal totaleMissione12Macroaggregati = totaliSpeseMissione.getValoriSpese().get(1)
									.getTotale().setScale(2);
							if (!totaleSpesaCorrenteMissione12.equals(totaleMissione12Macroaggregati)) {
								String errMsgB5 = errorMessageB;
								elencoerrori.getErrors().add(errMsgB5
										.replace("QUALEIMPORTO", "TOTALE SPESE CORRENTI della MISSIONE 12")
										.replace("IMPORTO",
												Util.convertBigDecimalToString(
														totaliSpeseMissione.getValoriSpese().get(1).getTotale()))
										.replace("MODELLO",
												"Macroaggregati (valore Missione 01 - parte spesa socio-assistenziale sommato a valore Missione 12)"));
							}
							GenericResponseWarnErr response = modelloBService
									.checkModelloB(rendicontazione.getIdRendicontazioneEnte());
							if (response.getWarnings().size() > 0) {
								List<GregRCheck> checkA = datiRendicontazioneDao.findAllMotivazioneByTab(
										SharedConstants.MODELLO_B, rendicontazione.getIdRendicontazioneEnte());
								if (checkA.size() > 0) {
									modellimotivazioneEsisteCheck = modellimotivazioneEsisteCheck
											+ SharedConstants.MODELLO_B + ", ";
									motivazioneEsiste = true;
								} else {
									modelliCheck = modelliCheck + SharedConstants.MODELLO_B + ", ";
									obblCheck = true;
								}
							}
						} else if (modello.getModello().equalsIgnoreCase(SharedConstants.MODELLO_C)) {
							// Controlli Modello C
							ModelRendicontazioneModC rendModC = modelloCService
									.getRendicontazioneModC(rendicontazione.getIdRendicontazioneEnte());
							String errorMessageC1 = listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_01).getTestoMessaggio();
							String errorMessageC2 = listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_02).getTestoMessaggio();
							String errorMessageC3 = listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_03).getTestoMessaggio();
							String errorMessageC4 = listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_04).getTestoMessaggio();
							String errorMessageC5 = listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_05).getTestoMessaggio();
							String errorMessageC6 = listeService
									.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODC_06).getTestoMessaggio();

							// CONTROLLO 1, Controllo NucleiFamiliari non deve superare somma target utenza
							for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
								BigDecimal numNucleiFamiliari = BigDecimal.ZERO;
								BigDecimal totaleTargetUtenze = BigDecimal.ZERO;
								for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
									if (!targetUtenza.getCodTargetUtenze().equals("U28")
											&& targetUtenza.getValore() != null) {
										totaleTargetUtenze = totaleTargetUtenze.add(targetUtenza.getValore());
									} else if (targetUtenza.getCodTargetUtenze().equals("U28")
											&& targetUtenza.getValore() != null) {
										numNucleiFamiliari = numNucleiFamiliari.add(targetUtenza.getValore());
									}
								}
								if (!numNucleiFamiliari.equals(BigDecimal.ZERO)
										&& !totaleTargetUtenze.equals(BigDecimal.ZERO)
										&& numNucleiFamiliari.compareTo(totaleTargetUtenze) == 1) {
									String errMsgC1 = errorMessageC1;
									elencoerrori.getErrors()
											.add(errMsgC1.replace("PRESTAZIONE", prestazione.getDescPrestazione()));
								}
							}

							// CONTROLLO 2, Controllo somma valori di cui inferiore al totale di
							// riferimento, Prest. Serv. Soc. Professionale
							for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
								if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
									for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
										BigDecimal totaleTargetUtenza = targetUtenza.getValore() != null
												? targetUtenza.getValore()
												: BigDecimal.ZERO;
										for (ModelCDettagliUtenze dettaglioUtenza : targetUtenza
												.getListaDettagliUtenze()) {
											if (dettaglioUtenza.getValore() != null
													&& dettaglioUtenza.getValore().compareTo(totaleTargetUtenza) == 1) {
												String errMsgC2 = errorMessageC2;
												elencoerrori.getErrors()
														.add(errMsgC2
																.replace("DICUI",
																		dettaglioUtenza.getDescDettagliUtenze())
																.replace("UTENZA", targetUtenza.getDescTargetUtenze()));
											}

										}
									}
								}
							}

							// CONTROLLO 3, Controllo TotaleMinoriDisabili con TotalePerDisabilita
							for (ModelCPrestazioni prestazione1 : rendModC.getListaPrestazioni()) {
								if (prestazione1.getCodPrestazione().equals("R_A.2.1")) {
									for (ModelCTargetUtenze targetUtenza1 : prestazione1.getListaTargetUtenze()) {
										if (targetUtenza1.getCodTargetUtenze().equals("U23")) {
											BigDecimal totaleTargetUtenzaDisabilitaMinori = targetUtenza1
													.getValore() != null ? targetUtenza1.getValore() : BigDecimal.ZERO;
											BigDecimal totaleSpecificaDisabilitaMinori = BigDecimal.ZERO;
											for (ModelCDisabilita disabilita1 : targetUtenza1.getListaDisabilita()) {
												if (!disabilita1.getCodDisabilita().equals("DIS08")
														&& disabilita1.getValore() != null) {
													totaleSpecificaDisabilitaMinori = totaleSpecificaDisabilitaMinori
															.add(disabilita1.getValore());
												}
											}
											if (!totaleSpecificaDisabilitaMinori.equals(BigDecimal.ZERO)
													&& !totaleTargetUtenzaDisabilitaMinori.equals(BigDecimal.ZERO)
													&& !totaleSpecificaDisabilitaMinori
															.equals(totaleTargetUtenzaDisabilitaMinori)) {
												String errMsgC3 = errorMessageC3;
												elencoerrori.getErrors().add(errMsgC3.replaceAll("MINADUL", "Minori")
														.replace("PRESTAZIONE", prestazione1.getDescPrestazione()));
											}
										}
									}
								}
							}

							// CONTROLLO 4, Controllo TotaleAdultiDisabili con TotalePerDisabilita
							for (ModelCPrestazioni prestazione2 : rendModC.getListaPrestazioni()) {
								if (prestazione2.getCodPrestazione().equals("R_A.2.1")) {
									for (ModelCTargetUtenze targetUtenza2 : prestazione2.getListaTargetUtenze()) {
										if (targetUtenza2.getCodTargetUtenze().equals("U25")) {
											BigDecimal totaleTargetUtenzaDisabilitaAdulti = targetUtenza2
													.getValore() != null ? targetUtenza2.getValore() : BigDecimal.ZERO;
											BigDecimal totaleSpecificaDisabilitaAdulti = BigDecimal.ZERO;
											for (ModelCDisabilita disabilita2 : targetUtenza2.getListaDisabilita()) {
												if (!disabilita2.getCodDisabilita().equals("DIS09")
														&& disabilita2.getValore() != null) {
													totaleSpecificaDisabilitaAdulti = totaleSpecificaDisabilitaAdulti
															.add(disabilita2.getValore());
												}
											}
											if (!totaleSpecificaDisabilitaAdulti.equals(BigDecimal.ZERO)
													&& !totaleTargetUtenzaDisabilitaAdulti.equals(BigDecimal.ZERO)
													&& !totaleSpecificaDisabilitaAdulti
															.equals(totaleTargetUtenzaDisabilitaAdulti)) {

												String errMsgC4 = errorMessageC3;
												elencoerrori.getErrors().add(errMsgC4.replaceAll("MINADUL", "Adulti")
														.replace("PRESTAZIONE", prestazione2.getDescPrestazione()));
											}
										}
									}
								}
							}

							// CONTROLLO 5, Controllo NucleiFamiliari con MinoriDisabili per disabilita
							for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
								if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
									for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
										if (targetUtenza.getCodTargetUtenze().equals("U23")) {
											BigDecimal nucleiFamiliariConMinoriDisabili = BigDecimal.ZERO;
											BigDecimal totSpecDisabMinori = BigDecimal.ZERO;
											for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
												if (!disabilita.getCodDisabilita().equals("DIS08")
														&& disabilita.getValore() != null) {
													totSpecDisabMinori = totSpecDisabMinori.add(disabilita.getValore());
												}
												if (disabilita.getCodDisabilita().equals("DIS08")
														&& disabilita.getValore() != null) {
													nucleiFamiliariConMinoriDisabili = nucleiFamiliariConMinoriDisabili
															.add(disabilita.getValore());
												}
											}
											if ((!nucleiFamiliariConMinoriDisabili.equals(BigDecimal.ZERO)
													&& !totSpecDisabMinori.equals(BigDecimal.ZERO)
													&& nucleiFamiliariConMinoriDisabili
															.compareTo(totSpecDisabMinori) > 0)
													|| nucleiFamiliariConMinoriDisabili
															.compareTo(totSpecDisabMinori) > 0) {
												String errMsgC5 = errorMessageC4;
												elencoerrori.getErrors().add(errMsgC5.replaceAll("MINADUL", "Minori"));
											}
										}
									}
								}
							}

							// CONTROLLO 6, Controllo NucleiFamiliari con AdultiDisabili per disabilita
							for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
								if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
									for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
										if (targetUtenza.getCodTargetUtenze().equals("U25")) {
											BigDecimal nucleiFamiliariConAdultiDisabili = BigDecimal.ZERO;
											BigDecimal totSpecDisabAdulti = BigDecimal.ZERO;
											for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
												if (!disabilita.getCodDisabilita().equals("DIS09")
														&& disabilita.getValore() != null) {
													totSpecDisabAdulti = totSpecDisabAdulti.add(disabilita.getValore());
												}
												if (disabilita.getCodDisabilita().equals("DIS09")
														&& disabilita.getValore() != null) {
													nucleiFamiliariConAdultiDisabili = nucleiFamiliariConAdultiDisabili
															.add(disabilita.getValore());
												}
											}
											if ((!nucleiFamiliariConAdultiDisabili.equals(BigDecimal.ZERO)
													&& !totSpecDisabAdulti.equals(BigDecimal.ZERO)
													&& nucleiFamiliariConAdultiDisabili
															.compareTo(totSpecDisabAdulti) > 0)
													|| nucleiFamiliariConAdultiDisabili
															.compareTo(totSpecDisabAdulti) > 0) {
												String errMsgC6 = errorMessageC4;
												elencoerrori.getErrors().add(errMsgC6.replaceAll("MINADUL", "Adulti"));
											}
										}
									}
								}
							}

							// CONTROLLO 7, Controllo NucleiFamiliari minore di somma target utenza
							for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
								if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
									for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
										// di cui Minori disabili
										if (targetUtenza.getCodTargetUtenze().equals("U23")) {
											for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
												if (disabilita.getCodDisabilita().equals("DIS01")
														|| disabilita.getCodDisabilita().equals("DIS02")
														|| disabilita.getCodDisabilita().equals("DIS03")) {
													BigDecimal valueDisabilitaMinori = disabilita.getValore() != null
															? disabilita.getValore()
															: BigDecimal.ZERO;
													for (ModelCDettagliDisabilita dettaglioDisabilita : disabilita
															.getListaDettagliDisabilita()) {
														if (dettaglioDisabilita.getValore() != null
																&& dettaglioDisabilita.getValore()
																		.compareTo(valueDisabilitaMinori) == 1) {
															String errMsgC7 = errorMessageC5;
															elencoerrori.getErrors().add(errMsgC7
																	.replace("PRESTAZIONE",
																			prestazione.getDescPrestazione())
																	.replace("DICUI",
																			dettaglioDisabilita
																					.getDescDettagliDisabilita())
																	.replace("DISABILITA",
																			disabilita.getDescDisabilita())
																	.replace("MINADUL", "Minori"));
														}
													}
												}
											}
										}
										// di cui Adulti disabili
										if (targetUtenza.getCodTargetUtenze().equals("U25")) {
											for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
												if (disabilita.getCodDisabilita().equals("DIS01")
														|| disabilita.getCodDisabilita().equals("DIS02")
														|| disabilita.getCodDisabilita().equals("DIS03")) {
													BigDecimal valueDisabilitaAdulti = disabilita.getValore() != null
															? disabilita.getValore()
															: BigDecimal.ZERO;
													for (ModelCDettagliDisabilita dettaglioDisabilita : disabilita
															.getListaDettagliDisabilita()) {
														if (dettaglioDisabilita.getValore() != null
																&& dettaglioDisabilita.getValore()
																		.compareTo(valueDisabilitaAdulti) == 1) {
															String errMsgC8 = errorMessageC5;
															elencoerrori.getErrors().add(errMsgC8
																	.replace("PRESTAZIONE",
																			prestazione.getDescPrestazione())
																	.replace("DICUI",
																			dettaglioDisabilita
																					.getDescDettagliDisabilita())
																	.replace("DISABILITA",
																			disabilita.getDescDisabilita())
																	.replace("MINADUL", "Adulti"));
														}
													}
												}
											}
										}
									}
								}
							}

							// CONTROLLO 8, Nuclei Totali < (Nuclei Totali Adulti + Nuclei Totali Minori)
							for (ModelCPrestazioni prestazione : rendModC.getListaPrestazioni()) {
								if (prestazione.getCodPrestazione().equals("R_A.2.1")) {
									BigDecimal totaleNucleiFamiliari = BigDecimal.ZERO;
									BigDecimal totaleNucleiDisabiliAdultiMinori = BigDecimal.ZERO;

									for (ModelCTargetUtenze targetUtenza : prestazione.getListaTargetUtenze()) {
										if (targetUtenza.getCodTargetUtenze().equals("U28")) {
											totaleNucleiFamiliari = targetUtenza.getValore() != null
													? targetUtenza.getValore()
													: BigDecimal.ZERO;
										}
										if (targetUtenza.getCodTargetUtenze().equals("U23")
												|| targetUtenza.getCodTargetUtenze().equals("U25")) {
											for (ModelCDisabilita disabilita : targetUtenza.getListaDisabilita()) {
												if (disabilita.getCodDisabilita().equals("DIS08")
														|| disabilita.getCodDisabilita().equals("DIS09")) {
													totaleNucleiDisabiliAdultiMinori = totaleNucleiDisabiliAdultiMinori
															.add(disabilita.getValore() != null ? disabilita.getValore()
																	: BigDecimal.ZERO);
												}
											}
										}
									}
									if (!totaleNucleiFamiliari.equals(BigDecimal.ZERO)
											&& !totaleNucleiDisabiliAdultiMinori.equals(BigDecimal.ZERO)
											&& totaleNucleiDisabiliAdultiMinori.compareTo(totaleNucleiFamiliari) == 1) {
										elencoerrori.getErrors().add(errorMessageC6);
									}
								}
							}
							GenericResponseWarnErr response = modelloCService
									.checkModelloC(rendicontazione.getIdRendicontazioneEnte());
							if (response.getWarnings().size() > 0) {
								List<GregRCheck> checkA = datiRendicontazioneDao.findAllMotivazioneByTab(
										SharedConstants.MODELLO_C, rendicontazione.getIdRendicontazioneEnte());
								if (checkA.size() > 0) {
									modellimotivazioneEsisteCheck = modellimotivazioneEsisteCheck
											+ SharedConstants.MODELLO_C + ", ";
									motivazioneEsiste = true;
								} else {
									modelliCheck = modelliCheck + SharedConstants.MODELLO_C + ", ";
									obblCheck = true;
								}
							}
						} else if (modello.getModello().equalsIgnoreCase(SharedConstants.MODELLO_F)) {
							// Controlli Modello F
							ModelRendicontazioneModF rendModF = modelloFService
									.getRendicontazioneModF(rendicontazione.getIdRendicontazioneEnte());
							ModelFConteggioPersonale conteggioPersonale = rendModF.getConteggi()
									.getConteggioPersonale();
							ModelFConteggioOre conteggioOre = rendModF.getConteggi().getConteggioOre();
							String errorMessageF = listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_01)
									.getTestoMessaggio();

							// CONTROLLO 1, Controllo NucleiFamiliari minore di somma target utenza
							BigDecimal totaleAssistenzaSociale = BigDecimal.ZERO;
							BigDecimal totaleEducatore = BigDecimal.ZERO;
							BigDecimal totaleMediatoreCulturale = BigDecimal.ZERO;
							BigDecimal totalePsicologo = BigDecimal.ZERO;
							BigDecimal totalePedagogista = BigDecimal.ZERO;
							BigDecimal totaleSociologo = BigDecimal.ZERO;
							BigDecimal totaleOssAdbOta = BigDecimal.ZERO;
							BigDecimal totaleInfermiere = BigDecimal.ZERO;
							BigDecimal totaleAltro = BigDecimal.ZERO;
							BigDecimal totaleAmministrativo = BigDecimal.ZERO;

							for (ModelFPersonaleEnte personaleEnte : conteggioPersonale.getListaPersonaleEnte()) {
								if (personaleEnte.getCodPersonaleEnte().equals("01")
										|| personaleEnte.getCodPersonaleEnte().equals("02")
										|| personaleEnte.getCodPersonaleEnte().equals("03")
										|| personaleEnte.getCodPersonaleEnte().equals("04")
										|| personaleEnte.getCodPersonaleEnte().equals("05")) {
									for (ModelFValori valore : personaleEnte.getListaValori()) {
										if (valore.getCodProfiloProfessionale().equals("01")) {
											totaleAssistenzaSociale = totaleAssistenzaSociale.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
										if (valore.getCodProfiloProfessionale().equals("02")) {
											totaleEducatore = totaleEducatore.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
										if (valore.getCodProfiloProfessionale().equals("03")) {
											totaleMediatoreCulturale = totaleMediatoreCulturale.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
										if (valore.getCodProfiloProfessionale().equals("04")) {
											totalePsicologo = totalePsicologo.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
										if (valore.getCodProfiloProfessionale().equals("05")) {
											totalePedagogista = totalePedagogista.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
										if (valore.getCodProfiloProfessionale().equals("06")) {
											totaleSociologo = totaleSociologo.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
										if (valore.getCodProfiloProfessionale().equals("07")) {
											totaleOssAdbOta = totaleOssAdbOta.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
										if (valore.getCodProfiloProfessionale().equals("08")) {
											totaleInfermiere = totaleInfermiere.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
										if (valore.getCodProfiloProfessionale().equals("09")) {
											totaleAltro = totaleAltro.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
										if (valore.getCodProfiloProfessionale().equals("10")) {
											totaleAmministrativo = totaleAmministrativo.add(
													valore.getValore() != null ? valore.getValore() : BigDecimal.ZERO);
										}
									}
								}
								// di cui
								else if (personaleEnte.getCodPersonaleEnte().equals("06")
										|| personaleEnte.getCodPersonaleEnte().equals("07")
										|| personaleEnte.getCodPersonaleEnte().equals("08")) {
									for (ModelFValori valore : personaleEnte.getListaValori()) {
										if (valore.getCodProfiloProfessionale().equals("01")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null
													&& !totaleAssistenzaSociale.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totaleAssistenzaSociale) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("01")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC6 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC6
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
										if (valore.getCodProfiloProfessionale().equals("02")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null && !totaleEducatore.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totaleEducatore) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("02")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC7 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC7
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
										if (valore.getCodProfiloProfessionale().equals("03")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null
													&& !totaleMediatoreCulturale.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totaleMediatoreCulturale) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("03")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC8 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC8
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
										if (valore.getCodProfiloProfessionale().equals("04")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null && !totalePsicologo.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totalePsicologo) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("04")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC9 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC9
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
										if (valore.getCodProfiloProfessionale().equals("05")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null && !totalePedagogista.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totalePedagogista) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("05")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC10 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC10
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
										if (valore.getCodProfiloProfessionale().equals("06")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null && !totaleSociologo.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totaleSociologo) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("06")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC11 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC11
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
										if (valore.getCodProfiloProfessionale().equals("07")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null && !totaleOssAdbOta.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totaleOssAdbOta) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("07")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC12 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC12
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
										if (valore.getCodProfiloProfessionale().equals("08")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null && !totaleInfermiere.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totaleInfermiere) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("08")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC13 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC13
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
										if (valore.getCodProfiloProfessionale().equals("09")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null && !totaleAltro.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totaleAltro) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("09")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC13 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC13
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
										if (valore.getCodProfiloProfessionale().equals("10")) {
											String descProfiloProfessionale = "";
											if (valore.getValore() != null && !totaleAltro.equals(BigDecimal.ZERO)
													&& valore.getValore().compareTo(totaleAmministrativo) == 1) {
												for (ModelFProfiloProfessionale profilo : conteggioPersonale
														.getListaProfiloProfessionale()) {
													if (profilo.getCodProfiloProfessionale().equals("10")) {
														descProfiloProfessionale = profilo
																.getDescProfiloProfessionale();
													}
												}
												String errMsgC13 = errorMessageF;
												elencoerrori.getErrors()
														.add(errMsgC13
																.replace("DICUI", personaleEnte.getDescPersonaleEnte())
																.replace("PROFILO", descProfiloProfessionale));
											}
										}
									}
								}
							}

							List<MonteOre> monteOreDip = new ArrayList<MonteOre>();
							List<MonteOre> monteOreNonDip = new ArrayList<MonteOre>();

							List<GregDProfiloProfessionale> profili = modelloFDao.findAllProfiloProfessionale();

							for (GregDProfiloProfessionale profilo : profili) {
								MonteOre monteD = new MonteOre();
								monteD.setCodProfessionale(profilo.getCodProfiloProfessionale());
								monteOreDip.add(monteD);
								MonteOre monteND = new MonteOre();
								monteND.setCodProfessionale(profilo.getCodProfiloProfessionale());
								monteOreNonDip.add(monteND);
							}

							BigDecimal[] monteDip = new BigDecimal[profili.size()];
							BigDecimal[] monteNonDip = new BigDecimal[profili.size()];
							for (int i = 0; i < profili.size(); i++) {
								monteDip[i] = BigDecimal.ZERO;
								monteNonDip[i] = BigDecimal.ZERO;
							}
							for (int i = 0; i < conteggioPersonale.getListaPersonaleEnte().size(); i++) {
								for (int j = 0; j < conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori()
										.size(); j++) {
									if (conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
											.equals("01")
											|| conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
													.equals("02")
											|| conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
													.equals("03")
											|| conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
													.equals("04")) {
										if (conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori().get(j)
												.getCodProfiloProfessionale()
												.equals(monteOreDip.get(j).getCodProfessionale())) {
											if (conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori()
													.get(j).getValore() != null) {
												monteDip[j] = monteDip[j].add(conteggioPersonale.getListaPersonaleEnte()
														.get(i).getListaValori().get(j).getValore());
											}
										}
									} else if (conteggioPersonale.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
											.equals("05")) {
										if (conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori().get(j)
												.getCodProfiloProfessionale()
												.equals(monteOreDip.get(j).getCodProfessionale())) {
											if (conteggioPersonale.getListaPersonaleEnte().get(i).getListaValori()
													.get(j).getValore() != null) {
												monteNonDip[j] = monteNonDip[j]
														.add(conteggioPersonale.getListaPersonaleEnte().get(i)
																.getListaValori().get(j).getValore());
											}
										}
									}

								}
							}

							for (int i = 0; i < monteOreDip.size(); i++) {
								monteOreDip.get(i).setNumeroPersonale(monteDip[i]);
								monteOreNonDip.get(i).setNumeroPersonale(monteNonDip[i]);
							}

							for (int i = 0; i < profili.size(); i++) {
								monteDip[i] = BigDecimal.ZERO;
								monteNonDip[i] = BigDecimal.ZERO;
							}
							for (int i = 0; i < conteggioOre.getListaPersonaleEnte().size(); i++) {
								for (int j = 0; j < conteggioOre.getListaPersonaleEnte().get(i).getListaValori()
										.size(); j++) {
									if (conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte().equals("09")
											|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
													.equals("10")
											|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
													.equals("11")
											|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
													.equals("12")
											|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
													.equals("13")
											|| conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
													.equals("14")) {
										if (conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j)
												.getCodProfiloProfessionale()
												.equals(monteOreDip.get(j).getCodProfessionale())) {
											if (conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j)
													.getValore() != null) {
												monteDip[j] = monteDip[j].add(conteggioOre.getListaPersonaleEnte()
														.get(i).getListaValori().get(j).getValore());
											}
										}
									} else if (conteggioOre.getListaPersonaleEnte().get(i).getCodPersonaleEnte()
											.equals("15")) {
										if (conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j)
												.getCodProfiloProfessionale()
												.equals(monteOreDip.get(j).getCodProfessionale())) {
											if (conteggioOre.getListaPersonaleEnte().get(i).getListaValori().get(j)
													.getValore() != null) {
												monteNonDip[j] = monteNonDip[j].add(conteggioOre.getListaPersonaleEnte()
														.get(i).getListaValori().get(j).getValore());
											}
										}
									}

								}
							}

							for (int i = 0; i < monteOreDip.size(); i++) {
								monteOreDip.get(i).setNumeroOre(monteDip[i]);
								monteOreNonDip.get(i).setNumeroOre(monteNonDip[i]);
							}
							boolean persDip = false;
							boolean persNonDip = false;
							boolean persDip40 = false;
							boolean persNonDip40 = false;
							for (MonteOre monte : monteOreDip) {
								if (((monte.getNumeroOre().equals(BigDecimal.ZERO)
										&& monte.getNumeroPersonale().compareTo(BigDecimal.ZERO) > 0)
										|| (monte.getNumeroOre().compareTo(BigDecimal.ZERO) > 0
												&& monte.getNumeroPersonale().equals(BigDecimal.ZERO)))
										&& !persDip) {
									elencoerrori.getErrors()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_03)
													.getTestoMessaggio().replace("PERSONALE", "Personale dipendente"));
									persDip = true;
								}
								BigDecimal mon = !monte.getNumeroPersonale().equals(BigDecimal.ZERO)
										? monte.getNumeroOre().divide(monte.getNumeroPersonale(), 2, RoundingMode.UP)
										: BigDecimal.ZERO;
								if (mon.compareTo(new BigDecimal(40)) > 0 && !persDip40) {
									persDip40 = true;
									elencoerrori.getWarnings()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_02)
													.getTestoMessaggio().replace("PERSONALE", "Personale dipendente"));
								}
							}

							for (MonteOre monte : monteOreNonDip) {
								if (((monte.getNumeroOre().equals(BigDecimal.ZERO)
										&& monte.getNumeroPersonale().compareTo(BigDecimal.ZERO) > 0)
										|| (monte.getNumeroOre().compareTo(BigDecimal.ZERO) > 0
												&& monte.getNumeroPersonale().equals(BigDecimal.ZERO)))
										&& !persNonDip) {
									elencoerrori.getErrors()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_03)
													.getTestoMessaggio()
													.replace("PERSONALE", "Personale esternalizzato"));
									persNonDip = true;
								}
								BigDecimal mon = !monte.getNumeroPersonale().equals(BigDecimal.ZERO)
										? monte.getNumeroOre().divide(monte.getNumeroPersonale(), 2, RoundingMode.UP)
										: BigDecimal.ZERO;
								if (mon.compareTo(new BigDecimal(40)) > 0 && !persNonDip40) {
									persNonDip40 = true;
									elencoerrori.getWarnings()
											.add(listeService.getMessaggio(SharedConstants.ERROR_INVIO_MODELLIMODF_02)
													.getTestoMessaggio()
													.replace("PERSONALE", "Personale esternalizzato"));
								}
							}

							GenericResponseWarnErr response = modelloFService
									.checkModelloF(rendicontazione.getIdRendicontazioneEnte());
							if (response.getWarnings().size() > 0) {
								List<GregRCheck> checkA = datiRendicontazioneDao.findAllMotivazioneByTab(
										SharedConstants.MODELLO_F, rendicontazione.getIdRendicontazioneEnte());
								if (checkA.size() > 0) {
									modellimotivazioneEsisteCheck = modellimotivazioneEsisteCheck
											+ SharedConstants.MODELLO_F + ", ";
									motivazioneEsiste = true;
								} else {
									modelliCheck = modelliCheck + SharedConstants.MODELLO_F + ", ";
									obblCheck = true;
								}
							}
						}
					}
				} // chiudi for modelli
			} // chiudi else
		}
		if (motivazioneEsiste && (elencoerrori.getErrors().size() > 0 || obblCheck)) {
			elencoerrori.getWarnings()
					.add(listeService.getMessaggio(SharedConstants.CHECK_MOTIVAZIONE_ESISTE_CON_ERRORI)
							.getTestoMessaggio().replace("MODELLI", modellimotivazioneEsisteCheck));
		} else if (motivazioneEsiste && elencoerrori.getErrors().size() == 0) {
			elencoerrori.getWarnings().add(listeService.getMessaggio(SharedConstants.CHECK_OK_MOTIVAZIONE)
					.getTestoMessaggio().replace("MODELLI", modellimotivazioneEsisteCheck));
		}

		if (obblCheck) {
			elencoerrori.setWarningCheck(listeService.getMessaggio(SharedConstants.CHECK_BLOCCANTE).getTestoMessaggio()
					.replace("MODELLI", modelliCheck));
		}

		if (elencoerrori.getErrors().size() > 0) {
			// ci sono errori bloccanti devo mettere anche l'eerore finale generico
			elencoerrori.setDescrizione(
					listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIA_KO).getTestoMessaggio());
		}

		if (elencoerrori.getErrors().size() == 0 && elencoerrori.getWarnings().size() > 0) {
			// ci sono errori bloccanti devo mettere anche l'errore finale generico
			elencoerrori.setDescrizione(listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_TRANCHE)
					.getTestoMessaggio().replace("TRANCHE",
							tranche.equalsIgnoreCase(SharedConstants.TRANCHEI) ? SharedConstants.PRIMA_TRANCHE
									: SharedConstants.SECONDA_TRANCHE));
		}
		if (elencoerrori.getErrors().size() == 0 && elencoerrori.getWarnings().size() == 0) {
			messaggio = listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_OK).getTestoMessaggio()
					.replace("TRANCHE",
							tranche.equalsIgnoreCase(SharedConstants.TRANCHEI) ? SharedConstants.PRIMA_TRANCHE
									: SharedConstants.SECONDA_TRANCHE)
					+ ";";
			messaggio = messaggio + listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIO_TRANCHE)
					.getTestoMessaggio().replace("TRANCHE",
							tranche.equalsIgnoreCase(SharedConstants.TRANCHEI) ? SharedConstants.PRIMA_TRANCHE
									: SharedConstants.SECONDA_TRANCHE);
			elencoerrori.setDescrizione(messaggio);
		}
		if (elencoerrori.getWarningCheck() != null) {
			// ci sono errori bloccanti devo mettere anche l'eerore finale generico
			elencoerrori.setDescrizione(
					listeService.getMessaggio(SharedConstants.MESSAGGIO_GENERICO_INVIA_KO).getTestoMessaggio());
		}

		elencoerrori.setId("OK");
		return elencoerrori;
	}

	@Transactional
	public SaveModelloOutput saveMotivazioneCheck(SaveMotivazioneCheck body, UserInfo userInfo) throws Exception {
		SaveModelloOutput out = new SaveModelloOutput();
		out.setWarnings(new ArrayList<String>());
		out.setErrors(new ArrayList<String>());
		GregTRendicontazioneEnte rendicontazione = getRendicontazione(body.getIdRendicontazione());

		Date dataAttuale = new Date();

		GregTCronologia cronologia = new GregTCronologia();
		// SALVO NOTE DI CRONOLOGIA
		if (Checker.isValorizzato(body.getNota())) {
			cronologia.setGregTRendicontazioneEnte(rendicontazione);
			cronologia.setGregDStatoRendicontazione(rendicontazione.getGregDStatoRendicontazione());
			cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
			cronologia.setModello(body.getModello());
			cronologia.setUtenteOperazione(userInfo.getCodFisc());
			cronologia.setNotaPerEnte(body.getNota());
			cronologia.setDataOra(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataCreazione(new Timestamp(System.currentTimeMillis()));
			cronologia.setDataModifica(new Timestamp(System.currentTimeMillis()));
			cronologia = datiRendicontazioneDao.insertCronologiaCheck(cronologia);
		}

		GregDTab modello = datiRendicontazioneDao.getModelloByCod(body.getCodModello());

		GregRCheck newCheck = new GregRCheck();
		newCheck.setDataCreazione(new Timestamp(dataAttuale.getTime()));
		newCheck.setDataModifica(new Timestamp(dataAttuale.getTime()));
		newCheck.setGregDTab(modello);
		newCheck.setGregTCronologia(cronologia);
		newCheck.setUtenteOperazione(userInfo.getCodFisc());

		newCheck = datiRendicontazioneDao.insertCheck(newCheck);

		out.setMessaggio(listeService.getMessaggio(SharedConstants.INSERIMENTO_MOTIVAZIONE).getTestoMessaggio()
				.replace("MODELLO", modello.getDesEstesaTab()));
		out.setEsito("OK");
		return out;
	}

	public List<GregRCheck> getMotivazioniCheck(String codModello, Integer idRendicontazione) {
		List<GregRCheck> check = datiRendicontazioneDao.findAllMotivazioneByTab(codModello, idRendicontazione);
		return check;
	}

	public ModelTabTranche findModellibyCod(Integer idRendicontazione, String codTab) {

		Object[] resultList;
		resultList = datiRendicontazioneDao.findModelliAssociatiByCod(idRendicontazione, codTab);

		ModelTabTranche modello = new ModelTabTranche(resultList, true);

		return modello;
	}

}
