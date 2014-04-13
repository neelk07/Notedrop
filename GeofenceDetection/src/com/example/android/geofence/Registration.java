package com.example.android.geofence;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.android.geofence.REST.Request;
import org.json.JSONException;

import java.security.NoSuchAlgorithmException;

/**
 * Created by neel on 4/11/14.
 */
public class Registration extends Activity {

    TextView response;
    String response_text;
    String username_text;
    String password_text;
    EditText username, password;
    Button register;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        retrieveViews();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_text = username.getText().toString();
                password_text = password.getText().toString();
                try {
                    password_text = Request.md5Hash(password_text);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                response.setText("Request Sent");
                new Register().execute();
            }
        });

    }

    public void retrieveViews()
    {
        response = (TextView)findViewById(R.id.response);
        username = (EditText)findViewById(R.id.username_edit);
        password = (EditText)findViewById(R.id.password_edit);
        register = (Button)findViewById(R.id.register_btn);
    }

    private class Register extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... params) {
            try {
                response_text = Request.createUser(username_text,password_text);
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
            checkResponse(response_text);
        }
    }


    public void checkResponse(String r)
    {
        if(r.equals("Success"))
        {
            response.setText("Success!");
        }
        else
        {
            response.setText(r);
        }

    }

}