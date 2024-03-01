package group2.webapp.FinalProject4.repositories;

import group2.webapp.FinalProject4.models.BillDetail;
import group2.webapp.FinalProject4.models.keys.BillDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDetailRepository extends JpaRepository<BillDetail, BillDetailKey> {
}
