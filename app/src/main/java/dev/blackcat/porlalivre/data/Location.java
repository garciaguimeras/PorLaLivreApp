package dev.blackcat.porlalivre.data;

import android.content.Context;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.blackcat.porlalivre.data.readers.AssetsLocationReader;

public class Location extends JSONData
{

	private static List<Location> locations = null;

	@JsonProperty("id") public long id;
	@JsonProperty("name") public String name;
	@JsonProperty("parent_id") public long parentId;
	@JsonProperty("plcode") public long plCode;
	@JsonProperty("site_id") public long siteId;	
	
	@Override
	public JSONData find(List<? extends JSONData> list, Object value) 
	{
		for (JSONData data: list)
		{
			Location a = (Location) data;
			long id = (Long) value;
			
			if (a.id == id)
				return a;
		}
		return null;
	}

	public static List<Location> getLocations(Context context)
	{
		if (locations == null)
			locations = new AssetsLocationReader().readLocations(context);
		return locations;
	}
	
}
