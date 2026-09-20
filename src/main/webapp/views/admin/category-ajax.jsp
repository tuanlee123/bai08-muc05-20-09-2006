<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Danh Mục (AJAX) - Admin</title>
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
            <li><a href="<c:url value='/admin/categories-ajax'/>" class="nav-link active"><i class="bi bi-folder2-open me-2"></i> Danh mục (AJAX)</a></li>
            <li><a href="<c:url value='/admin/products'/>" class="nav-link"><i class="bi bi-box-seam me-2"></i> Sản phẩm (Thường)</a></li>
            <li><a href="<c:url value='/admin/products-ajax'/>" class="nav-link"><i class="bi bi-boxes me-2"></i> Sản phẩm (AJAX)</a></li>
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
                <h3 class="fw-bold text-dark mb-0">Quản Lý Danh Mục (AJAX API)</h3>
                <small class="text-muted">Dữ liệu CRUD bất đồng bộ trực tiếp qua RESTful Service</small>
            </div>
            <button type="button" class="btn btn-primary fw-semibold shadow-sm" onclick="openAddCategoryModal()">
                <i class="bi bi-plus-lg me-1"></i> Thêm Mới Danh Mục
            </button>
        </div>

        <!-- Bảng Danh Sách Danh Mục -->
        <div class="card shadow-sm border-0">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover table-striped align-middle mb-0" id="categoryTable">
                        <thead class="table-dark">
                            <tr>
                                <th class="text-center" style="width: 80px;">#ID</th>
                                <th style="width: 100px;" class="text-center">Hình ảnh</th>
                                <th>Tên danh mục</th>
                                <th class="text-center" style="width: 140px;">Trạng thái</th>
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

<!-- Modal Thêm / Sửa Danh Mục -->
<div class="modal fade" id="categoryModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <form id="categoryForm" enctype="multipart/form-data">
                <div class="modal-header bg-dark text-white">
                    <h5 class="modal-title fw-bold" id="categoryModalTitle">Thêm Danh Mục</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body p-4">
                    <input type="hidden" id="categoryId" name="categoryId">

                    <div class="mb-3">
                        <label class="form-label fw-bold">Tên danh mục <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="categoryname" name="categoryname" placeholder="Nhập tên loại sản phẩm..." required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Trạng thái</label>
                        <select class="form-select" id="status" name="status">
                            <option value="1">Hoạt động</option>
                            <option value="0">Đã ẩn</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Hình ảnh đại diện</label>
                        <input type="file" class="form-control" id="images" name="images" accept="image/*">
                        <div id="previewCategoryImg" class="mt-2 text-center"></div>
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
    var isCategoryEdit = false;

    $(document).ready(function() {
        loadCategories();

        $("#categoryForm").submit(function(e) {
            e.preventDefault();
            var formData = new FormData(this);
            var apiUrl = isCategoryEdit ? (contextPath + '/api/category/updateCategory') : (contextPath + '/api/category/addCategory');
            var httpMethod = isCategoryEdit ? 'PUT' : 'POST';

            $.ajax({
                url: apiUrl,
                type: httpMethod,
                data: formData,
                processData: false,
                contentType: false,
                success: function(res) {
                    alert(res.message);
                    $("#categoryModal").modal('hide');
                    loadCategories();
                },
                error: function(xhr) {
                    var msg = xhr.responseJSON ? xhr.responseJSON.message : "Đã có lỗi xảy ra!";
                    alert(msg);
                }
            });
        });
    });

    function loadCategories() {
        $.getJSON(contextPath + '/api/category', function(data) {
            var html = '';
            data.forEach(function(item) {
                var imgSrc = '';
                if (item.images && item.images.startsWith('http')) {
                    imgSrc = item.images;
                } else if (item.images && item.images.trim() !== '') {
                    imgSrc = contextPath + '/uploads/' + item.images;
                } else {
                    imgSrc = 'https://via.placeholder.com/50?text=No+Img';
                }

                var imgTag = '<img src="' + imgSrc + '" class="rounded border" style="width: 50px; height: 50px; object-fit: cover;" onerror="this.onerror=null;this.src=\'https://via.placeholder.com/50?text=No+Img\';">';

                var statusBadge = item.status === 1 
                    ? '<span class="badge bg-success-subtle text-success border border-success-subtle px-2 py-1">Hoạt động</span>' 
                    : '<span class="badge bg-danger-subtle text-danger border border-danger-subtle px-2 py-1">Đã ẩn</span>';

                html += '<tr>' +
                    '<td class="text-center fw-bold text-secondary">#' + item.categoryId + '</td>' +
                    '<td class="text-center">' + imgTag + '</td>' +
                    '<td class="fw-semibold text-dark">' + item.categoryname + '</td>' +
                    '<td class="text-center">' + statusBadge + '</td>' +
                    '<td class="text-center">' +
                        '<button class="btn btn-outline-warning btn-sm me-1" onclick="openEditCategoryModal(' + item.categoryId + ')"><i class="bi bi-pencil-square"></i> Sửa</button>' +
                        '<button class="btn btn-outline-danger btn-sm" onclick="deleteCategory(' + item.categoryId + ')"><i class="bi bi-trash"></i> Xóa</button>' +
                    '</td>' +
                '</tr>';
            });
            $('#categoryTable tbody').html(html);
        });
    }

    function openAddCategoryModal() {
        isCategoryEdit = false;
        $("#categoryForm")[0].reset();
        $("#categoryId").val("");
        $("#previewCategoryImg").html("");
        $("#categoryModalTitle").text("Thêm Mới Danh Mục");
        $("#categoryModal").modal("show");
    }

    function openEditCategoryModal(id) {
        isCategoryEdit = true;
        $("#categoryModalTitle").text("Cập Nhật Danh Mục");
        $.getJSON(contextPath + '/api/category/getCategory?id=' + id, function(res) {
            if (res.status) {
                var c = res.body;
                $("#categoryId").val(c.categoryId);
                $("#categoryname").val(c.categoryname);
                $("#status").val(c.status);
                if (c.images && c.images.trim() !== '') {
                    var src = c.images.startsWith('http') ? c.images : (contextPath + '/uploads/' + c.images);
                    $("#previewCategoryImg").html('<img src="' + src + '" class="rounded border mt-2" style="width: 70px; height: 70px; object-fit: cover;" onerror="this.onerror=null;this.src=\'https://via.placeholder.com/70?text=No+Img\';">');
                } else {
                    $("#previewCategoryImg").html('');
                }
                $("#categoryModal").modal("show");
            }
        });
    }

    function deleteCategory(id) {
        if (confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
            $.ajax({
                url: contextPath + '/api/category/deleteCategory?categoryId=' + id,
                type: 'DELETE',
                success: function(res) {
                    alert(res.message);
                    loadCategories();
                },
                error: function(xhr) {
                    alert("Không thể xóa (danh mục có thể đang chứa sản phẩm)!");
                }
            });
        }
    }
</script>
</body>
</html>