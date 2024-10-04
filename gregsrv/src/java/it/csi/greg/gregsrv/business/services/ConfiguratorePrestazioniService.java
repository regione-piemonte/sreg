/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.dao.impl.ConfiguratorePrestazioniDao;
import it.csi.greg.gregsrv.business.dao.impl.EntiGestoriAttiviDao;
import it.csi.greg.gregsrv.business.entity.GregDColori;
import it.csi.greg.gregsrv.business.entity.GregDProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregDTargetUtenza;
import it.csi.greg.gregsrv.business.entity.GregDTipoStruttura;
import it.csi.greg.gregsrv.business.entity.GregDTipologia;
import it.csi.greg.gregsrv.business.entity.GregDTipologiaQuota;
import it.csi.greg.gregsrv.business.entity.GregDTipologiaSpesa;
import it.csi.greg.gregsrv.business.entity.GregDVoceIstat;
import it.csi.greg.gregsrv.business.entity.GregRCatUteVocePrestReg2Istat;
import it.csi.greg.gregsrv.business.entity.GregRNomencNazPrestReg2;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1MacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1PrestMinist;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1PrestReg2;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg1UtenzeRegionali1ProgrammaMissione;
import it.csi.greg.gregsrv.business.entity.GregRPrestReg2UtenzeRegionali2;
import it.csi.greg.gregsrv.business.entity.GregRTipologiaSpesaPrestReg1;
import it.csi.greg.gregsrv.business.entity.GregTMacroaggregatiBilancio;
import it.csi.greg.gregsrv.business.entity.GregTNomenclatoreNazionale;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniMinisteriali;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali1;
import it.csi.greg.gregsrv.business.entity.GregTPrestazioniRegionali2;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelDettaglioPrestazConfiguratore;
import it.csi.greg.gregsrv.dto.ModelMacroaggregatiConf;
import it.csi.greg.gregsrv.dto.ModelNomenclatore;
import it.csi.greg.gregsrv.dto.ModelPrest1Prest2;
import it.csi.greg.gregsrv.dto.ModelPrest1PrestCollegate;
import it.csi.greg.gregsrv.dto.ModelPrest1PrestMin;
import it.csi.greg.gregsrv.dto.ModelPrest2PrestIstat;
import it.csi.greg.gregsrv.dto.ModelPrestUtenza;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.util.SharedConstants;

@Service("configuratorePrestazioniService")
public class ConfiguratorePrestazioniService {

	@Autowired
	protected EntiGestoriAttiviDao entiGestoriAttiviDao;
	@Autowired
	protected ConfiguratorePrestazioniDao configuratorePrestazioniDao;
	@Autowired
	protected DatiEnteService datiEnteService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected ModelloAService modelloAService;
	@Autowired
	protected ModelloA1Service modelloA1Service;
	@Autowired
	protected ModelloA2Service modelloA2Service;
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
	protected ModelloEService modelloEService;
	@Autowired
	protected ModelloFService modelloFService;
	@Autowired
	protected ModelloAllDService modelloAllDService;
	@Autowired
	protected ListeService listeService;

	public List<ModelDettaglioPrestazConfiguratore> findPrestazioniRegionali1() {
		List<ModelDettaglioPrestazConfiguratore> prest1 = configuratorePrestazioniDao.findPrestazioni();
		return prest1;
	}

