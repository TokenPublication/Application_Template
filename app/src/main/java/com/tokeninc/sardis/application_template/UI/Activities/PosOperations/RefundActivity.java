package com.tokeninc.sardis.application_template.UI.Activities.PosOperations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.token.uicomponents.CustomInput.CustomInputFormat;
import com.token.uicomponents.CustomInput.EditTextInputType;
import com.token.uicomponents.CustomInput.InputListFragment;
import com.token.uicomponents.ListMenuFragment.IListMenuItem;
import com.token.uicomponents.ListMenuFragment.ListMenuFragment;
import com.token.uicomponents.ListMenuFragment.MenuItemClickListener;
import com.token.uicomponents.infodialog.InfoDialog;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.Entity.CardReadType;
import com.tokeninc.sardis.application_template.Entity.ICCCard;
import com.tokeninc.sardis.application_template.Entity.ICard;
import com.tokeninc.sardis.application_template.Entity.MSRCard;
import com.tokeninc.sardis.application_template.Entity.ResponseCode;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.MenuItem;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.token.uicomponents.CustomInput.EditTextInputType.Amount;

public class RefundActivity extends BaseActivity implements View.OnClickListener {

    private ICard card;
    private List<IListMenuItem> menuItems = new ArrayList<>();

    private CustomInputFormat inputTranDate;
    private CustomInputFormat inputOrgAmount;
    private CustomInputFormat inputRetAmount;
    private CustomInputFormat inputMerID;
    private CustomInputFormat inputRefNo;
    private CustomInputFormat inputAuthCode;

    private int installmentCount;
    private ListMenuFragment instFragment;
    private List<String> data;

