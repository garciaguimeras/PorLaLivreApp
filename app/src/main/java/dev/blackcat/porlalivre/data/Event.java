package dev.blackcat.porlalivre.data;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable
{

    public Date date;
    public String title;
    public int maxProgress;
    public int progress;
    public String[] messages;

}
