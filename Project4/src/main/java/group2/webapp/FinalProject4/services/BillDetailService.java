package group2.webapp.FinalProject4.services;

import group2.webapp.FinalProject4.models.Bill;
import group2.webapp.FinalProject4.models.BillDetail;
import group2.webapp.FinalProject4.models.Product;
import group2.webapp.FinalProject4.models.keys.BillDetailKey;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BillDetailService {
    Optional<BillDetail> findBillDetailsByProductIdAndBillId(Product productId, Bill billId);

    List<BillDetail> findAllByBillId(Bill billId);

    void saveBillDetail(BillDetail billDetail);

    void removeBillDetail(BillDetailKey billDetailKey);

    List<BillDetail> findAll();

    Page<BillDetail> PagingAllBillDetail(int offset, int pageSize);

    void deleteBillDetail(Bill bill, Product product);
}
