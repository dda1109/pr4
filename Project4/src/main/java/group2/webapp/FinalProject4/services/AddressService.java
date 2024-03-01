package group2.webapp.FinalProject4.services;

import group2.webapp.FinalProject4.models.Address;
import group2.webapp.FinalProject4.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AddressService {

    List<Address> findAllByUser(User user);

    List<Address> findAll();

    Address getById(Integer id);

    void deleteAddressById(Integer id);

    void saveAddress(Address address);

    Page<Address> pagingAddress(int offset, int pagesize);
}
