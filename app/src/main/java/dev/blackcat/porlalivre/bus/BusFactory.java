package dev.blackcat.porlalivre.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusFactory
{

    private static Bus processBus = null;

    public static Bus getProcessBus()
    {
        if (processBus == null)
            processBus = new Bus(ThreadEnforcer.ANY);
        return processBus;
    }

}
