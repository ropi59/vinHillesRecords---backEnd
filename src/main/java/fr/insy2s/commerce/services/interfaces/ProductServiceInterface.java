package fr.insy2s.commerce.services.interfaces;

import fr.insy2s.commerce.dtos.product.ProductDtoNoId;
import fr.insy2s.commerce.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductServiceInterface {

  public List<Product> findAllProducts();

  public Product findOneProductById(Long id);

  public Product createProduct(Optional<ProductDtoNoId> productDtoNoId);

  public Product updateProduct(Optional<ProductDtoNoId> productDtoNoId, Long id);
}
