package hu.elte.alkfejl.fishingshop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.annotation.Role;
import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.service.ProductService;

import static hu.elte.alkfejl.fishingshop.model.User.Role.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Role({ ADMIN, USER, GUEST })
	@GetMapping("/categories")
	public ResponseEntity<Iterable<String>> getCategories(
			@RequestParam(name = "cat", required = false) String category) {
		if (category != null) {
			return ResponseEntity.ok(productService.listSubCategories(category));
		}
		return ResponseEntity.ok(productService.listCategories());
	}

	@Role({ ADMIN, USER, GUEST })
	@GetMapping("/list")
	public ResponseEntity<Iterable<Product>> getProducts(@RequestParam(name = "cat", required = false) String category,
			@RequestParam(name = "subCat", required = false) String subCategory, Pageable pageable) {
		return ResponseEntity.ok(productService.list(category, subCategory, pageable));
	}

	@Role({ ADMIN, USER, GUEST })
	@GetMapping("/search")
	public ResponseEntity<Iterable<Product>> searchProducts(
			@QuerydslPredicate(root = Product.class) Predicate predicate,
			@PageableDefault(sort = "price", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(productService.advancedSearch(predicate, pageable));
	}

	@Role(ADMIN)
	@GetMapping("/listDeleted")
	public ResponseEntity<Iterable<Product>> getDeletedProducts() {
		return ResponseEntity.ok(productService.listDeleted());
	}

	@Role(ADMIN)
	@PostMapping("/save")
	public ResponseEntity<?> postProduct(@RequestBody Product product) {
		try {
			Product result = productService.createOrUpdate(product);
			return ResponseEntity.ok(result);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Item Number already exists!");
		}
	}

	@Role(ADMIN)
	@DeleteMapping("/delete")
	public ResponseEntity<Product> deleteProduct(@RequestBody Product product) {
		productService.delete(product);
		return ResponseEntity.ok().build();
	}

}
