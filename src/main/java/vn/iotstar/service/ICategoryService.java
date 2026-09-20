package vn.iotstar.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.iotstar.model.Category;

public interface ICategoryService {
    void insert(Category category);
    void update(Category category);
    void delete(int id);
    Category findById(int id);
    Category findByCategoryname(String name);
    List<Category> findAll();
    List<Category> searchByName(String keyword);
    List<Category> findAll(int page, int pagesize);
    int count();

    // Các phương thức bổ sung phục vụ phân trang và tìm kiếm chuẩn Spring Data JPA
    Page<Category> findAll(Pageable pageable);
    Page<Category> searchByName(String keyword, Pageable pageable);
}