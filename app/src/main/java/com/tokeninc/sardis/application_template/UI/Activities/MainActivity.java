package com.tokeninc.sardis.application_template.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Toast;

import com.token.uicomponents.ListMenuFragment.IAuthenticator;
import com.token.uicomponents.ListMenuFragment.IListMenuItem;
import com.token.uicomponents.ListMenuFragment.ListMenuFragment;
import com.token.uicomponents.ListMenuFragment.MenuItemClickListener;
import com.token.uicomponents.infodialog.InfoDialog;
import com.token.uicomponents.infodialog.InfoDialogListener;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static com.tokeninc.sardis.application_template.Helpers.KeyInjectHelper.exampleKey;

/*
This activity shows how to use List Menu
 */
public class MainActivity extends BaseActivity implements InfoDialogListener {

    private List<IListMenuItem> menuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItems, "", false);
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {

        List<IListMenuItem> subList2 = new ArrayList<>();
        subList2.add(new MenuItem("subMenuItem 1.1", (menuItem) -> {

        }, null));
        subList2.add(new MenuItem("subMenuItem 1.2", null));
        subList2.add(new MenuItem("subMenuItem 1.3", null));

        List<IListMenuItem> subList1 = new ArrayList<>();
        subList1.add(new MenuItem("MenuItem 1 with Sub menu", subList2, new IAuthenticator() {
            @Override
            public boolean verify(String s) {
                return s.equals("1234");
            }
        }));
        subList1.add(new MenuItem("MenuItem 2", null));
        subList1.add(new MenuItem("MenuItem 3", null));

        menuItems.add(new MenuItem("CustomInputList", new MenuItemClickListener<MenuItem>() {
            @Override
            public void onClick(MenuItem menuItem) {
                Intent myIntent = new Intent(MainActivity.this, EditLineActivity.class);
                startActivity(myIntent);
            }
        }));
        menuItems.add(new MenuItem("InfoDialog", (menuItem) -> {
            Intent myIntent = new Intent(MainActivity.this, PosTxnSecondActivity.class);
            startActivity(myIntent);
        }));
        menuItems.add(new MenuItem("ConfirmationDialog", (menuItem) ->
                showConfirmationDialog(InfoDialog.InfoType.Warning,"Warning", "Are you sure?", InfoDialog.InfoDialogButtons.Both, 99, MainActivity.this)));
        menuItems.add(new MenuItem("Device Number", (menuItem) -> {
            try {
                Toast.makeText(this, "Device SN: " + cardServiceBinding.getDeviceSN(), Toast.LENGTH_SHORT).show();
            }
            catch (RemoteException e) { }
        }));

        menuItems.add(new MenuItem("Inject Key", (menuItem) -> {
            exampleKey();
        }));


        menuItems.add(new MenuItem("Menu Item with Sub menu", subList1, null));

    }

    @Override
    public void confirmed(int arg) {
        if (arg == 99) {
            Toast.makeText(this, "Confirmed.", Toast.LENGTH_SHORT).show();
        }
        //else if (arg == ***) { Do something else... }
    }

    @Override
    public void canceled(int arg) {
        if (arg == 99) {
            Toast.makeText(this, "Canceled.", Toast.LENGTH_SHORT).show();
        }
        //else if (arg == ***) { Do something else... }
    }
}
