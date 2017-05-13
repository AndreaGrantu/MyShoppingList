package com.example.grantu.myshoppinglist.UI.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grantu.myshoppinglist.Classes.ShoppingHistoryItem;
import com.example.grantu.myshoppinglist.DBManager;
import com.example.grantu.myshoppinglist.R;
import com.example.grantu.myshoppinglist.UI.Dialog.CustomDialog;

import java.util.List;

public class HistoryListsFragment extends Fragment{

    public static final String UPDATE ="UPDATE";
    public static final String FILTER ="NEW_ELEMENT";
    public static final String REUSE_LIST="NEW_ELEMENT";

    public void setList(String s) {
        Intent intent = new Intent(ShoppingItemsFragment.FILTER);
        intent.putExtra(ShoppingItemsFragment.REUSE_LIST,s);
        mActivity.sendBroadcast(intent);
        Toast.makeText(mActivity,R.string.dialog_history_list_details_reuse_success,Toast.LENGTH_LONG).show();
    }


    public class HistoryReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras()!= null){
                if(intent.getBooleanExtra(UPDATE,false)){
                    loadData();
                }
            }
        }
    }


    private Activity mActivity;
    private ListView mHistoryList;
    private List<ShoppingHistoryItem> mHistoryItems;
    DBManager mDb;
    private HistoryReceiver localReceiver = new HistoryReceiver();

    public HistoryListsFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.registerReceiver(localReceiver, new IntentFilter(FILTER));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(localReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history_lists, container, false);
        mDb = DBManager.getInstance(mActivity);

        getViewReferences(v);

        loadData();

        return v;
    }

    private void loadData() {
        mHistoryItems = mDb.getAllShoppingHistory();
        if(mHistoryItems != null) {
            mHistoryList.setAdapter(new HistoryListAdapter(mActivity, mHistoryItems));
        }

    }

    private void getViewReferences(View v) {
        mHistoryList = (ListView)v.findViewById(R.id.history_shopping_list);
        mHistoryList.setEmptyView(v.findViewById(R.id.emptylayout));
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_shopping_history_fragment, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_history_list:
                if(!mHistoryItems.isEmpty()) {
                    showDialog(CustomDialog.DELETE_HISTORY_LIST);
                } else {
                    Toast.makeText(mActivity,R.string.delete_shopping_history_dialog_empty,Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void showDialog(int type,int id){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog2");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = CustomDialog.newInstance(mActivity.getApplicationContext(),type,id,this);
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

    public void deleteList() {
        for(ShoppingHistoryItem s : mHistoryItems){
            mDb.deleteHistoryList(s.getId());
        }
        loadData();
    }



    private class HistoryListAdapter extends BaseAdapter{

        List<ShoppingHistoryItem> mList;
        Context mContext;
        LayoutInflater layoutInflater;

        public HistoryListAdapter(Activity activity,List<ShoppingHistoryItem> list){
            mContext = activity.getApplicationContext();
            layoutInflater = activity.getLayoutInflater();
            mList = list;

        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(0);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            HistoryListPlaceholder holder;
            if(view == null){
                view = layoutInflater.inflate(R.layout.fragment_history_item, viewGroup, false);
                holder = new HistoryListPlaceholder(view);
                view.setTag(holder);
            } else {
                holder = (HistoryListPlaceholder)view.getTag();
            }

            holder.setView(mList.get(i));

            return view;
        }
    }

    private class HistoryListPlaceholder implements View.OnClickListener{

        TextView name;
        TextView notes;
        TextView date;
        ImageView delete;
        LinearLayout ll;
        ShoppingHistoryItem item;

        public HistoryListPlaceholder(View v){

            ll = (LinearLayout)v.findViewById(R.id.history_item_layout);
            name = (TextView)v.findViewById(R.id.history_item_name);
            notes = (TextView)v.findViewById(R.id.history_item_notes);
            date = (TextView)v.findViewById(R.id.history_item_date);
            delete = (ImageView)v.findViewById(R.id.history_item_delete);

            ll.setOnClickListener(this);
            delete.setOnClickListener(this);

        }

        public void setView(ShoppingHistoryItem s){

            item = s;
            name.setText(s.getName().toUpperCase());

            if(!s.getNotes().isEmpty()) {
                notes.setText(s.getNotes());
            } else {
                notes.setVisibility(View.GONE);
            }

            if(!s.getNotes().isEmpty()) {
                date.setText(s.getDate());
            } else {
                date.setVisibility(View.GONE);
            }


        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.history_item_layout:
                    showDialog(CustomDialog.HISTORY_LIST_ITEM,item.getId());
                    break;
                case R.id.history_item_delete:
                    DBManager.getInstance(mActivity).deleteHistoryList(item.getId());
                    loadData();
                    break;

            }
        }
    }

}
