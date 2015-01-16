package com.mycompany.readforme.remote;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class InputStreamConverter {

	public static String convertToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8192);
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} finally {
			is.close();
		}
		return sb.toString();
	}
}

