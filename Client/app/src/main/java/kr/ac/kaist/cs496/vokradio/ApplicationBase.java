package kr.ac.kaist.cs496.vokradio;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by q on 2017-08-01.
 */

public class ApplicationBase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "BMJUA_ttf.ttf"))
                .addBold(Typekit.createFromAsset(this, "BMJUA_ttf.ttf"));
    }
}
