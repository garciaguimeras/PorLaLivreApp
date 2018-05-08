package dev.blackcat.porlalivre.data;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import dev.blackcat.porlalivre.data.db.DatabaseHelper;

public class AnnounceFilter
{

	@DatabaseField(id = true) public Long id;
	@DatabaseField public double minPrice;
	@DatabaseField public double maxPrice;
	@DatabaseField public long site;
	@DatabaseField public boolean newerFirst;
	@DatabaseField public boolean cheaperFirst;
	@DatabaseField public String text;
	
	public AnnounceFilter(long id, double minPrice, double maxPrice, long site, boolean newerFirst, boolean cheaperFirst, String text)
	{
		this.id = id;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.site = site;
		this.newerFirst = newerFirst;
		this.cheaperFirst = cheaperFirst;
		this.text = text;
	}
	
	public AnnounceFilter(long id)
	{
		this(id, 0, 10000, 0, true, true, "");
	}

	public AnnounceFilter()
	{}

	public boolean isEmpty()
	{
		AnnounceFilter empty = new AnnounceFilter(id);
		return this.equals(empty);
	}

	@Override
	public boolean equals(Object obj)
	{
		AnnounceFilter f = (AnnounceFilter)obj;
		if (id != f.id)
			return false;
		if (minPrice != f.minPrice)
			return false;
		if (maxPrice != f.maxPrice)
			return false;
		if (site != f.site)
			return false;
		if (newerFirst != f.newerFirst)
			return false;
		if (cheaperFirst != f.cheaperFirst)
			return false;
		if (!text.equals(f.text))
			return false;
		return true;
	}

	public static AnnounceFilter get(Context context, long categoryId)
	{
		try
		{
			Dao<AnnounceFilter, Long> dao = DatabaseHelper.getAnnounceFilterDao(context);
			AnnounceFilter filter = dao.queryForId(categoryId);
			return (filter != null ? filter : new AnnounceFilter(categoryId));
		}
		catch (Exception e)
		{}
		return new AnnounceFilter(categoryId);
	}

	public static void save(Context context, AnnounceFilter filter)
	{
		try
		{
			Dao<AnnounceFilter, Long> dao = DatabaseHelper.getAnnounceFilterDao(context);
			dao.createOrUpdate(filter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
