package com.tokeninc.sardis.application_template.UI.Activities;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.token.components.CustomInput.CustomInputAdapter;
import com.token.components.CustomInput.CustomInputFormat;
import com.token.components.CustomInput.EditTextInputType;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;

import java.util.ArrayList;
import java.util.List;
/*
This activity shows how to use edit line list and info fragment
 */
public class EditLineActivity extends BaseActivity {
    private List<CustomInputFormat> inputList;
    private RecyclerView recyclerView;
    private CustomInputAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editline);

        recyclerView = findViewById(R.id.inputRecyclerView);

        inputList = new ArrayList<>();

        inputList.add(new CustomInputFormat("Card Number", EditTextInputType.CreditCardNumber, false, null, null));
        inputList.add(new CustomInputFormat("Expiry Date", EditTextInputType.ExpiryDate, false, null, null));
        inputList.add(new CustomInputFormat("CVV", EditTextInputType.CVV, false, null, null));

        adapter = new CustomInputAdapter(inputList);
        recyclerView.setAdapter(adapter);
    }
}
