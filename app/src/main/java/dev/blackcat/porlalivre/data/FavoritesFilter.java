package dev.blackcat.porlalivre.data;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import dev.blackcat.porlalivre.data.db.DatabaseHelper;

public class FavoritesFilter
{

    @DatabaseField(id = true) public int id;
    @DatabaseField public String text;

    public FavoritesFilter(String text)
    {
        this.id = 0;
        this.text = text;
    }

    public FavoritesFilter()
    {
        this("");
    }

    public boolean isEmpty()
    {
        FavoritesFilter empty = new FavoritesFilter();
        return this.equals(empty);
    }

    @Override
    public boolean equals(Object obj)
    {
        FavoritesFilter f = (FavoritesFilter)obj;
        if (!text.equals(f.text))
            return false;
        return true;
    }

    public static FavoritesFilter get(Context context)
    {
        try
        {
            Dao<FavoritesFilter, Integer> dao = DatabaseHelper.getFavoritesFilterDao(context);
            FavoritesFilter filter = dao.queryForId(0);
            return (filter != null ? filter : new FavoritesFilter());
        }
        catch (Exception e)
        {}
        return new FavoritesFilter();
    }

    public static void save(Context context, FavoritesFilter filter)
    {
        try
        {
            Dao<FavoritesFilter, Integer> dao = DatabaseHelper.getFavoritesFilterDao(context);
            dao.createOrUpdate(filter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
