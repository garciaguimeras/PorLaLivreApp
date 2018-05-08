package dev.blackcat.porlalivre.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.dialogs.DialogBuilder;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by noel on 3/25/18.
 */
public class DeviceUtils
{

    public static void sendMail(Context context, String addressTo, String subject, String text)
    {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { addressTo });
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);

        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.dialog_mail)));
    }

    public static void call(final Activity activity, final String phone)
    {
        try
        {
            String text = activity.getString(R.string.dialog_call);
            text = text.replace("${phone}", phone);

            AlertDialog dialog = DialogBuilder.oneButtonDialog(activity, "", text, activity.getString(R.string.button_accept), new DialogBuilder.OnDialogButtonListener() {
                @Override
                public void onPositiveButtonSelected()
                {
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.CALL_PHONE}, 1);
                        return;
                    }

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    activity.startActivity(callIntent);
                }
            });

            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static AppInfo getAppInfo(Context context)
    {
        String name = context.getString(R.string.app_name);
        try
        {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return new AppInfo(info.packageName + "." + name, info.versionName + "." + info.versionCode);
        }
        catch (PackageManager.NameNotFoundException e)
        { }
        return new AppInfo(name, "unknown");
    }

    public static void dismissKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText())
        {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

}
