package com.tokeninc.sardis.application_template.UI.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.token.components.ListMenuFragment.IListMenuItem;
import com.token.components.ListMenuFragment.ListMenuClickListener;
import com.token.components.ListMenuFragment.ListMenuFragment;
import com.token.components.infodialog.InfoDialog;
import com.token.components.infodialog.InfoDialogListener;
import com.tokeninc.cardservice.ITokenCardService;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import java.util.ArrayList;
import java.util.List;

/*
This activity shows how to use List Menu
 */
public class MainActivity extends BaseActivity implements InfoDialogListener, ListMenuClickListener {

    private List<IListMenuItem> menuItems = new ArrayList<>();

    private ITokenCardService emvService;
    private ServiceConnection emvServiceConnection;
    private boolean mBound = false;

    private static final String EMV_SERVICE_PACKAGE_NAME = "com.tokeninc.cardservice";
    private static final String EMV_SERVICE_NAME = "com.tokeninc.cardservice.CardService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItems, this, "", false);
        addFragment(R.id.container, fragment, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            bindService();
        }
    }

    @Override
    protected void onDestroy() {
        if (mBound) {
            unbindService(emvServiceConnection);
            mBound = false;
        }
        super.onDestroy();
    }

    private void prepareData() {

        List<IListMenuItem> subList2 = new ArrayList<>();
        subList2.add(new MenuItem(10,"subMenuItem 1.1"));
        subList2.add(new MenuItem(11,"subMenuItem 1.2"));
        subList2.add(new MenuItem(12,"subMenuItem 1.3"));

        List<IListMenuItem> subList1 = new ArrayList<>();
        subList1.add(new MenuItem(6,"MenuItem 1 with Sub menu", subList2));
        subList1.add(new MenuItem(7,"MenuItem 2"));
        subList1.add(new MenuItem(8,"MenuItem 3"));



        menuItems.add(new MenuItem(1,"CustomInputList"));
        menuItems.add(new MenuItem(2,"InfoDialog"));
        menuItems.add(new MenuItem(3,"ConfirmationDialog"));
        menuItems.add(new MenuItem(4,"Device Number"));
        menuItems.add(new MenuItem(5,"Menu Item with Sub menu", subList1));

    }

    private void startActivity(int menuNo){
        switch (menuNo){
            case 1: {
                Intent myIntent = new Intent(MainActivity.this, EditLineActivity.class);
                startActivity(myIntent);
                break;
            }
            case 2: {
                Intent myIntent = new Intent(MainActivity.this, PosTxnActivity.class);
                startActivity(myIntent);
                break;
            }
            case 3: {
                showConfirmationDialog(InfoDialog.InfoType.Warning,"Warning", "Are you sure?", InfoDialog.InfoDialogButtons.Both, 99, this);
                break;
            }
            case 4: {
                if (mBound) {
                    try {
                        Toast.makeText(this, "Device SN: " + emvService.getDeviceSN(), Toast.LENGTH_SHORT).show();
                    }
                    catch (RemoteException e) { }
                }
                else {
                    Toast.makeText(this, "Service not bound!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
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

    @Override
    public void onItemClick(IListMenuItem item) {
        startActivity(item.getId());
    }

    private void bindService() {
        emvServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                Log.d("TCardService", "Connected");
                emvService = ITokenCardService.Stub.asInterface(service);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d("TCardService", "Disconnected");
                mBound = false;
            }
        };

        Intent emvS = new Intent();
        emvS.setComponent(new ComponentName(EMV_SERVICE_PACKAGE_NAME, EMV_SERVICE_NAME));
        try {
            if (!bindService(emvS, emvServiceConnection, Context.BIND_AUTO_CREATE)) {
                Toast.makeText(getApplicationContext(), "Could not bind to the service", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("TCardService", "Successfully bound!");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
