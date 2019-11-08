package com.tokeninc.sardis.application_template;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.tokeninc.cardservicebinding.CardServiceBinding;
import com.tokeninc.components.infodialog.InfoDialog;
import com.tokeninc.components.infodialog.InfoDialogListener;
import com.tokeninc.customerservice.CustomerScreenServiceBinding;

public abstract class BaseActivity extends AppCompatActivity {

    protected CustomerScreenServiceBinding customerScreenService;
    protected CardServiceBinding cardServiceBinding;
    private BroadcastReceiver cardServiceReceiver;
    private MutableLiveData<String> cardData;
    private MutableLiveData<String> pinData;
    private MutableLiveData<Boolean> iccTakeOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerScreenService = new CustomerScreenServiceBinding(this);
        cardServiceBinding = new CardServiceBinding(this, null);
        registerBroadCastReceiver();
    }

    @Override
    protected void onDestroy() {
        if (cardServiceBinding != null) {
            cardServiceBinding.unBind(this);
        }
        if (cardServiceReceiver != null) {
            unregisterReceiver(cardServiceReceiver);
        }
        if (customerScreenService != null) {
            customerScreenService.unBind(this);
        }
        super.onDestroy();
    }

    private void registerBroadCastReceiver() {
        cardData = new MutableLiveData<>();
        pinData = new MutableLiveData<>();
        iccTakeOut = new MutableLiveData<>();
        cardData.observe(this, this::onCardDataReceived);
        pinData.observe(this, this::onPinReceived);
        iccTakeOut.observe(this, (value) -> {
            onICCTakeOut();
        });

        cardServiceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Template", "BroadCastReceived");
                if (intent.hasExtra("cardData")) {
                    cardData.setValue(intent.getStringExtra("cardData"));
                }
                else if (intent.hasExtra("PIN")) {
                    pinData.setValue(intent.getStringExtra("PIN"));
                }
                else if (intent.hasExtra("iccTakeOut")) {
                    iccTakeOut.setValue(true);
                }
            }
        };

        //Every app should register action named with its own package name
        IntentFilter filter = new IntentFilter(this.getPackageName());
        registerReceiver(cardServiceReceiver, filter);
    }

    protected void addFragment(@IdRes Integer resourceId, Fragment fragment, Boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(resourceId, fragment);
        if (addToBackStack) {
            ft.addToBackStack("");
        }
        ft.commit();
    }

    protected void replaceFragment(@IdRes Integer resourceId, Fragment fragment, Boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(resourceId, fragment);
        if (addToBackStack) {
            ft.addToBackStack("");
        }
        ft.commit();
    }

    protected void removeFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    /**
     * Shows a dialog to the user which informs the user about current progress.
     * See {@link InfoDialog#newInstance(InfoDialog.InfoType, String, boolean)}
     * Dialog can dismissed by calling .dismiss() method of the fragment instance returned from this method.
     */
    protected InfoDialog showInfoDialog(InfoDialog.InfoType type, String text, boolean isCancelable) {
        InfoDialog fragment = InfoDialog.newInstance(type, text, isCancelable);
        fragment.show(getSupportFragmentManager(), "");
        return fragment;
    }

    /**
     * Shows a dialog to the user which asks for a confirmation.
     * Dialog will be dismissed automatically when user taps on to confirm/cancel button.
     * See {@link InfoDialog#newInstance(InfoDialog.InfoType, String, String, InfoDialog.InfoDialogButtons, int, InfoDialogListener)}
     */
    protected InfoDialog showConfirmationDialog(InfoDialog.InfoType type, String title, String info, InfoDialog.InfoDialogButtons buttons, int arg, InfoDialogListener listener) {
        InfoDialog dialog = InfoDialog.newInstance(type, title, info, buttons, arg, listener);
        dialog.show(getSupportFragmentManager(), "");
        return dialog;
    }


    /**
     * Callback for TCardService getCard method.
     * As getCard is an asynchronous service operation, you will get the response after operation is done in this callback.
     * @apiNote Override this method in your activity in order to get card data after calling #getData() method of Card Service.
     * @param cardData: Card data json string
     */
    protected void onCardDataReceived(String cardData) { }

    /**
     * Callback for TCardService getOnlinePin method.
     * As getOnlinePin is an asynchronous real time operation, you will get the response after operation is done in this callback.
     * @apiNote Override this method in your activity in order to get pin data after calling #getOnlinePin() method of Card Service.
     * @param pin: Pin string
     */
    protected  void onPinReceived(String pin) {}

    /**
     * Callback for TCardService iccTakeOut method.
     *
     * @apiNote Override this method in your activity in order to get icc take out callback after calling #iccTakeOut() method of Card Service.
     */
    protected  void onICCTakeOut() {}
}
