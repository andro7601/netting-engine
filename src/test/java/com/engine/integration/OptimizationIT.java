package com.engine.integration;

import com.engine.TestcontainersConfiguration;
import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import com.engine.dto.ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.util.List;

@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class OptimizationIT {

    @Autowired
    private RestTestClient restTestClient;


    @Test
    void returnsCorrect() {
        RequestDto requestDto = new RequestDto(new BigDecimal("15"), List.of(
                new CandidateTradeDto("Trade Alpha", new BigDecimal("5"), new BigDecimal("120")),
                new CandidateTradeDto("Trade Beta", new BigDecimal("10"), new BigDecimal("200")),
                new CandidateTradeDto("Trade Gamma", new BigDecimal("3"), new BigDecimal("80")),
                new CandidateTradeDto("Trade Delta", new BigDecimal("8"), new BigDecimal("160"))
        ));

        ResponseDto response = restTestClient.post().uri("/api/v1/trades/optimize")
                .body(requestDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResponseDto.class)
                .returnResult().getResponseBody();



        restTestClient.get().uri("/api/v1/trades/" + response.requestId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseDto.class)
                .isEqualTo(response);


        restTestClient.get().uri("/api/v1/trades?page=1&size=1")
                .exchange()
                .expectStatus().isOk();
    }
}