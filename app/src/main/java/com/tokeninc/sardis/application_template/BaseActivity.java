package com.tokeninc.sardis.application_template;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tokeninc.cardservicebinding.CardServiceBinding;
import com.tokeninc.cardservicebinding.CardServiceListener;

public abstract class BaseActivity extends AppCompatActivity implements CardServiceListener {

    protected CardServiceBinding cardServiceBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardServiceBinding = new CardServiceBinding(this, this);
    }

    @Override
    protected void onDestroy() {
        cardServiceBinding.unBind();
        super.onDestroy();
    }

    /**
     * Callback for Card Service connected method. You will not need to check whether card service is connected or not in most cases.
     * When you make an asynchronous method call on CardServiceBinding,it will be executed automatically as soon as CardService is connected.
     */
    @Override
    public void onCardServiceConnected() {

    }

    /**
     * Callback for TCardService getCard method.
     * As getCard is an asynchronous service operation, you will get the response after operation is done in this callback.
     * @apiNote Override this method in your activity in order to get card data after calling #getData() method of Card Service.
     * @param cardData: Card data json string
     */
    public void onCardDataReceived(String cardData) { }

    /**
     * Callback for TCardService getOnlinePin method.
     * As getOnlinePin is an asynchronous real time operation, you will get the response after operation is done in this callback.
     * @apiNote Override this method in your activity in order to get pin data after calling #getOnlinePin() method of Card Service.
     * @param pin: Pin string
     */
    public  void onPinReceived(String pin) {}

    /**
     * Callback for TCardService iccTakeOut method.
     *
     * @apiNote Override this method in your activity in order to get icc take out callback after calling #iccTakeOut() method of Card Service.
     */
    public  void onICCTakeOut() {}
}
