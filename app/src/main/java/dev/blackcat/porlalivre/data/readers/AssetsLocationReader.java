package dev.blackcat.porlalivre.data.readers;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.data.Location;


public class AssetsLocationReader extends JSONReader 
{

	public List<Location> readLocations(Context context)
	{
		return read(context, "locations.json", Location.class);
	}

}
