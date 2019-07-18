package com.yumiad.mopubadapter;

import android.content.Context;

import com.mopub.mobileads.CustomEventBanner;

import java.util.Map;

/**
 * Description:
 * <p>
 * Created by lgd on 2019-07-18.
 */
public class YumiAdBanner extends CustomEventBanner {

    @Override
    protected void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {

    }

    @Override
    protected void onInvalidate() {

    }
}
