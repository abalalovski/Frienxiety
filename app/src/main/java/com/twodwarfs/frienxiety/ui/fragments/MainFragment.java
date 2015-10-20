package com.twodwarfs.frienxiety.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.prefs.AppPrefs;
import com.twodwarfs.frienxiety.qbox.QBlox;
import com.twodwarfs.frienxiety.ui.activities.ChatActivity;
import com.twodwarfs.frienxiety.ui.activities.RelaxationActivity;
import com.twodwarfs.frienxiety.ui.activities.SplashActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Aleksandar Balalovski on 6/2/15.
 */

public class MainFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        setTitle(getString(R.string.app_name));
        setHasOptionsMenu(true);
        setHasBackButton(false);

        if (!QBlox.instance().isChatInitialized()) {
            QBlox.instance().initChatService(getActivity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.inject(this, rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // settings
        } else if (id == R.id.action_logout) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.logout_confirm_title)
                    .backgroundColorRes(R.color.color_accent)
                    .positiveColorRes(R.color.dialog_button_color)
                    .negativeColorRes(R.color.dialog_button_color)
                    .titleColorRes(android.R.color.black)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .contentColorRes(R.color.primary_dark_material_light)
                    .content(R.string.logout_confirm)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);

                            AppPrefs.clearCredentials(getActivity());
                            startActivity(new Intent(getActivity(), SplashActivity.class));
                            getActivity().finish();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            dialog.dismiss();
                        }
                    }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getName() {
        return Constants.Fragments.MAIN;
    }


    @OnClick(R.id.cardView_needHelp)
    public void onNeedHelpNow() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title(R.string.hello_there)
                .backgroundColorRes(R.color.color_accent)
                .positiveColorRes(R.color.dialog_button_color)
                .negativeColorRes(R.color.dialog_button_color)
                .titleColorRes(android.R.color.black)
                .positiveText(R.string.agree)
                .negativeText(android.R.string.cancel)
                .contentColorRes(R.color.primary_dark_material_light)
                .content(R.string.disclaimer)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);

                        AppPrefs.setChatDisclaimerAccepted(getActivity(), true);

                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        dialog.dismiss();
                    }
                });

        if (AppPrefs.isChatDisclaimerAccepted(getActivity())) {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        } else {
            builder.show();
        }
    }

    @OnClick(R.id.cardView_mood)
    public void onMood() {
        startActivity(new Intent(getActivity(), RelaxationActivity.class));
    }

    @OnClick(R.id.cardView_reminders)
    public void onReminders() {
    }

    public static BaseFragment newInstance() {
        return new MainFragment();
    }
}
