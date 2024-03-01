package group2.webapp.FinalProject4.models;

import group2.webapp.FinalProject4.models.keys.BillDetailKey;

import javax.persistence.*;

@Entity
@Table(name = "BillDetail")
@IdClass(BillDetailKey.class)
public class BillDetail {

    private int quantity;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product productId;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill billId;

    public BillDetail() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Bill getBillId() {
        return billId;
    }

    public void setBillId(Bill billId) {
        this.billId = billId;
    }
}
