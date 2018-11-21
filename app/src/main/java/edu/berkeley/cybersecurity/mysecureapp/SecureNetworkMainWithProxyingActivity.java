package edu.berkeley.cybersecurity.mysecureapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import info.guardianproject.netcipher.proxy.OrbotHelper;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

public class SecureNetworkMainWithProxyingActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseUrl = getIntent().getDataString();

        OrbotHelper.get(this).init();

        try {
            client = StrongOkHttpClientBuilder
                    .forMaxSecurity(this)
                    .withTorValidation()
                    .withBestProxy().build(new Intent(this,SecureNetworkAndStorageMainActivity.class));
        }
        catch (Exception e) {
            Toast
                    .makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            Log.e(getClass().getSimpleName(),
                    "Exception loading SO questions", e);
            finish();
        }
    }


}
