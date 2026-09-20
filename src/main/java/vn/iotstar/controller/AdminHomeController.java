package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @GetMapping("/home")
    public String home() {
        return "admin/home";
    }

    // URL: /BAI3/admin/categories-ajax -> Map tới: /views/admin/category-ajax.jsp
    @GetMapping("/categories-ajax")
    public String categoryAjax() {
        return "admin/category-ajax";
    }

    // URL: /BAI3/admin/products-ajax -> Map tới: /views/admin/product-ajax.jsp
    @GetMapping("/products-ajax")
    public String productAjax() {
        return "admin/product-ajax";
    }
}