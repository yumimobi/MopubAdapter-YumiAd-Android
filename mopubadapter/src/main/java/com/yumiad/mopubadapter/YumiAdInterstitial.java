package com.yumiad.mopubadapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.mopub.mobileads.CustomEventInterstitial;
import com.yumi.android.sdk.ads.publish.AdError;
import com.yumi.android.sdk.ads.publish.YumiInterstitial;
import com.yumi.android.sdk.ads.publish.YumiSettings;
import com.yumi.android.sdk.ads.publish.listener.IYumiInterstitialListener;

import java.util.Map;

import static com.yumiad.mopubadapter.YumiAdUtil.getChannelId;
import static com.yumiad.mopubadapter.YumiAdUtil.getGDPRConsent;
import static com.yumiad.mopubadapter.YumiAdUtil.getSlotId;
import static com.yumiad.mopubadapter.YumiAdUtil.getVersionName;
import static com.yumiad.mopubadapter.YumiAdUtil.isAutoloadNext;
import static com.yumiad.mopubadapter.YumiAdUtil.isRunInCheckPermissions;
import static com.yumiad.mopubadapter.YumiAdUtil.recodeYumiError;

/**
 * Description:
 * <p>
 * Created by lgd on 2019-07-18.
 */
public class YumiAdInterstitial extends CustomEventInterstitial {
    private static final String TAG = "YumiAdInterstitial";
    private YumiInterstitial mYumiInterstitial;

    @Override
    protected void loadInterstitial(Context context, final CustomEventInterstitialListener customEventInterstitialListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {

        if (!(context instanceof Activity)) {
            Log.e(TAG, "YumiAd need Activity context.");
            return;
        }
        YumiSettings.runInCheckPermission(isRunInCheckPermissions(serverExtras));
        YumiSettings.setGDPRConsent(getGDPRConsent(serverExtras));

        Activity activity = (Activity) context;
        mYumiInterstitial = new YumiInterstitial(activity, getSlotId(serverExtras), isAutoloadNext(serverExtras));
        mYumiInterstitial.setChannelID(getChannelId(serverExtras));
        mYumiInterstitial.setVersionName(getVersionName(serverExtras));
        mYumiInterstitial.setInterstitialEventListener(new IYumiInterstitialListener() {
            @Override
            public void onInterstitialPrepared() {
                customEventInterstitialListener.onInterstitialLoaded();
            }

            @Override
            public void onInterstitialPreparedFailed(AdError adError) {
                Log.d(TAG, "onInterstitialPreparedFailed: " + adError);
                customEventInterstitialListener.onInterstitialFailed(recodeYumiError(adError));
            }

            @Override
            public void onInterstitialExposure() {
                customEventInterstitialListener.onInterstitialShown();
            }

            @Override
            public void onInterstitialClicked() {
                customEventInterstitialListener.onInterstitialClicked();
            }

            @Override
            public void onInterstitialClosed() {
                customEventInterstitialListener.onInterstitialDismissed();
            }

            @Override
            public void onInterstitialExposureFailed(AdError adError) {
                Log.d(TAG, "onInterstitialExposureFailed: " + adError);
                customEventInterstitialListener.onInterstitialFailed(recodeYumiError(adError));
            }

            @Override
            public void onInterstitialStartPlaying() {
                // CustomEventInterstitialListener has no such a reflection method.
            }
        });

        mYumiInterstitial.requestYumiInterstitial();
    }

    @Override
    protected void showInterstitial() {
        if (mYumiInterstitial != null && mYumiInterstitial.isReady()) {
            mYumiInterstitial.showInterstitial();
        }
    }

    @Override
    protected void onInvalidate() {
        if (mYumiInterstitial != null) {
            mYumiInterstitial.destroy();
        }
    }
}