package kr.ac.kaist.cs496.vokradio;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by q on 2017-07-31.
 */

public class CreateBdActivity extends AppCompatActivity {

    final int REQ_CODE_SELECT_IMAGE = 999;

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
    ImageButton submit_creation;
    boolean img_selected = false;

    File img_thumbnail;
    ArrayList<String> Producers = new ArrayList<>();
    ArrayList<String> Engineers = new ArrayList<>();
    ArrayList<String> Anouncers = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast_create);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_2);
        TextView appname = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.customBar);
        appname.setTypeface(Typeface.createFromAsset(getAssets(), "Lobster_1.3.otf"));

        askForPermissions();

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

        select_thumbnail = (Button) findViewById(R.id.select_thumbnail);
        select_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        submit_creation = (ImageButton) findViewById(R.id.submit_broadcast_create);
        submit_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpCall.setMethodtext("POST");
                HttpCall.setUrltext("/api/addbroadcast");

                JSONArray JSON_producer = new JSONArray();
                if (bdProducer1.getText().toString().length() > 0) JSON_producer.put(bdProducer1.getText().toString());
                if (bdProducer2.getText().toString().length() > 0) JSON_producer.put(bdProducer2.getText().toString());

                JSONArray JSON_engineer = new JSONArray();
                if (bdEngineer1.getText().toString().length() > 0) JSON_engineer.put(bdEngineer1.getText().toString());
                if (bdEngineer2.getText().toString().length() > 0) JSON_engineer.put(bdEngineer2.getText().toString());

                JSONArray JSON_anouncer = new JSONArray();
                if (bdAnouncer1.getText().toString().length() > 0) JSON_anouncer.put(bdAnouncer1.getText().toString());
                if (bdAnouncer2.getText().toString().length() > 0) JSON_anouncer.put(bdAnouncer2.getText().toString());
                if (bdAnouncer3.getText().toString().length() > 0) JSON_anouncer.put(bdAnouncer3.getText().toString());
                if (bdAnouncer4.getText().toString().length() > 0) JSON_anouncer.put(bdAnouncer4.getText().toString());

                JSONObject bodyJSON = new JSONObject();
                try {
                    bodyJSON.put("id", bdTitle.getText().toString())
                            .put("title", bdTitle.getText().toString())
                            .put("category", bdCategory.getSelectedItem().toString())
                            .put("day", bdDay.getSelectedItem().toString())
                            .put("time", bdStartTime.getText().toString()+" ~ "+bdEndTime.getText().toString())
                            .put("producer", JSON_producer)
                            .put("engineer", JSON_engineer)
                            .put("anouncer", JSON_anouncer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HttpCall.setBody(bodyJSON.toString());
                HttpCall.getResponse();

                HttpCall.setMethodtext("GET");
                HttpCall.setUrltext("/api/broadcast/"+bdTitle.getText().toString());
                JSONObject toGetId = new JSONObject();
                try {
                    toGetId = new JSONObject(HttpCall.getResponse());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (img_selected) {
                    HttpCall.setMethodtext("imgPUT");
                    HttpCall.setUrltext("/api/uploadimage/"+bdTitle.getText().toString());
                    HttpCall.setThumbnail(img_thumbnail);
                    try {
                        HttpCall.setIdtext(toGetId.getString("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpCall.getResponse();
                }

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    Log.d("ImgSuccess", "ASDF");
                    Uri uri = data.getData();
                    String[] filepath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getBaseContext().getContentResolver().query(uri, filepath, null, null, null);
                    cursor.moveToFirst();
                    final String imagepath = cursor.getString(cursor.getColumnIndex(filepath[0]));
                    cursor.close();

                    img_thumbnail = new File(imagepath);
                    img_selected = true;
                    select_thumbnail.setText("SELECTED");
                    select_thumbnail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_selected, 0, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