	public ModelDettaglioPrestazConfiguratore getPrestazioneRegionale(Integer idPrestazione) throws Exception {
		ModelDettaglioPrestazConfiguratore dettPrest = new ModelDettaglioPrestazConfiguratore();
		GregTPrestazioniRegionali1 prestazione = configuratorePrestazioniDao.findPrestazioneById(idPrestazione);
		dettPrest.setIdPrestazione(prestazione.getIdPrestReg1());
		dettPrest.setCodPrestRegionale(prestazione.getCodPrestReg1());
		dettPrest.setDesPrestRegionale(prestazione.getDesPrestReg1());
		dettPrest.setCodTipologia(prestazione.getGregDTipologia().getCodTipologia());
		dettPrest.setDataCreazione(prestazione.getDataCreazione());
		dettPrest.setTipoStruttura(
				prestazione.getGregDTipoStruttura() != null ? prestazione.getGregDTipoStruttura().getCodTipoStruttura()
						: null);
		dettPrest.setTipoQuota(prestazione.getGregDTipologiaQuota() != null
				? prestazione.getGregDTipologiaQuota().getCodTipologiaQuota()
				: null);
		dettPrest.setIdTipologia(prestazione.getGregDTipologia().getIdTipologia());
		dettPrest.setIdTipoStruttura(
				prestazione.getGregDTipoStruttura() != null ? prestazione.getGregDTipoStruttura().getIdTipoStruttura()
						: null);
		dettPrest.setIdTipoQuota(prestazione.getGregDTipologiaQuota() != null
				? prestazione.getGregDTipologiaQuota().getIdTipologiaQuota()
				: null);
		dettPrest.setOrdinamento(prestazione.getOrdinamento());
		dettPrest.setDal(prestazione.getDataInizioValidita());
		dettPrest.setAl(prestazione.getDataFineValidita());
		dettPrest.setModificabile(configuratorePrestazioniDao.getListaPrestazioniValorizzateModA(
				prestazione.getCodPrestReg1(), prestazione.getDataInizioValidita(), prestazione.getDataFineValidita())
				|| configuratorePrestazioniDao.getListaPrestazioniValorizzateModB1(prestazione.getCodPrestReg1(),
						prestazione.getDataInizioValidita(), prestazione.getDataFineValidita())
				|| configuratorePrestazioniDao.getListaPrestazioniValorizzateModC(prestazione.getCodPrestReg1(),
						prestazione.getDataInizioValidita(), prestazione.getDataFineValidita()));
		Timestamp data = configuratorePrestazioniDao.getAnnoMaxPrestazioniValorizzate(prestazione.getCodPrestReg1(),
				prestazione.getDataInizioValidita(), prestazione.getDataFineValidita());
		dettPrest.setDataMin(data.compareTo(dettPrest.getDal()) > 0 ? data
				: dettPrest.getDal());
		List<ModelMacroaggregatiConf> macroaggregati = configuratorePrestazioniDao
				.findMacroaggregatiByPrestRegionale(idPrestazione);
		dettPrest.setMacroaggregati(macroaggregati);
		List<ModelPrestUtenza> utenza = configuratorePrestazioniDao.findUtenzeByPrestRegionale(idPrestazione);
		dettPrest.setTargetUtenzaPrestReg1(utenza);
		dettPrest.setNotaPrestazione(prestazione.getInformativa());
		List<ModelPrest1Prest2> prest1Prest2 = configuratorePrestazioniDao.findPrest2ByPrest1(idPrestazione);
		dettPrest.setPrest1Prest2(prest1Prest2);
		dettPrest.setPrest1PrestMin(configuratorePrestazioniDao.findPrestMinByPrest1(idPrestazione));
		List<ModelPrest1PrestCollegate> prestCollegate = new ArrayList<ModelPrest1PrestCollegate>();
		if (prestazione.getGregTPrestazioniRegionali1() != null) {
			GregTPrestazioniRegionali1 prestazionePadre = configuratorePrestazioniDao
					.findPrestazioneById(prestazione.getGregTPrestazioniRegionali1().getIdPrestReg1());
			ModelPrest1PrestCollegate prestazioneColl = new ModelPrest1PrestCollegate();
			prestazioneColl.setIdPrestRegionale(prestazionePadre.getIdPrestReg1());
			prestazioneColl.setCodPrestRegionale(prestazionePadre.getCodPrestReg1());
			prestazioneColl.setDesPrestRegionale(prestazionePadre.getDesPrestReg1());
			prestazioneColl.setDesTipologia(prestazionePadre.getGregDTipologia().getDesTipologia());
			prestazioneColl.setTipo(prestazionePadre.getGregTPrestazioniRegionali1() != null
					? prestazionePadre.getGregDTipologiaQuota() != null
							? prestazionePadre.getGregDTipologiaQuota().getDescTipologiaQuota()
							: " "
					: prestazionePadre.getGregDTipoStruttura() != null
							? prestazionePadre.getGregDTipoStruttura().getDescTipoStruttura()
							: " ");
			prestazioneColl.setAl(prestazionePadre.getDataFineValidita());
			prestCollegate.add(prestazioneColl);
		} else {
			List<GregTPrestazioniRegionali1> prestazioniFiglie = configuratorePrestazioniDao
					.findPrestazioneFiglieById(prestazione.getIdPrestReg1());
			for (GregTPrestazioniRegionali1 prestFiglia : prestazioniFiglie) {
				ModelPrest1PrestCollegate prestazioneColl = new ModelPrest1PrestCollegate();
				prestazioneColl.setIdPrestRegionale(prestFiglia.getIdPrestReg1());
				prestazioneColl.setCodPrestRegionale(prestFiglia.getCodPrestReg1());
				prestazioneColl.setDesPrestRegionale(prestFiglia.getDesPrestReg1());
				prestazioneColl.setDesTipologia(prestFiglia.getGregDTipologia().getDesTipologia());
				prestazioneColl.setTipo(prestFiglia.getGregTPrestazioniRegionali1() != null
						? prestFiglia.getGregDTipologiaQuota() != null
								? prestFiglia.getGregDTipologiaQuota().getDescTipologiaQuota()
								: " "
						: prestFiglia.getGregDTipoStruttura() != null
								? prestFiglia.getGregDTipoStruttura().getDescTipoStruttura()
								: " ");
				prestazioneColl.setAl(prestFiglia.getDataFineValidita());
				prestCollegate.add(prestazioneColl);
			}
		}
		dettPrest.setPrestazioniCollegate(prestCollegate);

		return dettPrest;
	}

	public List<ModelListeConfiguratore> findTipologie() {
		List<ModelListeConfiguratore> tipologie = configuratorePrestazioniDao.findTipologie();
		return tipologie;
	}

	public List<ModelListeConfiguratore> findStrutture() {
		List<ModelListeConfiguratore> strutture = configuratorePrestazioniDao.findStrutture();
		return strutture;
	}

	public List<ModelListeConfiguratore> findQuote() {
		List<ModelListeConfiguratore> quote = configuratorePrestazioniDao.findQuote();
		return quote;
	}

	public List<ModelListeConfiguratore> findPrestazioniColl() {
		List<ModelListeConfiguratore> prest = configuratorePrestazioniDao.findPrestazioniColl();
		return prest;
	}

	public List<ModelListeConfiguratore> findMacroaggregati() {
		List<ModelListeConfiguratore> macro = configuratorePrestazioniDao.findMacroaggregati();
		return macro;
	}

	public List<ModelListeConfiguratore> findUtenza() {
		List<ModelListeConfiguratore> macro = configuratorePrestazioniDao.findUtenza();
		return macro;
	}

	public List<ModelListeConfiguratore> findMissioneProg() {
		List<ModelListeConfiguratore> macro = configuratorePrestazioniDao.findMissioniProg();
		return macro;
	}

	public List<ModelListeConfiguratore> findSpesa() {
		List<ModelListeConfiguratore> macro = configuratorePrestazioniDao.findSpese();
		return macro;
	}

	public List<ModelPrest1Prest2> findPrestazioniRegionali2() {
		List<ModelPrest1Prest2> prest1 = configuratorePrestazioniDao.findPrestazioni2();
		return prest1;
	}
	
