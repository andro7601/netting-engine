package com.engine.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CandidateTradeDto(

        @NotNull(message = "trade must be identifiable")
        String tradeName,

        @NotNull(message = "margin cant be null")
        @DecimalMin("0")
        BigDecimal marginRequired,

        @NotNull(message = "Pnl cant be null")
        @DecimalMin("0")
        BigDecimal expectedPnl
){}

