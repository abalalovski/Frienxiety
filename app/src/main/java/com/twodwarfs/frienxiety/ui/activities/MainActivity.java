package com.twodwarfs.frienxiety.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.services.FrienxietyService;
import com.twodwarfs.frienxiety.ui.fragments.MainFragment;

public class MainActivity extends BaseChatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        showFragment(R.id.container_user_fragments,
                MainFragment.newInstance(), false, false);

//        if (AppPrefs.isHelper(this) && TextUtils.isEmpty(AppPrefs.getUserStatus(this))) {
//            UserStatusDialog userStatusDialog = UserStatusDialog.newInstance();
//            userStatusDialog.show(getFragmentManager(), "user_status_dialog");
//            userStatusDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    Utils.doToast(MainActivity.this, getString(R.string.start_app_now));
//                }
//            });
//        }

        startService(new Intent(this, FrienxietyService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStartChat(Bundle messageExtras) {

    }
}
