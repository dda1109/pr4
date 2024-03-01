package group2.webapp.FinalProject4.services.implement;

import group2.webapp.FinalProject4.models.Category;
import group2.webapp.FinalProject4.models.Product;
import group2.webapp.FinalProject4.repositories.ProductRepository;
import group2.webapp.FinalProject4.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class ProductServiceImplement implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAllByCategory(Category category) {
        return productRepository.findAllByCategory(category);
    }

    @Override
    public Page<Product> PagingAllProduct(int offset, int pageSize) {
        return productRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by("id").descending()));
    }

    @Override
    public Slice<Product> PagingProductByCategory(int offset, int pageSize, Category category) {
        return productRepository.findAllByCategory(category, PageRequest.of(offset, pageSize, Sort.by("id")));
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.getById(id);
    }

    @Override
    public List<Product> getProductSameCategory(Category category, Integer id) {
        List<Product> list = productRepository.findTop5ByCategory(category);

        for (Product p : list) {
            if (p.getId() == id) {
                list.remove(p);
                return list;
            }
        }

        list.remove(list.size() - 1);
        return list;
    }

    @Override
    public List<Product> findProductByName(String name, int offset, int pagesize) {
        return productRepository.findAllByNameContains(name, PageRequest.of(offset, pagesize));
    }

    @Override
    public List<Product> findAllProductByName(String name) {

        return productRepository.searchByName(name);
    }

    @Override
    public List<Product> getTop8Product() {
        return productRepository.findTop8ByNameIsNotNull();
    }

    @Override
    public List<Product> getLast4Product() {
        return productRepository.findTop4ByNameIsNotNull(Sort.by("id").descending());
    }

    @Override
    public List<Product> getTop4ProductBestSeller() {
        return productRepository.findTop4ByNameIsNotNull(Sort.by("amount").descending());
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteProductById(id);
    }
}
