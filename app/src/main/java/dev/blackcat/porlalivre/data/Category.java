package dev.blackcat.porlalivre.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.blackcat.porlalivre.data.readers.AssetsCategoryReader;

public class Category extends JSONData
{

	private static List<Category> categories = null;

	@JsonProperty("id") public long id;
	@JsonProperty("parent_id") public long parentId;
	@JsonProperty("title") public String title;
	@JsonProperty("description") public String description;
	@JsonProperty("sort_order") public long sortOrder; 	
	
	public JSONData find(List<? extends JSONData> list, Object value) 
	{
		for (JSONData data: list)
		{
			Category c = (Category) data;
			long id = (Long)value;
			
			if (c.id == id)
				return c;
		}
		return null;
	}
	
	public List<Category> filterByParent(List<Category> list, long parentId)
	{
		ArrayList<Category> result = new ArrayList<Category>();
		for (Category c: list)
		{
			if (c.parentId == parentId)
				result.add(c);
		}
		return result;		
	}
	
	public Category findParent(List<Category> list, long id)
	{
		Category c = (Category) find(list, id);
		while (c.parentId != 0)
		{
			c = (Category) find(list, c.parentId);
		}
		return c;
	}

	public static List<Category> getCategories(Context context)
	{
		if (categories == null)
			categories = new AssetsCategoryReader().readCategories(context);
		return categories;
	}
	
}
