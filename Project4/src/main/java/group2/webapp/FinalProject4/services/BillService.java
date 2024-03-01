package group2.webapp.FinalProject4.services;

import group2.webapp.FinalProject4.models.Bill;
import group2.webapp.FinalProject4.models.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BillService {
    Optional<Bill> findByUserAndStatus(User user, Integer status);

    int saveBill(Bill bill);

    List<Bill> findAll();

    List<Bill> findAllByDate(@Param("year1") String year1, @Param("year2") String year2);

    List<Bill> findAllByUserAndStatus(User user, int status);

    List<Bill> findAllByStatus(int status);

    Slice<Bill> PagingAllBillByStatus(int offset, int pageSize, int role);

    void deleteBill(int id);

    Bill getBillById(Integer id);
}
