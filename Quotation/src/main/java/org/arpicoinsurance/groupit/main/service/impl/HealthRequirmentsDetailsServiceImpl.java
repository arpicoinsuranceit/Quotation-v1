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

		
		System.out.println("called mainlife");

		System.out.println(calculation.get_personalInfo().getBsa());

		HashMap<String, Object> details = new HashMap<>();

		String product = calculation.get_product();

		String mediGrade = "DEF";

		Double riskCurrent = 0.0;

		System.out.println("sum at riskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk: " + calculation.get_personalInfo().getmPreviousSumAtRisk());
		
		if (calculation.get_personalInfo().getmPreviousSumAtRisk() != null
				&& calculation.get_personalInfo().getmPreviousSumAtRisk() > 0) {
			riskCurrent = calculation.get_personalInfo().getmPreviousSumAtRisk();
		}
		
		System.out.println("sum at riskkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk: " + calculation.get_personalInfo().getmPreviousSumAtRisk());

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
			riskCurrent = riskCurrent + new BigDecimal(calculation.get_personalInfo().getTerm()).multiply(new BigDecimal(12).multiply(new BigDecimal(calculation.get_personalInfo().getMsfb()))).doubleValue();
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
		default:
			break;
		}

		if (riskCurrent > 0) {

			ArrayList<String> medicalReqList = getHealthDetails(riskCurrent, calculation, "M", mediGrade);

			ArrayList<String> medicalReports = new ArrayList<>();
			try {
				for (String medCode : medicalReqList) {

					MedicalReq medicalReq = medicalReqDao.findOneByMedCode(medCode);
					medicalReports.add(medicalReq.getMedName());

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			details.put("sumAtRisk", riskCurrent);
			details.put("reqListMain", medicalReqList);
			details.put("reqRepoetsMain", medicalReports);

		}

		return details;
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
			riskCurrent += calculation.get_personalInfo().getBsa();
			break;
		case "ASFP":
			riskCurrent += new BigDecimal(calculation.get_personalInfo().getTerm()).multiply(new BigDecimal(12).multiply(new BigDecimal(calculation.get_personalInfo().getMsfb()))).doubleValue();
			break;
		case "ASIP":
			riskCurrent += calculateRickArpAsipAtrmEndSpouse(calculation);
			riskCurrent += calculation.get_personalInfo().getBsa();
			break;
		case "ATRM":
			riskCurrent += calculateRickArpAsipAtrmEndSpouse(calculation);
			riskCurrent += calculation.get_personalInfo().getBsa();
			break;
		case "DTA":
			riskCurrent += calculation.get_personalInfo().getBsa();
			mediGrade += "DTG";
			break;
		case "DTAPL":
			riskCurrent += calculation.get_personalInfo().getBsa();
			mediGrade += "DTG";
			break;
		case "END1":
			riskCurrent += calculateRickArpAsipAtrmEndSpouse(calculation);
			riskCurrent += calculation.get_personalInfo().getBsa();
			break;
		case "INVP":
			riskCurrent += calculateRickArpAsipAtrmEndSpouse(calculation);
			riskCurrent += calculation.get_personalInfo().getBsa();
			break;
		default:
			break;
		}
		

		System.out.println("Sum at risk : " + riskCurrent);
		
		if (riskCurrent > 0) {

			ArrayList<String> medicalReqList = getHealthDetails(riskCurrent, calculation, "S", mediGrade);

			ArrayList<String> medicalReports = new ArrayList<>();

			try {
				for (String medCode : medicalReqList) {

					MedicalReq medicalReq = medicalReqDao.findOneByMedCode(medCode);
					medicalReports.add(medicalReq.getMedName());

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			details.put("sumAtRisk", riskCurrent);
			details.put("reqListMain", medicalReqList);
			details.put("reqRepoetsMain", medicalReports);
		}

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
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
					break;
				case "MFIBDT":
					sumAtRisk = sumAtRisk.add(new BigDecimal(benifict.getSumAssured()));
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

		ArrayList<String> mediTestList = new ArrayList<>();

		if (custType.equalsIgnoreCase("M")) {

			ArrayList<Benifict> benifictListM = null;

			if (calculation.get_riderDetails() != null) {
				benifictListM = calculation.get_riderDetails().get_mRiders();
			}

			boolean isShcbi = false;
			boolean isShcbf = false;

			boolean isHcbi = false;
			boolean isHcbf = false;
			if (benifictListM != null) {
				for (Benifict benifict : benifictListM) {
					switch (benifict.getType()) {
					case "SUHRB":
						isShcbi = true;
						break;
					case "SHCBI":
						isShcbi = true;
						break;
					case "SHCBF":
						isShcbf = true;
						break;
					case "HRB":
						isHcbi = true;
						break;
					case "HCBI":
						isHcbi = true;
						break;
					case "HCBF":
						isHcbf = true;
						break;

					default:
						break;
					}
				}
			}

			System.out.println("mage " + calculation.get_personalInfo().getMage());
			System.out.println(riskCurrent);
			List<MediTestGrid> grid = mediGridDao
					.findByAgeFromLessThanEqualAndAgeToGreaterThanEqualAndSumAssuredFromLessThanEqualAndSumAssuredToGreaterThanEqual(
							calculation.get_personalInfo().getMage(), calculation.get_personalInfo().getMage(),
							riskCurrent, riskCurrent);

			System.out.println(grid.size());

			for (MediTestGrid mediTestGrid : grid) {
				if (mediTestGrid.getMediGrade().equals(mediGrade)) {
					mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
				}
			}

			if (isShcbi || isShcbf) {
				for (MediTestGrid mediTestGrid : grid) {
					if (mediTestGrid.getMediGrade().equals("SHCB")) {
						mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
					}
				}

			}

			if (isHcbi || isHcbf) {
				for (MediTestGrid mediTestGrid : grid) {
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

			boolean isHcbi = false;
			boolean isHcbf = false;

			if (benifictListS != null) {

				for (Benifict benifict : benifictListS) {
					switch (benifict.getType()) {
					case "SUHRBS":
						isShcbi = true;
						break;
					case "SHCBIS":
						isShcbi = true;
						break;
					case "SHCBFS":
						isShcbf = true;
						break;
					case "HRBS":
						isHcbi = true;
						break;
					case "HCBIS":
						isHcbi = true;
						break;
					case "HCBFS":
						isHcbf = true;
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
				for (MediTestGrid mediTestGrid : grid) {
					if (mediTestGrid.getMediGrade().equals("SHCBS")) {
						mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
					}
				}

			}

			if (isHcbi || isHcbf) {
				for (MediTestGrid mediTestGrid : grid) {
					if (mediTestGrid.getMediGrade().equals("HCBS")) {
						mediTestList.addAll(Arrays.asList(mediTestGrid.getTests().split(",")));
					}
				}

			}
		}

		return new ArrayList<>(new HashSet<>(mediTestList));
	}

}
