package com.engine.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record RequestDto(

        @PositiveOrZero
        BigDecimal maxMargin,

        @NotNull
        @Valid
        List<CandidateTradeDto> candidateTrades
) {
}
