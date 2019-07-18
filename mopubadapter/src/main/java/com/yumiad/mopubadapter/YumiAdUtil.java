package com.yumiad.mopubadapter;

import android.text.TextUtils;
import android.util.Log;

import com.mopub.mobileads.MoPubErrorCode;
import com.yumi.android.sdk.ads.publish.AdError;
import com.yumi.android.sdk.ads.publish.enumbean.LayerErrorCode;
import com.yumi.android.sdk.ads.publish.enumbean.YumiGDPRStatus;

import java.util.Map;

import static android.text.TextUtils.isEmpty;

/**
 * Description:
 * <p>
 * Created by lgd on 2019-07-18.
 */
final class YumiAdUtil {
    private static final String TAG = "YumiAdUtil";

    static String getSlotId(Map<String, String> extras) {
        String slotId = extras.get("slotId");
        if (isEmpty(slotId)) {
            Log.e(TAG, "YumiAd slot id must not be null.");
            return "";
        }
        return slotId;
    }

    static boolean isAutoloadNext(Map<String, String> extras) {
        String isAuto = extras.get("autoLoadNext");
        if (TextUtils.equals(isAuto, "true")) {
            return true;
        }else if(TextUtils.equals(isAuto, "false")) {
            return false;
        }else {
            return false;
        }
    }

    static String getChannelId(Map<String, String> extras) {
        return extras.get("channelId");
    }

    static String getVersionName(Map<String, String> extras) {
        return extras.get("versionName");
    }

    static YumiGDPRStatus getGDPRConsent(Map<String, String> extras) {
        String gdprConsent = extras.get("GDPRConsent");
        if (TextUtils.equals(gdprConsent, "0")) {
            return YumiGDPRStatus.NON_PERSONALIZED;
        } else if (TextUtils.equals(gdprConsent, "1")) {
            return YumiGDPRStatus.PERSONALIZED;
        } else {
            return YumiGDPRStatus.UNKNOWN;
        }
    }

    static boolean isRunInCheckPermissions(Map<String, String> extras) {
        String runInCheckPermissions = extras.get("extras");
        if (TextUtils.equals(runInCheckPermissions, "true")) {
            return true;
        }else if(TextUtils.equals(runInCheckPermissions, "false")) {
            return false;
        }else {
            return false;
        }
    }

    static MoPubErrorCode recodeYumiError(AdError yumiError) {
        if (yumiError == null) {
            return MoPubErrorCode.NO_FILL;
        }

        LayerErrorCode yumiCode = yumiError.getErrorCode();
        switch (yumiCode) {
            case ERROR_INTERNAL:
                return MoPubErrorCode.INTERNAL_ERROR;
            case ERROR_NETWORK_ERROR:
            case ERROR_INVALID_NETWORK:
                return MoPubErrorCode.NETWORK_INVALID_STATE;
            case ERROR_NON_RESPONSE:
                return MoPubErrorCode.NETWORK_TIMEOUT;

            case ERROR_FAILED_TO_SHOW:
                return MoPubErrorCode.VIDEO_PLAYBACK_ERROR;
            default:
                return MoPubErrorCode.NO_FILL;
        }
    }
}
