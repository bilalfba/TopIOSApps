package com.fasgmail.bilal.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; //Need this for using logd tags
    private ListView listApps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = (ListView) findViewById(R.id.xmlListView);


        Log.d(TAG, "onCreate: starting Async task");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: Done");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        return true; //to tell android that wev actually inflated our view
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: parameter is " + s);
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);

            //Create new array adapter object with 3 parameters
            //1st - Context (instance of main activity) 2nd - Resource containing textView that arrayadapt will use to put text into, 2rd - providing arrayList
//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(MainActivity.this, R.layout.list_item, parseApplications.getApplications());
//            listApps.setAdapter(arrayAdapter); //link list view to the adapter

            FeedAdapter feedAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, parseApplications.getApplications());
            listApps.setAdapter(feedAdapter);


        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: Starts with" + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error Downloading, Null Value");
            }
            return rssFeed;
        }


        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //connect to the internet
                int response = connection.getResponseCode(); // for exam 404 is an HTML response code
                Log.d(TAG, "downloadXML: The response code was " + response);

//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //efficient
                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));

                    }
                }
                reader.close();

                return xmlResult.toString();
            } catch (MalformedURLException e) { //subclass of IOException, android goes in order
                Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage()); //e.getMessage gives more info
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception Reading Data" + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXML: Secuirty Exception. Needs Permission" + e.getMessage());
//                e.printStackTrace();
            }

            return null;
        }
    }
}
