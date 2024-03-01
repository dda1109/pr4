package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.Address;
import group2.webapp.FinalProject4.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddresRepository extends JpaRepository<Address, Integer> {

    List<Address> findAllByUser(User user);

}
