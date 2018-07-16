package org.arpicoinsurance.groupit.main.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.transaction.Transactional;
import org.arpicoinsurance.groupit.main.dao.NomineeDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.helper.Children;
import org.arpicoinsurance.groupit.main.helper.EditQuotation;
import org.arpicoinsurance.groupit.main.helper.MainLife;
import org.arpicoinsurance.groupit.main.helper.Plan;
import org.arpicoinsurance.groupit.main.helper.QuoBenf;
import org.arpicoinsurance.groupit.main.helper.QuoChildBenef;
import org.arpicoinsurance.groupit.main.helper.QuotationReceipt;
import org.arpicoinsurance.groupit.main.helper.Spouse;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.CustomerDetails;
import org.arpicoinsurance.groupit.main.model.Nominee;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_Child_DetailsService;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.arpicoinsurance.groupit.main.service.QuotationDetailsService;
import org.arpicoinsurance.groupit.main.service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class QuotationDetailsServiceImpl implements QuotationDetailsService {

	@Autowired
	private QuotationDao quotationDao;

	@Autowired
	private QuotationDetailsDao quotationDetailsDao;

	@Autowired
	private Quo_Benef_DetailsService quo_Benef_DetailsService;

	@Autowired
	private Quo_Benef_Child_DetailsService childBenefService;

	@Autowired
	private QuotationService quotationService;

	@Autowired
	private NomineeDao nomineeDao;

	@Override
	public QuotationDetails findQuotationDetails(Integer qdId) throws Exception {
		return quotationDetailsDao.findByQdId(qdId);
	}

	@Override
	public EditQuotation editQuotationDetails(Integer qdId) throws Exception {
		QuotationDetails details = findQuotationDetails(qdId);
		EditQuotation editQuotation = new EditQuotation();
		MainLife mainLife = new MainLife();
		Spouse spouse = new Spouse();
		if (details != null) {
			CustomerDetails customerDetails = details.getCustomerDetails();
			mainLife.set_mName(customerDetails.getCustName());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			LocalDate dateOfBirth = LocalDate.parse(dateFormat.format(customerDetails.getCustDob()));
			// LocalDate currentDate =
			// LocalDate.parse(dateFormat.format(details.getQuotationquotationCreateDate()));

			LocalDate currentDate = LocalDate.now();

			long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
			diffInYears += 1;
			String age = Long.toString(diffInYears);

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");

			System.out.println(customerDetails.getCustomer().getCustCode());

			mainLife.set_mAge(age);
			mainLife.set_mDob(dateFormat1.format(customerDetails.getCustDob()));
			mainLife.set_mEmail(customerDetails.getCustEmail());
			mainLife.set_mGender(customerDetails.getCustGender());
			mainLife.set_mMobile(customerDetails.getCustTel());
			mainLife.set_mNic(customerDetails.getCustNic());
			mainLife.set_mOccupation(Integer.toString(customerDetails.getOccupation().getOcupationid()));
			mainLife.set_mSmoking("No");
			mainLife.set_mTitle(customerDetails.getCustTitle());
			mainLife.set_mCivilStatus(customerDetails.getCustCivilStatus());
			mainLife.set_occuCode(customerDetails.getOccupation().getOcupationCode());
			mainLife.set_mCustCode(customerDetails.getCustomer().getCustCode());

			if (details.getSpouseDetails() != null) {
				CustomerDetails spouseDetails = details.getSpouseDetails();
				spouse.set_sName(spouseDetails.getCustName());

				LocalDate sdateOfBirth = LocalDate.parse(dateFormat.format(spouseDetails.getCustDob()));
				LocalDate scurrentDate = LocalDate.parse(dateFormat.format(details.getQuotationquotationCreateDate()));
				long sdiffInYears = ChronoUnit.YEARS.between(sdateOfBirth, scurrentDate);
				sdiffInYears += 1;
				String sage = Long.toString(sdiffInYears);

				spouse.set_sActive(true);
				spouse.set_sAge(sage);
				spouse.set_sDob(dateFormat1.format(spouseDetails.getCustDob()));
				spouse.set_sGender(spouseDetails.getCustGender());
				spouse.set_sNic(spouseDetails.getCustNic());
				spouse.set_sOccupation(Integer.toString(spouseDetails.getOccupation().getOcupationid()));
				spouse.set_sTitle(spouseDetails.getCustTitle());

				spouse.setOccuCode(spouseDetails.getOccupation().getOcupationCode());

			} else {
				spouse.set_sActive(false);
			}
		}

		editQuotation.set_mainlife(mainLife);
		editQuotation.set_spouse(spouse);
		editQuotation.set_plan(getPlanDetails(details));

		List<Nominee> nominees = nomineeDao.findByQuotationDetails(details);
		if (nominees.size() > 0) {
			editQuotation.get_plan().set_nomineeName(nominees.get(0).getNomineeName());
			editQuotation.get_plan().set_nomineeAge(nominees.get(0).getAge());
			editQuotation.get_plan()
					.set_nomineedob(new SimpleDateFormat("dd-MM-yyyy").format(nominees.get(0).getNomineeDob()));
			editQuotation.get_plan().set_nomoneeRelation(nominees.get(0).getRelation());
		}

		// return editQuotation;
		return getBenefitsAndChildDetails(details, editQuotation);
	}

	private EditQuotation getBenefitsAndChildDetails(QuotationDetails details, EditQuotation editQuotation)
			throws Exception {
		ArrayList<Quo_Benef_Details> benef_Details = (ArrayList<Quo_Benef_Details>) quo_Benef_DetailsService
				.findByQuotationDetails(details);

		ArrayList<QuoBenf> mainLifeBenef = new ArrayList<>();
		ArrayList<QuoBenf> spouseBenef = new ArrayList<>();
		ArrayList<QuoBenf> childBenef = new ArrayList<>();

		TreeMap<String, QuoChildBenef> childMap = new TreeMap<>();

		ArrayList<Children> childrenList = new ArrayList<>();

		if (benef_Details != null) {
			for (Quo_Benef_Details quo_Benef_Details : benef_Details) {
				Benefits benf = quo_Benef_Details.getBenefit();
				if (benf.getBenefitType().equals("s")) {// check benf_type is spouse
					QuoBenf qb = new QuoBenf();
					qb.setBenfName(benf.getRiderCode());
					qb.setPremium(quo_Benef_Details.getRiderPremium());
					qb.setRiderSum(quo_Benef_Details.getRiderSum());
					spouseBenef.add(qb);
				} else if (benf.getBenefitType().equals("m")) {// check benf_type is mainLife
					QuoBenf qb = new QuoBenf();
					qb.setBenfName(benf.getRiderCode());
					qb.setPremium(quo_Benef_Details.getRiderPremium());
					qb.setRiderSum(quo_Benef_Details.getRiderSum());
					mainLifeBenef.add(qb);
				} else if (benf.getBenefitType().equals("c")) {// check benf_type is child
					QuoBenf qb1 = new QuoBenf();
					qb1.setBenfName(benf.getRiderCode());
					qb1.setPremium(quo_Benef_Details.getRiderPremium());
					qb1.setRiderSum(quo_Benef_Details.getRiderSum());
					childBenef.add(qb1);

					List<Quo_Benef_Child_Details> qbcd = childBenefService
							.getQuo_Benef_Child_DetailsByQuo_Benf_DetailsId(quo_Benef_Details.getQuo_Benef_DetailsId());
					if (!qbcd.isEmpty()) {
						QuoBenf qb = new QuoBenf();
						qb.setBenfName(benf.getRiderCode());
						qb.setRiderSum(quo_Benef_Details.getRiderSum());

						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

						for (Quo_Benef_Child_Details quo_Benef_Child_Details : qbcd) {
							Child child = quo_Benef_Child_Details.getCustChildDetails().getChild();
							if (!childMap.containsKey(child.getChildName())) {
								ArrayList<QuoBenf> benfs = new ArrayList<>();// create list of benefits
								qb.setPremium(quo_Benef_Child_Details.getPremium());
								benfs.add(qb);

								QuoChildBenef benef = new QuoChildBenef();// create QuoChildBenef object
								benef.setChild(child);
								benef.setBenfs(benfs);// set list of benefits

								Children children = new Children();
								children.set_cActive(true);

								LocalDate sdateOfBirth = LocalDate.parse(dateFormat.format(child.getChildDob()));
								LocalDate scurrentDate = LocalDate
										.parse(dateFormat.format(details.getQuotationquotationCreateDate()));
								long sdiffInYears = ChronoUnit.YEARS.between(sdateOfBirth, scurrentDate);
								sdiffInYears += 1;
								String sage = Long.toString(sdiffInYears);

								SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");

								children.set_cAge(Integer.parseInt(sage));
								children.set_cDob(dateFormat1.format(child.getChildDob()));
								children.set_cName(child.getChildName());
								children.set_cNic(child.getChildNic());
								children.set_cTitle(child.getChildGender());

								childrenList.add(children);

								childMap.put(child.getChildName(), benef);
							} else {
								QuoChildBenef childBenefit = childMap.get(child.getChildName());
								ArrayList<QuoBenf> benflist = childBenefit.getBenfs();
								qb.setPremium(quo_Benef_Child_Details.getPremium());
								benflist.add(qb);

								childMap.get(child.getChildName()).setBenfs(benflist);
							}
						}

					}

				} else {

				}
			}
		}

		Set<Entry<String, QuoChildBenef>> benefs = childMap.entrySet();
		ArrayList<QuoChildBenef> childBenefList = new ArrayList<>();
		for (Entry<String, QuoChildBenef> entry : benefs) {// get all map data and add to arraylist
			QuoChildBenef cb = entry.getValue();
			childBenefList.add(cb);

			for (Children children : childrenList) {
				if (children.get_cName().equals(entry.getKey())) {
					for (QuoBenf bnf : cb.getBenfs()) {
						if (bnf.getBenfName().equals("CIBC")) {
							children.set_cCibc(true);
						}
						/*
						 * if(bnf.getBenfName().equals("HRBC")) { children.set_cHrbc(true); }
						 */
						if (bnf.getBenfName().equals("SHCBIC")) {
							children.set_cSuhrbc(true);
						}
						if (bnf.getBenfName().equals("HBC")) {
							children.set_cHbc(true);
						}

						if (bnf.getBenfName().equals("HCBIC")) {
							children.set_cHrbic(true);
						}

						if (bnf.getBenfName().equals("HCBFC")) {
							children.set_cHrbfc(true);
						}
					}
				}
			}
		}

		editQuotation.set_children(childrenList);
		editQuotation.set_mainLifeBenefits(mainLifeBenef);
		editQuotation.set_spouseBenefits(spouseBenef);
		editQuotation.set_childrenBenefits(childBenef);

		return editQuotation;

	}

	private Plan getPlanDetails(QuotationDetails details) {
		Plan plan = new Plan();
		plan.set_bsa(details.getBaseSum());
		plan.set_term(details.getPolTerm());
		plan.set_interestRate(details.getInterestRate());
		plan.set_payingterm(details.getPaingTerm());
		plan.setPensionPaingTerm(details.getPensionTerm());
		plan.setRetAge(details.getRetirmentAge());
		plan.set_payingterm(details.getPaingTerm());

		// System.out.println("paing term " + details.getPaingTerm());

		switch (details.getPayMode()) {
		case "M":
			plan.set_frequance("Monthly");
			plan.setContribution(details.getPremiumMonth());
			plan.set_bsaTotal(details.getPremiumMonthT());
			break;
		case "Q":
			plan.set_frequance("Quartaly");
			plan.setContribution(details.getPremiumQuater());
			plan.set_bsaTotal(details.getPremiumQuaterT());
			break;
		case "H":
			plan.set_frequance("Half Yearly");
			plan.setContribution(details.getPremiumHalf());
			plan.set_bsaTotal(details.getPremiumHalfT());
			break;
		case "Y":
			plan.set_frequance("Yearly");
			plan.setContribution(details.getPremiumYear());
			plan.set_bsaTotal(details.getPremiumYearT());
			break;
		case "S":
			plan.set_frequance("Single Premium");
			plan.setContribution(details.getPremiumSingle());
			plan.set_bsaTotal(details.getPremiumSingleT());
			break;

		default:
			break;
		}

		plan.setPolicyFee(details.getPolicyFee());
		plan.setAdminFee(details.getAdminFee());
		plan.setTax(plan.getAdminFee());
		plan.setGrsprm(plan.get_bsaTotal() - (plan.getAdminFee() + plan.getPolicyFee() + plan.getTax()));
		plan.setSumatRiskMain(details.getSumAtRiskMain());
		plan.setSumatRiskSpouse(details.getSumAtRiskSpouse());
		plan.setInvPos(details.getInvestmentPos());
		plan.setLifePos(details.getLifePos());

		return plan;
	}

	@Override
	public QuotationDetails findFirstByQuotationOrderByQdIdDesc(Integer quotationId) throws Exception {
		Quotation quotation = quotationService.getQuotation(quotationId);
		if (quotation != null) {
			QuotationDetails quotationDetails = quotationDetailsDao.findFirstByQuotationOrderByQdIdDesc(quotation);
			if (quotationDetails != null) {
				return quotationDetails;
			}
		}

		return null;
	}

	@Override
	public QuotationReceipt findQuotationDetailsForReceipt(Integer qdId) throws Exception {
		QuotationDetails details = quotationDetailsDao.findByQdId(qdId);

		QuotationReceipt quotationReceipt = new QuotationReceipt();
		quotationReceipt.setBranchCode(details.getQuotation().getUser().getBranch().getBranch_Code());
		quotationReceipt.setAgentCode(details.getQuotationCreateBy());
		quotationReceipt.setCustomerName(details.getCustomerDetails().getCustName());
		quotationReceipt.setCustTitle(details.getCustomerDetails().getCustTitle());
		quotationReceipt.setQuotationDetailId(details.getQdId());
		quotationReceipt.setQuotationId(details.getQuotation().getId());
		quotationReceipt.setProductCode(details.getQuotation().getProducts().getProductCode());
		quotationReceipt.setProductName(details.getQuotation().getProducts().getProductName());
		switch (details.getPayMode()) {
		case "M":
			quotationReceipt.setPremium(details.getPremiumMonthT());
			break;
		case "Q":
			quotationReceipt.setPremium(details.getPremiumQuaterT());
			break;
		case "H":
			quotationReceipt.setPremium(details.getPremiumHalfT());
			break;
		case "Y":
			quotationReceipt.setPremium(details.getPremiumYearT());
			break;
		case "S":
			quotationReceipt.setPremium(details.getPremiumSingleT());
			break;

		default:
			break;
		}
		return quotationReceipt;
	}
	
	

	@Override
	public boolean isAvailable(Integer qdId, Integer qId) throws Exception {
		QuotationDetails details = findQuotationDetails(qdId);
		if (details != null && details.getQuotation().getId().equals(qId)) {
			return true;
		}
		return false;
	}

	public EditQuotation editQuotationDetailsView(Integer qdId) throws Exception {
		QuotationDetails details = findQuotationDetails(qdId);
		EditQuotation editQuotation = new EditQuotation();
		MainLife mainLife = new MainLife();
		Spouse spouse = new Spouse();
		if (details != null) {
			CustomerDetails customerDetails = details.getCustomerDetails();
			mainLife.set_mName(customerDetails.getCustName());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			LocalDate dateOfBirth = LocalDate.parse(dateFormat.format(customerDetails.getCustDob()));
			LocalDate currentDate = LocalDate.parse(dateFormat.format(details.getQuotationquotationCreateDate()));
			// LocalDate currentDate = LocalDate.now();
			long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
			diffInYears += 1;
			String age = Long.toString(diffInYears);

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");

			mainLife.set_mAge(age);
			mainLife.set_mDob(dateFormat1.format(customerDetails.getCustDob()));
			mainLife.set_mEmail(customerDetails.getCustEmail());
			mainLife.set_mGender(customerDetails.getCustGender());
			mainLife.set_mMobile(customerDetails.getCustTel());
			mainLife.set_mNic(customerDetails.getCustNic());
			mainLife.set_mOccupation(Integer.toString(customerDetails.getOccupation().getOcupationid()));
			mainLife.set_mSmoking("No");
			mainLife.set_mTitle(customerDetails.getCustTitle());
			mainLife.set_mCivilStatus(customerDetails.getCustCivilStatus());

			if (details.getSpouseDetails() != null) {
				CustomerDetails spouseDetails = details.getSpouseDetails();
				spouse.set_sName(spouseDetails.getCustName());

				LocalDate sdateOfBirth = LocalDate.parse(dateFormat.format(spouseDetails.getCustDob()));
				LocalDate scurrentDate = LocalDate.parse(dateFormat.format(details.getQuotationquotationCreateDate()));
				long sdiffInYears = ChronoUnit.YEARS.between(sdateOfBirth, scurrentDate);
				sdiffInYears += 1;
				String sage = Long.toString(sdiffInYears);

				spouse.set_sActive(true);
				spouse.set_sAge(sage);
				spouse.set_sDob(dateFormat1.format(spouseDetails.getCustDob()));
				spouse.set_sGender(spouseDetails.getCustGender());
				spouse.set_sNic(spouseDetails.getCustNic());
				spouse.set_sOccupation(Integer.toString(spouseDetails.getOccupation().getOcupationid()));
				spouse.set_sTitle(spouseDetails.getCustTitle());

			} else {
				spouse.set_sActive(false);
			}
		}


		editQuotation.set_mainlife(mainLife);
		editQuotation.set_spouse(spouse);
		editQuotation.set_plan(getPlanDetails(details));

		List<Nominee> nominees = nomineeDao.findByQuotationDetails(details);
		if (nominees.size() > 0) {
			editQuotation.get_plan().set_nomineeName(nominees.get(0).getNomineeName());
			editQuotation.get_plan().set_nomineeAge(nominees.get(0).getAge());
			editQuotation.get_plan()
					.set_nomineedob(new SimpleDateFormat("dd-MM-yyyy").format(nominees.get(0).getNomineeDob()));
			editQuotation.get_plan().set_nomoneeRelation(nominees.get(0).getRelation());
		}

		// return editQuotation;
		return getBenefitsAndChildDetails(details, editQuotation);


	}

	@Override
	public boolean updateStatus(Integer qdId) throws Exception {
		QuotationDetails details = quotationDetailsDao.findByQdId(qdId);
		Quotation quotation = details.getQuotation();
		quotation.setStatus("PROP");
		quotationDao.save(quotation);
		return true;
	}

}
