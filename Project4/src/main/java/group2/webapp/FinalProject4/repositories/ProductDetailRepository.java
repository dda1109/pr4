package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.Product;
import group2.webapp.FinalProject4.models.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    ProductDetail findByProduct(Product product);

    void deleteProductDetailById(int id);
}
