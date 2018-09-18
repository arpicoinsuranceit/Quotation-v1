package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.dao.MediGridDao;
import org.arpicoinsurance.groupit.main.dao.MedicalReqDao;
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.model.MediTestGrid;
import org.arpicoinsurance.groupit.main.model.MedicalReq;
import org.arpicoinsurance.groupit.main.service.HealthRequirmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class HealthRequirmentsDetailsServiceImpl implements HealthRequirmentsService {

	@Autowired
	private MediGridDao mediGridDao;

	@Autowired
	private MedicalReqDao medicalReqDao;

	@Override
	public HashMap<String, Object> getSumAtRiskDetailsMainLife(QuotationCalculation calculation) {

		// System.out.println("called mainlife");
		// +
		// System.out.println(calculation.get_personalInfo().getBsa());

		HashMap<String, Object> details = new HashMap<>();

		String product = calculation.get_product();

		String mediGrade = "DEF";

		Double riskCurrent = 0.0;

		if (calculation.get_personalInfo().getmPreviousSumAtRisk() != null
				&& calculation.get_personalInfo().getmPreviousSumAtRisk() > 0) {
			riskCurrent = calculation.get_personalInfo().getmPreviousSumAtRisk();
		}
		switch (product) {
		case "AIB":
			riskCurrent = 0.0;
			break;
		case "AIP":
			riskCurrent = 0.0;
			break;
		case "ARP":
			riskCurrent = riskCurrent + calculateRickArpAsipAtrmEndMainlife(calculation);
			riskCurrent = riskCurrent + calculation.get_personalInfo().getBsa();
			break;
		case "ASFP":
			riskCurrent = riskCurrent + new BigDecimal(calculation.get_personalInfo().getTerm())
					.multiply(new BigDecimal(12).multiply(new BigDecimal(calculation.get_personalInfo().getMsfb())))
					.doubleValue();
			riskCurrent = riskCurrent / 2;
			riskCurrent = riskCurrent + calculateRickArpAsipAtrmEndMainlife(calculation);
			break;
		case "ASIP":
			riskCurrent = riskCurrent + calculateRickArpAsipAtrmEndMainlife(calculation);
			riskCurrent = riskCurrent + calculation.get_personalInfo().getBsa();
			break;
		case "ATRM":
			riskCurrent = riskCurrent + calculateRickArpAsipAtrmEndMainlife(calculation);
			riskCurrent = riskCurrent + calculation.get_personalInfo().getBsa();
			break;
		case "DTA":
			riskCurrent = riskCurrent + calculation.get_personalInfo().getBsa();
			mediGrade = "DTG";
			break;
		case "DTAPL":
			riskCurrent = riskCurrent + calculation.get_personalInfo().getBsa();
			mediGrade = "DTG";
			break;
		case "END1":
			riskCurrent = riskCurrent + calculateRickArpAsipAtrmEndMainlife(calculation);
			riskCurrent = riskCurrent + calculation.get_personalInfo().getBsa();
			break;
		case "INVP":
			riskCurrent = riskCurrent + calculateRickArpAsipAtrmEndMainlife(calculation);
			riskCurrent = riskCurrent + calculation.get_personalInfo().getBsa();
			break;
		case "ARTM":
			riskCurrent = riskCurrent + calculateRickArpAsipAtrmEndMainlife(calculation);
			break;
		default:
			break;
		}

		ArrayList<String> medicalReqList = getHealthDetails(riskCurrent, calculation, "M", mediGrade);
		ArrayList<String> medicalReports = new ArrayList<>();
		details.put("reqListMain", medicalReqList);
		// if (riskCurrent > 0) {
		try {
			medicalReqList = removeUnwanded(medicalReqList);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			for (String medCode : medicalReqList) {

				MedicalReq medicalReq = medicalReqDao.findOneByMedCode(medCode);
				medicalReports.add(medicalReq.getMedName());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		details.put("sumAtRisk", riskCurrent);
		details.put("reqRepoetsMain", medicalReports);

		// }

		return details;
	}

	private ArrayList<String> removeUnwanded(ArrayList<String> medicalReqList) {
		if (medicalReqList != null && medicalReqList.size() > 0) {
			if (medicalReqList.contains("F") && medicalReqList.contains("J")) {
				medicalReqList.remove("F");
			}
		}
		return medicalReqList;
	}

	@Override
	public HashMap<String, Object> getSumAtRiskDetailsSpouse(QuotationCalculation calculation) {
		String product = calculation.get_product();

		HashMap<String, Object> details = new HashMap<>();

		Double riskCurrent = 0.0;

		if (calculation.get_personalInfo().getsPreviousSumAtRisk() != null
				&& calculation.get_personalInfo().getsPreviousSumAtRisk() > 0) {
			riskCurrent = calculation.get_personalInfo().getsPreviousSumAtRisk();

		}
		String mediGrade = "DEF";

		switch (product) {
		case "AIB":
			riskCurrent = 0.0;
			break;
		case "AIP":
			riskCurrent += 0.0;
			break;
		case "ARP":
			riskCurrent += calculateRickArpAsipAtrmEndSpouse(calculation);
			break;
		case "ASFP":

			break;
		case "ASIP":
			riskCurrent += calculateRickArpAsipAtrmEndSpouse(calculation);
			break;
		case "ATRM":
			riskCurrent += calculateRickArpAsipAtrmEndSpouse(calculation);
			break;
		case "DTA":
			mediGrade += "DTG";
			riskCurrent += calculation.get_personalInfo().getBsa();
			break;
		case "DTAPL":
			mediGrade += "DTG";
			riskCurrent += calculation.get_personalInfo().getBsa();
			break;
		case "END1":
			riskCurrent += calculateRickArpAsipAtrmEndSpouse(calculation);
			break;
		case "INVP":
			riskCurrent += calculateRickArpAsipAtrmEndSpouse(calculation);
			break;
		case "ARTM":
			riskCurrent = 0.0;
			break;
		default:
			break;
		}

		// System.out.println("Sum at risk : " + riskCurrent);

		ArrayList<String> medicalReqList = getHealthDetails(riskCurrent, calculation, "S", mediGrade);
		ArrayList<String> medicalReports = new ArrayList<>();

		//medicalReqList = removeUnwanded(medicalReqList);
		try {
			for (String medCode : medicalReqList) {

				MedicalReq medicalReq = medicalReqDao.findOneByMedCode(medCode);
				medicalReports.add(medicalReq.getMedName());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		details.put("sumAtRisk", riskCurrent);
		details.put("reqRepoetsMain", medicalReports);
		details.put("reqListMain", medicalReqList);
		return details;
	}

	private Double calculateRickArpAsipAtrmEndMainlife(QuotationCalculation calculation) {

		BigDecimal sumAtRisk = new BigDecimal(0.0);

		ArrayList<Benifict> benifictListM = null;

		if (calculation.get_riderDetails() != null) {
			benifictListM = calculation.get_riderDetails().get_mRiders();
		}

		if (benifictListM != null) {

			for (Benifict benifict : benifictListM) {

				String type = benifict.getType();

				switch (type) {

				case "L2":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
					break;
				case "ATPB":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
					break;
				case "FEB":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
					break;
				case "CIB":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()).multiply(new BigDecimal(0.5)));
					break;
				case "MFIBD":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()).multiply(
							new BigDecimal(12).multiply(new BigDecimal(calculation.get_personalInfo().getTerm())
									.multiply(new BigDecimal(0.5)))));
					break;
				case "MFIBDT":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()).multiply(
							new BigDecimal(12).multiply(new BigDecimal(calculation.get_personalInfo().getTerm())
									.multiply(new BigDecimal(0.5)))));
					break;
				default:
					break;
				}

			}
		}

		return sumAtRisk.doubleValue();
	}

	private Double calculateRickArpAsipAtrmEndSpouse(QuotationCalculation calculation) {
		BigDecimal sumAtRisk = new BigDecimal(0.0);

		ArrayList<Benifict> benifictListS = null;

		if (calculation.get_riderDetails() != null) {
			benifictListS = calculation.get_riderDetails().get_sRiders();
		}

		if (benifictListS != null) {
			for (Benifict benifict : benifictListS) {

				String type = benifict.getType();

				switch (type) {
				case "BSAS":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
					break;
				case "SCB":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
					break;
				case "FEBS":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
					break;
				case "CIBS":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()).multiply(new BigDecimal(0.5)));
					break;
				case "SCIB":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()).multiply(new BigDecimal(0.5)));
					break;
				default:
					break;
				}

			}
		}

		return sumAtRisk.doubleValue();
	}

	private ArrayList<String> getHealthDetails(Double riskCurrent, QuotationCalculation calculation, String custType,
			String mediGrade) {

		// System.out.println("called get health
		// ???????????????????//////////////////////////");

		ArrayList<String> mediTestList = new ArrayList<>();

		if (custType.equalsIgnoreCase("M")) {

			ArrayList<Benifict> benifictListM = null;

			if (calculation.get_riderDetails() != null) {
				benifictListM = calculation.get_riderDetails().get_mRiders();
			}

			boolean isShcbi = false;
			boolean isShcbf = false;

			Double shcb = 0.0;

			boolean isHcbi = false;
			boolean isHcbf = false;

			Double hcb = 0.0;

			if (benifictListM != null) {
				for (Benifict benifict : benifictListM) {

					// System.out.println(benifict.getType()
					// + " main beneficts /////////////////////");

					switch (benifict.getType()) {
					case "SUHRB":
						isShcbi = true;
						shcb = benifict.getSumAssured();
						break;
					case "SHCBI":
						isShcbi = true;
						shcb = benifict.getSumAssured();
						break;
					case "SHCBF":
						isShcbf = true;
						shcb = benifict.getSumAssured();
						break;
					case "HRB":
						isHcbi = true;
						hcb = benifict.getSumAssured();
						break;
					case "HRBI":
						isHcbi = true;
						hcb = benifict.getSumAssured();
						break;
					case "HCBI":
						isHcbi = true;
						hcb = benifict.getSumAssured();
						break;
					case "HCBF":
						isHcbf = true;
						hcb = benifict.getSumAssured();
						break;

					default:
						break;
					}
				}
			}

			// System.out.println("mage " + calculation.get_personalInfo().getMage());
			// System.out.println(riskCurrent);
			List<MediTestGrid> grid = mediGridDao
					.findByAgeFromLessThanEqualAndAgeToGreaterThanEqualAndSumAssuredFromLessThanEqualAndSumAssuredToGreaterThanEqual(
							calculation.get_personalInfo().getMage(), calculation.get_personalInfo().getMage(),
							riskCurrent, riskCurrent);

			// System.out.println(grid.size());

			for (MediTestGrid mediTestGrid : grid) {
				if (mediTestGrid.getMediGrade().equals(mediGrade)) {
					mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
				}
			}

			if (isShcbi || isShcbf) {
				System.out.println("SHCB");
				List<MediTestGrid> gridSHCBM = mediGridDao
						.findByAgeFromLessThanEqualAndAgeToGreaterThanEqualAndSumAssuredFromLessThanEqualAndSumAssuredToGreaterThanEqual(
								calculation.get_personalInfo().getMage(), calculation.get_personalInfo().getMage(),
								shcb, shcb);
				System.out.println(calculation.get_personalInfo().getMage());
				System.out.println(calculation.get_personalInfo().getMage());
				gridSHCBM.forEach(e -> System.out.println(e.getTests()));
				
				for (MediTestGrid mediTestGrid : gridSHCBM) {
					if (mediTestGrid.getMediGrade().equals("SHCB")) {
						mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
					}
				}
			}

			if (isHcbi || isHcbf) {

				// System.out.println("HCBI");

				List<MediTestGrid> gridHCBM = mediGridDao
						.findByAgeFromLessThanEqualAndAgeToGreaterThanEqualAndSumAssuredFromLessThanEqualAndSumAssuredToGreaterThanEqual(
								calculation.get_personalInfo().getMage(), calculation.get_personalInfo().getMage(), hcb,
								hcb);

				for (MediTestGrid mediTestGrid : gridHCBM) {
					if (mediTestGrid.getMediGrade().equals("HCB")) {
						mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
					}
				}

			}
		}

		if (custType.equalsIgnoreCase("S")) {
			ArrayList<Benifict> benifictListS = null;

			if (calculation.get_riderDetails() != null) {
				benifictListS = calculation.get_riderDetails().get_sRiders();
			}

			boolean isShcbi = false;
			boolean isShcbf = false;

			Double shcb = 0.0;

			boolean isHcbi = false;
			boolean isHcbf = false;

			Double hcb = 0.0;

			if (benifictListS != null) {

				for (Benifict benifict : benifictListS) {

					// System.out.println(benifict.getType()
					// + " spouse beneficts /////////////////////");

					switch (benifict.getType()) {
					case "SUHRBS":
						isShcbi = true;
						shcb = benifict.getSumAssured();
						break;
					case "SHCBIS":
						isShcbi = true;
						shcb = benifict.getSumAssured();
						break;
					case "SHCBFS":
						isShcbf = true;
						shcb = benifict.getSumAssured();
						break;
					case "HRBS":
						isHcbi = true;
						hcb = benifict.getSumAssured();
						break;
					case "HRBIS":
						isHcbi = true;
						hcb = benifict.getSumAssured();
						break;
					case "HCBIS":
						isHcbi = true;
						hcb = benifict.getSumAssured();
						break;
					case "HCBFS":
						isHcbf = true;
						hcb = benifict.getSumAssured();
						break;

					default:
						break;
					}
				}
			}

			if (calculation.get_personalInfo().getSage() == null) {
				return new ArrayList<>(new HashSet<>(mediTestList));
			}

			List<MediTestGrid> grid = mediGridDao
					.findByAgeFromLessThanEqualAndAgeToGreaterThanEqualAndSumAssuredFromLessThanEqualAndSumAssuredToGreaterThanEqual(
							calculation.get_personalInfo().getSage(), calculation.get_personalInfo().getSage(),
							riskCurrent, riskCurrent);

			for (MediTestGrid mediTestGrid : grid) {

				if (mediTestGrid.getMediGrade().equals(mediGrade)) {
					mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
				}
			}

			if (isShcbi || isShcbf) {

				// System.out.println(shcb);
				// System.out.println(calculation.get_personalInfo().getSage());

				List<MediTestGrid> gridSHCBM = mediGridDao
						.findByAgeFromLessThanEqualAndAgeToGreaterThanEqualAndSumAssuredFromLessThanEqualAndSumAssuredToGreaterThanEqual(
								calculation.get_personalInfo().getSage(), calculation.get_personalInfo().getSage(),
								shcb, shcb);
				// System.out.println(gridSHCBM.size());
				for (MediTestGrid mediTestGrid : gridSHCBM) {
					if (mediTestGrid.getMediGrade().equals("SHCB")) {
						// System.out.println("SHCB");
						mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
					}
				}

			}

			if (isHcbi || isHcbf) {

				// System.out.println(hcb);
				// System.out.println(calculation.get_personalInfo().getSage());

				List<MediTestGrid> gridHCBM = mediGridDao
						.findByAgeFromLessThanEqualAndAgeToGreaterThanEqualAndSumAssuredFromLessThanEqualAndSumAssuredToGreaterThanEqual(
								calculation.get_personalInfo().getSage(), calculation.get_personalInfo().getSage(), hcb,
								hcb);
				for (MediTestGrid mediTestGrid : gridHCBM) {
					if (mediTestGrid.getMediGrade().equals("HCB")) {
						mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
					}
				}

			}
		}

		return new ArrayList<>(new HashSet<>(mediTestList));
	}

}
