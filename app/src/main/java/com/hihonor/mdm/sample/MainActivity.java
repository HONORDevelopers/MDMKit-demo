/*
 * Copyright (c) Honor Technologies Co., Ltd. 2019-2020. All rights reserved.
 */

package com.hihonor.mdm.sample;

import android.os.Bundle;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hihonor.android.app.admin.DeviceApplicationManager;
import com.hihonor.android.app.admin.DeviceControlManager;
import com.hihonor.android.app.admin.DeviceHwSystemManager;
import com.hihonor.android.app.admin.DeviceRestrictionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * The MainActivity for this Sample
 *
 * @author honor mdm
 * @since 2019-10-23
 */
public class MainActivity extends Activity {
    private static final String PACKAGE_NAME = "com.hihonor.mdm.sample";

    private DeviceRestrictionManager mDeviceRestrictionManager = null;
    private DevicePolicyManager mDevicePolicyManager = null;
    private DeviceControlManager mDeviceControlManager = null;
    private DeviceApplicationManager mDeviceApplicationManager = null;
    private DeviceHwSystemManager mDeviceHwSystemManager = null;
    private ComponentName mAdminName = null;
    private ArrayList<String> packageNames = null;
    private TextView mWifiStatusText;
    private TextView mKeepAliveStatusText;
    private TextView mScreenCaptureStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceRestrictionManager = new DeviceRestrictionManager();
        mDeviceControlManager = new DeviceControlManager();
        mDeviceApplicationManager = new DeviceApplicationManager();
        mDeviceHwSystemManager = new DeviceHwSystemManager();
        mAdminName = new ComponentName(this, SampleDeviceReceiver.class);
        packageNames = new ArrayList<String>();
        packageNames.add(PACKAGE_NAME);

        initSampleView();
        updateState();

        new SampleEula(this, mDevicePolicyManager, mAdminName).show();
    }

    private void initWifiView() {
        mWifiStatusText = (TextView) findViewById(R.id.wifiStateTxt);
    }

    private void initKeepAliveView() {
        mKeepAliveStatusText = (TextView) findViewById(R.id.keepAliveStateTxt);
    }

    private void initCaptureView() {
        mScreenCaptureStatusText = (TextView) findViewById(R.id.screenCaptureStateTxt);
    }

    private void initSampleView() {
        initWifiView();
        initKeepAliveView();
        initCaptureView();
    }

    private void updateState() {
        if (!isActiveMe()) {
            mWifiStatusText.setText(getString(R.string.state_not_actived));
            mScreenCaptureStatusText.setText(getString(R.string.state_not_actived));
            mKeepAliveStatusText.setText(getString(R.string.state_not_actived));
            return;
        }

        boolean isWifiDisabled = false;
        boolean isScreenCaptureDisabled = false;
        boolean isKeptAlive = false;
        try {
            isWifiDisabled = mDeviceRestrictionManager.isWifiDisabled(mAdminName);
            isScreenCaptureDisabled = mDeviceRestrictionManager.isScreenCaptureDisabled(mAdminName);
            isKeptAlive = isPackageKeptAlive(mAdminName, PACKAGE_NAME);
        } catch (NoSuchMethodError error) {
            Toast.makeText(getApplicationContext(), getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException securityException) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
        }
        if (isWifiDisabled) {
            mWifiStatusText.setText(R.string.state_restricted);
        } else {
            mWifiStatusText.setText(getString(R.string.state_nomal));
        }
        if (isScreenCaptureDisabled) {
            mScreenCaptureStatusText.setText(R.string.state_restricted);
        } else {
            mScreenCaptureStatusText.setText(getString(R.string.state_nomal));
        }
        if (isKeptAlive) {
            mKeepAliveStatusText.setText(R.string.state_keep_alive);
        } else {
            mKeepAliveStatusText.setText(getString(R.string.state_cancel_alive));
        }
    }

    private boolean isPackageKeptAlive(ComponentName admin, String pacakgeName) {
        boolean isKeptAlive = false;
        List<String> persistentApps = mDeviceApplicationManager.getPersistentApp(admin);
        ArrayList<String> superTrustApps = mDeviceHwSystemManager.getSuperTrustListForHwSystemManger(admin);
        if (persistentApps != null && persistentApps.contains(pacakgeName)
                && superTrustApps != null && superTrustApps.contains(pacakgeName)) {
            isKeptAlive = true;
        }
        return isKeptAlive;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateState();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isActiveMe() {
        if (mDevicePolicyManager == null) {
            return false;
        } else {
            return mDevicePolicyManager.isAdminActive(mAdminName);
        }
    }

    public void disableWifi(View view) {
        try {
            mDeviceRestrictionManager.setWifiDisabled(mAdminName, true);
        } catch (NoSuchMethodError error) {
            Toast.makeText(getApplicationContext(), getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException securityException) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
        }
        updateState();
    }

    public void enableWifi(View view) {
        try {
            mDeviceRestrictionManager.setWifiDisabled(mAdminName, false);
        } catch (NoSuchMethodError error) {
            Toast.makeText(getApplicationContext(), getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException securityException) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
        }
        updateState();
    }

    public void disableScreenCapture(View view) {
        try {
            mDeviceRestrictionManager.setScreenCaptureDisabled(mAdminName, true);
        } catch (NoSuchMethodError error) {
            Toast.makeText(getApplicationContext(), getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException securityException) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
        }
        updateState();
    }

    public void enableScreenCapture(View view) {
        try {
            mDeviceRestrictionManager.setScreenCaptureDisabled(mAdminName, false);
        } catch (NoSuchMethodError error) {
            Toast.makeText(getApplicationContext(), getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException securityException) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
        }
        updateState();
    }

    public void keepAlive(View view) {
        try {
            mDeviceApplicationManager.addPersistentApp(mAdminName, packageNames);
            mDeviceHwSystemManager.setSuperTrustListForHwSystemManger(mAdminName, packageNames);
        } catch (NoSuchMethodError error) {
            Toast.makeText(getApplicationContext(), getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException securityException) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
        }
        updateState();
    }

    public void cancelAlive(View view) {
        try {
            mDeviceApplicationManager.removePersistentApp(mAdminName, packageNames);
            mDeviceHwSystemManager.removeSuperTrustListForHwSystemManger(mAdminName, packageNames);
        } catch (NoSuchMethodError error) {
            Toast.makeText(getApplicationContext(), getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException securityException) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
        }
        updateState();
    }

    public void removeActiveAdmin(View view) {
        try {
            mDeviceControlManager.removeActiveDeviceAdmin(mAdminName);
        } catch (NoSuchMethodError error) {
            Toast.makeText(getApplicationContext(), getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException securityException) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
        }
        updateState();
    }
}