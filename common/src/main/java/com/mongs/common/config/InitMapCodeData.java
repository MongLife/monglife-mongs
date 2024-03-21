package com.mongs.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InitMapCodeData {

    MP000("MP000", "기본", "http://localhost:8002/resource/map/MP000"),
    MP001("MP001", "스타벅스", "http://localhost:8002/resource/map/MP001"),
    MP002("MP002", "이디야", "http://localhost:8002/resource/map/MP002"),
    MP003("MP003", "할리스", "http://localhost:8002/resource/map/MP003"),
    MP004("MP004", "투썸플레이스", "http://localhost:8002/resource/map/MP004"),
    MP005("MP005", "메가커피", "http://localhost:8002/resource/map/MP005"),
    MP006("MP006", "파스쿠찌", "http://localhost:8002/resource/map/MP006"),
    MP007("MP007", "블루보틀", "http://localhost:8002/resource/map/MP007"),
    MP008("MP008", "하이오커피", "http://localhost:8002/resource/map/MP008"),
    MP009("MP009", "블루샥", "http://localhost:8002/resource/map/MP009"),
    MP010("MP010", "컴포즈커피", "http://localhost:8002/resource/map/MP010"),
    MP011("MP011", "맥도날드", "http://localhost:8002/resource/map/MP011"),
    MP012("MP012", "버거킹", "http://localhost:8002/resource/map/MP012"),
    MP013("MP013", "롯데리아", "http://localhost:8002/resource/map/MP013"),
    MP014("MP014", "맘스터치", "http://localhost:8002/resource/map/MP014"),
    MP015("MP015", "CGV", "http://localhost:8002/resource/map/MP015"),
    MP016("MP016", "메가박스", "http://localhost:8002/resource/map/MP016"),
    MP017("MP017", "이마트24", "http://localhost:8002/resource/map/MP017"),
    MP018("MP018", "GS25", "http://localhost:8002/resource/map/MP018"),
    MP019("MP019", "CU", "http://localhost:8002/resource/map/MP019"),
    MP020("MP020", "올리브영", "http://localhost:8002/resource/map/MP020"),
    MP021("MP021", "나이키", "http://localhost:8002/resource/map/MP021"),
    MP022("MP022", "아디다스", "http://localhost:8002/resource/map/MP022"),
    MP023("MP023", "다이소", "http://localhost:8002/resource/map/MP023"),
    MP024("MP024", "PC방", "http://localhost:8002/resource/map/MP024"),
    MP025("MP025", "노래방", "http://localhost:8002/resource/map/MP025"),
    MP026("MP026", "던킨도너츠", "http://localhost:8002/resource/map/MP026"),
    MP027("MP027", "파리바게트", "http://localhost:8002/resource/map/MP027"),
    MP028("MP028", "뚜레쥬르", "http://localhost:8002/resource/map/MP028"),
    MP029("MP029", "베스킨라빈스31", "http://localhost:8002/resource/map/MP029"),
    MP030("MP030", "설빙", "http://localhost:8002/resource/map/MP030"),
    MP031("MP031", "KFC", "http://localhost:8002/resource/map/MP031"),
    MP032("MP032", "롯데시네마", "http://localhost:8002/resource/map/MP032"),
    MP033("MP033", "쿠팡", "http://localhost:8002/resource/map/MP033"),
    MP034("MP034", "요기요", "http://localhost:8002/resource/map/MP034"),
    MP035("MP035", "배달의민족", "http://localhost:8002/resource/map/MP035"),
    MP036("MP036", "카페", "http://localhost:8002/resource/map/MP036"),
    MP037("MP037", "세븐일레븐", "http://localhost:8002/resource/map/MP037"),
    MP038("MP038", "싸피자판기", "http://localhost:8002/resource/map/MP038"),
    MP039("MP039", "무신사", "http://localhost:8002/resource/map/MP039"),
    MP040("MP040", "병원", "http://localhost:8002/resource/map/MP040"),
    MP041("MP041", "더벤티", "http://localhost:8002/resource/map/MP041"),
    MP042("MP042", "아난티코브", "http://localhost:8002/resource/map/MP042"),
    MP043("MP043", "노브랜드", "http://localhost:8002/resource/map/MP043");

    private final String code;
    private final String name;
    private final String resourceUrl;
}
