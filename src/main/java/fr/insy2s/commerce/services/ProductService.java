package fr.insy2s.commerce.services;

import fr.insy2s.commerce.mappers.ProductMapper;
import fr.insy2s.commerce.dtos.product.ProductDtoNoId;
import fr.insy2s.commerce.models.Product;
import fr.insy2s.commerce.repositories.ProductRepository;
import fr.insy2s.commerce.services.interfaces.ProductServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductService implements ProductServiceInterface {
    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Optional<ProductDtoNoId> productDtoNoId) {
        Product product = ProductMapper.productDtoToProduct(productDtoNoId);
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public Product findOneProductById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Optional<ProductDtoNoId> productDtoNoId, Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product = ProductMapper.productDtoToProduct(productDtoNoId);
            product.setId(id);
            product = productRepository.save(product);
        }
        return product;
    }

}
