package com.iot.iot;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class firstp extends AppCompatActivity {

    private static final String TAG = "UsingThingspeakAPI";
    private static final String THINGSPEAK_CHANNEL_ID = "294981";
    private static final String THINGSPEAK_API_KEY ="02K8UFGMZ82DDU18"; //"JQNY7RB8OURDLS3Z";//read key
    private static final String THINGSPEAK_API_KEY_STRING = "api_key";//02K8UFGMZ82DDU18

    /* Be sure to use the correct fields for your own app*/
    private static final String THINGSPEAK_FIELD1 = "field1";
    private static final String THINGSPEAK_FIELD2 = "field2";

    private static final String THINGSPEAK_UPDATE_URL = "https://api.thingspeak.com/update?";
    private static final String THINGSPEAK_CHANNEL_URL = "https://api.thingspeak.com/channels/";
    private static final String THINGSPEAK_FEEDS_LAST = "/feeds/last?";


    Button bb, bedr,kitchen,wash,door, cc;
    TextView tv,tt;
    EditText ed;
    int a=0,b=0;
    int aa=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstp);

        cc= (Button) findViewById(R.id.button5);
       // bb= (Button) findViewById(R.id.button2);
        bedr= (Button) findViewById(R.id.button3);
        kitchen= (Button) findViewById(R.id.button4);
        wash= (Button) findViewById(R.id.button5);
        door= (Button) findViewById(R.id.button2);

        tv= (TextView) findViewById(R.id.textView);
        tt= (TextView) findViewById(R.id.textView5);
        //ed= (EditText) findViewById(R.id.editText3);



        bedr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aa=1;
                //startActivity(new Intent(firstp.this, bedroom.class));
                new FetchThingspeakTask().execute();

            }
        });


      /*  bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=Integer.parseInt(ed.getText().toString());
                b=a+20;

                new UpdateThingspeakTask().execute();
            }
        });

        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchThingspeakTask().execute();
            }
        });
        */
    }





    class UpdateThingspeakTask extends AsyncTask<Void, Void, String> {
int ck=0;
        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(THINGSPEAK_UPDATE_URL + THINGSPEAK_API_KEY_STRING + "=" +
                        THINGSPEAK_API_KEY + "&" + THINGSPEAK_FIELD1 + "=" + a +//latitude
                        "&" + THINGSPEAK_FIELD2 + "=" + b);//longitude
                ck=1;
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                    }
                finally{
                    urlConnection.disconnect();

                       }
                 }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(ck==1)
            Toast.makeText(firstp.this, "SENT", Toast.LENGTH_SHORT).show();
            // We completely ignore the response
            // Ideally we should confirm that our update was successful
        }
    }

    class FetchThingspeakTask extends AsyncTask<Void, Void, String> {


        protected void onPreExecute() {
           // double latitude = Double.parseDouble(latitudeEdit.getText().toString());
            //double longitude = Double.parseDouble(longitudeEdit.getText().toString());
           // target = new Location("");
          //  target.setLatitude(latitude);
          //  target.setLongitude(longitude);

           // latitudeEdit.setEnabled(false);
           // longitudeEdit.setEnabled(false);
           // distanceText.setText("");
            //distanceText.setVisibility(View.GONE);
           // progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(THINGSPEAK_CHANNEL_URL + THINGSPEAK_CHANNEL_ID +
                        THINGSPEAK_FEEDS_LAST + THINGSPEAK_API_KEY_STRING + "=" +
                        THINGSPEAK_API_KEY + "");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                Toast.makeText(firstp.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject channel = (JSONObject) new JSONTokener(response).nextValue();
               String lat = channel.getString(THINGSPEAK_FIELD1);
                String longe = channel.getString(THINGSPEAK_FIELD2);
                if(aa==1) {
                    Intent intent = new Intent(firstp.this, bedroom.class);
                    intent.putExtra("ac", lat);
                    intent.putExtra("light", longe);
                    startActivity(intent);
                      }
                Toast.makeText(firstp.this, "Updated", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
