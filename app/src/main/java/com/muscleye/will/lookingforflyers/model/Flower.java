package com.muscleye.will.lookingforflyers.model;

import android.graphics.Bitmap;

/**
 * Created by Will on 15-04-02.
 */
public class Flower
{
    private int productId;
    private String name;
    private String category;
    private String instruction;
    private double price;
    private String photo;
    private Bitmap bigmap;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Bitmap getBigmap() {
        return bigmap;
    }

    public void setBigmap(Bitmap bigmap) {
        this.bigmap = bigmap;
    }
}
