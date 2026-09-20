<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Sản Phẩm (AJAX) - Admin</title>
    <!-- Đồng bộ Bootstrap 5 & Bootstrap Icons từ categories.jsp -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script>var contextPath = "${pageContext.request.contextPath}";</script>
    <style>
        .sidebar { width: 250px; min-height: 100vh; background-color: #212529; }
        .sidebar .nav-link { color: #adb5bd; border-radius: 6px; margin-bottom: 4px; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { color: #fff; background-color: #343a40; }
        .main-wrapper { flex: 1; background-color: #f8f9fa; min-height: 100vh; }
        .table > tbody > tr > td { vertical-align: middle; }
    </style>
</head>
<body>
<div class="d-flex">
    <!-- Sidebar Admin -->
    <div class="sidebar p-3 d-flex flex-column text-white flex-shrink-0">
        <a href="<c:url value='/admin/home'/>" class="d-flex align-items-center mb-3 text-warning text-decoration-none fs-5 fw-bold">
            <i class="bi bi-shield-lock-fill me-2"></i> ADMIN PANEL
        </a>
        <hr class="text-secondary">
        <ul class="nav nav-pills flex-column mb-auto">
            <li><a href="<c:url value='/admin/home'/>" class="nav-link"><i class="bi bi-speedometer2 me-2"></i> Bảng điều khiển</a></li>
            <li><a href="<c:url value='/admin/categories'/>" class="nav-link"><i class="bi bi-folder2 me-2"></i> Danh mục (Thường)</a></li>
            <li><a href="<c:url value='/admin/categories-ajax'/>" class="nav-link"><i class="bi bi-folder2-open me-2"></i> Danh mục (AJAX)</a></li>
            <li><a href="<c:url value='/admin/products'/>" class="nav-link"><i class="bi bi-box-seam me-2"></i> Sản phẩm (Thường)</a></li>
            <li><a href="<c:url value='/admin/products-ajax'/>" class="nav-link active"><i class="bi bi-boxes me-2"></i> Sản phẩm (AJAX)</a></li>
            <li><a href="<c:url value='/admin/users'/>" class="nav-link"><i class="bi bi-people me-2"></i> Quản lý người dùng</a></li>
            <li class="mt-3 border-top border-secondary pt-3">
                <a href="<c:url value='/home'/>" class="nav-link text-info"><i class="bi bi-globe me-2"></i> Xem ngoài Web</a>
            </li>
        </ul>
        <hr class="text-secondary">
        <a href="<c:url value='/logout'/>" class="btn btn-outline-danger btn-sm w-100"><i class="bi bi-box-arrow-right me-1"></i> Đăng xuất</a>
    </div>

    <!-- Main Content -->
    <div class="main-wrapper p-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold text-dark mb-0">Quản Lý Sản Phẩm (AJAX API)</h3>
                <small class="text-muted">Quản lý kho hàng bằng RESTful Service và render bảng qua jQuery</small>
            </div>
            <button type="button" class="btn btn-primary fw-semibold shadow-sm" onclick="openAddProductModal()">
                <i class="bi bi-plus-lg me-1"></i> Thêm Mới Sản Phẩm
            </button>
        </div>

        <!-- Bảng Danh Sách Sản Phẩm -->
        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover table-striped align-middle mb-0" id="productTable">
                        <thead class="table-dark">
                            <tr>
                                <th class="text-center" style="width: 80px;">#ID</th>
                                <th style="width: 100px;" class="text-center">Hình ảnh</th>
                                <th>Tên sản phẩm</th>
                                <th style="width: 150px;">Đơn giá</th>
                                <th style="width: 160px;">Danh mục</th>
                                <th class="text-center" style="width: 130px;">Ngày tạo</th>
                                <th class="text-center" style="width: 180px;">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Dữ liệu render qua AJAX -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal Thêm / Sửa Sản Phẩm -->
<div class="modal fade" id="productModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <form id="productForm" enctype="multipart/form-data">
                <div class="modal-header bg-dark text-white">
                    <h5 class="modal-title fw-bold" id="productModalTitle">Thêm Mới Sản Phẩm</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body p-4">
                    <input type="hidden" id="productId" name="productId">

                    <div class="mb-3">
                        <label class="form-label fw-bold">Tên sản phẩm <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="productName" name="productName" placeholder="Nhập tên sản phẩm..." required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Giá bán (VNĐ) <span class="text-danger">*</span></label>
                        <input type="number" step="any" class="form-control" id="price" name="price" placeholder="0" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Danh mục <span class="text-danger">*</span></label>
                        <select class="form-select" id="categoryId" name="categoryId" required>
                            <!-- Load tự động qua AJAX -->
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Mô tả sản phẩm</label>
                        <textarea class="form-control" id="description" name="description" rows="3" placeholder="Mô tả tóm tắt..."></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Hình ảnh sản phẩm</label>
                        <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*">
                        <div id="previewProductImg" class="mt-2 text-center"></div>
                    </div>
                </div>
                <div class="modal-footer bg-light">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                    <button type="submit" class="btn btn-primary px-3"><i class="bi bi-save me-1"></i> Lưu lại</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    var isProductEdit = false;

    $(document).ready(function() {
        loadCategoryDropdown();
        loadProducts();

        $("#productForm").submit(function(e) {
            e.preventDefault();
            var formData = new FormData(this);
            var apiUrl = isProductEdit ? (contextPath + '/api/product/updateProduct') : (contextPath + '/api/product/addProduct');
            var httpMethod = isProductEdit ? 'PUT' : 'POST';

            $.ajax({
                url: apiUrl,
                type: httpMethod,
                data: formData,
                processData: false,
                contentType: false,
                success: function(res) {
                    alert(res.message);
                    $("#productModal").modal('hide');
                    loadProducts();
                },
                error: function(xhr) {
                    var msg = xhr.responseJSON ? xhr.responseJSON.message : "Thao tác thất bại!";
                    alert(msg);
                }
            });
        });
    });

    function loadCategoryDropdown() {
        $.getJSON(contextPath + '/api/category', function(categories) {
            var opts = '<option value="">-- Chọn danh mục --</option>';
            categories.forEach(function(c) {
                opts += '<option value="' + c.categoryId + '">' + c.categoryname + '</option>';
            });
            $('#categoryId').html(opts);
        });
    }

    function loadProducts() {
        $.getJSON(contextPath + '/api/product', function(data) {
            var rows = '';
            data.forEach(function(item) {
                var imgSrc = '';
                if (item.imageUrl && item.imageUrl.startsWith('http')) {
                    imgSrc = item.imageUrl;
                } else if (item.imageUrl && item.imageUrl.trim() !== '') {
                    imgSrc = contextPath + '/uploads/' + item.imageUrl;
                } else {
                    imgSrc = 'https://via.placeholder.com/50?text=No+Img';
                }

                var imgTag = '<img src="' + imgSrc + '" class="rounded border" style="width: 50px; height: 50px; object-fit: cover;" onerror="this.onerror=null;this.src=\'https://via.placeholder.com/50?text=No+Img\';">';
                var cateName = item.category ? item.category.categoryname : '<span class="text-muted fst-italic">Chưa gán</span>';
                var formattedDate = item.createdAt ? new Date(item.createdAt).toLocaleDateString("vi-VN") : '';

                rows += '<tr>' +
                    '<td class="text-center fw-bold text-secondary">#' + item.productId + '</td>' +
                    '<td class="text-center">' + imgTag + '</td>' +
                    '<td class="fw-semibold text-dark">' + item.productName + '</td>' +
                    '<td class="text-danger fw-bold">' + Number(item.price).toLocaleString('vi-VN') + ' đ</td>' +
                    '<td><span class="badge bg-light text-dark border">' + cateName + '</span></td>' +
                    '<td class="text-center text-muted small">' + formattedDate + '</td>' +
                    '<td class="text-center">' +
                        '<button class="btn btn-outline-warning btn-sm me-1" onclick="openEditProductModal(' + item.productId + ')"><i class="bi bi-pencil-square"></i> Sửa</button>' +
                        '<button class="btn btn-outline-danger btn-sm" onclick="deleteProduct(' + item.productId + ')"><i class="bi bi-trash"></i> Xóa</button>' +
                    '</td>' +
                '</tr>';
            });
            $('#productTable tbody').html(rows);
        });
    }

    function openAddProductModal() {
        isProductEdit = false;
        $("#productForm")[0].reset();
        $("#productId").val("");
        $("#previewProductImg").html("");
        $("#productModalTitle").text("Thêm Mới Sản Phẩm");
        $("#productModal").modal("show");
    }

    function openEditProductModal(id) {
        isProductEdit = true;
        $("#productModalTitle").text("Cập Nhật Sản Phẩm");
        $.getJSON(contextPath + '/api/product/getProduct?id=' + id, function(res) {
            if (res.status) {
                var p = res.body;
                $("#productId").val(p.productId);
                $("#productName").val(p.productName);
                $("#price").val(p.price);
                $("#description").val(p.description);
                if (p.category) {
                    $("#categoryId").val(p.category.categoryId);
                }
                if (p.imageUrl && p.imageUrl.trim() !== '') {
                    var src = p.imageUrl.startsWith('http') ? p.imageUrl : (contextPath + '/uploads/' + p.imageUrl);
                    $("#previewProductImg").html('<img src="' + src + '" class="rounded border mt-2" style="width: 70px; height: 70px; object-fit: cover;" onerror="this.onerror=null;this.src=\'https://via.placeholder.com/70?text=No+Img\';">');
                } else {
                    $("#previewProductImg").html('');
                }
                $("#productModal").modal("show");
            }
        });
    }

    function deleteProduct(id) {
        if (confirm("Bạn có chắc chắn muốn xóa sản phẩm ID #" + id + "?")) {
            $.ajax({
                url: contextPath + '/api/product/deleteProduct?productId=' + id,
                type: 'DELETE',
                success: function(res) {
                    alert(res.message);
                    loadProducts();
                },
                error: function(xhr) {
                    alert("Lỗi khi xóa sản phẩm!");
                }
            });
        }
    }
</script>
</body>
</html>