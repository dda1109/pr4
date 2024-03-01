package group2.webapp.FinalProject4.services.implement;

import group2.webapp.FinalProject4.models.Address;
import group2.webapp.FinalProject4.models.User;
import group2.webapp.FinalProject4.repositories.AddresRepository;
import group2.webapp.FinalProject4.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class AddressServiceImplement implements AddressService {

    @Autowired
    AddresRepository addresRepository;

    @Override
    public List<Address> findAllByUser(User user) {
        return addresRepository.findAllByUser(user);
    }

    @Override
    public List<Address> findAll() {
        return addresRepository.findAll();
    }

    @Override
    public Address getById(Integer id) {
        return addresRepository.getById(id);
    }

    @Override
    public void deleteAddressById(Integer id) {
        addresRepository.deleteById(id);
    }

    @Override
    public void saveAddress(Address address) {
        addresRepository.save(address);
    }

    @Override
    public Page<Address> pagingAddress(int offset, int pagesize) {
        return addresRepository.findAll(PageRequest.of(offset, pagesize, Sort.by("id").descending()));
    }
}
