/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.greg.gregsrv.business.dao.impl.CruscottoDao;
import it.csi.greg.gregsrv.business.dao.impl.EntiGestoriAttiviDao;
import it.csi.greg.gregsrv.business.dao.impl.MacroaggregatiDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloA1Dao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloA2Dao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloADao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloAllDDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloAllontanamentoZeroDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloB1Dao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloBDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloCDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloDDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloEDao;
import it.csi.greg.gregsrv.business.dao.impl.ModelloFDao;
import it.csi.greg.gregsrv.business.entity.GregDStatoRendicontazione;
import it.csi.greg.gregsrv.business.entity.GregDTab;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.GenericResponseWarnCheckErr;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.InfoModello;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.ModelRicercaCruscotto;
import it.csi.greg.gregsrv.dto.ModelStatoCruscotto;
import it.csi.greg.gregsrv.dto.ModelStatoModelli;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTabTrancheCruscotto;
import it.csi.greg.gregsrv.dto.RicercaEntiGestori;
import it.csi.greg.gregsrv.util.SharedConstants;

@Service("cruscottoService")
public class CruscottoService {

	@Autowired
	protected EntiGestoriAttiviDao entiGestoriAttiviDao;
	@Autowired
	protected CruscottoDao cruscottoDao;
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
	protected ModelloADao modelloADao;
	@Autowired
	protected ModelloA1Dao modelloA1Dao;
	@Autowired
	protected ModelloA2Dao modelloA2Dao;
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
	protected ModelloAllDDao modelloAllDDao;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected ModelloAllontanamentoZeroDao modelloAlZeroServiceDao;

	@Transactional
	public List<ModelRicercaCruscotto> findSchedeEntiGestori(RicercaEntiGestori ricerca) {

		List<Object> resultList = new ArrayList<Object>();
		List<ModelRicercaCruscotto> lista = new ArrayList<ModelRicercaCruscotto>();

		resultList = cruscottoDao.findSchedeEntiGestoriByValue(ricerca);

		Iterator<Object> itr = resultList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			lista.add(new ModelRicercaCruscotto(obj));
		}

