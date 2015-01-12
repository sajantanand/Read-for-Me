package com.mycompany.ttsdemo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.app.Activity;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by @sajantanand on 1/7/2015.
 */

public class MainActivity extends Activity implements OnClickListener, OnInitListener, MediaPlayer.OnPreparedListener

{
    private TextToSpeech tts;

    public EditText fileName1;
    public EditText input;
    public Button synthesize;

    private String extStorageDirectory = Environment.getExternalStorageDirectory().toString();      // "/storage/sdcard"
    public String folderName = "/myFolder";
    public String fileName = null;

    public File file1;

    private MediaPlayer.OnCompletionListener completed = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.release();
            mp = null;
        }
    };

    private UtteranceProgressListener mProgressListener = new UtteranceProgressListener()
    {
        @Override
        public void onStart(String utteranceId)
        {
            //Toast.makeText(getApplicationContext(), "Synthesis has begun.", Toast.LENGTH_SHORT).show();
            //input.setText("Synthesis has begun.");
        }

        @Override
        public void onDone(String utteranceId)
        {
            //Toast.makeText(getApplicationContext(), "Synthesis has finished.", Toast.LENGTH_SHORT).show();
            //input.setText("Synthesis has finished.");
            speak(file1);
        }

        @Override
        public void onError(String utteranceId)
        {

        }
    };

//--------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tts = new TextToSpeech(this, this);

        fileName1 = ((EditText) findViewById(R.id.editText2));
        input = ((EditText) findViewById(R.id.editText1));
        synthesize = ((Button) findViewById(R.id.button1));

        synthesize.setOnClickListener(this);
    }


    @Override
    public void onInit(int code)
    {
        if(code==TextToSpeech.SUCCESS)
        {
            tts.setLanguage(Locale.getDefault());
        }
        else
        {
            tts = null;
            Toast.makeText(this, "Failed to initialize TTS engine.", Toast.LENGTH_SHORT).show();
        }
    }

//--------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view)
    {


        if (tts != null) {

            String text = input.getText().toString();

            if (!tts.isSpeaking()) {

                //tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_IDThi );
                //tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

                fileManager(folderName);

                fileName = fileName1.getText().toString();
                file1 = new File(extStorageDirectory + folderName + "/" + fileName + ".wav");

                /*HashMap<String, String> myHashRender = new HashMap();
                myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);*/

                tts.setOnUtteranceProgressListener(mProgressListener);

                //tts.synthesizeToFile(text, myHashRender, file1.getAbsolutePath());
                tts.synthesizeToFile(text, null, file1, TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);

            }
         }
    }


    public void fileManager(String existingFolder)
    {
        File folderCheck = new File(extStorageDirectory+ existingFolder);

        if (!folderCheck.isDirectory())
        {
            folderCheck.mkdir();
        }
    }


    public void speak(File fileToPlay)
    {

        MediaPlayer player = new MediaPlayer();
        Uri.Builder builder1 = new Uri.Builder();


        try {
            player.setDataSource(this, builder1.build().fromFile(fileToPlay));
            //Toast.makeText(newContext.getApplicationContext(), "Data source was set.", Toast.LENGTH_SHORT).show();
            //player.setDataSource(newFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.prepareAsync();
    }


    public void onPrepared(MediaPlayer player2)
    {
        player2.setOnCompletionListener(completed);
        player2.start();
        Boolean isWorking = player2.isPlaying();

        if ( isWorking )
        {
            Toast.makeText(this, "The player is working.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "The player is not working.", Toast.LENGTH_SHORT).show();
        }

    }

//--------------------------------------------------------------------------------------------------

    @Override
    protected void onDestroy()
    {
        if (tts != null)
        {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

}