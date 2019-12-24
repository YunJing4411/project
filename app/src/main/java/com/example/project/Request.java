package com.example.project;

import java.util.List;

public class Request {

    private String Email;
    private String Name;
    private String address;
    private String total;
    private List<Order> Pruduct;

    public Request() {

    }
    public Request(String email, String name, String address, String total, List<Order> Pruduct) {
        Email = email;
        Name = name;
        this.address = address;
        this.total = total;
        this.Pruduct = Pruduct;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getPruduct() {
        return Pruduct;
    }

    public void setPruduct(List<Order> Pruduct) {
        this.Pruduct = Pruduct;
    }

}
