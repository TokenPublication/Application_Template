package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;
import com.tokeninc.sardis.application_template.UI.Definitions.IListMenuItem;
import com.tokeninc.sardis.application_template.UI.Fragments.InfoDialogFragment;
import com.tokeninc.sardis.application_template.UI.Fragments.ListMenuFragment;

import java.util.ArrayList;
import java.util.List;

public class PosTxnActivity extends BaseActivity implements ListMenuFragment.ListMenuClickListener {

    class InfoDialogItem implements IListMenuItem {

        private InfoDialogFragment.InfoType mType;
        private String mText;

        public InfoDialogItem(InfoDialogFragment.InfoType type, String text) {
            mType = type;
            mText = text;
        }

        @Override
        public String getName() {
            return mText;
        }
    }

    private List<IListMenuItem> menuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_txn);

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItems, this);
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {
        menuItems.add(new InfoDialogItem(InfoDialogFragment.InfoType.Confirmed, "Confirmed"));
        menuItems.add(new InfoDialogItem(InfoDialogFragment.InfoType.Warning, "Warning"));
        menuItems.add(new InfoDialogItem(InfoDialogFragment.InfoType.Error, "Error"));
        menuItems.add(new InfoDialogItem(InfoDialogFragment.InfoType.Info,"Info"));
        menuItems.add(new InfoDialogItem(InfoDialogFragment.InfoType.Declined, "Declined"));
        menuItems.add(new InfoDialogItem(InfoDialogFragment.InfoType.Connecting,"Connecting"));
        menuItems.add(new InfoDialogItem(InfoDialogFragment.InfoType.Downloading, "Downloading"));
        menuItems.add(new InfoDialogItem(InfoDialogFragment.InfoType.Uploading, "Uploading"));
        menuItems.add(new InfoDialogItem(InfoDialogFragment.InfoType.Processing, "Installing"));
    }

    @Override
    public void onItemClick(int position, IListMenuItem item) {
        showPopup((InfoDialogItem) item);
    }

    private void showPopup(InfoDialogItem item){
        InfoDialogFragment dialog = showInfoDialog(item.mType, item.mText);
        //Dismiss dialog by calling dialog.dismiss() when needed.
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
}
