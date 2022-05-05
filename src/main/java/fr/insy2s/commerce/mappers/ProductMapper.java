package fr.insy2s.commerce.mappers;

import fr.insy2s.commerce.dtos.product.ProductDtoNoId;
import fr.insy2s.commerce.models.Product;

import java.util.Optional;

public final class ProductMapper {

    private ProductMapper(){
        //Private constructor for sonarqube
    }

    /**
     * Convert a productDTO into a Product
     * @param productDtoNoId product information to convert
     * @return a product
     */
    public static Product productDtoToProduct(Optional<ProductDtoNoId> productDtoNoId) {
        Product product = new Product();
        if (productDtoNoId.isPresent()) {
            product.setTitle(productDtoNoId.get().getTitle());
            product.setArtist(productDtoNoId.get().getArtist());
            product.setDescription(productDtoNoId.get().getDescription());
            product.setPrice(productDtoNoId.get().getPrice());
            product.setLabel(productDtoNoId.get().getLabel());
            product.setFormat(productDtoNoId.get().getFormat());
            product.setStock(productDtoNoId.get().getStock());
            product.setCoverUrl(productDtoNoId.get().getCoverUrl());
            product.setReleaseDate(productDtoNoId.get().getReleaseDate());
            product.setDisponibilityDate(productDtoNoId.get().getDisponibilityDate());
            product.setTags(productDtoNoId.get().getTags());
        }
        return product;
    }

    /**
     * Convert a product into a productDTO
     * @param product the product information to convert
     * @return a productDTO
     */
    public static ProductDtoNoId productToProductDto(Product product) {
        ProductDtoNoId productDto = new ProductDtoNoId();
        productDto.setTitle(product.getTitle());
        productDto.setArtist(product.getArtist());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setLabel(product.getLabel());
        productDto.setFormat(product.getFormat());
        productDto.setStock(product.getStock());
        productDto.setCoverUrl(product.getCoverUrl());
        productDto.setReleaseDate(product.getReleaseDate());
        productDto.setDisponibilityDate(product.getDisponibilityDate());
        productDto.setTags(product.getTags());
        return productDto;
    }
}