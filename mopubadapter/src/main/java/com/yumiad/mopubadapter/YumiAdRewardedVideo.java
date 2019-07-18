package com.yumiad.mopubadapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mopub.common.LifecycleListener;
import com.mopub.mobileads.CustomEventRewardedVideo;

import java.util.Map;

/**
 * Description:
 * <p>
 * Created by lgd on 2019-07-18.
 */
public class YumiAdRewardedVideo extends CustomEventRewardedVideo {
    @Override
    protected boolean hasVideoAvailable() {
        return false;
    }

    @Override
    protected void showVideo() {

    }

    @Nullable
    @Override
    protected LifecycleListener getLifecycleListener() {
        return null;
    }

    @Override
    protected boolean checkAndInitializeSdk(@NonNull Activity launcherActivity, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) throws Exception {
        return false;
    }

    @Override
    protected void loadWithSdkInitialized(@NonNull Activity activity, @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) throws Exception {

    }

    @NonNull
    @Override
    protected String getAdNetworkId() {
        return null;
    }

    @Override
    protected void onInvalidate() {

    }
}
