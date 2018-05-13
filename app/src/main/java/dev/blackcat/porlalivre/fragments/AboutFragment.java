package dev.blackcat.porlalivre.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.utils.AppInfo;
import dev.blackcat.porlalivre.utils.DeviceUtils;

public class AboutFragment extends Fragment
{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        String text = "";
        TextView textView;
        final AppInfo info = DeviceUtils.getAppInfo(getActivity());

        textView = view.findViewById(R.id.aboutAppVersionTextView);
        text = getString(R.string.about_app_version);
        text = text.replace("${version}", info.packageVersion);
        textView.setText(text);

        text = getString(R.string.about_app_description);
        textView = view.findViewById(R.id.aboutAppDescriptionTextView);
        textView.setText(Html.fromHtml(text));

        text = getString(R.string.about_app_disclaimer);
        textView = view.findViewById(R.id.aboutAppDisclaimerTextView);
        textView.setText(Html.fromHtml(text));

        Button button = view.findViewById(R.id.aboutSendDevMail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String address = getString(R.string.dev_mail_address);
                String subject = getString(R.string.dev_mail_subject);
                String text = getString(R.string.dev_mail_text);
                text = text.replace("${appName}", info.packageName);
                text = text.replace("${appVersion}", info.packageVersion);
                DeviceUtils.sendMail(getActivity(), address, subject, text);
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.action_about);
        actionBar.setHomeAsUpIndicator(R.drawable.menu_icon);
    }

}
