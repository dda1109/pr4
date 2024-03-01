package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAll();

    void deleteCategoryById(int id);
}
