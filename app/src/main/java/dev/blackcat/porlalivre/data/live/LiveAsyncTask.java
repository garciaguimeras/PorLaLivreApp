package dev.blackcat.porlalivre.data.live;

import android.os.AsyncTask;

import java.util.List;

public class LiveAsyncTask<T> extends AsyncTask<Void, Void, List<T>>
{

    public interface LiveService<T>
    {
        void beforeStart();
        void afterFinish(List<T> list);
        List<T> populateData();
    }

    LiveService liveService;

    public LiveAsyncTask(LiveService liveService)
    {
        this.liveService = liveService;
    }

    @Override
    protected void onPreExecute()
    {
        if (liveService != null)
            liveService.beforeStart();
    }

    @Override
    protected List<T> doInBackground(Void... voids)
    {
        List<T> result = null;
        if (liveService != null)
            result = liveService.populateData();
        return result;
    }

    @Override
    protected void onPostExecute(final List<T> list)
    {
        if (liveService != null)
            liveService.afterFinish(list);
    }

    public static <T> void start(LiveService<T> liveService)
    {
        LiveAsyncTask<T> instance = new LiveAsyncTask<>(liveService);
        instance.execute();
    }

}
