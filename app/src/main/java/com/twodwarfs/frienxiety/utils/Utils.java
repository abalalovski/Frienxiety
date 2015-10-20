/**
 *
 */
package com.twodwarfs.frienxiety.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.twodwarfs.frienxiety.R;
import com.twodwarfs.frienxiety.ui.activities.ChatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Utils class used defining useful utilities methods.
 *
 * @author Aleksandar Balalovski <aleksandar.balalovski@nth.ch>
 */
public class Utils {

    /**
     * Show long Toast.
     *
     * @param context the context object
     * @param text    the text to be shown in the Toast
     */
    public static void doToast(Context context, String text) {
        if (context != null && !TextUtils.isEmpty(text)) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Show long Toast.
     *
     * @param context     the context object
     * @param stringResId the string resource ID to be shown in the Toast
     */
    public static void doToast(Context context, int stringResId) {
        if (context != null) {
            Toast.makeText(context, stringResId, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Check if it has an active connection.
     *
     * @param context some Context.
     * @return does it have an active Network connection.
     */
    public static boolean hasActiveNetworkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return ((networkInfo != null) && networkInfo.isConnected());
    }

    public static InputStream getRemoteContent(String url) {

        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);

        try {
            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Logger.doLog("Error " + statusCode + " for URL " + url);
            }

            HttpEntity getResponseEntity = getResponse.getEntity();
            return getResponseEntity.getContent();

        } catch (IOException e) {
            getRequest.abort();
            Logger.doLogException(e);
        }

        return null;
    }

    public static void openPdfFile(Context context, File pdfFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        if (isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
            searchAppOnPlayStore(context, "pdf reader");
        }
    }

    public static boolean isIntentAvailable(Context context, Intent checkIntent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                checkIntent, PackageManager.MATCH_DEFAULT_ONLY);
        return (resolveInfo.size() > 0);
    }

    public static void searchAppOnPlayStore(Context context, String appName) {
        // openExternalApp(context, C.PLAY_STORE_SCHEME + appName);
    }

    public static void openExternalApp(Context context, String uriString)
            throws ActivityNotFoundException {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                .parse(uriString)));
    }

    public static void makeCall(Context context, String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(String.format("tel:%s", phone)));
        context.startActivity(callIntent);
    }

    /**
     * Method returns file name from url link.
     *
     * @param url               File url.
     * @param extensionIncluded File extension included or not.
     * @return
     */
    public static String getFileNameFromUrl(String url,
                                            boolean extensionIncluded) {

        String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        String fileNameWithoutExtn = fileName.substring(0,
                fileName.lastIndexOf('.'));

        return extensionIncluded ? fileName : fileNameWithoutExtn;
    }

    public static int convertDpToPixel(Resources resources, float dp) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public static double getRoundedDouble(double value) {
        // DecimalFormat df = new DecimalFormat("#.00");
        // String formate = df.format(value);
        // return (Double) df.parse(formate);
        return (double) Math.round(value * 1000) / 1000;
    }

    /**
     * Fetch available user uniqueID. ID can be MSISDN (phone number), device
     * IMEI or androidID. On some devices may not be possible to fetch some of
     * data (for example: MSISDN is null if SIM card is not present) so all
     * cases are covered.
     *
     * @param context the current {@link Context}.
     * @return available user unique ID
     */

    public static String getAvailableID(Context context) {
        String availableID = null;

        availableID = getMSISDN(context);
        if (TextUtils.isEmpty(availableID)) {
            availableID = getIMEI(context);
            if (!TextUtils.isEmpty(availableID)) {
                return availableID;
            } else {
                availableID = getAndroidID(context);
                if (TextUtils.isEmpty(availableID)) {
                    availableID = "banana";
                }
            }
        }

        return availableID;
    }

    public static String getIMEI(Context context) {
        TelephonyManager mngr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }

    public static String getMSISDN(Context context) {
        TelephonyManager tMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        return tMgr.getLine1Number();
    }

    public static String getAndroidID(Context context) {
        return Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
    }

    public final static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static double getRandomDouble() {
        double start = 10;
        double end = 300;
        double random = new Random().nextDouble();

        return start + (random * (end - start));
    }

    public static int getRandomInt() {
        Random random = new Random();
        return random.nextInt(100 - 10 + 1) + 10;
    }

    public static String getRandomHelloMessage(Context context) {
        List<String> messages = Arrays.asList(context.getResources().getStringArray(
                R.array.welcome_messages));
        Collections.shuffle(messages);

        return messages.get(0);
    }
}
