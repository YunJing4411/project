package com.example.project;

public class goodsinfo {
    private  String name;
    private  String price;
    private  String description;
    private  String pic;



    public goodsinfo() {
    }

    public goodsinfo(String name, String price, String description, String pic){
        this.name = name;
        this.price = price;
        this.description = description;
        this.pic=pic;
    }
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
    public String getPic() {
        return pic;
    }
}
