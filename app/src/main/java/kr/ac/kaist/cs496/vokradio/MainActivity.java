package kr.ac.kaist.cs496.vokradio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaPlayer;

public class MainActivity extends AppCompatActivity {

    SeekBar volumeBar;
    ImageView backgroundImage;
    ImageView circularImage;
    ImageView control;
    ImageView syncImage;

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

        //BackgroundImage Blurring
        backgroundImage = (ImageView) findViewById(R.id.backgroundImageView);
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.landscape);
        Bitmap blurredBitmap = BlurBuilder.blur(this, drawable.getBitmap());
        backgroundImage.setImageBitmap(blurredBitmap);

        //CircularImage Cropping
        circularImage = (ImageView) findViewById(R.id.circularImageView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularImage.setBackground(new ShapeDrawable(new OvalShape()));
            circularImage.setClipToOutline(true);
        }

        // PlayButton Control
        control = (ImageView) findViewById(R.id.control);
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!started) {
                    mediaPlayer.start();
                    control.setBackgroundResource(R.drawable.ic_action_pause);
                    started = true;
                } else {
                    mediaPlayer.pause();
                    control.setBackgroundResource(R.drawable.ic_action_play);
                    started = false;
                }
            }
        });

        control.setEnabled(false);

        //Volume Control
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

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
                volume = progress;
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }
        });

        //Sync
        syncImage = (ImageView) findViewById(R.id.syncImage);
        syncImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                syncImage.setEnabled(false);
                if(started){
                    mediaPlayer.stop();
                }
                started = false;
                prepared = false;
                mediaPlayer.reset();
                control.setBackgroundResource(R.drawable.ic_hourglass_empty);
                control.setEnabled(false);
                new PlayerTask().execute(stream);
            }

        });


        //ExtraSetting
        //이부분이 시작화면일경우 주석처리하면됩니다.
        TextView title = (TextView) findViewById(R.id.broadcastTitle);
        TextView songText = (TextView) findViewById(R.id.songText);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        ArrayList<String> songs = intent.getStringArrayListExtra("songs");
        String songBuilder = "";
        for(int k = 0 ; k < songs.size(); k++){
            songBuilder += Integer.toString(k+1) + " : " + songs.get(k)+"\n";
        }
        songText.setText(songBuilder);


        //Media Loading
        mediaPlayer = new FFmpegMediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerTask().execute(stream);


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
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
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
            control.setEnabled(true);
            started = true;
            control.setBackgroundResource(R.drawable.ic_action_pause);
            syncImage.setEnabled(true);
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
