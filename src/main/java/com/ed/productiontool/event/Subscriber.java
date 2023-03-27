package com.ed.productiontool.event;

public interface Subscriber {
    void subscribe();
    void cancelSubscribe();
    void stateChanged();
}
