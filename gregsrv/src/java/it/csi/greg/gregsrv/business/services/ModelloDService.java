/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.jboss.resteasy.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.greg.gregsrv.business.dao.impl.ModelloDDao;
import it.csi.greg.gregsrv.business.entity.GregDTipoVoce;
import it.csi.greg.gregsrv.business.entity.GregRRendicontazioneModD;
import it.csi.greg.gregsrv.business.entity.GregDVoceModD;
import it.csi.greg.gregsrv.business.entity.GregRTipoVoceModD;
import it.csi.greg.gregsrv.business.entity.GregTCronologia;
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.EsitoMail;
import it.csi.greg.gregsrv.dto.EsportaModelloDInput;
import it.csi.greg.gregsrv.dto.GenericResponse;
import it.csi.greg.gregsrv.dto.GenericResponseWarnErr;
import it.csi.greg.gregsrv.dto.ModelCampiVoceModD;
import it.csi.greg.gregsrv.dto.ModelDatiA1;
import it.csi.greg.gregsrv.dto.ModelParametro;
import it.csi.greg.gregsrv.dto.ModelRendicontazioneModD;
import it.csi.greg.gregsrv.dto.ModelRisultatiModelloD;
import it.csi.greg.gregsrv.dto.ModelStatoMod;
import it.csi.greg.gregsrv.dto.ModelTabTranche;
import it.csi.greg.gregsrv.dto.ModelTipoVoce;
import it.csi.greg.gregsrv.dto.ModelTipoVoceModD;
import it.csi.greg.gregsrv.dto.ModelUltimoContatto;
import it.csi.greg.gregsrv.dto.ModelValoriModA;
import it.csi.greg.gregsrv.dto.ModelValoriVociModD;
import it.csi.greg.gregsrv.dto.ModelVociD;
import it.csi.greg.gregsrv.dto.SaveModelloOutput;
import it.csi.greg.gregsrv.dto.UserInfo;
import it.csi.greg.gregsrv.dto.VociModelloD;
import it.csi.greg.gregsrv.exception.IntegritaException;
import it.csi.greg.gregsrv.util.Checker;
import it.csi.greg.gregsrv.util.Converter;
import it.csi.greg.gregsrv.util.SharedConstants;
import it.csi.greg.gregsrv.util.Util;

@Service("modelloDService")
public class ModelloDService {

	@Autowired
	protected ModelloDDao modelloDDao;
	@Autowired
	protected ListeService listeService;
	@Autowired
	protected ControlloService controlloService;
	@Autowired
	protected DatiRendicontazioneService datiRendicontazioneService;
	@Autowired
	protected MailService mailService;

	public List<ModelTipoVoce> getTipoVociModD() {
		List<GregDTipoVoce> tipoVoci = modelloDDao.findAllTipoVociModelloD();

		List<ModelTipoVoce> listaTipoVoci = new ArrayList<ModelTipoVoce>();
		for (GregDTipoVoce tipoVoce : tipoVoci) {
			listaTipoVoci.add(new ModelTipoVoce(tipoVoce));
		}
		return listaTipoVoci;
	}

	public List<String> getSezioniVociModD() {

		return modelloDDao.findSezioniVociModelloD();
	}

	public List<VociModelloD> getVociModD() {
		List<GregDVoceModD> vociModD = modelloDDao.findAllVociModelloD();
		List<String> sezioni = getSezioniVociModD();

		// Sezioni Modello D
		String sezioneParteAccantonata = sezioni.get(0);
		String sezioneParteInvestimenti = sezioni.get(1);
		String sezioneParteVincolata = sezioni.get(2);
		String sezioneTabella = sezioni.get(3);

		List<ModelVociD> listaVoci = new ArrayList<ModelVociD>();
		for (GregDVoceModD voce : vociModD) {
			listaVoci.add(new ModelVociD(voce));
		}

		VociModelloD listaVociTabella = new VociModelloD();
		listaVociTabella.setSezioneModello(sezioneTabella);

		VociModelloD listaVociParteAccantonata = new VociModelloD();
		listaVociParteAccantonata.setSezioneModello(sezioneParteAccantonata);

		VociModelloD listaVociParteVincolata = new VociModelloD();
		listaVociParteVincolata.setSezioneModello(sezioneParteVincolata);

		VociModelloD listaVociInvestimenti = new VociModelloD();
		listaVociInvestimenti.setSezioneModello(sezioneParteInvestimenti);

		List<ModelVociD> listaTabella = new ArrayList<ModelVociD>();
		List<ModelVociD> listaParteAccantonata = new ArrayList<ModelVociD>();
		List<ModelVociD> listaParteVincolata = new ArrayList<ModelVociD>();
		List<ModelVociD> listaInvestimenti = new ArrayList<ModelVociD>();

		for (ModelVociD voce : listaVoci) {

			if (voce.getSezioneModello().equals(sezioneTabella)) {
				listaTabella.add(voce);
			}
			if (voce.getSezioneModello().equals(sezioneParteAccantonata)) {
				listaParteAccantonata.add(voce);
			}
			if (voce.getSezioneModello().equals(sezioneParteVincolata)) {
				listaParteVincolata.add(voce);
			}
			if (voce.getSezioneModello().equals(sezioneParteInvestimenti)) {
				listaInvestimenti.add(voce);
			}
		}

		listaVociTabella.setListaVoci(listaTabella);
		listaVociParteAccantonata.setListaVoci(listaParteAccantonata);
		listaVociParteVincolata.setListaVoci(listaParteVincolata);
		listaVociInvestimenti.setListaVoci(listaInvestimenti);

		List<VociModelloD> listaFinale = new ArrayList<VociModelloD>();
		listaFinale.add(0, listaVociTabella);
		listaFinale.add(1, listaVociParteAccantonata);
		listaFinale.add(2, listaVociParteVincolata);
		listaFinale.add(3, listaVociInvestimenti);

		return listaFinale;
	}

	public List<ModelTipoVoceModD> getRTipoVociModD() {
		List<GregRTipoVoceModD> tipoVociModD = modelloDDao.findAllRTipoVociModelloD();

		List<ModelTipoVoceModD> listaVoci = new ArrayList<ModelTipoVoceModD>();
		for (GregRTipoVoceModD tipoVoceModD : tipoVociModD) {
			GregDTipoVoce tipoVoce = modelloDDao.findTipoVoceById(tipoVoceModD.getGregDTipoVoce().getIdTipoVoce());
			listaVoci.add(new ModelTipoVoceModD(tipoVoceModD, tipoVoce));
		}
		return listaVoci;
	}

	public ModelRendicontazioneModD getRendicontazioneModDByIdScheda(Integer idScheda) {
		List<Object> rendicontazioneEnte = modelloDDao.findRendicontazioneEnteByIdScheda(idScheda);
		List<ModelRisultatiModelloD> listaRisultati = new ArrayList<ModelRisultatiModelloD>();
//		GregTSchedeEntiGestori schedaEnte = datiRendicontazioneService.getSchedaEnte(idScheda);

		Iterator<Object> itr = rendicontazioneEnte.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			listaRisultati.add(new ModelRisultatiModelloD(obj));
		}

