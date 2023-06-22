package com.tamayo.gcash.exam.controller;

import com.tamayo.gcash.exam.service.DeliveryCostService;
import feign.codec.ErrorDecoder;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = DeliverCostControllerTest.class)
@ComponentScan(basePackages = "com.tamayo.gcash.exam.*")
@EnableFeignClients
@ImportAutoConfiguration({FeignAutoConfiguration.class})
class DeliverCostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryCostService deliveryCostService;

    @MockBean
    private ErrorDecoder errorDecoder;

    @Test
    void returns_200() throws Exception {
        URIBuilder uriBuilder = new URIBuilder("/api/delivery/cost");
        uriBuilder.addParameter("weight", "50");
        uriBuilder.addParameter("height", "1");
        uriBuilder.addParameter("width", "1");
        uriBuilder.addParameter("length", "1");
        String url = uriBuilder.build().toString();
        System.out.println("Url: " + url);
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void returns_400() throws Exception {
        URIBuilder uriBuilder = new URIBuilder("/api/delivery/cost");
        uriBuilder.addParameter("weight", null);
        uriBuilder.addParameter("height", "1");
        uriBuilder.addParameter("width", "1");
        uriBuilder.addParameter("length", "1");
        String url = uriBuilder.build().toString();
        System.out.println("Url: " + url);
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isBadRequest()).andReturn();
    }
}