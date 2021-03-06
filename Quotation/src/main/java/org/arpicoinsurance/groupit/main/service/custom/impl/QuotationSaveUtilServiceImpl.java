package org.arpicoinsurance.groupit.main.service.custom.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.common.DateConverter;
import org.arpicoinsurance.groupit.main.dao.BenefitsDao;
import org.arpicoinsurance.groupit.main.dao.RateCardARPDao;
import org.arpicoinsurance.groupit.main.helper.Benifict;
import org.arpicoinsurance.groupit.main.helper.Children;
import org.arpicoinsurance.groupit.main.helper.DTAShedule;
import org.arpicoinsurance.groupit.main.helper.InvpSavePersonalInfo;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.RiderDetails;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.CustChildDetails;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.RateCardARP;
import org.arpicoinsurance.groupit.main.model.Shedule;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.CalculateBenifictTermService;
import org.arpicoinsurance.groupit.main.service.custom.QuotationSaveUtilService;
import org.arpicoinsurance.groupit.main.service.rider.CIBCService;
import org.arpicoinsurance.groupit.main.service.rider.HBCService;
import org.arpicoinsurance.groupit.main.service.rider.HRBIService;
import org.arpicoinsurance.groupit.main.service.rider.SUHRBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuotationSaveUtilServiceImpl implements QuotationSaveUtilService {

	@Autowired
	private BenefitsDao benefitsDao;

	@Autowired
	private CIBCService cibcService;

	@Autowired
	private SUHRBCService suhrbcService;

	@Autowired
	private HBCService hbcService;

	@Autowired
	private HRBIService hrbiService;
	
	@Autowired
	private RateCardARPDao rateCardARPDao;

	
	@Autowired
	private CalculateBenifictTermService calculateBenefictTerm;

	public CustomerDetails getCustomerDetail(Occupation occupationMainlife, InvpSavePersonalInfo get_personalInfo,
			Users user) throws Exception {
		CustomerDetails mainLifeDetail = null;
		try {
			mainLifeDetail = new CustomerDetails();
			mainLifeDetail.setCustName(get_personalInfo.get_mainlife().get_mName());
			mainLifeDetail.setCustCivilStatus(get_personalInfo.get_mainlife().get_mCivilStatus());
			mainLifeDetail.setCustCreateBy(user.getUser_Name());
			mainLifeDetail.setCustCreateDate(new Date());
			mainLifeDetail.setCustDob(new DateConverter().stringToDate(get_personalInfo.get_mainlife().get_mDob()));
			mainLifeDetail.setCustEmail(get_personalInfo.get_mainlife().get_mEmail());
			mainLifeDetail.setCustGender(get_personalInfo.get_mainlife().get_mGender());
			mainLifeDetail.setCustNic(get_personalInfo.get_mainlife().get_mNic());
			mainLifeDetail.setCustTel(get_personalInfo.get_mainlife().get_mMobile());
			mainLifeDetail.setCustTitle(get_personalInfo.get_mainlife().get_mTitle());
			mainLifeDetail.setOccupation(occupationMainlife);

			return mainLifeDetail;
		} finally {
			if (mainLifeDetail != null) {
				mainLifeDetail = null;
			}
		}
	}

	public CustomerDetails getSpouseDetail(Occupation occupationSpouse, InvpSavePersonalInfo get_personalInfo,
			Users user) throws Exception {
		CustomerDetails spouseDetail = null;
		if (get_personalInfo.get_spouse() != null && get_personalInfo.get_spouse().is_sActive() == true) {
			try {

				spouseDetail = new CustomerDetails();
				spouseDetail.setCustName(get_personalInfo.get_spouse().get_sName());
				spouseDetail.setCustCivilStatus("M");
				spouseDetail.setCustCreateBy(user.getUser_Name());
				spouseDetail.setCustCreateDate(new Date());
				spouseDetail.setCustDob(new DateConverter().stringToDate(get_personalInfo.get_spouse().get_sDob()));
				spouseDetail.setCustGender(get_personalInfo.get_spouse().get_sGender());
				spouseDetail.setCustNic(get_personalInfo.get_spouse().get_sNic());
				spouseDetail.setCustTitle(get_personalInfo.get_spouse().get_sTitle());
				spouseDetail.setOccupation(occupationSpouse);

				return spouseDetail;
			} finally {
				if (spouseDetail != null) {
					spouseDetail = null;
				}
			}
		}
		return null;
	}

	public ArrayList<Child> getChilds(ArrayList<Children> get_childrenList) throws Exception {
		ArrayList<Child> childList = null;
		if (get_childrenList != null && get_childrenList.size() > 0) {
			try {
				childList = new ArrayList<Child>();
				for (Children children : get_childrenList) {
					Child child = new Child();
					child.setChildName(children.get_cName());
					child.setChildGender(children.get_cTitle());
					////System.out.println("children.get_cTitle()" + children.get_cTitle());
					child.setChildDob(new DateConverter().stringToDate(children.get_cDob()));
					child.setChildNic(children.get_cNic());
					child.setChildRelation(children.get_cTitle().equals("F") ? "Daughter" : "Son");

					childList.add(child);
				}

				return childList;
			} finally {
				if (childList != null) {
					childList = null;
				}
			}
		}
		return null;

	}

	public QuotationDetails getQuotationDetail(QuotationQuickCalResponse calResp, QuotationCalculation calculation,
			Double lifePos) throws Exception {
		QuotationDetails quotationDetails = null;
		CalculationUtils calculationUtils = null;
		try {
			calculationUtils = new CalculationUtils();
			Double adminFee = calculationUtils.getAdminFee(calculation.get_personalInfo().getFrequance());
			Double taxAmount = calculationUtils.getTaxAmount(adminFee + calResp.getTotPremium() - calResp.getExtraOE());

			quotationDetails = new QuotationDetails();
			quotationDetails.setBaseSum(calculation.get_personalInfo().getBsa());
			// quotationDetails.setInterestRate(8.5);
			quotationDetails.setAdminFee(adminFee);
			quotationDetails.setLifePos(lifePos);
			quotationDetails.setInvestmentPos(calResp.getBasicSumAssured() - lifePos);

			quotationDetails.setPayMode(calculation.get_personalInfo().getFrequance());
			quotationDetails.setPaingTerm(calculation.get_personalInfo().getPayingterm());
			quotationDetails.setPolTerm(calculation.get_personalInfo().getTerm());
			quotationDetails.setPolicyFee(calculationUtils.getPolicyFee());
			quotationDetails.setTaxAmount(taxAmount);
			quotationDetails.setSumAtRiskMain((Double) calResp.getMainLifeHealthReq().get("sumAtRisk"));
			try {
			quotationDetails.setSumAtRiskSpouse((Double) calResp.getSpouseHealthReq().get("sumAtRisk"));
			}catch (Exception e) {
				quotationDetails.setSumAtRiskSpouse(0.0);
			}
			quotationDetails.setQuotationquotationCreateDate(new Date());
			quotationDetails.setPremium(calResp.getBasicSumAssured());
			switch (calculation.get_personalInfo().getFrequance()) {
			case "M":
				////System.out.println(calResp.getBasicSumAssured());
				////System.out.println(calResp.getTotPremium());
				////System.out.println(calResp.getExtraOE());
				////System.out.println(adminFee);
				////System.out.println(taxAmount);
				quotationDetails.setPremiumMonth(calResp.getBasicSumAssured());
				quotationDetails.setPremiumMonthT(calResp.getTotPremium());
				break;
			case "Q":
				////System.out.println(calResp.getBasicSumAssured());
				////System.out.println(calResp.getTotPremium());
				////System.out.println(calResp.getExtraOE());
				////System.out.println(adminFee);
				////System.out.println(taxAmount);
				quotationDetails.setPremiumQuater(calResp.getBasicSumAssured());
				quotationDetails.setPremiumQuaterT(calResp.getTotPremium());
				break;
			case "H":
				////System.out.println(calResp.getBasicSumAssured());
				////System.out.println(calResp.getTotPremium());
				////System.out.println(calResp.getExtraOE());
				////System.out.println(adminFee);
				////System.out.println(taxAmount);
				quotationDetails.setPremiumHalf(calResp.getBasicSumAssured());
				quotationDetails.setPremiumHalfT(calResp.getTotPremium());
				break;
			case "Y":
				////System.out.println(calResp.getBasicSumAssured());
				////System.out.println(calResp.getTotPremium());
				////System.out.println(calResp.getExtraOE());
				////System.out.println(adminFee);
				////System.out.println(taxAmount);
				quotationDetails.setPremiumYear(calResp.getBasicSumAssured());
				quotationDetails.setPremiumYearT(calResp.getTotPremium());
				break;
			case "S":
				////System.out.println(calResp.getBasicSumAssured());
				////System.out.println(calResp.getTotPremium());
				////System.out.println(calResp.getExtraOE());
				////System.out.println(adminFee);
				////System.out.println(taxAmount);
				quotationDetails.setPremiumSingle(calResp.getBasicSumAssured());
				quotationDetails.setPremiumSingleT(calResp.getTotPremium());
				break;
			default:
				break;
			}
			return quotationDetails;
		} finally {
			if (quotationDetails != null) {
				quotationDetails = null;
			}
		}

	}

	public ArrayList<Quo_Benef_Details> getBenifDetails(RiderDetails get_riderDetails,
			QuotationQuickCalResponse calResp, QuotationDetails quotationDetails, List<Children> childrenList,
			Integer integer) throws Exception {
		ArrayList<Quo_Benef_Details> benef_DetailList = null;
		
		
		try {
			benef_DetailList = new ArrayList<>();
			ArrayList<Benifict> benifictListM = get_riderDetails.get_mRiders();
			if (benifictListM != null && benifictListM.size() > 0) {
				for (Benifict benifict : benifictListM) {
					switch (benifict.getType()) {
					case "HRBI":
						benifict.setType("HCBI");
						break;
					case "HRBF":
						benifict.setType("HCBF");
						break;
					case "SUHRB":
						benifict.setType("SHCBI");
						break;

					default:
						break;
					}
					Quo_Benef_Details benef_Details = new Quo_Benef_Details();
					Benefits benifict2 = benefitsDao.findByRiderCode(benifict.getType());
					if (benifict2 != null) {
						benef_Details.setBenefit(benifict2);
					} else {
						////System.out.println("******************" + benifict.getType() + "******Error");
					}

					benef_Details.setQuotationDetails(quotationDetails);
					benef_Details.setRiderSum(benifict.getSumAssured());

					String type = benifict.getType();

					switch (type) {

					case "L2":
						benef_Details.setRiderPremium(calResp.getL2());
						benef_Details.setRiderTerm(calResp.getL2term());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "ADB":
						benef_Details.setRiderPremium(calResp.getAdb());
						benef_Details.setRiderTerm(calResp.getAdbTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "SFPO":
						benef_Details.setRiderPremium(calResp.getSfpo());
						benef_Details.setRiderTerm(calResp.getSfpoTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "ATPB":
						benef_Details.setRiderPremium(calResp.getAtpb());
						benef_Details.setRiderTerm(calResp.getAtpbTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "TPDASB":
						benef_Details.setRiderPremium(calResp.getTpdasb());
						benef_Details.setRiderTerm(calResp.getTpdasbTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "TPDB":
						benef_Details.setRiderPremium(calResp.getTpdb());
						benef_Details.setRiderTerm(calResp.getTpdbTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "PPDB":
						benef_Details.setRiderPremium(calResp.getPpdb());
						benef_Details.setRiderTerm(calResp.getPpdbTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "CIB":
						benef_Details.setRiderPremium(calResp.getCib());
						benef_Details.setRiderTerm(calResp.getCibTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "FEB":
						benef_Details.setRiderPremium(calResp.getFeb());
						benef_Details.setRiderTerm(calResp.getFebTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "MFIBD":
						benef_Details.setRiderPremium(calResp.getMifdb());
						benef_Details.setRiderTerm(calResp.getMifdbTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "MFIBT":
						benef_Details.setRiderPremium(calResp.getMifdt());
						benef_Details.setRiderTerm(calResp.getMifdtTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "MFIBDT":
						benef_Details.setRiderPremium(calResp.getMifdbt());
						benef_Details.setRiderTerm(calResp.getMifdbtTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					/*
					 * case "HRB": benef_Details.setRiderPremium(calResp.getHrb());
					 * benef_Details.setRiderTerm(calResp.getHrbTerm());
					 * benef_Details.setRierCode(type); break;
					 */
					case "HCBI":
						benef_Details.setRiderPremium(calResp.getHrbi());
						benef_Details.setRiderTerm(calResp.getHrbiTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;

					case "HCBF":
						if (calResp.getHrbf() > 0) {
							benef_Details.setRiderPremium(calResp.getHrbf());
							benef_Details.setRiderTerm(calResp.getHrbfTerm());
							benef_Details.setRierCode(type);
							benef_DetailList.add(benef_Details);
						}
						break;

					case "SHCBI":
						benef_Details.setRiderPremium(calResp.getShcbi());
						benef_Details.setRiderTerm(calResp.getShcbiTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					/*
					 * case "SUHRB": benef_Details.setRiderPremium(calResp.getSuhrb());
					 * benef_Details.setRiderTerm(calResp.getSuhrbTerm());
					 * benef_Details.setRierCode(type); break;
					 */
					case "HB":
						benef_Details.setRiderPremium(calResp.getHb());
						benef_Details.setRiderTerm(calResp.getHbTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;
					case "WPB":
						benef_Details.setRiderPremium(calResp.getWpb());
						benef_Details.setRiderTerm(calResp.getWpbTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;

					case "TPDDTA":
						benef_Details.setRiderPremium(calResp.getTpddta());
						benef_Details.setRiderTerm(calResp.getTpddtaTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;

					case "TPDDTAPL":
						benef_Details.setRiderPremium(calResp.getTpddtapl());
						benef_Details.setRiderTerm(calResp.getTpddtaplTerm());
						benef_Details.setRierCode(type);
						benef_DetailList.add(benef_Details);
						break;

					default:
						break;
					}

				}
			}

			ArrayList<Benifict> benifictListS = get_riderDetails.get_sRiders();

			if (benifictListS != null && benifictListS.size() > 0) {
				for (Benifict benifict : benifictListS) {
					Quo_Benef_Details benef_Details = new Quo_Benef_Details();
					if (benifict.getType().equals("BSAS"))
						benifict.setType("SCB");
					if (benifict.getType().equals("CIBS"))
						benifict.setType("SCIB");
					

					switch (benifict.getType()) {
					case "HRBIS":
						benifict.setType("HCBIS");
						break;
					case "HRBFS":
						benifict.setType("HCBFS");
						break;
					case "SUHRBS":
						benifict.setType("SHCBIS");
						break;

					default:
						break;
					}

					Benefits benifict2 = benefitsDao.findByRiderCode(benifict.getType());

					if (benifict2 != null) {
						benef_Details.setBenefit(benifict2);
					} else {
						////System.out.println("******************" + benifict.getType() + "******Error");
					}
					benef_Details.setBenefit(benifict2);
					benef_Details.setQuotationDetails(quotationDetails);
					benef_Details.setRiderSum(benifict.getSumAssured());

					String type = benifict.getType();

					switch (type) {

					case "SCB":
						benef_Details.setRiderPremium(calResp.getBsas());
						benef_Details.setRiderTerm(calResp.getBsasTerm());
						benef_Details.setRierCode(type);
						break;
					case "ADBS":
						benef_Details.setRiderPremium(calResp.getAdbs());
						benef_Details.setRiderTerm(calResp.getAdbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "TPDASBS":
						benef_Details.setRiderPremium(calResp.getTpdasbs());
						benef_Details.setRiderTerm(calResp.getTpdasbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "TPDBS":
						benef_Details.setRiderPremium(calResp.getTpdbs());
						benef_Details.setRiderTerm(calResp.getTpdbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "PPDBS":
						benef_Details.setRiderPremium(calResp.getPpdbs());
						benef_Details.setRiderTerm(calResp.getPpdbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "SCIB":
						benef_Details.setRiderPremium(calResp.getCibs());
						benef_Details.setRiderTerm(calResp.getCibsTerm());
						benef_Details.setRierCode(type);
						break;
					case "FEBS":
						benef_Details.setRiderPremium(calResp.getFebs());
						benef_Details.setRiderTerm(calResp.getFebsTerm());
						benef_Details.setRierCode(type);
						break;
					/*
					 * case "HRBS": benef_Details.setRiderPremium(calResp.getHrbs());
					 * benef_Details.setRiderTerm(calResp.getHrbsTerm());
					 * benef_Details.setRierCode(type); break;
					 */
					case "HCBIS":
						benef_Details.setRiderPremium(calResp.getHrbis());
						benef_Details.setRiderTerm(calResp.getHrbisTerm());
						benef_Details.setRierCode(type);
						break;
					case "HCBFS":
						benef_Details.setRiderPremium(calResp.getHrbfs());
						if(benef_Details.getRiderSum() == null) {

							benef_Details.setRiderSum(0.0);
						}
						benef_Details.setRiderTerm(calResp.getHrbfsTerm());
						benef_Details.setRierCode(type);
						break;
					/*
					 * case "SUHRBS": benef_Details.setRiderPremium(calResp.getSuhrbs());
					 * benef_Details.setRiderTerm(calResp.getSuhrbsTerm());
					 * benef_Details.setRierCode(type); break;
					 */
					case "SHCBIS":
						benef_Details.setRiderPremium(calResp.getSuhrbs());
						benef_Details.setRiderTerm(calResp.getSuhrbsTerm());
						benef_Details.setRierCode(type);
						break;
						
					case "HBS":
						benef_Details.setRiderPremium(calResp.getHbs());
						benef_Details.setRiderTerm(calResp.getHbsTerm());
						benef_Details.setRierCode(type);
						break;
					case "WPBS":
						benef_Details.setRiderPremium(calResp.getWpbs());
						benef_Details.setRiderTerm(calResp.getWpbsTerm());
						benef_Details.setRierCode(type);
						break;

					case "TPDDTAS":
						benef_Details.setRiderPremium(calResp.getTpddtas());
						benef_Details.setRiderTerm(calResp.getTpddtasTerm());
						benef_Details.setRierCode(type);
						break;

					case "JLB":
						benef_Details.setRiderPremium(calResp.getJlb());
						benef_Details.setRiderTerm(calResp.getJlbTerm());
						benef_Details.setRierCode(type);
						break;

					case "TPDDTASPL":
						benef_Details.setRiderPremium(calResp.getTpddtaspl());
						benef_Details.setRiderTerm(calResp.getTpddtasplTerm());
						benef_Details.setRierCode(type);
						break;

					case "JLBPL":
						benef_Details.setRiderPremium(calResp.getJlbpl());
						benef_Details.setRiderTerm(calResp.getJlbplTerm());
						benef_Details.setRierCode(type);
						break;

					default:
						break;
					}

					benef_DetailList.add(benef_Details);

				}
			}

			ArrayList<Benifict> benifictListC = get_riderDetails.get_cRiders();

			if (benifictListC != null && benifictListC.size() > 0) {
				for (Benifict benifict : benifictListC) {
					Quo_Benef_Details benef_Details = new Quo_Benef_Details();
					
					switch (benifict.getType()) {
					case "HRBIC":
						benifict.setType("HCBIC");
						break;
					case "HRBFC":
						benifict.setType("HCBFC");
						break;
					case "SUHRBC":
						benifict.setType("SHCBIC");
						break;

					default:
						break;
					}
					
					Benefits benifict2 = benefitsDao.findByRiderCode(benifict.getType());

					if (benifict2 != null) {
						benef_Details.setBenefit(benifict2);
					} else {
						////System.out.println("******************" + benifict.getType() + "******Error");
					}
					benef_Details.setBenefit(benifict2);
					benef_Details.setQuotationDetails(quotationDetails);
					benef_Details.setRiderSum(benifict.getSumAssured());

					String type = benifict.getType();

					switch (type) {

					case "CIBC":
						benef_Details.setRiderPremium(calResp.getCibc());
						benef_Details.setRierCode(type);
						break;
					case "SHCBIC":
						benef_Details.setRiderPremium(calResp.getSuhrbc());
						benef_Details.setRierCode(type);
						break;
					case "HBC":
						benef_Details.setRiderPremium(calResp.getHbc());
						benef_Details.setRierCode(type);
						break;
					/*
					 * case "HRBC": benef_Details.setRiderPremium(0.0);
					 * benef_Details.setRierCode(type); break;
					 */
					case "HCBFC":
						benef_Details.setRiderPremium(0.0);
						if(benef_Details.getRiderSum() == null) {

							benef_Details.setRiderSum(0.0);
						}
						benef_Details.setRierCode(type);
						break;

					case "HCBIC":
						benef_Details.setRiderPremium(calResp.getHrbic());
						benef_Details.setRierCode(type);
						break;
					default:
						break;
					}

					benef_DetailList.add(benef_Details);
				}
			}

			return benef_DetailList;
		} finally {
			if (benef_DetailList != null) {
				benef_DetailList = null;
			}
		}
	}

	public ArrayList<Quo_Benef_Child_Details> getChildBenif(ArrayList<Quo_Benef_Details> benef_DetailsList,
			ArrayList<CustChildDetails> custChildDetailsList, ArrayList<Child> childList,
			ArrayList<Children> get_childrenList, Integer term, String frequancy, ArrayList<Benifict> benifictListC, QuotationQuickCalResponse calResp)
			throws Exception {

		Double cib = null;
		Double suhrb = null;
		Double hb = null;
		Double hrbi = null;

		Quo_Benef_Details cibc_Benef_Details = null;
		// Quo_Benef_Details hrbc_Benef_Details = null;
		Quo_Benef_Details hrbfc_Benef_Details = null;
		Quo_Benef_Details hrbic_Benef_Details = null;
		Quo_Benef_Details suhrbc_Benef_Details = null;
		Quo_Benef_Details hbc_Benef_Details = null;

		if (benef_DetailsList != null && !benef_DetailsList.isEmpty()) {
			for (Quo_Benef_Details benef_Details : benef_DetailsList) {
				if (benef_Details.getRierCode() != null) {
					String type = benef_Details.getRierCode();
					////System.out.println(type + "Tttttttttttttttt");
					switch (type) {
					case "CIBC":
						cibc_Benef_Details = benef_Details;
						break;
					case "SHCBIC":
						suhrbc_Benef_Details = benef_Details;
						break;
					/*
					 * case "HRBC": hrbc_Benef_Details = benef_Details; break;
					 */
					case "HCBIC":
						hrbic_Benef_Details = benef_Details;
						break;
					case "HCBFC":
						hrbfc_Benef_Details = benef_Details;
						break;

					case "HBC":
						hbc_Benef_Details = benef_Details;
						break;

					default:
						break;
					}
				}
			}
		}
		if (benifictListC != null && !benifictListC.isEmpty())
			for (Benifict benifict : benifictListC) {
				if (benifict.getType().equals("CIBC"))
					cib = benifict.getSumAssured();
				if (benifict.getType().equals("SHCBIC"))
					suhrb = benifict.getSumAssured();
				if (benifict.getType().equals("HBC"))
					hb = benifict.getSumAssured();
				if (benifict.getType().equals("HCBIC"))
					hrbi = benifict.getSumAssured();
			}

		Double relife = 1.0;
		
		ArrayList<Quo_Benef_Child_Details> childBenifList = new ArrayList<>();
		for (Children children : get_childrenList) {
			for (Child child : childList) {
				if (child.getChildName().equals(children.get_cName())) {
					////System.out.println("hit*******************");
					for (CustChildDetails childDetails : custChildDetailsList) {
						if (childDetails.getChild().equals(child)) {
							////System.out.println("hit1*******************");

							if (children.is_cCibc()) {
								////System.out.println("addddddddddddddddddddddddddddddddddddddd1");
								Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();

								Integer valiedTerm = 0; 
								if(calResp.isArtm() && !frequancy.equalsIgnoreCase("S")) {
									valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
											"CIBC", Integer.parseInt(calResp.getPayTerm()));
								} else {
									valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
											"CIBC", term);
								}
								////System.out.println(valiedTerm + "//////////////////// valied term");
								benef_Child_Details.setTerm(valiedTerm);
								
								
								if (calResp.isArp()) {
									RateCardARP rateCardARP = rateCardARPDao
											.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(children.get_cAge(),
													valiedTerm, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
									relife = rateCardARP.getRate();
									
								
								}

								
								BigDecimal cibc = cibcService.calculateCIBC(children.get_cAge(), valiedTerm, new Date(),
										cib, frequancy, relife);

								benef_Child_Details.setCustChildDetails(childDetails);
								benef_Child_Details.setTerm(valiedTerm);
								benef_Child_Details.setPremium(cibc.doubleValue());
								////System.out.println(cibc_Benef_Details.getQuo_Benef_DetailsId()
								//		+ "===============================================1");
								benef_Child_Details.setQuo_Benef_Details(cibc_Benef_Details);
								childBenifList.add(benef_Child_Details);
							}
							if (children.is_cSuhrbc()) {
								////System.out.println("addddddddddddddddddddddddddddddddddddddd2");
								Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();
								//System.out.println(frequancy + "  :   sdfghjkl;fghjkl;'");
								Integer valiedTerm = 0; 
								if(calResp.isArtm() && !frequancy.equalsIgnoreCase("S")) {
									valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
											"SHCBIC", Integer.parseInt(calResp.getPayTerm()));
								} else {
									valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
											"SHCBIC", term);
								}
								benef_Child_Details.setTerm(valiedTerm);
								////System.out.println(children.get_cTitle() + "                                  test");
								
								if (calResp.isArp()) {
									RateCardARP rateCardARP = rateCardARPDao
											.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(children.get_cAge(),
													valiedTerm, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
									relife = rateCardARP.getRate();
								}
								
								BigDecimal cibc = suhrbcService.calculateSUHRBC(children.get_cAge(),
										child.getChildGender(), valiedTerm, suhrb, new Date(), frequancy, relife);

								benef_Child_Details.setCustChildDetails(childDetails);
								benef_Child_Details.setTerm(valiedTerm);
								benef_Child_Details.setPremium(cibc.doubleValue());
								benef_Child_Details.setQuo_Benef_Details(suhrbc_Benef_Details);
								childBenifList.add(benef_Child_Details);
							}
							if (children.is_cHbc()) {
								////System.out.println("addddddddddddddddddddddddddddddddddddddd3");
								Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();

								Integer valiedTerm = 0; 
								if(calResp.isArtm() && !frequancy.equalsIgnoreCase("S")) {
									valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
											"HBC", Integer.parseInt(calResp.getPayTerm()));
								} else {
									valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
											"HBC", term);
								}
								
								benef_Child_Details.setTerm(valiedTerm);
								
								if (calResp.isArp()) {
									RateCardARP rateCardARP = rateCardARPDao
											.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(children.get_cAge(),
													valiedTerm, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
									relife = rateCardARP.getRate();
								}
								
								
								BigDecimal cibc = hbcService.calculateHBC(valiedTerm, new Date(), hb, frequancy, relife);

								benef_Child_Details.setCustChildDetails(childDetails);
								benef_Child_Details.setTerm(valiedTerm);
								benef_Child_Details.setPremium(cibc.doubleValue());
								benef_Child_Details.setQuo_Benef_Details(hbc_Benef_Details);
								childBenifList.add(benef_Child_Details);
							}
							
							
							
							/*
							 * if (children.is_cHrbc()) {
							 * //System.out.println("addddddddddddddddddddddddddddddddddddddd4");
							 * Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();
							 * 
							 * Integer valiedTerm =
							 * calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(), "HBC",
							 * term); benef_Child_Details.setTerm(valiedTerm);
							 * 
							 * benef_Child_Details.setCustChildDetails(childDetails);
							 * benef_Child_Details.setTerm(valiedTerm); benef_Child_Details.setPremium(0.0);
							 * benef_Child_Details.setQuo_Benef_Details(hrbc_Benef_Details);
							 * childBenifList.add(benef_Child_Details); }
							 */
							if (children.is_cHrbic()) {
								////System.out.println("addddddddddddddddddddddddddddddddddddddd4  called HRBIC");
								Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();

								Integer valiedTerm = 0; 
								//System.out.println(calResp.getPayTerm());
								if(calResp.isArtm() && !frequancy.equalsIgnoreCase("S")) {
									try {
										valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
												"HCBIC", Integer.parseInt(calResp.getPayTerm()));
									}catch (Exception e) {
										e.printStackTrace();
									}
									
								} else {
									valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
											"HCBIC", term);
								}
								
								benef_Child_Details.setTerm(valiedTerm);
								
								if (calResp.isArp()) {
									RateCardARP rateCardARP = rateCardARPDao
											.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(children.get_cAge(),
													valiedTerm, calResp.getPayTerm(), new Date(), new Date(), new Date(), new Date());
									relife = rateCardARP.getRate();
								}

								BigDecimal hrbic = hrbiService.calculateHRBI(children.get_cAge(), valiedTerm, children.get_cTitle(), hrbi , new Date(), frequancy, relife, 1.0);

								benef_Child_Details.setCustChildDetails(childDetails);
								benef_Child_Details.setTerm(valiedTerm);
								benef_Child_Details.setPremium(hrbic.doubleValue());
								benef_Child_Details.setQuo_Benef_Details(hrbic_Benef_Details);
								childBenifList.add(benef_Child_Details);
							}

							if (children.is_cHrbfc()) {
								////System.out.println("addddddddddddddddddddddddddddddddddddddd4");
								Quo_Benef_Child_Details benef_Child_Details = new Quo_Benef_Child_Details();

								Integer valiedTerm = 0; 
								if(calResp.isArtm() && !frequancy.equalsIgnoreCase("S")) {
									valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
											"HCBFC", Integer.parseInt(calResp.getPayTerm()));
								} else {
									valiedTerm = calculateBenefictTerm.calculateBenifictTerm(children.get_cAge(),
											"HCBFC", term);
								}
								
								
								benef_Child_Details.setTerm(valiedTerm);

								benef_Child_Details.setCustChildDetails(childDetails);
								benef_Child_Details.setTerm(valiedTerm);
								benef_Child_Details.setPremium(0.0);
								benef_Child_Details.setQuo_Benef_Details(hrbfc_Benef_Details);
								childBenifList.add(benef_Child_Details);
							}
						}
					}
				}
			}
		}
		return childBenifList;
	}

	@Override
	public ArrayList<Shedule> getSheduleDtaDtapl(QuotationQuickCalResponse calResp, QuotationDetails quotationDetails)
			throws Exception {
		ArrayList<Shedule> sheduleList = new ArrayList<>();

		ArrayList<DTAShedule> dtaList = calResp.getDtaShedules();
		if (dtaList != null) {
			for (DTAShedule dtaShedule : dtaList) {
				Shedule shedule = new Shedule();
				shedule.setLorned(dtaShedule.getLonred());
				shedule.setOutSum(dtaShedule.getOutsum());
				shedule.setOutYear(dtaShedule.getOutyer());
				shedule.setPolicyYear(dtaShedule.getPolYear());
				shedule.setPremium(dtaShedule.getPremum());
				shedule.setPremiumRate(dtaShedule.getPrmrat());
				shedule.setQuotationDetails(quotationDetails);
				sheduleList.add(shedule);
			}
		}

		return sheduleList;
	}

	@Override
	public ArrayList<Quo_Benef_Details> addMaturity(String product, ArrayList<Quo_Benef_Details> benefictList,
			QuotationQuickCalResponse calResp, Integer term, QuotationDetails quotationDetails) throws Exception {

		Quo_Benef_Details mat1 = new Quo_Benef_Details();
		Quo_Benef_Details mat2 = new Quo_Benef_Details();
		Quo_Benef_Details mat3 = new Quo_Benef_Details();

		mat1.setRiderPremium(0.0);
		mat1.setRiderTerm(term);
		mat1.setRiderSum(calResp.getAt6());
		mat1.setQuotationDetails(quotationDetails);

		mat2.setRiderPremium(0.0);
		mat2.setRiderTerm(term);
		mat2.setRiderSum(calResp.getAt8());
		mat2.setQuotationDetails(quotationDetails);

		mat3.setRiderPremium(0.0);
		mat3.setRiderTerm(term);
		mat3.setRiderSum(calResp.getAt10());
		mat3.setQuotationDetails(quotationDetails);

		switch (product) {
		case "INVP":
			mat1.setRierCode("L3");
			mat1.setBenefit(benefitsDao.findByRiderCode("L3"));
			benefictList.add(mat1);

			mat2.setRierCode("L4");
			mat2.setBenefit(benefitsDao.findByRiderCode("L4"));
			benefictList.add(mat2);

			mat3.setRierCode("L5");
			mat3.setBenefit(benefitsDao.findByRiderCode("L5"));
			benefictList.add(mat3);
			break;

		case "END1":
			mat1.setRierCode("L1");
			mat1.setBenefit(benefitsDao.findByRiderCode("L1"));
			benefictList.add(mat1);

			break;

		case "ASIP":
			mat1.setRierCode("L11");
			mat1.setBenefit(benefitsDao.findByRiderCode("L11"));
			benefictList.add(mat1);

			mat2.setRierCode("L12");
			mat2.setBenefit(benefitsDao.findByRiderCode("L12"));
			benefictList.add(mat2);

			mat3.setRierCode("L13");
			mat3.setBenefit(benefitsDao.findByRiderCode("L13"));
			benefictList.add(mat3);
			break;

		case "ARP":
			mat1.setRierCode("L1");
			mat1.setBenefit(benefitsDao.findByRiderCode("L1"));
			benefictList.add(mat1);

			break;

		case "AIP":
			mat1.setRierCode("L6");
			mat1.setBenefit(benefitsDao.findByRiderCode("L6"));
			benefictList.add(mat1);

			mat2.setRierCode("L8");
			mat2.setBenefit(benefitsDao.findByRiderCode("L8"));
			benefictList.add(mat2);

			mat3.setRierCode("L9");
			mat3.setBenefit(benefitsDao.findByRiderCode("L9"));
			benefictList.add(mat3);
			break;
			
			
		case "ARTM":
			mat1.setRierCode("L14");
			mat1.setBenefit(benefitsDao.findByRiderCode("L14"));
			mat1.setRiderPremium(calResp.getPensionPremium1());
			benefictList.add(mat1);

			mat2.setRierCode("L15");
			mat2.setRiderPremium(calResp.getPensionPremium2());
			mat2.setBenefit(benefitsDao.findByRiderCode("L15"));
			benefictList.add(mat2);

			mat3.setRierCode("L16");
			mat3.setRiderPremium(calResp.getPensionPremium3());
			mat3.setBenefit(benefitsDao.findByRiderCode("L16"));
			benefictList.add(mat3);
			break;

		case "AIB":
			mat1.setRierCode("IAIB");
			mat1.setBenefit(benefitsDao.findByRiderCode("IAIB"));
			benefictList.add(mat1);

			break;

		default:
			break;
		}

		return benefictList;
	}

}