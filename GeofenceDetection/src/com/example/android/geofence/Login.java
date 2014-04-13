package com.example.android.geofence;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.geofence.REST.Request;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
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
    public static JSONObject user;
    JSONObject note;
    File userInfo;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //get views
        retrieveViews();
        //get cached json file
        userInfo = new File(this.getFilesDir().getPath(), "userInfo.json");



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_text = username.getText().toString();
                password_text = password.getText().toString();
                response.setText("Request Sent");

                try {
                    password_text = Request.md5Hash(password_text);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                new LoginUser().execute();
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

    private class LoginUser extends AsyncTask<Void,Void,Void> {


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
            if(login_made)
            {
                cacheLoginJson(user, userInfo);
                try {
                readCachedLoginJSON(userInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                moveToHome();
            }

            //response.setText(user.toString());
            //new CreateNote().execute();
            //new CreateNote().execute();
            //if (login_made)
            //    moveToMap(); //move to map
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

    public void moveToHome()
    {
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    public void cacheLoginJson(JSONObject user, File userInfo)
    {
        if (!userInfo.exists()) {
            try
            {
                if(!userInfo.createNewFile())
                {
                    Toast.makeText(this, "Couldn't Create Save Path", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FileWriter writer = new FileWriter(userInfo);
                    writer.write(user.toString());
                    writer.close();

                    FileReader reader = new FileReader(userInfo);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"Couldn't Create Save Path",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this,"Using cached file for user",Toast.LENGTH_SHORT).show();
        }

    }


    public JSONObject readCachedLoginJSON(File file) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            String response = sb.toString();
            JSONObject object = new JSONObject(response);
            return object;
        } finally {
            br.close();
        }
    }


}