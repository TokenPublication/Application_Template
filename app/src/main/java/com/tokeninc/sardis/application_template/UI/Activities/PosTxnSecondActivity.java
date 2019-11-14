package com.tokeninc.sardis.application_template.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.token.components.ListMenuFragment.IListMenuItem;
import com.token.components.ListMenuFragment.ListMenuClickListener;
import com.token.components.ListMenuFragment.ListMenuFragment;
import com.token.components.infodialog.InfoDialog;
import com.tokeninc.sardis.application_template.BaseActivity;
import com.tokeninc.sardis.application_template.R;

import java.util.ArrayList;
import java.util.List;

public class PosTxnSecondActivity extends BaseActivity implements ListMenuClickListener {

    class InfoDialogItem implements IListMenuItem {

        private InfoDialog.InfoType mType;
        private String mText;
        private int mID;

        public InfoDialogItem(int ID, InfoDialog.InfoType type, String text) {
            mType = type;
            mText = text;
            mID = ID;
        }

        @Override
        public String getName() {
            return mText;
        }

        @Nullable
        @Override
        public List<IListMenuItem> getSubMenuItemList() {
            return null;
        }

        @Override
        public int getId() {
            return mID;
        }
    }

    private List<IListMenuItem> menuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_txn);

        prepareData();
        ListMenuFragment fragment = ListMenuFragment.newInstance(menuItems, this, "");
        addFragment(R.id.container, fragment, false);
    }

    private void prepareData() {
        menuItems.add(new InfoDialogItem(0, InfoDialog.InfoType.Confirmed, "Confirmed"));
        menuItems.add(new InfoDialogItem(1,InfoDialog.InfoType.Warning, "Warning"));
        menuItems.add(new InfoDialogItem(2,InfoDialog.InfoType.Error, "Error"));
        menuItems.add(new InfoDialogItem(3,InfoDialog.InfoType.Info,"Info"));
        menuItems.add(new InfoDialogItem(4,InfoDialog.InfoType.Declined, "Declined"));
        menuItems.add(new InfoDialogItem(5,InfoDialog.InfoType.Connecting,"Connecting"));
        menuItems.add(new InfoDialogItem(6,InfoDialog.InfoType.Downloading, "Downloading"));
        menuItems.add(new InfoDialogItem(7,InfoDialog.InfoType.Uploading, "Uploading"));
        menuItems.add(new InfoDialogItem(8,InfoDialog.InfoType.Processing, "Installing"));
    }

    @Override
    public void onItemClick(IListMenuItem item) {
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
