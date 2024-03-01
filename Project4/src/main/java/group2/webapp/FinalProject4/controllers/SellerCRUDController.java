package group2.webapp.FinalProject4.controllers;

import group2.webapp.FinalProject4.models.*;
import group2.webapp.FinalProject4.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/seller")
public class SellerCRUDController {

    public static final int PAGE_SITE = 10;
    @Autowired
    UserService userService;
    @Autowired
    CustomerInfoService customerInfoService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService Cservice;
    @Autowired
    BillService billService;
    @Autowired
    BillDetailService billDetailService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductDetailService productDetailService;
    @Autowired
    UploadFileService uploadFileService;

    @RequestMapping(value = {"/list-product/{pagenumber}"})
    public String ListAllProduct(HttpServletRequest rq,
                                 @PathVariable("pagenumber") Integer pagenumber,
                                 Model model) {
        HttpSession session = rq.getSession();
        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                String indexPage = String.valueOf(pagenumber);
                if (indexPage == null) {
                    indexPage = "1";
                }

                List<Product> listAllProduct = productService.findAll();

                int count = listAllProduct.size();
                int index = Integer.parseInt(indexPage);

                int endPage = count / 10;
                if (count % 10 != 0) {
                    endPage = endPage + 1;
                }

                List<Category> listAllCategory = Cservice.findAll();

                List<Product> listProductPaging = productService.PagingAllProduct(pagenumber - 1, PAGE_SITE).getContent();
                model.addAttribute("endP", endPage);
                model.addAttribute("tag", index);
                model.addAttribute("listAllProduct", listProductPaging);
                model.addAttribute("listAllCategory", listAllCategory);
                model.addAttribute("counts", listAllProduct.size());
                //model.addAttribute("check", check);
                return "seller/list-product";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/edit-product"}, method = RequestMethod.POST)
    public String EditProduct(HttpServletRequest rq,
                              @RequestParam(required = false, value = "editID") int id,
                              @RequestParam(required = false, value = "editName") String name,
                              @RequestParam(required = false, value = "editPrice") int price,
                              @RequestParam(required = false, value = "editAmount") int amount,
                              @RequestParam(required = false, value = "editQuantity") int quantity,
                              @RequestParam(required = false, value = "editCategory") int categoryID,
                              @RequestParam("thumbnailPhoto") MultipartFile thumbnailPhoto,
                              Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();
        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    Product product = productService.getProductById(id);
                    product.setName(name);
                    product.setPrice(price);
                    product.setAmount(amount);
                    product.setQuantity(quantity);
                    product.setCategory(Cservice.findCategoryById(categoryID));

                    try {
                        product.setThumbnailPhoto(uploadFileService.storeFile(thumbnailPhoto));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    productService.saveProduct(product);
                    String message = "Edit Product Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Edit Product Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-product/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }


    }

    @RequestMapping(value = {"/add-product"}, method = RequestMethod.POST)
    public String AddProduct(HttpServletRequest rq,
                             @RequestParam(required = false, value = "addName") String name,
                             @RequestParam(required = false, value = "addPrice") int price,
                             @RequestParam(required = false, value = "addQuantity") int quantity,
                             @RequestParam(required = false, value = "addCategory") int category,
                             @RequestParam("addThumbnailPhoto") MultipartFile thumbnailPhoto,
                             Model model, RedirectAttributes redirectAttributes) {


        HttpSession session = rq.getSession();
        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    Product product = new Product();
                    product.setPrice(price);
                    product.setAmount(0);
                    product.setQuantity(quantity);
                    product.setName(name);
                    product.setCategory(Cservice.findCategoryById(category));

                    try {
                        product.setThumbnailPhoto(uploadFileService.storeFile(thumbnailPhoto));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    productService.saveProduct(product);
                    String message = "Add Product Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Add Product Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-product/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = "/delete-product", method = RequestMethod.POST)
    public String deleteProduct(HttpServletRequest rq,
                                @RequestParam(required = false, value = "deleteProduct") int id,
                                @RequestParam(required = false, value = "result") String result,
                                RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();
        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    if (id != 0) {
                        productService.deleteProduct(id);
                    } else {
                        String[] listID = result.split(",");
                        for (String idP : listID) {
                            productService.deleteProduct(Integer.parseInt(idP));
                        }
                    }

                    String message = "Delete Product Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Delete Product Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }


                return "redirect:/seller/list-product/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }

    }

    @RequestMapping(value = {"/list-category/{pagenumber}"})
    public String ListAllCategory(HttpServletRequest rq,
                                  @PathVariable("pagenumber") Integer pagenumber,
                                  Model model, RedirectAttributes redirectAttributes) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                String indexPage = String.valueOf(pagenumber);
                if (indexPage == null) {
                    indexPage = "1";
                }

                List<Category> listAllCategory = Cservice.findAll();

                int count = listAllCategory.size();
                int index = Integer.parseInt(indexPage);

                int endPage = count / 10;
                if (count % 10 != 0) {
                    endPage = endPage + 1;
                }

                //List<Category> listAllCategory = Cservice.findAll();

                List<Category> listCategoryPaging = Cservice.PagingAllCategory(pagenumber - 1, PAGE_SITE).getContent();
                model.addAttribute("endP", endPage);
                model.addAttribute("tag", index);
                model.addAttribute("listAllCategory", listCategoryPaging);
                model.addAttribute("counts", count);
                //model.addAttribute("check", check);
                return "seller/list-category";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/add-category"}, method = RequestMethod.POST)
    public String AddProduct(HttpServletRequest rq,
                             @RequestParam(required = false, value = "addName") String name,
                             Model model, RedirectAttributes redirectAttributes) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    Category category = new Category();
                    category.setName(name);
                    Cservice.saveCategory(category);
                    String message = "Add Category Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Add Category Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-category/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/edit-category"}, method = RequestMethod.POST)
    public String EditCategory(HttpServletRequest rq,
                               @RequestParam(required = false, value = "editID") int id,
                               @RequestParam(required = false, value = "editName") String name,
                               Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    Category category = Cservice.findCategoryById(id);
                    category.setName(name);

                    Cservice.saveCategory(category);
                    String message = "Edit Category Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Edit Category Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-category/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = "/delete-category", method = RequestMethod.POST)
    public String deleteCategory(HttpServletRequest rq,
                                 @RequestParam(required = false, value = "deleteCategory") int id,
                                 @RequestParam(required = false, value = "result") String result,
                                 RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    if (id != 0) {
                        Cservice.deleteCategory(id);
                    } else {
                        String[] listID = result.split(",");
                        for (String idC : listID) {
                            Cservice.deleteCategory(Integer.parseInt(idC));
                        }
                    }

                    String message = "Delete Category Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Delete Category Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-category/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }

    }

    @RequestMapping(value = {"/list-productimage/{pagenumber}"})
    public String ListAllProductImg(HttpServletRequest rq,
                                    @PathVariable("pagenumber") Integer pagenumber,
                                    Model model) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                String indexPage = String.valueOf(pagenumber);
                if (indexPage == null) {
                    indexPage = "1";
                }

                List<ProductImage> listAllProductImg = productImageService.findAll();

                int count = listAllProductImg.size();
                int index = Integer.parseInt(indexPage);

                int endPage = count / 10;
                if (count % 10 != 0) {
                    endPage = endPage + 1;
                }

                List<Product> listAllProduct = productService.findAll();

                List<ProductImage> productImageList = productImageService.PagingAllProductImg(pagenumber - 1, PAGE_SITE).getContent();
                model.addAttribute("endP", endPage);
                model.addAttribute("tag", index);
                model.addAttribute("listAllProductImg", productImageList);
                model.addAttribute("listAllProduct", listAllProduct);
                model.addAttribute("counts", count);
                //model.addAttribute("check", check);
                return "seller/list-productimage";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/add-productimage"}, method = RequestMethod.POST)
    public String AddProductImg(HttpServletRequest rq,
                                @RequestParam("addLink") MultipartFile thumbnailPhoto,
                                @RequestParam(required = false, value = "addProduct") int idProduct,
                                Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    ProductImage productImage = new ProductImage();
                    try {
                        productImage.setLink(uploadFileService.storeFile(thumbnailPhoto));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    productImage.setProduct(productService.getProductById(idProduct));
                    productImageService.save(productImage);
                    String message = "Add Product Imagge Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Add Product Imagge Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-productimage/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/edit-productimage"}, method = RequestMethod.POST)
    public String EditProductImg(HttpServletRequest rq,
                                 @RequestParam(required = false, value = "editID") int id,
                                 @RequestParam("editLink") MultipartFile thumbnailPhoto,
                                 Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    ProductImage productImage = productImageService.getProductImgById(id);

                    try {
                        productImage.setLink(uploadFileService.storeFile(thumbnailPhoto));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    productImageService.save(productImage);
                    String message = "Edit Product Imagge Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Edit Product Imagge Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-productimage/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = "/delete-productimage", method = RequestMethod.POST)
    public String deleteProductImg(HttpServletRequest rq,
                                   @RequestParam(required = false, value = "deleteProductImg") int id,
                                   @RequestParam(required = false, value = "result") String result,
                                   RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    if (id != 0) {
                        productImageService.deleteProductImg(id);
                    } else {
                        String[] listID = result.split(",");
                        for (String idC : listID) {
                            Cservice.deleteCategory(Integer.parseInt(idC));
                        }
                    }

                    String message = "Delete Product Imagge Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Delete Product Imagge Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-productimage/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }

    }

    @RequestMapping(value = {"/list-productdetail/{pagenumber}"})
    public String ListAllProductDetail(HttpServletRequest rq,
                                       @PathVariable("pagenumber") Integer pagenumber,
                                       Model model) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                String indexPage = String.valueOf(pagenumber);
                if (indexPage == null) {
                    indexPage = "1";
                }

                List<ProductDetail> listAllProductDetail = productDetailService.findAll();
                //System.out.println("name" + listAllCategory.get(0).getName());

                int count = listAllProductDetail.size();
                int index = Integer.parseInt(indexPage);

                int endPage = count / 10;
                if (count % 10 != 0) {
                    endPage = endPage + 1;
                }

                List<Product> listAllProduct = productService.findAll();
                List<Product> listProductNoDetail = new ArrayList<>();
                for (Product p : listAllProduct) {
                    int i = 0;
                    for (ProductDetail pd : listAllProductDetail) {
                        if (p.getId() == pd.getProduct().getId()) {
                            break;
                        }
                        i++;
                    }
                    if (i == listAllProductDetail.size()) {
                        listProductNoDetail.add(p);
                    }
                }

                List<ProductDetail> listProductDetailPaging = productDetailService.PagingAllProductDetail(pagenumber - 1, PAGE_SITE).getContent();
                model.addAttribute("endP", endPage);
                model.addAttribute("tag", index);
                model.addAttribute("listAllProductDetail", listProductDetailPaging);
                model.addAttribute("listAllProduct", listProductNoDetail);
                model.addAttribute("counts", count);
                //model.addAttribute("check", check);
                return "seller/list-productdetail";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/add-productdetail"}, method = RequestMethod.POST)
    public String AddProductDetail(HttpServletRequest rq,
                                   @RequestParam(required = false, value = "addBrand") String brand,
                                   @RequestParam(required = false, value = "addColor") String color,
                                   @RequestParam(required = false, value = "addDescription") String description,
                                   @RequestParam(required = false, value = "addMaterial") String material,
                                   @RequestParam(required = false, value = "addProduct") int idProduct,
                                   Model model, RedirectAttributes redirectAttributes) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    ProductDetail productDetail = new ProductDetail();
                    productDetail.setBrand(brand);
                    productDetail.setColor(color);
                    productDetail.setDescription(description);
                    productDetail.setMaterial(material);
                    productDetail.setProduct(productService.getProductById(idProduct));
                    productDetailService.save(productDetail);
                    String message = "Add Product Detail Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Add Product Detail Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }


                return "redirect:/seller/list-productdetail/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/edit-productdetail"}, method = RequestMethod.POST)
    public String EditProductDetail(HttpServletRequest rq,
                                    @RequestParam(required = false, value = "editID") int id,
                                    @RequestParam(required = false, value = "editBrand") String brand,
                                    @RequestParam(required = false, value = "editColor") String color,
                                    @RequestParam(required = false, value = "editDescription") String description,
                                    @RequestParam(required = false, value = "editMaterial") String material,
                                    Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    ProductDetail productDetail = productDetailService.getProductDetailById(id);
                    productDetail.setBrand(brand);
                    productDetail.setColor(color);
                    productDetail.setDescription(description);
                    productDetail.setMaterial(material);

                    productDetailService.save(productDetail);
                    String message = "Edit Product Detail Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Edit Product Detail Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-productdetail/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = "/delete-productdetail", method = RequestMethod.POST)
    public String deleteProductDetail(HttpServletRequest rq,
                                      @RequestParam(required = false, value = "deleteProductDetail") int id,
                                      @RequestParam(required = false, value = "result") String result,
                                      RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    if (id != 0) {
                        productDetailService.deleteProductDetail(id);
                    } else {
                        String[] listID = result.split(",");
                        for (String idC : listID) {
                            productDetailService.deleteProductDetail(Integer.parseInt(idC));
                        }
                    }

                    String message = "Delete Product Detail Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Delete Product Detail Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                    e.printStackTrace();
                }

                return "redirect:/seller/list-productdetail/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }

    }

    @RequestMapping(value = {"/list-bill/{pagenumber}"})
    public String ListAllBill(HttpServletRequest rq,
                              @PathVariable("pagenumber") Integer pagenumber,
                              Model model) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                String indexPage = String.valueOf(pagenumber);
                if (indexPage == null) {
                    indexPage = "1";
                }

                List<Bill> listAllBill = billService.findAll();
                //System.out.println("name" + listAllCategory.get(0).getName());

                int count = listAllBill.size();
                int index = Integer.parseInt(indexPage);

                int endPage = count / 10;
                if (count % 10 != 0) {
                    endPage = endPage + 1;
                }

                List<Bill> listBillPaging = billService.PagingAllBillByStatus(pagenumber - 1, PAGE_SITE, 1).getContent();
                List<User> listAllUser = userService.findUserByRole(1);
                model.addAttribute("endP", endPage);
                model.addAttribute("tag", index);
                model.addAttribute("listAllBill", listBillPaging);
                model.addAttribute("listAllUser", listAllUser);
                model.addAttribute("counts", count);
                //model.addAttribute("check", check);
                return "seller/list-bill";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/add-bill"}, method = RequestMethod.POST)
    public String AddBill(HttpServletRequest rq,
                          @RequestParam(required = false, value = "addName") int idUser,
                          @RequestParam(required = false, value = "addDate") Date date,
                          @RequestParam(required = false, value = "addTotal") double total,
                          @RequestParam(required = false, value = "addStt") int status,
                          Model model, RedirectAttributes redirectAttributes) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    Bill bill = new Bill();
                    bill.setUser(userService.getUserById(idUser));
                    bill.setDate(date);
                    bill.setTotal(total);
                    bill.setStatus(status);

                    billService.saveBill(bill);
                    String message = "Add Bill Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Add Bill Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }


                return "redirect:/seller/list-bill/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/edit-bill"}, method = RequestMethod.POST)
    public String EditBill(HttpServletRequest rq,
                           @RequestParam(required = false, value = "editID") int id,
                           @RequestParam(required = false, value = "editBrand") String brand,
                           @RequestParam(required = false, value = "editColor") String color,
                           @RequestParam(required = false, value = "editDescription") String description,
                           @RequestParam(required = false, value = "editMaterial") String material,
                           Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    ProductDetail productDetail = productDetailService.getProductDetailById(id);
                    productDetail.setBrand(brand);
                    productDetail.setColor(color);
                    productDetail.setDescription(description);
                    productDetail.setMaterial(material);

                    productDetailService.save(productDetail);
                    String message = "Edit Bill Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Edit Bill Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-bill/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = "/delete-bill", method = RequestMethod.POST)
    public String deleteBill(HttpServletRequest rq,
                             @RequestParam(required = false, value = "deleteBill") int id,
                             @RequestParam(required = false, value = "result") String result,
                             RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    if (id != 0) {
                        billService.deleteBill(id);
                    } else {
                        String[] listID = result.split(",");
                        for (String idC : listID) {
                            billService.deleteBill(Integer.parseInt(idC));
                        }
                    }

                    String message = "Delete Bill Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Delete Bill Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                    e.printStackTrace();
                }

                return "redirect:/seller/list-bill/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }

    }

    @RequestMapping(value = {"/list-billdetail/{pagenumber}"})
    public String ListAllBillDetail(HttpServletRequest rq,
                                    @PathVariable("pagenumber") Integer pagenumber,
                                    Model model) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                String indexPage = String.valueOf(pagenumber);
                if (indexPage == null) {
                    indexPage = "1";
                }

                List<BillDetail> billDetailList = billDetailService.findAll();
                //System.out.println("name" + listAllCategory.get(0).getName());

                int count = billDetailList.size();
                int index = Integer.parseInt(indexPage);

                int endPage = count / 10;
                if (count % 10 != 0) {
                    endPage = endPage + 1;
                }

                List<BillDetail> listBillPaging = billDetailService.PagingAllBillDetail(pagenumber - 1, PAGE_SITE).getContent();
                List<Bill> listAllBill = billService.findAll();
                List<Product> listAllProduct = productService.findAll();
                model.addAttribute("endP", endPage);
                model.addAttribute("tag", index);
                model.addAttribute("listAllBillDetail", listBillPaging);
                model.addAttribute("listAllBill", listAllBill);
                model.addAttribute("listAllProduct", listAllProduct);
                model.addAttribute("counts", count);
                //model.addAttribute("check", check);
                return "seller/list-billdetail";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/add-billdetail"}, method = RequestMethod.POST)
    public String AddBillDetail(HttpServletRequest rq,
                                @RequestParam(required = false, value = "addBill") int idBill,
                                @RequestParam(required = false, value = "addProduct") String idProduct,
                                @RequestParam(required = false, value = "addQuantity") int quantity,
                                Model model, RedirectAttributes redirectAttributes) {

        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    BillDetail billDetail = new BillDetail();
                    String[] listP = idProduct.split(",");
                    Product product = productService.getProductById(Integer.valueOf(listP[0]));
                    Bill bill = billService.getBillById(idBill);
                    int amount = product.getAmount();
                    product.setAmount(amount + quantity);
                    billDetail.setBillId(bill);
                    billDetail.setProductId(product);
                    billDetail.setQuantity(quantity);
                    //List<BillDetail> billDetailList = billDetailService.findAllByBillId(bill);
                    double total = bill.getTotal();

                    bill.setTotal(total + (quantity * product.getPrice()));

                    billDetailService.saveBillDetail(billDetail);
                    String message = "Add Bill Detail Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Add Bill Detail Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }


                return "redirect:/seller/list-billdetail/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = {"/edit-billdetail"}, method = RequestMethod.POST)
    public String EditBillDetail(HttpServletRequest rq,
                                 @RequestParam(required = false, value = "editBill") int bill,
                                 @RequestParam(required = false, value = "editProduct") String product,
                                 @RequestParam(required = false, value = "editQuantity") int quantity,
                                 @RequestParam(required = false, value = "editProductID") int productID,
                                 Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    Optional<BillDetail> billDetail = billDetailService.findBillDetailsByProductIdAndBillId(productService.getProductById(productID), billService.getBillById(bill));
                    Product product1 = productService.getProductById(productID);
                    int amount = product1.getAmount();
                    if (billDetail.get().getQuantity() > quantity) {
                        product1.setAmount(amount - (billDetail.get().getQuantity() - quantity));
                    } else {
                        product1.setAmount(amount + (quantity - billDetail.get().getQuantity()));
                    }
                    billDetail.get().setQuantity(quantity);

                    billDetailService.saveBillDetail(billDetail.get());
                    String message = "Edit Bill Detail Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Edit Bill Detail Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                }

                return "redirect:/seller/list-billdetail/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }
    }

    @RequestMapping(value = "/delete-billdetail", method = RequestMethod.POST)
    public String deleteBillDetail(HttpServletRequest rq,
                                   @RequestParam(required = false, value = "deleteBillDetail") int idBill,
                                   @RequestParam(required = false, value = "deleteProductDetail") int idProduct,
                                   @RequestParam(required = false, value = "result") String result,
                                   RedirectAttributes redirectAttributes) {
        HttpSession session = rq.getSession();

        if (session != null && session.getAttribute("account") != null) {
            User user = (User) session.getAttribute("account");
            if (user.getRole() == 2) {
                try {
                    if (idBill != 0 && idProduct != 0) {
                        billDetailService.deleteBillDetail(billService.getBillById(idBill), productService.getProductById(idProduct));
                    } else {
                        System.out.println("Đây" + result);
                        String[] listAllID = result.split(",");
                        for (String idBP : listAllID) {
                            String[] listID = idBP.split(":");
                            System.out.println("Đây" + idBP);
                            billDetailService.deleteBillDetail(billService.getBillById(Integer.parseInt(listID[0])), productService.getProductById(Integer.parseInt(listID[1])));
                        }
                    }

                    String message = "Delete Bill Detail Succes!!!";
                    redirectAttributes.addFlashAttribute("message", message);
                } catch (Exception e) {
                    String messageError = "Delete Bill Detail Failed!!!";
                    redirectAttributes.addFlashAttribute("messageError", messageError);
                    e.printStackTrace();
                }

                return "redirect:/seller/list-billdetail/1";
            } else {
                session.removeAttribute("account");
                return "redirect:/seller/login";
            }
        } else {
            return "redirect:/seller/login";
        }

    }

}
