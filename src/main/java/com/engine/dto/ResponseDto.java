package com.engine.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ResponseDto(
        UUID requestId,
        List<CandidateTradeDto> selectedTrades,
        BigDecimal totalMarginRequired,
        BigDecimal totalExpectedPnl,
        Instant createdAt
) {
}