	public ModelPrest1Prest2 findPrestazioneRegionale2(Integer idPrest2) {
		ModelPrest1Prest2 prest2 = configuratorePrestazioniDao.findPrestazione2(idPrest2);
		return prest2;
	}

	public List<ModelPrest1PrestMin> findPrestazioniMinisteriali() {
		List<ModelPrest1PrestMin> prest1 = configuratorePrestazioniDao.findPrestazioniMin();
		return prest1;
	}

	public List<ModelListeConfiguratore> findPrestIstat() {
		List<ModelListeConfiguratore> istat = configuratorePrestazioniDao.findPrestIstat();
		return istat;
	}

	public List<ModelListeConfiguratore> findUtenzaIstat(List<ModelPrestUtenza> utenze ) {
		List<ModelListeConfiguratore> macro = configuratorePrestazioniDao.findUtenzaIstat(utenze);
		return macro;
	}
	
	public ModelListeConfiguratore findUtenzaIstatForTrans(String codUtenza ) {
		ModelListeConfiguratore macro = configuratorePrestazioniDao.findUtenzaIstatPerTranscodifica(codUtenza);
		return macro;
	}

	@Transactional
	public GenericResponseWarnErr savePrestazione(ModelDettaglioPrestazConfiguratore prestazione, UserInfo userInfo)
			throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		List<ModelDettaglioPrestazConfiguratore> prestazioni = findPrestazioniRegionali1();
		boolean trovatoCod = false;
		boolean trovatoOrd = false;
		boolean trovato = false;
		
		for (ModelDettaglioPrestazConfiguratore p : prestazioni) {
			if (p.getCodPrestRegionale().equals(prestazione.getCodPrestRegionale())) {
				if (p.getAl() == null) {
					trovatoCod = true;
				}else if(p.getAl().compareTo(prestazione.getDal())>=0) {
					trovatoCod = true;
				} else {
					if (p.getOrdinamento().equals(prestazione.getOrdinamento())) {
						trovato = true;
					}
				}
			}
			if (p.getOrdinamento().equals(prestazione.getOrdinamento()) && !trovato) {
				trovatoOrd = true;
			}
		}
		if (trovatoCod) {
			response.setId("KO");
			String messaggio = listeService.getMsgInformativiByCodice(SharedConstants.MSG01CONFIGURATORE)
					.getTestoMsgInformativo();
			response.setDescrizione(messaggio);
			return response;
		}
		if (trovatoOrd) {
			response.setId("KO");
			String messaggio = listeService.getMsgInformativiByCodice(SharedConstants.MSG02CONFIGURATORE)
					.getTestoMsgInformativo();
			response.setDescrizione(messaggio);
			return response;
		}

