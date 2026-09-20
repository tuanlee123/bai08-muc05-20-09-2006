package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // 1. Tìm kiếm tương đối theo tên (danh sách thường)
    List<Category> findByCategorynameContainingIgnoreCase(String keyword);

    // 2. Tìm kiếm tương đối theo tên có hỗ trợ phân trang
    Page<Category> findByCategorynameContainingIgnoreCase(String keyword, Pageable pageable);

    // 3. Tìm chính xác theo tên trả về Optional (dùng kiểm tra trùng lặp)
    Optional<Category> findByCategoryname(String categoryname);
}