package kr.ac.kaist.cs496.vokradio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by q on 2017-07-31.
 */

public class MenuActivity extends AppCompatActivity{

    private ListView mListView;
    BroadcastAdapter adapter;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);

        mListView = (ListView) findViewById(R.id.contacts);
        adapter = new BroadcastAdapter() ;
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                BroadcastItem item = (BroadcastItem) adapter.getItem(position);
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putStringArrayListExtra("songs", new ArrayList<String>(item.getSongs()));
                //intent.putExtra("BJ", "ANN: ")
                startActivity(intent);
                //Toast.makeText(MenuActivity.this, Boolean.toString(item.getOnAir()), Toast.LENGTH_SHORT).show();
            }
        });

        //Get All Broadcasts
        HttpCall.setMethodtext("GET");
        HttpCall.setUrltext("/api/broadcast");

        try {
            JSONArray allbds = new JSONArray(HttpCall.getResponse());
            adapter.addItem(allbds);
        }catch (JSONException e){
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_admin) {
            Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
