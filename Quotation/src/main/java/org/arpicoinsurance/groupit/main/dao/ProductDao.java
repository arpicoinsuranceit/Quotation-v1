package org.arpicoinsurance.groupit.main.dao;

import org.arpicoinsurance.groupit.main.model.Products;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductDao extends CrudRepository<Products, String> {
	
	Products findByProductCode(String code) throws Exception;

	@Query("from Products where id=?1")
	Products findOne(Integer id) throws Exception;

	@Modifying
	@Query("DELETE FROM Products where id=?1")
	Integer deleteOne(Integer id) throws Exception;
}
