package org.arpicoinsurance.groupit.main.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.custom.Quo_Benef_DetailsDaoCustom;
import org.arpicoinsurance.groupit.main.helper.QuoBenf;
import org.arpicoinsurance.groupit.main.helper.QuoChildBenef;
import org.arpicoinsurance.groupit.main.helper.QuoCustomer;
import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.model.Child;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Child_Details;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_Child_DetailsService;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Quo_Benef_DetailsServiceImpl implements Quo_Benef_DetailsService{

	@Autowired
	private Quo_Benef_DetailsDaoCustom quoBenefDao;
	
	@Autowired
	private Quo_Benef_DetailsDao quoBenefDetailsDao;
	
	@Autowired
	private QuotationDao quotationDao;
	
	@Autowired
	private QuotationDetailsDao quotationDetailsDao;
	
	@Autowired
	private Quo_Benef_Child_DetailsService childBenefService;
	
	
	
	@Override
	public boolean saveQuo_Benef_Details(Quo_Benef_Details qbd) throws Exception {
		return false;
	}

	@Override
	public boolean updateQuo_Benef_Details(Quo_Benef_Details qbd) throws Exception {
		return false;
	}

	@Override
	public boolean deleteQuo_Benef_Details(Integer id) throws Exception {
		if(quoBenefDao.deleteOne(id)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public Quo_Benef_Details getQuo_Benef_Details(Integer id) throws Exception {
		return quoBenefDao.findOne(id);
	}

	@Override
	public List<Quo_Benef_Details> getAllQuo_Benef_Details() throws Exception {
		return null;
	}

	@Override
	public List<QuotationDetails> getQuo_Benef_DetailsByQuoDetailId(Quotation quotation) throws Exception {
		
		return quotationDetailsDao.findByQuotationOrderByQdIdDesc(quotation);
		
	}

	//set customer and spouse details according to quotationdetail object
	private QuoCustomer setCustomerDetails(QuotationDetails quoDetails) {
		QuoCustomer customer=new QuoCustomer();
		customer.setTerm(quoDetails.getPayTerm());
		customer.setMode(quoDetails.getPayMode());
		
		if(quoDetails.getPayMode()!=null) {
			if(quoDetails.getPayMode().equals("M")) {
				customer.setModePremium(quoDetails.getPremiumMonthT());
			}else if(quoDetails.getPayMode().equals("Y")) {
				customer.setModePremium(quoDetails.getPremiumYearT());
			}else if(quoDetails.getPayMode().equals("Q")) {
				customer.setModePremium(quoDetails.getPremiumQuaterT());
			}else if(quoDetails.getPayMode().equals("H")) {
				customer.setModePremium(quoDetails.getPremiumHalfT());
			}else if(quoDetails.getPayMode().equals("S")) {
				customer.setModePremium(quoDetails.getPremiumSingleT());
			}else {
				
			}
		}
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		
		LocalDate dateOfBirth = LocalDate.parse(dateFormat.format(quoDetails.getQuotation().getCustomerDetails().getCustDob()));
	    LocalDate currentDate = LocalDate.parse(dateFormat.format(quoDetails.getQuotationquotationCreateDate()));
	    long diffInYears = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
	    diffInYears+=1;
	    String age=Long.toString(diffInYears);
	    
		customer.setMainLifeName(quoDetails.getQuotation().getCustomerDetails().getCustName());
		customer.setMainLifeOccupation(quoDetails.getQuotation().getCustomerDetails().getOccupation().getOcupationName());
		customer.setMainLifeAge(Integer.parseInt(age));
		
		LocalDate sdateOfBirth = LocalDate.parse(dateFormat.format(quoDetails.getQuotation().getCustomerDetails().getCustDob()));
	    LocalDate scurrentDate = LocalDate.parse(dateFormat.format(quoDetails.getQuotationquotationCreateDate()));
	    long sdiffInYears = ChronoUnit.YEARS.between(sdateOfBirth, scurrentDate);
	    sdiffInYears+=1;
	    String sage=Long.toString(sdiffInYears);
		
		if(quoDetails.getQuotation().getSpouseDetails()!=null) {
			customer.setSpouseName(quoDetails.getQuotation().getSpouseDetails().getCustName());
			customer.setSpouseOccupation(quoDetails.getQuotation().getSpouseDetails().getOccupation().getOcupationName());
			customer.setSpouseAge(Integer.parseInt(sage));
		}else {
			customer.setSpouseName(null);
			customer.setSpouseOccupation(null);
			customer.setSpouseAge(null);
		}
		
		
		return customer;
	}

	//Get all benefits using quotationdetail Id..
	private QuotationView getQuotationBenfList(List<Quo_Benef_Details> benfDetails,QuoCustomer customer,Integer qdId) throws Exception {
		ArrayList<QuoBenf> mainLifeBenef=new ArrayList<>();
		ArrayList<QuoBenf> spouseBenef=new ArrayList<>();
		
		TreeMap< String, QuoChildBenef> childMap=new TreeMap<>();
		
		QuotationView quotationView=new QuotationView();
		quotationView.setCustDetails(customer);
		
		for (Quo_Benef_Details quo_Benef_Details : benfDetails) {
			Benefits benf=quo_Benef_Details.getBenefit();
			if(benf.getBenefitType().equals("s")) {//check benf_type is spouse
				QuoBenf qb=new QuoBenf();
				qb.setBenfName(benf.getBenefitName());
				qb.setPremium(quo_Benef_Details.getRiderPremium());
				qb.setRiderSum(quo_Benef_Details.getRiderSum());
				spouseBenef.add(qb);
			}else if(benf.getBenefitType().equals("m")) {//check benf_type is mainLife
				QuoBenf qb=new QuoBenf();
				qb.setBenfName(benf.getBenefitName());
				qb.setPremium(quo_Benef_Details.getRiderPremium());
				qb.setRiderSum(quo_Benef_Details.getRiderSum());
				mainLifeBenef.add(qb);
			}else if(benf.getBenefitType().equals("c")) {//check benf_type is child
				
				List<Quo_Benef_Child_Details> qbcd=childBenefService.getQuo_Benef_Child_DetailsByQuo_Benf_DetailsId(quo_Benef_Details.getQuo_Benef_DetailsId());
				if(!qbcd.isEmpty()) {
					QuoBenf qb=new QuoBenf();
					qb.setBenfName(benf.getBenefitName());
					qb.setRiderSum(quo_Benef_Details.getRiderSum());
					
					for (Quo_Benef_Child_Details quo_Benef_Child_Details : qbcd) {
						Child child=quo_Benef_Child_Details.getCustChildDetails().getChild();
						if(!childMap.containsKey(child.getChildName())) {
							ArrayList<QuoBenf> benfs=new ArrayList<>();//create list of benefits
							qb.setPremium(quo_Benef_Child_Details.getPremium());
							benfs.add(qb);
							
							QuoChildBenef benef=new QuoChildBenef();//create QuoChildBenef object
							benef.setChild(child);
							benef.setBenfs(benfs);//set list of benefits
							
							childMap.put(child.getChildName(), benef);
						}else {
							QuoChildBenef childBenefit=childMap.get(child.getChildName());
							ArrayList<QuoBenf> benflist=childBenefit.getBenfs();
							qb.setPremium(quo_Benef_Child_Details.getPremium());
							benflist.add(qb);
							
							childMap.get(child.getChildName()).setBenfs(benflist);
						}
					}
					
				}
				
				
			}else {
				
			}
		}
		
		Set<Entry<String, QuoChildBenef>> benefs=childMap.entrySet();
		ArrayList<QuoChildBenef> childBenefList=new ArrayList<>();
		for (Entry<String, QuoChildBenef> entry : benefs) {// get all map data and add to arraylist
			QuoChildBenef cb=entry.getValue();
			childBenefList.add(cb);
		}
		
		quotationView.setMainLifeBenf(mainLifeBenef);
		quotationView.setSpouseBenf(spouseBenef);
		quotationView.setChildBenf(childBenefList);
		quotationView.setQuoDetailId(qdId);
		
		
		return quotationView;
	}

	@Override
	public List<QuotationView> getQuo_Benef_DetailsByQuoDetailId(Integer id) throws Exception {
		Quotation quotation=quotationDao.findById(id);
		ArrayList<QuotationDetails> quotationDetails=(ArrayList<QuotationDetails>) getQuo_Benef_DetailsByQuoDetailId(quotation);
		
		ArrayList<QuotationView> viewQuotationDetailsList=new ArrayList<>();
		
		if(!quotationDetails.isEmpty() || quotationDetails != null) {
			for (QuotationDetails quoDetails : quotationDetails) {
				QuoCustomer customer=setCustomerDetails(quoDetails);
				List<Quo_Benef_Details> benef_Details=new ArrayList<>();
				quoBenefDao.findByQuoDetailId(quoDetails.getQdId()).forEach(benef_Details::add);
				
				if(!benef_Details.isEmpty()) {
					QuotationView quotationView=getQuotationBenfList(benef_Details, customer, quoDetails.getQdId());
					viewQuotationDetailsList.add(quotationView);
				}
				
			}
		}
		
		return viewQuotationDetailsList;
	}

	@Override
	public List<Quo_Benef_Details> findByQuotationDetails(QuotationDetails quotation) throws Exception {
		
		return quoBenefDetailsDao.findByQuotationDetails(quotation);
	}

	
}
