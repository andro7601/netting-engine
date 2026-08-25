package com.engine.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CandidateTradeDto(
        @NotNull(message = "trade must be identifiable")
        String tradeName,

        @PositiveOrZero(message = "margin cant be negative")
        BigDecimal marginRequired,

        @PositiveOrZero(message = "Pnl cant be negative")
        BigDecimal expectedPnl
){}

