package dev.blackcat.porlalivre.process;

import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

import dev.blackcat.porlalivre.LogActivity;
import dev.blackcat.porlalivre.R;
import dev.blackcat.porlalivre.bus.BusFactory;
import dev.blackcat.porlalivre.bus.ProcessBusEvent;
import dev.blackcat.porlalivre.data.Announce;
import dev.blackcat.porlalivre.utils.NotificationHelper;

public class DataCleanerProcess implements Processable
{

    private Context context;
    private int days;

    public DataCleanerProcess(Context context, int days)
    {
        this.context = context;
        this.days = days;
    }

    @Override
    public void execute()
    {
        NotificationHelper helper = new NotificationHelper(context);
        Calendar daysAgo = Calendar.getInstance();
        daysAgo.add(Calendar.DATE, -days);
        String title = context.getString(R.string.text_cleaning_data);

        final Intent intent = new Intent(context, LogActivity.class);
        ProcessBusEvent event = new ProcessBusEvent(title);
        intent.putExtra(LogActivity.EVENT, event);
        int id = helper.createNotification(context.getString(R.string.app_name), title, R.drawable.app_icon, intent, true);
        BusFactory.getProcessBus().post(event);

        List<Announce> announces = Announce.getOldAndNotStarred(context, daysAgo.getTime());
        int totalAnnounces = announces.size();

        for (int i = 0; i < announces.size(); i += 100)
        {
            int progress = Math.min(i + 100, announces.size());
            Announce.remove(context, announces.subList(i, progress));

            helper.updateNotificationProgress(id, totalAnnounces, progress, true);
            BusFactory.getProcessBus().post(new ProcessBusEvent(title, totalAnnounces, progress));
        }
        helper.cancelNotification(id);

        String summary = context.getString(R.string.text_cleaning_data_finished);
        String totalAnnouncesStr = context.getString(R.string.text_total_announces).replace("${totalAnnounces}", Integer.toString(totalAnnounces));
        String deletedAnnouncesStr = context.getString(R.string.text_deleted_announces).replace("${deletedAnnounces}", Integer.toString(totalAnnounces));
        String[] lines = new String[] { summary, totalAnnouncesStr, deletedAnnouncesStr };

        event = new ProcessBusEvent(title, lines);
        intent.putExtra(LogActivity.EVENT, event);
        helper.createNotification(context.getString(R.string.app_name), summary, lines, R.drawable.app_icon, intent,true);
        BusFactory.getProcessBus().post(event);
    }

}
