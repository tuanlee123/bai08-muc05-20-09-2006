package vn.iotstar.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.model.Category;
import vn.iotstar.model.Product;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.util.Constant;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/product")
public class ProductApiController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Value("${app.upload.dir:" + Constant.DIR + "}")
    private String uploadDir;

    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping(path = "/getProduct")
    public ResponseEntity<?> getProduct(@RequestParam("id") int id) {
        Product product = productService.findById(id);
        if (product != null) {
            return new ResponseEntity<>(new Response(true, "Thành công", product), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Response(false, "Không tìm thấy sản phẩm", null), HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/addProduct")
    public ResponseEntity<?> addProduct(@RequestParam("productName") String productName,
                                        @RequestParam("price") double price,
                                        @RequestParam("description") String description,
                                        @RequestParam("categoryId") int categoryId,
                                        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Product product = new Product();
        product.setProductName(productName);
        product.setPrice(price);
        product.setDescription(description);
        product.setCreatedAt(new Date());

        Category category = categoryService.findById(categoryId);
        product.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            try {
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                imageFile.transferTo(new File(dir, fileName));
                product.setImageUrl(fileName);
            } catch (IOException e) {
                return new ResponseEntity<>(new Response(false, "Lỗi upload file: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        productService.insert(product);
        return new ResponseEntity<>(new Response(true, "Thêm sản phẩm thành công", product), HttpStatus.OK);
    }

    @PutMapping(path = "/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestParam("productId") int productId,
                                           @RequestParam("productName") String productName,
                                           @RequestParam("price") double price,
                                           @RequestParam("description") String description,
                                           @RequestParam("categoryId") int categoryId,
                                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Product product = productService.findById(productId);
        if (product == null) {
            return new ResponseEntity<>(new Response(false, "Sản phẩm không tồn tại", null), HttpStatus.BAD_REQUEST);
        }

        product.setProductName(productName);
        product.setPrice(price);
        product.setDescription(description);

        Category category = categoryService.findById(categoryId);
        product.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            try {
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                imageFile.transferTo(new File(dir, fileName));
                product.setImageUrl(fileName);
            } catch (IOException e) {
                return new ResponseEntity<>(new Response(false, "Lỗi upload file: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        productService.update(product);
        return new ResponseEntity<>(new Response(true, "Cập nhật sản phẩm thành công", product), HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") int productId) {
        Product product = productService.findById(productId);
        if (product == null) {
            return new ResponseEntity<>(new Response(false, "Sản phẩm không tồn tại", null), HttpStatus.BAD_REQUEST);
        }
        productService.delete(productId);
        return new ResponseEntity<>(new Response(true, "Xóa sản phẩm thành công", null), HttpStatus.OK);
    }
}