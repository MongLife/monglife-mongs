package com.mongs.member.domain.member.dto.request;

import com.mongs.core.code.enums.member.SlotCountCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModifyMemberReqDto(
        @NotNull
        @NotBlank
        SlotCountCode slotCountCode
) {
}
