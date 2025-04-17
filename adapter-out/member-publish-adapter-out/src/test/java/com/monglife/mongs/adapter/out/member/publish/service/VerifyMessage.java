package com.monglife.mongs.adapter.out.member.publish.service;

import java.util.concurrent.CountDownLatch;

public class VerifyMessage<T> {

    private String topic;

    private T payload;

    private CountDownLatch countDownLatch;

    public VerifyMessage(String topic, T payload, CountDownLatch countDownLatch) {
        this.topic = topic;
        this.payload = payload;
        this.countDownLatch = countDownLatch;
    }

    public String getTopic() {
        return topic;
    }

    public T getPayload() {
        return payload;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
