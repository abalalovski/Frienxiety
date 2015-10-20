package com.twodwarfs.frienxiety.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.cons.Constants;
import com.twodwarfs.frienxiety.interfaces.LoginCallback;
import com.twodwarfs.frienxiety.prefs.AppPrefs;
import com.twodwarfs.frienxiety.qbox.QBlox;
import com.twodwarfs.frienxiety.qbox.UserHolder;
import com.twodwarfs.frienxiety.ui.activities.MainActivity;
import com.twodwarfs.frienxiety.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Aleksandar Balalovski on 6/2/15.
 */
public class SignUpFragment extends BaseFragment {

    @InjectView(R.id.editText_nickname)
    EditText mNicknameEditText;

    @InjectView(R.id.editText_password)
    EditText mEditTextPassword;

    @InjectView(R.id.editText_password_confirm)
    EditText mEditTextPasswordConfirm;

    @InjectView(R.id.checkBox_helper)
    CheckBox mCheckBoxHelper;

    LoginCallback mLoginCallback = new LoginCallback() {
        @Override
        public void onSignUp(UserHolder holder, boolean success, String msg) {
            String resultToast = success ? getString(R.string.signup_success)
                    : getString(R.string.signup_fail);
            resultToast = TextUtils.isEmpty(msg) ? resultToast : (resultToast += ": " + msg);

            Utils.doToast(getActivity(), resultToast);

            if (success) {
                clearFields();
                AppPrefs.saveUser(getActivity(), holder);
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }

            hideProgressDialog();
        }

        @Override
        public void onLogin(UserHolder holder, boolean success, String msg) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void clearFields() {
        mNicknameEditText.getText().clear();
        mEditTextPassword.getText().clear();
        mEditTextPasswordConfirm.getText().clear();
        mCheckBoxHelper.setChecked(false);
    }

    @OnClick(R.id.button_register)
    public void onRegister() {

        if (passwordValidates()) {
            showProgressDialog("", getString(R.string.registering));

            UserHolder user = new UserHolder();
            user.setNickname(mNicknameEditText.getText().toString());
            user.setPassword(mEditTextPassword.getText().toString());
            user.setIsHelper(mCheckBoxHelper.isChecked());

            QBlox.instance().createSession(false, user, mLoginCallback);
        } else {
            Utils.doToast(getActivity(), getString(R.string.err_passwords));
        }
    }

    private boolean passwordValidates() {
        boolean lengthCheck = mEditTextPassword.getText().length() >= 8;
        boolean samePasswords = mEditTextPassword.getText().toString().equals(
                mEditTextPasswordConfirm.getText().toString());
        return /*samePasswords && */lengthCheck;
    }

    @OnCheckedChanged(R.id.checkBox_helper)
    public void onHelperCheckboxChanged(boolean checked) {
        if (checked) {
            new MaterialDialog.Builder(getActivity())
                    .backgroundColorRes(R.color.color_accent)
                    .positiveColorRes(R.color.dialog_button_color)
                    .negativeColorRes(R.color.dialog_button_color)
                    .titleColorRes(android.R.color.black)
                    .title(R.string.helper_title)
                    .positiveText(R.string.yes)
                    .negativeText(R.string.no)
                    .contentColorRes(R.color.primary_dark_material_light)
                    .content(R.string.helper_disclaimer)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            mCheckBoxHelper.setChecked(false);
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public String getName() {
        return Constants.Fragments.LOGIN;
    }

    public static BaseFragment newInstance() {
        return new SignUpFragment();
    }
}
