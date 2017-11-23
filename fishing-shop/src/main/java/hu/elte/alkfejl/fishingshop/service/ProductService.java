package hu.elte.alkfejl.fishingshop.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;

import hu.elte.alkfejl.fishingshop.model.Product;
import hu.elte.alkfejl.fishingshop.model.QProduct;
import hu.elte.alkfejl.fishingshop.repository.ProductRepository;

/**
 * The ProductService service handles the connection between the
 * ProductController and the ProductRepository. The service's methods are called
 * by the ProductController and the service calls the methods of the
 * ProductRepository. The service also makes modifications on the incoming and
 * outgoing data to keep up the consistency and the business plan.
 */

// Spring annotation to mark UserService as a Service
@Service
public class ProductService {

	// Spring annotation to mark the productRepository for dependency injection
	@Autowired
	private ProductRepository productRepository;

	/**
	 * The createOrUpdate(Product) method handles product creation and modification.
	 * Expects the product in two parts: a Product object and a MultipartFile object
	 * that is the image, later stored as a LOB in the database
	 */
	public Product createOrUpdate(Product product, MultipartFile image) throws IllegalArgumentException, IOException {
		// check if the product already exists
		Optional<Product> original = productRepository.findById(product.getId());
		if (original.isPresent()) {
			// update the null properties to the original's value
			update(product, original.get());
		}
		// save the image
		saveImage(product, image);

		// save the product through the repository
		return productRepository.save(product);
	}

	// The saveImage(Product, MultipartFile) method takes the Product object and
	// MultipartFile object in order to set the product's image
	private void saveImage(Product product, MultipartFile image) throws IllegalArgumentException, IOException {
		// if there is no image provided, we skip this
		if (image != null && !image.isEmpty()) {
			// if the image is not in JPEG format, we throw an exception
			if (image.getContentType().equals("image/jpeg")) {
				// if the image is JPEG, we retrieve the byte array and set the product's image
				product.setImage(image.getBytes());
			} else {
				throw new IllegalArgumentException("Rossz képformátum! JPEG szükséges");
			}
		}
	}

	/**
	 * The update(Product, Product) takes the Product to-be-updated and the original
	 * Product and sets every null field of the to-be-updated Product to the
	 * original's value using reflection. This method allows us to only send the
	 * modified properties in an update.
	 */
	private void update(Product product, Product original) {
		// we iterate through the declared fields of the Product class
		// Note: the inherited fields are not included in this
		for (Field field : original.getClass().getDeclaredFields()) {
			// we set the field accessible for modification
			field.setAccessible(true);
			// we catch any exception thrown by reflection, but exceptions should not happen
			// if we make sure that both objects are of the Product class
			try {
				// we check if the product's field is null
				if (field.get(product) == null) {
					// we set the product's null field to the original's value
					field.set(product, field.get(original));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// we make sure that the properties inherited from BaseEntity are also set
		// correctly
		product.setVersion(original.getVersion());
		product.setCreateDate(original.getCreateDate());
		product.setActive(true);
	}

	// The delete(Product) method simply soft deletes a given order
	public void delete(Product product) {
		productRepository.delete(product);
	}

	// The list(String, String, Pageable) method takes the category and the
	// subcategory and a Pageable object to list the corresponding products
	public Iterable<Product> list(String category, String subCategory, Pageable pageable) {
		return productRepository.findByCategoryAndSubCategory(category, subCategory, pageable);
	}

	// The listCategories() method constructs a Map<String, Set<String> to return
	// every category with their corresponding list of subcategories
	public Map<String, Set<String>> listCategories() {
		Map<String, Set<String>> categories = new HashMap<>();
		// we iterate over the Iterable of Object arrays received from the
		// productRepository
		for (Object[] row : productRepository.findCategories()) {
			// if the category is not present, we create put it in the map and create a new
			// empty set for it. Note: Object must be converted to String
			if (!categories.containsKey(row[0])) {
				categories.put((String) row[0], new HashSet<>());
			}
			// we put the subcategory in its category's set
			categories.get((String) row[0]).add((String) row[1]);
		}
		return categories;
	}

	// The listDeleted() method lists all of the soft deleted products
	public Iterable<Product> listDeleted() {
		return productRepository.findInactive();
	}

	// The advancedSearch(Predicate, Pageable) method takes the given Predicate and
	// Pageable objects and lists every matching product
	public Iterable<Product> advancedSearch(Predicate predicate, Pageable pageable) {
		// make a new Predicate, adding the active property to it as true
		predicate = ExpressionUtils.allOf(predicate, QProduct.product.active.eq(true));
		return productRepository.findAll(predicate, pageable);
	}

}
