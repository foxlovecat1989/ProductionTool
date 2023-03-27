package com.ed.productiontool.event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RefreshEvent implements Events {
    private static final List<Subscriber> subscribers = new ArrayList<>();

    private RefreshEvent() {
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void cancel(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void stateChanged() {
        subscribers.forEach(Subscriber::stateChanged);
    }
}
