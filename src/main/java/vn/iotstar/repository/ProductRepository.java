package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.iotstar.model.Category;
import vn.iotstar.model.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Lấy 10 sản phẩm mới nhất sắp xếp giảm dần theo ID
    List<Product> findTop10ByOrderByProductIdDesc();

    // 1. Hiển thị tất cả product có price từ thấp đến cao (Trang Home)
    List<Product> findAllByOrderByPriceAsc();

    // 2. Lấy tất cả product của 01 category (Trang Home)
    List<Product> findByCategory(Category category);

    // 3. Tìm kiếm theo tên có hỗ trợ phân trang
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);
}