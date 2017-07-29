package kr.ac.kaist.cs496.vokradio;

import android.Manifest;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import wseemann.media.FFmpegMediaPlayer;

public class MainActivity extends AppCompatActivity {

    Button b_play;
    SeekBar volumeBar;
    TextView volumeText;

    FFmpegMediaPlayer mediaPlayer;
    AudioManager audioManager;

    boolean prepared = false;
    boolean started = false;

    int volume = 0;

    String stream = "http://37.72.100.39:8025/stream";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET, Manifest.permission.MODIFY_AUDIO_SETTINGS},0);

        b_play = (Button) findViewById(R.id.b_play);
        b_play.setEnabled(false);
        b_play.setText("LOADING");

        mediaPlayer = new FFmpegMediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        new PlayerTask().execute(stream);
        //

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

        //Volume Text (Delete Soon)
        volumeText = (TextView) findViewById(R.id.textView);


        //VolumeBar
        volumeBar = (SeekBar) findViewById(R.id.seekBar2);
        volumeBar.setMax(maxVolume);
        volumeBar.setProgress(curVolume);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onStopTrackingTouch(SeekBar seekBar) {
                //드래그 멈추는 순간
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                //탭하는 순간
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //드래그 하는 도중
                //float volume = (float) (1-(Math.log(100-progress)/Math.log(100)));
                //mediaPlayer.setVolume(volume,volume);
                //mediaPlayer.setVolume((((float) progress)/100)*15, (((float) progress)/100)*15);
                volume = progress;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                volumeText.setText(Integer.toString(volume));
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
