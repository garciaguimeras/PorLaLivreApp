package dev.blackcat.porlalivre.process;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


public class ProcessService extends IntentService
{

    public static final int TYPE_DESERIALIZER = 1;
    public static final int TYPE_DOWNLOADER = 2;
    public static final int TYPE_CLEANER = 3;

    public static final String TYPE = "dev.blackcat.porlalivre.Process.type";
    public static final String INPUT_FILE = "dev.blackcat.porlalivre.Process.inputFile";
    public static final String OUTPUT_FILE = "dev.blackcat.porlalivre.Process.outputFile";
    public static final String DAYS = "dev.blackcat.porlalivre.Process.days";


    public ProcessService()
    {
        super(ProcessService.class.getName());
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        synchronized (this)
        {
            Processable process = null;

            int type = (int)intent.getIntExtra(TYPE, 0);
            switch (type)
            {
                case TYPE_DESERIALIZER: {
                    String inFile = intent.getStringExtra(INPUT_FILE);
                    process = new DeserializerProcess(this, inFile);
                    break;
                }

                case TYPE_DOWNLOADER: {
                    String inFile = intent.getStringExtra(INPUT_FILE);
                    String outFile = intent.getStringExtra(OUTPUT_FILE);
                    process = new FileDownloaderProcess(this, inFile, outFile);
                    break;
                }

                case TYPE_CLEANER: {
                    int days = intent.getIntExtra(DAYS, 0);
                    process = new DataCleanerProcess(this, days);
                    break;
                }
            }

            if (process != null)
                process.execute();
        }
    }

}
