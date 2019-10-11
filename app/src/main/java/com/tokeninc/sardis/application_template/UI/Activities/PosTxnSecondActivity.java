package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tokeninc.components.ListMenuFragment.IListMenuItem;
import com.tokeninc.components.ListMenuFragment.ListMenuClickListener;
import com.tokeninc.components.ListMenuFragment.ListMenuFragment;
import com.tokeninc.components.infodialog.InfoDialog;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;

import java.util.ArrayList;
import java.util.List;

public class PosTxnSecondActivity extends BaseActivity implements ListMenuClickListener {

    class InfoDialogItem implements IListMenuItem {

        private InfoDialog.InfoType mType;
        private String mText;

        public InfoDialogItem(InfoDialog.InfoType type, String text) {
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
        menuItems.add(new InfoDialogItem(InfoDialog.InfoType.Confirmed, "Confirmed"));
        menuItems.add(new InfoDialogItem(InfoDialog.InfoType.Warning, "Warning"));
        menuItems.add(new InfoDialogItem(InfoDialog.InfoType.Error, "Error"));
        menuItems.add(new InfoDialogItem(InfoDialog.InfoType.Info,"Info"));
        menuItems.add(new InfoDialogItem(InfoDialog.InfoType.Declined, "Declined"));
        menuItems.add(new InfoDialogItem(InfoDialog.InfoType.Connecting,"Connecting"));
        menuItems.add(new InfoDialogItem(InfoDialog.InfoType.Downloading, "Downloading"));
        menuItems.add(new InfoDialogItem(InfoDialog.InfoType.Uploading, "Uploading"));
        menuItems.add(new InfoDialogItem(InfoDialog.InfoType.Processing, "Installing"));
    }

    @Override
    public void onItemClick(int position, IListMenuItem item) {
        showPopup((InfoDialogItem) item);
    }

    private void showPopup(InfoDialogItem item){
        InfoDialog dialog = showInfoDialog(item.mType, item.mText, true);
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
