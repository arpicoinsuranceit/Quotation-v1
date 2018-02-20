package org.arpicoinsurance.groupit.main.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

import org.arpicoinsurance.groupit.main.common.CalculationUtils;
import org.arpicoinsurance.groupit.main.dao.RateCardENDDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.InvpSaveQuotation;
import org.arpicoinsurance.groupit.main.helper.QuotationQuickCalResponse;
import org.arpicoinsurance.groupit.main.helper.QuotationCalculation;
import org.arpicoinsurance.groupit.main.dao.ChildDao;
import org.arpicoinsurance.groupit.main.dao.CustChildDetailsDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDao;
import org.arpicoinsurance.groupit.main.dao.CustomerDetailsDao;
import org.arpicoinsurance.groupit.main.dao.OccupationDao;
import org.arpicoinsurance.groupit.main.dao.ProductDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_Child_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.RateCardARPDao;
import org.arpicoinsurance.groupit.main.model.RateCardEND;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.CustChildDetails;
import org.arpicoinsurance.groupit.main.model.Customer;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.Occupation;
import org.arpicoinsurance.groupit.main.model.Products;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.RateCardARP;
import org.arpicoinsurance.groupit.main.service.ARPService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.custom.CalculateRiders;
import org.arpicoinsurance.groupit.main.service.custom.QuotationSaveUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ARPServiceImpl implements ARPService {

	ArrayList<Quo_Benef_Child_Details> childBenifList = new ArrayList<>();

	@Autowired
	private RateCardENDDao rateCardENDDao;

	@Autowired
	private RateCardARPDao rateCardARPDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private UsersDao userDao;

	@Autowired
	private OccupationDao occupationDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private ChildDao childDao;

	@Autowired
	private CustomerDetailsDao customerDetailsDao;

	@Autowired
	private CustChildDetailsDao custChildDetailsDao;

	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private QuotationDetailsDao quotationDetailDao;

	@Autowired
	private Quo_Benef_DetailsDao quoBenifDetailDao;

	@Autowired
	private Quo_Benef_Child_DetailsDao quoBenifChildDetailsDao;
	
	@Autowired
	private CalculateRiders calculateriders;

	@Autowired
	private QuotationSaveUtilService quotationSaveUtilService;
	
	@Autowired
	private QuotationDetailsService quotationDetailsService;
	
	
	@Override
	public QuotationQuickCalResponse getCalcutatedArp(QuotationCalculation quotationCalculation) throws Exception {
		
		CalculationUtils calculationUtils = null;
		try {

			QuotationQuickCalResponse calResp = new QuotationQuickCalResponse();
			calculationUtils = new CalculationUtils();
			Double rebate = calculationUtils.getRebate(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getFrequance());
			BigDecimal bsaPremium = calculateL2(quotationCalculation.get_personalInfo().getMage(),
					quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getPayingterm(),
					calculationUtils.getRebate(quotationCalculation.get_personalInfo().getFrequance()), new Date(),
					quotationCalculation.get_personalInfo().getBsa(),
					quotationCalculation.get_personalInfo().getFrequance());
			
			calResp = calculateriders.getRiders(quotationCalculation, calResp);
			calResp.setBasicSumAssured(calculationUtils.addRebatetoBSAPremium(rebate, bsaPremium));
			calResp.setAt6(calculateMaturity(quotationCalculation.get_personalInfo().getTerm(),
					quotationCalculation.get_personalInfo().getBsa()).doubleValue());

			System.out.println(calResp.getBasicSumAssured());
			Double tot = calResp.getBasicSumAssured() + calResp.getAddBenif();
			Double adminFee = calculationUtils.getAdminFee(quotationCalculation.get_personalInfo().getFrequance());
			Double tax = calculationUtils.getTaxAmount(tot + adminFee);
			Double extraOE = adminFee + tax;
			calResp.setExtraOE(extraOE);
			calResp.setTotPremium(tot + extraOE);

			return calResp;

		} finally {
			if (calculationUtils != null) {
				calculationUtils = null;
			}
		}
	}

	@Override
	public BigDecimal calculateL2(int age, int term, String rlfterm, double rebate, Date chedat, double bassum,
			String payFrequency) throws Exception {
		System.out.println("ARP bassum : " + bassum + " age : " + age + " term : " + term + " rebate : " + rebate
				+ " payFrequency : " + payFrequency + " rlfterm : " + rlfterm);
		BigDecimal premium = new BigDecimal(0);

		RateCardEND rateCardEND = rateCardENDDao.findByAgeAndTermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(
				age, term, chedat, chedat, chedat, chedat);
		System.out.println("rateCardARP : " + rateCardEND.getRate());
		RateCardARP rateCardARP = rateCardARPDao
				.findByAgeAndTermAndRlftermAndStrdatLessThanOrStrdatAndEnddatGreaterThanOrEnddat(age, term, rlfterm,
						chedat, chedat, chedat, chedat);
		System.out.println("rateCardARPRelief : " + rateCardARP.getRate());

		if (payFrequency.equalsIgnoreCase("S")) {
			// ((((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)) *@relief@
			premium = (((new BigDecimal(rateCardEND.getRate())
					.subtract(((new BigDecimal(rateCardEND.getRate()).multiply(new BigDecimal(rebate)))
							.divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6,
									RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum)))
											.multiply(new BigDecimal(rateCardARP.getRate()))
											.setScale(0, RoundingMode.HALF_UP);
		} else {
			// ((((@rate@-(@rate@*@rebate@/100))/1000)*@sum_assured@)/@payment_frequency@)
			// *@relief@
			premium = ((((new BigDecimal(rateCardEND.getRate())
					.subtract(((new BigDecimal(rateCardEND.getRate()).multiply(new BigDecimal(rebate)))
							.divide(new BigDecimal(100), 6, RoundingMode.HALF_UP)))).divide(new BigDecimal(1000), 6,
									RoundingMode.HALF_UP)).multiply(new BigDecimal(bassum))).divide(
											new BigDecimal(new CalculationUtils().getPayterm(payFrequency)), 10,
											RoundingMode.HALF_UP)).multiply(new BigDecimal(rateCardARP.getRate()))
													.setScale(0, RoundingMode.HALF_UP);
		}
		System.out.println("premium : " + premium.toString());
		return premium;
	}

	@Override
	public BigDecimal calculateMaturity(int term, double bassum) throws Exception {
		// @sum_assured@ + ((@sum_assured@*0.025)*@term@)
		BigDecimal maturity = new BigDecimal(0);
		System.out.println("term : " + term + " bassum : " + bassum);
		maturity = (new BigDecimal(bassum)
				.add(((new BigDecimal(bassum).multiply(new BigDecimal(0.025))).multiply(new BigDecimal(term)))))
						.setScale(0, RoundingMode.HALF_UP);
		System.out.println("maturity : " + maturity.toString());
		return maturity;
	}


	@Override
	public String saveQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer id)
			throws Exception {
		QuotationQuickCalResponse calResp = getCalcutatedArp(calculation);
		Products products = productDao.findByProductCode("ARP");
		Users user = userDao.findOne(id);
		Occupation occupationMainlife = occupationDao.findByOcupationid(calculation.get_personalInfo().getMocu());
		Occupation occupationSpouse = occupationDao.findByOcupationid(calculation.get_personalInfo().getSocu());

		CustomerDetails mainLifeDetail = quotationSaveUtilService.getCustomerDetail(occupationMainlife, _invpSaveQuotation.get_personalInfo(),
				user);

		CustomerDetails spouseDetail = quotationSaveUtilService.getSpouseDetail(occupationSpouse, _invpSaveQuotation.get_personalInfo(), user);

		Customer mainlife = new Customer();
		mainlife.setCustName(_invpSaveQuotation.get_personalInfo().get_mainlife().get_mName());
		mainlife.setCustCreateDate(new Date());
		mainlife.setCustCreateBy(user.getUser_Name());
		mainLifeDetail.setCustomer(mainlife);

		Customer spouse = null;
		if (_invpSaveQuotation.get_personalInfo().get_spouse() != null
				&& _invpSaveQuotation.get_personalInfo().get_spouse().is_sActive()) {
			spouse = new Customer();
			spouse.setCustName(spouseDetail.getCustName());
			spouseDetail.setCustomer(spouse);
		}

		ArrayList<Child> childList = quotationSaveUtilService.getChilds(_invpSaveQuotation.get_personalInfo().get_childrenList());

		ArrayList<CustChildDetails> custChildDetailsList = new ArrayList<>();
		if (childList != null && !childList.isEmpty())
			for (Child child : childList) {
				CustChildDetails custChildDetails = new CustChildDetails();
				custChildDetails.setChild(child);
				custChildDetails.setCustomer(mainLifeDetail);
				custChildDetailsList.add(custChildDetails);
			}

		QuotationDetails quotationDetails = quotationSaveUtilService.getQuotationDetail(calResp, calculation, 0.0);

		Quotation quotation = new Quotation();
		quotation.setCustomerDetails(mainLifeDetail);
		if (spouseDetail != null)
			quotation.setSpouseDetails(spouseDetail);
		quotation.setStatus("active");
		quotation.setUser(user);
		quotation.setProducts(products);

		quotationDetails.setQuotation(quotation);
		quotationDetails.setQuotationCreateBy(user.getUser_Code());

		ArrayList<Quo_Benef_Details> benef_DetailsList = quotationSaveUtilService.getBenifDetails(_invpSaveQuotation.get_riderDetails(), calResp,
				quotationDetails, _invpSaveQuotation.get_personalInfo().get_childrenList(),
				_invpSaveQuotation.get_personalInfo().get_plan().get_term());

		//////////////////////////// save//////////////////////////////////
		Customer life = (Customer) customerDao.save(mainlife);
		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);
		ArrayList<CustChildDetails> custChildDList = null;
		if (life != null && mainLifeDetails != null) {

			if (spouse != null) {
				Customer sp = customerDao.save(spouse);
				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
				if (sp == null && spDetsils != null) {
					return "Error at Spouse Saving";
				}
			}

			ArrayList<Child> cList = (ArrayList<Child>) childDao.save(childList);
			custChildDList = (ArrayList<CustChildDetails>) custChildDetailsDao.save(custChildDetailsList);
			if (childList != null && childList.size() > 0) {
				if (cList == null && custChildDList == null) {
					return "Error at Child Saving";
				}
			}

			Quotation quo = quotationDao.save(quotation);
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails);

			if (quo != null && quoDetails != null) {
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);
				if (bnfdList != null) {

					ArrayList<Quo_Benef_Child_Details> childBenifList = quotationSaveUtilService.getChildBenif(bnfdList, custChildDList,
							childList, _invpSaveQuotation.get_personalInfo().get_childrenList(),
							_invpSaveQuotation.get_personalInfo().get_plan().get_term(),
							calculation.get_personalInfo().getFrequance(),
							calculation.get_riderDetails().get_cRiders());

					if (quoBenifChildDetailsDao.save(childBenifList) == null) {
						return "Error at Child Benifict Saving";
					}

				} else {
					return "Error at Benifict Saving";
				}
			} else {
				return "Error at Quotation Saving";
			}

		} else {
			return "Error at MainLife Saving";
		}

		return "Success";

	}

	@Override
	public String editQuotation(QuotationCalculation calculation, InvpSaveQuotation _invpSaveQuotation, Integer userId,Integer qdId)
			throws Exception {
		CalculationUtils calculationUtils = new CalculationUtils();

		QuotationQuickCalResponse calResp = getCalcutatedArp(calculation);
		Products products = productDao.findByProductCode("ARP");
		Users user = userDao.findOne(userId);

		Occupation occupationMainlife = occupationDao.findByOcupationid(calculation.get_personalInfo().getMocu());
		Occupation occupationSpouse = occupationDao.findByOcupationid(calculation.get_personalInfo().getSocu());

		CustomerDetails mainLifeDetail = quotationSaveUtilService.getCustomerDetail(occupationMainlife,
				_invpSaveQuotation.get_personalInfo(), user);
		CustomerDetails spouseDetail = quotationSaveUtilService.getSpouseDetail(occupationSpouse,
				_invpSaveQuotation.get_personalInfo(), user);

		QuotationDetails quotationDetails = quotationDetailsService.findQuotationDetails(qdId);

		Customer mainlife = quotationDetails.getQuotation().getCustomerDetails().getCustomer();
		Customer spouse = null;
		if (spouseDetail != null) {
			try {
				spouse = quotationDetails.getQuotation().getSpouseDetails().getCustomer();
			} catch (NullPointerException ex) {
				spouse = null;
			}

			if (spouse != null) {
				spouseDetail.setCustomer(spouse);
			} else {
				spouse = new Customer();
				spouse.setCustName(spouseDetail.getCustName());
				spouse.setCustCreateDate(new Date());
				spouse.setCustCreateBy(user.getUser_Name());
				spouseDetail.setCustomer(spouse);
			}

		} else {

		}

		mainLifeDetail.setCustomer(mainlife);

		ArrayList<Child> childList = quotationSaveUtilService
				.getChilds(_invpSaveQuotation.get_personalInfo().get_childrenList());

		ArrayList<CustChildDetails> custChildDetailsList = new ArrayList<>();
		if (childList != null && !childList.isEmpty()) {
			for (Child child : childList) {
				CustChildDetails custChildDetails = new CustChildDetails();
				custChildDetails.setChild(child);
				custChildDetails.setCustomer(mainLifeDetail);
				custChildDetailsList.add(custChildDetails);
			}
		}

		Quotation quotation = quotationDetails.getQuotation();
		quotation.setCustomerDetails(mainLifeDetail);
		if (spouseDetail != null) {
			quotation.setSpouseDetails(spouseDetail);
		} else {
			quotation.setSpouseDetails(null);
		}

		QuotationDetails quotationDetails1 = quotationSaveUtilService.getQuotationDetail(calResp, calculation, 0.0);

		quotationDetails1.setQuotation(quotation);
		quotationDetails1.setQuotationCreateBy(user.getUser_Code());

		ArrayList<Quo_Benef_Details> benef_DetailsList = quotationSaveUtilService.getBenifDetails(
				_invpSaveQuotation.get_riderDetails(), calResp, quotationDetails1,
				_invpSaveQuotation.get_personalInfo().get_childrenList(),
				_invpSaveQuotation.get_personalInfo().get_plan().get_term());

		//////////////////////////// save edit//////////////////////////////////

		Customer life = (Customer) customerDao.save(mainlife);
		CustomerDetails mainLifeDetails = customerDetailsDao.save(mainLifeDetail);
		ArrayList<CustChildDetails> custChildDList = null;
		if (life != null && mainLifeDetails != null) {

			if (spouseDetail != null) {
				Customer sp = customerDao.save(spouse);
				CustomerDetails spDetsils = customerDetailsDao.save(spouseDetail);
				if (sp == null && spDetsils != null) {
					return "Error at Spouse Saving";
				}
			}

			ArrayList<Child> cList = (ArrayList<Child>) childDao.save(childList);
			custChildDList = (ArrayList<CustChildDetails>) custChildDetailsDao.save(custChildDetailsList);
			if (childList != null && childList.size() > 0) {
				if (cList == null && custChildDList == null) {
					return "Error at Child Updating";
				}
			}

			Quotation quo = quotationDao.save(quotation);
			QuotationDetails quoDetails = quotationDetailDao.save(quotationDetails1);

			if (quo != null && quoDetails != null) {
				ArrayList<Quo_Benef_Details> bnfdList = (ArrayList<Quo_Benef_Details>) quoBenifDetailDao
						.save(benef_DetailsList);
				if (bnfdList != null) {

					ArrayList<Quo_Benef_Child_Details> childBenifList = quotationSaveUtilService.getChildBenif(bnfdList,
							custChildDList, childList, _invpSaveQuotation.get_personalInfo().get_childrenList(),
							_invpSaveQuotation.get_personalInfo().get_plan().get_term(),
							calculation.get_personalInfo().getFrequance(),
							calculation.get_riderDetails().get_cRiders());

					if (quoBenifChildDetailsDao.save(childBenifList) == null) {
						return "Error at Child Benifict Updating";
					}

				} else {
					return "Error at Benifict Updating";
				}
			} else {
				return "Error at Quotation Updating";
			}

		} else {
			return "Error at MainLife Updating";
		}

		return "Success";
	}
	
}
