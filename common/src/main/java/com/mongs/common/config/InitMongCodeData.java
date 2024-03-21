package com.mongs.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InitMongCodeData {
    CH000("CH000", "화산알", "http://dev.paymong.com/resource/mong/CH000"),
    CH001("CH001", "석탄알", "http://dev.paymong.com/resource/mong/CH001"),
    CH002("CH002", "황금알", "http://dev.paymong.com/resource/mong/CH002"),
    CH003("CH003", "목성알", "http://dev.paymong.com/resource/mong/CH003"),
    CH004("CH004", "지구알", "http://dev.paymong.com/resource/mong/CH004"),
    CH005("CH005", "바람알", "http://dev.paymong.com/resource/mong/CH005"),
    CH100("CH100", "별몽", "http://dev.paymong.com/resource/mong/CH100"),
    CH101("CH101", "동글몽", "http://dev.paymong.com/resource/mong/CH101"),
    CH102("CH102", "네몽", "http://dev.paymong.com/resource/mong/CH102"),
    CH200("CH200", "안씻은 별별몽", "http://dev.paymong.com/resource/mong/CH200"),
    CH210("CH210", "무난한 별별몽", "http://dev.paymong.com/resource/mong/CH210"),
    CH220("CH220", "유쾌한 별별몽", "http://dev.paymong.com/resource/mong/CH220"),
    CH230("CH230", "완벽한 별별몽", "http://dev.paymong.com/resource/mong/CH230"),
    CH201("CH201", "안씻은 둥글몽", "http://dev.paymong.com/resource/mong/CH201"),
    CH211("CH211", "무난한 둥글몽", "http://dev.paymong.com/resource/mong/CH211"),
    CH221("CH221", "유쾌한 둥글몽", "http://dev.paymong.com/resource/mong/CH221"),
    CH231("CH231", "완벽한 둥글몽", "http://dev.paymong.com/resource/mong/CH231"),
    CH202("CH202", "안씻은 나네몽", "http://dev.paymong.com/resource/mong/CH202"),
    CH212("CH212", "무난한 나네몽", "http://dev.paymong.com/resource/mong/CH212"),
    CH222("CH222", "유쾌한 나네몽", "http://dev.paymong.com/resource/mong/CH222"),
    CH232("CH232", "완벽한 나네몽", "http://dev.paymong.com/resource/mong/CH232"),
    CH203("CH203", "까몽", "http://dev.paymong.com/resource/mong/CH203"),
    CH300("CH300", "병든 별뿔몽", "http://dev.paymong.com/resource/mong/CH300"),
    CH310("CH310", "평범한 별뿔몽", "http://dev.paymong.com/resource/mong/CH310"),
    CH320("CH320", "매력적인 별뿔몽", "http://dev.paymong.com/resource/mong/CH320"),
    CH330("CH330", "엘리트 별뿔몽", "http://dev.paymong.com/resource/mong/CH330"),
    CH301("CH301", "병든 땡글몽", "http://dev.paymong.com/resource/mong/CH301"),
    CH311("CH311", "평범한 땡글몽", "http://dev.paymong.com/resource/mong/CH311"),
    CH321("CH321", "매력적인 땡글몽", "http://dev.paymong.com/resource/mong/CH321"),
    CH331("CH331", "엘리트 땡글몽", "http://dev.paymong.com/resource/mong/CH331"),
    CH302("CH302", "병든 마미무메몽", "http://dev.paymong.com/resource/mong/CH302"),
    CH312("CH312", "평범한 마미무메몽", "http://dev.paymong.com/resource/mong/CH312"),
    CH322("CH322", "매력적인 마미무메몽", "http://dev.paymong.com/resource/mong/CH322"),
    CH332("CH332", "엘리트 마미무메몽", "http://dev.paymong.com/resource/mong/CH332"),
    CH303("CH303", "쌔까몽", "http://dev.paymong.com/resource/mong/CH303");
    private final String code;
    private final String name;
    private final String resourceUrl;
}
