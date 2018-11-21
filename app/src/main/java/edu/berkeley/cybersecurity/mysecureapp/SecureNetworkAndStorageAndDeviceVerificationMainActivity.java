package edu.berkeley.cybersecurity.mysecureapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.scottyab.rootbeer.RootBeer;

import java.util.Date;
import java.util.UUID;

import edu.berkeley.cybersecurity.mysecureapp.util.SafetyNetCheck;
import edu.berkeley.cybersecurity.mysecureapp.util.SafetyNetResponse;

import static edu.berkeley.cybersecurity.mysecureapp.util.SafetyNetCheck.parseJsonWebSignature;

public class SecureNetworkAndStorageAndDeviceVerificationMainActivity extends SecureNetworkAndStorageMainActivity {

    private boolean isBadRoot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RootBeer rootBeer = new RootBeer(this);
        if (rootBeer.isRooted()) {
            //we found indication of root

            if (rootBeer.detectPotentiallyDangerousApps()
                    || rootBeer.checkForDangerousProps())
            {
                setContentView(R.layout.activity_root_warning);
                isBadRoot = true;
            }
        }

        String myRequestId = "MySecureApp:" + UUID.randomUUID().toString();

        //if we can do safetycheck, then add that in as well
        new SafetyNetCheck().sendSafetyNetRequest(this, myRequestId, new OnSuccessListener<SafetyNetApi.AttestationResponse>() {
            @Override
            public void onSuccess(SafetyNetApi.AttestationResponse response) {
                // Indicates communication with the service was successful.
                // Use response.getJwsResult() to get the result data.

                String resultString = response.getJwsResult();
                SafetyNetResponse resp = parseJsonWebSignature(resultString);

                long timestamp = resp.getTimestampMs();
                boolean isBasicIntegrity = resp.isBasicIntegrity();
                boolean isCtsMatch = resp.isCtsProfileMatch();

                if ((!isBasicIntegrity) || (!isCtsMatch))
                    setContentView(R.layout.activity_root_warning);

                Log.d(TAG,"SafetyNet result: isBasicIntegrity: " + isBasicIntegrity + " isCts:" + isCtsMatch);

            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // An error occurred while communicating with the service.
                Log.d(TAG,"SafetyNet check failed",e);
            }
        });

    }

    @Override
    protected void onResume() {

        //override
        if (!isBadRoot)
            super.onResume();


    }


}
