package com.twodwarfs.frienxiety.ui.activities;

import android.os.Bundle;
import android.os.Handler;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.prefs.AppPrefs;
import com.twodwarfs.frienxiety.ui.fragments.StartScreenFragment;

import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.inject(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppPrefs.isLoggedIn(SplashActivity.this)) {
                    startActivityHelper(MainActivity.class);
                    finish();
                } else {
                    showLoginControls();
                }
            }
        }, Constants.SPLASH_DELAY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void showLoginControls() {
        showFragment(R.id.container_user_ops, StartScreenFragment.newInstance(), false);
    }
}