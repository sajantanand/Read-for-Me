package com.mycompany.readforme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.io.File;
import java.util.Date;

/**
 * Created by @sajantanand on 1/16/2015.
 */

public class FileDisplay extends Activity {

    public final static String EXTRA_MESSAGE = "com.mycompany.readforme.FileDisplay.Message";


    private File folder = new File(Settings.ENVIRONMENTAL + Settings.FOLDER_PATH);
    private int size = folder.list().length;
    private int k = 0;
    private int initial = 0;

    private File[] files = folder.listFiles();
    private Button[] buttonArray = new Button[6];
    private Button deletion;


    private String message = null;
    private String messager = null;

    Date modified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);

        buttonArray[0] = ((Button) findViewById(R.id.button0));
        buttonArray[1] = ((Button) findViewById(R.id.button1));
        buttonArray[2] = ((Button) findViewById(R.id.button2));
        buttonArray[3] = ((Button) findViewById(R.id.button3));
        buttonArray[4] = ((Button) findViewById(R.id.button4));
        buttonArray[5] = ((Button) findViewById(R.id.button5));
        deletion = (Button) findViewById(R.id.delete);
        deletion.setEnabled(false);

        size--;

        updateButtonsForward();
        longClickButtonManager();
    }

    public void longClickButtonManager()
    {
        buttonArray[0].setOnLongClickListener(longClick0);
        buttonArray[1].setOnLongClickListener(longClick1);
        buttonArray[2].setOnLongClickListener(longClick2);
        buttonArray[3].setOnLongClickListener(longClick3);
        buttonArray[4].setOnLongClickListener(longClick4);
        buttonArray[5].setOnLongClickListener(longClick5);
    }

    private View.OnLongClickListener longClick0 = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            messager = buttonArray[0].getText().toString();
            deletion.setEnabled(true);
            return true;
        }
    };

    private View.OnLongClickListener longClick1 = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            messager = buttonArray[1].getText().toString();
            deletion.setEnabled(true);
            return true;
        }
    };

    private View.OnLongClickListener longClick2 = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            messager = buttonArray[2].getText().toString();
            deletion.setEnabled(true);
            return true;
        }
    };

    private View.OnLongClickListener longClick3 = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            messager = buttonArray[3].getText().toString();
            deletion.setEnabled(true);
            return true;
        }
    };

    private View.OnLongClickListener longClick4 = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            messager = buttonArray[4].getText().toString();
            deletion.setEnabled(true);
            return true;
        }
    };

    private View.OnLongClickListener longClick5 = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            messager = buttonArray[5].getText().toString();
            deletion.setEnabled(true);
            return true;
        }
    };

    public void deletionHandler(View view)
    {
        File toBeDeleted = new File(Settings.ENVIRONMENTAL + Settings.FOLDER_PATH + "/" + messager);
        toBeDeleted.delete();
        updateButtons();
        deletion.setEnabled(false);
    }

    public void back(View view)
    {
        updateButtonsBack();
    }

    public void ahead(View view)
    {

        updateButtonsForward();

    }

    public void button0(View view)
    {
        message = buttonArray[0].getText().toString();
        send(message);


    }
    public void button1(View view)
    {
        message = buttonArray[1].getText().toString();
        send(message);
    }
    public void button2(View view)
    {
        message = buttonArray[2].getText().toString();
        send(message);
    }
    public void button3(View view)
    {
        message = buttonArray[3].getText().toString();
        send(message);
    }
    public void button4(View view)
    {
        message = buttonArray[4].getText().toString();
        send(message);
    }
    public void button5(View view)
    {
        message = buttonArray[5].getText().toString();
        send(message);
    }

    public void send(String entry)
    {
        Intent intent = new Intent(this,MediaPlayerActivity.class);
        intent.putExtra(EXTRA_MESSAGE, entry);
        startActivity(intent);
    }

    public void updateButtons()
    {
        k-=6;
        files = folder.listFiles();
        size = folder.list().length;
        size--;
        updateButtonsForward();
    }

    public void updateButtonsForward()
    {
        if (k > size - size%6)
        {
            k = size - size%6;
        }
        else if (k < 0)
        {
            k = 0;
        }

        for (initial = k; k < initial + 6; k++)
        {
            if (k <= size)
            {
                modified = new Date(files[size - k].lastModified());
                buttonArray[k%6].setText(files[size - k].getName()); // + "\t" + modified.toString()
            }
            else
            {
                buttonArray[k%6].setText(null);
            }
        }
    }

    public void updateButtonsBack()
    {
        if (k >= 7)
        {
            k-=12;
        }
        else
        {
            k=0;
        }

        for (initial = k; k < initial + 6; k++)
        {
            if (k <= size)
            {
                modified = new Date(files[size - k].lastModified());
                buttonArray[k%6].setText(files[size - k].getName()); // + "\t" + modified.toString()
            }
            else
            {
                buttonArray[k%6].setText(null);
            }
        }
    }

}
