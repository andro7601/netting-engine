package com.engine.controller;

import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import com.engine.dto.ResponseDto;
import com.engine.service.OptimizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.shaded.org.bouncycastle.asn1.ocsp.Request;

import java.util.List;
import java.util.UUID;

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
                new CandidateTradeDto("Now", 5, 5),
                new CandidateTradeDto("zebra", 89, 7)
        );
        RequestDto fakeRequest = new RequestDto(-54, candidates);

        restTestClient.post().uri("/api/v1/trades/optimize")
                .body(fakeRequest)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void badCandidate() {
        List<CandidateTradeDto> candidates = List.of(
                new CandidateTradeDto("Now", 5, 5),
                new CandidateTradeDto("zebra", -5, 7)  // negative marginRequired, should fail
        );
        RequestDto fakeRequest = new RequestDto(9, candidates);

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
                null,
                0,
                0,
                null));

        List<CandidateTradeDto> candidates = List.of(

        );
        RequestDto fakeRequest = new RequestDto(9, List.of(
                new CandidateTradeDto("first", 5, 40),
                new CandidateTradeDto("second", 20, 80)));


        restTestClient.post().uri("/api/v1/trades/optimize")
                .body(fakeRequest)
                .exchange()
                .expectStatus()
                .isCreated();
    }
}
