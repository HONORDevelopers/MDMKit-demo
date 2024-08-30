/*
 * Copyright (c) Honor Technologies Co., Ltd. 2019-2020. All rights reserved.
 */

package com.hihonor.mdm.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * The SampleEula for this Sample
 *
 * @author honor mdm
 * @since 2019-10-23
 */
public class SampleEula {
    private static final int REQUEST_ENABLE = 1;
    private static final String PERMISSION_STATEMENT_FILE = "honor_permission_statement.html";

    private Activity mActivity = null;
    private DevicePolicyManager mDevicePolicyManager = null;
    private ComponentName mAdminName = null;
    private boolean onlyShowOnce = false;

    public SampleEula(Activity context, DevicePolicyManager devicePolicyManager, ComponentName adminName) {
        mActivity = context;
        mDevicePolicyManager = devicePolicyManager;
        mAdminName = adminName;
    }

    /**
     * Show permission usage statement
     */
    @SuppressLint("InflateParams")
    public void show() {
        SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(mActivity);
        onlyShowOnce = sharedPreferenceUtil.hasUserAccepted();
        if (onlyShowOnce == false) {
            /* Show the Eula */
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setPositiveButton(mActivity.getString(R.string.accept_btn),
                        (dialog, which) -> {
                            sharedPreferenceUtil.saveUserChoice(onlyShowOnce);
                            dialog.dismiss();
                            activeProcess();
                        })
                    .setNegativeButton(mActivity.getString(R.string.exit_btn),
                        (dialog, which) -> {
                            mActivity.finish();
                        });
            AlertDialog eulaDialog = builder.create();
            eulaDialog.setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            View layout = inflater.inflate(R.layout.eula_layout, null);
            TextView permissionText = (TextView) layout.findViewById(R.id.content_permissions);
            String content = Utils.getStringFromHtmlFile(mActivity, PERMISSION_STATEMENT_FILE);
            permissionText.setText(Html.fromHtml(content));

            initStatementView(layout);

            CheckBox checkbox = (CheckBox) layout.findViewById(R.id.not_show_check);
            checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    onlyShowOnce = true;
                } else {
                    onlyShowOnce = false;
                }
            });
            eulaDialog.setView(layout);
            eulaDialog.show();
        } else {
            activeProcess();
        }
    }

    private void initStatementView(View layout) {
        if (layout == null) {
            return;
        }
        TextView statementText = (TextView) layout.findViewById(R.id.read_statement);
        statementText.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = statementText.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) text;
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans(); // should clear old spans
            for (URLSpan url : urls) {
                MyURLSpan myURLSpan = new MyURLSpan();
                style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            statementText.setText(style);
        }
    }

    private class MyURLSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            widget.setBackgroundColor(Color.parseColor("#00000000"));

            Intent intent = new Intent(mActivity, LicenseActivity.class);
            mActivity.startActivity(intent);
        }
    }

    private void activeProcess() {
        if (mDevicePolicyManager != null
                && !mDevicePolicyManager.isAdminActive(mAdminName)) {
            Intent intent = new Intent(mActivity, ActiveModeActivity.class);
            mActivity.startActivityForResult(intent, REQUEST_ENABLE);
        }
    }
}
