package dev.blackcat.porlalivre.dialogs;

import dev.blackcat.porlalivre.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class DialogBuilder
{
	
	public interface OnDialogButtonListener
	{
		void onPositiveButtonSelected();
	}

	public static AlertDialog simpleDialog(Activity activity, int messageId)
	{
		AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.setMessage(activity.getString(messageId));
		dialog.show();
		return dialog;
	}
	
	public static AlertDialog oneButtonDialog(Activity activity, int titleId, int messageId, final OnDialogButtonListener listener)
	{
		return oneButtonDialog(activity, titleId, messageId, R.string.button_accept, listener);
	}

	public static AlertDialog oneButtonDialog(Activity activity, int titleId, int messageId, int buttonTextId, final OnDialogButtonListener listener)
	{
		return oneButtonDialog(activity, activity.getString(titleId), activity.getString(messageId), activity.getString(buttonTextId), listener);
	}

	public static AlertDialog oneButtonDialog(Activity activity, String title, String message, String buttonText, final OnDialogButtonListener listener)
	{
		AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				listener.onPositiveButtonSelected();
			}
		});
		dialog.show();
		return dialog;
	}

}
