package com.engine.dto;

import java.util.List;

public record RequestDto(
        int maxMargin,
        List<CandidateTradeDto> candidateTrades
) {
}
