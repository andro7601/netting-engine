package com.engine.dto;

public record CandidateTradeDto(
        String tradeName,
        int marginRequired,
        int expectedPnl
){}

