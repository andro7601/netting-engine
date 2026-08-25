package com.engine.controller;

import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import com.engine.dto.ResponseDto;
import com.engine.service.OptimizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(OptimizationController.class)
@AutoConfigureRestTestClient
public class OptimizationControllerTest {
    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private OptimizationService optimizationService;


    @Test
    void badRequest() {
        List<CandidateTradeDto> candidates = List.of(
                new CandidateTradeDto("Now", new BigDecimal("5"), new BigDecimal("5")),
                new CandidateTradeDto("zebra", new BigDecimal("89"), new BigDecimal("7"))
        );
        RequestDto fakeRequest = new RequestDto(new BigDecimal("-54"), candidates);

        restTestClient.post().uri("/api/v1/trades/optimize")
                .body(fakeRequest)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void badCandidate() {
        List<CandidateTradeDto> candidates = List.of(
                new CandidateTradeDto("Now", new BigDecimal("5"), new BigDecimal("5")),
                new CandidateTradeDto("zebra", new BigDecimal("-5"), new BigDecimal("7"))  // negative marginRequired, should fail
        );
        RequestDto fakeRequest = new RequestDto(new BigDecimal("9"), candidates);

        restTestClient.post().uri("/api/v1/trades/optimize")
                .body(fakeRequest)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void validRequest() {

        when(optimizationService.optimize(any())).thenReturn(new ResponseDto(
                null,
                List.of(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                null));

        List<CandidateTradeDto> candidates = List.of(

        );
        RequestDto fakeRequest = new RequestDto(new BigDecimal("4"), List.of(
                new CandidateTradeDto("first", new BigDecimal("5"), new BigDecimal("40")),
                new CandidateTradeDto("second", new BigDecimal("20"), new BigDecimal("80"))));


        restTestClient.post().uri("/api/v1/trades/optimize")
                .body(fakeRequest)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
