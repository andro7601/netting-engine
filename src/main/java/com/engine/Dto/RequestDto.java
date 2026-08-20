package com.engine.Dto;

import java.util.List;

public record RequestDto(
        int maxMargin,
        List<CandidateTradeDto> candidateTrades
) {
}
