package com.example.grantu.myshoppinglist.Classes;

/**
 * Created by Grantu on 10/12/2016.
 */
public class ShoppingHistoryItem {

    private int id;
    private String date;
    private String content;
    private String name;
    private String notes;
    private String price;

    public ShoppingHistoryItem(){}

    public ShoppingHistoryItem(ShoppingHistoryItem s){
        this.name = s.getName();
        this.id = s.getId();
        this.date = s.getDate();
        this.content = s.getContent();
    }

    public ShoppingHistoryItem(String date, String tot_price, String content) {
        this.id = -1;
        this.date = date;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
