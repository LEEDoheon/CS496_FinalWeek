package kr.ac.kaist.cs496.vokradio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by q on 2017-07-31.
 */

public class CreateBdActivity extends AppCompatActivity {

    EditText bdTitle;
    Spinner bdCategory;
    Spinner bdDay;
    EditText bdStartTime;
    EditText bdEndTime;
    EditText bdProducer1;
    EditText bdProducer2;
    EditText bdEngineer1;
    EditText bdEngineer2;
    EditText bdAnouncer1;
    EditText bdAnouncer2;
    EditText bdAnouncer3;
    EditText bdAnouncer4;

    Button select_thumbnail;
    Button submit_creation;

    File img_thumbnail;
    ArrayList<String> Producers = new ArrayList<>();
    ArrayList<String> Engineers = new ArrayList<>();
    ArrayList<String> Anouncers = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast_create);

        bdTitle = (EditText) findViewById(R.id.create_title);
        bdCategory = (Spinner) findViewById(R.id.create_category);
        bdDay = (Spinner) findViewById(R.id.create_day);
        bdStartTime = (EditText) findViewById(R.id.create_startTime);
        bdEndTime = (EditText) findViewById(R.id.create_endTime);
        bdProducer1 = (EditText) findViewById(R.id.create_producer1);
        bdProducer2 = (EditText) findViewById(R.id.create_producer2);
        bdEngineer1 = (EditText) findViewById(R.id.create_engineer1);
        bdEngineer2 = (EditText) findViewById(R.id.create_engineer2);
        bdAnouncer1 = (EditText) findViewById(R.id.create_anouncer1);
        bdAnouncer2 = (EditText) findViewById(R.id.create_anouncer2);
        bdAnouncer3 = (EditText) findViewById(R.id.create_anouncer3);
        bdAnouncer4 = (EditText) findViewById(R.id.create_anouncer4);

        select_thumbnail = (Button) findViewById(R.id.submit_broadcast_create);
        select_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpCall.setTitletext(bdTitle.getText().toString());
                HttpCall.setCategorytext(bdCategory.getSelectedItem().toString());
                HttpCall.setDaytext(bdDay.getSelectedItem().toString());
                HttpCall.setTimetext(bdStartTime.getText().toString()+" ~ "+bdEndTime.getText().toString());
                HttpCall.setIdtext(bdDay.getSelectedItem().toString()+bdStartTime.getText().toString()+bdEndTime.getText().toString());

                if (bdProducer1.getText().toString() != null && bdProducer1.getText().toString().equals("")) Producers.add(bdProducer1.getText().toString());
                if (bdProducer2.getText().toString() != null && bdProducer2.getText().toString().equals("")) Producers.add(bdProducer2.getText().toString());
                if (bdEngineer1.getText().toString() != null && bdEngineer1.getText().toString().equals("")) Engineers.add(bdEngineer1.getText().toString());
                if (bdEngineer2.getText().toString() != null && bdEngineer2.getText().toString().equals("")) Engineers.add(bdEngineer2.getText().toString());
                if (bdAnouncer1.getText().toString() != null && bdAnouncer1.getText().toString().equals("")) Anouncers.add(bdAnouncer1.getText().toString());
                if (bdAnouncer2.getText().toString() != null && bdAnouncer2.getText().toString().equals("")) Anouncers.add(bdAnouncer2.getText().toString());
                if (bdAnouncer3.getText().toString() != null && bdAnouncer3.getText().toString().equals("")) Anouncers.add(bdAnouncer3.getText().toString());
                if (bdAnouncer4.getText().toString() != null && bdAnouncer4.getText().toString().equals("")) Anouncers.add(bdAnouncer4.getText().toString());
                HttpCall.setProducer(Producers);
                HttpCall.setEngineer(Engineers);
                HttpCall.setAnouncer(Anouncers);

                HttpCall.setMethodtext("POST");
                HttpCall.setUrltext("/api/addbroadcast");
                HttpCall.getResponse();
            }
        });
    }
}
