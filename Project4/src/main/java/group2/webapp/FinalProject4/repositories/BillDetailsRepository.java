package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.Bill;
import group2.webapp.FinalProject4.models.BillDetail;
import group2.webapp.FinalProject4.models.Product;
import group2.webapp.FinalProject4.models.keys.BillDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillDetailsRepository extends JpaRepository<BillDetail, BillDetailKey> {
    Optional<BillDetail> findBillDetailsByProductIdAndBillId(Product productId, Bill billId);

    List<BillDetail> findAllByBillId(Bill billId);

    List<BillDetail> findAll();

    void deleteBillDetailByBillIdAndProductId(Bill bill, Product product);
}