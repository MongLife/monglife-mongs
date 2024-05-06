package com.mongs.play.app.common.internal.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongCodeData {

    CH000("CH000", "화산알", 0, 0),
    CH001("CH001", "석탄알", 0, 0),
    CH002("CH002", "황금알", 0, 0),
    CH003("CH003", "목성알", 0, 0),
    CH004("CH004", "지구알", 0, 0),
    CH005("CH005", "바람알", 0, 0),
    CH100("CH100", "별몽", 1, 0),
    CH101("CH101", "동글몽", 1, 0),
    CH102("CH102", "네몽", 1, 0),
    CH200("CH200", "안씻은 별별몽", 2, 0),
    CH210("CH210", "무난한 별별몽", 2, 0),
    CH220("CH220", "유쾌한 별별몽", 2, 0),
    CH230("CH230", "완벽한 별별몽", 2, 0),
    CH201("CH201", "안씻은 둥글몽", 2, 0),
    CH211("CH211", "무난한 둥글몽", 2, 0),
    CH221("CH221", "유쾌한 둥글몽", 2, 0),
    CH231("CH231", "완벽한 둥글몽", 2, 0),
    CH202("CH202", "안씻은 나네몽", 2, 0),
    CH212("CH212", "무난한 나네몽", 2, 0),
    CH222("CH222", "유쾌한 나네몽", 2, 0),
    CH232("CH232", "완벽한 나네몽", 2, 0),
    CH203("CH203", "까몽", 2, 0),
    CH300("CH300", "병든 별뿔몽", 3, 0),
    CH310("CH310", "평범한 별뿔몽", 3, 0),
    CH320("CH320", "매력적인 별뿔몽", 3, 0),
    CH330("CH330", "엘리트 별뿔몽", 3, 0),
    CH301("CH301", "병든 땡글몽", 3, 0),
    CH311("CH311", "평범한 땡글몽", 3, 0),
    CH321("CH321", "매력적인 땡글몽", 3, 0),
    CH331("CH331", "엘리트 땡글몽", 3, 0),
    CH302("CH302", "병든 마미무메몽", 3, 0),
    CH312("CH312", "평범한 마미무메몽", 3, 0),
    CH322("CH322", "매력적인 마미무메몽", 3, 0),
    CH332("CH332", "엘리트 마미무메몽", 3, 0),
    CH303("CH303", "쌔까몽", 3, 0)
    ;

    private final String code;
    private final String name;
    private final Integer level;
    private final Integer evolutionPoint;
}
