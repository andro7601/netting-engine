package com.engine.algorithm;

import com.engine.dto.CandidateTradeDto;
import com.engine.dto.RequestDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlgoTest {

    @Test
    void baseTest() {
        RequestDto requestDto = new RequestDto(
                new BigDecimal("15"),
                List.of(
                        new CandidateTradeDto("Trade Alpha", new BigDecimal("5"), new BigDecimal("120")),
                        new CandidateTradeDto("Trade Beta", new BigDecimal("10"), new BigDecimal("200")),
                        new CandidateTradeDto("Trade Gamma", new BigDecimal("3"), new BigDecimal("80")),
                        new CandidateTradeDto("Trade Delta", new BigDecimal("8"), new BigDecimal("160"))
                )
        );
        HashSet<CandidateTradeDto> result = Algo.algorithm(requestDto);

        HashSet<CandidateTradeDto> expected = new HashSet<>(List.of(
                new CandidateTradeDto("Trade Beta", new BigDecimal("10"), new BigDecimal("200")),
                new CandidateTradeDto("Trade Alpha", new BigDecimal("5"), new BigDecimal("120"))
                ));
        assertEquals(expected.size(), result.size());
        result.forEach(elem -> assertTrue(expected.contains(elem)));
    }

    @Test
    void greedyCaseTest() {
        RequestDto requestDto = new RequestDto(
                new BigDecimal("17"),
                List.of(
                        new CandidateTradeDto("Small", new BigDecimal("5"), new BigDecimal("250")),
                        new CandidateTradeDto("Big", new BigDecimal("15"), new BigDecimal("400"))
                )
        );
        HashSet<CandidateTradeDto> result = Algo.algorithm(requestDto);

        HashSet<CandidateTradeDto> expected = new HashSet<>(List.of(
                new CandidateTradeDto("Big", new BigDecimal("15"), new BigDecimal("400"))
        ));

        assertEquals(expected.size(), result.size());
        result.forEach(elem -> assertTrue(expected.contains(elem)));
    }

    @Test
    void returnEmpty() {
        RequestDto requestDto = new RequestDto(new BigDecimal("8"), List.of(
                new CandidateTradeDto("Trade something", new BigDecimal("10"), new BigDecimal("150")),
                new CandidateTradeDto("Trade another", new BigDecimal("15"), new BigDecimal("400")),
                new CandidateTradeDto("Trade andAnother", new BigDecimal("9"), new BigDecimal("90"))
        ));

        HashSet<CandidateTradeDto> result = Algo.algorithm(requestDto);

        assertEquals(0, result.size());
    }
}
