package group2.webapp.FinalProject4.controllers;

import group2.webapp.FinalProject4.models.Bill;
import group2.webapp.FinalProject4.models.BillDetail;
import group2.webapp.FinalProject4.models.Product;
import group2.webapp.FinalProject4.models.User;
import group2.webapp.FinalProject4.models.keys.BillDetailKey;
import group2.webapp.FinalProject4.services.BillDetailService;
import group2.webapp.FinalProject4.services.BillService;
import group2.webapp.FinalProject4.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CartController {

    @Autowired
    ProductService productService;

    @Autowired
    BillService billService;

    @Autowired
    BillDetailService billDetailService;

    @RequestMapping(value = "/add-to-cart/{product}/{quantity}")
    public ResponseEntity<String> AddToCart(@PathVariable Integer product,
                                            @PathVariable Integer quantity,
                                            HttpServletRequest rq) {
        HttpSession session = rq.getSession();
        Product productModel = productService.getProductById(product);
        User user = (User) session.getAttribute("account");

        Bill bill = null;
        List<BillDetail> billDetails = null;

        if (user == null) {
            // Handle cart for non-logged-in users
            bill = (Bill) session.getAttribute("cart");
            if (bill == null) {
                // If cart doesn't exist in session, create a new one
                bill = new Bill();
                bill.setStatus(-1);
                Date date = Date.valueOf(LocalDate.now());
                bill.setDate(date);
                billService.saveBill(bill);
                session.setAttribute("cart", bill);
            }
            // Get cart details from session
            billDetails = (List<BillDetail>) session.getAttribute("cartDetails");
            if (billDetails == null) {
                billDetails = new ArrayList<>();
                session.setAttribute("cartDetails", billDetails);
            }
        } else {
            // Handle cart for logged-in users
            Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
            if (billCheck.isPresent()) {
                bill = billCheck.get();
            } else {
                // Create a new bill for the user if it doesn't exist
                bill = new Bill();
                bill.setStatus(-1);
                Date date = Date.valueOf(LocalDate.now());
                bill.setDate(date);
                bill.setUser(user);
                billService.saveBill(bill);
            }
            // Get cart details from database
            billDetails = billDetailService.findAllByBillId(bill);
        }

        // Check if the product already exists in the cart
        boolean productExists = false;
        for (BillDetail detail : billDetails) {
            if (detail.getProductId().getId().equals(productModel.getId())) {
                int totalQuantity = detail.getQuantity() + quantity;
                if (totalQuantity >= productModel.getQuantity()) {
                    // If the total quantity exceeds available quantity, return 400 Bad Request status
                    return new ResponseEntity<>("Sản phẩm không đủ số lượng!", HttpStatus.BAD_REQUEST);
                }
                // Update the quantity in bill details
                detail.setQuantity(totalQuantity);
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            // If the product doesn't exist, create a new bill detail
            BillDetail newBillDetail = new BillDetail();
            newBillDetail.setBillId(bill);
            newBillDetail.setProductId(productModel);
            newBillDetail.setQuantity(quantity);
            billDetails.add(newBillDetail);
        }

        // Update total of the bill
        double total = calculateTotal(billDetails);
        bill.setTotal(total);
        billService.saveBill(bill);

        // Calculate the updated cart count
        int cartCount = calculateCartCount(session);

        // Return the success message along with the updated cart count and 200 OK status
        return new ResponseEntity<>("Thêm thành công" + "-" + cartCount, HttpStatus.OK);
    }

    // Calculate total based on bill details
    private double calculateTotal(List<BillDetail> billDetails) {
        double total = 0;
        for (BillDetail billDetail : billDetails) {
            // Get the price of the product and multiply by the updated quantity
            double price = billDetail.getProductId().getPrice();
            total += price * billDetail.getQuantity();
        }
        return total;
    }




    @RequestMapping(value = "/update-cart/{productID}/{quantity}")
    public String UpdateCartItem(@PathVariable Integer productID,
                                 @PathVariable Integer quantity,
                                 HttpServletRequest rq) {

        Product product = productService.getProductById(productID);
        if (product == null) {
            return "Sản phẩm không tồn tại!";
        }

        // Check if the entered quantity exceeds the available quantity
        if (quantity > product.getQuantity()) {
            return "Số lượng không đủ!";
        }
        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        Bill bill = (Bill) session.getAttribute("cart");

        // If user is not logged in, update the quantity in session cart directly
        if (user == null) {
            List<BillDetail> cartDetails = (List<BillDetail>) session.getAttribute("cartDetails");
            for (BillDetail item : cartDetails) {
                if (item.getProductId().getId().equals(productID)) {
                    item.setQuantity(quantity);
                    break;
                }
            }
            // Recalculate total based on updated quantities
            double total = calculateTotal(cartDetails);
            bill.setTotal(total);
            // Save the updated cart and cart details in the session
            session.setAttribute("cart", bill);
            session.setAttribute("cartDetails", cartDetails);
            return "Cập nhật giỏ hàng thành công!";
        } else {
            // Handle the cart in the database if the user is logged in
            Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
            if (billCheck.isPresent()) {
                bill = billCheck.get();
                List<BillDetail> billDetails = billDetailService.findAllByBillId(bill);
                for (BillDetail billDetail : billDetails) {
                    if (billDetail.getProductId().getId().equals(productID)) {
                        // Update the quantity of the matching item
                        billDetail.setQuantity(quantity);
                        break;
                    }
                }
                // Recalculate the total based on the updated quantities
                double total = calculateTotal(billDetails);
                // Update the total of the bill
                bill.setTotal(total);
                // Save the updated bill
                billService.saveBill(bill);
                return "Cập nhật giỏ hàng thành công!";
            } else {
                return "Giỏ hàng của bạn trống!";
            }
        }
    }

// Method to calculate the total quantity of products in the cart
    private int calculateCartCount(HttpSession session) {
        User user = (User) session.getAttribute("account");
        int totalQuantity = 0;
        if (user == null) {
            // For non-logged-in users, get cart details from session
            List<BillDetail> cartItems = (List<BillDetail>) session.getAttribute("cartDetails");
            if (cartItems != null) {
                // Sum up the quantities of all products in the session cart
                for (BillDetail cartItem : cartItems) {
                    totalQuantity += cartItem.getQuantity();
                }
            }
        } else {
            // For logged-in users, get cart details from the database
            Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
            if (billCheck.isPresent()) {
                Bill bill = billCheck.get();
                List<BillDetail> cartItems = billDetailService.findAllByBillId(bill);
                if (cartItems != null) {
                    // Sum up the quantities of all products in the database cart
                    for (BillDetail cartItem : cartItems) {
                        totalQuantity += cartItem.getQuantity();
                    }
                }
            }
        }
        return totalQuantity;
    }






    @RequestMapping(value = "/remove-cart/{productID}/{subtotal}")
    public String RemoveCartItem(@PathVariable Integer productID,
                                 @PathVariable double subtotal,
                                 HttpServletRequest rq) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            // Handle the cart in session if the user is not logged in
            Bill bill = (Bill) session.getAttribute("cart");
            if (bill == null) {
                return "Vui lòng thêm sản phẩm vào giỏ hàng trước!!!";
            } else {
                // Remove the item from the session cart
                List<BillDetail> billDetails = (List<BillDetail>) session.getAttribute("cartDetails");
                if (billDetails != null) {
                    for (BillDetail billDetail : billDetails) {
                        if (billDetail.getProductId().getId().equals(productID)) {
                            // Remove the matching item from the session cart
                            billDetails.remove(billDetail);
                            break;
                        }
                    }
                    // Recalculate total based on remaining items
                    double total = calculateTotal(billDetails);
                    bill.setTotal(total);
                    // Update session attributes
                    session.setAttribute("cart", bill);
                    session.setAttribute("cartDetails", billDetails);
                    return "Sản phẩm đã được xóa khỏi giỏ hàng!";
                } else {
                    return "Giỏ hàng của bạn trống!";
                }
            }
        } else {
            // Handle the cart in the database if the user is logged in
            Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
            if (billCheck.isPresent()) {
                Bill bill = billCheck.get();
                BillDetailKey billDetailKey = new BillDetailKey();
                billDetailKey.setBillId(bill.getId());
                billDetailKey.setProductId(productID);
                billDetailService.removeBillDetail(billDetailKey);

                double newTotal = bill.getTotal() - subtotal;
                bill.setTotal(newTotal);
                billService.saveBill(bill);
                return "Sản phẩm đã được xóa khỏi giỏ hàng!";
            } else {
                return "Giỏ hàng của bạn trống!";
            }
        }
    }



}
