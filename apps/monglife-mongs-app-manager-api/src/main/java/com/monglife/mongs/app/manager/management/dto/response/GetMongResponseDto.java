package com.monglife.mongs.app.manager.management.dto.response;

import com.monglife.mongs.app.manager.management.dto.etc.GetMongDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMongResponseDto {

    private Long mongId;

    private String mongName;

    private String mongCode;


    public static GetMongResponseDto of(GetMongDto getMongDto) {
        return GetMongResponseDto.builder()
                .mongId(getMongDto.getMongId())
                .mongName(getMongDto.getMongName())
                .mongCode(getMongDto.getMongCode())
                .build();
    }

    public static List<GetMongResponseDto> toList(List<GetMongDto> getMongDtos) {
        return getMongDtos.stream()
                .map(GetMongResponseDto::of)
                .toList();
    }
}
