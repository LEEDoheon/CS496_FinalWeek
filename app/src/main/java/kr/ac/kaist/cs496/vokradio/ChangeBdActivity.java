package kr.ac.kaist.cs496.vokradio;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by q on 2017-07-31.
 */

public class ChangeBdActivity extends AppCompatActivity {

    Switch onairSwitch;
    EditText music1;
    EditText music2;
    EditText music3;
    EditText music4;
    EditText music5;
    EditText music6;
    EditText music7;
    EditText music8;
    Button onairBtn;

    String bdTitle;
    boolean isChecked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_broadcast);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_2);
        TextView appname = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.customBar);
        appname.setTypeface(Typeface.createFromAsset(getAssets(), "Lobster_1.3.otf"));
        bdTitle = getIntent().getStringExtra("title");

        onairSwitch = (Switch) findViewById(R.id.onair_switch);
        onairBtn = (Button) findViewById(R.id.onair_submit);

        music1 = (EditText) findViewById(R.id.music1);
        music2 = (EditText) findViewById(R.id.music2);
        music3 = (EditText) findViewById(R.id.music3);
        music4 = (EditText) findViewById(R.id.music4);
        music5 = (EditText) findViewById(R.id.music5);
        music6 = (EditText) findViewById(R.id.music6);
        music7 = (EditText) findViewById(R.id.music7);
        music8 = (EditText) findViewById(R.id.music8);

        HttpCall.setMethodtext("GET");
        HttpCall.setUrltext("/api/broadcast/"+bdTitle);
        JSONObject bdInfo = new JSONObject();
        isChecked = false;
        try {
            bdInfo = new JSONObject(HttpCall.getResponse());
            isChecked = bdInfo.getString("status").equals("on");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onairSwitch.setChecked(isChecked);

        if (!isChecked) {
            setUseableEditText(music1, isChecked); setUseableEditText(music2, isChecked); setUseableEditText(music3, isChecked); setUseableEditText(music4, isChecked);
            setUseableEditText(music5, isChecked); setUseableEditText(music6, isChecked); setUseableEditText(music7, isChecked); setUseableEditText(music8, isChecked);
        }

        onairSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked = b;
                setUseableEditText(music1, isChecked); setUseableEditText(music2, isChecked); setUseableEditText(music3, isChecked); setUseableEditText(music4, isChecked);
                setUseableEditText(music5, isChecked); setUseableEditText(music6, isChecked); setUseableEditText(music7, isChecked); setUseableEditText(music8, isChecked);
            }
        });

        onairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpCall.setMethodtext("statusPUT");
                HttpCall.setUrltext("/api/onair/"+bdTitle);

                JSONObject body = new JSONObject();
                if (isChecked) try {
                    body.put("status", "on");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                else try {
                    body.put("status", "off");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray songs = new JSONArray();
                if (isChecked) {
                    if (music1.getText().toString().length() > 0)
                        songs.put(music1.getText().toString());
                    if (music2.getText().toString().length() > 0)
                        songs.put(music2.getText().toString());
                    if (music3.getText().toString().length() > 0)
                        songs.put(music3.getText().toString());
                    if (music4.getText().toString().length() > 0)
                        songs.put(music4.getText().toString());
                    if (music5.getText().toString().length() > 0)
                        songs.put(music5.getText().toString());
                    if (music6.getText().toString().length() > 0)
                        songs.put(music6.getText().toString());
                    if (music7.getText().toString().length() > 0)
                        songs.put(music7.getText().toString());
                    if (music8.getText().toString().length() > 0)
                        songs.put(music8.getText().toString());
                }

                try {
                    body.put("songs", songs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("heyhey", body.toString());
                HttpCall.setBody(body.toString());
                HttpCall.getResponse();

                finish();
            }
        });
    }

    private void setUseableEditText(EditText et, boolean useable) {
        et.setClickable(useable);
        et.setEnabled(useable);
        et.setFocusable(useable);
        et.setFocusableInTouchMode(useable);
    }
}
