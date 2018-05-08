package dev.blackcat.porlalivre.dialogs;

import android.app.Activity;
import android.content.Intent;

import dev.blackcat.porlalivre.LogActivity;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.process.ProcessService;

public class DataCleanerDialog implements DialogBuilder.OnDialogButtonListener
{
	
	Activity activity;
	int days = 0;
	
	public DataCleanerDialog(Activity activity, int days)
	{
		this.activity = activity;
		this.days = days;
	}

	public void show()
	{
		String title = activity.getString(R.string.action_cleanup);
		String message = activity.getString(R.string.text_cleanup);
		message = message.replace("{DAYS}", Integer.toString(days));
		String buttonText = activity.getString(R.string.button_accept);
		DialogBuilder.oneButtonDialog(activity, title, message, buttonText, this);
	}

	@Override
	public void onPositiveButtonSelected() 
	{
		Intent logIntent = new Intent(activity, LogActivity.class);
		activity.startActivity(logIntent);

		Intent intent = new Intent(activity, ProcessService.class);
		intent.putExtra(ProcessService.TYPE, ProcessService.TYPE_CLEANER);
		intent.putExtra(ProcessService.DAYS, days);
		activity.startService(intent);
	}

}
