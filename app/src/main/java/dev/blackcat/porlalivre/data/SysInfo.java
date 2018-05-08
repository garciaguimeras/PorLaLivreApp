package dev.blackcat.porlalivre.data;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

import java.util.Calendar;
import java.util.Date;

import dev.blackcat.porlalivre.data.db.DatabaseHelper;

public class SysInfo
{

    @DatabaseField(id = true) public int id;

    @DatabaseField public int systemVersion;
    @DatabaseField public Date lastSystemUpdate;

    @DatabaseField public Date lastDataUpdate;

    public SysInfo()
    {
        this.id = 0;
        this.systemVersion = 1;
        this.lastSystemUpdate = null;
        this.lastDataUpdate = null;
    }

    public SysInfo(Date lastDataUpdate)
    {
        this();

        this.lastDataUpdate = lastDataUpdate;
    }

    public static SysInfo get(Context context)
    {
        try
        {
            Dao<SysInfo, Integer> dao = DatabaseHelper.getSysInfoDao(context);
            SysInfo info = dao.queryForId(0);
            return info;
        }
        catch (Exception e)
        {}
        return new SysInfo();
    }

    public static void save(Context context, SysInfo info)
    {
        try
        {
            Dao<SysInfo, Integer> dao = DatabaseHelper.getSysInfoDao(context);
            dao.createOrUpdate(info);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
