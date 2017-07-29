package kr.ac.kaist.cs496.vokradio;

import android.Manifest;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import wseemann.media.FFmpegMediaPlayer;

public class MainActivity extends AppCompatActivity {

    Button b_play;

    FFmpegMediaPlayer mediaPlayer;

    boolean prepared = false;
    boolean started = false;

    //String stream = "http://143.248.49.224:8000/TechKidda.ogg";
    String stream = "http://37.72.100.39:8025/stream";
    //String stream = "143.248.49.224:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET},0);

        b_play = (Button) findViewById(R.id.b_play);
        b_play.setEnabled(false);
        b_play.setText("LOADING");

        mediaPlayer = new FFmpegMediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerTask().execute(stream);

        b_play.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(started){
                    started = false;
                    mediaPlayer.pause();
                    b_play.setText("PLAY");
                }else{
                    started = true;
                    mediaPlayer.start();
                    b_play.setText("PAUSE");

                }

            }

        });
    }

    private class PlayerTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings){

            try{
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            } catch (IOException e){
                e.printStackTrace();
            }
            return prepared;

        }

        @Override
        protected void onPostExecute (Boolean aBoolean){
            super.onPostExecute(aBoolean);
            mediaPlayer.start();
            b_play.setEnabled(true);
            b_play.setText("PLAY");
        }


    }

    @Override
    protected void onPause(){
        super.onPause();
        if(started){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(started){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prepared){
            mediaPlayer.release();
        }
    }
}
