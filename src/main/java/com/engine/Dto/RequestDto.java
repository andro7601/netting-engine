package com.engine.Dto;

import java.util.List;

public record RequestDto(
        long maxMargin,
        List<CandidateTradeDto> candidateTrades
) {
}
