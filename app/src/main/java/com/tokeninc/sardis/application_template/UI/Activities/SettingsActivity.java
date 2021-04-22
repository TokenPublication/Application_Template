package com.tokeninc.sardis.application_template.UI.Activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.token.uicomponents.CustomInput.CustomInputFormat;
import com.token.uicomponents.CustomInput.CustomInputView;
import com.token.uicomponents.CustomInput.EditTextInputType;
import com.token.uicomponents.CustomInput.InputListFragment;
import com.token.uicomponents.CustomInput.InputValidator;
import com.token.uicomponents.ListMenuFragment.IListMenuItem;
import com.token.uicomponents.ListMenuFragment.ListMenuFragment;
import com.token.uicomponents.infodialog.InfoDialog;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Helpers.DataBase.DatabaseHelper;
import com.tokeninc.sardis.application_template.Helpers.PrintHelpers.PrintHelper;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

//import com.tokeninc.deviceinfo.DeviceInfo;

public class SettingsActivity extends BaseActivity {

    private InputListFragment hostFragment, TidMidFragment;
    private ListMenuFragment menuFragment;
    private String terminalId;
    private String merchantId, ip_no, port_no;
    private static Context context;
    DatabaseHelper databaseHelper;
    private boolean isBankActivateAction = true, DB_getAllTransactionsCount = true;

    public SettingsActivity() {
    }

    @Override
    protected int getTimeOutSec() {
        return TIME_OUT;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SettingsActivity.context = getApplicationContext();
        databaseHelper = new DatabaseHelper(this);

        isBankActivateAction = getIntent() != null && getIntent().getAction() != null
                && getIntent().getAction().equals("Activate_Bank");
        if (isBankActivateAction) {
            terminalId = getIntent().getStringExtra("terminalID");
            merchantId = getIntent().getStringExtra("merchantID");
            startActivation();
        }
        else {
            showMenu();
        }
    }

    private void showMenu() {
        List<IListMenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Setup", iListMenuItem -> {
            addTidMidFragment();
        }));
        menuItems.add(new MenuItem("Host Settings", iListMenuItem -> addIpFragment()));

        menuFragment = ListMenuFragment.newInstance(menuItems, "Settings",
                true, R.drawable.token_logo);
        addFragment(R.id.container, menuFragment, false);

    }

    private void addIpFragment() {
        List<CustomInputFormat> inputList = new ArrayList<>();
        inputList.add(new CustomInputFormat("IP", EditTextInputType.IpAddress, null, "Invalid IP!", new InputValidator() {
            @Override
            public boolean validate(CustomInputFormat customInputFormat) {
                String text = customInputFormat.getText();
                boolean isValid = StringUtils.countMatches(text, ".") == 3 && text.split("\\.").length == 4;
                if (isValid) {
                    String[] array = text.split("\\.");
                    int index = 0;
                    while (isValid && index < array.length) {
                        isValid = StringUtils.isNumeric(array[0]);
                        index++;
                    }
                }
                return isValid;
            }
        }));

        inputList.add(new CustomInputFormat("Port", EditTextInputType.Number, 4, "Invalid Port!", new InputValidator() {
            @Override
            public boolean validate(CustomInputFormat customInputFormat) {
                return customInputFormat.getText().length() >= 2 && Integer.parseInt(customInputFormat.getText()) > 0;
            }
        }));
        inputList.get(0).setText(databaseHelper.getIP_NO());
        inputList.get(1).setText(databaseHelper.getPort());

        hostFragment = InputListFragment.newInstance(inputList, "Save", new InputListFragment.ButtonListener() {
            @Override
            public void onButtonAction(List<String> list) {
                // TODO If Batch Close SUCCESS
                InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Progress, "Batch Close: Success", false);
                new Handler().postDelayed(() -> {
                    // TODO Set Host Settings
                    dialog.update(InfoDialog.InfoType.Confirmed, "Activation Completed");

                    ip_no = inputList.get(0).getText();
                    port_no = inputList.get(1).getText();

                    databaseHelper.updateIP_NO(ip_no);
                    databaseHelper.updatePort(port_no);

                    new Handler().postDelayed(() -> {
                        dialog.dismiss();
                    }, 2000);
                }, 2000);
            }
        });
        addFragment(R.id.secondContainer, hostFragment, true);
    }

    private void addTidMidFragment() {
        List<CustomInputFormat> inputList = new ArrayList<>();
        inputList.add(new CustomInputFormat("Merchant No", EditTextInputType.Number, 10, "Invalid Merchant No!", new InputValidator() {
                @Override
                public boolean validate(CustomInputFormat input) {
                    return input.getText().length() == 10;
                }
        }));

        inputList.add(new CustomInputFormat("Terminal No", EditTextInputType.Text, 8, "Invalid Terminal No!", new InputValidator() {
            @Override
            public boolean validate(CustomInputFormat input) {
                return input.getText().length() == 8;
            }
        }));

        inputList.get(0).setText(databaseHelper.getMerchantId());
        inputList.get(1).setText(databaseHelper.getTerminalId());

        TidMidFragment = InputListFragment.newInstance(inputList, "Save", new InputListFragment.ButtonListener() {
            @Override
            public void onButtonAction(List<String> list) {

                merchantId = inputList.get(0).getText();
                terminalId = inputList.get(1).getText();

                databaseHelper.updateMerchantId(merchantId);
                databaseHelper.updateTerminalId(terminalId);

                startActivation();
            }
        });
        addFragment(R.id.thirdContainer, TidMidFragment, true);
    }

    private void startActivation() {
        if (DB_getAllTransactionsCount == true) {

            InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Processing, "Starting Activation...", false);
                new Handler(Looper.myLooper()).postDelayed(() -> {
                    dialog.update(InfoDialog.InfoType.Progress, "Parameter Loading");
                    new Handler().postDelayed(() -> {
                        dialog.update(InfoDialog.InfoType.Confirmed, "Member activation: Completed");
                        new Handler().postDelayed(() -> {
                            dialog.update(InfoDialog.InfoType.Progress, "RKL Loading");
                        new Handler().postDelayed(() -> {
                            dialog.update(InfoDialog.InfoType.Confirmed, "RKL Loaded");
                            new Handler().postDelayed(() -> {
                                dialog.update(InfoDialog.InfoType.Progress, "Key Block Loading");
                                new Handler().postDelayed(() -> {
                                    dialog.update(InfoDialog.InfoType.Confirmed, "Activation Completed");
                            new Handler().postDelayed(() -> {
                                dialog.dismiss();
                                printService.print(PrintHelper.PrintSuccess()); // Print success
                                  }, 2000);
                             }, 2000);
                            }, 2000);
                        }, 2000);
                    }, 2000);
                    }, 2000);
                }, 2000);
            }

        else {
            new Handler().postDelayed(() -> {
            InfoDialog progress = showInfoDialog(InfoDialog.InfoType.Progress, "Parameter Loading...", false);
            }, 2000);
        }
    }
}