		GregDTipologia tipologia = configuratorePrestazioniDao.findTipologiaByCod(prestazione.getCodTipologia());
		GregDTipologiaQuota quota = null;
		GregDTipoStruttura struttura = null;
		GregTPrestazioniRegionali1 prest = null;
		if (prestazione.getTipoQuota() != null) {
			quota = configuratorePrestazioniDao.findTipologiaQuotaByCod(prestazione.getTipoQuota());
		}
		if (prestazione.getTipoStruttura() != null) {
			struttura = configuratorePrestazioniDao.findTipoStrutturaByCod(prestazione.getTipoStruttura());
		}
		if (prestazione.getTipoQuota() != null) {
			prest = configuratorePrestazioniDao.findPrestazioneById(prestazione.getPrestazioniCollegate().get(0).getIdPrestRegionale());
//			prest = configuratorePrestazioniDao.findPrestazioneById(prestazione.getIdPrestazione());
		}
		GregTPrestazioniRegionali1 p = new GregTPrestazioniRegionali1();
		p.setCodPrestReg1(prestazione.getCodPrestRegionale());
		p.setDesPrestReg1(prestazione.getDesPrestRegionale());
		p.setInformativa(prestazione.getNotaPrestazione());
		p.setOrdinamento(prestazione.getOrdinamento());
		p.setGregDTipologia(tipologia);
		p.setGregDTipologiaQuota(quota);
		p.setGregDTipoStruttura(struttura);
		p.setGregTPrestazioniRegionali1(prest);
		p.setDataCreazione(dataAttuale);
		p.setDataModifica(dataAttuale);
		p.setDataInizioValidita(prestazione.getDal());
		p.setDataFineValidita(prestazione.getAl());
		p.setUtenteOperazione(userInfo.getCodFisc());
		p = configuratorePrestazioniDao.savePrestazione1(p);
		for (ModelMacroaggregatiConf macro : prestazione.getMacroaggregati()) {
			GregTMacroaggregatiBilancio m = configuratorePrestazioniDao
					.findMacroBilancioByCod(macro.getIdMacroaggregati());
			GregRPrestReg1MacroaggregatiBilancio macroPrest = new GregRPrestReg1MacroaggregatiBilancio();
			macroPrest.setGregTPrestazioniRegionali1(p);
			macroPrest.setGregTMacroaggregatiBilancio(m);
			macroPrest.setDataCreazione(dataAttuale);
			macroPrest.setDataModifica(dataAttuale);
			macroPrest.setDataInizioValidita(macro.getDal());
			macroPrest.setDataFineValidita(macro.getAl());
			macroPrest.setUtenteOperazione(userInfo.getCodFisc());
			macroPrest = configuratorePrestazioniDao.saveRPrestMacro(macroPrest);
		}
		for (ModelPrestUtenza utenza : prestazione.getTargetUtenzaPrestReg1()) {
			GregDTargetUtenza u = configuratorePrestazioniDao.findUtenzaById(utenza.getIdUtenza());
			GregDColori colore = configuratorePrestazioniDao.findColoreByRGB(utenza.getColoreMissioneProgramma());
			GregRPrestReg1UtenzeRegionali1 utePrest = new GregRPrestReg1UtenzeRegionali1();
			utePrest.setGregTPrestazioniRegionali1(p);
			utePrest.setGregDTargetUtenza(u);
			utePrest.setDataCreazione(dataAttuale);
			utePrest.setDataModifica(dataAttuale);
			utePrest.setDataInizioValidita(utenza.getDal());
			utePrest.setDataFineValidita(utenza.getAl());
			utePrest.setGregDColori(colore);
			utePrest.setUtenteOperazione(userInfo.getCodFisc());
			utePrest = configuratorePrestazioniDao.saveRPrestUtenza(utePrest);
			if (!utenza.getCodMissioneProgramma().equals("Vuoto")) {
				GregDProgrammaMissione progMiss = configuratorePrestazioniDao
						.findProgMissioneByCod(utenza.getCodMissioneProgramma());
				GregRPrestReg1UtenzeRegionali1ProgrammaMissione utePrestMiss = new GregRPrestReg1UtenzeRegionali1ProgrammaMissione();
				utePrestMiss.setGregRPrestReg1UtenzeRegionali1(utePrest);
				utePrestMiss.setGregDProgrammaMissione(progMiss);
				utePrestMiss.setDataCreazione(dataAttuale);
				utePrestMiss.setDataModifica(dataAttuale);
				utePrestMiss.setDataInizioValidita(utenza.getDal());
				utePrestMiss.setDataFineValidita(utenza.getAl());
				utePrestMiss.setUtenteOperazione(userInfo.getCodFisc());
				utePrestMiss = configuratorePrestazioniDao.saveRPrestUtenzaMissione(utePrestMiss);
			}
			if (!utenza.getCodTipologiaSpesa().equals("Vuoto")) {
				GregDTipologiaSpesa spesa = configuratorePrestazioniDao.findSpesaByCod(utenza.getCodTipologiaSpesa());
				GregRTipologiaSpesaPrestReg1 spesaPrest = new GregRTipologiaSpesaPrestReg1();
				spesaPrest.setGregRPrestReg1UtenzeRegionali1(utePrest);
				spesaPrest.setGregDTipologiaSpesa(spesa);
				spesaPrest.setDataCreazione(dataAttuale);
				spesaPrest.setDataModifica(dataAttuale);
				spesaPrest.setDataInizioValidita(utenza.getDal());
				spesaPrest.setDataFineValidita(utenza.getAl());
				spesaPrest.setUtenteOperazione(userInfo.getCodFisc());
				spesaPrest = configuratorePrestazioniDao.saveRPrestUtenzaSpesa(spesaPrest);
			}
		}
		for (ModelPrest1Prest2 prest2 : prestazione.getPrest1Prest2()) {
			GregTPrestazioniRegionali2 p2 = configuratorePrestazioniDao.findPrestazione2ById(prest2.getIdPrest2());
			GregRPrestReg1PrestReg2 prest1prest2 = new GregRPrestReg1PrestReg2();
			prest1prest2.setGregTPrestazioniRegionali1(p);
			prest1prest2.setGregTPrestazioniRegionali2(p2);
			prest1prest2.setDataCreazione(dataAttuale);
			prest1prest2.setDataModifica(dataAttuale);
			prest1prest2.setDataInizioValidita(prest2.getDal());
			prest1prest2.setDataFineValidita(prest2.getAl());
			prest1prest2.setUtenteOperazione(userInfo.getCodFisc());
			prest1prest2 = configuratorePrestazioniDao.saveRPrest1Prest2(prest1prest2);
		}
		for (ModelPrest1PrestMin prestMin : prestazione.getPrest1PrestMin()) {
			GregTPrestazioniMinisteriali prestM = configuratorePrestazioniDao
					.findPrestazioneMinById(prestMin.getIdPrestMin());
			GregRPrestReg1PrestMinist prest1prestMin = new GregRPrestReg1PrestMinist();
			prest1prestMin.setGregTPrestazioniRegionali1(p);
			prest1prestMin.setGregTPrestazioniMinisteriali(prestM);
			prest1prestMin.setDataCreazione(dataAttuale);
			prest1prestMin.setDataModifica(dataAttuale);
			prest1prestMin.setDataInizioValidita(prestMin.getDal());
			prest1prestMin.setDataFineValidita(prestMin.getAl());
			prest1prestMin.setUtenteOperazione(userInfo.getCodFisc());
			prest1prestMin = configuratorePrestazioniDao.saveRPrest1PrestMin(prest1prestMin);
		}
		response.setIdPrestazione(p.getIdPrestReg1());
		response.setId("OK");
		response.setDescrizione("Prestazione salvata correttamente");
		return response;
	}

	@Transactional
	public GenericResponseWarnErr savePrestazione2(ModelPrest1Prest2 prestazione, UserInfo userInfo) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		List<ModelPrest1Prest2> prestazioni = findPrestazioniRegionali2();
		boolean trovatoCod = false;
		boolean trovatoOrd = false;
		boolean trovato = false;
		
		for (ModelPrest1Prest2 p : prestazioni) {
			if (p.getCodPrest2().equals(prestazione.getCodPrest2())) {
				if (p.getAl() == null) {
					trovatoCod = true;
				}else if(p.getAl().compareTo(prestazione.getDal())>=0) {
					trovatoCod = true;
				}else {
					if (p.getOrdinamento().equals(prestazione.getOrdinamento())) {
						trovato = true;
					}
				}
			}
			if (p.getOrdinamento().equals(prestazione.getOrdinamento()) && !trovato) {
				trovatoOrd = true;
			}
		}
		if (trovatoCod) {
			response.setId("KO");
			String messaggio = listeService.getMsgInformativiByCodice(SharedConstants.MSG01CONFIGURATORE)
					.getTestoMsgInformativo();
			response.setDescrizione(messaggio);
			return response;
		}
		if (trovatoOrd) {
			response.setId("KO");
			String messaggio = listeService.getMsgInformativiByCodice(SharedConstants.MSG02CONFIGURATORE)
					.getTestoMsgInformativo();
			response.setDescrizione(messaggio);
			return response;
		}

		GregDTipologia tipologia = configuratorePrestazioniDao.findTipologiaByCod(prestazione.getCodTipologia());

		GregTPrestazioniRegionali2 p = new GregTPrestazioniRegionali2();
		p.setCodPrestReg2(prestazione.getCodPrest2());
		p.setDesPrestReg2(prestazione.getDescPrest2());
		p.setInformativa(prestazione.getNota());
		p.setOrdinamento(prestazione.getOrdinamento());
		p.setGregDTipologia(tipologia);
		p.setDataCreazione(dataAttuale);
		p.setDataModifica(dataAttuale);
		p.setDataInizioValidita(prestazione.getDal());
		p.setDataFineValidita(prestazione.getAl());
		p.setUtenteOperazione(userInfo.getCodFisc());
		p = configuratorePrestazioniDao.savePrestazione2(p);
		//Salvataggio Relazione
		GregRPrestReg1PrestReg2 prest1prest2 = new GregRPrestReg1PrestReg2();
		GregTPrestazioniRegionali1 p1 = configuratorePrestazioniDao
				.findPrestazioneByIdAndDataInizio(prestazione.getIdPrest1());
		prest1prest2.setGregTPrestazioniRegionali1(p1);
		prest1prest2.setGregTPrestazioniRegionali2(p);
		prest1prest2.setDataCreazione(dataAttuale);
		prest1prest2.setDataModifica(dataAttuale);
		prest1prest2.setDataInizioValidita(p.getDataInizioValidita());
		prest1prest2.setDataFineValidita(null);
		prest1prest2.setUtenteOperazione(userInfo.getCodFisc());
		prest1prest2 = configuratorePrestazioniDao.saveRPrest1Prest2(prest1prest2);
		
		for (ModelPrestUtenza ute : prestazione.getUtenzeConf()) {
			GregDTargetUtenza m = configuratorePrestazioniDao.findUtenzaById(ute.getIdUtenza());
			GregRPrestReg2UtenzeRegionali2 utePrest = new GregRPrestReg2UtenzeRegionali2();
			utePrest.setGregTPrestazioniRegionali2(p);
			utePrest.setGregDTargetUtenza(m);
			utePrest.setDataCreazione(dataAttuale);
			utePrest.setDataModifica(dataAttuale);
			utePrest.setDataInizioValidita(ute.getDal());
			utePrest.setDataFineValidita(ute.getAl());
			utePrest.setUtenteOperazione(userInfo.getCodFisc());
			utePrest = configuratorePrestazioniDao.saveRPrest2Utenza(utePrest);
		}
		for (ModelPrest2PrestIstat prest : prestazione.getPrestIstat()) {
			GregDVoceIstat pIstat = configuratorePrestazioniDao.findPrestVoceIstat(prest.getIdPrestIstat());
			for (ModelPrestUtenza ute : prest.getUtenzeMinConf()) {
				GregDTargetUtenza m = configuratorePrestazioniDao.findUtenzaById(ute.getIdUtenza());
				GregRCatUteVocePrestReg2Istat utePrest = new GregRCatUteVocePrestReg2Istat();
				utePrest.setGregTPrestazioniRegionali2(p);
				utePrest.setGregDVoceIstat(pIstat);
				utePrest.setGregDTargetUtenza(m);
				utePrest.setDataCreazione(dataAttuale);
				utePrest.setDataModifica(dataAttuale);
				utePrest.setDataInizioValidita(ute.getDal());
				utePrest.setDataFineValidita(ute.getAl());
				utePrest.setUtenteOperazione(userInfo.getCodFisc());
				utePrest = configuratorePrestazioniDao.saveRPrest2PrestIstat(utePrest);
			}
		}
		for (ModelNomenclatore nome : prestazione.getNomenclatore()) {
			GregTNomenclatoreNazionale nomenclatore = configuratorePrestazioniDao
					.findNomenclatore(nome.getIdNomenclatore());
			GregRNomencNazPrestReg2 utePrest = new GregRNomencNazPrestReg2();
			utePrest.setGregTPrestazioniRegionali2(p);
			utePrest.setGregTNomenclatoreNazionale(nomenclatore);
			utePrest.setDataCreazione(dataAttuale);
			utePrest.setDataModifica(dataAttuale);
			utePrest.setDataInizioValidita(nome.getDal());
			utePrest.setDataFineValidita(nome.getAl());
			utePrest.setUtenteOperazione(userInfo.getCodFisc());
			utePrest = configuratorePrestazioniDao.saveRPrest2Nomencl(utePrest);
		}

		response.setId("OK");
		response.setDescrizione("Prestazione salvata correttamente");
		return response;
	}

	public List<ModelNomenclatore> findNomenclatore() {
		List<ModelNomenclatore> macro = configuratorePrestazioniDao.findNomenclatore();
		return macro;
	}
	
	@Transactional
	public GenericResponseWarnErr modificaPrestazione2(ModelPrest1Prest2 prestazione, UserInfo userInfo)
			throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		List<ModelPrest1Prest2> prestazioni = findPrestazioniRegionali2();
		boolean trovatoOrd = false;

		for (ModelPrest1Prest2 p : prestazioni) {
			if (p.getOrdinamento().equals(prestazione.getOrdinamento())
					&& !p.getCodPrest2().equals(prestazione.getCodPrest2())) {
				trovatoOrd = true;
			}
		}

		if (trovatoOrd) {
			response.setId("KO");
			String messaggio = listeService.getMsgInformativiByCodice(SharedConstants.MSG02CONFIGURATORE)
					.getTestoMsgInformativo();
			response.setDescrizione(messaggio);
			return response;
		}

		GregDTipologia tipologia = configuratorePrestazioniDao.findTipologiaByCod(prestazione.getCodTipologia());

		GregTPrestazioniRegionali2 p = configuratorePrestazioniDao
				.findPrestazione2ByIdDataInizio(prestazione.getIdPrest2(), prestazione.getDal());
		p.setDesPrestReg2(prestazione.getDescPrest2());
		p.setInformativa(prestazione.getNota());
		p.setOrdinamento(prestazione.getOrdinamento());
		p.setGregDTipologia(tipologia);
		p.setDataModifica(dataAttuale);
		p.setDataInizioValidita(prestazione.getDal());
		p.setDataFineValidita(prestazione.getAl());
		p.setUtenteOperazione(userInfo.getCodFisc());
		p = configuratorePrestazioniDao.savePrestazione2(p);
		
		if(prestazione.getIdPrest1()!=null) {
		//Salvataggio Relazione
		GregRPrestReg1PrestReg2 prest1prest2 = new GregRPrestReg1PrestReg2();
		GregTPrestazioniRegionali1 p1 = configuratorePrestazioniDao
				.findPrestazioneByIdAndDataInizio(prestazione.getIdPrest1());
		prest1prest2.setGregTPrestazioniRegionali1(p1);
		prest1prest2.setGregTPrestazioniRegionali2(p);
		prest1prest2.setDataCreazione(dataAttuale);
		prest1prest2.setDataModifica(dataAttuale);
		prest1prest2.setDataInizioValidita(prestazione.getDalRelazione());
		prest1prest2.setDataFineValidita(null);
		prest1prest2.setUtenteOperazione(userInfo.getCodFisc());
		prest1prest2 = configuratorePrestazioniDao.saveRPrest1Prest2(prest1prest2);
		response.setDataCreazione(prest1prest2.getDataCreazione());
		}
		
		for (ModelPrestUtenza ute : prestazione.getUtenzeConf()) {
			GregRPrestReg2UtenzeRegionali2 utePrest = configuratorePrestazioniDao
					.findRUtenzaRegByPrest2(prestazione.getIdPrest2(), ute.getIdUtenza());
			if (utePrest == null) {
				utePrest = new GregRPrestReg2UtenzeRegionali2();
				GregDTargetUtenza m = configuratorePrestazioniDao.findUtenzaById(ute.getIdUtenza());
				utePrest.setGregDTargetUtenza(m);
				utePrest.setGregTPrestazioniRegionali2(p);
				utePrest.setDataCreazione(dataAttuale);
			}
			utePrest.setDataModifica(dataAttuale);
			utePrest.setDataInizioValidita(ute.getDal());
			utePrest.setDataFineValidita(ute.getAl());
			if(ute.getDataCancellazione()!=null) {
				utePrest.setDataCancellazione(ute.getDataCancellazione());
			}
			utePrest.setUtenteOperazione(userInfo.getCodFisc());
			utePrest = configuratorePrestazioniDao.saveRPrest2Utenza(utePrest);
		}
		for (ModelPrest2PrestIstat prest : prestazione.getPrestIstat()) {
			for (ModelPrestUtenza ute : prest.getUtenzeMinConf()) {
				GregRCatUteVocePrestReg2Istat utePrest = configuratorePrestazioniDao
						.findRUtenzeMinisByPrest2IstatDataInizio(prestazione.getIdPrest2(), prest.getIdPrestIstat(),
								ute.getIdUtenza(), ute.getDal(), ute.getDataCreazione());
				if (utePrest == null) {
					utePrest = new GregRCatUteVocePrestReg2Istat();
					GregDTargetUtenza m = configuratorePrestazioniDao.findUtenzaById(ute.getIdUtenza());
					GregDVoceIstat pIstat = configuratorePrestazioniDao.findPrestVoceIstat(prest.getIdPrestIstat());
					utePrest.setGregDVoceIstat(pIstat);
					utePrest.setGregDTargetUtenza(m);
					utePrest.setGregTPrestazioniRegionali2(p);
					utePrest.setDataCreazione(dataAttuale);
				}
				utePrest.setDataModifica(dataAttuale);
				utePrest.setDataInizioValidita(ute.getDal());
				utePrest.setDataFineValidita(ute.getAl());
				if(ute.getDataCancellazione()!=null) {
					utePrest.setDataCancellazione(ute.getDataCancellazione());
				}
				utePrest.setUtenteOperazione(userInfo.getCodFisc());
				utePrest = configuratorePrestazioniDao.saveRPrest2PrestIstat(utePrest);
			}
		}
		for (ModelNomenclatore nome : prestazione.getNomenclatore()) {
			GregRNomencNazPrestReg2 utePrest = configuratorePrestazioniDao.findRNomenByPrest2IstatDataInizio(
					prestazione.getIdPrest2(), nome.getIdNomenclatore(), nome.getDal());
			if (utePrest == null) {
				utePrest = new GregRNomencNazPrestReg2();
				GregTNomenclatoreNazionale nomenclatore = configuratorePrestazioniDao
						.findNomenclatore(nome.getIdNomenclatore());
				utePrest.setGregTPrestazioniRegionali2(p);
				utePrest.setGregTNomenclatoreNazionale(nomenclatore);
				utePrest.setDataCreazione(dataAttuale);
			}
			utePrest.setDataModifica(dataAttuale);
			utePrest.setDataInizioValidita(nome.getDal());
			utePrest.setDataFineValidita(nome.getAl());
			if(nome.getDataCancellazione()!=null) {
				utePrest.setDataCancellazione(nome.getDataCancellazione());
			}
			utePrest.setUtenteOperazione(userInfo.getCodFisc());
			utePrest = configuratorePrestazioniDao.saveRPrest2Nomencl(utePrest);
		}
		
		response.setId("OK");
		response.setDescrizione("Prestazione modificata correttamente");
		return response;
	}

	@Transactional
	public GenericResponseWarnErr modificaPrestazione(ModelDettaglioPrestazConfiguratore prestazione, UserInfo userInfo)
			throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		Timestamp dataAttuale = new Timestamp(System.currentTimeMillis());
		List<ModelDettaglioPrestazConfiguratore> prestazioni = findPrestazioniRegionali1();
		boolean trovatoOrd = false;

		for (ModelDettaglioPrestazConfiguratore p : prestazioni) {
			if (p.getOrdinamento().equals(prestazione.getOrdinamento())
					&& !p.getCodPrestRegionale().equals(prestazione.getCodPrestRegionale())) {
				trovatoOrd = true;
			}
		}

		if (trovatoOrd) {
			response.setId("KO");
			String messaggio = listeService.getMsgInformativiByCodice(SharedConstants.MSG02CONFIGURATORE)
					.getTestoMsgInformativo();
			response.setDescrizione(messaggio);
			return response;
		}

		GregDTipologia tipologia = configuratorePrestazioniDao.findTipologiaByCod(prestazione.getCodTipologia());
		GregDTipologiaQuota quota = null;
		GregDTipoStruttura struttura = null;
		GregTPrestazioniRegionali1 prest = null;
		if (prestazione.getTipoQuota() != null) {
			quota = configuratorePrestazioniDao.findTipologiaQuotaByCod(prestazione.getTipoQuota());
		}
		if (prestazione.getTipoStruttura() != null) {
			struttura = configuratorePrestazioniDao.findTipoStrutturaByCod(prestazione.getTipoStruttura());
		}
		if (prestazione.getTipoQuota() != null) {
			if(prestazione.getPrestazioniCollegate().size()==1) {
				prest = configuratorePrestazioniDao.findPrestazioneById(prestazione.getPrestazioniCollegate().get(0).getIdPrestRegionale());
			}
		}
		GregTPrestazioniRegionali1 p = configuratorePrestazioniDao
				.findPrestazioneByIdAndDataInizio(prestazione.getIdPrestazione());
		p.setDesPrestReg1(prestazione.getDesPrestRegionale());
		p.setInformativa(prestazione.getNotaPrestazione());
		p.setOrdinamento(prestazione.getOrdinamento());
		p.setGregDTipologia(tipologia);
		p.setGregDTipologiaQuota(quota);
		p.setGregDTipoStruttura(struttura);
		p.setGregTPrestazioniRegionali1(prest);
		p.setDataModifica(dataAttuale);
		p.setDataInizioValidita(prestazione.getDal());
		p.setDataFineValidita(prestazione.getAl());
		p.setUtenteOperazione(userInfo.getCodFisc());
		p = configuratorePrestazioniDao.savePrestazione1(p);
		for (ModelMacroaggregatiConf macro : prestazione.getMacroaggregati()) {

			GregRPrestReg1MacroaggregatiBilancio macroPrest = null;
			if(macro.getDataCreazione()!=null) {
			macroPrest = configuratorePrestazioniDao
					.findRMacroaggregatiByPrestRegionaleAndDataInizio(prestazione.getIdPrestazione(),
							macro.getIdMacroaggregati(), macro.getDal(), macro.getDataCreazione());
			}
			if (macroPrest == null) {
				macroPrest = new GregRPrestReg1MacroaggregatiBilancio();
				GregTMacroaggregatiBilancio m = configuratorePrestazioniDao
						.findMacroBilancioByCod(macro.getIdMacroaggregati());
				macroPrest.setGregTPrestazioniRegionali1(p);
				macroPrest.setGregTMacroaggregatiBilancio(m);
				macroPrest.setDataCreazione(dataAttuale);
			}
			macroPrest.setDataModifica(dataAttuale);
			macroPrest.setDataInizioValidita(macro.getDal());
			macroPrest.setDataFineValidita(macro.getAl());
			if(macro.getDataCancellazione()!=null) {
				macroPrest.setDataCancellazione(macro.getDataCancellazione());
			}
			macroPrest.setUtenteOperazione(userInfo.getCodFisc());
			macroPrest = configuratorePrestazioniDao.saveRPrestMacro(macroPrest);
		}
		for (ModelPrestUtenza utenza : prestazione.getTargetUtenzaPrestReg1()) {
			GregDColori colore = configuratorePrestazioniDao.findColoreByRGB(utenza.getColoreMissioneProgramma());
			GregRPrestReg1UtenzeRegionali1 utePrest = null;
			if(utenza.getDataCreazione()!=null) {
			utePrest = configuratorePrestazioniDao.findRUtenzeByPrestRegionaleDataInizio(
					prestazione.getIdPrestazione(), utenza.getIdUtenza(), utenza.getDal(), utenza.getDataCreazione());
			}
			if (utePrest == null) {
				utePrest = new GregRPrestReg1UtenzeRegionali1();
				GregDTargetUtenza u = configuratorePrestazioniDao.findUtenzaById(utenza.getIdUtenza());
				utePrest.setGregTPrestazioniRegionali1(p);
				utePrest.setGregDTargetUtenza(u);
				utePrest.setDataCreazione(dataAttuale);

			}
			utePrest.setDataModifica(dataAttuale);
			utePrest.setGregDColori(colore);
			utePrest.setDataInizioValidita(utenza.getDal());
			utePrest.setDataFineValidita(utenza.getAl());
			if(utenza.getDataCancellazione()!=null) {
				utePrest.setDataCancellazione(utenza.getDataCancellazione());
			}
			utePrest.setUtenteOperazione(userInfo.getCodFisc());
			utePrest = configuratorePrestazioniDao.saveRPrestUtenza(utePrest);
			if (!utenza.getCodMissioneProgramma().equals("Vuoto")) {

				GregDProgrammaMissione progMiss = configuratorePrestazioniDao
						.findProgMissioneByCod(utenza.getCodMissioneProgramma());
				GregRPrestReg1UtenzeRegionali1ProgrammaMissione utePrestMiss = configuratorePrestazioniDao
						.findRProgrammaMissioneByPrestRegionaleUtenza(utenza.getIdMissioneProgrammaRelazione());
				if (utePrestMiss == null) {
					utePrestMiss = new GregRPrestReg1UtenzeRegionali1ProgrammaMissione();
					utePrestMiss.setDataCreazione(dataAttuale);
					utePrestMiss.setGregRPrestReg1UtenzeRegionali1(utePrest);
				}
				
				utePrestMiss.setGregDProgrammaMissione(progMiss);
				utePrestMiss.setDataModifica(dataAttuale);
				utePrestMiss.setDataInizioValidita(utenza.getDal());
				utePrestMiss.setDataFineValidita(utenza.getAl());
				if(utenza.getDataCancellazione()!=null) {
					utePrestMiss.setDataCancellazione(utenza.getDataCancellazione());
				}
				utePrestMiss.setUtenteOperazione(userInfo.getCodFisc());
				utePrestMiss = configuratorePrestazioniDao.saveRPrestUtenzaMissione(utePrestMiss);

			}
			if (!utenza.getCodTipologiaSpesa().equals("Vuoto")) {

				GregDTipologiaSpesa spesa = configuratorePrestazioniDao.findSpesaByCod(utenza.getCodTipologiaSpesa());
				GregRTipologiaSpesaPrestReg1 spesaPrest = configuratorePrestazioniDao
						.findTipologiaSpesaById(utenza.getIdTipologiaSpesaRelazione());
				if (spesaPrest == null) {
					spesaPrest = new GregRTipologiaSpesaPrestReg1();
					spesaPrest.setDataCreazione(dataAttuale);
					spesaPrest.setGregRPrestReg1UtenzeRegionali1(utePrest);
				}
				
				spesaPrest.setGregDTipologiaSpesa(spesa);
				spesaPrest.setDataModifica(dataAttuale);
				spesaPrest.setDataInizioValidita(utenza.getDal());
				spesaPrest.setDataFineValidita(utenza.getAl());
				if(utenza.getDataCancellazione()!=null) {
					spesaPrest.setDataCancellazione(utenza.getDataCancellazione());
				}
				spesaPrest.setUtenteOperazione(userInfo.getCodFisc());
				spesaPrest = configuratorePrestazioniDao.saveRPrestUtenzaSpesa(spesaPrest);

			}
		}
		for (ModelPrest1Prest2 prest2 : prestazione.getPrest1Prest2()) {
			GregRPrestReg1PrestReg2 prest1prest2 = null;
			if(prest2.getDataCreazione()!=null) {
				prest1prest2 = configuratorePrestazioniDao
						.findRPrestazione2ByPrestRegionaleDataInizio(prestazione.getIdPrestazione(), prest2.getIdPrest2(),
								prest2.getDalRelazione(), prest2.getDataCreazione());
			}
			if (prest1prest2 == null) {
				prest1prest2 = new GregRPrestReg1PrestReg2();
				GregTPrestazioniRegionali2 p2 = configuratorePrestazioniDao.findPrestazione2ById(prest2.getIdPrest2());
				prest1prest2.setGregTPrestazioniRegionali1(p);
				prest1prest2.setGregTPrestazioniRegionali2(p2);
				prest1prest2.setDataCreazione(dataAttuale);
			}

			prest1prest2.setDataModifica(dataAttuale);
			prest1prest2.setDataInizioValidita(prest2.getDalRelazione());
			prest1prest2.setDataFineValidita(prest2.getAlRelazione());
			if(prest2.getDataCancellazione()!=null) {
				prest1prest2.setDataCancellazione(prest2.getDataCancellazione());
			}
			prest1prest2.setUtenteOperazione(userInfo.getCodFisc());
			prest1prest2 = configuratorePrestazioniDao.saveRPrest1Prest2(prest1prest2);
		}
		for (ModelPrest1PrestMin prestMin : prestazione.getPrest1PrestMin()) {

			GregRPrestReg1PrestMinist prest1prestMin = null;
			if(prestMin.getDataCreazione()!=null) {
				prest1prestMin = configuratorePrestazioniDao
						.findRPrestazioneMinByPrestRegionaleDataInizio(prestazione.getIdPrestazione(),
								prestMin.getIdPrestMin(), prestMin.getDal(), prestMin.getDataCreazione());
			}
			if (prest1prestMin == null) {
				prest1prestMin = new GregRPrestReg1PrestMinist();
				GregTPrestazioniMinisteriali prestM = configuratorePrestazioniDao
						.findPrestazioneMinById(prestMin.getIdPrestMin());
				prest1prestMin.setGregTPrestazioniRegionali1(p);
				prest1prestMin.setGregTPrestazioniMinisteriali(prestM);
				prest1prestMin.setDataCreazione(dataAttuale);
			}
			prest1prestMin.setDataModifica(dataAttuale);
			prest1prestMin.setDataInizioValidita(prestMin.getDal());
			prest1prestMin.setDataFineValidita(prestMin.getAl());
			if(prestMin.getDataCancellazione()!=null) {
				prest1prestMin.setDataCancellazione(prestMin.getDataCancellazione());
			}
			prest1prestMin.setUtenteOperazione(userInfo.getCodFisc());
			prest1prestMin = configuratorePrestazioniDao.saveRPrest1PrestMin(prest1prestMin);
		}
		
		response.setId("OK");
		response.setDescrizione("Prestazione salvata correttamente");
		return response;
	}

}
