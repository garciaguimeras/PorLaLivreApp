package dev.blackcat.porlalivre.utils;

import android.content.Intent;

import dev.blackcat.porlalivre.data.Event;

public class EventHelper
{

    public static final String EVENT = "dev.blackcat.porlalivre.Event";

    public static Intent createEventIntent(String title)
    {
        Event event = new Event();
        event.title = title;

        Intent intent = new Intent();
        intent.putExtra(EVENT, event);
        return intent;
    }

    public static Intent createEventIntent(String title, int progress, int maxProgress)
    {
        Event event = new Event();
        event.title = title;
        event.progress = progress;
        event.maxProgress = maxProgress;

        Intent intent = new Intent();
        intent.putExtra(EVENT, event);
        return intent;
    }

    public static Intent createEventIntent(String title, String[] messages)
    {
        Event event = new Event();
        event.title = title;
        event.messages = messages;

        Intent intent = new Intent();
        intent.putExtra(EVENT, event);
        return intent;
    }

}
