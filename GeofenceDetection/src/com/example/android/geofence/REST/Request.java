package com.example.android.geofence.REST;

import android.app.DownloadManager;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Map;

public class Request {

    private static final String DOMAIN = "http://notedrop-server.herokuapp.com";
    private static final String CREATE_USER_URL = DOMAIN + "/user/create";
    private static final String LOGIN_USER_URL = DOMAIN + "/user/login";
    private static final String GET_USER_URL = DOMAIN + "/user/get/";
    private static final String GET_NOTE_URL = DOMAIN + "/note/get/";
    private static final String DELETE_NOTE_URL = DOMAIN + "/note/delete";
    private static final String UPDATE_USER_URL = DOMAIN + "/user/update";

    private static final String CREATE_NOTE_URL = DOMAIN + "/note/create";

    public static String createUser(String username, String password) throws JSONException {
            // String response
            InputStream inputStream = null;
            String result = "";

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(CREATE_USER_URL);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", username);
            jsonObject.accumulate("password", password);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = null;
            try {
                se = new StringEntity(json, "UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = null;
            try {
                httpResponse = httpclient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject object = new JSONObject(result);
            if(object.getBoolean("success") == true)
                return "Success";
            else
                return object.getString("message");
    }

    public static JSONObject loginUser(String username, String password) throws JSONException {
        // String response
        InputStream inputStream = null;
        String result = "";

        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPut httpPut = new HttpPut(LOGIN_USER_URL);

        String json = "";

        // 3. build jsonObject
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("username", username);
        jsonObject.accumulate("password", password);

        // 4. convert JSONObject to JSON to String
        json = jsonObject.toString();

        // ** Alternative way to convert Person object to JSON string usin Jackson Lib
        // ObjectMapper mapper = new ObjectMapper();
        // json = mapper.writeValueAsString(person);

        // 5. set json to StringEntity
        StringEntity se = null;
        try {
            se = new StringEntity(json, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 6. set httpPost Entity
        httpPut.setEntity(se);

        // 7. Set some headers to inform server about the type of the content
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");

        // 8. Execute POST request to the given URL
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject(result);
        if(object.getBoolean("success") == true)
            return object;
        else
            return null;
    }

    public static JSONObject getUser(String user_id){
        // String response
        InputStream inputStream = null;
        String result = "";

        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpGet httpGet = new HttpGet(GET_USER_URL + user_id);

        /*
        String json = "";

        // 3. build jsonObject
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("username", username);
        jsonObject.accumulate("password", password);

        // 4. convert JSONObject to JSON to String
        json = jsonObject.toString();

        // ** Alternative way to convert Person object to JSON string usin Jackson Lib
        // ObjectMapper mapper = new ObjectMapper();
        // json = mapper.writeValueAsString(person);

        // 5. set json to StringEntity
        StringEntity se = null;
        try {
            se = new StringEntity(json, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 6. set httpPost Entity
        httpPut.setEntity(se);
        */

        // 7. Set some headers to inform server about the type of the content
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");

        // 8. Execute POST request to the given URL
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(object.getBoolean("success") == true)
                return object;
            else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject createNote(String text, String latitude, String longitude, String radius, String start_date, String end_date, String [] users) throws JSONException {
        // String response
        InputStream inputStream = null;
        String result = "";

        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPost httpPost = new HttpPost(CREATE_USER_URL);

        String json = "";

        // 3. build jsonObject
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("text", text);
        jsonObject.accumulate("latitude", latitude);
        jsonObject.accumulate("longitude", longitude);
        jsonObject.accumulate("radius", radius);
        jsonObject.accumulate("startDate", start_date);
        jsonObject.accumulate("endDate",end_date);
        jsonObject.accumulate("users", users);

        // 4. convert JSONObject to JSON to String
        json = jsonObject.toString();

        // ** Alternative way to convert Person object to JSON string usin Jackson Lib
        // ObjectMapper mapper = new ObjectMapper();
        // json = mapper.writeValueAsString(person);

        // 5. set json to StringEntity
        StringEntity se = null;
        try {
            se = new StringEntity(json, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 6. set httpPost Entity
        httpPost.setEntity(se);

        // 7. Set some headers to inform server about the type of the content
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        // 8. Execute POST request to the given URL
        HttpResponse httpResponse = null;

        try {
            httpResponse = httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject(result);
        if(object.getBoolean("success") == true)
            return object;
        else
            return null;
    }

    public static JSONObject getNote(String note_id){
        // String response
        InputStream inputStream = null;
        String result = "";

        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpGet httpGet = new HttpGet(GET_NOTE_URL + note_id);

        /*
        String json = "";

        // 3. build jsonObject
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("username", username);
        jsonObject.accumulate("password", password);

        // 4. convert JSONObject to JSON to String
        json = jsonObject.toString();

        // ** Alternative way to convert Person object to JSON string usin Jackson Lib
        // ObjectMapper mapper = new ObjectMapper();
        // json = mapper.writeValueAsString(person);

        // 5. set json to StringEntity
        StringEntity se = null;
        try {
            se = new StringEntity(json, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 6. set httpPost Entity
        httpPut.setEntity(se);
        */

        // 7. Set some headers to inform server about the type of the content
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-type", "application/json");

        // 8. Execute POST request to the given URL
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(object.getBoolean("success") == true)
                return object;
            else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean deleteNote(String note_id) throws JSONException {
        // String response
        InputStream inputStream = null;
        String result = "";

        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpDelete httpDelete = new HttpDelete(DELETE_NOTE_URL);


        String json = "";

        // 3. build jsonObject
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("noteID", note_id);

        // 4. convert JSONObject to JSON to String
        json = jsonObject.toString();


        // ** Alternative way to convert Person object to JSON string usin Jackson Lib
        // ObjectMapper mapper = new ObjectMapper();
        // json = mapper.writeValueAsString(person);

        // 5. set json to StringEntity
        StringEntity se = null;
        try {
            se = new StringEntity(json, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 6. set httpPost Entity
        httpDelete.setEntity(se);


        // 7. Set some headers to inform server about the type of the content
        httpDelete.setHeader("Accept", "application/json");
        httpDelete.setHeader("Content-type", "application/json");

        // 8. Execute POST request to the given URL
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            result = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject object = null;
        try {
            object = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(object.getBoolean("success") == true)
                return true;
            else
                return false;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String md5Hash(String input) throws NoSuchAlgorithmException {

        StringBuffer hexString = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(input.getBytes());

        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }

        return hexString.toString();
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


}
/**
 * Created by neel on 4/11/14.
 */
