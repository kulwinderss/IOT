package com.iot.iot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class bedroom extends AppCompatActivity {
    Switch ac, light;
    Button ok;
    int aa=0,bb=0;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedroom);

        ac= (Switch) findViewById(R.id.switch2);
        light=(Switch) findViewById(R.id.switch4);
        ok= (Button) findViewById(R.id.button6);

        aa = (Integer.parseInt(getIntent().getExtras().getString("ac")));
         bb=(Integer.parseInt(getIntent().getExtras().getString("light")));

        if(aa==1) {
            ac.setChecked(true);
            Toast.makeText(bedroom.this, "AC is on", Toast.LENGTH_SHORT).show();
        }
        if(bb==1){
            light.setChecked(true);
            Toast.makeText(bedroom.this, "Lights are on", Toast.LENGTH_SHORT).show();
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateThingspeakTask().execute();
            }
        });


        ac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(ac.isChecked()==true)
                    aa=1;
                else
                    aa=0;
            }
        });



        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(light.isChecked()==true)
                    bb=1;
                else
                    bb=0;
            }
        });




    }


    class UpdateThingspeakTask extends AsyncTask<Void, Void, String> {
        int ck=0;
        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(THINGSPEAK_UPDATE_URL + THINGSPEAK_API_KEY_STRING + "=" +
                        THINGSPEAK_API_KEY + "&" + THINGSPEAK_FIELD1 + "=" + aa +//latitude
                        "&" + THINGSPEAK_FIELD2 + "=" + bb);//longitude
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
                Toast.makeText(bedroom.this, "SENT", Toast.LENGTH_SHORT).show();
            // We completely ignore the response
            // Ideally we should confirm that our update was successful
        }
    }
}
