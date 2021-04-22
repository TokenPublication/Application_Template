package com.tokeninc.sardis.application_template.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.token.uicomponents.ListMenuFragment.IListMenuItem;
import com.token.uicomponents.ListMenuFragment.ListMenuFragment;
import com.token.uicomponents.infodialog.InfoDialog;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Helpers.DataBase.DatabaseHelper;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Activities.PosOperations.RefundActivity;
import com.tokeninc.sardis.application_template.UI.Activities.PosOperations.TransactionsActivity;
import com.tokeninc.sardis.application_template.UI.Activities.PosOperations.VoidActivity;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<IListMenuItem> menuItems = new ArrayList<>();
    DatabaseHelper databaseHelper;
    VoidActivity voidActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.createTables();
        voidActivity = new VoidActivity();

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItems, "Pos Operations", false, R.drawable.token_logo);
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {

        menuItems.add(new MenuItem("Sales", iListMenuItem -> {
            Intent myIntent = new Intent(MainActivity.this, TransactionsActivity.class);
            startActivity(myIntent);
        }));

        menuItems.add(new MenuItem("Refund", iListMenuItem -> {
            Intent myIntent = new Intent(MainActivity.this, RefundActivity.class);
            startActivity(myIntent);
        }));
        menuItems.add(new MenuItem("Settings", iListMenuItem -> {
            // Settings Activity is in service menu.
            Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(myIntent);
        }));

        menuItems.add(new MenuItem("Batch Close", iListMenuItem -> {
            InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Processing, "Batch Close", false);
            new Handler().postDelayed(() -> {
                dialog.update(InfoDialog.InfoType.Confirmed, "Batch Close: Done");
                databaseHelper.batchClose();
                new Handler().postDelayed(() -> {
                    dialog.dismiss();
                    }, 2000);
                }, 2000);
        }));

        menuItems.add(new MenuItem("Examples", iListMenuItem -> {
            Intent myIntent = new Intent(MainActivity.this, ExamplesActivity.class);
            startActivity(myIntent);
        }));


    }
}
