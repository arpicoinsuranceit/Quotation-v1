package org.arpicoinsurance.groupit.main.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.arpicoinsurance.groupit.main.dao.Quo_Benef_DetailsDao;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.springframework.stereotype.Repository;

@Repository
public class Quo_Benef_DetailsDaoImpl implements Quo_Benef_DetailsDao {
	
	@PersistenceContext
    EntityManager entityManager;

	@Override
	public Quo_Benef_Details findOne(Integer id) throws Exception {
		Query query = entityManager.createNativeQuery("Select * from Quo_Benef_Details qbd,Quotation q,Quotation_Details qd where \r\n" + 
				"qd.quotation_id=q.id and qd.qd_id=qbd.quodetails_id\r\n" + 
				"and q.status='quotation' and qbd.quodetails_id=(Select qd_id from Quotation_Details where quotation_id=q.id order by quotationquotation_create_date desc limit 1) and q.user_id=? ", Quo_Benef_Details.class);
        query.setParameter(1, id);
        
        System.out.println(query.getResultList());
 
        return (Quo_Benef_Details) query.getResultList().get(0);
	}

	@Override
	public Integer deleteOne(Integer id) throws Exception {
		String queryString = "DELETE FROM Quo_Benef_Details where id=" + id;
		

		return null;

	}

	@Override
	public List<Quo_Benef_Details> findByUserId(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
