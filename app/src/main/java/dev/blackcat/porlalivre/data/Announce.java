package dev.blackcat.porlalivre.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.data.db.DatabaseHelper;
import dev.blackcat.porlalivre.fragments.SearchFragment;

public class Announce extends JSONData
{

	@JsonProperty("id")	@DatabaseField(id = true) public String id;
	@JsonProperty("created_on") @DatabaseField public Date createDate;
	@JsonProperty("updated_on") @DatabaseField public Date updateDate;
	@JsonProperty("edited_on") @DatabaseField public Date editDate;
	
	@JsonProperty("title") @DatabaseField(dataType=DataType.LONG_STRING) public String title;
	@JsonProperty("description") @DatabaseField(dataType=DataType.LONG_STRING) public String description;
	@JsonProperty("price") @DatabaseField public double price;
	
	@JsonProperty("is_outstanding") @DatabaseField public boolean isOutstanding;
	@JsonProperty("has_images") @DatabaseField public boolean hasImages;
	@JsonProperty("mail_contact") @DatabaseField public boolean mailContact;
	@JsonIgnore @DatabaseField public boolean starred;
	
	@JsonProperty("category_id") @DatabaseField public long categoryId;
	@JsonProperty("tags_id") @DatabaseField(dataType=DataType.SERIALIZABLE) public ArrayList<Long> tags;
	@JsonProperty("site_id") @DatabaseField public long siteId;
	@JsonProperty("location_id") @DatabaseField public long locationId;
	
	@JsonProperty("contact_name") @DatabaseField(dataType=DataType.LONG_STRING) public String contactName;
	@JsonProperty("email") @DatabaseField(dataType=DataType.LONG_STRING) public String contactEmail;
	@JsonProperty("contact_phone") @DatabaseField(dataType=DataType.LONG_STRING) public String contactPhone;
	
	public enum InsertStatus
	{
        EXISTING, ADDED, UPDATED, ERROR;
	}
	
	private Date fixTime(Date date)
	{
		if (date == null)
			return null;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);		
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public void fixDateTimes()
	{
		createDate = fixTime(createDate);
		updateDate = fixTime(updateDate);
		editDate = fixTime(editDate);		
	}
	
	@Override
	public JSONData find(List<? extends JSONData> list, Object value) 
	{
		for (JSONData data: list)
		{
			Announce a = (Announce) data;
			String id = (String) value;
			
			if (a.id.equals(id))
				return a;
		}
		return null;
	}
	
	public static Announce find(Context context, String id)
	{
		try
		{
			Dao<Announce, String> announceDao = DatabaseHelper.getAnnounceDao(context);
			return announceDao.queryForId(id);
		}
		catch (Exception e)
		{}
		return null;
	}
	
	public static List<Announce> getOldAndNotStarred(Context context, Date date)
	{
		try
		{
			Dao<Announce, String> announceDao = DatabaseHelper.getAnnounceDao(context);
			QueryBuilder<Announce, String> queryBuilder = announceDao.queryBuilder();
			Where<Announce, String> where = queryBuilder.where();
			where.eq("starred", false);
			where.and().le("updateDate", date);
			queryBuilder.setWhere(where);
			return announceDao.query(queryBuilder.prepare());
		}
		catch (Exception e)
		{}		
		return new ArrayList<Announce>();		
	}

	public static List<Announce> filterByFavorites(Context context)
	{
		try
		{
			FavoritesFilter favoritesFilter = FavoritesFilter.get(context);

			Dao<Announce, String> announceDao = DatabaseHelper.getAnnounceDao(context);
			QueryBuilder<Announce, String> queryBuilder = announceDao.queryBuilder();
			Where<Announce, String> where = queryBuilder.where();
			where.eq("starred", true);
			if (favoritesFilter.text.length() > 0)
			{
				where.and(where, where.or(where.like("title", "%" + favoritesFilter.text + "%"),
						where.like("description", "%" + favoritesFilter.text + "%")));
			}
			return announceDao.query(queryBuilder.prepare());
		}
		catch (Exception e)
		{}
		return new ArrayList<Announce>();
	}