		List<GregDVoceModD> vociModD = modelloDDao.findAllVociModelloD();
		List<GregDTipoVoce> tipoVoci = modelloDDao.findAllTipoVociModelloD();

		ModelRendicontazioneModD listaElementi = new ModelRendicontazioneModD();
//		listaElementi.setDenominazioneEnte(schedaEnte.getDenominazione());
		GregTRendicontazioneEnte rendi = datiRendicontazioneService.getRendicontazione(idScheda);
		listaElementi.setAnnoGestione(rendi.getAnnoGestione());
		listaElementi.setIdRendicontazioneEnte(rendi.getIdRendicontazioneEnte());
		listaElementi.setIdSchedaEnteGestore(rendi.getGregTSchedeEntiGestori().getIdSchedaEnteGestore());
		if (!listaRisultati.isEmpty()) {
			List<ModelValoriVociModD> elemRiga = new ArrayList<ModelValoriVociModD>();

			for (GregDVoceModD riga : vociModD) {
				List<ModelCampiVoceModD> elemColonna = new ArrayList<ModelCampiVoceModD>();

				ModelValoriVociModD valoriRendCorrente = new ModelValoriVociModD();
				valoriRendCorrente.setIdVoce(riga.getIdVoceModD());
				valoriRendCorrente.setOrdinamento(riga.getOrdinamento());
				valoriRendCorrente.setDescrizioneVoce(riga.getDescVoceModD());
				valoriRendCorrente.setOperatore(
						Checker.isValorizzato(riga.getOperatoreMatematico()) ? riga.getOperatoreMatematico() : "");
				valoriRendCorrente.setCodVoce(riga.getCodVoceModD());

				for (GregDTipoVoce colonna : tipoVoci) {

					ModelRisultatiModelloD rendCorrente = listaRisultati.stream()
							.filter(rend -> rend.getCodiceVoceModello().equals(riga.getCodVoceModD())
									&& rend.getCodiceTipoVoce().equals(colonna.getCodTipoVoce()))
							.findFirst().orElse(null);

					if (rendCorrente != null) {

//						if (rendCorrente.getAnnoGestione() != null) {
//							listaElementi.setAnnoGestione(rendCorrente.getAnnoGestione());
//						}
//						if (rendCorrente.getIdRendicontazioneEnte() != null) {
//							listaElementi.setIdRendicontazioneEnte(rendCorrente.getIdRendicontazioneEnte());
//						}
//						if (rendCorrente.getIdSchedaEnteGestore() != null) {
//							listaElementi.setIdSchedaEnteGestore(rendCorrente.getIdSchedaEnteGestore());
//						}

						ModelCampiVoceModD campoVoce = new ModelCampiVoceModD();
						campoVoce.setVoce(colonna.getDescTipoVoce());
						campoVoce.setCodVoce(colonna.getCodTipoVoce());
						campoVoce.setShow(rendCorrente.getDataEntry());
						campoVoce.setValue(rendCorrente.getValore());
						campoVoce.setOrdinamento(colonna.getOrdinamento());

						elemColonna.add(campoVoce);
						valoriRendCorrente.setCampi(elemColonna);
					}
				}

				elemRiga.add(valoriRendCorrente);
				listaElementi.setVociModello(elemRiga);
			}
		}
		
