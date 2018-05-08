package dev.blackcat.porlalivre.data.readers;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.data.Workshop;


public class AssetsWorkshopReader extends JSONReader
{

	public List<Workshop> readWorkshops(Context context)
	{
		return read(context, "workshops.json", Workshop.class);
	}

}
