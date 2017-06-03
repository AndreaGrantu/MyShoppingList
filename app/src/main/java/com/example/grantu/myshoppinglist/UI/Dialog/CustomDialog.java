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
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grantu.myshoppinglist.Classes.ShoppingHistoryItem;
import com.example.grantu.myshoppinglist.Classes.ShoppingItem;
import com.example.grantu.myshoppinglist.Classes.ShoppingListManager;
import com.example.grantu.myshoppinglist.R;
import com.example.grantu.myshoppinglist.UI.Fragments.HistoryListsFragment;
import com.example.grantu.myshoppinglist.UI.Fragments.ShoppingItemsFragment;
import com.example.grantu.myshoppinglist.Utils.ShopHistoryParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Grantu on 17/12/2016.
 */
public class CustomDialog extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {



    public final static  String TYPE = "TYPE";
    public final static  String ITEM_ID = "ITEM_ID";
    public final static  String FORMAT_DATE = "dd-MM-yyyy";


    public final static int ADD_ITEM = 0;
    public final static int DELETE_LIST = ADD_ITEM+1;
    public final static int SAVE_LIST = DELETE_LIST+1;
    public final static int UPDATE_ITEM = SAVE_LIST+1;
    public final static int DELETE_HISTORY_LIST = UPDATE_ITEM+1;
    public final static int HISTORY_LIST_ITEM = DELETE_HISTORY_LIST+1;
    public final static int SORT_LIST = HISTORY_LIST_ITEM+1;
    public final static int SEARCH_ITEM = SORT_LIST +1;
    public final static int MONEY_SPENT = SEARCH_ITEM +1;


    private Context mContext;
    private int mType;
    private int mId;
    private static Fragment previousFragment;
    private String itemToSend;

    private TextView dialogContent;
    private TextView dialogTitle;
    private TextView dialogDatePrice;
    private TextView positiveButton;
    private TextView negativeButton;
    private ListView listView;
    private RadioGroup radioGroup;
    private TextView warningView;

    private EditText nameEdit;
    private EditText notesEdit;
    private EditText priceEdit;


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
                notesEdit = (EditText)v.findViewById(R.id.notes_edit);

                positiveButton.setOnClickListener(this);
                negativeButton.setOnClickListener(this);

