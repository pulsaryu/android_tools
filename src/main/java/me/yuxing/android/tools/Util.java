package me.yuxing.android.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 常用工具类
 * Created by yuxing on 2014-09-02.
 */
public class Util {

    public static int getAppVersion(Context context) {
        int appVersion = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
//            throw new RuntimeException("Could not get package name: " + e);
        }

        return appVersion;
    }

    public static String getAppVersionName(final Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            //throw new RuntimeException("Could not get package name: " + e);
        }
        return "unknown";
    }

    public static boolean isNetworkConnected(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected())
            return true;

        return false;
    }

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String buildQuery(Map<String, String> params) {
        Set<String> keys = params.keySet();
        StringBuilder sb = new StringBuilder();
        try {
            boolean first = true;
            for (String key : keys) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(URLEncoder.encode(params.get(key), "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static boolean checkInstall(Context context, String packageName) {

        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(0);
        for (ApplicationInfo app : apps) {
            if (app.packageName.equals(packageName)) {
                return true;
            }
        }

        return false;
    }

    public synchronized static String deviceId(Context context) {
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (deviceId == null) {
            deviceId = Installation.id(context);
        }

        return deviceId;
    }

    public static void showView(int visible, View... views) {
        for (View view : views) {
            view.setVisibility(visible);
        }
    }

    public static String implode(List<?> list, String split) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object item : list) {
            if (first) {
                first = false;
            } else {
                sb.append(split);
            }
            sb.append(item);
        }

        return sb.toString();
    }
}
