package dev.blackcat.porlalivre.data.readers;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.data.Site;


public class AssetsSiteReader extends JSONReader 
{

	public List<Site> readSites(Context context)
	{
		return read(context, "sites.json", Site.class);
	}

}