    private int amount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Prevent screen from turning of when sale is active

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItems, "Pos İşlemleri", false, R.drawable.token_logo);
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {

        menuItems.add(new MenuItem("Eşlenikli İade", iListMenuItem -> {
            showMatchedReturnFragment();
        }));

        menuItems.add(new MenuItem("Peşin İade", iListMenuItem -> {
            showReturnFragment();
        }));

        menuItems.add(new MenuItem("Taksitli İade", iListMenuItem -> {
            showInstallments();
        }));

        menuItems.add(new MenuItem("Puan İade", iListMenuItem -> {
            showReturnFragment();
        }));

    }



    private void showMatchedReturnFragment() { // EŞLENİKLİ İADE
        List<CustomInputFormat> inputList = new ArrayList<>();

        inputOrgAmount = new CustomInputFormat("Orijinal Tutar", Amount, null, "Geçersiz Tutar",
                input -> {
                    int amount = input.getText().isEmpty() ? 0 : Integer.parseInt(input.getText());
                    return amount > 0;
                });
        inputList.add(inputOrgAmount);
        inputRetAmount = new CustomInputFormat("İade Tutarı", Amount, null, "Geçersiz Tutar",
                input -> {
                    int amount = input.getText().isEmpty() ? 0 : Integer.parseInt(input.getText());
                    int original = inputOrgAmount.getText().isEmpty() ? 0 : Integer.parseInt(inputOrgAmount.getText());
                    return amount > 0 && amount <= original;
                });
        inputList.add(inputRetAmount);

            inputRefNo = new CustomInputFormat("Ref No", EditTextInputType.Number, 10, "Ref No geçersiz (10 hane)",
                    customInputFormat -> {
                        return !isCurrentDay(inputTranDate.getText()) || isCurrentDay(inputTranDate.getText()) && customInputFormat.getText().length() == 10;
                    });
            inputList.add(inputRefNo);
            inputAuthCode = new CustomInputFormat("Onay Kodu", EditTextInputType.Number, 6, "Onay kodu geçersiz (6 hane)",
                    customInputFormat -> customInputFormat.getText().length() == 6);
            inputList.add(inputAuthCode);

        inputTranDate = new CustomInputFormat("İşlem Tarihi", EditTextInputType.Date, null, "İşlem tarihi geçersiz",
                customInputFormat -> {
                    try {
                        String[] array = customInputFormat.getText().split("/");
                        String date = array[2].substring(2) + array[1] + array[0];
                        Date now = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                        return Integer.parseInt(sdf.format(now)) >= Integer.parseInt(date);
                    } catch (Exception e) {
                    }
                    return false;
                }
        );
        inputList.add(inputTranDate);

        InputListFragment fragment = InputListFragment.newInstance(inputList, "Satış İade", list -> {

        });
        addFragment(R.id.container, fragment, true);
    }


    int myNum = 0;
    public void showReturnFragment() { // İADE
        List<CustomInputFormat> inputList = new ArrayList<>();
        inputList.add(new CustomInputFormat("İade Tutarı", Amount, null, "Hatalı tutar", input -> {
            int ListAmount = input.getText().isEmpty() ? 0 : Integer.parseInt(input.getText());
            try {
                amount = ListAmount;
            } catch(NumberFormatException n) {
                n.printStackTrace();
            }
            return ListAmount > 0;
        }));
        InputListFragment fragment = InputListFragment.newInstance(inputList, "Satış İade", list -> {
            readCard();
        });
        addFragment(R.id.container, fragment, true);
    }

    private void showInstallments() { // TAKSİTLİ İADE
        MenuItemClickListener<MenuItem> listener = menuItem -> {
            installmentCount = 12;
            showMatchedReturnFragment();
        };

        int maxInst = 12;
        List<IListMenuItem> menuItems = new ArrayList<>();
        for (int i = 2; i <= maxInst; i++) {
            MenuItem menuItem = new MenuItem(i + " Taksit", listener);
            //menuItem.setArg(i);
            menuItems.add(menuItem);
        }

        instFragment = ListMenuFragment.newInstance(menuItems, "Taksitli İade", true, R.drawable.token_logo);
        addFragment(R.id.container, instFragment, true);
    }

    private void readCard() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("forceOnline", 1);
            obj.put("zeroAmount", 0);
            obj.put("fallback", 1);

            cardServiceBinding.getCard(amount, 40, obj.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeOutICC() {
        cardServiceBinding.takeOutICC(40);
    }

    private void showInfoDialog() {
        InfoDialog dialog = showInfoDialog(InfoDialog.InfoType.Progress, "Bağlanıyor", false);
        new Handler().postDelayed(() -> {
            dialog.update(InfoDialog.InfoType.Confirmed, "İşlem Başarılı \n Onay kodu: 000782");
            new Handler().postDelayed(() -> {
                dialog.update(InfoDialog.InfoType.Progress, "Belge Oluşturuluyor");
                new Handler().postDelayed(() -> {
                    dialog.dismiss();
                    if (card instanceof ICCCard)
                        takeOutICC();
                    else {
                        finishSale(ResponseCode.SUCCESS);
                    }
                }, 2000);
            }, 2000);
        }, 2000);
    }

    private void finishSale(ResponseCode code) {
        Bundle bundle = new Bundle();
        bundle.putInt("ResponseCode", code.ordinal());
        if (card != null) {
            bundle.putString("CardOwner", card.getOwnerName());
            bundle.putString("CardNumber", card.getCardNumber());
        }
        Intent result = new Intent();
        result.putExtras(bundle);
        setResult(Activity.RESULT_OK, result);

        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onCardDataReceived(String cardData) {

        try {
            JSONObject json = new JSONObject(cardData);
            int type = json.getInt("mCardReadType");

            if (type == CardReadType.ICC.value) {
                ICCCard card = new Gson().fromJson(cardData, ICCCard.class);
                this.card = card;
                showInfoDialog();
            } else if (type == CardReadType.ICC2MSR.value || type == CardReadType.MSR.value || type == CardReadType.KeyIn.value) {
                MSRCard card = new Gson().fromJson(cardData, MSRCard.class);
                this.card = card;
                cardServiceBinding.getOnlinePIN(amount, card.getCardNumber(), 0x0A01, 0, 4, 8, 30);
                //TODO Do transaction after pin verification
            }
            //TODO
            //..check and process other read types
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPinReceived(String pin) {
        showInfoDialog();
    }

    @Override
    public void onICCTakeOut() {
        finishSale(ResponseCode.SUCCESS);
    }


    @Override
    public void onClick(View v) {

    }

    private String getFormattedDate(String dateText) {
        String[] array = dateText.split("/");
        return array[0] + array[1] + array[2].substring(2);
    }

    private boolean isCurrentDay(String dateText) {
        if (dateText.isEmpty()) {
            return false;
        }
        String date = getFormattedDate(dateText);
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        return sdf.format(Calendar.getInstance().getTime()).equals(date);
    }
}
