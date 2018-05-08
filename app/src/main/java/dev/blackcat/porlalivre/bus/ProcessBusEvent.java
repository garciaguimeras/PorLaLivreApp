package dev.blackcat.porlalivre.bus;

import java.io.Serializable;

public class ProcessBusEvent implements Serializable
{

    public String title;
    public int max;
    public int progress;
    public String[] messages;

    public ProcessBusEvent(String title)
    {
        this.title = title;
        this.max = 0;
        this.progress = 0;
        this.messages = null;
    }

    public ProcessBusEvent(String title, int max, int progress)
    {
        this.title = title;
        this.max = max;
        this.progress = progress;
        this.messages = null;
    }

    public ProcessBusEvent(String title, String[] messages)
    {
        this.title = title;
        this.max = 0;
        this.progress = 0;
        this.messages = messages;
    }
}
