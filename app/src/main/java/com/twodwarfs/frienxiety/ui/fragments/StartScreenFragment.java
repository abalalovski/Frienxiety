package com.twodwarfs.frienxiety.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aleksandar Balalovski on 6/3/15.
 */
public class StartScreenFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start_screen, container, false);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getName() {
        return Constants.Fragments.START_SCREEN;
    }

    @OnClick(R.id.button_login)
    public void onLogin() {
        showFragment(LoginFragment.newInstance(), true, true);
    }

    @OnClick(R.id.button_register)
    public void onRegister() {
        showFragment(SignUpFragment.newInstance(), true, true);
    }

    public static BaseFragment newInstance() {
        return new StartScreenFragment();
    }

}
