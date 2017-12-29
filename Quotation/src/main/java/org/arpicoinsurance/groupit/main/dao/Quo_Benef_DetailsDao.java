package org.arpicoinsurance.groupit.main.dao;

import java.util.List;
import org.arpicoinsurance.groupit.main.model.Quo_Benef_Details;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface Quo_Benef_DetailsDao extends CrudRepository<Quo_Benef_Details,String> {
	@Query("from Quo_Benef_Details where id=?1")
	Quo_Benef_Details findOne(Integer id) throws Exception;
	
	@Modifying
	@Query("DELETE FROM Quo_Benef_Details where id=?1")
	Integer deleteOne(Integer id) throws Exception;
	
	@Query("from Quo_Benef_Details as qbd,Quotation as q,QuotationDetails as qd,Login as login")
	List<Quo_Benef_Details> findByUserId() throws Exception;
}
