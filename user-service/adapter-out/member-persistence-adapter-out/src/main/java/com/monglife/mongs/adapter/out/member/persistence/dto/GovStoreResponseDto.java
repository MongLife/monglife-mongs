package com.monglife.mongs.adapter.out.member.persistence.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovStoreResponseDto {

    @JsonAlias("body")
    private Data data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonIgnoreProperties(ignoreUnknown = true)
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
