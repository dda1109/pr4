package group2.webapp.FinalProject4.models.keys;

import java.io.Serializable;

public class BillDetailKey implements Serializable {
    private Integer productId;
    private Integer billId;

    public BillDetailKey() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }
}
