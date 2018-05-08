package dev.blackcat.porlalivre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LogSummaryAdapter extends BaseAdapter
{

    private Context context;
    private String[] summary;
    private LayoutInflater inflater;

    public LogSummaryAdapter(Context context, String[] summary)
    {
        this.context = context;
        this.summary = summary;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return summary.length;
    }

    @Override
    public Object getItem(int i)
    {
        return summary[i];
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
            view = inflater.inflate(R.layout.cell_log_summary, null);

        TextView textView = view.findViewById(R.id.logSummaryTextView);
        textView.setText(summary[i]);

        return view;
    }
}
