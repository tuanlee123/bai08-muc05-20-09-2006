package vn.iotstar.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.model.Category;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.Constant;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/category")
public class CategoryApiController {

    @Autowired
    private ICategoryService categoryService;

    @Value("${app.upload.dir:" + Constant.DIR + "}")
    private String uploadDir;

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping(path = "/getCategory")
    public ResponseEntity<?> getCategory(@RequestParam("id") int id) {
        Category category = categoryService.findById(id);
        if (category != null) {
            return new ResponseEntity<>(new Response(true, "Thành công", category), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/addCategory")
    public ResponseEntity<?> addCategory(@RequestParam("categoryname") String categoryname,
                                         @RequestParam(value = "status", defaultValue = "1") int status,
                                         @RequestParam(value = "images", required = false) MultipartFile images) {
        Category exist = categoryService.findByCategoryname(categoryname);
        if (exist != null) {
            return new ResponseEntity<>(new Response(false, "Category đã tồn tại trong hệ thống", null), HttpStatus.BAD_REQUEST);
        }

        Category category = new Category();
        category.setCategoryname(categoryname);
        category.setStatus(status);

        if (images != null && !images.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + images.getOriginalFilename();
            try {
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                images.transferTo(new File(dir, fileName));
                category.setImages(fileName);
            } catch (IOException e) {
                return new ResponseEntity<>(new Response(false, "Lỗi upload ảnh: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        categoryService.insert(category);
        return new ResponseEntity<>(new Response(true, "Thêm thành công", category), HttpStatus.OK);
    }

    @PutMapping(path = "/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestParam("categoryId") int categoryId,
                                            @RequestParam("categoryname") String categoryname,
                                            @RequestParam(value = "status", defaultValue = "1") int status,
                                            @RequestParam(value = "images", required = false) MultipartFile images) {
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.BAD_REQUEST);
        }

        category.setCategoryname(categoryname);
        category.setStatus(status);

        if (images != null && !images.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + images.getOriginalFilename();
            try {
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                images.transferTo(new File(dir, fileName));
                category.setImages(fileName);
            } catch (IOException e) {
                return new ResponseEntity<>(new Response(false, "Lỗi upload ảnh: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        categoryService.update(category);
        return new ResponseEntity<>(new Response(true, "Cập nhật thành công", category), HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@RequestParam("categoryId") int categoryId) {
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Category", null), HttpStatus.BAD_REQUEST);
        }
        categoryService.delete(categoryId);
        return new ResponseEntity<>(new Response(true, "Xóa thành công", null), HttpStatus.OK);
    }
}