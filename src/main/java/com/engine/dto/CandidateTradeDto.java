package com.engine.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CandidateTradeDto(
        @NotNull(message = "trade must be identifiable")
        String tradeName,

        @PositiveOrZero(message = "margin cant be negative")
        int marginRequired,

        @PositiveOrZero(message = "Pnl cant be negative")
        int expectedPnl
){}

