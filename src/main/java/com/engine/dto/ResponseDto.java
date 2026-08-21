package com.engine.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ResponseDto(
        UUID requestId,
        List<CandidateTradeDto> selectedTrades,
        int totalMarginRequired,
        int totalExpectedPnl,
        Instant createdAt
) {
}
