package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.Bill;
import group2.webapp.FinalProject4.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    Optional<Bill> findByUserAndStatus(User user, Integer status);

    List<Bill> findAll();

    @Query(nativeQuery = true, value = "SELECT * FROM bill WHERE bill.date <= :year2 AND bill.date >= :year1")
    List<Bill> findAllByDate(@Param("year1") String year1, @Param("year2") String year2);

    List<Bill> findAllByUserAndStatus(User user, int status);

    List<Bill> findAllByStatus(int status);

    void deleteBillById(int id);

    Slice<Bill> findAllByStatus(int status, Pageable pageable);
}
