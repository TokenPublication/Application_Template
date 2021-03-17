package com.tokeninc.sardis.application_template.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Toast;

import com.token.uicomponents.ListMenuFragment.IListMenuItem;
import com.token.uicomponents.ListMenuFragment.ListMenuFragment;
import com.token.uicomponents.ListMenuFragment.MenuItemClickListener;
import com.token.uicomponents.infodialog.InfoDialog;
import com.token.uicomponents.infodialog.InfoDialogListener;
import com.token.uicomponents.numpad.NumPadDialog;
import com.token.uicomponents.numpad.NumPadListener;
import com.tokeninc.barcodescannerimpl.TokenBarcodeScanner;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Helpers.PrintHelpers.BankSuccessPrintHelper;
import com.tokeninc.sardis.application_template.Helpers.PrintHelpers.PrintServiceBinding;
import com.tokeninc.sardis.application_template.Helpers.StringHelper;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.tokeninc.sardis.application_template.Helpers.KeyInjectHelper.exampleKey;

/*
This activity shows how to use List Menu
 */
public class MainActivity extends BaseActivity implements InfoDialogListener {

    private List<IListMenuItem> menuItems = new ArrayList<>();
    private PrintServiceBinding printService;

    protected int qrAmount = 100;
    protected String qrString = "QR Kod Test";
    boolean qrResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printService = new PrintServiceBinding();

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItems, "Pos İşlemleri", false, R.drawable.token_logo);
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {

        List<IListMenuItem> subList1 = new ArrayList<>();
        subList1.add(new MenuItem("Menu Item 1", (menuItem) -> {

            Toast.makeText(this,"Sub Menu 1", Toast.LENGTH_LONG).show();

        }, null));

        subList1.add(new MenuItem("Menu Item 2", (menuItem) -> {

            Toast.makeText(this,"Sub Menu 2", Toast.LENGTH_LONG).show();

        }, null));
        subList1.add(new MenuItem("Menu Item 3", (menuItem) -> {

            Toast.makeText(this,"Sub Menu 3", Toast.LENGTH_LONG).show();

        }, null));

        menuItems.add(new MenuItem("Sub Menu", subList1, null));

        menuItems.add(new MenuItem("Custom Input List", new MenuItemClickListener<MenuItem>() {
            @Override
            public void onClick(MenuItem menuItem) {
                Intent myIntent = new Intent(MainActivity.this, EditLineActivity.class);
                startActivity(myIntent);
            }
        }));

        menuItems.add(new MenuItem("Info Dialog", (menuItem) -> {
            Intent myIntent = new Intent(MainActivity.this, InfoDialogActivity.class);
            startActivity(myIntent);
        }));

        menuItems.add(new MenuItem("Confirmation Dialog", (menuItem) -> {
            Intent myIntent = new Intent(MainActivity.this, ConfirmationDialogActivity.class);
            startActivity(myIntent);
        }));

        menuItems.add(new MenuItem("Device Number", (menuItem) -> {
            try {
                Toast.makeText(this, "Device SN: " + cardServiceBinding.getDeviceSN(), Toast.LENGTH_SHORT).show();
            }
            catch (RemoteException e) { }
        }));

        menuItems.add(new MenuItem("Inject Key", (menuItem) -> {
            exampleKey();
        }));

        menuItems.add(new MenuItem("Barcode Scanner", iListMenuItem -> {
            new TokenBarcodeScanner(new WeakReference<>(MainActivity.this),data -> {
                Toast.makeText(this, "Barcode Data: " + data, Toast.LENGTH_SHORT).show();
            });
        }));

        menuItems.add(new MenuItem("Num Pad", (menuItem) -> {

            NumPadDialog dialog = NumPadDialog.newInstance(new NumPadListener(){

                @Override
                public void enter(String pin) {

                }

                @Override
                public void onCanceled() {
                    //Num pad canceled callback
                    NumPadCancelled();
                }

            }, "Please enter PIN", 8);
            dialog.show(getSupportFragmentManager(), "Num Pad");
        }));

        menuItems.add(new MenuItem("Print Load Success", (menuItem) -> {
            printService.print(BankSuccessPrintHelper.PrintSuccess()); // Message print: Load Success
            printService.print(BankSuccessPrintHelper.PrintError()); // Message print: Load Error
        }));

        menuItems.add(new MenuItem("Show QR", (menuItem) -> {
            qrResponse = true;
            QrDialog();
        }));
    }

    public void NumPadCancelled(){
        Toast.makeText(this,"NumPad Cancelled", Toast.LENGTH_LONG).show();
    }

    protected void QrDialog(){
        InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Progress, "QR Loading", true);
        // Request a response ->
        if(qrResponse == true){
            cardServiceBinding.showQR("PLEASE READ THE QR CODE", StringHelper.getAmount(qrAmount), qrString); // Shows QR on the back screen
            dialog.setQr(qrString, "WAITING FOR THE QR CODE"); // Shows the same QR on Info Dialog
        }
        else {
            dialog.update(InfoDialog.InfoType.Declined, "Declined");
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
