package com.intuit.vkamble.biztrack.biztracksdk.client;

import com.intuit.vkamble.biztrack.biztracksdk.config.EndPoint;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

public class BizTrackServiceTest {

    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    private EndPoint endPoint = Mockito.mock(EndPoint.class);

    private BizTrackService bizTrackService;

    @Before
    public void init(){
        bizTrackService = new BizTrackService(restTemplate,endPoint);
    }

    @Test
    public void existsByIdTest(){

    }
}
