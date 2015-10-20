package com.twodwarfs.frienxiety.ui.activities;

import android.os.Bundle;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.ui.fragments.RelaxationFragment;

public class RelaxationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxation);
        initToolbar();

        showFragment(R.id.container_relaxation_fragments,
                RelaxationFragment.newInstance(), false, false);
    }
}
