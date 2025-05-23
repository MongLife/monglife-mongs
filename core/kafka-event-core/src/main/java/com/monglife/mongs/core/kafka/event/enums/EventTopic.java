package com.monglife.mongs.core.kafka.event.enums;

public class EventTopic {

    public static final String NOTIFICATION_MONGS                      = "notification.mongs";

    public static final String COMMIT_EXCHANGE_CURRENT_WALKING_COUNT   = "commit.exchange-current-walking-count";
    public static final String COMMIT_EXCHANGE_STAR_POINT              = "commit.exchange-star-point";
    public static final String COMMIT_CREATE_COLLECTION_MONG           = "commit.create-collection-mong";
    public static final String COMMIT_EGG_EVOLUTION                    = "commit.egg-evolution-mong";
    public static final String COMMIT_INCREASE_STATUS                  = "commit.increase-status";
    public static final String COMMIT_DECREASE_STATUS                  = "commit.decrease-status";
    public static final String COMMIT_INCREASE_POOP                    = "commit.increase-poop";
    public static final String COMMIT_DEAD                             = "commit.dead";
    public static final String COMMIT_SLEEP                            = "commit.sleep";
    public static final String COMMIT_WAKEUP                           = "commit.wakeup";
    public static final String COMMIT_CREATE_MONG                      = "commit.create-mong";
    public static final String COMMIT_EVOLUTION_MONG                   = "commit.evolution-mong";
    public static final String COMMIT_RANDOM_DRAW_MAP                  = "commit.random-draw-map";

    public static final String ROLLBACK_EXCHANGE_CURRENT_WALKING_COUNT = "rollback.exchange-current-walking-count";
    public static final String ROLLBACK_EXCHANGE_STAR_POINT            = "rollback.exchange-star-point";
}
