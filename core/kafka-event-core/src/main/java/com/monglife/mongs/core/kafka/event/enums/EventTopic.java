package com.monglife.mongs.core.kafka.event.enums;

public class EventTopic {

    public static final String NOTIFICATION_MONGS                      = "notification.mongs";

    public static final String COMMIT_EXCHANGE_CURRENT_WALKING_COUNT   = "commit.exchange-current-walking-count";       //      user -> character (with rollback)
    public static final String COMMIT_EXCHANGE_STAR_POINT              = "commit.exchange-star-point";                  //      user -> character (with rollback)
    public static final String COMMIT_EGG_EVOLUTION                    = "commit.egg-evolution-mong";                   // character -> character (without rollback)
    public static final String COMMIT_INCREASE_STATUS                  = "commit.increase-status";                      // character -> character (without rollback)
    public static final String COMMIT_DECREASE_STATUS                  = "commit.decrease-status";                      // character -> character (without rollback)
    public static final String COMMIT_INCREASE_POOP                    = "commit.increase-poop";                        // character -> character (without rollback)
    public static final String COMMIT_DEAD                             = "commit.dead";                                 // character -> character (without rollback)
    public static final String COMMIT_SLEEP                            = "commit.sleep";                                // character -> character (without rollback)
    public static final String COMMIT_WAKEUP                           = "commit.wakeup";                               // character -> character (without rollback)
    public static final String COMMIT_CREATE_MONG                      = "commit.create-mong";                          // character -> user      (without rollback)
    public static final String COMMIT_EVOLUTION_MONG                   = "commit.evolution-mong";                       // character -> user      (without rollback)
    public static final String COMMIT_RANDOM_DRAW_MAP                  = "commit.random-draw-map";                      // character -> user      (without rollback)

    public static final String ROLLBACK_EXCHANGE_CURRENT_WALKING_COUNT = "rollback.exchange-current-walking-count";     // character -> user
    public static final String ROLLBACK_EXCHANGE_STAR_POINT            = "rollback.exchange-star-point";                // character -> user
}
