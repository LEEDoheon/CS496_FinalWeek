package kr.ac.kaist.cs496.vokradio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by q on 2017-07-30.
 */

public class LoginActivity extends AppCompatActivity {

    Context context;
    EditText loginId;
    EditText loginPw;
    Button loginSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_2);
        TextView appname = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.customBar);
        appname.setTypeface(Typeface.createFromAsset(getAssets(), "Lobster_1.3.otf"));
        context = this;

        loginId = (EditText) findViewById(R.id.login_id);
        loginPw = (EditText) findViewById(R.id.login_pw);
        loginSubmit = (Button) findViewById(R.id.login_submit);

        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginId.getText().toString().length() == 0) {
                    Toast.makeText(context, "Please enter your EMAIL", Toast.LENGTH_SHORT).show();
                    return;
                } else if (loginPw.getText().toString().length() == 0) {
                    Toast.makeText(context, "Please enter your PW", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String user_id = loginId.getText().toString();
                    String user_pw = loginPw.getText().toString();
                    HttpCall.setMethodtext("GET");
                    HttpCall.setUrltext("/api/admin");
                    String userlist = HttpCall.getResponse();
                    if (!userlist.contains(user_id)) {
                        Toast.makeText(context, "Yout EMAIL doesn't exist", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        HttpCall.setMethodtext("GET");
                        HttpCall.setUrltext("/api/admin/"+user_id);
                        String pwd = new String();
                        try {
                            JSONObject userinfo = new JSONObject(HttpCall.getResponse());
                            pwd = userinfo.getString("password");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!pwd.equals(user_pw)) {
                            Toast.makeText(context, "Yout PW doesn't match", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            try {
                                UserInfo.Login(user_id);
                                Log.d("Login Successful", user_id);
                                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                ///
            }
        });
    }
}
