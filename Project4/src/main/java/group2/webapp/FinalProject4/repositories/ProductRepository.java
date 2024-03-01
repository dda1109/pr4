package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.Category;
import group2.webapp.FinalProject4.models.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Slice<Product> findAllByCategory(Category category, Pageable pageable);

    List<Product> findAllByCategory(Category category);

    List<Product> findAllByNameContains(String text, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM Product WHERE name like '%' :name '%'")
    List<Product> searchByName(@Param("name") String text);

    List<Product> findTop8ByNameIsNotNull();

    List<Product> findTop4ByNameIsNotNull(Sort sort);

    List<Product> findTop5ByCategory(Category category);

    void deleteProductById(int id);
}
