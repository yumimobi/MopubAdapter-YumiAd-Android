package com.yumiad.mopubsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * <p>
 * Created by lgd on 2019-07-18.
 */
public class BannerActivity extends AppCompatActivity implements MoPubView.BannerAdListener {
    private static String TAG = "BannerActivity";
    private TextView text;

    private MoPubView moPubView;

    private String MOPUB_UNIT_ID_BANNER = "bfc6e32196ca4c0dbd5f6c5af4616e72";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_banner);
        initView();
        createBanner();

    }


    public void initView() {
        text = (TextView) findViewById(R.id.textView2);

    }

    private void createBanner() {
        moPubView = new MoPubView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        addContentView(moPubView, params);


        moPubView.setAdUnitId(MOPUB_UNIT_ID_BANNER);
        Map<String,Object> localExtras =new HashMap<>();
        localExtras.put("moPubView", moPubView);
        moPubView.setLocalExtras(localExtras);
        moPubView.setBannerAdListener(this);
    }

    @Override
    public void onBannerLoaded(MoPubView banner) {
        Log.i(TAG,"onBannerLoaded");
        setInfo("onBannerLoaded");
    }

    @Override
    public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
        Log.i(TAG,"onBannerFailed MoPubErrorCode :" + errorCode);
        setInfo("onBannerFailed MoPubErrorCode :" + errorCode);
    }

    @Override
    public void onBannerClicked(MoPubView banner) {
        Log.i(TAG,"onBannerClicked");
        setInfo("onBannerClicked");
    }

    @Override
    public void onBannerExpanded(MoPubView banner) {
        Log.i(TAG,"onBannerExpanded");
        setInfo("onBannerExpanded");
    }

    @Override
    public void onBannerCollapsed(MoPubView banner) {
        Log.i(TAG,"onBannerCollapsed");
        setInfo("onBannerCollapsed");
    }

    public void loadAd(View view) {
        if(moPubView != null){
            Map<String,Object> localExtras =new HashMap<>();
            localExtras.put("moPubView", moPubView);
            moPubView.setLocalExtras(localExtras);
            moPubView.loadAd();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(moPubView != null){
            moPubView.destroy();
        }
    }

    private void setInfo(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (text != null) {
                    text.append(msg + "\n");
                }

            }
        });
    }
}
