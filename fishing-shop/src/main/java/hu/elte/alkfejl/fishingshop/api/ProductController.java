package hu.elte.alkfejl.fishingshop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/list")
	public ResponseEntity<Iterable<Product>> getProducts() {
		return ResponseEntity.ok(productService.list());
	}

	@PostMapping("/add")
	public ResponseEntity<Product> postProduct(@RequestBody Product product) {
		return ResponseEntity.ok(productService.createOrUpdate(product));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Product> deleteProduct(@RequestBody Product product) {
		productService.delete(product);
		return ResponseEntity.ok().build();
	}

}
