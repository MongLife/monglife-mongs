package com.mongs.management.domain.mong.service.enums;

import com.mongs.core.code.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongCollapse implements Code {

    STARVING("EV000", "공복"),
    NO_STAMINA("EV001", "체력0"),
    NORMAL("EV002", "일상생활");

    private final String groupCode = "EV";
    private final String code;
    private final String name;
}