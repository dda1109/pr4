package group2.webapp.FinalProject4.controllers;

import group2.webapp.FinalProject4.models.*;
import group2.webapp.FinalProject4.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    BillService billService;
    @Autowired
    ProductService productService;

    @Autowired
    BillDetailService billDetailService;
    @Autowired
    CustomerInfoService customerInfoService;

    @RequestMapping(value = {"/", "/home", "/index"})
    public String HomePage(HttpServletRequest rq, Model model) {


        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null)
            return "redirect:/admin/login";

        if (user.getRole() != 3) {
            session.removeAttribute("account");
            return "redirect:/admin/login";
        }

        List<Bill> billList = billService.findAllByStatus(1);
        List<BillDetail> billDetailList = billDetailService.findAll();
        List<CustomerInfo> customerInfoList = customerInfoService.findAll();
        Date date = Date.valueOf(LocalDate.now());
        String dateStart = String.valueOf(LocalDate.now().getYear() + "/" + LocalDate.now().getMonthValue() + "/1");
        List<Bill> billList1 = billService.findAllByDate(dateStart, date.toString());
        List<Product> productList = productService.getTop4ProductBestSeller();
        double total = 0;
        for (int i = 0; i < billList.size(); i++) {
            total += billList.get(i).getTotal();
        }

        double total1 = 0;
        for (int j = 0; j < billList1.size(); j++) {
            if (billList1.get(j).getStatus() == 1) {
                total1 += billList1.get(j).getTotal();
            }
        }

        int totalcustomer = customerInfoList.size();
        int totalbill = billList.size();

        int totalproduct = 0;
        for (BillDetail billDetail : billDetailList) {
            if (billDetail.getBillId().getStatus() == 1) {
                totalproduct += billDetail.getQuantity();
            }
        }
        model.addAttribute("Total", total);
        model.addAttribute("Total1", total1);
        model.addAttribute("Totalbill", totalbill);
        model.addAttribute("totalproduct", totalproduct);
        model.addAttribute("totalcustomer", totalcustomer);
        model.addAttribute("customerInfoList", customerInfoList);
        model.addAttribute("productList", productList);
        return "admin/home";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public String LoginPage(@RequestParam(required = false, value = "username") String username,
                            @RequestParam(required = false, value = "password") String password,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest rq) {
        User user = userService.getUserByUserNameAndPassWordAndRole(username, password, 3);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message1", "Sai thông đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            redirectAttributes.addFlashAttribute("message1", "Đăng nhập thành công!!!");
            CustomerInfo customerInfo = customerInfoService.findByUser(user);
            if (customerInfo == null) {
                CustomerInfo temp = new CustomerInfo();
                temp.setFullname(username);
                customerInfo = temp;
            }
            HttpSession session = rq.getSession();

            session.setAttribute("account", user);
            session.setAttribute("info", customerInfo);
            return "redirect:/admin/home";
        }
    }

    @RequestMapping(value = "/logout")
    public String LogoutPage(HttpServletRequest rq) {
        HttpSession session = rq.getSession();
        session.removeAttribute("account");
        session.removeAttribute("info");
        return "admin/login";
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("fullname") String fullname,
                               RedirectAttributes redirectAttributes) {
        // Kiểm tra xem tên đăng nhập đã tồn tại chưa
        User existingUser = userService.getUserByUserNameAndRole(username, 3);
        if (existingUser != null) {
            redirectAttributes.addFlashAttribute("message", "Tên đăng nhập đã tồn tại!");
            return "redirect:/admin/register";
        }

        User newUser = new User();
        newUser.setUserName(username);
        newUser.setPassWord(password);
        newUser.setRole(3);

        userService.saveUser(newUser);

        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setFullname(fullname);
        customerInfo.setUser(newUser);

        customerInfoService.saveInfo(customerInfo);

        redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!");
        return "redirect:/admin/login";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String showProfile(HttpServletRequest rq, Model model) {
        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null)
            return "redirect:/admin/login";
        if (user.getRole() != 3) {
            session.removeAttribute("account");
            return "redirect:/admin/login";
        }

        CustomerInfo customerInfo = customerInfoService.findByUser(user);
        if (customerInfo == null) {

            // return "errorPage";
        }

        model.addAttribute("customerInfo", customerInfo);
        return "admin/profile";
    }

    // Xử lý cập nhật thông tin profile
    //Định nghĩa một phương thức xử lý yêu cầu POST đến URL /update-admin.
    @RequestMapping(value = {"/update-admin"}, method = RequestMethod.POST)
    //Phương thức này có tham số ID nhận giá trị từ request parameter editID.
    public String UpdateAdmin(@RequestParam(required = false, value = "editID") int ID,
                              @RequestParam(required = false, value = "editFullName") String fullname,
                              @RequestParam(required = false, value = "editUserName") String username,
                              @RequestParam(required = false, value = "editStt") int status,
                              HttpServletRequest rq, RedirectAttributes redirectAttributes) {
        //Các tham số rq và redirectAttributes giúp điều hướng và chuyển tiếp thông tin giữa các trang.

        User user = userService.getUserById(ID);
        CustomerInfo info = customerInfoService.findCustomerInfoByUserID(ID);

        int check = 0;
        //Kiểm tra xem tên người dùng mới đã được sử dụng bởi người dùng khác hay chưa.
        // Nếu chưa, cập nhật thông tin người dùng và đặt check thành 1.
        if (userService.checkEdiUsername(username, ID) == false) {
            user.setUserName(username);
            user.setStatus(status);
            userService.saveUser(user);
            check = 1;

        }

        //Kiểm tra xem tên đầy đủ mới có khác với tên đầy đủ hiện tại của CustomerInfo.
        // Nếu có, cập nhật thông tin và đặt check thành 1.
        if (fullname != info.getFullname()) {
            info.setFullname(fullname);
            customerInfoService.saveInfo(info);
            check = 1;
        }

        //Dựa vào giá trị `check`, xử lý việc cập nhật thông tin người dùng
        // và thông tin `CustomerInfo` và cập nhật session tương ứng.
        if (check == 1) {
            HttpSession session = rq.getSession();
            session.removeAttribute("account");
            session.setAttribute("account", user);
            session.removeAttribute("info");
            session.setAttribute("info", info);
            redirectAttributes.addFlashAttribute("check", 1);
        } else {
            redirectAttributes.addFlashAttribute("check", 0);
        }
        System.out.println("Day la" + check);

        return "redirect:/admin/profile";
    }

    @RequestMapping(value = {"/changepassword"}, method = RequestMethod.POST)
    public String UpdateAdmin(@RequestParam(required = false, value = "idUser") int id,
                              @RequestParam(required = false, value = "password") String password,
                              @RequestParam(required = false, value = "oldPW") String oldPW,
                              @RequestParam(required = false, value = "newPW") String newPW,
                              @RequestParam(required = false, value = "confirmPW") String confirmPW,
                              HttpServletRequest rq, RedirectAttributes redirectAttributes) {


        //So sánh mật khẩu hiện tại (oldPW) với mật khẩu được nhập (password).
        // Nếu trùng, tiếp tục kiểm tra mật khẩu mới và mật khẩu xác nhận.
        if (password.compareTo(oldPW) == 0) {
            if (newPW.compareTo(confirmPW) == 0) {
                //Lấy thông tin người dùng từ cơ sở dữ liệu dựa trên id
                // và cập nhật mật khẩu mới cho người dùng.
                User user = userService.getUserById(id);
                user.setPassWord(newPW);
                userService.saveUser(user);
                //Cập nhật session của người dùng và đặt một thuộc tính flash
                // để thông báo rằng mật khẩu đã được thay đổi thành công.
                HttpSession session = rq.getSession();
                session.removeAttribute("account");
                session.setAttribute("account", user);
                redirectAttributes.addFlashAttribute("check", 1);


            } else {
                redirectAttributes.addFlashAttribute("check", 0);
            }

        } else {
            redirectAttributes.addFlashAttribute("check", 0);
        }
        return "redirect:/admin/profile";
    }

    @RequestMapping(value = {"/revenue-statistics"})
    public String RevenueStatisticsPage() {
        return "admin/revenue-statistics";
    }
}
