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
    static final String MOPUB_UNIT_ID_INTERSTITIAL = "a23446955ad945a78fc0bf6ca4b2eb6f";
    static final String MOPUB_UNIT_ID_BANNER = "bfc6e32196ca4c0dbd5f6c5af4616e72";
    static final String MOPUB_UNIT_ID_REWARDED_VIDEO = "5c387b79f5c7478bb4b2f284692611e6";

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
