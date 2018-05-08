package dev.blackcat.porlalivre.data.readers;

import android.content.Context;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dev.blackcat.porlalivre.data.JSONData;

public class JSONReader
{

	public <T extends JSONData> List<T> read(Context context, String filename, Class<T> clazz)
	{
		try
		{
			InputStream is = context.getAssets().open(filename);
			ArrayListJacksonConsumer<T> consumer = new ArrayListJacksonConsumer<T>();
			JacksonReader reader = new JacksonReader();
			reader.read(is, clazz, consumer);
			return consumer.getArrayList();
		}
		catch (Exception e)
		{}
		return new ArrayList<T>();
	}

	
}
