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
import com.tokeninc.deviceinfo.DeviceInfo;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Helpers.DataBase.DatabaseHelper;
import com.tokeninc.sardis.application_template.Helpers.PrintHelpers.PrintHelper;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends BaseActivity {

    private CustomInputView inputMerchantId;
    private CustomInputView inputTerminalId;
    private TextView btnSave;
    private InputListFragment hostFragment;
    private ViewGroup setupLayout;
    private ListMenuFragment menuFragment;
    private String terminalId;
    private String merchantId;
    private static Context context;
    DatabaseHelper databaseHelper;
    private WeakReference<AppCompatActivity> mContext;
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
        setupLayout = findViewById(R.id.setupLayout);
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

    public void showMenu() {
        List<IListMenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Kurulum", iListMenuItem -> {
            setupLayout.setVisibility(View.VISIBLE);
        }));
        menuItems.add(new MenuItem("Host Ayarları", iListMenuItem -> addIpFragment()));

        menuFragment = ListMenuFragment.newInstance(menuItems, "Ayarlar",
                true, R.drawable.token_logo);
        addFragment(R.id.container, menuFragment, false);

        inputMerchantId = findViewById(R.id.inputMerchantId);
        inputTerminalId = findViewById(R.id.inputTerminalId);

        inputTerminalId.setText("");
        inputMerchantId.setText("");

        inputMerchantId.setFormat(new CustomInputFormat("Merchant No", EditTextInputType.Number, 10, "İş Yeri No Geçersiz!",
                customInputFormat -> !customInputFormat.getText().isEmpty()));
        inputTerminalId.setFormat(new CustomInputFormat("Terminal ID", EditTextInputType.Text, 8, "Terminal No Geçersiz!",
                customInputFormat -> !customInputFormat.getText().isEmpty()));
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener((v) -> {
            onSaveClicked();
        });
    }

    private void addIpFragment() {
        List<CustomInputFormat> inputList = new ArrayList<>();
        inputList.add(new CustomInputFormat("IP", EditTextInputType.IpAddress, null, "Geçersiz IP!", new InputValidator() {
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

        inputList.add(new CustomInputFormat("Port", EditTextInputType.Number, 4, "Geçersiz Port!", new InputValidator() {
            @Override
            public boolean validate(CustomInputFormat customInputFormat) {
                return customInputFormat.getText().length() >= 2 && Integer.parseInt(customInputFormat.getText()) > 0;
            }
        }));

             hostFragment = InputListFragment.newInstance(inputList, "Kaydet", new InputListFragment.ButtonListener() {
            @Override
            public void onButtonAction(List<String> list) {
                // TODO If Batch Close SUCCESS
                InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Progress, "Batch Close: Success", false);
                new Handler().postDelayed(() -> {
                    // TODO Set Host Settings
                    dialog.update(InfoDialog.InfoType.Confirmed, "Activation Completed");
                    new Handler().postDelayed(() -> {
                        dialog.dismiss();
                    }, 2000);
                }, 2000);
                // TODO Else Do Batch Close than set Host
            }
        });
        addFragment(R.id.secondContainer, hostFragment, true);
    }

    private void onSaveClicked() {
        boolean terminalIdValid = inputTerminalId.isValid();
        boolean merchantIdValid = inputMerchantId.isValid();
        if (terminalIdValid && merchantIdValid) {
            terminalId = inputTerminalId.getText();
            merchantId = inputMerchantId.getText();
            inputTerminalId.setText("");
            inputMerchantId.setText("");
            startActivation();

            databaseHelper.dropDataTidMid();
            databaseHelper.insertDataTidMid(merchantId,terminalId);

            setupLayout.setVisibility(View.GONE);
        }
    }

    private void startActivation() {
        if (DB_getAllTransactionsCount == true) {

            insertActivation(context, terminalId, merchantId );

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

    public void insertActivation(Context context, String terminalId, String merchantId) {

        DeviceInfo deviceInfo = new DeviceInfo(context);
        deviceInfo.setBankParams(new DeviceInfo.DeviceInfoBankParamsSetterHandler() {
            @Override
            public void onReturn(boolean success) {
                //Handle result, it might not be written for some case.
                //If it's successfully written, you will get result as true
            }
        },terminalId, merchantId);
        Log.i("Terminal ID: " +terminalId, "Merchant ID: " + merchantId);
    }

    @Override
    public void onBackPressed() {
        if (setupLayout.getVisibility() == View.VISIBLE) {
            setupLayout.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
        }
    }
}
