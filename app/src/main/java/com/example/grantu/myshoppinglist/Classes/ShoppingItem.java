package com.example.grantu.myshoppinglist.Classes;

/**
 * Created by Grantu on 10/12/2016.
 */
public class ShoppingItem {

    private int id;
    private String name;
    private String amount;
    private String  price;
    private boolean isChecked;

    public ShoppingItem(){};

    public ShoppingItem(ShoppingItem s){
        this.id = s.getId();
        this.name = s.getName();
        this.amount = s.getAmount();
        this.price = s.getPrice();
        this.isChecked = s.isChecked();
    }

    public ShoppingItem(String name, String amount, String price, boolean isChecked){
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.isChecked = isChecked;
        id = -1;
    }

    public ShoppingItem(int ids,String name, String amount, String price, boolean isChecked){
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.isChecked = isChecked;
        this.id = ids;
    }

    public  int getId(){
        return this.id;
    }

    public void setId(int i){
        this.id = i;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
