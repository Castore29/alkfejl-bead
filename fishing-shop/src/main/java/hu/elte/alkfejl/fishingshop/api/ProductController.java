package hu.elte.alkfejl.fishingshop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.annotation.Role;
import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.service.ProductService;
import lombok.extern.slf4j.Slf4j;

import static hu.elte.alkfejl.fishingshop.model.User.Role.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * The ProductController handles incoming requests from the frontend regarding
 * products. It uses the ProductService to compose a response.
 */

// Lombok annotation for easy logging
@Slf4j
// Spring annotation to mark this controller RESTful
@RestController
// Spring annotation to define the main endpoint of this controller for the
// dispatcher
@RequestMapping("/api/product")
// Enables CORS, so we can reach the backend through a browser and properly use
// cookies for session identification
@CrossOrigin("*")
public class ProductController {

	// Spring annotation to mark the orderService for dependency injection
	@Autowired
	private ProductService productService;

	@Autowired
	// The mapper is used to map a JSON formatted String to a Product
	private ObjectMapper mapper;

	// Custom annotation to mark which type of users can reach this endpoint (all of
	// them in this case)
	@Role({ ADMIN, USER, GUEST })
	// Maps to the /api/product/categories endpoint for GET requests
	@GetMapping("/categories")
	// This method returns every category and their subcategories in a Map<String,
	// Set<String>> format. Should be requested every time someone loads the webshop
	public ResponseEntity<Map<String, Set<String>>> getCategories() {
		return ResponseEntity.ok(productService.listCategories());
	}

	@Role({ ADMIN, USER, GUEST })
	@GetMapping("/list")
	// This method returns a list of products by category and subcategory with
	// optional pagination. Should be requested for basic product listing
	public ResponseEntity<Iterable<Product>> getProducts(
			@RequestParam(name = "category", required = true) String category,
			@RequestParam(name = "subCategory", required = true) String subCategory,
			@PageableDefault(size = 10, sort = { "discount",
					"price" }, direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(productService.list(category, subCategory, pageable));
	}

	@Role({ ADMIN, USER, GUEST })
	@GetMapping("/search")
	// This method allows complex search parameters with the use of request
	// parameters which are handled by QueryDsl's Predicate and optional pagination
	public ResponseEntity<Iterable<Product>> searchProducts(
			@QuerydslPredicate(root = Product.class) Predicate predicate,
			@PageableDefault(size = 10, sort = "price", direction = Sort.Direction.DESC) Pageable pageable) {
		return ResponseEntity.ok(productService.advancedSearch(predicate, pageable));
	}

	@Role(ADMIN)
	@GetMapping("/listDeleted")
	// This method returns all of the soft deleted products. There is currently no
	// option to reactivate deleted products
	public ResponseEntity<Iterable<Product>> getDeletedProducts() {
		return ResponseEntity.ok(productService.listDeleted());
	}

	@Role(ADMIN)
	@PostMapping("/save")
	/**
	 * This method expects Content-Type multipart/mixed data. The product part
	 * should come as a JSON string, which the mapper will try to deserialize into a
	 * Product object. The file type is a MultipartFile (should be JPEG), which will
	 * be added to the product by the productService. If any error occurs during
	 * this process, the error message will be send back to the user
	 */
	public ResponseEntity<?> postProduct(@RequestPart("product") String productJson,
			@RequestPart(name = "file", required = false) MultipartFile image) {
		try {
			Product product = mapper.readValue(productJson, Product.class);
			return ResponseEntity.ok(productService.createOrUpdate(product, image));
		} catch (DataIntegrityViolationException e) {
			log.warn("A megadott termékazonosító már létezik!");
			return ResponseEntity.badRequest().body("A megadott termékazonosító már létezik!");
		} catch (IllegalStateException e) {
			log.warn("A kép túl nagy! Max. 1 MB");
			return ResponseEntity.badRequest().body("A kép túl nagy! Max. 1 MB");
		} catch (IllegalArgumentException e) {
			log.warn(e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (JsonParseException | JsonMappingException e) {
			log.warn("Hiba a JSON átalakításban!");
			return ResponseEntity.badRequest().body("Hiba a JSON átalakításban!");
		} catch (IOException e) {
			log.warn("Hiba a kép fájl mentés közben!");
			return ResponseEntity.badRequest().body("Hiba a kép fájl mentés közben!");
		}
	}

	@Role(ADMIN)
	@DeleteMapping("/delete")
	// This method deletes the given product, it should be rarely used, the available
	// property can be used to mark the product as not available
	public ResponseEntity<Product> deleteProduct(@RequestBody Product product) {
		productService.delete(product);
		return ResponseEntity.ok().build();
	}

}
