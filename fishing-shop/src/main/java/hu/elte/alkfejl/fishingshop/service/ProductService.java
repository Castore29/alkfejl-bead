package hu.elte.alkfejl.fishingshop.service;

import java.io.IOException;
import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.model.QProduct;
import hu.elte.alkfejl.fishingshop.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product createOrUpdate(Product product, MultipartFile image) throws IllegalArgumentException, IOException {
		Product p = productRepository.findOne(product.getId());
		if (p != null) {
			update(p, product);
		}
		saveImage(image, product);

		return productRepository.save(product);
	}

	private void saveImage(MultipartFile image, Product product) throws IllegalArgumentException, IOException {
		if (image != null && !image.isEmpty()) {
			if (image.getContentType().equals("image/jpeg")) {
				product.setImage(image.getBytes());
			} else {
				throw new IllegalArgumentException();
			}
		}
	}

	private void update(Product p, Product product) {
		for (Field field : p.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if (field.get(product) == null) {
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
