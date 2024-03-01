package group2.webapp.FinalProject4.services;

import group2.webapp.FinalProject4.models.Product;
import group2.webapp.FinalProject4.models.ProductImage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductImageService {
    List<ProductImage> findAll();

    List<ProductImage> getAllImageOfProduct(Product product);

    Page<ProductImage> PagingAllProductImg(int offset, int pageSize);

    void save(ProductImage productImage);

    ProductImage getProductImgById(Integer id);

    void deleteProductImg(int id);
}
