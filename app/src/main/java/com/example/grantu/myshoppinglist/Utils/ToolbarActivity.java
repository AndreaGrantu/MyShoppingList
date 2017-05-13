package com.example.grantu.myshoppinglist.Utils;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.grantu.myshoppinglist.R;

/**
 * Created by Gianmarco David on 24/04/15.
 * Activity to automatize the inflation of the toolbar.
 * An activity extending this class should pass to setContentView a layout containing a toolbar
 * with id @+id/toolbar
 */
public class ToolbarActivity extends AppCompatActivity {
    protected ActionBar actionbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            actionbar = getSupportActionBar();
        }
    }
}
