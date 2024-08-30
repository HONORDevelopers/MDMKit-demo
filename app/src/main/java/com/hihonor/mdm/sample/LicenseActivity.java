/*
 * Copyright (c) Honor Technologies Co., Ltd. 2019-2020. All rights reserved.
 */

package com.hihonor.mdm.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * The LicenseActivity for this Sample
 *
 * @author honor mdm
 * @since 2019-10-23
 */
public class LicenseActivity extends Activity {
    private static final String LICENSE_FILE = "honor_software_license.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.license_layout);
        Button acceptBtn = (Button) findViewById(R.id.cancelBtn);
        acceptBtn.setOnClickListener(view -> finish());
        TextView licenseText = (TextView) findViewById(R.id.license_content);
        String content = Utils.getStringFromHtmlFile(this, LICENSE_FILE);
        licenseText.setText(Html.fromHtml(content));
    }
}
