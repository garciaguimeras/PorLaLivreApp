package dev.blackcat.porlalivre.data.readers;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.data.Announce;


public class AssetsAnnounceReader extends JSONReader 
{

	public List<Announce> readAnnounces(Activity context)
	{
		return read(context, "porlalivre.json", Announce.class);
	}

}
