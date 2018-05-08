package dev.blackcat.porlalivre.data.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import dev.blackcat.porlalivre.data.Announce;

public class DBv1
{
    public static void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) throws SQLException
    {
        TableUtils.createTableIfNotExists(connectionSource, Announce.class);
    }
}
