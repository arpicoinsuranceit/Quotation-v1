package org.arpicoinsurance.groupit.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.arpicoinsurance.groupit.main.dao.ProductDao;
import org.arpicoinsurance.groupit.main.model.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductDao productDao;
	
	@Override
	public List<String> getActiveProducts() throws Exception {
		
		List<String> activeProducts = new ArrayList<>();
		
		List<Products> products = productDao.findAllByActive(1) ;
		
		for (Products product : products) {
			activeProducts.add(product.getProductCode());
		}
		
		return activeProducts;
	}

}
