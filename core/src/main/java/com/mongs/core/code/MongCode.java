package com.mongs.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongCode implements Code {

    CH000("CH000", "화산알"),
    CH001("CH001", "석탄알"),
    CH002("CH002", "황금알"),
    CH003("CH003", "목성알"),
    CH004("CH004", "지구알"),
    CH005("CH005", "바람알"),
    CH100("CH100", "별몽"),
    CH101("CH101", "동글몽"),
    CH102("CH102", "네몽"),
    CH200("CH200", "안씻은 별별몽"),
    CH210("CH210", "무난한 별별몽"),
    CH220("CH220", "유쾌한 별별몽"),
    CH230("CH230", "완벽한 별별몽"),
    CH201("CH201", "안씻은 둥글몽"),
    CH211("CH211", "무난한 둥글몽"),
    CH221("CH221", "유쾌한 둥글몽"),
    CH231("CH231", "완벽한 둥글몽"),
    CH202("CH202", "안씻은 나네몽"),
    CH212("CH212", "무난한 나네몽"),
    CH222("CH222", "유쾌한 나네몽"),
    CH232("CH232", "완벽한 나네몽"),
    CH203("CH203", "까몽"),
    CH300("CH300", "병든 별뿔몽"),
    CH310("CH310", "평범한 별뿔몽"),
    CH320("CH320", "매력적인 별뿔몽"),
    CH330("CH330", "엘리트 별뿔몽"),
    CH301("CH301", "병든 땡글몽"),
    CH311("CH311", "평범한 땡글몽"),
    CH321("CH321", "매력적인 땡글몽"),
    CH331("CH331", "엘리트 땡글몽"),
    CH302("CH302", "병든 마미무메몽"),
    CH312("CH312", "평범한 마미무메몽"),
    CH322("CH322", "매력적인 마미무메몽"),
    CH332("CH332", "엘리트 마미무메몽"),
    CH303("CH303", "쌔까몽"),
    CH444("CH444", "");

    private final String groupCode = "CH";
    private final String code;
    private final String name;
}
