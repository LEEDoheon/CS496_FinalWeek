package kr.ac.kaist.cs496.vokradio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

}
