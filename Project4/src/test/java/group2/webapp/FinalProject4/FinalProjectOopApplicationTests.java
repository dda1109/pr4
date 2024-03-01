package group2.webapp.FinalProject4;

import group2.webapp.FinalProject4.repositories.*;
import group2.webapp.FinalProject4.services.BillDetailService;
import group2.webapp.FinalProject4.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FinalProjectOopApplicationTests {

    @Autowired
    public static CategoryRepository categoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    BillRepository billRepository;

    @Autowired
    BillDetailService billDetailService;

    @Test
    void contextLoads() {

    }

}
