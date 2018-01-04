package org.arpicoinsurance.groupit.main.dao.impl;

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.arpicoinsurance.groupit.main.dao.QuotationDao;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.springframework.stereotype.Repository;


@Repository
public class QuotationDaoImpl implements QuotationDao{
	
	@PersistenceContext
    EntityManager entityManager;

	@Override
	public Quotation findOne(Integer id) throws Exception {
		String sql="Select * from Quotation where id=?";
		Query query=entityManager.createNativeQuery(sql,Quotation.class);
		query.setParameter(1, id);
		ArrayList<Quotation> quotation=(ArrayList<Quotation>) query.getResultList();
		
		if(quotation!=null) {
			return quotation.get(0);
		}
		
		return null;
	}

	@Override
	public Integer deleteOne(Integer id) throws Exception {
		String sql="Delete from Quotation where id=?";
		Query query=entityManager.createNativeQuery(sql,Quotation.class);
		query.setParameter(1, id);
		return query.getFirstResult();
	}

	@Override
	public ArrayList<Quotation> findByUserId(Integer id) throws Exception {
		String sql="Select * from Quotation q,Customer_Details cd,Products p,Users u,Branch b where "
				+ "q.customer_id=cd.cust_id and q.product_id=p.product_id and q.user_id=u.user_id and u.branch_branch_id=b.branch_id and q.status='quotation' and q.user_id=?";
		Query query=entityManager.createNativeQuery(sql,Quotation.class);
		query.setParameter(1, id);
		
		return (ArrayList<Quotation>) query.getResultList();
	}

}
