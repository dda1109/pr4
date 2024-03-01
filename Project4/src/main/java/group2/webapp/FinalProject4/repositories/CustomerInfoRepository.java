package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.CustomerInfo;
import group2.webapp.FinalProject4.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Integer> {
    CustomerInfo findByUser(User user);

    CustomerInfo findByEmail(String email);

}
