package edu.berkeley.cybersecurity.mysecureapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

import info.guardianproject.netcipher.client.StrongOkHttpClientBuilder;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

public class SecureNetworkMainActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseUrl = "https://lab.freitas.net/mysecureapp/api/";

        //find you site's public key hash here: https://report-uri.com/home/pkp_hash
        String pinSha256="6TGRGuGfOyEMr2YZyqxmD6I47rdT0SFRdcqIwMo/b+s=";

        client = new OkHttpClient.Builder()

                //limit the cipher suites
                .connectionSpecs(Arrays.asList(ConnectionSpec.RESTRICTED_TLS))

                //ping a certificate
                .certificatePinner(new CertificatePinner.Builder()
                        .add("lab.freitas.net", "sha256/" + pinSha256).build())

                .build();


    }


}
