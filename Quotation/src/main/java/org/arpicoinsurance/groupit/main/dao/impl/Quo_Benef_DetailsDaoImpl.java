package org.arpicoinsurance.groupit.main.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.arpicoinsurance.groupit.main.dao.custom.Quo_Benef_DetailsDaoCustom;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.springframework.stereotype.Repository;

@Repository
public class Quo_Benef_DetailsDaoImpl implements Quo_Benef_DetailsDaoCustom {
	
	@PersistenceContext
    EntityManager entityManager;

	@Override
	public Quo_Benef_Details findOne(Integer id) throws Exception {
		String sql="Select * from Quo_Benef_Details where quo_benef_details_id=?";
		Query query=entityManager.createNativeQuery(sql,Quo_Benef_Details.class);
		query.setParameter(1, id);
		ArrayList<Quo_Benef_Details> benfDetails=(ArrayList<Quo_Benef_Details>) query.getResultList();
		
		if(benfDetails!=null) {
			return benfDetails.get(0);
		}
		
		return null;
	}

	@Override
	public Integer deleteOne(Integer id) throws Exception {
		String sql="Delete from Quo_Benef_Details where quo_benef_details_id=?";
		Query query=entityManager.createNativeQuery(sql,Quo_Benef_Details.class);
		query.setParameter(1, id);
		return query.getFirstResult();

	}

	@Override
	public List<Quo_Benef_Details> findByQuoDetailId(Integer id) throws Exception {
		String sql="Select * from Customer_Details cd,Quotation_Details qd,Quo_Benef_Details qbd,Benefits b where \r\n" + 
				"qd.customer_id=cd.cust_detail_id and qbd.quodetails_id=qd.qd_id and b.id=qbd.benef_id and qd.qd_id=?";
		
		Query query=entityManager.createNativeQuery(sql,Quo_Benef_Details.class);
		query.setParameter(1, id);

		return query.getResultList();
	}

}
