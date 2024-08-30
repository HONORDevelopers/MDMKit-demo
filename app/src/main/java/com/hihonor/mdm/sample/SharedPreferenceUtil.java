/*
 * Copyright (c) Honor Technologies Co., Ltd. 2019-2020. All rights reserved.
 */

package com.hihonor.mdm.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * The SharedPreferenceUtil for this Sample
 *
 * @author honor mdm
 * @since 2019-10-23
 */
public class SharedPreferenceUtil {
    private static final String TAG = "SharedPreferenceUtil";
    private static final int DEFAULT_VERSION_CODE = 0;
    private static String EULA_PREFIX = "eula_useraccepted_";
    private int mVersionCode;
    private String mEulaKey = null;
    private Context mContext = null;
    private SharedPreferences mSharedPreferences = null;

    public SharedPreferenceUtil(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // the eulaKey changes every time you increment the version number in AndroidManifest.xml
        mVersionCode = getVersionCodeInner();;
        mEulaKey = EULA_PREFIX + mVersionCode;
    }

    /**
     * Whether the user select not show again
     *
     * @return boolean true if selected otherwise false.
     */
    public boolean hasUserAccepted() {
        return mSharedPreferences.getBoolean(mEulaKey, false);
    }

    /**
     * Save user's choice to SharedPreferences.
     *
     * @param accepted Whether the user select not show again.
     */
    public void saveUserChoice(boolean accepted) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(mEulaKey, accepted);
        editor.commit();
    }

    private int getVersionCodeInner() {
        PackageInfo pi = null;
        try {
            pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Name not found exception");
        }
        return pi == null ? DEFAULT_VERSION_CODE : pi.versionCode;
    }
}