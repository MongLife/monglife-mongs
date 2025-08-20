package com.monglife.mongs.adapter.out.member.persistence.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GovStoreResponseDto {

    @JsonAlias("body")
    private Data data;

    public GovStoreResponseDto(Data data) {
        this.data = data;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {

        @JsonAlias("items")
        private List<Item> items;

        @JsonAlias("pageNo")
        private Integer page;

        @JsonAlias("numOfRows")
        private Integer size;

        @JsonAlias("totalCount")
        private Integer totalCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {

        @JsonAlias("bizesNm")
        private String name;

        @JsonAlias("brchNm")
        private String subName;

        @JsonAlias("lon")
        private Double longitude;

        @JsonAlias("lat")
        private Double latitude;
    }
}