		for (ModelRicercaCruscotto ente : lista) {
			ente.setRendicontazioni(
					cruscottoDao.findRendicontazioniApertebyIdSchedaAndValue(ente.getIdSchedaEnteGestore(), ricerca));
			List<ModelStatoModelli> mod = new ArrayList<ModelStatoModelli>();
//				List<ModelTabTrancheCruscotto> modelli = findModelli();
			List<ModelStatoModelli> modelli = cruscottoDao
					.getStatoModello(ente.getRendicontazioni().getIdRendicontazioneEnte());
			for (ModelStatoModelli modello : modelli) {
				ModelStatoModelli m = new ModelStatoModelli();
				m.setCodModello(modello.getCodModello());
				if (!modello.isTrovato()) {
					m.setStatoModello(SharedConstants.NON_ASSEGNATO);
				} else {
					m.setStatoModello(getStatoModelloEnte(ente.getRendicontazioni().getIdRendicontazioneEnte(),
							modello.getCodModello(), modello.getCodTranche(),
							ente.getRendicontazioni().getStatoRendicontazione().getCodStatoRendicontazione()));
				}
				mod.add(m);
			}
			ente.setModelli(mod);
			ente.setStatoFnpsAttuale(
					modelloAllDService.getStatoFNPS(ente.getRendicontazioni().getIdRendicontazioneEnte()));
			GregTRendicontazioneEnte rendiPassata = datiRendicontazioneService
					.getRendicontazionePassata(ente.getIdSchedaEnteGestore(), (ricerca.getAnnoEsercizio() - 1));
			if (rendiPassata != null) {
				ente.setStatoFnpsPrecedente(modelloAllDService.getStatoFNPS(rendiPassata.getIdRendicontazioneEnte()));
				ente.setIdRendicontazionePassata(rendiPassata.getIdRendicontazioneEnte());
			} else {
				ente.setStatoFnpsPrecedente(SharedConstants.NON_COMPILATO);
			}
			
			ente.setStatoAlZero(modelloAlZeroServiceDao.getStatusModelloAlZero(ente.getRendicontazioni().getIdRendicontazioneEnte(), datiRendicontazioneService.getRendicontazione(ente.getRendicontazioni().getIdRendicontazioneEnte()).getAnnoGestione()));

		}
		return lista;
	}

	public List<ModelTabTrancheCruscotto> findModelli() {

		List<Object> resultList = new ArrayList<Object>();
		resultList = cruscottoDao.findModelli();

		List<ModelTabTrancheCruscotto> listaModelliAssociati = new ArrayList<ModelTabTrancheCruscotto>();
		Iterator<Object> itr = resultList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			listaModelliAssociati.add(new ModelTabTrancheCruscotto(obj));
		}

		for (ModelTabTrancheCruscotto modello : listaModelliAssociati) {
			switch (modello.getCodTab()) {
			case SharedConstants.MODELLO_A:
				modello.setAzione("ModelloA");
				break;
			case SharedConstants.MODELLO_A1:
				modello.setAzione("ModelloA1");
				break;
			case SharedConstants.MODELLO_A2:
				modello.setAzione("ModelloA2");
				break;
			case SharedConstants.MODELLO_D:
				modello.setAzione("ModelloD");
				break;
			case SharedConstants.MODELLO_MA:
				modello.setAzione("ModelloMacroaggregati");
				break;
			case SharedConstants.MODELLO_B1:
				modello.setAzione("ModelloB1");
				break;
			case SharedConstants.MODELLO_B:
				modello.setAzione("ModelloB");
				break;
			case SharedConstants.MODELLO_C:
				modello.setAzione("ModelloC");
				break;
			case SharedConstants.MODELLO_E:
				modello.setAzione("ModelloE");
				break;
			case SharedConstants.MODELLO_F:
				modello.setAzione("ModelloF");
				break;

			}
		}

		return listaModelliAssociati;
	}

	public List<ModelStatoCruscotto> findStati(RicercaEntiGestori ricerca) {

		List<GregDStatoRendicontazione> stati = cruscottoDao.findAllStato();
		List<ModelStatoCruscotto> listaStati = new ArrayList<ModelStatoCruscotto>();
		for (GregDStatoRendicontazione stato : stati) {
			ModelStatoCruscotto s = new ModelStatoCruscotto();
			s.setCodStato(stato.getCodStatoRendicontazione());
			s.setDesStato(stato.getDesStatoRendicontazione());
			s.setnEnti(cruscottoDao.findStati(ricerca, stato.getCodStatoRendicontazione()));
			listaStati.add(s);
		}

		return listaStati;
	}

	public String getStatoModelloEnte(Integer idRendicontazione, String codTab, String codTranche,
			String codStatoRendicontazione) {
		String stato = null;

		switch (codTab) {
		case SharedConstants.MODELLO_A:
			stato = modelloAService.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		case SharedConstants.MODELLO_A1:
			stato = modelloA1Service.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		case SharedConstants.MODELLO_A2:
			stato = modelloA2Service.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		case SharedConstants.MODELLO_D:
			stato = modelloDService.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		case SharedConstants.MODELLO_MA:
			stato = macroaggregatiService.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		case SharedConstants.MODELLO_B1:
			stato = modelloB1Service.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		case SharedConstants.MODELLO_B:
			stato = modelloBService.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		case SharedConstants.MODELLO_C:
			stato = modelloCService.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		case SharedConstants.MODELLO_E:
			stato = modelloEService.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		case SharedConstants.MODELLO_F:
			stato = modelloFService.getStatoModelloEnte(idRendicontazione, codTranche, codStatoRendicontazione);
			break;
		default:
			break;
		}
		return stato;
	}

	public GenericResponseWarnCheckErr controlloModelloEnte(InfoModello body) throws Exception {

		GenericResponseWarnCheckErr response = new GenericResponseWarnCheckErr();
		GenericResponseWarnErr invio = new GenericResponseWarnErr();
		GenericResponseWarnErr check = new GenericResponseWarnErr();
		boolean valorizzato = false;
		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(body.getIdRendicontazione(),
				body.getCodModello());
		switch (body.getCodModello()) {
		case SharedConstants.MODELLO_A:
			valorizzato = modelloADao.getValorizzatoModelloA(body.getIdRendicontazione());
			invio = modelloAService.controlloModelloA(body.getIdRendicontazione());
			if (modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
				check = modelloAService.checkModelloA(body.getIdRendicontazione());
			}
			response.setErrors(invio.getErrors());
			response.setWarnings(invio.getWarnings());
			response.setCheck(check!=null ? check.getWarnings() : new ArrayList<String>());
			break;
		case SharedConstants.MODELLO_A1:
			valorizzato = modelloA1Dao.getValorizzatoModelloA1(body.getIdRendicontazione());
			invio = modelloA1Service.controlloModelloA1(body.getIdRendicontazione());
			if (modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
				check = modelloA1Service.checkModelloA1(body.getIdRendicontazione());
			}
			response.setErrors(invio.getErrors());
			response.setWarnings(invio.getWarnings());
			response.setCheck(check.getWarnings()!=null ? check.getWarnings() : new ArrayList<String>());
			break;
		case SharedConstants.MODELLO_A2:
			valorizzato = modelloA2Dao.getValorizzatoModelloA2(body.getIdRendicontazione());
			invio = modelloA2Service.controlloModelloA2(body.getIdRendicontazione());
			check = null;
			response.setErrors(invio.getErrors());
			response.setWarnings(invio.getWarnings());
			response.setCheck(new ArrayList<String>());
			break;
		case SharedConstants.MODELLO_D:
			valorizzato = modelloDDao.getValorizzatoModelloD(body.getIdRendicontazione());
			invio = modelloDService.controlloModelloD(body.getIdRendicontazione());
			check = null;
			response.setErrors(invio.getErrors());
			response.setWarnings(invio.getWarnings());
			response.setCheck(new ArrayList<String>());
			break;
		case SharedConstants.MODELLO_MA:
			valorizzato = macroaggregatiDao.getValorizzatoModelloMA(body.getIdRendicontazione());
			invio = macroaggregatiService.controlloMacroaggregati(body.getIdRendicontazione());
			if (modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
				check = macroaggregatiService.checkMacroaggregati(body.getIdRendicontazione());
			}
			response.setErrors(invio.getErrors());
			response.setWarnings(invio.getWarnings());
			response.setCheck(check.getWarnings()!=null ? check.getWarnings() : new ArrayList<String>());
			break;
		case SharedConstants.MODELLO_B1:
			valorizzato = modelloB1Dao.getValorizzatoModelloB1(body.getIdRendicontazione());
			invio = modelloB1Service.controlloModelloB1(body.getIdRendicontazione());
			if (modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
				check = modelloB1Service.checkModelloB1(body.getIdRendicontazione());
			}
			response.setErrors(invio.getErrors());
			response.setWarnings(invio.getWarnings());
			response.setCheck(check.getWarnings()!=null ? check.getWarnings() : new ArrayList<String>());
			break;
		case SharedConstants.MODELLO_B:
			valorizzato = modelloBDao.getValorizzatoModelloB(body.getIdRendicontazione());
			invio = modelloBService.controlloModelloB(body.getIdRendicontazione());
			if (modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
				check = modelloBService.checkModelloB(body.getIdRendicontazione());
			}
			response.setErrors(invio.getErrors());
			response.setWarnings(invio.getWarnings());
			response.setCheck(check.getWarnings()!=null ? check.getWarnings() : new ArrayList<String>());
			break;
		case SharedConstants.MODELLO_C:
			valorizzato = modelloCDao.getValorizzatoModelloC(body.getIdRendicontazione());
			invio = modelloCService.controlloModelloC(body.getIdRendicontazione());
			if (modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
				check = modelloCService.checkModelloC(body.getIdRendicontazione());
			}
			response.setErrors(invio.getErrors());
			response.setWarnings(invio.getWarnings());
			response.setCheck(check.getWarnings()!=null ? check.getWarnings() : new ArrayList<String>());
			break;
		case SharedConstants.MODELLO_E:
			valorizzato = modelloEDao.getValorizzatoModelloE(body.getIdRendicontazione());
			invio = modelloEService.controlloModelloE(body.getIdRendicontazione());
			check = null;
			response.setErrors(invio.getErrors());
			response.setWarnings(new ArrayList<String>());
			response.setCheck(new ArrayList<String>());
			break;
		case SharedConstants.MODELLO_F:
			valorizzato = modelloFDao.getValorizzatoModelloF(body.getIdRendicontazione());
			invio = modelloFService.controlloModelloF(body.getIdRendicontazione());
			if (modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
				check = modelloFService.checkModelloF(body.getIdRendicontazione());
			}
			response.setErrors(invio.getErrors());
			response.setWarnings(invio.getWarnings());
			response.setCheck(check.getWarnings()!=null ? check.getWarnings() : new ArrayList<String>());
			break;
		default:
			break;
		}

		String messaggio;
		if (response.getErrors().size() == 0 && response.getWarnings().size() == 0 && response.getCheck().size() == 0
				&& valorizzato) {
			messaggio = listeService.getMessaggio(SharedConstants.MSG01CRUSCOTTO).getTestoMessaggio().replace("MODELLO",
					body.getDesModello()) + ";";
			messaggio = messaggio + listeService.getMessaggio(SharedConstants.MSG01CRUSCOTTO).getTestoMessaggio()
					.replace("MODELLO", body.getDesModello());
			response.setDescrizione(messaggio);
		}

		if (!valorizzato
				&& modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			messaggio = listeService.getMessaggio(SharedConstants.MSG02CRUSCOTTO).getTestoMessaggio().replace("MODELLO",
					body.getDesModello()) + ";";
			response.setDescrizione(messaggio);
		}
		
		response.setValorizzato(valorizzato);
		return response;
	}

	public Integer getMaxAnno() {
		Integer anno = cruscottoDao.getMaxAnno();
		return anno;
	}
	
}
