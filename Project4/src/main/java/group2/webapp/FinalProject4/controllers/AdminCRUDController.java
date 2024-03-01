package group2.webapp.FinalProject4.controllers;

import group2.webapp.FinalProject4.models.Address;
import group2.webapp.FinalProject4.models.Card;
import group2.webapp.FinalProject4.models.CustomerInfo;
import group2.webapp.FinalProject4.models.User;
import group2.webapp.FinalProject4.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminCRUDController {

    private static final int PAGE_SIZE = 5;
    @Autowired
    UserService userService;
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    AddressService addressService;
    @Autowired
    CardService cardService;
    @Autowired
    UploadFileService uploadFileService;

    @RequestMapping(value = "/list-user/{page}")
    public String ListUserPage(@PathVariable Integer page, //Biến này lấy giá trị từ đoạn URL có tên là "page". Nó đại diện cho số trang cần hiển thị.
                               HttpServletRequest rq,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //Gọi phương thức pagingUser của userService để lấy danh sách người dùng
                Page<User> userList = userService.pagingUser(page - 1, PAGE_SIZE);
                int amount = userService.findAll().size();

                //Lấy tổng số lượng người dùng trong hệ thống
                int endPage = amount / PAGE_SIZE;

                if (amount % PAGE_SIZE != 0) {
                    endPage += 1;
                }

                if (page == null) {
                    page = 1;
                }

                model.addAttribute("userList", userList);
                model.addAttribute("tag", page);
                model.addAttribute("endPage", endPage);

                return "admin/list-user";
            }
        }
    }

    @RequestMapping(value = "/edit-user")
    public String saveUser(@RequestParam(required = false, name = "editID") Integer id,
                           @RequestParam(required = false, name = "editUserName") String username,
                           @RequestParam(required = false, name = "editPW") String password,
                           @RequestParam(required = false, name = "editStt") Integer status,
                           HttpServletRequest rq,
                           RedirectAttributes redirectAttributes) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                User newUser = userService.getUserById(id);
                newUser.setUserName(username);
                newUser.setStatus(status);
                newUser.setPassWord(password);
                userService.saveUser(newUser);
                redirectAttributes.addFlashAttribute("message", "Đã sửa thành công!!!");
                return "redirect:/admin/list-user/1";
            }
        }
    }

    @RequestMapping(value = "/delete-user")
    public String deleteUser(@RequestParam(required = false, name = "deleteID") Integer id,
                             @RequestParam(required = false, name = "resultDelete") String listID,
                             HttpServletRequest rq,
                             RedirectAttributes redirectAttributes) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                // cung cấp một id cụ thể, người dùng với id đó sẽ được xóa.
                // Nếu không cung cấp id mà thay vào đó cung cấp một danh sách listID,
                // thì tất cả các người dùng trong danh sách đó sẽ được xóa.
                if (id != 0) {
                    userService.deleteUserById(id);
                } else {
                    String[] splitListId = listID.split(",");

                    for (String x : splitListId) {
                        userService.deleteUserById(Integer.parseInt(x));
                    }
                }
                redirectAttributes.addFlashAttribute("message", "Đã xoá thành công!!!");
                return "redirect:/admin/list-user/1";
            }
        }
    }

    @RequestMapping(value = "/add-user")
    public String insertUser(@RequestParam(required = false, name = "addUserName") String username,
                             @RequestParam(required = false, name = "addPW") String password,
                             @RequestParam(required = false, name = "addStt") Integer status,
                             HttpServletRequest rq,
                             RedirectAttributes redirectAttributes) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                User newUser = userService.getUserByUserName(username);
                if (newUser == null) {
                    newUser = new User();
                    newUser.setUserName(username);
                    newUser.setPassWord(password);
                    newUser.setRole(1);
                    newUser.setStatus(status);
                    userService.saveUser(newUser);
                    redirectAttributes.addFlashAttribute("message", "Đã thêm thành công!!!");
                } else {
                    redirectAttributes.addFlashAttribute("messageError", "Đã tồn tại tên tài khoản!!!");

                }
                return "redirect:/admin/list-user/1";
            }
        }
    }

    //----------------
    @RequestMapping(value = "/list-customerinfo/{page}")
    public String ListCustomerInfoPage(@PathVariable Integer page, HttpServletRequest rq,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                Page<CustomerInfo> customerInfoList = customerInfoService.pagingCustomerInfo(page - 1, PAGE_SIZE);

                int amount = customerInfoService.findAll().size();

                int endPage = amount / PAGE_SIZE;

                if (amount % PAGE_SIZE != 0) {
                    endPage += 1;
                }

                if (page == null) {
                    page = 1;
                }

                // đoạn mã loại bỏ tất cả các User trong userList mà có một liên kết với CustomerInfo.
                List<User> userList = userService.findAllByRoleAndStatus(1, 1);
                List<CustomerInfo> customerInfoListTemp = customerInfoService.findAll();
                boolean flag = true;
                //Bắt đầu vòng lặp ngoài với biến i duyệt qua từng User trong userList.
                //vòng lặp trong với biến j duyệt qua từng CustomerInfo trong customerInfoListTemp.
                for (int i = 0; i < userList.size(); ) {
                    for (int j = 0; j < customerInfoListTemp.size(); j++) {
                        //Kiểm tra xem User tại vị trí i trong userList có cùng id
                        // với User liên kết với CustomerInfo tại vị trí j trong customerInfoListTemp hay không.
                        // Nếu có, User tại vị trí i sẽ được loại bỏ khỏi userList.
                        // Sau khi xóa một User, cờ flag = false để không tăng biến i trong vòng lặp ngoài
                        if (userList.get(i).getId() == customerInfoListTemp.get(j).getUser().getId()) {
                            userList.remove(i);
                            flag = false;
                            break;
                        }
                    }
                    //Nếu không có User nào được loại bỏ, biến i sẽ được tăng lên.
                    if (flag) {
                        i++;
                    } else {
                        flag = true;
                    }
                }

                model.addAttribute("tag", page);
                model.addAttribute("endPage", endPage);
                model.addAttribute("customerInfoList", customerInfoList);
                model.addAttribute("listUser", userList);
                return "admin/list-customerinfo";
            }
        }
    }

    @RequestMapping(value = "/add-customerinfo")
    public String AddCustomerInfo(@RequestParam(required = false, name = "addUserID") Integer addUserID,
                                  @RequestParam(required = false, name = "addFullName") String addFullName,
                                  @RequestParam(required = false, name = "addBirthday") Date addBirthday,
                                  @RequestParam(required = false, name = "addEmail") String addEmail,
                                  @RequestParam(required = false, name = "addPhone") String addPhone,
                                  @RequestParam(required = false, name = "addAVT") MultipartFile addAVT,
                                  HttpServletRequest rq,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                CustomerInfo customerInfo = new CustomerInfo();
                customerInfo.setUser(userService.getUserById(addUserID));
                customerInfo.setFullname(addFullName);
                customerInfo.setBithday(addBirthday);
                customerInfo.setPhone(addPhone);
                customerInfo.setEmail(addEmail);

                if (addAVT != null) {
                    customerInfo.setLinkAVT(uploadFileService.storeFile(addAVT));
                }

                customerInfoService.saveInfo(customerInfo);
                redirectAttributes.addFlashAttribute("message", "Đã thêm thành công!!!");
                return "redirect:/admin/list-customerinfo/1";
            }
        }
    }

    @RequestMapping(value = "/edit-customerinfo")
    public String EditCustomerInfo(@RequestParam(required = false, name = "editID") Integer editID,
                                   @RequestParam(required = false, name = "editFullName") String editFullName,
                                   @RequestParam(required = false, name = "editBirthday") Date editBirthday,
                                   @RequestParam(required = false, name = "editEmail") String editEmail,
                                   @RequestParam(required = false, name = "editPhone") String editPhone,
                                   @RequestParam(required = false, name = "editAVT") MultipartFile editAVT,
                                   HttpServletRequest rq,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                System.out.println(editID);
                CustomerInfo customerInfo = customerInfoService.getByID(editID);
                customerInfo.setFullname(editFullName);
                customerInfo.setBithday(editBirthday);
                customerInfo.setPhone(editPhone);
                customerInfo.setEmail(editEmail);

                if (editAVT != null) {
                    customerInfo.setLinkAVT(uploadFileService.storeFile(editAVT));
                }

                customerInfoService.saveInfo(customerInfo);
                redirectAttributes.addFlashAttribute("message", "Đã sửa thành công!!!");
                return "redirect:/admin/list-customerinfo/1";
            }
        }
    }

    @RequestMapping(value = "/delete-customerinfo")
    public String deleteCustomerInfo(@RequestParam(required = false, name = "deleteCustomerInfo") Integer deleteID,
                                     @RequestParam(required = false, name = "resultCustomerInfo") String listID,
                                     HttpServletRequest rq,
                                     RedirectAttributes redirectAttributes) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                if (deleteID != 0) {
                    customerInfoService.deletInfoById(deleteID);
                } else {
                    String[] listIDInt = listID.split(",");
                    for (String idTemp : listIDInt) {
                        customerInfoService.deletInfoById(Integer.parseInt(idTemp));
                    }
                }
                redirectAttributes.addFlashAttribute("message", "Đã xoá thành công!!!");
                return "redirect:/admin/list-customerinfo/1";
            }
        }
    }


    @RequestMapping(value = "/list-card/{page}")
    public String ListCardPage(@PathVariable Integer page,
                               HttpServletRequest rq,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                Page<Card> cardList = cardService.pagingCard(page - 1, PAGE_SIZE);

                int amount = cardService.findAll().size();

                int endPage = amount / PAGE_SIZE;

                if (amount % PAGE_SIZE != 0) {
                    endPage += 1;
                }

                if (page == null) {
                    page = 1;
                }

                List<User> userList = userService.findAll();

                model.addAttribute("cardList", cardList);
                model.addAttribute("tag", page);
                model.addAttribute("endPage", endPage);
                model.addAttribute("listUser", userList);
                return "admin/list-card";
            }
        }
    }


    @RequestMapping(value = "/add-card")
    public String AddCard(@RequestParam(required = false, name = "addUserID") Integer addUserID,
                          @RequestParam(required = false, name = "addBank") String addBank,
                          @RequestParam(required = false, name = "addNumber") String addNumber,
                          HttpServletRequest rq,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                Card card = new Card();
                card.setUser(userService.getUserById(addUserID));
                card.setBank(addBank);
                card.setNumber(addNumber);
                cardService.saveCard(card);
                redirectAttributes.addFlashAttribute("message", "Đã thêm thành công!!!");
                return "redirect:list-card/1";
            }
        }
    }


    @RequestMapping(value = "/edit-card")
    public String EditCard(@RequestParam(required = false, name = "editID") Integer editID,
                           @RequestParam(required = false, name = "editBank") String editBank,
                           @RequestParam(required = false, name = "editNumber") String editNumber,
                           HttpServletRequest rq,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                Card card = cardService.getById(editID);
                card.setBank(editBank);
                card.setNumber(editNumber);
                cardService.saveCard(card);
                redirectAttributes.addFlashAttribute("message", "Đã sửa thành công!!!");
                return "redirect:list-card/1";
            }
        }
    }


    @RequestMapping(value = "/delete-card")
    public String DeleteCard(@RequestParam(required = false, name = "deleteCard") Integer deleteCard,
                             @RequestParam(required = false, name = "resultCard") String resultCard,
                             HttpServletRequest rq,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                if (deleteCard != 0) {
                    cardService.deleteCardById(deleteCard);
                } else {
                    String[] listID = resultCard.split(",");
                    for (String idTemp : listID) {
                        cardService.deleteCardById(Integer.parseInt(idTemp));
                    }
                }
                redirectAttributes.addFlashAttribute("message", "Đã xoá thành công!!!");
                return "redirect:list-card/1";
            }
        }
    }


    @RequestMapping(value = "/list-address/{page}")
    public String ListAddressPage(@PathVariable Integer page,
                                  HttpServletRequest rq,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                Page<Address> addressList = addressService.pagingAddress(page - 1, PAGE_SIZE);
                int amount = addressService.findAll().size();

                int endPage = amount / PAGE_SIZE;

                if (amount % PAGE_SIZE != 0) {
                    endPage += 1;
                }

                if (page == null) {
                    page = 1;
                }

                List<User> userList = userService.findAll();
                model.addAttribute("addressList", addressList);
                model.addAttribute("tag", page);
                model.addAttribute("endPage", endPage);
                model.addAttribute("listUser", userList);
                return "admin/list-address";
            }
        }
    }


    @RequestMapping(value = "/add-address")
    public String AddAddress(@RequestParam(required = false, name = "addUserID") Integer addUserID,
                             @RequestParam(required = false, name = "addDescription") String addDescription,
                             HttpServletRequest rq,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                Address address = new Address();
                address.setUser(userService.getUserById(addUserID));
                address.setDescription(addDescription);
                addressService.saveAddress(address);
                redirectAttributes.addFlashAttribute("message", "Đã thêm thành công!!!");
                return "redirect:list-address/1";
            }
        }
    }


    @RequestMapping(value = "/edit-address")
    public String EditAddress(@RequestParam(required = false, name = "editID") Integer editID,
                              @RequestParam(required = false, name = "editDescription") String editDescription,
                              HttpServletRequest rq,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                Address address = addressService.getById(editID);
                address.setDescription(editDescription);
                addressService.saveAddress(address);
                redirectAttributes.addFlashAttribute("message", "Đã sửa thành công!!!");
                return "redirect:list-address/1";
            }
        }
    }


    @RequestMapping(value = "/delete-address")
    public String DeleteAddress(@RequestParam(required = false, name = "deleteAddress") Integer deleteAddress,
                                @RequestParam(required = false, name = "resultAddress") String resultAddress,
                                HttpServletRequest rq,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        HttpSession session = rq.getSession();
        User user = (User) session.getAttribute("account");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "Vui lòng đăng nhập!!!");
            return "redirect:/admin/login";
        } else {
            if (user.getRole() != 3) {
                redirectAttributes.addFlashAttribute("message", "Tài khoản không có quyền admin!!!");
                return "redirect:/admin/login";
            } else {
                //do some thing
                if (deleteAddress != 0) {
                    addressService.deleteAddressById(deleteAddress);
                } else {
                    String[] listID = resultAddress.split(",");
                    for (String idTemp : listID) {
                        addressService.deleteAddressById(Integer.parseInt(idTemp));
                    }
                }
                redirectAttributes.addFlashAttribute("message", "Đã xoá thành công!!!");
                return "redirect:list-address/1";
            }
        }
    }
}