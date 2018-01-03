package org.arpicoinsurance.groupit.main.dao;

import java.util.List;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.arpicoinsurance.groupit.main.model.Quotation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface Quo_Benef_DetailsDao extends CrudRepository<Quo_Benef_Details,String> {
	@Query("from Quo_Benef_Details where id=?1")
	Quo_Benef_Details findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Quo_Benef_Details where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	@Query("Select qbd from Quo_Benef_Details qbd inner join qbd.quotationDetails qd inner join "
			+ "qd.quotation q inner join q.user u where (qd) IN (from QuotationDetails where quotation_id=q.id order by quotationquotation_create_date desc) and q.status='quotation' and u.userId=?1")
	List<Quo_Benef_Details> findByUserId(Integer id) throws Exception;
	
}
