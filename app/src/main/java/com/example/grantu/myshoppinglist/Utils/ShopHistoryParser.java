package com.example.grantu.myshoppinglist.Utils;

import com.example.grantu.myshoppinglist.Classes.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grantu on 23/12/2016.
 */
public class ShopHistoryParser {

    private static final String INTRA_ITEM_DIVISOR = "&";
    private static final String INTER_ITEM_DIVISOR = "_";

    private static ShopHistoryParser sInstance;

    public static ShopHistoryParser getInstance(){
        if(null == sInstance){
            sInstance = new ShopHistoryParser();
        }

        return sInstance;
    }


    public String parseListToString(List<ShoppingItem> items){

        StringBuffer output= new StringBuffer();
        for(ShoppingItem s : items){
            if(s.isChecked()) {
                output.append(s.getName());
                output.append(INTRA_ITEM_DIVISOR);
                output.append(s.getNotes().isEmpty() ? " " : s.getNotes());
                //end element
                output.append(INTER_ITEM_DIVISOR);
            }
        }

        return  output.toString();
    }

    public List<ShoppingItem> parseStringToList(String input){

        List<ShoppingItem> list= new ArrayList<>();

        String[] items = input.split(INTER_ITEM_DIVISOR);

        for(String s : items){
            ShoppingItem si = new ShoppingItem();
            String[] field = s.split(INTRA_ITEM_DIVISOR);
            si.setName((field[0].equals(" "))? "" : field[0] );
            si.setNotes((field[1].equals(" "))? "" : field[1] );
            si.setIsChecked(false);
            list.add(si);
        }

        return  list;
    }


}
