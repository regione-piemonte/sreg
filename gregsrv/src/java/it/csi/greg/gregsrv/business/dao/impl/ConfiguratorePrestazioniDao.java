/*******************************************************************************
* Copyright Regione Piemonte - 2024
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/
package it.csi.greg.gregsrv.business.dao.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
import it.csi.greg.gregsrv.business.entity.GregTRendicontazioneEnte;
import it.csi.greg.gregsrv.dto.ModelDettaglioPrestazConfiguratore;
import it.csi.greg.gregsrv.dto.ModelMacroaggregatiConf;
import it.csi.greg.gregsrv.dto.ModelNomenclatore;
import it.csi.greg.gregsrv.dto.ModelPrest1Prest2;
import it.csi.greg.gregsrv.dto.ModelPrest1PrestMin;
import it.csi.greg.gregsrv.dto.ModelPrest2PrestIstat;
import it.csi.greg.gregsrv.dto.ModelPrestUtenza;
import it.csi.greg.gregsrv.dto.ModelListeConfiguratore;

@Repository("configuratorePrestazioniDao")
@Transactional(readOnly = true)
public class ConfiguratorePrestazioniDao {
	@PersistenceContext
	private EntityManager em;

	public List<ModelDettaglioPrestazConfiguratore> findPrestazioni() {
		String hqlQuery = "select reg1 " + "from GregTPrestazioniRegionali1 reg1 "
				+ "LEFT JOIN FETCH reg1.gregTPrestazioniRegionali1 p1 " + "where reg1.dataCancellazione is null "
				+ "and p1.dataCancellazione is null " + "order by reg1.ordinamento ";

		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);

		List<GregTPrestazioniRegionali1> result = (ArrayList<GregTPrestazioniRegionali1>) query.getResultList();

		List<ModelDettaglioPrestazConfiguratore> prest1 = new ArrayList<ModelDettaglioPrestazConfiguratore>();

		for (GregTPrestazioniRegionali1 prestazione : result) {
			ModelDettaglioPrestazConfiguratore preg1 = new ModelDettaglioPrestazConfiguratore();
			preg1.setIdPrestazione(prestazione.getIdPrestReg1());
			preg1.setCodPrestRegionale(prestazione.getCodPrestReg1());
			preg1.setDesPrestRegionale(prestazione.getDesPrestReg1());
			preg1.setOrdinamento(prestazione.getOrdinamento());
			preg1.setDal(prestazione.getDataInizioValidita());
			preg1.setAl(prestazione.getDataFineValidita());
			prest1.add(preg1);
		}
		return prest1;
	}

	public GregTPrestazioniRegionali1 findPrestazioneById(Integer idPrestazione) {
		try {
		String hqlQuery = "SELECT g FROM GregTPrestazioniRegionali1 g " + "LEFT JOIN FETCH g.gregDTipoStruttura s "
				+ "LEFT JOIN FETCH g.gregDTipologia t " + "LEFT JOIN FETCH g.gregDTipologiaQuota tq "
				+ "LEFT JOIN FETCH g.gregTPrestazioniRegionali1 p1 "
				+ "WHERE g.idPrestReg1 = :idPrestazione AND g.dataCancellazione IS NULL "
				+ "AND t.dataCancellazione IS NULL " + "AND tq.dataCancellazione IS NULL "
				+ "AND s.dataCancellazione IS NULL " + "AND p1.dataCancellazione IS NULL ";
		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
		query.setParameter("idPrestazione", idPrestazione);
		return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregTPrestazioniRegionali1 findPrestazioneByIdAndDataInizio(Integer idPrestazione) {
		String hqlQuery = "SELECT g FROM GregTPrestazioniRegionali1 g " + "LEFT JOIN FETCH g.gregDTipoStruttura s "
				+ "LEFT JOIN FETCH g.gregDTipologia t " + "LEFT JOIN FETCH g.gregDTipologiaQuota tq "
				+ "LEFT JOIN FETCH g.gregTPrestazioniRegionali1 p1 " + "WHERE g.idPrestReg1 = :idPrestazione "
				+ "AND g.dataCancellazione IS NULL " + "AND t.dataCancellazione IS NULL "
				+ "AND tq.dataCancellazione IS NULL " + "AND s.dataCancellazione IS NULL "
				+ "AND p1.dataCancellazione IS NULL ";
		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
		query.setParameter("idPrestazione", idPrestazione);
		return query.getSingleResult();
	}

//	public List<ModelMacroaggregatiConf> findMacroaggregatiByPrestRegionale(Integer idPrestazione)
//			throws ParseException {
//		String hqlQuery = "SELECT m FROM GregTMacroaggregatiBilancio m "
//				+ "LEFT JOIN GregRPrestReg1MacroaggregatiBilancio pm on m.idMacroaggregatoBilancio = pm.gregTMacroaggregatiBilancio.idMacroaggregatoBilancio "
//				+ "LEFT JOIN GregTPrestazioniRegionali1 p on pm.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
//				+ "WHERE p.idPrestReg1 = :idPrestazione " + "AND p.dataCancellazione IS NULL "
//				+ "AND pm.dataCancellazione IS NULL " + "AND m.dataCancellazione IS NULL "
//				+ "Order by m.codMacroaggregatoBilancio";
//		TypedQuery<GregTMacroaggregatiBilancio> query = em.createQuery(hqlQuery, GregTMacroaggregatiBilancio.class);
//		query.setParameter("idPrestazione", idPrestazione);
//		List<GregTMacroaggregatiBilancio> macro = query.getResultList();
//		List<ModelMacroaggregatiConf> macroaggregati = new ArrayList<ModelMacroaggregatiConf>();
//		for (GregTMacroaggregatiBilancio m : macro) {
//			ModelMacroaggregatiConf macroaggregato = new ModelMacroaggregatiConf();
//			macroaggregato.setIdMacroaggregati(m.getIdMacroaggregatoBilancio());
//			macroaggregato.setCodMacroaggregati(m.getCodMacroaggregatoBilancio());
//			macroaggregato.setDescMacroaggregati(m.getDesMacroaggregatoBilancio());
//			GregRPrestReg1MacroaggregatiBilancio mac = findRMacroaggregatiByPrestRegionale(idPrestazione,
//					m.getIdMacroaggregatoBilancio());
//			macroaggregato.setDal(mac.getDataInizioValidita());
//			macroaggregato.setAl(mac.getDataFineValidita());
//			macroaggregato.setModificabile(getListaMacroValorizzate(idPrestazione, m.getIdMacroaggregatoBilancio()));
//			Timestamp data = getAnnoMacroValorizzate(idPrestazione, m.getIdMacroaggregatoBilancio());
//			macroaggregato.setDataMin(data.compareTo(macroaggregato.getDal()) > 0 ? new Timestamp(data.getTime()+1) : new Timestamp(macroaggregato.getDal().getTime()+1));
//			macroaggregati.add(macroaggregato);
//		}
//		return macroaggregati;
//	}

	public List<ModelMacroaggregatiConf> findMacroaggregatiByPrestRegionale(Integer idPrestazione)
			throws ParseException {
		String hqlQuery = "SELECT pm FROM GregTMacroaggregatiBilancio m "
				+ "LEFT JOIN GregRPrestReg1MacroaggregatiBilancio pm on m.idMacroaggregatoBilancio = pm.gregTMacroaggregatiBilancio.idMacroaggregatoBilancio "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p on pm.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
				+ "WHERE p.idPrestReg1 = :idPrestazione " + "AND p.dataCancellazione IS NULL "
				+ "AND pm.dataCancellazione IS NULL " + "AND m.dataCancellazione IS NULL "
				+ "Order by m.codMacroaggregatoBilancio";
		TypedQuery<GregRPrestReg1MacroaggregatiBilancio> query = em.createQuery(hqlQuery,
				GregRPrestReg1MacroaggregatiBilancio.class);
		query.setParameter("idPrestazione", idPrestazione);
		List<GregRPrestReg1MacroaggregatiBilancio> macro = query.getResultList();
		List<ModelMacroaggregatiConf> macroaggregati = new ArrayList<ModelMacroaggregatiConf>();
		for (GregRPrestReg1MacroaggregatiBilancio m : macro) {
			ModelMacroaggregatiConf macroaggregato = new ModelMacroaggregatiConf();
			macroaggregato.setIdMacroaggregati(m.getGregTMacroaggregatiBilancio().getIdMacroaggregatoBilancio());
			macroaggregato.setCodMacroaggregati(m.getGregTMacroaggregatiBilancio().getCodMacroaggregatoBilancio());
			macroaggregato.setDescMacroaggregati(m.getGregTMacroaggregatiBilancio().getDesMacroaggregatoBilancio());
			macroaggregato.setDal(m.getDataInizioValidita());
			macroaggregato.setAl(m.getDataFineValidita());
			macroaggregato.setDataCreazione(m.getDataCreazione());
			macroaggregato.setModificabile(getListaMacroValorizzate(idPrestazione,
					m.getGregTMacroaggregatiBilancio().getIdMacroaggregatoBilancio()));
			Timestamp data = getAnnoMacroValorizzate(idPrestazione,
					m.getGregTMacroaggregatiBilancio().getIdMacroaggregatoBilancio());
			macroaggregato.setDataMin(data.compareTo(macroaggregato.getDal()) > 0 ? data : macroaggregato.getDal());
			macroaggregati.add(macroaggregato);
		}
		return macroaggregati;
	}

	@SuppressWarnings("unchecked")
	public boolean getListaMacroValorizzate(Integer idPrestazione, Integer idMacro) {

		Query query = em.createNativeQuery("select  " + "case  " + "when count(r.valore)>0 " + "then true "
				+ "else false " + "end " + "from greg_r_rendicontazione_preg1_macro r "
				+ "left join greg_r_prest_reg1_macroaggregati_bilancio pm on r.id_prest_reg1_macroaggregati_bilancio = pm.id_prest_reg1_macroaggregati_bilancio  "
				+ "left join greg_t_prestazioni_regionali_1 p on pm.id_prest_reg_1 = p.id_prest_reg_1  "
				+ "left join greg_t_macroaggregati_bilancio m on pm.id_macroaggregato_bilancio = m.id_macroaggregato_bilancio  "
				+ "where  " + "r.valore is not null and pm.data_cancellazione is null "
				+ "and p.id_prest_reg_1 = :idPrestazione  "
				+ "and m.id_macroaggregato_bilancio = :idMacro and p.data_cancellazione is null and m.data_cancellazione is null ");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idMacro", idMacro);

		boolean result = (boolean) query.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Timestamp getAnnoMacroValorizzate(Integer idPrestazione, Integer idMacro) throws ParseException {

		Query query = em.createNativeQuery("with s as(select   " + "case  "
				+ " when case  when count(r.valore)>0 then true  " + "else false end  " + "then g.anno_gestione  "
				+ "end as anno " + "from greg_r_rendicontazione_preg1_macro r  "
				+ "left join greg_r_prest_reg1_macroaggregati_bilancio pm on r.id_prest_reg1_macroaggregati_bilancio = pm.id_prest_reg1_macroaggregati_bilancio   "
				+ "left join greg_t_prestazioni_regionali_1 p on pm.id_prest_reg_1 = p.id_prest_reg_1   "
				+ "left join greg_t_macroaggregati_bilancio m on pm.id_macroaggregato_bilancio = m.id_macroaggregato_bilancio   "
				+ "left join greg_t_rendicontazione_ente g on g.id_rendicontazione_ente = r.id_rendicontazione_ente  "
				+ "where  r.valore is not null and pm.data_cancellazione is null and p.id_prest_reg_1 = :idPrestazione   "
				+ "and m.id_macroaggregato_bilancio = :idMacro and p.data_cancellazione is null and m.data_cancellazione is null "
				+ "group by g.anno_gestione) " + "select max(s.anno) " + "from s");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idMacro", idMacro);

		Integer result = (Integer) query.getSingleResult();

		Timestamp dataMax = null;
		if (result != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/" + (result + 1));
			dataMax = new Timestamp(date.getTime());
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/2000");
			dataMax = new Timestamp(date.getTime());
		}

		return dataMax;
	}

	public GregRPrestReg1MacroaggregatiBilancio findRMacroaggregatiByPrestRegionale(Integer idPrestazione,
			Integer idMacro) {
		try {
			String hqlQuery = "SELECT pm FROM GregTMacroaggregatiBilancio m "
					+ "LEFT JOIN GregRPrestReg1MacroaggregatiBilancio pm on m.idMacroaggregatoBilancio = pm.gregTMacroaggregatiBilancio.idMacroaggregatoBilancio "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on pm.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE p.idPrestReg1 = :idPrestazione " + "AND m.idMacroaggregatoBilancio = :idMacro "
					+ "AND p.dataCancellazione IS NULL " + "AND pm.dataCancellazione IS NULL "
					+ "AND m.dataCancellazione IS NULL " + "Order by m.codMacroaggregatoBilancio";
			TypedQuery<GregRPrestReg1MacroaggregatiBilancio> query = em.createQuery(hqlQuery,
					GregRPrestReg1MacroaggregatiBilancio.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idMacro", idMacro);
			GregRPrestReg1MacroaggregatiBilancio macro = query.getSingleResult();
			return macro;
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPrestReg1MacroaggregatiBilancio findRMacroaggregatiByPrestRegionaleAndDataInizio(Integer idPrestazione,
			Integer idMacro, Date dataInizio, Date dataCreazione) {
		try {
			String hqlQuery = "SELECT pm FROM GregTMacroaggregatiBilancio m "
					+ "LEFT JOIN GregRPrestReg1MacroaggregatiBilancio pm on m.idMacroaggregatoBilancio = pm.gregTMacroaggregatiBilancio.idMacroaggregatoBilancio "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on pm.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE p.idPrestReg1 = :idPrestazione " + "AND m.idMacroaggregatoBilancio = :idMacro "
					+ "And DATE_TRUNC('second', pm.dataInizioValidita) = DATE_TRUNC('second', cast(:dataInizio as timestamp)) "
					+ "And DATE_TRUNC('second', pm.dataCreazione) = DATE_TRUNC('second', cast(:dataCreazione as timestamp)) "
					+ "AND p.dataCancellazione IS NULL " + "AND pm.dataCancellazione IS NULL "
					+ "AND m.dataCancellazione IS NULL " + "Order by m.codMacroaggregatoBilancio";
			TypedQuery<GregRPrestReg1MacroaggregatiBilancio> query = em.createQuery(hqlQuery,
					GregRPrestReg1MacroaggregatiBilancio.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idMacro", idMacro);
			query.setParameter("dataInizio", dataInizio);
			query.setParameter("dataCreazione", dataCreazione);
			GregRPrestReg1MacroaggregatiBilancio macro = query.getSingleResult();
			return macro;
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<GregRPrestReg1UtenzeRegionali1> findTargetUtenzeByPrestRegionale(Integer idPrestazione) {
		String hqlQuery = "SELECT pu FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRPrestReg1UtenzeRegionali1 pu on u.idTargetUtenza = pu.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p on pu.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
				+ "WHERE p.idPrestReg1 = :idPrestazione " + "AND p.dataCancellazione IS NULL "
				+ "AND pu.dataCancellazione IS NULL " + "AND u.dataCancellazione IS NULL " + "Order by u.codUtenza";
		TypedQuery<GregRPrestReg1UtenzeRegionali1> query = em.createQuery(hqlQuery,
				GregRPrestReg1UtenzeRegionali1.class);
		query.setParameter("idPrestazione", idPrestazione);
		return query.getResultList();
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissione findProgrammaMissioneByPrestRegionaleUtenza(
			Integer idPrestazione, Integer idUtenza, Date dataInizio) {
		try {
			String hqlQuery = "SELECT pupm FROM GregDProgrammaMissione pm "
					+ "LEFT JOIN GregRPrestReg1UtenzeRegionali1ProgrammaMissione pupm on pm.idProgrammaMissione = pupm.gregDProgrammaMissione.idProgrammaMissione "
					+ "LEFT JOIN GregDTargetUtenza u on u.idTargetUtenza = pupm.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on pupm.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE p.idPrestReg1 = :idPrestazione " + "AND u.idTargetUtenza = :idUtenza "
					+ "AND p.dataCancellazione IS NULL " + "AND pm.dataCancellazione IS NULL "
					+ "AND pupm.dataCancellazione IS NULL " + "AND pupm.dataInizioValidita = :dataInizio "
					+ "AND u.dataCancellazione IS NULL " + "Order by pm.siglaProgrammaMissione";
			TypedQuery<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> query = em.createQuery(hqlQuery,
					GregRPrestReg1UtenzeRegionali1ProgrammaMissione.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idUtenza", idUtenza);
			query.setParameter("dataInizio", dataInizio);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissione findRProgrammaMissioneByPrestRegionaleUtenza(
			Integer idRelazione) {
		try {
			String hqlQuery = "SELECT pupm FROM GregDProgrammaMissione pm "
					+ "LEFT JOIN GregRPrestReg1UtenzeRegionali1ProgrammaMissione pupm on pm.idProgrammaMissione = pupm.gregDProgrammaMissione.idProgrammaMissione "
					+ "LEFT JOIN GregDTargetUtenza u on u.idTargetUtenza = pupm.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on pupm.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE pupm.idPrestReg1UtenzeRegionali1ProgrammaMissione = :idRelazione "
					+ "AND p.dataCancellazione IS NULL " + "AND pm.dataCancellazione IS NULL "
					+ "AND pupm.dataCancellazione IS NULL " + "AND u.dataCancellazione IS NULL "
					+ "Order by pm.siglaProgrammaMissione";
			TypedQuery<GregRPrestReg1UtenzeRegionali1ProgrammaMissione> query = em.createQuery(hqlQuery,
					GregRPrestReg1UtenzeRegionali1ProgrammaMissione.class);
			query.setParameter("idRelazione", idRelazione);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public GregRTipologiaSpesaPrestReg1 findTipologiaSpesaByPrestRegionaleUtenza(Integer idPrestazione,
			Integer idUtenza, Date dataInizio) {
		try {
			String hqlQuery = "SELECT tspu FROM GregDTipologiaSpesa ts "
					+ "LEFT JOIN GregRTipologiaSpesaPrestReg1 tspu on ts.idTipologiaSpesa = tspu.gregDTipologiaSpesa.idTipologiaSpesa "
					+ "LEFT JOIN GregDTargetUtenza u on u.idTargetUtenza = tspu.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on tspu.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE p.idPrestReg1 = :idPrestazione " + "AND u.idTargetUtenza = :idUtenza "
					+ "AND p.dataCancellazione IS NULL " + "AND ts.dataCancellazione IS NULL "
					+ "AND tspu.dataCancellazione IS NULL " + "AND tspu.dataInizioValidita = :dataInizio "
					+ "AND tspu.gregRPrestReg1UtenzeRegionali1.dataCancellazione IS NULL "
					+ "AND u.dataCancellazione IS NULL "

					+ "Order by ts.codTipologiaSpesa";
			TypedQuery<GregRTipologiaSpesaPrestReg1> query = em.createQuery(hqlQuery,
					GregRTipologiaSpesaPrestReg1.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idUtenza", idUtenza);
			query.setParameter("dataInizio", dataInizio);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public GregRTipologiaSpesaPrestReg1 findTipologiaSpesaById(Integer idRelazione) {
		try {
			String hqlQuery = "SELECT tspu FROM GregDTipologiaSpesa ts "
					+ "LEFT JOIN GregRTipologiaSpesaPrestReg1 tspu on ts.idTipologiaSpesa = tspu.gregDTipologiaSpesa.idTipologiaSpesa "
					+ "LEFT JOIN GregDTargetUtenza u on u.idTargetUtenza = tspu.gregRPrestReg1UtenzeRegionali1.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on tspu.gregRPrestReg1UtenzeRegionali1.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE tspu.idTipologiaSpesaPrestReg1 = :idRelazione " + "AND p.dataCancellazione IS NULL "
					+ "AND ts.dataCancellazione IS NULL " + "AND tspu.dataCancellazione IS NULL "
					+ "AND tspu.gregRPrestReg1UtenzeRegionali1.dataCancellazione IS NULL "
					+ "AND u.dataCancellazione IS NULL "

					+ "Order by ts.codTipologiaSpesa";
			TypedQuery<GregRTipologiaSpesaPrestReg1> query = em.createQuery(hqlQuery,
					GregRTipologiaSpesaPrestReg1.class);
			query.setParameter("idRelazione", idRelazione);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<ModelPrestUtenza> findUtenzeByPrestRegionale(Integer idPrestazione) throws ParseException {
		List<GregRPrestReg1UtenzeRegionali1> targetUtenze = findTargetUtenzeByPrestRegionale(idPrestazione);

		List<ModelPrestUtenza> utenze = new ArrayList<ModelPrestUtenza>();
		for (GregRPrestReg1UtenzeRegionali1 u : targetUtenze) {
			ModelPrestUtenza utenza = new ModelPrestUtenza();
			utenza.setIdUtenza(u.getGregDTargetUtenza().getIdTargetUtenza());
			utenza.setCodUtenza(u.getGregDTargetUtenza().getCodUtenza());
			utenza.setDescUtenza(u.getGregDTargetUtenza().getDesUtenza());
			utenza.setDal(u.getDataInizioValidita());
			utenza.setAl(u.getDataFineValidita());
			utenza.setDataCreazione(u.getDataCreazione());
			Timestamp data = getAnnoUtenzaPrestazioneValorizzata(idPrestazione,
					u.getGregDTargetUtenza().getIdTargetUtenza());
			utenza.setDataMin(data.compareTo(utenza.getDal()) > 0 ? data : utenza.getDal());
			utenza.setModificabile(
					getUtenzaPrestazioneValorizzata(idPrestazione, u.getGregDTargetUtenza().getIdTargetUtenza()));
			GregRPrestReg1UtenzeRegionali1ProgrammaMissione programma = findProgrammaMissioneByPrestRegionaleUtenza(
					idPrestazione, u.getGregDTargetUtenza().getIdTargetUtenza(), u.getDataInizioValidita());
			if (programma != null) {
				utenza.setIdMissioneProgrammaRelazione(programma.getIdPrestReg1UtenzeRegionali1ProgrammaMissione());
				utenza.setCodMissioneProgramma(programma.getGregDProgrammaMissione().getSiglaProgrammaMissione());
				utenza.setDescMissioneProgramma(programma.getGregDProgrammaMissione().getInformativa());
				utenza.setColoreMissioneProgramma(programma.getGregDProgrammaMissione().getGregDColori().getRgb());
			} else {
				utenza.setCodMissioneProgramma("Vuoto");
			}

			GregRTipologiaSpesaPrestReg1 spesa = findTipologiaSpesaByPrestRegionaleUtenza(idPrestazione,
					u.getGregDTargetUtenza().getIdTargetUtenza(), u.getDataInizioValidita());
			if (spesa != null) {
				utenza.setIdTipologiaSpesaRelazione(spesa.getIdTipologiaSpesaPrestReg1());
				utenza.setCodTipologiaSpesa(spesa.getGregDTipologiaSpesa().getCodTipologiaSpesa());
				utenza.setDescTipologiaSpesa(spesa.getGregDTipologiaSpesa().getDesTipologiaSpesa());

			} else {
				utenza.setCodTipologiaSpesa("Vuoto");
			}
			utenze.add(utenza);
		}
		return utenze;
	}

	public GregRPrestReg1UtenzeRegionali1 findRUtenzeByPrestRegionale(Integer idPrestazione, Integer idUtenza) {
		try {
			String hqlQuery = "SELECT pu FROM GregDTargetUtenza u "
					+ "LEFT JOIN GregRPrestReg1UtenzeRegionali1 pu on u.idTargetUtenza = pu.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on pu.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE p.idPrestReg1 = :idPrestazione " + "AND p.dataCancellazione IS NULL "
					+ "AND pu.dataCancellazione IS NULL " + "AND u.dataCancellazione IS NULL "
					+ "AND u.idTargetUtenza=:idUtenza " + "Order by u.codUtenza";
			TypedQuery<GregRPrestReg1UtenzeRegionali1> query = em.createQuery(hqlQuery,
					GregRPrestReg1UtenzeRegionali1.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idUtenza", idUtenza);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPrestReg1UtenzeRegionali1 findRUtenzeByPrestRegionaleDataInizio(Integer idPrestazione, Integer idUtenza,
			Date dataInizio, Date dataCreazione) {
		try {
			String hqlQuery = "SELECT pu FROM GregDTargetUtenza u "
					+ "LEFT JOIN GregRPrestReg1UtenzeRegionali1 pu on u.idTargetUtenza = pu.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p on pu.gregTPrestazioniRegionali1.idPrestReg1 = p.idPrestReg1 "
					+ "WHERE p.idPrestReg1 = :idPrestazione "
					+ "And DATE_TRUNC('second', pu.dataInizioValidita) = DATE_TRUNC('second', cast(:dataInizio as timestamp)) "
					+ "And DATE_TRUNC('second', pu.dataCreazione) = DATE_TRUNC('second', cast(:dataCreazione as timestamp)) "
					+ "AND p.dataCancellazione IS NULL " + "AND pu.dataCancellazione IS NULL "
					+ "AND u.dataCancellazione IS NULL " + "AND u.idTargetUtenza=:idUtenza " + "Order by u.codUtenza";
			TypedQuery<GregRPrestReg1UtenzeRegionali1> query = em.createQuery(hqlQuery,
					GregRPrestReg1UtenzeRegionali1.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idUtenza", idUtenza);
			query.setParameter("dataInizio", dataInizio);
			query.setParameter("dataCreazione", dataCreazione);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<GregRPrestReg1PrestReg2> findPrestazione2ByPrestRegionale(Integer idPrestazione) {
		String hqlQuery = "SELECT p12 FROM GregTPrestazioniRegionali2 p2 "
				+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p12.dataCancellazione IS NULL "
				+ "AND p2.dataCancellazione IS NULL " + "Order by p2.codPrestReg2";
		TypedQuery<GregRPrestReg1PrestReg2> query = em.createQuery(hqlQuery, GregRPrestReg1PrestReg2.class);
		query.setParameter("idPrestazione", idPrestazione);
		return query.getResultList();
	}

	public List<GregRPrestReg2UtenzeRegionali2> findUtenzaByPrest2(Integer idPrestazione, Integer idPrestazione2) {
		String hqlQuery = "SELECT p2u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRPrestReg2UtenzeRegionali2 p2u on u.idTargetUtenza = p2u.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2u.gregTPrestazioniRegionali2.idPrestReg2 "
//				+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
//				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE "
//				+ "p1.idPrestReg1 = :idPrestazione " 
//				+ "AND "
				+ "p2.idPrestReg2 = :idPrestazione2 "
//				+ "AND p1.dataCancellazione IS NULL " 
				+ "AND u.dataCancellazione IS NULL " + "AND p2u.dataCancellazione IS NULL "
				+ "AND p2.dataCancellazione IS NULL "
//				+ "AND p12.dataCancellazione IS NULL " 
				+ "Order by u.codUtenza";
		TypedQuery<GregRPrestReg2UtenzeRegionali2> query = em.createQuery(hqlQuery,
				GregRPrestReg2UtenzeRegionali2.class);
//		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPrestazione2", idPrestazione2);
		return query.getResultList();
	}

	public List<GregRPrestReg2UtenzeRegionali2> findUtenzaRegByPrest2(Integer idPrestazione2) {
		String hqlQuery = "SELECT p2u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRPrestReg2UtenzeRegionali2 p2u on u.idTargetUtenza = p2u.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2u.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "WHERE p2.idPrestReg2 = :idPrestazione2 " + "AND u.dataCancellazione IS NULL "
				+ "AND p2u.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL " + "Order by u.codUtenza";
		TypedQuery<GregRPrestReg2UtenzeRegionali2> query = em.createQuery(hqlQuery,
				GregRPrestReg2UtenzeRegionali2.class);
		query.setParameter("idPrestazione2", idPrestazione2);
		return query.getResultList();
	}

	public GregRPrestReg2UtenzeRegionali2 findRUtenzaByPrest2(Integer idPrestazione, Integer idPrestazione2,
			Integer idUtenza) {
		String hqlQuery = "SELECT p2u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRPrestReg2UtenzeRegionali2 p2u on u.idTargetUtenza = p2u.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2u.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p2.idPrestReg2 = :idPrestazione2 "
				+ "AND u.idTargetUtenza = :idUtenza " + "AND p1.dataCancellazione IS NULL "
				+ "AND u.dataCancellazione IS NULL " + "AND p2u.dataCancellazione IS NULL "
				+ "AND p2.dataCancellazione IS NULL " + "AND p12.dataCancellazione IS NULL " + "Order by u.codUtenza";
		TypedQuery<GregRPrestReg2UtenzeRegionali2> query = em.createQuery(hqlQuery,
				GregRPrestReg2UtenzeRegionali2.class);
		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPrestazione2", idPrestazione2);
		query.setParameter("idUtenza", idUtenza);
		return query.getSingleResult();
	}

	public GregRPrestReg2UtenzeRegionali2 findRUtenzaRegByPrest2(Integer idPrestazione2, Integer idUtenza) {
		try {
			String hqlQuery = "SELECT p2u FROM GregDTargetUtenza u "
					+ "LEFT JOIN GregRPrestReg2UtenzeRegionali2 p2u on u.idTargetUtenza = p2u.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2u.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "WHERE  p2.idPrestReg2 = :idPrestazione2 " + "AND u.idTargetUtenza = :idUtenza "
					+ "AND u.dataCancellazione IS NULL " + "AND p2u.dataCancellazione IS NULL "
					+ "AND p2.dataCancellazione IS NULL " + "Order by u.codUtenza";
			TypedQuery<GregRPrestReg2UtenzeRegionali2> query = em.createQuery(hqlQuery,
					GregRPrestReg2UtenzeRegionali2.class);
			query.setParameter("idPrestazione2", idPrestazione2);
			query.setParameter("idUtenza", idUtenza);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPrestReg2UtenzeRegionali2 findRUtenzaRegByPrest2DataInizo(Integer idPrestazione2, Integer idUtenza,
			Date dataInizio) {
		try {
			String hqlQuery = "SELECT p2u FROM GregDTargetUtenza u "
					+ "LEFT JOIN GregRPrestReg2UtenzeRegionali2 p2u on u.idTargetUtenza = p2u.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2u.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "WHERE  p2.idPrestReg2 = :idPrestazione2 " + "AND u.idTargetUtenza = :idUtenza "
					+ "AND u.dataCancellazione IS NULL " + "AND p2u.dataCancellazione IS NULL "
					+ "And p2u.dataInizioValidita = :dataInizio " + "AND p2.dataCancellazione IS NULL "
					+ "Order by u.codUtenza";
			TypedQuery<GregRPrestReg2UtenzeRegionali2> query = em.createQuery(hqlQuery,
					GregRPrestReg2UtenzeRegionali2.class);
			query.setParameter("idPrestazione2", idPrestazione2);
			query.setParameter("idUtenza", idUtenza);
			query.setParameter("dataInizio", dataInizio);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<GregDVoceIstat> findPrestMinisterialeByPrest2(Integer idPrestazione, Integer idPrestazione2,
			Timestamp dataInizio) {
		try {
			String hqlQuery = "SELECT DISTINCT vi FROM GregDVoceIstat vi "
					+ "LEFT JOIN GregRCatUteVocePrestReg2Istat p2ui on vi.idVoceIstat = p2ui.gregDVoceIstat.idVoceIstat "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p2.idPrestReg2 = :idPrestazione2 "
					+ "AND p1.dataCancellazione IS NULL " + "AND vi.dataCancellazione IS NULL "
					+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
					+ "AND p12.dataCancellazione IS NULL " + "AND p12.dataInizioValidita = :dataInizio ";
			TypedQuery<GregDVoceIstat> query = em.createQuery(hqlQuery, GregDVoceIstat.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idPrestazione2", idPrestazione2);
			query.setParameter("dataInizio", dataInizio);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<GregDVoceIstat> findMinisterialeByPrest2(Integer idPrestazione2) {
		try {
			String hqlQuery = "SELECT DISTINCT vi FROM GregDVoceIstat vi "
					+ "LEFT JOIN GregRCatUteVocePrestReg2Istat p2ui on vi.idVoceIstat = p2ui.gregDVoceIstat.idVoceIstat "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "WHERE p2.idPrestReg2 = :idPrestazione2 " + "AND vi.dataCancellazione IS NULL "
					+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL ";
			TypedQuery<GregDVoceIstat> query = em.createQuery(hqlQuery, GregDVoceIstat.class);
			query.setParameter("idPrestazione2", idPrestazione2);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<GregDTargetUtenza> findUtenzeMinisterialeByPrest2Istat(Integer idPrestazione, Integer idPrestazione2,
			Integer idIstat, Timestamp dataInizio) {
		String hqlQuery = "SELECT u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRCatUteVocePrestReg2Istat p2ui on u.idTargetUtenza = p2ui.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregDVoceIstat vi on vi.idVoceIstat = p2ui.gregDVoceIstat.idVoceIstat "
				+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p2.idPrestReg2 = :idPrestazione2 "
				+ "AND vi.idVoceIstat = :idIstat " + "AND p1.dataCancellazione IS NULL "
				+ "AND u.dataCancellazione IS NULL " + "AND p2ui.dataCancellazione IS NULL "
				+ "AND p2.dataCancellazione IS NULL " + "AND vi.dataCancellazione IS NULL "
				+ "AND p12.dataCancellazione IS NULL " + "AND p12.dataInizioValidita = :dataInizio "
				+ "Order by u.desUtenza";
		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPrestazione2", idPrestazione2);
		query.setParameter("idIstat", idIstat);
		query.setParameter("dataInizio", dataInizio);
		return query.getResultList();
	}

	public List<GregDTargetUtenza> findUtenzeMinisByPrest2Istat(Integer idPrestazione2, Integer idIstat) {
		String hqlQuery = "SELECT u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRCatUteVocePrestReg2Istat p2ui on u.idTargetUtenza = p2ui.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregDVoceIstat vi on vi.idVoceIstat = p2ui.gregDVoceIstat.idVoceIstat "
				+ "WHERE p2.idPrestReg2 = :idPrestazione2 " + "AND vi.idVoceIstat = :idIstat "
				+ "AND u.dataCancellazione IS NULL " + "AND p2ui.dataCancellazione IS NULL "
				+ "AND p2.dataCancellazione IS NULL " + "AND vi.dataCancellazione IS NULL " + "Order by u.desUtenza";
		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("idPrestazione2", idPrestazione2);
		query.setParameter("idIstat", idIstat);
		return query.getResultList();
	}

	public List<GregTNomenclatoreNazionale> findNomenclatoreByPrest2Istat(Integer idPrestazione,
			Integer idPrestazione2) {
		try {
			String hqlQuery = "SELECT u FROM GregTNomenclatoreNazionale u "
					+ "LEFT JOIN GregRNomencNazPrestReg2 p2ui on u.idNomenclatoreNazionale = p2ui.gregTNomenclatoreNazionale.idNomenclatoreNazionale "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p2.idPrestReg2 = :idPrestazione2 "
					+ "AND p1.dataCancellazione IS NULL " + "AND u.dataCancellazione IS NULL "
					+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
					+ "AND p12.dataCancellazione IS NULL " + "Order by u.codNomenclatoreNazionale";
			TypedQuery<GregTNomenclatoreNazionale> query = em.createQuery(hqlQuery, GregTNomenclatoreNazionale.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idPrestazione2", idPrestazione2);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<GregRNomencNazPrestReg2> findNomenByPrest2Istat(Integer idPrestazione2) {
		try {
			String hqlQuery = "SELECT p2ui FROM GregTNomenclatoreNazionale u "
					+ "LEFT JOIN GregRNomencNazPrestReg2 p2ui on u.idNomenclatoreNazionale = p2ui.gregTNomenclatoreNazionale.idNomenclatoreNazionale "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "WHERE p2.idPrestReg2 = :idPrestazione2 " + "AND u.dataCancellazione IS NULL "
					+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
					+ "Order by u.codNomenclatoreNazionale";
			TypedQuery<GregRNomencNazPrestReg2> query = em.createQuery(hqlQuery, GregRNomencNazPrestReg2.class);
			query.setParameter("idPrestazione2", idPrestazione2);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public GregRPrestReg1PrestReg2 findRPrestazione2ByPrestRegionale(Integer idPrestazione, Integer idPrestazione2) {
		try {
			String hqlQuery = "SELECT p12 FROM GregTPrestazioniRegionali2 p2 "
					+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p12.dataCancellazione IS NULL "
					+ "AND p2.dataCancellazione IS NULL " + "AND p2.idPrestReg2 = :idPrestazione2 "
					+ "Order by p2.codPrestReg2";
			TypedQuery<GregRPrestReg1PrestReg2> query = em.createQuery(hqlQuery, GregRPrestReg1PrestReg2.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idPrestazione2", idPrestazione2);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPrestReg1PrestReg2 findRPrestazione2ByPrestRegionaleDataInizio(Integer idPrestazione,
			Integer idPrestazione2, Date dataInizio, Date dataCreazione) {
		try {
			String hqlQuery = "SELECT p12 FROM GregTPrestazioniRegionali2 p2 "
					+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p12.dataCancellazione IS NULL "
					+ "AND p2.dataCancellazione IS NULL " + "AND p2.idPrestReg2 = :idPrestazione2 "
					+ "And DATE_TRUNC('second', p12.dataInizioValidita) = DATE_TRUNC('second', cast(:dataInizio as timestamp)) "
					+ "And DATE_TRUNC('second', p12.dataCreazione) = DATE_TRUNC('second', cast(:dataCreazione as timestamp)) "
					+ "Order by p2.codPrestReg2";
			TypedQuery<GregRPrestReg1PrestReg2> query = em.createQuery(hqlQuery, GregRPrestReg1PrestReg2.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idPrestazione2", idPrestazione2);
			query.setParameter("dataInizio", dataInizio);
			query.setParameter("dataCreazione", dataCreazione);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRCatUteVocePrestReg2Istat findRUtenzeMinisterialeByPrest2Istat(Integer idPrestazione,
			Integer idPrestazione2, Integer idIstat, Integer idUtenza) {
		String hqlQuery = "SELECT p2ui FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRCatUteVocePrestReg2Istat p2ui on u.idTargetUtenza = p2ui.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregDVoceIstat vi on vi.idVoceIstat = p2ui.gregDVoceIstat.idVoceIstat "
				+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p2.idPrestReg2 = :idPrestazione2 "
				+ "AND vi.idVoceIstat = :idIstat " + "AND u.idTargetUtenza = :idUtenza "
				+ "AND p1.dataCancellazione IS NULL " + "AND u.dataCancellazione IS NULL "
				+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
				+ "AND vi.dataCancellazione IS NULL " + "AND p12.dataCancellazione IS NULL " + "Order by u.desUtenza";
		TypedQuery<GregRCatUteVocePrestReg2Istat> query = em.createQuery(hqlQuery, GregRCatUteVocePrestReg2Istat.class);
		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPrestazione2", idPrestazione2);
		query.setParameter("idIstat", idIstat);
		query.setParameter("idUtenza", idUtenza);
		return query.getSingleResult();
	}

	public GregRCatUteVocePrestReg2Istat findRUtenzeMinisByPrest2Istat(Integer idPrestazione2, Integer idIstat,
			Integer idUtenza) {
		try {
			String hqlQuery = "SELECT p2ui FROM GregDTargetUtenza u "
					+ "LEFT JOIN GregRCatUteVocePrestReg2Istat p2ui on u.idTargetUtenza = p2ui.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregDVoceIstat vi on vi.idVoceIstat = p2ui.gregDVoceIstat.idVoceIstat "
					+ "WHERE p2.idPrestReg2 = :idPrestazione2 " + "AND vi.idVoceIstat = :idIstat "
					+ "AND u.idTargetUtenza = :idUtenza " + "AND u.dataCancellazione IS NULL "
					+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
					+ "AND vi.dataCancellazione IS NULL " + "Order by u.desUtenza";
			TypedQuery<GregRCatUteVocePrestReg2Istat> query = em.createQuery(hqlQuery,
					GregRCatUteVocePrestReg2Istat.class);
			query.setParameter("idPrestazione2", idPrestazione2);
			query.setParameter("idIstat", idIstat);
			query.setParameter("idUtenza", idUtenza);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRCatUteVocePrestReg2Istat findRUtenzeMinisByPrest2IstatDataInizio(Integer idPrestazione2,
			Integer idIstat, Integer idUtenza, Date dataInizio, Date dataCreazione) {
		try {
			String hqlQuery = "SELECT p2ui FROM GregDTargetUtenza u "
					+ "LEFT JOIN GregRCatUteVocePrestReg2Istat p2ui on u.idTargetUtenza = p2ui.gregDTargetUtenza.idTargetUtenza "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregDVoceIstat vi on vi.idVoceIstat = p2ui.gregDVoceIstat.idVoceIstat "
					+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p2.idPrestReg2 = :idPrestazione2 " + "AND vi.idVoceIstat = :idIstat "
					+ "AND u.idTargetUtenza = :idUtenza " + "AND p1.dataCancellazione IS NULL "
					+ "AND u.dataCancellazione IS NULL " + "AND p2ui.dataCancellazione IS NULL "
					+ "AND p2.dataCancellazione IS NULL "
					+ "And DATE_TRUNC('second', p2ui.dataInizioValidita) = DATE_TRUNC('second', cast(:dataInizio as timestamp)) "
					+ "And DATE_TRUNC('second', p2ui.dataCreazione) = DATE_TRUNC('second', cast(:dataCreazione as timestamp)) "
					+ "AND vi.dataCancellazione IS NULL " + "AND p12.dataCancellazione IS NULL "
					+ "Order by u.desUtenza";
			TypedQuery<GregRCatUteVocePrestReg2Istat> query = em.createQuery(hqlQuery,
					GregRCatUteVocePrestReg2Istat.class);
			query.setParameter("idPrestazione2", idPrestazione2);
			query.setParameter("idIstat", idIstat);
			query.setParameter("idUtenza", idUtenza);
			query.setParameter("dataInizio", dataInizio);
			query.setParameter("dataCreazione", dataCreazione);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRNomencNazPrestReg2 findRNomenclatoreByPrest2Istat(Integer idPrestazione, Integer idPrestazione2,
			Integer idNomenclatoreNazionale) {
		try {
			String hqlQuery = "SELECT p2ui FROM GregTNomenclatoreNazionale u "
					+ "LEFT JOIN GregRNomencNazPrestReg2 p2ui on u.idNomenclatoreNazionale = p2ui.gregTNomenclatoreNazionale.idNomenclatoreNazionale "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregRPrestReg1PrestReg2 p12 on p2.idPrestReg2 = p12.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p12.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p2.idPrestReg2 = :idPrestazione2 "
					+ "AND u.idNomenclatoreNazionale = :idNomenclatoreNazionale " + "AND p1.dataCancellazione IS NULL "
					+ "AND u.dataCancellazione IS NULL " + "AND p2ui.dataCancellazione IS NULL "
					+ "AND p2.dataCancellazione IS NULL " + "AND p12.dataCancellazione IS NULL "
					+ "Order by u.codNomenclatoreNazionale";
			TypedQuery<GregRNomencNazPrestReg2> query = em.createQuery(hqlQuery, GregRNomencNazPrestReg2.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idPrestazione2", idPrestazione2);
			query.setParameter("idNomenclatoreNazionale", idNomenclatoreNazionale);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public GregRNomencNazPrestReg2 findRNomenByPrest2Istat(Integer idPrestazione2, Integer idNomenclatoreNazionale) {
		try {
			String hqlQuery = "SELECT p2ui FROM GregTNomenclatoreNazionale u "
					+ "LEFT JOIN GregRNomencNazPrestReg2 p2ui on u.idNomenclatoreNazionale = p2ui.gregTNomenclatoreNazionale.idNomenclatoreNazionale "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "WHERE p2.idPrestReg2 = :idPrestazione2 "
					+ "AND u.idNomenclatoreNazionale = :idNomenclatoreNazionale " + "AND u.dataCancellazione IS NULL "
					+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
					+ "Order by u.codNomenclatoreNazionale";
			TypedQuery<GregRNomencNazPrestReg2> query = em.createQuery(hqlQuery, GregRNomencNazPrestReg2.class);
			query.setParameter("idPrestazione2", idPrestazione2);
			query.setParameter("idNomenclatoreNazionale", idNomenclatoreNazionale);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public GregRNomencNazPrestReg2 findRNomenByPrest2IstatDataInizio(Integer idPrestazione2,
			Integer idNomenclatoreNazionale, Date dataInizio) {
		try {
			String hqlQuery = "SELECT p2ui FROM GregTNomenclatoreNazionale u "
					+ "LEFT JOIN GregRNomencNazPrestReg2 p2ui on u.idNomenclatoreNazionale = p2ui.gregTNomenclatoreNazionale.idNomenclatoreNazionale "
					+ "LEFT JOIN GregTPrestazioniRegionali2 p2 on p2.idPrestReg2 = p2ui.gregTPrestazioniRegionali2.idPrestReg2 "
					+ "WHERE p2.idPrestReg2 = :idPrestazione2 "
					+ "AND u.idNomenclatoreNazionale = :idNomenclatoreNazionale " + "AND u.dataCancellazione IS NULL "
					+ "AND p2ui.dataCancellazione IS NULL " + "AND p2.dataCancellazione IS NULL "
					+ "And p2ui.dataInizioValidita = :dataInizio " + "Order by u.codNomenclatoreNazionale";
			TypedQuery<GregRNomencNazPrestReg2> query = em.createQuery(hqlQuery, GregRNomencNazPrestReg2.class);
			query.setParameter("idPrestazione2", idPrestazione2);
			query.setParameter("idNomenclatoreNazionale", idNomenclatoreNazionale);
			query.setParameter("dataInizio", dataInizio);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<ModelPrest1Prest2> findPrest2ByPrest1(Integer idPrestazione) throws ParseException {
		List<GregRPrestReg1PrestReg2> prest2 = findPrestazione2ByPrestRegionale(idPrestazione);

		List<ModelPrest1Prest2> prestazioni2 = new ArrayList<ModelPrest1Prest2>();
		for (GregRPrestReg1PrestReg2 p2 : prest2) {
			ModelPrest1Prest2 prestazione2 = new ModelPrest1Prest2();
			prestazione2.setIdPrest2(p2.getGregTPrestazioniRegionali2().getIdPrestReg2());
			prestazione2.setCodPrest2(p2.getGregTPrestazioniRegionali2().getCodPrestReg2());
			prestazione2.setDescPrest2(p2.getGregTPrestazioniRegionali2().getDesPrestReg2());
			prestazione2.setIdTipologia(p2.getGregTPrestazioniRegionali2().getGregDTipologia() != null
					? p2.getGregTPrestazioniRegionali2().getGregDTipologia().getIdTipologia()
					: null);
			prestazione2.setCodTipologia(p2.getGregTPrestazioniRegionali2().getGregDTipologia() != null
					? p2.getGregTPrestazioniRegionali2().getGregDTipologia().getCodTipologia()
					: null);
			prestazione2.setOrdinamento(p2.getGregTPrestazioniRegionali2().getOrdinamento());
			prestazione2.setDal(p2.getGregTPrestazioniRegionali2().getDataInizioValidita());
			prestazione2.setAl(p2.getGregTPrestazioniRegionali2().getDataFineValidita());
			prestazione2.setDalRelazione(p2.getDataInizioValidita());
			prestazione2.setAlRelazione(p2.getDataFineValidita());
			prestazione2.setDataCreazione(p2.getDataCreazione());
			prestazione2.setNota(p2.getGregTPrestazioniRegionali2().getInformativa());
			Timestamp data = getAnnoPrestazione2Valorizzata(idPrestazione,
					p2.getGregTPrestazioniRegionali2().getIdPrestReg2());
			prestazione2.setDataMin(
					data.compareTo(prestazione2.getDalRelazione()) > 0 ? data : prestazione2.getDalRelazione());
			prestazione2.setModificabile(
					getPrestazione2Valorizzata(idPrestazione, p2.getGregTPrestazioniRegionali2().getIdPrestReg2()));
			List<GregRPrestReg2UtenzeRegionali2> targetUtenze = findUtenzaByPrest2(idPrestazione,
					p2.getGregTPrestazioniRegionali2().getIdPrestReg2());
			List<ModelPrestUtenza> utenze = new ArrayList<ModelPrestUtenza>();
			for (GregRPrestReg2UtenzeRegionali2 u : targetUtenze) {
				ModelPrestUtenza ute = new ModelPrestUtenza();
				ute.setIdUtenza(u.getGregDTargetUtenza().getIdTargetUtenza());
				ute.setCodUtenza(u.getGregDTargetUtenza().getCodUtenza());
				ute.setDescUtenza(u.getGregDTargetUtenza().getDesUtenza());
				ute.setModificabile(getPrestazione2UtenzaValorizzata(idPrestazione,
						p2.getGregTPrestazioniRegionali2().getIdPrestReg2(),
						u.getGregDTargetUtenza().getIdTargetUtenza()));
				Timestamp dataU = getAnnoPrestazione2UtenzaValorizzata(idPrestazione,
						p2.getGregTPrestazioniRegionali2().getIdPrestReg2(),
						u.getGregDTargetUtenza().getIdTargetUtenza());

				ute.setDal(u.getDataInizioValidita());
				ute.setAl(u.getDataFineValidita());
				ute.setDataCreazione(u.getDataCreazione());
				ute.setDataMin(dataU.compareTo(ute.getDal()) > 0 ? dataU : ute.getDal());
				utenze.add(ute);
			}
			prestazione2.setUtenzeConf(utenze);
			List<GregDVoceIstat> voce = findPrestMinisterialeByPrest2(idPrestazione,
					p2.getGregTPrestazioniRegionali2().getIdPrestReg2(), p2.getDataInizioValidita());
			if (voce != null) {
				List<ModelPrest2PrestIstat> i = new ArrayList<ModelPrest2PrestIstat>();
				for (GregDVoceIstat v : voce) {
					ModelPrest2PrestIstat istat = new ModelPrest2PrestIstat();
					istat.setIdPrestIstat(v.getIdVoceIstat());
					istat.setCodPrestIstat(v.getCodVoceIstat());
					istat.setDescPrestIstat(v.getDescVoceIstat());
					List<GregDTargetUtenza> utenzeMin = findUtenzeMinisterialeByPrest2Istat(idPrestazione,
							p2.getGregTPrestazioniRegionali2().getIdPrestReg2(), v.getIdVoceIstat(),
							p2.getDataInizioValidita());
					List<ModelPrestUtenza> utenzeM = new ArrayList<ModelPrestUtenza>();
					for (GregDTargetUtenza uM : utenzeMin) {
						ModelPrestUtenza u = new ModelPrestUtenza();
						u.setIdUtenza(uM.getIdTargetUtenza());
						u.setCodUtenza(uM.getCodUtenza());
						u.setDescUtenza(uM.getDesUtenza());
						GregRCatUteVocePrestReg2Istat ru = findRUtenzeMinisterialeByPrest2Istat(idPrestazione,
								p2.getGregTPrestazioniRegionali2().getIdPrestReg2(), v.getIdVoceIstat(),
								uM.getIdTargetUtenza());
						u.setDal(ru.getDataInizioValidita());
						u.setAl(ru.getDataFineValidita());
						u.setDataCreazione(ru.getDataCreazione());
						utenzeM.add(u);
					}
					istat.setUtenzeMinConf(utenzeM);
					i.add(istat);
				}
				prestazione2.setPrestIstat(i);
			}
			List<GregTNomenclatoreNazionale> nomenclatore = findNomenclatoreByPrest2Istat(idPrestazione,
					p2.getGregTPrestazioniRegionali2().getIdPrestReg2());

			if (nomenclatore != null) {
				List<ModelNomenclatore> nom2013 = new ArrayList<ModelNomenclatore>();
				for (GregTNomenclatoreNazionale nom : nomenclatore) {
					ModelNomenclatore n = new ModelNomenclatore();
					n.setIdNomenclatore(nom.getIdNomenclatoreNazionale());
					n.setCodClassificazionePresidio(nom.getGregDClassificazionePresidio() != null
							? nom.getGregDClassificazionePresidio().getCodClassificazionePresidio()
							: " ");
					n.setDescClassificazionePresidio(nom.getGregDClassificazionePresidio() != null
							? nom.getGregDClassificazionePresidio().getDesClassificazionePresidio()
							: " ");
					n.setCodFunzionePresidio(nom.getGregDFunzionePresidio() != null
							? nom.getGregDFunzionePresidio().getCodFunzionePresidio()
							: " ");
					n.setDescFunzionePresidio(nom.getGregDFunzionePresidio() != null
							? nom.getGregDFunzionePresidio().getDesFunzionePresidio()
							: " ");
					n.setCodMacroArea(
							nom.getGregDMacroaree() != null ? nom.getGregDMacroaree().getCodMacroarea() : " ");
					n.setDescMacroArea(
							nom.getGregDMacroaree() != null ? nom.getGregDMacroaree().getDesMacroarea() : " ");
					n.setCodPresidio(
							nom.getGregDTipoPresidio() != null ? nom.getGregDTipoPresidio().getCodTipoPresidio() : " ");
					n.setDescPresidio(
							nom.getGregDTipoPresidio() != null ? nom.getGregDTipoPresidio().getDesTipoPresidio() : " ");
					n.setCodSottoArea(
							nom.getGregDSottoaree() != null ? nom.getGregDSottoaree().getCodSottoarea() : " ");
					n.setDescSottoArea(
							nom.getGregDSottoaree() != null ? nom.getGregDSottoaree().getDesSottoarea() : " ");
					n.setCodSottoAreaDet(
							nom.getGregDSottoareeDet() != null ? nom.getGregDSottoareeDet().getCodSottoareaDet() : " ");
					n.setDescSottoAreaDet(
							nom.getGregDSottoareeDet() != null ? nom.getGregDSottoareeDet().getDesSottoareaDet() : " ");
					n.setCodSottoVoce(
							nom.getGregDSottovoci() != null ? nom.getGregDSottovoci().getCodSottovoce() : " ");
					n.setDescSottoVoce(
							nom.getGregDSottovoci() != null ? nom.getGregDSottovoci().getDesSottovoce() : " ");
					n.setCodTipoResidenza(
							nom.getGregDTipoResidenza() != null ? nom.getGregDTipoResidenza().getCodTipoResidenza()
									: " ");
					n.setDescTipoResidenza(
							nom.getGregDTipoResidenza() != null ? nom.getGregDTipoResidenza().getDesTipoResidenza()
									: " ");
					n.setCodVoce(nom.getGregDVoci() != null ? nom.getGregDVoci().getCodVoce() : " ");
					n.setDescVoce(nom.getGregDVoci() != null ? nom.getGregDVoci().getDesVoce() : " ");

					GregRNomencNazPrestReg2 rn = findRNomenclatoreByPrest2Istat(idPrestazione,
							p2.getGregTPrestazioniRegionali2().getIdPrestReg2(), nom.getIdNomenclatoreNazionale());
					n.setDal(rn.getDataInizioValidita());
					n.setAl(rn.getDataFineValidita());
					n.setDataMin(rn.getDataInizioValidita());
					nom2013.add(n);
				}
				prestazione2.setNomenclatore(nom2013);
			}
			prestazioni2.add(prestazione2);
		}
		return prestazioni2;
	}

	public List<GregRPrestReg1PrestMinist> findPrestazioneMinByPrestRegionale(Integer idPrestazione) {
		try {
			String hqlQuery = "SELECT p1pm FROM GregTPrestazioniMinisteriali pm "
					+ "LEFT JOIN GregRPrestReg1PrestMinist p1pm on pm.idPrestazioneMinisteriale = p1pm.gregTPrestazioniMinisteriali.idPrestazioneMinisteriale "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p1pm.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p1.dataCancellazione IS NULL "
					+ "AND pm.dataCancellazione IS NULL " + "AND p1pm.dataCancellazione IS NULL "
					+ "Order by pm.codPrestazioneMinisteriale";
			TypedQuery<GregRPrestReg1PrestMinist> query = em.createQuery(hqlQuery, GregRPrestReg1PrestMinist.class);
			query.setParameter("idPrestazione", idPrestazione);
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<GregDTargetUtenza> findUtenzeMinisterialeByPrestMin(String codPrestazione1, String codPrestazioneMin) {
		String hqlQuery = "SELECT u FROM GregDTargetUtenza u "
				+ "LEFT JOIN GregRPrestMinistUtenzeMinist puu on u.idTargetUtenza = puu.gregDTargetUtenza.idTargetUtenza "
				+ "LEFT JOIN GregTPrestazioniMinisteriali pm on pm.idPrestazioneMinisteriale = puu.gregTPrestazioniMinisteriali.idPrestazioneMinisteriale "
				+ "LEFT JOIN GregRPrestReg1PrestMinist p1pm on pm.idPrestazioneMinisteriale = p1pm.gregTPrestazioniMinisteriali.idPrestazioneMinisteriale "
				+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p1pm.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
				+ "WHERE p1.codPrestReg1 = :codPrestazione1 "
				+ "AND pm.codPrestazioneMinisteriale = :codPrestazioneMin " + "AND p1.dataCancellazione IS NULL "
				+ "AND u.dataCancellazione IS NULL " + "AND puu.dataCancellazione IS NULL "
				+ "AND pm.dataCancellazione IS NULL " + "AND p1pm.dataCancellazione IS NULL " + "Order by u.codUtenza";
		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("codPrestazione1", codPrestazione1);
		query.setParameter("codPrestazioneMin", codPrestazioneMin);
		return query.getResultList();
	}

	public GregRPrestReg1PrestMinist findRPrestazioneMinByPrestRegionale(Integer idPrestazione,
			Integer idPrestazioneMinisteriale) {
		try {
			String hqlQuery = "SELECT p1pm FROM GregTPrestazioniMinisteriali pm "
					+ "LEFT JOIN GregRPrestReg1PrestMinist p1pm on pm.idPrestazioneMinisteriale = p1pm.gregTPrestazioniMinisteriali.idPrestazioneMinisteriale "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p1pm.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p1.dataCancellazione IS NULL "
					+ "AND pm.idPrestazioneMinisteriale = :idPrestazioneMinisteriale "
					+ "AND pm.dataCancellazione IS NULL " + "AND p1pm.dataCancellazione IS NULL "
					+ "Order by pm.codPrestazioneMinisteriale";
			TypedQuery<GregRPrestReg1PrestMinist> query = em.createQuery(hqlQuery, GregRPrestReg1PrestMinist.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idPrestazioneMinisteriale", idPrestazioneMinisteriale);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public GregRPrestReg1PrestMinist findRPrestazioneMinByPrestRegionaleDataInizio(Integer idPrestazione,
			Integer idPrestazioneMinisteriale, Date dataInizio, Date dataCreazione) {
		try {
			String hqlQuery = "SELECT p1pm FROM GregTPrestazioniMinisteriali pm "
					+ "LEFT JOIN GregRPrestReg1PrestMinist p1pm on pm.idPrestazioneMinisteriale = p1pm.gregTPrestazioniMinisteriali.idPrestazioneMinisteriale "
					+ "LEFT JOIN GregTPrestazioniRegionali1 p1 on p1pm.gregTPrestazioniRegionali1.idPrestReg1 = p1.idPrestReg1 "
					+ "WHERE p1.idPrestReg1 = :idPrestazione " + "AND p1.dataCancellazione IS NULL "
					+ "AND pm.idPrestazioneMinisteriale = :idPrestazioneMinisteriale "
					+ "AND pm.dataCancellazione IS NULL " + "AND p1pm.dataCancellazione IS NULL "
					+ "And DATE_TRUNC('second', p1pm.dataInizioValidita) = DATE_TRUNC('second', cast(:dataInizio as timestamp)) "
					+ "And DATE_TRUNC('second', p1pm.dataCreazione) = DATE_TRUNC('second', cast(:dataCreazione as timestamp)) "
					+ "Order by pm.codPrestazioneMinisteriale";
			TypedQuery<GregRPrestReg1PrestMinist> query = em.createQuery(hqlQuery, GregRPrestReg1PrestMinist.class);
			query.setParameter("idPrestazione", idPrestazione);
			query.setParameter("idPrestazioneMinisteriale", idPrestazioneMinisteriale);
			query.setParameter("dataInizio", dataInizio);
			query.setParameter("dataCreazione", dataCreazione);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public List<ModelPrest1PrestMin> findPrestMinByPrest1(Integer idPrestazione) throws ParseException {

		List<GregRPrestReg1PrestMinist> prestMin = findPrestazioneMinByPrestRegionale(idPrestazione);

		List<ModelPrest1PrestMin> pMin = new ArrayList<ModelPrest1PrestMin>();

		if (prestMin != null) {
			for (GregRPrestReg1PrestMinist p : prestMin) {
				ModelPrest1PrestMin prestazioniMin = new ModelPrest1PrestMin();
				prestazioniMin.setIdPrestMin(p.getGregTPrestazioniMinisteriali().getIdPrestazioneMinisteriale());
				prestazioniMin
						.setCodMacro(p.getGregTPrestazioniMinisteriali().getGregDMacroattivita().getCodMacroattivita());
				prestazioniMin.setDescMacro(
						p.getGregTPrestazioniMinisteriali().getGregDMacroattivita().getDescMacroattivita());
				prestazioniMin.setCodPrestMin(p.getGregTPrestazioniMinisteriali().getCodPrestazioneMinisteriale());
				prestazioniMin.setDescPrestMin(p.getGregTPrestazioniMinisteriali().getDescPrestazioneMinisteriale());
				prestazioniMin.setDal(p.getDataInizioValidita());
				prestazioniMin.setAl(p.getDataFineValidita());
				prestazioniMin.setDataCreazione(p.getDataCreazione());
				Timestamp data = getAnnoPrestazioneMinValorizzata(idPrestazione,
						p.getGregTPrestazioniMinisteriali().getIdPrestazioneMinisteriale());
				prestazioniMin.setDataMin(data.compareTo(prestazioniMin.getDal()) > 0 ? data : prestazioniMin.getDal());
				prestazioniMin.setModificabile(getPrestazioneMinValorizzata(idPrestazione,
						p.getGregTPrestazioniMinisteriali().getIdPrestazioneMinisteriale()));
				pMin.add(prestazioniMin);
			}
		}
		return pMin;
	}

	public List<GregTPrestazioniRegionali1> findPrestazioneFiglieById(Integer idPrestazione) {
		String hqlQuery = "SELECT g FROM GregTPrestazioniRegionali1 g " + "LEFT JOIN FETCH g.gregDTipoStruttura s "
				+ "LEFT JOIN FETCH g.gregDTipologia t " + "LEFT JOIN FETCH g.gregDTipologiaQuota tq "
				+ "LEFT JOIN FETCH g.gregTPrestazioniRegionali1 p1 "
				+ "WHERE g.gregTPrestazioniRegionali1.idPrestReg1 = :idPrestazione "
				+ "AND g.dataCancellazione IS NULL " + "AND s.dataCancellazione IS NULL "
				+ "AND t.dataCancellazione IS NULL " + "AND tq.dataCancellazione IS NULL "
				+ "AND p1.dataCancellazione IS NULL " + "Order By g.codPrestReg1";
		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);
		query.setParameter("idPrestazione", idPrestazione);
		return query.getResultList();
	}

	public List<ModelListeConfiguratore> findTipologie() {
		String hqlQuery = "select tip " + "from GregDTipologia tip " + "where tip.dataCancellazione is null "
				+ "order by tip.codTipologia ";

		TypedQuery<GregDTipologia> query = em.createQuery(hqlQuery, GregDTipologia.class);

		List<GregDTipologia> result = (ArrayList<GregDTipologia>) query.getResultList();

		List<ModelListeConfiguratore> tipologie = new ArrayList<ModelListeConfiguratore>();

		for (GregDTipologia tipologia : result) {
			ModelListeConfiguratore tip = new ModelListeConfiguratore();
			tip.setId(tipologia.getIdTipologia());
			tip.setCodice(tipologia.getCodTipologia());
			tip.setDescrizione(tipologia.getDesTipologia());
			tipologie.add(tip);
		}
		return tipologie;
	}

	public List<ModelListeConfiguratore> findPrestIstat() {
		String hqlQuery = "select tip " + "from GregDVoceIstat tip " + "where tip.dataCancellazione is null "
				+ "order by tip.codVoceIstat ";

		TypedQuery<GregDVoceIstat> query = em.createQuery(hqlQuery, GregDVoceIstat.class);

		List<GregDVoceIstat> result = (ArrayList<GregDVoceIstat>) query.getResultList();

		List<ModelListeConfiguratore> tipologie = new ArrayList<ModelListeConfiguratore>();

		for (GregDVoceIstat tipologia : result) {
			ModelListeConfiguratore tip = new ModelListeConfiguratore();
			tip.setId(tipologia.getIdVoceIstat());
			tip.setCodice(tipologia.getCodVoceIstat());
			tip.setDescrizione(tipologia.getDescVoceIstat());
			tipologie.add(tip);
		}
		return tipologie;
	}

	public List<ModelListeConfiguratore> findStrutture() {
		String hqlQuery = "select strutt " + "from GregDTipoStruttura strutt "
				+ "where strutt.dataCancellazione is null " + "order by strutt.codTipoStruttura ";

		TypedQuery<GregDTipoStruttura> query = em.createQuery(hqlQuery, GregDTipoStruttura.class);

		List<GregDTipoStruttura> result = (ArrayList<GregDTipoStruttura>) query.getResultList();

		List<ModelListeConfiguratore> strutture = new ArrayList<ModelListeConfiguratore>();

		for (GregDTipoStruttura struttura : result) {
			ModelListeConfiguratore strutt = new ModelListeConfiguratore();
			strutt.setId(struttura.getIdTipoStruttura());
			strutt.setCodice(struttura.getCodTipoStruttura());
			strutt.setDescrizione(struttura.getDescTipoStruttura());
			strutture.add(strutt);
		}
		return strutture;
	}

	public List<ModelListeConfiguratore> findQuote() {
		String hqlQuery = "select quote " + "from GregDTipologiaQuota quote " + "where quote.dataCancellazione is null "
				+ "order by quote.codTipologiaQuota ";

		TypedQuery<GregDTipologiaQuota> query = em.createQuery(hqlQuery, GregDTipologiaQuota.class);

		List<GregDTipologiaQuota> result = (ArrayList<GregDTipologiaQuota>) query.getResultList();

		List<ModelListeConfiguratore> quote = new ArrayList<ModelListeConfiguratore>();

		for (GregDTipologiaQuota quota : result) {
			ModelListeConfiguratore q = new ModelListeConfiguratore();
			q.setId(quota.getIdTipologiaQuota());
			q.setCodice(quota.getCodTipologiaQuota());
			q.setDescrizione(quota.getDescTipologiaQuota());
			quote.add(q);
		}
		return quote;
	}

	public List<ModelListeConfiguratore> findPrestazioniColl() {
		String hqlQuery = "select reg1 " + "from GregTPrestazioniRegionali1 reg1 "
				+ "where reg1.dataCancellazione is null " + "and reg1.gregDTipologia.codTipologia = 'MA03' "
				+ "order by reg1.codPrestReg1 ";

		TypedQuery<GregTPrestazioniRegionali1> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali1.class);

		List<GregTPrestazioniRegionali1> result = (ArrayList<GregTPrestazioniRegionali1>) query.getResultList();

		List<ModelListeConfiguratore> prest = new ArrayList<ModelListeConfiguratore>();

		for (GregTPrestazioniRegionali1 reg1 : result) {
			ModelListeConfiguratore r1 = new ModelListeConfiguratore();
			r1.setId(reg1.getIdPrestReg1());
			r1.setCodice(reg1.getCodPrestReg1());
			r1.setDescrizione(reg1.getDesPrestReg1());
			prest.add(r1);
		}
		return prest;
	}

	public List<ModelListeConfiguratore> findMacroaggregati() {
		String hqlQuery = "select macro " + "from GregTMacroaggregatiBilancio macro "
				+ "where macro.dataCancellazione is null " + "order by macro.codMacroaggregatoBilancio ";

		TypedQuery<GregTMacroaggregatiBilancio> query = em.createQuery(hqlQuery, GregTMacroaggregatiBilancio.class);

		List<GregTMacroaggregatiBilancio> result = (ArrayList<GregTMacroaggregatiBilancio>) query.getResultList();

		List<ModelListeConfiguratore> macro = new ArrayList<ModelListeConfiguratore>();

		for (GregTMacroaggregatiBilancio m : result) {
			ModelListeConfiguratore ma = new ModelListeConfiguratore();
			ma.setId(m.getIdMacroaggregatoBilancio());
			ma.setCodice(m.getCodMacroaggregatoBilancio());
			ma.setDescrizione(m.getDesMacroaggregatoBilancio());
			macro.add(ma);
		}
		return macro;
	}

	@SuppressWarnings("unchecked")
	public boolean getListaPrestazioniValorizzateModA(String codPreg1, Timestamp inizio, Timestamp fine) {

		Query query = em.createNativeQuery("select " + "	case  " + "	when q.cod_tipologia <> 'MA05' "
				+ "	then false  " + "	else " + "		case " + "			when p2.id_prest_reg_1 in ( "
				+ "			select " + "				distinct p2.id_prest_reg_1  " + "			from "
				+ "				greg_t_rendicontazione_ente g "
				+ "			left join greg_r_rendicontazione_mod_a_part2 r on "
				+ "				g.id_rendicontazione_ente = r.id_rendicontazione_ente "
				+ "			left join greg_r_prest_reg1_utenze_regionali1 p on "
				+ "				r.id_prest_reg1_utenza_regionale1 = p.id_prest_reg1_utenza_regionale1 "
				+ "			left join greg_t_prestazioni_regionali_1 p2 on "
				+ "				p.id_prest_reg_1 = p2.id_prest_reg_1 " + "			where "
				+ "				r.valore is not null " + "				and g.data_cancellazione is null "
				+ "				and r.data_cancellazione is null " + "				and p.data_cancellazione is null "
				+ "				and p2.data_cancellazione is null ) " + "		then true " + "			else false "
				+ "		end " + "		end " + "		 as modificabile " + "from "
				+ "	greg_t_prestazioni_regionali_1 p2 " + "left join greg_d_tipologia q on "
				+ "	q.id_tipologia = p2.id_tipologia  " + "where " + "	p2.cod_prest_reg_1 = :codPreg1 "
				+ "and p2.data_cancellazione is null");

		query.setParameter("codPreg1", codPreg1);

		boolean result = (boolean) query.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Timestamp getAnnoMaxPrestazioniValorizzate(String codPreg1, Timestamp inizio, Timestamp fine)
			throws ParseException {

		Query query = em.createNativeQuery("with a as( " + "select " + "	case " + "		when " + "		case "
				+ "			when q.cod_tipologia <> 'MA05' then false " + "			else " + "			case "
				+ "				when count(r.valore)>0 " + "			then true " + "				else false "
				+ "			end " + "		end " + "	then g.anno_gestione " + "	end as anno " + "from "
				+ "	greg_t_rendicontazione_ente g " + "left join greg_r_rendicontazione_mod_a_part2 r on "
				+ "				g.id_rendicontazione_ente = r.id_rendicontazione_ente "
				+ "left join greg_r_prest_reg1_utenze_regionali1 p on "
				+ "				r.id_prest_reg1_utenza_regionale1 = p.id_prest_reg1_utenza_regionale1 "
				+ "left join greg_t_prestazioni_regionali_1 p2 on "
				+ "				p.id_prest_reg_1 = p2.id_prest_reg_1 " + "left join greg_d_tipologia q on  "
				+ "	q.id_tipologia = p2.id_tipologia " + "where " + "	r.valore is not null "
				+ "	and g.data_cancellazione is null " + "	and r.data_cancellazione is null "
				+ "	and p.data_cancellazione is null " + "	and p2.data_cancellazione is null "
				+ "	and p2.cod_prest_reg_1 = :codPreg1 " + "group by " + "	g.anno_gestione, q.cod_tipologia  "
				+ "	union  " + "	 " + " " + "select " + "	case " + "		when " + "		case "
				+ "			when q.cod_tipologia = 'MA05' then false " + "			else " + "			case "
				+ "				when count(r.valore)>0 or count(r1.valore)>0 or count(r3.valore)>0 "
				+ "			then true " + "				else false " + "			end " + "		end "
				+ "	then g.anno_gestione " + "	end as anno " + "from " + "	greg_t_rendicontazione_ente g "
				+ "left join greg_r_rendicontazione_preg1_macro r on "
				+ "				g.id_rendicontazione_ente = r.id_rendicontazione_ente "
				+ "left join greg_r_prest_reg1_macroaggregati_bilancio macro on "
				+ "				macro.id_prest_reg1_macroaggregati_bilancio = r.id_prest_reg1_macroaggregati_bilancio "
				+ "left join greg_r_rendicontazione_preg1_utereg1 r1 on "
				+ "				g.id_rendicontazione_ente = r1.id_rendicontazione_ente "
				+ "left join greg_r_prest_reg1_utenze_regionali1 ute on "
				+ "				ute.id_prest_reg1_utenza_regionale1 = r1.id_prest_reg1_utenza_regionale1 "
				+ "left join greg_r_rendicontazione_preg2_utereg2 r3 on "
				+ "				g.id_rendicontazione_ente = r3.id_rendicontazione_ente "
				+ "left join greg_r_prest_reg2_utenze_regionali2 ute2 on "
				+ "				ute2.id_prest_reg2_utenza_regionale2 = r3.id_prest_reg2_utenza_regionale2 "
				+ "left join greg_t_prestazioni_regionali_2 p3 on "
				+ "				ute2.id_prest_reg_2 = p3.id_prest_reg_2 "
				+ "				left join greg_t_prestazioni_regionali_1 p2 on "
				+ "				macro.id_prest_reg_1 = p2.id_prest_reg_1 and ute.id_prest_reg_1 = p2.id_prest_reg_1 "
				+ "left join greg_d_tipologia q on  " + "	q.id_tipologia = p2.id_tipologia " + "where "
				+ "	r.valore is not null " + "	and r.data_cancellazione is null " + "	and r1.valore is not null "
				+ "	and r1.data_cancellazione is null " + "	and r3.valore is not null "
				+ "	and r3.data_cancellazione is null " + "	and p3.data_cancellazione is null "
				+ "	and p2.data_cancellazione is null " + "	and p2.cod_prest_reg_1 = :codPreg1 " + "group by "
				+ "	g.anno_gestione, " + "	q.cod_tipologia  " + "union " + "	select  " + "case  " + "when case  "
				+ "	when count(r.valore)>0 " + "		then true  " + "		else false  " + "	end "
				+ "	then g.anno_gestione " + "end as anno " + "from greg_t_rendicontazione_ente g   "
				+ "left join greg_r_rendicontazione_mod_c_parte1 r on g.id_rendicontazione_ente = r.id_rendicontazione_ente   "
				+ "left join greg_r_prest_reg1_utenze_modc p on r.id_prest_reg1_utenza_modc = p.id_prest_reg1_utenza_modc   "
				+ "left join greg_t_prestazioni_regionali_1 p2 on p.id_prest_reg_1 = p2.id_prest_reg_1   "
				+ "where g.data_cancellazione is null  and r.data_cancellazione is null   "
				+ "and p.data_cancellazione is null  and p2.data_cancellazione is null "
				+ "and p2.cod_prest_reg_1 = :codPreg1 " + "group by g.anno_gestione)  " + "select " + "	max(a.anno) "
				+ "from " + "	a;");

		query.setParameter("codPreg1", codPreg1);

		Integer result = (Integer) query.getSingleResult();

		Timestamp dataMax = null;
		if (result != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/" + (result + 1));
			dataMax = new Timestamp(date.getTime());
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/2000");
			dataMax = new Timestamp(date.getTime());
		}

		return dataMax;
	}

	@SuppressWarnings("unchecked")
	public boolean getListaPrestazioniValorizzateModB1(String codPreg1, Timestamp inizio, Timestamp fine) {

		Query query = em.createNativeQuery("select " + "	case " + "		when q.cod_tipologia = 'MA05' then false "
				+ "		else " + "		case " + "			when p2.id_prest_reg_1 in ( " + "			select "
				+ "				distinct p2.id_prest_reg_1 " + "			from "
				+ "				greg_t_rendicontazione_ente g "
				+ "			left join greg_r_rendicontazione_preg1_macro r on "
				+ "				g.id_rendicontazione_ente = r.id_rendicontazione_ente "
				+ "			left join greg_r_prest_reg1_macroaggregati_bilancio macro on "
				+ "				macro.id_prest_reg1_macroaggregati_bilancio = r.id_prest_reg1_macroaggregati_bilancio "
				+ "			left join greg_t_prestazioni_regionali_1 p2 on "
				+ "				macro.id_prest_reg_1 = p2.id_prest_reg_1 " + "			where "
				+ "				r.valore is not null " + "				and g.data_cancellazione is null "
				+ "				and r.data_cancellazione is null " + "				and p2.data_cancellazione is null "
				+ "		union " + "			select " + "				distinct p2.id_prest_reg_1 "
				+ "			from " + "				greg_t_rendicontazione_ente g "
				+ "			left join greg_r_rendicontazione_preg1_utereg1 r1 on "
				+ "				g.id_rendicontazione_ente = r1.id_rendicontazione_ente "
				+ "			left join greg_r_prest_reg1_utenze_regionali1 ute on "
				+ "				ute.id_prest_reg1_utenza_regionale1 = r1.id_prest_reg1_utenza_regionale1 "
				+ "			left join greg_t_prestazioni_regionali_1 p2 on "
				+ "				ute.id_prest_reg_1 = p2.id_prest_reg_1 " + "			where "
				+ "				r1.valore is not null " + "				and g.data_cancellazione is null "
				+ "				and r1.data_cancellazione is null "
				+ "				and p2.data_cancellazione is null) or  " + "				p3.id_prest_reg_2 in ( "
				+ "			select " + "				distinct p3.id_prest_reg_2 " + "			from "
				+ "				greg_t_rendicontazione_ente g "
				+ "			left join greg_r_rendicontazione_preg2_utereg2 r3 on "
				+ "				g.id_rendicontazione_ente = r3.id_rendicontazione_ente "
				+ "			left join greg_r_prest_reg2_utenze_regionali2 ute2 on "
				+ "				ute2.id_prest_reg2_utenza_regionale2 = r3.id_prest_reg2_utenza_regionale2 "
				+ "			left join greg_t_prestazioni_regionali_2 p3 on "
				+ "				ute2.id_prest_reg_2 = p3.id_prest_reg_2 " + "			where "
				+ "				r3.valore is not null " + "				and g.data_cancellazione is null "
				+ "				and r3.data_cancellazione is null "
				+ "				and p3.data_cancellazione is null)  " + "				then true "
				+ "			else false " + "		end " + "	end " + "from " + "	greg_t_prestazioni_regionali_1 p2 "
				+ "left join greg_d_tipologia q on " + "	q.id_tipologia = p2.id_tipologia "
				+ "left join greg_r_prest_reg1_prest_reg2 p4 on " + "	p4.id_prest_reg_1 = p2.id_prest_reg_1	 "
				+ "left join greg_t_prestazioni_regionali_2 p3 on " + "	p4.id_prest_reg_2 = p3.id_prest_reg_2 "
				+ "where " + "	p2.cod_prest_reg_1 = :codPreg1 " + "and p2.data_cancellazione is null "
				+ "and p3.data_cancellazione is null " + "and p4.data_cancellazione is null");

		query.setParameter("codPreg1", codPreg1);

		boolean result = (boolean) query.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public boolean getListaPrestazioniValorizzateModC(String codPreg1, Timestamp inizio, Timestamp fine) {

		Query query = em.createNativeQuery("select  " + "case  " + "	when count(r.valore)>0 " + "	then true  "
				+ "	else false  " + "end " + "from greg_t_rendicontazione_ente g   "
				+ "left join greg_r_rendicontazione_mod_c_parte1 r on g.id_rendicontazione_ente = r.id_rendicontazione_ente   "
				+ "left join greg_r_prest_reg1_utenze_modc p on r.id_prest_reg1_utenza_modc = p.id_prest_reg1_utenza_modc   "
				+ "left join greg_t_prestazioni_regionali_1 p2 on p.id_prest_reg_1 = p2.id_prest_reg_1   "
				+ "where g.data_cancellazione is null  and r.data_cancellazione is null   "
				+ "and p.data_cancellazione is null  and p2.data_cancellazione is null "
				+ "and p2.cod_prest_reg_1 = :codPreg1");

		query.setParameter("codPreg1", codPreg1);

		boolean result = (boolean) query.getSingleResult();
		return result;
	}

	public List<ModelListeConfiguratore> findUtenza() {
		String hqlQuery = "select u " + "from GregDTargetUtenza u " + "left join fetch u.gregDTipoFlusso f "
				+ "where u.dataCancellazione is null " + "and f.codTipoFlusso = 'REG' " + "order by u.codUtenza ";

		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);

		List<GregDTargetUtenza> result = (ArrayList<GregDTargetUtenza>) query.getResultList();

		List<ModelListeConfiguratore> macro = new ArrayList<ModelListeConfiguratore>();

		for (GregDTargetUtenza m : result) {
			ModelListeConfiguratore ma = new ModelListeConfiguratore();
			ma.setId(m.getIdTargetUtenza());
			ma.setCodice(m.getCodUtenza());
			ma.setDescrizione(m.getDesUtenza());
			macro.add(ma);
		}
		return macro;
	}

	public List<ModelListeConfiguratore> findUtenzaIstat(List<ModelPrestUtenza> utenze) {
//		String hqlQuery = "select u " + "from GregDTargetUtenza u " + "left join fetch u.gregDTipoFlusso f "
//				+ "where u.dataCancellazione is null " + "and f.codTipoFlusso = 'ISTAT' " + "order by u.codUtenza ";

		String hqlQuery = "select " + "	DISTINCT u " + "from " + "	GregDTargetUtenza u "
				+ "left join GregDTipoFlusso f on f.idTipoFlusso = u.gregDTipoFlusso.idTipoFlusso  "
				+ "left join GregRAlgoritmoTargetUtenza a on a.gregDTargetUtenza1.idTargetUtenza = u.idTargetUtenza  "
				+ "left join GregDTargetUtenza u2 on u2.idTargetUtenza = a.gregDTargetUtenza2.idTargetUtenza "
				+ "where " + "	u.dataCancellazione is null " + "	and f.codTipoFlusso = 'ISTAT' "
				+ "and u2.codUtenza in (";
		for (int i = 0; i < utenze.size(); i++) {
			if (i < utenze.size() - 1) {
				hqlQuery += "'" + utenze.get(i).getCodUtenza() + "'" + ", ";
			} else {
				hqlQuery += "'" + utenze.get(i).getCodUtenza() + "'";
			}

		}
		hqlQuery += ") " + "order by " + "	u.codUtenza";

		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);

		List<GregDTargetUtenza> result = (ArrayList<GregDTargetUtenza>) query.getResultList();

		List<ModelListeConfiguratore> macro = new ArrayList<ModelListeConfiguratore>();

		for (GregDTargetUtenza m : result) {
			ModelListeConfiguratore ma = new ModelListeConfiguratore();
			ma.setId(m.getIdTargetUtenza());
			ma.setCodice(m.getCodUtenza());
			ma.setDescrizione(m.getDesUtenza());
			macro.add(ma);
		}
		return macro;
	}

	public ModelListeConfiguratore findUtenzaIstatPerTranscodifica(String codUtenza) {
//		String hqlQuery = "select u " + "from GregDTargetUtenza u " + "left join fetch u.gregDTipoFlusso f "
//				+ "where u.dataCancellazione is null " + "and f.codTipoFlusso = 'ISTAT' " + "order by u.codUtenza ";

		String hqlQuery = "select " + "	DISTINCT u " + "from " + "	GregDTargetUtenza u "
				+ "left join GregDTipoFlusso f on f.idTipoFlusso = u.gregDTipoFlusso.idTipoFlusso  "
				+ "left join GregRAlgoritmoTargetUtenza a on a.gregDTargetUtenza1.idTargetUtenza = u.idTargetUtenza  "
				+ "left join GregDTargetUtenza u2 on u2.idTargetUtenza = a.gregDTargetUtenza2.idTargetUtenza "
				+ "where " + "	u.dataCancellazione is null " + "	and f.codTipoFlusso = 'ISTAT' "
				+ "and u2.codUtenza = :codUtenza " + "and u.codUtenza <> 'U21' " + "order by " + "	u.codUtenza";

		TypedQuery<GregDTargetUtenza> query = em.createQuery(hqlQuery, GregDTargetUtenza.class);
		query.setParameter("codUtenza", codUtenza);
		GregDTargetUtenza result = (GregDTargetUtenza) query.getSingleResult();

		ModelListeConfiguratore ma = new ModelListeConfiguratore();
		ma.setId(result.getIdTargetUtenza());
		ma.setCodice(result.getCodUtenza());
		ma.setDescrizione(result.getDesUtenza());
		return ma;
	}

	public List<ModelNomenclatore> findNomenclatore() {
		String hqlQuery = "select u " + "from GregTNomenclatoreNazionale u " + "where u.dataCancellazione is null "
				+ "order by u.gregDVoci.codVoce ";

		TypedQuery<GregTNomenclatoreNazionale> query = em.createQuery(hqlQuery, GregTNomenclatoreNazionale.class);

		List<GregTNomenclatoreNazionale> result = (ArrayList<GregTNomenclatoreNazionale>) query.getResultList();

		List<ModelNomenclatore> macro = new ArrayList<ModelNomenclatore>();

		for (GregTNomenclatoreNazionale m : result) {
			ModelNomenclatore ma = new ModelNomenclatore();
			ma.setIdNomenclatore(m.getIdNomenclatoreNazionale());
			ma.setCodNomenclatore(m.getCodNomenclatoreNazionale());
			ma.setCodClassificazionePresidio(m.getGregDClassificazionePresidio() != null
					? m.getGregDClassificazionePresidio().getCodClassificazionePresidio()
					: "");
			ma.setDescClassificazionePresidio(m.getGregDClassificazionePresidio() != null
					? m.getGregDClassificazionePresidio().getDesClassificazionePresidio()
					: "");
			ma.setCodFunzionePresidio(
					m.getGregDFunzionePresidio() != null ? m.getGregDFunzionePresidio().getCodFunzionePresidio() : "");
			ma.setDescFunzionePresidio(
					m.getGregDFunzionePresidio() != null ? m.getGregDFunzionePresidio().getDesFunzionePresidio() : "");
			ma.setCodMacroArea(m.getGregDMacroaree() != null ? m.getGregDMacroaree().getCodMacroarea() : "");
			ma.setDescMacroArea(m.getGregDMacroaree() != null ? m.getGregDMacroaree().getDesMacroarea() : "");
			ma.setCodPresidio(m.getGregDTipoPresidio() != null ? m.getGregDTipoPresidio().getCodTipoPresidio() : "");
			ma.setDescPresidio(m.getGregDTipoPresidio() != null ? m.getGregDTipoPresidio().getDesTipoPresidio() : "");
			ma.setCodSottoArea(m.getGregDSottoaree() != null ? m.getGregDSottoaree().getCodSottoarea() : "");
			ma.setDescSottoArea(m.getGregDSottoaree() != null ? m.getGregDSottoaree().getDesSottoarea() : "");
			ma.setCodSottoAreaDet(
					m.getGregDSottoareeDet() != null ? m.getGregDSottoareeDet().getCodSottoareaDet() : "");
			ma.setDescSottoAreaDet(
					m.getGregDSottoareeDet() != null ? m.getGregDSottoareeDet().getDesSottoareaDet() : "");
			ma.setCodSottoVoce(m.getGregDSottovoci() != null ? m.getGregDSottovoci().getCodSottovoce() : "");
			ma.setDescSottoVoce(m.getGregDSottovoci() != null ? m.getGregDSottovoci().getDesSottovoce() : "");
			ma.setCodTipoResidenza(
					m.getGregDTipoResidenza() != null ? m.getGregDTipoResidenza().getCodTipoResidenza() : "");
			ma.setDescTipoResidenza(
					m.getGregDTipoResidenza() != null ? m.getGregDTipoResidenza().getDesTipoResidenza() : "");
			ma.setCodVoce(m.getGregDVoci() != null ? m.getGregDVoci().getCodVoce() : "");
			ma.setDescVoce(m.getGregDVoci() != null ? m.getGregDVoci().getDesVoce() : "");
			macro.add(ma);
		}
		return macro;
	}

	public List<ModelListeConfiguratore> findMissioniProg() {
		String hqlQuery = "select u " + "from GregDProgrammaMissione u " + "where u.dataCancellazione is null "
				+ "and u.siglaProgrammaMissione is not null " + "order by u.codProgrammaMissione ";

		TypedQuery<GregDProgrammaMissione> query = em.createQuery(hqlQuery, GregDProgrammaMissione.class);

		List<GregDProgrammaMissione> result = (ArrayList<GregDProgrammaMissione>) query.getResultList();

		List<ModelListeConfiguratore> macro = new ArrayList<ModelListeConfiguratore>();

		for (GregDProgrammaMissione m : result) {
			ModelListeConfiguratore ma = new ModelListeConfiguratore();
			ma.setId(m.getIdProgrammaMissione());
			ma.setCodice(m.getSiglaProgrammaMissione());
			ma.setDescrizione(m.getInformativa());
			ma.setColore(m.getGregDColori().getRgb());
			macro.add(ma);
		}
		return macro;
	}

	public List<ModelListeConfiguratore> findSpese() {
		String hqlQuery = "select u " + "from GregDTipologiaSpesa u " + "where u.dataCancellazione is null "
				+ "and u.valoriDalModello = 'B1' " + "order by u.codTipologiaSpesa ";

		TypedQuery<GregDTipologiaSpesa> query = em.createQuery(hqlQuery, GregDTipologiaSpesa.class);

		List<GregDTipologiaSpesa> result = (ArrayList<GregDTipologiaSpesa>) query.getResultList();

		List<ModelListeConfiguratore> macro = new ArrayList<ModelListeConfiguratore>();

		for (GregDTipologiaSpesa m : result) {
			ModelListeConfiguratore ma = new ModelListeConfiguratore();
			ma.setId(m.getIdTipologiaSpesa());
			ma.setCodice(m.getCodTipologiaSpesa());
			ma.setDescrizione(m.getDesTipologiaSpesa());
			macro.add(ma);
		}
		return macro;
	}

	@SuppressWarnings("unchecked")
	public boolean getUtenzaPrestazioneValorizzata(Integer idPrestazione, Integer idUtenza) {

		Query query = em.createNativeQuery("select " + "	case " + "		when q.cod_tipologia ='MA05' "
				+ "		then  " + "			case " + "				when  " + "				( " + "			select "
				+ "				count(r2.valore) " + "			from "
				+ "				greg_r_rendicontazione_mod_a_part2 r2 " + "			where "
				+ "				r2.valore is not null " + "				and  "
				+ "					r2.id_prest_reg1_utenza_regionale1 = pu.id_prest_reg1_utenza_regionale1 )>0 "
				+ "				then true " + "			else false " + "		end " + "		else  " + "		case "
				+ "				when  " + "				( " + "			select " + "				count(r.valore) "
				+ "			from " + "				greg_r_rendicontazione_preg1_utereg1 r " + "			where "
				+ "				r.valore is not null " + "				and  "
				+ "					r.id_prest_reg1_utenza_regionale1 = pu.id_prest_reg1_utenza_regionale1 )>0 "
				+ "				then true " + "			else false " + "		end " + "	end " + "from "
				+ "		greg_r_prest_reg1_utenze_regionali1 pu, " + "		greg_t_prestazioni_regionali_1 p, "
				+ "		greg_d_tipologia q, " + "		greg_d_target_utenza u " + "where "
				+ "		q.id_tipologia = p.id_tipologia " + "	and " + "	pu.id_prest_reg_1 = p.id_prest_reg_1 "
				+ "	and  " + "	pu.id_target_utenza = u.id_target_utenza " + "	and  "
				+ "	p.id_prest_reg_1 = :idPrestazione " + "	and " + "	u.id_target_utenza = :idUtenza "
				+ "and pu.data_cancellazione is null");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idUtenza", idUtenza);

		boolean result = (boolean) query.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Timestamp getAnnoUtenzaPrestazioneValorizzata(Integer idPrestazione, Integer idUtenza)
			throws ParseException {

		Query query = em.createNativeQuery("with s as ( " + "select " + "	case " + "		when " + "		case "
				+ "			when q.cod_tipologia = 'MA05' then " + "			case " + "				when  "
				+ "					count(r2.valore)>0 " + "				then true " + "				else false "
				+ "			end " + "		end " + "		then g.anno_gestione " + "	end as anno " + "from "
				+ "		greg_r_prest_reg1_utenze_regionali1 pu, " + "		greg_t_prestazioni_regionali_1 p, "
				+ "		greg_d_tipologia q, " + "		greg_d_target_utenza u, "
				+ "		greg_t_rendicontazione_ente g, " + "		greg_r_rendicontazione_mod_a_part2 r2 " + "where "
				+ "		q.id_tipologia = p.id_tipologia " + "	and pu.id_prest_reg_1 = p.id_prest_reg_1 "
				+ "	and pu.id_target_utenza = u.id_target_utenza " + "	and p.id_prest_reg_1 = :idPrestazione "
				+ "	and u.id_target_utenza = :idUtenza "
				+ "	and r2.valore is not null and pu.data_cancellazione is null "
				+ "	and r2.id_prest_reg1_utenza_regionale1 = pu.id_prest_reg1_utenza_regionale1 " + "group by "
				+ "		g.anno_gestione, " + "	q.cod_tipologia  " + "union  " + "select  " + "case  " + "when  "
				+ "	case  " + "		when q.cod_tipologia <>'MA05'  " + "		then " + "		case  "
				+ "				when   " + "				count(r.valore) >0  " + "				then true  "
				+ "			else false  " + "		end  " + "	end " + "	then g.anno_gestione  " + "end as anno "
				+ "from  " + "greg_r_rendicontazione_preg1_utereg1 r, "
				+ "		greg_r_prest_reg1_utenze_regionali1 pu,  " + "		greg_t_prestazioni_regionali_1 p,  "
				+ "		greg_d_tipologia q,  " + "		greg_d_target_utenza u,  "
				+ "		greg_t_rendicontazione_ente g " + "where  " + "		q.id_tipologia = p.id_tipologia  "
				+ "	and  " + "	pu.id_prest_reg_1 = p.id_prest_reg_1  " + "	and   "
				+ "	pu.id_target_utenza = u.id_target_utenza  " + "	and   " + "	p.id_prest_reg_1 = :idPrestazione  "
				+ "	and  " + "	u.id_target_utenza = :idUtenza "
				+ "	and r.valore is not null and pu.data_cancellazione is null " + "				and   "
				+ "					r.id_prest_reg1_utenza_regionale1 = pu.id_prest_reg1_utenza_regionale1 "
				+ "group by g.anno_gestione, q.cod_tipologia) " + "select max(s.anno) " + "from s");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idUtenza", idUtenza);

		Integer result = (Integer) query.getSingleResult();

		Timestamp dataMax = null;
		if (result != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/" + (result + 1));
			dataMax = new Timestamp(date.getTime());
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/2000");
			dataMax = new Timestamp(date.getTime());
		}

		return dataMax;
	}

	public List<ModelPrest1Prest2> findPrestazioni2() {
		String hqlQuery = "select reg2 " + "from GregTPrestazioniRegionali2 reg2 "
				+ "where reg2.dataCancellazione is null " + "order by reg2.ordinamento ";

		TypedQuery<GregTPrestazioniRegionali2> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali2.class);

		List<GregTPrestazioniRegionali2> result = (ArrayList<GregTPrestazioniRegionali2>) query.getResultList();

		List<ModelPrest1Prest2> prest1 = new ArrayList<ModelPrest1Prest2>();

		for (GregTPrestazioniRegionali2 prestazione : result) {
			ModelPrest1Prest2 preg1 = new ModelPrest1Prest2();
			preg1.setIdPrest2(prestazione.getIdPrestReg2());
			preg1.setCodPrest2(prestazione.getCodPrestReg2());
			preg1.setDescPrest2(prestazione.getDesPrestReg2());
			preg1.setOrdinamento(prestazione.getOrdinamento());
			preg1.setIdTipologia(
					prestazione.getGregDTipologia() != null ? prestazione.getGregDTipologia().getIdTipologia() : null);
			preg1.setCodTipologia(
					prestazione.getGregDTipologia() != null ? prestazione.getGregDTipologia().getCodTipologia() : null);
			preg1.setDal(prestazione.getDataInizioValidita());
			preg1.setAl(prestazione.getDataFineValidita());
			preg1.setNota(prestazione.getInformativa());
			preg1.setDataMin(preg1.getDal());
			preg1.setModificabile(true);
			preg1.setUtilizzato(getPrestazione2Utilizzata(prestazione.getIdPrestReg2()));
			if (!preg1.isUtilizzato()) {
				Timestamp maxData = getPrestazione2MaxDataFine(prestazione.getIdPrestReg2());
				preg1.setDataLastRelazione(maxData != null ? maxData : null);
			}
			prest1.add(preg1);

		}
		return prest1;
	}

	public ModelPrest1Prest2 findPrestazione2(Integer idPrest2) {
		String hqlQuery = "select reg2 " + "from GregTPrestazioniRegionali2 reg2 "
				+ "where reg2.dataCancellazione is null " + "AND reg2.idPrestReg2 = :idPrest2 "
				+ "order by reg2.ordinamento ";

		TypedQuery<GregTPrestazioniRegionali2> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali2.class);
		query.setParameter("idPrest2", idPrest2);
		GregTPrestazioniRegionali2 prestazione = (GregTPrestazioniRegionali2) query.getSingleResult();

		ModelPrest1Prest2 preg1 = new ModelPrest1Prest2();
		preg1.setIdPrest2(prestazione.getIdPrestReg2());
		preg1.setCodPrest2(prestazione.getCodPrestReg2());
		preg1.setDescPrest2(prestazione.getDesPrestReg2());
		preg1.setOrdinamento(prestazione.getOrdinamento());
		preg1.setIdTipologia(
				prestazione.getGregDTipologia() != null ? prestazione.getGregDTipologia().getIdTipologia() : null);
		preg1.setCodTipologia(
				prestazione.getGregDTipologia() != null ? prestazione.getGregDTipologia().getCodTipologia() : null);
		preg1.setDal(prestazione.getDataInizioValidita());
		preg1.setAl(prestazione.getDataFineValidita());
		preg1.setNota(prestazione.getInformativa());
		preg1.setDataMin(preg1.getDal());
		preg1.setDataCreazione(preg1.getDataCreazione());
		preg1.setModificabile(true);
		List<GregRPrestReg2UtenzeRegionali2> targetUtenze = findUtenzaRegByPrest2(prestazione.getIdPrestReg2());
		List<ModelPrestUtenza> utenze = new ArrayList<ModelPrestUtenza>();
		for (GregRPrestReg2UtenzeRegionali2 u : targetUtenze) {
			ModelPrestUtenza ute = new ModelPrestUtenza();
			ute.setIdUtenza(u.getGregDTargetUtenza().getIdTargetUtenza());
			ute.setCodUtenza(u.getGregDTargetUtenza().getCodUtenza());
			ute.setDescUtenza(u.getGregDTargetUtenza().getDesUtenza());
			ute.setDal(u.getDataInizioValidita());
			ute.setAl(u.getDataFineValidita());
			ute.setDataMin(ute.getDal());
			ute.setDataCreazione(ute.getDataCreazione());
			utenze.add(ute);
		}
		preg1.setUtenzeConf(utenze);
		List<GregDVoceIstat> voce = findMinisterialeByPrest2(prestazione.getIdPrestReg2());
		if (voce != null) {
			List<ModelPrest2PrestIstat> i = new ArrayList<ModelPrest2PrestIstat>();
			for (GregDVoceIstat v : voce) {
				ModelPrest2PrestIstat istat = new ModelPrest2PrestIstat();
				istat.setIdPrestIstat(v.getIdVoceIstat());
				istat.setCodPrestIstat(v.getCodVoceIstat());
				istat.setDescPrestIstat(v.getDescVoceIstat());
				List<GregDTargetUtenza> utenzeMin = findUtenzeMinisByPrest2Istat(prestazione.getIdPrestReg2(),
						v.getIdVoceIstat());
				List<ModelPrestUtenza> utenzeM = new ArrayList<ModelPrestUtenza>();
				for (GregDTargetUtenza uM : utenzeMin) {
					ModelPrestUtenza u = new ModelPrestUtenza();
					u.setIdUtenza(uM.getIdTargetUtenza());
					u.setCodUtenza(uM.getCodUtenza());
					u.setDescUtenza(uM.getDesUtenza());
					GregRCatUteVocePrestReg2Istat ru = findRUtenzeMinisByPrest2Istat(prestazione.getIdPrestReg2(),
							v.getIdVoceIstat(), uM.getIdTargetUtenza());
					u.setDal(ru.getDataInizioValidita());
					u.setDataCreazione(ru.getDataCreazione());
					u.setAl(ru.getDataFineValidita());
					utenzeM.add(u);
				}
				istat.setUtenzeMinConf(utenzeM);
				i.add(istat);
			}
			preg1.setPrestIstat(i);
		}
		List<GregRNomencNazPrestReg2> nomenclatore = findNomenByPrest2Istat(prestazione.getIdPrestReg2());

		if (nomenclatore != null) {
			List<ModelNomenclatore> nom2013 = new ArrayList<ModelNomenclatore>();
			for (GregRNomencNazPrestReg2 nom : nomenclatore) {
				ModelNomenclatore n = new ModelNomenclatore();
				n.setIdNomenclatore(nom.getGregTNomenclatoreNazionale().getIdNomenclatoreNazionale());
				n.setCodClassificazionePresidio(
						nom.getGregTNomenclatoreNazionale().getGregDClassificazionePresidio() != null
								? nom.getGregTNomenclatoreNazionale().getGregDClassificazionePresidio()
										.getCodClassificazionePresidio()
								: " ");
				n.setDescClassificazionePresidio(
						nom.getGregTNomenclatoreNazionale().getGregDClassificazionePresidio() != null
								? nom.getGregTNomenclatoreNazionale().getGregDClassificazionePresidio()
										.getDesClassificazionePresidio()
								: " ");
				n.setCodFunzionePresidio(nom.getGregTNomenclatoreNazionale().getGregDFunzionePresidio() != null
						? nom.getGregTNomenclatoreNazionale().getGregDFunzionePresidio().getCodFunzionePresidio()
						: " ");
				n.setDescFunzionePresidio(nom.getGregTNomenclatoreNazionale().getGregDFunzionePresidio() != null
						? nom.getGregTNomenclatoreNazionale().getGregDFunzionePresidio().getDesFunzionePresidio()
						: " ");
				n.setCodMacroArea(nom.getGregTNomenclatoreNazionale().getGregDMacroaree() != null
						? nom.getGregTNomenclatoreNazionale().getGregDMacroaree().getCodMacroarea()
						: " ");
				n.setDescMacroArea(nom.getGregTNomenclatoreNazionale().getGregDMacroaree() != null
						? nom.getGregTNomenclatoreNazionale().getGregDMacroaree().getDesMacroarea()
						: " ");
				n.setCodPresidio(nom.getGregTNomenclatoreNazionale().getGregDTipoPresidio() != null
						? nom.getGregTNomenclatoreNazionale().getGregDTipoPresidio().getCodTipoPresidio()
						: " ");
				n.setDescPresidio(nom.getGregTNomenclatoreNazionale().getGregDTipoPresidio() != null
						? nom.getGregTNomenclatoreNazionale().getGregDTipoPresidio().getDesTipoPresidio()
						: " ");
				n.setCodSottoArea(nom.getGregTNomenclatoreNazionale().getGregDSottoaree() != null
						? nom.getGregTNomenclatoreNazionale().getGregDSottoaree().getCodSottoarea()
						: " ");
				n.setDescSottoArea(nom.getGregTNomenclatoreNazionale().getGregDSottoaree() != null
						? nom.getGregTNomenclatoreNazionale().getGregDSottoaree().getDesSottoarea()
						: " ");
				n.setCodSottoAreaDet(nom.getGregTNomenclatoreNazionale().getGregDSottoareeDet() != null
						? nom.getGregTNomenclatoreNazionale().getGregDSottoareeDet().getCodSottoareaDet()
						: " ");
				n.setDescSottoAreaDet(nom.getGregTNomenclatoreNazionale().getGregDSottoareeDet() != null
						? nom.getGregTNomenclatoreNazionale().getGregDSottoareeDet().getDesSottoareaDet()
						: " ");
				n.setCodSottoVoce(nom.getGregTNomenclatoreNazionale().getGregDSottovoci() != null
						? nom.getGregTNomenclatoreNazionale().getGregDSottovoci().getCodSottovoce()
						: " ");
				n.setDescSottoVoce(nom.getGregTNomenclatoreNazionale().getGregDSottovoci() != null
						? nom.getGregTNomenclatoreNazionale().getGregDSottovoci().getDesSottovoce()
						: " ");
				n.setCodTipoResidenza(nom.getGregTNomenclatoreNazionale().getGregDTipoResidenza() != null
						? nom.getGregTNomenclatoreNazionale().getGregDTipoResidenza().getCodTipoResidenza()
						: " ");
				n.setDescTipoResidenza(nom.getGregTNomenclatoreNazionale().getGregDTipoResidenza() != null
						? nom.getGregTNomenclatoreNazionale().getGregDTipoResidenza().getDesTipoResidenza()
						: " ");
				n.setCodVoce(nom.getGregTNomenclatoreNazionale().getGregDVoci() != null
						? nom.getGregTNomenclatoreNazionale().getGregDVoci().getCodVoce()
						: " ");
				n.setDescVoce(nom.getGregTNomenclatoreNazionale().getGregDVoci() != null
						? nom.getGregTNomenclatoreNazionale().getGregDVoci().getDesVoce()
						: " ");

				n.setDal(nom.getDataInizioValidita());
				n.setAl(nom.getDataFineValidita());
				n.setDataMin(nom.getDataInizioValidita());
				nom2013.add(n);
			}
			preg1.setNomenclatore(nom2013);
		}

		return preg1;
	}

	@SuppressWarnings("unchecked")
	public boolean getPrestazione2Utilizzata(Integer idPreg2) {

		Query query = em.createNativeQuery("select " + "	case " + "		when reg2.id_prest_reg_2 not in( "
				+ "		select " + "			id_prest_reg_2 " + "		from "
				+ "			greg_r_prest_reg1_prest_reg2 r " + "		where "
				+ "			r.data_cancellazione is null " + "			and r.data_fine_validita is null) "
				+ "then false " + "		else true " + "	end as utilizzato " + "from "
				+ "	Greg_T_Prestazioni_Regionali_2 reg2 " + "where " + "	reg2.data_Cancellazione is null "
				+ " and reg2.id_prest_reg_2 = :idPreg2 " + "order by " + "	reg2.ordinamento");

		query.setParameter("idPreg2", idPreg2);

		boolean result = (boolean) query.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Timestamp getPrestazione2MaxDataFine(Integer idPreg2) {
		try {
			Query query = em.createNativeQuery("select " + "		( " + "	select "
					+ "			max(r.data_fine_validita)  " + "	from " + "			greg_r_prest_reg1_prest_reg2 r "
					+ "	where " + "			r.data_cancellazione is null " + "		and r.data_fine_validita is null) "
					+ "from " + "	Greg_T_Prestazioni_Regionali_2 reg2 " + "where "
					+ "	reg2.data_Cancellazione is null " + "	and reg2.id_prest_reg_2 = :idPreg2  " + "order by "
					+ "	reg2.ordinamento ");

			query.setParameter("idPreg2", idPreg2);

			Timestamp result = (Timestamp) query.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean getPrestazione2Valorizzata(Integer idPrestazione, Integer idPrestazione2) {

		Query query = em.createNativeQuery("select " + "	case " + "		when " + "			count(r.valore)>0 "
				+ "		then true " + "		else false " + "	end " + "from "
				+ "	greg_r_prest_reg2_utenze_regionali2 reg2u, " + "	greg_r_prest_reg1_prest_reg2 reg1reg2, "
				+ "	greg_t_prestazioni_regionali_1 reg1, " + "	greg_t_prestazioni_regionali_2 reg2, "
				+ "	greg_d_target_utenza u, " + "	greg_r_rendicontazione_preg2_utereg2 r " + "where "
				+ "	reg1reg2.id_prest_reg_1 = reg1.id_prest_reg_1 " + "	and  "
				+ "reg1reg2.id_prest_reg_2 = reg2.id_prest_reg_2 " + "	and  "
				+ "reg2u.id_prest_reg_2 = reg2.id_prest_reg_2 " + "	and  " + "reg1.id_prest_reg_1 = :idPrestazione "
				+ "	and " + "reg2.id_prest_reg_2 = :idPrestazione2 " + "	and  "
				+ "reg2u.id_target_utenza = u.id_target_utenza " + "	and  "
				+ "r.valore is not null and reg2u.data_cancellazione is null "
				+ "	and reg1reg2.data_cancellazione is null " + "	and reg1.data_cancellazione is null "
				+ "	and reg2.data_cancellazione is null "
				+ "	and r.id_prest_reg2_utenza_regionale2 = reg2u.id_prest_reg2_utenza_regionale2");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPrestazione2", idPrestazione2);

		boolean result = (boolean) query.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public boolean getPrestazione2UtenzaValorizzata(Integer idPrestazione, Integer idPrestazione2,
			Integer idTargetUtenza) {

		Query query = em.createNativeQuery("select " + "	case " + "		when count(r.valore)>0 then true "
				+ "		else false " + "	end " + "from " + "	greg_r_prest_reg2_utenze_regionali2 reg2u, "
				+ "	greg_r_prest_reg1_prest_reg2 reg1reg2, " + "	greg_t_prestazioni_regionali_1 reg1, "
				+ "	greg_t_prestazioni_regionali_2 reg2, " + "	greg_d_target_utenza u, "
				+ "	greg_r_rendicontazione_preg2_utereg2 r " + "where "
				+ "	reg1reg2.id_prest_reg_1 = reg1.id_prest_reg_1 "
				+ "	and reg1reg2.id_prest_reg_2 = reg2.id_prest_reg_2 "
				+ "	and reg2u.id_prest_reg_2 = reg2.id_prest_reg_2 " + "	and reg1.id_prest_reg_1 = :idPrestazione "
				+ "	and reg2.id_prest_reg_2 = :idPrestazione2 " + "	and u.id_target_utenza = :idTargetUtenza "
				+ "	and reg2u.id_target_utenza = u.id_target_utenza "
				+ "	and r.valore is not null and reg2u.data_cancellazione is null "
				+ "	and reg1reg2.data_cancellazione is null " + "	and reg1.data_cancellazione is null "
				+ "	and reg2.data_cancellazione is null "
				+ "	and r.id_prest_reg2_utenza_regionale2 = reg2u.id_prest_reg2_utenza_regionale2");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPrestazione2", idPrestazione2);
		query.setParameter("idTargetUtenza", idTargetUtenza);
		boolean result = (boolean) query.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Timestamp getAnnoPrestazione2Valorizzata(Integer idPrestazione, Integer idPrestazione2)
			throws ParseException {

		Query query = em.createNativeQuery("with s as(select  " + "case  " + "when  " + "	case  " + "		when  "
				+ "			count(r.valore)>0  " + "		then true  " + "		else false  " + "	end  "
				+ "	then g.anno_gestione  " + "end as anno " + "from  "
				+ "	greg_r_prest_reg2_utenze_regionali2 reg2u,  " + "	greg_r_prest_reg1_prest_reg2 reg1reg2,  "
				+ "	greg_t_prestazioni_regionali_1 reg1,  " + "	greg_t_prestazioni_regionali_2 reg2,  "
				+ "	greg_d_target_utenza u,  " + "	greg_r_rendicontazione_preg2_utereg2 r, "
				+ "	greg_t_rendicontazione_ente g " + "where  " + "	reg1reg2.id_prest_reg_1 = reg1.id_prest_reg_1  "
				+ "	and   " + "reg1reg2.id_prest_reg_2 = reg2.id_prest_reg_2  " + "	and   "
				+ "reg2u.id_prest_reg_2 = reg2.id_prest_reg_2  " + "	and   "
				+ "reg1.id_prest_reg_1 = :idPrestazione  " + "	and  " + "reg2.id_prest_reg_2 = :idPrestazione2  "
				+ "	and   " + "reg2u.id_target_utenza = u.id_target_utenza  " + "	and   "
				+ "r.valore is not null  and reg2u.data_cancellazione is null "
				+ "	and reg1reg2.data_cancellazione is null " + "	and reg1.data_cancellazione is null "
				+ "	and reg2.data_cancellazione is null "
				+ "	and r.id_prest_reg2_utenza_regionale2 = reg2u.id_prest_reg2_utenza_regionale2 "
				+ "	and g.id_rendicontazione_ente = r.id_rendicontazione_ente  " + "	group by g.anno_gestione) "
				+ "	select max(s.anno) " + "	from s");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPrestazione2", idPrestazione2);

		Integer result = (Integer) query.getSingleResult();

		Timestamp dataMax = null;
		if (result != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/" + (result + 1));
			dataMax = new Timestamp(date.getTime());
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/2000");
			dataMax = new Timestamp(date.getTime());
		}

		return dataMax;
	}

	@SuppressWarnings("unchecked")
	public Timestamp getAnnoPrestazione2UtenzaValorizzata(Integer idPrestazione, Integer idPrestazione2,
			Integer idTargetUtenza) throws ParseException {

		Query query = em.createNativeQuery("with s as(select  " + "case  " + "when  " + "	case  " + "		when  "
				+ "			count(r.valore)>0  " + "		then true  " + "		else false  " + "	end  "
				+ "	then g.anno_gestione  " + "end as anno " + "from  "
				+ "	greg_r_prest_reg2_utenze_regionali2 reg2u,  " + "	greg_r_prest_reg1_prest_reg2 reg1reg2,  "
				+ "	greg_t_prestazioni_regionali_1 reg1,  " + "	greg_t_prestazioni_regionali_2 reg2,  "
				+ "	greg_d_target_utenza u,  " + "	greg_r_rendicontazione_preg2_utereg2 r, "
				+ "	greg_t_rendicontazione_ente g " + "where  " + "	reg1reg2.id_prest_reg_1 = reg1.id_prest_reg_1  "
				+ "	and   " + "reg1reg2.id_prest_reg_2 = reg2.id_prest_reg_2  " + "	and   "
				+ "reg2u.id_prest_reg_2 = reg2.id_prest_reg_2  " + "	and   "
				+ "reg1.id_prest_reg_1 = :idPrestazione  " + "	and  " + "reg2.id_prest_reg_2 = :idPrestazione2  "
				+ "and u.id_target_utenza = :idTargetUtenza " + "	and   "
				+ "reg2u.id_target_utenza = u.id_target_utenza  " + "	and   "
				+ "r.valore is not null  and reg2u.data_cancellazione is null "
				+ "	and reg1reg2.data_cancellazione is null " + "	and reg1.data_cancellazione is null "
				+ "	and reg2.data_cancellazione is null "
				+ "	and r.id_prest_reg2_utenza_regionale2 = reg2u.id_prest_reg2_utenza_regionale2 "
				+ "	and g.id_rendicontazione_ente = r.id_rendicontazione_ente  " + "	group by g.anno_gestione) "
				+ "	select max(s.anno) " + "	from s");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPrestazione2", idPrestazione2);
		query.setParameter("idTargetUtenza", idTargetUtenza);

		Integer result = (Integer) query.getSingleResult();

		Timestamp dataMax = null;
		if (result != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/" + (result + 1));
			dataMax = new Timestamp(date.getTime());
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/2000");
			dataMax = new Timestamp(date.getTime());
		}

		return dataMax;
	}

	public List<ModelPrest1PrestMin> findPrestazioniMin() {
		String hqlQuery = "select p " + "from GregTPrestazioniMinisteriali p " + "where p.dataCancellazione is null "
				+ "order by p.codPrestazioneMinisteriale ";

		TypedQuery<GregTPrestazioniMinisteriali> query = em.createQuery(hqlQuery, GregTPrestazioniMinisteriali.class);

		List<GregTPrestazioniMinisteriali> result = (ArrayList<GregTPrestazioniMinisteriali>) query.getResultList();

		List<ModelPrest1PrestMin> prest1 = new ArrayList<ModelPrest1PrestMin>();

		for (GregTPrestazioniMinisteriali prestazione : result) {
			ModelPrest1PrestMin preg1 = new ModelPrest1PrestMin();
			preg1.setIdPrestMin(prestazione.getIdPrestazioneMinisteriale());
			preg1.setCodPrestMin(prestazione.getCodPrestazioneMinisteriale());
			preg1.setDescPrestMin(prestazione.getDescPrestazioneMinisteriale());
			prest1.add(preg1);
		}
		return prest1;
	}

	@SuppressWarnings("unchecked")
	public boolean getPrestazioneMinValorizzata(Integer idPrestazione, Integer idPmin) {

		Query query = em.createNativeQuery(
				"select  " + "case  " + "	when count(r.valore)>0 " + "	then true " + "	else false  " + "end "
						+ "from greg_r_rendicontazione_modulo_fnps r, " + "greg_r_prest_minist_utenze_minist pmum, "
						+ "greg_r_prest_reg1_prest_minist p1pm, " + "greg_t_prestazioni_regionali_1 p1, "
						+ "greg_t_prestazioni_ministeriali pm " + "where  " + "r.valore is not null  " + "and  "
						+ "r.id_prest_minist_utenze_minist = pmum.id_prest_minist_utenze_minist  " + "and  "
						+ "pmum.id_prest_minist = pm.id_prestazione_ministeriale  " + "and  "
						+ "p1pm.id_prest_minist = pm.id_prestazione_ministeriale  " + "and "
						+ "p1pm.id_prest_reg1 = p1.id_prest_reg_1  " + "and  " + "p1.id_prest_reg_1 = :idPrestazione "
						+ "and  " + "pm.id_prestazione_ministeriale = :idPmin " + "and pmum.data_cancellazione is null "
						+ "and p1pm.data_cancellazione is null " + "and p1.data_cancellazione is null "
						+ "and pm.data_cancellazione is null");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPmin", idPmin);

		boolean result = (boolean) query.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public Timestamp getAnnoPrestazioneMinValorizzata(Integer idPrestazione, Integer idPmin) throws ParseException {

		Query query = em.createNativeQuery("with s as (select   " + "case  " + "when  " + "	case   "
				+ "		when count(r.valore)>0  " + "		then true  " + "		else false   " + "	end  "
				+ "	then g.anno_gestione  " + "end as anno " + "from greg_r_rendicontazione_modulo_fnps r,  "
				+ "greg_r_prest_minist_utenze_minist pmum,  " + "greg_r_prest_reg1_prest_minist p1pm,  "
				+ "greg_t_prestazioni_regionali_1 p1,  " + "greg_t_prestazioni_ministeriali pm,  "
				+ "greg_t_rendicontazione_ente g " + "where   " + "r.valore is not null   " + "and   "
				+ "r.id_prest_minist_utenze_minist = pmum.id_prest_minist_utenze_minist   " + "and   "
				+ "pmum.id_prest_minist = pm.id_prestazione_ministeriale   " + "and   "
				+ "p1pm.id_prest_minist = pm.id_prestazione_ministeriale   " + "and  "
				+ "p1pm.id_prest_reg1 = p1.id_prest_reg_1   " + "and   " + "p1.id_prest_reg_1 = :idPrestazione  "
				+ "and   " + "pm.id_prestazione_ministeriale = :idPmin " + "and pmum.data_cancellazione is null "
				+ "and p1pm.data_cancellazione is null " + "and p1.data_cancellazione is null "
				+ "and pm.data_cancellazione is null "
				+ "and g.id_rendicontazione_ente = r.id_rendicontazione_ente " + "group by g.anno_gestione) "
				+ "select max(s.anno) " + "from s");

		query.setParameter("idPrestazione", idPrestazione);
		query.setParameter("idPmin", idPmin);

		Integer result = (Integer) query.getSingleResult();

		Timestamp dataMax = null;
		if (result != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/" + (result + 1));
			dataMax = new Timestamp(date.getTime());
		} else {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = (Date) formatter.parse("01/01/2000");
			dataMax = new Timestamp(date.getTime());
		}

		return dataMax;
	}

	public GregDTipologia findTipologiaByCod(String codTipologia) {
		try {
			TypedQuery<GregDTipologia> query = em.createNamedQuery("GregDTipologia.findByCod", GregDTipologia.class);
			query.setParameter("codTipologia", codTipologia);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregDTipologiaQuota findTipologiaQuotaByCod(String codTipologia) {
		TypedQuery<GregDTipologiaQuota> query = em.createNamedQuery("GregDTipologiaQuota.findByCod",
				GregDTipologiaQuota.class);
		query.setParameter("codTipologia", codTipologia);
		return query.getSingleResult();
	}

	public GregDTipoStruttura findTipoStrutturaByCod(String codStruttura) {
		TypedQuery<GregDTipoStruttura> query = em.createNamedQuery("GregDTipoStruttura.findByCod",
				GregDTipoStruttura.class);
		query.setParameter("codStruttura", codStruttura);
		return query.getSingleResult();
	}

	public GregTPrestazioniRegionali1 savePrestazione1(GregTPrestazioniRegionali1 prestazione) {
		return em.merge(prestazione);
	}

	public GregTPrestazioniRegionali2 savePrestazione2(GregTPrestazioniRegionali2 prestazione) {
		return em.merge(prestazione);
	}

	public GregTMacroaggregatiBilancio findMacroBilancioByCod(Integer idMacro) {
		TypedQuery<GregTMacroaggregatiBilancio> query = em.createNamedQuery(
				"GregTMacroaggregatiBilancio.findByIdMacroaggregatoBilancioNotDeleted",
				GregTMacroaggregatiBilancio.class);
		query.setParameter("idMacroaggregatoBilancio", idMacro);
		return query.getSingleResult();
	}

	public GregRPrestReg1MacroaggregatiBilancio saveRPrestMacro(GregRPrestReg1MacroaggregatiBilancio prestMacro) {
		return em.merge(prestMacro);
	}

	public GregDTargetUtenza findUtenzaById(Integer idUtenza) {
		TypedQuery<GregDTargetUtenza> query = em.createNamedQuery("GregDTargetUtenza.findByIdUtenzaNotDeleted",
				GregDTargetUtenza.class);
		query.setParameter("idUtenza", idUtenza);
		return query.getSingleResult();
	}

	public GregDColori findColoreByRGB(String rgb) {
		try {
			String hqlQuery = "select u " + "from GregDColori u " + "where u.dataCancellazione is null "
					+ "and u.rgb = :rgb " + "order by u.rgb ";
			TypedQuery<GregDColori> query = em.createQuery(hqlQuery, GregDColori.class);
			query.setParameter("rgb", rgb);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRPrestReg1UtenzeRegionali1 saveRPrestUtenza(GregRPrestReg1UtenzeRegionali1 prestUtenza) {
		return em.merge(prestUtenza);
	}

	public GregDProgrammaMissione findProgMissioneByCod(String codMissione) {
		TypedQuery<GregDProgrammaMissione> query = em
				.createNamedQuery("GregDProgrammaMissione.findBySiglaProgMissNotDeleted", GregDProgrammaMissione.class);
		query.setParameter("codMissione", codMissione);
		return query.getSingleResult();
	}

	public GregRPrestReg1UtenzeRegionali1ProgrammaMissione saveRPrestUtenzaMissione(
			GregRPrestReg1UtenzeRegionali1ProgrammaMissione prestUtenzaMissione) {
		return em.merge(prestUtenzaMissione);
	}

	public GregDTipologiaSpesa findSpesaByCod(String codSpesa) {
		TypedQuery<GregDTipologiaSpesa> query = em.createNamedQuery("GregDTipologiaSpesa.findByCodSpesaNotDeleted",
				GregDTipologiaSpesa.class);
		query.setParameter("codSpesa", codSpesa);
		return query.getSingleResult();
	}

	public GregRTipologiaSpesaPrestReg1 saveRPrestUtenzaSpesa(GregRTipologiaSpesaPrestReg1 prestUtenzaSpesa) {
		return em.merge(prestUtenzaSpesa);
	}

	public GregTPrestazioniMinisteriali findPrestazioneMinById(Integer idPrestazioneMin) {
		TypedQuery<GregTPrestazioniMinisteriali> query = em.createNamedQuery(
				"GregTPrestazioniMinisteriali.findByIdMinNotDeleted", GregTPrestazioniMinisteriali.class);
		query.setParameter("idPrestazioneMin", idPrestazioneMin);
		return query.getSingleResult();
	}

	public GregRPrestReg1PrestMinist saveRPrest1PrestMin(GregRPrestReg1PrestMinist prest1PrestMin) {
		return em.merge(prest1PrestMin);
	}

	public GregTPrestazioniRegionali2 findPrestazione2ById(Integer idPrestazione2) {
		TypedQuery<GregTPrestazioniRegionali2> query = em.createNamedQuery(
				"GregTPrestazioniRegionali2.findByIdPrest2NotDeleted", GregTPrestazioniRegionali2.class);
		query.setParameter("idPrestazione2", idPrestazione2);
		return query.getSingleResult();
	}

	public GregTPrestazioniRegionali2 findPrestazione2ByIdDataInizio(Integer idPrestazione2, Date dataInizio) {
		String hqlQuery = "SELECT g FROM GregTPrestazioniRegionali2 g WHERE g.idPrestReg2= :idPrestazione2 And DATE_TRUNC('second', g.dataInizioValidita) = DATE_TRUNC('second', cast(:dataInizio as timestamp)) AND g.dataCancellazione IS NULL";
		TypedQuery<GregTPrestazioniRegionali2> query = em.createQuery(hqlQuery, GregTPrestazioniRegionali2.class);
		query.setParameter("idPrestazione2", idPrestazione2);
		query.setParameter("dataInizio", dataInizio);
		return query.getSingleResult();
	}

	public GregRPrestReg1PrestReg2 saveRPrest1Prest2(GregRPrestReg1PrestReg2 prest1Prest2) {
		return em.merge(prest1Prest2);
	}

	public GregRPrestReg2UtenzeRegionali2 saveRPrest2Utenza(GregRPrestReg2UtenzeRegionali2 prestUtenza) {
		return em.merge(prestUtenza);
	}

	public GregDVoceIstat findPrestVoceIstat(Integer idVoce) {
		try {
			String hqlQuery = "select tip " + "from GregDVoceIstat tip " + "where tip.dataCancellazione is null "
					+ "and tip.idVoceIstat = :idVoce " + "order by tip.codVoceIstat ";

			TypedQuery<GregDVoceIstat> query = em.createQuery(hqlQuery, GregDVoceIstat.class);
			query.setParameter("idVoce", idVoce);
			GregDVoceIstat result = (GregDVoceIstat) query.getSingleResult();

			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	public GregRCatUteVocePrestReg2Istat saveRPrest2PrestIstat(GregRCatUteVocePrestReg2Istat prestUtenza) {
		return em.merge(prestUtenza);
	}

	public GregTNomenclatoreNazionale findNomenclatore(Integer idNome) {
		String hqlQuery = "select tip " + "from GregTNomenclatoreNazionale tip "
				+ "where tip.dataCancellazione is null " + "and tip.idNomenclatoreNazionale = :idNome "
				+ "order by tip.codNomenclatoreNazionale ";

		TypedQuery<GregTNomenclatoreNazionale> query = em.createQuery(hqlQuery, GregTNomenclatoreNazionale.class);
		query.setParameter("idNome", idNome);
		GregTNomenclatoreNazionale result = (GregTNomenclatoreNazionale) query.getSingleResult();

		return result;
	}

	public GregRNomencNazPrestReg2 saveRPrest2Nomencl(GregRNomencNazPrestReg2 prestUtenza) {
		return em.merge(prestUtenza);
	}

}
