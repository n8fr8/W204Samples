package edu.berkeley.cybersecurity.mysecureapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

public class SecureNetworkAndStorageMainActivity extends SecureNetworkMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void connectToService ()
    {
        doLogin();

        String response = downloadData();

        if (!TextUtils.isEmpty(response))
        {
            //save the data here
        }

        String mysecretdata = "foobar";
        submitData(mysecretdata);
        doLogout();
    }
}
