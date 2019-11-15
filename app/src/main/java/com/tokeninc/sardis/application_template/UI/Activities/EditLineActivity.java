package com.tokeninc.sardis.application_template.UI.Activities;

import android.os.Bundle;

import com.token.components.CustomInput.CustomInputFormat;
import com.token.components.CustomInput.EditTextInputType;
import com.token.components.CustomInput.InputListFragment;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;

import java.util.ArrayList;
import java.util.List;
/*
This activity shows how to use edit line list and info fragment
 */
public class EditLineActivity extends BaseActivity {
    private List<CustomInputFormat> inputList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editline);

        inputList = new ArrayList<>();

        inputList.add(new CustomInputFormat("Card Number", EditTextInputType.CreditCardNumber, 19, null, null));
        inputList.add(new CustomInputFormat("Expiry Date", EditTextInputType.ExpiryDate, null, null, null));
        inputList.add(new CustomInputFormat("CVV", EditTextInputType.CVV, null, null, null));

        InputListFragment fragment = InputListFragment.newInstance(inputList);
        addFragment(R.id.container, fragment, false);

    }
}
