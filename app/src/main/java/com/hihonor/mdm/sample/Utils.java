/*
 * Copyright (c) Honor Technologies Co., Ltd. 2019-2020. All rights reserved.
 */

package com.hihonor.mdm.sample;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The Utils for this Sample
 *
 * @author honor mdm
 * @since 2019-10-23
 */
public class Utils {
    private static final String TAG = "SampleUtils";
    private static final int EXPECTED_BUFFER_DATA = 2048;
    private static final int MAX_LENGTH = 1024;
    private static final int MAX_LINE_LENGTH = 128;

    /**
     * Get help string from html file
     *
     * @param context Context object
     * @param filePath html file path
     * @return string in html
     */
    public static String getStringFromHtmlFile(Context context, String filePath) {
        String result = "";
        if (context == null || filePath == null) {
            return result;
        }
        try (InputStreamReader streamReader =
                new InputStreamReader(context.getAssets().open(filePath), "utf-8");
                BufferedReader reader = new BufferedReader(streamReader)) {
            StringBuilder builder = new StringBuilder(EXPECTED_BUFFER_DATA);
            String line = null;

            boolean readCurrentLine = true;

            // Read each line of the html file, and build a string.
            while ((line = reader.readLine()) != null && line.length() < MAX_LINE_LENGTH) {
                // Don't read the Head tags when CSS styling is not supporeted.
                if (line.contains("<style")) {
                    readCurrentLine = false;
                }
                if (line.contains("</style")) {
                    readCurrentLine = true;
                }
                if (readCurrentLine && builder.length() < MAX_LENGTH) {
                    builder.append(line).append(System.lineSeparator());
                }
            }
            result = builder.toString();
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return result;
    }
}