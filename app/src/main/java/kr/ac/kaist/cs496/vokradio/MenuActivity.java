package kr.ac.kaist.cs496.vokradio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

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

        adapter.addItem(new BroadcastItem("스크럼직전방송", "매캠"));
        adapter.addItem(new BroadcastItem("스크럼직전방송2", "매캠2"));
        adapter.addItem(new BroadcastItem("스크럼직전방송3", "매캠3"));
        adapter.addItem(new BroadcastItem("스크럼직전방송4", "매캠4"));
        adapter.addItem(new BroadcastItem("스크럼직전방송5", "매캠5"));

    }

}
