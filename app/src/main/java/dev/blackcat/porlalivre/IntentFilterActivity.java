package dev.blackcat.porlalivre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import dev.blackcat.porlalivre.data.db.DatabaseHelper;
import dev.blackcat.porlalivre.process.DeserializerProcess;
import dev.blackcat.porlalivre.process.ProcessService;

public class IntentFilterActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		String intentData = getIntent().getDataString();
		if (intentData != null)
		{
			if (intentData.startsWith("file://"))
				intentData = intentData.substring(7);

			Intent intent = new Intent(this, ProcessService.class);
			intent.putExtra(ProcessService.TYPE, ProcessService.TYPE_DESERIALIZER);
			intent.putExtra(ProcessService.INPUT_FILE, intentData);
			startService(intent);

			finish();
		}
	}
	
}
