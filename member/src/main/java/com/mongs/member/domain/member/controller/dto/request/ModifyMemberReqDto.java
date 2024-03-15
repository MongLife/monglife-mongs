package com.mongs.member.domain.member.controller.dto.request;

import com.mongs.core.enums.member.SlotCountCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModifyMemberReqDto(
        @NotNull
        @NotBlank
        SlotCountCode slotCountCode
) {
}
