package hu.elte.alkfejl.fishingshop.service;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.model.QProduct;
import hu.elte.alkfejl.fishingshop.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product createOrUpdate(Product product) {
		Product p = productRepository.findOne(product.getId());
		if (p != null) {
			update(p, product);
		}
		return productRepository.save(product);
	}

	private void update(Product p, Product product) {
		for (Field field : p.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if (!"orders".equals(field.getName()) && field.get(product) == null) {
					field.set(product, field.get(p));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		product.setActive(true);
	}

	public void delete(Product product) {
		productRepository.delete(product);
	}

	public Iterable<Product> list(String category, String subCategory, Pageable pageable) {
		if (category != null && subCategory != null) {
			return productRepository.findByCategoryAndSubCategory(category, subCategory, pageable);
		}
		return productRepository.findAll();
	}

	public Iterable<String> listCategories() {
		return productRepository.findCategories();
	}

	public Iterable<String> listSubCategories(String category) {
		return productRepository.findSubCategories(category);
	}

	public Iterable<Product> listDeleted() {
		return productRepository.findInactive();
	}
	
	public Iterable<Product> advancedSearch(Predicate predicate, Pageable pageable) {
		predicate = ExpressionUtils.allOf(predicate, QProduct.product.active.eq(true));
		return productRepository.findAll(predicate, pageable);
	}

}
