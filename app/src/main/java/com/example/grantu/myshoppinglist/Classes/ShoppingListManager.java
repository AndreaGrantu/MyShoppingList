package com.example.grantu.myshoppinglist.Classes;

import android.content.Context;

import com.example.grantu.myshoppinglist.DBManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Grantu on 06/05/2017.
 */
public class ShoppingListManager {

    public static final int NO_ORDER = 0;
    public static final int UNSELECT_ORDER = NO_ORDER + 1;
    public static final int SELECT_ORDER = UNSELECT_ORDER + 1;
    public static final int PRICE_ORDER = SELECT_ORDER + 1;
    public static final int NAME_ORDER = PRICE_ORDER + 1;

    public static int SORT_MODE = NO_ORDER;


    private static ShoppingListManager sInstance;
    private ArrayList<ShoppingItem> mShopList;
    private DBManager mDb;

    public static ShoppingListManager getInstance(Context mContext) {

        if (sInstance == null) {
            sInstance = new ShoppingListManager(mContext);
        }
        return sInstance;
    }

    private ShoppingListManager(Context mContext) {
        mDb = DBManager.getInstance(mContext);
        mShopList = (ArrayList<ShoppingItem>) mDb.getAllShoppingItems();
    }

    private ArrayList<ShoppingItem> getShopList() {
        return mShopList;
    }

    private void addItem(ShoppingItem item) {
        mDb.insertProduct(item);
        refreshList();

    }

    private void updateItem(ShoppingItem item) {
        mDb.updateProduct(item);
        refreshList();
    }

    private void deleteItem(ShoppingItem item) {
        mDb.deleteProduct(item);
        refreshList();
    }

    private float getTotalPrice() {
        float mTotalPrice = 0;
        if (!mShopList.isEmpty()) {
            for (ShoppingItem s : mShopList) {
                if (s.isChecked() && !s.getPrice().isEmpty()) {
                    mTotalPrice += Float.parseFloat(s.getPrice());
                }
            }
        }
        return mTotalPrice;
    }

    private void deleteList() {
        for (ShoppingItem s : mShopList) {
            mDb.deleteProduct(s);
        }
        refreshList();
    }

    private void refreshList(){
        mShopList = (ArrayList<ShoppingItem>) mDb.getAllShoppingItems();
    }

    private ArrayList<ShoppingItem> sortList() {

        if (SORT_MODE == NO_ORDER ) {
            mShopList = (ArrayList<ShoppingItem>) mDb.getAllShoppingItems();
        } else {
            Collections.sort(mShopList, new ShopItemComparator());
        }

        return mShopList;
    }

}

class ShopItemComparator implements Comparator<ShoppingItem>{


    @Override
    public int compare(ShoppingItem t1, ShoppingItem t2) {

        switch(ShoppingListManager.SORT_MODE){
            case ShoppingListManager.PRICE_ORDER:
                if(t1.getPrice().isEmpty() && !t2.getPrice().isEmpty()){
                    return -1;
                } else if (t1.getPrice().isEmpty() && t2.getPrice().isEmpty()) {
                    return 0;
                } else if(!t1.getPrice().isEmpty() && t2.getPrice().isEmpty()){
                    return 1;
                } else {
                    float p1 = Float.parseFloat(t1.getPrice());
                    float p2 = Float.parseFloat(t2.getPrice());
                    if ( p1 > p2){
                        return 1;
                    } else if ( p1 < p2){
                        return -1;
                    } else {
                        return 0;
                    }
                }

            case ShoppingListManager.NAME_ORDER:
                return t1.getName().compareTo(t2.getName());

            case ShoppingListManager.SELECT_ORDER:
                if(t1.isChecked() && !t2.isChecked()){
                    return 1;
                } else if (!t1.isChecked() && t2.isChecked()){
                    return -1;
                } else {
                    return 0;
                }

            case ShoppingListManager.UNSELECT_ORDER:
                if(t1.isChecked() && !t2.isChecked()){
                    return -1;
                } else if (!t1.isChecked() && t2.isChecked()){
                    return 1;
                } else {
                    return 0;
                }

        }
        return 0;
    }
}
