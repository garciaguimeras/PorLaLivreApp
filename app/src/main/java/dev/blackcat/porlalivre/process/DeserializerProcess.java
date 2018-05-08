package dev.blackcat.porlalivre.process;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.Calendar;

import dev.blackcat.porlalivre.LogActivity;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.bus.BusFactory;
import dev.blackcat.porlalivre.bus.ProcessBusEvent;
import dev.blackcat.porlalivre.data.Announce;
import dev.blackcat.porlalivre.data.JSONData;
import dev.blackcat.porlalivre.data.SysInfo;
import dev.blackcat.porlalivre.data.readers.JacksonReader;
import dev.blackcat.porlalivre.data.writers.SQLiteAnnounceWriter;
import dev.blackcat.porlalivre.utils.NotificationHelper;

public class DeserializerProcess implements Processable
{

    private class DeserializerInfo
    {
        int added;
        int updated;
        int existing;
        int error;
    }

    private Context context;
    private String filename;

    public DeserializerProcess(Context context, String filename)
    {
        this.context = context;
        this.filename = filename;
    }

    @Override
    public void execute()
    {
        final NotificationHelper helper = new NotificationHelper(context);
        final String title = context.getString(R.string.text_importing_data);

        Intent intent = new Intent(context, LogActivity.class);
        ProcessBusEvent event = new ProcessBusEvent(title);
        intent.putExtra(LogActivity.EVENT, event);
        final int id = helper.createNotification(context.getString(R.string.app_name), title, R.drawable.app_icon, intent, false);
        BusFactory.getProcessBus().post(event);

        SQLiteAnnounceWriter pud = new SQLiteAnnounceWriter();
        File jsonFile = pud.decompress(new File(filename));

        final int totalAnnounces = pud.getTotalElements(jsonFile);
        final DeserializerProcess.DeserializerInfo info = new DeserializerProcess.DeserializerInfo();
        JacksonReader.Consumer sqliteConsumer = new JacksonReader.Consumer()
        {
            @Override
            public void consume(JSONData obj, int count)
            {
                Announce a = (Announce)obj;
                a.fixDateTimes();
                Announce.InsertStatus status = Announce.insertOrUpdateIfOld(context, a);
                if (status == Announce.InsertStatus.ADDED)
                    info.added++;
                if (status == Announce.InsertStatus.UPDATED)
                    info.updated++;
                if (status == Announce.InsertStatus.EXISTING)
                    info.existing++;
                if (status == Announce.InsertStatus.ERROR)
                    info.error++;

                helper.updateNotificationProgress(id, totalAnnounces, count, false);
                BusFactory.getProcessBus().post(new ProcessBusEvent(title, totalAnnounces, count));
            }
        };

        pud.deserialize(jsonFile, sqliteConsumer);
        pud.remove(jsonFile);
        SysInfo.save(context, new SysInfo(Calendar.getInstance().getTime()));
        helper.cancelNotification(id);

        String summary = context.getString(R.string.text_importing_data_finished);
        String totalAnnouncesStr = context.getString(R.string.text_total_announces).replace("${totalAnnounces}", Integer.toString(totalAnnounces));
        String addedAnnouncesStr = context.getString(R.string.text_added_announces).replace("${addedAnnounces}", Integer.toString(info.added));
        String updatedAnnouncesStr = context.getString(R.string.text_updated_announces).replace("${updatedAnnounces}", Integer.toString(info.updated));
        String existingAnnouncesStr = context.getString(R.string.text_existing_announces).replace("${existingAnnounces}", Integer.toString(info.existing));
        String errorAnnouncesStr = context.getString(R.string.text_error_announces).replace("${errorAnnounces}", Integer.toString(info.error));
        String[] lines = new String[] { summary, totalAnnouncesStr, addedAnnouncesStr, updatedAnnouncesStr, existingAnnouncesStr, errorAnnouncesStr };

        event = new ProcessBusEvent(title, lines);
        intent.putExtra(LogActivity.EVENT, event);
        helper.createNotification(context.getString(R.string.app_name), summary, lines, R.drawable.app_icon, intent, true);
        BusFactory.getProcessBus().post(event);
    }
}