		return listaElementi;

	}

	public GenericResponse controllaImportiModelloD(ModelRendicontazioneModD body) throws IntegritaException {

		// controllo tutti i campi di tipo importo not null
		for (ModelValoriVociModD dati : body.getVociModello()) {
			for (ModelCampiVoceModD campo : dati.getCampi()) {
				if (campo.getValue() != null) {
					// Controllo Importi
					controlloService.numberDecimalValidate(
							campo.getValue().setScale(2, RoundingMode.HALF_UP).toString(),
							"Importo " + dati.getDescrizioneVoce() + " " + campo.getVoce(), 14, 2);
				}
			}
		}
		// Totali Riga
		// controllo i campi totale
		for (ModelValoriVociModD dati : body.getVociModello()) {
			BigDecimal somma = BigDecimal.ZERO;
			BigDecimal totale = BigDecimal.ZERO;
			// Totali Riga
			if (dati.getOrdinamento() == 2 || dati.getOrdinamento() == 3 || dati.getOrdinamento() == 8
					|| dati.getOrdinamento() == 9) {
				if (dati.getCampi().get(0).getValue() != null && dati.getCampi().get(1).getValue() != null
						&& dati.getCampi().get(2).getValue() != null) {
					somma = somma.add(dati.getCampi().get(0).getValue()).setScale(2, RoundingMode.HALF_UP)
							.add(dati.getCampi().get(1).getValue()).setScale(2, RoundingMode.HALF_UP);

					totale = totale.add(dati.getCampi().get(2).getValue()).setScale(2, RoundingMode.HALF_UP);

					if (!somma.equals(totale)) {
						String errortotale = listeService.getMessaggio(SharedConstants.ERROR_TOTALE)
								.getTestoMessaggio();
						errortotale = errortotale.replace("SPECIFICARE",
								"Totale " + dati.getDescrizioneVoce() + " non corretto");
						GenericResponse errore = new GenericResponse();
						errore.setId("400");
						errore.setDescrizione(errortotale);
						return errore;
					}
				}
			}
		}
		// Totali Colonna
		// Totale Saldo Cassa
		BigDecimal sommaSaldo = BigDecimal.ZERO;
		BigDecimal totaleSaldo = BigDecimal.ZERO;
		for (ModelValoriVociModD dati : body.getVociModello()) {
			if (dati.getOrdinamento() == 1 || dati.getOrdinamento() == 2) {
				if (dati.getCampi().get(2).getValue() != null) {
					sommaSaldo = sommaSaldo.add(dati.getCampi().get(2).getValue()).setScale(2, RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 3) {
				if (dati.getCampi().get(2).getValue() != null) {
					sommaSaldo = sommaSaldo.subtract(dati.getCampi().get(2).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 4) {
				if (dati.getCampi().get(2).getValue() != null) {
					totaleSaldo = totaleSaldo.add(dati.getCampi().get(2).getValue()).setScale(2, RoundingMode.HALF_UP);
				}
			}
		}
		if (sommaSaldo != BigDecimal.ZERO && totaleSaldo != BigDecimal.ZERO && !sommaSaldo.equals(totaleSaldo)) {
			String errortotale = listeService.getMessaggio(SharedConstants.ERROR_TOTALE).getTestoMessaggio();
			errortotale = errortotale.replace("SPECIFICARE",
					"Totale " + body.getVociModello().get(3).getDescrizioneVoce() + " non corretto");
			GenericResponse errore = new GenericResponse();
			errore.setId("400");
			errore.setDescrizione(errortotale);
			return errore;

		}

		// Totale Fondo Cassa
		BigDecimal sommaFondo = BigDecimal.ZERO;
		BigDecimal totaleFondo = BigDecimal.ZERO;
		for (ModelValoriVociModD dati : body.getVociModello()) {
			if (dati.getOrdinamento() == 4) {
				if (dati.getCampi().get(2).getValue() != null) {
					sommaFondo = sommaFondo.add(dati.getCampi().get(2).getValue()).setScale(2, RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 5) {
				if (dati.getCampi().get(2).getValue() != null) {
					sommaFondo = sommaFondo.subtract(dati.getCampi().get(2).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 6) {
				if (dati.getCampi().get(2).getValue() != null) {
					totaleFondo = totaleFondo.add(dati.getCampi().get(2).getValue()).setScale(2, RoundingMode.HALF_UP);
				}
			}
		}
		if (sommaFondo != BigDecimal.ZERO && totaleFondo != BigDecimal.ZERO && !sommaFondo.equals(totaleFondo)) {
			String errortotale = listeService.getMessaggio(SharedConstants.ERROR_TOTALE).getTestoMessaggio();
			errortotale = errortotale.replace("SPECIFICARE",
					"Totale " + body.getVociModello().get(5).getDescrizioneVoce() + " non corretto");
			GenericResponse errore = new GenericResponse();
			errore.setId("400");
			errore.setDescrizione(errortotale);
			return errore;
		}

		// Totale Risultato Amministrazione
		BigDecimal sommaRisultatoAmm = BigDecimal.ZERO;
		BigDecimal totaleRisultatoAmm = BigDecimal.ZERO;
		for (ModelValoriVociModD dati : body.getVociModello()) {
			if (dati.getOrdinamento() == 6 || dati.getOrdinamento() == 7) {
				if (dati.getCampi().get(2).getValue() != null) {
					sommaRisultatoAmm = sommaRisultatoAmm.add(dati.getCampi().get(2).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 9 || dati.getOrdinamento() == 10 || dati.getOrdinamento() == 11) {
				if (dati.getCampi().get(2).getValue() != null) {
					sommaRisultatoAmm = sommaRisultatoAmm.subtract(dati.getCampi().get(2).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 12) {
				if (dati.getCampi().get(2).getValue() != null) {
					totaleRisultatoAmm = totaleRisultatoAmm.add(dati.getCampi().get(2).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
		}
		if (sommaRisultatoAmm != BigDecimal.ZERO && totaleRisultatoAmm != BigDecimal.ZERO
				&& !sommaRisultatoAmm.equals(totaleRisultatoAmm)) {
			String errortotale = listeService.getMessaggio(SharedConstants.ERROR_TOTALE).getTestoMessaggio();
			errortotale = errortotale.replace("SPECIFICARE",
					"Totale " + body.getVociModello().get(11).getDescrizioneVoce() + " non corretto");
			GenericResponse errore = new GenericResponse();
			errore.setId("400");
			errore.setDescrizione(errortotale);
			return errore;
		}

		// Totale Accantonata
		BigDecimal sommaAccantonata = BigDecimal.ZERO;
		BigDecimal totaleAccantonata = BigDecimal.ZERO;
		for (ModelValoriVociModD dati : body.getVociModello()) {
			if (dati.getOrdinamento() == 14 || dati.getOrdinamento() == 15 || dati.getOrdinamento() == 16
					|| dati.getOrdinamento() == 17 || dati.getOrdinamento() == 18) {
				if (dati.getCampi().get(0).getValue() != null) {
					sommaAccantonata = sommaAccantonata.add(dati.getCampi().get(0).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 19) {
				if (dati.getCampi().get(0).getValue() != null) {
					totaleAccantonata = totaleAccantonata.add(dati.getCampi().get(0).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
		}
		if (sommaAccantonata != BigDecimal.ZERO && totaleAccantonata != BigDecimal.ZERO
				&& !sommaAccantonata.equals(totaleAccantonata)) {
			String errortotale = listeService.getMessaggio(SharedConstants.ERROR_TOTALE).getTestoMessaggio();
			errortotale = errortotale.replace("SPECIFICARE",
					"Totale " + body.getVociModello().get(18).getDescrizioneVoce() + " non corretto");
			GenericResponse errore = new GenericResponse();
			errore.setId("400");
			errore.setDescrizione(errortotale);
			return errore;
		}

		// Totale Vincolata
		BigDecimal sommaVincolata = BigDecimal.ZERO;
		BigDecimal totaleVincolata = BigDecimal.ZERO;
		for (ModelValoriVociModD dati : body.getVociModello()) {
			if (dati.getOrdinamento() == 21 || dati.getOrdinamento() == 22 || dati.getOrdinamento() == 23
					|| dati.getOrdinamento() == 24 || dati.getOrdinamento() == 25) {
				if (dati.getCampi().get(0).getValue() != null) {
					sommaVincolata = sommaVincolata.add(dati.getCampi().get(0).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 26) {
				if (dati.getCampi().get(0).getValue() != null) {
					totaleVincolata = totaleVincolata.add(dati.getCampi().get(0).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
		}
		if (sommaVincolata != BigDecimal.ZERO && totaleVincolata != BigDecimal.ZERO
				&& !sommaVincolata.equals(totaleVincolata)) {
			String errortotale = listeService.getMessaggio(SharedConstants.ERROR_TOTALE).getTestoMessaggio();
			errortotale = errortotale.replace("SPECIFICARE",
					"Totale " + body.getVociModello().get(25).getDescrizioneVoce() + " non corretto");
			GenericResponse errore = new GenericResponse();
			errore.setId("400");
			errore.setDescrizione(errortotale);
			return errore;
		}

		// Totale Disponibile
		BigDecimal sommaDisponibile = BigDecimal.ZERO;
		BigDecimal totaleDisponibile = BigDecimal.ZERO;
		for (ModelValoriVociModD dati : body.getVociModello()) {
			if (dati.getOrdinamento() == 12) {
				if (dati.getCampi().get(2).getValue() != null) {
					sommaDisponibile = sommaDisponibile.add(dati.getCampi().get(2).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 19 || dati.getOrdinamento() == 26 || dati.getOrdinamento() == 28) {
				if (dati.getCampi().get(0).getValue() != null) {
					sommaDisponibile = sommaDisponibile.subtract(dati.getCampi().get(0).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
			if (dati.getOrdinamento() == 29) {
				if (dati.getCampi().get(0).getValue() != null) {
					totaleDisponibile = totaleDisponibile.add(dati.getCampi().get(0).getValue()).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
		}
		if (sommaDisponibile != BigDecimal.ZERO && totaleDisponibile != BigDecimal.ZERO
				&& !sommaDisponibile.equals(totaleDisponibile)) {
			String errortotale = listeService.getMessaggio(SharedConstants.ERROR_TOTALE).getTestoMessaggio();
			errortotale = errortotale.replace("SPECIFICARE",
					"Totale " + body.getVociModello().get(28).getDescrizioneVoce() + " non corretto");
			GenericResponse errore = new GenericResponse();
			errore.setId("400");
			errore.setDescrizione(errortotale);
			return errore;
		}

		return null;
	}

	public List<String> controllaWarningsImportinegativiModelloD(ModelRendicontazioneModD body)
			throws IntegritaException {
		List<String> listaWarnings = new ArrayList<String>();

		List<GregDVoceModD> vociModD = modelloDDao.findAllVociModelloD();
		List<GregDTipoVoce> tipoVoci = modelloDDao.findAllTipoVociModelloD();

		// Verifico eventuali Warnings per Importi Negativi
		for (ModelValoriVociModD dati : body.getVociModello()) {
			for (ModelCampiVoceModD campo : dati.getCampi()) {
				if (dati.getDescrizioneVoce().equals(vociModD.get(11).getDescVoceModD())
						&& campo.getVoce().equals(tipoVoci.get(2).getDescTipoVoce())) {
					String warning = controlloService.isNegativeNumber(campo.getValue(),
							listeService.getMessaggio(SharedConstants.WARNING_MOD_D_01).getTestoMessaggio());
					if (warning != null) {
						listaWarnings.add(warning);
					}
				}
				if (dati.getDescrizioneVoce().equals(vociModD.get(28).getDescVoceModD())
						&& campo.getVoce().equals(tipoVoci.get(2).getDescTipoVoce())) {
					String warning = controlloService.isNegativeNumber(campo.getValue(),
							listeService.getMessaggio(SharedConstants.WARNING_MOD_D_02).getTestoMessaggio());
					if (warning != null) {
						listaWarnings.add(warning);
					}
				}
			}
		}

		return listaWarnings;
	}

	@Transactional
	public SaveModelloOutput saveModelloD(ModelRendicontazioneModD body, UserInfo userInfo, String notaEnte,
			String notaInterna) throws Exception {
		SaveModelloOutput out = new SaveModelloOutput();
		// Verifico la presenza della rendicontazione
		GregTRendicontazioneEnte rendToUpdate = datiRendicontazioneService
				.getRendicontazione(body.getIdRendicontazioneEnte());
		if (rendToUpdate == null) {
			throw new IntegritaException(Util.composeMessage(
					listeService.getMessaggio(SharedConstants.ERROR_ANNO_CONTABILE).getTestoMessaggio(), ""));
		} else {
			Timestamp dataModifica = new Timestamp(new Date().getTime());
			String newNotaEnte = "";

			String statoOld = rendToUpdate.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
			rendToUpdate = datiRendicontazioneService.modificaStatoRendicontazione(rendToUpdate, userInfo,
					SharedConstants.OPERAZIONE_SALVA, body.getProfilo());
			String statoNew = rendToUpdate.getGregDStatoRendicontazione().getDesStatoRendicontazione().toUpperCase();
			if (!statoOld.equals(statoNew)) {
				newNotaEnte = listeService.getMessaggio(SharedConstants.MESSAGE_STANDARD_CAMBIO_STATO)
						.getTestoMessaggio().replace("OPERAZIONE", "SALVA").replace("STATOOLD", "'" + statoOld + "'")
						.replace("STATONEW", "'" + statoNew + "'");
			}

			// Recupero eventuale ultima cronologia inserita
			GregTCronologia lastCrono = datiRendicontazioneService
					.findLastCronologiaEnte(rendToUpdate.getIdRendicontazioneEnte());

//			if((userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_MULTIENTE)) 
//					|| (userInfo.getRuolo() != null && userInfo.getRuolo().equalsIgnoreCase(SharedConstants.OP_ENTE))) {
			if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("CronologiaEnte")[1]) {
				if (!Checker.isValorizzato(notaEnte)) {
					String msgUpdateDati = listeService
							.getMessaggio(SharedConstants.MESSAGE_STANDARD_AGGIORNAMENTO_DATI).getTestoMessaggio()
							.replace("OPERAZIONE", "SALVA");
					newNotaEnte = !newNotaEnte.equals("") ? newNotaEnte
							: lastCrono != null && !lastCrono.getUtenteOperazione().equals(userInfo.getCodFisc())
									? msgUpdateDati
									: newNotaEnte;
				} else {
					newNotaEnte = notaEnte;
				}

			} else {
				newNotaEnte = notaEnte;
			}

			// Inserimento nuova cronologia
			if (Checker.isValorizzato(newNotaEnte) || Checker.isValorizzato(notaInterna)) {
				GregTCronologia cronologia = new GregTCronologia();
				cronologia.setGregTRendicontazioneEnte(rendToUpdate);
				cronologia.setGregDStatoRendicontazione(rendToUpdate.getGregDStatoRendicontazione());
				cronologia.setUtente(userInfo.getNome() + " " + userInfo.getCognome());
				cronologia.setModello("Mod. D");
				cronologia.setUtenteOperazione(userInfo.getCodFisc());
				cronologia.setNotaInterna(notaInterna);
				cronologia.setNotaPerEnte(newNotaEnte);
				cronologia.setDataOra(dataModifica);
				cronologia.setDataCreazione(dataModifica);
				cronologia.setDataModifica(dataModifica);
				datiRendicontazioneService.insertCronologia(cronologia);
			}

			List<GregDVoceModD> vociModD = modelloDDao.findAllVociModelloD();
			List<GregDTipoVoce> tipoVoci = modelloDDao.findAllTipoVociModelloD();

			// Recupero la lista delle voci da salvare o aggiornare
			List<ModelValoriVociModD> listaToSaveOrUpdate = body.getVociModello();
			for (ModelValoriVociModD voce : listaToSaveOrUpdate) {
				for (ModelCampiVoceModD valore : voce.getCampi()) {

					if (valore.getShow()) {

						GregDVoceModD voceCorrente = vociModD.stream()
								.filter(voceC -> voceC.getCodVoceModD().equals(voce.getCodVoce())).findFirst()
								.orElse(null);

						GregDTipoVoce tipoVoceCorrente = tipoVoci.stream()
								.filter(tipo -> tipo.getCodTipoVoce().equals(valore.getCodVoce())).findFirst()
								.orElse(null);

						GregRTipoVoceModD voceTipoVoce = modelloDDao.findVoceTipoVoce(voceCorrente.getIdVoceModD(),
								tipoVoceCorrente.getIdTipoVoce());

						GregRRendicontazioneModD rendicontazioneToUpdate = modelloDDao.findRendicontazioneByVoceEnte(
								voceCorrente.getIdVoceModD(), tipoVoceCorrente.getIdTipoVoce(),
								rendToUpdate.getIdRendicontazioneEnte());

						if (rendicontazioneToUpdate != null) {
							if (valore.getValue() == null) {
								// Cancello il record
								modelloDDao.deleteRendicontazioneModelloD(
										rendicontazioneToUpdate.getIdRendicontazioneModD());
							} else {
								// Effettuo l'aggiornamento del record
								rendicontazioneToUpdate.setValore(valore.getValue());
								rendicontazioneToUpdate.setDataModifica(dataModifica);
								modelloDDao.updateRendicontazioneModelloD(rendicontazioneToUpdate);
							}

						} else {
							if (valore.getValue() != null) {
								// Effettuo l'inserimento a DB
								GregRRendicontazioneModD newRendicontazione = new GregRRendicontazioneModD();
								newRendicontazione.setGregTRendicontazioneEnte(rendToUpdate);
								newRendicontazione.setGregRTipoVoceModD(voceTipoVoce);
								newRendicontazione.setValore(valore.getValue());
								newRendicontazione.setDataInizioValidita(dataModifica);
								newRendicontazione.setUtenteOperazione(userInfo.getCodFisc());
								newRendicontazione.setDataCreazione(dataModifica);
								newRendicontazione.setDataModifica(dataModifica);
								modelloDDao.insertRendicontazioneModelloD(newRendicontazione);
							}
						}
					}
				}
			}

		}
		if (body.getProfilo() != null && body.getProfilo().getListaazioni().get("InviaEmail")[1]) {
			ModelUltimoContatto ultimoContatto = mailService.findDatiUltimoContatto(body.getIdSchedaEnteGestore());
			// Invio Mail a EG e ResponsabileEnte
			boolean trovataemail = mailService.verificaMailAzione(SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
			if (trovataemail) {
				EsitoMail esitoMail = mailService.sendEmailEGRespEnte(ultimoContatto.getEmail(),
						ultimoContatto.getDenominazione(), ultimoContatto.getResponsabileEnte(),
						SharedConstants.MAIL_MODIFICA_DATI_RENDICONTAZIONE);
				out.setWarnings(esitoMail != null ? esitoMail.getWarnings() : new ArrayList<String>());
				out.setErrors(esitoMail != null ? esitoMail.getErrors() : new ArrayList<String>());
			}
		}

		return out;
	}

	@Transactional
	public String esportaModelloD(EsportaModelloDInput body) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("ModelloD");
		int rowCount = 0;
		int columnCount = 0;
		String pattern = "###,##0.00";
		DataFormat format = workbook.createDataFormat();
		Row row = sheet.createRow(rowCount);

		// crea stili arial 12 bold italic
		CellStyle cellStyleb12I = sheet.getWorkbook().createCellStyle();
		cellStyleb12I.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontb12I = sheet.getWorkbook().createFont();
		fontb12I.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontb12I.setFontHeightInPoints((short) 12);
		fontb12I.setFontName(HSSFFont.FONT_ARIAL);
		fontb12I.setItalic(true);
		cellStyleb12I.setFont(fontb12I);
		cellStyleb12I.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold
		CellStyle cellStyle12b = sheet.getWorkbook().createCellStyle();
		cellStyle12b.setAlignment(CellStyle.ALIGN_LEFT);
		Font font12b = sheet.getWorkbook().createFont();
		font12b.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font12b.setFontHeightInPoints((short) 12);
		font12b.setFontName(HSSFFont.FONT_ARIAL);
		cellStyle12b.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle12b.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12b.setFont(font12b);
		cellStyle12b.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold center
		CellStyle cellStyle12bCenter = sheet.getWorkbook().createCellStyle();
		cellStyle12bCenter.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle12bCenter.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle12bCenter.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12bCenter.setFont(font12b);
		cellStyle12bCenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial 12 bold yellow
		CellStyle cellStyle12by = sheet.getWorkbook().createCellStyle();
		cellStyle12by.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyle12by.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle12by.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyle12by.setFont(font12b);
		cellStyle12by.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial titolo
		CellStyle cellStyleTitolo = sheet.getWorkbook().createCellStyle();
		cellStyleTitolo.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleTitolo.setFont(font12b);
		cellStyleTitolo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial titolo parti
		CellStyle cellStyleTitoloParti = sheet.getWorkbook().createCellStyle();
		cellStyleTitoloParti.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleTitoloParti.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cellStyleTitoloParti.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTitoloParti.setFont(font12b);
		cellStyleTitoloParti.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// crea stili arial titolo parti
		CellStyle cellStyleTotaliParti = sheet.getWorkbook().createCellStyle();
		cellStyleTotaliParti.setAlignment(CellStyle.ALIGN_LEFT);
		cellStyleTotaliParti.setFont(font12b);
		cellStyleTotaliParti.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Valore Green
		CellStyle cellStyleTotaleParteValueGreen = sheet.getWorkbook().createCellStyle();
		cellStyleTotaleParteValueGreen.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleTotaleParteValueGreen.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleTotaleParteValueGreen.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleTotaleParteValueGreen.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleTotaleParteValueGreen.setBorderTop(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleParteValueGreen.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleParteValueGreen.setBorderRight(CellStyle.BORDER_MEDIUM);
		cellStyleTotaleParteValueGreen.setBorderBottom(CellStyle.BORDER_MEDIUM);
		// Crea Stile Vertical Align
		CellStyle cellStyleVocetabellaVerticalAlign = sheet.getWorkbook().createCellStyle();
		cellStyleVocetabellaVerticalAlign.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontVocetabellaVerticalAlign = sheet.getWorkbook().createFont();
		fontVocetabellaVerticalAlign.setFontHeightInPoints((short) 10);
		fontVocetabellaVerticalAlign.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleVocetabellaVerticalAlign.setFont(fontVocetabellaVerticalAlign);
		cellStyleVocetabellaVerticalAlign.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Voce tabella
		CellStyle cellStyleVocetabella = sheet.getWorkbook().createCellStyle();
		cellStyleVocetabella.setAlignment(CellStyle.ALIGN_LEFT);
		Font fontVocetabella = sheet.getWorkbook().createFont();
		fontVocetabella.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fontVocetabella.setFontHeightInPoints((short) 10);
		fontVocetabella.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleVocetabella.setFont(fontVocetabella);
		cellStyleVocetabella.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Operatore
		CellStyle cellStyleOperatore = sheet.getWorkbook().createCellStyle();
		cellStyleOperatore.setAlignment(CellStyle.ALIGN_CENTER);
		Font fontOperatore = sheet.getWorkbook().createFont();
		fontOperatore.setFontHeightInPoints((short) 10);
		fontOperatore.setFontName(HSSFFont.FONT_ARIAL);
		cellStyleOperatore.setFont(fontOperatore);
		cellStyleOperatore.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// Crea Stile Colonna Valore Not Show
		CellStyle cellStyleValueNotShow = sheet.getWorkbook().createCellStyle();
		cellStyleValueNotShow.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueNotShow.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyleValueNotShow.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueNotShow.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueNotShow.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueNotShow.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueNotShow.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueNotShow.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore White
		CellStyle cellStyleValueWhite = sheet.getWorkbook().createCellStyle();
		cellStyleValueWhite.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueWhite.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyleValueWhite.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueWhite.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueWhite.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueWhite.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueWhite.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueWhite.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore White with border left
		CellStyle cellStyleValueWhiteWithBorderLeft = sheet.getWorkbook().createCellStyle();
		cellStyleValueWhiteWithBorderLeft.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueWhiteWithBorderLeft.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		cellStyleValueWhiteWithBorderLeft.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueWhiteWithBorderLeft.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueWhiteWithBorderLeft.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueWhiteWithBorderLeft.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cellStyleValueWhiteWithBorderLeft.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueWhiteWithBorderLeft.setBorderBottom(CellStyle.BORDER_THIN);
		// Crea Stile Colonna Valore Green
		CellStyle cellStyleValueGreen = sheet.getWorkbook().createCellStyle();
		cellStyleValueGreen.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleValueGreen.setFillForegroundColor(IndexedColors.LIME.getIndex());
		cellStyleValueGreen.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleValueGreen.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleValueGreen.setBorderTop(CellStyle.BORDER_THIN);
		cellStyleValueGreen.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyleValueGreen.setBorderRight(CellStyle.BORDER_THIN);
		cellStyleValueGreen.setBorderBottom(CellStyle.BORDER_THIN);

		ModelParametro parametro = listeService.getParametro("EXP");
		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(body.getIdEnte());
		ModelTabTranche tabTrancheModello = datiRendicontazioneService.getTranchePerModelloEnte(body.getIdEnte(),
				SharedConstants.MODELLO_D);
		// List<VociModelloD> listaVoci = getVociModD();

		// RIGA 0
		Cell cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) parametro.getValtext());
		cell.setCellStyle(cellStyleb12I);
		// RIGA 1
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 1, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) "ENTE GESTORE DENOMINAZIONE E NUMERO :");
		cell.setCellStyle(cellStyle12b);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) rendicontazione.getGregTSchedeEntiGestori().getCodiceRegionale() + " - "
				+ body.getDatiD().getDenominazioneEnte());
		cell.setCellStyle(cellStyle12by);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGA 2
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		Integer annoril = rendicontazione.getAnnoGestione() + 1;
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue(
				(String) "ANNO DI RIFERIMENTO : " + rendicontazione.getAnnoGestione() + " - RILEVAZIONE " + annoril);
		cell.setCellStyle(cellStyle12b);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGA 3
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		cell.setCellValue((String) tabTrancheModello.getDesEstesaTab());
		cell.setCellStyle(cellStyleTitolo);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGA 4
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cell.setCellStyle(cellStyle12b);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle12b);
		cell = row.createCell(++columnCount);
		cell.setCellValue((String) "GESTIONE");
		cell.setCellStyle(cellStyle12bCenter);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle12b);
		cell = row.createCell(++columnCount);
		cell.setCellStyle(cellStyle12b);
		cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		// RIGA 5
		columnCount = 0;
		row = sheet.createRow(++rowCount);
		cell = row.createCell(columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		cell = row.createCell(++columnCount);
		cellRangeAddress = new CellRangeAddress(rowCount - 1, rowCount, columnCount, columnCount);
		sheet.addMergedRegion(cellRangeAddress);
		RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		for (ModelTipoVoce voce : body.getVociD()) {
			cell = row.createCell(++columnCount);
			cell.setCellValue(voce.getDescTipoVoce().toUpperCase());
			cell.setCellStyle(cellStyle12bCenter);
			cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount, columnCount);
			sheet.addMergedRegion(cellRangeAddress);
			RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
			RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
		}

		// RIGHE TABELLE
		for (ModelValoriVociModD rowModello : body.getDatiD().getVociModello()) {
			columnCount = 0;
			// Riga Di cui Residui Attivi
			if (rowModello.getCodVoce().equals("08")) {
				row = sheet.createRow(++rowCount);
				cell = row.createCell(columnCount);
				cell = row.createCell(++columnCount);
				cell.setCellValue((String) rowModello.getDescrizioneVoce());
				cell.setCellStyle(cellStyleVocetabellaVerticalAlign);
				cell.getCellStyle().setWrapText(true);
				for (ModelCampiVoceModD voceRow : rowModello.getCampi()) {
					cell = row.createCell(++columnCount);
					if (voceRow.getShow()) {
						if (voceRow.getValue() != null) {
							cell.setCellValue((double) Double.valueOf(Util.convertBigDecimalToString(voceRow.getValue())
									.replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
						if (voceRow.getCodVoce().equals("02")) {
							cell.setCellStyle(cellStyleValueWhiteWithBorderLeft);
						} else if (voceRow.getCodVoce().equals("03")) {
							cell.setCellStyle(cellStyleValueGreen);
						} else {
							cell.setCellStyle(cellStyleValueWhite);
						}
					} else {
						if (voceRow.getValue() != null) {
							cell.setCellValue((double) Double.valueOf(Util.convertBigDecimalToString(voceRow.getValue())
									.replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							cell.setCellStyle(cellStyleValueGreen);
						} else {
							cell.setCellStyle(cellStyleValueNotShow);
						}
					}
					cell.getCellStyle().setDataFormat(format.getFormat(pattern));
				}
			} else {
				// TABELLA PRINCIPALE
				if (rowModello.getCodVoce().equals("01") || rowModello.getCodVoce().equals("02")
						|| rowModello.getCodVoce().equals("03") || rowModello.getCodVoce().equals("04")
						|| rowModello.getCodVoce().equals("05") || rowModello.getCodVoce().equals("06")
						|| rowModello.getCodVoce().equals("07") || rowModello.getCodVoce().equals("09")
						|| rowModello.getCodVoce().equals("10") || rowModello.getCodVoce().equals("11")
						|| rowModello.getCodVoce().equals("12")) {
					row = sheet.createRow(++rowCount);
					cell = row.createCell(columnCount);
					cell.setCellValue((String) rowModello.getDescrizioneVoce());
					if (rowModello.getCodVoce().equals("01") || rowModello.getCodVoce().equals("04")
							|| rowModello.getCodVoce().equals("06") || rowModello.getCodVoce().equals("12")) {
						cell.setCellStyle(cellStyleVocetabella);
					} else {
						cell.setCellStyle(cellStyleVocetabellaVerticalAlign);
					}
					cell = row.createCell(++columnCount);
					cell.setCellValue((String) rowModello.getOperatore());
					cell.setCellStyle(cellStyleOperatore);
					for (ModelCampiVoceModD voceRow : rowModello.getCampi()) {
						cell = row.createCell(++columnCount);
						if (voceRow.getShow()) {
							if (voceRow.getValue() != null) {
								cell.setCellValue(
										(double) Double.valueOf(Util.convertBigDecimalToString(voceRow.getValue())
												.replaceAll("\\.", "").replace(",", ".")));
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);
							}

							if (voceRow.getCodVoce().equals("02") && (rowModello.getCodVoce().equals("02")
									|| rowModello.getCodVoce().equals("03") || rowModello.getCodVoce().equals("07")
									|| rowModello.getCodVoce().equals("08") || rowModello.getCodVoce().equals("09"))) {
								cell.setCellStyle(cellStyleValueWhiteWithBorderLeft);
							} else if (voceRow.getCodVoce().equals("03") && (!rowModello.getCodVoce().equals("01")
									&& !rowModello.getCodVoce().equals("05") && !rowModello.getCodVoce().equals("10")
									&& !rowModello.getCodVoce().equals("11"))) {
								cell.setCellStyle(cellStyleValueGreen);
							} else if (voceRow.getCodVoce().equals("03") && (rowModello.getCodVoce().equals("01")
									|| rowModello.getCodVoce().equals("05") || rowModello.getCodVoce().equals("10")
									|| rowModello.getCodVoce().equals("11"))) {
								cell.setCellStyle(cellStyleValueWhite);
							} else {
								cell.setCellStyle(cellStyleValueWhite);
							}
						} else {
							if (voceRow.getValue() != null) {
								cell.setCellValue(
										(double) Double.valueOf(Util.convertBigDecimalToString(voceRow.getValue())
												.replaceAll("\\.", "").replace(",", ".")));
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);
								if (voceRow.getCodVoce().equals("03")
										&& (rowModello.getCodVoce().equals("04") || rowModello.getCodVoce().equals("06")
												|| rowModello.getCodVoce().equals("12"))) {
									cell.setCellStyle(cellStyleTotaleParteValueGreen);
								} else {
									cell.setCellStyle(cellStyleValueGreen);
								}
							} else {
								cell.setCellStyle(cellStyleValueNotShow);
							}
						}
					}

					if (rowModello.getCodVoce().equals("12")) {
						// Riga Composizione del risultato...
						columnCount = 0;
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
						sheet.addMergedRegion(cellRangeAddress);
						cell.setCellValue((String) "Composizione del risultato di amministrazione  al 31 dicembre");
						cell.setCellStyle(cellStyleTitolo);
						RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
						RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet,
								sheet.getWorkbook());
						RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet,
								sheet.getWorkbook());
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount - 1);
						RegionUtil.setBorderTop(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet, sheet.getWorkbook());
					}
				}
				// PARTE ACCANTONATA
				else if (rowModello.getCodVoce().equals("13") || rowModello.getCodVoce().equals("14")
						|| rowModello.getCodVoce().equals("15") || rowModello.getCodVoce().equals("16")
						|| rowModello.getCodVoce().equals("17") || rowModello.getCodVoce().equals("18")
						|| rowModello.getCodVoce().equals("19")) {
					columnCount = 0;
					if (rowModello.getCodVoce().equals("13")) {
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell.setCellValue((String) rowModello.getDescrizioneVoce());
						// cell.setCellValue((String) rowModello.getDescrizioneVoce() + " (" +
						// listaVoci.get(1).getListaVoci().get(0).getMsgInformativo() +")");
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
						cell.setCellStyle(cellStyleTitoloParti);
						sheet.addMergedRegion(cellRangeAddress);
						RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					} else if (rowModello.getCodVoce().equals("19")) {
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell = row.createCell(++columnCount);
						cell.setCellValue((String) rowModello.getDescrizioneVoce());
						cell.setCellStyle(cellStyleTotaliParti);
						cell = row.createCell(++columnCount);
						cell = row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
						sheet.addMergedRegion(cellRangeAddress);
						cell = row.createCell(++columnCount);
						if (rowModello.getCampi().get(0).getValue() != null) {
							cell.setCellValue((double) Double
									.valueOf(Util.convertBigDecimalToString(rowModello.getCampi().get(0).getValue())
											.replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
						cell.setCellStyle(cellStyleTotaleParteValueGreen);
					} else {
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell.setCellValue((String) rowModello.getDescrizioneVoce());
						cell = row.createCell(++columnCount);
						cell = row.createCell(++columnCount);
						cell = row.createCell(++columnCount);
						cell = row.createCell(++columnCount);
						if (rowModello.getCampi().get(0).getValue() != null) {
							cell.setCellValue((double) Double
									.valueOf(Util.convertBigDecimalToString(rowModello.getCampi().get(0).getValue())
											.replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
						cell.setCellStyle(cellStyleValueWhite);
					}
				}
				// PARTE VINCOLATA
				else if (rowModello.getCodVoce().equals("20") || rowModello.getCodVoce().equals("21")
						|| rowModello.getCodVoce().equals("22") || rowModello.getCodVoce().equals("23")
						|| rowModello.getCodVoce().equals("24") || rowModello.getCodVoce().equals("25")
						|| rowModello.getCodVoce().equals("26")) {
					columnCount = 0;
					if (rowModello.getCodVoce().equals("20")) {
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell.setCellValue((String) rowModello.getDescrizioneVoce());
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
						cell.setCellStyle(cellStyleTitoloParti);
						sheet.addMergedRegion(cellRangeAddress);
						RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					} else if (rowModello.getCodVoce().equals("26")) {
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell = row.createCell(++columnCount);
						cell.setCellValue((String) rowModello.getDescrizioneVoce());
						cell.setCellStyle(cellStyleTotaliParti);
						cell = row.createCell(++columnCount);
						cell = row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
						sheet.addMergedRegion(cellRangeAddress);
						cell = row.createCell(++columnCount);
						if (rowModello.getCampi().get(0).getValue() != null) {
							cell.setCellValue((double) Double
									.valueOf(Util.convertBigDecimalToString(rowModello.getCampi().get(0).getValue())
											.replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
						cell.setCellStyle(cellStyleTotaleParteValueGreen);
					} else {
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell.setCellValue((String) rowModello.getDescrizioneVoce());
						cell = row.createCell(++columnCount);
						cell = row.createCell(++columnCount);
						cell = row.createCell(++columnCount);
						cell = row.createCell(++columnCount);
						if (rowModello.getCampi().get(0).getValue() != null) {
							cell.setCellValue((double) Double
									.valueOf(Util.convertBigDecimalToString(rowModello.getCampi().get(0).getValue())
											.replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
						cell.setCellStyle(cellStyleValueWhite);
					}
				}
				// PARTE DESTINATA INVESTIMENTI
				else if (rowModello.getCodVoce().equals("27") || rowModello.getCodVoce().equals("28")) {
					columnCount = 0;
					if (rowModello.getCodVoce().equals("27")) {
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell.setCellValue((String) rowModello.getDescrizioneVoce());
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount);
						cell.setCellStyle(cellStyleTitoloParti);
						sheet.addMergedRegion(cellRangeAddress);
						RegionUtil.setBorderTop(CellStyle.BORDER_THIN, cellRangeAddress, sheet, sheet.getWorkbook());
					} else {
						row = sheet.createRow(++rowCount);
						cell = row.createCell(columnCount);
						cell = row.createCell(++columnCount);
						cell.setCellValue((String) rowModello.getDescrizioneVoce());
						cell.setCellStyle(cellStyleTotaliParti);
						cell = row.createCell(++columnCount);
						cell = row.createCell(++columnCount);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 2, columnCount);
						sheet.addMergedRegion(cellRangeAddress);
						cell = row.createCell(++columnCount);
						if (rowModello.getCampi().get(0).getValue() != null) {
							cell.setCellValue((double) Double
									.valueOf(Util.convertBigDecimalToString(rowModello.getCampi().get(0).getValue())
											.replaceAll("\\.", "").replace(",", ".")));
							cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						}
						cell.setCellStyle(cellStyleValueWhite);
						cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 4, columnCount - 1);
						RegionUtil.setBorderBottom(CellStyle.BORDER_DOUBLE, cellRangeAddress, sheet,
								sheet.getWorkbook());
					}
				}
				// TOTALE PARTE DISPONIBILE
				else if (rowModello.getCodVoce().equals("29")) {
					columnCount = 0;
					row = sheet.createRow(++rowCount);
					cell = row.createCell(columnCount);
					cell.setCellValue((String) rowModello.getDescrizioneVoce());
					cell.setCellStyle(cellStyleTotaliParti);
					cell = row.createCell(++columnCount);
					cell = row.createCell(++columnCount);
					cell = row.createCell(++columnCount);
					cellRangeAddress = new CellRangeAddress(rowCount, rowCount, columnCount - 3, columnCount);
					sheet.addMergedRegion(cellRangeAddress);
					cell = row.createCell(++columnCount);
					if (rowModello.getCampi().get(0).getValue() != null) {
						cell.setCellValue((double) Double
								.valueOf(Util.convertBigDecimalToString(rowModello.getCampi().get(0).getValue())
										.replaceAll("\\.", "").replace(",", ".")));
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					}
					cell.setCellStyle(cellStyleTotaleParteValueGreen);
					cell.getCellStyle().setDataFormat(format.getFormat(pattern));
				}
			}
		}

		// Bordo Esterno Documento Finale
		Integer rowsSheet = sheet.getWorkbook().getSheetAt(0).getPhysicalNumberOfRows();
		cellRangeAddress = new CellRangeAddress(3, rowsSheet - 1, 0, 4);
		RegionUtil.setBorderTop(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderLeft(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderRight(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());
		RegionUtil.setBorderBottom(CellStyle.BORDER_MEDIUM, cellRangeAddress, sheet, sheet.getWorkbook());

		Util.autoSizeColumns(sheet.getWorkbook());
		// Set dimensione colonne importi
		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(2, 30 * 256);
		sheet.setColumnWidth(3, 30 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		sheet.getRow(13).setHeight((short) (row.getHeight() * 2));
		String nomefile = Converter.getData(new Date()).replace("/", "");
		File fileXls = File.createTempFile("ExportModelloD_" + nomefile, ".xls");
		FileOutputStream ouputStream = new FileOutputStream(fileXls);
		workbook.write(ouputStream);
		if (ouputStream != null)
			ouputStream.close();
		return Base64.encodeBytes(FileUtils.readFileToByteArray(fileXls));
	}

	public String getStatoModelloEnte(Integer idRendicontazione, String codTranche, String codStatoRendicontazione) {
		ModelStatoMod stato = modelloDDao.getStatoModelloD(idRendicontazione);
		if (!stato.isValorizzato()) {
			if (!codTranche.equals(SharedConstants.TRANCHEI)) {
				if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)) {
					return SharedConstants.NON_DISPONIBILE;
				} else if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)) {
					return SharedConstants.NON_COMPILATO;
				} else if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
					return SharedConstants.NON_DISPONIBILE;
				}
			} else {
				if (!codTranche.equals(SharedConstants.TRANCHEII)) {
					if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
							|| codStatoRendicontazione
									.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
							|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
						return SharedConstants.NON_DISPONIBILE;
					}
				}
				if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_I)) {
					return SharedConstants.NON_COMPILATO;
				} else if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
					return SharedConstants.COMPILATO;
				}
			}

		} else {
			if (codTranche.equals(SharedConstants.TRANCHEI)) {
				if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_I)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_RETTIFICA_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_DA_COMPILARE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_COMPILAZIONE_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
					return SharedConstants.COMPILATO;
				} else {
					return stato.getStato();
				}
			} else if (codTranche.equals(SharedConstants.TRANCHEII)) {
				if (codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_RIESAME_II)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_IN_ATTESA_VALIDAZIONE)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_STORICIZZATA)
						|| codStatoRendicontazione.equals(SharedConstants.STATO_RENDICONTAZIONE_VALIDATA)) {
					return SharedConstants.COMPILATO;
				} else {
					return stato.getStato();
				}
			}
		}
		return null;
	}

	@Transactional
	public GenericResponseWarnErr controlloModelloD(Integer idRendicontazione) throws Exception {
		GenericResponseWarnErr response = new GenericResponseWarnErr();
		response.setWarnings(new ArrayList<String>());
		response.setErrors(new ArrayList<String>());

		GregTRendicontazioneEnte rendicontazione = datiRendicontazioneService.getRendicontazione(idRendicontazione);
		ModelTabTranche modello = datiRendicontazioneService.findModellibyCod(idRendicontazione,
				SharedConstants.MODELLO_D);
		boolean facoltativo = false;
		boolean valorizzato = modelloDDao.getValorizzatoModelloD(idRendicontazione);
		if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.OBBLIGATORIO)) {
			response.getErrors().add(listeService.getMessaggio(SharedConstants.ERRORE_MODELLO_OBBLIGATORIO)
					.getTestoMessaggio().replace("MODELLINO", modello.getDesEstesaTab()));
		} else if (!valorizzato && modello.getCodObbligo().equals(SharedConstants.FACOLTATIVO)) {
			facoltativo = true;
		}

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
				response.getWarnings().add(warn);
		} catch (IntegritaException e) {
			e.printStackTrace();
		}
		try {
			String warn = controlloService.isNegativeNumber(totalepartedisp,
					listeService.getMessaggio(SharedConstants.WARNING_MOD_D_04).getTestoMessaggio());
			if (warn != null)
				response.getWarnings().add(warn);
		} catch (IntegritaException e) {
			e.printStackTrace();
		}

		return response;

	}
}
