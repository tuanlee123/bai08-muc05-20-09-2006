# Spring Boot GraphQL - Shopping Service (BAI4)

Dự án tích hợp **GraphQL API** cùng giao diện **Thymeleaf + AJAX** phục vụ hiển thị, tìm kiếm, phân trang và thao tác CRUD trên CSDL `ShoppingServiceMVC`.

---

## 1. Môi trường & Công nghệ
* **Framework:** Spring Boot 3.3.4 (Java 17+)
* **Database:** Microsoft SQL Server
* **ORM:** Spring Data JPA / Hibernate
* **API Engine:** Spring for GraphQL
* **Frontend:** Thymeleaf, AJAX (Fetch/jQuery), Bootstrap 5

---

## 2. Cấu hình chạy ứng dụng
* **Port:** `8086`
* **Context Path:** `/BAI4`

---

## 3. Các chức năng và Đường dẫn kiểm thử

### 1. Công cụ GraphiQL IDE (Slide 14–16)
* **URL:** `http://localhost:8086/BAI4/ql-graphql/ide`
* **Mô tả:** Giao diện trực quan để viết và thực thi các câu truy vấn GraphQL (Query & Mutation).

### 2. Trang Chủ (Home - Lọc & Sắp xếp)
* **URL:** `http://localhost:8086/BAI4/ql-graphql/home`
* **Chức năng:**
  * Hiển thị tất cả sản phẩm có `price` sắp xếp từ thấp đến cao (`productsPriceAsc`).
  * Dropdown lọc động toàn bộ sản phẩm của 01 danh mục (`productsByCategory`).

### 3. Quản lý Danh mục (Category CRUD)
* **URL:** `http://localhost:8086/BAI4/ql-graphql/categories`
* **Chức năng:** Tìm kiếm tương đối theo tên, phân trang server-side (`categoriesPaged`), Thêm/Sửa/Xóa danh mục bằng GraphQL Mutation.

### 4. Quản lý Sản phẩm (Product CRUD)
* **URL:** `http://localhost:8086/BAI4/ql-graphql/products`
* **Chức năng:** Tìm kiếm, phân trang server-side (`productsPaged`), cập nhật thông tin và danh mục sản phẩm thông qua GraphQL Mutation.