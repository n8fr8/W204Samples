package edu.berkeley.cybersecurity.mysecureapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import info.guardianproject.iocipher.VirtualFileSystem;

public class SecureNetworkAndStorageMainActivity extends SecureNetworkMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initSecureStorage (String passphrase)
    {
        VirtualFileSystem vfs = VirtualFileSystem.get();
        vfs.createNewContainer(passphrase);
        vfs.mount(passphrase);
    }
    @Override
    protected void connectToService ()
    {
        doLogin();

        String response = downloadData();
        info.guardianproject.iocipher.File fileOuput = new info.guardianproject.iocipher.File("response.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new info.guardianproject.iocipher.FileWriter(fileOuput));
            writer.write(response);
            writer.close();
        } catch (IOException e) {
            Log.w(TAG,"error saving data",e);
        }

        info.guardianproject.iocipher.File fileInput = new info.guardianproject.iocipher.File("requests.txt");
        StringBuffer request = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new info.guardianproject.iocipher.FileReader(fileInput));

            String line = null;

            while ((line = reader.readLine()) != null)
                request.append(line).append('\n');

            reader.close();
        } catch (IOException e) {
            Log.w(TAG,"error saving data",e);
        }
        submitData(request.toString());

        doLogout();
    }
}
