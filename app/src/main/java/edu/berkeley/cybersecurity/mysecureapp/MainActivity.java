package edu.berkeley.cybersecurity.mysecureapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    protected static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    protected OkHttpClient client = new OkHttpClient();

    protected String baseUrl;

    private String sessionToken;

    protected final static String TAG = "MySecureApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseUrl = "http://lab.freitas.net/mysecureapp/api/";

    }

    @Override
    protected void onResume() {
        super.onResume();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                connectToService();
                return null;
            }
        }.execute();

    }

    protected void connectToService ()
    {
        doLogin();

        String response = downloadData();
        File fileOuput = new File(getFilesDir(),"response.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileOuput));
            writer.write(response);
            writer.close();
        } catch (IOException e) {
            Log.w(TAG,"error saving data",e);
        }


        submitData("foo");

        doLogout();
    }

    public boolean doLogin ()
    {
        String jsonData = "";
        try {
            sessionToken = post("/login", jsonData);
            return !TextUtils.isEmpty(sessionToken);

        } catch (IOException e) {
            Log.w(TAG,"Error on Login",e);
            return false;
        }
    }

    public String downloadData ()
    {
        try {
            return get("/feed");
        } catch (IOException e) {
            Log.w(TAG,"Error on download data",e);
            return null;
        }
    }

    public boolean submitData (String request)
    {
        try {
            String result = post("/submit", request);
            return Boolean.parseBoolean(result);

        } catch (IOException e) {
            Log.w(TAG,"Error on Login",e);
            return false;
        }
    }

    public boolean doLogout ()
    {
        try {
            String result = post("/logout", sessionToken);
            return Boolean.parseBoolean(result);

        } catch (IOException e) {
            Log.w(TAG,"Error on Login",e);
            return false;
        }
    }

    protected String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    protected String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(baseUrl + url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
