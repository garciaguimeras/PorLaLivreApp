package dev.blackcat.porlalivre;

import android.app.Application;
import android.os.Environment;

import dev.blackcat.porlalivre.data.AnnounceFilter;

public class PorLaLivreApp extends Application
{

    public static final String SDCARD_APP_DIR = Environment.getExternalStorageDirectory().getPath() + "/PorLaLivreDroid";

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

}
