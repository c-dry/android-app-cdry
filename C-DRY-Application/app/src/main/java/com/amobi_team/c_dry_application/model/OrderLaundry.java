package com.amobi_team.c_dry_application.model;

/**
 * Created by D on 11-May-17.
 */

public class OrderLaundry {
    private String id_order;
    private String email;
    private String address;
    private String weight;
    private String price;
    private String date_order;
    private String date_end;
    private String status;

    public OrderLaundry(){}

    public String getId_order() {
        return id_order;
    }

    public void setId_order(String id_order) {
        this.id_order = id_order;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return

                "email : " + email + "\n" +
                "address : " + address + "\n" +
                "weight : " + weight + "\n" +
                "price : " + price + "\n" +
                "Order : " + date_order +"\n" +
                "Finish : " + date_end + "\n" +
                "status : " + status +"\n";
    }
}
