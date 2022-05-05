package fr.insy2s.commerce.controllers;

import fr.insy2s.commerce.dtos.product.ProductDtoNoId;
import fr.insy2s.commerce.models.Product;
import fr.insy2s.commerce.repositories.custom.ProductCustomRepository;
import fr.insy2s.commerce.services.ProductService;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost/")
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;
  private final ProductCustomRepository productCustomRepository;

  /**
   * API to create a product
   *
   * // @param productDtoNoId
   * @return ProductDto productDto
   */
  @PostMapping()
  public ResponseEntity<Product> create(@Valid @RequestBody Optional<ProductDtoNoId> productDtoNoId) {
    if(productDtoNoId.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }else {
      Product newProduct = productService.createProduct(productDtoNoId);
      return newProduct == null ?
              ResponseEntity.badRequest().build() :
              new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }
  }

  /**
   * API to get all products
   *
   * @return ProductDto products
   */
  @GetMapping()
  public List<Product> getAllProduct() {
    return productService.findAllProducts();
  }

  /**
   * API to get one product by id
   *
   * @param id of a product
   * @return ProductDto productDto
   */
  @GetMapping("{id}")
  public ResponseEntity<Product> getOneProduct(@PathVariable Long id) {
    Product product = productService.findOneProductById(id);
    return product == null ?
            ResponseEntity.notFound().build() :
            ResponseEntity.ok(product);
  }

  /**
   * API to update a product
   * @params productDto as information of a product, id of a product
   * @return ProductDto productDto
   */
  @PutMapping("{id}")
  public ResponseEntity<Product> update(@RequestBody Optional<ProductDtoNoId> productDtoRequest, @PathVariable Long id) {
    Product product = productService.updateProduct(productDtoRequest, id);
    return product == null ?
            ResponseEntity.badRequest().build() :
            ResponseEntity.ok(product);
  }

  /**
   * API to get products form tagId
   *
   * @param search
   * @return List<Product> productDto
   */
  @GetMapping(params = "search")
  public List<Product> getProductsByTagId(@RequestParam(value = "search") String search) {
    return  StringUtils.isNumeric(search) ?
    productCustomRepository.getProductsByTagId(Long.parseLong(search)) :
            new ArrayList<>();
  }
}
