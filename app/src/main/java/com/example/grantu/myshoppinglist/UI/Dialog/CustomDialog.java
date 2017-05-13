package com.example.grantu.myshoppinglist.UI.Dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grantu.myshoppinglist.Classes.ShoppingHistoryItem;
import com.example.grantu.myshoppinglist.Classes.ShoppingItem;
import com.example.grantu.myshoppinglist.Classes.ShoppingListManager;
import com.example.grantu.myshoppinglist.DBManager;
import com.example.grantu.myshoppinglist.R;
import com.example.grantu.myshoppinglist.UI.Fragments.HistoryListsFragment;
import com.example.grantu.myshoppinglist.UI.Fragments.ShoppingItemsFragment;
import com.example.grantu.myshoppinglist.Utils.ShopHistoryParser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Grantu on 17/12/2016.
 */
public class CustomDialog extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {



    public final static  String TYPE = "TYPE";
    public final static  String ITEM_ID = "ITEM_ID";
    public final static  String FORMAT_DATE = "\"dd-MMM-yyyy\"";


    public final static int ADD_ITEM = 0;
    public final static int DELETE_LIST = ADD_ITEM+1;
    public final static int SAVE_LIST = DELETE_LIST+1;
    public final static int UPDATE_ITEM = SAVE_LIST+1;
    public final static int DELETE_HISTORY_LIST = UPDATE_ITEM+1;
    public final static int HISTORY_LIST_ITEM = DELETE_HISTORY_LIST+1;
    public final static int SORT_LIST = HISTORY_LIST_ITEM+1;





    private Context mContext;
    private int mType;
    private int mId;
    private static Fragment previousFragment;
    private String itemToSend;

    private TextView dialogContent;
    private TextView dialogTitle;
    private TextView positiveButton;
    private TextView negativeButton;
    private ListView listView;
    private RadioGroup radioGroup;

    private EditText nameEdit;
    private EditText priceEdit;
    private EditText amountEdit;



    public CustomDialog(Context context){
        mContext = context;
    }



