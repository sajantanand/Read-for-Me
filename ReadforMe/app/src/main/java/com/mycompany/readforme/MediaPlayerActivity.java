package com.mycompany.readforme;

import com.mycompany.readforme.remote.LongmanAPIHelper;

import java.io.File;
import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by @sajantanand on 1/16/2015.
 */

public class MediaPlayerActivity extends Activity {

    public TextView songName,startTimeField,endTimeField;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private ImageButton playButton,pauseButton;
    public static int oneTimeOnly = 0;

    private ProgressDialog progressDialog;
    private String headWord;
    private short counter = 0;
    private LongmanAPIHelper helper = new LongmanAPIHelper();
    private TextView tvWord;
    private EditText entryBox;
    private Button alternateButton;

    private File filePlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String fileToPlay = intent.getStringExtra(FileDisplay.EXTRA_MESSAGE);
        String filePath = Settings.ENVIRONMENTAL + Settings.FOLDER_PATH + "/" + fileToPlay;
        filePlayer = new File(filePath);
        Uri.Builder builder1 = new Uri.Builder();

        setContentView(R.layout.activity_media_player);
        songName = (TextView)findViewById(R.id.textView4);
        startTimeField =(TextView)findViewById(R.id.textViewStartTime);
        endTimeField =(TextView)findViewById(R.id.textViewEndTime);
        seekbar = (SeekBar)findViewById(R.id.seekBar1);
        playButton = (ImageButton)findViewById(R.id.imageButtonPlay);
        pauseButton = (ImageButton)findViewById(R.id.imageButtonPause);
        songName.setText(filePlayer.getName());

        mediaPlayer = MediaPlayer.create(this, builder1.build().fromFile(filePlayer));
        seekbar.setClickable(false);
        pauseButton.setEnabled(false);

        tvWord = (TextView) findViewById(R.id.textViewDefinition);
        alternateButton = (Button) findViewById(R.id.buttonAlternate);
        alternateButton.setEnabled(false);

    }

    public void define(View view)
    {
        helper = new LongmanAPIHelper();
        showProgressDialog();
        entryBox = (EditText) findViewById(R.id.editTextDefinition);
        headWord = (entryBox.getText().toString());
        new RetrieveDictionaryEntryTask().execute((Void)null); // .execute(null)

        alternateButton.setEnabled(true);
        /*alternateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                showProgressDialog();
                new RetrieveAlternateDefinition().execute();
            }
        });*/

    }

    public void alternate(View view)
    {
        counter++;
        showProgressDialog();
        new RetrieveAlternateDefinition().execute((Void)null); // .execute(null)
    }


    private void completeEntryLoad(String entry) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (entry != null) {
            //tvWord.setText(getFilesDir().toString() + "\n" + getExternalFilesDir(null).toString());
            tvWord.setText(entry);
        } else {
            tvWord.setText("Formatting error in returned response. Please try again.");
        }
    }

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", "Getting Definition...", true);
    }

    /**
     * AsyncTask for retrieval of Definition.
     */
    class RetrieveDictionaryEntryTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            try {
                return helper.getDictionaryEntry(headWord);
            } catch (Exception e) {
                Log.w(Settings.LOG_TAG, e.getClass().getSimpleName() + ", " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String entry) {
            completeEntryLoad(entry);
        }

    }

    class RetrieveAlternateDefinition extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0){
            try {
                return helper.getAlternateDefinition(counter);
            } catch (Exception e) {
                Log.w(Settings.LOG_TAG, e.getClass().getSimpleName() + ", " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String entry){
            completeEntryLoad(entry);
        }

    }


    public void play(View view){
        Toast.makeText(getApplicationContext(), "Playing sound",
                Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if(oneTimeOnly == 0){
            seekbar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        endTimeField.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) finalTime)))
        );
        startTimeField.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
        );
        seekbar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
        pauseButton.setEnabled(true);
        playButton.setEnabled(false);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeField.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
    public void pause(View view){
        Toast.makeText(getApplicationContext(), "Pausing sound",
                Toast.LENGTH_SHORT).show();

        mediaPlayer.pause();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }
    public void forward(View view){
        int temp = (int)startTime;
        if((temp+forwardTime)<=finalTime){
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Cannot jump forward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }

    }
    public void rewind(View view){
        int temp = (int)startTime;
        if((temp-backwardTime)>0){
            startTime = startTime - backwardTime;
            mediaPlayer.seekTo((int) startTime);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Cannot jump backward 5 seconds",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_media_player, menu);
        return true;
    }

}
