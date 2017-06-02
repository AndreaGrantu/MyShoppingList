package com.example.grantu.myshoppinglist.UI.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grantu.myshoppinglist.Classes.ShoppingItem;
import com.example.grantu.myshoppinglist.Classes.ShoppingListManager;
import com.example.grantu.myshoppinglist.R;
import com.example.grantu.myshoppinglist.UI.Dialog.CustomDialog;
import com.example.grantu.myshoppinglist.Utils.ShopHistoryParser;

import java.util.ArrayList;
import java.util.List;


public class ShoppingItemsFragment extends Fragment {

    public static final String FILTER ="NEW_LIST";
    private static boolean IS_SEARCH_MODE = false;
    public class ShoppingItemsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras()!= null){
                String s = intent.getStringExtra(REUSE_LIST);
                if(s != null){
                    List<ShoppingItem> list = ShopHistoryParser.getInstance().parseStringToList(s);
                    mSLManager.insertShopItemList((ArrayList<ShoppingItem>)list);
                    loadData();
                }
            }
        }
    }

    private static final String TAG = "ShoppingItemsFragment";
    public static final String REUSE_LIST="NEW_ELEMENT";

    private Menu menu;
    private Activity mActivity;
    private ShoppingListManager mSLManager;

    private ListView mShoppingListView;
    private ShoppingItemsReceiver receiver = new ShoppingItemsReceiver();


    public ShoppingItemsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping_items, container, false);
        mSLManager = ShoppingListManager.getInstance(mActivity);

        getViewReferences(v);
        loadData();

        return v;
    }

    private void loadData(){
        mShoppingListView.setAdapter(new ShoppingListAdapter(mActivity, mSLManager.getShopList()));
    }

    private void getViewReferences(View v) {
        mShoppingListView =(ListView)v.findViewById(R.id.shopping_list);
        mShoppingListView.setEmptyView(v.findViewById(R.id.emptylayout));
    }

    private void showDialog(int type,int id){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog2");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = CustomDialog.newInstance(mActivity.getApplicationContext(), type, id, this);
        newFragment.show(ft, "dialog2");


    }
    private void showDialog(int type){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = CustomDialog.newInstance(mActivity.getApplicationContext(),type,this);
        newFragment.show(ft, "dialog");

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        mActivity.registerReceiver(receiver, new IntentFilter(FILTER));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mActivity.unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_shoppinglist_fragment, menu);
        this.menu = menu;
        if(IS_SEARCH_MODE){
            setSearchMenu();
        } else {
            exitFromSearchMenuMode();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ArrayList<ShoppingItem> mItems = mSLManager.getShopList();

        switch (item.getItemId()){

            case R.id.action_add_item:
                showDialog(CustomDialog.ADD_ITEM);
                break;
            case R.id.action_save_list:
                if(mItems.isEmpty()){
                    Toast.makeText(mActivity,R.string.save_shopping_item_alert_empty,Toast.LENGTH_LONG).show();
                } else {
                    showDialog(CustomDialog.SAVE_LIST);
                }
                break;

            case R.id.action_delete_list:
                if(mItems.isEmpty()){
                    Toast.makeText(mActivity,R.string.delete_shopping_item_alert_empty,Toast.LENGTH_LONG).show();
                } else {
                    showDialog(CustomDialog.DELETE_LIST);
                }
                break;

            case R.id.action_unselect_list:
                if(!mItems.isEmpty()){
                    unselectAllItems();
                }
                break;
            case R.id.action_sort_list:
                if(!mItems.isEmpty()){
                    showDialog(CustomDialog.SORT_LIST);
                }
                break;
            case R.id.action_search_item:
                if(!mItems.isEmpty()){
                    showDialog(CustomDialog.SEARCH_ITEM);
                }
            case R.id.action_back:
                exitFromSearchMode();
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void refresh(){
        loadData();
    }

    public void orderList(){
        loadData();
    }


    private void unselectAllItems(){
        mSLManager.unselectShopItemList();
        loadData();
    }

    public void deleteList(){
        mSLManager.deleteShopItemList();
        loadData();
    }


    public List<ShoppingItem> getListItem(){
        return mSLManager.getShopList();
    }

    public void searchItem(String itemToSearch){
        IS_SEARCH_MODE = true;
        setSearchMode(itemToSearch);
    }

    private void setSearchMode(String s) {
        ArrayList<ShoppingItem> items = (ArrayList<ShoppingItem>) mSLManager.searchItem(s);
        if(items.isEmpty()){
            View v = mShoppingListView.getEmptyView();
            TextView text = (TextView) v.findViewById(R.id.empty_text);
            text.setText(getString(R.string.shop_empty_search));
        }
        mShoppingListView.setAdapter(new ShoppingListAdapter(mActivity,items));
        setSearchMenu();
    }

    private void setSearchMenu(){
        menu.findItem(R.id.action_add_item).setVisible(false);
        menu.findItem(R.id.action_search_item).setVisible(false);
        menu.findItem(R.id.action_save_list).setVisible(false);
        menu.findItem(R.id.action_unselect_list).setVisible(false);
        menu.findItem(R.id.action_delete_list).setVisible(false);
        menu.findItem(R.id.action_sort_list).setVisible(false);
        menu.findItem(R.id.action_back).setVisible(true);
    }

    private void exitFromSearchMenuMode(){
        menu.findItem(R.id.action_add_item).setVisible(true);
        menu.findItem(R.id.action_search_item).setVisible(true);
        menu.findItem(R.id.action_save_list).setVisible(true);
        menu.findItem(R.id.action_unselect_list).setVisible(true);
        menu.findItem(R.id.action_delete_list).setVisible(true);
        menu.findItem(R.id.action_sort_list).setVisible(true);
        menu.findItem(R.id.action_back).setVisible(false);
    }
    private void exitFromSearchMode(){
        IS_SEARCH_MODE = false;
        mShoppingListView.setAdapter(new ShoppingListAdapter(mActivity, mSLManager.getShopList()));
        exitFromSearchMenuMode();
    }
    public void notifyToHistoryFragment() {
        Intent intent = new Intent(HistoryListsFragment.FILTER);
        intent.putExtra(HistoryListsFragment.UPDATE,true);
        mActivity.sendBroadcast(intent);
        //delete all my list
        deleteList();
    }

    /**
     * adapter
     */

    private class ShoppingListAdapter extends BaseAdapter{

        Context mContext;
        ArrayList<ShoppingItem> itemsList;
        LayoutInflater layoutInflater;

        public ShoppingListAdapter(Activity activity,List<ShoppingItem> list){
            layoutInflater = getActivity().getLayoutInflater();
            mContext = getActivity().getApplicationContext();
            itemsList = (ArrayList<ShoppingItem>) list;
        }

        @Override
        public int getCount() {
            return itemsList.size();
        }

        @Override
        public ShoppingItem getItem(int i) {
            return itemsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {

            ShoppingItemHolder holder;
            if(view == null){
                view = layoutInflater.inflate(R.layout.shopping_list_item, viewGroup, false);
                holder = new ShoppingItemHolder(view);
                view.setTag(holder);
            } else {
                holder = (ShoppingItemHolder)view.getTag();
            }

            holder.setView(itemsList.get(pos));

            return view;
        }
    }


    /**
     * PlaceHolder
     */


    private class ShoppingItemHolder implements View.OnClickListener {

        ShoppingItem item;
        TextView item_name;
        TextView item_notes;
        CheckBox checkBox;
        ImageView deleteImage;
        LinearLayout background;

        public ShoppingItemHolder(View v){
            background = (LinearLayout)v.findViewById(R.id.linear_layout_list_item);

            LinearLayout ll = (LinearLayout)background.findViewById(R.id.item_layout);
                item_name = (TextView)ll.findViewById(R.id.product_name);
                item_notes = (TextView)ll.findViewById(R.id.product_notes);

            checkBox = (CheckBox)background.findViewById(R.id.item_checkbox);
            deleteImage = (ImageView)background.findViewById(R.id.delete_item);

            ll.setOnClickListener(this);
            checkBox.setOnClickListener(this);
            deleteImage.setOnClickListener(this);

        }

        public void setView(ShoppingItem s){
            item = s;
            item_name.setText(s.getName().toUpperCase());
            if(!s.getNotes().isEmpty()) {
                item_notes.setText(s.getNotes());
            } else {
                item_notes.setVisibility(View.GONE);
            }

            checkBox.setChecked(s.isChecked());
            if(s.isChecked()){
                background.setBackgroundResource(R.color.colorPrimaryLight);
            } else {
                background.setBackgroundResource(Color.TRANSPARENT);
            }

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.item_layout:
                    showDialog(CustomDialog.UPDATE_ITEM,item.getId());
                    break;
                case R.id.delete_item:
                    mSLManager.deleteShopListItem(item);
                    loadData();
                    break;
                case R.id.item_checkbox:
                    item.setIsChecked(!item.isChecked());
                    if(item.isChecked()){
                        background.setBackgroundResource(R.color.colorPrimaryLight);
                    } else {
                        background.setBackgroundResource(Color.TRANSPARENT);
                    }
                    mSLManager.updateShopListItem(item);
                    loadData();
                    break;

            }
        }
    }

}