	public static List<Announce> filterByCategoryId(Context context, Long categoryId)
	{
		try
		{
			AnnounceFilter announceFilter = AnnounceFilter.get(context, categoryId);

			Dao<Announce, String> announceDao = DatabaseHelper.getAnnounceDao(context);
			QueryBuilder<Announce, String> queryBuilder = announceDao.queryBuilder();
			Where<Announce, String> where = queryBuilder.where();

			where.eq("categoryId", categoryId);
			if (announceFilter.site != 0)
				where.and().eq("siteId", announceFilter.site);
			where.and().between("price", announceFilter.minPrice, announceFilter.maxPrice);
	        if (announceFilter.text.length() > 0)
            {
                where.and(where, where.or(where.like("title", "%" + announceFilter.text + "%"),
                                          where.like("description", "%" + announceFilter.text + "%")));
            }
    		queryBuilder.setWhere(where);
			
			String orderingTypeDate = announceFilter.newerFirst ? "DESC" : "ASC";
			String orderingTypePrice = announceFilter.cheaperFirst ? "ASC" : "DESC";

			queryBuilder.orderByRaw("updateDate " + orderingTypeDate + ", price " + orderingTypePrice);
			
			return announceDao.query(queryBuilder.prepare());
		}
		catch (Exception e)
		{}		
		return new ArrayList<Announce>();
	}

	public static List<Announce> filterAll(Context context)
	{
		try
		{
			AnnounceFilter announceFilter = AnnounceFilter.get(context, SearchFragment.SEARCH_FILTER_ID);

			Dao<Announce, String> announceDao = DatabaseHelper.getAnnounceDao(context);
			QueryBuilder<Announce, String> queryBuilder = announceDao.queryBuilder();
			Where<Announce, String> where = queryBuilder.where();

			where.between("price", announceFilter.minPrice, announceFilter.maxPrice);
			if (announceFilter.site != 0)
				where.and().eq("siteId", announceFilter.site);
			if (announceFilter.text.length() > 0)
			{
				where.and(where, where.or(where.like("title", "%" + announceFilter.text + "%"),
						where.like("description", "%" + announceFilter.text + "%")));
			}
			queryBuilder.setWhere(where);

			String orderingTypeDate = announceFilter.newerFirst ? "DESC" : "ASC";
			String orderingTypePrice = announceFilter.cheaperFirst ? "ASC" : "DESC";

			queryBuilder.orderByRaw("updateDate " + orderingTypeDate + ", price " + orderingTypePrice);

			return announceDao.query(queryBuilder.prepare());
		}
		catch (Exception e)
		{}
		return new ArrayList<Announce>();
	}
	
	public static void remove(Context context, Announce a)
	{
		try
		{
			Dao<Announce, String> announceDao = DatabaseHelper.getAnnounceDao(context);
			announceDao.delete(a);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}			
	}

	public static void remove(Context context, Collection<Announce> list)
	{
		try
		{
			Dao<Announce, String> announceDao = DatabaseHelper.getAnnounceDao(context);
			announceDao.delete(list);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void update(Context context, Announce a)
	{
		try
		{
			Dao<Announce, String> announceDao = DatabaseHelper.getAnnounceDao(context);
			announceDao.update(a);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}			
	}
	
	public static InsertStatus insertOrUpdateIfOld(Context context, Announce a)
	{
		try
		{
			Dao<Announce, String> announceDao = DatabaseHelper.getAnnounceDao(context);
			Announce remote = announceDao.queryForId(a.id);
			if (remote == null)
			{
				announceDao.create(a);
				return InsertStatus.ADDED;
			}
			if (remote.updateDate.before(a.updateDate))
			{
				a.starred = remote.starred;
				announceDao.update(a);
				return InsertStatus.UPDATED;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return InsertStatus.ERROR;
		}
		return InsertStatus.EXISTING;
	}
	
}
