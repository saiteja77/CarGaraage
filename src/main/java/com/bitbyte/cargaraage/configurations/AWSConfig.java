package com.bitbyte.cargaraage.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class AWSConfig {
    private String accessKey;
    private String secretKey;
    private String s3Url;
    private String s3BucketName;
    private String s3FolderName;
}