package dev.blackcat.porlalivre.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.blackcat.porlalivre.data.readers.AssetsTagReader;

public class Tag extends JSONData
{

	private static List<Tag> tags = null;

	@JsonProperty("id") public long id;
	@JsonProperty("title") public String title;
	@JsonProperty("categories_id") public ArrayList<Long> categories;
	
	@Override
	public JSONData find(List<? extends JSONData> list, Object value) 
	{
		for (JSONData data: list)
		{
			Tag a = (Tag) data;
			long id = (Long) value;
			
			if (a.id == id)
				return a;
		}
		return null;
	}

	public static List<Tag> getTags(Context context)
	{
		if (tags == null)
			tags = new AssetsTagReader().readTags(context);
		return tags;
	}
	
}
