package dev.blackcat.porlalivre;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import dev.blackcat.porlalivre.bus.BusFactory;
import dev.blackcat.porlalivre.bus.ProcessBusEvent;
import dev.blackcat.porlalivre.data.Event;

public class LogActivity extends AppCompatActivity
{

    public static final String EVENT = "dev.blackcat.porlalivre.LogActivity.event";

    ImageView logImageView;
    TextView titleTextView;
    ProgressBar progressBar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);

        logImageView = (ImageView) findViewById(R.id.logLogoImageView);
        logImageView.setVisibility(View.GONE);

        titleTextView = (TextView) findViewById(R.id.logTitleTextView);
        titleTextView.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.logProgressBar);
        progressBar.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.logListView);
        listView.setVisibility(View.GONE);

        BusFactory.getProcessBus().register(this);

        Intent intent = getIntent();
        ProcessBusEvent event = (ProcessBusEvent)intent.getSerializableExtra(EVENT);
        if (event != null)
        {
            showEvent(event);
        }
    }

    private void showEvent(ProcessBusEvent event)
    {
        // Title
        if (event.title != null)
        {
            titleTextView.setText(event.title);
            titleTextView.setVisibility(View.VISIBLE);
            logImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            titleTextView.setVisibility(View.GONE);
        }

        // Progress bar
        if (event.max > 0)
        {
            progressBar.setProgress(event.progress);
            progressBar.setMax(event.max);
            progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
        }

        // List
        if (event.messages != null)
        {
            listView.setAdapter(new LogSummaryAdapter(LogActivity.this, event.messages));
            listView.setVisibility(View.VISIBLE);
        }
        else
        {
            listView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onProcessBusEvent(final ProcessBusEvent event)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                showEvent(event);
            }
        });
    }
}
