package dev.blackcat.porlalivre.data.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import dev.blackcat.porlalivre.PorLaLivreApp;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.data.Announce;
import dev.blackcat.porlalivre.data.AnnounceFilter;
import dev.blackcat.porlalivre.data.FavoritesFilter;
import dev.blackcat.porlalivre.data.SysInfo;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper 
{

	private static DatabaseHelper helper = null;

	private static final String DATABASE_NAME = PorLaLivreApp.SDCARD_APP_DIR + "/PorLaLivreDroid.sqlite";
	private static final int DATABASE_VERSION = 2;

	private Context context;

	public DatabaseHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);

		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) 
	{
		try 
		{
			DBv1.onCreate(sqliteDatabase, connectionSource);
			DBv2.onCreate(context, sqliteDatabase, connectionSource);
		}
		catch (SQLException e) 
		{
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) 
	{
		try 
		{
			if (oldVer < 2)
				DBv2.onCreate(context, sqliteDatabase, connectionSource);
		}
		catch (SQLException e) 
		{
			Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new " + newVer, e);
		}
	}

	public static DatabaseHelper getHelper(Context context)
	{
		if (helper == null)
			helper = new DatabaseHelper(context);
		return helper;
	}

	public static Dao<Announce, String> getAnnounceDao(Context context)
	{
		try
		{
			Dao<Announce, String> announceDao = getHelper(context).getDao(Announce.class);
			return announceDao;
		}
		catch (Exception e)
		{}
		return null;
	}

	public static Dao<FavoritesFilter, Integer> getFavoritesFilterDao(Context context)
	{
		try
		{
			Dao<FavoritesFilter, Integer> favoritesFiltersDao = getHelper(context).getDao(FavoritesFilter.class);
			return favoritesFiltersDao;
		}
		catch (Exception e)
		{}
		return null;
	}

	public static Dao<AnnounceFilter, Long> getAnnounceFilterDao(Context context)
	{
		try
		{
			Dao<AnnounceFilter, Long> announceFilterDao = getHelper(context).getDao(AnnounceFilter.class);
			return announceFilterDao;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Dao<SysInfo, Integer> getSysInfoDao(Context context)
	{
		try
		{
			Dao<SysInfo, Integer> sysInfoDao = getHelper(context).getDao(SysInfo.class);
			return sysInfoDao;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
