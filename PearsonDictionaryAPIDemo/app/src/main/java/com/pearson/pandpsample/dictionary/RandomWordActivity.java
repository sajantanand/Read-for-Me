package com.pearson.pandpsample.dictionary;

import com.pearson.pandpsample.dictionary.remote.LongmanAPIHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RandomWordActivity extends Activity {

	private TextView tvWord;
    private EditText entryBox;
	private ProgressDialog progressDialog;
    private String headWord;
    private int counter = 0;
    private LongmanAPIHelper helper = new LongmanAPIHelper();

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button btnGetRandomWord = (Button) findViewById(R.id.btn_get_random_word);
        tvWord = (TextView) findViewById(R.id.tv_word_label);

        final Button alternateDefinition = (Button) findViewById(R.id.alternateDefinition);

        btnGetRandomWord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper = new LongmanAPIHelper();
                showProgressDialog();
                entryBox = (EditText) findViewById(R.id.editText);
                headWord = (entryBox.getText().toString());
                new RetrieveDictionaryEntryTask().execute(null);


                alternateDefinition.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        counter++;
                        showProgressDialog();
                        new RetrieveAlternateDefinition().execute(null);
                    }
                });

			}
		});
        
    }
    
    private void completeEntryLoad(String entry) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		if (entry != null) {
			tvWord.setText(entry);
		} else {
			tvWord.setText("Formatting error in returned response. Please try again.");
		}
    }
    
	private void showProgressDialog() {
		progressDialog = ProgressDialog.show(this, "", "Getting Definition...", true);
	}

    
    /**
     * AsyncTask for retrieval of RandomWord.
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

}