package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.helper.QuoBenf;
import org.arpicoinsurance.groupit.main.helper.QuoCustomer;
import org.arpicoinsurance.groupit.main.helper.QuotationView;
import org.arpicoinsurance.groupit.main.model.Benefits;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.service.Quo_Benef_DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Quo_Benef_DetailsServiceImpl implements Quo_Benef_DetailsService{

	@Autowired
	private Quo_Benef_DetailsDao quoBenefDao;
	
	@Autowired
	private QuotationDetailsDao quotationDetailsDao;
	
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
	public List<QuotationView> getQuo_Benef_DetailsByQuoDetailId(String quoNum) throws Exception {
		
		List<QuotationDetails> quotationDetails=new ArrayList<>();
		quotationDetailsDao.findByQuoNum(quoNum).forEach(quotationDetails::add);
		ArrayList<QuotationView> viewQuotationDetailsList=new ArrayList<>();
		
		if(!quotationDetails.isEmpty()) {
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

	//set customer and spouse details according to quotationdetail object
	private QuoCustomer setCustomerDetails(QuotationDetails quoDetails) {
		QuoCustomer customer=new QuoCustomer();
		customer.setTerm(quoDetails.getPayTerm());
		customer.setMode(quoDetails.getPayMode());
		
		if(quoDetails.getPayMode()!=null) {
			if(quoDetails.getPayMode().equals("m")) {
				customer.setModePremium(quoDetails.getPremiumMonth());
			}else if(quoDetails.getPayMode().equals("y")) {
				customer.setModePremium(quoDetails.getPremiumYear());
			}else if(quoDetails.getPayMode().equals("q")) {
				customer.setModePremium(quoDetails.getPremiumQuater());
			}else if(quoDetails.getPayMode().equals("h")) {
				customer.setModePremium(quoDetails.getPremiumHalf());
			}else {
				
			}
		}
		
		
		customer.setMainLifeName(quoDetails.getQuotation().getCustomerDetails().getCustName());
		customer.setMainLifeOccupation(quoDetails.getQuotation().getCustomerDetails().getOccupation().getOcupationName());
		customer.setMainLifeAge(quoDetails.getQuotation().getCustomerDetails().getCustDetailId());
		
		if(quoDetails.getQuotation().getSpouseDetails()!=null) {
			customer.setSpouseName(quoDetails.getQuotation().getSpouseDetails().getCustName());
			customer.setSpouseOccupation(quoDetails.getQuotation().getSpouseDetails().getOccupation().getOcupationName());
			customer.setSpouseAge(quoDetails.getQuotation().getSpouseDetails().getCustDetailId());
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
		ArrayList<QuoBenf> childBenef=new ArrayList<>();
		
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
				QuoBenf qb=new QuoBenf();
				qb.setBenfName(benf.getBenefitName());
				qb.setPremium(quo_Benef_Details.getRiderPremium());
				qb.setRiderSum(quo_Benef_Details.getRiderSum());
				childBenef.add(qb);
			}else {
				
			}
		}
		
		quotationView.setMainLifeBenf(mainLifeBenef);
		quotationView.setSpouseBenf(spouseBenef);
		quotationView.setChildBenf(childBenef);
		quotationView.setQuoDetailId(qdId);
		
		
		return quotationView;
	}

	
}
