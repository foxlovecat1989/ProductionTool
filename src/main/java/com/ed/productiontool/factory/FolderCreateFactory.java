package com.ed.productiontool.factory;

import com.ed.productiontool.app_config.Config;
import com.ed.productiontool.event.RefreshEvent;
import com.ed.productiontool.event.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class FolderCreateFactory implements Subscriber {
    private final RefreshEvent refreshEvent;
    private static String sequenceNo = getNewSequenceNo();

    @Autowired
    private FolderCreateFactory(RefreshEvent refreshEvent) {
        this.refreshEvent = refreshEvent;
        subscribe();
    }

    private static String getNewSequenceNo() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private static void refreshSequenceNo() {
        sequenceNo = getNewSequenceNo();
    }

    public static String getFolderName() {
        return String.format(
                "%s_%s_prod_v%s",
                new SimpleDateFormat("yyyyMMdd").format(new Date()),
                Config.PROJECT_NAME,
                sequenceNo);
    }

    @Override
    public void subscribe() {
        refreshEvent.subscribe(this);
    }

    @Override
    public void cancelSubscribe() {
        refreshEvent.cancel(this);
    }

    @Override
    public void stateChanged() {
        refreshSequenceNo();
    }
}
