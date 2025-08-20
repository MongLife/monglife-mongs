package com.monglife.mongs.adapter.out.member.persistence.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "gov.store")
public class GovTemplateProperty {

    private String url;

    private String serviceKey;
}
