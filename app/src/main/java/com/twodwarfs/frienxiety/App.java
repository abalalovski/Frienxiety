package com.twodwarfs.frienxiety;

import android.app.Application;

import com.twodwarfs.frienxiety.qbox.QBlox;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Aleksandar Balalovski on 6/2/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        QBlox.instance().init(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/RobotoCondensed-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
