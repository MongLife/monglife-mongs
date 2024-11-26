package com.monglife.mongs.app.manager.management.domain;

import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MongStatusEntityListener {

    @PostUpdate
    public void postUpdate(MongStatusEntity mongStatusEntity) {


    }
}
