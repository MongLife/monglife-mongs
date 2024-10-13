package com.monglife.mongs.app.management.global.code.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EvolutionCode {

    CH000(null,    0,   "화산알"),
    CH001(null,    0,   "석탄알"),
    CH002(null,    0,   "황금알"),
    CH003(null,    0,   "목성알"),
    CH004(null,    0,   "지구알"),
    CH005(null,    0,   "바람알"),
    CH100("CH100", 0,   "별몽"),
    CH101("CH101", 0,   "동글몽"),
    CH102("CH102", 0,   "네몽"),
    CH200("CH100", 150, "안씻은 별별몽"),
    CH210("CH100", 200, "무난한 별별몽"),
    CH220("CH100", 250, "유쾌한 별별몽"),
    CH230("CH100", 300, "완벽한 별별몽"),
    CH201("CH101", 150, "안씻은 둥글몽"),
    CH211("CH101", 200, "무난한 둥글몽"),
    CH221("CH101", 250, "유쾌한 둥글몽"),
    CH231("CH101", 300, "완벽한 둥글몽"),
    CH202("CH102", 150, "안씻은 나네몽"),
    CH212("CH102", 200, "무난한 나네몽"),
    CH222("CH102", 250, "유쾌한 나네몽"),
    CH232("CH102", 300, "완벽한 나네몽"),
    CH203("CH203", 0,   "까몽"),
    CH300("CH100", 150, "병든 별뿔몽"),
    CH310("CH100", 200, "평범한 별뿔몽"),
    CH320("CH100", 250, "매력적인 별뿔몽"),
    CH330("CH100", 300, "엘리트 별뿔몽"),
    CH301("CH101", 150, "병든 땡글몽"),
    CH311("CH101", 200, "평범한 땡글몽"),
    CH321("CH101", 250, "매력적인 땡글몽"),
    CH331("CH101", 300, "엘리트 땡글몽"),
    CH302("CH102", 150, "병든 마미무메몽"),
    CH312("CH102", 200, "평범한 마미무메몽"),
    CH322("CH102", 250, "매력적인 마미무메몽"),
    CH332("CH102", 300, "엘리트 마미무메몽"),
    CH303("CH203", 0,   "쌔까몽")
    ;

    public final String baseCode;
    public final Integer requireEvolutionPoint;
    public final String name;
}
