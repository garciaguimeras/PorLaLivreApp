package dev.blackcat.porlalivre.data;

import android.content.Context;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.blackcat.porlalivre.data.readers.AssetsSiteReader;

public class Site extends JSONData
{

	private static List<Site> sites;

	@JsonProperty("id") public long id;
	@JsonProperty("name") public String name;
	@JsonProperty("domain") public String domain;
	
	public Site()
	{}
	
	public Site(long id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	@Override
	public JSONData find(List<? extends JSONData> list, Object value) 
	{
		for (JSONData data: list)
		{
			Site a = (Site) data;
			long id = (Long) value;
			
			if (a.id == id)
				return a;
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	public static List<Site> getSites(Context context)
	{
		if (sites == null)
			sites = new AssetsSiteReader().readSites(context);
		return sites;
	}
	
}
