package com.example.grantu.myshoppinglist.Classes;

/**
 * Created by Grantu on 10/12/2016.
 */
public class ShoppingItem {

    private int id;
    private String name;
    private String notes;
    private boolean isChecked;

    public ShoppingItem(){};

    public ShoppingItem(ShoppingItem s){
        this.id = s.getId();
        this.name = s.getName();
        this.notes = s.getNotes();
        this.isChecked = s.isChecked();
    }

    public ShoppingItem(String name, String notes, boolean isChecked){
        this.name = name;
        this.notes = notes;
        this.isChecked = isChecked;
        id = -1;
    }

    public ShoppingItem(int ids,String name, String notes, boolean isChecked){
        this.name = name;
        this.notes = notes;
        this.isChecked = isChecked;
        this.id = ids;
    }

    public  int getId(){
        return this.id;
    }

    public void setId(int i){
        this.id = i;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(String n){
        this.notes = n;
    }

    public String getNotes(){
        return this.notes;
    }
}