                if(mType == UPDATE_ITEM){
                    ShoppingItem s =ShoppingListManager.getInstance(mContext).getShopItemById(mId);
                    dialogTitle.setText(R.string.modify_shopping_item_dialog_title);
                    nameEdit.setText(s.getName());
                    notesEdit.setText(s.getNotes());
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
                notesEdit = (EditText)v.findViewById(R.id.notes_edit);
                priceEdit = (EditText)v.findViewById(R.id.price_edit);
                warningView = (TextView) v.findViewById(R.id.save_shopping_list_warning);
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
                dialogDatePrice = (TextView)v.findViewById(R.id.dialog_date_price);
                positiveButton = (TextView)v.findViewById(R.id.positive_btn);
                negativeButton = (TextView)v.findViewById(R.id.negative_btn);
                listView = (ListView)v.findViewById(R.id.history_details);
                positiveButton.setOnClickListener(this);
                negativeButton.setOnClickListener(this);

                ShoppingHistoryItem s = ShoppingListManager.getInstance(mContext).getShoppingHistoryItem(mId);
                dialogTitle.setText(s.getName());
                dialogDatePrice.setText(s.getDate()+"\n"+(s.getPrice().isEmpty() ? " - " : s.getPrice()));
                dialogContent.setText(s.getNotes());
                itemToSend = s.getContent();
                List<ShoppingItem> list = ShopHistoryParser.getInstance().parseStringToList(s.getContent());
                listView.setAdapter(new HistoryItemDetailAdapter(getActivity(),list));
                break;

            case SORT_LIST:
                v = inflater.inflate(R.layout.dialog_sort_items,container,false);
                radioGroup = (RadioGroup) v.findViewById(R.id.sort_radiogroup);
                radioGroup.check(getIdViewToCheck());
                radioGroup.setOnCheckedChangeListener(this);
                negativeButton = (TextView)v.findViewById(R.id.negative_btn);
                negativeButton.setOnClickListener(this);
                break;

            case SEARCH_ITEM:
                v = inflater.inflate(R.layout.dialog_search_shopping_item, container, false);
                dialogContent = (TextView)v.findViewById(R.id.dialog_content);
                dialogTitle = (TextView)v.findViewById(R.id.dialog_title);
                positiveButton = (TextView)v.findViewById(R.id.positive_btn);
                negativeButton = (TextView)v.findViewById(R.id.negative_btn);
                nameEdit = (EditText)v.findViewById(R.id.name_edit);
                positiveButton.setOnClickListener(this);
                negativeButton.setOnClickListener(this);
                break;
            case MONEY_SPENT:
                v = inflater.inflate(R.layout.dialog_money_spent, container, false);
                dialogContent = (TextView)v.findViewById(R.id.dialog_content);
                dialogTitle = (TextView)v.findViewById(R.id.dialog_title);
                positiveButton = (TextView)v.findViewById(R.id.positive_btn);
                listView = (ListView)v.findViewById(R.id.periods);

                break;


        }
        return v;
    }


    @Override
    public void onClick(View view) {

        switch (mType){
            case UPDATE_ITEM:
            case ADD_ITEM:
                if(!(previousFragment instanceof ShoppingItemsFragment)){
                    break;
                }

                if(view.getId() == R.id.positive_btn){
                    String name = nameEdit.getText().toString().trim();
                    String notes = notesEdit.getText().toString().trim();

                    if(name.isEmpty()){
                        nameEdit.setError(getString(R.string.add_shopping_item_name_empty));
                    } else{
                        //save or udpate item
                        ShoppingItem s;
                        if(mType == ADD_ITEM){
                            s = new ShoppingItem(name, notes, false);
                        }  else {
                            s = ShoppingListManager.getInstance(mContext).getShopItemById(mId);
                            s.setName(name);
                            s.setNotes(notes);
                        }
                        if (!((mType == ADD_ITEM)? ShoppingListManager.getInstance(mContext).addShopListItem(s) : ShoppingListManager.getInstance(mContext).updateShopListItem(s))) {
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
                if(!(previousFragment instanceof ShoppingItemsFragment)){
                    break;
                }
                if(view.getId() == R.id.positive_btn){
                    ((ShoppingItemsFragment) previousFragment).deleteList();
                }
                this.dismiss();

            break;

            case SAVE_LIST:
                if(!(previousFragment instanceof ShoppingItemsFragment)){
                    break;
                }
                if(view.getId() == R.id.positive_btn){
                    String name = nameEdit.getText().toString().trim();
                    String notes = notesEdit.getText().toString().trim();
                    String price = priceEdit.getText().toString().trim();
                    if(name.isEmpty()){
                        nameEdit.setError(getString(R.string.add_shopping_item_name_empty));
                    } else {
                        ShoppingHistoryItem s = new ShoppingHistoryItem();
                        Calendar c = Calendar.getInstance();

                        s.setName(name);
                        s.setNotes(notes);
                        s.setPrice(price);
                        s.setContent(ShopHistoryParser.getInstance().parseListToString(((ShoppingItemsFragment) previousFragment).getListItem()));
                        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATE);
                        String formattedDate = df.format(c.getTime());
                        s.setDate(formattedDate);
                        if(s.getContent().isEmpty()){
                            hideKeyboard();
                            warningView.setVisibility(View.VISIBLE);
                        } else {

                            if (!ShoppingListManager.getInstance(mContext).saveShopHistoryItem(s)) {
                                Toast.makeText(mContext, getString(R.string.add_shopping_item_insert_error), Toast.LENGTH_LONG).show();
                            } else {
                                ((ShoppingItemsFragment) previousFragment).notifyToHistoryFragment();
                                Toast.makeText(mContext, getString(R.string.add_shopping_item_insert_well_done), Toast.LENGTH_LONG).show();
                            }
                            this.dismiss();
                        }
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
            case SEARCH_ITEM:
                if(view.getId() == R.id.positive_btn){
                    ((ShoppingItemsFragment) previousFragment).searchItem(nameEdit.getText().toString().trim());
                }
                this.dismiss();
                break;
            case MONEY_SPENT:
                if(view.getId() == R.id.positive_btn){
                    this.dismiss();
                };
                break;

        }

    }

    private void hideKeyboard() {
        if (this.getView() != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromInputMethod(getView().getWindowToken(), 0);
        }

    }
    private int getIdViewToCheck(){

        switch (ShoppingListManager.SORT_MODE){
            case ShoppingListManager.SELECT_ORDER:
                return R.id.selected_radiobutton;
            case ShoppingListManager.UNSELECT_ORDER:
                return R.id.unselected_radiobutton;
            case ShoppingListManager.NAME_ORDER:
                return R.id.name_radiobutton;
            case ShoppingListManager.NO_ORDER:
                return R.id.none_radiobutton;
            default:
                return R.id.none_radiobutton;

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
        List<ShoppingItem> mList;


        public HistoryItemDetailAdapter(Activity activity,List<ShoppingItem> l){
            mList = l;
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
        TextView notes;

        public HistoryItemDetailPlaceholder(View v){
            name = (TextView)v.findViewById(R.id.history_detail_name);
            notes = (TextView)v.findViewById(R.id.history_detail_notes);

        }

        public void setView(ShoppingItem s){
            name.setText(s.getName());
            notes.setText(s.getNotes());
        }



    }


    private class HistoryPeriodsMoneyAdapter extends BaseAdapter{

        LayoutInflater layoutInflater;
        String[] periods;


        public HistoryPeriodsMoneyAdapter(Activity activity){
            layoutInflater = activity.getLayoutInflater();
            periods = mContext.getResources().getStringArray(R.array.periods_array);
        }

        @Override
        public int getCount() {
            return periods.length;
        }

        @Override
        public Object getItem(int i) {
            return periods[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            periodPlaceHolder holder;

            if( view == null ){
                view = layoutInflater.inflate(R.layout.dialog_item_periods,viewGroup,false);
                holder = new periodPlaceHolder(view);
                view.setTag(holder);
            } else {
                holder = (periodPlaceHolder) view.getTag();
            }
            holder.setView(periods[i]);

            return view;
        }



    }

    private class periodPlaceHolder{
        TextView textView;
        String period;

        public periodPlaceHolder(View v){
            textView = (TextView)v.findViewById(R.id.periods_item);
        }

        public void setView(String s){
            textView.setText(s);
            period = s;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calculateMoney();
                }
            });
        }

        private void calculateMoney(){

            String result;

            if(period.equals(mContext.getString(R.string.dialog_money_jan))){
                result = getMoneySpent(Calendar.JANUARY);
            } else if(period.equals(getString(R.string.dialog_money_feb))){
                    result = getMoneySpent(Calendar.FEBRUARY);
            } else if(period.equals(getString(R.string.dialog_money_mar))){
                result = getMoneySpent(Calendar.MARCH);
            } else if(period.equals(getString(R.string.dialog_money_apr))){
                result = getMoneySpent(Calendar.APRIL);
            } else if(period.equals(getString(R.string.dialog_money_may))){
                result = getMoneySpent(Calendar.MAY);
            } else if(period.equals(mContext.getString(R.string.dialog_money_june))){
                result = getMoneySpent(Calendar.JUNE);
            } else if(period.equals(mContext.getString(R.string.dialog_money_july))){
                result = getMoneySpent(Calendar.JULY);
            } else if(period.equals(mContext.getString(R.string.dialog_money_aug))){
                result = getMoneySpent(Calendar.AUGUST);
            } else if(period.equals(mContext.getString(R.string.dialog_money_sep))){
                result = getMoneySpent(Calendar.SEPTEMBER);
            } else if(period.equals(mContext.getString(R.string.dialog_money_oct))){
                result = getMoneySpent(Calendar.OCTOBER);
            } else if(period.equals(mContext.getString(R.string.dialog_money_nov))){
                result = getMoneySpent(Calendar.NOVEMBER);
            } else if(period.equals(mContext.getString(R.string.dialog_money_dec))){
                result = getMoneySpent(Calendar.DECEMBER);
            } else if(period.equals(mContext.getString(R.string.dialog_money_month))){
                result = getMoneySpentLastMonth(1);
            } else if(period.equals(mContext.getString(R.string.dialog_money_two_month))){
                result = getMoneySpentLastMonth(2);
            }else if(period.equals(mContext.getString(R.string.dialog_money_three_month))){
                result = getMoneySpentLastMonth(3);
            }else if(period.equals(mContext.getString(R.string.dialog_money_six_month))){
                result = getMoneySpentLastMonth(6);
            } else if(period.equals(mContext.getString(R.string.dialog_money_year))){
                result = getMoneySpentLastMonth(12);
            }

            //set the textview

        }


        private String getMoneySpent(int p){
            float total = 0;
            List<ShoppingHistoryItem> list = ShoppingListManager.getInstance(mContext).getAllShoppingHistoryItems();
            SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATE);
            Calendar c = Calendar.getInstance();

            for(ShoppingHistoryItem sh : list){
                try {
                    c.setTime(df.parse(sh.getDate()));
                    if(p == c.get(Calendar.MONTH) && !sh.getPrice().isEmpty()){
                        total += Float.parseFloat(sh.getPrice());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            return Float.toString(total);
        }

        private String getMoneySpentLastMonth(int n){
            float total = 0;
            Calendar thr = Calendar.getInstance();
            thr.set(Calendar.MONTH,(thr.get(Calendar.MONTH)-n));
            SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATE);
            List<ShoppingHistoryItem> list = ShoppingListManager.getInstance(mContext).getAllShoppingHistoryItems();

            for(ShoppingHistoryItem sh : list){
                Calendar c1 = Calendar.getInstance();
                try {
                    c1.setTime(df.parse(sh.getDate()));
                    if(c1.after(thr) && !sh.getPrice().isEmpty()){
                        total += Float.parseFloat(sh.getPrice());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            return Float.toString(total);

        }

    }



}
