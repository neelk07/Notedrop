package com.example.android.geofence;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.android.geofence.REST.Request;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

/**
 * Created by neel on 4/11/14.
 */
public class Login extends Activity {

    TextView response;
    String response_text;
    String username_text;
    String password_text;
    boolean login_made = false;
    EditText username, password;
    Button login;
    JSONObject user;
    JSONObject note;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        retrieveViews();

        //Intent i = new Intent(this, Registration.class);
        //startActivity(i);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_text = username.getText().toString();
                password_text = password.getText().toString();
                response.setText("Request Sent");
                /*
                try {
                    password_text = Request.md5Hash(password_text);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                */
                new Register().execute();
            }
        });

    }

    public void retrieveViews()
    {
        response = (TextView)findViewById(R.id.response);
        username = (EditText)findViewById(R.id.username_edit);
        password = (EditText)findViewById(R.id.password_edit);
        login = (Button)findViewById(R.id.login_btn);
    }

    private class Register extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                user = Request.loginUser(username_text,password_text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void...error){

        }


        @Override
        protected void onPostExecute(Void worked) {
            login_made = checkResponse(user);
            new CreateNote().execute();


            //if (login_made)
            //    moveToMap(); //move to map
        }
    }

    private class CreateNote extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {
                note = Request.createNote("testing", "12.2323", "23.23232", "43", "12/23/43", "11/23/45", "5349f467c81a060200e0cfc9");
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
            response.setText(note.toString());
            //login_made = checkResponse(user);
            //if (login_made)
            //    response.setText(user.toString());
                //moveToMap(); //move to map
        }
    }


    public boolean checkResponse(JSONObject user)
    {
        if(user == null)
        {
            response.setText("Null User Object");
            return false;
        }

        try {
            if(user.getBoolean("success") == true)
            {
                response.setText("Login Success!");
                return true;
            }
            else
            {
                response.setText(user.getString("message"));
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;

    }

    public void moveToMap()
    {
        Intent i = new Intent(this,GeofenceMap.class);
        startActivity(i);
    }

}