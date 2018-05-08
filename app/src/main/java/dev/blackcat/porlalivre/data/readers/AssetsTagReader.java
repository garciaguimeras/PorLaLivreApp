package dev.blackcat.porlalivre.data.readers;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.data.Tag;


public class AssetsTagReader extends JSONReader 
{

	public List<Tag> readTags(Context context)
	{
		return read(context, "tags.json", Tag.class);
	}

}
