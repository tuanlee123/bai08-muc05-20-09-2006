package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.model.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.Constant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Value("${app.upload.dir:" + Constant.DIR + "}")
    private String uploadDir;

    // 1. Danh sách, Tìm kiếm & Phân trang
    @GetMapping("/categories")
    public String listCategories(@RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                 Model model) {
        // Pageable trong Spring Data JPA tính từ index 0
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("categoryId").descending());
        Page<Category> categoryPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryPage = categoryService.searchByName(keyword.trim(), pageable);
        } else {
            categoryPage = categoryService.findAll(pageable);
        }

        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/categories/list";
    }

    // 2. Mở form thêm mới
    @GetMapping("/category/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/add";
    }

    // 3. Xử lý thêm mới
    @PostMapping(value = {"/category/insert", "/category/add"})
    public String insertCategory(@RequestParam("categoryname") String categoryname,
                                 @RequestParam(value = "status", defaultValue = "1") int status,
                                 @RequestParam(value = "image", required = false) MultipartFile file) {
        Category category = new Category();
        category.setCategoryname(categoryname);
        category.setStatus(status);

        try {
            if (file != null && !file.isEmpty() && file.getOriginalFilename() != null) {
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
                String ext = "";
                int dotIndex = originalFileName.lastIndexOf(".");
                if (dotIndex >= 0) {
                    ext = originalFileName.substring(dotIndex);
                }
                String newFileName = "cate_" + System.currentTimeMillis() + ext;
                Path destination = Paths.get(uploadDir, newFileName);
                Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                category.setImages(newFileName);
            } else {
                category.setImages("default_cate.png");
            }
        } catch (Exception e) {
            e.printStackTrace();
            category.setImages("default_cate.png");
        }

        categoryService.insert(category);
        return "redirect:/admin/categories";
    }

    // 4. Mở form chỉnh sửa
    @GetMapping("/category/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        try {
            Category category = categoryService.findById(id);
            if (category != null) {
                model.addAttribute("category", category);
                return "admin/categories/edit";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/categories";
    }

    // 5. Xử lý cập nhật
    @PostMapping(value = {"/category/update", "/category/edit"})
    public String updateCategory(@RequestParam("categoryId") int categoryId,
                                 @RequestParam("categoryname") String categoryname,
                                 @RequestParam(value = "status", defaultValue = "1") int status,
                                 @RequestParam(value = "image", required = false) MultipartFile file) {
        try {
            Category category = categoryService.findById(categoryId);
            if (category != null) {
                category.setCategoryname(categoryname);
                category.setStatus(status);

                if (file != null && !file.isEmpty() && file.getOriginalFilename() != null) {
                    File dir = new File(uploadDir);
                    if (!dir.exists()) dir.mkdirs();

                    String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
                    String ext = "";
                    int dotIndex = originalFileName.lastIndexOf(".");
                    if (dotIndex >= 0) {
                        ext = originalFileName.substring(dotIndex);
                    }
                    String newFileName = "cate_" + System.currentTimeMillis() + ext;
                    Path destination = Paths.get(uploadDir, newFileName);

                    // Xóa file ảnh cũ nếu có
                    String oldImage = category.getImages();
                    if (oldImage != null && !oldImage.startsWith("http") && !"default_cate.png".equals(oldImage)) {
                        deleteOldFile(uploadDir + File.separator + oldImage);
                    }

                    Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                    category.setImages(newFileName);
                }

                categoryService.update(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/categories";
    }

    // 6. Xóa danh mục
    @GetMapping("/category/delete")
    public String deleteCategory(@RequestParam("id") int id) {
        try {
            Category category = categoryService.findById(id);
            if (category != null) {
                String oldImage = category.getImages();
                if (oldImage != null && !oldImage.startsWith("http") && !"default_cate.png".equals(oldImage)) {
                    deleteOldFile(uploadDir + File.separator + oldImage);
                }
                categoryService.delete(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/categories";
    }

    private void deleteOldFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}