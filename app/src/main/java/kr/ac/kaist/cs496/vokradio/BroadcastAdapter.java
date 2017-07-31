package kr.ac.kaist.cs496.vokradio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by q on 2017-07-31.
 */

public class BroadcastAdapter extends BaseAdapter{
    ArrayList<BroadcastItem> items = new ArrayList<BroadcastItem>();
    Bitmap bitmap;

    @Override
    public int getCount(){
        return items.size();
    }

    //AddItem using JSONArray
    public void addItem(JSONArray jArray) {
        try{
            for(int i = 0; i < jArray.length();i++){
                BroadcastItem tempItem = new BroadcastItem();
                JSONObject jItem = jArray.getJSONObject(i);

                tempItem.setId(jItem.optString("id"));
                tempItem.setTitle(jItem.optString("title"));
                tempItem.setCategory(jItem.optString("category"));
                tempItem.setDay(jItem.optString("day"));
                tempItem.setTime(jItem.optString("time"));
                tempItem.setThumbnail(jItem.optString("thumbnail"));

                if(!jItem.optString("status").equals("off")){
                    tempItem.setOnAir(true);
                }else{
                    tempItem.setOnAir(false);
                }


                JSONArray tempArray = new JSONArray(jItem.optString("producer"));
                List<String> temps = new ArrayList<String>();
                for(int j = 0; j<tempArray.length();j++) {
                    temps.add(tempArray.get(j).toString());
                }
                tempItem.setProducer(temps);

                tempArray = new JSONArray(jItem.optString("engineer"));
                temps = new ArrayList<String>();
                for(int j = 0; j<tempArray.length();j++) {
                    temps.add(tempArray.get(j).toString());
                }
                tempItem.setEngineer(temps);

                tempArray = new JSONArray(jItem.optString("anouncer"));
                temps = new ArrayList<String>();
                for(int j = 0; j<tempArray.length();j++) {
                    temps.add(tempArray.get(j).toString());
                }
                tempItem.setAnouncer(temps);

                tempArray = new JSONArray(jItem.optString("songs"));
                temps = new ArrayList<String>();
                for(int j = 0; j<tempArray.length();j++) {
                    temps.add(tempArray.get(j).toString());
                }
                tempItem.setSongs(temps);

                items.add(tempItem);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.broadcast_list, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.titleText);
        TextView categoryTextView = (TextView) convertView.findViewById(R.id.categoryText);
        TextView dayTextView = (TextView) convertView.findViewById(R.id.dayText);
        TextView timeTextView = (TextView) convertView.findViewById(R.id.timeText);
        TextView ANNTextView = (TextView) convertView.findViewById(R.id.annText);
        TextView ENGTextView = (TextView) convertView.findViewById(R.id.engText);
        TextView PDTextView = (TextView) convertView.findViewById(R.id.pdText);
        final ImageView backgroundImage = (ImageView) convertView.findViewById(R.id.backgroundImage);
        ImageView onAirImage = (ImageView) convertView.findViewById(R.id.liveImage);

        final BroadcastItem broadcastItem = items.get(position);

        String img_path = broadcastItem.getThumbnail();
        if (img_path != null && img_path.length() > 0) {
            final String img_path_real = "http://52.78.17.108:8080/"+broadcastItem.getThumbnail();
            Thread thread = new Thread() {
                public void run() {
                    InputStream in = null;
                    try {
                        URL url = new URL(img_path_real);
                        URLConnection urlConn = url.openConnection();
                        HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                        httpConn.connect();
                        in = httpConn.getInputStream();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                }
            };
            thread.start();
            try{
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            backgroundImage.setImageBitmap(bitmap);
        }
        titleTextView.setText(broadcastItem.getId());
        categoryTextView.setText(broadcastItem.getCategory());
        dayTextView.setText(broadcastItem.getDay());
        timeTextView.setText(broadcastItem.getTime());
        //ANN
        String temp = "ANN:";
        List<String> temps = broadcastItem.getAnouncer();
        for(int i = 0; i < temps.size();i++){
            temp += " "+ temps.get(i);
        }
        ANNTextView.setText(temp);
        //ENG
        temp = "ENG:";
        temps = broadcastItem.getEngineer();
        for(int i = 0; i < temps.size();i++){
            temp += " "+ temps.get(i);
        }
        ENGTextView.setText(temp);
        //PD
        temp = "PD:";
        temps = broadcastItem.getProducer();
        for(int i = 0; i < temps.size();i++){
            temp += " "+ temps.get(i);
        }
        PDTextView.setText(temp);

        if(broadcastItem.getOnAir()){
            onAirImage.setImageResource(R.drawable.liveicon);
        }

        return convertView;

    }

}