    public static CustomDialog newInstance(Context context,int type,Fragment f){
        previousFragment = f;
        Bundle b = new Bundle();
        b.putInt(TYPE, type);
        CustomDialog dialog = new CustomDialog(context);
        dialog.setArguments(b);
        return dialog;
    }
    public static CustomDialog newInstance(Context context,int type,int id, Fragment f){
        previousFragment = f;
        Bundle b = new Bundle();
        b.putInt(TYPE, type);
        b.putInt(ITEM_ID, id);
        CustomDialog dialog = new CustomDialog(context);
        dialog.setArguments(b);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(TYPE);
        if(mType == UPDATE_ITEM || mType == HISTORY_LIST_ITEM){
            mId = getArguments().getInt(ITEM_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = null;
        switch (mType){
            case UPDATE_ITEM:
            case ADD_ITEM:
                v = inflater.inflate(R.layout.dialog_add_shopping_item, container, false);
                dialogContent = (TextView)v.findViewById(R.id.dialog_content);
                dialogTitle = (TextView)v.findViewById(R.id.dialog_title);
                positiveButton = (TextView)v.findViewById(R.id.positive_btn);
                negativeButton = (TextView)v.findViewById(R.id.negative_btn);

                nameEdit = (EditText)v.findViewById(R.id.name_edit);
                priceEdit = (EditText)v.findViewById(R.id.price_edit);
                amountEdit = (EditText)v.findViewById(R.id.amount_edit);

                positiveButton.setOnClickListener(this);
                negativeButton.setOnClickListener(this);

                if(mType == UPDATE_ITEM){
                    ShoppingItem s = DBManager.getInstance(mContext).getShopItem(mId);
                    dialogTitle.setText(R.string.modify_shopping_item_dialog_title);
                    nameEdit.setText(s.getName());
                    priceEdit.setText(s.getPrice());
                    amountEdit.setText(s.getAmount());
                }
            break;
            case DELETE_LIST:
                v = inflater.inflate(R.layout.dialog_delete_shopping_list, container, false);
                dialogContent = (TextView)v.findViewById(R.id.dialog_content);
                dialogTitle = (TextView)v.findViewById(R.id.dialog_title);
                positiveButton = (TextView)v.findViewById(R.id.positive_btn);
                negativeButton = (TextView)v.findViewById(R.id.negative_btn);

                positiveButton.setOnClickListener(this);
                negativeButton.setOnClickListener(this);
                break;
            case SAVE_LIST:
                v = inflater.inflate(R.layout.dialog_save_shopping_list, container, false);
                dialogContent = (TextView)v.findViewById(R.id.dialog_content);
                dialogTitle = (TextView)v.findViewById(R.id.dialog_title);
                positiveButton = (TextView)v.findViewById(R.id.positive_btn);
                negativeButton = (TextView)v.findViewById(R.id.negative_btn);
                nameEdit = (EditText)v.findViewById(R.id.name_edit);
                amountEdit = (EditText)v.findViewById(R.id.amount_edit);

                positiveButton.setOnClickListener(this);
                negativeButton.setOnClickListener(this);
                break;

            case DELETE_HISTORY_LIST:
                v = inflater.inflate(R.layout.dialog_delete_history_list, container, false);
                dialogContent = (TextView)v.findViewById(R.id.dialog_content);
                dialogTitle = (TextView)v.findViewById(R.id.dialog_title);
                positiveButton = (TextView)v.findViewById(R.id.positive_btn);
                negativeButton = (TextView)v.findViewById(R.id.negative_btn);

                positiveButton.setOnClickListener(this);
                negativeButton.setOnClickListener(this);
                break;

            case HISTORY_LIST_ITEM:
                v = inflater.inflate(R.layout.dialog_history_item_details, container, false);
                dialogContent = (TextView)v.findViewById(R.id.dialog_content);
                dialogTitle = (TextView)v.findViewById(R.id.dialog_title);
                positiveButton = (TextView)v.findViewById(R.id.positive_btn);
                negativeButton = (TextView)v.findViewById(R.id.negative_btn);
                listView = (ListView)v.findViewById(R.id.history_details);
                positiveButton.setOnClickListener(this);
                negativeButton.setOnClickListener(this);

                ShoppingHistoryItem s = DBManager.getInstance(mContext).getHistoryItem(mId);
                dialogTitle.setText(s.getName()+" - "+s.getDate()+ " - "+s.getTot_price());
                dialogContent.setText(s.getNotes());
                itemToSend = s.getContent();
                List<ShoppingItem> list = ShopHistoryParser.getInstance().parseStringToList(s.getContent());
                listView.setAdapter(new HistoryItemDetailAdapter(getActivity(),list));
                break;

            case SORT_LIST:
                v = inflater.inflate(R.layout.dialog_sort_items,container,false);
                radioGroup = (RadioGroup) v.findViewById(R.id.sort_radiogroup);
                radioGroup.setOnCheckedChangeListener(this);
                negativeButton = (TextView)v.findViewById(R.id.negative_btn);
                negativeButton.setOnClickListener(this);

                break;

        }
        return v;
    }


    @Override
    public void onClick(View view) {

        switch (mType){
            case UPDATE_ITEM:
            case ADD_ITEM:
                if(view.getId() == R.id.positive_btn){
                    String name = nameEdit.getText().toString().trim();
                    String amount = amountEdit.getText().toString().trim();
                    String price = priceEdit.getText().toString().trim();

                    if(name.isEmpty()){
                        nameEdit.setError(getString(R.string.add_shopping_item_name_empty));
                    } else{
                        //save or udpate item
                        ShoppingItem s;
                        if(mType == ADD_ITEM){
                            s = new ShoppingItem(name, amount, price, false);
                        }  else {
                            s = DBManager.getInstance(mContext).getShopItem(mId);
                            s.setName(name);
                            s.setPrice(price);
                            s.setAmount(amount);
                        }
                        if (!((mType == ADD_ITEM)? DBManager.getInstance(mContext).insertProduct(s): DBManager.getInstance(mContext).updateProduct(s))) {
                            Toast.makeText(mContext, getString(R.string.add_shopping_item_insert_error), Toast.LENGTH_LONG).show();
                        } else {
                            ((ShoppingItemsFragment) previousFragment).refresh();
                        }

                        this.dismiss();

                    }


                } else {
                    this.dismiss();
                }
            break;

            case DELETE_LIST:
                if(view.getId() == R.id.positive_btn){
                    ((ShoppingItemsFragment) previousFragment).deleteList();
                }
                this.dismiss();

            break;

            case SAVE_LIST:
                if(view.getId() == R.id.positive_btn){
                    String name = nameEdit.getText().toString().trim();
                    String notes = amountEdit.getText().toString().trim();
                    if(name.isEmpty()){
                        nameEdit.setError(getString(R.string.add_shopping_item_name_empty));
                    } else {
                        ShoppingHistoryItem s = new ShoppingHistoryItem();
                        Calendar c = Calendar.getInstance();

                        s.setName(name);
                        s.setTot_price(((ShoppingItemsFragment) previousFragment).getTotalPrice());
                        s.setNotes(notes);
                        s.setContent(ShopHistoryParser.getInstance().parseListToString(((ShoppingItemsFragment) previousFragment).getListItem()));
                        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATE);
                        String formattedDate = df.format(c.getTime());
                        s.setDate(formattedDate);

                        if(!DBManager.getInstance(mContext).insertHistoryList(s)){
                            Toast.makeText(mContext, getString(R.string.add_shopping_item_insert_error), Toast.LENGTH_LONG).show();
                        } else {
                            ((ShoppingItemsFragment) previousFragment).notifyToHistoryFragment();
                            Toast.makeText(mContext, getString(R.string.add_shopping_item_insert_well_done), Toast.LENGTH_LONG).show();
                        }
                        this.dismiss();
                    }

                } else {
                    this.dismiss();
                }

            break;
            case DELETE_HISTORY_LIST:
                if(view.getId() == R.id.positive_btn){
                    ((HistoryListsFragment) previousFragment).deleteList();
                }
                this.dismiss();

            break;

            case HISTORY_LIST_ITEM:
                if(view.getId() == R.id.negative_btn){
                    ((HistoryListsFragment) previousFragment).setList(itemToSend);
                }
                this.dismiss();
                break;
            case SORT_LIST:
                if(view.getId() == R.id.negative_btn){
                    this.dismiss();
                }
                break;

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        switch(mType){
            case SORT_LIST:
                //ShoppingItemsFragment.SORT_MODE = i;

                int sort_id = i;
                if(sort_id == R.id.unselected_radiobutton){
                    ShoppingListManager.SORT_MODE = ShoppingListManager.UNSELECT_ORDER;
                } else if(sort_id == R.id.selected_radiobutton){
                    ShoppingListManager.SORT_MODE = ShoppingListManager.SELECT_ORDER;
                } else if (sort_id == R.id.price_radiobutton){
                    ShoppingListManager.SORT_MODE = ShoppingListManager.PRICE_ORDER;
                } else if( sort_id == R.id.name_radiobutton){
                    ShoppingListManager.SORT_MODE = ShoppingListManager.NAME_ORDER;
                } else if( sort_id == R.id.none_radiobutton){
                    ShoppingListManager.SORT_MODE = ShoppingListManager.NO_ORDER;
                }

                //back button
                ((ShoppingItemsFragment) previousFragment).orderList();
                this.dismiss();
                break;
        }
    }


    private class HistoryItemDetailAdapter extends BaseAdapter {

        LayoutInflater layoutInflater;
        Context mActivity;
        List<ShoppingItem> mList;


        public HistoryItemDetailAdapter(Activity activity,List<ShoppingItem> l){
            mList = l;
            mContext = activity.getApplicationContext();
            layoutInflater = activity.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mList.size();
        }


        @Override
        public ShoppingItem getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            HistoryItemDetailPlaceholder holder;

            if(view == null){
                view = layoutInflater.inflate(R.layout.dialog_history_item_detail,viewGroup,false);
                holder = new HistoryItemDetailPlaceholder(view);
                view.setTag(holder);
            } else {
                holder = (HistoryItemDetailPlaceholder) view.getTag();
            }


            holder.setView(getItem(i));
            return view;
        }
    }

    private class HistoryItemDetailPlaceholder{

        TextView name;
        TextView amount;
        TextView price;


        public HistoryItemDetailPlaceholder(View v){

            name = (TextView)v.findViewById(R.id.history_detail_name);
            amount = (TextView)v.findViewById(R.id.history_detail_amount);
            price = (TextView)v.findViewById(R.id.history_detail_price);

        }

        public void setView(ShoppingItem s){
            name.setText(s.getName());
            price.setText((!s.getPrice().isEmpty())? s.getPrice()+ " "+mContext.getString(R.string.euro_label) : "");
            amount.setText(s.getAmount());
        }



    }

}
