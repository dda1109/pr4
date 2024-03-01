package group2.webapp.FinalProject4.controllers;

import group2.webapp.FinalProject4.models.*;
import group2.webapp.FinalProject4.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserCustomerController {

    @Autowired
    UserService userService;

    @Autowired
    CustomerInfoService customerInfoService;

    @Autowired
    UploadFileService uploadFileService;

    @Autowired
    BillService billService;

    @Autowired
    BillDetailService billDetailService;

    @Autowired
    AddressService addressService;

    @Autowired
    CardService cardService;

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String LoginPage(@RequestParam(required = false, value = "username") String username,
                            @RequestParam(required = false, value = "password") String password,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest rq) {
        User user = userService.getUserByUserNameAndPassWord(username, password);

        if (user == null) {
            redirectAttributes.addFlashAttribute("message1", "Sai thông đăng nhập!!!");
            return "redirect:/login";
        } else {
            if (user.getStatus() == 1) {
                redirectAttributes.addFlashAttribute("message1", "Đăng nhập thành công!!!");

                // Retrieve user's cart items from session
                HttpSession session = rq.getSession();
                List<BillDetail> sessionCartItems = (List<BillDetail>) session.getAttribute("cartDetails");

                // Get the bill associated with the current session
                Bill sessionBill = (Bill) session.getAttribute("cart");

                // If the user has a bill in the session
                if (sessionBill != null) {
                    // Check if the user has a bill in the database
                    Optional<Bill> userBillOptional = billService.findByUserAndStatus(user, -1);
                    if (userBillOptional.isPresent()) {
                        // User has a bill in the database, merge the cart items
                        Bill userBill = userBillOptional.get();
                        List<BillDetail> userCartItems = billDetailService.findAllByBillId(userBill);

                        // Add session cart items to user's cart items
                        for (BillDetail sessionCartItem : sessionCartItems) {
                            boolean found = false;
                            for (BillDetail userCartItem : userCartItems) {
                                if (sessionCartItem.getProductId().getId().equals(userCartItem.getProductId().getId())) {
                                    // Product already exists in the user's cart, update quantity
                                    userCartItem.setQuantity(userCartItem.getQuantity() + sessionCartItem.getQuantity());
                                    billDetailService.saveBillDetail(userCartItem);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                // Product doesn't exist in the user's cart, add it
                                sessionCartItem.setBillId(userBill);
                                billDetailService.saveBillDetail(sessionCartItem);
                            }
                        }
                    } else {
                        // User doesn't have a bill in the database, set session bill's user_id
                        sessionBill.setUser(user);
                        billService.saveBill(sessionBill);

                        // Update the user_id for session bill's bill details
                        for (BillDetail sessionCartItem : sessionCartItems) {
                            sessionCartItem.setBillId(sessionBill);
                            billDetailService.saveBillDetail(sessionCartItem);
                        }
                    }
                }

                // Clear session cart after merging
                session.removeAttribute("cartDetails");
                session.removeAttribute("cart");

                CustomerInfo customerInfo = customerInfoService.findByUser(user);
                if (customerInfo == null) {
                    CustomerInfo temp = new CustomerInfo();
                    temp.setFullname(username);
                    customerInfo = temp;
                }

                session.setAttribute("account", user);
                session.setAttribute("info", customerInfo);
                return "redirect:/home";
            } else {
                redirectAttributes.addFlashAttribute("message1", "Tài khoản đã bị khóa!!!");
                return "redirect:/login";
            }
        }
    }




    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String RegisterPage(@RequestParam(value = "fullname") String fullname,
                               @RequestParam(value = "email") String email,
                               @RequestParam(value = "phone") String phone,
                               @RequestParam(value = "username") String username,
                               @RequestParam(value = "password") String password,
                               RedirectAttributes redirectAttributes) {

        User user = userService.getUserByUserName(username);
        if (user != null) {
            redirectAttributes.addFlashAttribute("message2", "Tài khoản đăng nhập đã tồn tại!!!");
            return "redirect:/login";
        } else {
            User userRegister = new User();
            userRegister.setUserName(username);
            userRegister.setPassWord(password);
            userRegister.setRole(1);
            userService.saveUser(userRegister);

            userRegister = userService.getUserByUserName(username);
            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setFullname(fullname);
            customerInfo.setEmail(email);
            customerInfo.setPhone(phone);
            customerInfo.setUser(userRegister);
            customerInfoService.saveInfo(customerInfo);

            redirectAttributes.addFlashAttribute("message2", "Đăng kí tài khoản thành công!!!");
            return "redirect:/login";
        }
    }

    @RequestMapping(value = {"/recover-password"}, method = RequestMethod.POST)
    public String RecoverPasswordPage(@RequestParam(required = false, value = "username") String username,
                                      @RequestParam(required = false, value = "email") String email,
                                      @RequestParam(required = false, value = "newpassword") String newpassword,
                                      @RequestParam(required = false, value = "confirmpassword") String confirmpassword,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        User user = userService.getUserByUserName(username);
        if (user != null) {
            CustomerInfo customerInfo = customerInfoService.findByEmail(email);

            if (customerInfo.getUser().getId() == user.getId()) {
                if (confirmpassword.equals(confirmpassword)) {
                    user.setPassWord(newpassword);
                    userService.saveUser(user);
                    redirectAttributes.addFlashAttribute("message1", "Lấy lại mật khẩu thành công!!!");
                    return "redirect:/login";
                } else {
                    redirectAttributes.addFlashAttribute("message1", "Mật khẩu không trùng khớp!!!");
                    return "redirect:/recover-password";
                }
            } else {
                redirectAttributes.addFlashAttribute("message1", "Email không đúng!!!");
                return "redirect:/recover-password";
            }
        } else {
            redirectAttributes.addFlashAttribute("message1", "Không tìm thấy tài khoản!!!");
            return "redirect:/recover-password";
        }
    }

    @RequestMapping(value = "/logout")
    public String LogoutPage(HttpServletRequest rq) {
        HttpSession session = rq.getSession();
        session.removeAttribute("account");
        session.removeAttribute("info");
        return "web/login";
    }

    @RequestMapping(value = "/profile")
    public String ProfileUserPage(HttpServletRequest rq,
                                  Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");
        if (user != null) {

            Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
            if (billCheck.isPresent()) {
                Bill bill = billCheck.get();

                List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
                if (billDetailList.size() > 0) {
                    int count = 0;
                    for (BillDetail p : billDetailList) {
                        count += p.getQuantity();
                    }
                    model.addAttribute("count", count);
                }
            }

            return "web/profile";
        }

        return "redirect:/login";
    }

    @RequestMapping(value = "/edit-profile", method = RequestMethod.POST)
    public String EditProfile(@RequestParam(required = false, value = "fullname") String fullname,
                              @RequestParam(required = false, value = "phone") String phone,
                              @RequestParam(required = false, value = "email") String email,
                              @RequestParam(required = false, value = "username") String username,
                              @RequestParam(required = false, value = "birthday") Date birthday,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest rq,
                              Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");

        if (user == null)
            return "redirect:/login";

        try {

            User usernameCheck = userService.getUserByUserName(username);
            if (username.isEmpty()) {
                redirectAttributes.addFlashAttribute("message1", "Tên tài khoản không được để trống!!!");
                return "redirect:/profile";
            } else if (usernameCheck != null && user.getUserName().equals(username) == false) {
                redirectAttributes.addFlashAttribute("message1", "Tên tài khoản đã tồn tại!!!");
                return "redirect:/profile";
            }
            user.setUserName(username);
            userService.saveUser(user);
            CustomerInfo customerInfo = (CustomerInfo) session.getAttribute("info");
            customerInfo.setUser(user);
            customerInfo.setFullname(fullname);
            customerInfo.setPhone(phone);
            customerInfo.setEmail(email);
            customerInfo.setBithday(birthday);
            customerInfoService.saveInfo(customerInfo);

            User newUser = userService.getUserByUserName(username);
            CustomerInfo newCustomerInfo = customerInfoService.findByUser(newUser);

            session.removeAttribute("account");
            session.removeAttribute("info");
            session.setAttribute("account", newUser);
            session.setAttribute("info", customerInfo);

            redirectAttributes.addFlashAttribute("message1", "Cập nhập thông tin thành công!!!");
            return "redirect:/profile";
        } catch (Exception e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message1", "Cập nhập thông tin thất bại!!!");

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }

        return "redirect:/profile";
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public String ChangePassword(@RequestParam(required = false, value = "oldpassword") String oldpassword,
                                 @RequestParam(required = false, value = "newpassword") String newpassword,
                                 @RequestParam(required = false, value = "confirmpassword") String confirmpassword,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest rq,
                                 Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");

        if (user == null)
            return "redirect:/login";

        try {

            if (oldpassword.equals(user.getPassWord())) {
                if (newpassword.equals(confirmpassword)) {
                    user.setPassWord(newpassword);
                    userService.saveUser(user);
                    session.removeAttribute("account");
                    session.setAttribute("account", userService.getUserByUserName(user.getUserName()));
                    redirectAttributes.addFlashAttribute("message2", "Đổi mật khẩu thành công!!!");
                    return "redirect:/profile";
                } else {
                    redirectAttributes.addFlashAttribute("message2", "Mật khẩu mới không trùng nhau!!!");
                    return "redirect:/profile";
                }
            } else {
                redirectAttributes.addFlashAttribute("message2", "Mật khẩu cũ không đúng!!!");
                return "redirect:/profile";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message2", "Đổi mật khẩu thất bại!!!");

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }

        return "redirect:/profile";

    }

    @RequestMapping(value = "/change-avt", method = RequestMethod.POST)
    public String ChangeAvatar(@RequestParam("avatar") MultipartFile avatar,
                               HttpServletRequest rq,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        HttpSession session = rq.getSession();

        CustomerInfo customerInfo = (CustomerInfo) session.getAttribute("info");

        User user = (User) session.getAttribute("account");
        if (user == null)
            return "redirect:/login";

        try {
            customerInfo.setLinkAVT(uploadFileService.storeFile(avatar));
            customerInfo.setUser(user);
            customerInfoService.saveInfo(customerInfo);

            session.removeAttribute("info");
            session.setAttribute("info", customerInfoService.findByUser(customerInfo.getUser()));

            redirectAttributes.addFlashAttribute("message3", "Đổi avatar thành công!!!");
            return "redirect:/profile";
        } catch (Exception e) {
            e.printStackTrace();
        }
        redirectAttributes.addFlashAttribute("message3", "Đổi avatar thất bại!!!");

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }
        return "redirect:/profile";
    }

    @RequestMapping(value = "/transaction-history")
    public String TransactionHistoryPage(HttpServletRequest rq,
                                         Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");
        if (user != null) {

            List<Bill> billList = billService.findAllByUserAndStatus(user, 1);
            List<Bill> billListReverse = new ArrayList<>();
            for (int i = billList.size() - 1; i > -1; i--) {
                billListReverse.add(billList.get(i));
            }
            List<BillDetail> billDetailList = billDetailService.findAll();

            model.addAttribute("billList", billListReverse);
            model.addAttribute("billDetailList", billDetailList);

            Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
            if (billCheck.isPresent()) {
                Bill bill = billCheck.get();

                billDetailList = billDetailService.findAllByBillId(bill);
                if (billDetailList.size() > 0) {
                    int count = 0;
                    for (BillDetail p : billDetailList) {
                        count += p.getQuantity();
                    }
                    model.addAttribute("count", count);
                }
            }

            return "web/transaction-history";
        }
        return "redirect:/login";

    }

    @RequestMapping(value = "/address")
    public String AddressPage(HttpServletRequest rq,
                              RedirectAttributes attributes,
                              Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");

        if (user == null)
            return "redirect:/login";

        List<Address> addressList = addressService.findAllByUser(user);
        model.addAttribute("ListAddress", addressList);

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }

        return "web/address";
    }

    @RequestMapping(value = "/delete-address")
    public String DeleteAddress(@RequestParam(required = false, value = "idAddress") Integer idAddress,
                                HttpServletRequest rq,
                                RedirectAttributes attributes,
                                Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");

        if (user == null)
            return "redirect:/login";

        addressService.deleteAddressById(idAddress);
        attributes.addFlashAttribute("message", "Đã xoá thành công !!!");

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }

        return "redirect:/address";
    }

    @RequestMapping(value = "/add-address")
    public String AddAddress(@RequestParam(required = false, value = "addAddress") String addAddress,
                             HttpServletRequest rq,
                             RedirectAttributes attributes,
                             Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");

        if (user == null)
            return "redirect:/login";
        Address address = new Address();
        address.setDescription(addAddress);
        address.setUser(user);
        addressService.saveAddress(address);
        attributes.addFlashAttribute("message", "Đã thêm thành công !!!");

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }

        return "redirect:/address";
    }

    @RequestMapping(value = "/edit-address")
    public String EditAddress(@RequestParam(required = false, value = "editID") Integer editID,
                              @RequestParam(required = false, value = "editAddress") String editAddress,
                              HttpServletRequest rq,
                              RedirectAttributes attributes,
                              Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");

        if (user == null)
            return "redirect:/login";
        Address address = addressService.getById(editID);
        address.setDescription(editAddress);
        addressService.saveAddress(address);
        attributes.addFlashAttribute("message", "Đã sửa thành công !!!");

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }

        return "redirect:/address";
    }

    @RequestMapping(value = "/card")
    public String CardPage(HttpServletRequest rq,
                           RedirectAttributes attributes,
                           Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");


        if (user == null)
            return "redirect:/login";

        List<Card> cardList = cardService.findAllByUser(user);
        model.addAttribute("ListCard", cardList);

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }

        return "web/card";
    }

    @RequestMapping(value = "/delete-card")
    public String DeleteCard(@RequestParam(required = false, name = "deleteID") Integer deleteID,
                             HttpServletRequest rq,
                             RedirectAttributes attributes,
                             Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");


        if (user == null)
            return "redirect:/login";

        cardService.deleteCardById(deleteID);
        attributes.addFlashAttribute("message", "Đã xoá thành công !!!");

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }
        return "redirect:/card";
    }

    @RequestMapping(value = "/add-card")
    public String AddCard(@RequestParam(required = false, name = "addBank") String addBank,
                          @RequestParam(required = false, name = "addNumber") String addNumber,
                          HttpServletRequest rq,
                          RedirectAttributes attributes,
                          Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");


        if (user == null)
            return "redirect:/login";

        Card card = new Card();
        card.setBank(addBank);
        card.setNumber(addNumber);
        card.setUser(user);
        cardService.saveCard(card);
        attributes.addFlashAttribute("message", "Đã thêm thành công !!!");

        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }

        return "redirect:/card";
    }

    @RequestMapping(value = "/edit-card")
    public String EditCard(@RequestParam(required = false, name = "editID") Integer editID,
                           @RequestParam(required = false, name = "editBank") String editBank,
                           @RequestParam(required = false, name = "editNumber") String editNumber,
                           HttpServletRequest rq,
                           RedirectAttributes attributes,
                           Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");


        if (user == null)
            return "redirect:/login";

        Card card = cardService.getById(editID);
        card.setBank(editBank);
        card.setNumber(editNumber);

        cardService.saveCard(card);
        attributes.addFlashAttribute("message", "Đã sửa thành công !!!");


        Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
        if (billCheck.isPresent()) {
            Bill bill = billCheck.get();

            List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
            if (billDetailList.size() > 0) {
                int count = 0;
                for (BillDetail p : billDetailList) {
                    count += p.getQuantity();
                }
                model.addAttribute("count", count);
            }
        }

        return "redirect:/card";
    }

    @RequestMapping(value = "/payment")
    public String PaymentPage(HttpServletRequest rq,
                              RedirectAttributes attributes,
                              Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");

        if (user == null) {
            return "redirect:/login";
        } else {
            Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
            if (billCheck.isPresent()) {
                Bill bill = billCheck.get();
                List<Address> addressList = addressService.findAllByUser(user);
                List<Card> cardList = cardService.findAllByUser(user);

                model.addAttribute("total", bill.getTotal());
                model.addAttribute("addressList", addressList);
                model.addAttribute("cardList", cardList);
            }
        }

        return "web/payment";
    }

    @RequestMapping(value = {"/payment-confirm"})
    public String PaymentConfirmPage(@RequestParam(required = false, name = "address") String address,
                                     @RequestParam(required = false, name = "card") String card,
                                     @RequestParam(required = false, name = "email") String email,
                                     HttpServletRequest rq,
                                     RedirectAttributes attributes,
                                     Model model) {
        HttpSession session = rq.getSession();

        User user = (User) session.getAttribute("account");

        if (user == null) {
            return "redirect:/login";
        } else {
            Optional<Bill> billCheck = billService.findByUserAndStatus(user, -1);
            if (billCheck.isPresent()) {
                Bill bill = billCheck.get();
                Date date = Date.valueOf(LocalDate.now());
                bill.setDate(date);
                bill.setStatus(1);
                List<BillDetail> listBillDetail = billDetailService.findAllByBillId(bill);
                for (BillDetail bd : listBillDetail) {
                    Product product = bd.getProductId();
                    int newAmount = product.getAmount() + bd.getQuantity();
                    if (newAmount > product.getQuantity()) {
                        attributes.addFlashAttribute("message", "Thanh toán thất bại !!!");
                        return "redirect:/cart";
                    }

                    product.setAmount(newAmount);
                    productService.saveProduct(product);
                }
                billService.saveBill(bill);
                attributes.addFlashAttribute("message", "Thanh toán thành công !!!");

                CustomerInfo customerInfo = (CustomerInfo) session.getAttribute("info");
                if (customerInfo != null) {
                    if (!email.isEmpty()) {
                        customerInfo.setEmail(email);
                        customerInfo.setUser(user);
                    }
                } else {
                    customerInfo = new CustomerInfo();
                    customerInfo.setEmail(email);
                    customerInfo.setUser(user);
                    customerInfoService.saveInfo(customerInfo);
                }

                session.removeAttribute("info");
                session.setAttribute("info", customerInfo);

                if (address != null) {
                    Address newAddress = new Address();
                    newAddress.setDescription(address);
                    newAddress.setUser(user);
                    addressService.saveAddress(newAddress);
                }

                if (card != null) {
                    Card newCard = new Card();
                    String[] paraCard = card.split(" ");
                    newCard.setBank(paraCard[0]);
                    newCard.setNumber(paraCard[1]);
                    newCard.setUser(user);
                    cardService.saveCard(newCard);
                }

                return "redirect:/cart";
            }

            attributes.addFlashAttribute("message", "Vui lòng đặt hàng trước khi thanh toán !!!");
            return "redirect:/cart";
        }
    }
}
