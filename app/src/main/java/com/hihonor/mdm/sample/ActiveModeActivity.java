/*
 * Copyright (c) Honor Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.hihonor.mdm.sample;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hihonor.android.app.admin.DeviceControlManager;

/**
 * The ActiveModeActivity for this Sample
 *
 * @author honor mdm
 * @since 2020-10-15
 */
public class ActiveModeActivity extends Activity {
    private DeviceControlManager mDeviceControlManager = null;
    private ComponentName mAdminName = null;
    private EditText editDelayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activemode_layout);
        mDeviceControlManager = new DeviceControlManager();
        mAdminName = new ComponentName(this, SampleDeviceReceiver.class);
        initView();
    }

    private void initView() {
        editDelayTime = (EditText) findViewById(R.id.edit_input);
    }

    private void userActiveAdmin() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
        ActiveModeActivity.this.startActivity(intent);
    }

    private void forceActiveAdmin() {
        try {
            mDeviceControlManager.setForcedActiveDeviceAdmin(mAdminName, ActiveModeActivity.this);
        } catch (NoSuchMethodError error) {
            Toast.makeText(ActiveModeActivity.this, getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException | IllegalArgumentException e) {
            Toast.makeText(ActiveModeActivity.this, getString(R.string.force_active_error), Toast.LENGTH_LONG).show();
        }
    }

    private void delayDeactiveAdmin() {
        try {
            String hourTime = editDelayTime.getText().toString();
            int hour = Integer.parseInt(hourTime);
            mDeviceControlManager.setDelayDeactiveDeviceAdmin(mAdminName, hour, ActiveModeActivity.this);
        } catch (NoSuchMethodError error) {
            Toast.makeText(ActiveModeActivity.this, getString(R.string.not_support), Toast.LENGTH_SHORT).show();
        } catch (SecurityException | IllegalArgumentException e) {
            Toast.makeText(ActiveModeActivity.this, getString(R.string.delay_deactive_error), Toast.LENGTH_LONG).show();
        }
    }

    public void userActiveAdmin(View view) {
        userActiveAdmin();
    }

    public void forceActiveAdmin(View view) {
        forceActiveAdmin();
    }

    public void delayDeactiveAdmin(View view) {
        delayDeactiveAdmin();
    }
}
