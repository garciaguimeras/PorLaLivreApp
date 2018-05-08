package dev.blackcat.porlalivre.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import dev.blackcat.porlalivre.data.AnnounceFilter;
import dev.blackcat.porlalivre.data.FavoritesFilter;
import dev.blackcat.porlalivre.data.SysInfo;

public class DBv2
{

    public static void onCreate(Context context, SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) throws SQLException
    {
        TableUtils.createTableIfNotExists(connectionSource, FavoritesFilter.class);
        TableUtils.createTableIfNotExists(connectionSource, AnnounceFilter.class);
        TableUtils.createTableIfNotExists(connectionSource, SysInfo.class);

        SysInfo.save(context, new SysInfo());
    }

}
