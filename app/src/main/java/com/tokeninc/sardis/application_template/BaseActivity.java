package com.tokeninc.sardis.application_template;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tokeninc.sardis.application_template.UI.Fragments.ConfirmationDialog;
import com.tokeninc.sardis.application_template.UI.Fragments.InfoDialogFragment;

public abstract class BaseActivity extends AppCompatActivity {

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
     * @param type: Type of the progress.
     * @param text: The text to be shown under the progress animation/drawable.
     * Dialog can dismissed by calling .dismiss() method of the fragment instance returned from this method.
     */
    protected InfoDialogFragment showInfoDialog(InfoDialogFragment.InfoType type, String text) {
        InfoDialogFragment fragment = InfoDialogFragment.newInstance(type, text);
        fragment.show(getSupportFragmentManager(), "");
        return fragment;
    }

    /**
     * Shows a dialog to the user which asks for a confirmation.
     * @param title: Confirmation dialog title.
     * @param text: Confirmation dialog description.
     * @param confirmButtonText: Confirm button text.
     * @param cancelButtonText: Cancel button text.
     * @param arg: An argument which identifies your current confirmation dialog. Should be a unique identifier for each confirmation dialog that will be shown in your fragment/activity class.
     * @param listener: This is an object which implements {@link ConfirmationDialog.ConfirmationDialogListener} interface to get callbacks from user actions (eg. confirm or cancel).
     * Dialog will be dismissed automatically when user taps on to confirm/cancel button.
     */
    protected void showConfirmationDialog(String title, String text, String confirmButtonText, String cancelButtonText, int arg, ConfirmationDialog.ConfirmationDialogListener listener) {
        ConfirmationDialog dialog = ConfirmationDialog.newInstance(title, text, confirmButtonText, cancelButtonText, arg, listener);
        dialog.show(getSupportFragmentManager(), "");
    }
}
