package hu.elte.alkfejl.fishingshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.model.Tag;
import hu.elte.alkfejl.fishingshop.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Product createOrUpdate(Product product) {
		for (Tag t : product.getTags()) {
			t.getProducts().add(product);
		}
		return productRepository.save(product);
	}

	public void delete(Product product) {
		productRepository.delete(product);
	}

	public Iterable<Product> list() {
		return productRepository.findAll();
	}

}
