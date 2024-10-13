package com.monglife.mongs.app.management.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterMongReqDto {

    private String name;

    private String sleepStart;

    private String sleepEnd;
}
