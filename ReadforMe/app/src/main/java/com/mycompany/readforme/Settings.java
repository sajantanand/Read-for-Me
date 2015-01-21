package com.mycompany.readforme;

import android.os.Environment;

/**
 * Created by @sajantanand on 1/16/2015.
 */

public class Settings {

	// PUT YOUR API KEY HERE
	public static final String API_KEY = //Enter your API key here.

	// Logging Unique Label
	public static final String LOG_TAG = "PearsonSamDict";

    //Folder to which sound files are saved
    public static final String FOLDER_PATH = "/ReadForMe";

    //Environmental path used by emulator
    public static final String ENVIRONMENTAL = Environment.getExternalStorageDirectory().toString();
}
