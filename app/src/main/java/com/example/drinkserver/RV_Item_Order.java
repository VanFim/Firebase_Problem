package com.example.drinkserver;

public class RV_Item_Order {

    private String name;
    private String commentText;

    private long orderDateMilliSeconds;
    private String orderedDateFormatted;
    private String orderOtherInformation;
    private int tableNumber;
    private String orderStatus;
    private int quantity;
    private int orderID;
    private boolean currentlySelected = false;




    public RV_Item_Order(String name, String commentText, long orderDateMilliSeconds, String orderedDateFormatted, String orderOtherInformation, int tableNumber, String orderStatus, int quantity, int orderID) {
        this.name = name;
        this.commentText = commentText;
        this.orderDateMilliSeconds = orderDateMilliSeconds;
        this.orderedDateFormatted = orderedDateFormatted;
        this.orderOtherInformation = orderOtherInformation;
        this.tableNumber = tableNumber;
        this.orderStatus = orderStatus;
        this.quantity = quantity;
        this.orderID = orderID;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }


    public long getOrderDateMilliSeconds() {
        return orderDateMilliSeconds;
    }

    public void setOrderDateMilliSeconds(long orderDateMilliSeconds) {
        this.orderDateMilliSeconds = orderDateMilliSeconds;
    }

    public String getOrderedDateFormatted() {
        return orderedDateFormatted;
    }

    public void setOrderedDateFormatted(String orderedDateFormatted) {
        this.orderedDateFormatted = orderedDateFormatted;
    }

    public String getOrderOtherInformation() {
        return orderOtherInformation;
    }

    public void setOrderOtherInformation(String orderOtherInformation) {
        this.orderOtherInformation = orderOtherInformation;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public boolean isCurrentlySelected() {
        return currentlySelected;
    }

    public void setCurrentlySelected(boolean currentlySelected) {
        this.currentlySelected = currentlySelected;
    }


}