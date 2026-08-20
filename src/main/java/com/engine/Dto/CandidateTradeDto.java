package com.engine.Dto;

public record CandidateTradeDto(
        String tradeName,
        int marginRequired,
        int expectedPnl
){}

