package com.example.android.geofence.home_fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.geofence.Home;
import com.example.android.geofence.Login;
import com.example.android.geofence.R;
import com.example.android.geofence.REST.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by neel on 4/13/14.
 */
public class AddFriend extends Fragment {

    Button search, add, invite;
    EditText search_username;
    TextView found_username;
    JSONObject friend, user;
    String username_query;
    String found_username_id = null;
    ProgressDialog pd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addfriend,null);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        View view = getView();
        search = (Button)view.findViewById(R.id.search_btn);
        add = (Button)view.findViewById(R.id.add_btn);
        invite = (Button)view.findViewById(R.id.invite);
        search_username = (EditText)view.findViewById(R.id.username_edit);
        found_username = (TextView)view.findViewById(R.id.found_username);
        user = Login.user;
        found_username.setText("Search Above For Friends!");
        add.setVisibility(View.INVISIBLE);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send the request to check if username can be found
                username_query = search_username.getText().toString();
                new FindUser().execute();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FriendUser().execute();
            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Download Notedrop!");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "I think you should download this new android app Notedrop because its awesome!");
                startActivity(emailIntent);
            }
        });
    }

    private class FindUser extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Searching For Username...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                friend = Request.findUser(username_query);
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
            boolean found = false;

            if(friend == null)
            {
                found_username.setText("Sorry, username not found! Try another username");
                add.setVisibility(View.INVISIBLE);
                pd.dismiss();
                return;
            }
            else
            {
                try
                {
                    if(friend.getBoolean("success"))
                    {
                        JSONObject message = friend.getJSONObject("message");
                        found_username_id = message.getString("ID");

                        //lets make sure they aren't friends already
                        JSONObject messages = user.getJSONObject("message");
                        JSONArray friends = messages.getJSONArray("friends");
                        for(int i = 0; i<friends.length(); i++)
                        {
                            JSONObject friend = friends.getJSONObject(i);
                            String id = friend.getString("_id");
                            if(id.equals(found_username_id))
                            {
                                pd.dismiss();
                                found_username.setText("This username is already your friend!");
                                return;
                            }
                        }

                        found_username.setText(username_query);

                        add.setVisibility(View.VISIBLE);
                        found = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pd.dismiss();
            }



        }
    }

    private class FriendUser extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Friending User...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONObject message = null;
            String userId = null;
            try {
                message = user.getJSONObject("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                userId = message.getString("ID");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                friend = Request.updateUser(userId,found_username_id , null, null, null);
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
            boolean found = false;

            if(friend == null)
            {

                found_username.setText("NOT SUCCESSFULLY");
            }
            else
            {
                try
                {
                    if(friend.getBoolean("success"))
                    {
                        pd.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Added Friend!", Toast.LENGTH_SHORT).show();
                        moveToHome();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }



        }
    }

    public void moveToHome()
    {
        Intent i = new Intent(getActivity(), Home.class);
        startActivity(i);
    }

}
