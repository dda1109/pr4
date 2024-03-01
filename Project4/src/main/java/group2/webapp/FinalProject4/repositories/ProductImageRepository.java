package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.Product;
import group2.webapp.FinalProject4.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    List<ProductImage> findAll();

    List<ProductImage> findAllByProduct(Product product);

    void deleteProductImageById(int id);
}
