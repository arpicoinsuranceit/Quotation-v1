package org.arpicoinsurance.groupit.main.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.dao.QuotationDetailsDao;
import org.arpicoinsurance.groupit.main.dao.UsersDao;
import org.arpicoinsurance.groupit.main.helper.QuoDetails;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.arpicoinsurance.groupit.main.model.QuotationDetails;
import org.arpicoinsurance.groupit.main.model.Users;
import org.arpicoinsurance.groupit.main.service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class QuotationServiceImpl implements QuotationService{
	
	@Autowired
	private QuotationDetailsDao quotationDetailsDao;
	
	@Autowired
	private QuotationDao quotationDao;
	
	@Autowired
	private UsersDao usersDao;

	@Override
	public boolean saveQuotation(Quo_Benef_Details qbd) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateQuotation(Quo_Benef_Details qbd) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteQuotation(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Quotation getQuotation(Integer id) throws Exception {
		return quotationDao.findById(id);
	}

	@Override
	public List<Quotation> getAllQuotation() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Quotation> getQuotationByUserId(Users user,List<String> status) throws Exception {
			return quotationDao.findByUserAndStatusInOrderByIdDesc(user, status);
	}
	
	@Override
	public ArrayList<QuoDetails> getQuotationDetails(Integer id) throws Exception{
		
		Users users=usersDao.findOne(id);
		
		List<String> status = new ArrayList<>();
		status.add("active");
		status.add("recal");
		
		if(users!=null) {
			ArrayList<Quotation> quoList=(ArrayList<Quotation>) getQuotationByUserId(users, status);
			if(quoList!=null) {
				ArrayList<QuoDetails> quoDetailsList = new ArrayList<>();
				for (Quotation quotation : quoList) {
					LocalDate currentTime=LocalDate.now();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					
					QuoDetails details=new QuoDetails();
					details.setQuotationNum(quotation.getId());
					QuotationDetails quotationDetails = quotationDetailsDao.findFirstByQuotationOrderByQdIdDesc(quotation);
					String parsedDate = formatter.format(quotationDetails.getQuotationCreateDate());
					
					LocalDate quotationCreateTime=LocalDate.parse(parsedDate);
					
					long datediff=ChronoUnit.DAYS.between(quotationCreateTime, currentTime);
					details.setCustomerName(quotationDetails.getCustomerDetails().getCustName());
					details.setCustomerNic(quotationDetails.getCustomerDetails().getCustNic());
					details.setBranchCode(quotation.getUser().getBranch().getBranch_Code());
					details.setProductCode(quotation.getProducts().getProductCode());
					
					if(datediff <= 60) {
						quoDetailsList.add(details);
					}
					
				}
				
				return quoDetailsList;
				
			}
		}
		
		return null;
		
	}

}
