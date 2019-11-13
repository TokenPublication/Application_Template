package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.tokeninc.cardservice.ITokenCardService;
import com.token.components.ListMenuFragment.IListMenuItem;
import com.token.components.ListMenuFragment.ListMenuClickListener;
import com.token.components.ListMenuFragment.ListMenuFragment;
import com.token.components.infodialog.InfoDialog;
import com.token.components.infodialog.InfoDialogListener;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class PosTxnActivity extends BaseActivity implements ListMenuClickListener, InfoDialogListener {

    private ITokenCardService emvService;
    private ServiceConnection emvServiceConnection;
    private boolean mBound = false;

    private static final String EMV_SERVICE_PACKAGE_NAME = "com.tokeninc.cardservice";
    private static final String EMV_SERVICE_NAME = "com.tokeninc.cardservice.CardService";

    private List<IListMenuItem> menuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_txn);

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItems, this, "");
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
        menuItems.add(new MenuItem("EditLineListFragment"));
        menuItems.add(new MenuItem("InfoDialog"));
        menuItems.add(new MenuItem("ConfirmationDialog"));
        menuItems.add(new MenuItem("Device Number"));
    }

    @Override
    public void onItemClick(int position, IListMenuItem item) {
        startActivity(position);
    }

    private void startActivity(int menuNo){
        switch (menuNo){
            case 0: {
                Intent myIntent = new Intent(this, EditLineActivity.class);
                startActivity(myIntent);
                break;
            }
            case 1: {
                Intent myIntent = new Intent(this, PosTxnSecondActivity.class);
                startActivity(myIntent);
                break;
            }
            case 2: {
                showConfirmationDialog(InfoDialog.InfoType.Warning,"Warning", "Are you sure?", InfoDialog.InfoDialogButtons.Both, 99, this);
                break;
            }
            case 3: {
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

    public void onPosTxnResponse() {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        //bundle.putString("ResponseCode", PosTxnResponse);
        resultIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK, resultIntent);//PosTxn_Request_Code:13
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
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
