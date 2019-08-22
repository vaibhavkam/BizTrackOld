package com.intuit.vkamble.biztrack.biztracksdk.config;

import com.intuit.vkamble.biztrack.biztracksdk.client.BizTrackService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnClass(BizTrackService.class)
@EnableConfigurationProperties(EndPoint.class)
public class SDKConfig {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }


    @Bean
    @ConditionalOnMissingBean
    public BizTrackService bizTrackService(RestTemplate restTemplate, EndPoint endPoint) {
        return new BizTrackService(restTemplate, endPoint);
    }
}
