package group2.webapp.FinalProject4.controllers;

import group2.webapp.FinalProject4.models.Bill;
import group2.webapp.FinalProject4.models.CustomerInfo;
import group2.webapp.FinalProject4.models.Product;
import group2.webapp.FinalProject4.models.User;
import group2.webapp.FinalProject4.services.BillService;
import group2.webapp.FinalProject4.services.CustomerInfoService;
import group2.webapp.FinalProject4.services.ProductService;
import group2.webapp.FinalProject4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/seller")
public class SellerController {

    @Autowired
    UserService userService;

    @Autowired
    CustomerInfoService customerInfoService;

    @Autowired
    ProductService productService;

    @Autowired
    BillService Cservice;

    @Autowired
    BillService billService;

    @RequestMapping(value = {"/", "/home", "/index"})
    public String HomePage(HttpServletRequest rq, Model model) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                List<Product> listAllProduct = productService.findAll();
                List<Bill> listAllBill = billService.findAllByStatus(1);
                double Total = 0;
                for (int i = 0; i < listAllBill.size(); i++) {
                    Total = Total + listAllBill.get(i).getTotal();
                }

                Product best = listAllProduct.get(0);

                for (int i = 1; i < listAllProduct.size(); i++) {
                    if (listAllProduct.get(i).getAmount() > best.getAmount())
                        best = listAllProduct.get(i);
                }
                model.addAttribute("CountProduct", listAllProduct.size());
                model.addAttribute("CountInvoice", listAllBill.size());
                model.addAttribute("Total", Total);
                model.addAttribute("BestProduct", best);
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }

        } else {
            return "redirect:/seller/login";
        }

        return "seller/home";
    }

    @RequestMapping(value = {"/login"})
    public String LoginPage() {
        return "seller/login";
    }

    @RequestMapping(value = {"/register"})
    public String Register() {
        return "seller/register";
    }

    @RequestMapping(value = {"/profile"})
    public String Profile(HttpServletRequest rq, Model model) {
        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (session != null && session.getAttribute("account") != null) {
            return "seller/profile";
        } else {
            return "redirect:/seller/login";
        }


    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public String LoginPage(@RequestParam(required = false, value = "username") String username,
                            @RequestParam(required = false, value = "password") String password,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest rq) {
        User user = userService.getUserByUserNameAndPassWord(username, password);

        if (user == null || user.getRole() != 2) {
            redirectAttributes.addFlashAttribute("message", "Sai thông đăng nhập!!!");
            return "redirect:/seller/login";
        } else {
            redirectAttributes.addFlashAttribute("message", "Đăng nhập thành công!!!");
            CustomerInfo customerInfo = customerInfoService.findByUser(user);
            if (customerInfo == null) {
                CustomerInfo temp = new CustomerInfo();
                temp.setFullname(username);
                customerInfo = temp;
            }
            HttpSession session = rq.getSession();

            session.setAttribute("account", user);
            session.setAttribute("info", customerInfo);
            return "redirect:/seller/home";


        }
    }

    @RequestMapping(value = {"/logout"})
    public String LogoutPage(HttpServletRequest rq) {
        HttpSession session = rq.getSession();
        session.removeAttribute("account");
        session.removeAttribute("info");
        return "seller/login";
    }

    @RequestMapping(value = {"/update-seller"}, method = RequestMethod.POST)
    public String UpdateSeller(@RequestParam(required = false, value = "editID") int ID,
                               @RequestParam(required = false, value = "editFullName") String fullname,
                               @RequestParam(required = false, value = "editUserName") String username,
                               @RequestParam(required = false, value = "editStt") int status,
                               HttpServletRequest rq, RedirectAttributes redirectAttributes) {


        User user = userService.getUserById(ID);
        CustomerInfo info = customerInfoService.findCustomerInfoByUserID(ID);

        int check = 0;

        if (userService.checkEdiUsername(username, ID) == false) {
            user.setUserName(username);
            user.setStatus(status);
            userService.saveUser(user);
            check = 1;
            //System.out.println("Day la" + String.valueOf(userService.checkEdiUsername(username, ID) == false));
        }
        if (fullname != info.getFullname()) {
            info.setFullname(fullname);
            customerInfoService.saveInfo(info);
            check = 1;
        }

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

        return "redirect:/seller/profile";
    }

    @RequestMapping(value = {"/changepassword"}, method = RequestMethod.POST)
    public String UpdateSeller(@RequestParam(required = false, value = "idUser") int id,
                               @RequestParam(required = false, value = "password") String password,
                               @RequestParam(required = false, value = "oldPW") String oldPW,
                               @RequestParam(required = false, value = "newPW") String newPW,
                               @RequestParam(required = false, value = "confirmPW") String confirmPW,
                               HttpServletRequest rq, RedirectAttributes redirectAttributes) {


        if (password.compareTo(oldPW) == 0) {
            if (newPW.compareTo(confirmPW) == 0) {
                User user = userService.getUserById(id);
                user.setPassWord(newPW);
                userService.saveUser(user);
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
        return "redirect:/seller/profile";
    }

}
