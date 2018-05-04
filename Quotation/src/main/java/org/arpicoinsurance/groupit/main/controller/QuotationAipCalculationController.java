package org.arpicoinsurance.groupit.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.arpicoinsurance.groupit.main.helper.AIPCalResp;
import org.arpicoinsurance.groupit.main.helper.AipCalShedule;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.Plan;
import org.arpicoinsurance.groupit.main.service.AIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class QuotationAipCalculationController {

	@Autowired
	private AIPService aipService;

	@RequestMapping(value = "/aipCal", method = RequestMethod.POST)
	public AIPCalResp calculateAIP(@RequestBody Plan plan) {

		Double contribution = plan.get_bsa();
		try {
			AIPCalResp aipCalResp = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.2, 9.0, contribution,
					new Date(), plan.get_frequance(), false, true);
			AIPCalResp aipCalResp11 = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.2, 10.0, contribution,
					new Date(), plan.get_frequance(), false, false);
			AIPCalResp aipCalResp12 = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.2, 11.0, contribution,
					new Date(), plan.get_frequance(), false, false);

			aipCalResp.setMaturaty10(aipCalResp11.getMaturaty());
			aipCalResp.setMaturaty12(aipCalResp12.getMaturaty());

			return aipCalResp;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/aipshedule", method = RequestMethod.POST)
	public ArrayList<AipCalShedule> loadSheduleAIP(@RequestBody Plan plan) {
		Double contribution = plan.get_bsa();
		try {
			AIPCalResp aipCalResp = aipService.calculateAIPMaturaty(plan.get_term(), 2.0, 0.2, 9.0, contribution,
					new Date(), plan.get_frequance(), true, false);

			ArrayList<AipCalShedule> aipCalSchedule = (ArrayList<AipCalShedule>) aipCalResp.getAipCalShedules();
			if (aipCalSchedule != null && !aipCalSchedule.isEmpty()) {
				return aipCalSchedule;
			}

			return null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/aipSavequo/{id}", method = RequestMethod.POST)
	public HashMap<String, Object> saveAIP(@RequestBody InvpSavePersonalInfo _invpSaveQuotation, @PathVariable Integer id) {
		String resp = "Fail";
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		try {
			if (id != null) {
				if (_invpSaveQuotation != null) {

					if ((Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mAge())
							+ _invpSaveQuotation.get_plan().get_term()) <= 100) {

					//if ((Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mAge())
					//		+ _invpSaveQuotation.get_plan().get_term()) <= 70) {
						responseMap = aipService.saveQuotation(_invpSaveQuotation, id);
					} else {
						responseMap.replace("status", "Term is too large for Mainlife age");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return responseMap;
	}

	@RequestMapping(value = "/quoAipEdit/{userId}/{qdId}", method = RequestMethod.POST)
	public HashMap<String, Object> editAip(@RequestBody InvpSavePersonalInfo _invpSaveQuotation, @PathVariable("userId") Integer userId,
			@PathVariable("qdId") Integer qdId) {
		String resp = "Fail";
		HashMap<String, Object> responseMap = new HashMap<>();
		responseMap.put("status", "fail");
		try {
			if (userId != null) {
				if (qdId != null) {
					if (_invpSaveQuotation != null) {
		if ((Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mAge())
								+ _invpSaveQuotation.get_plan().get_term()) <= 100) {
						/*if ((Integer.parseInt(_invpSaveQuotation.get_mainlife().get_mAge())
								+ _invpSaveQuotation.get_plan().get_term()) <= 70) {*/
							responseMap = aipService.editQuotation(_invpSaveQuotation, userId, qdId);
						} else {
							responseMap.replace("status", "Term is too large for Mainlife age");
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return responseMap;
	}
}