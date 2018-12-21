package com.ald.asjauthlib.event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Yangyang on 2018/4/16.
 * desc:
 */

public class EventBusUtil {
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }

    public static void removeStickyEvent(Event event) {
        EventBus.getDefault().removeStickyEvent(event);
    }
}
