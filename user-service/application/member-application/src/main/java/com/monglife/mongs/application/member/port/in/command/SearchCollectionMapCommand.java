package com.monglife.mongs.application.member.port.in.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchCollectionMapCommand {

    private final Long accountId;

    private final Double latitude;

    private final Double longitude;

    @Builder
    public SearchCollectionMapCommand(Long accountId, Double latitude, Double longitude) {
        this.accountId = accountId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
