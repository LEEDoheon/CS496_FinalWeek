package kr.ac.kaist.cs496.vokradio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Request;

/**
 * Created by q on 2017-07-30.
 */

public class SettingActivity extends AppCompatActivity {

    Context context;
    Button loginButton;

    TextView userJob;
    TextView userName;
    Button bdcreateButton;
    Button logoutButton;
    ListView adminListView;

    ArrayList<Cast> castList = new ArrayList<>();
    ArrayList<Cast> items = new ArrayList<>();
    CustomAdapter adapter;

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

            try {
                getBroadCasts();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adminListView = (ListView) findViewById(R.id.adminListView);
            adapter = new CustomAdapter(this, R.layout.admin_row, castList);
            adminListView.setAdapter(adapter);
            adminListView.setOnItemClickListener(adminListListener);
        }
    }

    public class Cast {
        String title;
        String category;
        boolean status;
    }

    void getBroadCasts() throws JSONException {
        castList.clear();
        HttpCall.setMethodtext("GET");
        HttpCall.setUrltext("/api/broadcast");
        JSONArray AllofBroadcast = new JSONArray(HttpCall.getResponse());
        for (int i=0;i<AllofBroadcast.length();i++) {
            Cast element = new Cast();
            if (AllofBroadcast.getJSONObject(i).toString().contains("title")
                    && AllofBroadcast.getJSONObject(i).toString().contains("category")
                    && AllofBroadcast.getJSONObject(i).toString().contains("status")) {
                element.title = AllofBroadcast.getJSONObject(i).getString("title");
                element.category = AllofBroadcast.getJSONObject(i).getString("category");
                element.status = AllofBroadcast.getJSONObject(i).getString("status").equals("on");
                castList.add(element);
            }
        }
    }

    private class CustomAdapter extends ArrayAdapter<Cast> {
        public CustomAdapter(Context context, int textViewResourceId, ArrayList<Cast> objects) {
            super(context, textViewResourceId, objects);
            items = new ArrayList<>();
            items.addAll(objects);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Cast getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.admin_row, null);
            }
            Log.d("STEP1", items.get(0).title);

            TextView rowTitle = (TextView) v.findViewById(R.id.row_title);
            TextView rowCategory = (TextView) v.findViewById(R.id.row_category);
            ImageView rowStatus = (ImageView) v.findViewById(R.id.row_status);

            rowTitle.setText(items.get(position).title);
            rowCategory.setText(items.get(position).category);
            if (!items.get(position).status) rowStatus.setImageResource(R.drawable.grey_dot);
            if (items.get(position).status) rowStatus.setImageResource(R.drawable.red_dot);

            return v;
        }
    }

    private AdapterView.OnItemClickListener adminListListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final Cast cast = castList.get(i);
            final String[] todo = new String[] {"방송 상태 변경", "방송 수정", "방송 삭제"};
            AlertDialog.Builder ad = new AlertDialog.Builder(context);

            ad.setTitle(cast.title);
            ad.setItems(
                    todo, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {
                                Intent intent = new Intent(context, ChangeBdActivity.class);
                                intent.putExtra("title", cast.title);
                                startActivity(intent);
                            } else if (i == 1) {
                                /*
                                Intent intent = new Intent(context, ModifyBdActivity.class);
                                intent.putExtra("title", cast.title);
                                startActivity(intent);
                                */
                            } else if (i == 2) {
                                HttpCall.setMethodtext("DELETE");
                                HttpCall.setUrltext("/api/broadcast/"+cast.title);
                                HttpCall.getResponse();
                            }
                        }
                    }
            );
            ad.setNeutralButton("닫기", null).show();
        }
    };
}
