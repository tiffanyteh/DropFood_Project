package com.example.tiffany.dropfood_project;

public class Order {
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price1;
    private String Discount1;

    public Order(){

    }

    public Order(String productId, String productName, String quantity, String price1, String discount1) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price1 = price1;
        Discount1 = discount1;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice1() {
        return Price1;
    }

    public void setPrice1(String price1) {
        Price1 = price1;
    }

    public String getDiscount1() {
        return Discount1;
    }

    public void setDiscount1(String discount1) {
        Discount1 = discount1;
    }
}
