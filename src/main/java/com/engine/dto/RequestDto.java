package com.engine.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record RequestDto(

        @PositiveOrZero
        int maxMargin,

        @NotNull
        @Valid
        List<CandidateTradeDto> candidateTrades
) {
}
