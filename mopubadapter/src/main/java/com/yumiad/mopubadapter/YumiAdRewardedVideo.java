package com.yumiad.mopubadapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mopub.common.LifecycleListener;
import com.mopub.common.MoPubReward;
import com.mopub.mobileads.CustomEventRewardedVideo;
import com.mopub.mobileads.MoPubRewardedVideoManager;
import com.yumi.android.sdk.ads.publish.AdError;
import com.yumi.android.sdk.ads.publish.YumiMedia;
import com.yumi.android.sdk.ads.publish.YumiSettings;
import com.yumi.android.sdk.ads.publish.listener.IYumiMediaListener;

import java.util.Map;

import static com.yumiad.mopubadapter.YumiAdUtil.getChannelId;
import static com.yumiad.mopubadapter.YumiAdUtil.getGDPRConsent;
import static com.yumiad.mopubadapter.YumiAdUtil.getSlotId;
import static com.yumiad.mopubadapter.YumiAdUtil.getVersionName;
import static com.yumiad.mopubadapter.YumiAdUtil.recodeYumiError;

/**
 * Description:
 * <p>
 * Created by lgd on 2019-07-18.
 */
public class YumiAdRewardedVideo extends CustomEventRewardedVideo {
    private static final String TAG = "YumiAdRewardedVideo";
    private static final String REWARD_LABEL = "yumiAdReward";
    private static final int REWARD_AMOUNT = 1;

    private YumiMedia mYumiMedia;
    private String mSlotId;

    @Override
    protected boolean hasVideoAvailable() {
        if (mYumiMedia != null) {
            return mYumiMedia.isReady();
        }
        return false;
    }

    @Override
    protected void showVideo() {
        if (mYumiMedia != null) {
            mYumiMedia.showMedia();
        }
    }

    @Nullable
    @Override
    protected LifecycleListener getLifecycleListener() {
        return null;
    }

    @Override
    protected boolean checkAndInitializeSdk(@NonNull Activity launcherActivity, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) throws Exception {
        mSlotId = getSlotId(serverExtras);

        YumiSettings.setGDPRConsent(getGDPRConsent(serverExtras));

        mYumiMedia = new YumiMedia(launcherActivity, mSlotId);
        mYumiMedia.setMediaEventListener(new IYumiMediaListener() {
            @Override
            public void onMediaPrepared() {
                MoPubRewardedVideoManager.onRewardedVideoLoadSuccess(YumiAdRewardedVideo.class, mSlotId);
            }

            @Override
            public void onMediaPreparedFailed(AdError adError) {
                Log.d(TAG, "onMediaPreparedFailed: " + adError);
                MoPubRewardedVideoManager.onRewardedVideoLoadFailure(YumiAdRewardedVideo.class, mSlotId, recodeYumiError(adError));
            }

            @Override
            public void onMediaExposure() {
                // MoPubRewardedVideoManager.onXXX no such callback
                // use onMediaStartPlaying() to fire MoPubRewardedVideoManager.onRewardedVideoStarted action.
            }

            @Override
            public void onMediaExposureFailed(AdError adError) {
                Log.d(TAG, "onMediaExposureFailed: " + adError);
                MoPubRewardedVideoManager.onRewardedVideoPlaybackError(YumiAdRewardedVideo.class, mSlotId, recodeYumiError(adError));
            }

            @Override
            public void onMediaClicked() {
                MoPubRewardedVideoManager.onRewardedVideoClicked(YumiAdRewardedVideo.class, mSlotId);
            }

            @Override
            public void onMediaClosed(boolean isRewarded) {
                MoPubRewardedVideoManager.onRewardedVideoClosed(YumiAdRewardedVideo.class, mSlotId);
                if (isRewarded) {
                    MoPubReward reward = MoPubReward.success(REWARD_LABEL, REWARD_AMOUNT);
                    MoPubRewardedVideoManager.onRewardedVideoCompleted(YumiAdRewardedVideo.class, mSlotId, reward);
                }
            }

            @Override
            public void onMediaRewarded() {
                // use onMediaClosed(boolean) to fire reward action.
            }

            @Override
            public void onMediaStartPlaying() {
                MoPubRewardedVideoManager.onRewardedVideoStarted(YumiAdRewardedVideo.class, mSlotId);

            }
        });
        mYumiMedia.setChannelID(getChannelId(serverExtras));
        mYumiMedia.setVersionName(getVersionName(serverExtras));
        return true;
    }

    @Override
    protected void loadWithSdkInitialized(@NonNull Activity activity, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) throws Exception {
        if (mYumiMedia != null) {
            mYumiMedia.requestYumiMedia();
        }
    }

    @NonNull
    @Override
    protected String getAdNetworkId() {
        return mSlotId;
    }

    @Override
    protected void onInvalidate() {
        if (mYumiMedia != null) {
            mYumiMedia.destroy();
        }
    }
}
