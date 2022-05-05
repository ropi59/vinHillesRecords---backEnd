package fr.insy2s.commerce.repositories.custom;

import fr.insy2s.commerce.models.Product;
import fr.insy2s.commerce.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.util.List;

@Repository
public class ProductCustomRepository implements ProductCustomRepositoryInterface {

  @Autowired
  private EntityManager entityManager;

  @Override
  public List<Product> getProductsByTagId(Long id) {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);

    Root<Product> products = query.from(Product.class);

    Join<Product, Tag> tags = products.join("tags");
    query.select(products).distinct(true);
    Predicate predicate = criteriaBuilder.equal(tags.get("id"), id);
    query.where(predicate);
    TypedQuery<Product> typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultList();
  }
}
