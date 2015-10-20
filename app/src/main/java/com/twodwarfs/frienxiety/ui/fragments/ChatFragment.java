package com.twodwarfs.frienxiety.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.quickblox.users.model.QBUser;
import com.twodwarfs.frienxiety.BuildConfig;
import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.interfaces.DialogCallback;
import com.twodwarfs.frienxiety.interfaces.LogoutCallback;
import com.twodwarfs.frienxiety.prefs.AppPrefs;
import com.twodwarfs.frienxiety.qbox.QBlox;
import com.twodwarfs.frienxiety.qbox.QBloxHelper;
import com.twodwarfs.frienxiety.qbox.UserHolder;
import com.twodwarfs.frienxiety.services.FrienxietyService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Aleksandar Balalovski on 6/4/15.
 */
public class ChatFragment extends BaseFragment implements DialogCallback {

    @InjectView(R.id.dialog_container)
    ViewGroup mDialogContainer;

    @InjectView(R.id.scrollView_dialog)
    ScrollView mDialogScrollView;

    @InjectView(R.id.editText_message)
    EditText mMessageEditText;

    private Bundle mExtras;

    public static BaseFragment newInstance(Bundle extras) {
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setHasBackButton(true);
        setTitle(getString(R.string.chat_dialog_title));

        if (getArguments() != null) {
            mExtras = getArguments();
        }

        QBlox.instance().setDialogCallback(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mExtras != null) {
            if (mExtras.getBoolean(Constants.Fields.IS_RECEIVING)) {
                addDialog(mExtras.getString(Constants.Fields.MESSAGE), false);
            } else {
                QBlox.instance().sendMessage(mExtras);
            }

            QBUser user = (QBUser) mExtras.getSerializable(Constants.Fields.USER);
            String login = getString(R.string.friend_placeholder);
            if (user != null) {
                if (!TextUtils.isEmpty(user.getLogin())) {
                    login = user.getLogin();
                }
            }

            String title = String.format("%s %s", getString(R.string.chat_dialog_title), login);
            setTitle(title);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        QBlox.instance().setDialogCallback(null);
        getActivity().startService(new Intent(getActivity(), FrienxietyService.class));
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().stopService(new Intent(getActivity(), FrienxietyService.class));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.chat_users_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.chat_end_confirm_title)
                    .backgroundColorRes(R.color.color_accent)
                    .positiveColorRes(R.color.dialog_button_color)
                    .negativeColorRes(R.color.dialog_button_color)
                    .titleColorRes(android.R.color.black)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .contentColorRes(R.color.primary_dark_material_light)
                    .content(R.string.chat_end_confirm)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);

                            QBlox.instance().logoutForChat(new LogoutCallback() {
                                @Override
                                public void onSignOut() {
                                    // nop
                                }

                                @Override
                                public void onLogoutForChat() {
                                    getActivity().finish();
                                }
                            });

                            UserHolder holder = AppPrefs.getUser(getActivity());
                            if (holder.toQBUser() != null) {
                                QBloxHelper.setUserOccupied(holder.toQBUser(), false);
                            }
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

    public void addDialog(String message, boolean isOwn) {
        DialogLineHolder dialogView = new DialogLineHolder(isOwn);
        dialogView.setText(message);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        int right = isOwn ? 20 : 100;
        int left = isOwn ? 100 : 20;
        int top = 20;
        int bottom = 20;
        params.gravity = isOwn ? Gravity.RIGHT : Gravity.LEFT;
        params.setMargins(left, top, right, bottom);

        mDialogContainer.addView(dialogView.getView(), params);
        mDialogScrollView.fullScroll(View.FOCUS_DOWN);

        mMessageEditText.getText().clear();
        hideKeyboard();
    }

    @Override
    protected void initData() {
    }

    @Override
    public String getName() {
        return Constants.Fragments.CHAT_DIALOG;
    }

    @OnClick(R.id.button_send_message)
    public void onSendMessage() {
        if (!TextUtils.isEmpty(mMessageEditText.getText().toString())) {
            QBUser user = (QBUser) mExtras.getSerializable(Constants.Fields.USER);
            String message = mMessageEditText.getText().toString();

            Bundle newExtras = new Bundle();
            newExtras.putSerializable(Constants.Fields.USER, user);
            newExtras.putString(Constants.Fields.MESSAGE, message);

            QBlox.instance().sendMessage(newExtras);
        }
    }

    @Override
    public void onMessageReceived(final Bundle messageExtras) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = messageExtras.getString(Constants.Fields.MESSAGE);
                if (!TextUtils.isEmpty(message)) {
                    addDialog(message, false);
                }
            }
        });
    }

    @Override
    public void onMessageSent(Bundle messageExtras) {
        String message = messageExtras.getString(Constants.Fields.MESSAGE);
        if (!TextUtils.isEmpty(message)) {
            addDialog(message, true);
        }
    }

    private class DialogLineHolder {

        private View mRootView;
        private TextView mMainTitleTextView;

        public DialogLineHolder(boolean isOwn) {
            final Context context = getActivity();
            mRootView = LayoutInflater.from(context).inflate(isOwn ? R.layout.chat_own_line_view :
                    R.layout.chat_user_line_view, null);

            mMainTitleTextView = (TextView) mRootView.findViewById(R.id.textView_main_text);
            mMainTitleTextView.setGravity(isOwn ? Gravity.RIGHT : Gravity.LEFT);
        }

        public void setText(String text) {
            mMainTitleTextView.setText(text);
        }


        public View getView() {
            return mRootView;
        }
    }

}
