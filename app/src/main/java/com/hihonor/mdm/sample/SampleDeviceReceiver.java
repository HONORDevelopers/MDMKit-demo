/*
 * Copyright (c) Honor Technologies Co., Ltd. 2019-2020. All rights reserved.
 */

package com.hihonor.mdm.sample;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * The DeviceAdminReceiver for this Sample
 *
 * @author honor mdm
 * @since 2019-10-23
 */
public class SampleDeviceReceiver extends DeviceAdminReceiver {
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return context.getString(R.string.disable_warning);
    }
}