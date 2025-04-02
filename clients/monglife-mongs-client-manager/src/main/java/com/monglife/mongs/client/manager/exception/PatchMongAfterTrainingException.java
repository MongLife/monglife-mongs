package com.monglife.mongs.client.manager.exception;

import com.monglife.core.exception.ErrorException;
import com.monglife.mongs.client.manager.enums.ManagerResponse;
import lombok.Getter;

import java.util.Collections;

@Getter
public class PatchMongAfterTrainingException extends ErrorException {

    public PatchMongAfterTrainingException(Long mongId) {
        this.response = ManagerResponse.CLIENT_MANAGER_PATCH_MONG_AFTER_TRAINING_FAIL;
        this.result = Collections.singletonMap("mongId", mongId);
    }
}
