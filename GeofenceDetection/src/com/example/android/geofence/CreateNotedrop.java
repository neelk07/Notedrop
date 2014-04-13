package com.example.android.geofence;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.android.geofence.REST.Request;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by neel on 4/13/14.
 */
public class CreateNotedrop extends Activity {

    EditText description, radius;
    TextView friends;
    Button createNote;
    private double latitude, longitude;
    private String user_ids;
    private String start_date = "1/11/1111";
    private String end_date = "3/3/3333";
    private String text;
    private String rad;
    private String lat;
    private String lon;
    private String user_id;
    JSONObject user = Login.user;
    JSONObject note;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        while(extras != null)
        {
            longitude = extras.getDouble("long");
            latitude = extras.getDouble("lat");
        }

        retrieveViews();


    }

    public void retrieveViews()
    {
        description = (EditText)findViewById(R.id.description);
        radius = (EditText)findViewById(R.id.value_radius_1);
        friends = (TextView)findViewById(R.id.add_friends);
        createNote = (Button)findViewById(R.id.create_notes);
    }

    //retrieves all important information needed to create note
    public void retrieveInfo()
    {
        text = description.getText().toString();
        rad = radius.getText().toString();
        lat = ""+latitude;
        lon = ""+longitude;

        //we just need to retrieve the userID's now and we are done

    }

    private class CreateNote extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {
                note = Request.createNote(text, lon, lat, rad, start_date, end_date, user_id);
            } catch (JSONException e) {
                note = new JSONObject();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void...error){

        }


        @Override
        protected void onPostExecute(Void worked) {
            //login_made = checkResponse(user);
            //if (login_made)
            //    response.setText(user.toString());
            //moveToMap(); //move to map
        }
    }



}