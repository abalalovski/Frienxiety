package com.twodwarfs.frienxiety.ui.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.ui.fragments.BaseFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Aleksandar Balalovski on 6/2/15.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count > 0) {
            manager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected void showFragment(int resId, BaseFragment fragment) {
        showFragment(resId, fragment, true);
    }

    protected void showFragment(int resId, BaseFragment fragment,
                                boolean addToBackstack) {
        showFragment(resId, fragment, addToBackstack, true);
    }

    protected void showFragment(int resId, BaseFragment fragment,
                                boolean addToBackstack, boolean animate) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (animate) transaction.setCustomAnimations(R.animator.fade_in, 0);
        transaction.replace(resId, fragment, fragment.getName());
        if (addToBackstack) transaction.addToBackStack(fragment.getName());
        transaction.commit();
    }

    protected void startActivityHelper(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    protected void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void showProgressDialog(String title, String text) {
        mProgressDialog = ProgressDialog.show(this, title, text);
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


}
