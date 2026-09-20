package vn.iotstar.dto;

import vn.iotstar.model.Product;
import java.util.List;

public class ProductPage {
    private List<Product> content;
    private int totalPages;
    private int totalElements;
    private int currentPage;

    public ProductPage() {}

    public ProductPage(List<Product> content, int totalPages, int totalElements, int currentPage) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.currentPage = currentPage;
    }

    public List<Product> getContent() { return content; }
    public void setContent(List<Product> content) { this.content = content; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getTotalElements() { return totalElements; }
    public void setTotalElements(int totalElements) { this.totalElements = totalElements; }
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
}