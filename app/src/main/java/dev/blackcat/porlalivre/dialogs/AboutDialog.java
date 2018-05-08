package dev.blackcat.porlalivre.dialogs;

import dev.blackcat.porlalivre.R;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutDialog extends DialogFragment
{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		Dialog dialog = getDialog();
		dialog.setTitle(getString(R.string.action_about));
		
		View view = inflater.inflate(R.layout.fragment_about, container, false);

		String text = "";
		TextView textView;

		text = getString(R.string.about_app_description);
		textView = (TextView)view.findViewById(R.id.aboutAppDescriptionTextView);
		textView.setText(Html.fromHtml(text));
		
		text = getString(R.string.about_app_disclaimer);
		textView = (TextView)view.findViewById(R.id.aboutAppDisclaimerTextView);
		textView.setText(Html.fromHtml(text));

		return view;
	}

}
