package dev.blackcat.porlalivre.data;

import android.content.Context;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.blackcat.porlalivre.data.readers.AssetsWorkshopReader;

public class Workshop extends JSONData
{

	private static List<Workshop> workshops = null;

	@JsonProperty("id") public long id;
	@JsonProperty("name") public String name;
	@JsonProperty("address") public String address;
	@JsonProperty("email") public String email;
	@JsonProperty("phone") public String phone;
	@JsonProperty("location_id") public long locationId;
	@JsonProperty("site_id") public long siteId;	
	@JsonProperty("description") public String description;
	
	@Override
	public JSONData find(List<? extends JSONData> list, Object value) 
	{
		for (JSONData data: list)
		{
			Workshop a = (Workshop) data;
			long id = (Long) value;
			
			if (a.id == id)
				return a;
		}
		return null;
	}

	public static List<Workshop> getWorkshops(Context context)
	{
		if (workshops == null)
			workshops = new AssetsWorkshopReader().readWorkshops(context);
		return workshops;
	}
	
}
