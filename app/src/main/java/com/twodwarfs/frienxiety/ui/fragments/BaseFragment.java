package com.twodwarfs.frienxiety.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.twodwarfs.frienxiety.R;

/**
 * Created by Aleksandar Balalovski on 6/2/15.
 */

public abstract class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    protected abstract void initData();

    public abstract String getName();

    protected void showProgressDialog(String title, String text) {
        mProgressDialog = ProgressDialog.show(getActivity(), title, text);
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void showFragment(BaseFragment fragment) {
        showFragment(fragment, false, true);
    }

    protected void showFragment(BaseFragment fragment,
                                boolean addToBackStack, boolean animate) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animate) transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out,
                R.animator.fade_in, R.animator.fade_out);
        transaction.replace(((ViewGroup) (getView().getParent())).getId(), fragment, fragment.getName());
        if(addToBackStack) transaction.addToBackStack(fragment.getName());
        transaction.commit();
    }

    protected ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    protected void setTitle(String title) {
        if (getActionBar() != null) {
            getActionBar().setTitle(title);
        }
    }

    public void setHasBackButton(boolean hasBackButton) {
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
        }
    }

    protected void hideKeyboard() {
        if (getActivity() != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputManager != null) {
                View v = getActivity().getCurrentFocus();
                if (v != null) {
                    inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }
}
