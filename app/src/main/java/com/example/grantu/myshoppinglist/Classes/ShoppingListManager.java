package com.example.grantu.myshoppinglist.Classes;

import android.content.Context;

import com.example.grantu.myshoppinglist.DBManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Grantu on 06/05/2017.
 */
public class ShoppingListManager {

    public static final int NO_ORDER = 0;
    public static final int UNSELECT_ORDER = NO_ORDER + 1;
    public static final int SELECT_ORDER = UNSELECT_ORDER + 1;
    public static final int NAME_ORDER = SELECT_ORDER + 1;

    public static int SORT_MODE;


    private static ShoppingListManager sInstance;
    private DBManager mDb;

    public static ShoppingListManager getInstance(Context mContext) {

        if (sInstance == null) {
            sInstance = new ShoppingListManager(mContext);
        }
        return sInstance;
    }

    private ShoppingListManager(Context mContext) {
        mDb = DBManager.getInstance(mContext);
}

    /**
     *  Shopping Item List methods
     *
     */
    public ArrayList<ShoppingItem> getShopList() {
        ArrayList<ShoppingItem> list = sortShopItemList();
        if (list == null) {
            list = new ArrayList<ShoppingItem>();
        }

        return list;
    }

    public boolean addShopListItem(ShoppingItem item) {
        return mDb.insertProduct(item);
    }

    public boolean updateShopListItem(ShoppingItem item) {
        return mDb.updateProduct(item);
    }

    public void deleteShopListItem(ShoppingItem item) {
        mDb.deleteProduct(item);
    }


    public void deleteShopItemList() {
        ArrayList<ShoppingItem> list = (ArrayList<ShoppingItem>) mDb.getAllShoppingItems();
        for (ShoppingItem s : list) {
            mDb.deleteProduct(s);
        }
    }


    public ArrayList<ShoppingItem> sortShopItemList() {
        ArrayList<ShoppingItem> list = (ArrayList < ShoppingItem >) mDb.getAllShoppingItems();
        if (SORT_MODE == NO_ORDER ) {
            return list;
        }

        Collections.sort(list, new ShopItemComparator());
        return list;
    }

    public void unselectShopItemList(){
        ArrayList<ShoppingItem> list = (ArrayList < ShoppingItem >) mDb.getAllShoppingItems();
        for(ShoppingItem s : list){
            if(s.isChecked()){
                s.setIsChecked(false);
                mDb.updateProduct(s);
            }

        }
    }

    public void insertShopItemList(ArrayList<ShoppingItem> list){
        for(ShoppingItem sp : list){
            mDb.insertProduct(sp);
        }
    }

    public ShoppingItem getShopItemById(int id){
        return mDb.getShopItem(id);
    }


    public boolean saveShopHistoryItem( ShoppingHistoryItem s){
        return mDb.insertHistoryList(s);
    }

    public List<ShoppingItem> searchItem(String name) {
        ArrayList<ShoppingItem> list = new ArrayList<>();
        if(!name.isEmpty()) {
            for (ShoppingItem s : getShopList()) {
                if (s.getName().startsWith(name)) {
                    list.add(s);
                }
            }
        }
        return list;
    }

    /**
     *  Shopping History Item List methods
     *
     */

    public ShoppingHistoryItem getShoppingHistoryItem(int id){
        return mDb.getHistoryItem(id);
    }



}

class ShopItemComparator implements Comparator<ShoppingItem>{


    @Override
    public int compare(ShoppingItem t1, ShoppingItem t2) {

        switch(ShoppingListManager.SORT_MODE){

            case ShoppingListManager.NAME_ORDER:
                return t1.getName().compareTo(t2.getName());

            case ShoppingListManager.SELECT_ORDER:
                if(t1.isChecked() && !t2.isChecked()){
                    return -1;
                } else if (!t1.isChecked() && t2.isChecked()){
                    return 1;
                } else {
                    return 0;
                }

            case ShoppingListManager.UNSELECT_ORDER:
                if(t1.isChecked() && !t2.isChecked()){
                    return 1;
                } else if (!t1.isChecked() && t2.isChecked()){
                    return -1;
                } else {
                    return 0;
                }

        }
        return 0;
    }
}
