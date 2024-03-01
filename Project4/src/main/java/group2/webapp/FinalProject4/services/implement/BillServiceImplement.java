package group2.webapp.FinalProject4.services.implement;

import group2.webapp.FinalProject4.models.Bill;
import group2.webapp.FinalProject4.models.User;
import group2.webapp.FinalProject4.repositories.BillRepository;
import group2.webapp.FinalProject4.services.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BillServiceImplement implements BillService {

    @Autowired
    BillRepository billRepository;

    @Override
    public Optional<Bill> findByUserAndStatus(User user, Integer status) {
        return billRepository.findByUserAndStatus(user, status);
    }

    @Override
    public int saveBill(Bill bill) {
        return billRepository.save(bill).getId();
    }

    @Override
    public List<Bill> findAllByDate(String year1, String year2) {
        return billRepository.findAllByDate(year1, year2);
    }

    @Override
    public List<Bill> findAllByUserAndStatus(User user, int status) {
        return billRepository.findAllByUserAndStatus(user, status);
    }

    @Override
    public List<Bill> findAllByStatus(int status) {
        return billRepository.findAllByStatus(status);
    }

    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    @Override
    public Slice<Bill> PagingAllBillByStatus(int offset, int pageSize, int status) {
        return billRepository.findAllByStatus(status, PageRequest.of(offset, pageSize).withSort(Sort.by("id").descending()));
    }

    @Override
    public void deleteBill(int id) {
        billRepository.deleteBillById(id);
    }

    @Override
    public Bill getBillById(Integer id) {
        return billRepository.getById(id);
    }


}
