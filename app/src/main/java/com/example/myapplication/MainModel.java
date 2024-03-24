package com.example.myapplication;

public class MainModel {
    String name,price,kurl;

    // สร้างคอนสตรักเตอร์ที่ไม่มีพารามิเตอร์
    MainModel(){


    }  // สร้างคอนสตรักเตอร์ที่มีพารามิเตอร์เพื่อกำหนดค่าตั้งต้นให้กับข้อมูล
    public MainModel(String name, String price, String type, String kurl) {
        this.name = name;
        this.price = price;
        this.kurl = kurl;
    }
    // เมธอดเรียกคืนค่าชื่อ
    public String getName() {
        return name;
    }
    // เมธอดกำหนดค่าชื่อ
    public void setName(String name) {
        this.name = name;
    }
    // เมธอดเรียกคืนราคา
    public String getPrice() {
        return price;
    }
    // เมธอดกำหนดค่าราคา
    public void getPrice(String price) {
        this.price = price;
    }
    // เมธอดเรียกคืน URL ของรูปภาพ
    public String getKurl() {
        return kurl;
    }
    // เมธอดกำหนดค่า URL ของรูปภาพ
    public void setKurl(String kurl) {
        this.kurl = kurl;
    }
}
