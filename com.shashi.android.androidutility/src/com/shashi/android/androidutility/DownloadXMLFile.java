package com.shashi.android.androidutility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DownloadXMLFile extends Activity{

	
	ProgressDialog mProgressDialog;
    Button button;
 
    // Insert image URL
    String URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.download_file);
 
        // Locate a Button in your activity_main.xml layout
        button = (Button) findViewById(R.id.button1);
 
        // Capture Button clicks
        button.setOnClickListener(new OnClickListener() {
 
            public void onClick(View arg0) {
                // Execute DownloadFile AsyncTask
                new DownloadFile().execute(URL);
            }
        });
 
    }
    
    
    // DownloadFile AsyncTask
    private class DownloadFile extends AsyncTask<String, Integer, String> {
 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create progress dialog
            mProgressDialog = new ProgressDialog(DownloadXMLFile.this);
            // Set your progress dialog Title
            mProgressDialog.setTitle("Progress Bar Tutorial");
            // Set your progress dialog Message
            mProgressDialog.setMessage("Downloading, Please Wait!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            // Show progress dialog
            mProgressDialog.show();
        }
 
        @Override
        protected String doInBackground(String... Url) {
            try {
                URL url = new URL(Url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
 
                // Detect the file lenghth
                int fileLength = connection.getContentLength();
 
                // Locate storage location
                String filepath = Environment.getExternalStorageDirectory()
                        .getPath();
 
                // Download the file
                InputStream input = new BufferedInputStream(url.openStream());
 
                // Save the downloaded file
                OutputStream output = new FileOutputStream(filepath + "/"
                        + "euro.xml");
 
                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
 
                // Close connection
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                // Error Log
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
 
        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // Update the progress dialog
            mProgressDialog.setProgress(progress[0]);
            // Dismiss the progress dialog
            //mProgressDialog.dismiss();
        }
    }
    
    
}
