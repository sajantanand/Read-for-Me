package com.mycompany.readforme;

import android.content.Intent;

import android.media.MediaPlayer;

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

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by @sajantanand on 1/16/2015.
 */

public class MainActivity extends Activity implements OnClickListener, OnInitListener// MediaPlayer.OnPreparedListener

{


    private TextToSpeech tts;

    public EditText fileName1;
    public EditText input;
    public Button synthesize;

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

        }

        @Override
        public void onDone(String utteranceId)
        {
            transition();
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
        /*play = ((Button) findViewById(R.id.button2));
        play.setEnabled(false);*/

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


        if (tts != null)
        {

            String text = input.getText().toString();

            if (!tts.isSpeaking())
            {

                fileManager(Settings.FOLDER_PATH);

                fileName = fileName1.getText().toString();
                file1 = new File(Settings.ENVIRONMENTAL + Settings.FOLDER_PATH + "/" + fileName + ".wav");

                HashMap<String, String> myHashRender = new HashMap();
                myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);

                tts.setOnUtteranceProgressListener(mProgressListener);

                tts.synthesizeToFile(text, myHashRender, file1.getAbsolutePath());

            }
        }
    }


    public void fileManager(String existingFolder)
    {
        File folderCheck = new File(Settings.ENVIRONMENTAL+ existingFolder);

        if (!folderCheck.isDirectory())
        {
            folderCheck.mkdir();
        }
    }

    public void goToFiles(View view)
    {
        transition();
    }


    public void transition()
    {
        Intent intent = new Intent(this, FileDisplay.class);
        startActivity(intent);
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
