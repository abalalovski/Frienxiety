package com.twodwarfs.frienxiety.ui.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.prefs.AppPrefs;
import com.twodwarfs.frienxiety.ui.activities.SplashActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Aleksandar Balalovski on 24.6.15.
 */
public class ColorFragment extends BaseFragment {

    @InjectView(R.id.imageView_color)
    ImageView mColorImageView;

    private int mColor;

    public static BaseFragment newInstance(int colorIndex) {
        ColorFragment colorFragment = new ColorFragment();
        Bundle extras = new Bundle();
        extras.putInt(Constants.Fields.COLOR_INDEX, colorIndex);
        colorFragment.setArguments(extras);
        return colorFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getFragmentManager().popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int colorIndex = getArguments().getInt(Constants.Fields.COLOR_INDEX);
            String[] colors = getResources().getStringArray(R.array.chromo_colors);
            mColor = Color.parseColor(colors[colorIndex]);
        }

        setHasBackButton(true);
        setHasOptionsMenu(true);

        setTitle(getString(R.string.colors_title));

        new MaterialDialog.Builder(getActivity())
                .title(R.string.chromotherapy_title)
                .backgroundColorRes(R.color.color_accent)
                .positiveColorRes(R.color.dialog_button_color)
                .negativeColorRes(R.color.dialog_button_color)
                .titleColorRes(android.R.color.black)
                .positiveText(android.R.string.ok)
                .contentColorRes(R.color.primary_dark_material_light)
                .content(R.string.chromotherapy_confirm)
                .show();

    }

    @Override
    public void onPause() {
        super.onPause();

        if (getActionBar() != null) getActionBar().show();
        getActivity().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActionBar() != null) getActionBar().hide();
        getActivity().getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_color, container, false);
        ButterKnife.inject(this, rootView);

        mColorImageView.setBackgroundColor(mColor);

        String propertyName = "backgroundColor";
        Object[] values = new Object[20];
        for (int i = 0; i < 20; i++) {
            mColor += 4;
            values[i] = new Integer(mColor);
        }

        ArgbEvaluator evaluator = new ArgbEvaluator();
        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(mColorImageView, propertyName, evaluator, values);
        colorAnimator.setDuration(8000);
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.start();

        return rootView;
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getName() {
        return Constants.Fragments.COLOR_FRAGMENT;
    }
}
