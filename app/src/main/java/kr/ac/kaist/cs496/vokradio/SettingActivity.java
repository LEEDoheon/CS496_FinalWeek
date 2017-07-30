package kr.ac.kaist.cs496.vokradio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by q on 2017-07-30.
 */

public class SettingActivity extends AppCompatActivity {

    Context context;
    Button loginButton;

    TextView userJob;
    TextView userName;
    Button bdcreateButton;
    Button bdmodifyButton;
    Button bdonairButton;
    Button logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if (!UserInfo.isLogined()) {
            setContentView(R.layout.activity_setting_guest);
            loginButton = (Button) findViewById(R.id.login_button);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            setContentView(R.layout.activity_setting_admin);
            userJob = (TextView) findViewById(R.id.user_job);
            userName = (TextView) findViewById(R.id.user_name);
            userJob.setText("제 "+UserInfo.getNumber()+"대 "+UserInfo.getJob()+"부");
            userName.setText(UserInfo.getName());

            bdcreateButton = (Button) findViewById(R.id.create_broadcast);
            bdcreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CreateBdActivity.class);
                    startActivity(intent);
                }
            });

            logoutButton = (Button) findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserInfo.Logout();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
    }
}
