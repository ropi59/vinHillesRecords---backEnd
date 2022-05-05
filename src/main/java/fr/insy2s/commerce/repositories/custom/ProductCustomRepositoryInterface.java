package fr.insy2s.commerce.repositories.custom;

import fr.insy2s.commerce.models.Product;

import java.util.List;

public interface ProductCustomRepositoryInterface {
  List<Product> getProductsByTagId(Long id);
}
