package com.engine.algorithm;

import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.bouncycastle.cert.ocsp.Req;
import org.testcontainers.shaded.org.bouncycastle.jcajce.provider.asymmetric.mldsa.MLDSAKeyFactorySpi;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlgoTest {

    @Test
    void baseTest() {
        RequestDto requestDto = new RequestDto(
                15,
                List.of(
                        new CandidateTradeDto("Trade Alpha", 5, 120),
                        new CandidateTradeDto("Trade Beta", 10, 200),
                        new CandidateTradeDto("Trade Gamma", 3, 80),
                        new CandidateTradeDto("Trade Delta", 8, 160)
                )
        );
        HashSet<CandidateTradeDto> result = Algo.algorithm(requestDto);

        HashSet<CandidateTradeDto> expected = new HashSet<>(List.of(
                new CandidateTradeDto("Trade Beta", 10, 200),
                new CandidateTradeDto("Trade Alpha", 5, 120)
                ));
        assertEquals(expected.size(), result.size());
        result.forEach(elem -> assertTrue(expected.contains(elem)));
    }

    @Test
    void greedyCaseTest() {
        RequestDto requestDto = new RequestDto(
                17,
                List.of(
                        new CandidateTradeDto("Small", 5, 250),
                        new CandidateTradeDto("Big", 15, 400)
                )
        );
        HashSet<CandidateTradeDto> result = Algo.algorithm(requestDto);

        HashSet<CandidateTradeDto> expected = new HashSet<>(List.of(
                new CandidateTradeDto("Big", 15, 400)
        ));

        assertEquals(expected.size(), result.size());
        result.forEach(elem -> assertTrue(expected.contains(elem)));
    }

    @Test
    void returnEmpty() {
        RequestDto requestDto = new RequestDto(8, List.of(
                new CandidateTradeDto("Trade something", 10, 150),
                new CandidateTradeDto("Trade another", 15, 400),
                new CandidateTradeDto("Trade andAnother", 9, 90)
        ));

        HashSet<CandidateTradeDto> result = Algo.algorithm(requestDto);

        assertEquals(0, result.size());
    }
}
