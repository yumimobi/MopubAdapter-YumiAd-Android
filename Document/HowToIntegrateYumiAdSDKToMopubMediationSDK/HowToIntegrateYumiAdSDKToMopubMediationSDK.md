 * [How to integrate YumiAdSDK to Mopub Mediation SDK](#how-to-integrate-yumiadsdk-to-mopub-mediation-sdk)
      * [1. Mopub Mediation SDK  Integrated YumiAdSDK adapter](#1-mopub-mediation-sdk--integrated-yumiadsdk-adapter)
         * [1.1 Add YumiAdSDK Mopub Adapter to your project](#11-add-yumiadsdk-mopub-adapter-to-your-project)
         * [1.2 add build.gradle dependencies](#12-add-buildgradle-dependencies)
      * [2. Add new custom sdk network in Mopub dashboard](#2-add-new-custom-sdk-network-in-mopub-dashboard)
         * [2.1 Click new network button](#21-click-new-network-button)
         * [2.2 Choose custom sdk network](#22-choose-custom-sdk-network)
         * [2.3 Enter the network name，like YumiAdSDK](#23-enter-the-network-namelike-yumiadsdk)
         * [2.4 Enter Custom event class and Custom event class data](#24-enter-custom-event-class-and-custom-event-class-data)
         * [2.5 Make sure the network status is running.](#25-make-sure-the-network-status-is-running)
         * [2.6 Choose the Segments -&gt; Global Segment](#26-choose-the-segments---global-segment)
         * [2.7 Make sure the source status is running.](#27-make-sure-the-source-status-is-running)
         * [2.8 Final check list](#28-final-check-list)
      * [3. Note](#3-note)
         * [3.1 Permissions for Android 6.0 and newer versions（Optional）](#31-permissions-for-android-60-and-newer-versionsoptional)
         * [3.2 If you loaded YumiAdSDK  rewarded video ads, please call the MoPub.onDestroy(this) interface in your Activity's onDestroy method.](#32-if-you-loaded-yumiadsdk--rewarded-video-ads-please-call-the-mopubondestroythis-interface-in-your-activitys-ondestroy-method)
		 * [3.3 targetSdkVersion &gt;= 24 compatibility considerations](#33-targetsdkversion--24-compatibility-considerations)
      * [4.Test SLOTID](#4test-slotid)


# How to integrate YumiAdSDK to Mopub Mediation SDK


## 1. Mopub Mediation SDK  Integrated YumiAdSDK adapter

### 1.1 Add YumiAdSDK Mopub Adapter to your project
[Download YumiAdSDK  Mopub Adapter](https://github.com/yumimobi/MopubAdapter-YumiAd-Android/tree/master/mopubadapter/src/main/java/com/yumiad/mopubadapter)

![Alt text](./android-image4.png)

### 1.2 add build.gradle dependencies

```java
//add YumiAdSDK maven url to project's build.gradle
buildscript {
    repositories {
   	 jcenter()
    }
}
allprojets {
    repositories {
        jcenter()
		//mopub sdk maven
        maven { url "https://s3.amazonaws.com/moat-sdk-builds" }
    }
}

//add YumiAdSDK and mopub dependencies.
dependencies {
	//YumiAdSDK
    implementation 'com.yumimobi.ads:yumiad:1.1.0'
	// mopub sdk
    implementation('com.mopub:mopub-sdk:+@aar') {
        transitive = true
    }
}
```

## 2. Add new custom sdk network in Mopub dashboard
### 2.1 Click new network button
![Alt text](./1560409646437.png)
### 2.2 Choose custom sdk network 
![Alt text](./1560409697619.png)
### 2.3 Enter the network name，like YumiAdSDK
![Alt text](./1560409809820.png)
### 2.4 Enter Custom event class and Custom event class data
- for Banner:
	- custom event class should be `com.yumiad.mopubadapter.YumiAdBanner`
	- custom event class data should be
		`{ "slotId":"YOUR_SLOTID"}`

- for Interstitial:
	- custom event class should be `com.yumiad.mopubadapter.YumiAdInterstitial`
	- custom event class data should be
		`{ "slotId":"YOUR_SLOTID"}`

- for RewardedVideo:
	- custom event class should be `com.yumiad.mopubadapter.YumiAdRewardedVideo`
	- custom event class data should be
		`{ "slotId":"YOUR_SLOTID"}`

![Alt text](./1560409912883.png)
### 2.5 Make sure the network status is running.
![Alt text](./1560410523146.png)
### 2.6 Choose the Segments -> Global Segment
![Alt text](./1560410634439.png)
### 2.7 Make sure the source status is running.
![Alt text](./1560410708081.png)
### 2.8 Final check list
Apps->Your_App->Your_Ad_Unit_Name-> Ad source 
	make sure the ad source status is running.
![Alt text](./1560410861974.png)

## 3. Note

### 3.1 Permissions for Android 6.0 and newer versions（Optional）

	When the targetSdkVersion of your app is 23 or above, you can choose the following method to check permission and prompt for user authorization

	Please at Mopub custom event class data add runInCheckPermissions：
	
`{ "slotId":"YOUR_SLOTID", "runInCheckPermissions": true}`
	
<div style="background-color:rgb(228,244,253);padding:10px;">
	<span style="color:rgb(62,113,167);">
	<b>Note: </b>The default setting for this runInCheckPermissions is false, it won’t prompt for user authorisation or causing crash. If set as true, it will check permission and prompt for user authorisation with popups.
	</span>
	</div>

<div style="background-color:rgb(228,244,253);padding:10px;">
	<span style="color:rgb(255,0,0);">
	<b>Important Note: </b>Please ensure can get READ_PHONE_STATE, WRITE_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION permissions, if not get these permissions, it will affect the revenue of the ads.
	</span>
	</div>

### 3.2 If you loaded YumiAdSDK  rewarded video ads, please call the MoPub.onDestroy(this) interface in your Activity's onDestroy method.
    
![Alt text](./android-image5.png)
<div style="background-color:rgb(228,244,253);padding:10px;">
	<span style="color:rgb(62,113,167);">
	<b>Note：</b>YumiAdSDK will clean up the video data after calling the MoPub.onDestroy(this) interface.
	</span>
	</div>

### 3.3 targetSdkVersion >= 24 compatibility considerations
 when you package the app setting targetSdkVersion >= 24 , in order for the SDK to download and install the App class ads can be support normally, you must follow the steps below for compatibility.
 
 **Step 1: Add this provider tag in the Application tag at AndroidManifest.xml**
  ```java
     <provider
      android:name="android.support.v4.content.FileProvider"
      android:authorities="${applicationId}.fileprovider"
      android:exported="false"
      android:grantUriPermissions="true">
      <meta-data
          android:name="android.support.FILE_PROVIDER_PATHS"
          android:resource="@xml/gdt_file_path" />
     </provider>

     <provider
            android:name="com.baidu.mobads.openad.FileProvider"
            android:authorities="${applicationId}.bd.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/bd_file_paths" />
    </provider>
  ```
<div style="background-color:rgb(228,244,253);padding:10px;">
<span style="color:rgb(62,113,167);">
<b>Note：</b>If your project does not support the ${applicationId} configuration, you can replace ${applicationId} with your app package name.
</span>
</div>

**Step 2: Add an xml folder under the res directory under the project structure, download the bd_file_paths.xml and gdt_file_path.xml files, and add the downloaded xml file to the created xml folder:**

Download [bd_file_paths.xml](https://github.com/yumimobi/MopubAdapter-YumiAd-Android/tree/master/app/src/main/res/xml/bd_file_paths.xml)

Download [gdt_file_path.xml](https://github.com/yumimobi/MopubAdapter-YumiAd-Android/tree/master/app/src/main/res/xml/gdt_file_path.xml)

<div style="background-color:rgb(228,244,253);padding:10px;">
<span style="color:rgb(250,0,0);">
<b>Note：</b> If you do not configure the above, it will affect the advertising revenue.
</span>
</div>

## 4.Test SLOTID 

| OS | Slot Format | Slot ID |
| ----- | ----- | ----- |
| Android |  banner | uz852t89 |
| Android |  Interstitial | 56ubk22h |
| Android | Reawrd Video | ew9hyvl4 |
