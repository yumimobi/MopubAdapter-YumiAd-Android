package com.yumiad.mopubsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;

public class MainActivity extends AppCompatActivity {
    static final String MOPUB_UNIT_ID_INTERSTITIAL = "cda07ed1040b49b491fe84f4f9a2c996";
    static final String MOPUB_UNIT_ID_BANNER = "a6a855f5db944de8856413681371ccff";
    static final String MOPUB_UNIT_ID_REWARDED_VIDEO = "2337db1514974536866171f51fade057";

    private boolean isMopubInitFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(MOPUB_UNIT_ID_BANNER).build();
        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());
    }

    public void launchBanner(View view) {
        if (!isMopubInitFinished) {
            Toast.makeText(this, "Mopub initialization is not finished", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, BannerActivity.class);
        startActivity(i);
    }

    public void launchInterstitial(View view) {
        if (!isMopubInitFinished) {
            Toast.makeText(this, "Mopub initialization is not finished", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, InterstitialActivity.class);
        startActivity(i);
    }

    public void launchRewardedVideo(View view) {
        if (!isMopubInitFinished) {
            Toast.makeText(this, "Mopub initialization is not finished", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, RewardedVideoActivity.class);
        startActivity(i);
    }

    private SdkInitializationListener initSdkListener() {
        return new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                isMopubInitFinished = true;
            }
        };
    }
}
