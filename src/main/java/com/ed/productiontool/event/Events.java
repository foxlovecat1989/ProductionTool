package com.ed.productiontool.event;

public interface Events {
    void subscribe(Subscriber subscriber);
    void cancel(Subscriber subscriber);
    void stateChanged();
}
