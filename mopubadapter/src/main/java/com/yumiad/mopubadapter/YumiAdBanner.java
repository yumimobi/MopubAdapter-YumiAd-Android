package com.yumiad.mopubadapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mopub.common.DataKeys;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;
import com.yumi.android.sdk.ads.publish.AdError;
import com.yumi.android.sdk.ads.publish.YumiBanner;
import com.yumi.android.sdk.ads.publish.YumiSettings;
import com.yumi.android.sdk.ads.publish.enumbean.AdSize;
import com.yumi.android.sdk.ads.publish.listener.IYumiBannerListener;
import com.yumi.android.sdk.ads.utils.ZplayDebug;

import java.util.Map;

import static com.yumiad.mopubadapter.YumiAdUtil.getChannelId;
import static com.yumiad.mopubadapter.YumiAdUtil.getGDPRConsent;
import static com.yumiad.mopubadapter.YumiAdUtil.getVersionName;
import static com.yumiad.mopubadapter.YumiAdUtil.isRunInCheckPermissions;
import static com.yumiad.mopubadapter.YumiAdUtil.recodeYumiError;

/**
 * Description:
 * <p>
 * Created by lgd on 2019-07-18.
 */
public class YumiAdBanner extends CustomEventBanner {
    private String TAG = "YumiAdBanner";

    private static boolean onoff = true;

    private FrameLayout bannerContainer;
    private YumiBanner banner;

    private String SLOTID = "slotId";
    private String AUTOLOADNEXT = "autoLoadNext";

    @Override
    protected void loadBanner(Context context, final CustomEventBannerListener mBannerListener, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        ZplayDebug.e(TAG, "loadBanner", onoff);
        if (!(context instanceof Activity)) {
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }
            ZplayDebug.e(TAG, "Banner Load Failed , context not instanceof Activity", onoff);
            return;
        }
        YumiSettings.runInCheckPermission(isRunInCheckPermissions(serverExtras));
        YumiSettings.setGDPRConsent(getGDPRConsent(serverExtras));
        int width;
        int height;
        if (localExtrasAreValid(localExtras)) {
            width = (Integer) localExtras.get(DataKeys.AD_WIDTH);
            height = (Integer) localExtras.get(DataKeys.AD_HEIGHT);
            ZplayDebug.i(TAG, "Banner Load size width : " + width + ", height : " + height, onoff);
        } else {
            ZplayDebug.i(TAG, "Banner Load Failed,local Extras Are not Valid", onoff);
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }
            return;
        }


        String slotId;
        String autoLoadNext;
        if (serverExtrasAreValid(serverExtras)) {
            slotId = serverExtras.get(SLOTID);
            autoLoadNext = serverExtras.get(AUTOLOADNEXT);
            ZplayDebug.i(TAG, "Banner Load slotId : " + slotId, onoff);
            ZplayDebug.i(TAG, "Banner Load autoLoadNext : " + autoLoadNext, onoff);
        } else {
            ZplayDebug.i(TAG, "Banner Load Failed,server Extras Are not Valid", onoff);
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(MoPubErrorCode.NETWORK_NO_FILL);
            }
            return;
        }

        /*
         * First step:
         *  create banner container , this container is a viewgroup, and add the container into your activity content view.
         */
        bannerContainer = new FrameLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;
        ((Activity) context).addContentView(bannerContainer, params);
        /*
         * Thrid step :
         * create YumiBanner instance by activity and your YumiID.
         */
        banner = new YumiBanner((Activity) context, slotId, false);
        //setBannerContainer
        banner.setBannerContainer(bannerContainer, calculateAdSize(width, height), true);
        //setChannelID . (Recommend)
        banner.setChannelID(getChannelId(serverExtras));
        // set channel and version (optional)
        banner.setDefaultChannelAndVersion(context);
        //setVersionName . (Recommend)
        banner.setVersionName(getVersionName(serverExtras));
        //setBannerEventListener. (Recommend)
        banner.setBannerEventListener(new IYumiBannerListener() {
            @Override
            public void onBannerPrepared() {
                if (mBannerListener != null) {
                    removeBannerContainer();
                    mBannerListener.onBannerLoaded(bannerContainer);
                }
            }

            @Override
            public void onBannerPreparedFailed(AdError adError) {
                removeBannerContainer();
                if(mBannerListener != null){
                    mBannerListener.onBannerFailed(recodeYumiError(adError));
                }
            }

            @Override
            public void onBannerExposure() {
                if (mBannerListener != null) {
                    mBannerListener.onBannerImpression();
                }
            }

            @Override
            public void onBannerClicked() {
                if (mBannerListener != null) {
                    mBannerListener.onBannerClicked();
                }
            }

            @Override
            public void onBannerClosed() {

            }
        });

        banner.requestYumiBanner();
    }

    @Override
    protected void onInvalidate() {
        ZplayDebug.e(TAG, "onInvalidate", onoff);
        if(banner != null){
            banner.destroy();
        }
    }


    private boolean serverExtrasAreValid(final Map<String, String> serverExtras) {
        final String slotId = serverExtras.get(SLOTID);
        final String autoLoadNext = serverExtras.get(AUTOLOADNEXT);

        return !TextUtils.isEmpty(slotId) && !TextUtils.isEmpty(autoLoadNext);
    }


    private boolean localExtrasAreValid(@NonNull final Map<String, Object> localExtras) {
        return localExtras.get(DataKeys.AD_WIDTH) instanceof Integer
                && localExtras.get(DataKeys.AD_HEIGHT) instanceof Integer;
    }


    private AdSize calculateAdSize(int width, int height) {
        // Use the smallest AdSize that will properly contain the adView
        if (height <= 50) {
            return AdSize.BANNER_SIZE_320X50;
        } else if (height <= 90) {
            return AdSize.BANNER_SIZE_728X90;
        } else {
            return AdSize.BANNER_SIZE_320X50;
        }
    }

    private void removeBannerContainer(){
        if(bannerContainer != null && bannerContainer.getParent() instanceof ViewGroup){
            ((ViewGroup) bannerContainer.getParent()).removeView(bannerContainer);
        }
    }

}
