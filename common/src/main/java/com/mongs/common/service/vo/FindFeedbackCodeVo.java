package com.mongs.common.service.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

@JsonPropertyOrder({"code", "groupCode", "message", "buildVersion"})
@Builder
public record FindFeedbackCodeVo() {
}
