package com.tokeninc.sardis.application_template;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.tokeninc.components.infodialog.InfoDialog;
import com.tokeninc.components.infodialog.InfoDialogListener;
import com.tokeninc.customerservice.CustomerScreenServiceBinding;

public abstract class BaseActivity extends AppCompatActivity {

    protected CustomerScreenServiceBinding customerScreenService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerScreenService = new CustomerScreenServiceBinding(this);
    }

    @Override
    protected void onDestroy() {
        customerScreenService.unBind(this);
        super.onDestroy();
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
}
