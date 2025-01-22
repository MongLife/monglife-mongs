package com.monglife.mongs.module.mqtt.utils;

public class TopicUtil {

    public static String preProcessTopic(String topic) {

        String nextTopic = topic;

        while (nextTopic.startsWith("/")) {
            nextTopic = nextTopic.substring(1);
        }

        while (nextTopic.endsWith("/")) {
            nextTopic = nextTopic.substring(0, nextTopic.length() - 1);
        }

        return nextTopic;
    }
}
