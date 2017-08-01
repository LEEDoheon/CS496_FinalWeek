package kr.ac.kaist.cs496.vokradio;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by q on 2017-08-01.
 */

public class IntroActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView c = (ImageView) findViewById(R.id.vok_logo);
        YoYo.with(Techniques.FadeIn).duration(1500).playOn(c);

        TextView b = (TextView) findViewById(R.id.vok_text);
        b.setTypeface(Typeface.createFromAsset(getAssets(), "kiddySans.otf"));
        YoYo.with(Techniques.FadeIn).duration(1800).playOn(b);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MenuActivity.class);
                startActivity(intent);

                finish();
            }
        }, 4000);
    }
}
