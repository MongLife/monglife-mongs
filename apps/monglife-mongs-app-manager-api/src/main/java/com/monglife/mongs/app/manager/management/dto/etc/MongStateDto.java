package com.monglife.mongs.app.manager.management.dto.etc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monglife.mongs.domain.mong.enums.MongStateCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MongStateDto {

    private Long mongId;

    private MongStateCode stateCode;

    private Boolean isSleep;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "Asia/Seoul")
    private LocalDateTime updatedAt;

    @Builder
    public MongStateDto(Long mongId, MongStateCode stateCode, Boolean isSleep, LocalDateTime updatedAt) {
        this.mongId = mongId;
        this.stateCode = stateCode;
        this.isSleep = isSleep;
        this.updatedAt = updatedAt;
    }
}
