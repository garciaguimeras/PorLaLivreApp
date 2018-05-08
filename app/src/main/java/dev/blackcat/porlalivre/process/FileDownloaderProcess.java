package dev.blackcat.porlalivre.process;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import dev.blackcat.porlalivre.LogActivity;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.bus.BusFactory;
import dev.blackcat.porlalivre.bus.ProcessBusEvent;
import dev.blackcat.porlalivre.utils.NotificationHelper;

public class FileDownloaderProcess implements Processable
{

    private Context context;
    private String inputURL;
    private String outputFile;

    public FileDownloaderProcess(Context context, String inputURL, String outputFile)
    {
        this.context = context;
        this.inputURL = inputURL;
        this.outputFile = outputFile;
    }

    private boolean downloadFile(NotificationHelper helper, int notificationId, String inputURL, String outputFile)
    {
        try
        {
            int count;
            URL url = new URL(inputURL);
            URLConnection connection = url.openConnection();
            connection.connect();
            int fileLength = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(outputFile);

            int progress = 0;
            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1)
            {
                progress += count;
                output.write(data, 0, count);

                helper.updateNotificationProgress(notificationId, fileLength, progress, false);
                BusFactory.getProcessBus().post(new ProcessBusEvent(context.getString(R.string.text_downloading_update), fileLength, progress));
            }

            output.flush();
            output.close();
            input.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void execute()
    {
        NotificationHelper helper = new NotificationHelper(context);

        final Intent intent = new Intent(context, LogActivity.class);
        ProcessBusEvent event = new ProcessBusEvent(context.getString(R.string.text_downloading_update));
        intent.putExtra(LogActivity.EVENT, event);
        int id = helper.createNotification(context.getString(R.string.app_name), context.getString(R.string.text_downloading_update), R.drawable.app_icon, intent, false);
        BusFactory.getProcessBus().post(event);

        boolean result = downloadFile(helper, id, inputURL, outputFile);
        if (result)
        {
            helper.cancelNotification(id);
            new DeserializerProcess(context, outputFile).execute();
        }
        else
        {
            event = new ProcessBusEvent(context.getString(R.string.text_downloading_update_error));
            intent.putExtra(LogActivity.EVENT, event);
            helper.updateNotification(id, context.getString(R.string.text_downloading_update_error), R.drawable.app_icon, true);
            BusFactory.getProcessBus().post(event);
        }

    }
}
